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
import com.hp.psg.corona.replication.quote.vo.QuoteAttachmentsVO;

public class QuoteAttachmentsDao extends QuoteConnection implements IBaseDao {
	QueryRepository queryRepository = null;
	String dataSourceName = null;
	LoggerInfo logInfo=null;
	
	public QuoteAttachmentsDao() {
		queryRepository = ReplicationQueryManager.getInstance();
		dataSourceName = Config.getProperty("corona.replication.secondary.data.source.name");
		logInfo = new LoggerInfo (this.getClass().getName());
	}

	@Override
	public void create(Connection con, long slsQtnID, String slsQtnVrsn)
			throws ReplicationException {
		List<QuoteAttachmentsVO> quoteAttachments = getQuoteAttachments(con,slsQtnID,slsQtnVrsn);
		if(quoteAttachments!=null && quoteAttachments.size()>0){
			for(QuoteAttachmentsVO quoteAttachmentsVO : quoteAttachments){
				insertQuoteAttachments(quoteAttachmentsVO);
			}
		}
	}
	

	@Override
	public void update(Connection con, long slsQtnID, String slsQtnVrsn)
			throws ReplicationException {
		
		Connection dstCon = null;		
		try {
			dstCon = getConnection(dataSourceName);
		
			updateQuoteAttachementDeleteFlag(dstCon, slsQtnID, slsQtnVrsn);
		
			List<QuoteAttachmentsVO> quoteAttachments = getQuoteAttachments(con,slsQtnID,slsQtnVrsn);
			if(quoteAttachments!=null && quoteAttachments.size()>0){
				for(QuoteAttachmentsVO quoteAttachmentsVO : quoteAttachments){
					updateQuoteAttachments(dstCon, slsQtnID,slsQtnVrsn,quoteAttachmentsVO);
				}
			}
		
			removeDeletedQuoteAttachements(dstCon, slsQtnID, slsQtnVrsn);
		}
		catch (Exception ex) {
			Logger.repError(logInfo, "update", "Exception while updating QuoteAttachement details...", ex);
			throw new ReplicationException(ex.getMessage(),ex);
		}
		finally{
			closeConnection(dstCon);
		}
	}
	
	@Override
	public void delete(Connection con, long slsQtnID, String slsQtnVrsn)
			throws ReplicationException {
		List<QuoteAttachmentsVO> quoteAttachments = getQuoteAttachments(con,slsQtnID,slsQtnVrsn);
		if(quoteAttachments!=null && quoteAttachments.size()>0){
			Connection dstCon = null;		
			try {
				dstCon = getConnection(dataSourceName);
			
				for(QuoteAttachmentsVO quoteAttachmentsVO : quoteAttachments){
					deleteQuoteAttachments(dstCon, slsQtnID,slsQtnVrsn,quoteAttachmentsVO);
				}
			}
			catch (Exception ex) {
				Logger.repError(logInfo, "delete", "Exception while deleting QuoteAttachement details.", ex);
				throw new ReplicationException(ex.getMessage(),ex);
			}
			finally{
				closeConnection(dstCon);
			}
		}
	}
	
	@Override
	public void change(Connection con, long slsQtnID, String slsQtnVrsn)
			throws ReplicationException {
		Logger.repDebug(logInfo, "change", "Incorrectly called change for QUOTE_ATTACHMENTS TABLE");

	}

