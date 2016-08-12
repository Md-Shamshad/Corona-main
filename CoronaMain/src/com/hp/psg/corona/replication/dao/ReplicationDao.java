/**
 * 
 */
package com.hp.psg.corona.replication.dao;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.hp.psg.common.db.dax.DaxDB;
import com.hp.psg.common.db.dax.DaxDataBeanGeneral;
import com.hp.psg.common.db.dax.DaxMgr;
import com.hp.psg.common.db.dax.DaxParsedStmt;
import com.hp.psg.common.error.CoronaException;
import com.hp.psg.common.error.ReplicationException;
import com.hp.psg.common.error.util.CoronaErrorConstants;
import com.hp.psg.common.util.logging.LoggerInfo;
import com.hp.psg.corona.common.beans.ReplicationInfo;
import com.hp.psg.corona.common.constants.CoronaFwkConstants;
import com.hp.psg.corona.common.cto.beans.ConfigPriceHeaderInfo;
import com.hp.psg.corona.common.cto.beans.PriceInfo;
import com.hp.psg.corona.common.util.CoronaFwkUtil;
import com.hp.psg.corona.common.util.Logger;
import com.hp.psg.corona.error.util.CoronaErrorHandler;
import com.hp.psg.corona.replication.beans.ConfigPriceReplication;
import com.hp.psg.corona.replication.beans.PriceCacheDataBean;

/**
 * @author dholakia
 * @version 1.0
 * 
 */
public class ReplicationDao {


	private static LoggerInfo logInfo = new LoggerInfo(
			"com.hp.psg.corona.replication.dao.ReplicationDao");

	private static final int UID_SIZE = 30;
	private static final String FETCH_REPLICATION_DATA_STMT_GROUP_ID = "REPLICATION_STMT_GROUP_ID";
	

	private static final String FETCH_IPCS_PRICE_DATA = "FETCH_IPCS_PRICE_DATA";
	private static final String UPDATE_IPCS_PRICE_INFO = "UPDATE_IPCS_PRICE_INFO";
	private static final String DELETE_IPCS_PRICE_INFO = "DELETE_IPCS_PRICE_INFO";
	private static final String INSERT_IPCS_PRICE_INFO = "INSERT_IPCS_PRICE_INFO";

	private static final String REPLICATION_INFO = "REPLICATION_INFO";
	private static final String UPDATE_REPLICATION_GROUP_TIMESTAMP="UPDATE_REPLICATION_GROUP_TIMESTAMP";
	private static final String UPDATE_REPLICATION_GROUP_LAST_MOD_TIMESTAMP="UPDATE_REPLICATION_GROUP_LAST_MOD_TIMESTAMP";
	private static final String UPDATE_REPLICATION_TIMESTAMP = "UPDATE_REPLICATION_TIMESTAMP";

	private static final String FETCH_CONFIG_PRICE_REPLICATION_EVENTS="FETCH_CONFIG_PRICE_REPLICATION_EVENTS";
	private static final String PICK_REPLICATION_EVENTS_FOR_GROUP="PICK_REPLICATION_EVENTS_FOR_GROUP";
	private static final String FETCH_PRICE_IFNO_COMP_DATA="FETCH_PRICE_IFNO_COMP_DATA";
	private static final String FETCH_CONFIG_PRICE_HEADER_DATA="FETCH_CONFIG_PRICE_HEADER_DATA";
	
	private static final String UPDATE_CPR_STATUS_TO_ERR_OR_READY="UPDATE_CPR_STATUS_TO_ERR_OR_READY";
	private static final String FETCH_LAST_REPLICATION_DATE_FOR_THIS_GROUP="FETCH_LAST_REPLICATION_DATE_FOR_THIS_GROUP";
	private static final String FETCH_PRICE_HEADER_AND_COMP_DATA="FETCH_PRICE_HEADER_AND_COMP_DATA";
	private static final String UPDATE_CPR_STATUS_TO_COMPLETED_FOR_NO_REC="UPDATE_CPR_STATUS_TO_COMPLETED_FOR_NO_REC";
	private static final String UPDATE_CPR_STATUS_TO_COMPLETED_FOR_REC="UPDATE_CPR_STATUS_TO_COMPLETED_FOR_REC";
	
