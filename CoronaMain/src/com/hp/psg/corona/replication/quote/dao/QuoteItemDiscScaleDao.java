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
import com.hp.psg.corona.replication.quote.vo.QuoteItemDiscScaleVO;

public class QuoteItemDiscScaleDao extends QuoteConnection implements IBaseDao {
	QueryRepository queryRepository = null;
	String dataSourceName = null;
	LoggerInfo logInfo=null;

	public QuoteItemDiscScaleDao() {
		queryRepository = ReplicationQueryManager.getInstance();
		dataSourceName = Config.getProperty("corona.replication.secondary.data.source.name");
		logInfo = new LoggerInfo (this.getClass().getName());
	}

	@Override
	public void create(Connection con, long slsQtnID, String slsQtnVrsn)
	throws ReplicationException {
		List<QuoteItemDiscScaleVO> quoteItemDiscScales = getQuoteItemDiscScales(con,slsQtnID,slsQtnVrsn);
		if(quoteItemDiscScales!=null && quoteItemDiscScales.size()>0){
			for(QuoteItemDiscScaleVO quoteItemDiscScaleVO : quoteItemDiscScales){
				insertQuoteItemDiscScales(quoteItemDiscScaleVO);
			}
		}
	}

	@Override
	public void update(Connection con, long slsQtnID, String slsQtnVrsn)
	throws ReplicationException {
		
		Connection dstCon = null;		
		try {
			dstCon = getConnection(dataSourceName);
		
			updateQuoteItemDiscScaleDeleteFlag(dstCon, slsQtnID, slsQtnVrsn);
		
			List<QuoteItemDiscScaleVO> quoteItemDiscScales = getQuoteItemDiscScales(con,slsQtnID,slsQtnVrsn);
			if(quoteItemDiscScales!=null && quoteItemDiscScales.size()>0){
				for(QuoteItemDiscScaleVO quoteItemDiscScaleVO : quoteItemDiscScales){
					updateQuoteItemDiscScales(dstCon, slsQtnID, slsQtnVrsn, quoteItemDiscScaleVO);
				}
			}
			removeDeletedQuoteItemDiscScales(dstCon, slsQtnID, slsQtnVrsn);
		}
		catch (Exception ex) {
			Logger.repError(logInfo, "update", "Exception while updating QuoteItemDiscScale details...", ex);
			throw new ReplicationException(ex.getMessage(),ex);
		}
		finally{
			closeConnection(dstCon);
		}
	}

