package com.hp.psg.corona.datachange.dao;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.hp.psg.common.db.dax.DaxDB;
import com.hp.psg.common.db.dax.DaxDataBeanGeneral;
import com.hp.psg.common.db.dax.DaxMgr;
import com.hp.psg.common.db.dax.DaxParsedStmt;
import com.hp.psg.common.error.CoronaException;
import com.hp.psg.common.error.util.CoronaErrorConstants;
import com.hp.psg.common.util.logging.LoggerInfo;
import com.hp.psg.corona.common.beans.CountryCodeBean;
import com.hp.psg.corona.common.beans.CurrencyBean;
import com.hp.psg.corona.common.beans.DataChangeEvent;
import com.hp.psg.corona.common.beans.EventKeyFormatBean;
import com.hp.psg.corona.common.beans.EventMappingRuleBean;
import com.hp.psg.corona.common.beans.ModuleControlBean;
import com.hp.psg.corona.common.beans.PriceTermBean;
import com.hp.psg.corona.common.beans.RegionalAttributeDefaults;
import com.hp.psg.corona.common.constants.CoronaFwkConstants;
import com.hp.psg.corona.common.util.CoronaFwkUtil;
import com.hp.psg.corona.common.util.Logger;
import com.hp.psg.corona.error.util.CoronaErrorHandler;
import com.hp.psg.corona.util.cache.CtoTransformationUtilMap;

/**
 * @author dudeja
 * @version 1.0
 */
public class DataChangeEventsFwkDao {

	private static LoggerInfo logInfo = new LoggerInfo(
	"com.hp.psg.corona.dataload.dao.DataChangeEventsDao");

	private static Map<String, List<String>> sourceFormatKeyGetterMethodMap = new HashMap<String, List<String>>();
	private static Map<String, List<String>> sourceFormatKeySetterMethodMap = new HashMap<String, List<String>>();
	private static Map<String, List<String[]>> eventMappingRuleMap = new HashMap<String, List<String[]>>(); 
	private static Map<String, List<String>> sourceFormatKeyMap = new HashMap<String, List<String>>();
	private static Map<String, List<HashMap<String, List<String>>>> ctoPluginMap;
	private static Map<String, String> hmPTtoDaoMethodsMap;
	private static Map<String, String> hmPTtoPluginTypeMap;
	private static Map<String, CountryCodeBean> hmCountryCodesMap;
	private static Map<String, PriceTermBean> hmPriceTermMap;
	private static Map<String, CurrencyBean> hmCurrencyMap;
	private static Map<String, List<String>> hmBeanKeyMethodListMap= new HashMap<String, List<String>>();
	private static Map<String, String> hmModuleMap;

	private static Map<String, List<CtoTransformationUtilMap>> hmTransoformationClassList;

	private static Map<String, List<String>> regionalAttributeMaps = new HashMap<String, List<String>>();
	private static Map<String, List<String>> dataChangeCallHandlerMethodMap = new HashMap<String, List<String>>();

	public static final String DATA_CHANGE_FWK_GROUP = "DATA_CHANGE_FWK_GROUP";
	public static final String CORONA_FWK_UTIL_GROUP = "CORONA_FWK_UTIL_GROUP";

	private static String GET_REGIONAL_DEF_ATTR = "GET_REGIONAL_DEF_ATTR";
	public static final String GET_ALL_FORMAT_KEYS = "GET_ALL_FORMAT_KEYS";
	public static final String GET_EVENT_MAPPING_SQL = "GET_EVENT_MAPPING_SQL";
	public static final String GET_COUNTRY_CODES_VALUES = "GET_COUNTRY_CODES_VALUES";
	public static final String GET_PRICE_TERM_VALUES = "GET_PRICE_TERM_VALUES";
	public static final String GET_CURRENCY_VALUES = "GET_CURRENCY_VALUES";
	public static final String GET_MODULE_ACCESS_DATA = "GET_MODULE_ACCESS_DATA";
	public static final String GET_DATA_CHANGE_EVENTS_BY_STRING="GET_DATA_CHANGE_EVENTS_BY_STRING";

