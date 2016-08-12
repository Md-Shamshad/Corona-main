package com.hp.psg.corona.propagation.dao;

import com.hp.psg.common.db.util.DaxManager;
import com.hp.psg.common.error.CoronaException;
import com.hp.psg.common.util.logging.LoggerInfo;
import com.hp.psg.corona.common.beans.CTODaxDataBeanGeneral;
import com.hp.psg.corona.common.constants.CoronaFwkConstants;
import com.hp.psg.corona.common.cto.beans.ConfigDescription;
import com.hp.psg.corona.common.cto.beans.ConfigHeaderInfo;
import com.hp.psg.corona.common.cto.beans.ConfigPermutationInfo;
import com.hp.psg.corona.common.cto.beans.ConfigPermutationPriceInfo;
import com.hp.psg.corona.common.cto.beans.ConfigPriceHeaderInfo;
import com.hp.psg.corona.common.cto.beans.PriceInfo;
import com.hp.psg.corona.common.util.Logger;
import com.hp.psg.corona.error.util.CoronaErrorHandler;

/**
 * @author ghatta
 * @version 1.0
 */
public class PropagationEventsDao {

	private static String GROUP_CONFIG_PROP_EVENT = "GROUP_CONFIG_PROP_EVENT";
	private static String PROCESS_CONFIG_PERM_UPDATE = "PROCESS_CONFIG_PERM_UPDATE";
	private static String PROCESS_CONFIG_PRICE_UPDATE = "PROCESS_CONFIG_PRICE_UPDATE";
	private static String PROCESS_CONFIG_CNTRY_UPDATE = "PROCESS_CONFIG_CNTRY_UPDATE";
	private static String PROCESS_CONFIG_PERM_PRICE_UPDATE = "PROCESS_CONFIG_PERM_PRICE_UPDATE";
	private static String PROCESS_CONFIG_DESC_UPDATE = "PROCESS_CONFIG_DESC_UPDATE";
	private static String GET_CONFIG_PERM_UPDATE = "GET_CONFIG_PERM_UPDATE";
	private static String GET_CONFIG_PRICE_UPDATE = "GET_CONFIG_PRICE_UPDATE";
	private static String GET_CONFIG_CNTRY_UPDATE = "GET_CONFIG_CNTRY_UPDATE";
	private static String GET_CONFIG_PERM_PRICE_UPDATE = "GET_CONFIG_PERM_PRICE_UPDATE";
	private static String GET_CONFIG_DESC_UPDATE = "GET_CONFIG_DESC_UPDATE";
	private static String PROCESS_CONFIG_PRICE_PROP = "PROCESS_CONFIG_PRICE_PROP";
	private static String PROCESS_LIST_PRICE_ADD = "PROCESS_LIST_PRICE_ADD";

	public static void processConfigPermUpdate(Long peId,
			ConfigPermutationInfo[] cfgPermInfo) throws CoronaException {
		LoggerInfo logInfo = new LoggerInfo ("com.hp.psg.corona.dataload.dao.PropagationEventsDao");
		
		Logger.info(logInfo, "processConfigPermUpdate","@@@@@@@@@ Start processConfigPermUpdate");
		CTODaxDataBeanGeneral cddbg = new CTODaxDataBeanGeneral();
		cddbg.setPeId(peId);
		cddbg.setConfigPermutationInfo(cfgPermInfo);
		DaxManager.getDAOMgr().executeSP(GROUP_CONFIG_PROP_EVENT,
				PROCESS_CONFIG_PERM_UPDATE, cddbg);

		int returnCode = cddbg.getReturnCode();
		if (returnCode == CoronaFwkConstants.ERROR) {
			String msg = cddbg.getMsg() +" for propagationEvent Id "+ peId;
			if (msg == null) {
				msg = "Error executing SP " + PROCESS_CONFIG_PERM_UPDATE;
			}
			throw new CoronaException(msg);
		} else if (returnCode == CoronaFwkConstants.WARNING) {
			Logger.info(logInfo, "processConfigPermUpdate","Zero records updated or inserted executing SP "
					+ PROCESS_CONFIG_PERM_UPDATE);
		} else if (returnCode == CoronaFwkConstants.SUCCESS) {
			Logger.info(logInfo, "processConfigPermUpdate","Success executing SP " + PROCESS_CONFIG_PERM_UPDATE);
		}
	}

