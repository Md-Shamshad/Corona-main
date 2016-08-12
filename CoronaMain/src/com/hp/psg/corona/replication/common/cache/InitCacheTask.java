package com.hp.psg.corona.replication.common.cache;

import java.lang.reflect.Method;

import com.hp.psg.corona.replication.common.exception.CacheException;
import com.hp.psg.corona.replication.common.util.Logger;

/**
 * This is the class which initiates the task based on the class name of corresponding Table.
 * Using reflection API, it will get the getInstance method and invoke the same.
 * 
 * @author kn
 *
 */
public class InitCacheTask implements Runnable {
	private String cacheClassName = null;
	private boolean refresh = false;
	
	InitCacheTask(String cacheClassName, boolean refresh) {
		this.cacheClassName = cacheClassName;
		this.refresh = refresh;
	}
	
	/** 
	 *  Running the task
	 */
	public void run() {
		try {
			initializeCache(cacheClassName, refresh);
		} catch (Exception e) {
			Logger.error(getClass(),"Could not load " + cacheClassName);
		}
	}//run
	
	/**
	 * By using reflection gets an instance of the class and then gets the singleton instance
	 *
	 * @param cacheClassName String
	 * @throws CacheException
	 */
	public void initializeCache(String cacheClassName, boolean refresh) throws Exception {
		Class args[] = {};
		Logger.info(getClass(),"Initializing the class :" + cacheClassName + ":");
		Class cacheClass = Class.forName(cacheClassName);

		Method method = cacheClass.getMethod("getInstance", args);

		CacheTable cacheObj = (CacheTable) method.invoke(null, new Object[0]);
		if (refresh) {
			method = cacheClass.getMethod("initialize", args);
			method.invoke(cacheObj, new Object[0]);
		}
		Logger.info(getClass(),"Successfully initialized the class :" + cacheClassName);

		cacheClass = null;
		method = null;
		cacheObj=null;
	}//initializeCache

}//end of class