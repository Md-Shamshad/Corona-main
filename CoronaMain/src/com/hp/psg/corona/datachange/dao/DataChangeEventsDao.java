package com.hp.psg.corona.datachange.dao;

import com.hp.psg.common.db.util.DaxManager;
import com.hp.psg.common.error.CoronaException;
import com.hp.psg.common.util.logging.LoggerInfo;
import com.hp.psg.corona.common.beans.CTODaxDataBeanGeneral;
import com.hp.psg.corona.common.beans.PropagationEvent;
import com.hp.psg.corona.common.constants.CoronaFwkConstants;
import com.hp.psg.corona.common.cto.beans.ConfigComponentInfo;
import com.hp.psg.corona.common.cto.beans.ConfigHeaderInfo;
import com.hp.psg.corona.common.cto.beans.ConfigPermutationInfo;
import com.hp.psg.corona.common.cto.beans.ConfigPriceHeaderInfo;
import com.hp.psg.corona.common.cto.beans.PriceInfo;
import com.hp.psg.corona.common.cto.beans.ProductDescription;
import com.hp.psg.corona.common.util.Logger;
import com.hp.psg.corona.error.util.CoronaErrorHandler;

/**
 * @author ghatta
 * @version 1.0
 */
public class DataChangeEventsDao {

	private static String GROUP_CONFIG_DATA_CHANGE = "GROUP_CONFIG_DATA_CHANGE";
	private static String ADD_CONFIG_PERMUTATION_EVENTS = "ADD_CONFIG_PERMUTATION_EVENTS";
	private static String ADD_CONFIG_PRICE_EVENTS = "ADD_CONFIG_PRICE_EVENTS";
	private static String ADD_CONFIG_CNTRY_EVENTS = "ADD_CONFIG_CNTRY_EVENTS";
	private static String ADD_PRODUCT_DESC_EVENTS = "ADD_PRODUCT_DESC_EVENTS";
	private static String ADD_LIST_PRICE_EVENTS = "ADD_LIST_PRICE_EVENTS";
	private static String PROCESS_CONFIG_CNTRY_DATA = "PROCESS_CONFIG_CNTRY_DATA";
	private static String PROCESS_CONFIG_PERMUTATION_DATA = "PROCESS_CONFIG_PERMUTATION_DATA";
	private static String PROCESS_CONFIG_PRICE_DATA = "PROCESS_CONFIG_PRICE_DATA";
	private static String PROCESS_PRODUCT_DESC_DATA = "PROCESS_PRODUCT_DESC_DATA";
	private static String GET_CONFIG_PRICE_DATA_CHANGE = "GET_CONFIG_PRICE_DATA_CHANGE";
	private static String GET_CONFIG_PERMUTATION_DATA_CHANGE = "GET_CONFIG_PERMUTATION_DATA_CHANGE";
	private static String GET_CONFIG_CNTRY_DATA_CHANGE = "GET_CONFIG_CNTRY_DATA_CHANGE";
	private static String GET_PRODUCT_DESC_DATA_CHANGE = "GET_PRODUCT_DESC_DATA_CHANGE";
	private static String ADD_PROPAGATION_EVENTS = "ADD_PROPAGATION_EVENTS";
	// New Optimized method - no getter required
	private static String PROCESS_CONFIG_PRICE = "PROCESS_CONFIG_PRICE";
	private static String PROCESS_LIST_PRICE_UPDATE = "PROCESS_LIST_PRICE_UPDATE";
	private static String PROCESS_LIST_PRICE_INSERT = "PROCESS_LIST_PRICE_INSERT";

	public static void addConfigPermDataChangeEvents() throws CoronaException {
		LoggerInfo logInfo = new LoggerInfo ("com.hp.psg.corona.dataload.dao.DataChangeEventsDao");
		
		Logger.info(logInfo, "addConfigPermDataChangeEvents","@@@@@@@@@ Start addConfigPermDataChangeEvents");
		CTODaxDataBeanGeneral cddbg = new CTODaxDataBeanGeneral();
		DaxManager.getDAOMgr().executeSP(GROUP_CONFIG_DATA_CHANGE,
				ADD_CONFIG_PERMUTATION_EVENTS, cddbg);

		int returnCode = cddbg.getReturnCode();
		if (returnCode == CoronaFwkConstants.ERROR) {
			String msg = cddbg.getMsg();
			if (msg == null) {
				msg = "Error executing SP " + ADD_CONFIG_PERMUTATION_EVENTS;
			}
			
			throw new CoronaException(msg);
		} else if (returnCode == CoronaFwkConstants.WARNING) {
			Logger.info(logInfo, "addConfigPermDataChangeEvents","Zero records updated or inserted executing SP "
					+ ADD_CONFIG_PERMUTATION_EVENTS);
		} else if (returnCode == CoronaFwkConstants.SUCCESS) {
			Logger.info(logInfo, "addConfigPermDataChangeEvents","Success executing SP "
							+ ADD_CONFIG_PERMUTATION_EVENTS);
		}

	}

