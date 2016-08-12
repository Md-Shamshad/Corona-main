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
import com.hp.psg.corona.replication.quote.vo.QuoteResellerVO;

public class QuoteResellerDao extends QuoteConnection implements IBaseDao {
	QueryRepository queryRepository = null;
	String dataSourceName = null;
	LoggerInfo logInfo=null;


	public QuoteResellerDao() {
		queryRepository = ReplicationQueryManager.getInstance();
		dataSourceName = Config.getProperty("corona.replication.secondary.data.source.name");
		logInfo = new LoggerInfo (this.getClass().getName());
	}

	@Override
	public void create(Connection con, long slsQtnID, String slsQtnVrsn)
	throws ReplicationException {
		List<QuoteResellerVO> quoteResellers = getQuoteResellers(con,slsQtnID,slsQtnVrsn);
		if(quoteResellers!=null && quoteResellers.size()>0){
			for(QuoteResellerVO quoteResellerVO : quoteResellers){
				insertQuoteResellers(quoteResellerVO);
			}
		}

	}

	private void insertQuoteResellers(QuoteResellerVO quoteResellerVO) throws ReplicationException {
		Logger.repDebug(logInfo, "insertQuoteResellers", "Begining of inserting QuoteResellers details...");

		String insertQuery = null;
		Connection con = null;

		try {
			con = getConnection(dataSourceName);

			insertQuery = queryRepository.getQuery("Quote","Create","QuoteReseller"); 
			List<Object> options = new ArrayList<Object>();

			options.add(String.valueOf(quoteResellerVO.getSlsQtnId()));
			options.add(quoteResellerVO.getSlsQtnVrsnSqnNr());
			options.add(quoteResellerVO.getRslrType());
			options.add(quoteResellerVO.getCustName());
			options.add(quoteResellerVO.getRslrCityNm());
			options.add(quoteResellerVO.getRslrStProvCd());
			options.add(quoteResellerVO.getRslrCntryCd());
			options.add(String.valueOf(quoteResellerVO.getOutletId()));
			options.add(quoteResellerVO.getRslrSearchKey());
			options.add(quoteResellerVO.getPtnrCmpnyId());
			options.add(String.valueOf(quoteResellerVO.getDeleteInd()));
			options.add(ReplicationUtility.dateString(quoteResellerVO.getCreatedTs()));
			options.add(quoteResellerVO.getCreatedBy());
			options.add(ReplicationUtility.dateString(quoteResellerVO.getLastModifiedTs()));
			options.add(quoteResellerVO.getLastModifiedBy());
			options.add(String.valueOf(quoteResellerVO.getRslrID()));

			executeUpdatePreparedStatement(con,insertQuery,options);
		}
		catch (Exception ex) {
			Logger.repError(logInfo, "insertQuoteResellers", "Exception while inserting QuoteResellers details...", ex);
			throw new ReplicationException(ex.getMessage(),ex);
		}
		finally{
			closeConnection(con);
		}
		Logger.repDebug(logInfo, "insertQuoteResellers", "End of inserting QuoteResellers details.");

	}

	private List<QuoteResellerVO> getQuoteResellers(Connection con,
			long slsQtnID, String slsQtnVrsn) throws ReplicationException {
		Logger.repDebug(logInfo, "getQuoteResellers", "Begining of getting QuoteResellers details for quotation id "+ slsQtnID+
				" and sls version "+slsQtnVrsn);
		List<QuoteResellerVO> quoteResellers = new ArrayList<QuoteResellerVO>();
		QuoteResellerVO quoteResellerVO = null;
		String selectQuery = null;

		try {
			selectQuery = queryRepository.getQuery("Quote","Select","QuoteReseller"); 
			List<Object> options = new ArrayList<Object>();
			options.add(String.valueOf(slsQtnID));
			options.add(slsQtnVrsn);

			ResultSet resultSet = runPreparedStatement(con, selectQuery, options);
			while (resultSet.next()){
				quoteResellerVO = createVO(resultSet);
				quoteResellers.add(quoteResellerVO);
			}
		}catch(SQLException sqlEx){
			Logger.repError(logInfo, "getQuoteResellers", "Exception while getting QuoteResellers details...", sqlEx);
			throw new ReplicationException(sqlEx.getMessage(), sqlEx);
		}catch (Exception e) {
			Logger.repError(logInfo, "getQuoteResellers", "Exception while getting QuoteResellers details...", e);
			throw new ReplicationException(e.getMessage(), e);
		}

		Logger.repDebug(logInfo, "getQuoteResellers", "End of getting QuoteResellers details for quotation id "+ slsQtnID+
				" and sls version "+slsQtnVrsn);
		return quoteResellers;
	}

