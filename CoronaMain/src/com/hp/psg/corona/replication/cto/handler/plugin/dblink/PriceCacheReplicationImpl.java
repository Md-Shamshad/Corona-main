package com.hp.psg.corona.replication.cto.handler.plugin.dblink;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.hp.psg.common.error.ReplicationException;
import com.hp.psg.common.util.logging.LoggerInfo;
import com.hp.psg.corona.common.util.Logger;
import com.hp.psg.corona.replication.beans.PriceCacheDataBean;
import com.hp.psg.corona.replication.dao.ReplicationDao;
import com.hp.psg.corona.replication.plugin.interfaces.IReplicationPlugin;
import com.hp.psg.corona.replication.plugin.interfaces.ReplicationResult;

public class PriceCacheReplicationImpl implements IReplicationPlugin{
	public static final String SUCCESSFUL = "S";
	public static final String FAILED = "F";
	public static final String PARTIALSUCCESSFUL = "W";
	public static final String DEL_FLAG="Y";
	public static final String ERROR = "ER";
	private LoggerInfo logInfo=null;
	public static int RepCount = 0;	
	public PriceCacheReplicationImpl(){
		logInfo = new LoggerInfo (this.getClass().getName());
	}
	
	public ReplicationResult replicate (List<PriceCacheDataBean> priceCacheList) {
		long startTime = System.currentTimeMillis();
		RepCount++;
		Logger.repInfo(logInfo,"replicate",RepCount + " starts : " + startTime);

		Logger.repInfo(logInfo,"replicate","A list of priceCacheDataBean objects are passed in");
		ReplicationResult repResult = new ReplicationResult();
		
		int allNumber = 0;
		if(priceCacheList==null || priceCacheList.size()==0){
			Logger.repInfo(logInfo, "replicate", "@@@@@@@@@@@@@@@@@@ Input:: Bean list has 0");
			repResult.setStatus(SUCCESSFUL);
			repResult.setNumberOfRecordsProcessed(0l);
			Logger.repInfo(logInfo,"replicate",RepCount +" ***ElapsedTime : " + getElapsedTime(startTime));
			return repResult;
		}else {
			allNumber = priceCacheList.size();
			Logger.repInfo(logInfo, "replicate", "@@@@@@@@@@@@@@@@@@ Input:: Bean list has "+allNumber);
			PriceCacheDataBean firstDataBean = priceCacheList.get(0);
			
			StringBuffer strBuffer = new StringBuffer();
			strBuffer.append(firstDataBean.getConfigIdStr()); strBuffer.append("/");
			strBuffer.append(firstDataBean.getPriceId()); strBuffer.append("/");
			strBuffer.append(firstDataBean.getShipToCountry()); strBuffer.append("/");
			strBuffer.append(firstDataBean.getPriceDescriptor()); strBuffer.append("/");
			strBuffer.append("\n");
			for(int i=allNumber-1; i>-1; i--){
				strBuffer.append(priceCacheList.get(i).getMaterialId());
				strBuffer.append("\n");
				String priceSource = priceCacheList.get(i).getPriceSource();
				if(priceSource==null||priceSource.length()==0||priceSource.equalsIgnoreCase(ERROR)){
					priceCacheList.get(i).setDelFlag(DEL_FLAG);
				}
			}
//			Logger.repInfo(logInfo, "replicate", strBuffer.toString());

			strBuffer = null;
		}
		

		
		// Get Map of Objects on Object Keys
		List<PriceCacheDataBean> updateList = new ArrayList<PriceCacheDataBean> ();
		List<PriceCacheDataBean> deleteList = new ArrayList<PriceCacheDataBean> ();
		List<PriceCacheDataBean> insertList = new ArrayList<PriceCacheDataBean> ();

		List<Long> errorList = new ArrayList<Long>();
		
		boolean done = producePriceCacheList(priceCacheList,updateList,deleteList,insertList);
		
		Logger.repInfo(logInfo, "replicate", "Produce the transactioin list:   done = "+done);
		StringBuffer strBuffer = new StringBuffer();
		if(done){
			
			List<PriceCacheDataBean> failedDeleteList = deletePriceCacheRecordsList(deleteList);
			List<PriceCacheDataBean> failedUpdateList = updatePriceCacheRecordsList(updateList);
			List<PriceCacheDataBean> failedInsertList = insertPriceCacheRecordsList(insertList);
			
			appendMaterialInfo(strBuffer, insertList, deleteList, updateList, failedInsertList, failedDeleteList, failedUpdateList);
			
			if(failedInsertList!=null){
				for(int k=0; k<failedInsertList.size(); k++){
					errorList.add(new Long(failedInsertList.get(k).getPriceInfoId()));
				}
			}
			if(failedDeleteList!=null){
				for(int k=0; k<failedDeleteList.size(); k++){
					errorList.add(new Long(failedDeleteList.get(k).getPriceInfoId()));
				}
			}
			if(failedUpdateList!=null){
				for(int k=0; k<failedUpdateList.size(); k++){
					errorList.add(new Long(failedUpdateList.get(k).getPriceInfoId()));
				}
			}
			
//			Logger.repDebug(getClass().getName(), "replicate", strBuffer.toString());
			
			strBuffer=null;

		}else{ //fail all
			Logger.repDebug(getClass().getName(), "replicate", "Failed to produce transaction list - it is failed for all objects passed in");
			for(int k=0; k<priceCacheList.size(); k++){
				errorList.add(new Long(priceCacheList.get(k).getPriceInfoId()));
			}
		}
		
		
		updateList.clear();
		insertList.clear();
		deleteList.clear();
		updateList = null;
		insertList = null;	
		deleteList = null;

		int errorNumber = errorList.size();
		if(errorNumber == 0){
			repResult.setStatus(SUCCESSFUL);
			repResult.setNumberOfRecordsProcessed((long)allNumber);
		}else if(errorNumber == allNumber){
			repResult.setStatus(FAILED);
			repResult.setNumberOfRecordsProcessed(0l);
			repResult.setErrorList(errorList);
		}else if(errorNumber < allNumber){
			repResult.setStatus(PARTIALSUCCESSFUL);
			repResult.setNumberOfRecordsProcessed((long)(allNumber-errorNumber));
			repResult.setErrorList(errorList);
		}
			
Logger.repInfo(logInfo,"replicate",RepCount +" ***ElapsedTime : " + getElapsedTime(startTime));
		
		
		return repResult;
		
	}
	
