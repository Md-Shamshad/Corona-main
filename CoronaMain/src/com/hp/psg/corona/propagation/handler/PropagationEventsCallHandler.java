package com.hp.psg.corona.propagation.handler;

import com.hp.psg.common.error.CoronaException;
import com.hp.psg.common.util.logging.LoggerInfo;
import com.hp.psg.corona.common.beans.CTODaxDataBeanGeneral;
import com.hp.psg.corona.common.cto.beans.ConfigDescription;
import com.hp.psg.corona.common.cto.beans.ConfigHeaderInfo;
import com.hp.psg.corona.common.cto.beans.ConfigPermutationInfo;
import com.hp.psg.corona.common.cto.beans.ConfigPermutationPriceInfo;
import com.hp.psg.corona.common.cto.beans.ConfigPriceHeaderInfo;
import com.hp.psg.corona.common.cto.beans.PriceInfo;
import com.hp.psg.corona.common.handler.HandlerBase;
import com.hp.psg.corona.common.util.Logger;
import com.hp.psg.corona.propagation.dao.PropagationEventsDao;

/*
* @author dudeja
* @version 1.0
*
*/
public class PropagationEventsCallHandler extends HandlerBase {

	public static void processConfigPermUpdate(Long peId,
			ConfigPermutationInfo[] cfgPermInfo) throws CoronaException {
		LoggerInfo logInfo = new LoggerInfo("PropagationEventsCallHandler");
		long startTime=System.currentTimeMillis();
		PropagationEventsDao.processConfigPermUpdate(peId, cfgPermInfo);
		Logger.perfInfo(logInfo, "processConfigPermUpdate", "(TimeStamp - "+new java.util.Date()+" ) - processing for processConfigPermUpdate - "+peId+" - took processing(timestamp) - "+(System.currentTimeMillis()-startTime)/1000);
	}

	public static void processConfigPriceUpdate(Long peId,
			ConfigPriceHeaderInfo[] priceHeader, PriceInfo[] priceInfo)
			throws CoronaException {
		LoggerInfo logInfo = new LoggerInfo("PropagationEventsCallHandler");
		long startTime=System.currentTimeMillis();
		PropagationEventsDao.processConfigPriceUpdate(peId, priceHeader,
				priceInfo);
		Logger.perfInfo(logInfo, "processConfigPriceUpdate", "(TimeStamp - "+new java.util.Date()+" ) - processing for processConfigPriceUpdate - "+peId+" - took processing(timestamp) - "+(System.currentTimeMillis()-startTime)/1000);
	}

	public static void processInStoredProcConfigPriceUpdate(Long peId)
	throws CoronaException {
		LoggerInfo logInfo = new LoggerInfo("PropagationEventsCallHandler");
		long startTime=System.currentTimeMillis();
		PropagationEventsDao.processConfigPriceUpdateStoedProc(peId);
		Logger.perfInfo(logInfo, "processInStoredProcConfigPriceUpdate", "(TimeStamp - "+new java.util.Date()+" ) - processing for processInStoredProcConfigPriceUpdate - "+peId+" - took processing(timestamp) - "+(System.currentTimeMillis()-startTime)/1000);
	}

	public static void processInStoredProcListPriceAdd(Long peId)
	throws CoronaException {
		LoggerInfo logInfo = new LoggerInfo("PropagationEventsCallHandler");
		long startTime=System.currentTimeMillis();
		PropagationEventsDao.processInStoredProcListPriceAdd(peId);
		Logger.perfInfo(logInfo, "processListPriceAdd", "(TimeStamp - "+new java.util.Date()+" ) - processing for processListPriceAdd - "+peId+" - took processing(timestamp) - "+(System.currentTimeMillis()-startTime)/1000);
	}
	
	
	public static void processConfigCntryUpdate(Long peId,
			ConfigHeaderInfo[] configHeaderInfo) throws CoronaException {
		LoggerInfo logInfo = new LoggerInfo("PropagationEventsCallHandler");
		long startTime=System.currentTimeMillis();
		PropagationEventsDao.processConfigCntryUpdate(peId, configHeaderInfo);
		Logger.perfInfo(logInfo, "processConfigCntryUpdate", "(TimeStamp - "+new java.util.Date()+" ) - processing for processConfigCntryUpdate - "+peId+" - took processing(timestamp) - "+(System.currentTimeMillis()-startTime)/1000);
	}

	public static void processConfigDescUpdate(Long peId,
			ConfigDescription[] cfgDesc) throws CoronaException {
		LoggerInfo logInfo = new LoggerInfo("PropagationEventsCallHandler");
		long startTime=System.currentTimeMillis();
		PropagationEventsDao.processConfigDescUpdate(peId, cfgDesc);
		Logger.perfInfo(logInfo, "processConfigDescUpdate", "(TimeStamp - "+new java.util.Date()+" ) - processing for processConfigDescUpdate - "+peId+" - took processing(timestamp) - "+(System.currentTimeMillis()-startTime)/1000);
	}

	public static void processConfigPermPriceUpdate(Long peId,
			ConfigPermutationPriceInfo[] cfgPermPriceInfo)
			throws CoronaException {
		LoggerInfo logInfo = new LoggerInfo("PropagationEventsCallHandler");
		long startTime=System.currentTimeMillis();
		PropagationEventsDao.processConfigPermPriceUpdate(peId,
				cfgPermPriceInfo);
		Logger.perfInfo(logInfo, "processConfigPermPriceUpdate", "(TimeStamp - "+new java.util.Date()+" ) - processing for processConfigPermPriceUpdate - "+peId+" - took processing(timestamp) - "+(System.currentTimeMillis()-startTime)/1000);
	}