	public static void processConfigPermPriceUpdate(Long peId,
			ConfigPermutationPriceInfo[] cfgPermPriceInfo)
			throws CoronaException {
		LoggerInfo logInfo = new LoggerInfo ("com.hp.psg.corona.dataload.dao.PropagationEventsDao");
		Logger.info(logInfo, "processConfigPermPriceUpdate","@@@@@@@@@ Start processConfigPermPriceUpdate");
		CTODaxDataBeanGeneral cddbg = new CTODaxDataBeanGeneral();
		cddbg.setPeId(peId);
		cddbg.setConfigPermutationPriceInfo(cfgPermPriceInfo);
		DaxManager.getDAOMgr().executeSP(GROUP_CONFIG_PROP_EVENT,
				PROCESS_CONFIG_PERM_PRICE_UPDATE, cddbg);

		int returnCode = cddbg.getReturnCode();
		if (returnCode == CoronaFwkConstants.ERROR) {
			String msg = cddbg.getMsg() +" for propagationEvent Id "+ peId;
			if (msg == null) {
				msg = "Error executing SP " + PROCESS_CONFIG_PERM_PRICE_UPDATE;
			}
			throw new CoronaException(msg);
		} else if (returnCode == CoronaFwkConstants.WARNING) {
			Logger.info(logInfo, "processConfigPermPriceUpdate","Zero records updated or inserted executing SP "
					+ PROCESS_CONFIG_PERM_PRICE_UPDATE);
		} else if (returnCode == CoronaFwkConstants.SUCCESS) {
			Logger.info(logInfo, "processConfigPermPriceUpdate","Success executing SP "
					+ PROCESS_CONFIG_PERM_PRICE_UPDATE);
		}
	}

