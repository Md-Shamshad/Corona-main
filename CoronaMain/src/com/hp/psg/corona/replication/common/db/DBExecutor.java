package com.hp.psg.corona.replication.common.db;

import java.io.BufferedReader;
import java.io.Reader;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.hp.psg.corona.replication.common.exception.CoronaException;
import com.hp.psg.corona.replication.common.message.MessageConstants;
import com.hp.psg.corona.replication.common.util.Logger;


public abstract class DBExecutor {
    protected PreparedStatement prepStatement = null;    
    protected ResultSet returnSet = null;
    protected PreparedStatement bulkPrepStatement = null; 
    
	
    /**
     * Run a prepared statement with parameters on DB
     *
     * @param sqlStr String
     * @param parms ArrayList
     * @return ResultSet
     */
    @SuppressWarnings("unchecked")
	public List<Object> runStoredProcedure(Connection con, String sql, List<Object> options, List<Object> outputList) throws CoronaException {
    	List<Object> returnList = new ArrayList<Object>();
    	CallableStatement cs = null;
                
        int optSize = -1;
        if (options != null) {
        	optSize = options.size();
        }
        int outSize = -1;
        if( outputList != null){
        	outSize = outputList.size();
        }
        
        try {
        	cs = con.prepareCall(sql);

        	//set input parameters
            for (int i = 0; i < optSize; i++) {
            	cs.setObject(i + 1, options.get(i));
             }//i

            //set output parameters
            for (int i=0; i<outSize; i++) {
            	cs.registerOutParameter(i+optSize+1, Integer.parseInt((String)outputList.get(i)));
            }

            // debugSql(sql,options,outputList);
            cs.execute();

            int numberOfParameters = optSize + outSize;
            for (int i= optSize+1; i<=numberOfParameters; i++) {
            	returnList.add(cs.getObject(i));
            }
        }
        catch (SQLException sqlEx) {
            Logger.error(getClass(),"DBExecutor.runPreparedStatement() Caught an SQL Exception "+sqlEx.getMessage());
            throw new CoronaException(MessageConstants.DB_PROCESSING_ERROR,sqlEx.getMessage(),sqlEx);
        } catch (Exception ex) {
            Logger.error(getClass(),"DBExecutor.runPreparedStatement() Caught an SQL Exception "+ex.getMessage());
            throw new CoronaException(MessageConstants.DB_PROCESSING_ERROR,ex.getMessage(),ex);
        }
        finally{
        	 cs = null;
        }
        
        return returnList;        
    }//runConfigStoredProcedure
    
   
    
    /**
     * Run a prepared statement without any parameters
     *
     * @param sqlStr String
     * @return ResultSet
     */
    public ResultSet runPreparedStatement(Connection con, String sqlStr) throws CoronaException {
        return runPreparedStatement(con, sqlStr, null);
    }

    /**
     * Run a prepared statement with parameters
     *
     * @param sqlStr String
     * @param parms ArrayList
     * @return ResultSet
     */
    public ResultSet runPreparedStatement(Connection con, String sqlStr, List<Object> parms) throws CoronaException {               
        try {
            prepStatement = con.prepareStatement(sqlStr);

            if (parms != null) {
            	int parmSize = parms.size();
                for (int i = 0; i < parmSize; i++) {
                    prepStatement.setObject(i + 1, parms.get(i));
                }
            }

            prepStatement.execute();
            returnSet = prepStatement.getResultSet();
        }
        catch (SQLException sqlEx) {
            Logger.error(getClass(),"DBExecutor.runPreparedStatement() Caught an SQL Exception "+sqlEx.getMessage());
            throw new CoronaException(MessageConstants.DB_PROCESSING_ERROR,sqlEx.getMessage(),sqlEx);
        }
      
        return returnSet;
    }//runPreparedStatement
    
    
    public void initiateBulkOperation(Connection con, String sqlStr)
	    throws CoronaException {
	try {
		bulkPrepStatement = con.prepareStatement(sqlStr);
	} catch (SQLException sqlEx) {
	    Logger.error(getClass(),
		    "DBExecutor.bulkInsertPrepare() Caught an SQL Exception "
			    + sqlEx.getMessage());
	    throw new CoronaException(MessageConstants.DB_PROCESSING_ERROR,sqlEx.getMessage(), sqlEx);
	}
    }// initiateBulkOperation

    /**
     * Prepare the bulk insert statement with parameters
     * 
     * @param sqlStr
     *            String
     * @param parms
     *            ArrayList
     * @return ResultSet
     */
    public void bulkInsertPrepare(List<Object> parms) throws CoronaException {
	try {
	    if (parms != null) {
		int parmSize = parms.size();
		for (int i = 0; i < parmSize; i++) {
			bulkPrepStatement.setObject(i + 1, parms.get(i));
		}
	    }

	    bulkPrepStatement.addBatch();
	} catch (SQLException sqlEx) {
	    Logger.error(getClass(),
		    "DBExecutor.bulkInsertPrepare() Caught an SQL Exception "
			    + sqlEx.getMessage());
	    throw new CoronaException(MessageConstants.DB_PROCESSING_ERROR,sqlEx.getMessage(), sqlEx);
	}
    }// bulkInsertPrepare

    
    
