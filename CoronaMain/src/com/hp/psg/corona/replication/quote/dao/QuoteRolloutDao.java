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
import com.hp.psg.corona.replication.quote.vo.QuoteRollOutVO;

public class QuoteRolloutDao extends QuoteConnection implements IBaseDao {
	QueryRepository queryRepository = null;
	String dataSourceName = null;
	LoggerInfo logInfo=null;


	public QuoteRolloutDao() {
		queryRepository = ReplicationQueryManager.getInstance();
		dataSourceName = Config.getProperty("corona.replication.secondary.data.source.name");
		logInfo = new LoggerInfo (this.getClass().getName());
	}

	@Override
	public void create(Connection con, long slsQtnID, String slsQtnVrsn)
	throws ReplicationException {
		List<QuoteRollOutVO> quoteRollOuts = getQuoteRollOuts(con,slsQtnID,slsQtnVrsn);
		if(quoteRollOuts!=null && quoteRollOuts.size()>0){
			for(QuoteRollOutVO quoteRollOutVO : quoteRollOuts){
				insertQuoteRollOuts(quoteRollOutVO);
			}
		}

	}

	private void insertQuoteRollOuts(QuoteRollOutVO quoteRollOutVO) throws ReplicationException {
		Logger.repDebug(logInfo, "insertQuoteRollOuts", "Begining of inserting QuoteRollOuts details...");

		String insertQuery = null;
		Connection con = null;

		try {
			con = getConnection(dataSourceName);

			insertQuery = queryRepository.getQuery("Quote","Create","QuoteRollOut"); 
			List<Object> options = new ArrayList<Object>();

			options.add(quoteRollOutVO.getSlsQtnItmSqnNr());
			options.add(String.valueOf(quoteRollOutVO.getSlsQtnId()));
			options.add(quoteRollOutVO.getSlsQtnVrsnSqnNr());
			options.add(quoteRollOutVO.getLoadType());
			options.add(String.valueOf(quoteRollOutVO.getEstQty()));
			options.add(String.valueOf(quoteRollOutVO.getMth1Qty()));
			options.add(String.valueOf(quoteRollOutVO.getMth2Qty()));
			options.add(String.valueOf(quoteRollOutVO.getMth3Qty()));
			options.add(String.valueOf(quoteRollOutVO.getMth4Qty()));
			options.add(String.valueOf(quoteRollOutVO.getMth5Qty()));
			options.add(String.valueOf(quoteRollOutVO.getMth6Qty()));
			options.add(String.valueOf(quoteRollOutVO.getMth7Qty()));
			options.add(String.valueOf(quoteRollOutVO.getMth8Qty()));
			options.add(String.valueOf(quoteRollOutVO.getMth9Qty()));
			options.add(String.valueOf(quoteRollOutVO.getMth10Qty()));
			options.add(String.valueOf(quoteRollOutVO.getMth11Qty()));
			options.add(String.valueOf(quoteRollOutVO.getMth12Qty()));
			options.add(String.valueOf(quoteRollOutVO.getMth13Qty()));
			options.add(String.valueOf(quoteRollOutVO.getMth14Qty()));
			options.add(String.valueOf(quoteRollOutVO.getMth15Qty()));
			options.add(String.valueOf(quoteRollOutVO.getRslrMaxQty()));
			options.add(String.valueOf(quoteRollOutVO.getOrdrMinQty()));
			options.add(String.valueOf(quoteRollOutVO.getLineMaxQty()));
			options.add(String.valueOf(quoteRollOutVO.getRslrMinQty()));
			options.add(String.valueOf(quoteRollOutVO.getDeleteInd()));
			options.add(ReplicationUtility.dateString(quoteRollOutVO.getCreatedTs()));
			options.add(quoteRollOutVO.getCreatedBy());
			options.add(ReplicationUtility.dateString(quoteRollOutVO.getLastModifiedTs()));
			options.add(quoteRollOutVO.getLastModifiedBy());


			executeUpdatePreparedStatement(con,insertQuery,options);
		}
		catch (Exception ex) {
			Logger.repError(logInfo, "insertQuoteRollOuts", "Exception while inserting QuoteRollOuts details...", ex);
			throw new ReplicationException(ex.getMessage(),ex);
		}
		finally{
			closeConnection(con);
		}
		Logger.repDebug(logInfo, "insertQuoteRollOuts", "End of inserting QuoteRollOuts details.");

	}

