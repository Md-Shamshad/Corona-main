package com.hp.psg.corona.replication.common.cache;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.hp.psg.corona.replication.common.exception.CacheException;
import com.hp.psg.corona.replication.common.exception.ReplicationException;
import com.hp.psg.corona.replication.common.util.Logger;
import com.hp.psg.corona.replication.common.util.Preferences;

 
/**
 * <p>Title: CacheTable</p>
 * <p>Description: This is a base class for any object that needs the ability to cache something.  The class
 * has the ability to create a table with cached enteries (cacheTable) when the child class is initialized.</p>
 *
 * <p>Copyright: Copyright (c) 2011</p>
 * <p>Company: Hewlett Packard</p>
 * @author 
 * @version 1.0
 */
public abstract class CacheTable implements java.io.Serializable  {

    private HashMap cacheMap = null;
    private static Map<String,String> cacheTableMap = new HashMap<String,String>();
    private static Map<String,List<String>> cacheClassTableMap = new HashMap<String,List<String>>();


    public CacheTable() {
    	cacheMap = new HashMap();
    }

    /**
     * Adding entry into Cache.
     * 
     * @param cacheEntity
     * @param oneCacheMap
     */
   @SuppressWarnings("unchecked")
	public void addCacheMap(String cacheEntity, Map oneCacheMap) {
    	
    	if (!cacheTableMap.containsKey(cacheEntity)) {
    		cacheTableMap.put(cacheEntity, cacheEntity);
    		
    		List<String> cacheList = null;
    		if (cacheClassTableMap.containsKey(getClass().getName())) {
    			cacheList = cacheClassTableMap.get(getClass().getName());
    		}
    		else {//adding to the class map
    			cacheList = new ArrayList<String>();
    			cacheClassTableMap.put(getClass().getName(), cacheList);
    		}
    		cacheList.add(cacheEntity);
    	}
        try {
        	//adding entry into cache
        	cacheMap.put(cacheEntity, oneCacheMap);
        }
        catch (Exception ex) {
            Logger.error(getClass(), "Exception while adding entry into Cache..."+ ex.getMessage());
        }        
    }//addCacheMap
   

   /**
    * Adding entry into Cache.
    * 
    * @param cacheEntity
    * @param cacheKey
    * @param cacheObject
    */
    @SuppressWarnings("unchecked")
	public void addCacheRecord(String cacheEntity, String cacheKey, Object cacheObject) {
    	if (!cacheTableMap.containsKey(cacheEntity)) {
    		cacheTableMap.put(cacheEntity, cacheEntity);
    	}
        try {
            HashMap oneHash = (HashMap)cacheMap.get(cacheEntity);
            if(oneHash==null){
            	oneHash = new HashMap();
            	cacheMap.put(cacheEntity, oneHash);
            }
            oneHash.put(cacheKey, cacheObject);
        }
        catch (Exception ex) {
        	Logger.error(getClass(), "addCacheRecord::: Exception while adding entry into Cache..."+ ex.getMessage());
        }        
    }//addCacheRecord
    
 
    /**
     * Reading entry from Cache.
     * 
     * @param cacheEntity
     * @param cacheKey
     * @return
     */
    @SuppressWarnings("unchecked")
	public Object getCacheRecord(String cacheEntity, String cacheKey) {
        Object returnObj = null;
        try {
           HashMap node = (HashMap)cacheMap.get(cacheEntity);
           if(node!=null){
        	   return node.get(cacheKey);
           }
        }
        catch (Exception cacheEx) {
        	Logger.error(getClass(), "getCacheRecord::: Exception while reading entry from Cache..."+ cacheEx.getMessage());
        }
        
        return returnObj;
    }//getCacheRecord


    /**
     * Reading data from Cache.
     * 
     * @param cacheEntity
     * @return
     */
    public Map getCacheNode(String cacheEntity) {
        Map dataMap = null;
        try {
            dataMap = (Map) cacheMap.get(cacheEntity);
        }
        catch (Exception cacheEx) {
        	Logger.error(getClass(), "getCacheNode::: Exception while reading entry from Cache..."+ cacheEx.getMessage());
        }
        
        return dataMap;
    }//getCacheNode

    /**
     * Deleting data from Cache
     * 
     * @param cacheEntity
     * @param cacheKey
     */
    public void deleteRecord(String cacheEntity, String cacheKey) {
        try {
            HashMap oneHash = (HashMap)cacheMap.get(cacheEntity);
            if(oneHash!=null){
            	cacheMap.remove(cacheKey);
            }            
        }
        catch (Exception cacheEx) {
        	Logger.error(getClass(), "deleteRecord::: Exception while deleting data in Cache..."+ cacheEx.getMessage());
        }
        
    }//deleteRecord

    /**
     * Deleting data in Cache
     * 
     * @param cacheEntity
     */
    public void deleteAllRecords(String cacheEntity) {
        try {
            HashMap oneHash = (HashMap)cacheMap.get(cacheEntity);
            if(oneHash!=null){
            	oneHash.clear();
            	cacheMap.remove(cacheEntity);
            }            
        }
        catch (Exception cacheEx) {
        	Logger.error(getClass(), "deleteAllRecords::: Exception while deleting data in Cache..."+ cacheEx.getMessage());
        }        
    }//deleteAllRecords


    /**
     * Create all the cache objects for the table
     *
     * @param cacheTableId int cache table id
     */
    public abstract void createCacheTable() throws CacheException;

    /**
     * Abstract method for initializing the cache
     */
    public abstract void initialize() throws CacheException;
    
    public static Map<String,String> getCacheTableMap() {
    	return cacheTableMap;
    }
    
    public static Map<String,List<String>> getCacheClassTableMap() {
    	return cacheClassTableMap;
    }
    
}//end of class