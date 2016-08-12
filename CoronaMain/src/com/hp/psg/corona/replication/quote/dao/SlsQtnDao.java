package com.hp.psg.corona.replication.quote.dao;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.hp.psg.common.util.logging.LoggerInfo;
import com.hp.psg.corona.common.util.Config;
import com.hp.psg.corona.replication.common.cache.query.QueryRepository;
import com.hp.psg.corona.replication.common.cache.query.ReplicationQueryManager;
import com.hp.psg.corona.replication.common.exception.CoronaException;
import com.hp.psg.corona.replication.common.exception.ReplicationException;
import com.hp.psg.corona.replication.common.message.MessageConstants;
import com.hp.psg.corona.replication.common.util.ReplicationUtility;
import com.hp.psg.corona.common.util.Logger;
import com.hp.psg.corona.replication.quote.QuoteConnection;
import com.hp.psg.corona.replication.quote.vo.SlsQtnVO;

/**
 * DAO for SLS_QTN table
 * @author rohitc
 *
 */
public class SlsQtnDao extends QuoteConnection implements IBaseDao{
	QueryRepository queryRepository = null;
	String dataSourceName = null;
	LoggerInfo logInfo=null;

	public SlsQtnDao(){
		queryRepository = ReplicationQueryManager.getInstance();
		dataSourceName = Config.getProperty("corona.replication.secondary.data.source.name");
		logInfo = new LoggerInfo (this.getClass().getName());
	}
	
	
	@Override
	public void create(Connection con, long slsQtnID, String slsQtnVrsn)
			throws ReplicationException {
		SlsQtnVO slsQtnVO = getSlsQtn(con,slsQtnID);
		if(slsQtnVO!=null){
			insertSlsQtn(slsQtnVO);
		}
	}

	@Override
	public void update(Connection con, long slsQtnID, String slsQtnVrsn)
			throws ReplicationException {
		SlsQtnVO slsQtnVO = getSlsQtn(con,slsQtnID);
		if(slsQtnVO!=null){
			updateSlsQtn(slsQtnID,slsQtnVO);
		}
	}

	@Override
	public void delete(Connection con, long slsQtnID, String slsQtnVrsn)
			throws ReplicationException {
		SlsQtnVO slsQtnVO = getSlsQtn(con,slsQtnID);
		if(slsQtnVO!=null){
			deleteSlsQtn(slsQtnID,slsQtnVO);
		}
		
	}

	@Override
	public void change(Connection con, long slsQtnID, String slsQtnVrsn)
			throws ReplicationException {
		Logger.repDebug(logInfo, "change", "Incorrectly called change for SLS_QTN TABLE");
	}

	public SlsQtnVO getSlsQtn(Connection con, long slsQtnID) throws ReplicationException{
		Logger.repDebug(logInfo, "getSlsQtn", "Begining of getting Sls quote details for quotation id "+ slsQtnID);
		SlsQtnVO slsQtnVO = null;
		String selectQuery = null;
		
		try {
			selectQuery = queryRepository.getQuery("Quote","Select","SlsQtn"); 
			List<Object> options = new ArrayList<Object>();
			options.add(String.valueOf(slsQtnID));

			ResultSet resultSet = runPreparedStatement(con, selectQuery, options);
			if (resultSet.next()){
				slsQtnVO = createVO(resultSet);
			}
		}catch(SQLException sqlEx){
			Logger.repError(logInfo, "getSlsQtn", "Exception while getting Sls quote details...", sqlEx);
			throw new ReplicationException(sqlEx.getMessage(), sqlEx);
		}catch (Exception e) {
			Logger.repError(logInfo, "getSlsQtn", "Exception while getting Sls quote details...", e);
			throw new ReplicationException(e.getMessage(), e);
		}
		Logger.repDebug(logInfo, "getSlsQtn", "End of getting Sls quote details for quotation id "+ slsQtnID);
		
		return slsQtnVO;
	}//getSlsQtn

