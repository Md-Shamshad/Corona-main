package com.hp.psg.corona.datachange.session.impl;

import java.rmi.RemoteException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import javax.ejb.EJBException;
import javax.ejb.SessionBean;
import javax.ejb.SessionContext;
import javax.ejb.TimedObject;
import javax.ejb.Timer;
import javax.ejb.TimerHandle;
import javax.ejb.TimerService;
import javax.naming.Context;
import javax.naming.InitialContext;

import weblogic.management.MBeanHome;
import weblogic.management.runtime.ServerRuntimeMBean;

import com.hp.psg.common.util.logging.LoggerInfo;
import com.hp.psg.corona.common.beans.DataChangeEvent;
import com.hp.psg.corona.common.constants.CoronaFwkConstants;
import com.hp.psg.corona.common.util.Config;
import com.hp.psg.corona.common.util.CoronaFwkUtil;
import com.hp.psg.corona.common.util.DynamicConfig;
import com.hp.psg.corona.common.util.JndiUtils;
import com.hp.psg.corona.common.util.Logger;
import com.hp.psg.corona.common.util.SendMessageToJmsQueue;
import com.hp.psg.corona.datachange.dao.DataChangeEventsFwkDao;
import com.hp.psg.corona.error.util.CoronaErrorHandler;

/*
 * @author dudeja
 * @version 1.0
 *
 */
public class DataChangeQueuePollerBean implements SessionBean, TimedObject {
	private SessionContext _txnPollerContext;
	MBeanHome home = null;
	Context context = null;
	Set mbeanSet = null;
	String pollerType = null;
	LoggerInfo logInfo = null;
	private static final int UID_SIZE = 30;
	private static final String PICK_READY_EVENT_TO_BE_QUEUED="PICK_READY_EVENT_TO_BE_QUEUED";


	private TimerHandle timerHandle = null;
	public void ejbCreate() {
		logInfo = new LoggerInfo (this.getClass().getName());
	}

	public void setSessionContext(SessionContext context) throws EJBException {
		_txnPollerContext = context;
	}

	public void ejbRemove() throws EJBException {
	}

	public void ejbActivate() throws EJBException {
	}

	public void ejbPassivate() throws EJBException {
	}

	/**
	 * @param timer
	 */
	public void ejbTimeout(Timer timer) {
		try {

			Timestamp timeStamp= new Timestamp(System.currentTimeMillis());

			Logger.info(logInfo, "ejbTimeout","@@@@@@@@@ Timeout for DataChangeQueueTopicDepositor (DataChangeQueuePollerBean) at "+ timeStamp);

			if (!DynamicConfig.doPutDataChangeEventsProcessingOnHold()) {
				String serverRunningTimer = CoronaFwkUtil.executionLockedBy("DATA_CHANGE_QUEUE_POLLER");
				if (("".equals(serverRunningTimer))) {
					CoronaFwkUtil.lockTimerByName("DATA_CHANGE_QUEUE_POLLER");
					sendMessagesToTxnJMSQueue();
					CoronaFwkUtil.releaseLockForTimerByName("DATA_CHANGE_QUEUE_POLLER");
				}
			}
			Logger.info(logInfo, "ejbTimeout","@@@@@@@@@ Timeout return for DataChangeQueueTopicDepositor (DataChangeQueuePollerBean) for "+ timeStamp);
		} catch (Exception e) {
			CoronaErrorHandler.logError(e, null, null);
		}
	}

	public void sendMessagesToTxnJMSQueue() throws Exception {

		boolean hasMoreGroupedEventsInQueue = true;

		try {
			SendMessageToJmsQueue objSender = new SendMessageToJmsQueue();
			//Check if queue if available for loading.
			Logger.info(logInfo, "sendMessagesToTxnJMSQueue", "Sending DATACHANGE Message to JMS queue");

			int readyToQueuedBatchCount=DynamicConfig.getReadyToQueuedBatchUpdateCountForDataChange();
			int maxThreasholdQueuedCount = DynamicConfig.getMaxDataChangeQueueThreshholdValue();

			while(hasMoreGroupedEventsInQueue){
				
				DataChangeEventsFwkDao.verifyDataChangeRedundancyState();
				
				int numCurrentQueuedCount=DataChangeEventsFwkDao.getDataChangeEventQueuedCount();
				List<DataChangeEvent> dceListToQueue = pickReadyEventForProcessing(readyToQueuedBatchCount);

				Logger.info(logInfo, "processEvents", "events size for processing (datachange events) : "
						+ dceListToQueue.size());

				//Added a new block to continue processing till there are no more events in queue to be processed.
				if (dceListToQueue != null && dceListToQueue.size() > 0) {
					if (numCurrentQueuedCount > maxThreasholdQueuedCount ) {
						hasMoreGroupedEventsInQueue = false;
					}
					Logger.info(logInfo, "sendMessagesToTxnJMSQueue", "putting more messages to queue");

					if (dceListToQueue != null && dceListToQueue.size()> 0) {
						for (DataChangeEvent dg :dceListToQueue ){
							objSender.sendDataChangeMessage("TestTxnQueueMessage",dg.getPriority(), dg.getId());
						}
					}
				}else {
					hasMoreGroupedEventsInQueue = false;
					Logger.debug(logInfo, "processEvents", "No more GROUPS to be picked from datachange queue, kicking self out of loop.");
				}
			}
			objSender.destroyJMS();

		}catch (Throwable t) {
			CoronaErrorHandler.logError(t, "Exception while sending messae to txn jms queue", null);
		}
	}