	public static void processConfigPriceUpdate(Long peId,
			ConfigPriceHeaderInfo[] priceHeader, PriceInfo[] priceInfo)
			throws CoronaException {
		LoggerInfo logInfo = new LoggerInfo ("com.hp.psg.corona.dataload.dao.PropagationEventsDao");
		Logger.info(logInfo, "processConfigPriceUpdate","@@@@@@@@@ Start processConfigPriceUpdate");
		CTODaxDataBeanGeneral cddbg = new CTODaxDataBeanGeneral();
		cddbg.setPeId(peId);
		cddbg.setConfigPriceHeaderInfo(priceHeader);
		cddbg.setPriceInfo(priceInfo);
		DaxManager.getDAOMgr().executeSP(GROUP_CONFIG_PROP_EVENT,
				PROCESS_CONFIG_PRICE_UPDATE, cddbg);

		int returnCode = cddbg.getReturnCode();
		if (returnCode == CoronaFwkConstants.ERROR) {
			String msg = cddbg.getMsg() +" for propagationEvent Id "+ peId;
			if (msg == null) {
				msg = "Error executing SP " + PROCESS_CONFIG_PRICE_UPDATE;
			}
			throw new CoronaException(msg);
		} else if (returnCode == CoronaFwkConstants.WARNING) {
			Logger.info(logInfo, "processConfigPriceUpdate","Zero records updated or inserted executing SP "
					+ PROCESS_CONFIG_PRICE_UPDATE);
		} else if (returnCode == CoronaFwkConstants.SUCCESS) {
			Logger.info(logInfo, "processConfigPriceUpdate","Success executing SP " + PROCESS_CONFIG_PRICE_UPDATE);
		}
	}

	
	public static void processConfigPriceUpdateStoedProc(Long peId)
			throws CoronaException {
		LoggerInfo logInfo = new LoggerInfo ("com.hp.psg.corona.dataload.dao.PropagationEventsDao");
		Logger.info(logInfo, "processConfigPriceUpdateStoedProc","@@@@@@@@@ Start processConfigPriceUpdateStoedProc");
		CTODaxDataBeanGeneral cddbg = new CTODaxDataBeanGeneral();
		cddbg.setPeId(peId);
		DaxManager.getDAOMgr().executeSP(GROUP_CONFIG_PROP_EVENT,
				PROCESS_CONFIG_PRICE_PROP, cddbg);
		int returnCode = cddbg.getReturnCode();
		if (returnCode == CoronaFwkConstants.ERROR) {
			String msg = cddbg.getMsg() +" for propagationevent Id "+ peId;
			if (msg == null) {
				msg = "Error executing SP " + PROCESS_CONFIG_PRICE_PROP;
			}
			
			throw new CoronaException(msg);
		} else if (returnCode == CoronaFwkConstants.WARNING) {
			Logger.info(logInfo, "processConfigPriceUpdateStoedProc","Zero records updated or inserted executing SP "
					+ PROCESS_CONFIG_PRICE_PROP);
		} else if (returnCode == CoronaFwkConstants.SUCCESS) {
			Logger.info(logInfo, "processConfigPriceUpdateStoedProc","Success executing SP " + PROCESS_CONFIG_PRICE_PROP);
		}
	}

	
	public static void processConfigCntryUpdate(Long peId,
			ConfigHeaderInfo[] configHeaderInfo) throws CoronaException {
		LoggerInfo logInfo = new LoggerInfo ("com.hp.psg.corona.dataload.dao.PropagationEventsDao");
		Logger.info(logInfo, "processConfigCntryUpdate","@@@@@@@@@ Start processConfigCntryUpdate");
		CTODaxDataBeanGeneral cddbg = new CTODaxDataBeanGeneral();
		cddbg.setPeId(peId);
		cddbg.setConfigHeaderInfo(configHeaderInfo);
		DaxManager.getDAOMgr().executeSP(GROUP_CONFIG_PROP_EVENT,
				PROCESS_CONFIG_CNTRY_UPDATE, cddbg);

		int returnCode = cddbg.getReturnCode();
		if (returnCode == CoronaFwkConstants.ERROR) {
			String msg = cddbg.getMsg() +" for propagationEvent Id "+ peId;
			if (msg == null) {
				msg = "Error executing SP " + PROCESS_CONFIG_CNTRY_UPDATE;
			}

			throw new CoronaException(msg);
		} else if (returnCode == CoronaFwkConstants.WARNING) {
			Logger.info(logInfo, "processConfigCntryUpdate","Zero records updated or inserted executing SP "
					+ PROCESS_CONFIG_CNTRY_UPDATE);
		} else if (returnCode == CoronaFwkConstants.SUCCESS) {
			Logger.info(logInfo, "processConfigCntryUpdate","Success executing SP " + PROCESS_CONFIG_CNTRY_UPDATE);
		}
	}

	public static void processConfigDescUpdate(Long peId,
			ConfigDescription[] cfgDesc) throws CoronaException {
		LoggerInfo logInfo = new LoggerInfo ("com.hp.psg.corona.dataload.dao.PropagationEventsDao");
		Logger.info(logInfo, "processConfigDescUpdate","@@@@@@@@@ Start processConfigDescUpdate");
		CTODaxDataBeanGeneral cddbg = new CTODaxDataBeanGeneral();
		cddbg.setPeId(peId);
		cddbg.setConfigDescription(cfgDesc);
		DaxManager.getDAOMgr().executeSP(GROUP_CONFIG_PROP_EVENT,
				PROCESS_CONFIG_DESC_UPDATE, cddbg);

		int returnCode = cddbg.getReturnCode();
		if (returnCode == CoronaFwkConstants.ERROR) {
			String msg = cddbg.getMsg() +" for propagationEvent Id "+ peId;
			if (msg == null) {
				msg = "Error executing SP " + PROCESS_CONFIG_DESC_UPDATE;
			}

			throw new CoronaException(msg);
		} else if (returnCode == CoronaFwkConstants.WARNING) {
			Logger.info(logInfo, "processConfigDescUpdate","Zero records updated or inserted executing SP "
					+ PROCESS_CONFIG_DESC_UPDATE);
		} else if (returnCode == CoronaFwkConstants.SUCCESS) {
			Logger.info(logInfo, "processConfigDescUpdate","Success executing SP " + PROCESS_CONFIG_DESC_UPDATE);
		}
	}