	@Override
	public void delete(Connection con, long slsQtnID, String slsQtnVrsn)
	throws ReplicationException {
		List<QuoteItemDiscScaleVO> quoteItemDiscScales = getQuoteItemDiscScales(con,slsQtnID,slsQtnVrsn);
		if(quoteItemDiscScales!=null && quoteItemDiscScales.size()>0){
			Connection dstCon = null;		
			try {
				dstCon = getConnection(dataSourceName);
				
				for(QuoteItemDiscScaleVO quoteItemDiscScaleVO : quoteItemDiscScales){
					deleteQuoteItemDiscScales(dstCon, slsQtnID, slsQtnVrsn, quoteItemDiscScaleVO);
				}
			}
			catch (Exception ex) {
				Logger.repError(logInfo, "update", "Exception while deleting QuoteItemDiscScale details...", ex);
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
		Logger.repDebug(logInfo, "change", "Incorrectly called change for QUOTE_ITEM_DISC_SCALE TABLE");

	}

	private void insertQuoteItemDiscScales(
			QuoteItemDiscScaleVO quoteItemDiscScaleVO) throws ReplicationException {
		Logger.repDebug(logInfo, "insertQuoteItemDiscScales", "Begining of inserting QuoteItemDiscScales details...");

		String insertQuery = null;
		Connection con = null;

		try {
			con = getConnection(dataSourceName);

			insertQuery = queryRepository.getQuery("Quote","Create","QuoteItemDiscScale"); 
			List<Object> options = new ArrayList<Object>();

			options.add(quoteItemDiscScaleVO.getSlsQtnItmSqnNr());
			options.add(String.valueOf(quoteItemDiscScaleVO.getSlsQtnId()));
			options.add(quoteItemDiscScaleVO.getSlsQtnVrsnSqnNr());
			options.add(String.valueOf(quoteItemDiscScaleVO.getScaleId()));
			options.add(quoteItemDiscScaleVO.getDiscTypeCd());
			options.add(String.valueOf(quoteItemDiscScaleVO.getProdLstPrcAm()));
			options.add(String.valueOf(quoteItemDiscScaleVO.getAuthBdNetPrcAm()));
			options.add(String.valueOf(quoteItemDiscScaleVO.getAuthDiscOtPc()));
			options.add(String.valueOf(quoteItemDiscScaleVO.getHighRslrASdPc()));
			options.add(ReplicationUtility.dateString(quoteItemDiscScaleVO.getUpperBndryDt()));
			options.add(ReplicationUtility.dateString(quoteItemDiscScaleVO.getLowerBndryDt()));
			options.add(quoteItemDiscScaleVO.getAuthFl());
			options.add(String.valueOf(quoteItemDiscScaleVO.getAuthFixDiscAm()));
			options.add(String.valueOf(quoteItemDiscScaleVO.getAuthIncrDiscAm()));
			options.add(String.valueOf(quoteItemDiscScaleVO.getDeleteInd()));
			options.add(ReplicationUtility.dateString(quoteItemDiscScaleVO.getCreatedTs()));
			options.add(quoteItemDiscScaleVO.getCreatedBy());
			options.add(ReplicationUtility.dateString(quoteItemDiscScaleVO.getLastModifiedTs()));
			options.add(quoteItemDiscScaleVO.getLastModifiedBy());
			options.add(String.valueOf(quoteItemDiscScaleVO.getExtAuthBdNetPrcAm()));

			executeUpdatePreparedStatement(con,insertQuery,options);
		}
		catch (Exception ex) {
			Logger.repError(logInfo, "insertQuoteItemDiscScales", "Exception while inserting QuoteItemDiscScales details...", ex);
			throw new ReplicationException(ex.getMessage(),ex);
		}
		finally{
			closeConnection(con);
		}
		Logger.repDebug(logInfo, "insertQuoteItemDiscScales", "End of inserting QuoteItemDiscScales details.");


	}

	private List<QuoteItemDiscScaleVO> getQuoteItemDiscScales(Connection con,
			long slsQtnID, String slsQtnVrsn) throws ReplicationException {
		Logger.repDebug(logInfo, "getQuoteItemDiscScales", "Begining of getting QuoteItemDiscScales details for quotation id "+ slsQtnID+
				" and sls version "+slsQtnVrsn);
		List<QuoteItemDiscScaleVO> quoteItemDiscScales = new ArrayList<QuoteItemDiscScaleVO>();
		QuoteItemDiscScaleVO quoteItemDiscScaleVO = null;
		String selectQuery = null;

		try {
			selectQuery = queryRepository.getQuery("Quote","Select","QuoteItemDiscScale"); 
			List<Object> options = new ArrayList<Object>();
			options.add(String.valueOf(slsQtnID));
			options.add(slsQtnVrsn);

			ResultSet resultSet = runPreparedStatement(con, selectQuery, options);
			while (resultSet.next()){
				quoteItemDiscScaleVO = createVO(resultSet);
				quoteItemDiscScales.add(quoteItemDiscScaleVO);
			}
		}catch(SQLException sqlEx){
			Logger.repError(logInfo, "getQuoteItemDiscScales", "Exception while getting QuoteItemDiscScales details...", sqlEx);
			throw new ReplicationException(sqlEx.getMessage(), sqlEx);
		}catch (Exception e) {
			Logger.repError(logInfo, "getQuoteItemDiscScales", "Exception while getting QuoteItemDiscScales details...", e);
			throw new ReplicationException(e.getMessage(), e);
		}

		Logger.repDebug(logInfo, "getQuoteItemDiscScales", "End of getting QuoteItemDiscScales details for quotation id "+ slsQtnID+
				" and sls version "+slsQtnVrsn);
		return quoteItemDiscScales;
	}

	private QuoteItemDiscScaleVO createVO(ResultSet resultSet) throws ReplicationException {
		QuoteItemDiscScaleVO quoteItemDiscScaleVO = new QuoteItemDiscScaleVO();
		try{
			quoteItemDiscScaleVO.setAuthBdNetPrcAm(resultSet.getDouble("AUTH_BD_NET_PRC_AM"));
			quoteItemDiscScaleVO.setAuthDiscOtPc(resultSet.getDouble("AUTH_DISC_OT_PC"));
			quoteItemDiscScaleVO.setAuthFixDiscAm(resultSet.getDouble("AUTH_FIX_DISC_AM"));
			quoteItemDiscScaleVO.setAuthFl(resultSet.getString("AUTH_FL"));
			quoteItemDiscScaleVO.setAuthIncrDiscAm(resultSet.getDouble("AUTH_INCR_DISC_AM"));
			quoteItemDiscScaleVO.setCreatedBy(resultSet.getString("CREATED_BY"));
			quoteItemDiscScaleVO.setCreatedTs(resultSet.getTimestamp("CREATED_TS"));
			quoteItemDiscScaleVO.setDeleteInd((resultSet.getString("DELETE_IND")).charAt(0));
			quoteItemDiscScaleVO.setDiscTypeCd(resultSet.getString("DISC_TYPE_CD"));
			quoteItemDiscScaleVO.setExtAuthBdNetPrcAm(resultSet.getDouble("EXT_AUTH_BD_NET_PRC_AM"));
			quoteItemDiscScaleVO.setHighRslrASdPc(resultSet.getDouble("HIGH_RSLR_A_SD_PC"));
			quoteItemDiscScaleVO.setLastModifiedBy(resultSet.getString("LAST_MODIFIED_BY"));
			quoteItemDiscScaleVO.setLastModifiedTs(resultSet.getTimestamp("LAST_MODIFIED_TS"));
			quoteItemDiscScaleVO.setLowerBndryDt(resultSet.getTimestamp("LOWER_BNDRY_DT"));
			quoteItemDiscScaleVO.setProdLstPrcAm(resultSet.getDouble("PROD_LST_PRC_AM"));
			quoteItemDiscScaleVO.setScaleId(resultSet.getInt("SCALE_ID"));
			quoteItemDiscScaleVO.setSlsQtnId(resultSet.getLong("SLS_QTN_ID"));
			quoteItemDiscScaleVO.setSlsQtnItmSqnNr(resultSet.getString("SLS_QTN_ITM_SQN_NR"));
			quoteItemDiscScaleVO.setSlsQtnVrsnSqnNr(resultSet.getString("SLS_QTN_VRSN_SQN_NR"));
			quoteItemDiscScaleVO.setUpperBndryDt(resultSet.getTimestamp("UPPER_BNDRY_DT"));
		}catch(SQLException sqlEx){
			Logger.repError(logInfo, "createVO", "Exception while creating QuoteItemDiscScaleVO...", sqlEx);
			throw new ReplicationException(sqlEx.getMessage(), sqlEx);
		}

		return quoteItemDiscScaleVO;
	}

	private void deleteQuoteItemDiscScales(Connection con, long slsQtnID, String slsQtnVrsn,
			QuoteItemDiscScaleVO quoteItemDiscScaleVO) throws ReplicationException {
		updateQuoteItemDiscScales(con, slsQtnID, slsQtnVrsn, quoteItemDiscScaleVO);

	}


	private void updateQuoteItemDiscScales(Connection con, long slsQtnID, String slsQtnVrsn,
			QuoteItemDiscScaleVO quoteItemDiscScaleVO) throws ReplicationException {
		Logger.repDebug(logInfo, "updateQuoteItemDiscScales", "Begining of updating QuoteItemDiscScales details for quotation id "+ slsQtnID+
				" and sls version "+slsQtnVrsn);
		String updateQuery = null;
		try {
			updateQuery = queryRepository.getQuery("Quote","Update","QuoteItemDiscScale"); 
			List<Object> options = new ArrayList<Object>();

			options.add(quoteItemDiscScaleVO.getDiscTypeCd());
			options.add(String.valueOf(quoteItemDiscScaleVO.getProdLstPrcAm()));
			options.add(String.valueOf(quoteItemDiscScaleVO.getAuthBdNetPrcAm()));
			options.add(String.valueOf(quoteItemDiscScaleVO.getAuthDiscOtPc()));
			options.add(String.valueOf(quoteItemDiscScaleVO.getHighRslrASdPc()));
			options.add(ReplicationUtility.dateString(quoteItemDiscScaleVO.getUpperBndryDt()));
			options.add(ReplicationUtility.dateString(quoteItemDiscScaleVO.getLowerBndryDt()));
			options.add(quoteItemDiscScaleVO.getAuthFl());
			options.add(String.valueOf(quoteItemDiscScaleVO.getAuthFixDiscAm()));
			options.add(String.valueOf(quoteItemDiscScaleVO.getAuthIncrDiscAm()));
			options.add(String.valueOf(quoteItemDiscScaleVO.getDeleteInd()));
			options.add(ReplicationUtility.dateString(quoteItemDiscScaleVO.getCreatedTs()));
			options.add(quoteItemDiscScaleVO.getCreatedBy());
			options.add(ReplicationUtility.dateString(quoteItemDiscScaleVO.getLastModifiedTs()));
			options.add(quoteItemDiscScaleVO.getLastModifiedBy());
			options.add(String.valueOf(quoteItemDiscScaleVO.getExtAuthBdNetPrcAm()));

			//for where conditions
			options.add(String.valueOf(slsQtnID));
			options.add(slsQtnVrsn);
			options.add(quoteItemDiscScaleVO.getSlsQtnItmSqnNr());
			options.add(String.valueOf(quoteItemDiscScaleVO.getScaleId()));

			int rowUpdated = executeUpdatePreparedStatement(con,updateQuery,options);
			if(rowUpdated == 0){
				Logger.repDebug(logInfo, "updateQuoteItemDiscScales", "QuoteItemDiscScales doesn't exist for SLS_QTN_ID =  "+slsQtnID+ " and SLS_QTN_VRSN = "
						+slsQtnVrsn+" .Inserting record now.");
				insertQuoteItemDiscScales(quoteItemDiscScaleVO);
			}
		}
		catch (Exception ex) {
			Logger.repError(logInfo, "updateQuoteItemDiscScales", "Exception while updating QuoteItemDiscScales details...", ex);
			throw new ReplicationException(ex.getMessage(),ex);
		}
		
		Logger.repDebug(logInfo, "updateQuoteItemDiscScales", "End of updating QuoteItemDiscScales details for quotation id "+ slsQtnID+" and sls version "+slsQtnVrsn);

	}
	
	private void updateQuoteItemDiscScaleDeleteFlag(Connection con, long slsQtnID,
			String slsQtnVrsn) throws ReplicationException {
		String query = null;
		try {
			query = queryRepository.getQuery("Quote","Update","QuoteItemDiscScaleDeleteFlag"); 
			List<Object> options = new ArrayList<Object>();
			options.add(String.valueOf(slsQtnID));
			options.add(slsQtnVrsn);
			
			executeUpdatePreparedStatement(con,query,options);
		}
		catch (Exception ex) {
			Logger.repError(logInfo, "updateQuoteItemDiscScaleDeleteFlag", "Exception while updating QuoteItemDiscScaleDeleteFlag...", ex);
			throw new ReplicationException(ex.getMessage(),ex);
		}
				
		Logger.repDebug(logInfo, "updateQuoteItemDiscScaleDeleteFlag", "End of updating QuoteItemDiscScaleDeleteFlag for quotation id "+ slsQtnID +" and version "+slsQtnVrsn);
		
	}

	private void removeDeletedQuoteItemDiscScales(Connection con, long slsQtnID,
			String slsQtnVrsn) throws ReplicationException {
		String query = null;
		try {
			query = queryRepository.getQuery("Quote","Delete","DeleteQuoteItemDiscScales"); 
			List<Object> options = new ArrayList<Object>();
			options.add(String.valueOf(slsQtnID));
			options.add(slsQtnVrsn);
			
			executeUpdatePreparedStatement(con,query,options);
		}
		catch (Exception ex) {
			Logger.repError(logInfo, "removeDeletedQuoteItemDiscScales", "Exception while removing DeleteQuoteItemDiscScales...", ex);
			throw new ReplicationException(ex.getMessage(),ex);
		}
				
		Logger.repDebug(logInfo, "removeDeletedQuoteItemDiscScales", "End of removing DeleteQuoteItemDiscScales for quotation id "+ slsQtnID + " and version "+slsQtnVrsn);
		
	}

}