	public static void addConfigCntryDataChangeEvents() throws CoronaException {
		LoggerInfo logInfo = new LoggerInfo ("com.hp.psg.corona.dataload.dao.DataChangeEventsDao");
		Logger.info(logInfo, "addConfigCntryDataChangeEvents","@@@@@@@@@ Start addConfigCntryDataChangeEvents");
		CTODaxDataBeanGeneral cddbg = new CTODaxDataBeanGeneral();
		DaxManager.getDAOMgr().executeSP(GROUP_CONFIG_DATA_CHANGE,
				ADD_CONFIG_CNTRY_EVENTS, cddbg);

		int returnCode = cddbg.getReturnCode();
		if (returnCode == CoronaFwkConstants.ERROR) {
			String msg = cddbg.getMsg();
			if (msg == null) {
				msg = "Error executing SP " + ADD_CONFIG_CNTRY_EVENTS;
			}
			
			throw new CoronaException(msg);
		} else if (returnCode == CoronaFwkConstants.WARNING) {
			Logger.info(logInfo, "addConfigCntryDataChangeEvents","Zero records updated or inserted executing SP "
					+ ADD_CONFIG_CNTRY_EVENTS);
		} else if (returnCode == CoronaFwkConstants.SUCCESS) {
			Logger.info(logInfo, "addConfigCntryDataChangeEvents","Success executing SP " + ADD_CONFIG_CNTRY_EVENTS);
		}

	}

	public static void addConfigPriceDataChangeEvents() throws CoronaException {
		LoggerInfo logInfo = new LoggerInfo ("com.hp.psg.corona.dataload.dao.DataChangeEventsDao");
		Logger.info(logInfo, "addConfigPriceDataChangeEvents","@@@@@@@@@ Start addConfigPriceDataChangeEvents");
		CTODaxDataBeanGeneral cddbg = new CTODaxDataBeanGeneral();
		DaxManager.getDAOMgr().executeSP(GROUP_CONFIG_DATA_CHANGE,
				ADD_CONFIG_PRICE_EVENTS, cddbg);

		int returnCode = cddbg.getReturnCode();
		if (returnCode == CoronaFwkConstants.ERROR) {
			String msg = cddbg.getMsg();
			if (msg == null) {
				msg = "Error executing SP " + ADD_CONFIG_PRICE_EVENTS;
			}
			
			throw new CoronaException(msg);
		} else if (returnCode == CoronaFwkConstants.WARNING) {
			Logger.info(logInfo, "addConfigPriceDataChangeEvents","Zero records updated or inserted executing SP "
					+ ADD_CONFIG_PRICE_EVENTS);
		} else if (returnCode == CoronaFwkConstants.SUCCESS) {
			Logger.info(logInfo, "addConfigPriceDataChangeEvents","Success executing SP " + ADD_CONFIG_PRICE_EVENTS);
		}

	}

	public static void addProductDescDataChangeEvents() throws CoronaException {
		LoggerInfo logInfo = new LoggerInfo ("com.hp.psg.corona.dataload.dao.DataChangeEventsDao");
		Logger.info(logInfo, "addProductDescDataChangeEvents","@@@@@@@@@ Start addProductDescDataChangeEvents");
		CTODaxDataBeanGeneral cddbg = new CTODaxDataBeanGeneral();
		DaxManager.getDAOMgr().executeSP(GROUP_CONFIG_DATA_CHANGE,
				ADD_PRODUCT_DESC_EVENTS, cddbg);

		int returnCode = cddbg.getReturnCode();
		if (returnCode == CoronaFwkConstants.ERROR) {
			String msg = cddbg.getMsg();
			if (msg == null) {
				msg = "Error executing SP " + ADD_PRODUCT_DESC_EVENTS;
			}
			
			throw new CoronaException(msg);
		} else if (returnCode == CoronaFwkConstants.WARNING) {
			Logger.info(logInfo, "addProductDescDataChangeEvents","Zero records updated or inserted executing SP "
					+ ADD_PRODUCT_DESC_EVENTS);
		} else if (returnCode == CoronaFwkConstants.SUCCESS) {
			Logger.info(logInfo, "addProductDescDataChangeEvents","Success executing SP " + ADD_PRODUCT_DESC_EVENTS);
		}

	}

	public static void addListPriceDataChangeEvents() throws CoronaException {
		LoggerInfo logInfo = new LoggerInfo ("com.hp.psg.corona.dataload.dao.DataChangeEventsDao");
		Logger.info(logInfo, "addListPriceDataChangeEvents","@@@@@@@@@ Start addListPriceDataChangeEvents");
		CTODaxDataBeanGeneral cddbg = new CTODaxDataBeanGeneral();
		DaxManager.getDAOMgr().executeSP(GROUP_CONFIG_DATA_CHANGE,
				ADD_LIST_PRICE_EVENTS, cddbg);

		int returnCode = cddbg.getReturnCode();
		if (returnCode == CoronaFwkConstants.ERROR) {
			String msg = cddbg.getMsg();
			if (msg == null) {
				msg = "Error executing SP " + ADD_LIST_PRICE_EVENTS;
			}
			
			throw new CoronaException(msg);
		} else if (returnCode == CoronaFwkConstants.WARNING) {
			Logger.info(logInfo, "addListPriceDataChangeEvents","Zero records updated or inserted executing SP "
					+ ADD_LIST_PRICE_EVENTS);
		} else if (returnCode == CoronaFwkConstants.SUCCESS) {
			Logger.info(logInfo, "addListPriceDataChangeEvents","Success executing SP " + ADD_LIST_PRICE_EVENTS);
		}

	}
	