	public static CTODaxDataBeanGeneral getConfigPermUpdate(Long peId,
			Long srcEventId, String priceId, String bundleId, String shipCntry,
			String priceDescriptor) throws CoronaException {
		LoggerInfo logInfo = new LoggerInfo ("com.hp.psg.corona.dataload.dao.PropagationEventsDao");
		Logger.info(logInfo, "getConfigPermUpdate","@@@@@@@@@ Start getConfigPermUpdate");
		CTODaxDataBeanGeneral cddbg = new CTODaxDataBeanGeneral();
		cddbg.setPeId(peId);
		cddbg.setSrcEventId(srcEventId);
		cddbg.setBundleId(bundleId);
		cddbg.setPriceId(priceId);
		cddbg.setShipToCountry(shipCntry);
		cddbg.setPriceDescriptor(priceDescriptor);

		DaxManager.getDAOMgr().executeSP(GROUP_CONFIG_PROP_EVENT,
				GET_CONFIG_PERM_UPDATE, cddbg);

		int returnCode = cddbg.getReturnCode();
		if (returnCode == CoronaFwkConstants.ERROR) {
			String msg = cddbg.getMsg() +" for propagationEvent Id "+ peId;
			if (msg == null) {
				msg = "Error executing SP " + GET_CONFIG_PERM_UPDATE;
			}
			
			throw new CoronaException(msg);
		} else if (returnCode == CoronaFwkConstants.MULTIPLE_ROWS) {
			String msg = cddbg.getMsg() +" for propagationEvent Id "+ peId;
			if (msg == null) {
				msg = "Multiple CPPI rows returned " + PROCESS_CONFIG_DESC_UPDATE;
			}
			throw new CoronaException(msg);
		} else if (returnCode == CoronaFwkConstants.WARNING) {
			Logger.info(logInfo, "getConfigPermUpdate","Zero records updated or inserted executing SP "
					+ GET_CONFIG_PERM_UPDATE);
		} else if (returnCode == CoronaFwkConstants.SUCCESS) {
			Logger.info(logInfo, "getConfigPermUpdate","Success executing SP " + GET_CONFIG_PERM_UPDATE);
		}

		
		return cddbg;

	}

	public static CTODaxDataBeanGeneral getConfigPermPriceUpdate(Long peId,
			Long srcEventId, String priceId, String bundleId, String shipCntry,
			String priceDescriptor) throws CoronaException {
		LoggerInfo logInfo = new LoggerInfo ("com.hp.psg.corona.dataload.dao.PropagationEventsDao");
		Logger.info(logInfo, "getConfigPermPriceUpdate","@@@@@@@@@ Start getConfigPermPriceUpdate");
		CTODaxDataBeanGeneral cddbg = new CTODaxDataBeanGeneral();
		cddbg.setPeId(peId);
		cddbg.setSrcEventId(srcEventId);
		cddbg.setBundleId(bundleId);
		cddbg.setPriceId(priceId);
		cddbg.setShipToCountry(shipCntry);
		cddbg.setPriceDescriptor(priceDescriptor);

		DaxManager.getDAOMgr().executeSP(GROUP_CONFIG_PROP_EVENT,
				GET_CONFIG_PERM_PRICE_UPDATE, cddbg);

		int returnCode = cddbg.getReturnCode();
		if (returnCode == CoronaFwkConstants.ERROR) {
			String msg = cddbg.getMsg() +" for propagationEvent Id "+ peId;
			if (msg == null) {
				msg = "Error executing SP " + GET_CONFIG_PERM_PRICE_UPDATE;
			}
			
			throw new CoronaException(msg);
		} else if (returnCode == CoronaFwkConstants.WARNING) {
			Logger.info(logInfo, "getConfigPermPriceUpdate","Zero records updated or inserted executing SP "
					+ GET_CONFIG_PERM_PRICE_UPDATE);
		} else if (returnCode == CoronaFwkConstants.SUCCESS) {
			Logger.info(logInfo, "getConfigPermPriceUpdate","Success executing SP " + GET_CONFIG_PERM_PRICE_UPDATE);
		}

		return cddbg;

	}