	private static String PICK_THIS_EVENT_ON_STATUS_AND_ID="PICK_THIS_EVENT_ON_STATUS_AND_ID";
	private static String GET_DATA_CHANGE_EVENT_BY_ID="GET_DATA_CHANGE_EVENT_BY_ID";
	private static String GET_DATA_CHANGE_EVENT_QUEUED_COUNTS="GET_DATA_CHANGE_EVENT_QUEUED_COUNTS";
	public static final String DATA_CHANGE_EVENT_READY_TO_WAIT = "DATA_CHANGE_EVENT_READY_TO_WAIT";
	public static final String DATA_CHANGE_EVENT_READY_TO_ERROR_WAIT = "DATA_CHANGE_EVENT_READY_TO_ERROR_WAIT";
	public static final String DATA_CHANGE_EVENT_ERRWAIT_TO_READY = "DATA_CHANGE_EVENT_ERRWAIT_TO_READY";
	public static final String DATA_CHANGE_EVENT_WAIT_TO_READY = "DATA_CHANGE_EVENT_WAIT_TO_READY";
	public static final String DATA_CHANGE_EVENT_WAIT_TO_ERRWAIT = "DATA_CHANGE_EVENT_WAIT_TO_ERRWAIT";
	public static final String GET_DATA_CHANGE_EVENTS_BY_STATUS = "GET_DATA_CHANGE_EVENTS_BY_STATUS";
	public static final String GET_DATA_CHANGE_EVENTS_BY_STATUS_W_ID = "GET_DATA_CHANGE_EVENTS_BY_STATUS_W_ID";

	private static String UPDATE_DATA_CHANGE_EVENT_STATUS_BY_ID = "UPDATE_DATA_CHANGE_EVENT_STATUS_BY_ID";
	private static String UPDATE_DATA_CHANGE_EVENT_STATUS_AND_COMMENT_BY_ID = "UPDATE_DATA_CHANGE_EVENT_STATUS_AND_COMMENT_BY_ID";
	private static String UPDATE_DATA_CHANGE_EVENT_IN_PROGRESS_AND_COMMENT_BY_ID = "UPDATE_DATA_CHANGE_EVENT_IN_PROGRESS_AND_COMMENT_BY_ID";
	private static String UPDATE_DATA_CHANGE_EVENT_COMPLETED_AND_COMMENT_BY_ID = "UPDATE_DATA_CHANGE_EVENT_COMPLETED_AND_COMMENT_BY_ID";
	private static String UPDATE_DATA_CHANGE_EVENT_COMPLETED_BY_ID = "UPDATE_DATA_CHANGE_EVENT_COMPLETED_BY_ID";
	private static String UPDATE_DATA_CHANGE_EVENT_IN_PROGRESS_BY_ID = "UPDATE_DATA_CHANGE_EVENT_IN_PROGRESS_BY_ID";
	private static String UPDATE_DATA_CHANGE_EVENT_ERROR_AND_MSG_BY_ID="UPDATE_DATA_CHANGE_EVENT_ERROR_AND_MSG_BY_ID";


	// Caching methods

	public static void cacheFormatKeys() throws CoronaException {
		try {
			EventKeyFormatBean formatKeyBean = new EventKeyFormatBean();
			List<EventKeyFormatBean> keyFormatList = null;

			DaxMgr dxMgr = DaxMgr.getInstance();
			DaxParsedStmt dxPstmt = dxMgr.makeParsedStmt(CORONA_FWK_UTIL_GROUP,
					GET_ALL_FORMAT_KEYS, formatKeyBean, null, null);
			DaxDB dxDb = dxMgr.getDaxDB();
			keyFormatList = dxDb.doSelect(dxPstmt, EventKeyFormatBean.class,
					formatKeyBean);

			for (EventKeyFormatBean sb : keyFormatList) {
				String key = sb.getProcessType();
				String value = sb.getProcessKeyFormat();

				sourceFormatKeyGetterMethodMap.put(key, sb
						.getGetMethodCallsForKeys());
				sourceFormatKeySetterMethodMap.put(key, sb
						.getSetMethodCallsForKeys());
				sourceFormatKeyMap.put(key, sb.getKeyList());
			}
			if (sourceFormatKeyMap != null)
				Logger.info(logInfo, "cacheFormatKeys","@@@@@@@@@ Cached SourceFormatKeys "+sourceFormatKeyMap.size());
		}catch (Exception ex){
			CoronaErrorHandler.logError(ex, "Exception while caching format keys ", null);
		}
	}

	public static void cacheDataChangeCallHandlerMethodMap()
	throws CoronaException, Exception {
		try {
			Class clDao = Class
			.forName("com.hp.psg.corona.datachange.handler.DataChangeCallHandler");

			Method[] mAllMethods = clDao.getDeclaredMethods();
			for (Method m : mAllMethods) {
				List<String> parmList = new ArrayList<String>();
				for (Class cl : m.getParameterTypes()) {
					String paramType = cl.getName();
					if (paramType.startsWith("[L")) {
						String str = paramType.substring(2, paramType.length() - 1);
						str = str.substring(str.lastIndexOf(".") + 1);
						parmList.add(str);
					} else {
						String str = paramType
						.substring(paramType.lastIndexOf(".") + 1);
						parmList.add(str);
					}
				}

				dataChangeCallHandlerMethodMap.put(m.getName().trim(), parmList);
			}
			if (dataChangeCallHandlerMethodMap != null)
				Logger.info(logInfo,"cacheDataChangeCallHandlerMethodMap","@@@@@@@@@ Cached DataChangeCallHandler map "+dataChangeCallHandlerMethodMap.size());
		}catch (Exception ex){
			CoronaErrorHandler.logError(ex, "Exception while caching DataChangeCallHandlerMap", null);
		}
	}

