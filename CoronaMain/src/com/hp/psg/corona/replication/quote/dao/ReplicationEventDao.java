package com.hp.psg.corona.replication.quote.dao;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.hp.psg.common.util.logging.LoggerInfo;
import com.hp.psg.corona.common.util.Logger;
import com.hp.psg.corona.replication.common.cache.query.QueryRepository;
import com.hp.psg.corona.replication.common.cache.query.ReplicationQueryManager;
import com.hp.psg.corona.replication.common.exception.CoronaException;
import com.hp.psg.corona.replication.common.exception.ReplicationException;
import com.hp.psg.corona.replication.common.util.ReplicationConstants;
import com.hp.psg.corona.replication.common.vo.ReplicationEventVo;
import com.hp.psg.corona.replication.quote.QuoteConnection;

public class ReplicationEventDao extends QuoteConnection {
	
	private QueryRepository queryRepository = null;
	String repEventByStatusQuery = null;
	String updateRepEventsStatusQuery = null;
	String updateReEventWithErrorQuery = null;	
	LoggerInfo logInfo=null;
	
	public ReplicationEventDao(){
		queryRepository = ReplicationQueryManager.getInstance();
		repEventByStatusQuery = queryRepository.getQuery("Quote", "RepEvents", "GET");
		updateRepEventsStatusQuery = queryRepository.getQuery("Quote", "RepEvents", "UPDATE");
		updateReEventWithErrorQuery = queryRepository.getQuery("Quote", "RepEvents", "UPDATEWITHERROR");		
		logInfo = new LoggerInfo (this.getClass().getName());
	}
	