	private static Map<String, ReplicationInfo> hmReplicationCache ;
	
	


	
	//This method will return the list of all configPriceReplication For all group and their processedByString.
	public static Map<String [],List<ConfigPriceReplication>> retreiveConfigHeaderEventsToReplicate(String[] groupIds)
			throws ReplicationException {

		Map<String [],List<ConfigPriceReplication>> hmGroupListMapHolder = new HashMap<String[], List<ConfigPriceReplication>>();
		
		for (String group : groupIds){
			
			Map<String [], List<ConfigPriceReplication>> hmConfigPriceReplicationList = new HashMap<String[], List<ConfigPriceReplication>>();
			//This method will update the status in CONFIG_PRICE_REPLICATION to PICKED status already.
			Logger.repInfo(logInfo, "retreiveConfigHeaderEventsToReplicate", "Picking up List of events for group "+group);

			hmConfigPriceReplicationList = pickConfigPriceRepGroupForProcessing(group);
			
			if (hmConfigPriceReplicationList != null && hmConfigPriceReplicationList.size() > 0)
				hmGroupListMapHolder.putAll(hmConfigPriceReplicationList);
		}
		
		Logger.repDebug(logInfo, "retreiveConfigHeaderEventsToReplicate", "@@@@############# Map returned for group ids and List of ConfigPriceReplcication size  == "+hmGroupListMapHolder.size());	

		return hmGroupListMapHolder;
	}

	//This will select the list of headers from DB for a specific group called from above method.
	public static Map<String[], List<ConfigPriceReplication>> pickConfigPriceRepGroupForProcessing(String groupId)  {
		
		Map<String[], List<ConfigPriceReplication>> retMap = null;
		List<ConfigPriceReplication> configPriceReplicationList = new ArrayList<ConfigPriceReplication>();
		String lastModifiedBy = null;
		
		try {
			lastModifiedBy= CoronaFwkUtil.getUniqueId(
					CoronaFwkUtil.REPLICATIION_EVENTS, UID_SIZE);

			long batchSize = 5000; //Defaulted to 5000, and also we were discussing to have the limitation in database view, verify it once.
			ReplicationInfo rInfoObj = fetchCachedReplicationInfoObj("IPCS|PRICE_CACHE");
			if (rInfoObj != null){
				batchSize = rInfoObj.getBatchSize();
			}

			
			DaxMgr dxMgr = DaxMgr.getInstance();
			DaxDataBeanGeneral dgBean = new DaxDataBeanGeneral();
			
			int grpId= (Integer.parseInt(groupId));
			dgBean.setInt1(grpId);
			dgBean.setString2(lastModifiedBy);
			dgBean.setLong1(batchSize);

			//update the status of group as PICKED with processdByString marked as lastModifiedBy.
			DaxParsedStmt dxPstmt = dxMgr.makeParsedStmt(FETCH_REPLICATION_DATA_STMT_GROUP_ID,
					PICK_REPLICATION_EVENTS_FOR_GROUP, dgBean, null, null);
			DaxDB dxDb = dxMgr.getDaxDB();
			
			int numbOfRows = dxDb.doUpdate(dxPstmt, dgBean);
			
			DaxParsedStmt dxPstmt2 = dxMgr.makeParsedStmt(FETCH_REPLICATION_DATA_STMT_GROUP_ID,
					FETCH_CONFIG_PRICE_REPLICATION_EVENTS, dgBean, null, null);
			
			
			//select the same group and return the list
			configPriceReplicationList = dxDb.doSelect(dxPstmt2, ConfigPriceReplication.class, dgBean);

			if (numbOfRows != configPriceReplicationList.size()){
				//This should not happen, raise an Error alert.
				Logger.repDebug(logInfo, "pickConfigPriceRepGroupForProcessing", "Pick size list and the dax return is not same");
			}else {
				Logger.repDebug(logInfo, "pickConfigPriceRepGroupForProcessing", "@$$$$$$$$$$$$$$$$$$$$$$ size of map coming to be replicated "+configPriceReplicationList.size()+" for group Id "+groupId);
			}

		}catch (Exception ex){
			//Exception while picking group from event table to replicate.
			ex.printStackTrace();
		}

		if (lastModifiedBy != null ){
			String str[] = {groupId,lastModifiedBy}; // Group + lastModifiedBy string
			retMap=new HashMap<String[], List<ConfigPriceReplication>>();
			retMap.put(str, configPriceReplicationList);
		}
		
		return retMap;
	}

