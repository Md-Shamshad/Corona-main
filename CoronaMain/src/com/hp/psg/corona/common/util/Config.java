package com.hp.psg.corona.common.util;

import java.io.FileInputStream;
import java.util.Properties;

import com.hp.psg.common.error.util.CoronaErrorConstants;
import com.hp.psg.common.util.constants.EventConstants;
import com.hp.psg.corona.error.util.CoronaErrorHandler;

/**
 * @author dudeja
 * 
 */
public class Config {
	static Properties props = null;
	static String currentServerName = null;

	static {
		try {
			props = new Properties();

			CoronaFwkUtil.println(getConfigDir());
			String configFile = getConfigDir() + "corona.properties";
			Logger.debug("Config", "staticBlock",
					"Initializing Configuration from file: " + configFile);
			props.load(new FileInputStream(configFile));

		} catch (Throwable t) {
			Logger.error("Config", "staticBlock",
					"Unable to initialize from fileSystem" + getConfigDir()
							+ "corona.properties", t);
			CoronaErrorHandler.logError(t.getClass(),
					CoronaErrorConstants.configErr, "staticBlock",
					"Unable to initialize from fileSystem" + getConfigDir()
							+ "corona.properties", false, t, true);
			try {
				props.load(props.getClass().getResourceAsStream(
						getConfigDir() + "corona.properties"));
			} catch (Throwable t2) {
				Logger.error("Config", "getConfigDir",
						"Unable to initialize from classpath: "
								+ getConfigDir() + "corona.properties", t);
				CoronaErrorHandler.logError(t2.getClass(),
						CoronaErrorConstants.configErr, "staticBlock",
						"Unable to initialize from classpath: "
								+ getConfigDir() + "corona.properties", false,
						t2, true);
			}
		}
		if (props != null)
			Logger.info("Config", "getConfigDir", "Loaded properties "
					+ props.toString());
	}

	public static String getProperty(String name) {
		return getProperty(name, "");
	}

	public static String getProperty(String name, String defaultVal) {
		String str = (String) props.get(name);
		return (str != null ? str.trim() : defaultVal);
	}

	public static String getConfigDir() {
		String configDir = null;
		try {
			configDir = System.getProperty(EventConstants.ComcatConfigDir);
			if (configDir == null) {
				Logger.debug("Config", "getConfigDir", "System properties are "
						+ System.getProperties().toString());
				Logger
						.debug(
								"Config",
								"getConfigDir",
								"Unable to get comcat.config.dir from system property, using default /opt/sasuapps/ecomcat/shared/corona/");
				configDir = "/opt/sasuapps/ecomcat/shared/comcat_corona_local/conf/";
				System.setProperty(EventConstants.ComcatConfigDir, configDir);
			}
			if (!configDir.endsWith(System.getProperty("file.separator")))
				configDir += System.getProperty("file.separator");
		} catch (Exception e) {
			Logger.error("Config", "getConfigDir", "getConfigDir() failed", e);
			CoronaErrorHandler.logError(e.getClass(),
					CoronaErrorConstants.configErr, "getConfigDir",
					"getConfigDir() failed", false, e, true);
		}
		return configDir;
	}

	public static long getDataChangeEventsPollingInterval() {
		try {
			return Long
					.parseLong(getProperty("corona.datachange.poller.interval.seconds"));
		} catch (Exception e) {
			Logger.error("Config", "getDataChangeEventsPollingInterval", e);
			CoronaErrorHandler.logError(e.getClass(),
					CoronaErrorConstants.configErr,
					"getDataChangeEventsPollingInterval",
					"Error in getDataChangeEventsPollingInterval ", false, e,
					true);
			return 5;
		}
	}

	public static long getRetrialPollingInterval() {
		try {
			return Long
					.parseLong(getProperty("corona.retrial.poller.interval.seconds"));
		} catch (Exception e) {
			Logger.error("Config", "getRetrialPollingInit", e);
			CoronaErrorHandler.logError(e.getClass(),
					CoronaErrorConstants.configErr, "getRetrialPollingInit",
					"Error in getRetrialPollingInit ", false, e, true);
			return 60;
		}
	}

	public static long getTxnQueueEventsPollingInterval() {
		try {
			return Long
					.parseLong(getProperty("corona.txn.queue.poller.interval.seconds"));
		} catch (Exception e) {
			Logger.error("Config", "getTxnQueueEventsPollingInterval", e);
			CoronaErrorHandler.logError(e.getClass(),
					CoronaErrorConstants.configErr,
					"getTxnQueueEventsPollingInterval",
					"Error in getTxnQueueEventsPollingInterval ", false, e,
					true);
			return 60;
		}
	}

