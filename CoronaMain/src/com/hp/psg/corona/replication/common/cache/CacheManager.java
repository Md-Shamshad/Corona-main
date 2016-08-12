package com.hp.psg.corona.replication.common.cache;

import com.hp.psg.corona.replication.common.exception.CacheException;
import com.hp.psg.corona.replication.common.util.Logger;

/**
 * @author kn
 *
 */
public class CacheManager extends ACacheRepository {
	public final static String CACHE_PREFS_NAME="cache.master.tables";
	private static ACacheRepository instance = null;
	
	private CacheManager() throws CacheException{
		initialize();
	}//
	
	public static ACacheRepository getInstance() throws CacheException {
    	if (instance == null) {
    		instance = new CacheManager();
    	}
        return instance;
    }//getInstance
	
	
	/* 
	 * Initialize the classes which are to be stored in Cache by using prefences entries.
	 */
	@Override
	public void initialize() throws CacheException {
		Logger.info(getClass(),"Initializing Cache for Master tables...");
    	initialize(CACHE_PREFS_NAME);
	}

	/* 
	 * 
	 */
	@Override
	public void refreshCache() throws CacheException {
		refreshCache(CACHE_PREFS_NAME);
	}//refreshCache


}//end of class
