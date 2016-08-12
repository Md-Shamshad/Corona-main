package com.hp.psg.corona.propagation.session.impl;

import java.rmi.RemoteException;
import java.sql.Timestamp;
import java.util.Collection;
import java.util.Iterator;
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

import com.hp.psg.common.db.dax.DaxDB;
import com.hp.psg.common.db.dax.DaxDataBeanGeneral;
import com.hp.psg.common.db.dax.DaxMgr;
import com.hp.psg.common.db.dax.DaxParsedStmt;
import com.hp.psg.common.error.util.CoronaErrorConstants;
import com.hp.psg.common.util.logging.LoggerInfo;
import com.hp.psg.corona.common.constants.CoronaFwkConstants;
import com.hp.psg.corona.common.handler.ErrorAndHangingEventsHandler;
import com.hp.psg.corona.common.util.Config;
import com.hp.psg.corona.common.util.CoronaFwkUtil;
import com.hp.psg.corona.common.util.JndiUtils;
import com.hp.psg.corona.common.util.Logger;
import com.hp.psg.corona.error.util.CoronaErrorHandler;

/**
 * @author dudeja
 * @version 1.0 
 */
public class RetrialPollerBean implements SessionBean, TimedObject {
	private SessionContext _retrialContext;
	private LoggerInfo logInfo = null;
	MBeanHome home = null;
	Context context = null;
	Set mbeanSet = null;
	

	public static final String RETRIAL_GROUP = "RETRIAL_GROUP";
	public static final String RETRIAL_PROP_EVENTS_GROUP="RETRIAL_PROP_EVENTS_GROUP";
	public static final String RETRIAL_DATA_CHANGE_GROUP="RETRIAL_DATA_CHANGE_GROUP";
	public static final String RETRY_ERROR_EVENTS="RETRY_ERROR_EVENTS";
	public static final String RETRY_HANGING_EVENTS="RETRY_HANGING_EVENTS";

	
	private TimerHandle timerHandle = null;
	public void ejbCreate() {
		logInfo = new LoggerInfo (this.getClass().getName());
	}