	public static Date fetchLastReplicatedDate(int groupId){
		try {
			DaxMgr dxMgr = DaxMgr.getInstance();
			DaxDB dxDb = dxMgr.getDaxDB();
			
			DaxDataBeanGeneral dgBean2 = new DaxDataBeanGeneral();
			dgBean2.setInt1(groupId);

			DaxParsedStmt dxPstmt = dxMgr.makeParsedStmt(
					FETCH_REPLICATION_DATA_STMT_GROUP_ID,
					FETCH_LAST_REPLICATION_DATE_FOR_THIS_GROUP, null, null, null);
			
			List <DaxDataBeanGeneral> groupsUpdatedList= dxDb.doSelect(dxPstmt, DaxDataBeanGeneral.class, dgBean2);
			
			if (groupsUpdatedList != null && groupsUpdatedList.size() > 0) {
				Date dt= groupsUpdatedList.get(0).getDate1();
				if (dt != null){
					System.out.println("Returning date as "+dt);
					return dt;
				}else {
					Logger.repDebug(logInfo, "fetchLastReplicatedDate", "Date not defined for group "+groupId+" choosing the current sysdate");
					return new java.util.Date(); 
				}
			}
				
		}catch (Exception ex){
			System.out.println("Exception while fetching the date from group");
			ex.printStackTrace();
		}
		return null;
	}
	
	public static void updateLastReplicationTimestampforGroup(String groupId, Date lastPickedObjectDate){
		try{
			DaxMgr dxMgr = DaxMgr.getInstance();
			DaxDB dxDb = dxMgr.getDaxDB();
			
			DaxDataBeanGeneral dgBean2 = new DaxDataBeanGeneral();
			dgBean2.setDate1(lastPickedObjectDate);
			dgBean2.setString2("FWK_PROCESSOR");
			dgBean2.setString1(groupId);
			DaxParsedStmt dxUpdTimeStmpPstmt = null;

			dxUpdTimeStmpPstmt = dxMgr.makeParsedStmt(
				FETCH_REPLICATION_DATA_STMT_GROUP_ID,
				UPDATE_REPLICATION_GROUP_TIMESTAMP, null, null, null);
		
			int numbRowsUpdated = dxDb.doUpdate(dxUpdTimeStmpPstmt, dgBean2);

			switch (numbRowsUpdated) {
				case 0 :
					CoronaException ex = new CoronaException("No group defined in CONFIG_REPLICATION_GROUP TABLE - "+groupId);
					CoronaErrorHandler.logError(null, "No group defined in CONFIG_REPLICATION_GROUP TABLE - "+groupId, ex);
					break;
				case 1 :
					System.out.println("updated successfuly !");
					break;
				default :
					CoronaException e = new CoronaException("Number of rows updated for group - "+groupId+" are more than 1");
					CoronaErrorHandler.logError(null, "Number of rows updates in CONFIG_REPLICATION_GROUP should never be more than 1", e);
			}
		}catch(Exception ex){
			// Do Error Handling
			ex.printStackTrace();
		}
	}	
	public static void updateLastModifiedTimestampforGroup(String groupId){
		try{
			
			DaxMgr dxMgr = DaxMgr.getInstance();
			DaxDB dxDb = dxMgr.getDaxDB();
			
			DaxDataBeanGeneral dgBean2 = new DaxDataBeanGeneral();

			dgBean2.setString2("FWK_PROCESSOR");
			dgBean2.setString1(groupId);
			DaxParsedStmt dxUpdTimeStmpPstmt = null;

			//here we wont update the last run date we will only update the lastmodified date.
				dxUpdTimeStmpPstmt = dxMgr.makeParsedStmt(
						FETCH_REPLICATION_DATA_STMT_GROUP_ID,
						UPDATE_REPLICATION_GROUP_LAST_MOD_TIMESTAMP, null, null, null);
			
			int numbRowsUpdated = dxDb.doUpdate(dxUpdTimeStmpPstmt, dgBean2);

			switch (numbRowsUpdated) {
				case 0 :
					CoronaException ex = new CoronaException("No group defined in job_info TABLE - "+groupId);
					CoronaErrorHandler.logError(null, "No group defined in job_info TABLE - "+groupId, ex);
					break;
				case 1 :
					System.out.println("updated successfuly !");
					break;
				default :
					CoronaException e = new CoronaException("Number of rows updated for group - "+groupId+" are more than 1");
					CoronaErrorHandler.logError(null, "Number of rows updates in CONFIG_REPLICATION_GROUP should never be more than 1", e);
			}
			
			
		}catch(Exception ex){
			// Do Error Handling
			ex.printStackTrace();
		}
	}

