package com.hp.psg.corona.propagation.session.impl;

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
import com.hp.psg.corona.propagation.dao.PropagationEventsFwkDao;


/*
 * @author dudeja
 * @version 1.0
 *
 */
public class PropagationQueuePollerBean implements SessionBean, TimedObject {
	private SessionContext _propPollerContext;
	MBeanHome home = null;
	Context context = null;
	Set mbeanSet = null;
	LoggerInfo logInfo=null;
	private static final int UID_SIZE = 30;
	private static final String PICK_GROUPED_EVENT_TO_BE_QUEUED="PICK_GROUPED_EVENT_TO_BE_QUEUED";

	private TimerHandle timerHandle = null;
	public void ejbCreate() {
		logInfo = new LoggerInfo (this.getClass().getName());
	}

	public void setSessionContext(SessionContext context) throws EJBException {
		_propPollerContext = context;
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

			Logger.info(logInfo, "ejbTimeout", "@@@@@@@@@ Timeout for queue depositor to propagation Queue Reader bean (PropagationQueuePollerBean) at "+timeStamp);
			if (!DynamicConfig.doPutPropagationEventsProcessingOnHold()) {
				String serverRunningTimer = CoronaFwkUtil.executionLockedBy("PROPAGATION_QUEUE_POLLER");
				if (("".equals(serverRunningTimer))) {
					CoronaFwkUtil.lockTimerByName("PROPAGATION_QUEUE_POLLER");
					sendMessagesToPropJMSQueue();
					CoronaFwkUtil.releaseLockForTimerByName("PROPAGATION_QUEUE_POLLER");
				}
			}
			Logger.info(logInfo, "ejbTimeout", "@@@@@@@@@ Timeout return  for queue depositor to propagation Queue Reader bean (PropagationQueuePollerBean) for "+timeStamp);

		} catch (Exception e) {
			CoronaErrorHandler.logError(e, null, null);
		}
	}

	public void sendMessagesToPropJMSQueue() throws Exception {
		boolean hasMoreGroupedEventsInQueue = true;
		try {

			//Check if queue if available for loading.
			Logger.info(logInfo, "sendMessagesToPropJMSQueue", "Sending PROPAGATION Message to JMS queue");

			int groupedToQueuedBatchCount=DynamicConfig.getReadyToQueuedBatchUpdateCountForPropagation();
			int maxThreasholdQueuedCount = DynamicConfig.getMaxDataChangeQueueThreshholdValue();
			SendMessageToJmsQueue objSender = new SendMessageToJmsQueue();

			while(hasMoreGroupedEventsInQueue){

				PropagationEventsFwkDao.verifyPropagationRedundancyState();
				
				int numCurrentQueuedCount=PropagationEventsFwkDao.getPropagationEventsQueuedCount();
				List<Long> peGroupIdList= pickGroupedEventForProcessing(groupedToQueuedBatchCount);
				if (peGroupIdList != null && peGroupIdList.size()> 0) {

					if (numCurrentQueuedCount > maxThreasholdQueuedCount ) {
						hasMoreGroupedEventsInQueue = false;
					}
					Logger.info(logInfo, "sendMessagesToTxnJMSQueue", "putting more messages to queue");

					for (Long groupId :peGroupIdList ){
						Logger.info(logInfo, "sendMessagesToTxnJMSQueue", "sending message id - "+groupId);
						objSender.sendPropagationMessage("TestTxnQueueMessage",groupId);
					}
				}
				else {
					hasMoreGroupedEventsInQueue = false;
					Logger.debug(logInfo, "processEvents", "No more GROUPS to be picked from datachange queue, kicking self out of loop.");
				}
			}
			objSender.destroyJMS();
		} 
		catch (Throwable t) {
			CoronaErrorHandler.logError(t, "Exception while sending messae to txn jms queue", null);
		}
	}

	protected List<Long> pickGroupedEventForProcessing(int groupedToQueuedBatchCount) throws Exception {

		Logger.info(logInfo, "pickReadyEventForProcessing", "picked the messages for processing to be put to queue");
		String lastModifiedBy = CoronaFwkUtil.getUniqueId(
				CoronaFwkUtil.PROPAGATION_EVENT, UID_SIZE);

		PropagationEventsFwkDao.pickGroupedEventsToBeQueued(
				PICK_GROUPED_EVENT_TO_BE_QUEUED, groupedToQueuedBatchCount, lastModifiedBy,
				CoronaFwkConstants.ProcessingStatus.QUEUED,
				CoronaFwkConstants.ProcessingStatus.GROUPED);

		List <Long> peGroupList =Collections.EMPTY_LIST; 

		peGroupList= PropagationEventsFwkDao.retriveEventGroupIdOnStatusAndIdFromDb(lastModifiedBy, CoronaFwkConstants.ProcessingStatus.QUEUED);

		return peGroupList;
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
			TimerService ts = _propPollerContext.getTimerService();
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
			home = (MBeanHome) context.lookup(MBeanHome.LOCAL_JNDI_NAME);
			mbeanSet = home.getMBeansByType("ServerRuntime");
			Iterator mbeanIterator = mbeanSet.iterator();
			Timer timer = null;

			ServerRuntimeMBean serverBean = (ServerRuntimeMBean) mbeanIterator
			.next();
			String serverName = serverBean.getName();

			TimerService tservice = _propPollerContext.getTimerService();
			activeTimers = tservice.getTimers();

			if (activeTimers.isEmpty()) {
				if (manageServerGrp1Serv1.equalsIgnoreCase(serverName)
						|| manageServerGrp2Serv1.equalsIgnoreCase(serverName))
					startTimeInit = Config
					.getPropagationQueueEventsPollingInterval();
				else if (manageServerGrp1Serv2.equalsIgnoreCase(serverName)
						|| manageServerGrp2Serv2.equalsIgnoreCase(serverName))
					startTimeInit = (Config
							.getPropagationQueueEventsPollingInterval() + (Config
									.getPropagationQueueEventsPollingInterval() / 2));

				if (startTimeInit != null)
					timer = tservice.createTimer(startTimeInit * 1000, Config
							.getPropagationQueueEventsPollingInterval() * 1000,
							CoronaFwkConstants.PropagationEventsTimer);
				else
					timer = tservice
					.createTimer(
							Config
							.getPropagationQueueEventsPollingInterval() * 1000,
							Config
							.getPropagationQueueEventsPollingInterval() * 1000,
							CoronaFwkConstants.PropagationEventsTimer);

			} else {
				Logger.info(logInfo,"invokeRemote", "Number of Timers was already active with this bean are "
						+ activeTimers.size());
				Iterator timerItr = activeTimers.iterator();
				Logger.info(logInfo,"invokeRemote", "ActiveTimer : ");
				while (timerItr.hasNext())
					Logger.info(logInfo,"invokeRemote", timerItr.next()
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
