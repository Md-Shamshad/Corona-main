package com.hp.psg.corona.datachange.session.impl;

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

import com.hp.psg.common.util.logging.LoggerInfo;
import com.hp.psg.corona.common.constants.CoronaFwkConstants;
import com.hp.psg.corona.common.util.Config;
import com.hp.psg.corona.common.util.CoronaFwkUtil;
import com.hp.psg.corona.common.util.JndiUtils;
import com.hp.psg.corona.common.util.Logger;
import com.hp.psg.corona.datachange.handler.ConfigDataChangeHandler;
import com.hp.psg.corona.error.util.CoronaErrorHandler;

/*
 * @author dudeja
 * @version 1.0
 *
 */
public class DataChangePollerBean implements SessionBean, TimedObject {
	private SessionContext _dataChangePollerContext;
	MBeanHome home = null;
	Context context = null;
	Set mbeanSet = null;
	String dataChangeType = null;

	LoggerInfo logInfo = null;

	private TimerHandle timerHandle = null;
	public void ejbCreate() {
		logInfo = new LoggerInfo (this.getClass().getName());
	}

	public void setSessionContext(SessionContext context) throws EJBException {
		_dataChangePollerContext = context;
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
			
			ConfigDataChangeHandler cfgHandler = new ConfigDataChangeHandler(); 
			Logger.info(logInfo, "ejbTimeOut","@@@@@@@@@ DATACHANGE TYPE "+ this.dataChangeType); 
			
			if ("CONFIG_PERMUTATION".equalsIgnoreCase(this.dataChangeType)) {
				Logger.info(logInfo, "ejbTimeOut","@@@@@@@@@ Timeout for CONFIG_PERMUTATION (DataChangePollerBean)) at "+ timeStamp);
				String serverRunningTimer = CoronaFwkUtil.executionLockedBy("CONFIG_PERMUTATION");
				if (("".equals(serverRunningTimer))) {
					CoronaFwkUtil.lockTimerByName("CONFIG_PERMUTATION");
					cfgHandler.addConfigPermDataChangeEvents();
					CoronaFwkUtil.releaseLockForTimerByName("CONFIG_PERMUTATION");
				}else {
					Logger.info(logInfo, "ejbTimeOut","@@@@@@@@@ execution locked for CONFIG_PERMUTATION (DataChangePollerBean)) by  "+serverRunningTimer);
				}
				Logger.info(logInfo, "ejbTimeOut","@@@@@@@@@ Timeout return for CONFIG_PERMUTATION (DataChangePollerBean)) for "+ timeStamp);
			} else if ("CONFIG_PRICE".equalsIgnoreCase(this.dataChangeType)) {
				Logger.info(logInfo, "ejbTimeOut","@@@@@@@@@ Timeout for CONFIG_PRICE (DataChangePollerBean) at "+ timeStamp);
				String serverRunningTimer = CoronaFwkUtil.executionLockedBy("CONFIG_PRICE");
				if (("".equals(serverRunningTimer))) {
					CoronaFwkUtil.lockTimerByName("CONFIG_PRICE");
					cfgHandler.addConfigPriceDataChangeEvents();
					CoronaFwkUtil.releaseLockForTimerByName("CONFIG_PRICE");
				}else {
					Logger.info(logInfo, "ejbTimeOut","@@@@@@@@@ execution locked for CONFIG_PRICE (DataChangePollerBean)) by  "+serverRunningTimer);
				}
				Logger.info(logInfo, "ejbTimeOut","@@@@@@@@@ Timeout return for CONFIG_PRICE (DataChangePollerBean) for "+ timeStamp);
			} else if ("CONFIG_CNTRY".equalsIgnoreCase(this.dataChangeType)) {
				Logger.info(logInfo, "ejbTimeOut","@@@@@@@@@ Timeout for CONFIG_CNTRY (DataChangePollerBean) at "+timeStamp);
				String serverRunningTimer = CoronaFwkUtil.executionLockedBy("CONFIG_CNTRY");
				if (("".equals(serverRunningTimer))) {
					CoronaFwkUtil.lockTimerByName("CONFIG_CNTRY");
					cfgHandler.addConfigCntryDataChangeEvents();
					CoronaFwkUtil.releaseLockForTimerByName("CONFIG_CNTRY");
				}else {
					Logger.info(logInfo, "ejbTimeOut","@@@@@@@@@ execution locked for CONFIG_CNTRY (DataChangePollerBean)) by  "+serverRunningTimer);
				}
				Logger.info(logInfo, "ejbTimeOut","@@@@@@@@@ Timeout return for CONFIG_CNTRY (DataChangePollerBean) for "+timeStamp);
			} else if ("PRODUCT_DESCRIPTION"
					.equalsIgnoreCase(this.dataChangeType)) {
				Logger.info(logInfo, "ejbTimeOut","@@@@@@@@@ Timeout for PRODUCT_DESCRIPTION (DataChangePollerBean) at "+timeStamp);
				String serverRunningTimer = CoronaFwkUtil.executionLockedBy("PRODUCT_DESCRIPTION");
				if (("".equals(serverRunningTimer))) {
					CoronaFwkUtil.lockTimerByName("PRODUCT_DESCRIPTION");
					cfgHandler.addProductDescDataChangeEvents();
					CoronaFwkUtil.releaseLockForTimerByName("PRODUCT_DESCRIPTION");
				}else {
					Logger.info(logInfo, "ejbTimeOut","@@@@@@@@@ execution locked for PRODUCT_DESCRIPTION (DataChangePollerBean)) by  "+serverRunningTimer);
				}
				Logger.info(logInfo, "ejbTimeOut","@@@@@@@@@ Timeout return for PRODUCT_DESCRIPTION (DataChangePollerBean) for "+timeStamp);
			} else if ("LIST_PRICE".equalsIgnoreCase(this.dataChangeType)) {
				Logger.info(logInfo, "ejbTimeOut","@@@@@@@@@ Timeout for LIST_PRICE (DataChangePollerBean) at "+ timeStamp);
				String serverRunningTimer = CoronaFwkUtil.executionLockedBy("CONFIG_PRICE");
				if (("".equals(serverRunningTimer))) {
					CoronaFwkUtil.lockTimerByName("LIST_PRICE");
					cfgHandler.addListPriceDataChangeEvents();
					CoronaFwkUtil.releaseLockForTimerByName("LIST_PRICE");
				}else {
					Logger.info(logInfo, "ejbTimeOut","@@@@@@@@@ execution locked for LIST_PRICE (DataChangePollerBean)) by  "+serverRunningTimer);
				}
				Logger.info(logInfo, "ejbTimeOut","@@@@@@@@@ Timeout return for CONFIG_PRICE (DataChangePollerBean) for "+ timeStamp);
			}else {
				Logger.error(logInfo,"Time out does not match any of datachangetype being set in env properties - "+this.dataChangeType,new Exception("dataChangeType not found"));
			}
		} catch (Exception e) {
			CoronaErrorHandler.logError(e, null, null);
		}
	}


	public void invokeRemote() throws EJBException, RemoteException {
		Long startTimeInit = null;
		try {
			TimerService ts = _dataChangePollerContext.getTimerService();
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

			dataChangeType = (String) context
			.lookup("java:comp/env/DataChangeType");

			home = (MBeanHome) context.lookup(MBeanHome.LOCAL_JNDI_NAME);
			mbeanSet = home.getMBeansByType("ServerRuntime");
			Iterator mbeanIterator = mbeanSet.iterator();
			Timer timer = null;

			ServerRuntimeMBean serverBean = (ServerRuntimeMBean) mbeanIterator
			.next();
			String serverName = serverBean.getName();

			TimerService tservice = _dataChangePollerContext.getTimerService();
			activeTimers = tservice.getTimers();

			if (activeTimers.isEmpty()) {
				if (manageServerGrp1Serv1.equalsIgnoreCase(serverName)
						|| manageServerGrp2Serv1.equalsIgnoreCase(serverName))
					startTimeInit = Config.getDataChangeEventsPollingInterval();
				else if (manageServerGrp1Serv2.equalsIgnoreCase(serverName)
						|| manageServerGrp2Serv2.equalsIgnoreCase(serverName))
					startTimeInit = (Config
							.getDataChangeEventsPollingInterval() + (Config
									.getDataChangeEventsPollingInterval() / 2));

				if (startTimeInit != null)
					timer = tservice.createTimer(startTimeInit * 1000, Config
							.getDataChangeEventsPollingInterval() * 1000,
							CoronaFwkConstants.DataChangeEventsTimer);
				else
					timer = tservice.createTimer(Config
							.getDataChangeEventsPollingInterval() * 1000,
							Config.getDataChangeEventsPollingInterval() * 1000,
							CoronaFwkConstants.DataChangeEventsTimer);
			} else {
				Logger.info(logInfo,"invokeRemote","Number of Timers was already active with "+this.getClass().getName()+" are "
						+ activeTimers.size());
				Iterator timerItr = activeTimers.iterator();
				Logger.info(logInfo,"invokeRemote","ActiveTimer : ");
				while (timerItr.hasNext())
					Logger.info(logInfo,"invokeRemote",timerItr.next()
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
