package com.hp.psg.corona.replication.quote.events;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;

import com.hp.psg.common.util.logging.LoggerInfo;
import com.hp.psg.corona.common.util.Config;
import com.hp.psg.corona.replication.common.events.ReplicationProcessor;
import com.hp.psg.corona.replication.common.exception.ReplicationException;
import com.hp.psg.corona.common.util.Logger;
import com.hp.psg.corona.replication.common.util.ReplicationConstants;
import com.hp.psg.corona.replication.common.vo.ReplicationEventVo;
import com.hp.psg.corona.replication.common.cache.replicationmapping.value.ReplicationMappingVo;
import com.hp.psg.corona.replication.plugin.interfaces.ReplicationResult;
import com.hp.psg.corona.replication.quote.QuoteConnection;
import com.hp.psg.corona.replication.quote.dao.QuoteReplicationDao;
import com.hp.psg.corona.replication.quote.dao.QuoteReplicationDaoImpl;


/**
 * @author kn
 *
 */
public class QuoteReplicationProcessor implements ReplicationProcessor {		
	private QuoteReplicationDao quoteReplicationDao = null;
	LoggerInfo logInfo=null;
	private int pickSize = -1;
	private int retryCount = -1;
	
	public QuoteReplicationProcessor(){
		quoteReplicationDao = new QuoteReplicationDaoImpl();	
		logInfo = new LoggerInfo (this.getClass().getName());
		pickSize = Integer.parseInt(Config.getProperty("corona.replication.events.picksize"));
		retryCount = Integer.parseInt(Config.getProperty("replication.ids.retrycount"));
	}
	
	
	/**
	 * For a given groups, it fetch replication events based on READY and ERROR status for all non skipped events.
	 * 
	 */
	@Override
	public List<ReplicationEventVo> getReplicationEvents(Connection con, String[] groupIds) throws ReplicationException {
		List<ReplicationEventVo> listOfRepEvents = null;
		
		//GET READY state events
		listOfRepEvents = quoteReplicationDao.getRepEventsByStatus(con, ReplicationConstants.READY_STATUS, groupIds, pickSize);
				
		//GET ERROR state events
		List<ReplicationEventVo> errorStateEvents = quoteReplicationDao.getRepEventsByStatus(con, ReplicationConstants.ERROR_STATUS, groupIds, pickSize);		
		if( errorStateEvents!=null && errorStateEvents.size() > 0){
			if( listOfRepEvents == null){
				listOfRepEvents = new ArrayList<ReplicationEventVo>();
			}
			listOfRepEvents.addAll(errorStateEvents);
		}
		
		return listOfRepEvents;
	}//getReplicationEvents
			
	
	/**
	 * It retrieves replication mapping info
	 */
	public List<ReplicationMappingVo> getRepMapping(Connection con) throws ReplicationException{
		List<ReplicationMappingVo> listOfMapping = null;
		
		listOfMapping =  quoteReplicationDao.getRepMappings(con);
		
		return listOfMapping;
	}//getRepMappinglistOfMapping
	
	
	/**
	 * Main method for replication. 
	 * It picks events based on READY and ERROR status and change status to PICKED
	 * It replication source to destination table
	 * On successful it update status to COMPLETED only if it is PICKED
	 * On failure it update status to ERROR
	 * If there is no events to process it returns with message
	 *  
	 */
	@Override
	public ReplicationResult processReplication(Connection con, String threadName, String[] groupIds) throws ReplicationException {
		Logger.repInfo(logInfo, "processReplication", "Begin of Processing replication events...");
		long startTime=System.currentTimeMillis();
		
		ReplicationResult repResult = new ReplicationResult();
		List<Long> errorList = new ArrayList<Long>();
		long numOfEventsProcessed = 0;
		boolean isErrorEvents = false;
				
		try{							
			//GET the replication events based on READY and ERROR status for given groups
			Logger.repInfo(logInfo, "processReplication", "Fetching all replication events with READY and ERROR status...");
			List<ReplicationEventVo> listOfRepEvents = getReplicationEvents(con, groupIds);
			
			if( listOfRepEvents == null){
				ReplicationResult response = new ReplicationResult();
				response.setStatus(ReplicationConstants.SUCCESS);
				response.setMessage("There is nothing to replicate for groupIds "+groupIds);
				response.setErrorList(null);
				response.setNumberOfRecordsProcessed(0L);
				
				Logger.repInfo(logInfo, "processReplication", "End of Processing replication events and it took::: " + (System.currentTimeMillis()-startTime) +" msecs.");
				
				return response;
			}
			
					
			int numOfRepEvents = listOfRepEvents.size();			
			if( numOfRepEvents > 0){
				boolean updated = false;
				long repEventId = -1;
				String comments = null;
				
				Logger.repInfo(logInfo, "processReplication", "There are "+ numOfRepEvents + " READY replication events to process...");
				for(ReplicationEventVo repEvent : listOfRepEvents){	
					
					//lastRepEventId
					repEventId = repEvent.getReEventId();
				
					//CHANGE status to PICKED
					updated = quoteReplicationDao.updateRepEventStatus(con, ReplicationConstants.PICKED_STATUS, repEventId, null, -1);
				
					try{
						//Copy source table data to destination table data
						boolean replicated = quoteReplicationDao.replicateSourceToDestination(con, repEvent.getProcessType(),repEvent.getProcessKey());
					}
					catch(ReplicationException e){
						isErrorEvents = true;
						int rCount = repEvent.getRetryCount();
						 if(  rCount < retryCount ){
							 //Change status to ERROR	
							 int msgCount = e.getErrorMessage().length();
							 String errMsg = e.getErrorMessage();
							 if(msgCount > 200){
								 errMsg = errMsg.substring(0, 200);
							 }
							 updated = quoteReplicationDao.updateRepEventStatus(con, ReplicationConstants.ERROR_STATUS, repEventId, errMsg, (rCount+1));
						 }
						 else{
							 updated = quoteReplicationDao.updateRepEventStatus(con, ReplicationConstants.SKIPPED_STATUS, repEventId, null, -1);
						 }
						
						errorList.add(repEventId);

						//Continue from the next event as it is ERROR event, so no need to make it as COMPLETED 
						continue;
					}
															
					//Change status to COMPLETED							
					updated = quoteReplicationDao.updateRepEventStatus(con, ReplicationConstants.COMPLETED_STATUS, repEventId, null, -1);
					
					numOfEventsProcessed++;
				}//for			
				
			}//numOfRepEvents
			else{
				Logger.repInfo(logInfo, "processReplication", "There are "+ numOfRepEvents + " READY/ERROR replication events to process.");				
			}
			
			repResult = constructResponse(numOfRepEvents,errorList,numOfEventsProcessed,isErrorEvents);
			Logger.repInfo(logInfo, "constructResponse", "The replication results are: "+repResult + " for " + groupIds);			
			
		}
		catch(ReplicationException ex){
			throw ex;
		}	
		
		long endTime=System.currentTimeMillis();
		
		Logger.repInfo(logInfo, "processReplication", "End of Processing replication events and it has taken::: " + (endTime-startTime) +" msecs.");
		
		return repResult;
		
	}//processReplication	
	
	
	/**
	 * 
	 * @param numOfRepEvents
	 * @param eList
	 * @param numOfProcessed
	 * @param isErrorEvents
	 * @return
	 */
	private ReplicationResult constructResponse(int numOfRepEvents, List<Long> eList, long numOfProcessed, boolean isErrorEvents){
		ReplicationResult response = new ReplicationResult();
		
		int errorCount = eList.size();
		
		if( isErrorEvents && errorCount == numOfRepEvents){
			response.setStatus(ReplicationConstants.FAILURE);
			response.setMessage("All events are in ERROR state.");
		}
		else if( isErrorEvents && errorCount < numOfRepEvents){
			response.setStatus(ReplicationConstants.WARNING);
			response.setMessage("Successfully replicated some of the events and other are in ERROR state.");
		}
		else{
			response.setStatus(ReplicationConstants.SUCCESS);
			response.setMessage("Successfully replicated all the events.");
		}
		response.setNumberOfRecordsProcessed(numOfProcessed);
		response.setErrorList(eList);
				
		return response;		
	}//constructResponse
	
	public boolean defineGroupsForRepEvents(){
		boolean added = false;
		
		return added;
	}
	
		
	public static void main(String[] args){
		
		try{
			ReplicationProcessor repProcessor = new QuoteReplicationProcessor();
			QuoteConnection qCon = new QuoteConnection();
			String dataSourceName = Config.getProperty("corona.replication.primary.data.source.name");
			Connection con = qCon.getConnection(dataSourceName);
			String[] groupIds = {"8","9","10","13","14","15","16"};
			repProcessor.processReplication(con,"Thread1",groupIds);
		}
		catch(Exception ex){
			ex.printStackTrace();
		}
		
	}
	
}//class
