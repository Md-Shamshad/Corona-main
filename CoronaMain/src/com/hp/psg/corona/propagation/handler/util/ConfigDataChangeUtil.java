package com.hp.psg.corona.propagation.handler.util;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;

import com.hp.jep.ModuleAccessControl;
import com.hp.jep.ModuleAccessException;
import com.hp.psg.common.error.CoronaException;
import com.hp.psg.common.util.logging.LoggerInfo;
import com.hp.psg.corona.common.constants.CTOConstants;
import com.hp.psg.corona.common.util.Config;
import com.hp.psg.corona.common.util.CoronaFwkUtil;
import com.hp.psg.corona.common.util.Logger;
import com.hp.psg.corona.datachange.dao.DataChangeEventsFwkDao;
import com.hp.psg.corona.error.util.CoronaErrorHandler;

/**
 * @author yanchao
 * @version 1.0
 *
 */
public class ConfigDataChangeUtil {

	public static final String DEFAULT_PRS_PRICE_PROC = "HPPRI3";
	public static final String TOP_CONFIG_DEAL_ID = "NONE";
	public static final String PRICE_RECIPIENT_PRE = "corona.pricing.recipient.";
	public static final String PRICE_PROCEDURE_LIST = "corona.price.procedure.list";

	public static String getPriceDescriptorFromPRSParams(String inPrsGeo,
			String inPrsCurrency, String inPrsIncoTerm,
			String inPrsPriceListType) {

		LoggerInfo logInfo = new LoggerInfo ("com.hp.psg.corona.propagation.handler.util.ConfigDataChangeUtil");
		
		Logger.info(logInfo,"getPriceDescriptorFromPRSParams","inPrsGeo -"+inPrsGeo + " country code "+getGpsyCountryFromPRSCountry(inPrsGeo) +" wears value"+CoronaFwkUtil.getWaersCodeFromIsoCd(inPrsCurrency)+" inPrsCurrency to lookup " + inPrsCurrency);
		
		StringBuffer priceDescriptor = new StringBuffer(
				getGpsyCountryFromPRSCountry(inPrsGeo)).append(CoronaFwkUtil
				.getWaersCodeFromIsoCd(inPrsCurrency));

		if ((inPrsIncoTerm != null)
				&& (CoronaFwkUtil.getPriceTermCdFromInco1Cd(inPrsIncoTerm) != null)) {
			priceDescriptor.append(CoronaFwkUtil
					.getPriceTermCdFromInco1Cd(inPrsIncoTerm));
		} else if (CoronaFwkUtil.getPriceTermCdFromPltypCd(inPrsPriceListType) != null) {
			priceDescriptor.append((CoronaFwkUtil
					.getPriceTermCdFromPltypCd(inPrsPriceListType)));
		}

		return priceDescriptor.toString();
	}

	public static String getGpsyCountryFromPRSCountry(String prsCountry) {
		return CoronaFwkUtil.getGpsyCntryCdFromPrsCntryCd(prsCountry);
	}

	public static String getPrsCountryCodeFromGpsyCountry(String gpsyCountry) {
		return CoronaFwkUtil.getPrsCntryCdFromGpsyCntryCd(gpsyCountry);
	}

	public static String getPriceGeoFromPriceDesc(String priceDesc) {
		if (priceDesc != null) {
			String gpsyCountryCode = priceDesc.substring(0, 2);
			return CoronaFwkUtil.getPrsCntryCdFromGpsyCntryCd(gpsyCountryCode);
		}
		return null;
	}

	public static String getCurrencyFromPriceDesc(String priceDesc) {
		if (priceDesc != null) {
			String isoCurrencyCode = priceDesc.substring(2, 5);
			return CoronaFwkUtil.getWaersCodeFromIsoCd(isoCurrencyCode);
		}
		return null;
	}

	public static String getIncoTermFromPriceDesc(String priceDesc) {
		if (priceDesc != null) {
			String priceTerm = priceDesc.substring(5, 7);
			return CoronaFwkUtil.getInco1FromPriceTermCd(priceTerm);
		}
		return null;
	}

	public static String getPriceListFromPriceDesc(String priceDesc) {
		if (priceDesc != null) {
			String priceTerm = priceDesc.substring(5, 7);
			return CoronaFwkUtil.getPltypCdFromPriceTermCd(priceTerm);
		}
		return null;
	}

