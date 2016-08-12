package com.hp.psg.corona.propagation.cto.handler.impl;

import java.sql.Timestamp;
import java.util.Collections;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.hp.psg.common.error.CoronaException;
import com.hp.psg.common.util.logging.LoggerInfo;
import com.hp.psg.corona.common.beans.CTODaxDataBeanGeneral;
import com.hp.psg.corona.common.constants.CTOConstants;
import com.hp.psg.corona.common.cto.beans.ConfigHeaderInfo;
import com.hp.psg.corona.common.cto.beans.ConfigPermutationInfo;
import com.hp.psg.corona.common.cto.beans.ConfigPermutationPriceInfo;
import com.hp.psg.corona.common.cto.beans.CoronaBaseObject;
import com.hp.psg.corona.common.util.CoronaFwkUtil;
import com.hp.psg.corona.common.util.Logger;
import com.hp.psg.corona.propagation.handler.interfaces.IBeanPropagation;
import com.hp.psg.corona.propagation.handler.util.ConfigDataChangeUtil;

/**
 * @author bilgi
 * @version 1.0
 *
 */
public class PermutationInfoPropagation implements IBeanPropagation {

	
	private LoggerInfo logInfo=null;
	
	public PermutationInfoPropagation() {
		// TODO Auto-generated constructor stub
		logInfo = new LoggerInfo (this.getClass().getName());
	}
	
	
	@SuppressWarnings("unchecked")
	public CTODaxDataBeanGeneral processBeans(CTODaxDataBeanGeneral generalBean)
	throws CoronaException {
		if (generalBean ==null) {
			throw new CoronaException("CTODescTransformation:: NULL Input CTODaxDataBeanGeneral has been passed as null");
		}

		Map<CoronaBaseObject, List<? extends CoronaBaseObject>> permutation2HeaderMap = Collections.EMPTY_MAP;
		Map<CoronaBaseObject, List<? extends CoronaBaseObject>> permutation2PriceMap = Collections.EMPTY_MAP;

		List<Map<CoronaBaseObject, List<? extends CoronaBaseObject>>> myMappingList = generalBean.getListMapRelations();

		// Initialize the maps
		for (Iterator<Map<CoronaBaseObject, List<? extends CoronaBaseObject>>> iterator = myMappingList
				.iterator(); iterator.hasNext();) {
			Map<CoronaBaseObject, List<? extends CoronaBaseObject>> myMap = iterator
			.next();

			String valueInListType = CoronaFwkUtil.getType(myMap);
			if (valueInListType != null) {
				if (valueInListType.equals(CTOConstants.CONFIG_HEADER_INFO)) {
					permutation2HeaderMap = myMap;
				} else if (valueInListType.equals(CTOConstants.CONFIG_PERMUTATION_PRICE_INFO)) {
					permutation2PriceMap = myMap;
				}
			}

		}

		//Get Header Status and EOL Date from Header.
		//Get Validity Status for the current date range from CPPI
		if (null != generalBean.getConfigPermutationInfo()) {
			for (int i = 0; i < generalBean.getConfigPermutationInfo().length; i++) {
				ConfigPermutationInfo cpInfo = generalBean.getConfigPermutationInfo()[i];
				// Set lower of EOL date from header and current price end date in CPI
				// Set validity status in CPI based on validity status of header and validity status of perm price info
				getAndSetEolAndStatus(cpInfo, permutation2HeaderMap,permutation2PriceMap);
			}
		}
		return generalBean;	
	}