	public static void processConfigCntryDataChange(Long dceId,
			ConfigHeaderInfo[] configHeaderInfo,
			ConfigComponentInfo[] configComponentInfo,
			ProductDescription[] productDescription) throws CoronaException {
		LoggerInfo logInfo = new LoggerInfo ("com.hp.psg.corona.dataload.dao.DataChangeEventsDao");
		Logger.info(logInfo, "processConfigCntryDataChange","@@@@@@@@@ Start processConfigCntryDataChange");
		CTODaxDataBeanGeneral cddbg = new CTODaxDataBeanGeneral();
		cddbg.setDceId(dceId);
		cddbg.setConfigHeaderInfo(configHeaderInfo);
		cddbg.setConfigComponentInfo(configComponentInfo);
		cddbg.setProductDescription(productDescription);
		DaxManager.getDAOMgr().executeSP(GROUP_CONFIG_DATA_CHANGE,
				PROCESS_CONFIG_CNTRY_DATA, cddbg);

		int returnCode = cddbg.getReturnCode();
		if (returnCode == CoronaFwkConstants.ERROR) {
			String msg = cddbg.getMsg();
			if (msg == null) {
				msg = "Error executing SP " + PROCESS_CONFIG_CNTRY_DATA;
			}
			
			throw new CoronaException(msg);
		} else if (returnCode == CoronaFwkConstants.WARNING) {
			Logger.info(logInfo, "processConfigCntryDataChange","Zero records updated or inserted executing SP "
					+ PROCESS_CONFIG_CNTRY_DATA);
		} else if (returnCode == CoronaFwkConstants.SUCCESS) {
			Logger.info(logInfo, "processConfigCntryDataChange","Success executing SP " + PROCESS_CONFIG_CNTRY_DATA);
		}

	}

	public static void processConfigPermDataChange(Long dceId,
			ConfigPermutationInfo[] configPermInfo) throws CoronaException {
		LoggerInfo logInfo = new LoggerInfo ("com.hp.psg.corona.dataload.dao.DataChangeEventsDao");
		Logger.info(logInfo, "processConfigPermDataChange","@@@@@@@@@ Start processConfigPermDataChange");
		CTODaxDataBeanGeneral cddbg = new CTODaxDataBeanGeneral();
		cddbg.setDceId(dceId);
		cddbg.setConfigPermutationInfo(configPermInfo);
		DaxManager.getDAOMgr().executeSP(GROUP_CONFIG_DATA_CHANGE,
				PROCESS_CONFIG_PERMUTATION_DATA, cddbg);

		int returnCode = cddbg.getReturnCode();
		if (returnCode == CoronaFwkConstants.ERROR) {
			String msg = cddbg.getMsg();
			if (msg == null) {
				msg = "Error executing SP " + PROCESS_CONFIG_PERMUTATION_DATA;
			}
			
			throw new CoronaException(msg);
		} else if (returnCode == CoronaFwkConstants.WARNING) {
			Logger.info(logInfo, "processConfigPermDataChange","Zero records updated or inserted executing SP "
					+ PROCESS_CONFIG_PERMUTATION_DATA);
		} else if (returnCode == CoronaFwkConstants.SUCCESS) {
			Logger.info(logInfo, "processConfigPermDataChange","Success executing SP "
					+ PROCESS_CONFIG_PERMUTATION_DATA);
		}

	}

	public static void processConfigPriceDataChange(Long dceId,
			ConfigPriceHeaderInfo[] priceHeader, PriceInfo[] priceInfo)
			throws CoronaException {
		System.out.println("Call came to processConfigPriceDataChange");
		LoggerInfo logInfo = new LoggerInfo ("com.hp.psg.corona.dataload.dao.DataChangeEventsDao");
		Logger.info(logInfo, "processConfigPriceDataChange","@@@@@@@@@ Start processConfigPriceDataChange");
		CTODaxDataBeanGeneral cddbg = new CTODaxDataBeanGeneral();
		cddbg.setDceId(dceId);
		cddbg.setConfigPriceHeaderInfo(priceHeader);
		cddbg.setPriceInfo(priceInfo);
		DaxManager.getDAOMgr().executeSP(GROUP_CONFIG_DATA_CHANGE,
				PROCESS_CONFIG_PRICE_DATA, cddbg);

		int returnCode = cddbg.getReturnCode();
		if (returnCode == CoronaFwkConstants.ERROR) {
			String msg = cddbg.getMsg() +" for datachangeevent Id "+ dceId;
			if (msg == null) {
				msg = "Error executing SP " + PROCESS_CONFIG_PRICE_DATA;
			}
			
			throw new CoronaException(msg);
		} else if (returnCode == CoronaFwkConstants.WARNING) {
			Logger.info(logInfo, "processConfigPriceDataChange","Zero records updated or inserted executing SP "
					+ PROCESS_CONFIG_PRICE_DATA);
		} else if (returnCode == CoronaFwkConstants.SUCCESS) {
			Logger.info(logInfo, "processConfigPriceDataChange","Success executing SP " + PROCESS_CONFIG_PRICE_DATA);
		}

	}
	