	public static Timestamp getDefaultStartDate(int days) {
		Calendar thisDate = Calendar.getInstance();
		thisDate.add(Calendar.DATE, days);
		return new Timestamp(formatDate(thisDate.getTime()).getTime());
	}

	public static Timestamp getDefaultEndDate(int days) {
		Calendar thisDate = Calendar.getInstance();
		thisDate.add(Calendar.DATE, days);
		return new Timestamp(formatDate(thisDate.getTime()).getTime());
	}

	public static Date formatDate(Date givenDate) {
		java.util.Date newDate = new Date();
		SimpleDateFormat formatterOut = new SimpleDateFormat("dd-MMM-yyyy");
		String date4MySQL = "";
		if (givenDate != null) {
			date4MySQL = formatterOut.format(givenDate);
		} else {
			date4MySQL = formatterOut.format(newDate);
		}

		try {
			newDate = formatterOut.parse(date4MySQL);
		} catch (Exception e) {
			CoronaErrorHandler.logError(e,null,null);
		}

		return newDate;

	}
	/**
	 * To return pricing procedure name based on region and marketing program of
	 * a configuration.
	 * 
	 * IPCS will have the following rules for obtaining the pricing procedure::
	 * 
	 * 1) If you do not provide a deal id then we will use HPLP01 2) If you are
	 * region EMEA and you provide a deal id then we will use HPPRI3. 3) If you
	 * are region APJ and you provide a deal id then we wil use HPCE02.
	 * 
	 * @param variableValueMap
	 *            HashMap
	 * @return String
	 * @exception EdaException
	 */
	public static String getPriceProcedure(String region, String dealId)
			throws CoronaException {
		String priceProcedure = null;
		if (dealId == null || dealId.trim().length() == 0) {
			dealId = TOP_CONFIG_DEAL_ID;
		}
		Map<String, String> variableValueMap = new HashMap<String, String>();
		variableValueMap.put("region", region);
		variableValueMap.put("dealid", dealId);

		try {
			Logger.debug(ConfigDataChangeUtil.class.getName(),
					"getPriceProcedure",
					"EventProcessor::getPriceProcedure:: start...");
			// Search for appropriate pricing source based on price source info
			// present in preferences table and
			// expressions of module_control_table.
			String priceProcListString = Config.getProperty(
					PRICE_PROCEDURE_LIST,
					ConfigDataChangeUtil.DEFAULT_PRS_PRICE_PROC).toUpperCase();
			Logger.debug(ConfigDataChangeUtil.class.getName(),
					"getPriceProcedure", "Price procedure list: "
							+ priceProcListString);
			String[] priceProcList = readTokens(priceProcListString, ",");
			Logger.debug(ConfigDataChangeUtil.class.getName(),
					"getPriceProcedure",
					"Total count of pricing procedure is: "
							+ priceProcList.length);

			// gets hashmap containing MODULE_CONTROL_TAB data from
			// ModuleAccessControlCachedModel
			Map<String, String> moduleMap = DataChangeEventsFwkDao.getModuleMap();

			String moduleId = null;
			boolean returnFlag = false;
			for (int i = 0; i < priceProcList.length; i++) {
				moduleId = priceProcList[i].toUpperCase() + "_MODULE";
				Logger.debug(ConfigDataChangeUtil.class.getName(),
						"getPriceProcedure", "Looking for Module Id: "
								+ moduleId);
				try {
					returnFlag = ModuleAccessControl.checkModuleAccess(
							moduleMap, moduleId, variableValueMap);
					if (returnFlag) {
						Logger.debug(ConfigDataChangeUtil.class.getName(),
								"getPriceProcedure",
								"Expression for module id '" + moduleId
										+ "' is found valid.");
						priceProcedure = priceProcList[i];
						break;
					} else {
						Logger.debug(ConfigDataChangeUtil.class.getName(),
								"getPriceProcedure",
								"Expression for module id '" + moduleId
										+ "' is found invalid.");
					}
				} catch (ModuleAccessException moduleAccessEx) {
					// Failed to do a lookup of module id property and
					// expression in Module Control Tab.
					Logger
							.error(
									ConfigDataChangeUtil.class.getName(),
									"getPriceProcedure",
									"Module '"
											+ moduleId
											+ "' does not exist in ModuleControlTable.",
									moduleAccessEx);
					throw new CoronaException(moduleAccessEx, moduleAccessEx
							.getMessage());
				}
			}
			Logger.debug(ConfigDataChangeUtil.class.getName(),
					"getPriceProcedure", "Pricing expression found:"
							+ returnFlag);
			if (!returnFlag) {
				// Failed to do a lookup of module id property and expression in
				// Module Control Tab.
				Logger
						.debug(
								ConfigDataChangeUtil.class.getName(),
								"getPriceProcedure",
								"There is no pricing module entry exist in ModuleControlTable... Considering the default one...");
				// If there is no "price procedure" entry found in preferences
				// table, consider the default price procedure (i.e., HPPRI2) .
				priceProcedure = ConfigDataChangeUtil.DEFAULT_PRS_PRICE_PROC;
			}
		} finally {
			if(priceProcedure==null){
				priceProcedure = ConfigDataChangeUtil.DEFAULT_PRS_PRICE_PROC;
			}
		}
		Logger.debug(ConfigDataChangeUtil.class.getName(), "getPriceProcedure",
				"EventProcessor::getPriceProcedure:: end...");
		return priceProcedure;
	}

