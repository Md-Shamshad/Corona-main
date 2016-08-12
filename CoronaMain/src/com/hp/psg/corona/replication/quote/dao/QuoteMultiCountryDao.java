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
import com.hp.psg.corona.replication.quote.vo.QuoteMultiCountryVO;

public class QuoteMultiCountryDao extends QuoteConnection implements IBaseDao {

	QueryRepository queryRepository = null;
	String dataSourceName = null;
	LoggerInfo logInfo=null;

	public QuoteMultiCountryDao() {
		queryRepository = ReplicationQueryManager.getInstance();
		dataSourceName = Config.getProperty("corona.replication.secondary.data.source.name");
		logInfo = new LoggerInfo (this.getClass().getName());
	}

	@Override
	public void create(Connection con, long slsQtnID, String slsQtnVrsn)
	throws ReplicationException {
		List<QuoteMultiCountryVO> quoteMultiCountries = getQuoteMultiCountry(con,slsQtnID,slsQtnVrsn);
		if(quoteMultiCountries!=null && quoteMultiCountries.size()>0){
			for(QuoteMultiCountryVO quoteMultiCountryVO : quoteMultiCountries){
				insertQuoteMultiCountry(quoteMultiCountryVO);
			}
		}
	}

	private List<QuoteMultiCountryVO> getQuoteMultiCountry(Connection con,
			long slsQtnID, String slsQtnVrsn) throws ReplicationException {
		Logger.repDebug(logInfo, "getQuoteMultiCountry", "Begining of getting QuoteMultiCountry details for quotation id "+ slsQtnID+
				" and sls version "+slsQtnVrsn);
		List<QuoteMultiCountryVO> quoteMultiCountries = new ArrayList<QuoteMultiCountryVO>();
		QuoteMultiCountryVO quoteMultiCountryVO = null;
		String selectQuery = null;

		try {
			selectQuery = queryRepository.getQuery("Quote","Select","QuoteMultiCountry"); 
			List<Object> options = new ArrayList<Object>();
			options.add(String.valueOf(slsQtnID));
			options.add(slsQtnVrsn);

			ResultSet resultSet = runPreparedStatement(con, selectQuery, options);
			while (resultSet.next()){
				quoteMultiCountryVO = createVO(resultSet);
				quoteMultiCountries.add(quoteMultiCountryVO);
			}
		}catch(SQLException sqlEx){
			Logger.repError(logInfo, "getQuoteMultiCountry", "Exception while getting QuoteMultiCountry details...", sqlEx);
			throw new ReplicationException(sqlEx.getMessage(), sqlEx);
		}catch (Exception e) {
			Logger.repError(logInfo, "getQuoteMultiCountry", "Exception while getting QuoteMultiCountry details...", e);
			throw new ReplicationException(e.getMessage(), e);
		}

		Logger.repDebug(logInfo, "getQuoteMultiCountry", "End of getting QuoteMultiCountry details for quotation id "+ slsQtnID+
				" and sls version "+slsQtnVrsn);
		return quoteMultiCountries;
	}

	private QuoteMultiCountryVO createVO(ResultSet resultSet) throws ReplicationException {
		QuoteMultiCountryVO quoteMultiCountryVO =  new QuoteMultiCountryVO(); 

		try{
			quoteMultiCountryVO.setCountryCd(resultSet.getString("COUNTRY_CD"));
			quoteMultiCountryVO.setCountryNm(resultSet.getString("COUNTRY_NM"));
			quoteMultiCountryVO.setCreatedBy(resultSet.getString("CREATED_BY"));
			quoteMultiCountryVO.setCreatedTs(resultSet.getTimestamp("CREATED_TS"));
			quoteMultiCountryVO.setDeleteInd((resultSet.getString("DELETE_IND")).charAt(0));
			quoteMultiCountryVO.setLastModifiedBy(resultSet.getString("LAST_MODIFIED_BY"));
			quoteMultiCountryVO.setLastModifiedTs(resultSet.getTimestamp("LAST_MODIFIED_TS"));
			quoteMultiCountryVO.setSlsQtnId(resultSet.getLong("SLS_QTN_ID"));
			quoteMultiCountryVO.setSlsQtnVrsnSqnNr(resultSet.getString("SLS_QTN_VRSN_SQN_NR"));
		}catch(SQLException sqlEx){
			Logger.repError(logInfo, "createVO", "Exception while creating QuoteMultiCountryVO...", sqlEx);
			throw new ReplicationException(sqlEx.getMessage(), sqlEx);
		}

		return quoteMultiCountryVO;
	}

