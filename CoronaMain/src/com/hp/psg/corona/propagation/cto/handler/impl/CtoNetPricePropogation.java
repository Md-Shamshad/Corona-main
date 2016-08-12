package com.hp.psg.corona.propagation.cto.handler.impl;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.hp.psg.common.error.CoronaException;
import com.hp.psg.common.util.logging.LoggerInfo;
import com.hp.psg.corona.common.beans.CTODaxDataBeanGeneral;
import com.hp.psg.corona.common.cto.beans.ConfigHeaderInfo;
import com.hp.psg.corona.common.cto.beans.ConfigPermutationPriceInfo;
import com.hp.psg.corona.common.cto.beans.ConfigPriceHeaderInfo;
import com.hp.psg.corona.common.cto.beans.CoronaBaseObject;
import com.hp.psg.corona.common.cto.beans.PriceInfo;
import com.hp.psg.corona.common.util.CoronaFwkUtil;
import com.hp.psg.corona.common.util.Logger;
import com.hp.psg.corona.propagation.dao.PropagationEventsDao;
import com.hp.psg.corona.propagation.handler.PropagationHandlerUtilities;
import com.hp.psg.corona.propagation.handler.interfaces.IBeanPropagation;
import com.hp.psg.corona.propagation.handler.util.ConfigDataChangeUtil;

/*
* @author sjoshi
* @version 1.0
*
*/
public class CtoNetPricePropogation implements IBeanPropagation {
	public static String DATE_FORMAT = "MM/dd/yyyy";
	public static SimpleDateFormat dateFormat = new SimpleDateFormat(DATE_FORMAT);
	public static String INPUT_DATE_KEY = "dateObjStr";
	public static String INPUT_OFFSET_KEY = "offsetStr";
	private Date startDate;
	private int offsetPeriod;	// 0 means today;
	LoggerInfo logInfo=null;
	
	public CtoNetPricePropogation(String dateStr, int offset){
		try{
			startDate = dateFormat.parse(dateStr);
		}catch (ParseException e){
			Logger.info(logInfo,"CtoNetPricePropogation", "Date format error - useing current date ");
			startDate = new Date();
		}
		offsetPeriod = offset;
		logInfo = new LoggerInfo (this.getClass().getName());
	}
	
	
	public CtoNetPricePropogation(){
		logInfo = new LoggerInfo (this.getClass().getName());
	}
	
	/**
	 * Interface method implemented.
	 * @return void
	 * Method called for setting the instance parameters... before starting processing , values are being set in construct_args for the process type.
	 * 
	 */
	public void prepareBean(Map<String, Object> constructArgs) {
		// TODO Auto-generated method stub
		Logger.debug(logInfo, "prepareBean", "Prepare bean method called for CtoNetPropaagation object with "+constructArgs.size() +" arguments");
		
		if(constructArgs != null && constructArgs.size()> 0){
			try{
				startDate = (Date) constructArgs.get(INPUT_DATE_KEY);
				Logger.debug(logInfo, "prepareBean"," The date is: "+startDate.toString());
				if(startDate==null){
					startDate = new Date();
				}
			}catch(Exception e){
				startDate = new Date();
			}
			
			try{
				Integer days = (Integer)constructArgs.get(INPUT_OFFSET_KEY);
				Logger.debug(logInfo, "prepareBean"," The off set number is: "+days);
				if(days==null){
					offsetPeriod = 1;
				}else{
					offsetPeriod = days.intValue();
				}
			}catch(Exception e){
				offsetPeriod = 1;
			}
			for (String strKey : constructArgs.keySet()){
				System.out.println("Key  --- > "+ strKey + " value  -- > "+constructArgs.get(strKey));
				
			}
		}
	}	
	
	public void setStartDate(Date start){
		this.startDate = start;
	}
	
	public void setOffsetPeriod(int offset){
		this.offsetPeriod = offset;
	}
	
	public Date getStartDate(){
		return this.startDate;
	}
	