	/**
	 * New Method added which does not require associated getter SP. This method replaces
	 * processConfigPriceDataChange. 
	 * @param dceId
	 * @throws CoronaException
	 */
	public static void processConfigPrice(Long dceId )
			throws CoronaException {
		LoggerInfo logInfo = new LoggerInfo ("com.hp.psg.corona.dataload.dao.DataChangeEventsDao");
		Logger.info(logInfo, "processConfigPrice","@@@@@@@@@ Start processConfigPrice");
		CTODaxDataBeanGeneral cddbg = new CTODaxDataBeanGeneral();
		cddbg.setDceId(dceId);
		DaxManager.getDAOMgr().executeSP(GROUP_CONFIG_DATA_CHANGE,
				PROCESS_CONFIG_PRICE, cddbg);

		int returnCode = cddbg.getReturnCode();
		if (returnCode == CoronaFwkConstants.ERROR) {
			String msg = cddbg.getMsg() +" for datachangeevent Id "+ dceId;
			if (msg == null) {
				msg = "Error executing SP " + PROCESS_CONFIG_PRICE;
			}
			
			throw new CoronaException(msg);
		} else if (returnCode == CoronaFwkConstants.WARNING) {
			Logger.info(logInfo, "processConfigPrice","Zero records updated or inserted executing SP "
					+ PROCESS_CONFIG_PRICE);
		} else if (returnCode == CoronaFwkConstants.SUCCESS) {
			Logger.info(logInfo, "processConfigPrice","Success executing SP " + PROCESS_CONFIG_PRICE);
		}

	}
	
	public static void processProductDescDataChange(Long dceId,
			ProductDescription[] productDescription) throws CoronaException {
		LoggerInfo logInfo = new LoggerInfo ("com.hp.psg.corona.dataload.dao.DataChangeEventsDao");
		Logger.info(logInfo, "processProductDescDataChange","@@@@@@@@@ Start processProductDescDataChange");
		CTODaxDataBeanGeneral cddbg = new CTODaxDataBeanGeneral();
		cddbg.setDceId(dceId);
		cddbg.setProductDescription(productDescription);
		DaxManager.getDAOMgr().executeSP(GROUP_CONFIG_DATA_CHANGE,
				PROCESS_PRODUCT_DESC_DATA, cddbg);

		int returnCode = cddbg.getReturnCode();
		if (returnCode == CoronaFwkConstants.ERROR) {
			String msg = cddbg.getMsg()+" for datachangeevent Id "+ dceId;;
			if (msg == null) {
				msg = "Error executing SP " + PROCESS_PRODUCT_DESC_DATA;
			}
			
			throw new CoronaException(msg);
		} else if (returnCode == CoronaFwkConstants.WARNING) {
			Logger.info(logInfo, "processProductDescDataChange","Zero records updated or inserted executing SP "
					+ PROCESS_PRODUCT_DESC_DATA);
		} else if (returnCode == CoronaFwkConstants.SUCCESS) {
			Logger.info(logInfo, "processProductDescDataChange","Success executing SP " + PROCESS_PRODUCT_DESC_DATA);
		}

	}

	public static CTODaxDataBeanGeneral getConfigPermDataChange(Long dceId,
			Long trnId, String priceId, String bundleId, String shipCntry,
			String priceDescriptor) throws CoronaException {
		LoggerInfo logInfo = new LoggerInfo ("com.hp.psg.corona.dataload.dao.DataChangeEventsDao");
		Logger.info(logInfo, "getConfigPermDataChange","@@@@@@@@@ Start getConfigPermDataChange");
		CTODaxDataBeanGeneral cddbg = new CTODaxDataBeanGeneral();
		cddbg.setDceId(dceId);
		cddbg.setTrnId(trnId);
		cddbg.setBundleId(bundleId);
		cddbg.setPriceId(priceId);
		cddbg.setShipToCountry(shipCntry);
		cddbg.setPriceDescriptor(priceDescriptor);

		DaxManager.getDAOMgr().executeSP(GROUP_CONFIG_DATA_CHANGE,
				GET_CONFIG_PERMUTATION_DATA_CHANGE, cddbg);

		int returnCode = cddbg.getReturnCode();
		if (returnCode == CoronaFwkConstants.ERROR) {
			String msg = cddbg.getMsg() +" for datachangeevent Id "+ dceId;
			if (msg == null) {
				msg = "Error executing SP "
						+ GET_CONFIG_PERMUTATION_DATA_CHANGE;
			}
			
			throw new CoronaException(msg);
		} else if (returnCode == CoronaFwkConstants.WARNING) {
			Logger.info(logInfo, "getConfigPermDataChange","Zero records updated or inserted executing SP "
					+ GET_CONFIG_PERMUTATION_DATA_CHANGE);
		} else if (returnCode == CoronaFwkConstants.SUCCESS) {
			Logger.info(logInfo, "getConfigPermDataChange","Success executing SP "
					+ GET_CONFIG_PERMUTATION_DATA_CHANGE);
		}

		return cddbg;

	}

	public static CTODaxDataBeanGeneral getConfigCntryDataChange(Long dceId,
			Long trnId, String bundleId, String shipCntry)
			throws CoronaException {
		LoggerInfo logInfo = new LoggerInfo ("com.hp.psg.corona.dataload.dao.DataChangeEventsDao");
		Logger.info(logInfo, "getConfigCntryDataChange","@@@@@@@@@ Start getConfigCntryDataChange");
		CTODaxDataBeanGeneral cddbg = new CTODaxDataBeanGeneral();
		cddbg.setDceId(dceId);
		cddbg.setTrnId(trnId);
		cddbg.setBundleId(bundleId);
		cddbg.setShipToCountry(shipCntry);

		DaxManager.getDAOMgr().executeSP(GROUP_CONFIG_DATA_CHANGE,
				GET_CONFIG_CNTRY_DATA_CHANGE, cddbg);

		int returnCode = cddbg.getReturnCode();
		if (returnCode == CoronaFwkConstants.ERROR) {
			String msg = cddbg.getMsg() +" for datachangeevent Id "+ dceId;
			if (msg == null) {
				msg = "Error executing SP " + GET_CONFIG_CNTRY_DATA_CHANGE;
			}
			
			throw new CoronaException(msg);
		} else if (returnCode == CoronaFwkConstants.WARNING) {
			Logger.info(logInfo, "getConfigCntryDataChange","Zero records updated or inserted executing SP "
					+ GET_CONFIG_CNTRY_DATA_CHANGE);
		} else if (returnCode == CoronaFwkConstants.SUCCESS) {
			Logger.info(logInfo, "getConfigCntryDataChange","Success executing SP " + GET_CONFIG_CNTRY_DATA_CHANGE);
		}

		return cddbg;

	}


