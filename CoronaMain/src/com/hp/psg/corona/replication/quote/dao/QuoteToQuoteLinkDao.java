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
import com.hp.psg.corona.replication.quote.vo.QuoteToQuoteLinkVO;

public class QuoteToQuoteLinkDao extends QuoteConnection implements IBaseDao {
	QueryRepository queryRepository = null;
	String dataSourceName = null;
	LoggerInfo logInfo=null;



	public QuoteToQuoteLinkDao() {
		queryRepository = ReplicationQueryManager.getInstance();
		dataSourceName = Config.getProperty("corona.replication.secondary.data.source.name");
		logInfo = new LoggerInfo (this.getClass().getName());
	}

	@Override
	public void create(Connection con, long slsQtnID, String slsQtnVrsn)
	throws ReplicationException {
		List<QuoteToQuoteLinkVO> quoteToQuoteLinks = getQuoteToQuoteLinks(con,slsQtnID,slsQtnVrsn);
		if(quoteToQuoteLinks!=null && quoteToQuoteLinks.size()>0){
			for(QuoteToQuoteLinkVO quoteToQuoteLinkVO : quoteToQuoteLinks){
				insertQuoteToQuoteLinks(quoteToQuoteLinkVO);
			}
		}

	}

	private void insertQuoteToQuoteLinks(QuoteToQuoteLinkVO quoteToQuoteLinkVO) throws ReplicationException {
		Logger.repDebug(logInfo, "insertQuoteToQuoteLinks", "Begining of inserting QuoteToQuoteLinks details...");

		String insertQuery = null;
		Connection con = null;

		try {
			con = getConnection(dataSourceName);

			insertQuery = queryRepository.getQuery("Quote","Create","QuoteToQuoteLink"); 
			List<Object> options = new ArrayList<Object>();

			options.add(String.valueOf(quoteToQuoteLinkVO.getSlsQtnId()));
			options.add(quoteToQuoteLinkVO.getSlsQtnVrsnSqnNr());
			options.add(quoteToQuoteLinkVO.getQtnSource());
			options.add(String.valueOf(quoteToQuoteLinkVO.getLnkSlsQtnId()));
			options.add(quoteToQuoteLinkVO.getLnkSlsQtnVrsnSqnNr());
			options.add(quoteToQuoteLinkVO.getLnkQtnSource());
			options.add(String.valueOf(quoteToQuoteLinkVO.getUpdateInd()));
			options.add(String.valueOf(quoteToQuoteLinkVO.getDeleteInd()));
			options.add(ReplicationUtility.dateString(quoteToQuoteLinkVO.getCreatedDate()));
			options.add(quoteToQuoteLinkVO.getCreatedBy());
			options.add(ReplicationUtility.dateString(quoteToQuoteLinkVO.getLastModifiedDate()));
			options.add(quoteToQuoteLinkVO.getLastModifiedBy());

			executeUpdatePreparedStatement(con,insertQuery,options);
		}
		catch (Exception ex) {
			Logger.repError(logInfo, "insertQuoteToQuoteLinks", "Exception while inserting QuoteToQuoteLinks details...", ex);
			throw new ReplicationException(ex.getMessage(),ex);
		}
		finally{
			closeConnection(con);
		}
		Logger.repDebug(logInfo, "insertQuoteToQuoteLinks", "End of inserting QuoteToQuoteLinks details.");

	}

	private List<QuoteToQuoteLinkVO> getQuoteToQuoteLinks(Connection con,
			long slsQtnID, String slsQtnVrsn) throws ReplicationException {
		Logger.repDebug(logInfo, "getQuoteToQuoteLinks", "Begining of getting QuoteToQuoteLinks details for quotation id "+ slsQtnID+
				" and sls version "+slsQtnVrsn);
		List<QuoteToQuoteLinkVO> quoteToQuoteLinks = new ArrayList<QuoteToQuoteLinkVO>();
		QuoteToQuoteLinkVO quoteToQuoteLinkVO = null;
		String selectQuery = null;

		try {
			selectQuery = queryRepository.getQuery("Quote","Select","QuoteToQuoteLink"); 
			List<Object> options = new ArrayList<Object>();
			options.add(String.valueOf(slsQtnID));
			options.add(slsQtnVrsn);

			ResultSet resultSet = runPreparedStatement(con, selectQuery, options);
			while (resultSet.next()){
				quoteToQuoteLinkVO = createVO(resultSet);
				quoteToQuoteLinks.add(quoteToQuoteLinkVO);
			}
		}catch(SQLException sqlEx){
			Logger.repError(logInfo, "getQuoteToQuoteLinks", "Exception while getting QuoteToQuoteLinks details...", sqlEx);
			throw new ReplicationException(sqlEx.getMessage(), sqlEx);
		}catch (Exception e) {
			Logger.repError(logInfo, "getQuoteToQuoteLinks", "Exception while getting QuoteToQuoteLinks details...", e);
			throw new ReplicationException(e.getMessage(), e);
		}

		Logger.repDebug(logInfo, "getQuoteToQuoteLinks", "End of getting QuoteToQuoteLinks details for quotation id "+ slsQtnID+
				" and sls version "+slsQtnVrsn);
		return quoteToQuoteLinks;
	}

