package com.hp.psg.corona.replication.quote.dao;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.hp.psg.common.util.logging.LoggerInfo;
import com.hp.psg.corona.common.util.Config;
import com.hp.psg.corona.common.util.Logger;
import com.hp.psg.corona.replication.common.cache.query.QueryRepository;
import com.hp.psg.corona.replication.common.cache.query.ReplicationQueryManager;
import com.hp.psg.corona.replication.common.exception.ReplicationException;
import com.hp.psg.corona.replication.common.util.ReplicationUtility;
import com.hp.psg.corona.replication.quote.QuoteConnection;
import com.hp.psg.corona.replication.quote.vo.QuoteItemCommentVO;

public class QuoteItemCommentDao extends QuoteConnection implements IBaseDao {
	QueryRepository queryRepository = null;
	String dataSourceName = null;
	LoggerInfo logInfo=null;


	public QuoteItemCommentDao() {
		queryRepository = ReplicationQueryManager.getInstance();
		dataSourceName = Config.getProperty("corona.replication.secondary.data.source.name");
		logInfo = new LoggerInfo (this.getClass().getName());
	}

	@Override
	public void create(Connection con, long slsQtnID, String slsQtnVrsn)
	throws ReplicationException {
		List<QuoteItemCommentVO> quoteItemComments = getQuoteItemComments(con,slsQtnID,slsQtnVrsn);
		if(quoteItemComments!=null && quoteItemComments.size()>0){
			for(QuoteItemCommentVO quoteItemCommentVO : quoteItemComments){
				insertQuoteItemComment(quoteItemCommentVO);
			}
		}

	}

	@Override
	public void update(Connection con, long slsQtnID, String slsQtnVrsn)
	throws ReplicationException {
		
		Connection dstCon = null;		
		try {
			dstCon = getConnection(dataSourceName);
			
			updateQuoteItemCommentDeleteFlag(dstCon, slsQtnID, slsQtnVrsn);
		
			List<QuoteItemCommentVO> quoteItemComments = getQuoteItemComments(con,slsQtnID,slsQtnVrsn);
			if(quoteItemComments!=null && quoteItemComments.size()>0){
				for(QuoteItemCommentVO quoteItemCommentVO : quoteItemComments){
					updateQuoteItemComment(dstCon, slsQtnID, slsQtnVrsn, quoteItemCommentVO);
				}
			}
			removeDeletedQuoteItemComments(dstCon, slsQtnID, slsQtnVrsn);
		}
		catch (Exception ex) {
			Logger.repError(logInfo, "update", "Exception while updating QuoteItemComments detail...", ex);
			throw new ReplicationException(ex.getMessage(),ex);
		}
		finally{
			closeConnection(dstCon);
		}
	}

	@Override
	public void delete(Connection con, long slsQtnID, String slsQtnVrsn)
	throws ReplicationException {
		List<QuoteItemCommentVO> quoteItemComments = getQuoteItemComments(con,slsQtnID,slsQtnVrsn);
		
		Connection dstCon = null;
		try{			
			if(quoteItemComments!=null && quoteItemComments.size()>0){
				dstCon = getConnection(dataSourceName);
				
				for(QuoteItemCommentVO quoteItemCommentVO : quoteItemComments){
					deleteQuoteItemComment(dstCon, slsQtnID, slsQtnVrsn, quoteItemCommentVO);
				}
			}
		}
		catch (Exception ex) {
			Logger.repError(logInfo, "update", "Exception while updating QuoteItemComments detail...", ex);
			throw new ReplicationException(ex.getMessage(),ex);
		}
		finally{
			closeConnection(dstCon);
		}

	}

	@Override
	public void change(Connection con, long slsQtnID, String slsQtnVrsn)
	throws ReplicationException {
		Logger.repDebug(logInfo, "change", "Incorrectly called change for QUOTE_ITEM_COMMENT TABLE");

	}

