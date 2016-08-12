package com.hp.psg.corona.replication.common.cache.replicationmapping.dao;

import java.sql.Connection;
import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.TreeMap;

import com.hp.psg.corona.replication.common.cache.CacheTable;
import com.hp.psg.corona.replication.common.cache.replicationmapping.value.ReplicationMappingVo;
import com.hp.psg.corona.replication.common.exception.CacheException;
import com.hp.psg.corona.replication.common.exception.CoronaException;
import com.hp.psg.corona.replication.common.util.Logger;
import com.hp.psg.corona.replication.common.util.Preferences;
import com.hp.psg.corona.replication.quote.QuoteConnection;

public class ReplicationMappingDao extends QuoteConnection{
	
	public static final String CACHE_REPLICATIONMAPPING_NODE = "/replicationmapping/ReplicationMapping";
	//db query
	private static final String TABLE_SQL_REPLICATIONMAPPINGTAB = "SELECT * FROM REPLICATION_MAPPING ORDER BY PROCESS_TYPE, PROCESSING_ORDER ASC";
	
	private Connection con = null;
	
	//Used to identify whether con has passed by client or created by application. If created by application, it will get closed by end.
	private boolean isConCreated = false;

	public ReplicationMappingDao() throws CacheException {

	}
	
	public ReplicationMappingDao(Connection con) throws CacheException {
		this.con = con;
	}
	
	public Map<String, ReplicationMappingVo> getAll() throws CacheException{
		return getAll(null);
	}
	
	public Map<String, ReplicationMappingVo> getAll(CacheTable cacheTable) throws CacheException{
		Map<String, ReplicationMappingVo> rootMap = new LinkedHashMap<String, ReplicationMappingVo>();		
		
		isConCreated = false;
		ResultSet resultSet = null;
		try {   
			//Initiate the transaction
			if( con == null){		
				String dataSource = Preferences.getInstance().get("corona.replication.primary.data.source.name","PrimaryDS");
	    		con = getConnection(dataSource);
	    		isConCreated = true;
			}
    		//get all the data
            resultSet = runPreparedStatement(con,TABLE_SQL_REPLICATIONMAPPINGTAB);
            
            ReplicationMappingVo replicationMappingVo = null;
            String processType = null;
	    	String sourceTable = null;
		    String destinationTable = null;
		    String processingOrder = null;
		    String comments = null;
		    Date lastRunDate = null;
		    String createdBy = null;
		    Date createdDate = null;
		    String lastModifiedBy = null;
		    Date lastModifiedDate = null;
		    String replicationKey = null;
		    long lastRunEventId = -1;	
		     		    
            while (resultSet.next()) {
            	processType = resultSet.getString(1).trim();
            	sourceTable = resultSet.getString(2).trim();
            	destinationTable = resultSet.getString(3).trim();
            	processingOrder = resultSet.getString(4).trim();
            	comments = resultSet.getString(5).trim();
            	lastRunDate = resultSet.getDate(6);
            	createdBy = resultSet.getString(7).trim();
            	createdDate = resultSet.getDate(8);
            	lastModifiedBy = resultSet.getString(9).trim();
            	lastModifiedDate = resultSet.getDate(10);
            	lastRunEventId = resultSet.getLong(11);
            	            	
            	//construct VO object
				replicationMappingVo = new ReplicationMappingVo();
				replicationMappingVo.setProcessType(processType);
				replicationMappingVo.setSourceTable(sourceTable);
				replicationMappingVo.setDestinationTable(destinationTable);
				replicationMappingVo.setProcessingOrder(processingOrder);
				replicationMappingVo.setComments(comments);
				replicationMappingVo.setLastRunDate(lastRunDate);
				replicationMappingVo.setCreatedBy(createdBy);
				replicationMappingVo.setCreatedDate(createdDate);
				replicationMappingVo.setLastModifiedBy(lastModifiedBy);
				replicationMappingVo.setLastModifiedDate(lastModifiedDate);
				replicationMappingVo.setLastRunEventId(lastRunEventId);
                
				//key to identify the record
				replicationKey = processType + "-" + sourceTable;				
				rootMap.put(replicationKey, replicationMappingVo);
				
            } //while
            
            //putting into cache
            if (cacheTable != null && rootMap.size()>0) {
                cacheTable.addCacheMap(CACHE_REPLICATIONMAPPING_NODE,rootMap);
            }

        }
        catch (CacheException hpWsEx) {
            throw hpWsEx;
        }
        catch (CoronaException cEx) {
        	throw new CacheException(cEx.getErrorNumber(),null,cEx.getMessage(), cEx);            
        } 
        catch (SQLException e) {
            Logger.fatal(getClass(),"Problem occured in SQLException block of getAll " + e);
            Logger.error(getClass(),e.getMessage());
        }     
        finally{
        	if( isConCreated ){
        		closeConnection(con);
        	}
        }
                
		return rootMap;
	}//getAll
	
}//class
