package com.hp.psg.corona.propagation.cto.handler.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.hp.psg.common.error.CoronaException;
import com.hp.psg.common.util.logging.LoggerInfo;
import com.hp.psg.corona.common.beans.CTODaxDataBeanGeneral;
import com.hp.psg.corona.common.constants.CTOConstants;
import com.hp.psg.corona.common.cto.beans.ConfigHeaderInfo;
import com.hp.psg.corona.common.cto.beans.ConfigPermutationInfo;
import com.hp.psg.corona.common.util.Logger;
import com.hp.psg.corona.propagation.handler.interfaces.IBeanPropagation;


/*
 * 
 * ProcessType : Config_Perm_To_Config_Country
 * 
 * In this Class the input is ConfigPermutationInfo[]. 
 * This is read and based on the content in ConfigHeaderInfo[], Add/Modify/Update operation is performed
 * 
 * 
 * 
 * @author sandeep
 * @version 1.0
 *
 */
public class CtoConfPermPropagation implements IBeanPropagation {

	LoggerInfo logInfo=null;
	
	public CtoConfPermPropagation() {
		// TODO Auto-generated constructor stub
		logInfo = new LoggerInfo (this.getClass().getName());	
	}

	public void prepareBean(Map<String, Object> constructArgs) {
		// TODO Auto-generated method stub
		
	}	
	public CTODaxDataBeanGeneral processBeans(CTODaxDataBeanGeneral generalBean)
			throws CoronaException {
		String priceId = null;
		String bundleId = null;
		String ship2Ctry = null;
		String priceDescriptor = null;
		String registerIPCS = null;
		String delFlag = null;
		// String lastModifiedBy = null;
		String regionalCode = null;
		boolean flag = false;

		if (generalBean == null) {
			throw new CoronaException(
					"CTOConfPermTransformation:: NULL Input CTODaxDataBeanGeneral has been passed as null");
		}
		List<ConfigHeaderInfo> chList = new ArrayList<ConfigHeaderInfo>();

		Logger.debug(logInfo, "processBeans","CTOConfPermTransformation::processBeans: Before entering into generalBean.getConfigPermutationInfo() FOR LOOP");

		for (int i = 0; i < generalBean.getConfigPermutationInfo().length; i++) {

			Logger.debug(logInfo, "processBeans","CTOConfPermTransformation::processBeans: Entered generalBean.getConfigPermutationInfo() FOR LOOP");

			ConfigPermutationInfo cpInfo = generalBean
					.getConfigPermutationInfo()[i];
			priceId = cpInfo.getPriceId();
			bundleId = cpInfo.getBundleId();
			ship2Ctry = cpInfo.getShipToCountry();
			priceDescriptor = cpInfo.getPriceDescriptor();
			delFlag = cpInfo.getDelFlag();
			registerIPCS = cpInfo.getRegisterIPCS();
			// lastModifiedBy = cpInfo.getLastModifiedBy();
			regionalCode = cpInfo.getRegionCode();

			flag = false;

			for (int j = 0; j < generalBean.getConfigHeaderInfo().length; j++) {
				Logger.debug(logInfo, "processBeans","CTOConfPermTransformation::processBeans: Start generalBean.getConfigHeaderInfo() FOR LOOP"+ j);

				ConfigHeaderInfo chInfo = generalBean.getConfigHeaderInfo()[j];
				String chInfoStr = chInfo.getBundleId()
						+ chInfo.getShipToCountry();
				if (chInfoStr.equals(bundleId + ship2Ctry)) {
					Logger.debug(logInfo, "processBeans","CTOConfPermTransformation::processBeans: MATCH FOUND ["
									+ chInfoStr + "]");
						
					if (chInfo.getDelFlag().equals(delFlag)) {
						Logger.debug(logInfo, "processBeans","CTOConfPermTransformation::processBeans: UPDATE FLAG is U. KEY ["
										+ chInfoStr
										+ "]. CHI-DEL-FLAG =["
										+ chInfo.getDelFlag()
										+ "] CPI-DEL-FLAG = [" + delFlag + "]");
						// String recordChange = "N";
						/*
						 * Author: Anand Gangoor
						 * We will reuse the update flag to set the register flag to Y in CHI. 
						 * If the IPCS register flag is R and if chi register flag is N , then set the udpate flag as M 
						 * which will ensure that register flag is set to Y in CHI
						 * The OR condition is to ensure that incase the register flag was already set in previous permutation iteration
						 * */
						Logger.debug(logInfo, "processBeans","CTOConfPermTransformation::processBeans: Reregistering for ["
								+ chInfoStr
								+ "] Register IPCS =[" +registerIPCS + "] CHI Register Flag =["+chInfo.getRegisterFlag()+ "] Modified Falg=["+chInfo.getUpdtFlag()+"].");
						
						if(("R".equals(registerIPCS) && "N".equals(chInfo.getRegisterFlag())) || CTOConstants.UPDT_FLAG_MODIFIED.equals(chInfo.getUpdtFlag()))
						{ 
 
							chInfo.setUpdtFlag(CTOConstants.UPDT_FLAG_MODIFIED); 
						}    
						else
						{
							
							chInfo.setUpdtFlag(CTOConstants.UPDT_FLAG_UNCHANGED);
						} 
						
						chInfo.setLastModifiedBy(CTOConstants.CPI2CHI_LASTMOD_NAME); 
						chInfo.setTrnId(CTOConstants.CPI2CHI_TRANSACTION_ID);
						chInfo.setRegionCode(regionalCode);
						chList.add(chInfo);
						Logger.debug(logInfo, "processBeans","CTOConfPermTransformation::processBeans: UPDATE FLAG is U. KEY ["
										+ chInfoStr
										+ "] Values = [updflag = U] and regioncode = ["
										+ regionalCode + "]");
						flag = true;
					} else if ((delFlag 
							.equals(CTOConstants.DELETE_FLAG_NOT_SET) && chInfo
							.getDelFlag().equals(CTOConstants.DELETE_FLAG_SET))
							|| (delFlag.equals(CTOConstants.DELETE_FLAG_SET) && chInfo
									.getDelFlag().equals(
											CTOConstants.DELETE_FLAG_NOT_SET))) {
						Logger.debug(logInfo, "processBeans","CTOConfPermTransformation::processBeans: UPDATE FLAG is M. KEY ["
										+ chInfoStr
										+ "]. CHI-DEL-FLAG =["
										+ chInfo.getDelFlag()
										+ "] CPI-DEL-FLAG = [" + delFlag + "]");
						// String recordChange = "Y";
						chInfo.setUpdtFlag(CTOConstants.UPDT_FLAG_MODIFIED);
						chInfo
								.setLastModifiedBy(CTOConstants.CPI2CHI_LASTMOD_NAME);
						chInfo.setTrnId(CTOConstants.CPI2CHI_TRANSACTION_ID);
						chInfo.setDelFlag(delFlag);
						chInfo.setRegionCode(regionalCode);
						chList.add(chInfo);
						Logger.debug(logInfo, "processBeans","CTOConfPermTransformation::processBeans: UPDATE FLAG is M. KEY ["
										+ chInfoStr
										+ "] Values = [updflag = M] and regioncode = ["
										+ regionalCode
										+ "] deleteflag = ["
										+ delFlag + "]");
						flag = true;
					}
				}
			}
			Logger.debug(logInfo, "processBeans","CTOConfPermTransformation::processBeans: End generalBean.getConfigHeaderInfo() FOR LOOP");
			if (!flag) {
				Logger.debug(logInfo, "processBeans","CTOConfPermTransformation::processBeans: UPDATE FLAG is A. New Record - KEY ["
								+ bundleId + ship2Ctry + "].");
				// Adding a new record to CHI
				ConfigHeaderInfo ch_Info = new ConfigHeaderInfo();
				ch_Info.setBundleId(bundleId);
				ch_Info.setDelFlag(delFlag);
				ch_Info.setShipToCountry(ship2Ctry);
				// set lastModifiedBy with a CONSTANT -- As per discussion on
				// 2/19
				ch_Info.setLastModifiedBy(CTOConstants.CPI2CHI_LASTMOD_NAME);
				ch_Info.setRegionCode(regionalCode);
				// This flag should be based on comparision for now add as is
				ch_Info.setUpdtFlag(CTOConstants.UPDT_FLAG_ADD);
				ch_Info.setTrnId(CTOConstants.CPI2CHI_TRANSACTION_ID);
				chList.add(ch_Info);
				Logger.debug(logInfo, "processBeans","CTOConfPermTransformation::processBeans: UPDATE FLAG is A. KEY ["
								+ bundleId
								+ ship2Ctry
								+ "] Values = [updflag = A] and regioncode = ["
								+ regionalCode
								+ "] deleteflag = ["
								+ delFlag
								+ "] bundleId = ["
								+ bundleId
								+ "] ship2ctry =[" + ship2Ctry + "]");
			}

			Logger.debug(logInfo, "processBeans","CTOConfPermTransformation::processBeans: Exiting generalBean.getConfigPermutationInfo() FOR LOOP");
		}

		/*
		 * HashMap<String, String> PermMap = getPermutationInfo(generalBean);
		 * 
		 * Logger.debug("processBeans: No of Permutation in Map = [" +
		 * PermMap.size() + "]" );
		 * 
		 * List chInfoList = populateConfigHeaderInfo(PermMap, generalBean);
		 * 
		 * Logger.debug("processBeans: No of ConfigHeaderInfo Populated = [" +
		 * chInfoList.size() + "]" );
		 */

		Logger.debug(logInfo, "processBeans","CTOConfPermTransformation::processBeans: No of ConfigHeaderInfo Populated with new record = ["
						+ chList.size() + "]");

		ConfigHeaderInfo[] chIArray = (ConfigHeaderInfo[]) chList
				.toArray(new ConfigHeaderInfo[chList.size()]);

		Logger.debug(logInfo, "processBeans","CTOConfPermTransformation::processBeans: Inputting into generalBean");

		generalBean.setConfigHeaderInfo(chIArray);

		Logger.debug(logInfo, "processBeans","CTOConfPermTransformation::processBeans: returning generalBean");

		return generalBean;
	}