	public static CTODaxDataBeanGeneral getConfigPriceUpdate(Long peId,
			Long srcEventId, String priceId, String bundleId, String shipCntry,
			String priceDescriptor) throws CoronaException {
		LoggerInfo logInfo = new LoggerInfo ("com.hp.psg.corona.dataload.dao.PropagationEventsDao");
		Logger.info(logInfo, "getConfigPriceUpdate","@@@@@@@@@ Start getConfigPriceUpdate");
		CTODaxDataBeanGeneral cddbg = new CTODaxDataBeanGeneral();
		cddbg.setPeId(peId);
		cddbg.setSrcEventId(srcEventId);
		cddbg.setBundleId(bundleId);
		cddbg.setPriceId(priceId);
		cddbg.setShipToCountry(shipCntry);
		cddbg.setPriceDescriptor(priceDescriptor);

		DaxManager.getDAOMgr().executeSP(GROUP_CONFIG_PROP_EVENT,
				GET_CONFIG_PRICE_UPDATE, cddbg);

		int returnCode = cddbg.getReturnCode();
		if (returnCode == CoronaFwkConstants.ERROR) {
			String msg = cddbg.getMsg() +" for propagationEvent Id "+ peId;
			if (msg == null) {
				msg = "Error executing SP " + GET_CONFIG_PRICE_UPDATE;
			}
			
			throw new CoronaException(msg);
		} else if (returnCode == CoronaFwkConstants.WARNING) {
			Logger.info(logInfo, "getConfigPriceUpdate","Zero records updated or inserted executing SP "
					+ GET_CONFIG_PRICE_UPDATE);
		} else if (returnCode == CoronaFwkConstants.SUCCESS) {
			Logger.info(logInfo, "getConfigPriceUpdate","Success executing SP " + GET_CONFIG_PRICE_UPDATE);
		}

		return cddbg;

	}

	public static CTODaxDataBeanGeneral getConfigCntryUpdate(Long peId,
			Long srcEventId, String bundleId, String shipCntry)
			throws CoronaException {
		LoggerInfo logInfo = new LoggerInfo ("com.hp.psg.corona.dataload.dao.PropagationEventsDao");
		Logger.info(logInfo, "getConfigCntryUpdate","@@@@@@@@@ Start getConfigCntryUpdate");
		CTODaxDataBeanGeneral cddbg = new CTODaxDataBeanGeneral();
		cddbg.setPeId(peId);
		cddbg.setSrcEventId(srcEventId);
		cddbg.setBundleId(bundleId);
		cddbg.setShipToCountry(shipCntry);

		DaxManager.getDAOMgr().executeSP(GROUP_CONFIG_PROP_EVENT,
				GET_CONFIG_CNTRY_UPDATE, cddbg);

		int returnCode = cddbg.getReturnCode();
		if (returnCode == CoronaFwkConstants.ERROR) {
			String msg = cddbg.getMsg() +" for propagationEvent Id "+ peId;
			if (msg == null) {
				msg = "Error executing SP " + GET_CONFIG_CNTRY_UPDATE;
			}
			
			throw new CoronaException(msg);
		} else if (returnCode == CoronaFwkConstants.WARNING) {
			Logger.info(logInfo, "getConfigCntryUpdate","Zero records updated or inserted executing SP "
					+ GET_CONFIG_CNTRY_UPDATE);
		} else if (returnCode == CoronaFwkConstants.SUCCESS) {
			Logger.info(logInfo, "getConfigCntryUpdate","Success executing SP " + GET_CONFIG_CNTRY_UPDATE);
		}

		return cddbg;

	}

