package com.hp.psg.corona.replication.quote.dao;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;

import com.hp.psg.common.util.logging.LoggerInfo;
import com.hp.psg.corona.replication.common.vo.ReplicationEventVo;
import com.hp.psg.corona.replication.common.cache.replicationmapping.value.ReplicationMappingVo;
import com.hp.psg.corona.replication.common.exception.ReplicationException;


/**
 * This is implementation class for DAO Layer interface. It contains all the
 * CRUD operation methods related to Quote Replication and get from Primary DB and 
 * finally replicate data into Replication DB.
 * 
 * @author
 * 
 */
public class QuoteReplicationDaoImpl implements QuoteReplicationDao{	
	private ReplicationEventDao repEventDao = null;
	private ReplicationMappingDao repMappingDao = null;
	private ReplicateTablesDao replicateDao = null;	
	LoggerInfo logInfo = null;
	
	public QuoteReplicationDaoImpl(){
		repEventDao = new ReplicationEventDao();
		repMappingDao = new ReplicationMappingDao();
		replicateDao = new ReplicateTablesDaoImpl();		
	}
		
	public List<ReplicationEventVo> getRepEventsByStatus(Connection con,String status, String[] groupIds, int pickSize) throws ReplicationException{
		List<ReplicationEventVo> listOfRepEvents = new ArrayList<ReplicationEventVo>();
		
		listOfRepEvents = repEventDao.getRepEventsByStatus(con, status, groupIds, pickSize);
		
		return listOfRepEvents;
	}//getReplicationEventsByStatus
	
	
		
	public List<ReplicationMappingVo> getRepMappings(Connection con) throws ReplicationException{
		List<ReplicationMappingVo> listOfRepMapping = null;
		
		listOfRepMapping = repMappingDao.getRepMappings(con);
		
		return listOfRepMapping;		
	}//getRepMappings
	
	
	public boolean updateRepEventStatus(Connection con, String status, long repEventId, String comments, int retryCount) throws ReplicationException{
		boolean updated = false;
		
		updated = repEventDao.updateRepEventStatus(con, status, repEventId, comments, retryCount);
		
		return updated;
	}//updateRepEventStatus
	
		
	public boolean replicateSourceToDestination(Connection con, String processType,String processKey) throws ReplicationException{
		boolean replicated = false;
		
		replicated = replicateDao.replicateSourceToDestination(con, processType, processKey);
		
		return replicated;
	}//replicateSourceToDestination			
	
}//end of class