	public List populateConfigHeaderInfo(
			HashMap<String, String> PermutationMap,
			CTODaxDataBeanGeneral generalBean) {
		String priceId = null;
		String bundleId = null;
		String ship2Ctry = null;
		String priceDescriptor = null;
		String delFlag = null;
		String lastModifiedBy = null;
		String regionalCode = null;

		Logger.debug(logInfo, "populateConfigHeaderInfo","Inside populateConfigHeaderInfo");

		Set set = PermutationMap.entrySet();
		List<ConfigHeaderInfo> chList = new ArrayList<ConfigHeaderInfo>();

		Iterator iter = set.iterator();
		while (iter.hasNext()) {
			Map.Entry mapEtry = (Map.Entry) iter.next();
			String permdta = (String) mapEtry.getValue();

			List newlst = split(permdta, "::");
			priceId = (String) newlst.get(0);
			bundleId = (String) newlst.get(1);
			ship2Ctry = (String) newlst.get(2);
			priceDescriptor = (String) newlst.get(3);
			delFlag = (String) newlst.get(4);
			lastModifiedBy = (String) newlst.get(5);
			regionalCode = (String) newlst.get(6);

			Logger.debug(logInfo, "populateConfigHeaderInfo","populateConfigHeaderInfo : Value Read From Map = ["
							+ priceId
							+ "::"
							+ bundleId
							+ "::"
							+ ship2Ctry
							+ "::"
							+ priceDescriptor
							+ "::"
							+ delFlag
							+ "::"
							+ lastModifiedBy + "::" + regionalCode + "]");

			for (int i = 0; i < generalBean.getConfigHeaderInfo().length; i++) {
				ConfigHeaderInfo cpInfo = generalBean.getConfigHeaderInfo()[i];
				if (PermutationMap.containsKey(cpInfo.getBundleId() + "::"
						+ cpInfo.getShipToCountry())) {
					// PermutationMap.get
				}
			}

			ConfigHeaderInfo chInfo = new ConfigHeaderInfo();
			chInfo.setBundleId(bundleId);
			chInfo.setDelFlag(delFlag);
			chInfo.setShipToCountry(ship2Ctry);
			// set lastModifiedBy with a CONSTANT -- As per discussion on 2/19
			chInfo.setLastModifiedBy(lastModifiedBy);
			chInfo.setRegionCode(regionalCode);
			// This flag should be based on comparision for now add as is
			chInfo.setUpdtFlag("A");
			chInfo.setTrnId(new Long(0));
			chList.add(chInfo);
		}

		Logger.debug(logInfo, "populateConfigHeaderInfo","End populateConfigHeaderInfo");

		return chList;
	}

