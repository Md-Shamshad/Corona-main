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
import com.hp.psg.corona.replication.common.exception.ReplicationException;
import com.hp.psg.corona.replication.common.util.ReplicationUtility;
import com.hp.psg.corona.common.util.Logger;
import com.hp.psg.corona.replication.quote.QuoteConnection;
import com.hp.psg.corona.replication.quote.vo.QuoteVO;

/**
 * DAO for QUOTE table
 * @author rohitc
 *
 */
public class QuoteDao extends QuoteConnection implements IBaseDao{
	QueryRepository queryRepository = null;
	String dataSourceName = null;
	LoggerInfo logInfo=null;

	public QuoteDao(){
		queryRepository = ReplicationQueryManager.getInstance();
		dataSourceName = Config.getProperty("corona.replication.secondary.data.source.name");
		logInfo = new LoggerInfo (this.getClass().getName());
	}
	
	@Override
	public void create(Connection con, long slsQtnID, String slsQtnVrsn)throws ReplicationException{
		QuoteVO quoteVO = getQuote(con,slsQtnID);
		if(quoteVO!=null){
			insertQuote(quoteVO);
		}
	}
	
	@Override
	public void update(Connection con, long slsQtnID, String slsQtnVrsn)throws ReplicationException{
		QuoteVO quoteVO = getQuote(con,slsQtnID);
		if(quoteVO!=null){
			updateQuote(slsQtnID,quoteVO);
		}
	}
	
	@Override
	public void delete(Connection con, long slsQtnID, String slsQtnVrsn)throws ReplicationException{
		QuoteVO quoteVO = getQuote(con,slsQtnID);
		if(quoteVO!=null){
			deleteQuote(slsQtnID,quoteVO);
		}
	}
	

	@Override
	public void change(Connection con, long slsQtnID, String slsQtnVrsn)
			throws ReplicationException {
		Logger.repDebug(logInfo, "change", "Incorrectly called change for QUOTE TABLE");
	}

	public QuoteVO getQuote(Connection con, long slsQtnID) throws ReplicationException{
		Logger.repDebug(logInfo, "getQuote", "Begining of getting quote details for quotation id "+ slsQtnID);
		
		QuoteVO quoteVO = null;
		String selectQuery = null;
				
		try {
			selectQuery = queryRepository.getQuery("Quote","Select","Quote"); 
			List<Object> options = new ArrayList<Object>();
			options.add(String.valueOf(slsQtnID));

			ResultSet resultSet = runPreparedStatement(con, selectQuery, options);
			if (resultSet.next()){
				quoteVO = createVO(resultSet);
			}
		}catch(SQLException sqlEx){
			Logger.repError(logInfo, "getQuote", "Exception while getting quote details...", sqlEx);
			throw new ReplicationException(sqlEx.getMessage(), sqlEx);
		}catch (Exception e) {
			Logger.repError(logInfo, "getQuote", "Exception while getting quote details...", e);
			throw new ReplicationException(e.getMessage(), e);
		}
		
		Logger.repDebug(logInfo, "getQuote", "End of getting quote details for quotation id "+ slsQtnID);
		
		return quoteVO;
	}
	
	
	//create record in Quote table
	public void insertQuote(QuoteVO quoteVO)throws ReplicationException{
		Logger.repDebug(logInfo, "insertQuote", "Begining of inserting quote details...");
		
		String insertQuery = null;
		Connection con = null;
		
		try {
			con = getConnection(dataSourceName);
			
			insertQuery = queryRepository.getQuery("Quote","Create","Quote"); 
			List<Object> options = new ArrayList<Object>();
			
			options.add(String.valueOf(quoteVO.getSlsQtnID())); 
			options.add(quoteVO.getCreationPersonID());
			options.add(String.valueOf(quoteVO.getDeleteInd()));
			options.add(ReplicationUtility.dateString(quoteVO.getCreateTimestamp()));
			options.add(quoteVO.getCreatedBy());
			options.add(ReplicationUtility.dateString(quoteVO.getLastModifiedTimestamp()));
			options.add(quoteVO.getLastModifiedBy());
			
			executeUpdatePreparedStatement(con,insertQuery,options);
		}
		catch (Exception ex) {
			Logger.repError(logInfo, "getQuote", "Exception while inserting quote details...", ex);
			throw new ReplicationException(ex.getMessage(),ex);
		}
		finally{
			closeConnection(con);
		}
		
		Logger.repDebug(logInfo, "insertQuote", "End of inserting quote details.");
	}