	public void setSessionContext(SessionContext context) throws EJBException {
		_retrialContext = context;
		logInfo = new LoggerInfo(this.getClass().getName());
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
						
			Logger.info(logInfo, "ejbTimeout", "@@@@@@@@@ Timeout for Retrial poller bean (RetrialPollerBean) at "+ timeStamp);

			startRetrialTasks(); //Update error events back to ready (count for 3).
			Logger.info(logInfo, "ejbTimeout", "@@@@@@@@@ Timeout return for Retrial poller bean (RetrialPollerBean) for "+timeStamp);
			
		} catch (Exception e) {
			CoronaErrorHandler.logError(e, null, null);
		}
	}

	public void startRetrialTasks() throws Exception {
		try {
			try {
				Logger.info(logInfo, "startRetrialTasks", "@@@ Retrial task for Datachange Error events events");
				verifyDataChanegErrorAndHangingEvents();
				
			}catch (Exception ex){
				CoronaErrorHandler.logError(ex, "Exception while retrial for datachange events", null);
			}

			try {
				Logger.info(logInfo, "startRetrialTasks", "@@@ Retrial task for proagation events");
				verifyPropErrorAndHangingEvents();
			}catch (Exception ex){
				CoronaErrorHandler.logError(ex, "Exception while retrial of propagation events ", null);
			}

			ErrorAndHangingEventsHandler errorEventsProcessor = new ErrorAndHangingEventsHandler();
			errorEventsProcessor.processErrorAndHangingEvents();
			
		} catch (Throwable t) {
			CoronaErrorHandler.logError(t, null, null);
		}
	}

	public void invokeRemote() throws EJBException, RemoteException {
		Long startTimeInit = null;
		try {
			TimerService ts = _retrialContext.getTimerService();
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

			TimerService tservice = _retrialContext.getTimerService();
			activeTimers = tservice.getTimers();

			if (activeTimers.isEmpty()) {
				if (manageServerGrp1Serv1.equalsIgnoreCase(serverName)
						|| manageServerGrp2Serv1.equalsIgnoreCase(serverName))
					startTimeInit = Config.getRetrialPollingInterval();
				else if (manageServerGrp1Serv2.equalsIgnoreCase(serverName)
						|| manageServerGrp2Serv2.equalsIgnoreCase(serverName))
					startTimeInit = (Config.getRetrialPollingInterval() + (Config
							.getRetrialPollingInterval() / 2));

				if (startTimeInit != null)
					timer = tservice.createTimer(startTimeInit * 1000, Config
							.getRetrialPollingInterval() * 1000,
							CoronaFwkConstants.RetrialTimer);
				else
					timer = tservice.createTimer(Config
							.getRetrialPollingInterval() * 1000, Config
							.getRetrialPollingInterval() * 1000,
							CoronaFwkConstants.RetrialTimer);

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


	public void verifyPropErrorAndHangingEvents(){
		verifyErrorPropagationToRetry();
		verifyHangingPropagationEvents();
	}

	public void verifyDataChanegErrorAndHangingEvents(){
		verifyErrorDataEventsToRetry();
		verifyHangingDataChangeEvents();
	}
	
	public void verifyErrorPropagationToRetry() {
		try {
			DaxDataBeanGeneral dataBean = new DaxDataBeanGeneral();
			DaxMgr dxMgr = DaxMgr.getInstance();

			dataBean.setString2(CoronaFwkUtil.getManageServerName());

			DaxParsedStmt dxPstmt = dxMgr.makeParsedStmt(RETRIAL_PROP_EVENTS_GROUP, RETRY_ERROR_EVENTS,
					dataBean, null, null);
			DaxDB dxDb = dxMgr.getDaxDB();
			dxDb.doUpdate(dxPstmt, dataBean);
			
		} catch (Exception e) {
			CoronaErrorHandler.logError(e.getClass(),
					CoronaErrorConstants.processingErr,
					"verifyErrorPropagationToRetry",
					"Exception while retrial of error events in datachange queue ",
					false, e, true);
		}
	}

	public void verifyHangingPropagationEvents() {
		try {
			DaxDataBeanGeneral dataBean = new DaxDataBeanGeneral();
			DaxMgr dxMgr = DaxMgr.getInstance();

			dataBean.setString2(CoronaFwkUtil.getManageServerName());

			DaxParsedStmt dxPstmt = dxMgr.makeParsedStmt(RETRIAL_PROP_EVENTS_GROUP, RETRY_HANGING_EVENTS,
					dataBean, null, null);
			DaxDB dxDb = dxMgr.getDaxDB();
			dxDb.doUpdate(dxPstmt, dataBean);
			
		} catch (Exception e) {
			CoronaErrorHandler.logError(e.getClass(),
					CoronaErrorConstants.processingErr,
					"verifyHangingPropagationEvents",
					"Exception while updating hanging events in datachange queue ",
					false, e, true);
		}
	}
	
	
	public static void verifyErrorDataEventsToRetry() {
		try {
			DaxDataBeanGeneral dataBean = new DaxDataBeanGeneral();
			DaxMgr dxMgr = DaxMgr.getInstance();

			dataBean.setString2(CoronaFwkUtil.getManageServerName());

			DaxParsedStmt dxPstmt = dxMgr.makeParsedStmt(RETRIAL_DATA_CHANGE_GROUP, RETRY_ERROR_EVENTS,
					dataBean, null, null);
			DaxDB dxDb = dxMgr.getDaxDB();
			dxDb.doUpdate(dxPstmt, dataBean);

		} catch (Exception e) {
			CoronaErrorHandler.logError(e.getClass(),
					CoronaErrorConstants.processingErr,
					"verifyErrorEventsStatusRetrial",
					"Exception while retrial of error events in datachange queue ",
					false, e, true);
		}
	}

	public static void verifyHangingDataChangeEvents() {
		try {
			DaxDataBeanGeneral dataBean = new DaxDataBeanGeneral();
			DaxMgr dxMgr = DaxMgr.getInstance();

			dataBean.setString2(CoronaFwkUtil.getManageServerName());

			DaxParsedStmt dxPstmt = dxMgr.makeParsedStmt(RETRIAL_DATA_CHANGE_GROUP, RETRY_HANGING_EVENTS,
					dataBean, null, null);
			DaxDB dxDb = dxMgr.getDaxDB();
			dxDb.doUpdate(dxPstmt, dataBean);

		} catch (Exception e) {
			CoronaErrorHandler.logError(e.getClass(),
					CoronaErrorConstants.processingErr,
					"verifyHangingDataChangeEvents",
					"Exception while updating hanging events in datachange queue ",
					false, e, true);
		}
	}


}