	public HashMap<String, String> getPermutationInfo(
			CTODaxDataBeanGeneral generalBean) {
		String priceId = null;
		String bundleId = null;
		String ship2Ctry = null;
		String priceDescriptor = null;
		String delFlag = null;
		String lastModifiedBy = null;
		String regionalCode = null;

		Logger.debug(logInfo, "getPermutationInfo","Inside getPermutationInfo");

		HashMap<String, String> PermutationMap = new HashMap<String, String>();
		List<String> delflagLst = new ArrayList<String>();

		for (int i = 0; i < generalBean.getConfigPermutationInfo().length; i++) {
			ConfigPermutationInfo cpInfo = generalBean
					.getConfigPermutationInfo()[i];
			priceId = cpInfo.getPriceId();
			bundleId = cpInfo.getBundleId();
			ship2Ctry = cpInfo.getShipToCountry();
			priceDescriptor = cpInfo.getPriceDescriptor();
			delFlag = cpInfo.getDelFlag();
			lastModifiedBy = cpInfo.getLastModifiedBy();
			regionalCode = cpInfo.getRegionCode();
			// Set this to ZERO for testing.
			// cpInfo.getTrnId();

			String permdata = priceId + "::" + bundleId + "::" + ship2Ctry
					+ "::" + priceDescriptor + "::" + delFlag + "::"
					+ lastModifiedBy + "::" + regionalCode;

			Logger.debug(logInfo, "getPermutationInfo","Key Value = [" + permdata + "]");

			if (PermutationMap.size() == 0) {
				PermutationMap.put(bundleId + "::" + ship2Ctry, permdata);
				if (delFlag.equals("N")) {
					delflagLst.add(bundleId + "::" + ship2Ctry);
					Logger.debug(logInfo, "getPermutationInfo","Inside getPermutationInfo: 1 : Delete Flag Set: Key = ["
									+ bundleId + "::" + ship2Ctry + "]");
				}

			} else {
				if ((delflagLst.isEmpty()) && (delFlag.equals("N"))) {
					PermutationMap.put(bundleId + "::" + ship2Ctry, permdata);
					delflagLst.add(bundleId + "::" + ship2Ctry);
					Logger.debug(logInfo, "getPermutationInfo","Inside getPermutationInfo: 2 : Delete Flag Set: Key = ["
									+ bundleId + "::" + ship2Ctry + "]");
				} else if ((!delflagLst.isEmpty())
						&& (!delflagLst.contains(bundleId + "::" + ship2Ctry))) {
					PermutationMap.put(bundleId + "::" + ship2Ctry, permdata);
					if (delFlag.equals("N")) {
						delflagLst.add(bundleId + "::" + ship2Ctry);
						Logger.debug(logInfo, "getPermutationInfo","Inside getPermutationInfo: 3 : Delete Flag Set: Key = ["
										+ bundleId + "::" + ship2Ctry + "]");
					}
				}
			}
		}

		Logger.debug(logInfo, "getPermutationInfo","End getPermutationInfo");

		return PermutationMap;
	}
	/**
	 * Split an string by separator
	 */
	public ArrayList split(String s, String sep) {
		// static public ArrayList split ( String s, String sep ) {
		if (this.isEmpty(s)) {
			return new ArrayList();
		}

		if (this.isEmpty(sep)) {
			ArrayList list1 = new ArrayList();
			list1.add(s);
			return list1;
		}

		ArrayList list = new ArrayList();
		int len = sep.length();
		int i = 0;
		int n = 0;
		while (true) {
			int j = s.indexOf(sep, i);
			if (j < 0) {
				String t = s.substring(i);
				if (t != null) {
					t = t.trim();
				}
				if (t != null && t.length() > 0) {
					list.add(t);
				}
				break;
			} else {
				String t = s.substring(i, j);
				if (t != null) {
					t = t.trim();
				}
				if (t != null && t.length() > 0) {
					list.add(t);
				}
				i = j + len;
				n++;
			}
		}

		return list;
	}

	/**
	 * Check if a string is empty or null
	 */
	public boolean isEmpty(String s) {
		if (s == null || s.trim().length() == 0) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

}