	public static void processConfigPermPriceFuture(Long peId,
			ConfigPermutationPriceInfo[] cfgPermPriceInfo)
			throws CoronaException {
		LoggerInfo logInfo = new LoggerInfo("PropagationEventsCallHandler");
		long startTime=System.currentTimeMillis();
		PropagationEventsDao.processConfigPermPriceUpdate(peId,
				cfgPermPriceInfo);
		Logger.perfInfo(logInfo, "processConfigPermPriceFuture", "(TimeStamp - "+new java.util.Date()+" ) - processing for processConfigPermPriceFuture - "+peId+" - took processing(timestamp) - "+(System.currentTimeMillis()-startTime)/1000);
	}

	public static CTODaxDataBeanGeneral getConfigPermUpdate(Long peId,
			Long srcEventId, String priceId, String bundleId, String shipCntry,
			String priceDescriptor) throws CoronaException {
		LoggerInfo logInfo = new LoggerInfo("PropagationEventsCallHandler");
		long startTime=System.currentTimeMillis();
		CTODaxDataBeanGeneral ctxBean=  PropagationEventsDao.getConfigPermUpdate(peId, srcEventId,
				priceId, bundleId, shipCntry, priceDescriptor);

		Logger.perfInfo(logInfo, "getConfigPermUpdate", "(TimeStamp - "+new java.util.Date()+" ) - processing for getConfigPermUpdate - "+peId+" - took processing(timestamp) - "+(System.currentTimeMillis()-startTime)/1000);
		return ctxBean;
	}

	public static CTODaxDataBeanGeneral getConfigPriceUpdate(Long peId,
			Long srcEventId, String priceId, String bundleId, String shipCntry,
			String priceDescriptor) throws CoronaException {
		LoggerInfo logInfo = new LoggerInfo("PropagationEventsCallHandler");
		long startTime=System.currentTimeMillis();
		CTODaxDataBeanGeneral ctxBean=  PropagationEventsDao.getConfigPriceUpdate(peId, srcEventId,
				priceId, bundleId, shipCntry, priceDescriptor);
		Logger.perfInfo(logInfo, "getConfigPriceUpdate", "(TimeStamp - "+new java.util.Date()+" ) - processing for getConfigPriceUpdate - "+peId+" - took processing(timestamp) - "+(System.currentTimeMillis()-startTime)/1000);
		return ctxBean;
	}

	public static CTODaxDataBeanGeneral getConfigCntryUpdate(Long peId,
			Long srcEventId, String bundleId, String shipCntry)
			throws CoronaException {
		LoggerInfo logInfo = new LoggerInfo("PropagationEventsCallHandler");
		long startTime=System.currentTimeMillis();
		CTODaxDataBeanGeneral ctxBean=  PropagationEventsDao.getConfigCntryUpdate(peId, srcEventId,
				bundleId, shipCntry);

		Logger.perfInfo(logInfo, "getConfigCntryUpdate", "(TimeStamp - "+new java.util.Date()+" ) - processing for getConfigCntryUpdate - "+peId+" - took processing(timestamp) - "+(System.currentTimeMillis()-startTime)/1000);
		return ctxBean;
	}

	public static CTODaxDataBeanGeneral getConfigPermPriceUpdate(Long peId,
			Long srcEventId, String priceId, String bundleId, String shipCntry,
			String priceDescriptor) throws CoronaException {
		LoggerInfo logInfo = new LoggerInfo("PropagationEventsCallHandler");
		long startTime=System.currentTimeMillis();
		CTODaxDataBeanGeneral ctxBean=  PropagationEventsDao.getConfigPermPriceUpdate(peId, srcEventId,
				priceId, bundleId, shipCntry, priceDescriptor);
		Logger.perfInfo(logInfo, "getConfigPermPriceUpdate", "(TimeStamp - "+new java.util.Date()+" ) - processing for getConfigPermPriceUpdate - "+peId+" - took processing(timestamp) - "+(System.currentTimeMillis()-startTime)/1000);
		return ctxBean;
	}

	public static CTODaxDataBeanGeneral getConfigPermPriceFuture(Long peId,
			Long srcEventId, String priceId, String bundleId, String shipCntry,
			String priceDescriptor) throws CoronaException {
		LoggerInfo logInfo = new LoggerInfo("PropagationEventsCallHandler");
		long startTime=System.currentTimeMillis();
		CTODaxDataBeanGeneral ctxBean=  PropagationEventsDao.getConfigPermPriceUpdate(peId, srcEventId,
				priceId, bundleId, shipCntry, priceDescriptor);
		Logger.perfInfo(logInfo, "getConfigPermPriceFuture", "(TimeStamp - "+new java.util.Date()+" ) - processing for getConfigPermPriceFuture - "+peId+" - took processing(timestamp) - "+(System.currentTimeMillis()-startTime)/1000);
		return ctxBean;
	}
	
	public static CTODaxDataBeanGeneral getConfigDescUpdate(Long peId,
			Long srcEventId, String bundleId, String shipCntry)
			throws CoronaException {
		LoggerInfo logInfo = new LoggerInfo("PropagationEventsCallHandler");
		long startTime=System.currentTimeMillis();
		CTODaxDataBeanGeneral ctxBean= PropagationEventsDao.getConfigDescUpdate(peId, srcEventId,
				bundleId, shipCntry);
		Logger.perfInfo(logInfo, "getConfigDescUpdate", "(TimeStamp - "+new java.util.Date()+" ) - processing for getConfigDescUpdate - "+peId+" - took processing(timestamp) - "+(System.currentTimeMillis()-startTime)/1000);
		return ctxBean;

	}
}