	private List<QuoteRollOutVO> getQuoteRollOuts(Connection con,
			long slsQtnID, String slsQtnVrsn) throws ReplicationException {
		Logger.repDebug(logInfo, "getQuoteRollOuts", "Begining of getting QuoteRollOuts details for quotation id "+ slsQtnID+
				" and sls version "+slsQtnVrsn);
		List<QuoteRollOutVO> quoteRollouts = new ArrayList<QuoteRollOutVO>();
		QuoteRollOutVO quoteRollOutVO = null;
		String selectQuery = null;

		try {
			selectQuery = queryRepository.getQuery("Quote","Select","QuoteRollOut"); 
			List<Object> options = new ArrayList<Object>();
			options.add(String.valueOf(slsQtnID));
			options.add(slsQtnVrsn);

			ResultSet resultSet = runPreparedStatement(con, selectQuery, options);
			while (resultSet.next()){
				quoteRollOutVO = createVO(resultSet);
				quoteRollouts.add(quoteRollOutVO);
			}
		}catch(SQLException sqlEx){
			Logger.repError(logInfo, "getQuoteRollOuts", "Exception while getting QuoteRollOuts details...", sqlEx);
			throw new ReplicationException(sqlEx.getMessage(), sqlEx);
		}catch (Exception e) {
			Logger.repError(logInfo, "getQuoteRollOuts", "Exception while getting QuoteRollOuts details...", e);
			throw new ReplicationException(e.getMessage(), e);
		}

		Logger.repDebug(logInfo, "getQuoteRollOuts", "End of getting QuoteRollOuts details for quotation id "+ slsQtnID+
				" and sls version "+slsQtnVrsn);
		return quoteRollouts;
	}

	private QuoteRollOutVO createVO(ResultSet resultSet) throws ReplicationException {
		QuoteRollOutVO quoteRollOutVO = new QuoteRollOutVO();

		try{
			quoteRollOutVO.setCreatedBy(resultSet.getString("CREATED_BY"));
			quoteRollOutVO.setCreatedTs(resultSet.getTimestamp("CREATED_TS"));
			quoteRollOutVO.setDeleteInd((resultSet.getString("DELETE_IND")).charAt(0));
			quoteRollOutVO.setEstQty(resultSet.getLong("EST_QTY"));
			quoteRollOutVO.setLastModifiedBy(resultSet.getString("LAST_MODIFIED_BY"));
			quoteRollOutVO.setLastModifiedTs(resultSet.getTimestamp("LAST_MODIFIED_TS"));
			quoteRollOutVO.setLineMaxQty(resultSet.getLong("LINE_MAX_QTY"));
			quoteRollOutVO.setLoadType(resultSet.getString("LOAD_TYPE"));
			quoteRollOutVO.setMth10Qty(resultSet.getLong("MTH10_QTY"));
			quoteRollOutVO.setMth11Qty(resultSet.getLong("MTH11_QTY"));
			quoteRollOutVO.setMth12Qty(resultSet.getLong("MTH12_QTY"));
			quoteRollOutVO.setMth13Qty(resultSet.getLong("MTH13_QTY"));
			quoteRollOutVO.setMth14Qty(resultSet.getLong("MTH14_QTY"));
			quoteRollOutVO.setMth15Qty(resultSet.getLong("MTH15_QTY"));
			quoteRollOutVO.setMth1Qty(resultSet.getLong("MTH1_QTY"));
			quoteRollOutVO.setMth2Qty(resultSet.getLong("MTH2_QTY"));
			quoteRollOutVO.setMth3Qty(resultSet.getLong("MTH3_QTY"));
			quoteRollOutVO.setMth4Qty(resultSet.getLong("MTH4_QTY"));
			quoteRollOutVO.setMth5Qty(resultSet.getLong("MTH5_QTY"));
			quoteRollOutVO.setMth6Qty(resultSet.getLong("MTH6_QTY"));
			quoteRollOutVO.setMth7Qty(resultSet.getLong("MTH7_QTY"));
			quoteRollOutVO.setMth8Qty(resultSet.getLong("MTH8_QTY"));
			quoteRollOutVO.setMth9Qty(resultSet.getLong("MTH9_QTY"));
			quoteRollOutVO.setOrdrMinQty(resultSet.getLong("ORDR_MIN_QTY"));
			quoteRollOutVO.setRslrMaxQty(resultSet.getLong("RSLR_MAX_QTY"));
			quoteRollOutVO.setRslrMinQty(resultSet.getLong("RSLR_MIN_QTY"));
			quoteRollOutVO.setSlsQtnId(resultSet.getLong("SLS_QTN_ID"));
			quoteRollOutVO.setSlsQtnItmSqnNr(resultSet.getString("SLS_QTN_ITM_SQN_NR"));
			quoteRollOutVO.setSlsQtnVrsnSqnNr(resultSet.getString("SLS_QTN_VRSN_SQN_NR"));
		}catch(SQLException sqlEx){
			Logger.repError(logInfo, "createVO", "Exception while creating QuoteRollOutVO...", sqlEx);
			throw new ReplicationException(sqlEx.getMessage(), sqlEx);
		}


		return quoteRollOutVO;
	}

