package com.hp.psg.corona.datachange.handler;

import com.hp.psg.common.error.CoronaException;
import com.hp.psg.common.util.logging.LoggerInfo;
import com.hp.psg.corona.common.beans.CTODaxDataBeanGeneral;
import com.hp.psg.corona.common.beans.PropagationEvent;
import com.hp.psg.corona.common.cto.beans.ConfigComponentInfo;
import com.hp.psg.corona.common.cto.beans.ConfigHeaderInfo;
import com.hp.psg.corona.common.cto.beans.ConfigPermutationInfo;
import com.hp.psg.corona.common.cto.beans.ConfigPriceHeaderInfo;
import com.hp.psg.corona.common.cto.beans.PriceInfo;
import com.hp.psg.corona.common.cto.beans.ProductDescription;
import com.hp.psg.corona.common.handler.HandlerBase;
import com.hp.psg.corona.common.util.Logger;
import com.hp.psg.corona.datachange.dao.DataChangeEventsDao;
import com.hp.psg.corona.datachange.plugin.interfaces.IDataChangeEventsCallHandler;

/*
* @author dudeja
* @version 1.0
*
*/
public class DataChangeCallHandler extends HandlerBase implements IDataChangeEventsCallHandler {

	public static void processConfigCntryDataChange(Long dceId,
			ConfigHeaderInfo[] configHeaderInfo,
			ConfigComponentInfo[] configComponentInfo,
			ProductDescription[] productDescription) throws CoronaException {
		
		LoggerInfo logInfo = new LoggerInfo("DataChangeCallHandler");
		long startTime=System.currentTimeMillis();
		
		DataChangeEventsDao.processConfigCntryDataChange(dceId,
				configHeaderInfo, configComponentInfo, productDescription);
		Logger.perfInfo(logInfo, "processConfigCntryDataChange", "(TimeStamp - "+new java.util.Date()+" ) - processing for processConfigCntryDataChange - "+dceId+" - took processing(timestamp) - "+(System.currentTimeMillis()-startTime)/1000);
		
	}

	public static void processConfigPermDataChange(Long dceId,
			ConfigPermutationInfo[] configPermInfo) throws CoronaException {
		LoggerInfo logInfo = new LoggerInfo("DataChangeCallHandler");

		long startTime=System.currentTimeMillis();
		DataChangeEventsDao.processConfigPermDataChange(dceId, configPermInfo);
		Logger.perfInfo(logInfo, "processConfigPermDataChange", "(TimeStamp - "+new java.util.Date()+" ) - processing for processConfigPermDataChange - "+dceId+" - took processing(timestamp) - "+(System.currentTimeMillis()-startTime)/1000);

	}

	public static void processConfigPriceDataChange(Long dceId,
			ConfigPriceHeaderInfo[] priceHeader, PriceInfo[] priceInfo)
			throws CoronaException {
		LoggerInfo logInfo = new LoggerInfo("DataChangeCallHandler");
		long startTime=System.currentTimeMillis();
		DataChangeEventsDao.processConfigPriceDataChange(dceId, priceHeader,
				priceInfo);
		Logger.perfInfo(logInfo, "processConfigPriceDataChange", "(TimeStamp - "+new java.util.Date()+" ) - processing for processConfigPriceDataChange - "+dceId+" - took processing(timestamp) - "+(System.currentTimeMillis()-startTime)/1000);
	}

	public static void processInStoredProcConfigPriceDataChange(Long dceId)
			throws CoronaException {
		LoggerInfo logInfo = new LoggerInfo("DataChangeCallHandler");
		long startTime=System.currentTimeMillis();
		DataChangeEventsDao.processConfigPrice(dceId);
		Logger.perfInfo(logInfo, "processConfigPrice", "(TimeStamp - "+new java.util.Date()+" ) - processing for processConfigPrice - "+dceId+" - took processing(timestamp) - "+(System.currentTimeMillis()-startTime)/1000);
	}

	public static void processInStoredProcListPriceUpdate(Long dceId)
		throws CoronaException {
		LoggerInfo logInfo = new LoggerInfo("DataChangeCallHandler");
		long startTime=System.currentTimeMillis();
		DataChangeEventsDao.processInStoredProcListPriceUpdate(dceId);
		Logger.perfInfo(logInfo, "processListPriceUpdate", "(TimeStamp - "+new java.util.Date()+" ) - processing for processListPriceUpdate - "+dceId+" - took processing(timestamp) - "+(System.currentTimeMillis()-startTime)/1000);
	}

