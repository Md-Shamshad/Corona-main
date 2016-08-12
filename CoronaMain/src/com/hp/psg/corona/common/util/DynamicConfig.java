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
public class DynamicConfig {
	static Properties props = null;

	static {
		refreshConfigProperties();
	}

	public static void refreshConfigProperties (){
		try {
			props = new Properties();

			CoronaFwkUtil.println(getConfigDir());
			String configFile = getConfigDir() + "dynamic_corona.properties";
			Logger.debug("Config", "staticBlock",
					"Initializing Configuration from file: " + configFile);
			props.load(new FileInputStream(configFile));

		} catch (Throwable t) {
			Logger.error("Config", "staticBlock",
					"Unable to initialize from fileSystem" + getConfigDir()
							+ "dynamic_corona.properties", t);
			CoronaErrorHandler.logError(t.getClass(),
					CoronaErrorConstants.configErr, "staticBlock",
					"Unable to initialize from fileSystem" + getConfigDir()
							+ "dynamic_corona.properties", false, t, true);
			try {
				props.load(props.getClass().getResourceAsStream(
						getConfigDir() + "corona.properties"));
			} catch (Throwable t2) {
				Logger.error("Config", "getConfigDir",
						"Unable to initialize from classpath: "
								+ getConfigDir() + "dynamic_corona.properties", t);
				CoronaErrorHandler.logError(t2.getClass(),
						CoronaErrorConstants.configErr, "staticBlock",
						"Unable to initialize from classpath: "
								+ getConfigDir() + "dynamic_corona.properties", false,
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


	public static int getReadyToQueuedBatchUpdateCountForDataChange() {
		return new Integer(getProperty("corona.dynamic.readyToQueuedBatchUpdateCountForDataChange", "500"))
				.intValue();
	}

	public static int getReadyToQueuedBatchUpdateCountForPropagation() {
		return new Integer(getProperty("corona.dynamic.readyToQueuedBatchUpdateCountForPropagation", "500"))
				.intValue();
	}

	public static boolean doPutDataChangeEventsProcessingOnHold() {
		return new Boolean(getProperty(
				"corona.dynamic.putDataChangeEventsProcessingOnHold", "false"))
				.booleanValue();

	}

	public static boolean doPutPropagationEventsProcessingOnHold() {
		return new Boolean(getProperty(
				"corona.dynamic.putPropagationEventsProcessingOnHold", "false"))
				.booleanValue();

	}

	public static int getMaxDataChangeQueueThreshholdValue() {
		return new Integer(getProperty("corona.dynamic.maxDataChangeQueueThresholdValue", "20000"))
				.intValue();
	}
	
	
	public static int getMaxQueuePropagationThreshholdValue() {
		return new Integer(getProperty("corona.dynamic.maxPropagationQueueThresholdValue", "20000"))
				.intValue();
	}
	
	//This method will create a map for all threadNames present and their groups to work upon., This property will be loaded by retrial Poller so cache will remain in dynamic.
	//Please note the thread name from this method should have a mapping in ejb.jar.xml and weblogic-ejb.jar.xml
	public static String[] getAllReplicationThreadNames() {
		
		String[]  returnThreadNames = null;
		try {
			String threadNames=getProperty("replication.dynamic.all.threadNames");
			if (threadNames != null){
				//Split the thread names and groups
				returnThreadNames = threadNames.split("\\,");
			}
			
		}catch (Exception ex){
			ex.printStackTrace();
		}
		
		return returnThreadNames;
	}
	
	public static String getMasterInstanceName(String threadName){
		return getProperty("replication.dynamic.sourceObjectKey."+threadName);
	}
	public static String getSourceObjectKey(String threadName){
		return getProperty("replication.dynamic.sourceObjectKey."+threadName);
	}
	public static String getAllGroupIds(String threadName){
		return getProperty("replication.dynamic.groupIds."+threadName);
	}
}