	public static void cacheRegionalAttributeDefaults() throws CoronaException {
		try {
			DaxMgr dxMgr = DaxMgr.getInstance();
			RegionalAttributeDefaults dataBean = new RegionalAttributeDefaults();
			List<RegionalAttributeDefaults> dataList = null;

			DaxParsedStmt dxPstmt = dxMgr.makeParsedStmt(CORONA_FWK_UTIL_GROUP,
					GET_REGIONAL_DEF_ATTR, dataBean, null, null);
			DaxDB dxDb = dxMgr.getDaxDB();
			dataList = dxDb.doSelect(dxPstmt, RegionalAttributeDefaults.class,
					dataBean);


			for (RegionalAttributeDefaults rad : dataList) {

				String value = rad.getRadAttributeValue();
				String key = rad.getRadRegionCode() + "^" + rad.getRadObjectName()
				+ "^" + rad.getRadAttributeName();

				if (regionalAttributeMaps.containsKey(key.toUpperCase())) {
					List<String> tempList = regionalAttributeMaps.get(key.toUpperCase());
					tempList.add(value);
					regionalAttributeMaps.put(key.toUpperCase(), tempList);
				} else {
					List<String> keyListValue = new ArrayList<String>();
					keyListValue.add(value);
					regionalAttributeMaps.put(key.toUpperCase(), keyListValue);
				}
			}
			if (regionalAttributeMaps != null)
				Logger.info(logInfo,"cacheRegionalAttributeDefaults","@@@@@@@@@ Cached RegionalDefaultAttributes map "+regionalAttributeMaps.size());
		}catch (Exception ex){
			CoronaErrorHandler.logError(ex, "Exception while caching Regional Attribute default map", null);
		}
	}

	public static Map<String, CountryCodeBean> cacheCountryCodesValues() throws CoronaException {
		try {
			DaxMgr dxMgr = DaxMgr.getInstance();
			CountryCodeBean dataBean = new CountryCodeBean();
			List<CountryCodeBean> dataList = null;
			hmCountryCodesMap = new HashMap<String, CountryCodeBean>();

			DaxParsedStmt dxPstmt = dxMgr.makeParsedStmt(CORONA_FWK_UTIL_GROUP,
					GET_COUNTRY_CODES_VALUES, dataBean, null, null);
			DaxDB dxDb = dxMgr.getDaxDB();
			dataList = dxDb.doSelect(dxPstmt, CountryCodeBean.class, dataBean);

			// Putting in values for both gpsy and prs keys
			for (CountryCodeBean cc : dataList) {
				String key = cc.getGpsyCountry();
				hmCountryCodesMap.put(key.toUpperCase(), cc);
				key = cc.getKdkgr();
				hmCountryCodesMap.put(key.toUpperCase(), cc);
			}
			if (hmCountryCodesMap != null)
				Logger.info(logInfo,"cacheCountryCodesValues","@@@@@@@@@ Cached CountryCode map attributes map "+hmCountryCodesMap.size());
		}catch (Exception ex){
			CoronaErrorHandler.logError(ex, "Exception while caching Country code values", null);
		}

		return hmCountryCodesMap;
	}

	public static void cachePriceTermValues() throws CoronaException {
		try {
			DaxMgr dxMgr = DaxMgr.getInstance();
			PriceTermBean dataBean = new PriceTermBean();
			List<PriceTermBean> dataList = null;

			hmPriceTermMap = new HashMap<String, PriceTermBean>();

			DaxParsedStmt dxPstmt = dxMgr.makeParsedStmt(CORONA_FWK_UTIL_GROUP,
					GET_PRICE_TERM_VALUES, dataBean, null, null);
			DaxDB dxDb = dxMgr.getDaxDB();
			dataList = dxDb.doSelect(dxPstmt, PriceTermBean.class, dataBean);

			for (PriceTermBean pt : dataList) {
				String key = pt.getPltyp();
				hmPriceTermMap.put(key, pt);
				key = pt.getPriceTerm();
				hmPriceTermMap.put(key, pt);
				key = pt.getInco1();
				hmPriceTermMap.put(key, pt);
			}
			if (hmPriceTermMap != null)		
				Logger.info(logInfo,"cachePriceTermValues","@@@@@@@@@ Cached PriceTerm map "+hmPriceTermMap.size());
		}catch(Exception ex){
			CoronaErrorHandler.logError(ex, "Exception while caching Price Term Values" , null);
		}
	}