	public static void updateCprStatusToCompleteForNoMaterial (String lastModifiedBy, String status, long cprId) {
		try{
			DaxMgr dxMgr = DaxMgr.getInstance();
			DaxDB dxDb = dxMgr.getDaxDB();
			
			DaxDataBeanGeneral dgBean2 = new DaxDataBeanGeneral();

			if (CoronaFwkConstants.Success.equalsIgnoreCase(status)){
				dgBean2.setString1("COMPLETED");
			}else if (CoronaFwkConstants.Warning.equalsIgnoreCase(status)){
				//Special handling for the IDs in ERROR and rest to READY.
				dgBean2.setString1("ERROR");
			}else if (CoronaFwkConstants.Failure.equalsIgnoreCase(status)){
				dgBean2.setString1("ERROR");
			}else {
				//This should not be the case.
				dgBean2.setString1("COMPLETED");
			}

			dgBean2.setString2(lastModifiedBy);
			dgBean2.setLong1(cprId);
			
			DaxParsedStmt dxUpdTimeStmpPstmt = dxMgr.makeParsedStmt(
					FETCH_REPLICATION_DATA_STMT_GROUP_ID,
					UPDATE_CPR_STATUS_TO_COMPLETED_FOR_NO_REC, null, null, null);

			int numbRowsUpdated = dxDb.doUpdate(dxUpdTimeStmpPstmt, dgBean2);

			if (numbRowsUpdated == 0) {
					CoronaException ex = new CoronaException("No updates in CONFIG_PRICE_REPLICATION TABLE for - "+lastModifiedBy);
					CoronaErrorHandler.logError(null, "No updates in CONFIG_PRICE_REPLICATION TABLE for - "+lastModifiedBy, ex);
			}else {
					//Update Successful !.
					System.out.println("Updated back to status for "+lastModifiedBy+" as completed");
			}
		}catch(Exception ex){
			ex.printStackTrace();
		}
	}

	public static void updateCprStatusToCompleteForMaterial (String lastModifiedBy,String status, Date lastRunDate, long cprId) {
		try{
			DaxMgr dxMgr = DaxMgr.getInstance();
			DaxDB dxDb = dxMgr.getDaxDB();
			
			DaxDataBeanGeneral dgBean2 = new DaxDataBeanGeneral();
			if (CoronaFwkConstants.Success.equalsIgnoreCase(status)){
				dgBean2.setString1("COMPLETED");
			}else if (CoronaFwkConstants.Warning.equalsIgnoreCase(status)){
				//Special handling for the IDs in ERROR and rest to READY.
				dgBean2.setString1("ERROR");
			}else if (CoronaFwkConstants.Failure.equalsIgnoreCase(status)){
				dgBean2.setString1("ERROR");
			}else {
				//This should not be the case.
				dgBean2.setString1("COMPLETED");
			}
			
			dgBean2.setString2(lastModifiedBy);
			dgBean2.setDate1(lastRunDate);
			dgBean2.setLong1(cprId);
			
			DaxParsedStmt dxUpdTimeStmpPstmt = dxMgr.makeParsedStmt(
					FETCH_REPLICATION_DATA_STMT_GROUP_ID,
					UPDATE_CPR_STATUS_TO_COMPLETED_FOR_REC, null, null, null);

			int numbRowsUpdated = dxDb.doUpdate(dxUpdTimeStmpPstmt, dgBean2);

			if (numbRowsUpdated == 0) {
					CoronaException ex = new CoronaException("No updates in CONFIG_PRICE_REPLICATION TABLE for - "+lastModifiedBy);
					CoronaErrorHandler.logError(null, "No updates in CONFIG_PRICE_REPLICATION TABLE for - "+lastModifiedBy, ex);
			}else {
					//Update Successful !.
					System.out.println("Updated back to status for "+lastModifiedBy+" as completed");
			}
		}catch(Exception ex){
			ex.printStackTrace();
		}
	}
	
