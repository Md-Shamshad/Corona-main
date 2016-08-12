package com.hp.psg.corona.common.util;

import java.io.ByteArrayOutputStream;

import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.security.SecureRandom;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.StringTokenizer;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.swing.text.Document;

import org.dom4j.io.OutputFormat;
import org.dom4j.io.XMLWriter;

import weblogic.management.MBeanHome;
import weblogic.management.WebLogicObjectName;
import weblogic.management.runtime.ServerRuntimeMBean;

import com.hp.psg.common.error.DataChangeException;
import com.hp.psg.common.util.logging.LoggerInfo;
import com.hp.psg.corona.common.beans.CTODaxDataBeanGeneral;
import com.hp.psg.corona.common.beans.CountryCodeBean;
import com.hp.psg.corona.common.beans.CurrencyBean;
import com.hp.psg.corona.common.beans.PriceTermBean;
import com.hp.psg.corona.common.cto.beans.ConfigComponentInfo;
import com.hp.psg.corona.common.cto.beans.ConfigDescription;
import com.hp.psg.corona.common.cto.beans.ConfigHeaderInfo;
import com.hp.psg.corona.common.cto.beans.ConfigPermutationInfo;
import com.hp.psg.corona.common.cto.beans.ConfigPermutationPriceInfo;
import com.hp.psg.corona.common.cto.beans.ConfigPriceHeaderInfo;
import com.hp.psg.corona.common.cto.beans.CoronaBaseObject;
import com.hp.psg.corona.common.cto.beans.PriceInfo;
import com.hp.psg.corona.common.cto.beans.ProductDescription;
import com.hp.psg.corona.datachange.dao.DataChangeEventsFwkDao;
import com.hp.psg.corona.datachange.plugin.helper.CoronaObjectWrapper;
import com.hp.psg.corona.datachange.plugin.interfaces.FeaturePluginRequest;
import com.hp.psg.corona.datachange.plugin.interfaces.FeaturePluginResult;
import com.hp.psg.corona.error.util.CoronaErrorHandler;
import com.hp.psg.corona.propagation.handler.interfaces.IBeanPropagationUtil;
import com.hp.psg.corona.util.cache.CtoPluginHashMap;
import com.hp.psg.corona.util.cache.CtoTransformationUtilMap;

/**
 * @author dudeja
 * @version 1.0
 *
 */
public class CoronaFwkUtil {
	public static String PROPAGATION_EVENT = "PROPAGATION_EVENT";
	public static String DATA_CHANGE_EVENT = "DATA_CHANGE_EVENT";
	public static String PENDING_PROP_EVENTS = "PENDING_PROP_EVENTS";
	public static String REPLICATIION_EVENTS = "REPLICATION_EVENTS";

	/**
	 * return a string that is composed of the passed in prefix and a random
	 * number.
	 * 
	 * @param prefix
	 * @return
	 * @throws Exception
	 */
	public static String getUniqueId(String eventType, int maxLength)
	throws Exception {
		StringBuffer sb = new StringBuffer();
		String prefix = null;
		String uid = null;

		try {
			prefix = CoronaFwkUtil.getUidPrefix(eventType);
			String randomNum = getRandomNumString();
			sb.append(prefix).append(randomNum);
			uid = sb.toString();

		} catch (Exception e) {
			throw new Exception(e);
		}

		if (uid != null && !uid.equalsIgnoreCase(prefix)
				&& uid.length() < maxLength) {
			return uid;

		} else {
			throw new Exception(
					"get uid is null or the size of uid is over max length, uid="
					+ uid + ",size=" + maxLength);
		}

	}


	//This method will fetch the state and status of the server if its running or not.
	public static boolean isServerActiveAndRunning(String serverName)
	{
		MBeanHome mbeanhome = null;
		Object obj = null;
		try
		{
			Context context = JndiUtils.getInitialContext(Config.getJndiAdminUrl());
			mbeanhome = (MBeanHome)context.lookup("weblogic.management.adminhome");
			WebLogicObjectName weblogicobjectname = null;
			weblogicobjectname = new WebLogicObjectName(serverName, "ServerRuntime", mbeanhome.getDomainName(), serverName);
			ServerRuntimeMBean serverruntimembean = (ServerRuntimeMBean)mbeanhome.getMBean(weblogicobjectname);
			if(serverruntimembean.getHealthState().toString().indexOf("HEALTH_OK") != -1){
				//State is ok, checking for status
				if ((serverruntimembean.getState().toString()).equals("RUNNING")) {
					return true;
				}
			}else {
				return false;
			}
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
			return false;
		}
		return false;
	}


	/**
	 * based on the event, return the String.
	 * 
	 * @param type
	 * @return
	 */
	public static String getUidPrefix(String type) {
		String preFix = null;

		if (PROPAGATION_EVENT.equalsIgnoreCase(type)) {
			preFix = "PE";
		} else if (DATA_CHANGE_EVENT.equalsIgnoreCase(type)) {
			preFix = "DCE";
		} else if (PENDING_PROP_EVENTS.equalsIgnoreCase(type)) {
			preFix = "PPE";
		} else if (REPLICATIION_EVENTS.equalsIgnoreCase(type)) {
			preFix = "RE";
		} else {
			preFix="DEF";
		}

		return preFix;
	}

