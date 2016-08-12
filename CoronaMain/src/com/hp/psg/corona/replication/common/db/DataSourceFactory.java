package com.hp.psg.corona.replication.common.db;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashMap;

import javax.naming.InitialContext;
import javax.sql.DataSource;

import org.apache.commons.dbcp.ConnectionFactory;
import org.apache.commons.dbcp.DriverManagerConnectionFactory;
import org.apache.commons.dbcp.PoolableConnectionFactory;
import org.apache.commons.dbcp.PoolingDataSource;
import org.apache.commons.pool.ObjectPool;
import org.apache.commons.pool.impl.GenericObjectPool;

import com.hp.psg.corona.common.util.JndiUtils;
import com.hp.psg.corona.replication.common.exception.CoronaException;
import com.hp.psg.corona.replication.common.message.MessageConstants;
import com.hp.psg.corona.replication.common.util.Logger;
import com.hp.psg.corona.replication.common.util.Preferences;


/**
 * <p>Title: DataSourceFactory</p>
 *
 * <p>Description: Creates jdbc DataSource object using local JDBC depending upon preferences.
 * Based on database object, it will create connection and pooled it. It also set the properties to pooled object.</p>
 *
 * <p>Copyright: Copyright (c) 2012</p>
 *
 * <p>Company: HP</p>
 *
 * @author 
 * @version 1.0
 */
public class DataSourceFactory {
	static int openConCount = 0;
	
	//To caputre the singleton instance of DataSourceFactory 
	private static DataSourceFactory dataSourceFactory = null;
	
	//To cache the dataSourceName and its DataSource object
    HashMap<String, DataSource> dataSources = new HashMap<String, DataSource>();
    
    /**
     * private Default constructor. 
     * Create appropriate datasource object.
    */
    private DataSourceFactory() {

    }
    
    /**
     * Create a singleton instance if not already created
     * @return DataSourceFactory
     * @throws 
     */
    public static DataSourceFactory getInstance(){
    	
    	if( dataSourceFactory == null){    		
    		dataSourceFactory = new DataSourceFactory();
    	}
    	
    	return dataSourceFactory;
    }//getInstances
    
    /**
     * Return already populated datasource object
     * @param dataSourceName
     * @return dataSource
     */
    public DataSource getDataSource(String dataSourceName) throws CoronaException {
    	DataSource dataSource = (DataSource) dataSources.get(dataSourceName);
    	
    	if( dataSource == null){    		
    		HashMap<String,String> prefs = Preferences.getPreferencesFromFile();
    		String jndiFlag = (String) prefs.get("jdbc.use.jndi");
            if (jndiFlag == null||jndiFlag.trim().length()==0)
                jndiFlag = "true";
            
            //create appropriate datasource
            if(jndiFlag.equalsIgnoreCase("true")){
                dataSource = getContainerDataSource(dataSourceName);
            }else{
                dataSource = getDBCPDataSource();
            }
            
    		//cacheing dataSource object
    		dataSources.put(dataSourceName, dataSource);
    	}
    	return dataSource;
    }//getConfigDataSource
    
    
    /**
     * Use service locator and obtain JNDI based datasource
     * @return javax.sql.DataSource
     * @throws HpCacheException
     */
    private javax.sql.DataSource getContainerDataSource(String dataSourceName) throws CoronaException {
        javax.sql.DataSource dataSource = null;
        //do the lookup
        try {
            //dataSource = (javax.sql.DataSource) ServiceLocator.getInstance().getFromContext(dataSourceName);
        	InitialContext context = (InitialContext) JndiUtils.getInitialContext();
        	dataSource = (DataSource) context.lookup(dataSourceName);
        }
        catch (javax.naming.NamingException nameEx) {
            Logger.error(getClass(),"Caught a Naming Exception "+nameEx.getMessage());
            throw new CoronaException(nameEx.getMessage(),nameEx);
        }      
        catch (Exception ex) {
            Logger.error(getClass(),"Caught a General Exception "+ex.getMessage());
            throw new CoronaException(ex.getMessage(),ex);

        }
        return dataSource;
        
     }//getContainerDataSource
    
    
        