	public static void cacheCurrencyValues() throws CoronaException {
		try {
			DaxMgr dxMgr = DaxMgr.getInstance();
			CurrencyBean dataBean = new CurrencyBean();
			List<CurrencyBean> dataList = null;

			hmCurrencyMap = new HashMap<String, CurrencyBean>();

			DaxParsedStmt dxPstmt = dxMgr.makeParsedStmt(CORONA_FWK_UTIL_GROUP,
					GET_CURRENCY_VALUES, dataBean, null, null);
			DaxDB dxDb = dxMgr.getDaxDB();
			dataList = dxDb.doSelect(dxPstmt, CurrencyBean.class, dataBean);

			for (CurrencyBean cb : dataList) {
				String key = cb.getIsocd();
				hmCurrencyMap.put(key, cb);
				key = cb.getWaers();
				hmCurrencyMap.put(key, cb);
			}
			if (hmCurrencyMap != null)
				Logger.info(logInfo,"cacheCurrencyValues","@@@@@@@@@ Cached Currencies map "+hmCurrencyMap.size());
		}catch (Exception ex){
			CoronaErrorHandler.logError(ex, "Exception while caching currency values map", null);
		}
	}

	public static void cacheModuleMap() throws CoronaException {
		try {
			DaxMgr dxMgr = DaxMgr.getInstance();
			ModuleControlBean moduleControlBean = new ModuleControlBean();
			List<ModuleControlBean> moduleControlList = null;

			DaxParsedStmt dxPstmt = dxMgr.makeParsedStmt(CORONA_FWK_UTIL_GROUP,
					GET_MODULE_ACCESS_DATA, moduleControlBean, null, null);
			DaxDB dxDb = dxMgr.getDaxDB();
			moduleControlList = dxDb.doSelect(dxPstmt, ModuleControlBean.class,
					moduleControlBean);

			hmModuleMap = new HashMap<String, String>();

			// Putting in values for both gpsy and prs keys
			for (ModuleControlBean cc : moduleControlList) {
				String key = cc.getModuleId();
				String expression = cc.getModuleExpression();
				hmModuleMap.put(key.toUpperCase(), expression);
			}

			if (hmModuleMap != null)
				Logger.info(logInfo,"cacheModuleMap","@@@@@@@@@ Cached Modules map "+hmModuleMap.size());
		}catch(Exception ex){
			CoronaErrorHandler.logError(ex, "Exception while loading Module map", null);
		}

	}

	public static void cacheEventMappingRules() throws CoronaException {
		try {
			DaxMgr dxMgr = DaxMgr.getInstance();
			EventMappingRuleBean dataBean = new EventMappingRuleBean();
			List<EventMappingRuleBean> dataList = null;

			DaxParsedStmt dxPstmt = dxMgr.makeParsedStmt(CORONA_FWK_UTIL_GROUP,
					GET_EVENT_MAPPING_SQL, dataBean, null, null);
			DaxDB dxDb = dxMgr.getDaxDB();
			dataList = dxDb.doSelect(dxPstmt, EventMappingRuleBean.class, dataBean);

			for (EventMappingRuleBean sb : dataList) {
				String key = sb.getInProcessType();

				// Value , first element will be the mapping sql and second element
				// is the out process type.
				String mappingSQL = sb.getMappingSql();
				String outProcessType = sb.getOutProcessType();

				if (eventMappingRuleMap.containsKey(key.toUpperCase()))
					eventMappingRuleMap.get(key.toUpperCase()).add(
							new String[]{mappingSQL, outProcessType});
				else {
					List tempList = new ArrayList<String[]>();
					tempList.add((new String[]{mappingSQL, outProcessType}));
					eventMappingRuleMap.put(key.toUpperCase(), tempList);
				}
			}
			if (eventMappingRuleMap != null)
				Logger.info(logInfo,"cacheEventMappingRules","@@@@@@@@@ Cached EventsMappingRule map "+eventMappingRuleMap.size());
		}catch (Exception ex){
			CoronaErrorHandler.logError(ex, "Exception while caching Event mapping rules", null);
		}

	}

	public static void updateDataChangeEventReadyToWait() {
		updateDataChangeEventStatus(DATA_CHANGE_EVENT_READY_TO_WAIT,
				CoronaFwkConstants.ProcessingStatus.WAIT);
	}

	public static void updateDataChangeEventReadyToErrorWait() {
		updateDataChangeEventStatus(DATA_CHANGE_EVENT_READY_TO_ERROR_WAIT,
				CoronaFwkConstants.ProcessingStatus.ERROR_WAIT);
	}

	public static void updateDataChangeEventWaitToReady() {
		updateDataChangeEventStatus(DATA_CHANGE_EVENT_WAIT_TO_READY,
				CoronaFwkConstants.ProcessingStatus.READY);
	}