	public static String getRandomNumString() throws Exception {

		SecureRandom prng = SecureRandom.getInstance("SHA1PRNG");
		// generate a random number
		String randomNum = new Integer(prng.nextInt()).toString();

		return randomNum;
	}

	// Get the manage server name currently which have this event
	public static String getManageServerName() throws DataChangeException,
	Exception {
		InitialContext context = (InitialContext) JndiUtils.getInitialContext();
		Set mbeanSet = null;
		MBeanHome home = null;

		home = (MBeanHome) context.lookup(MBeanHome.LOCAL_JNDI_NAME);
		mbeanSet = home.getMBeansByType("ServerRuntime");
		Iterator mbeanIterator = mbeanSet.iterator();

		ServerRuntimeMBean serverBean = (ServerRuntimeMBean) mbeanIterator
		.next();
		String serverName = serverBean.getName();
		if (serverName == null || "".equals(serverName)) {
			serverName = "Localhost";
		}

		if (serverName.equals(Config.getcoronaManageServerGrp1Serv1()))
			serverName="ManageInstance1";
		else if (serverName.equals(Config.getcoronaManageServerGrp1Serv2()))
			serverName="ManageInstance2";
		else if (serverName.equals(Config.getcoronaManageServerGrp2Serv1()))
			serverName="ManageInstance3";
		else if (serverName.equals(Config.getcoronaManageServerGrp2Serv2()))
			serverName="ManageInstance4";

		return serverName;
	}

	public static String getActualMiName() {
		String serverName = null;
		try {
			InitialContext context = (InitialContext) JndiUtils.getInitialContext();
			Set mbeanSet = null;
			MBeanHome home = null;
	
			home = (MBeanHome) context.lookup(MBeanHome.LOCAL_JNDI_NAME);
			mbeanSet = home.getMBeansByType("ServerRuntime");
			Iterator mbeanIterator = mbeanSet.iterator();
	
			ServerRuntimeMBean serverBean = (ServerRuntimeMBean) mbeanIterator
			.next();
			serverName = serverBean.getName();
			if (serverName == null || "".equals(serverName)) {
				serverName = "Localhost";
			}
		}catch (Exception ex){
			ex.printStackTrace();
			serverName = "";
		}
		return serverName;
	}


	public static Object[] getAllParamsArray(Long id, Long trxId,
			List<String> allKeys, Map<String, String> processKeyMapVal)
	throws Exception {
		List<Object> allKeyVars = new ArrayList<Object>();

		allKeyVars.add(id); // ProcessId
		allKeyVars.add(trxId); // Src transactionId
		for (String key : allKeys) {
			allKeyVars.add(processKeyMapVal.get(key));
		}

		return allKeyVars.toArray();
	}

	public static Object[] getAllParamsArray(Long id)
	throws Exception {
		List<Object> allKeyVars = new ArrayList<Object>();

		allKeyVars.add(id); // ProcessId

		return allKeyVars.toArray();
	}
	public static String getProcessorMethodNameUsingCachedMap(String type,
			String processType) throws Exception {
		try {
			String fieldValue = null;

			if (DataChangeEventsFwkDao.getHmPTtoDaoMethodsMap().containsKey(
					processType.toUpperCase())) {
				fieldValue = DataChangeEventsFwkDao.getHmPTtoDaoMethodsMap()
				.get(processType.toUpperCase());
			} else {
				return null;
			}

			return type + "" + fieldValue;
		} catch (Exception ex) {
			String message="Process type does not matches the list available "+processType;
			CoronaErrorHandler.logError(ex, message, null);

			throw new Exception(message);
		}
	}

	public static String getProcessorMethodName(String type,
			String processType, Class cl) throws Exception {
		try {
			Field fieldName = cl.getField(processType.toUpperCase());
			String fieldValue = fieldName.get(cl.newInstance()) + "";

			return type + "" + fieldValue;
		} catch (Exception ex) {
			String message ="Process type does not matches the list available "+ processType;
			CoronaErrorHandler.logError(ex, message, null);
			throw new Exception(message);
		}
	}

	public static String getProcessorMethodName(String type, String methodName)
	throws Exception {
		try {
			return type + "" + methodName;
		} catch (Exception ex) {
			String message ="Exception while getProcessorMethodName "+type+" and methodname "+methodName;
			CoronaErrorHandler.logError(ex, message, null);
			throw new Exception(message);

		}
	}

//	//Remove if the new format is working.
//	public static Map<String, String> getFormatKeyValueMapOld(List<String> list,
//	String value) throws Exception {

//	Map<String, String> hm = new HashMap<String, String>();

//	StringTokenizer strTokenVal = new StringTokenizer(value, "^");

//	//Revisit
//	// if (strTokenVal.countTokens() != list.size()){
//	// return null;
//	// }

//	for (String key : list) {
//	String tempStr = null;
//	try {
//	tempStr = strTokenVal.nextToken();
//	} catch (Exception e) {
//	String message =" Element does not exists for key (check if its ok) key- "+key;
//	System.out.println(message);
//	Logger.debug(message);
//	}
//	hm.put(key, tempStr);
//	}

//	return hm;
//	}


