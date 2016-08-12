package com.hp.psg.corona.replication.cto.handler;

import java.sql.Connection;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.hp.psg.common.error.ReplicationException;
import com.hp.psg.common.error.util.CoronaErrorConstants;
import com.hp.psg.common.util.logging.LoggerInfo;
import com.hp.psg.corona.common.beans.ReplicationInfo;
import com.hp.psg.corona.common.constants.CoronaFwkConstants;
import com.hp.psg.corona.common.util.Config;
import com.hp.psg.corona.common.util.CoronaFwkUtil;
import com.hp.psg.corona.common.util.Logger;
import com.hp.psg.corona.error.util.CoronaErrorHandler;
import com.hp.psg.corona.replication.beans.ConfigPriceReplication;
import com.hp.psg.corona.replication.beans.PriceCacheDataBean;
import com.hp.psg.corona.replication.common.events.ReplicationProcessor;
import com.hp.psg.corona.replication.common.exception.CacheException;
import com.hp.psg.corona.replication.common.util.ReplicationConstants;
import com.hp.psg.corona.replication.dao.ReplicationDao;
import com.hp.psg.corona.replication.plugin.interfaces.IReplicationPlugin;
import com.hp.psg.corona.replication.plugin.interfaces.ReplicationResult;
import com.hp.psg.corona.replication.quote.QuoteConnection;
import com.hp.psg.corona.replication.quote.events.QuoteReplicationProcessor;

/*
 * @author dudeja
 * @version 1.0
 *
 */
public class ReplicationEventProcessor {
	LoggerInfo logInfo=null;
	static IReplicationPlugin replicationPlugin = null;
	private ReplicationProcessor repProcessor = null;
	private String threadName = null;
	private String priceCacheReplicationEnabled = null;

