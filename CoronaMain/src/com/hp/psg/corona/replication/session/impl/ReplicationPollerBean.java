/**
 * 
 */
package com.hp.psg.corona.replication.session.impl;

import java.rmi.RemoteException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.StringTokenizer;

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
import com.hp.psg.corona.common.util.DynamicConfig;
import com.hp.psg.corona.common.util.JndiUtils;
import com.hp.psg.corona.common.util.Logger;
import com.hp.psg.corona.error.util.CoronaErrorHandler;
import com.hp.psg.corona.replication.cto.handler.ReplicationEventProcessor;


/**
 * @author dudeja
 * @version 1.0
 */
public class ReplicationPollerBean implements SessionBean, TimedObject {

	private SessionContext _replicationPollerContext;
	MBeanHome home = null;
	Context context = null;
	LoggerInfo logInfo=null;

	//Wlll get initialized when poller is triggered.
	String sourceObjectKey = null;
	String groupIds = null;
	String masterInsatance = null;
	String threadName = null;

	Set mbeanSet = null;


	private TimerHandle timerHandle = null;
	public void ejbCreate() {
		logInfo = new LoggerInfo (this.getClass().getName());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.ejb.SessionBean#ejbActivate()
	 */
	public void ejbActivate() throws EJBException, RemoteException {
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.ejb.SessionBean#ejbPassivate()
	 */
	public void ejbPassivate() throws EJBException, RemoteException {
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.ejb.SessionBean#ejbRemove()
	 */
	public void ejbRemove() throws EJBException, RemoteException {
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.ejb.SessionBean#setSessionContext(javax.ejb.SessionContext)
	 */
	public void setSessionContext(SessionContext context) throws EJBException,
			RemoteException {

		_replicationPollerContext = context;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.ejb.TimedObject#ejbTimeout(javax.ejb.Timer)
	 */
	public void ejbTimeout(Timer timer) {

		ReplicationEventProcessor handler = new ReplicationEventProcessor();
		Timestamp timeStamp= new Timestamp(System.currentTimeMillis());
		
		List<String> objectNames = null;
		String source = null;

		Logger.repInfo(logInfo,"ejbTimeout", "@@@@@@@@@ Timeout for Replication poller bean (ReplicationPollerBean) at "+timeStamp);
		
		// Always send source as first and rest as objects to be replicated and
		// they are seperated by | as delimiter.
		
		//Now the object on which replication will work is defined in deployment descriptor and the group and thread mapping is defined in dynami_ccorona.properies

		if (threadName != null){
			masterInsatance = DynamicConfig.getMasterInstanceName(threadName);
			sourceObjectKey = DynamicConfig.getSourceObjectKey(threadName);
			groupIds =DynamicConfig.getAllGroupIds(threadName);
		}
		
		if (sourceObjectKey != null) {
			StringTokenizer sourceObjectTokens = new StringTokenizer(
					sourceObjectKey, "|");
			if (sourceObjectTokens.countTokens() >= 2) {

				source = sourceObjectTokens.nextToken();
				objectNames = new ArrayList<String>();
				while (sourceObjectTokens.hasMoreTokens()) {
					objectNames.add(sourceObjectTokens.nextToken());
				}
			}
			

			if (groupIds != null) { 
				String[] groups = groupIds.split("\\|");
				if (source != null && objectNames.size() > 0) {
					for (String objectName : objectNames){
						Logger.repInfo(logInfo,"ejbTimeout","Processing replication for Source = "+source+"  and object = "+objectName);
						handler.setThreadName(threadName);
						handler.processReplication(source, objectName , groups);
					}
				}
			}
			else {
				Logger.repDebug(logInfo,"ejbTimeout", "No group defintion found");
			}
				
		}
		Logger.repInfo(logInfo,"ejbTimeout", "@@@@@@@@@ Timeout return for Replication poller bean (ReplicationPollerBean) for "+timeStamp);
	}

	public void invokeRemote() throws EJBException, RemoteException {
		Long startTimeInit = null;
		try {
			TimerService tservice = _replicationPollerContext.getTimerService();
			Collection activeTimers = tservice.getTimers();

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

			//Now only thread name is passed from the environmental property.. else all is picked up from dynamic config property file..
			threadName = (String) context
			.lookup("java:comp/env/THREAD_NAME");

			ServerRuntimeMBean serverBean = (ServerRuntimeMBean) mbeanIterator
					.next();
			String serverName = serverBean.getName();

			if (activeTimers.isEmpty()) {
				if (manageServerGrp1Serv1.equalsIgnoreCase(serverName)
						|| manageServerGrp2Serv1.equalsIgnoreCase(serverName))
					startTimeInit = Config.getReplicationPollingInterval();
				else if (manageServerGrp1Serv2.equalsIgnoreCase(serverName)
						|| manageServerGrp2Serv2.equalsIgnoreCase(serverName))
					startTimeInit = (Config.getReplicationPollingInterval() + (Config
							.getReplicationPollingInterval() / 2));

				if (startTimeInit != null)
					timer = tservice.createTimer(startTimeInit * 1000, Config
							.getReplicationPollingInterval() * 1000,
							CoronaFwkConstants.ReplicationTimer);
				else
					timer = tservice.createTimer(Config
							.getReplicationPollingInterval() * 1000, Config
							.getReplicationPollingInterval() * 1000,
							CoronaFwkConstants.ReplicationTimer);

			} else {
				Logger.repInfo(logInfo,"invokeRemote", "Number of Timers was already active with this bean are "
								+ activeTimers.size());
				Iterator timerItr = activeTimers.iterator();
				Logger.repInfo(logInfo,"invokeRemote", "ActiveTimer : ");
				while (timerItr.hasNext())
					Logger.repInfo(logInfo,"invokeRemote", timerItr.next()
							.toString());
			}
		} catch (Exception e) {
			CoronaErrorHandler.logError(e, null, null);
		}

	}

	
	public void reinitializeTimer (Long startTimeInit) {
		try {
			TimerService tservice = _replicationPollerContext.getTimerService();
			Collection<Timer> activeTimers = tservice.getTimers();
			Timer timer;
			
			if ( ! activeTimers.isEmpty()) {
				Logger.repInfo(logInfo,"invokeRemote", "Number of Timers was already active with this bean are "
								+ activeTimers.size());
				Iterator<Timer> timerItr = activeTimers.iterator();
				Logger.repInfo(logInfo,"invokeRemote", "ActiveTimer : ");
				while (timerItr.hasNext()) {
					System.out.println("Canceling timer "+timerItr.next()
							.toString());
					//Cancelling current existing timers.
					timer = timerItr.next();
					timer.cancel();
				}
			}		
			
			//Initialize it again 
			if (startTimeInit != null)
				timer = tservice.createTimer(startTimeInit * 1000, Config
						.getReplicationPollingInterval() * 1000,
						CoronaFwkConstants.ReplicationTimer);
			else
				timer = tservice.createTimer(Config
						.getReplicationPollingInterval() * 1000, Config
						.getReplicationPollingInterval() * 1000,
						CoronaFwkConstants.ReplicationTimer);

		}catch (Exception ex){
			ex.printStackTrace();
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