	/**
	 * To return the recipient based on shipToCountry, region and pricing
	 * procedure The format of the key should be like
	 * pricing.recipient.(region).country.pricingProcedure example entries in
	 * Config file: pricing.recipient.AP.ALL.HPCE03=IPCS
	 * pricing.recipient.AP.ALL.HPLP01=IPCS
	 * pricing.recipient.EU.ALL.HPCE03=IPCSEMEA
	 * pricing.recipient.EU.ALL.HPLP01=IPCSEMEA
	 * pricing.recipient.NA.ALL.HPCE05=IPCS
	 * 
	 * @param String
	 *            shipToCountry
	 * @param String
	 *            region
	 * @param String
	 *            pricingProcedure
	 * @return String
	 * @exception CoronaException
	 */
	public static String getPricingRecipient(String shipToCountry,
			String region, String pricingProcedure) throws CoronaException {
		String recipient = null;

		String key = PRICE_RECIPIENT_PRE + region.toUpperCase() + "."
				+ shipToCountry.toUpperCase() + "."
				+ pricingProcedure.toUpperCase();
		Logger.debug(ConfigDataChangeUtil.class.getName(),
				"getPricingRecipient", "Looking pricing recipient for key: "
						+ key);
		recipient = Config.getProperty(key, null);
		if (recipient == null) {
			key = PRICE_RECIPIENT_PRE + region + ".ALL." + pricingProcedure;
			Logger.debug(ConfigDataChangeUtil.class.getName(),
					"getPricingRecipient",
					"Looking pricing recipient for key: " + key);
			recipient = Config.getProperty(key, null);
		}

		Logger.debug(ConfigDataChangeUtil.class.getName(),
				"getPricingRecipient", "Pricing recipient for key: " + key
						+ " is: " + recipient);

		if (recipient == null) {
			throw new CoronaException(
					"PRS price recipient can not be found for key: " + key);
		}

		return recipient;
	}

	// Converting productId into productNumber and productOptionCd
	public static String getProductNumberFromProductId(String productId) {
		int index = productId.indexOf(CTOConstants.PRODUCT_ID_DELIMITER);
		if (index > 0) {
			return (productId.substring(0, index));
		} else {
			return productId;
		}
	}

	// Converting productId into productNumber and productOptionCd
	public static String getOptionCdFromProductId(String productId) {
		int index = productId.indexOf(CTOConstants.PRODUCT_ID_DELIMITER);
		if (index > 0) {
			return (productId.substring(index + 1, productId.length()));
		} else {
			return (null);
		}
	}

	// Converting productId into productNumber and productOptionCd
	public static String getProductIdFromProductNumberAndOptionCd(
			String productNumber, String optionCd) {
		StringBuffer productId = new StringBuffer();
		if (productNumber != null && !"".equals(productNumber.trim())) {
			productId.append(productNumber);
		}
		if (optionCd != null && !"".equals(optionCd.trim())) {
			productId.append(CTOConstants.PRODUCT_ID_DELIMITER);
			productId.append(optionCd);
		}
		return productId.toString();
	}