	//This is just for tracking purpose, updating timestamp for source and object in REPLICATION_INFO - Cross Check once.
	public static void updateReplicationTimestampInReplicationInfo(String source,
			String objectName, Date lastDateTimeStamp) throws ReplicationException {

		DaxMgr dxMgr = DaxMgr.getInstance();
		DaxDataBeanGeneral dgBean = new DaxDataBeanGeneral();
		dgBean.setDate1(lastDateTimeStamp);
		dgBean.setString1(source);
		dgBean.setString2(objectName);
		dgBean.setString3(CoronaFwkConstants.FWK_PROCESSOR);

		DaxParsedStmt dxPstmt = dxMgr.makeParsedStmt(
				FETCH_REPLICATION_DATA_STMT_GROUP_ID,
				UPDATE_REPLICATION_TIMESTAMP, null, null, null);

		DaxDB dxDb = dxMgr.getDaxDB();
		int numbRowsUpdated = dxDb.doUpdate(dxPstmt, dgBean);

		switch (numbRowsUpdated) {
			case 0 :
				System.out
						.println("Source and object combination not found in replication info table (source= "
								+ source + " , object = " + objectName + ")");
				break;
			case 1 :
				System.out.println("updated successfuly !");
			default :
				System.out
						.println("Number of rows updated should never be more than 1");
		}
	}
	
	
	public static void updateCprStatus (String lastModifiedBy, String status, List<Long> havingProblems, Date lastModifiedDateFromView, long cprId) {
		try{
			DaxMgr dxMgr = DaxMgr.getInstance();
			DaxDB dxDb = dxMgr.getDaxDB();
			
			DaxDataBeanGeneral dgBean2 = new DaxDataBeanGeneral();

			//Update all to READY again.
			if (CoronaFwkConstants.Success.equalsIgnoreCase(status)){
				dgBean2.setString1("COMPLETED");
			}else if (CoronaFwkConstants.Warning.equalsIgnoreCase(status)){
				//Special handling for the IDs in ERROR and rest to READY.
				dgBean2.setString1("ERROR");
			}else if (CoronaFwkConstants.Failure.equalsIgnoreCase(status)){
				dgBean2.setString1("ERROR");
			}else {
				//This should not be the case.
				dgBean2.setString1("COMPLETED");
			}
			dgBean2.setString2(lastModifiedBy);
			dgBean2.setDate1(lastModifiedDateFromView);
			dgBean2.setLong1(cprId);
			
			DaxParsedStmt dxUpdTimeStmpPstmt = dxMgr.makeParsedStmt(
					FETCH_REPLICATION_DATA_STMT_GROUP_ID,
					UPDATE_CPR_STATUS_TO_ERR_OR_READY, null, null, null);

			int numbRowsUpdated = dxDb.doUpdate(dxUpdTimeStmpPstmt, dgBean2);

			if (numbRowsUpdated == 0) {
					CoronaException ex = new CoronaException("No updates in CONFIG_PRICE_REPLICATION TABLE for - "+lastModifiedBy);
					CoronaErrorHandler.logError(null, "No updates in CONFIG_PRICE_REPLICATION TABLE for - "+lastModifiedBy, ex);
			}else {
					//Update Successful !.
					System.out.println("Updated back to status for "+lastModifiedBy+" as "+status);
			}
		}catch(Exception ex){
			ex.printStackTrace();
		}
	}
	