	//update complete record based on slsQtnID
	public void updateQuote(long slsQtnID, QuoteVO quoteVO) throws ReplicationException {
		Logger.repDebug(logInfo, "updateQuote", "Begining of updating quote details for quotation id "+ slsQtnID);
		
		String updateQuery = null;
		Connection con = null;
		
		try {
			con = getConnection(dataSourceName);
			
			updateQuery = queryRepository.getQuery("Quote","Update","Quote"); 
			List<Object> options = new ArrayList<Object>();

			options.add(quoteVO.getCreationPersonID());
			options.add(String.valueOf(quoteVO.getDeleteInd()));
			options.add(ReplicationUtility.dateString(quoteVO.getCreateTimestamp()));
			options.add(quoteVO.getCreatedBy());
			options.add(ReplicationUtility.dateString(quoteVO.getLastModifiedTimestamp()));
			options.add(quoteVO.getLastModifiedBy());
			
			options.add(String.valueOf(slsQtnID)); 

			int rowUpdated = executeUpdatePreparedStatement(con,updateQuery,options);
			if(rowUpdated == 0){
				Logger.repDebug(logInfo, "updateQuote", "QUOTE doesn't exist for SLS_QTN_ID =  "+slsQtnID+ " .Inserting record now.");
				insertQuote(quoteVO);
			}
		}
		catch (Exception ex) {
			Logger.repError(logInfo, "updateQuote", "Exception while getting quote details...", ex);
			throw new ReplicationException(ex.getMessage(),ex);
		}
		finally{
			closeConnection(con);
		}
		Logger.repDebug(logInfo, "updateQuote", "End of updating quote details for quotation id "+ slsQtnID);
			
	}//updateQuote
	

	//in case of delete copy complete record. #to be checked...do we need to update DELETE_IND column  
	public void deleteQuote(long slsQtnID, QuoteVO quoteVO) throws ReplicationException{
		Logger.repDebug(logInfo, "updateQuote", "Begining of deleting quote for quotation id "+ slsQtnID);
		
		updateQuote(slsQtnID, quoteVO);
		
		Logger.repDebug(logInfo, "updateQuote", "End of deleting quote for quotation id "+ slsQtnID);
	}//deleteQuote
	

	private QuoteVO createVO(ResultSet rs) throws ReplicationException {
		QuoteVO quoteVO = new QuoteVO();
		try{
			
			quoteVO.setCreatedBy(rs.getString("CREATED_BY"));
			quoteVO.setCreateTimestamp(rs.getTimestamp("CREATED_TS"));
			quoteVO.setCreationPersonID(rs.getString("CREATION_PERSON_ID"));
			quoteVO.setDeleteInd((rs.getString("DELETE_IND")).charAt(0));
			quoteVO.setLastModifiedBy(rs.getString("LAST_MODIFIED_BY"));
			quoteVO.setLastModifiedTimestamp(rs.getTimestamp("LAST_MODIFIED_TS"));
			quoteVO.setSlsQtnID(rs.getLong("SLS_QTN_ID"));
		
		}catch(SQLException sqEx){
			Logger.repError(logInfo, "createVO", "Exception while creating VO details...", sqEx);
			throw new ReplicationException(sqEx.getMessage(), sqEx);
		}

		return quoteVO;
	}
}//class