	public ReplicationEventProcessor() {
		// TODO Auto-generated constructor stub
		logInfo = new LoggerInfo (this.getClass().getName());

		repProcessor = new QuoteReplicationProcessor();		
				
		priceCacheReplicationEnabled = Config.getProperty("corona.replication.pricecache.enabled");	
	}
	public void processReplication(String source, String objectName, String[] groups) {
		ReplicationResult repResult = null;

		try {
			if ("IPCS".equalsIgnoreCase(source) && "PRICE_CACHE".equalsIgnoreCase(objectName)){
				//If it is enabled then only initiates the Price Cache replication
				if( priceCacheReplicationEnabled != null && priceCacheReplicationEnabled.equals("true")){				
				if (groups != null && groups.length >= 1){
					Logger.repDebug(logInfo, "processReplication","processing replication fro IPCS for PRICE_CACHE, this poller will work on groups  "+groups);

					int maxPermutationPerTransacation = 1;  //Defaulted to one.
					String objKey = "IPCS|PRICE_CACHE"; // this method is used only for PRICE_CACHE replication from IPCS.

					ReplicationInfo rInfoObj = ReplicationDao.fetchCachedReplicationInfoObj(objKey);
					if (rInfoObj != null){
						Logger.repDebug(logInfo, "processReplication", "Setting the max transaction size to pass to the process for replication "+rInfoObj.getPermPickSize());
						maxPermutationPerTransacation = rInfoObj.getPermPickSize();
					}

					//Return expected the Array { groupId, lastModifiedBy} and CPR list.
					Map<String [],List<ConfigPriceReplication>> mCprList = retrievePriceCacheHeaderData(groups);
					Logger.repDebug(logInfo, "processReplication", "Retrieved the Map size for PriceCacheHeader data from ConfigPriceReplication (CPR) "+mCprList.size());

					long startTime=System.currentTimeMillis();

					//Loop thru all groups and retrieve the components for headers.
					if (mCprList != null && mCprList.size() > 0){
						//Got some header data to be replicated , retrieving the component list.
						Set<String[]> groupIds = mCprList.keySet();

						for (String[] group : groupIds){

							System.out.println("Checking for group, this one should be the array of last modifiedby and group id "+group[0] + " and "+group[1]);

							List<PriceCacheDataBean> priceCacheBeanRawList = Collections.EMPTY_LIST;

							//create the object of corresponding component here and pass in for replication..
							//Verify this part , it should work .. can i get the value on an array Key ?
							List<ConfigPriceReplication> configPriceRepList = mCprList.get(group);

							Logger.repDebug(logInfo, "processReplication", "Iteration for group :  "+group +"with ConfigPriceReplication list size "+configPriceRepList.size() );

							if (configPriceRepList != null && configPriceRepList.size() > 0 ) {

								for (ConfigPriceReplication cpr : configPriceRepList){
									Date lastPickedObjectDate = null;
									priceCacheBeanRawList = ReplicationDao.retrieveReplicationInfo(cpr.getCphId());
									
									Logger.repDebug(logInfo, "processReplication", "Iteration for CPHI_ID :  "+cpr.getCphId() +"with PriceCacheBean list size "+priceCacheBeanRawList.size() );									
									//Also need to list the map relations.. check the syntax once. ToDo
									try {
										List<String> classList = getReplicationClasses(objKey);
										String str = ("Replication classes for objectKey "+objKey +"list is :"+ ((classList != null)?classList.toString(): " ##### classList is coming as null"));

										Logger.repDebug(logInfo, "processReplication", str);

										for (String cls : classList) {
											try {
												Class clazz = Class.forName(cls);

												//Passing list to the interface.
												IReplicationPlugin replicationPlugin = (IReplicationPlugin) clazz.newInstance();
												if (priceCacheBeanRawList != null && priceCacheBeanRawList.size() > 0) {

													//Need to confirm
													SimpleDateFormat outputFormat = new SimpleDateFormat ("MM/dd/yyyy HH:mm:ss");
													String lastModifiedDateStr=priceCacheBeanRawList.get(priceCacheBeanRawList.size()-1).getLastModifiedDateStr();
													try{
														if (lastModifiedDateStr != null && !"".equals(lastModifiedDateStr))
															lastPickedObjectDate= outputFormat.parse(lastModifiedDateStr);
														else 
															lastPickedObjectDate = priceCacheBeanRawList.get(priceCacheBeanRawList.size()-1).getLastModifiedDate();
														
														Logger.repDebug(logInfo, "processReplication", "Will be setting this date "+lastPickedObjectDate);
													}catch (Exception ex){
														ex.printStackTrace();
													}
													Logger.repDebug(logInfo, "processReplication", "Calling replicate method here ");
													repResult = replicationPlugin.replicate(priceCacheBeanRawList); 
													
													if (repResult == null){
														ReplicationDao.updateCprStatusToCompleteForNoMaterial(group[1],CoronaFwkConstants.Success,cpr.getCphId());
													}else {
														if (CoronaFwkConstants.RepSuccess.equals(repResult.getStatus()))
															ReplicationDao.updateCprStatusToCompleteForMaterial(group[1],CoronaFwkConstants.Success,lastPickedObjectDate, cpr.getCphId() );
														else if (CoronaFwkConstants.RepWarning.equals(repResult.getStatus())||CoronaFwkConstants.RepFailure.equals(repResult.getStatus()))
															ReplicationDao.updateCprStatusToCompleteForMaterial(group[1],CoronaFwkConstants.Failure,lastPickedObjectDate, cpr.getCphId() );
														else
															ReplicationDao.updateCprStatusToCompleteForMaterial(group[1],CoronaFwkConstants.Success,lastPickedObjectDate, cpr.getCphId());
													}

												}else {
													ReplicationDao.updateCprStatusToCompleteForNoMaterial(group[1],CoronaFwkConstants.Success, cpr.getCphId());
												}
											} catch (Exception e) {
												CoronaErrorHandler.logError(this.getClass(),
														CoronaErrorConstants.retriveReplicationDataEvents,
														"retreiveReplicationData", e.getMessage(), false,
														e, true);
												if (repResult == null){
													ReplicationDao.updateCprStatusToCompleteForNoMaterial(group[1],CoronaFwkConstants.Failure, cpr.getCphId());
												}else {
													ReplicationDao.updateCprStatusToCompleteForMaterial(group[1],CoronaFwkConstants.Failure,lastPickedObjectDate, cpr.getCphId() );
												}
												e.printStackTrace();
											}
										}
									}catch (Exception ex){
										CoronaErrorHandler.logError (ex,"Exception while processing price cache events for IPCS ",null);
										ex.printStackTrace();
									}
								}
							}else {
								Logger.repDebug(logInfo, "processReplication", "configPriceRepList is null");
							}
							Logger.repPerfInfo(logInfo, "replicationProcess" , "(TimeStamp - "+new java.util.Date()+" ) - Group took - "+group[0]+" - took processing(timeInSeconds) - "+(System.currentTimeMillis()-startTime)/1000);
							
							ReplicationDao.updateLastReplicationTimestampforGroup(group[0], new java.util.Date());
						}
					}
				}else {
					Logger.repDebug(logInfo, "processReplication", "groups passed is set as null or size array does not have any groups defined"+groups);
				}
				}
			}
			else if ( ReplicationConstants.SOURCE_OBJECT.equalsIgnoreCase(source) && ReplicationConstants.QUOTE_THREAD_PROCESS.equalsIgnoreCase(objectName) ){
				
				String idsReplicationEnabled = Config.getProperty("replication.ids.enabled");
				
				//If it is enabled then only initiates the IDS replication
				if( idsReplicationEnabled != null && idsReplicationEnabled.equals("true")){
				
					Logger.repDebug(logInfo, "processReplication",  source + " | " + objectName + " starting the process...");
					try{
						QuoteConnection quoteCon = new QuoteConnection();
						String dataSourceName = Config.getProperty("corona.replication.primary.data.source.name");
						Connection con = quoteCon.getConnection(dataSourceName);
						try {				
							repProcessor.processReplication(con, getThreadName(), groups);
						} catch (com.hp.psg.corona.replication.common.exception.ReplicationException e) {
							CoronaErrorHandler.logError(null, "Error while replication of " +source + " | " + objectName +" object on "+ getThreadName() , e);
						}
						finally{
							quoteCon.closeConnection(con);
						}
					}
					catch(CacheException ex){
						CoronaErrorHandler.logError(null, "Unable to connect Primary Database " , ex);
					}
					Logger.repDebug(logInfo, "processReplication", "End of "+ objectName + " process.");
				}
			}
			else {
				Logger.repDebug(logInfo, "processReplication", "Key Object passed is not yet supported for replication");
				Logger.repDebug(logInfo, "processReplication", "\t Source Name  -- > "+source);
				Logger.repDebug(logInfo, "processReplication", "\t Object Name  -- > "+objectName);
			}

		}catch (ReplicationException ex){
			CoronaErrorHandler.logError(null, "Error while replication of object" , ex);
		}
	}