	public static List<PriceCacheDataBean>  getPriceMapFromEDA(int configId,String priceId,String shipToCountry, String priceDescriptor) throws ReplicationException {

		DaxMgr dxMgr = DaxMgr.getInstance();
		DaxDataBeanGeneral dgBean = new DaxDataBeanGeneral();
		dgBean.setInt1(configId);
		dgBean.setString1(priceId);
		dgBean.setString2(shipToCountry);
		dgBean.setString3(priceDescriptor);
		
		PriceCacheDataBean pInfo = new PriceCacheDataBean();
		pInfo.setConfigId(configId);
		pInfo.setPriceId(priceId);
		pInfo.setShipToCountry(shipToCountry);
		pInfo.setPriceDescriptor(priceDescriptor);
		List<PriceCacheDataBean> priceInfoList = null;
		DaxParsedStmt dxPstmt = dxMgr.makeParsedStmt(
				FETCH_REPLICATION_DATA_STMT_GROUP_ID,
				FETCH_IPCS_PRICE_DATA, pInfo, null, null);

		DaxDB dxDb = dxMgr.getDaxDB();
		priceInfoList= dxDb.doSelect(dxPstmt, PriceCacheDataBean.class, pInfo);

//		System.out.println("getPriceMapFromEDA : "+priceInfoList.get(0).getMaterialId()+  " : " +priceInfoList.get(0).getLastModifiedDate().getTime()+":"+priceInfoList.get(0).getLastModifiedDateTime());
		
		return priceInfoList;
	}
	
	public static List<PriceCacheDataBean> updatePriceCacheRecordsList(List<PriceCacheDataBean> updateList) throws ReplicationException {

		List<PriceCacheDataBean> failed = null;
		if(updateList == null || updateList.size() ==0){
			return failed;
		}
		DaxMgr dxMgr = DaxMgr.getInstance();
		DaxDataBeanGeneral dgBean = new DaxDataBeanGeneral();
		DaxParsedStmt dxPstmt = null;
		DaxDB dxDb = dxMgr.getDaxDB();
		boolean error = false;
		PriceCacheDataBean pInfo = new PriceCacheDataBean();
		try{
			dxPstmt = dxMgr.makeParsedStmt(
				FETCH_REPLICATION_DATA_STMT_GROUP_ID,
				UPDATE_IPCS_PRICE_INFO, pInfo, null, null);
			
			dxDb.startBatch();
				for (PriceCacheDataBean pi: updateList) {
					dxDb.doUpdate(dxPstmt, pi);
				}
			dxDb.finishBatch();
			dxDb.finish(false);

		}catch (Exception e){
			error = true;
			System.out.println("Error in update Price Cache into Price_cache table - try to update in non-batch mode");
			try{
				dxPstmt = dxMgr.makeParsedStmt(
					FETCH_REPLICATION_DATA_STMT_GROUP_ID,
					UPDATE_IPCS_PRICE_INFO, pInfo, null, null);
			}catch (Exception ee){
				failed = new ArrayList<PriceCacheDataBean>();
				failed.addAll(updateList);
				System.out.println("Failed in parsing the statement - none of the records is updated");
				return failed;
			}
			for (PriceCacheDataBean pi: updateList) {
				try{
					dxDb.doUpdate(dxPstmt, pi);
					dxDb.finish(false);
				}catch (Exception ex){
					if(failed ==null){
						failed = new ArrayList<PriceCacheDataBean>();
					}
					failed.add(pi);
				}
			}
		}
		
		return failed;
	}
	
	public static List<PriceCacheDataBean> insertPriceCacheRecordsList(List<PriceCacheDataBean> insertList) throws ReplicationException {

		LoggerInfo logInfo = new LoggerInfo ("ReplicationDao");
		
		List<PriceCacheDataBean> failed = null;
		if(insertList == null || insertList.size() ==0){
			return failed;
		}
		DaxMgr dxMgr = DaxMgr.getInstance();
		DaxDataBeanGeneral dgBean = new DaxDataBeanGeneral();
		DaxParsedStmt dxPstmt = null;
		DaxDB dxDb = dxMgr.getDaxDB();
		dxDb.setAutoCommit(true);
		boolean error = false;
		PriceCacheDataBean pInfo = new PriceCacheDataBean();
		//Logger.repInfo(logInfo, "insertPriceCacheRecordsList", "updating for material Material = "+insertList.get(0).getConfigId());
		try{
			//throw new Exception("test");
			dxPstmt = dxMgr.makeParsedStmt(
				FETCH_REPLICATION_DATA_STMT_GROUP_ID,
				INSERT_IPCS_PRICE_INFO, pInfo, null, null);
			dxDb.setBatchSize(150);
			dxDb.startBatch();
				for (PriceCacheDataBean pi: insertList) {
					//Logger.repInfo(logInfo, "insertPriceCacheRecordsList", "updating for material Material = "+pi.getMaterialId());					
					dxDb.doUpdate(dxPstmt, pi);
				}
			dxDb.finishBatch();
			dxDb.finish(false);

		}catch (Exception e){
			error = true;
			Logger.repInfo(logInfo, "insertPriceCacheRecordsList", "##############33 Error in Insert Price Cache into Price_cache table - try to insert in non-batch mode");
			
			try{
				dxPstmt = dxMgr.makeParsedStmt(
					FETCH_REPLICATION_DATA_STMT_GROUP_ID,
					INSERT_IPCS_PRICE_INFO, pInfo, null, null);
			}catch (Exception ee){
				failed = new ArrayList<PriceCacheDataBean>();
				failed.addAll(insertList);
				Logger.repInfo(logInfo, "insertPriceCacheRecordsList", "##################### Failed in parsing the statement - none of the records is updated");
				
				return failed;
			}
			for (PriceCacheDataBean pi: insertList) {
				try{
					dxDb.doUpdate(dxPstmt, pi);
					dxDb.finish(false);
				}catch (Exception ex){
					if(failed ==null){
						failed = new ArrayList<PriceCacheDataBean>();
					}
					failed.add(pi);
				}
			}
			e.printStackTrace();
			
		}
		
		return failed;
	}
	
