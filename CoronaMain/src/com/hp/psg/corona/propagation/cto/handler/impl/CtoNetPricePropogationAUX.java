package com.hp.psg.corona.propagation.cto.handler.impl;

import java.sql.Timestamp;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import com.hp.psg.common.util.logging.LoggerInfo;
import com.hp.psg.corona.common.beans.CTODaxDataBeanGeneral;
import com.hp.psg.corona.common.constants.CTOConstants;
import com.hp.psg.corona.common.cto.beans.ConfigHeaderInfo;
import com.hp.psg.corona.common.cto.beans.ConfigPermutationPriceInfo;
import com.hp.psg.corona.common.cto.beans.ConfigPriceHeaderInfo;
import com.hp.psg.corona.common.cto.beans.PriceInfo;
import com.hp.psg.corona.common.util.Logger;
import com.hp.psg.corona.propagation.handler.util.ConfigDataChangeUtil;

/*
* @author sjoshi
* @version 1.0
*
*/
public class CtoNetPricePropogationAUX  {

	LoggerInfo logInfo=null;

	
	public CtoNetPricePropogationAUX() {
		// TODO Auto-generated constructor stub
		logInfo = new LoggerInfo (this.getClass().getName());
	}
	
	public CtoNetPricePropogationAUX(boolean calculateFutureDate) {
		// TODO Auto-generated constructor stub
		logInfo = new LoggerInfo (this.getClass().getName());
		
	}	

	public Status processForConfigPermutationPriceInfo(
			ConfigHeaderInfo chi,
			ConfigPriceHeaderInfo cphi,
			List<PriceInfo> piList,
			CTODaxDataBeanGeneral generalBean,
			Date newDate) {

		// if ConfigHeaderInfo.validity_status = 'V'
		Status status = null;
		if (chi.getValidityStatus().equals(CTOConstants.VALID_STATUS)) {
			status = priceCurrentValidConfig(cphi,piList,generalBean, newDate);
		}
		else {
			processConfigPriceForInvalidConfig(chi, cphi, generalBean);
		}
		
		return status;
	}


	public void constructCppiFromCphi(ConfigPriceHeaderInfo cphi,
			String priceStatus, String priceErrMsg, String delFlag, boolean setDates,
			Timestamp startDate, Timestamp endDate,
			CTODaxDataBeanGeneral generalBean) {
		Logger.info(logInfo,"constructCppiFromCphi", "constructCppiFromCphi");

		ConfigPermutationPriceInfo cppi = new ConfigPermutationPriceInfo();
		cppi.setPriceStatus(priceStatus);
		if (priceErrMsg != null || !"".equals(priceErrMsg))
			cppi.setPriceErrMsg(priceErrMsg);
		cppi.setUpdtFlag(CTOConstants.UPDT_FLAG_ADD);
		cppi.setBundleId(cphi.getBundleId());
		cppi.setShipToCountry(cphi.getShipToCountry());
		cppi.setPriceDescriptor(cphi.getPriceDescriptor());
		cppi.setPriceId(cphi.getPriceId());
		if (!setDates)
		{
			if(startDate==null){
				cppi.setPriceStartDate(ConfigDataChangeUtil.getDefaultStartDate(CTOConstants.DEFAULT_START_DAYS));
			}else{
				cppi.setPriceStartDate(startDate);
			}
			cppi.setPriceEndDate(ConfigDataChangeUtil
					.getDefaultEndDate(CTOConstants.DEFAULT_END_DAYS));
		}else{
			//cppi.setPriceStartDate(ConfigDataChangeUtil.getDefaultStartDate(startDateOffset));
			//cppi.setPriceEndDate(ConfigDataChangeUtil.getDefaultEndDate(endDateOffset));
			
			cppi.setPriceStartDate(startDate);
			cppi.setPriceEndDate(endDate);
		}
		cppi.setDelFlag(delFlag);
		int newLen = 0;
		if (generalBean.getConfigPermutationPriceInfo() != null &&
				generalBean.getConfigPermutationPriceInfo().length > 0	)
			newLen = generalBean.getConfigPermutationPriceInfo().length + 1;
		else
			newLen=1;			
		ConfigPermutationPriceInfo[] cdesc = new ConfigPermutationPriceInfo[newLen];

		if (generalBean.getConfigPermutationPriceInfo() != null
				&& generalBean.getConfigPermutationPriceInfo().length > 0)
			System.arraycopy(generalBean.getConfigPermutationPriceInfo(), 0,
					cdesc, 0,
					generalBean.getConfigPermutationPriceInfo().length);
		// yls set previous generalBean.getConfigPermutationPriceInfo() = null
		// ???
		cdesc[newLen - 1] = cppi;
		generalBean.setConfigPermutationPriceInfo(cdesc);
	}

