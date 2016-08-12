package com.hp.psg.corona.common.handler;

import com.hp.psg.corona.datachange.dao.DataChangeEventsFwkDao;
import com.hp.psg.corona.propagation.dao.PropagationEventsFwkDao;
import com.hp.psg.corona.replication.dao.ReplicationDao;
import com.hp.psg.corona.util.cache.CtoPluginHashMap;

/*
* @author dudeja
* @version 1.0
*
*/
public class CachingModulesCallHandler {

	public static void cacheFormatKeys() throws Exception {
		DataChangeEventsFwkDao.cacheFormatKeys();
	}

	public static void cacheEventMappingRules() throws Exception {
		DataChangeEventsFwkDao.cacheEventMappingRules();
	}

	public static void cacheRegionalAttributeDefaults() throws Exception {
		DataChangeEventsFwkDao.cacheRegionalAttributeDefaults();
	}

	public static void cacheDataChangeCallHandlerMethodMap() throws Exception {
		DataChangeEventsFwkDao.cacheDataChangeCallHandlerMethodMap();
	}

	public static void cachePropagationCallHandlerMethodMap() throws Exception {
		PropagationEventsFwkDao.cachePropagationCallHandlerMethodMap();
	}

	public static void cacheCtoPluginMap() throws Exception {
		CtoPluginHashMap.loadCtoCache();
	}
	
	public static void cacheKeyBeanMap() throws Exception {
		CtoPluginHashMap.loadBeanKeyFieldsMap();
	}

	public static void cacheCountryCodesValues() throws Exception {
		DataChangeEventsFwkDao.cacheCountryCodesValues();
	}

	public static void cacheCurrencyValues() throws Exception {
		DataChangeEventsFwkDao.cacheCurrencyValues();
	}

	public static void cachePriceTermValues() throws Exception {
		DataChangeEventsFwkDao.cachePriceTermValues();
	}
	
	public static void cacheModuleMap() throws Exception {
		DataChangeEventsFwkDao.cacheModuleMap();
	}
	
	//Replication info objects to be cached.
	public static void cacheReplicationInfo() throws Exception {
		ReplicationDao.cacheReplicationInfoMap();
	}
}