	/**
	 * Splits a string into various tokens Creation date: (5/23/2002 3:01:21 PM)
	 * 
	 * @param text
	 *            The text to split
	 * @param token
	 *            The separator
	 * @return The list of substrings
	 */
	public static String[] readTokens(String text, String token) {
		StringTokenizer parser = new StringTokenizer(text, token);

		int numTokens = parser.countTokens();

		String[] list = new String[numTokens];

		for (int i = 0; i < numTokens; i++) {
			list[i] = parser.nextToken().trim();
		}
		return list;
	}

	// remove duplicates and sort list
	public static void getDateRange(List<Date> dateRangeList) {
		LoggerInfo logInfo = new LoggerInfo ("com.hp.psg.corona.propagation.handler.util.ConfigDataChangeUtil");
		
		quickSort3(dateRangeList, 0, dateRangeList.size() - 1);
		Logger.info(logInfo,"getDateRange",dateRangeList.toString());
		Date duplicateDate = new Date(0);
		Date nextDate = new Date();
		ArrayList<Date> newList = new ArrayList<Date>();
		int index = 0;

		for (Iterator<Date> dateIterator = dateRangeList.iterator(); dateIterator
				.hasNext();) {
			nextDate = dateIterator.next();
			// first time
			if (duplicateDate.getTime() == 0) {
				duplicateDate = nextDate;
				newList.add(nextDate);
			} else {
				if (duplicateDate.equals(nextDate)) {
					duplicateDate = nextDate;
				} else {
					duplicateDate = nextDate;
					newList.add(nextDate);
				}
			}
			index++;
		}
		dateRangeList = newList;
		Logger.info(logInfo,"getDateRange",dateRangeList.toString());
	}

	/*
	 * //remove duplicates and sort list public static void getDateRange3(List<Date>
	 * dateRangeList) { Date startDate = new Date(0); Date endDate = new Date();
	 * Date date1 = new Date(0); Date date2 = new Date(); Date date3 = new
	 * Date(); for (Iterator<Date> dateIterator = dateRangeList.iterator();
	 * dateIterator.hasNext();) { if (date1.getTime() == 0) { date1 =
	 * dateIterator.next(); }
	 * 
	 * if (dateIterator.hasNext()) { date2 = dateIterator.next(); } else {
	 * date2.setTime(0); }
	 * 
	 * if (dateIterator.hasNext()) { date3 = dateIterator.next(); } else {
	 * date3.setTime(0); }
	 * 
	 * startDate = date1; if (date3.getTime()-date2.getTime() == 1000) { endDate =
	 * date2; //do Logic System.out.println("startdate = "+startDate+" and
	 * enddate = "+endDate); } else if (date3.getTime() != 0) { endDate = new
	 * Date(date2.getTime()-1000); //Do Logic System.out.println("startdate =
	 * "+startDate+" and enddate = "+endDate);
	 * 
	 * date1 = date2; date2=date3; if (dateIterator.hasNext()) { date3 =
	 * dateIterator.next(); } else { date3.setTime(0); } startDate= date1;
	 * endDate = date2; //do logic System.out.println("startdate = "+startDate+"
	 * and enddate = "+endDate);
	 *  } else { endDate = date2; //Do logic System.out.println("startdate =
	 * "+startDate+" and enddate = "+endDate); }
	 * 
	 * date1.setTime(date3.getTime()); } }
	 */

	public static void quickSort3(List<Date> a, int l, int r) {
		if (r <= l)
			return;
		int i = l - 1;
		int j = r;
		int p = l - 1;
		int q = r;
		Date v = a.get(r);

		for (;;) {
			while (a.get(++i).before(v));
			while (v.before(a.get(--j))) {
				if (j == l)
					break;
			}
			if (i >= j)
				break;
			swap3(a, i, j);
			if (a.get(i).equals(v)) {
				p++;
				swap3(a, p, i);
			}
			if (v.equals(a.get(j))) {
				q--;
				swap3(a, j, q);
			}
		}
		swap3(a, i, r);
		j = i - 1;
		i = i + 1;
		for (int k = l; k < p; k++, j--) {
			swap3(a, p, i);
		}
		for (int k = r - 1; k > q; k--, i++) {
			swap3(a, i, k);
		}
		quickSort3(a, l, j);
		quickSort3(a, i, r);
	}