	private void appendMaterialInfo(StringBuffer strBuffer, List<PriceCacheDataBean> insertList, 
			List<PriceCacheDataBean> deleteList, 
			List<PriceCacheDataBean> updateList, 
			List<PriceCacheDataBean> failedInsertList, 
			List<PriceCacheDataBean> failedDeleteList, 
			List<PriceCacheDataBean> failedUpdateList){
		strBuffer.append("To insert: ");
		strBuffer.append(insertList.size());
		strBuffer.append("\n");
		appendMaterials(strBuffer, insertList);
		if(failedInsertList!=null){
			strBuffer.append("Failed to insert: ");
			strBuffer.append(failedInsertList.size());
			strBuffer.append("\n");
			appendMaterials(strBuffer, failedInsertList);
		}else{
			strBuffer.append("Failed to insert: 0\n");
		}
		
		strBuffer.append("To update: ");
		strBuffer.append(updateList.size());
		strBuffer.append("\n");
		appendMaterials(strBuffer, updateList);
		if(failedUpdateList!=null){
			strBuffer.append("Failed to update: ");
			strBuffer.append(failedUpdateList.size());
			strBuffer.append("\n");
			appendMaterials(strBuffer, failedUpdateList);
		}else{
			strBuffer.append("Failed to update: 0\n");
		}
		
		strBuffer.append("To delete: ");
		strBuffer.append(deleteList.size());
		strBuffer.append("\n");
		appendMaterials(strBuffer, deleteList);
		if(failedDeleteList!=null){
			strBuffer.append("Failed to update: ");
			strBuffer.append(failedDeleteList.size());
			strBuffer.append("\n");
			appendMaterials(strBuffer, failedDeleteList);
		}else{
			strBuffer.append("Failed to delete: 0\n");
		}
		return;
	}
	
	private void appendMaterials(StringBuffer strBuffer, List<PriceCacheDataBean> insertList){
		for(int i=0; i< insertList.size(); i++){
			strBuffer.append(insertList.get(i).getMaterialId());
			strBuffer.append("\n");
		}
		return;
	}
	