	@Override
	public void update(Connection con, long slsQtnID, String slsQtnVrsn)
	throws ReplicationException {
		Connection dstCon = null;

		try {
			dstCon = getConnection(dataSourceName);
			updateQuoteRollOutDeleteFlag(dstCon,slsQtnID, slsQtnVrsn);

			List<QuoteRollOutVO> quoteRollOuts = getQuoteRollOuts(con,slsQtnID,slsQtnVrsn);
			if(quoteRollOuts!=null && quoteRollOuts.size()>0){
				for(QuoteRollOutVO quoteRollOutVO : quoteRollOuts){
					updateQuoteRollOuts(dstCon,slsQtnID, slsQtnVrsn, quoteRollOutVO);
				}
			}
			removeDeletedQuoteRollOuts(dstCon,slsQtnID, slsQtnVrsn);
		}
		catch (Exception ex) {
			Logger.repError(logInfo, "update", "Exception while updating quote rollouts details...", ex);
			throw new ReplicationException(ex.getMessage(),ex);
		}
		finally{
			closeConnection(dstCon);
		}
	}

	private void updateQuoteRollOuts(Connection con, long slsQtnID, String slsQtnVrsn,
			QuoteRollOutVO quoteRollOutVO) throws ReplicationException {
		Logger.repDebug(logInfo, "updateQuoteRollOuts", "Begining of updating QuoteRollOuts details for quotation id "+ slsQtnID+
				" and sls version "+slsQtnVrsn);
		String updateQuery = null;
		try {
			updateQuery = queryRepository.getQuery("Quote","Update","QuoteRollOut"); 
			List<Object> options = new ArrayList<Object>();

			options.add(quoteRollOutVO.getLoadType());
			options.add(String.valueOf(quoteRollOutVO.getEstQty()));
			options.add(String.valueOf(quoteRollOutVO.getMth1Qty()));
			options.add(String.valueOf(quoteRollOutVO.getMth2Qty()));
			options.add(String.valueOf(quoteRollOutVO.getMth3Qty()));
			options.add(String.valueOf(quoteRollOutVO.getMth4Qty()));
			options.add(String.valueOf(quoteRollOutVO.getMth5Qty()));
			options.add(String.valueOf(quoteRollOutVO.getMth6Qty()));
			options.add(String.valueOf(quoteRollOutVO.getMth7Qty()));
			options.add(String.valueOf(quoteRollOutVO.getMth8Qty()));
			options.add(String.valueOf(quoteRollOutVO.getMth9Qty()));
			options.add(String.valueOf(quoteRollOutVO.getMth10Qty()));
			options.add(String.valueOf(quoteRollOutVO.getMth11Qty()));
			options.add(String.valueOf(quoteRollOutVO.getMth12Qty()));
			options.add(String.valueOf(quoteRollOutVO.getMth13Qty()));
			options.add(String.valueOf(quoteRollOutVO.getMth14Qty()));
			options.add(String.valueOf(quoteRollOutVO.getMth15Qty()));
			options.add(String.valueOf(quoteRollOutVO.getRslrMaxQty()));
			options.add(String.valueOf(quoteRollOutVO.getOrdrMinQty()));
			options.add(String.valueOf(quoteRollOutVO.getLineMaxQty()));
			options.add(String.valueOf(quoteRollOutVO.getRslrMinQty()));
			options.add(String.valueOf(quoteRollOutVO.getDeleteInd()));
			options.add(ReplicationUtility.dateString(quoteRollOutVO.getCreatedTs()));
			options.add(quoteRollOutVO.getCreatedBy());
			options.add(ReplicationUtility.dateString(quoteRollOutVO.getLastModifiedTs()));
			options.add(quoteRollOutVO.getLastModifiedBy());

			//for where conditions
			options.add(String.valueOf(slsQtnID));
			options.add(slsQtnVrsn);
			options.add(quoteRollOutVO.getSlsQtnItmSqnNr());

			int rowUpdated = executeUpdatePreparedStatement(con,updateQuery,options);
			if(rowUpdated == 0){
				Logger.repDebug(logInfo, "updateQuoteRollOuts", "QuoteRollOuts doesn't exist for SLS_QTN_ID =  "+slsQtnID+ " and SLS_QTN_VRSN = "
						+slsQtnVrsn+" .Inserting record now.");
				insertQuoteRollOuts(quoteRollOutVO);
			}
		}
		catch (Exception ex) {
			Logger.repError(logInfo, "updateQuoteRollOuts", "Exception while updating QuoteRollOuts details...", ex);
			throw new ReplicationException(ex.getMessage(),ex);
		}
		Logger.repDebug(logInfo, "updateQuoteRollOuts", "End of updating QuoteRollOuts details for quotation id "+ slsQtnID+
				" and sls version "+slsQtnVrsn);

	}