	private static void swap3(List<Date> a, int l, int r) {
		Date temp = a.get(l);
		a.add(l, a.get(r));
		a.remove(l + 1);
		a.remove(r);
		a.add(r, temp);
	}

	public static void main(String[] args) {

		try{
			DataChangeEventsFwkDao.cacheModuleMap();
		}catch (Exception e){
			
		}
		try{
			String naProc = getPriceProcedure("NA", "FSA");
			String euProc = getPriceProcedure("EU", "FSA");
			String apProc = getPriceProcedure("AP", "FSA");
		}
		catch (Exception e){
			
		}
		
		List<Date> dates = new ArrayList<Date>();
		Calendar cal = Calendar.getInstance();

		Calendar cal3 = Calendar.getInstance();
		cal.add(Calendar.DATE, 1);
		Date day = Calendar.getInstance().getTime();
		Calendar cal2 = (Calendar) cal.clone();
		dates.add(day);
		cal.add(Calendar.DATE, 1);
		day = cal.getTime();
		dates.add(day);
//		Logger.debug("day " + day);
		cal.add(Calendar.SECOND, -1);
		day = cal.getTime();
//		Logger.debug("day2 " + day);
		dates.add(day);
		cal.add(Calendar.DATE, 1);
		day = cal.getTime();
		dates.add(day);
		cal3.add(Calendar.MINUTE, 1);
		day = cal3.getTime();
//		Logger.debug("day3 " + day);
		dates.add(day);
		cal.add(Calendar.DATE, 1);
		day = cal.getTime();
		dates.add(day);

		cal.add(Calendar.DATE, 1);
		day = cal.getTime();
		dates.add(day);
		/*
		 * cal.add(Calendar.DATE, 1); day = cal.getTime(); dates.add(day);
		 * cal.add(Calendar.SECOND, -1); day = cal.getTime(); dates.add(day);
		 * cal.add(Calendar.DATE, 1); day = cal.getTime(); dates.add(day);
		 * cal.add(Calendar.DATE, 1); day = cal.getTime(); dates.add(day);
		 * cal.add(Calendar.DATE, 1); day = cal.getTime(); dates.add(day);
		 * cal.add(Calendar.DATE, 1); day = cal.getTime(); dates.add(day);
		 * cal.add(Calendar.DATE, 1); day = cal.getTime(); dates.add(day);
		 * cal.add(Calendar.DATE, 1); day = cal.getTime(); dates.add(day);
		 * cal.add(Calendar.DATE, 1); day = cal.getTime(); dates.add(day);
		 */

		cal2.add(Calendar.DATE, 1);
		day = cal2.getTime();
		dates.add(day);
		cal2.add(Calendar.DATE, 1);
		day = cal2.getTime();
		dates.add(day);
		cal2.add(Calendar.DATE, 1);
		day = cal2.getTime();
		dates.add(day);
		/*
		 * cal2.add(Calendar.DATE, 1); day = cal2.getTime(); dates.add(day);
		 * cal2.add(Calendar.DATE, 1); day = cal2.getTime(); dates.add(day);
		 * cal2.add(Calendar.DATE, 1); day = cal2.getTime(); dates.add(day);
		 * cal2.add(Calendar.DATE, 1); day = cal2.getTime(); dates.add(day);
		 * cal2.add(Calendar.DATE, 1); day = cal2.getTime(); dates.add(day);
		 * cal2.add(Calendar.DATE, 1); day = cal2.getTime(); dates.add(day);
		 * cal2.add(Calendar.DATE, 1); day = cal2.getTime(); dates.add(day);
		 * cal2.add(Calendar.DATE, 1); day = cal2.getTime(); dates.add(day);
		 * cal2.add(Calendar.DATE, 1); day = cal2.getTime(); dates.add(day);
		 * cal2.add(Calendar.DATE, 1); day = cal2.getTime(); dates.add(day);
		 */

		// System.out.println(dates.toString());
		// quickSort3(dates, 0, dates.size()-1);
		// getDateRange(dates);
		// formatDate(new Date());
		System.out.println(getProductIdFromProductNumberAndOptionCd("test", ""));
		System.out.println(getProductIdFromProductNumberAndOptionCd("test", " "));
		System.out.println(getProductIdFromProductNumberAndOptionCd("test",
				null));
		System.out.println(getProductIdFromProductNumberAndOptionCd("test",
				"0D1"));

	}

}