	public static List<PriceCacheDataBean> deletePriceCacheRecordsList(List<PriceCacheDataBean> insertList) throws ReplicationException {

		List<PriceCacheDataBean> failed = null;
		if(insertList == null || insertList.size() ==0){
			return failed;
		}
		DaxMgr dxMgr = DaxMgr.getInstance();
		DaxParsedStmt dxPstmt = null;
		DaxDB dxDb = dxMgr.getDaxDB();
		boolean error = false;
		PriceCacheDataBean pInfo = new PriceCacheDataBean();
		try{
			dxPstmt = dxMgr.makeParsedStmt(
				FETCH_REPLICATION_DATA_STMT_GROUP_ID,
				DELETE_IPCS_PRICE_INFO, pInfo, null, null);
			
			dxDb.startBatch();
				for (PriceCacheDataBean pi: insertList) {
					dxDb.doUpdate(dxPstmt, pi);
				}
			dxDb.finishBatch();
			dxDb.finish(false);

		}catch (Exception e){
			error = true;
			System.out.println("Error in Delete Price Cache into Price_cache table - try to insert in non-batch mode");
			try{
				dxPstmt = dxMgr.makeParsedStmt(
					FETCH_REPLICATION_DATA_STMT_GROUP_ID,
					DELETE_IPCS_PRICE_INFO, pInfo, null, null);
			}catch (Exception ee){
				failed = new ArrayList<PriceCacheDataBean>();
				failed.addAll(insertList);
				System.out.println("Failed in parsing the statement - none of the records is deleted");
				return failed;
			}
			for (PriceCacheDataBean pi: insertList) {
				try{
					dxDb.doUpdate(dxPstmt, pi);
					dxDb.finish(false);
				}catch (Exception ex){
					if(failed ==null){
						failed = new ArrayList<PriceCacheDataBean>();
					}
					failed.add(pi);
				}
			}
			
		}
		
		return failed;
	}
	
	
	//todo : Update Config Price Replication Status (Picked, Ready and Error)
	public static List<ConfigPriceReplication> updateConfigPriceReplicationList(List<ConfigPriceReplication> updateList) throws ReplicationException {

		List<ConfigPriceReplication> failed = null;
		if(updateList == null && updateList.size() ==0){
			return failed;
		}
		return failed;
	}

	//todo : Update Config Price Group timestamp
	public static String updateConfigPriceGroup(ConfigPriceReplication cpr) throws ReplicationException {

		String failed = CoronaFwkConstants.Failure;
		if(cpr == null){
			return failed;
		}
		return failed;
	}
	