	private SlsQtnVO createVO(ResultSet resultSet) throws ReplicationException {
		SlsQtnVO slsQtnVO = new SlsQtnVO();
		try{
			String bidInd = resultSet.getString("BID_IND");
			if( bidInd != null){
				slsQtnVO.setBidInd(bidInd.charAt(0));
			}
			slsQtnVO.setBidNr(resultSet.getString("BID_NR"));
			slsQtnVO.setBidTs(resultSet.getDate("BID_TS"));
			slsQtnVO.setCreatedBy(resultSet.getString("CREATED_BY"));
			slsQtnVO.setCreatedTs(resultSet.getDate("CREATED_TS"));
			slsQtnVO.setCustSpcfdQtNr(resultSet.getString("CUST_SPCFD_QT_NR"));
			slsQtnVO.setDeleteInd((resultSet.getString("DELETE_IND")).charAt(0));
			slsQtnVO.setDescription(resultSet.getString("DESCRIPTION"));
			slsQtnVO.setLastModifiedBy(resultSet.getString("LAST_MODIFIED_BY"));
			slsQtnVO.setLastModifiedTs(resultSet.getDate("LAST_MODIFIED_TS"));
			slsQtnVO.setName(resultSet.getString("NAME"));
			slsQtnVO.setSlsQtnID(resultSet.getLong("SLS_QTN_ID"));
		}catch(SQLException sqEx){
			Logger.repError(logInfo, "createVO", "Exception while creating VO for SlsQtn...", sqEx);
			throw new ReplicationException(sqEx.getMessage(), sqEx);
		}
		return slsQtnVO;
	}//createVO
	
	public void insertSlsQtn(SlsQtnVO slsQtnVO)throws ReplicationException{
		Logger.repDebug(logInfo, "insertSlsQtn", "Begining of inserting Sls quote details...");
		String insertQuery = null;
		Connection con = null;
		
		try {
			con = getConnection(dataSourceName);
			
			//check if quote id already exists or not
			boolean isExists = checkQuoteExists(con, slsQtnVO.getSlsQtnID());
			
			if( !isExists ){
			
				insertQuery = queryRepository.getQuery("Quote","Create","SlsQtn"); 
				List<Object> options = new ArrayList<Object>();
			
				options.add(slsQtnVO.getBidNr());
				options.add(slsQtnVO.getCreatedBy());
				options.add(slsQtnVO.getCustSpcfdQtNr());
				options.add(slsQtnVO.getDescription());
				options.add(slsQtnVO.getLastModifiedBy());
				options.add(slsQtnVO.getName());
				options.add(String.valueOf(slsQtnVO.getBidInd()));
				options.add(ReplicationUtility.dateString(slsQtnVO.getBidTs()));
				options.add(ReplicationUtility.dateString(slsQtnVO.getCreatedTs()));
				options.add(ReplicationUtility.dateString(slsQtnVO.getLastModifiedTs()));
				options.add(String.valueOf(slsQtnVO.getSlsQtnID()));
				options.add(String.valueOf(slsQtnVO.getDeleteInd()));
			
				executeUpdatePreparedStatement(con,insertQuery,options);
			}
			else{
				Logger.repDebug(logInfo, "insertSlsQtn", "Data already exists for qutoe id: " + slsQtnVO.getSlsQtnID() + ", it didn't insert again.");				
			}
		}
		catch (Exception ex) {
			Logger.repError(logInfo, "insertSlsQtn", "Exception while inserting Sls quote details...", ex);
			if( ex.getMessage().contains("ORA-00001")){
				throw new ReplicationException(MessageConstants.DATA_ALREADY_EXISTS,MessageConstants.IDS_MESSAGE_1001,ex);
			}else{
				throw new ReplicationException(MessageConstants.DATA_PROCESSING_ERROR, MessageConstants.IDS_MESSAGE_1000, ex);
			}	
			
			//throw new ReplicationException(ex.getMessage(),ex);
		}
		finally{
			closeConnection(con);
		}
		Logger.repDebug(logInfo, "insertSlsQtn", "End of inserting Sls quote details.");
			
	}//insertSlsQtn
	