	private QuoteToQuoteLinkVO createVO(ResultSet resultSet) throws ReplicationException {
		QuoteToQuoteLinkVO quoteToQuoteLinkVO = new QuoteToQuoteLinkVO(); 

		try{
			quoteToQuoteLinkVO.setCreatedBy(resultSet.getString("CREATED_BY"));
			quoteToQuoteLinkVO.setCreatedDate(resultSet.getTimestamp("CREATED_DATE"));
			quoteToQuoteLinkVO.setDeleteInd((resultSet.getString("DELETE_IND")).charAt(0));
			quoteToQuoteLinkVO.setLastModifiedBy(resultSet.getString("LAST_MODIFIED_BY"));
			quoteToQuoteLinkVO.setLastModifiedDate(resultSet.getTimestamp("LAST_MODIFIED_DATE"));
			quoteToQuoteLinkVO.setLnkQtnSource(resultSet.getString("LNK_QTN_SOURCE"));
			quoteToQuoteLinkVO.setLnkSlsQtnId(resultSet.getLong("LNK_SLS_QTN_ID"));
			quoteToQuoteLinkVO.setLnkSlsQtnVrsnSqnNr(resultSet.getString("LNK_SLS_QTN_VRSN_SQN_NR"));
			quoteToQuoteLinkVO.setQtnSource(resultSet.getString("QTN_SOURCE"));
			quoteToQuoteLinkVO.setSlsQtnId(resultSet.getLong("SLS_QTN_ID"));
			quoteToQuoteLinkVO.setSlsQtnVrsnSqnNr(resultSet.getString("SLS_QTN_VRSN_SQN_NR"));
			String updateInd = resultSet.getString("UPDATE_IND");
			if(updateInd!=null)
				quoteToQuoteLinkVO.setUpdateInd(updateInd.charAt(0));
		}catch(SQLException sqlEx){
			Logger.repError(logInfo, "createVO", "Exception while creating QuoteToQuoteLinkVO...", sqlEx);
			throw new ReplicationException(sqlEx.getMessage(), sqlEx);
		}



		return quoteToQuoteLinkVO;
	}

	@Override
	public void update(Connection con, long slsQtnID, String slsQtnVrsn)
	throws ReplicationException {
		Connection dstCon = null;

		try {
			dstCon = getConnection(dataSourceName);
			updateQuoteToQuoteLinkDeleteFlag(dstCon, slsQtnID, slsQtnVrsn);

			List<QuoteToQuoteLinkVO> quoteToQuoteLinks = getQuoteToQuoteLinks(con,slsQtnID,slsQtnVrsn);
			if(quoteToQuoteLinks!=null && quoteToQuoteLinks.size()>0){
				for(QuoteToQuoteLinkVO quoteToQuoteLinkVO : quoteToQuoteLinks){
					updateQuoteToQuoteLinks(dstCon,slsQtnID, slsQtnVrsn, quoteToQuoteLinkVO);
				}
			}
			removeDeletedQuoteToQuoteLinks(dstCon,slsQtnID, slsQtnVrsn);
		}
		catch (Exception ex) {
			Logger.repError(logInfo, "update", "Exception while updating quote links details...", ex);
			throw new ReplicationException(ex.getMessage(),ex);
		}
		finally{
			closeConnection(dstCon);
		}
	}