	// todo : get replication_info
	public static void cacheReplicationInfoMap () {
		try {
			DaxMgr dxMgr = DaxMgr.getInstance();
			ReplicationInfo dataBean = new ReplicationInfo();
			List<ReplicationInfo> dataList = null;
	
			DaxParsedStmt dxPstmt = dxMgr.makeParsedStmt(FETCH_REPLICATION_DATA_STMT_GROUP_ID,
					REPLICATION_INFO, dataBean, null, null);
			DaxDB dxDb = dxMgr.getDaxDB();
			dataList = dxDb.doSelect(dxPstmt, ReplicationInfo.class, dataBean);
			
			if (hmReplicationCache == null){
				hmReplicationCache = new HashMap<String, ReplicationInfo>();
			}
			
			//Setting cache Objects.
			for (ReplicationInfo cb : dataList){
				String key = cb.getSource()+"|"+cb.getObjectName();
				System.out.println("@ key to be put to map -> "+key +" and object is  -> "+ cb);
				hmReplicationCache.put(key, cb);
			}
			
			if (hmReplicationCache != null){
				Logger.repInfo(logInfo,"cacheReplicationInfoMap","@@@@@@@@@ Cached Replication info map "+hmReplicationCache.size());
			}
		}catch (Exception ex){
			CoronaErrorHandler.logError (ex, "Exception while caching replication info values map", null);
		}
	}

	public static Map<String, ReplicationInfo> getHmReplicationCache() {
		return hmReplicationCache;
	}

	public static void setHmReplicationCache(
			Map<String, ReplicationInfo> hmReplicationCache) {
		ReplicationDao.hmReplicationCache = hmReplicationCache;
	}

	public static ReplicationInfo fetchCachedReplicationInfoObj(String sourceObjectKey){

		Map<String, ReplicationInfo> hmRInfo = getHmReplicationCache();
		ReplicationInfo rInfo = null;

		//Trigger caching again
		if (hmRInfo == null)
			cacheReplicationInfoMap();

		if (hmRInfo != null){
			//Fetch Object key passed from Cached Replication Info Object
			rInfo=hmRInfo.get(sourceObjectKey);

		}else {
			ReplicationException ex = new ReplicationException("Replication Info Object for sourceObjectKey is coming as null "+ sourceObjectKey);
			CoronaErrorHandler.logError(CoronaErrorConstants.replicationInfoObjectNull,"Replication Info Object for sourceObjectKey is coming as null "+sourceObjectKey, ex);
		}
		return  rInfo;
	}

	public static List<PriceInfo> retrievePriceInfoCompoenentBean(long cprhId){

		DaxMgr dxMgr = DaxMgr.getInstance();
		DaxDataBeanGeneral dgBean = new DaxDataBeanGeneral();
		dgBean.setLong1(cprhId);
		
		List<PriceInfo> dataList = null;

		DaxParsedStmt dxPstmt = dxMgr.makeParsedStmt(FETCH_REPLICATION_DATA_STMT_GROUP_ID,
				FETCH_PRICE_IFNO_COMP_DATA, dgBean, null, null);
		DaxDB dxDb = dxMgr.getDaxDB();
		dataList = dxDb.doSelect(dxPstmt, PriceInfo.class, dgBean);

		return dataList;
	}

	public static List<ConfigPriceHeaderInfo> retrievePriceHeaderInfoBean(long cprhId){

		DaxMgr dxMgr = DaxMgr.getInstance();
		DaxDataBeanGeneral dgBean = new DaxDataBeanGeneral();
		dgBean.setLong1(cprhId);
		
		List<ConfigPriceHeaderInfo> dataList = null;

		DaxParsedStmt dxPstmt = dxMgr.makeParsedStmt(FETCH_REPLICATION_DATA_STMT_GROUP_ID,
				FETCH_CONFIG_PRICE_HEADER_DATA, dgBean, null, null);
		DaxDB dxDb = dxMgr.getDaxDB();
		dataList = dxDb.doSelect(dxPstmt, ConfigPriceHeaderInfo.class, dgBean);

		return dataList;
	}
	
	
	public static List<PriceCacheDataBean> retrieveReplicationInfo(long cprhId){

		DaxMgr dxMgr = DaxMgr.getInstance();
		DaxDataBeanGeneral dgBean = new DaxDataBeanGeneral();
		dgBean.setLong1(cprhId);
		
		List<PriceCacheDataBean> dataList = null;

		DaxParsedStmt dxPstmt = dxMgr.makeParsedStmt(FETCH_REPLICATION_DATA_STMT_GROUP_ID,
				FETCH_PRICE_HEADER_AND_COMP_DATA, dgBean, null, null);
		DaxDB dxDb = dxMgr.getDaxDB();
		dataList = dxDb.doSelect(dxPstmt, PriceCacheDataBean.class, dgBean);

		return dataList;
	}


}