	public static void updateDataChangeEventErrorWaitToReady() {
		updateDataChangeEventStatus(DATA_CHANGE_EVENT_ERRWAIT_TO_READY,
				CoronaFwkConstants.ProcessingStatus.READY);
	}

	public static void updateDataChangeEventWaitToErrorWait() {
		updateDataChangeEventStatus(DATA_CHANGE_EVENT_WAIT_TO_ERRWAIT,
				CoronaFwkConstants.ProcessingStatus.ERROR_WAIT);
	}


	public static int pickGroupForProcessing(String stmtId, int count,
			String lastModifiedBy, String oldStatus, String newStatus)
	throws Exception {
		try {
			DaxDataBeanGeneral dataBean = new DaxDataBeanGeneral();
			dataBean.setString1(lastModifiedBy);
			dataBean.setString2(newStatus);
			dataBean.setString3(oldStatus);
			dataBean.setInt1(count);

			DaxMgr dxMgr = DaxMgr.getInstance();
			DaxParsedStmt dxPstmt = dxMgr.makeParsedStmt(DATA_CHANGE_FWK_GROUP,
					stmtId, dataBean, null, null);
			DaxDB dxDb = dxMgr.getDaxDB();
			return dxDb.doUpdate(dxPstmt, dataBean);

		} catch (Exception e) {
			CoronaErrorHandler.logError(e, null, null);
			return -1;
		}
	}


	public static List<DataChangeEvent> retriveEventOnStatusAndIdFromDb(
			String lastModifiedBy, String status) throws Exception {
		List<DataChangeEvent> events = null;

		DaxDataBeanGeneral dataBean = new DaxDataBeanGeneral();
		dataBean.setString1(lastModifiedBy);
		dataBean.setString2(status);

		DataChangeEvent pEvent = new DataChangeEvent();

		DaxMgr dxMgr = DaxMgr.getInstance();
		DaxParsedStmt dxPstmt = dxMgr.makeParsedStmt(DATA_CHANGE_FWK_GROUP,
				PICK_THIS_EVENT_ON_STATUS_AND_ID, pEvent, null, null);
		DaxDB dxDb = dxMgr.getDaxDB();
		events = dxDb.doSelect(dxPstmt, DataChangeEvent.class, dataBean);
		return events;

	}

	public static List<DataChangeEvent> getDataChangeEventOnId(
			Long dceId) throws Exception {
		List<DataChangeEvent> events = null;

		DaxDataBeanGeneral dataBean = new DaxDataBeanGeneral();
		dataBean.setLong1(dceId);

		DataChangeEvent pEvent = new DataChangeEvent();

		DaxMgr dxMgr = DaxMgr.getInstance();
		DaxParsedStmt dxPstmt = dxMgr.makeParsedStmt(DATA_CHANGE_FWK_GROUP,
				GET_DATA_CHANGE_EVENT_BY_ID, pEvent, null, null);
		DaxDB dxDb = dxMgr.getDaxDB();
		events = dxDb.doSelect(dxPstmt, DataChangeEvent.class, dataBean);
		return events;

	}

	public static List<DataChangeEvent> retrieveDatachangeErrorEvents() {
		return retrieveDataChangeEventByStatusFromDB(
				CoronaFwkConstants.ProcessingStatus.ERROR, null);
	}


	protected static List<DataChangeEvent> retrieveDataChangeEventByStatusFromDB(
			String status, String lastModifiedBy) {

		List<DataChangeEvent> events = null;
		DaxDataBeanGeneral dataBean = new DaxDataBeanGeneral();
		dataBean.setString1(status);
		dataBean.setString2(lastModifiedBy);
		String stmtId = (lastModifiedBy == null)
		? GET_DATA_CHANGE_EVENTS_BY_STATUS
				: GET_DATA_CHANGE_EVENTS_BY_STATUS_W_ID;
		if (status != null && "ERROR".equals(status)) {
			stmtId = "GET_ERR_DATA_CHANGE_EVENTS";
		}

		DataChangeEvent pEvent = new DataChangeEvent();

		DaxMgr dxMgr = DaxMgr.getInstance();
		DaxParsedStmt dxPstmt = dxMgr.makeParsedStmt(DATA_CHANGE_FWK_GROUP,
				stmtId, pEvent, null, null);
		DaxDB dxDb = dxMgr.getDaxDB();
		events = dxDb.doSelect(dxPstmt, DataChangeEvent.class, dataBean);

		return events;
	}