	/*
	 * This is old method which goes in dax style.
	 * 
	 */
	 public static CTODaxDataBeanGeneral getConfigPriceDataChange(Long dceId,
			Long trnId, String priceId, String bundleId, String shipToGeo,
			String priceGeo, String currency, String incoTerm,
			String priceListType) throws CoronaException {
		LoggerInfo logInfo = new LoggerInfo ("com.hp.psg.corona.dataload.dao.DataChangeEventsDao");
		Logger.info(logInfo, "getConfigPriceDataChange","@@@@@@@@@ Start getConfigPriceDataChange");
		CTODaxDataBeanGeneral cddbg = new CTODaxDataBeanGeneral();
		cddbg.setDceId(dceId);
		cddbg.setTrnId(trnId);
		cddbg.setBundleId(bundleId);
		cddbg.setPriceId(priceId);
		cddbg.setShipToGeo(shipToGeo);
		cddbg.setPriceGeo(priceGeo);
		cddbg.setCurrency(currency);
		cddbg.setIncoTerm(incoTerm);
		cddbg.setPriceListType(priceListType);

		DaxManager.getDAOMgr().executeSP(GROUP_CONFIG_DATA_CHANGE,
				GET_CONFIG_PRICE_DATA_CHANGE, cddbg);

		int returnCode = cddbg.getReturnCode();
		if (returnCode == CoronaFwkConstants.ERROR) {
			String msg = cddbg.getMsg() +" for datachangeevent Id "+ dceId;
			if (msg == null) {
				msg = "Error executing SP " + GET_CONFIG_PRICE_DATA_CHANGE;
			}
			
			throw new CoronaException(msg);
		} else if (returnCode == CoronaFwkConstants.WARNING) {
			Logger.info(logInfo, "getConfigPriceDataChange","Zero records updated or inserted executing SP "
					+ GET_CONFIG_PRICE_DATA_CHANGE);
		} else if (returnCode == CoronaFwkConstants.SUCCESS) {
			Logger.info(logInfo, "getConfigPriceDataChange","Success executing SP " + GET_CONFIG_PRICE_DATA_CHANGE);
		}

		return cddbg;

	}
	
