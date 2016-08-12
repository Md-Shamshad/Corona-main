package com.hp.psg.corona.replication.quote.dao;

import java.sql.Connection;

import com.hp.psg.corona.replication.common.exception.ReplicationException;

/**
 * Interface for replication process
 * 
 * @author kn
 *
 */
public interface ReplicateTablesDao {

	public boolean replicateSourceToDestination(Connection con, String processType,String processKey) throws ReplicationException;
	
}//class