	public static Map<String, String> getFormatKeyValueMap(List<String> list,
			String value) throws Exception {
		LoggerInfo logInfo = new LoggerInfo ("com.hp.psg.corona.dataload.util.CoronaFwkUtil");

		Map<String, String> hm = new HashMap<String, String>();

		String strArr[]=value.split("\\^");

		if (strArr.length != list.size()){
			//Logger.error(logInfo, "getFormatKeyValueMap",new Exception("Size of tokens available is not same as that of values."));
			Logger.debug(logInfo, "getFormatKeyValueMap","Size of tokens available is not same as that of values.");
		}

		int i =0;
		for (String key : list) {
			String tempStr = null;
			try {
				tempStr = ""+strArr[i];
				i++;
			} catch (Exception e) {
				String message =" Element does not exists for key (check if its ok) key- "+key;
				Logger.debug(logInfo, "getFormatKeyValueMap",message);
			}
			hm.put(key, tempStr);
		}

		return hm;
	}


	public static String initCap(String str) {
		if (str != null) {
			return str.substring(0, 1).toUpperCase() + str.substring(1);
		}
		return null;
	}

	public static List<String> getFormatKeyMethodCalls(String procesType)
	throws Exception {
		if (DataChangeEventsFwkDao.getSourceFormatKeyGetterMethodMap()
				.containsKey(procesType)) {
			return DataChangeEventsFwkDao.getSourceFormatKeyGetterMethodMap()
			.get(procesType);
		} else {
			throw new Exception("Key is not there in hashmap for eventFormatKeys "+procesType);
		}
	}

	public static List<String> getSourceFormatKeys(String procesType)
	throws Exception {
		LoggerInfo logInfo = new LoggerInfo ("com.hp.psg.corona.dataload.util.CoronaFwkUtil");
		if (DataChangeEventsFwkDao.getSourceFormatKeyMap().containsKey(
				procesType)) {

			return DataChangeEventsFwkDao.getSourceFormatKeyMap().get(
					procesType);
		} else {
			Logger.debug(logInfo, "getSourceFormatKeys","Key is not there in hashmap for eventFormatKeys for process Type "
					+ procesType);
			throw new Exception(
					"Source Format key not found for process type - "
					+ procesType);
		}
	}

	public static List<String> getRegionalAttributeDefaultValueAsList(
			String regionCode, String objectName, String attrName) {
		String key = regionCode + "^" + objectName + "^" + attrName;

		return DataChangeEventsFwkDao.getRegionalAttributeMaps().get(
				key.toUpperCase());
	}

	public static String getRegionalAttributeDefaultValueAsString(
			String regionCode, String objectName, String attrName) {
		String key = regionCode + "^" + objectName + "^" + attrName;

		return DataChangeEventsFwkDao.getRegionalAttributeMaps().get(
				key.toUpperCase()).get(0)
				+ "";
	}

	public static List<String> getListOfClasses(String source, String typeId) {

		LoggerInfo logInfo = new LoggerInfo ("com.hp.psg.corona.dataload.util.CoronaFwkUtil");
		if (DataChangeEventsFwkDao.getCtoPluginMap() == null
				|| DataChangeEventsFwkDao.getCtoPluginMap().size() == 0) {
			Logger.debug(logInfo, "getListOfClasses","Cached object for plugin class list is null, trying to load again. "+source +" type id "+typeId);
			CtoPluginHashMap.loadCtoCache();
		}

		if (DataChangeEventsFwkDao.getCtoPluginMap().containsKey(
				source.toUpperCase())) {
			for (HashMap<String, List<String>> innerHm : DataChangeEventsFwkDao
					.getCtoPluginMap().get(source.toUpperCase()))
				if (innerHm.containsKey(typeId.toUpperCase()))
					return innerHm.get(typeId.toUpperCase());
		} else {
			Logger.debug(logInfo, "getListOfClasses","################### Source key not found in cached map "
					+ source.toUpperCase()
					+ " and type Id "
					+ typeId.toUpperCase());
			return null;
		}

		return null;
	}

	public static List<String> getKeyFieldList(CoronaBaseObject cboObj) {

		LoggerInfo logInfo = new LoggerInfo ("com.hp.psg.corona.dataload.util.CoronaFwkUtil");

		if (DataChangeEventsFwkDao.getHmBeanKeyMethodListMap() == null
				|| DataChangeEventsFwkDao.getHmBeanKeyMethodListMap().size() == 0) {
			Logger.debug(logInfo, "getKeyFieldList","Cached object for plugin class list is null, trying to load again...(getKeyFieldList)");
			CtoPluginHashMap.loadBeanKeyFieldsMap();
		}

		if (DataChangeEventsFwkDao.getHmBeanKeyMethodListMap().containsKey(
				cboObj.getType())) {
			return DataChangeEventsFwkDao.getHmBeanKeyMethodListMap().get(
					cboObj.getType());
		} else {
			Logger.error(logInfo, "getKeyFieldList",new Exception("Error  Object Type " + cboObj.getType()
					+ " not found in the map list"));

			//Print atleast the id out and check why its not getting the list initialized to be printed for debug String.
			List <String> defaultList = new ArrayList<String>();
			defaultList.add("id");
			return defaultList;
		}
	}