	/*
	public static CTODaxDataBeanGeneral getConfigPriceDataChange(Long dceId,
			Long trnId, String priceId, String bundleId, String shipToGeo,
			String priceGeo, String currency, String incoTerm,
			String priceListType) throws CoronaException {
		LoggerInfo logInfo = new LoggerInfo ("com.hp.psg.corona.dataload.dao.DataChangeEventsDao");
		Logger.info(logInfo, "getConfigPriceDataChange","@@@@@@@@@ Start getConfigPriceDataChange");
		CTODaxDataBeanGeneral cddbg = new CTODaxDataBeanGeneral();
		cddbg.setDceId(dceId);
		cddbg.setTrnId(trnId);
		cddbg.setBundleId(bundleId);
		cddbg.setPriceId(priceId);
		cddbg.setShipToGeo(shipToGeo);
		cddbg.setPriceGeo(priceGeo);
		cddbg.setCurrency(currency);
		cddbg.setIncoTerm(incoTerm);
		cddbg.setPriceListType(priceListType);

		OracleCallableStatement cstmt = null;
		DaxMgr dx_mgr = null;
		Connection conn = null;
		DaxConnection daxConn = null;
		DaxConnectionPool daxConnPool=null;

		System.out.println("Method call came to getConfigPriceDataChange ");
		try {

			dx_mgr = DaxMgr.getInstance();
			daxConnPool = dx_mgr.getConnectionPool();
			daxConn=daxConnPool.getConnection();
			conn = daxConn.getJdbcConnection();
	
			Map mapping = conn.getTypeMap();
			mapping.put("SYSTEST_EZCIPCS_EDA.TY_CONFIG_PRICE_HEADER_INFO", Class.forName("com.hp.psg.corona.common.cto.beans.ConfigPriceHeaderInfo"));
			mapping.put("SYSTEST_EZCIPCS_EDA.TY_PRICE_INFO", Class.forName("com.hp.psg.corona.common.cto.beans.PriceInfo"));
			//mapping.put("CORONA_SCH.TY_CONFIG_PRICE_HEADER_INFO", Class.forName("ConfigPriceHeaderInfo"));
			conn.setTypeMap(mapping);
			
			cstmt = (OracleCallableStatement)conn.prepareCall("{call CONFIG_DATA_CHANGE.get_config_price_data_change(?,?,?,?,?,?,?,?,?,?,?,?,?)}");
			cstmt.setLong(1,dceId);  //i_dce_id
			cstmt.setLong(2, trnId);  //i_trn_id 
			cstmt.setString(3, bundleId);     //i_price_id
			cstmt.setString(4, priceId);    //i_bundle_id
			cstmt.setString(5, shipToGeo);          //i_ship_cntry
			cstmt.setString(6, priceGeo);     //i_price_geo
			cstmt.setString(7, currency);     //i_currency
			cstmt.setString(8, incoTerm);     //i_inco_term
			cstmt.setString(9, priceListType);     //i_price_list_type
			
			cstmt.registerOutParameter(10, OracleTypes.ARRAY, "SYSTEST_EZCIPCS_EDA.CONFIG_PRICE_HEADER_INFO_TBL" );  //o_config_permutation_tbl
			cstmt.registerOutParameter(11, OracleTypes.ARRAY, "SYSTEST_EZCIPCS_EDA.PRICE_INFO_TBL" );  //o_config_permutation_tbl
			
			cstmt.registerOutParameter(12, OracleTypes.NUMBER); //o_status
			cstmt.registerOutParameter(13, OracleTypes.VARCHAR); //o_msg
			
			cstmt.execute();
			
			ARRAY priceHeaderInfo = cstmt.getARRAY(10);
			ARRAY priceInfo = cstmt.getARRAY(11);

			Object[] priceInfoArr = (Object[]) priceInfo.getArray();
			Object[] priceHdrInfoArr = (Object[]) priceHeaderInfo.getArray();
			
			System.out.println("Length for priceinfo is >>>>>>>>>>>>>>> " + priceInfoArr.length +" and for priceheader info is "+priceHdrInfoArr.length);
			
			PriceInfo[] ob = null;
			if(priceInfoArr.length > 0){
				oracle.sql.STRUCT str = (oracle.sql.STRUCT)priceInfoArr[0];
				ob = (PriceInfo []) str.getAttributes();
			}

			ConfigPriceHeaderInfo[] obh = null;
			if(priceHdrInfoArr.length > 0){
				oracle.sql.STRUCT str2 = (oracle.sql.STRUCT)priceHdrInfoArr[0];
				obh = (ConfigPriceHeaderInfo []) str2.getAttributes();
			}

			cddbg.setPriceInfo(ob);
			cddbg.setConfigPriceHeaderInfo(obh);
			
			cddbg.setReturnCode(cstmt.getInt(12));
			cddbg.setStatus(cstmt.getString(13));
			cstmt.close();
		}
		catch (Exception e) {
			e.printStackTrace();
			throw new CoronaException(e);
		}finally {
			daxConnPool.releaseConnection(daxConn);
		}
		int returnCode = cddbg.getReturnCode();
		if (returnCode == CoronaFwkConstants.ERROR) {
			String msg = cddbg.getMsg() +" for datachangeevent Id "+ dceId;
			if (msg == null) {
				msg = "Error executing SP " + GET_CONFIG_PRICE_DATA_CHANGE;
			}
			
			throw new CoronaException(msg);
		} else if (returnCode == CoronaFwkConstants.WARNING) {
			Logger.info(logInfo, "getConfigPriceDataChange","Zero records updated or inserted executing SP "
					+ GET_CONFIG_PRICE_DATA_CHANGE);
		} else if (returnCode == CoronaFwkConstants.SUCCESS) {
			Logger.info(logInfo, "getConfigPriceDataChange","Success executing SP " + GET_CONFIG_PRICE_DATA_CHANGE);
		}

		return cddbg;

	}
*/
	

	public static CTODaxDataBeanGeneral getProductDescDataChange(Long dceId,
			Long trnId, String productId) throws CoronaException {
		LoggerInfo logInfo = new LoggerInfo ("com.hp.psg.corona.dataload.dao.DataChangeEventsDao");
		Logger.info(logInfo, "getProductDescDataChange","@@@@@@@@@ Start getProductDescDataChange");
		CTODaxDataBeanGeneral cddbg = new CTODaxDataBeanGeneral();
		cddbg.setDceId(dceId);
		cddbg.setTrnId(trnId); 

		DaxManager.getDAOMgr().executeSP(GROUP_CONFIG_DATA_CHANGE,
				GET_PRODUCT_DESC_DATA_CHANGE, cddbg);

		int returnCode = cddbg.getReturnCode();
		if (returnCode == CoronaFwkConstants.ERROR) {
			String msg = cddbg.getMsg() +" for datachangeevent Id "+ dceId;
			if (msg == null) {
				msg = "Error executing SP " + GET_PRODUCT_DESC_DATA_CHANGE;
			}
			
			throw new CoronaException(msg);
		} else if (returnCode == CoronaFwkConstants.WARNING) {
			Logger.info(logInfo, "getProductDescDataChange","Zero records updated or inserted executing SP "
					+ GET_PRODUCT_DESC_DATA_CHANGE);
		} else if (returnCode == CoronaFwkConstants.SUCCESS) {
			Logger.info(logInfo, "getProductDescDataChange","Success executing SP " + GET_PRODUCT_DESC_DATA_CHANGE);
		}

		return cddbg;

	}