	private void updateQuoteToQuoteLinks(Connection con, long slsQtnID, String slsQtnVrsn,
			QuoteToQuoteLinkVO quoteToQuoteLinkVO) throws ReplicationException {
		Logger.repDebug(logInfo, "updateQuoteToQuoteLinks", "Begining of updating QuoteToQuoteLinks details for quotation id "+ slsQtnID+
				" and sls version "+slsQtnVrsn);
		String updateQuery = null;
		try {
			updateQuery = queryRepository.getQuery("Quote","Update","QuoteToQuoteLink"); 
			List<Object> options = new ArrayList<Object>();

			options.add(quoteToQuoteLinkVO.getQtnSource());
			options.add(quoteToQuoteLinkVO.getLnkQtnSource());
			options.add(String.valueOf(quoteToQuoteLinkVO.getUpdateInd()));
			options.add(String.valueOf(quoteToQuoteLinkVO.getDeleteInd()));
			options.add(ReplicationUtility.dateString(quoteToQuoteLinkVO.getCreatedDate()));
			options.add(quoteToQuoteLinkVO.getCreatedBy());
			options.add(ReplicationUtility.dateString(quoteToQuoteLinkVO.getLastModifiedDate()));
			options.add(quoteToQuoteLinkVO.getLastModifiedBy());

			//for where condition
			options.add(String.valueOf(slsQtnID));
			options.add(slsQtnVrsn);
			options.add(String.valueOf(quoteToQuoteLinkVO.getLnkSlsQtnId()));
			options.add(quoteToQuoteLinkVO.getLnkSlsQtnVrsnSqnNr());

			int rowUpdated = executeUpdatePreparedStatement(con,updateQuery,options);
			if(rowUpdated == 0){
				Logger.repDebug(logInfo, "updateQuoteToQuoteLinks", "QuoteToQuoteLinks doesn't exist for SLS_QTN_ID =  "+slsQtnID+ " and SLS_QTN_VRSN = "
						+slsQtnVrsn+" .Inserting record now.");
				insertQuoteToQuoteLinks(quoteToQuoteLinkVO);
			}
		}
		catch (Exception ex) {
			Logger.repError(logInfo, "updateQuoteToQuoteLinks", "Exception while updating QuoteToQuoteLinks details...", ex);
			throw new ReplicationException(ex.getMessage(),ex);
		}
		Logger.repDebug(logInfo, "updateQuoteToQuoteLinks", "End of updating QuoteToQuoteLinks details for quotation id "+ slsQtnID+
				" and sls version "+slsQtnVrsn);
	}

	@Override
	public void delete(Connection con, long slsQtnID, String slsQtnVrsn)
	throws ReplicationException {
		List<QuoteToQuoteLinkVO> quoteToQuoteLinks = getQuoteToQuoteLinks(con,slsQtnID,slsQtnVrsn);
		if(quoteToQuoteLinks!=null && quoteToQuoteLinks.size()>0){
			Connection dstCon = null;
			try {
				dstCon = getConnection(dataSourceName);
				for(QuoteToQuoteLinkVO quoteToQuoteLinkVO : quoteToQuoteLinks){
					deleteQuoteToQuoteLinks(dstCon, slsQtnID, slsQtnVrsn, quoteToQuoteLinkVO);
				}
			}
			catch (Exception ex) {
				Logger.repError(logInfo, "delete", "Exception while deleting quote links details...", ex);
				throw new ReplicationException(ex.getMessage(),ex);
			}
			finally{
				closeConnection(dstCon);
			}
		}

	}

	private void deleteQuoteToQuoteLinks(Connection dstCon, long slsQtnID, String slsQtnVrsn,
			QuoteToQuoteLinkVO quoteToQuoteLinkVO) throws ReplicationException {
		updateQuoteToQuoteLinks(dstCon, slsQtnID, slsQtnVrsn, quoteToQuoteLinkVO);

	}

	@Override
	public void change(Connection con, long slsQtnID, String slsQtnVrsn)
	throws ReplicationException {
		Logger.repDebug(logInfo, "change", "Incorrectly called change for QUOTE_TO_QUOTE_LINKS TABLE");

	}

	private void updateQuoteToQuoteLinkDeleteFlag(Connection con, long slsQtnID,
			String slsQtnVrsn) throws ReplicationException {
		String query = null;
		try {
			query = queryRepository.getQuery("Quote","Update","QuoteToQuoteLinkDeleteFlag"); 
			List<Object> options = new ArrayList<Object>();
			options.add(String.valueOf(slsQtnID));
			options.add(slsQtnVrsn);

			executeUpdatePreparedStatement(con,query,options);
		}
		catch (Exception ex) {
			Logger.repError(logInfo, "updateQuoteToQuoteLinkDeleteFlag", "Exception while updating QuoteToQuoteLinkDeleteFlag...", ex);
			throw new ReplicationException(ex.getMessage(),ex);
		}
		Logger.repDebug(logInfo, "updateQuoteToQuoteLinkDeleteFlag", "End of updating QuoteToQuoteLinkDeleteFlag for quotation id "+ slsQtnID +" and version "
				+slsQtnVrsn);

	}

	private void removeDeletedQuoteToQuoteLinks(Connection con,long slsQtnID, String slsQtnVrsn) throws ReplicationException {
		String query = null;
		try {
			query = queryRepository.getQuery("Quote","Delete","DeleteQuoteToQuoteLinks"); 
			List<Object> options = new ArrayList<Object>();
			options.add(String.valueOf(slsQtnID));
			options.add(slsQtnVrsn);

			executeUpdatePreparedStatement(con,query,options);
		}
		catch (Exception ex) {
			Logger.repError(logInfo, "removeDeletedQuoteToQuoteLinks", "Exception while removing DeleteQuoteToQuoteLinks...", ex);
			throw new ReplicationException(ex.getMessage(),ex);
		}

		Logger.repDebug(logInfo, "removeDeletedQuoteToQuoteLinks", "End of removing DeleteQuoteToQuoteLinks for quotation id "+ slsQtnID + " and version "
				+slsQtnVrsn);
	}

}