	//group Array contains the groupId as well as the lastModified String
	public void handlePriceCacheReplicationResult(ReplicationResult repResult, String groupAndModifiedByArr[], Date lastModifiedDate, long cprId){
		//Timestamp in CPG and status in CPR need to be updated accordingly.

		String groupId=groupAndModifiedByArr[0];
		String lastModifiedBy = groupAndModifiedByArr[1];

		if (repResult != null) {
			Logger.repDebug(logInfo, "handlePriceCacheReplicationResult", "Result is with status as "+repResult.getStatus()+" and last modified by key - "+lastModifiedBy);

			try {
				//Success
				if (CoronaFwkConstants.RepSuccess.equals(repResult.getStatus())){
					ReplicationDao.updateCprStatus(lastModifiedBy,CoronaFwkConstants.Success, null, lastModifiedDate, cprId);
				}else if (CoronaFwkConstants.RepWarning.equals(repResult.getStatus())){
					//Take the list of objects which had some issue, 
					List<Long> headersHavingProblem= repResult.getErrorList();
					if (headersHavingProblem == null || headersHavingProblem.size()==0){
						Throwable t = new Exception("Replication result returned status "+repResult.getStatus()+" which was not expected");
						Logger.repError(logInfo, "processReplication", t);
					}else {
						ReplicationDao.updateCprStatus(lastModifiedBy,CoronaFwkConstants.Warning,headersHavingProblem,lastModifiedDate, cprId );															
					}
				}else if (CoronaFwkConstants.RepFailure.equals(repResult.getStatus())){
					//Just would need to update the status , no need to update the replication timestamp.
					ReplicationDao.updateCprStatus(lastModifiedBy,CoronaFwkConstants.Failure, null,lastModifiedDate, cprId);
				}else {
					Throwable t = new Exception("Replication result returned status "+repResult.getStatus()+" which was not expected");
					Logger.repError(logInfo, "processReplication", t);
				}

				if (lastModifiedDate != null)
					ReplicationDao.updateLastReplicationTimestampforGroup(groupId,lastModifiedDate);
				else 
					ReplicationDao.updateLastModifiedTimestampforGroup(groupId);
				
			} catch (Exception e) {
				CoronaErrorHandler.logError(this.getClass(),
						CoronaErrorConstants.retriveReplicationDataEvents,
						"processReplication", e.getMessage(), false, e, true);							
			}
		} else {
			Logger.repError(logInfo,"processReplication", new Exception("Result from replication call is null"));

		}		
	}

	public Map<String[],List<ConfigPriceReplication>> retrievePriceCacheHeaderData(String[] groupIds)
	throws ReplicationException {

		return(ReplicationDao.retreiveConfigHeaderEventsToReplicate(groupIds));
	}

	public List<String> getReplicationClasses(String objectKey) {
		return CoronaFwkUtil.getListOfClasses(objectKey, "Replication");

	}


	//To do , use reflection instead of hardcoding elements here.
	public Map<String, List<PriceCacheDataBean>> splitListOnPermutation (String bindingKey, List<PriceCacheDataBean> list){
		Map <String, List<PriceCacheDataBean>> mapHolder = new HashMap<String, List<PriceCacheDataBean>>();
		String key;
		List<PriceCacheDataBean> tempList=null;

		if (list != null && list.size()>0){
			for (PriceCacheDataBean pr : list){
				key = pr.getConfigId()+"|"+pr.getPriceId()+"|"+pr.getShipToCountry()+"|"+pr.getPriceDescriptor();

				if (mapHolder.containsKey(key)){
					tempList = mapHolder.get(key);
					tempList.add(pr);
				}else{
					tempList= new ArrayList<PriceCacheDataBean>();
					tempList.add(pr);
				}

				mapHolder.put(key,tempList);
			}
		}

		Logger.repDebug(logInfo, "splitListOnPermutations", "##### Return map contains elements count = "+mapHolder.size());

		return mapHolder;

	}

	public String getThreadName() {
		return threadName;
	}

	public void setThreadName(String threadName) {
		this.threadName = threadName;
	}


	/**
	 * @param args
	 */
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		String[] groups = {"1","2","3","4","5"};
		ReplicationEventProcessor processor = new ReplicationEventProcessor();
		//processor.processReplication("IPCS", "PRICE_CACHE",groups);
		processor.processReplication("CORONA", "QUOTE",groups);
	}
}