    public ResultSet executeBulkInsert(Connection con) throws CoronaException {           
        try {
        	bulkPrepStatement.executeBatch();
            returnSet = bulkPrepStatement.getResultSet();
        }
        catch (SQLException sqlEx) {
            Logger.error(getClass(),"DBExecutor.executeBulkInsert() Caught an SQL Exception "+sqlEx.getMessage());
            throw new CoronaException(MessageConstants.DB_PROCESSING_ERROR,sqlEx.getMessage(),sqlEx);
        }
        finally{
        	try{
        		if (bulkPrepStatement != null){
        			bulkPrepStatement.close();
        		}
        	}
        	catch(SQLException ex){
        		throw new CoronaException(MessageConstants.DB_PROCESSING_ERROR,ex.getMessage(),ex);
        	}
        }
                
        return returnSet;
    }//executeBulkInsert
    
    

    public void resetPreparedStatement() throws CoronaException {
        try {
        	prepStatement.clearParameters();
        }
        catch (SQLException sqlEx) {
            Logger.error(getClass(),"DBExecutor.resetPreparedStatement() Caught an SQL Exception "+sqlEx.getMessage());
            throw new CoronaException(MessageConstants.DB_PROCESSING_ERROR,sqlEx.getMessage(),sqlEx);
        }
    }//resetPreparedStatement

    /**
     * Execute an update prepared statement without any parms. This must be used when you want to modify the data in a DB.
     * If you want to run an insert / delete / update query, this is what must be used.
     *
     * @param sqlStr String
     * @return ResultSet
     */
    public int executeUpdatePreparedStatement(Connection con, String sqlStr) throws CoronaException {
        return executeUpdatePreparedStatement(con, sqlStr, null);
    }

    /**
     * Run a prepared statement with parameters
     *
     * @param sqlStr String
     * @param parms  ArrayList
     * @return ResultSet
     */
    public int executeUpdatePreparedStatement(Connection con, String sqlStr, List<Object> parms) throws CoronaException {         
        try {        	
            prepStatement = con.prepareStatement(sqlStr);

            if (parms != null) {
            	int parmSize = parms.size();
                for (int i = 0; i < parmSize; i++) {
                    prepStatement.setObject(i + 1, parms.get(i));
                }
            }
            return prepStatement.executeUpdate();
        }
        catch (Exception sqlEx) {        	
            Logger.error(getClass(),"DBExecutor.executeUpdatePreparedStatement() Caught an SQL Exception " + sqlEx.getMessage());

           	throw new CoronaException(MessageConstants.DB_PROCESSING_ERROR,sqlEx.getMessage(),sqlEx);
         
        }       
    }//executeUpdatePreparedStatement

    public void closeStatement() {
        try {
            if (prepStatement != null) {
                prepStatement.close();
            }
        }
        catch (SQLException sqlEx) {
            Logger.error(getClass(),"DBExecutor.closeStatement() Caught an SQL Exception "+sqlEx.getMessage());
        }
        finally {
            prepStatement = null;
        }
    }//closeStatement

    public void closeResultSet() {
        try {
            if (returnSet != null) {
                returnSet.close();
            }
        }
        catch (SQLException sqlEx) {
            Logger.error(getClass(),"DBExecutor.closeResultSet() Caught an SQL Exception "+sqlEx.getMessage());
        }
        finally {
            prepStatement = null;
        }
    }//closeResultSet

    
    public String clobToString(Reader reader) throws Exception {
    	BufferedReader bufReader = new BufferedReader(reader);
        StringBuffer strBuf = new StringBuffer();
        String line = null;
        while( (line = bufReader.readLine()) != null ) {
        	strBuf.append(line);
        }
        bufReader.close();
        return strBuf.toString();
    }


    public String getProcedureSql(String procedureName, List<Object> inputOptions, List<Object> outputOptions){
        StringBuffer sql = new StringBuffer("BEGIN ");
        sql.append(procedureName + "(");
        
        int optSize = inputOptions.size();
        for (int i=0; i<optSize; i++) {
        	sql.append("?");
            if(i < optSize-1){
                sql.append(",");
            }
        }
        
        int outSize = outputOptions.size();
        for (int i=0; i<outSize; i++) {
        	sql.append("?");
            if (i < outSize-1) {
            	sql.append(",");
            }
        }
        sql.append("); END;");
        
        return sql.toString();
    }//getProcedureSql

}//class