	public static List<DataChangeEvent> retriveThisPickedEventFromDb(String lastModifiedBy) throws Exception{
		return retriveEventOnStatusAndIdFromDb(lastModifiedBy,"PICKED");
	}
	public static int updateDataChangeEventStatus(String stmtId, String status) {
		DataChangeEvent pEvent = new DataChangeEvent();
		pEvent.setStatus(status);
		// TODO need to change the value of last modified by
		pEvent.setLastModifiedBy(CoronaFwkConstants.FWK_PROCESSOR);

		return doDataChangeEventDbUpd(stmtId, pEvent);
	}

	public static void updateDataChangeEventStatus(DataChangeEvent dge,
			String newProcessingStatus, String message) throws Exception {
		try {
			if (message != null) {
				if (CoronaFwkConstants.ProcessingStatus.IN_PROGRESS
						.equals(newProcessingStatus))
					updateDataChangeEventStatusAndCommentById(
							UPDATE_DATA_CHANGE_EVENT_IN_PROGRESS_AND_COMMENT_BY_ID,
							newProcessingStatus, dge, message);
				else if (CoronaFwkConstants.ProcessingStatus.ERROR
						.equals(newProcessingStatus))
					updateDataChangeEventStatusAndCommentById(
							UPDATE_DATA_CHANGE_EVENT_ERROR_AND_MSG_BY_ID,
							newProcessingStatus, dge, message);
				else if (CoronaFwkConstants.ProcessingStatus.COMPLETED
						.equals(newProcessingStatus))
					updateDataChangeEventStatusAndCommentById(
							UPDATE_DATA_CHANGE_EVENT_COMPLETED_AND_COMMENT_BY_ID,
							newProcessingStatus, dge, message);
				else 
					updateDataChangeEventStatusAndCommentById(
							UPDATE_DATA_CHANGE_EVENT_STATUS_AND_COMMENT_BY_ID,
							newProcessingStatus, dge, message);
			} else {
				if (CoronaFwkConstants.ProcessingStatus.IN_PROGRESS
						.equals(newProcessingStatus))
					updateDataChangeEventStatusById(
							UPDATE_DATA_CHANGE_EVENT_IN_PROGRESS_BY_ID,
							newProcessingStatus, dge);
				else if (CoronaFwkConstants.ProcessingStatus.ERROR
						.equals(newProcessingStatus))
					updateDataChangeEventStatusAndCommentById(
							UPDATE_DATA_CHANGE_EVENT_ERROR_AND_MSG_BY_ID,
							newProcessingStatus, dge, message);
				else if (CoronaFwkConstants.ProcessingStatus.COMPLETED
						.equals(newProcessingStatus))
					updateDataChangeEventStatusById(
							UPDATE_DATA_CHANGE_EVENT_COMPLETED_BY_ID,
							newProcessingStatus, dge);
				else
					updateDataChangeEventStatusById(
							UPDATE_DATA_CHANGE_EVENT_STATUS_BY_ID,
							newProcessingStatus, dge);
			}
		}catch  (Exception ex){
			throw new Exception (ex.getMessage());
		}
	}

	public static int updateDataChangeEventStatusById(String stmtId,
			String newStatus, DataChangeEvent pEvent) throws Exception {
		try {
			DaxDataBeanGeneral dataBean = new DaxDataBeanGeneral();
			DaxMgr dxMgr = DaxMgr.getInstance();

			dataBean.setString1(newStatus);
			dataBean.setLong1(pEvent.getId());
			dataBean.setString2(CoronaFwkConstants.FWK_PROCESSOR);

			DaxParsedStmt dxPstmt = dxMgr.makeParsedStmt(DATA_CHANGE_FWK_GROUP,
					stmtId, dataBean, null, null);
			DaxDB dxDb = dxMgr.getDaxDB();
			return dxDb.doUpdate(dxPstmt, dataBean);
		} catch (Exception e) {
			CoronaErrorHandler.logError(e.getClass(),
					CoronaErrorConstants.processingErr,
					"doDataChangeEventDbUpd",
					"updateDataChangeEventStatusById", false, e, true);

			throw new Exception ("Error while updating status for events to "+newStatus);
		}
	}

	public static int updateDataChangeEventStatusAndCommentById(String stmtId,
			String newStatus, DataChangeEvent pEvent, String comment) throws Exception {
		try {
			DaxDataBeanGeneral dataBean = new DaxDataBeanGeneral();
			DaxMgr dxMgr = DaxMgr.getInstance();

			dataBean.setString1(newStatus);
			dataBean.setLong1(pEvent.getId());
			dataBean.setString2(CoronaFwkConstants.FWK_PROCESSOR);
			dataBean.setString3(comment);

			DaxParsedStmt dxPstmt = dxMgr.makeParsedStmt(DATA_CHANGE_FWK_GROUP,
					stmtId, dataBean, null, null);
			DaxDB dxDb = dxMgr.getDaxDB();
			return dxDb.doUpdate(dxPstmt, dataBean);
		} catch (Exception e) {
			CoronaErrorHandler.logError(e.getClass(),
					CoronaErrorConstants.processingErr,
					"doDataChangeEventDbUpd",
					"updateDataChangeEventStatusById", false, e, true);

			throw new Exception ("Error while updating status for events to "+newStatus);
		}
	}