	public int getOffsetPeriod(){
		return this.offsetPeriod;
	}
	
	
	/**
	 * Interface method implemented.
	 * 
	 * @return CTODaxDataBeanGeneral
	 * @throws CoronaException
	 */
	public CTODaxDataBeanGeneral processBeans(CTODaxDataBeanGeneral generalBean)
			throws CoronaException {
		if (generalBean == null) {
			throw new CoronaException(
					"CtoNetPricePropogation:: NULL Input CTODaxDataBeanGeneral has been passed as null");
		}
		

		try{
			String valueInType = null;
			Map<CoronaBaseObject, List<? extends CoronaBaseObject>> header2ConfigPriceHeaderInfoMap = Collections.EMPTY_MAP;
			Map<CoronaBaseObject, List<? extends CoronaBaseObject>> configPriceHeaderInfo2PriceInfoMap = Collections.EMPTY_MAP;
			List<Map<CoronaBaseObject, List<? extends CoronaBaseObject>>> myMappingList = generalBean
					.getListMapRelations();
	
	
			// Get Map of Objects on Object Keys
			for (Iterator<Map<CoronaBaseObject, List<? extends CoronaBaseObject>>> iterator = myMappingList
					.iterator(); iterator.hasNext();) {
				Map<CoronaBaseObject, List<? extends CoronaBaseObject>> myMap = iterator
						.next();
				valueInType = CoronaFwkUtil.getType(myMap);
				if ("PriceInfo".equals(valueInType))
					configPriceHeaderInfo2PriceInfoMap = myMap;
				else if ("ConfigPriceHeaderInfo".equals(valueInType))
					header2ConfigPriceHeaderInfoMap = myMap;
			}
	
			ConfigHeaderInfo[] chi = generalBean.getConfigHeaderInfo();	//size
	
			Logger.info(logInfo,"processBeans", "CtoNetPricePropogation processBeans "
					+ " \n configPriceHeaderInfo2PriceInfoMap size = "
					+ configPriceHeaderInfo2PriceInfoMap.keySet().size()
					+ " \n header2ConfigPriceHeaderInfoMap size = "
					+ header2ConfigPriceHeaderInfoMap.keySet().size()
					+ " \n ConfigHeaderInfo[] length = " + chi.length);
	
			CtoNetPricePropogationAUX ctoPPX  = new CtoNetPricePropogationAUX();
			Date newDate = null;
			if(startDate == null){
				newDate = new Date();
				newDate = ConfigDataChangeUtil.formatDate(newDate);
			}else{
				newDate = ConfigDataChangeUtil.formatDate(startDate);
			}
				
			Calendar thisDate = Calendar.getInstance();
			thisDate.setTime(newDate);
			thisDate.add(Calendar.DATE, offsetPeriod);
			System.out.println(thisDate.getTime());
			Date futureStartDate = thisDate.getTime();
			
			for(int i=0; i<chi.length; i++ ) {	
				ConfigHeaderInfo configHeader = chi[i];
				// yls - use the cphiList reference if exist
				List<ConfigPriceHeaderInfo> cphiList = null;
				if (header2ConfigPriceHeaderInfoMap.containsKey(configHeader))
					cphiList = (List<ConfigPriceHeaderInfo>) header2ConfigPriceHeaderInfoMap
							.get(configHeader);
					if(cphiList!=null && cphiList.size()>0){	//at most, it contains one cphi record
						for (int m=0; m<cphiList.size(); m++){
							ConfigPriceHeaderInfo cphi = cphiList.get(m);
							List<PriceInfo> piList = (List<PriceInfo>) configPriceHeaderInfo2PriceInfoMap.get(cphi);
						
							processOneConfigPriceHeaderInfo(ctoPPX, newDate, futureStartDate, 
									configHeader, cphi, piList,generalBean);
						}
					}
			}
		}catch(Exception ex){
			Logger.info(logInfo,"processBeans",ex.getMessage());
		}
		
		ConfigPermutationPriceInfo[] cppis =  generalBean.getConfigPermutationPriceInfo();
		
		if (cppis != null && cppis.length>0){
			for (int i = 0; i < cppis.length; i++) { 
				ConfigPermutationPriceInfo cppi = cppis[i]; 
				Logger.info(logInfo,"processBeans", cppi.getBundleId()+" "+cppi.getPriceDescriptor()+" "+cppi.getPriceId()+" "+cppi.getShipToCountry());
				Logger.info(logInfo,"processBeans", "price status=" + cppi.getPriceStatus()+" price net price=" + cppi.getNetPrice()
						+" price start date=" + cppi.getPriceStartDate()+" price end date=" + cppi.getPriceEndDate()+" update flag=" + cppi.getUpdtFlag());
			}
		}
		
		return generalBean;
	}
	