	@Override
	public void delete(Connection con, long slsQtnID, String slsQtnVrsn)
	throws ReplicationException {
		List<QuoteRollOutVO> quoteRollOuts = getQuoteRollOuts(con,slsQtnID,slsQtnVrsn);
		if(quoteRollOuts!=null && quoteRollOuts.size()>0){
			Connection dstCon = null;

			try {
				dstCon = getConnection(dataSourceName);
				for(QuoteRollOutVO quoteRollOutVO : quoteRollOuts){
					deleteQuoteRollOuts(dstCon,slsQtnID, slsQtnVrsn, quoteRollOutVO);
				}
			}
			catch (Exception ex) {
				Logger.repError(logInfo, "deleteQuoteRollOuts", "Exception while deleting quote rollouts details...", ex);
				throw new ReplicationException(ex.getMessage(),ex);
			}
			finally{
				closeConnection(dstCon);
			}
		}

	}

	private void deleteQuoteRollOuts(Connection dstCon, long slsQtnID, String slsQtnVrsn,
			QuoteRollOutVO quoteRollOutVO) throws ReplicationException {
		updateQuoteRollOuts(dstCon, slsQtnID, slsQtnVrsn, quoteRollOutVO);
	}


	@Override
	public void change(Connection con, long slsQtnID, String slsQtnVrsn)
	throws ReplicationException {
		Logger.repDebug(logInfo, "change", "Incorrectly called change for QUOTE_ROLLOUTS TABLE");

	}


	private void updateQuoteRollOutDeleteFlag(Connection con,long slsQtnID, String slsQtnVrsn) throws ReplicationException {
		String query = null;
		try {
			query = queryRepository.getQuery("Quote","Update","QuoteRollOutDeleteFlag"); 
			List<Object> options = new ArrayList<Object>();
			options.add(String.valueOf(slsQtnID));
			options.add(slsQtnVrsn);

			executeUpdatePreparedStatement(con,query,options);
		}
		catch (Exception ex) {
			Logger.repError(logInfo, "updateQuoteRollOutDeleteFlag", "Exception while updating QuoteRollOutDeleteFlag...", ex);
			throw new ReplicationException(ex.getMessage(),ex);
		}
		Logger.repDebug(logInfo, "updateQuoteRollOutDeleteFlag", "End of updating QuoteRollOutDeleteFlag for quotation id "+ slsQtnID +" and version "
				+slsQtnVrsn);

	}

	private void removeDeletedQuoteRollOuts(Connection con, long slsQtnID, String slsQtnVrsn) throws ReplicationException {
		String query = null;
		try {
			query = queryRepository.getQuery("Quote","Delete","DeleteQuoteRollOuts"); 
			List<Object> options = new ArrayList<Object>();
			options.add(String.valueOf(slsQtnID));
			options.add(slsQtnVrsn);

			executeUpdatePreparedStatement(con,query,options);
		}
		catch (Exception ex) {
			Logger.repError(logInfo, "removeDeletedQuoteRollOuts", "Exception while removing DeleteQuoteRollOuts...", ex);
			throw new ReplicationException(ex.getMessage(),ex);
		}
		Logger.repDebug(logInfo, "removeDeletedQuoteRollOuts", "End of removing DeleteQuoteRollOuts for quotation id "+ slsQtnID + " and version "
				+slsQtnVrsn);
	}

}