    /**
     * Return already populated datasource object
     * @param dataSourceName
     * @param connectURI
     * @param maxActiveCon
     * @param maxIdleCon
     * @return
     */
	public DataSource getDataSource(String dataSourceName, String connectURI, int maxActiveCon, int maxIdleCon) throws CoronaException{
		DataSource dataSource = (DataSource) dataSources.get(dataSourceName);
		
		try{
			if( dataSource == null){
				Logger.debug(getClass(), "Initiating the processing for create data source...");
				
				//load underlying driver class
				Class.forName("oracle.jdbc.driver.OracleDriver");
				
				//construct generic object pool
				ObjectPool connectionPool = new GenericObjectPool(null);
				
				//Set properties 
				((GenericObjectPool)connectionPool).setMaxActive(maxActiveCon);				
				((GenericObjectPool)connectionPool).setMaxIdle(maxIdleCon);
				
				ConnectionFactory connectionFactory = new DriverManagerConnectionFactory(connectURI, null);	
				@SuppressWarnings("unused")
				PoolableConnectionFactory poolableConnectionFactory = new PoolableConnectionFactory(connectionFactory,connectionPool,null,null,false,true);
                
				//create Pooling data source
				dataSource = new PoolingDataSource(connectionPool);
				
				//Check if connection can be obtained to validate connect string
				Connection connection = dataSource.getConnection();
				
				//If connectURI was correct, previous line didn't throw any sql exception then test passed... So close the connection
				connection.close();
				
				//cacheing dataSource object
				dataSources.put(dataSourceName, dataSource);
				Logger.debug(getClass(), "Data source initiation process successfully completed.");
			}			
		}
		catch(ClassNotFoundException clEx){
			Logger.error(getClass(),"Caught a Class Not Found Exception..."+clEx.getMessage());
			throw new CoronaException(MessageConstants.GENERAL_PROCESSING_ERROR, clEx.getMessage(), clEx);
		}
		catch(SQLException sqlEx){
			Logger.error(getClass(),"Caught a SQL Exception..."+sqlEx.getMessage());
			throw new CoronaException(MessageConstants.DB_PROCESSING_ERROR, sqlEx.getMessage(), sqlEx);
		}
		
		return dataSource;
	}//getDataSource
	
	
	
	
	/**
	 * Use DBCP api to create a local JDBC connection pool and datasource for Quote DB
	 * @param dataSourceName
	 * @return
	 */
	private DataSource getDBCPDataSource() throws CoronaException {
		PoolingDataSource dataSource = null;
        Connection connection = null;
        String connectURI = "";
        String strMaxActive = "";
        String strMaxIdle = "";
        try {
        	if(Preferences.isInstCreated()){
            	//read from preferences db
        		Preferences prefs = Preferences.getInstance();
        		connectURI = prefs.get("jdbc.connect.uri", "");
            	strMaxActive = prefs.get("max.active.connections", "");
            	strMaxIdle = prefs.get("max.idle.connections", "");
        	}else{
            	//read from preferences file
        		HashMap prefs = Preferences.getPreferencesFromFile();
            	connectURI = (String) prefs.get("jdbc.connect.uri");
            	strMaxActive = (String)prefs.get("max.active.connections");
            	strMaxIdle = (String) prefs.get("max.idle.connections");
        	}
            Logger.info(getClass(),"Connect URI for JDBC connection: " + connectURI);
            if(connectURI==null || connectURI.trim().length()==0){
                Logger.error(getClass(), "JDBC connect URI is null. Set jdbc.connect.uri in hp_ws.cfg");
            }
            
            Logger.debug(getClass(), "Initiating the processing for creating data source...");

            //Load the underlying JDBC driver
            Class.forName("oracle.jdbc.driver.OracleDriver");
            ObjectPool connectionPool = new GenericObjectPool(null);

            //infinite number of active connections..default is 10
            int maxActive = -1;
            if(strMaxActive!=null){
                try{
                    maxActive = Integer.parseInt(strMaxActive);
                }catch(Exception e){
                    maxActive = -1;
                }
            }
            ((GenericObjectPool)connectionPool).setMaxActive(maxActive);

            //infinite number of idle connections..default is 10
            int maxIdle = -1;
            if (strMaxIdle != null) {
                try {
                    maxIdle = Integer.parseInt(strMaxIdle);
                } catch (Exception e) {
                    maxIdle = -1;
                }
            }
            ((GenericObjectPool)connectionPool).setMaxIdle(maxIdle);

            ConnectionFactory connectionFactory = new DriverManagerConnectionFactory(connectURI,null);
            @SuppressWarnings("unused")
			PoolableConnectionFactory poolableConnectionFactory = new PoolableConnectionFactory(connectionFactory,connectionPool,null,null,false,true);
            dataSource = new PoolingDataSource(connectionPool);
            dataSource.setAccessToUnderlyingConnectionAllowed(true);
            //Check if connection can be obtained to validate connect string
            connection = dataSource.getConnection();
            
            //if connectURI was correct, previous line didnt throw any sql exception then test passed...So close the connection
            connection.close();
            
            Logger.debug(getClass(), "Data source initiation process successfully completed.");
         }
         catch(ClassNotFoundException clEx){
 			Logger.error(getClass(),"Caught a Class Not Found Exception..."+clEx.getMessage());
 			throw new CoronaException(MessageConstants.GENERAL_PROCESSING_ERROR,clEx.getMessage(),clEx);
 		}
 		catch(SQLException sqlEx){
 			Logger.error(getClass(),"Caught a SQL Exception..."+sqlEx.getMessage());
 			throw new CoronaException(MessageConstants.DB_PROCESSING_ERROR,sqlEx.getMessage(),sqlEx);
 		}
 		
 		return dataSource;		
	}//getDBCPQuoteDataSource	
	
	
}//end of class