package com.hp.psg.corona.replication.quote.dao;

import java.sql.Connection;
import java.util.List;

import com.hp.psg.corona.replication.common.vo.ReplicationEventVo;
import com.hp.psg.corona.replication.common.cache.replicationmapping.value.ReplicationMappingVo;
import com.hp.psg.corona.replication.common.exception.ReplicationException;


/**
 * This class is a DAO Layer interface related to Quote Replication.
 * 
 * @author
 *
 */
public interface QuoteReplicationDao {
	
	public List<ReplicationEventVo> getRepEventsByStatus(Connection con,String status, String[] groupIds, int pickSize) throws ReplicationException;
	public List<ReplicationMappingVo> getRepMappings(Connection con) throws ReplicationException;
	public boolean updateRepEventStatus(Connection con, String status, long repEventId, String comments, int retryCount) throws ReplicationException;
	public boolean replicateSourceToDestination(Connection con, String processType,String processKey) throws ReplicationException;	
	
}// end of class
