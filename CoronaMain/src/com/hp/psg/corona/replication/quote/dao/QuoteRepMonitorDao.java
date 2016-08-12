package com.hp.psg.corona.replication.quote.dao;

import java.sql.Connection;

import javax.naming.InitialContext;
import javax.sql.DataSource;

import com.hp.psg.common.util.logging.LoggerInfo;
import com.hp.psg.corona.common.util.Config;
import com.hp.psg.corona.common.util.JndiUtils;
import com.hp.psg.corona.common.util.Logger;
import com.hp.psg.corona.error.util.CoronaErrorHandler;
import com.hp.psg.corona.replication.common.cache.query.QueryRepository;
import com.hp.psg.corona.replication.common.cache.query.ReplicationQueryManager;
import com.hp.psg.corona.replication.common.exception.CacheException;
import com.hp.psg.corona.replication.common.exception.CoronaException;
import com.hp.psg.corona.replication.common.exception.ReplicationException;
import com.hp.psg.corona.replication.quote.QuoteConnection;

public class QuoteRepMonitorDao extends QuoteConnection {
	private QueryRepository queryRepository = null;
	String updatePickedToReadyStateQuery = null;
	
	LoggerInfo logInfo=null;
	
	public QuoteRepMonitorDao(){
		queryRepository = ReplicationQueryManager.getInstance();
		updatePickedToReadyStateQuery = queryRepository.getQuery("Quote", "RepEvents", "UPDATEPICKEDTOREADY");
		logInfo = new LoggerInfo (this.getClass().getName());
	}
			
	public void updatePickedToReadyStatus() throws ReplicationException{
		QuoteConnection quoteCon = null;
		Connection con = null;
		Logger.repDebug(logInfo, "updatePickedToReadyStatus", "Begining of the method to update picked to ready state for rep events... ");	
		
		try{	
			quoteCon = new QuoteConnection();
			String dataSourceName = Config.getProperty("corona.replication.primary.data.source.name");
			con = quoteCon.getConnection(dataSourceName);			
			Logger.repDebug(logInfo, "updatePickedToReadyStatus", "Executing query::: "+ updatePickedToReadyStateQuery);
			int noOfUpdate = executeUpdatePreparedStatement(con, updatePickedToReadyStateQuery);			
		}		
		catch(CoronaException ex){
			throw new ReplicationException(ex.getErrorNumber(),ex.getMessage(),ex);
		}
		catch(Exception ex){
			throw new ReplicationException(ex.getMessage(),ex);
		}
		finally{
			quoteCon.closeConnection(con);
		}
		
		Logger.repDebug(logInfo, "updatePickedToReadyStatus", "End of the method to update picked to ready state for rep events. ");
				
	}//updateLastRunDateForReports	

}//class
