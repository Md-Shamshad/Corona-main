package com.hp.psg.corona.replication.common.cache.replicationmapping;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import org.apache.commons.lang.StringUtils;

import com.hp.psg.corona.replication.common.cache.CacheTable;
import com.hp.psg.corona.replication.common.cache.replicationmapping.dao.ReplicationMappingDao;
import com.hp.psg.corona.replication.common.cache.replicationmapping.value.ReplicationMappingVo;
import com.hp.psg.corona.replication.common.exception.CacheException;
import com.hp.psg.corona.replication.common.exception.ReplicationException;
import com.hp.psg.corona.replication.common.message.MessageConstants;
import com.hp.psg.corona.replication.common.util.Logger;

@SuppressWarnings({ "rawtypes", "unchecked" })
public class ReplicationMappingTable extends CacheTable {

	
    private static final long serialVersionUID = 1L;
	private static ReplicationMappingTable instance = null;
	
	private ReplicationMappingTable() throws CacheException{
		Logger.info(getClass(),"*** Start Loading ReplicationMapping in to Cache ***");
		createCacheTable();
	    Logger.info(getClass(),"*** Finish Loading ReplicationMapping in to Cache ***");		
	}
	
	private ReplicationMappingTable(Connection con) throws CacheException{
		Logger.info(getClass(),"*** Start Loading ReplicationMapping in to Cache ***");
		createCacheTable(con);
	    Logger.info(getClass(),"*** Finish Loading ReplicationMapping in to Cache ***");		
	}
	
	public static ReplicationMappingTable getInstance() throws CacheException{
		if( instance == null){
			instance = new ReplicationMappingTable();
		}
		
		return instance;
	}//getInstance	
	
	public static ReplicationMappingTable getInstance(Connection con) throws CacheException{
		if( instance == null){
			instance = new ReplicationMappingTable(con);
		}
		
		return instance;
	}//getInstance
	

	@Override
	public void createCacheTable() throws CacheException {
		ReplicationMappingDao replicationMappingDao = new ReplicationMappingDao();
		replicationMappingDao.getAll(this);
	}
	
	public void createCacheTable(Connection con) throws CacheException {
		ReplicationMappingDao replicationMappingDao = new ReplicationMappingDao(con);
		replicationMappingDao.getAll(this);
	}

	/**
	 * 
	 */
	@Override
	public void initialize() throws CacheException {
		createCacheTable();
	}
	
	
	public boolean isProcessTypeExists(String processType) {
		boolean isExists = false;
		Map repMappingMap = getCacheNode(ReplicationMappingDao.CACHE_REPLICATIONMAPPING_NODE);

		if (repMappingMap != null) {
		    Collection<ReplicationMappingVo> values = repMappingMap.values();
		    Iterator<ReplicationMappingVo> itr = values.iterator();
		    ReplicationMappingVo keyObject = null;
		    while (itr.hasNext()) {
		    	keyObject = itr.next();
		    	if (keyObject.getProcessType().equals(processType)) {
		    		isExists = true;
		    		break;
		    	}
		    }
		}
		return isExists;
	 }//isProcessTypeExists 
	
	
	public Map<String, ReplicationMappingVo> getValues(String processType){
		Map<String, ReplicationMappingVo> dataValues = null;
		
		Map repMappingMap = getCacheNode(ReplicationMappingDao.CACHE_REPLICATIONMAPPING_NODE);

		if (repMappingMap != null) {
			dataValues = new LinkedHashMap<String, ReplicationMappingVo>();
			
		    Collection<ReplicationMappingVo> values = repMappingMap.values();
		    Iterator<ReplicationMappingVo> itr = values.iterator();
		    ReplicationMappingVo keyObject = null;
		    while (itr.hasNext()) {
		    	keyObject = itr.next();
		    	if (keyObject.getProcessType().equals(processType)) {
		    		dataValues.put(keyObject.getProcessingOrder(),keyObject);
		    	}
		    }
		}		
		
		return dataValues;
	}//getValues
	
	public static void main(String[] args){
		try{
			ReplicationMappingTable rTable = new ReplicationMappingTable();
			rTable.getValues("200001329^1");
		}
		catch(Exception ex){
			ex.printStackTrace();
		}
		
	}

}//class
