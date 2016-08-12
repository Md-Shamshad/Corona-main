package com.hp.psg.corona.replication.quote.dao;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.sql.Connection;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import javax.naming.InitialContext;
import javax.transaction.UserTransaction;

import com.hp.psg.common.util.logging.LoggerInfo;
import com.hp.psg.corona.replication.common.cache.replicationmapping.ReplicationMappingTable;
import com.hp.psg.corona.replication.common.cache.replicationmapping.value.ReplicationMappingVo;
import com.hp.psg.corona.replication.common.exception.CacheException;
import com.hp.psg.corona.replication.common.exception.ReplicationException;
import com.hp.psg.corona.replication.common.message.MessageConstants;
import com.hp.psg.corona.replication.common.util.ReplicationUtility;
import com.hp.psg.corona.common.util.Config;
import com.hp.psg.corona.common.util.JndiUtils;
import com.hp.psg.corona.common.util.Logger;

public class ReplicateTablesDaoImpl implements ReplicateTablesDao{
	LoggerInfo logInfo=null;
	
	public ReplicateTablesDaoImpl(){
		logInfo = new LoggerInfo (this.getClass().getName());
	}
	
	/**
	 * For a given processKey, it replicate source table data to destination table data.
	 * and the replication process will be controlled by single transaction context 
	 * for a given processType.  
	 */
	public boolean replicateSourceToDestination(Connection con, String processType,String processKey) throws ReplicationException{
		Logger.repDebug(logInfo, "replicateSourceToDestination", "Begining of replication  tables based on processType "+ processType + " and processKey "+ processKey);
		
		boolean replicated = false;
		UserTransaction transaction = null;
		Object txObj = null;
		
		try{
			ReplicationMappingTable rCacheTable = ReplicationMappingTable.getInstance();
			//GET mapping tables for a given processType
			Map<String, ReplicationMappingVo> rMapingVos = rCacheTable.getValues(processType);
			//Get transaction context
			InitialContext context = (InitialContext) JndiUtils.getInitialContext();
			txObj = context.lookup("javax.transaction.UserTransaction");			
			//txObj = ServiceLocator.getInstance().getFromContext("java:comp/UserTransaction");
			transaction = (UserTransaction) txObj;
			
			if ( rMapingVos != null && rMapingVos.size() > 0){
				Set<String> tableOrders = rMapingVos.keySet();
				Iterator<String> itr = tableOrders.iterator();
				Logger.repDebug(logInfo, "replicateSourceToDestination", "#### Begining transaction #####");
				transaction.begin();				
				
				ReplicationMappingVo rMappingVo = null;
				while(itr.hasNext()){
					rMappingVo = rMapingVos.get(itr.next());
					replicated = invokeDynamicTableQuery(con, rMappingVo.getSourceTable(), rMappingVo.getDestinationTable(), processKey, processType);
				}
				Logger.repDebug(logInfo, "replicateSourceToDestination", "#### Commiting transaction #####");
				transaction.commit();
			}
		}catch(CacheException ex){
			Logger.repError(logInfo, "replicateSourceToDestination", "Exception while caching REPLICATION_MAPPING table" , ex);
			try {
				Logger.repError(logInfo, "replicateSourceToDestination", "#### Rollbacking transaction #####",ex);
				transaction.rollback();
			} catch (Exception e) {
				throw new ReplicationException(ex.getMessage(),ex);
			}
			throw new ReplicationException(ex.getErrorNumber(),ex.getErrorMessage(),ex);
		}catch(ReplicationException repEx){
			Logger.repError(logInfo, "replicateSourceToDestination", "Exception while replicating data" , repEx);
			try {
				Logger.repError(logInfo, "replicateSourceToDestination", "#### Rollbacking transaction #####",repEx);
				transaction.rollback();
			} catch (Exception e) {
				throw new ReplicationException(repEx.getMessage(),repEx);
			}
			throw repEx;		
						
		}catch(Exception ex){
			Logger.repError(logInfo, "replicateSourceToDestination", "Exception while replicating data" , ex);
			try {
				Logger.repError(logInfo, "replicateSourceToDestination", "#### Rollbacking transaction #####",ex);
				transaction.rollback();
			} catch (Exception e) {
				throw new ReplicationException(ex.getMessage(),ex);
			}
			throw new ReplicationException(ex.getMessage(),ex);
		}
		
		Logger.repDebug(logInfo, "replicateSourceToDestination", "End of replication  tables based on processType "+ processType + " and processKey "+ processKey);
		
		return replicated;
	}
	
	/**
	 * This method reads DAO class for source table from property file, 
	 * creates its instance and invokes appropriate method on it based on processType.
	 * 
	 * @param con
	 * @param srcTable
	 * @param dstTable
	 * @param processKey
	 * @param processType
	 * @return
	 * @throws ReplicationException
	 */
	private boolean invokeDynamicTableQuery(Connection con, String srcTable, String dstTable, String processKey, String processType) throws ReplicationException{
		Logger.repDebug(logInfo, "invokeDynamicTableQuery", "Begining of replication for sourceTable " + srcTable + " and dstTable "+ dstTable + " replication process...");
		boolean copied = false;

		String propertyName = "replication.table."+srcTable+".class";
		String className = Config.getProperty(propertyName);
		String[] strArray = ReplicationUtility.splitProcessKey(processKey);
		try{
			//Dynamically creating objects and invoking methods based on operation.
			Class<?> classObject = Class.forName(className);
			String typeOfOperation = processType.substring(processType.lastIndexOf("_")+1, processType.length());
			if(classObject!=null){
				Object object = classObject.newInstance();
				Class<?>[] parameters = {Connection.class, Long.TYPE, String.class};
				Method method = classObject.getDeclaredMethod(typeOfOperation.toLowerCase(), parameters);
				method.invoke(object, con, Long.parseLong(strArray[0]),strArray[1]);
			}
		}
		catch(InvocationTargetException ex){
			Throwable tEx = ex.getCause();			
			if( tEx.getMessage().contains("ORA-00001")){
				throw new ReplicationException(MessageConstants.DATA_ALREADY_EXISTS,MessageConstants.IDS_MESSAGE_1001,tEx);
			}
			else{
				throw new ReplicationException(MessageConstants.DATA_PROCESSING_ERROR, tEx.getMessage(), tEx);
			}	
		}
		catch(Exception ex){
			Logger.repError(logInfo, "invokeDynamicTableQuery", "Exception while replicating data from "+srcTable+ " to "+dstTable, ex);
			
			throw new ReplicationException(ex.getMessage(),ex);
			
		}
		Logger.repDebug(logInfo, "invokeDynamicTableQuery", "End of replication for sourceTable " + srcTable + " and dstTable "+ dstTable + " replication process.");
		return copied;
	}//invokeDynamicTableQuery
	
}//class