	private void insertQuoteAttachments(QuoteAttachmentsVO quoteAttachmentsVO) throws ReplicationException {
		Logger.repDebug(logInfo, "insertQuoteAttachments", "Begining of inserting quote Attachments details...");

		String insertQuery = null;
		Connection con = null;

		try {
			con = getConnection(dataSourceName);

			insertQuery = queryRepository.getQuery("Quote","Create","QuoteAttachments"); 
			List<Object> options = new ArrayList<Object>();
			
			options.add(String.valueOf(quoteAttachmentsVO.getSlsQtnId()));
			options.add(quoteAttachmentsVO.getSlsQtnVrsnSqnNr());
			options.add(String.valueOf(quoteAttachmentsVO.getAttachNr()));
			options.add(quoteAttachmentsVO.getFileNm());
			options.add(quoteAttachmentsVO.getDocDesc());
			options.add(String.valueOf(quoteAttachmentsVO.getDeleteInd()));
			options.add(ReplicationUtility.dateString(quoteAttachmentsVO.getCreatedTs()));
			options.add(quoteAttachmentsVO.getCreatedBy());
			options.add(ReplicationUtility.dateString(quoteAttachmentsVO.getLastModifiedTs()));
			options.add(quoteAttachmentsVO.getLastModifiedBy());
			
			executeUpdatePreparedStatement(con,insertQuery,options);
		}
		catch (Exception ex) {
			Logger.repError(logInfo, "insertQuoteAttachments", "Exception while inserting quote Attachments details...", ex);
			throw new ReplicationException(ex.getMessage(),ex);
		}
		finally{
			closeConnection(con);
		}
		Logger.repDebug(logInfo, "insertQuoteAttachments", "End of inserting quote Attachments details.");
		
	}

	private List<QuoteAttachmentsVO> getQuoteAttachments(Connection con,
			long slsQtnID, String slsQtnVrsn) throws ReplicationException {
		Logger.repDebug(logInfo, "getQuoteAttachments", "Begining of getting quote Attachments details for quotation id "+slsQtnID+
				" and sls version "+slsQtnVrsn);
		List<QuoteAttachmentsVO> quoteAttachments = new ArrayList<QuoteAttachmentsVO>();
		QuoteAttachmentsVO quoteAttachmentsVO = null;
		String selectQuery = null;

		try {
			selectQuery = queryRepository.getQuery("Quote","Select","QuoteAttachments"); 
			List<Object> options = new ArrayList<Object>();
			options.add(String.valueOf(slsQtnID));
			options.add(slsQtnVrsn);

			ResultSet resultSet = runPreparedStatement(con, selectQuery, options);
			while (resultSet.next()){
				quoteAttachmentsVO = createVO(resultSet);
				quoteAttachments.add(quoteAttachmentsVO);
			}
		}catch(SQLException sqlEx){
			Logger.repError(logInfo, "getQuoteAttachments", "Exception while getting quote Attachments details...", sqlEx);
			throw new ReplicationException(sqlEx.getMessage(), sqlEx);
		}catch (Exception e) {
			Logger.repError(logInfo, "getQuoteAttachments", "Exception while getting quote Attachments details...", e);
			throw new ReplicationException(e.getMessage(), e);
		}
		Logger.repDebug(logInfo, "getQuoteAttachments", "End of getting quote Attachments details for quotation id "+ slsQtnID+
				" and sls version "+slsQtnVrsn);

		return quoteAttachments;
	}

	private QuoteAttachmentsVO createVO(ResultSet resultSet) throws ReplicationException {
		QuoteAttachmentsVO quoteAttachmentsVO = new QuoteAttachmentsVO();
		try {
			
			quoteAttachmentsVO.setAttachNr(resultSet.getLong("ATTACH_NR"));
			quoteAttachmentsVO.setCreatedBy(resultSet.getString("CREATED_BY"));
			quoteAttachmentsVO.setCreatedTs(resultSet.getTimestamp("CREATED_TS"));
			quoteAttachmentsVO.setDeleteInd((resultSet.getString("DELETE_IND")).charAt(0));
			quoteAttachmentsVO.setDocDesc(resultSet.getString("DOC_DESC"));
			quoteAttachmentsVO.setFileNm(resultSet.getString("FILE_NM"));
			quoteAttachmentsVO.setLastModifiedBy(resultSet.getString("LAST_MODIFIED_BY"));
			quoteAttachmentsVO.setLastModifiedTs(resultSet.getTimestamp("LAST_MODIFIED_TS"));
			quoteAttachmentsVO.setSlsQtnId(resultSet.getLong("SLS_QTN_ID"));
			quoteAttachmentsVO.setSlsQtnVrsnSqnNr(resultSet.getString("SLS_QTN_VRSN_SQN_NR"));
		
		} catch (SQLException e) {
			Logger.repError(logInfo, "createVO", "Exception while creating VO for QuoteAttachments...", e);
			throw new ReplicationException(e.getMessage(), e);
		}
		
		return quoteAttachmentsVO;
	}