	public void processOneConfigPriceHeaderInfo(CtoNetPricePropogationAUX ctoPPX,
			Date currentDate, Date futureDate, ConfigHeaderInfo chi,ConfigPriceHeaderInfo cphi,List<PriceInfo> piList,
			CTODaxDataBeanGeneral generalBean){

		CtoNetPricePropogationAUX.Status status = ctoPPX.processForConfigPermutationPriceInfo(
				chi,cphi,piList,generalBean,currentDate);
		Date futureTimestamp = new Timestamp(futureDate.getTime());
		Logger.info(logInfo,"processOneConfigPriceHeaderInfo", "status.needFuturePricing()"+status.needFuturePricing()); 
		while(status!=null && status.needFuturePricing()){
			Date currentEndDate = status.getCurrentEndDate();
			Logger.info(logInfo,"processOneConfigPriceHeaderInfo", "status.needFuturePricing()"+currentEndDate.toString()); 
			if(currentEndDate!=null && currentEndDate.compareTo(futureTimestamp)<0){
				//find new start date
				Calendar thisDate = Calendar.getInstance();
				thisDate.setTime(currentEndDate);
				thisDate.add(Calendar.DATE, 1);
				System.out.println("New start date = "+thisDate.getTime());
				status = ctoPPX.processForConfigPermutationPriceInfo(chi,cphi,piList,generalBean,thisDate.getTime());
			}else{
				break;
			}
		}

	}
	
	

	public static void main(String[] args) {

		try {
			System.out.println("@@@@@@@@@ Start CtoNetPricePropagation main");

			PropagationEventsDao ped = new PropagationEventsDao();
			CTODaxDataBeanGeneral cddbg = ped.getConfigPermPriceUpdate( new Long(150153),
					new Long(51096), "P299990", "04813988", "FR",
					"FREURDP");

			//Mapping 
			PropagationHandlerUtilities.mapRelationsWithMultipleKeys(cddbg,"com.hp.psg.corona.common.cto.beans.ConfigHeaderInfo",
					"com.hp.psg.corona.common.cto.beans.ConfigPriceHeaderInfo",
					"bundleId|shipToCountry","|");
			
			PropagationHandlerUtilities.mapRelationsWithMultipleKeys(cddbg,"com.hp.psg.corona.common.cto.beans.ConfigPriceHeaderInfo",
					"com.hp.psg.corona.common.cto.beans.PriceInfo",
					"bundleId|shipToCountry|priceId|priceDescriptor","|");
			
			
			PriceInfo[] priceInfo = cddbg.getPriceInfo();

			for (int i = 0; i < priceInfo.length; i++) { 
				PriceInfo pi = priceInfo[i]; 
				System.out.println("prod=" + pi.getProductId()+" Start = "+pi.getPriceStartDate()+" End = "+
						pi.getPriceEndDate()+"    "+pi.getNetPrice()+"  "+pi.getConfigQty()+"  "+pi.getPriceStatus());
			}
			Calendar cal = Calendar.getInstance();
			cal.setTime(new Date());
			cal.add(Calendar.HOUR, 7);
			Date dateToStart = cal.getTime();
			System.out.println(dateToStart);
			
			//String date1 = "3/31/2010";
			Map<String, Object> constructArgs = new HashMap<String, Object>();
			//constructArgs.put("dateObjStr", dateFormat.parse(date1));
			constructArgs.put("dateObjStr", dateToStart);
			constructArgs.put("offsetStr", new Integer(1));
			
			//CtoNetPricePropogation netPrcProp = new CtoNetPricePropogation(date1, 3);
			CtoNetPricePropogation netPrcProp = new CtoNetPricePropogation();
			netPrcProp.prepareBean(constructArgs);
			CTODaxDataBeanGeneral cddbg1 = netPrcProp.processBeans(cddbg);
			
			ConfigPermutationPriceInfo[] cppis =  cddbg1.getConfigPermutationPriceInfo();
			
			if (cppis != null && cppis.length>0)
			for (int i = 0; i < cppis.length; i++) { 
				ConfigPermutationPriceInfo cppi = cppis[i]; 
				System.out.println(cppi.getBundleId()+" "+cppi.getPriceDescriptor()+" "+cppi.getPriceId()+" "+cppi.getShipToCountry());
				System.out.println("price status=" + cppi.getPriceStatus());
				System.out.println("price net price=" + cppi.getNetPrice());
				System.out.println("price start date=" + cppi.getPriceStartDate());
				System.out.println("price end date=" + cppi.getPriceEndDate());
				System.out.println("update flag=" + cppi.getUpdtFlag());
			}
			
			System.out.println(cddbg.getConfigPermutationPriceInfo().length);
			
			PropagationEventsDao.processConfigPermPriceUpdate(new Long(150153),cddbg.getConfigPermutationPriceInfo() );
			
			System.out.println("@@@@@@@@@ Stop CtoNetPricePropagation main");
		} catch (Exception ex) {
			System.err.println("Caught an unexpected exception!");
			ex.printStackTrace();
		}
	}
}