	public static void addPropagationEvents(PropagationEvent[] propEvent)
			throws CoronaException {
		LoggerInfo logInfo = new LoggerInfo ("com.hp.psg.corona.dataload.dao.DataChangeEventsDao");
		Logger.info(logInfo, "addPropagationEvents","@@@@@@@@@ Start addPropagationEvents");
		CTODaxDataBeanGeneral cddbg = new CTODaxDataBeanGeneral();
		cddbg.setPropagationEvent(propEvent);
		DaxManager.getDAOMgr().executeSP(GROUP_CONFIG_DATA_CHANGE,
				ADD_PROPAGATION_EVENTS, cddbg);

		int returnCode = cddbg.getReturnCode();
		if (returnCode == CoronaFwkConstants.ERROR) {
			String msg = cddbg.getMsg();
			if (msg == null) {
				msg = "Error executing SP " + ADD_PROPAGATION_EVENTS;
			}
			
			throw new CoronaException(msg);
		} else if (returnCode == CoronaFwkConstants.WARNING) {
			Logger.info(logInfo, "addPropagationEvents","Zero records updated or inserted executing SP "
					+ ADD_PROPAGATION_EVENTS);
		} else if (returnCode == CoronaFwkConstants.SUCCESS) {
			Logger.info(logInfo, "addPropagationEvents","Success executing SP " + ADD_PROPAGATION_EVENTS);
		}

	}
	
	public static void processInStoredProcListPriceUpdate(Long dceId )
		throws CoronaException {
		LoggerInfo logInfo = new LoggerInfo ("com.hp.psg.corona.dataload.dao.DataChangeEventsDao");
		Logger.info(logInfo, "processListPriceUpdate","@@@@@@@@@ Start processListPriceUpdate");
		CTODaxDataBeanGeneral cddbg = new CTODaxDataBeanGeneral();
		cddbg.setDceId(dceId);
		DaxManager.getDAOMgr().executeSP(GROUP_CONFIG_DATA_CHANGE,
				PROCESS_LIST_PRICE_UPDATE, cddbg);

		int returnCode = cddbg.getReturnCode();
		if (returnCode == CoronaFwkConstants.ERROR) {
			String msg = cddbg.getMsg() +" for datachangeevent Id "+ dceId;
			if (msg == null) {
				msg = "Error executing SP " + PROCESS_LIST_PRICE_UPDATE;
			}
			
			throw new CoronaException(msg);
		} else if (returnCode == CoronaFwkConstants.WARNING) {
			Logger.info(logInfo, "processListPriceUpdate","Zero records updated or inserted executing SP "
					+ PROCESS_LIST_PRICE_UPDATE);
		} else if (returnCode == CoronaFwkConstants.SUCCESS) {
			Logger.info(logInfo, "processListPriceUpdate","Success executing SP " + PROCESS_LIST_PRICE_UPDATE);
		}
		
	}