	public void updateSlsQtn(long slsQtnID, SlsQtnVO slsQtnVO)throws ReplicationException{
		Logger.repDebug(logInfo, "updateSlsQtn", "Begining of updating Sls quote details for quotation id "+ slsQtnID);
		String updateQuery = null;
		Connection con = null;
		
		try {
			con = getConnection(dataSourceName);
						
			updateQuery = queryRepository.getQuery("Quote","Update","SlsQtn"); 
			List<Object> options = new ArrayList<Object>();
			
			options.add(slsQtnVO.getBidNr());
			options.add(slsQtnVO.getCreatedBy());
			options.add(slsQtnVO.getCustSpcfdQtNr());
			options.add(slsQtnVO.getDescription());
			options.add(slsQtnVO.getLastModifiedBy());
			options.add(slsQtnVO.getName());
			options.add(String.valueOf(slsQtnVO.getBidInd()));
			options.add(ReplicationUtility.dateString(slsQtnVO.getBidTs()));
			options.add(ReplicationUtility.dateString(slsQtnVO.getCreatedTs()));
			options.add(ReplicationUtility.dateString(slsQtnVO.getLastModifiedTs()));
			options.add(String.valueOf(slsQtnVO.getDeleteInd()));
			
			options.add(String.valueOf(slsQtnID)); //for where condition
			
			int rowUpdated = executeUpdatePreparedStatement(con,updateQuery,options);
			if(rowUpdated == 0){
				Logger.repDebug(logInfo, "updateQuote", "SLS_QTN doesn't exist for SLS_QTN_ID =  "+slsQtnID+ " .Inserting record now.");
				insertSlsQtn(slsQtnVO);
			}
		}
		catch (Exception ex) {
			Logger.repError(logInfo, "updateQuote", "Exception while updating Sls quote details...", ex);
			throw new ReplicationException(ex.getMessage(),ex);
		}
		finally{
			closeConnection(con);
		}
		Logger.repDebug(logInfo, "updateSlsQtn", "End of updating Sls quote details for quotation id "+ slsQtnID);
		
	}//updateSlsQtn
	
	public void deleteSlsQtn(long slsQtnID, SlsQtnVO slsQtnVO) throws ReplicationException{
		Logger.repDebug(logInfo, "deleteSlsQtn", "Begining of deleting Sls quote for quotation id "+ slsQtnID);
		
		updateSlsQtn(slsQtnID, slsQtnVO);
		
		Logger.repDebug(logInfo, "deleteSlsQtn", "End of deleting Sls quote for quotation id "+ slsQtnID);
	}//deleteSlsQtn
	
	
	/**
	 * Check if given quote id exists already or not.
	 * 
	 * @param con
	 * @param slsQtnId
	 * @return
	 * @throws ReplicationException
	 */
	private boolean checkQuoteExists(Connection con, long slsQtnId) throws ReplicationException{
		boolean isExists = false;
		String checkQuery = null;
		int numOfQtnIds = -1;
		
		Logger.repDebug(logInfo, "checkQuoteExists", "Begining of checking Sls quote id exists in db..."+ slsQtnId);
		
		try {
			checkQuery = queryRepository.getQuery("Quote","Select","CHECK_SLS_QTNID_EXIST"); 
			List<Object> options = new ArrayList<Object>();
			options.add(String.valueOf(slsQtnId));

			ResultSet resultSet = runPreparedStatement(con, checkQuery, options);
			if (resultSet.next()){
				numOfQtnIds = resultSet.getInt(1);
				if (numOfQtnIds > 0){
					isExists = true;
				}
			}
		}catch(SQLException sqlEx){
			Logger.repError(logInfo, "checkQuoteExists", "Exception while checking Sls quote id...", sqlEx);
			throw new ReplicationException(sqlEx.getMessage(), sqlEx);
		}catch (Exception e) {
			Logger.repError(logInfo, "checkQuoteExists", "Exception while checking Sls quote id...", e);
			throw new ReplicationException(e.getMessage(), e);
		}
		Logger.repDebug(logInfo, "checkQuoteExists", "End of checking Sls quote id exists in db "+ slsQtnId);
		
		return isExists;
	}//checkQuoteExists

}//class