	private QuoteResellerVO createVO(ResultSet resultSet) throws ReplicationException {
		QuoteResellerVO quoteResellerVO = new QuoteResellerVO();

		try{
			quoteResellerVO.setCreatedBy(resultSet.getString("CREATED_BY"));
			quoteResellerVO.setCreatedTs(resultSet.getTimestamp("CREATED_TS"));
			quoteResellerVO.setCustName(resultSet.getString("CUST_NAME"));
			quoteResellerVO.setDeleteInd((resultSet.getString("DELETE_IND")).charAt(0));
			quoteResellerVO.setLastModifiedBy(resultSet.getString("LAST_MODIFIED_BY"));
			quoteResellerVO.setLastModifiedTs(resultSet.getTimestamp("LAST_MODIFIED_TS"));
			quoteResellerVO.setOutletId(resultSet.getLong("OUTLET_ID"));
			quoteResellerVO.setPtnrCmpnyId(resultSet.getString("PTNR_CMPNY_ID"));
			quoteResellerVO.setRslrCityNm(resultSet.getString("RSLR_CITY_NM"));
			quoteResellerVO.setRslrCntryCd(resultSet.getString("RSLR_CNTRY_CD"));
			quoteResellerVO.setRslrID(resultSet.getLong("RSLR_ID"));
			quoteResellerVO.setRslrSearchKey(resultSet.getString("RSLR_SEARCH_KEY"));
			quoteResellerVO.setRslrStProvCd(resultSet.getString("RSLR_ST_PROV_CD"));
			quoteResellerVO.setRslrType(resultSet.getString("RSLR_TYPE"));
			quoteResellerVO.setSlsQtnId(resultSet.getLong("SLS_QTN_ID"));
			quoteResellerVO.setSlsQtnVrsnSqnNr(resultSet.getString("SLS_QTN_VRSN_SQN_NR"));
		}catch(SQLException sqlEx){
			Logger.repError(logInfo, "createVO", "Exception while creating QuoteResellerVO...", sqlEx);
			throw new ReplicationException(sqlEx.getMessage(), sqlEx);
		}

		return quoteResellerVO;
	}

	@Override
	public void update(Connection con, long slsQtnID, String slsQtnVrsn)
	throws ReplicationException {
		Connection dstCon = null;

		try {
			dstCon = getConnection(dataSourceName);
			updateQuoteResellersDeleteFlag(dstCon,slsQtnID, slsQtnVrsn);

			List<QuoteResellerVO> quoteResellers = getQuoteResellers(con,slsQtnID,slsQtnVrsn);
			if(quoteResellers!=null && quoteResellers.size()>0){
				for(QuoteResellerVO quoteResellerVO : quoteResellers){
					updateQuoteResellers(dstCon,slsQtnID, slsQtnVrsn, quoteResellerVO);
				}
			}
			removeDeletedQuoteResellers(dstCon,slsQtnID, slsQtnVrsn);
		}
		catch (Exception ex) {
			Logger.repError(logInfo, "update", "Exception while updating quote resellers details...", ex);
			throw new ReplicationException(ex.getMessage(),ex);
		}
		finally{
			closeConnection(dstCon);
		}
	}