	// In the replication task, we connect to destination table and check if
	// the record exists. If the record is already present then we update the
	// record else insert the record into the database.
	public boolean producePriceCacheList(List<PriceCacheDataBean> priceCacheList,
			List<PriceCacheDataBean> updateList,
			List<PriceCacheDataBean> deleteList,
			List<PriceCacheDataBean> insertList){
		
		boolean successful = true;
		Logger.repInfo(logInfo, "producePriceCacheList", "Total number of Records in the list : " +priceCacheList.size());
		Iterator<PriceCacheDataBean> it = priceCacheList.iterator();
		long countDeleted = 0;
		long countInserted = 0;
		long countUpdated = 0;
long startTime = System.currentTimeMillis();		
Logger.repInfo(logInfo,"producePriceCacheList"," starts : " + startTime);
		
		PriceCacheDataBean priceObject = priceCacheList.get(0);
		int configId = priceObject.getConfigId();
		String priceId = priceObject.getPriceId();
		String shipToCountry = priceObject.getShipToCountry();
		String priceDescriptor = priceObject.getPriceDescriptor();
		Logger.repInfo(logInfo, "producePriceCacheList",configId+"  "+priceId+"  "+shipToCountry+"  "+priceDescriptor);
		
		try {
			Map<String,PriceCacheDataBean> priceMap = getPriceMap(configId,
					priceId,
					shipToCountry,
					priceDescriptor);
			

			while (it.hasNext()) {
				PriceCacheDataBean priceInfo = it.next();
				
				try {
					if (priceInfo.getDelFlag().equals("Y")){
						deleteList.add(priceInfo);
						countDeleted++;
					} else {
						// check if the record already exists in the table
						PriceCacheDataBean test = null;
						if(priceMap!=null){
							test = priceMap.get(priceInfo.getMaterialId());
						}
						if(test!=null){						
							updateList.add(priceInfo);
							countUpdated++;
						}else{
							insertList.add(priceInfo);
							countInserted++;
						}
					}
				}// try
				catch (Exception ex) {
					Logger.repError(logInfo, "producePriceCacheList", ex);
					successful = false;
					break;
				}

			}// while
			
			
		} catch (Exception ex) {
			Logger.repError(logInfo, "producePriceCacheList", ex);
			successful = false;
					
		} 
Logger.repInfo(logInfo,"producePriceCacheList"," ElapsedTime : " + getElapsedTime(startTime));	
		return successful;

	}
	
	Map<String,PriceCacheDataBean> getPriceMap(int configId,
			String priceId,
			String shipToCountry,
			String priceDescriptor){
			
		Map<String, PriceCacheDataBean> priceMap = null;
		try{
			List<PriceCacheDataBean> priceInfoList = ReplicationDao.getPriceMapFromEDA(configId,priceId,shipToCountry,priceDescriptor);
			
			if(priceInfoList!=null ){
				priceMap =new HashMap<String, PriceCacheDataBean>();
				for(int i=0; i<priceInfoList.size(); i++){
					PriceCacheDataBean priceInfo = priceInfoList.get(i);
					priceMap.put(priceInfo.getMaterialId(), priceInfo);
				}
				
			}
		}catch(Exception e){
			Logger.repError(logInfo, "producePriceCacheList", e);
		}
		return priceMap;
	}
	
	List<PriceCacheDataBean>  insertPriceCacheRecordsList(List<PriceCacheDataBean> insertList){
long startTime = System.currentTimeMillis();		
Logger.repInfo(logInfo,"insertPriceCacheRecordsList"," starts : " + startTime);
		List<PriceCacheDataBean>  failedList = null;
		try{
			failedList = ReplicationDao.insertPriceCacheRecordsList(insertList);
		}catch (ReplicationException ce){
			failedList = new ArrayList<PriceCacheDataBean> ();
			failedList.addAll(insertList);
			Logger.repError(logInfo, "insertPriceCacheRecordsList", ce);
		}
Logger.repInfo(logInfo,"insertPriceCacheRecordsList"," ElapsedTime : " + getElapsedTime(startTime));			
		return failedList;
		
	}
	
