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
import com.hp.psg.corona.replication.quote.vo.QuoteAffiliatesVO;

public class QuoteAffiliatesDao extends QuoteConnection implements IBaseDao {
	QueryRepository queryRepository = null;
	String dataSourceName = null;
	LoggerInfo logInfo=null;

	public QuoteAffiliatesDao() {
		queryRepository = ReplicationQueryManager.getInstance();
		dataSourceName = Config.getProperty("corona.replication.secondary.data.source.name");
		logInfo = new LoggerInfo (this.getClass().getName());
	}

	@Override
	public void create(Connection con, long slsQtnID, String slsQtnVrsn)
	throws ReplicationException {
		List<QuoteAffiliatesVO> quoteAffiliates = getQuoteAffiliates(con,slsQtnID,slsQtnVrsn);
		if(quoteAffiliates!=null && quoteAffiliates.size()>0){
			for(QuoteAffiliatesVO quoteAffiliatesVO : quoteAffiliates){
				insertQuoteAffiliates(quoteAffiliatesVO);
			}
		}
	}

	@Override
	public void update(Connection con, long slsQtnID, String slsQtnVrsn)
	throws ReplicationException {
		Connection dstCon = null;

		try {
			dstCon = getConnection(dataSourceName);
		
			updateQuoteAffiliatesDeleteFlag(dstCon, slsQtnID, slsQtnVrsn);
		
			List<QuoteAffiliatesVO> quoteAffiliates = getQuoteAffiliates(con,slsQtnID,slsQtnVrsn);
			if(quoteAffiliates!=null && quoteAffiliates.size()>0){
				for(QuoteAffiliatesVO quoteAffiliatesVO : quoteAffiliates){
					updateQuoteAffiliates(dstCon,slsQtnID,slsQtnVrsn,quoteAffiliatesVO);
				}
			}
		
			removeDeletedQuoteAffiliates(dstCon, slsQtnID, slsQtnVrsn);
		}
		catch (Exception ex) {
			Logger.repError(logInfo, "update", "Exception while updating quote affiliates details...", ex);
			throw new ReplicationException(ex.getMessage(),ex);
		}
		finally{
			closeConnection(dstCon);
		}
		
	}