	/**
	 * 
	 * @param cpInfo
	 */
	@SuppressWarnings("unchecked")
	private void getAndSetEolAndStatus(ConfigPermutationInfo cpInfo,
			Map<CoronaBaseObject, List<? extends CoronaBaseObject>> permutation2HeaderMap,
			Map<CoronaBaseObject, List<? extends CoronaBaseObject>> permutation2PriceMap
	) throws CoronaException{

		String headerValidity = CTOConstants.INVALID_STATUS;
		String priceValidity = CTOConstants.INVALID_STATUS;

		String currentStatus = cpInfo.getOverallStatus();
		Timestamp currentEOL =  cpInfo.getOverallEol();
		Long currentCppi = cpInfo.getCppiId();
		
		Logger.debug(logInfo, "getAndSetEolAndStatus", "INPUT - Overall Status "+currentStatus +" update Falg "+cpInfo.getUpdtFlag() +" currentEOL "+currentEOL);
		
		ConfigHeaderInfo chInfo =null;
		List<ConfigPermutationPriceInfo> cppList = null;

		//get Header Validity 
		List<ConfigHeaderInfo> chList = (List<ConfigHeaderInfo>)permutation2HeaderMap.get(cpInfo);
		if ((chList !=null) && (chList.size() >0)) {
			chInfo = (ConfigHeaderInfo)chList.get(0);
			headerValidity = (chInfo.getValidityStatus()==null ? CTOConstants.INVALID_STATUS : chInfo.getValidityStatus() );
			Logger.debug(logInfo, "getAndSetEolAndStatus", "Set header validity as "+headerValidity);
		}

		// If header is pending set overall status to pending. 
		if (headerValidity.equals(CTOConstants.PENDING_STATUS)) {
			cpInfo.setOverallStatus(headerValidity);
			cpInfo.setCppiId(null);
			Logger.debug(logInfo, "getAndSetEolAndStatus",	"Check>>>> "+cpInfo.getBundleId() +" Price Id "+ cpInfo.getPriceId() +" Header validity is in PENDING status and hence setting the CPPI ID as null");
			Logger.debug(logInfo, "getAndSetEolAndStatus", "Setting the validity overall status  "+headerValidity);
		} else if (headerValidity.equals(CTOConstants.INVALID_STATUS)) {
			cpInfo.setOverallStatus(headerValidity);
			cpInfo.setCppiId(null);
			Logger.debug(logInfo, "getAndSetEolAndStatus", "Setting the validity overall status  "+headerValidity);
			Logger.debug(logInfo, "getAndSetEolAndStatus",	"Check>>>> "+cpInfo.getBundleId() +" Price Id "+ cpInfo.getPriceId() +" Header validity is in INVALID status and hence setting the CPPI ID as null");

			cpInfo.setOverallEol(chInfo.getConfigCntryEol());
		} else { //Config Header is VALID
			cppList = (List<ConfigPermutationPriceInfo>) permutation2PriceMap.get(cpInfo);

			ConfigPermutationPriceInfo myCppi = getValidCPPI(cppList); 
			priceValidity = myCppi.getPriceStatus();
			cpInfo.setCppiId(myCppi.getId());
			
			//If price status is R or P and header is valid then overall status should be W
			if ((CTOConstants.REGISTERED_STATUS).equals(priceValidity) || 
					(CTOConstants.PENDING_STATUS).equals(priceValidity)) {
				cpInfo.setOverallStatus(CTOConstants.PRICE_PENDING_STATUS);
				Logger.debug(logInfo, "getAndSetEolAndStatus", "Check>>>> "+cpInfo.getBundleId() +" Price Id "+ cpInfo.getPriceId() +" Setting the validity overall status  "+CTOConstants.PRICE_PENDING_STATUS);
			} else if ((CTOConstants.MISSING_STATUS).equals(priceValidity) || 
					(CTOConstants.ERROR_STATUS).equals(priceValidity)) {
				//If price status is M or E and header is valid then overall status should be I
				cpInfo.setOverallStatus(CTOConstants.INVALID_STATUS);
				Logger.debug(logInfo, "getAndSetEolAndStatus", "Check>>>> "+cpInfo.getBundleId() +" Price Id "+ cpInfo.getPriceId() +"  Setting the validity overall status  "+CTOConstants.INVALID_STATUS+ " as  price validity "+priceValidity);

			} else if ((CTOConstants.VALID_STATUS).equals(priceValidity)) {
				cpInfo.setOverallStatus(CTOConstants.VALID_STATUS);
				Logger.debug(logInfo, "getAndSetEolAndStatus", "Check>>>> "+cpInfo.getBundleId() +" Price Id "+ cpInfo.getPriceId() +"  Setting the validity overall status  "+CTOConstants.VALID_STATUS+ " as  price validity "+priceValidity);

				//EOL is only set when the header is VALID- else keep the same EOL.
				Timestamp headerEOL = chInfo.getConfigCntryEol();
				Timestamp priceEOL = getPriceEOL(cppList);
				// set lower of header EOL and price end date
				if ((headerEOL == null) && (priceEOL != null)) {
					cpInfo.setOverallEol(priceEOL);
				} else if ((headerEOL != null) && (priceEOL == null)) {
					cpInfo.setOverallEol(headerEOL);
				} else if ((headerEOL != null) && (priceEOL != null)) {
					cpInfo.setOverallEol(headerEOL.before(priceEOL) ? headerEOL	: priceEOL);
				}
			} else {
				Logger.debug(logInfo, "getAndSetEolAndStatus", "Check>>>> "+cpInfo.getBundleId() +" Price Id "+ cpInfo.getPriceId() +" -ELSE - Setting the validity overall status  "+CTOConstants.INVALID_STATUS);
				cpInfo.setOverallStatus(CTOConstants.INVALID_STATUS);
			}
		}

		//Set the update flag
		if ((cpInfo.getOverallStatus() != null)
				&& !((cpInfo.getOverallStatus()).equals(currentStatus))) {
			Logger.debug(logInfo, "getAndSetEolAndStatus","Check>>>> 1 "+cpInfo.getBundleId() +" Price Id "+ cpInfo.getPriceId() +" - overall status is not null and OLD overall status is not same as current one , setting the flag as modified one");
			cpInfo.setUpdtFlag(CTOConstants.UPDT_FLAG_MODIFIED);
		} else if ((cpInfo.getOverallEol() != null)
				&& !(cpInfo.getOverallEol().equals(currentEOL))) {
			Logger.debug(logInfo, "getAndSetEolAndStatus","Check>>>> 2  "+cpInfo.getBundleId() +" Price Id "+ cpInfo.getPriceId() +" - overall EOL is not null and OLD overall EOL is not equal to current EOL , setting the flag as modified one");
			cpInfo.setUpdtFlag(CTOConstants.UPDT_FLAG_MODIFIED);
		} else if ((cpInfo.getCppiId() != null)
				&& !(cpInfo.getCppiId().equals(currentCppi))){
			Logger.debug(logInfo, "getAndSetEolAndStatus","Check>>>> 3 "+cpInfo.getBundleId() +" Price Id "+ cpInfo.getPriceId() +" - CPPIID is not null and OLD CPPIID is not equal to current CPPIID , setting the flag as modified one");
			cpInfo.setUpdtFlag(CTOConstants.UPDT_FLAG_MODIFIED);
		} else if ((cpInfo.getCppiId() == null)
				&& (currentCppi != null)){
			Logger.debug(logInfo, "getAndSetEolAndStatus","Check>>>> 4 "+cpInfo.getBundleId() +" Price Id "+ cpInfo.getPriceId() +" - OLD CPPIID was null and current CPPI ID is not null , setting the flag as modified one");
			cpInfo.setUpdtFlag(CTOConstants.UPDT_FLAG_MODIFIED);
		} else {
			cpInfo.setUpdtFlag(CTOConstants.UPDT_FLAG_UNCHANGED);
			Logger.debug(logInfo, "getAndSetEolAndStatus",	"Check>>>> 5 "+cpInfo.getBundleId() +" Price Id "+ cpInfo.getPriceId() +" In else for modification part, Flag remained unchanged");
		}
		
		Logger.debug(logInfo, "getAndSetEolAndStatus", "Bundle Id  - "+cpInfo.getBundleId() +" Price Id "+ cpInfo.getPriceId() +" Output - Overall Status "+cpInfo.getOverallStatus() +" update Falg "+cpInfo.getUpdtFlag() +" EOL "+cpInfo.getOverallEol());
		
	}