	List<PriceCacheDataBean>  deletePriceCacheRecordsList(List<PriceCacheDataBean> deleteList){
		List<PriceCacheDataBean>  failedList = null;
long startTime = System.currentTimeMillis();		
Logger.repInfo(logInfo,"deletePriceCacheRecordsList"," starts : " + startTime);
		try{
			failedList = ReplicationDao.deletePriceCacheRecordsList(deleteList);
		}catch (ReplicationException ce){
			failedList = new ArrayList<PriceCacheDataBean> ();
			failedList.addAll(deleteList);
			Logger.repError(logInfo, "deletePriceCacheRecordsList", ce);
		}
Logger.repInfo(logInfo,"deletePriceCacheRecordsList"," ElapsedTime : " + getElapsedTime(startTime));	
		return failedList;
	}
	
	List<PriceCacheDataBean>  updatePriceCacheRecordsList(List<PriceCacheDataBean> updateList){
long startTime = System.currentTimeMillis();		
Logger.repInfo(logInfo,"updatePriceCacheRecordsList"," starts : " + startTime);
		List<PriceCacheDataBean>  failedList = null;
		try{
			failedList = ReplicationDao.updatePriceCacheRecordsList(updateList);
		}catch (ReplicationException ce){
			failedList = new ArrayList<PriceCacheDataBean> ();
			failedList.addAll(updateList);
			Logger.repError(logInfo, "updatePriceCacheRecordsList", ce);
		}
Logger.repInfo(logInfo,"updatePriceCacheRecordsList"," ElapsedTime : " + getElapsedTime(startTime));	
		return failedList;
	}
	
	
	
	
	public static void main(String[] args) {

		try {
			System.out.println("@@@@@@@@@ Start CtoNetPricePropagation main");

			List<PriceCacheDataBean> priceCacheList = new ArrayList<PriceCacheDataBean> ();
			String[] materialList = new String[]{"408855-B21","408855-B21#0D1"};
			Date date = new Date();
			Calendar cal = Calendar.getInstance();
			cal.set(2010, 4, 9);
			Date start = cal.getTime();
			cal.set(2014,3,31);
			Date end = cal.getTime();
			String addi ="<?xml version=\"1.0\" encoding=\"utf-8\"?><headers> <baseNetPrice>  <header id=\"price\">14454.25</header>  <header id=\"priceSource\">PA</header>  <header id=\"listPrice\">17005.00</header>  <header id=\"agreement\">AG435</header>  <header id=\"exibitNumber\">P58EM</header>  <header id=\"discountColumn\">01</header>  <header id=\"discountPct\">15.00</header> </baseNetPrice></headers>";
			for (int i = 0; i < materialList.length; i++) { 
				PriceCacheDataBean onePriceCache = new PriceCacheDataBean();
				onePriceCache.setDelFlag("N");
				onePriceCache.setConfigIdStr("04742004");
				onePriceCache.setConfigId(4742004);
				onePriceCache.setPriceId("P11616");
				onePriceCache.setPriceDescriptor("MYMYRDP");
				onePriceCache.setShipToCountry("MY");
				onePriceCache.setLastModifiedDate(date);
				onePriceCache.setPriceSource("PA");
				onePriceCache.setListPrice("17005");
				onePriceCache.setNetPrice("14454.25");
				onePriceCache.setPriceStartDate(start);
				onePriceCache.setPriceEndDate(end);
				onePriceCache.setMaterialId(materialList[i]);
				onePriceCache.setAdditionalData(addi);
				priceCacheList.add(onePriceCache);
				
			}
			addi ="<?xml version=\"1.0\" encoding=\"utf-8\"?><headers>  <header id=\"price\">0.00</header>  <header id=\"priceSource\">PA</header>  <header id=\"listPrice\">0.00</header>  <header id=\"agreement\">AG435</header>  <header id=\"exibitNumber\">P58EM</header>  <header id=\"discountColumn\">01</header>  <header id=\"discountPct\">15.00</header> </headers>";
			priceCacheList.get(1).setAdditionalData(addi);


			PriceCacheReplicationImpl priceReplication = new PriceCacheReplicationImpl();
			ReplicationResult result = priceReplication.replicate (priceCacheList);
			
		}catch(Exception e){
			
		}
	}
	
   
    public double getElapsedTime(long startTime) {
    	long endtime = System.currentTimeMillis();
        return ( (endtime - startTime) / 1000.000);
   }
    
}