	public static String getDaoMethodName(String processType) {

		LoggerInfo logInfo = new LoggerInfo ("com.hp.psg.corona.dataload.util.CoronaFwkUtil");
		if (DataChangeEventsFwkDao.getHmPTtoDaoMethodsMap() == null
				|| DataChangeEventsFwkDao.getHmPTtoDaoMethodsMap().size() == 0) {
			Logger.debug(logInfo, "getDaoMethodName","Cached object for Dao method names is null, trying to load again");
			CtoPluginHashMap.loadCtoCache();
		}

		if (DataChangeEventsFwkDao.getHmPTtoDaoMethodsMap().containsKey(
				processType.toUpperCase())) {
			return DataChangeEventsFwkDao.getHmPTtoDaoMethodsMap().get(
					processType.toUpperCase());
		} else {
			Logger.error(logInfo, "getDaoMethodName",new Exception("################### Source key not found in cached map "
					+ processType.toUpperCase()));
			return null;
		}
	}
	public static List<CtoTransformationUtilMap> getPtTransformationObject(
			String processType) {

		LoggerInfo logInfo = new LoggerInfo ("com.hp.psg.corona.dataload.util.CoronaFwkUtil");
		if (DataChangeEventsFwkDao.getHmTransoformationClassList() == null
				|| DataChangeEventsFwkDao.getHmTransoformationClassList()
				.size() == 0) {
			Logger.debug(logInfo, "getPtTransformationObject","Cached object for plugin transformation object is null, trying to load again");
			CtoPluginHashMap.loadCtoCache();
		}

		if (DataChangeEventsFwkDao.getHmTransoformationClassList().containsKey(
				processType.toUpperCase())) {
			return DataChangeEventsFwkDao.getHmTransoformationClassList().get(
					processType.toUpperCase());
		} else {
			Logger.error(logInfo, "getPtTransformationObject",new Exception("################### Source key not found in cached map for classes"
					+ processType.toUpperCase()));
			return null;
		}
	}

	public static FeaturePluginRequest getFeaturePluginRequestFromCTOBean(
			CTODaxDataBeanGeneral outputBean) {

		LoggerInfo logInfo = new LoggerInfo ("com.hp.psg.corona.dataload.util.CoronaFwkUtil");
		FeaturePluginRequest featurePluginRequest = CoronaObjectWrapperUtil
		.getEmptyPluginReqObject();
		try {
			if (outputBean != null) {
				if (outputBean.getPriceInfo() != null) {
					Logger.debug(logInfo, "getFeaturePluginRequestFromCTOBean","@@!@@ Got price info object");
					PriceInfo[] cbObj = outputBean.getPriceInfo();
					Logger.debug(logInfo, "getFeaturePluginRequestFromCTOBean","#####PriceInfo elements size: "
							+ cbObj.length);
					featurePluginRequest = CoronaObjectWrapperUtil
					.putToCoronaObjectList(featurePluginRequest,
							"PriceInfo", convertToList(cbObj));
				}
				if (outputBean.getConfigComponentInfo() != null) {
					Logger.debug(logInfo, "getFeaturePluginRequestFromCTOBean","@@!@@ Got config component info object");
					ConfigComponentInfo[] cbObj = outputBean
					.getConfigComponentInfo();
					Logger.debug(logInfo, "getFeaturePluginRequestFromCTOBean","#####ConfigComponent elements size: "
							+ cbObj.length);
					featurePluginRequest = CoronaObjectWrapperUtil
					.putToCoronaObjectList(featurePluginRequest,
							"ConfigComponentInfo", convertToList(cbObj));
				}
				if (outputBean.getConfigDescription() != null) {
					Logger.debug(logInfo, "getFeaturePluginRequestFromCTOBean","@@!@@ Got config description info object");
					ConfigDescription[] cbObj = outputBean.getConfigDescription();
					Logger.debug(logInfo, "getFeaturePluginRequestFromCTOBean","#####ConfigDescription elements size: "
							+ cbObj.length);
					featurePluginRequest = CoronaObjectWrapperUtil
					.putToCoronaObjectList(featurePluginRequest,
							"ConfigDescription", convertToList(cbObj));
				}
				if (outputBean.getConfigHeaderInfo() != null) {
					Logger.debug(logInfo, "getFeaturePluginRequestFromCTOBean","@@!@@ Got config header info object");
					ConfigHeaderInfo[] cbObj = outputBean.getConfigHeaderInfo();
					Logger.debug(logInfo, "getFeaturePluginRequestFromCTOBean","#####ConfigHeaderInfo elements size: "
							+ cbObj.length);
					featurePluginRequest = CoronaObjectWrapperUtil
					.putToCoronaObjectList(featurePluginRequest,
							"ConfigHeaderInfo", convertToList(cbObj));
				}
				if (outputBean.getConfigPermutationPriceInfo() != null) {
					Logger.debug(logInfo, "getFeaturePluginRequestFromCTOBean","@@!@@ Got config permutation price info object");
					ConfigPermutationPriceInfo[] cbObj = outputBean
					.getConfigPermutationPriceInfo();
					Logger.debug(logInfo, "getFeaturePluginRequestFromCTOBean","#####ConfigPermutationPriceInfo elements size: "
							+ cbObj.length);
					featurePluginRequest = CoronaObjectWrapperUtil
					.putToCoronaObjectList(featurePluginRequest,
							"ConfigPermutationPriceInfo",
							convertToList(cbObj));
				}
				if (outputBean.getConfigPermutationInfo() != null) {
					Logger.debug(logInfo, "getFeaturePluginRequestFromCTOBean","@@!@@ Got Config permutation info object");
					ConfigPermutationInfo[] cbObj = outputBean
					.getConfigPermutationInfo();
					Logger.debug(logInfo, "getFeaturePluginRequestFromCTOBean","#####ConfigPermutationInfo elements size: "
							+ cbObj.length);
					featurePluginRequest = CoronaObjectWrapperUtil
					.putToCoronaObjectList(featurePluginRequest,
							"ConfigPermutationInfo", convertToList(cbObj));
				}
				if (outputBean.getProductDescription() != null) {
					Logger.debug(logInfo, "getFeaturePluginRequestFromCTOBean","@@!@@ Got Product Description object");
					ProductDescription[] cbObj = outputBean.getProductDescription();
					Logger.debug(logInfo, "getFeaturePluginRequestFromCTOBean","#####ProductDescription elements size: "
							+ cbObj.length);
					featurePluginRequest = CoronaObjectWrapperUtil
					.putToCoronaObjectList(featurePluginRequest,
							"ProductDescription", convertToList(cbObj));
				}
				if (outputBean.getConfigPriceHeaderInfo() != null) {
					Logger.debug(logInfo, "getFeaturePluginRequestFromCTOBean","@@!@@ Got ConfigPriceHeaderInfo object");
					ConfigPriceHeaderInfo[] cbObj = outputBean
					.getConfigPriceHeaderInfo();
					Logger.debug(logInfo, "getFeaturePluginRequestFromCTOBean","#####ConfigPriceHeaderInfo elements size: "
							+ cbObj.length);
					featurePluginRequest = CoronaObjectWrapperUtil
					.putToCoronaObjectList(featurePluginRequest,
							"ConfigPriceHeaderInfo", convertToList(cbObj));
				}
			}
		}catch (Exception ex){
			CoronaErrorHandler.logError(ex, "Exception while converting CTODaxObject to featurePlugin request", null);
		}
		return featurePluginRequest;
	}