	private void updateQuoteAttachments(Connection con, long slsQtnID, String slsQtnVrsn,
			QuoteAttachmentsVO quoteAttachmentsVO) throws ReplicationException {
		Logger.repDebug(logInfo, "updateQuoteAttachments", "Begining of updating quote Attachments details for quotation id "+ slsQtnID);
		String updateQuery = null;
	
		try {
			updateQuery = queryRepository.getQuery("Quote","Update","QuoteAttachments"); 
			List<Object> options = new ArrayList<Object>();
			
			options.add(quoteAttachmentsVO.getFileNm());
			options.add(quoteAttachmentsVO.getDocDesc());
			options.add(String.valueOf(quoteAttachmentsVO.getDeleteInd()));
			options.add(ReplicationUtility.dateString(quoteAttachmentsVO.getCreatedTs()));
			options.add(quoteAttachmentsVO.getCreatedBy());
			options.add(ReplicationUtility.dateString(quoteAttachmentsVO.getLastModifiedTs()));
			options.add(quoteAttachmentsVO.getLastModifiedBy());
			
			options.add(String.valueOf(slsQtnID));
			options.add(slsQtnVrsn);
			options.add(String.valueOf(quoteAttachmentsVO.getAttachNr()));

			int rowUpdated = executeUpdatePreparedStatement(con,updateQuery,options);
			if(rowUpdated == 0){
				Logger.repDebug(logInfo, "updateQuoteAttachments", "Quote attachment doesn't exist for SLS_QTN_ID =  "+slsQtnID+ " and SLS_QTN_VRSN = "
						+slsQtnVrsn+" .Inserting record now.");
				insertQuoteAttachments(quoteAttachmentsVO);
			}
		}
		catch (Exception ex) {
			Logger.repError(logInfo, "updateQuoteAttachments", "Exception while updating quote attachment details...", ex);
			throw new ReplicationException(ex.getMessage(),ex);
		}
		
		Logger.repDebug(logInfo, "updateQuoteAttachments", "End of updating quote attachment details for quotation id "+ slsQtnID);
		
	}

	private void deleteQuoteAttachments(Connection con, long slsQtnID, String slsQtnVrsn,
			QuoteAttachmentsVO quoteAttachmentsVO) throws ReplicationException {
			updateQuoteAttachments(con, slsQtnID, slsQtnVrsn, quoteAttachmentsVO);		
	}
	
	private void updateQuoteAttachementDeleteFlag(Connection con, long slsQtnID,
			String slsQtnVrsn) throws ReplicationException {
		String query = null;
				
		try {
			query = queryRepository.getQuery("Quote","Update","QuoteAttachementDeleteFlag"); 
			List<Object> options = new ArrayList<Object>();
			options.add(String.valueOf(slsQtnID));
			options.add(slsQtnVrsn);
			
			executeUpdatePreparedStatement(con,query,options);
		}
		catch (Exception ex) {
			Logger.repError(logInfo, "updateQuoteAttachementDeleteFlag", "Exception while updating QuoteAttachementDeleteFlag...", ex);
			throw new ReplicationException(ex.getMessage(),ex);
		}
				
		Logger.repDebug(logInfo, "updateQuoteAttachementDeleteFlag", "End of updating QuoteAttachementDeleteFlag for quotation id "+ slsQtnID +" and version "+slsQtnVrsn);
		
	}

	private void removeDeletedQuoteAttachements(Connection con, long slsQtnID, String slsQtnVrsn) throws ReplicationException {
		String query = null;
		try {
			query = queryRepository.getQuery("Quote","Delete","DeleteQuoteAttachements"); 
			List<Object> options = new ArrayList<Object>();
			options.add(String.valueOf(slsQtnID));
			options.add(slsQtnVrsn);
			
			executeUpdatePreparedStatement(con,query,options);
		}
		catch (Exception ex) {
			Logger.repError(logInfo, "removeDeletedQuoteAttachements", "Exception while removing DeleteQuoteAttachements...", ex);
			throw new ReplicationException(ex.getMessage(),ex);
		}
				
		Logger.repDebug(logInfo, "removeDeletedQuoteAttachements", "End of removing DeleteQuoteAttachements for quotation id "+ slsQtnID + " and version "+slsQtnVrsn);	
	}
	
}
