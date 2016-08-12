package com.hp.psg.corona.replication.common.events;

import java.sql.Connection;
import java.util.List;

import com.hp.psg.corona.replication.common.cache.replicationmapping.value.ReplicationMappingVo;
import com.hp.psg.corona.replication.common.exception.ReplicationException;
import com.hp.psg.corona.replication.common.vo.ReplicationEventVo;
import com.hp.psg.corona.replication.plugin.interfaces.ReplicationResult;

/**
 * Interface to replication process
 * 
 * @author kn
 *
 */
public interface ReplicationProcessor {
	public List<ReplicationEventVo> getReplicationEvents(Connection con, String[] groupIds) throws ReplicationException;
	public List<ReplicationMappingVo> getRepMapping(Connection con) throws ReplicationException;
	public ReplicationResult processReplication(Connection con, String threadName, String[] groupIds) throws ReplicationException;	

}//ReplicationProcessor