	public static CTODaxDataBeanGeneral getCTOBeanFromFeaturePluginResult(
			CTODaxDataBeanGeneral ctoOutputBean, FeaturePluginResult fprResult) {

		LoggerInfo logInfo = new LoggerInfo ("com.hp.psg.corona.dataload.util.CoronaFwkUtil");
		Map<String, List<? extends CoronaBaseObject>> coronaObjectsHolderMap = new HashMap<String, List<? extends CoronaBaseObject>>();
		try {
			Class ctoDaxDataGeneralBeanClass = Class
			.forName("com.hp.psg.corona.common.beans.CTODaxDataBeanGeneral");

			if (fprResult != null) {

				List<CoronaObjectWrapper> coronaWrapperObjList = fprResult
				.getFeatureBean().getHeaders();

				for (CoronaObjectWrapper coronaWrapperObj : coronaWrapperObjList) {
					Iterator<String> objNamesInMap = coronaWrapperObj
					.getAllCoronaObjName();

					if (objNamesInMap != null && objNamesInMap.hasNext()) {
						String str = (String) objNamesInMap.next();

						List<? extends CoronaBaseObject> list = coronaWrapperObj
						.GetCoronaObj(str);
						if (list != null) {
							for (CoronaBaseObject cob : list) {
								if (coronaObjectsHolderMap.containsKey(cob
										.getType())) {
									// Have a check if the object itself a Map ,
									// then need to call same method for adding
									// in to traverse itself.
									List<CoronaBaseObject> tmpList = new ArrayList<CoronaBaseObject>();
									tmpList.addAll(coronaObjectsHolderMap
											.get(cob.getType()));
									tmpList.add(cob);
									coronaObjectsHolderMap.put(cob.getType(),
											tmpList);
								} else {
									List<CoronaBaseObject> tmpList = new ArrayList<CoronaBaseObject>();
									tmpList.add(cob);
									coronaObjectsHolderMap.put(cob.getType(),
											tmpList);
								}
							}
						} else {
							Logger.debug(logInfo, "getCTOBeanFromFeaturePluginResult","List does not contain any element !!! for (in corona object wrapper list)"
									+ str);
						}
					}
				}

				CTODaxDataBeanGeneral ctoTestbean = (CTODaxDataBeanGeneral) ctoDaxDataGeneralBeanClass
				.newInstance();

				for (String strKeyBeanName : coronaObjectsHolderMap.keySet()) {

					List<? extends CoronaBaseObject> listCbo = coronaObjectsHolderMap
					.get(strKeyBeanName);
					if (listCbo != null && listCbo.size() > 0) {
						CoronaBaseObject cboObj = listCbo.get(0);

						List parList = new ArrayList();
						parList
						.add(Array
								.newInstance(
										Class
										.forName("com.hp.psg.corona.common.cto.beans."
												+ ((CoronaBaseObject) cboObj)
												.getType()),
												1));

						java.lang.reflect.Method mthd = ctoDaxDataGeneralBeanClass
						.getMethod(
								"set"
								+ ((CoronaBaseObject) cboObj)
								.getType(),
								new Class[]{parList.get(0).getClass()});

						Logger.info(logInfo, "getCTOBeanFromFeaturePluginResult"," ((((((((((( Method call ))))))))))) - "
								+ mthd);

						// Need to revisit this place to check the dynamic
						// casting using reflection
						if ("PriceInfo".equalsIgnoreCase(cboObj.getType()))
							mthd.invoke(ctoTestbean, new Object[]{listCbo
									.toArray(new PriceInfo[0])});
						if ("ConfigComponentInfo".equalsIgnoreCase(cboObj
								.getType()))
							mthd.invoke(ctoTestbean, new Object[]{listCbo
									.toArray(new ConfigComponentInfo[0])});
						if ("ConfigDescription".equalsIgnoreCase(cboObj
								.getType()))
							mthd.invoke(ctoTestbean, new Object[]{listCbo
									.toArray(new ConfigDescription[0])});
						if ("ConfigHeaderInfo".equalsIgnoreCase(cboObj
								.getType()))
							mthd.invoke(ctoTestbean, new Object[]{listCbo
									.toArray(new ConfigHeaderInfo[0])});
						if ("ConfigPermutationInfo".equalsIgnoreCase(cboObj
								.getType()))
							mthd.invoke(ctoTestbean, new Object[]{listCbo
									.toArray(new ConfigPermutationInfo[0])});
						if ("ProductDescription".equalsIgnoreCase(cboObj
								.getType()))
							mthd.invoke(ctoTestbean, new Object[]{listCbo
									.toArray(new ProductDescription[0])});
						if ("ConfigPriceHeaderInfo".equalsIgnoreCase(cboObj
								.getType()))
							mthd.invoke(ctoTestbean, new Object[]{listCbo
									.toArray(new ConfigPriceHeaderInfo[0])});

					}
				}
			} else
				Logger.error(logInfo, "getCTOBeanFromFeaturePluginResult",new Exception("Plugin result is itself wrong, feature plugin request is coming as null "));
		} catch (Exception ex) {
			String message ="Exception while converting featureplugin response to CTODataDaxGeneral bean";
			CoronaErrorHandler.logError(ex, message, null);
		}
		return ctoOutputBean;
	}
	public static List<CoronaBaseObject> convertToList(
			CoronaBaseObject[] cbgArray) {
		List<CoronaBaseObject> listRet = new ArrayList<CoronaBaseObject>();

		for (CoronaBaseObject cbg : cbgArray) {
			listRet.add(cbg);
		}
		return listRet;
	}