	public void constructCppifromPi(CTODaxDataBeanGeneral generalBean,
			ConfigPriceHeaderInfo cphi, List<PriceInfo> piList, Date startDate,
			Date endDate) {

		Logger.info(logInfo,"constructCppifromPi"," constructCppifromPi");

		double netPrice = 0;
		long qty = 0;
		String lastValidProductId = "";
		boolean invalidConfig = false;

		ConfigPermutationPriceInfo cppi;
		startDate = ConfigDataChangeUtil.formatDate(startDate);
		endDate = ConfigDataChangeUtil.formatDate(endDate);

		if (piList != null && piList.size() > 0) {
			for (Iterator<PriceInfo> piIterator = piList.iterator(); piIterator
					.hasNext();) {
				PriceInfo pi = piIterator.next();
				Logger.info(logInfo,"constructCppifromPi"," startDate "+startDate+" endDate "+endDate+" pi.getPriceStartDate() "+pi.getPriceStartDate()+
						" pi.getPriceEndDate() "+pi.getPriceEndDate());
				
					if (((ConfigDataChangeUtil.formatDate(pi.getPriceStartDate())
							.compareTo(startDate) == 0) || (ConfigDataChangeUtil
							.formatDate(pi.getPriceStartDate()).compareTo(endDate) < 0))
							&& ((ConfigDataChangeUtil.formatDate(
									pi.getPriceEndDate()).compareTo(endDate) >= 0))) {
						Logger.info(logInfo,"constructCppifromPi"," Calculating net price with " + pi.getProductId());
						qty = pi.getConfigQty();
						netPrice = netPrice + (pi.getNetPrice() * qty);
						lastValidProductId = pi.getProductId();
						invalidConfig = false;
					} else {
						if (!lastValidProductId.equals(pi.getProductId())) {
							invalidConfig = true;
							Logger.info(logInfo,"constructCppifromPi"," invalidConfig is true ");
	
						}
					}
			}

			Logger.info(logInfo,"constructCppifromPi"," cppi insert for netPrice:  invalidConfig = "
							+ invalidConfig+ "    netPrice = " + netPrice);

			cppi = new ConfigPermutationPriceInfo();
			cppi.setPriceStartDate(new Timestamp(startDate.getTime()));
			cppi.setPriceEndDate(new Timestamp(endDate.getTime()));
			cppi.setBundleId(cphi.getBundleId());
			cppi.setShipToCountry(cphi.getShipToCountry());
			cppi.setPriceId(cphi.getPriceId());
			cppi.setPriceDescriptor(cphi.getPriceDescriptor());
			cppi.setNetPrice(netPrice);				
			cppi.setDelFlag(CTOConstants.DELETE_FLAG_NOT_SET);
			if (invalidConfig) {
				// insert with invalid status for that date range if not
				// exists in cppi else update
				cppi.setPriceStatus(CTOConstants.INVALID_STATUS);
				cppi.setUpdtFlag(CTOConstants.UPDT_FLAG_ADD);
				cppi.setPriceErrMsg("Missing price condition record for selected product");
			} else {
				// if does not exist in CPPI
				// insert with valid status and date range else update
				cppi.setPriceStatus(CTOConstants.VALID_STATUS);
				cppi.setUpdtFlag(CTOConstants.UPDT_FLAG_ADD);
			}
			int newLen = 0;
			if (generalBean.getConfigPermutationPriceInfo() != null &&
					generalBean.getConfigPermutationPriceInfo().length > 0	)
				newLen = generalBean.getConfigPermutationPriceInfo().length + 1;
			else
				newLen=1;
			ConfigPermutationPriceInfo[] cdesc = new ConfigPermutationPriceInfo[newLen];

			if (generalBean.getConfigPermutationPriceInfo() != null
					&& generalBean.getConfigPermutationPriceInfo().length > 0)
				System.arraycopy(generalBean
						.getConfigPermutationPriceInfo(), 0, cdesc, 0,
						generalBean.getConfigPermutationPriceInfo().length);
			cdesc[newLen - 1] = cppi;
			generalBean.setConfigPermutationPriceInfo(cdesc);
		}
	}
	