	private void insertQuoteMultiCountry(QuoteMultiCountryVO quoteMultiCountryVO) throws ReplicationException {
		Logger.repDebug(logInfo, "insertQuoteMultiCountry", "Begining of inserting QuoteMultiCountry details...");

		String insertQuery = null;
		Connection con = null;

		try {
			con = getConnection(dataSourceName);

			insertQuery = queryRepository.getQuery("Quote","Create","QuoteMultiCountry"); 
			List<Object> options = new ArrayList<Object>();

			options.add(String.valueOf(quoteMultiCountryVO.getSlsQtnId()));
			options.add(quoteMultiCountryVO.getSlsQtnVrsnSqnNr());
			options.add(quoteMultiCountryVO.getCountryNm());
			options.add(quoteMultiCountryVO.getCountryCd());
			options.add(String.valueOf(quoteMultiCountryVO.getDeleteInd()));
			options.add(ReplicationUtility.dateString(quoteMultiCountryVO.getCreatedTs()));
			options.add(quoteMultiCountryVO.getCreatedBy());
			options.add(ReplicationUtility.dateString(quoteMultiCountryVO.getLastModifiedTs()));
			options.add(quoteMultiCountryVO.getLastModifiedBy());

			executeUpdatePreparedStatement(con,insertQuery,options);
		}
		catch (Exception ex) {
			Logger.repError(logInfo, "insertQuoteMultiCountry", "Exception while inserting QuoteMultiCountry details...", ex);
			throw new ReplicationException(ex.getMessage(),ex);
		}
		finally{
			closeConnection(con);
		}
		Logger.repDebug(logInfo, "insertQuoteMultiCountry", "End of inserting QuoteMultiCountry details.");

	}

	@Override
	public void update(Connection con, long slsQtnID, String slsQtnVrsn)
	throws ReplicationException {
		Connection dstCon = null;
		try {
			dstCon = getConnection(dataSourceName);
			updateQuoteMultiCountryDeleteFlag(dstCon,slsQtnID, slsQtnVrsn);

			List<QuoteMultiCountryVO> quoteMultiCountries = getQuoteMultiCountry(con,slsQtnID,slsQtnVrsn);
			if(quoteMultiCountries!=null && quoteMultiCountries.size()>0){
				for(QuoteMultiCountryVO quoteMultiCountryVO : quoteMultiCountries){
					updateQuoteMultiCountry(dstCon,slsQtnID, slsQtnVrsn, quoteMultiCountryVO);
				}
			}
			removeDeletedQuoteMultiCountries(dstCon,slsQtnID, slsQtnVrsn);
		}
		catch (Exception ex) {
			Logger.repError(logInfo, "update", "Exception while updating QuoteMultiCountry details...", ex);
			throw new ReplicationException(ex.getMessage(),ex);
		}
		finally{
			closeConnection(dstCon);
		}
	}

	private void updateQuoteMultiCountry(Connection con,long slsQtnID, String slsQtnVrsn,
			QuoteMultiCountryVO quoteMultiCountryVO) throws ReplicationException {
		Logger.repDebug(logInfo, "updateQuoteMultiCountry", "Begining of updating QuoteMultiCountry details for quotation id "+ slsQtnID+
				" and sls version "+slsQtnVrsn);
		String updateQuery = null;
		try {
			updateQuery = queryRepository.getQuery("Quote","Update","QuoteMultiCountry"); 
			List<Object> options = new ArrayList<Object>();

			options.add(quoteMultiCountryVO.getCountryNm());
			options.add(String.valueOf(quoteMultiCountryVO.getDeleteInd()));
			options.add(ReplicationUtility.dateString(quoteMultiCountryVO.getCreatedTs()));
			options.add(quoteMultiCountryVO.getCreatedBy());
			options.add(ReplicationUtility.dateString(quoteMultiCountryVO.getLastModifiedTs()));
			options.add(quoteMultiCountryVO.getLastModifiedBy());

			//for where conditions
			options.add(String.valueOf(slsQtnID));
			options.add(slsQtnVrsn);
			options.add(quoteMultiCountryVO.getCountryCd());

			int rowUpdated = executeUpdatePreparedStatement(con,updateQuery,options);
			if(rowUpdated == 0){
				Logger.repDebug(logInfo, "updateQuoteMultiCountry", "QuoteMultiCountry doesn't exist for SLS_QTN_ID =  "+slsQtnID+ " and SLS_QTN_VRSN = "
						+slsQtnVrsn+" .Inserting record now.");
				insertQuoteMultiCountry(quoteMultiCountryVO);
			}
		}
		catch (Exception ex) {
			Logger.repError(logInfo, "updateQuoteMultiCountry", "Exception while updating QuoteMultiCountry details...", ex);
			throw new ReplicationException(ex.getMessage(),ex);
		}
		Logger.repDebug(logInfo, "updateQuoteMultiCountry", "End of updating QuoteMultiCountry details for quotation id "+ slsQtnID+
				" and sls version "+slsQtnVrsn);

	}