	public static CTODaxDataBeanGeneral getConfigDescUpdate(Long peId,
			Long srcEventId, String bundleId, String shipCntry)
			throws CoronaException {
		LoggerInfo logInfo = new LoggerInfo ("com.hp.psg.corona.dataload.dao.PropagationEventsDao");
		Logger.info(logInfo, "getConfigDescUpdate","@@@@@@@@@ Start getConfigDescUpdate");
		CTODaxDataBeanGeneral cddbg = new CTODaxDataBeanGeneral();
		cddbg.setPeId(peId);
		cddbg.setSrcEventId(srcEventId);
		cddbg.setBundleId(bundleId);
		cddbg.setShipToCountry(shipCntry);

		DaxManager.getDAOMgr().executeSP(GROUP_CONFIG_PROP_EVENT,
				GET_CONFIG_DESC_UPDATE, cddbg);

		int returnCode = cddbg.getReturnCode();
		if (returnCode == CoronaFwkConstants.ERROR) {
			String msg = cddbg.getMsg() +" for propagationEvent Id "+ peId;
			if (msg == null) {
				msg = "Error executing SP " + GET_CONFIG_DESC_UPDATE;
			}
			
			throw new CoronaException(msg);
		} else if (returnCode == CoronaFwkConstants.WARNING) {
			Logger.info(logInfo, "getConfigDescUpdate","Zero records updated or inserted executing SP "
					+ GET_CONFIG_DESC_UPDATE);
		} else if (returnCode == CoronaFwkConstants.SUCCESS) {
			Logger.info(logInfo, "getConfigDescUpdate","Success executing SP " + GET_CONFIG_DESC_UPDATE);
		}

		return cddbg;

	}
	
	public static void processInStoredProcListPriceAdd(Long peId)
	throws CoronaException {
		LoggerInfo logInfo = new LoggerInfo ("com.hp.psg.corona.dataload.dao.PropagationEventsDao");
		Logger.info(logInfo, "processListPriceAdd","@@@@@@@@@ Start processListPriceAdd");
		CTODaxDataBeanGeneral cddbg = new CTODaxDataBeanGeneral();
		cddbg.setPeId(peId);
		DaxManager.getDAOMgr().executeSP(GROUP_CONFIG_PROP_EVENT,
				PROCESS_LIST_PRICE_ADD, cddbg);
		int returnCode = cddbg.getReturnCode();
		if (returnCode == CoronaFwkConstants.ERROR) {
			String msg = cddbg.getMsg() +" for propagationevent Id "+ peId;
			if (msg == null) {
				msg = "Error executing SP " + PROCESS_LIST_PRICE_ADD;
			}
			throw new CoronaException(msg);
		} else if (returnCode == CoronaFwkConstants.WARNING) {
			Logger.info(logInfo, "processListPriceAdd","Zero records updated or inserted executing SP "
					+ PROCESS_LIST_PRICE_ADD);
		} else if (returnCode == CoronaFwkConstants.SUCCESS) {
			Logger.info(logInfo, "processListPriceAdd","Success executing SP " + PROCESS_LIST_PRICE_ADD);
		}
	}