	public static void processInStoredProcListPriceInsert(Long dceId )
		throws CoronaException {
		LoggerInfo logInfo = new LoggerInfo ("com.hp.psg.corona.dataload.dao.DataChangeEventsDao");
		Logger.info(logInfo, "processListPriceInsert","@@@@@@@@@ Start processListPriceInsert");
		CTODaxDataBeanGeneral cddbg = new CTODaxDataBeanGeneral();
		cddbg.setDceId(dceId);
		DaxManager.getDAOMgr().executeSP(GROUP_CONFIG_DATA_CHANGE,
			PROCESS_LIST_PRICE_INSERT, cddbg);

		int returnCode = cddbg.getReturnCode();
		if (returnCode == CoronaFwkConstants.ERROR) {
			String msg = cddbg.getMsg() +" for datachangeevent Id "+ dceId;
			if (msg == null) {
				msg = "Error executing SP " + PROCESS_LIST_PRICE_INSERT;
			}
		
			throw new CoronaException(msg);
		} else if (returnCode == CoronaFwkConstants.WARNING) {
			Logger.info(logInfo, "processListPriceInsert","Zero records updated or inserted executing SP "
				+ PROCESS_LIST_PRICE_INSERT);
		} else if (returnCode == CoronaFwkConstants.SUCCESS) {
			Logger.info(logInfo, "processListPriceInsert","Success executing SP " + PROCESS_LIST_PRICE_INSERT);
		}
	
	}

	
	public static void main(String[] args) {
		LoggerInfo logInfo = new LoggerInfo ("com.hp.psg.corona.dataload.dao.DataChangeEventsDao");
		try {
			
			Logger.info(logInfo, "main","@@@@@@@@@ Start DataChangeEventsDao main");
			// addConfigPermDataChangeEvents();

			PropagationEvent[] propEvents = new PropagationEvent[2];
			propEvents[0] = new PropagationEvent();
			propEvents[0].setProcessKey("C1000^FR");
			propEvents[0].setProcessType("CFG_PERM_TO_CFG_CNTRY");
			propEvents[0].setSource("ECOMCAT");
			propEvents[0].setSrcEventId(new Long(101));
			propEvents[1] = new PropagationEvent();
			propEvents[1].setProcessKey("P123^C1000^FR^FREURDP");
			propEvents[1].setProcessType("CFG_PERM_TO_CFG_PRICE");
			propEvents[1].setSource("ECOMCAT");
			propEvents[1].setSrcEventId(new Long(131));

			addPropagationEvents(propEvents);

			// ConfigPermutationInfo[] cpi = new ConfigPermutationInfo[2];
			//		    
			// cpi[0] = new ConfigPermutationInfo();
			// cpi[0].setPriceId("PID1112");
			// cpi[0].setBundleId("08080219");
			// cpi[0].setShipToCountry("FR");
			// cpi[0].setPriceDescriptor("FREURDP");
			// cpi[0].setGenericPriceFlag("N");
			// cpi[0].setCreatedBy("ECOMCAT");
			// cpi[0].setLastModifiedBy("ECOMCAT");
			// cpi[0].setPriceProcedure("P1234");
			// cpi[0].setOverallStatus("P");
			// cpi[0].setRegionCode("EU");
			// cpi[0].setNewPermFlag("Y");
			// cpi[0].setCustKey("12345-12345");
			// cpi[0].setPriceIdType("PMID");
			// cpi[0].setTrnId(new Long(100));
			// cpi[0].setSrcTrnId(new Long(100));
			// cpi[0].setAction("ADD");
			//
			// cpi[1] = new ConfigPermutationInfo();
			// cpi[1].setPriceId("PID2223");
			// cpi[1].setBundleId("09090319");
			// cpi[1].setShipToCountry("SG");
			// cpi[1].setPriceDescriptor("SGUSDDP");
			// cpi[1].setGenericPriceFlag("N");
			// cpi[1].setCreatedBy("ECOMCAT");
			// cpi[1].setLastModifiedBy("ECOMCAT");
			// cpi[1].setPriceProcedure("P1234");
			// cpi[1].setOverallStatus("P");
			// cpi[1].setRegionCode("AP");
			// cpi[1].setNewPermFlag("Y");
			// cpi[1].setCustKey("AP-676767");
			// cpi[1].setPriceIdType("PMID");
			// cpi[1].setTrnId(new Long(200));
			// cpi[1].setAction("DELETE");
			//		        

			// CTODaxDataBeanGeneral cddbg = getConfigPermDataChange(new
			// Long(0),new Long(101),"P123","C1000","FR","FREURDP");
			// ConfigPermutationInfo[] configPermInfos =
			// cddbg.getConfigPermutationInfo();
			// for (int i = 0; i < configPermInfos.length; i++) {
			// ConfigPermutationInfo configPermutationInfo = configPermInfos[i];
			// System.out.println("id=" + configPermutationInfo.getId());
			// System.out.println("price id=" +
			// configPermutationInfo.getPriceId());
			// System.out.println("bundle id=" +
			// configPermutationInfo.getBundleId());
			// System.out.println("cust key=" +
			// configPermutationInfo.getCustKey());
			// }

			/*
			 * CTODaxDataBeanGeneral cddbg = getConfigPriceDataChange(new
			 * Long(0),new
			 * Long(2),"PCORONA","90144558","US","US","USD","DDP",null);
			 * PriceInfo[] priceInfos = cddbg.getPriceInfo();
			 * System.out.println("@@@@@@@@@ priceInfos length " +
			 * priceInfos.length); for (int i = 0; i < priceInfos.length; i++) {
			 * PriceInfo priceInfo = priceInfos[i]; System.out.println("id=" +
			 * priceInfo.getId()); System.out.println("price id=" +
			 * priceInfo.getPriceId()); System.out.println("bundle id=" +
			 * priceInfo.getBundleId()); System.out.println("src trn id=" +
			 * priceInfo.getSrcTrnId()); System.out.println("prod number=" +
			 * priceInfo.getProductNumber()); System.out.println("prod opt cd=" +
			 * priceInfo.getProductOptionCd()); }
			 * 
			 * processConfigPriceDataChange(priceInfos);
			 */
			/*
			 * CTODaxDataBeanGeneral cddbg = getConfigCntryDataChange(new
			 * Long(0),new Long(3),"10001","FR"); ConfigHeaderInfo[]
			 * configHeaderInfos = cddbg.getConfigHeaderInfo(); for (int i = 0;
			 * i < configHeaderInfos.length; i++) { ConfigHeaderInfo
			 * configHeaderInfo = configHeaderInfos[i]; System.out.println("id=" +
			 * configHeaderInfo.getId()); System.out.println("cntry=" +
			 * configHeaderInfo.getShipToCountry()); System.out.println("bundle
			 * id=" + configHeaderInfo.getBundleId()); } ConfigComponentInfo[]
			 * configCompInfos = cddbg.getConfigComponentInfo(); for (int i = 0;
			 * i < configCompInfos.length; i++) { ConfigComponentInfo
			 * configCompInfo = configCompInfos[i]; System.out.println("id=" +
			 * configCompInfo.getId()); System.out.println("product id=" +
			 * configCompInfo.getProductId()); } ProductDescription[] prodDescs =
			 * cddbg.getProductDescription(); for (int i = 0; i <
			 * prodDescs.length; i++) { ProductDescription prodDesc =
			 * prodDescs[i]; System.out.println("id=" + prodDesc.getId());
			 * System.out.println("product id=" + prodDesc.getProductId());
			 * System.out.println("desc=" + prodDesc.getShortDesc()); }
			 * 
			 * processConfigCntryDataChange(new
			 * Long(0),configHeaderInfos,configCompInfos,prodDescs);
			 */
			// addProductDescDataChangeEvents();
			/*
			 * CTODaxDataBeanGeneral cddbg = getProductDescDataChange(new
			 * Long(0),new Long(1)); ProductDescription[] prodDescs =
			 * cddbg.getProductDescription(); for (int i = 0; i <
			 * prodDescs.length; i++) { ProductDescription prodDesc =
			 * prodDescs[i]; System.out.println("id=" + prodDesc.getId());
			 * System.out.println("product id=" + prodDesc.getProductId());
			 * System.out.println("desc=" + prodDesc.getShortDesc()); }
			 * processProductDescDataChange(prodDescs);
			 */

			System.out.println("@@@@@@@@@ Stop DataChangeEventsDao main");
		} catch (Exception ex) {
			CoronaErrorHandler.logError(ex, null, null);
		}
	}
}