	public static String getType(
			Map<CoronaBaseObject, List<? extends CoronaBaseObject>> inputMap) {

		Set<CoronaBaseObject> cboKeySet = inputMap.keySet();

		for (CoronaBaseObject cbo : cboKeySet) {
			List<? extends CoronaBaseObject> cboList = inputMap.get(cbo);
			if (cboList != null && cboList.size() > 0) {
				return cboList.get(0).getType();
			}
		}

		return "";
	}

	public static String getTypeOnKeysMap(
			Map<String, List<? extends CoronaBaseObject>> inputMap) {

		Set<String> cboKeySet = inputMap.keySet();

		for (String cbo : cboKeySet) {
			List<? extends CoronaBaseObject> cboList = inputMap.get(cbo);
			if (cboList != null && cboList.size() > 0) {
				return cboList.get(0).getType();
			}
		}

		return "";
	}

	public static String getGpsyCntryCdFromPrsCntryCd(String prsCntryCd) {
		// Lookup GpsyCountryCode on PRS column

		CountryCodeBean cntryCodeBean = null;
		if (DataChangeEventsFwkDao.getHmCountryCodesMap() != null)
			cntryCodeBean = DataChangeEventsFwkDao.getHmCountryCodesMap().get(
					prsCntryCd);

		if (cntryCodeBean != null) {
			return cntryCodeBean.getGpsyCountry();
		}

		return null;
	}

	public static String getPrsCntryCdFromGpsyCntryCd(String gpsyCntryCd) {
		// Lookup PRSCountryCode on Gpsy Column
		CountryCodeBean cntryCodeBean = null;

		if (DataChangeEventsFwkDao.getHmCountryCodesMap() != null)
			cntryCodeBean = DataChangeEventsFwkDao.getHmCountryCodesMap().get(
					gpsyCntryCd);

		if (cntryCodeBean != null)
			return cntryCodeBean.getKdkgr();

		return null;
	}

	public static CountryCodeBean getCntryCdObjOnGpsyOrPrsKey(String key) {
		if (DataChangeEventsFwkDao.getHmCountryCodesMap() != null)
			return DataChangeEventsFwkDao.getHmCountryCodesMap().get(key);

		return null;
	}

	public static String getIsoCdFromWaersCode(String waersKey) {
		CurrencyBean currencyBean = null;
		if (DataChangeEventsFwkDao.getHmCurrencyMap() != null)
			currencyBean = DataChangeEventsFwkDao.getHmCurrencyMap().get(
					waersKey);

		if (currencyBean != null)
			return currencyBean.getIsocd();

		return null;
	}
	public static String getWaersCodeFromIsoCd(String isoCd) {

		CurrencyBean currencyBean = null;
		if (DataChangeEventsFwkDao.getHmCurrencyMap() != null)
			currencyBean = DataChangeEventsFwkDao.getHmCurrencyMap().get(isoCd);

		if (currencyBean != null)
			return currencyBean.getWaers();

		return null;
	}