	private void insertQuoteItemComment(QuoteItemCommentVO quoteItemCommentVO) throws ReplicationException {
		Logger.repDebug(logInfo, "insertQuoteItemComment", "Begining of inserting QuoteItemComment details...");

		String insertQuery = null;
		Connection con = null;

		try {
			con = getConnection(dataSourceName);

			insertQuery = queryRepository.getQuery("Quote","Create","QuoteItemComment"); 
			List<Object> options = new ArrayList<Object>();
			
			options.add(quoteItemCommentVO.getSlsQtnItmSqnNr());
			options.add(String.valueOf(quoteItemCommentVO.getSlsQtnId()));
			options.add(quoteItemCommentVO.getSlsQtnVrsnSqnNr());
			options.add(String.valueOf(quoteItemCommentVO.getCmntId()));
			options.add(quoteItemCommentVO.getCmntTextMemo());
			options.add(String.valueOf(quoteItemCommentVO.getDeleteInd()));
			options.add(ReplicationUtility.dateString(quoteItemCommentVO.getCreatedTs()));
			options.add(quoteItemCommentVO.getCreatedBy());
			options.add(ReplicationUtility.dateString(quoteItemCommentVO.getLastModifiedTs()));
			options.add(quoteItemCommentVO.getLastModifiedBy());
		
			executeUpdatePreparedStatement(con,insertQuery,options);
		}
		catch (Exception ex) {
			Logger.repError(logInfo, "insertQuoteItemComment", "Exception while inserting QuoteItemComment details...", ex);
			throw new ReplicationException(ex.getMessage(),ex);
		}
		finally{
			closeConnection(con);
		}
		Logger.repDebug(logInfo, "insertQuoteItemComment", "End of inserting QuoteItemComment details.");


	}

	private List<QuoteItemCommentVO> getQuoteItemComments(Connection con,
			long slsQtnID, String slsQtnVrsn) throws ReplicationException {
		Logger.repDebug(logInfo, "getQuoteItemComments", "Begining of getting QuoteItemComments details for quotation id "+ slsQtnID+
				" and sls version "+slsQtnVrsn);
		List<QuoteItemCommentVO> quoteItemComments = new ArrayList<QuoteItemCommentVO>();
		QuoteItemCommentVO quoteItemCommentVO = null;
		String selectQuery = null;

		try {
			selectQuery = queryRepository.getQuery("Quote","Select","QuoteItemComment"); 
			List<Object> options = new ArrayList<Object>();
			options.add(String.valueOf(slsQtnID));
			options.add(slsQtnVrsn);

			ResultSet resultSet = runPreparedStatement(con, selectQuery, options);
			while (resultSet.next()){
				quoteItemCommentVO = createVO(resultSet);
				quoteItemComments.add(quoteItemCommentVO);
			}
		}catch(SQLException sqlEx){
			Logger.repError(logInfo, "getQuoteItemComments", "Exception while getting QuoteItemComments details...", sqlEx);
			throw new ReplicationException(sqlEx.getMessage(), sqlEx);
		}catch (Exception e) {
			Logger.repError(logInfo, "getQuoteItemComments", "Exception while getting QuoteItemComments details...", e);
			throw new ReplicationException(e.getMessage(), e);
		}

		Logger.repDebug(logInfo, "getQuoteItemComments", "End of getting QuoteItemComments details for quotation id "+ slsQtnID+
				" and sls version "+slsQtnVrsn);
		return quoteItemComments;
	}


	private QuoteItemCommentVO createVO(ResultSet resultSet) throws ReplicationException {
		QuoteItemCommentVO quoteItemCommentVO = new QuoteItemCommentVO();

		try{
			quoteItemCommentVO.setCmntId(resultSet.getInt("CMNT_ID"));
			quoteItemCommentVO.setCmntTextMemo(resultSet.getString("CMNT_TEXT_MEMO"));
			quoteItemCommentVO.setCreatedBy(resultSet.getString("CREATED_BY"));
			quoteItemCommentVO.setCreatedTs(resultSet.getTimestamp("CREATED_TS"));
			quoteItemCommentVO.setDeleteInd((resultSet.getString("DELETE_IND")).charAt(0));
			quoteItemCommentVO.setLastModifiedBy(resultSet.getString("LAST_MODIFIED_BY"));
			quoteItemCommentVO.setLastModifiedTs(resultSet.getTimestamp("LAST_MODIFIED_TS"));
			quoteItemCommentVO.setSlsQtnId(resultSet.getLong("SLS_QTN_ID"));
			quoteItemCommentVO.setSlsQtnItmSqnNr(resultSet.getString("SLS_QTN_ITM_SQN_NR"));
			quoteItemCommentVO.setSlsQtnVrsnSqnNr(resultSet.getString("SLS_QTN_ITM_SQN_NR"));
			
		}catch(SQLException sqlEx){
			Logger.repError(logInfo, "createVO", "Exception while creating QuoteItemCommentVO...", sqlEx);
			throw new ReplicationException(sqlEx.getMessage(), sqlEx);
		}

		return quoteItemCommentVO;
	}