	public class Status{
		boolean needFuturePrice = false;
		Date currentStartDate = null;
		Date currentEndDate = null;
		String message = null;
		
		Status(){}
		
		Status(boolean value, Date startDate, Date endDate, String messageStr){
			needFuturePrice = value;
			currentStartDate = startDate;
			currentEndDate = endDate;
			message = messageStr;
			
		}
		
		public boolean needFuturePricing(){
			return needFuturePrice;
		}
		
		public Date getCurrentStartDate(){
			return currentStartDate;
		}
		
		public void setFuturePricing(boolean value){
			this.needFuturePrice = value;
		}
		
		public void setCurrentStartDate(Date endDate){
			this.currentStartDate = endDate;
		}
		
		public Date getCurrentEndDate(){
			return currentEndDate;
		}
		
		public void setCurrentEndDate(Date endDate){
			this.currentEndDate = endDate;
		}
		
		public String getMessage(){
			return message;
		}
		
		public void setMessage(String msg){
			message = msg;
		}
		
		
	}
	
	protected Status priceCurrentValidConfig(
			ConfigPriceHeaderInfo cphi,
			List<PriceInfo> piList,
			CTODaxDataBeanGeneral generalBean, 
			Date newDate) {
		
		Status status = new Status();
		status.setFuturePricing(false);	//set default value
		if(cphi == null){
			return status;
		}

		newDate = ConfigDataChangeUtil.formatDate(newDate);
		Timestamp newDateTimestamp = new Timestamp(newDate.getTime());

		// If Cphi delete flag is not set
		if (!cphi.getDelFlag().equals(CTOConstants.DELETE_FLAG_SET)) {
			// If not Pending status
			if (cphi.getPriceStatus() == null
					|| (!cphi.getPriceStatus().equals(CTOConstants.PENDING_STATUS))) {
				
				if(piList == null ||piList.size() == 0){// If Status is pending
					constructCppiFromCphi(cphi, CTOConstants.PENDING_STATUS, cphi.getPriceErrorMsg(),
							CTOConstants.DELETE_FLAG_NOT_SET, false,null,null,generalBean);
					return status;
				}
				
				// get List of all PI's for this CPHI
				CtoPriceInfoStatus piStatus = new CtoPriceInfoStatus();
				
				
				piStatus.setPriceInfoStatus(piList, newDate);
				Logger.info(logInfo,"processConfigPriceForValidConfig","Current : piStatus.isAllPiHavePrices() "+piStatus.isAllPiHavePrices()+
						"piStatus.isAnyhaveerror "+piStatus.isAnyPiHaveError()+"AnyPending"+piStatus.isAnyPiHavePending());
						
				if (piStatus.isAllPiHavePrices()) {
					if (piStatus.getCurrentStartDate() != null
							&& piStatus.getCurrentEndDate() != null) {
						Logger.info(logInfo,"processConfigPriceForValidConfig","isAllPiHavePrices and construct cppifrom pi ");
						constructCppifromPi(generalBean, cphi, piList, piStatus
										.getCurrentStartDate(),	piStatus.getCurrentEndDate());
						status.setFuturePricing(true);
						status.setCurrentStartDate(piStatus.getCurrentStartDate());
						status.setCurrentEndDate(piStatus.getCurrentEndDate());
					}else{
						Logger.info(logInfo,"processConfigPriceForValidConfig","isAllPiHavePrices, but start date or end date is null");
					}

				}else if (piStatus.isAnyPiHaveError()) {
					Logger.info(logInfo,"processConfigPriceForValidConfig","isAnyPiHaveError and construct cppifrom pi ");
					System.out.println("isAnyPiHaveError and construct cppifrom pi");
					constructCppiFromCphi(cphi,CTOConstants.ERROR_STATUS, piStatus.getErrorMsg().toString(),
							CTOConstants.DELETE_FLAG_NOT_SET,true, newDateTimestamp,newDateTimestamp,generalBean);
					status.setFuturePricing(true);
					
					//////////////////////////////////////////////////////
					//status.setCurrentStartDate(newDateTimestamp);
					//status.setCurrentEndDate(newDateTimestamp);
					status.setCurrentStartDate(piStatus.getCurrentStartDate());
					status.setCurrentEndDate(piStatus.getCurrentEndDate());
					//////////////////////////////////////////////////////
					
				}

				else if (piStatus.isAllPiHavePending()) { // If any starting point have error
					Logger.info(logInfo,"processConfigPriceForValidConfig","isAllPiHavePending  updateOrConstructCppiRecordsForCphi");
					Timestamp endDate = ConfigDataChangeUtil.getDefaultStartDate(CTOConstants.DEFAULT_END_DAYS);
					constructCppiFromCphi(cphi,CTOConstants.REGISTERED_STATUS,null,CTOConstants.DELETE_FLAG_NOT_SET,
							true,newDateTimestamp,endDate,generalBean);
				}
				// If any starting point has no prices and others have prices
				else if (piStatus.isAnyPiHavePending()) {
					Logger.info(logInfo,"processConfigPriceForValidConfig","isAnyPiHavePending  updateOrConstructCppiRecordsForCphi");
					Timestamp endDate = ConfigDataChangeUtil.getDefaultStartDate(CTOConstants.DEFAULT_END_DAYS);
					constructCppiFromCphi(cphi,CTOConstants.MISSING_STATUS,piStatus
							.getErrorMsg().toString(),CTOConstants.DELETE_FLAG_NOT_SET,
							true,newDateTimestamp,endDate,generalBean);
						
				}
			}else{	//CPHI is pending
				Logger.info(logInfo,"processConfigPriceForValidConfig","Status is Pending");
				constructCppiFromCphi(cphi, CTOConstants.PENDING_STATUS, cphi.getPriceErrorMsg(),
						CTOConstants.DELETE_FLAG_NOT_SET, false,newDateTimestamp,null,generalBean);
			}
			
		} 
		else {// if CPHI.del flag is set
			Logger.info(logInfo,"processConfigPriceForValidConfig","if CPHI.del flag is set");
			constructCppiFromCphi(cphi,	CTOConstants.INVALID_STATUS, "The record has marked for delete in Config_price_header_info table",
					CTOConstants.DELETE_FLAG_SET, false,newDateTimestamp,null,generalBean);
		}

		return status;
	}


	
	protected void processConfigPriceForInvalidConfig(
			ConfigHeaderInfo chi,
			ConfigPriceHeaderInfo cphi,
			CTODaxDataBeanGeneral generalBean) {


		if(cphi!=null){
			
			String key = cphi.getBundleId() + "|" + cphi.getShipToCountry()
					+ "|" + cphi.getPriceId() + "|"
					+ cphi.getPriceDescriptor();
	
			Logger.info(logInfo,"processConfigPriceForInvalidConfig","cphi key " + key);
			Timestamp statDate = ConfigDataChangeUtil.getDefaultStartDate(CTOConstants.DEFAULT_START_DAYS);
			Timestamp endDate = ConfigDataChangeUtil.getDefaultStartDate(CTOConstants.DEFAULT_END_DAYS);
			
			String cppiStatus = CTOConstants.INVALID_STATUS;
			if(chi.getValidityStatus().equals(CTOConstants.PENDING_STATUS)){
				cppiStatus = CTOConstants.PENDING_STATUS;
			}
	
			constructCppiFromCphi(cphi, cppiStatus, null,CTOConstants.DELETE_FLAG_NOT_SET,
					true,statDate,endDate,generalBean);
		}
			
		
	}
	
}
