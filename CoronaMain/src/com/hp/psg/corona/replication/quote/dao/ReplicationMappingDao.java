package com.hp.psg.corona.replication.quote.dao;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.hp.psg.corona.replication.common.cache.query.QueryRepository;
import com.hp.psg.corona.replication.common.cache.query.ReplicationQueryManager;
import com.hp.psg.corona.replication.common.cache.replicationmapping.value.ReplicationMappingVo;
import com.hp.psg.corona.replication.common.exception.CoronaException;
import com.hp.psg.corona.replication.common.exception.ReplicationException;
import com.hp.psg.corona.replication.quote.QuoteConnection;

public class ReplicationMappingDao extends QuoteConnection {
	private QueryRepository queryRepository = null;
	String getRepMappingQuery = null;
	
	public ReplicationMappingDao(){
		queryRepository = ReplicationQueryManager.getInstance();
		getRepMappingQuery = queryRepository.getQuery("Quote", "RepMappings", "GET");		
	}
	
	public List<ReplicationMappingVo> getRepMappings(Connection con) throws ReplicationException{
		List<ReplicationMappingVo> listOfMappings = new ArrayList<ReplicationMappingVo>();
		
		try{
			ResultSet repMappings = runPreparedStatement(con,getRepMappingQuery);
			
			if( repMappings != null){
				ReplicationMappingVo repMappingVo = null;
				String processType = null;
				String sourceTable = null;
				String destinationTable = null;
				String processingOrder = null;
				String comments = null;
				Date lastRunDate = null;
				String createdBy = null;
				Date createdDate = null;
				String modifiedBy = null;
				Date modifiedDate = null;
				
				while(repMappings.next()){
					repMappingVo = new ReplicationMappingVo();
					
					processType = repMappings.getString(1);
					sourceTable = repMappings.getString(2);
					destinationTable = repMappings.getString(3);
					processingOrder = repMappings.getString(4);
					comments = repMappings.getString(5);
					lastRunDate = repMappings.getDate(6);
					createdBy = repMappings.getString(7);
					createdDate = repMappings.getDate(8);
					modifiedBy = repMappings.getString(9);
					modifiedDate = repMappings.getDate(10);
					
					repMappingVo.setProcessType(processType);
					repMappingVo.setSourceTable(sourceTable);
					repMappingVo.setDestinationTable(destinationTable);
					repMappingVo.setProcessingOrder(processingOrder);
					repMappingVo.setComments(comments);
					repMappingVo.setLastRunDate(lastRunDate);
					repMappingVo.setCreatedBy(createdBy);
					repMappingVo.setCreatedDate(createdDate);
					repMappingVo.setLastModifiedBy(modifiedBy);
					repMappingVo.setLastModifiedDate(modifiedDate);
					
					listOfMappings.add(repMappingVo);					
				}
			}
			
		}
		catch(CoronaException ex){
			throw new ReplicationException(ex.getErrorNumber(),ex.getMessage(),ex);
		}
		catch(SQLException sqlEx){
			throw new ReplicationException(sqlEx.getMessage(),sqlEx);
		}
		
		return listOfMappings;		
	}//getRepMappings
		
}//class
