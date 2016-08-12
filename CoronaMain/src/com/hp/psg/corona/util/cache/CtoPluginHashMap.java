package com.hp.psg.corona.util.cache;

import java.io.File;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.w3c.dom.Document;
import org.xml.sax.HandlerBase;

import com.hp.psg.common.util.logging.LoggerInfo;
import com.hp.psg.corona.common.util.BeanXmlUtil;
import com.hp.psg.corona.common.util.Config;
import com.hp.psg.corona.common.util.Logger;
import com.hp.psg.corona.datachange.dao.DataChangeEventsFwkDao;


/**
 * @author dudeja
 * @version 1.0
 * 
 * This class will cache the XML map for process types and dataload mappings. 
 */
public class CtoPluginHashMap extends HandlerBase {

	public CtoPluginHashMap() {
	}

	public static void loadCtoCache() {

		LoggerInfo logInfo = new LoggerInfo ("com.hp.psg.corona.util.cache.CtoPluginHashMap");
		String coronaPluginConfig = Config.getConfigDir()
				+ "CoronaPluginConfig.xml";
		File pluginConfigFile = new File(coronaPluginConfig);

		if (pluginConfigFile.exists()) {
			BeanXmlUtil xmlUtilObj = new BeanXmlUtil();
			Document configDoc = xmlUtilObj.parseXmlFile(coronaPluginConfig);

			if (configDoc != null) {

				Map<String, List<HashMap<String, List<String>>>> hmPluginClasses = null;
				Map<String, String> hmPTtoDaoMethodsMap = null;
				Map<String, List<CtoTransformationUtilMap>> hmTransoformationClassList = null;
				Map<String, String> hmPTtoPluginTypeMap = null;

				hmPluginClasses = xmlUtilObj
						.parseDocumentForFeaturePluginClasses(configDoc);
				hmPTtoDaoMethodsMap = xmlUtilObj
						.parseDocumentForPtToDaoMethodMap(configDoc);
				hmTransoformationClassList = xmlUtilObj
						.parseDocumentForPtToTransformUtilClass(configDoc);
				hmPTtoPluginTypeMap = xmlUtilObj
						.parseDocumentForPtToPluginTypeMap(configDoc);
				// Parsing done.. for all maps from CTOPlugin XML .
				
				if (hmPluginClasses != null)
					Logger.info(logInfo, "loadCtoCache","@@@@@@@@@ Cached hmPluginClasses map "+hmPluginClasses.size());
				
				if (hmPTtoDaoMethodsMap != null)
					Logger.info(logInfo, "loadCtoCache","@@@@@@@@@ Cached hmPTtoDaoMethodsMap map "+hmPTtoDaoMethodsMap.size());

				if (hmTransoformationClassList != null)
					Logger.info(logInfo, "loadCtoCache","@@@@@@@@@ Cached hmTransoformationClassList map "+hmTransoformationClassList.size());
				
				// Setting in DataFrameworkDao
				DataChangeEventsFwkDao.setCtoPluginMap(hmPluginClasses);
				DataChangeEventsFwkDao
						.setHmPTtoDaoMethodsMap(hmPTtoDaoMethodsMap);
				DataChangeEventsFwkDao
						.setHmTransoformationClassList(hmTransoformationClassList);
				DataChangeEventsFwkDao
						.setHmPTtoPluginTypeMap(hmPTtoPluginTypeMap);

			}
		} else {
			Logger.error(logInfo, "loadCtoCache",new Exception("File does not exist to be loaded for cache maps "
							+ pluginConfigFile.getName()));
		}
	}

	public static void loadBeanKeyFieldsMap() {
		
		LoggerInfo logInfo = new LoggerInfo ("com.hp.psg.corona.util.cache.CtoPluginHashMap");
		String beanKeyFieldsConfig = Config.getConfigDir()
				+ "CoronaBeanConfig.xml";
		File beanConfigFile = new File(beanKeyFieldsConfig);
		Map<String, List<String>> hmBeanKeyMethodListMap = null;
				
		if (beanConfigFile.exists()) {
			BeanXmlUtil xmlUtilObj = new BeanXmlUtil();
			Document configDoc = xmlUtilObj.parseXmlFile(beanKeyFieldsConfig);

			if (configDoc != null) {
				
				hmBeanKeyMethodListMap = xmlUtilObj
						.parseDocumentForBeanKeyFields(configDoc);
				// Parsing done.. for all maps from CTOPlugin XML .
				Logger.info(logInfo, "loadBeanKeyFieldsMap","@@@@@@@@@ Cached hmBeanKeyMethodListMap map "+ 
						hmBeanKeyMethodListMap.size());

				// Setting in DataFrameworkDao
				DataChangeEventsFwkDao
						.setHmBeanKeyMethodListMap(hmBeanKeyMethodListMap);

			}
		} else {
			Logger.error(logInfo, "loadBeanKeyFieldsMap",new Exception("File does not exist to be loaded for cache maps "
							+ beanConfigFile.getName()));
		}
	}

	private static void printHashMap(
			Map<String, List<HashMap<String, List<String>>>> hm) {
		
		LoggerInfo logInfo = new LoggerInfo ("com.hp.psg.corona.util.cache.CtoPluginHashMap");
		Logger.info(logInfo, "printHashMap","Size of hashmap " + hm.size());
		if (hm != null && hm.size() > 0) {
			Iterator<String> strKeyItr = hm.keySet().iterator();
			while (strKeyItr.hasNext())
				Logger.info(logInfo, "printHashMap","@ key " + strKeyItr.next());
		}
	}
}
