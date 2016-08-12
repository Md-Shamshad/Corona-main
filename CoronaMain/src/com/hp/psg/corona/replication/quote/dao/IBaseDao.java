package com.hp.psg.corona.replication.quote.dao;

import java.sql.Connection;

import com.hp.psg.corona.replication.common.exception.ReplicationException;

public interface IBaseDao {
	public void create(Connection con, long slsQtnID, String slsQtnVrsn)throws ReplicationException;
	public void update(Connection con, long slsQtnID, String slsQtnVrsn)throws ReplicationException;
	public void delete(Connection con, long slsQtnID, String slsQtnVrsn)throws ReplicationException;
	public void change(Connection con, long slsQtnID, String slsQtnVrsn)throws ReplicationException;
	
}