	@Override
	public void delete(Connection con, long slsQtnID, String slsQtnVrsn)
	throws ReplicationException {
		List<QuoteMultiCountryVO> quoteMultiCountries = getQuoteMultiCountry(con,slsQtnID,slsQtnVrsn);
		if(quoteMultiCountries!=null && quoteMultiCountries.size()>0){
			Connection dstCon = null;
			try {
				dstCon = getConnection(dataSourceName);
				for(QuoteMultiCountryVO quoteMultiCountryVO : quoteMultiCountries){
					deleteQuoteMultiCountry(dstCon,slsQtnID, slsQtnVrsn, quoteMultiCountryVO);
				}
			}
			catch (Exception ex) {
				Logger.repError(logInfo, "delete", "Exception while updating QuoteMultiCountry details...", ex);
				throw new ReplicationException(ex.getMessage(),ex);
			}
			finally{
				closeConnection(dstCon);
			}
		}

	}

	private void deleteQuoteMultiCountry(Connection con,long slsQtnID, String slsQtnVrsn,
			QuoteMultiCountryVO quoteMultiCountryVO) throws ReplicationException {
		updateQuoteMultiCountry(con,slsQtnID, slsQtnVrsn, quoteMultiCountryVO);

	}

	@Override
	public void change(Connection con, long slsQtnID, String slsQtnVrsn)
	throws ReplicationException {
		Logger.repDebug(logInfo, "change", "Incorrectly called change for QUOTE_MULTI_COUNTRY TABLE");

	}

	private void updateQuoteMultiCountryDeleteFlag(Connection con, long slsQtnID,
			String slsQtnVrsn) throws ReplicationException {
		String query = null;
		try {
			query = queryRepository.getQuery("Quote","Update","QuoteMultiCountryDeleteFlag"); 
			List<Object> options = new ArrayList<Object>();
			options.add(String.valueOf(slsQtnID));
			options.add(slsQtnVrsn);

			executeUpdatePreparedStatement(con,query,options);
		}
		catch (Exception ex) {
			Logger.repError(logInfo, "updateQuoteMultiCountryDeleteFlag", "Exception while updating QuoteMultiCountryDeleteFlag...", ex);
			throw new ReplicationException(ex.getMessage(),ex);
		}
		Logger.repDebug(logInfo, "updateQuoteMultiCountryDeleteFlag", "End of updating QuoteMultiCountryDeleteFlag for quotation id "+ slsQtnID +" and version "
				+slsQtnVrsn);

	}

	private void removeDeletedQuoteMultiCountries(Connection con,long slsQtnID,
			String slsQtnVrsn) throws ReplicationException {
		String query = null;
		try {
			query = queryRepository.getQuery("Quote","Delete","DeleteQuoteMultiCountry"); 
			List<Object> options = new ArrayList<Object>();
			options.add(String.valueOf(slsQtnID));
			options.add(slsQtnVrsn);

			executeUpdatePreparedStatement(con,query,options);
		}
		catch (Exception ex) {
			Logger.repError(logInfo, "removeDeletedQuoteMultiCountries", "Exception while removing DeleteQuoteMultiCountry...", ex);
			throw new ReplicationException(ex.getMessage(),ex);
		}
		Logger.repDebug(logInfo, "removeDeletedQuoteMultiCountries", "End of removing DeleteQuoteMultiCountry for quotation id "+ slsQtnID + " and version "
				+slsQtnVrsn);

	}

}