	public static CurrencyBean getCurrencyCdObjOnIsoCdOrWaersKey(String key) {

		if (DataChangeEventsFwkDao.getHmCurrencyMap() != null)
			return DataChangeEventsFwkDao.getHmCurrencyMap().get(key);

		return null;
	}

	public static PriceTermBean getPriceTermObjOnPltypCdOrPriceTermCd(String key) {

		if (DataChangeEventsFwkDao.getHmPriceTermMap() != null)
			return DataChangeEventsFwkDao.getHmPriceTermMap().get(key);

		return null;
	}

	public static String getPltypCdFromPriceTermCd(String priceTerm) {

		PriceTermBean priceTermBean = null;

		if (DataChangeEventsFwkDao.getHmPriceTermMap() != null)
			priceTermBean = DataChangeEventsFwkDao.getHmPriceTermMap().get(
					priceTerm);

		if (priceTermBean != null)
			return priceTermBean.getPltyp();

		return null;
	}
	public static String getPriceTermCdFromPltypCd(String pltypCd) {
		PriceTermBean priceTermBean = null;
		if (DataChangeEventsFwkDao.getHmPriceTermMap() != null)
			priceTermBean = DataChangeEventsFwkDao.getHmPriceTermMap().get(
					pltypCd);

		if (priceTermBean != null)
			return priceTermBean.getPriceTerm();

		return null;
	}
	public static String getInco1FromPriceTermCd(String priceTerm) {
		PriceTermBean priceTermBean = null;
		if (DataChangeEventsFwkDao.getHmPriceTermMap() != null)
			priceTermBean = DataChangeEventsFwkDao.getHmPriceTermMap().get(
					priceTerm);

		if (priceTermBean != null)
			return priceTermBean.getInco1();

		return null;
	}
	public static String getPriceTermCdFromInco1Cd(String inco1Cd) {
		PriceTermBean priceTermBean = null;
		if (DataChangeEventsFwkDao.getHmPriceTermMap() != null)
			priceTermBean = DataChangeEventsFwkDao.getHmPriceTermMap().get(
					inco1Cd);

		if (priceTermBean != null)
			return priceTermBean.getPriceTerm();

		return null;
	}
	public static String getPlTypCdFromInco1Cd(String inco1Cd) {
		PriceTermBean priceTermBean = null;
		if (DataChangeEventsFwkDao.getHmPriceTermMap() != null)
			priceTermBean = DataChangeEventsFwkDao.getHmPriceTermMap().get(
					inco1Cd);

		if (priceTermBean != null)
			return priceTermBean.getPltyp();

		return null;
	}

	public static String toDebugString(CoronaBaseObject cbo) {

		StringBuffer sb = new StringBuffer("[");
		boolean retVal = false;
		List<String> list = getKeyFieldList(cbo);

		try {
			if (list != null) {
				for (String fieldName : list) {
					String getterMethodName = CoronaFwkUtil
					.getProcessorMethodName("get", CoronaFwkUtil
							.initCap(fieldName));
					java.lang.reflect.Method parentMethod = (cbo.getClass())
					.getMethod(getterMethodName);

					Object value = (parentMethod.invoke(cbo));

					sb.append("\t\t"+fieldName + " = " + ((value != null)? value.toString(): "null") + "\n");
				}
			} else {
				System.out.println("list cache for bean name is coming as null");
				sb.append(" Key is not available in XML for " + cbo.getType()
						+ "");
			}
		} catch (Exception e) {
			CoronaErrorHandler.logError(e, null, null);
		}

		sb.append("]");
		return sb.toString();
	}
	public static boolean isEqual(CoronaBaseObject tempobj1,
			CoronaBaseObject tempobj2) {
		boolean retVal = false;
		try {
			List<String> list = getKeyFieldList(tempobj1);

			String str1 = getValOnKeys(list, tempobj1);
			String str2 = getValOnKeys(list, tempobj2);

			if (!"[]".equals(str1) && !"[]".equals(str2) && str1.equals(str2))
				return true;
			else
				return false;

		} catch (Exception e) {
			CoronaErrorHandler.logError(e, null, null);
		}

		return retVal;
	}

	public static String getValOnKeys(List<String> list, CoronaBaseObject cbo) {
		StringBuffer methodCallValue = new StringBuffer("[");
		try {
			for (String fieldName : list) {
				String getterMethodName = CoronaFwkUtil.getProcessorMethodName(
						"get", CoronaFwkUtil.initCap(fieldName));
				java.lang.reflect.Method parentMethod = (cbo.getClass())
				.getMethod(getterMethodName);
				String key = (String) parentMethod.invoke(cbo);
				methodCallValue.append(key + "|");
			}
			methodCallValue.append("]");
		} catch (Exception ex) {
			CoronaErrorHandler.logError(ex, null, null);
		}
		return methodCallValue.toString();
	}

	public static void println(String msg) {
		System.out.println(msg);
	}