	public static long getPropagationQueueEventsPollingInterval() {
		try {
			return Long
					.parseLong(getProperty("corona.propevents.poller.interval.seconds"));
		} catch (Exception e) {
			Logger.error("Config", "getPropagationQueueEventsPollingInterval",
					e);
			CoronaErrorHandler.logError(e.getClass(),
					CoronaErrorConstants.configErr,
					"getPropagationQueueEventsPollingInterval",
					"Error in getPropagationQueueEventsPollingInterval ",
					false, e, true);
			return 60;
		}
	}

	public static long getReplicationPollingInterval() {
		try {
			return Long
					.parseLong(getProperty("corona.replication.interval.seconds"));
		} catch (Exception e) {
			Logger.error("Config", "getReplicationPollingInterval", e);
			CoronaErrorHandler.logError(e.getClass(),
					CoronaErrorConstants.configErr,
					"getReplicationPollingInterval",
					"Error in getReplicationPollingInterval", false, e, true);
			return 180;
		}
	}

	public static String getJndiAdminUrl() {
		return getProperty("corona.jndi.admin.url");
	}

	public static String getJndiClusterUrl() {
		return getProperty("corona.jndi.cluster.url");
	}

	public static String getWeblogicUser() {
		return getProperty("corona.weblogic.username");
	}

	public static String getWeblogicPassword() {
		return getProperty("corona.weblogic.password");
	}

	public static String getSendNotification() {
		return getProperty("send.notification");
	}

	public static String getDefaultPropagationEventsQueue() {
		return getProperty("corona.defaultPropagationEventsQueue","PropQueue");
	}

	public static String getDefaultDataChangeEventsTxnQueue(){
		return getProperty("corona.defaultDataChangeEventsTxnQueue","TxnQueue");
	}
	
	public static String getDefaultTxnQueueConnectionFactory() {
		return getProperty("corona.DefaultTxnQueueConnectionFactoryJndiName");
	}

	public static String getDefaultPropConnectionFactory() {
		return getProperty("corona.DefaultPropConnectionFactoryJndiName");
	}

	public static String getcoronaManageServerGrp1Serv1() {
		return getProperty("corona.ManageServerGrp1Serv1");
	}
	public static String getcoronaManageServerGrp1Serv2() {
		return getProperty("corona.ManageServerGrp1Serv2");
	}
	public static String getcoronaManageServerGrp2Serv1() {
		return getProperty("corona.ManageServerGrp2Serv1");
	}
	public static String getcoronaManageServerGrp2Serv2() {
		return getProperty("corona.ManageServerGrp2Serv2");
	}

	public static String getPropagationPollerJndiName() {
		return getProperty("corona.PropagationPollerJndiName");
	}

	public static String getRetrialPollerJndiName() {
		return getProperty("corona.RetrialPollerJndiName");
	}

	public static String getDataChangeConfigPollerJndiName() {
		return getProperty("corona.DataChangeConfigPollerJndiName");
	}

	public static String getDataChangePermPollerJndiName() {
		return getProperty("corona.DataChangePermPollerJndiName");
	}
	
	public static String getDataChangePrsPollerJndiName() {
		return getProperty("corona.DataChangePrsPollerJndiName");
	}

	public static String getDataChangeListPricePollerJndiName() {
		return getProperty("corona.DataChangeListPricePollerJndiName");
	}

	
	public static String getDataChangeProdDescPollerJndiName()
	{
		return getProperty("corona.DataChangeProdDescPollerJndiName");
	}

	public static String getDataChangeQueuePollerJndiName() {
		return getProperty("corona.DataChangeQueuePollerJndiName");
	}

	
	public static String getWeblogicInitialContextFactoryName() {
		return getProperty("corona.weblogic.InitialContextFactory",
				"weblogic.jndi.WLInitialContextFactory");
	}
	
	public static boolean doPerformanceTimeLogging() {
		return new Boolean(getProperty(
				"corona.doPerformanceTimeLogging", "true"))
				.booleanValue();

	}

	public static boolean doCheckForServerStateAndStatusForTimer() {
		return new Boolean(getProperty(
				"corona.checkForServerStateAndStatusForTimer", "false"))
				.booleanValue();
	}
	
	public static String getCurrentServerName(){
		if (currentServerName == null || "".equals(currentServerName) ){
			currentServerName = CoronaFwkUtil.getActualMiName();
		}
		return currentServerName;
	}
	public static boolean doProcessingInStoredProc() {
		return new Boolean(getProperty(
				"corona.doProcessingInStoredProc", "true"))
				.booleanValue();
	}

}
