package com.hp.psg.corona.replication.common.cache;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import com.hp.psg.corona.replication.common.cache.InitCacheTask;
import com.hp.psg.corona.replication.common.exception.CacheException;
import com.hp.psg.corona.replication.common.util.Logger;
import com.hp.psg.corona.replication.common.util.Preferences;


/**
 * <p>Title: ACacheRepository</p>
 * <p>Description: This is a base class for any object that needs the ability to cache something. 
 *  The class has the ability to create a table with cached enteries (cacheTable1) when the child class is initialized.  
 *  </p>
 *
 * @author kn
 *
 */
public abstract class ACacheRepository {

	//default tables
	public final static String DEFAULT_CACHE_TABLE_NAME="cache.master.tables";
	public static final String CACHE_REFRESH_PREFS_NAME="cache.master.tables";
	//for threads
	private ExecutorService cacheThreadExecutor = null;
	private int initThreads = 3;
	
	public ACacheRepository(){
		
	}
	
	/**
     * Abstract method for initializing the cache
     */
	public abstract void initialize() throws CacheException;
    
    public abstract void refreshCache() throws CacheException;
    
    
    public void initialize(String cachePrefName) throws CacheException {
    	initialize(cachePrefName,false,false);
    }
	
    
    /**
     * This method reads the Cache tables from preferences
     * based on each cache table get the corresponding CacheTable class
     * Then create thread and assign the task to it.
     * 
     * @param cachePrefName
     * @param useTransaction
     * @param refresh
     * @throws CacheException
     */
    public void initialize(String cachePrefName, boolean useTransaction, boolean refresh) throws CacheException {
        Preferences prefs = Preferences.getInstance();
                        
        List<Future> taskList = new ArrayList<Future>();
        if (cachePrefName == null) {
        	cachePrefName = "DEFAULT_CACHE_TABLE_NAME";
        }
        
        int cacheCount = 0;
        boolean initFirst = false;
        if (cachePrefName != null) {        	
        	try {
        		//read the tables from preferences
        		String[] cacheList = prefs.readTokens(prefs.get(cachePrefName, ""), ",");
        		Logger.debug(getClass(), "The number of Caching tables are:" + cacheList.length);
	            if (cacheList.length == 0) {
	            	return;
	            }
	            
	            //findout number of threads
	    		initThreads = prefs.getInt("cache.threads", 3);
	    		int tableCount = cacheList.length;
	    		if (tableCount < initThreads ) {
	    			initThreads = cacheList.length;
	    		}
	    		
	    		//create thread pool
	    		cacheThreadExecutor = Executors.newFixedThreadPool(initThreads);
	    	
	            for (int i = 0; i < tableCount; i++) {
	                //cache info is class name for respective table 
	                String[] cacheInfo = prefs.readTokens(prefs.get("cache." + cacheList[i], ""), ",");
	                try {
	                    //Initialize the singleton object
	                    if (cacheInfo != null && cacheInfo.length>0 && cacheInfo[0] != null) {	
	                    	InitCacheTask initTask = new InitCacheTask(cacheInfo[0],refresh);
	                    	
	                    	//start the thread
							Future future = cacheThreadExecutor.submit(initTask);
							taskList.add(future);
							
							cacheCount++;
							if (cacheCount == initThreads || !initFirst || i+1 == cacheList.length ) {
								initFirst = true;
								for (int z=0;z<taskList.size();z++) {
									try {
										taskList.get(z).get();
									}
									catch (Exception ex) {
										Logger.error(getClass(),ex.getMessage(),ex);
									}
								}
								cacheCount = 0;
								taskList.clear();
							}
	                    }
	                }
	                catch (Exception e) {
	                    Logger.fatal(getClass(),"Exception while initializing the Cache Manager [" + cacheList[i] + "]!"+e.getMessage(),e);
	                }
	            }//i	            
    		}
    		catch (Exception ex) {
    			ex.printStackTrace();
    		}
        }
        else {
            Logger.fatal(getClass(),"Could not initialize the Cache Manager!");
        }
    }//initialize
    
 
    public void refreshCache(String refreshPrefName) throws CacheException{
        if (refreshPrefName == null) {
        	refreshPrefName = CACHE_REFRESH_PREFS_NAME;
        } 
    	initialize(refreshPrefName,true,true);
    }
	
}//end of class