	public static List<DataChangeEvent> retrieveHangingDataChangeEventsForNotification() {

		StringBuffer whereString = new StringBuffer();
		whereString.append(" where dce.dce_last_modified_date between (SYSDATE - 5/24) and (SYSDATE -1/24)");
		whereString.append(" and dce.dce_event_skip_flag = 'N' ");
		whereString.append(" and dce.dce_processing_status <> 'COMPLETED' ");

		return getDataChangeEventsByString(whereString.toString());
	}


	public static List<DataChangeEvent> getDataChangeEventsByString(
			String whereString) {
		DaxDataBeanGeneral dataBean = new DaxDataBeanGeneral();
		DaxMgr dMgr = DaxMgr.getInstance();
		DaxDB db = dMgr.getDaxDB();
		dataBean.setString1(whereString);

		DaxParsedStmt pstmt = dMgr.makeParsedStmt(DATA_CHANGE_FWK_GROUP,
				GET_DATA_CHANGE_EVENTS_BY_STRING, dataBean, null, null);
		List<DataChangeEvent> list = db.doSelect(pstmt, DataChangeEvent.class,
				dataBean);
		return list;

	}

	public static void verifyDataChangeRedundancyState() {
		LoggerInfo logInfo= new LoggerInfo("com.hp.psg.corona.dataload.dao.DataChangeEventFwkDao");

		Logger.info(logInfo, "verifyDataChangeRedundancyState",
		"***** inside verifyRedundancyState for datachange events");

		updateDataChangeEventWaitToReady();
		Logger.info(logInfo, "verifyDataChangeRedundancyState",
		"***** updateDataChangeEventWaitToReady in retry poller");

		updateDataChangeEventErrorWaitToReady();
		Logger.info(logInfo, "verifyDataChangeRedundancyState",
		"***** updateDataChangeEventErrorWaitToReady in retry poller");

		updateDataChangeEventReadyToErrorWait();
		Logger.info(logInfo, "verifyDataChangeRedundancyState",
		"***** updateDataChangeEventReadyToErrorWait in retry poller");

		updateDataChangeEventReadyToWait();
		Logger.info(logInfo, "verifyDataChangeRedundancyState",
		"***** updateDataChangeEventReadyToWait in retry poller");

	}

	public static int getDataChangeEventQueuedCount() {

		DaxDataBeanGeneral dataBean = new DaxDataBeanGeneral();
		DaxMgr dMgr = DaxMgr.getInstance();
		DaxDB db = dMgr.getDaxDB();

		DaxParsedStmt pstmt = dMgr.makeParsedStmt(DATA_CHANGE_FWK_GROUP,
				GET_DATA_CHANGE_EVENT_QUEUED_COUNTS, dataBean, null, null);
		List list = db.doSelect(pstmt, DaxDataBeanGeneral.class, dataBean);
		dataBean = (DaxDataBeanGeneral) list.get(0);
		return dataBean.getInt1();
	}


	protected static int doDataChangeEventDbUpd(String stmtId,
			DataChangeEvent pEvent) {
		try {
			DaxDataBeanGeneral dataBean = new DaxDataBeanGeneral();
			DaxMgr dxMgr = DaxMgr.getInstance();

			dataBean.setString1(pEvent.getLastModifiedBy());
			dataBean.setString2(CoronaFwkUtil.getManageServerName());

			DaxParsedStmt dxPstmt = dxMgr.makeParsedStmt(DATA_CHANGE_FWK_GROUP,
					stmtId, dataBean, null, null);
			DaxDB dxDb = dxMgr.getDaxDB();
			return dxDb.doUpdate(dxPstmt, dataBean);

		} catch (Exception e) {
			CoronaErrorHandler.logError(e.getClass(),
					CoronaErrorConstants.processingErr,
					"doDataChangeEventDbUpd",
					"Error in Events Handler while updating events status",
					false, e, true);

			return -1;
		}
	}

	// Getter- Setter

	public static Map<String, List<String>> getSourceFormatKeyGetterMethodMap() {
		return sourceFormatKeyGetterMethodMap;
	}

	public static void setSourceFormatKeyGetterMethodMap(
			HashMap<String, List<String>> sourceFormatKeyGetterMethodMap) {
		DataChangeEventsFwkDao.sourceFormatKeyGetterMethodMap = sourceFormatKeyGetterMethodMap;
	}

	public static Map<String, List<String>> getSourceFormatKeySetterMethodMap() {
		return sourceFormatKeySetterMethodMap;
	}