	/**
	 * Get Price Validity Status
	 * @param cppList
	 * @return
	 */
	private ConfigPermutationPriceInfo getValidCPPI(
			List<ConfigPermutationPriceInfo> cppList) {
		ConfigPermutationPriceInfo retCppi = new ConfigPermutationPriceInfo();
		retCppi.setPriceStatus(CTOConstants.INVALID_STATUS);

		if (cppList != null && cppList.size() > 0) {
		for (ConfigPermutationPriceInfo cppi : cppList) {
			if (isSysdateInRange(cppi.getPriceStartDate(), cppi.getPriceEndDate())){
				if ( null!=cppi.getPriceStatus() ){
					return cppi;
				}
			}
		  }
		}
		return retCppi;
	}

	/**
	 * Get Price EOL Date
	 * @param cppList
	 * @return Date
	 */
	private Timestamp getPriceEOL(
			List<ConfigPermutationPriceInfo> cppList) {
		Timestamp priceEOL=null;

		for (ConfigPermutationPriceInfo cppi : cppList) {
			if (isSysdateInRange(cppi.getPriceStartDate(), cppi.getPriceEndDate())){
				priceEOL = cppi.getPriceEndDate();
				return priceEOL;
			}
		}
		return priceEOL;
	}


	private boolean isSysdateInRange(Date startDate, Date endDate){

		boolean retval= false;

		Date currentDate = new Date(System.currentTimeMillis());

		if (ConfigDataChangeUtil.formatDate(currentDate).equals(ConfigDataChangeUtil.formatDate(startDate))
				|| ConfigDataChangeUtil.formatDate(currentDate).equals(ConfigDataChangeUtil.formatDate(endDate))){
			retval=true;
		}

		if (ConfigDataChangeUtil.formatDate(currentDate).after(ConfigDataChangeUtil.formatDate(startDate))){
			if (ConfigDataChangeUtil.formatDate(currentDate).before(ConfigDataChangeUtil.formatDate(endDate))){
				retval=true;
			}
		}

		return retval;
	}


	public void prepareBean(Map<String, Object> constructArgs) {
		// TODO Auto-generated method stub
		
	}

}