	public static void main(String[] args) {
		LoggerInfo logInfo = new LoggerInfo ("com.hp.psg.corona.dataload.dao.PropagationEventsDao");
		
		try {
			
			Logger.info(logInfo, "main","@@@@@@@@@ Start PropagationEventsDao main");

			ConfigPermutationPriceInfo[] cppi = new ConfigPermutationPriceInfo[2];
			/*
			 * cppi[0] = new ConfigPermutationPriceInfo();
			 * cppi[0].setPriceId("P125"); cppi[0].setBundleId("C1000");
			 * cppi[0].setShipToCountry("FR");
			 * cppi[0].setPriceDescriptor("FREURDP");
			 * cppi[0].setCreatedBy("ECOMCAT");
			 * cppi[0].setLastModifiedBy("ECOMCAT");
			 * cppi[0].setPriceStartDate(Date.valueOf("1582-10-10"));
			 * cppi[0].setPriceEndDate(Date.valueOf("1582-10-31"));
			 * cppi[0].setPriceStatus("V"); cppi[0].setPriceErrMsg(null);
			 * cppi[0].setDelFlag("N"); cppi[0].setUpdtFlag("A");
			 * 
			 * cppi[1] = new ConfigPermutationPriceInfo();
			 * cppi[1].setPriceId("P125"); cppi[1].setBundleId("C1000");
			 * cppi[1].setShipToCountry("FR");
			 * cppi[1].setPriceDescriptor("FREURDP");
			 * cppi[1].setCreatedBy("ECOMCAT");
			 * cppi[1].setLastModifiedBy("ECOMCAT");
			 * cppi[1].setPriceStartDate(Date.valueOf("1582-11-10"));
			 * cppi[1].setPriceEndDate(Date.valueOf("1582-11-30"));
			 * cppi[1].setPriceStatus("V"); cppi[1].setPriceErrMsg(null);
			 * cppi[1].setDelFlag("N"); cppi[1].setUpdtFlag("A");
			 * 
			 * processConfigPermPriceUpdate(new Long(0),cppi);
			 * 
			 */

			ConfigHeaderInfo[] chi = new ConfigHeaderInfo[1];
			chi[0] = new ConfigHeaderInfo();
			chi[0].setBundleId("C1000");
			chi[0].setShipToCountry("FR");
			chi[0].setDelFlag("N");
			chi[0].setCreatedBy("ECOMCAT");
			chi[0].setLastModifiedBy("ECOMCAT");
			chi[0].setRegionCode("EU");
			chi[0].setUpdtFlag("M");
			chi[0].setTrnId(new Long(0));
			processConfigCntryUpdate(new Long(0), chi);

			/*
			 * PriceInfo[] pi = new PriceInfo[1]; pi[0] = new PriceInfo();
			 * pi[0].setRecipient("Recepient");
			 * pi[0].setPriceProcedure("PProc"); pi[0].setPriceId("P1234");
			 * pi[0].setPriceIdType("OFFER"); pi[0].setBundleId("C1234");
			 * pi[0].setShipToCountry("FR");
			 * pi[0].setPriceDescriptor("FREURDP"); pi[0].setPriceGeo("FR");
			 * pi[0].setShipToGeo("FR"); pi[0].setCurrency("CR");
			 * pi[0].setIncoTerm("DP"); pi[0].setCreatedBy("Test price prop");
			 * pi[0].setLastModifiedBy("Test price prop");
			 * pi[0].setDelFlag("N"); pi[0].setConfigDelFlag("N");
			 * pi[0].setProductId("abcd#123"); pi[0].setProductNumber("abcd");
			 * pi[0].setProductOptionCd("123"); pi[0].setGenericPriceFlag("N");
			 * pi[0].setUpdtFlag("A");
			 * 
			 * processCfgCntryToCfgPrice(new Long(0),pi);
			 * 
			 */
			/*
			 * CTODaxDataBeanGeneral cddbg = getConfigDescUpdate(new Long(0),new
			 * Long(101),"10001","FR"); ConfigHeaderInfo[] configHeaderInfos =
			 * cddbg.getConfigHeaderInfo(); for (int i = 0; i <
			 * configHeaderInfos.length; i++) { ConfigHeaderInfo
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
			 * 
			 * ConfigDescription[] cd = new ConfigDescription[1]; cd[0] = new
			 * ConfigDescription(); cd[0].setBundleId("08080220");
			 * cd[0].setShipToCountry("FR");
			 * cd[0].setLongDesc("aaaaaaaaaaaaaaaaa"); cd[0].setLocale("en_US");
			 * cd[0].setCreatedBy("test"); cd[0].setLastModifiedBy("test");
			 * cd[0].setDeleteFlag("N"); processCfgCntryToCfgDesc(cd);
			 * 
			 */
			/*
			 * CTODaxDataBeanGeneral cddbg = getConfigCntryUpdate(new
			 * Long(0),new Long(101),"04755164","MY"); ConfigPermutationInfo[]
			 * configPermInfos = cddbg.getConfigPermutationInfo(); for (int i =
			 * 0; i < configPermInfos.length; i++) { ConfigPermutationInfo
			 * configPermutationInfo = configPermInfos[i];
			 * System.out.println("bundle id=" +
			 * configPermutationInfo.getBundleId()); System.out.println("ship to
			 * country=" + configPermutationInfo.getShipToCountry());
			 * System.out.println("delete flag=" +
			 * configPermutationInfo.getDelFlag()); }
			 */
			System.out.println("@@@@@@@@@ Stop PropagationEventsDao main");
		} catch (Exception ex) {
			System.err.println("Caught an unexpected exception!");
			CoronaErrorHandler.logError(ex, null, null);
		}
	}

}