	@Override
	public void delete(Connection con, long slsQtnID, String slsQtnVrsn)
	throws ReplicationException {
		List<QuoteAffiliatesVO> quoteAffiliates = getQuoteAffiliates(con,slsQtnID,slsQtnVrsn);
		if(quoteAffiliates!=null && quoteAffiliates.size()>0){
			
			Connection dstCon = null;
			try {
				dstCon = getConnection(dataSourceName);
			
				for(QuoteAffiliatesVO quoteAffiliatesVO : quoteAffiliates){
					deleteQuoteAffiliates(dstCon, slsQtnID,slsQtnVrsn,quoteAffiliatesVO);
				}
			}
			catch (Exception ex) {
				Logger.repError(logInfo, "delete", "Exception while deleting quote affiliates details...", ex);
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
		Logger.repDebug(logInfo, "change", "Incorrectly called change for QUOTE_AFFILIATES TABLE");

	}

	private void deleteQuoteAffiliates(Connection con, long slsQtnID, String slsQtnVrsn,
			QuoteAffiliatesVO quoteAffiliatesVO) throws ReplicationException {
			updateQuoteAffiliates(con, slsQtnID, slsQtnVrsn, quoteAffiliatesVO);		
	}

	private void updateQuoteAffiliates(Connection con, long slsQtnID, String slsQtnVrsn,
			QuoteAffiliatesVO quoteAffiliatesVO) throws ReplicationException {
		Logger.repDebug(logInfo, "updateQuoteAffiliates", "Begining of updating quote affiliates details for quotation id "+ slsQtnID);
		String updateQuery = null;
		
		try {
			updateQuery = queryRepository.getQuery("Quote","Update","QuoteAffiliates"); 
			List<Object> options = new ArrayList<Object>();

			options.add(quoteAffiliatesVO.getEuAuthAffilNm());
			options.add(quoteAffiliatesVO.getEuAuthCityNm());
			options.add(quoteAffiliatesVO.getEuAuthStProvCd());
			options.add(String.valueOf(quoteAffiliatesVO.getDeleteInd()));
			options.add(ReplicationUtility.dateString(quoteAffiliatesVO.getCreatedTs()));
			options.add(quoteAffiliatesVO.getCreatedBy());
			options.add(ReplicationUtility.dateString(quoteAffiliatesVO.getLastModifiedTs()));
			options.add(quoteAffiliatesVO.getLastModifiedBy());

			options.add(String.valueOf(slsQtnID));
			options.add(slsQtnVrsn);
			options.add(String.valueOf(quoteAffiliatesVO.getEuAuthAffilNr()));

			int rowUpdated = executeUpdatePreparedStatement(con,updateQuery,options);
			if(rowUpdated == 0){
				Logger.repDebug(logInfo, "updateQuoteAffiliates", "Quote affiliate doesn't exist for SLS_QTN_ID =  "+slsQtnID+ " and SLS_QTN_VRSN = "
						+slsQtnVrsn+" .Inserting record now.");
				insertQuoteAffiliates(quoteAffiliatesVO);
			}
		}
		catch (Exception ex) {
			Logger.repError(logInfo, "updateQuoteAffiliates", "Exception while updating quote affiliates details...", ex);
			throw new ReplicationException(ex.getMessage(),ex);
		}
		
		Logger.repDebug(logInfo, "updateSlsQtnItem", "End of updating quote affiliates details for quotation id "+ slsQtnID);

	}

	private void insertQuoteAffiliates(QuoteAffiliatesVO quoteAffiliatesVO) throws ReplicationException {
		Logger.repDebug(logInfo, "insertQuoteAffiliates", "Begining of inserting quote affiliates details...");

		String insertQuery = null;
		Connection con = null;

		try {
			con = getConnection(dataSourceName);

			insertQuery = queryRepository.getQuery("Quote","Create","QuoteAffiliates"); 
			List<Object> options = new ArrayList<Object>();

			options.add(String.valueOf(quoteAffiliatesVO.getSlsQtnId()));
			options.add(quoteAffiliatesVO.getSlsQtnVrsnSqnNr());
			options.add(String.valueOf(quoteAffiliatesVO.getEuAuthAffilNr()));
			options.add(quoteAffiliatesVO.getEuAuthAffilNm());
			options.add(quoteAffiliatesVO.getEuAuthCityNm());
			options.add(quoteAffiliatesVO.getEuAuthStProvCd());
			options.add(String.valueOf(quoteAffiliatesVO.getDeleteInd()));
			options.add(ReplicationUtility.dateString(quoteAffiliatesVO.getCreatedTs()));
			options.add(quoteAffiliatesVO.getCreatedBy());
			options.add(ReplicationUtility.dateString(quoteAffiliatesVO.getLastModifiedTs()));
			options.add(quoteAffiliatesVO.getLastModifiedBy());

			executeUpdatePreparedStatement(con,insertQuery,options);
		}
		catch (Exception ex) {
			Logger.repError(logInfo, "insertQuoteAffiliates", "Exception while inserting quote affiliates details...", ex);
			throw new ReplicationException(ex.getMessage(),ex);
		}
		finally{
			closeConnection(con);
		}
		Logger.repDebug(logInfo, "insertQuoteAffiliates", "End of inserting quote affiliates details.");
	}

	private List<QuoteAffiliatesVO> getQuoteAffiliates(Connection con,
			long slsQtnID, String slsQtnVrsn) throws ReplicationException {
		Logger.repDebug(logInfo, "getQuoteAffiliates", "Begining of getting quote affiliates details for quotation id "+slsQtnID+
				" and sls version "+slsQtnVrsn);
		List<QuoteAffiliatesVO> quoteAffiliates = new ArrayList<QuoteAffiliatesVO>();
		QuoteAffiliatesVO quoteAffiliatesVO = null;
		String selectQuery = null;

		try {
			selectQuery = queryRepository.getQuery("Quote","Select","QuoteAffiliates"); 
			List<Object> options = new ArrayList<Object>();
			options.add(String.valueOf(slsQtnID));
			options.add(slsQtnVrsn);

			ResultSet resultSet = runPreparedStatement(con, selectQuery, options);
			while (resultSet.next()){
				quoteAffiliatesVO = createVO(resultSet);
				quoteAffiliates.add(quoteAffiliatesVO);
			}
		}catch(SQLException sqlEx){
			Logger.repError(logInfo, "getQuoteAffiliates", "Exception while getting quote affiliates details...", sqlEx);
			throw new ReplicationException(sqlEx.getMessage(), sqlEx);
		}catch (Exception e) {
			Logger.repError(logInfo, "getQuoteAffiliates", "Exception while getting quote affiliates details...", e);
			throw new ReplicationException(e.getMessage(), e);
		}
		Logger.repDebug(logInfo, "getQuoteAffiliates", "End of getting quote affiliates details for quotation id "+ slsQtnID+
				" and sls version "+slsQtnVrsn);

		return quoteAffiliates;
	}

	private QuoteAffiliatesVO createVO(ResultSet resultSet) throws ReplicationException {
		QuoteAffiliatesVO quoteAffiliatesVO = new QuoteAffiliatesVO();

		try {
			quoteAffiliatesVO.setSlsQtnId(resultSet.getLong("SLS_QTN_ID"));
			quoteAffiliatesVO.setSlsQtnVrsnSqnNr(resultSet.getString("SLS_QTN_VRSN_SQN_NR"));
			quoteAffiliatesVO.setEuAuthAffilNr(resultSet.getLong("EU_AUTH_AFFIL_NR"));
			quoteAffiliatesVO.setEuAuthAffilNm(resultSet.getString("EU_AUTH_AFFIL_NM"));
			quoteAffiliatesVO.setEuAuthCityNm(resultSet.getString("EU_AUTH_CITY_NM"));
			quoteAffiliatesVO.setEuAuthStProvCd(resultSet.getString("EU_AUTH_ST_PROV_CD"));
			quoteAffiliatesVO.setDeleteInd((resultSet.getString("DELETE_IND")).charAt(0));
			quoteAffiliatesVO.setCreatedTs(resultSet.getTimestamp("CREATED_TS"));
			quoteAffiliatesVO.setCreatedBy(resultSet.getString("CREATED_BY"));
			quoteAffiliatesVO.setLastModifiedTs(resultSet.getTimestamp("LAST_MODIFIED_TS"));
			quoteAffiliatesVO.setLastModifiedBy(resultSet.getString("LAST_MODIFIED_BY"));
		} catch (SQLException e) {
			Logger.repError(logInfo, "createVO", "Exception while creating VO for QuoteAffiliates...", e);
			throw new ReplicationException(e.getMessage(), e);
		}
		return quoteAffiliatesVO;
	}
	
	private void removeDeletedQuoteAffiliates(Connection con, long slsQtnID, String slsQtnVrsn) throws ReplicationException {
		String query = null;
		
		try {
			query = queryRepository.getQuery("Quote","Delete","DeleteQuoteAffiliates"); 
			List<Object> options = new ArrayList<Object>();
			options.add(String.valueOf(slsQtnID));
			options.add(slsQtnVrsn);
			
			executeUpdatePreparedStatement(con,query,options);
		}
		catch (Exception ex) {
			Logger.repError(logInfo, "removeDeletedQuoteAffiliates", "Exception while removing DeleteQuoteAffiliates...", ex);
			throw new ReplicationException(ex.getMessage(),ex);
		}
				
		Logger.repDebug(logInfo, "removeDeletedQuoteAffiliates", "End of removing DeleteQuoteAffiliates for quotation id "+ slsQtnID + " and version "
				+slsQtnVrsn);
		
	}

	private void updateQuoteAffiliatesDeleteFlag(Connection con, long slsQtnID,
			String slsQtnVrsn) throws ReplicationException {
		String query = null;
				
		try {						
			query = queryRepository.getQuery("Quote","Update","QuoteAffiliateDeleteFlag"); 
			List<Object> options = new ArrayList<Object>();
			options.add(String.valueOf(slsQtnID));
			options.add(slsQtnVrsn);
			
			executeUpdatePreparedStatement(con,query,options);
		}
		catch (Exception ex) {
			Logger.repError(logInfo, "updateQuoteAffiliatesDeleteFlag", "Exception while updating QuoteAffiliateDeleteFlag...", ex);
			throw new ReplicationException(ex.getMessage(),ex);
		}
			
		Logger.repDebug(logInfo, "updateQuoteAffiliatesDeleteFlag", "End of updating QuoteAffiliateDeleteFlag for quotation id "+ slsQtnID +" and version "	+slsQtnVrsn);
		
	}
	
}