	public static void processInStoredProcListPriceInsert(Long dceId)
	throws CoronaException {
		LoggerInfo logInfo = new LoggerInfo("DataChangeCallHandler");
		long startTime=System.currentTimeMillis();
		DataChangeEventsDao.processInStoredProcListPriceInsert(dceId);
		Logger.perfInfo(logInfo, "processListPriceInsert", "(TimeStamp - "+new java.util.Date()+" ) - processing for processListPriceUpdate - "+dceId+" - took processing(timestamp) - "+(System.currentTimeMillis()-startTime)/1000);
}
	
	
	public static void processProductDescDataChange(Long dceId,
			ProductDescription[] productDescription) throws CoronaException {
		LoggerInfo logInfo = new LoggerInfo("DataChangeCallHandler");
		long startTime=System.currentTimeMillis();
		DataChangeEventsDao.processProductDescDataChange(dceId,
				productDescription);
		Logger.perfInfo(logInfo, "processProductDescDataChange", "(TimeStamp - "+new java.util.Date()+" ) - processing for processProductDescDataChange - "+dceId+" - took processing(timestamp) - "+(System.currentTimeMillis()-startTime)/1000);
	}

	public static CTODaxDataBeanGeneral getConfigPermDataChange(Long dceId,
			Long trnId, String priceId, String bundleId, String shipCntry,
			String priceDescriptor) throws CoronaException {
		LoggerInfo logInfo = new LoggerInfo("DataChangeCallHandler");
		long startTime=System.currentTimeMillis();
		CTODaxDataBeanGeneral ctxBean= DataChangeEventsDao.getConfigPermDataChange(dceId, trnId,
				priceId, bundleId, shipCntry, priceDescriptor);
		Logger.perfInfo(logInfo, "getConfigPermDataChange", "(TimeStamp - "+new java.util.Date()+" ) - processing for getConfigPermDataChange - "+dceId+" - took processing(timestamp) - "+(System.currentTimeMillis()-startTime)/1000);
		return ctxBean;
	}

	public static CTODaxDataBeanGeneral getConfigCntryDataChange(Long dceId,
			Long trnId, String bundleId, String shipCntry)
			throws CoronaException {
		LoggerInfo logInfo = new LoggerInfo("DataChangeCallHandler");
		long startTime=System.currentTimeMillis();
		CTODaxDataBeanGeneral ctxBean= DataChangeEventsDao.getConfigCntryDataChange(dceId, trnId,
				bundleId, shipCntry);
		Logger.perfInfo(logInfo, "getConfigCntryDataChange", "(TimeStamp - "+new java.util.Date()+" ) - processing for getConfigCntryDataChange - "+dceId+" - took processing(timestamp) - "+(System.currentTimeMillis()-startTime)/1000);
		return ctxBean;
	}

	public static CTODaxDataBeanGeneral getConfigPriceDataChange(Long dceId,
			Long trnId, String priceId, String bundleId, String shipToGeo,
			String priceGeo, String currency, String incoTerm,
			String priceListType) throws CoronaException {
		LoggerInfo logInfo = new LoggerInfo("DataChangeCallHandler");
		long startTime=System.currentTimeMillis();
		CTODaxDataBeanGeneral ctxBean= DataChangeEventsDao.getConfigPriceDataChange(dceId, trnId,
				priceId, bundleId, shipToGeo, priceGeo, currency, incoTerm,
				priceListType);
		Logger.perfInfo(logInfo, "getConfigPriceDataChange", "(TimeStamp - "+new java.util.Date()+" ) - processing for getConfigPriceDataChange - "+dceId+" - took processing(timestamp) - "+(System.currentTimeMillis()-startTime)/1000);
		return ctxBean;
	}

	public static CTODaxDataBeanGeneral getProductDescDataChange(Long dceId,
			Long trnId,String productId) throws CoronaException {
		LoggerInfo logInfo = new LoggerInfo("DataChangeCallHandler");
		long startTime=System.currentTimeMillis();
		CTODaxDataBeanGeneral ctxBean= DataChangeEventsDao.getProductDescDataChange(dceId, trnId, productId);
		Logger.perfInfo(logInfo, "getProductDescDataChange", "(TimeStamp - "+new java.util.Date()+" ) - processing for getProductDescDataChange - "+dceId+" - took processing(timestamp) - "+(System.currentTimeMillis()-startTime)/1000);
		return ctxBean;
	}

	public static void addPropagationEvents(PropagationEvent[] propEvent)
			throws CoronaException {
		LoggerInfo logInfo = new LoggerInfo("DataChangeCallHandler");
		long startTime=System.currentTimeMillis();
		DataChangeEventsDao.addPropagationEvents(propEvent);
		Logger.perfInfo(logInfo, "addPropagationEvents", "(TimeStamp - "+new java.util.Date()+" ) - addPropagationEvents -  - took processing(timestamp)  - "+(System.currentTimeMillis()-startTime)/1000);
	}

	public void processConfigPriceDataChange(Long dceId)
	throws CoronaException {
		processInStoredProcConfigPriceDataChange(dceId);
	}
	
	public void processListPriceInsert(Long dceId) throws CoronaException {
		// TODO Auto-generated method stub
		processInStoredProcListPriceInsert(dceId);
	}

	public void processListPriceUpdate(Long dceId) throws CoronaException {
		// TODO Auto-generated method stub
		processInStoredProcListPriceUpdate(dceId);
	}
}