	public static void setSourceFormatKeySetterMethodMap(
			HashMap<String, List<String>> sourceFormatKeySetterMethodMap) {
		DataChangeEventsFwkDao.sourceFormatKeySetterMethodMap = sourceFormatKeySetterMethodMap;
	}

	public static Map<String, List<String>> getSourceFormatKeyMap() {
		return sourceFormatKeyMap;
	}

	public static void setSourceFormatKeyMap(
			Map<String, List<String>> sourceFormatKeyMap) {
		DataChangeEventsFwkDao.sourceFormatKeyMap = sourceFormatKeyMap;
	}

	public static Map<String, List<HashMap<String, List<String>>>> getCtoPluginMap() {
		return ctoPluginMap;
	}

	public static void setCtoPluginMap(
			Map<String, List<HashMap<String, List<String>>>> ctoPluginMap) {
		DataChangeEventsFwkDao.ctoPluginMap = ctoPluginMap;
	}

	public static Map<String, List<String[]>> getEventMappingRuleMap() {
		return eventMappingRuleMap;
	}

	public static void setEventMappingRuleMap(
			Map<String, List<String[]>> eventMappingRuleMap) {
		DataChangeEventsFwkDao.eventMappingRuleMap = eventMappingRuleMap;
	}

	public static Map<String, List<String>> getRegionalAttributeMaps() {
		return regionalAttributeMaps;
	}

	public static void setRegionalAttributeMaps(
			Map<String, List<String>> regionalAttributeMaps) {
		DataChangeEventsFwkDao.regionalAttributeMaps = regionalAttributeMaps;
	}

	public static Map<String, List<String>> getDataChangeCallHandlerMethodMap() {
		return dataChangeCallHandlerMethodMap;
	}

	public static void setDataChangeCallHandlerMethodMap(
			Map<String, List<String>> dataChangeCallHandlerMethodMap) {
		DataChangeEventsFwkDao.dataChangeCallHandlerMethodMap = dataChangeCallHandlerMethodMap;
	}

	public static Map<String, String> getHmPTtoDaoMethodsMap() {
		return hmPTtoDaoMethodsMap;
	}

	public static void setHmPTtoDaoMethodsMap(
			Map<String, String> hmPTtoDaoMethodsMap) {
		DataChangeEventsFwkDao.hmPTtoDaoMethodsMap = hmPTtoDaoMethodsMap;
	}

	public static Map<String, List<CtoTransformationUtilMap>> getHmTransoformationClassList() {
		return hmTransoformationClassList;
	}

	public static void setHmTransoformationClassList(
			Map<String, List<CtoTransformationUtilMap>> hmTransoformationClassList) {
		DataChangeEventsFwkDao.hmTransoformationClassList = hmTransoformationClassList;
	}

	public static Map<String, String> getHmPTtoPluginTypeMap() {
		return hmPTtoPluginTypeMap;
	}

	public static void setHmPTtoPluginTypeMap(
			Map<String, String> hmPTtoPluginTypeMap) {
		DataChangeEventsFwkDao.hmPTtoPluginTypeMap = hmPTtoPluginTypeMap;
	}

	public static Map<String, CountryCodeBean> getHmCountryCodesMap() {
		return hmCountryCodesMap;
	}

	public static void setHmCountryCodesMap(
			Map<String, CountryCodeBean> hmCountryCodesMap) {
		DataChangeEventsFwkDao.hmCountryCodesMap = hmCountryCodesMap;
	}

	public static Map<String, PriceTermBean> getHmPriceTermMap() {
		return hmPriceTermMap;
	}

	public static void setHmPriceTermMap(
			Map<String, PriceTermBean> hmPriceTermMap) {
		DataChangeEventsFwkDao.hmPriceTermMap = hmPriceTermMap;
	}

	public static Map<String, CurrencyBean> getHmCurrencyMap() {
		return hmCurrencyMap;
	}

	public static void setHmCurrencyMap(Map<String, CurrencyBean> hmCurrencyMap) {
		DataChangeEventsFwkDao.hmCurrencyMap = hmCurrencyMap;
	}

	public static Map<String, List<String>> getHmBeanKeyMethodListMap() {
		return hmBeanKeyMethodListMap;
	}

	public static Map<String, String> getModuleMap() {
		return hmModuleMap;
	}

	public static void setHmBeanKeyMethodListMap(
			Map<String, List<String>> hmBeanKeyMethodListMap) {
		DataChangeEventsFwkDao.hmBeanKeyMethodListMap = hmBeanKeyMethodListMap;
	}

	public static void setModuleMap(Map<String, String> moduleMap) {
		DataChangeEventsFwkDao.hmModuleMap = moduleMap;
	}

}