	public static void prettyPrint(Document document) {
		// Pretty print the document to System.out
		try {
			OutputFormat format = OutputFormat.createPrettyPrint();
			XMLWriter writer = new XMLWriter( System.out, format );
			writer.write( document );
		}
		catch (Exception e) {
			CoronaErrorHandler.logError(e, null, null);
		}
	}

	public static String xmlToString (Document document) {
		String returnStr=null;
		// Pretty print the document to System.out
		try {
			StringBuffer sb = new StringBuffer ();
			ByteArrayOutputStream out = new ByteArrayOutputStream();
			OutputFormat format = OutputFormat.createPrettyPrint();
			XMLWriter writer = new XMLWriter( out, format );
			writer.write( document );
			returnStr=out.toString();

		}
		catch (Exception e) {
			CoronaErrorHandler.logError(e, null, null);
		}
		return returnStr;

	}

	public static List<String> getListFromTokens(StringTokenizer strToken){
		List <String> list = new ArrayList<String>();
		while(strToken.hasMoreTokens()){
			String strTokenVal= strToken.nextToken();

			if (strTokenVal == null)
				strTokenVal="null";

			list.add(""+strTokenVal);
		}

		return list;
	}

	public static Map<String, Object> formatTranformInitMapObjects(Map<String, Map<String, String>> inputMap){
		Map<String, Object> initArgsMap = new HashMap<String, Object>();

		LoggerInfo logInfo = new LoggerInfo ("com.hp.psg.corona.dataload.util.CoronaFwkUtil");

		for (String strKey : inputMap.keySet()){
			Map<String, String>typeValueObjMap = inputMap.get(strKey); 

			if (typeValueObjMap != null && typeValueObjMap.size()> 0){

				String value = typeValueObjMap.get("value");
				String type = typeValueObjMap.get("type");

				if (type != null ){
					if ("Class".equals(type)){
						//Will execute the class to return the object and will store it directly in hashmap. !!!!
						try {
							Class className = Class.forName(value);
							IBeanPropagationUtil transformationUtilObj = (IBeanPropagationUtil) className.newInstance();
							Object obj = transformationUtilObj.getConstructArgObject();

							initArgsMap.put(strKey, obj);

						}catch (Exception ex){
							CoronaErrorHandler.logError(ex, "Exception while creating consctructor argument for class "+value, null);
						}

					}else if ("Boolean".equals(type)){
						initArgsMap.put(strKey, new Boolean(value));

					}else if ("Date".equals(type)){
						try {
							//Assuming the format coming is with offset, for now testing purpose, i will just take the util date and will pass it on.
							SimpleDateFormat outputFormat = new SimpleDateFormat ("MM/dd/yyyy");
							initArgsMap.put(strKey, outputFormat.parse(value));
						}catch(Exception ex){
							CoronaErrorHandler.logError(ex, "Exception while parsing date in format (MM/dd/yyyy) "+value, null);
						}
					}else if ("Integer".equals(type)){
						try{
							Integer integerValue = new Integer(value);
							initArgsMap.put(strKey, integerValue);
						}catch (Exception ex){
							CoronaErrorHandler.logError(ex, "Exception while passing integer object in constructor argument", null);
						}
					}
					else {
						//Else assuming the value is in string format.
						initArgsMap.put(strKey, value);
					}
				}else {
					Logger.info(logInfo,"formatTranformInitMapObjects","Type is not mentioned for object "+strKey+" assuming it to be String" );
					initArgsMap.put(strKey,value);
				}

			}
		}

		return initArgsMap;

	}

	//ExecuteOrNotExecute 
	public static String executionLockedBy(String timerInstanceName){
		try {
			String clusterAddress = Config.getJndiClusterUrl();
			Context ctx = JndiUtils.getInitialContext(clusterAddress);

			//Also check if the instance is active or not.
			String lockedByServer = (String)ctx.lookup(timerInstanceName);
			
			if (lockedByServer != null){
				if (Config.doCheckForServerStateAndStatusForTimer()) {
					if (CoronaFwkUtil.isServerActiveAndRunning(lockedByServer))
						return lockedByServer;
					else {
						//Server is in false state locked. unbind the jndiname and return nothing.
						ctx.unbind(timerInstanceName);
						return "";
					}
				}else {
					return lockedByServer;
				}
			}else{
				//execution can proceed with new timer.
				return "";
			}

		}catch (Exception ex){
			//ex.printStackTrace();
		}
		
		//just execute in case of exception
		return "";
	}

	public static void lockTimerByName(String timerInstanceName){
		try{
			String clusterAddress = Config.getJndiClusterUrl();
			Context ctx = JndiUtils.getInitialContext(clusterAddress);
			String serverName = Config.getCurrentServerName();

			ctx.bind(timerInstanceName, serverName);
			
		}catch (Exception ex){
			ex.printStackTrace();
		}
	}
	
	public static void releaseLockForTimerByName(String timerInstanceName){

		String serverName = Config.getCurrentServerName();
		String lockedByServer = null;
		try{
			String clusterAddress = Config.getJndiClusterUrl();
			Context ctx = JndiUtils.getInitialContext(clusterAddress);
			lockedByServer = (String)ctx.lookup(timerInstanceName);

			if (serverName !=null && serverName.equals(lockedByServer)){
				ctx.unbind(timerInstanceName);
			}
		}catch (Exception ex){
			ex.printStackTrace();
		}
	}
	
}