	/**
	 * 
	 * @param con
	 * @param status
	 * @param repEventId
	 * @param pickSize
	 * @return
	 * @throws ReplicationException
	 */
	public List<ReplicationEventVo> getRepEventsByStatus(Connection con,String status, String[] groupIds, int pickSize) throws ReplicationException{
		Logger.repDebug(logInfo, "getRepEventsByStatus", "Begining of the method to fetch rep events by status " + status );	
		
		List<ReplicationEventVo> listOfRepEvents = null;
		
		try{			
			StringBuilder query = new StringBuilder(repEventByStatusQuery);
			
			List<Object> params = new ArrayList<Object>();
			params.add(status);
			params.add(pickSize);
			
			//Build dynamic query based on groups
			StringBuilder grpIds = new StringBuilder();
			if( groupIds != null && groupIds.length > 0){							
				int len = groupIds.length;
				for(int i=0; i<len; i++){
					grpIds.append(groupIds[i]);
					if(i < len-1){
						grpIds.append(",");
					}
				}
				
				if( grpIds.length() > 0){
					query.append(" AND ");
					query.append(" GROUP_ID IN (");
					query.append(grpIds.toString());
					query.append(")");
				}
			}		
						
			Logger.repDebug(logInfo, "getRepEventsByStatus", "Executing query::: "+ query.toString());
			ResultSet repEvents = runPreparedStatement(con,query.toString(),params);
			
			if( repEvents != null){				
				listOfRepEvents = constructRepEventResultList(repEvents);
			}
		}		
		catch(CoronaException ex){
			throw new ReplicationException(ex.getErrorNumber(),ex.getMessage(),ex);
		}
		catch(SQLException ex){
			Logger.repError(logInfo, "getRepEventsByStatus", ex);
			
			throw new ReplicationException(ex.getMessage(),ex);
		}
		
		Logger.repDebug(logInfo, "getRepEventsByStatus", "End of the method to fetch rep events by status " + status);
		
		return listOfRepEvents;		
	}//getRepEventsByStatus
	
		
	public boolean updateRepEventStatus(Connection con, String status, long repEventId, String comments, int retryCount) throws ReplicationException{		
		Logger.repDebug(logInfo, "updateRepEventStatus", "Begining of the method to update rep events based on status " + status + " repEventId " + repEventId);
		boolean updated = false;
		
		try{						
			List<Object> params = new ArrayList<Object>();
			StringBuilder query = new StringBuilder();
			StringBuilder wQuery = new StringBuilder();
			
			//UPDATE status to COMPLETED or ERROR or SKIPPED only if the event processing status is PICKED
			if( status.equals(ReplicationConstants.COMPLETED_STATUS) || status.equals(ReplicationConstants.ERROR_STATUS) || status.equals(ReplicationConstants.SKIPPED_STATUS)){
				wQuery.append(" AND ");
				wQuery.append("PROCESSING_STATUS=");
				wQuery.append("'");
				wQuery.append(ReplicationConstants.PICKED_STATUS);
				wQuery.append("'");
			}
			
			if( status.equals(ReplicationConstants.ERROR_STATUS)){
				query.append(updateReEventWithErrorQuery);
				query.append(wQuery.toString());
				
				params.add(status);
				params.add(retryCount);
				params.add(comments);
				params.add(repEventId);	
			}
			else{
				query.append(updateRepEventsStatusQuery);
				query.append(wQuery.toString());
				
				params.add(status);
				params.add(repEventId);	
			}
			
			Logger.repDebug(logInfo, "updateRepEventStatus", "Execute Query::: "+ query.toString());
			
			int update = executeUpdatePreparedStatement(con,query.toString(),params);
			
			if( update > 0){				
				updated = true;
			}
			
			//nullifying
			params = null;
			query = null;
			wQuery = null;
			
		}
		catch(CoronaException ex){
			throw new ReplicationException(ex.getErrorNumber(),ex.getMessage(),ex);
		}
		
		Logger.repDebug(logInfo, "updateRepEventStatus", "End of the method to update rep events based on status " + status + " for repEventId " + repEventId);
				
		return updated;
	}//updateRepEventStatus
	
	
	private List<ReplicationEventVo> constructRepEventResultList(ResultSet repEvents) throws SQLException{
		List<ReplicationEventVo> listOfRepEvents = new ArrayList<ReplicationEventVo>();

		ReplicationEventVo repEventVo = null;
		long reEventId = -1;
		String processKey = null;
		String processType = null;
		String source = null;
		String eventSkipFlag = null;
		String processStatus = null;
		int retryCount = -1;
		int priority = -1;
		int groupId = -1;
		String comments = null;
		String createdBy = null;
		Date createdDate = null;
		String modifiedBy = null;
		Date modifiedDate = null;
		
		while(repEvents.next()){
			repEventVo = new ReplicationEventVo();
			
			reEventId = repEvents.getLong(1);
			processKey = repEvents.getString(2);
			processType = repEvents.getString(3);
			source = repEvents.getString(4);
			eventSkipFlag = repEvents.getString(5);
			processStatus = repEvents.getString(6);
			retryCount = repEvents.getInt(7);
			priority = repEvents.getInt(8);
			groupId = repEvents.getInt(9);
			comments = repEvents.getString(10);
			createdBy = repEvents.getString(11);
			createdDate = repEvents.getDate(12);
			modifiedBy = repEvents.getString(13);
			modifiedDate = repEvents.getDate(14);	
			
			repEventVo.setReEventId(reEventId);
			repEventVo.setProcessKey(processKey);
			repEventVo.setProcessType(processType);
			repEventVo.setSource(source);
			repEventVo.setEventSkipFlag(eventSkipFlag);
			repEventVo.setProcessStatus(processStatus);
			repEventVo.setRetryCount(retryCount);
			repEventVo.setPriority(priority);
			repEventVo.setGroupId(groupId);
			repEventVo.setComments(comments);
			repEventVo.setCreatedBy(createdBy);
			repEventVo.setCreatedDate(createdDate);
			repEventVo.setLastModifiedBy(modifiedBy);
			repEventVo.setLastModifiedDate(modifiedDate);
			
			listOfRepEvents.add(repEventVo);					
		}				
		
		Logger.repDebug(logInfo, "constructRepEventResultList", "The number of retrieved events from DB::: "+ listOfRepEvents.size());
	
		return listOfRepEvents;
		
	}//constructRepEventResultList
		
			
}//class