	private void updateQuoteItemComment(Connection con, long slsQtnID, String slsQtnVrsn,
			QuoteItemCommentVO quoteItemCommentVO) throws ReplicationException {
		Logger.repDebug(logInfo, "updateQuoteItemComment", "Begining of updating QuoteItemComment details for quotation id "+ slsQtnID+
				" and sls version "+slsQtnVrsn);
		String updateQuery = null;
		try {
			updateQuery = queryRepository.getQuery("Quote","Update","QuoteItemComment"); 
			List<Object> options = new ArrayList<Object>();
			
			options.add(String.valueOf(quoteItemCommentVO.getCmntId()));
			options.add(quoteItemCommentVO.getCmntTextMemo());
			options.add(String.valueOf(quoteItemCommentVO.getDeleteInd()));
			options.add(ReplicationUtility.dateString(quoteItemCommentVO.getCreatedTs()));
			options.add(quoteItemCommentVO.getCreatedBy());
			options.add(ReplicationUtility.dateString(quoteItemCommentVO.getLastModifiedTs()));
			options.add(quoteItemCommentVO.getLastModifiedBy());
			
			//for where conditions
			options.add(String.valueOf(slsQtnID));
			options.add(slsQtnVrsn);
			options.add(quoteItemCommentVO.getSlsQtnItmSqnNr());

			int rowUpdated = executeUpdatePreparedStatement(con,updateQuery,options);
			if(rowUpdated == 0){
				Logger.repDebug(logInfo, "updateQuoteItemComment", "QuoteItemComment doesn't exist for SLS_QTN_ID =  "+slsQtnID+ " and SLS_QTN_VRSN = "
						+slsQtnVrsn+" .Inserting record now.");
				insertQuoteItemComment(quoteItemCommentVO);
			}
		}
		catch (Exception ex) {
			Logger.repError(logInfo, "updateQuoteItemComment", "Exception while updating QuoteItemComment details...", ex);
			throw new ReplicationException(ex.getMessage(),ex);
		}
		
		Logger.repDebug(logInfo, "updateQuoteItemComment", "End of updating QuoteItemComment details for quotation id "+ slsQtnID+" and sls version "+slsQtnVrsn);

	}

	private void deleteQuoteItemComment(Connection con, long slsQtnID, String slsQtnVrsn,
			QuoteItemCommentVO quoteItemCommentVO) throws ReplicationException {
		updateQuoteItemComment(con, slsQtnID, slsQtnVrsn, quoteItemCommentVO);

	}
	
	private void updateQuoteItemCommentDeleteFlag(Connection con, long slsQtnID,
			String slsQtnVrsn) throws ReplicationException {
		String query = null;
		try {
			query = queryRepository.getQuery("Quote","Update","QuoteItemCommentDeleteFlag"); 
			List<Object> options = new ArrayList<Object>();
			options.add(String.valueOf(slsQtnID));
			options.add(slsQtnVrsn);
			
			executeUpdatePreparedStatement(con,query,options);
		}
		catch (Exception ex) {
			Logger.repError(logInfo, "updateQuoteItemCommentDeleteFlag", "Exception while updating QuoteItemCommentDeleteFlag...", ex);
			throw new ReplicationException(ex.getMessage(),ex);
		}
				
		Logger.repDebug(logInfo, "updateQuoteItemCommentDeleteFlag", "End of updating QuoteItemCommentDeleteFlag for quotation id "+ slsQtnID +" and version "+slsQtnVrsn);
		
	}

	private void removeDeletedQuoteItemComments(Connection con, long slsQtnID, String slsQtnVrsn) throws ReplicationException {
		String query = null;
		try {
			query = queryRepository.getQuery("Quote","Delete","DeleteQuoteItemComments"); 
			List<Object> options = new ArrayList<Object>();
			options.add(String.valueOf(slsQtnID));
			options.add(slsQtnVrsn);
			
			executeUpdatePreparedStatement(con,query,options);
		}
		catch (Exception ex) {
			Logger.repError(logInfo, "removeDeletedQuoteItemComments", "Exception while removing DeleteQuoteItemComments...", ex);
			throw new ReplicationException(ex.getMessage(),ex);
		}
			
		Logger.repDebug(logInfo, "removeDeletedQuoteItemComments", "End of removing DeleteQuoteItemComments for quotation id "+ slsQtnID + " and version "+slsQtnVrsn);
		
	}

}
