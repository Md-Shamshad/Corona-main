package com.hp.psg.corona.replication.quote;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashMap;

import com.hp.psg.corona.replication.common.db.DBExecutor;
import com.hp.psg.corona.replication.common.db.DataSourceFactory;
import com.hp.psg.corona.replication.common.exception.CacheException;
import com.hp.psg.corona.replication.common.util.Logger;
import com.hp.psg.corona.replication.common.util.Preferences;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.sql.DataSource;

/**
 * This file is the parent of all the Dao classes (which interact with a Quote database table) used in system.
 * It has methods for general interaction with the database.
 * 
 * @author kn
 *
 */
public class QuoteConnection extends DBExecutor{
	
	public Connection getConnection() throws CacheException {
	      Connection connection = null;
	      DataSource dataSource = null;
	      try {
	          Context context = new InitialContext();
	          dataSource = (DataSource) context.lookup("java:comp/env/jdbc/multiDS");
	          connection = dataSource.getConnection();

	      } catch (java.sql.SQLException sqlEx) {
	          Logger.error(
	                getClass(),
	                " getConnection() Caught an SQL Exception "
	                      + sqlEx.getMessage());
	          throw new CacheException(sqlEx.getMessage(), sqlEx);
	      } catch (Exception ex) {
	          Logger.error(
	                getClass(),
	                " getConnection() Caught a General Exception "
	                      + ex.getMessage());
	          throw new CacheException(ex.getMessage(), ex);
	      }
	      return connection;
	}//getConnection
	
	
	           
    /**
     * Get a connection from the datasoruce
     * 
     * @return Connection
     */
    public Connection getConnection(String dataSourceName) throws CacheException {
        Connection connection = null;
      
        Logger.debug(getClass(), "Creating DB Connection by using a Data Souce::: "+ dataSourceName);
        
        try {//get connection
        	javax.sql.DataSource ds = DataSourceFactory.getInstance().getDataSource(dataSourceName);
            connection = ds.getConnection();  
       }
       catch (java.sql.SQLException sqlEx) {
            Logger.error(getClass(),"CacheDao.getConnection() Caught an SQL Exception "+sqlEx.getMessage());
            throw new CacheException(sqlEx.getMessage(), sqlEx);
        }
        catch (Exception ex) {
            Logger.error(getClass(),"CacheDao.getConnection() Caught a General Exception "+ex.getMessage());
            throw new CacheException(ex.getMessage(),ex);

        }
            
        return connection;        
    }//getConnection
        
    
    /**
     * Get a connection by passing a connection string 
     * 
     * @param connectURI
     * @param maxActiveConn
     * @param maxIdleConn
     * @return
     * @throws HpCacheException
     */
    public Connection getConnection(String connectURI, int maxActiveConn, int maxIdleConn) throws CacheException {
        Connection connection = null;

        try {
        	javax.sql.DataSource ds = DataSourceFactory.getInstance().getDataSource(null, connectURI, maxActiveConn, maxIdleConn);
            connection = ds.getConnection();
       }
       catch (java.sql.SQLException sqlEx) {
            Logger.error(getClass(),"CacheDao.getConnection() Caught an SQL Exception "+sqlEx.getMessage());
            throw new CacheException(sqlEx.getMessage(),sqlEx);
        }
        catch (Exception ex) {
            Logger.error(getClass(),"CacheDao.getConnection() Caught a General Exception "+ex.getMessage());
            throw new CacheException(ex.getMessage(),ex);

        }
                
        return connection;        
    }//getConnection
    
     
    
    /**
     * To enable the transaction
     *
     * @param conn Connection
     * @return boolean
     */
    public boolean openTransaction(Connection con) {
        try {
            con.setAutoCommit(false);
        } catch (Exception e) {
            e.printStackTrace();
            Logger.error(getClass(),"Exception while commiting transaction...",e);
            return false;
        } 
        
        return true;
    }//openTransaction
	
	/**
     * Committing the transaction.
     *
     * @param conn Connection
     * @return boolean
     */
    public boolean commitTransaction(Connection conn) {
        try {
            conn.commit();
        } catch (Exception e) {
            Logger.error(getClass(),"Exception while commiting transaction...",e);
            return false;
        } 
        
        return true;
    }//commitTransaction
    
    /**
     * Rollback the transaction.
     *
     * @param conn Connection
     * @return boolean
     */
    public boolean rollbackTransaction(Connection conn) {
        try {
            conn.rollback();
        } catch (Exception e) {
            Logger.error(getClass(),"Exception while rollbacking the transaction...",e);
            return false;
        } 
        
        return true;
    }//commitTransaction
	
	 /**
     * Once you are done with the result set make sure you close the connection
     */
    public void closeConnection(Connection con) {
        try {
            if (prepStatement != null) {
                prepStatement.close();
            }
            
            if (returnSet != null) {
                returnSet.close();
            }
            if (con != null) {
            	con.close();
            }
        }
        catch (SQLException sqlEx) {
            Logger.error(getClass(),"CacheDao.closeConnection() Caught an SQL Exception "+sqlEx.getMessage());
        }
        finally {
            prepStatement = null;
            returnSet = null;
            con = null;
         }
    }//closeConnection 

}//class