	protected List<DataChangeEvent> pickReadyEventForProcessing(int readyToQueuedBatchCount) throws Exception {

		Logger.info(logInfo, "pickReadyEventForProcessing", "picked the messages for processing to be put to queue");
		String lastModifiedBy = CoronaFwkUtil.getUniqueId(
				CoronaFwkUtil.DATA_CHANGE_EVENT, UID_SIZE);
		DataChangeEventsFwkDao.pickGroupForProcessing(
				PICK_READY_EVENT_TO_BE_QUEUED, readyToQueuedBatchCount, lastModifiedBy,
				CoronaFwkConstants.ProcessingStatus.QUEUED,
				CoronaFwkConstants.ProcessingStatus.READY);

		List <DataChangeEvent> dceList =Collections.EMPTY_LIST; 

		dceList= DataChangeEventsFwkDao.retriveEventOnStatusAndIdFromDb(lastModifiedBy, CoronaFwkConstants.ProcessingStatus.QUEUED);

		return dceList;
	}

	protected List<DataChangeEvent> retrievePickedEvent(String lastModifiedBy)
	throws Exception {

		List<DataChangeEvent> events = new ArrayList<DataChangeEvent>();
		events = DataChangeEventsFwkDao
		.retriveThisPickedEventFromDb(lastModifiedBy);
		return events;
	}


	public void invokeRemote() throws EJBException, RemoteException {
		Long startTimeInit = null;
		try {
			TimerService ts = _txnPollerContext.getTimerService();
			Collection activeTimers = ts.getTimers();

			String manageServerGrp1Serv1 = (String) Config
			.getcoronaManageServerGrp1Serv1();
			String manageServerGrp1Serv2 = (String) Config
			.getcoronaManageServerGrp1Serv2();
			String manageServerGrp2Serv1 = (String) Config
			.getcoronaManageServerGrp2Serv1();
			String manageServerGrp2Serv2 = (String) Config
			.getcoronaManageServerGrp2Serv2();

			InitialContext context = (InitialContext) JndiUtils
			.getInitialContext();
			pollerType = (String) context.lookup("java:comp/env/PollerType");

			home = (MBeanHome) context.lookup(MBeanHome.LOCAL_JNDI_NAME);
			mbeanSet = home.getMBeansByType("ServerRuntime");
			Iterator mbeanIterator = mbeanSet.iterator();
			Timer timer = null;

			ServerRuntimeMBean serverBean = (ServerRuntimeMBean) mbeanIterator
			.next();
			String serverName = serverBean.getName();

			TimerService tservice = _txnPollerContext.getTimerService();
			activeTimers = tservice.getTimers();

			if (activeTimers.isEmpty()) {
				if (manageServerGrp1Serv1.equalsIgnoreCase(serverName)
						|| manageServerGrp2Serv1.equalsIgnoreCase(serverName))
					startTimeInit = Config.getTxnQueueEventsPollingInterval();
				else if (manageServerGrp1Serv2.equalsIgnoreCase(serverName)
						|| manageServerGrp2Serv2.equalsIgnoreCase(serverName))
					startTimeInit = (Config.getTxnQueueEventsPollingInterval() + (Config
							.getTxnQueueEventsPollingInterval() / 2));

				if (startTimeInit != null)
					timer = tservice.createTimer(startTimeInit * 1000, Config
							.getTxnQueueEventsPollingInterval() * 1000,
							CoronaFwkConstants.TxnEventsTimer);
				else
					timer = tservice.createTimer(Config
							.getTxnQueueEventsPollingInterval() * 1000, Config
							.getTxnQueueEventsPollingInterval() * 1000,
							CoronaFwkConstants.TxnEventsTimer);

				Logger.info(logInfo, "invokeRemote","Servers initialized");
			} else {
				Logger.info(logInfo, "invokeRemote","Number of Timers was already active with this bean are "
						+ activeTimers.size());
				Iterator timerItr = activeTimers.iterator();
				Logger.info(logInfo, "invokeRemote","ActiveTimer : ");
				while (timerItr.hasNext())
					Logger.info(logInfo, "invokeRemote",timerItr.next()
							.toString());
			}
		} catch (Exception e) {
			CoronaErrorHandler.logError(e, null, null);
		}

	}

	/**
	 * @return the timerHandle
	 */
	public TimerHandle getTimerHandle() {
		return timerHandle;
	}

	/**
	 * @param timerHandle
	 *            the timerHandle to set
	 */
	public void setTimerHandle(TimerHandle timerHandle) {
		this.timerHandle = timerHandle;
	}
}