	private void updateQuoteResellers(Connection con,long slsQtnID, String slsQtnVrsn,
			QuoteResellerVO quoteResellerVO) throws ReplicationException {
		Logger.repDebug(logInfo, "updateQuoteResellers", "Begining of updating QuoteResellers details for quotation id "+ slsQtnID+
				" and sls version "+slsQtnVrsn);
		String updateQuery = null;

		try {
			updateQuery = queryRepository.getQuery("Quote","Update","QuoteReseller"); 
			List<Object> options = new ArrayList<Object>();


			options.add(quoteResellerVO.getCustName());
			options.add(quoteResellerVO.getRslrCityNm());
			options.add(quoteResellerVO.getRslrStProvCd());
			options.add(quoteResellerVO.getRslrCntryCd());
			options.add(String.valueOf(quoteResellerVO.getOutletId()));
			options.add(quoteResellerVO.getRslrSearchKey());
			options.add(quoteResellerVO.getPtnrCmpnyId());
			options.add(String.valueOf(quoteResellerVO.getDeleteInd()));
			options.add(ReplicationUtility.dateString(quoteResellerVO.getCreatedTs()));
			options.add(quoteResellerVO.getCreatedBy());
			options.add(ReplicationUtility.dateString(quoteResellerVO.getLastModifiedTs()));
			options.add(quoteResellerVO.getLastModifiedBy());

			//for where conditions
			options.add(String.valueOf(slsQtnID));
			options.add(slsQtnVrsn);
			options.add(quoteResellerVO.getRslrType());
			options.add(String.valueOf(quoteResellerVO.getRslrID()));

			int rowUpdated = executeUpdatePreparedStatement(con,updateQuery,options);
			if(rowUpdated == 0){
				Logger.repDebug(logInfo, "updateQuoteResellers", "QuoteResellers doesn't exist for SLS_QTN_ID =  "+slsQtnID+ " and SLS_QTN_VRSN = "
						+slsQtnVrsn+" .Inserting record now.");
				insertQuoteResellers(quoteResellerVO);
			}
		}
		catch (Exception ex) {
			Logger.repError(logInfo, "updateQuoteResellers", "Exception while updating QuoteResellers details...", ex);
			throw new ReplicationException(ex.getMessage(),ex);
		}
		Logger.repDebug(logInfo, "updateQuoteResellers", "End of updating QuoteResellers details for quotation id "+ slsQtnID+
				" and sls version "+slsQtnVrsn);

	}

	@Override
	public void delete(Connection con, long slsQtnID, String slsQtnVrsn)
	throws ReplicationException {
		List<QuoteResellerVO> quoteResellers = getQuoteResellers(con,slsQtnID,slsQtnVrsn);
		if(quoteResellers!=null && quoteResellers.size()>0){
			Connection dstCon = null;
			try {
				dstCon = getConnection(dataSourceName);
				for(QuoteResellerVO quoteResellerVO : quoteResellers){
					deleteQuoteResellers(dstCon,slsQtnID, slsQtnVrsn, quoteResellerVO);
				}
			}
			catch (Exception ex) {
				Logger.repError(logInfo, "deleteQuoteResellers", "Exception while deleting quote resellers details...", ex);
				throw new ReplicationException(ex.getMessage(),ex);
			}
			finally{
				closeConnection(dstCon);
			}
		}
	}

	private void deleteQuoteResellers(Connection dstCon, long slsQtnID, String slsQtnVrsn,
			QuoteResellerVO quoteResellerVO) throws ReplicationException {
		updateQuoteResellers(dstCon, slsQtnID, slsQtnVrsn, quoteResellerVO);
	}

	@Override
	public void change(Connection con, long slsQtnID, String slsQtnVrsn)
	throws ReplicationException {
		Logger.repDebug(logInfo, "change", "Incorrectly called change for QUOTE_RESELLERS TABLE");

	}

	private void removeDeletedQuoteResellers(Connection con, long slsQtnID, String slsQtnVrsn) throws ReplicationException {
		String query = null;
		try {
			query = queryRepository.getQuery("Quote","Delete","DeleteQuoteResellers"); 
			List<Object> options = new ArrayList<Object>();
			options.add(String.valueOf(slsQtnID));
			options.add(slsQtnVrsn);

			executeUpdatePreparedStatement(con,query,options);
		}
		catch (Exception ex) {
			Logger.repError(logInfo, "removeDeletedQuoteResellers", "Exception while removing DeletedQuoteResellers...", ex);
			throw new ReplicationException(ex.getMessage(),ex);
		}
		Logger.repDebug(logInfo, "removeDeletedQuoteResellers", "End of removing DeletedQuoteResellers for quotation id "+ slsQtnID + " and version "
				+slsQtnVrsn);
	}

	private void updateQuoteResellersDeleteFlag(Connection con, long slsQtnID, String slsQtnVrsn) throws ReplicationException {
		String query = null;

		try {
			query = queryRepository.getQuery("Quote","Update","QuoteResellersDeleteFlag"); 
			List<Object> options = new ArrayList<Object>();
			options.add(String.valueOf(slsQtnID));
			options.add(slsQtnVrsn);

			executeUpdatePreparedStatement(con,query,options);
		}
		catch (Exception ex) {
			Logger.repError(logInfo, "updateQuoteResellersDeleteFlag", "Exception while updating QuoteResellersDeleteFlag...", ex);
			throw new ReplicationException(ex.getMessage(),ex);
		}

		Logger.repDebug(logInfo, "updateQuoteResellersDeleteFlag", "End of updating QuoteResellersDeleteFlag for quotation id "+ slsQtnID +" and version "
				+slsQtnVrsn);

	}

}
