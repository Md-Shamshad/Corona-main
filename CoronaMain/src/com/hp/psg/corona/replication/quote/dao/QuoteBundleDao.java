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
import com.hp.psg.corona.replication.quote.vo.QuoteBundleVO;

public class QuoteBundleDao extends QuoteConnection implements IBaseDao {
	QueryRepository queryRepository = null;
	String dataSourceName = null;
	LoggerInfo logInfo=null;

	public QuoteBundleDao() {
		queryRepository = ReplicationQueryManager.getInstance();
		dataSourceName = Config.getProperty("corona.replication.secondary.data.source.name");
		logInfo = new LoggerInfo (this.getClass().getName());
	}

	@Override
	public void create(Connection con, long slsQtnID, String slsQtnVrsn)
	throws ReplicationException {
		List<QuoteBundleVO> quoteBundles = getQuoteBundles(con,slsQtnID,slsQtnVrsn);
		if(quoteBundles!=null && quoteBundles.size()>0){
			for(QuoteBundleVO quoteBundleVO : quoteBundles){
				insertQuoteBundles(quoteBundleVO);
			}
		}
	}

	private void insertQuoteBundles(QuoteBundleVO quoteBundleVO) throws ReplicationException {
		Logger.repDebug(logInfo, "insertQuoteBundles", "Begining of inserting QuoteBundles details...");

		String insertQuery = null;
		Connection con = null;

		try {
			con = getConnection(dataSourceName);

			insertQuery = queryRepository.getQuery("Quote","Create","QuoteBundle"); 
			List<Object> options = new ArrayList<Object>();

			options.add(quoteBundleVO.getSlsQtnItmSqnNr());
			options.add(String.valueOf(quoteBundleVO.getSlsQtnId()));
			options.add(quoteBundleVO.getSlsQtnVrsnSqnNr());
			options.add(String.valueOf(quoteBundleVO.getScaleId()));
			options.add(String.valueOf(quoteBundleVO.getItemNr()));
			options.add(quoteBundleVO.getProductBaseNr());
			options.add(quoteBundleVO.getProdOptCd());
			options.add(String.valueOf(quoteBundleVO.getQty()));
			options.add(quoteBundleVO.getProductDescription());
			options.add(String.valueOf(quoteBundleVO.getListPrice()));
			options.add(String.valueOf(quoteBundleVO.getAuthBdNetPrcAm()));
			options.add(String.valueOf(quoteBundleVO.getAuthIncrDiscAm()));
			options.add(String.valueOf(quoteBundleVO.getVersionCreated()));
			options.add(quoteBundleVO.getAssocPl());
			options.add(String.valueOf(quoteBundleVO.getDiscAsPct()));
			options.add(quoteBundleVO.getDiscTypeCd());
			options.add(String.valueOf(quoteBundleVO.getAuthDiscOtPc()));
			options.add(quoteBundleVO.getBandedFl());
			options.add(String.valueOf(quoteBundleVO.getDeleteInd()));
			options.add(ReplicationUtility.dateString(quoteBundleVO.getCreatedTs()));
			options.add(quoteBundleVO.getCreatedBy());
			options.add(ReplicationUtility.dateString(quoteBundleVO.getLastModifiedTs()));
			options.add(quoteBundleVO.getLastModifiedBy());

			executeUpdatePreparedStatement(con,insertQuery,options);
		}
		catch (Exception ex) {
			Logger.repError(logInfo, "insertQuoteBundles", "Exception while inserting QuoteBundles details...", ex);
			throw new ReplicationException(ex.getMessage(),ex);
		}
		finally{
			closeConnection(con);
		}
		Logger.repDebug(logInfo, "insertQuoteBundles", "End of inserting QuoteBundles details.");

	}

	private List<QuoteBundleVO> getQuoteBundles(Connection con, long slsQtnID,
			String slsQtnVrsn) throws ReplicationException {
		Logger.repDebug(logInfo, "getQuoteBundles", "Begining of getting QuoteBundles details for quotation id "+ slsQtnID+
				" and sls version "+slsQtnVrsn);
		List<QuoteBundleVO> quoteBundles = new ArrayList<QuoteBundleVO>();
		QuoteBundleVO quoteBundleVO = null;
		String selectQuery = null;

		try {
			selectQuery = queryRepository.getQuery("Quote","Select","QuoteBundle"); 
			List<Object> options = new ArrayList<Object>();
			options.add(String.valueOf(slsQtnID));
			options.add(slsQtnVrsn);

			ResultSet resultSet = runPreparedStatement(con, selectQuery, options);
			while (resultSet.next()){
				quoteBundleVO = createVO(resultSet);
				quoteBundles.add(quoteBundleVO);
			}
		}catch(SQLException sqlEx){
			Logger.repError(logInfo, "getQuoteBundles", "Exception while getting QuoteBundles details...", sqlEx);
			throw new ReplicationException(sqlEx.getMessage(), sqlEx);
		}catch (Exception e) {
			Logger.repError(logInfo, "getQuoteBundles", "Exception while getting QuoteBundles details...", e);
			throw new ReplicationException(e.getMessage(), e);
		}

		Logger.repDebug(logInfo, "getQuoteBundles", "End of getting QuoteBundles details for quotation id "+ slsQtnID+
				" and sls version "+slsQtnVrsn);
		return quoteBundles;
	}

	private QuoteBundleVO createVO(ResultSet resultSet) throws ReplicationException {
		QuoteBundleVO quoteBundleVO = new QuoteBundleVO();

		try{
			quoteBundleVO.setAssocPl(resultSet.getString("ASSOC_PL"));
			quoteBundleVO.setAuthBdNetPrcAm(resultSet.getDouble("AUTH_BD_NET_PRC_AM"));
			quoteBundleVO.setAuthDiscOtPc(resultSet.getDouble("AUTH_DISC_OT_PC"));
			quoteBundleVO.setAuthIncrDiscAm(resultSet.getDouble("AUTH_INCR_DISC_AM"));
			quoteBundleVO.setBandedFl(resultSet.getString("BANDED_FL"));
			quoteBundleVO.setCreatedBy(resultSet.getString("CREATED_BY"));
			quoteBundleVO.setCreatedTs(resultSet.getTimestamp("CREATED_TS"));
			quoteBundleVO.setDeleteInd((resultSet.getString("DELETE_IND")).charAt(0));
			quoteBundleVO.setDiscAsPct(resultSet.getDouble("DISC_AS_PCT"));
			quoteBundleVO.setDiscTypeCd(resultSet.getString("DISC_TYPE_CD"));
			quoteBundleVO.setItemNr(resultSet.getLong("ITEM_NR"));
			quoteBundleVO.setLastModifiedBy(resultSet.getString("LAST_MODIFIED_BY"));
			quoteBundleVO.setLastModifiedTs(resultSet.getTimestamp("LAST_MODIFIED_TS"));
			quoteBundleVO.setListPrice(resultSet.getDouble("LIST_PRICE"));
			quoteBundleVO.setProdOptCd(resultSet.getString("PROD_OPT_CD"));
			quoteBundleVO.setProductBaseNr(resultSet.getString("PRODUCT_BASE_NR"));
			quoteBundleVO.setProductDescription(resultSet.getString("PRODUCT_DESCRIPTION"));
			quoteBundleVO.setQty(resultSet.getLong("QTY"));
			quoteBundleVO.setScaleId(resultSet.getInt("SCALE_ID"));
			quoteBundleVO.setSlsQtnId(resultSet.getLong("SLS_QTN_ID"));
			quoteBundleVO.setSlsQtnItmSqnNr(resultSet.getString("SLS_QTN_ITM_SQN_NR"));
			quoteBundleVO.setSlsQtnVrsnSqnNr(resultSet.getString("SLS_QTN_VRSN_SQN_NR"));
			quoteBundleVO.setVersionCreated(resultSet.getLong("VERSION_CREATED"));
		}catch(SQLException sqlEx){
			Logger.repError(logInfo, "createVO", "Exception while creating QuoteBundleVO...", sqlEx);
			throw new ReplicationException(sqlEx.getMessage(), sqlEx);
		}
		return quoteBundleVO;
	}

	@Override
	public void update(Connection con, long slsQtnID, String slsQtnVrsn)
	throws ReplicationException {
		
		Connection dstCon = null;		
		try {
			dstCon = getConnection(dataSourceName);
			
			updateQuoteBundleDeleteFlag(dstCon, slsQtnID, slsQtnVrsn);
		
			List<QuoteBundleVO> quoteBundles = getQuoteBundles(con,slsQtnID,slsQtnVrsn);
			if(quoteBundles!=null && quoteBundles.size()>0){
				for(QuoteBundleVO quoteBundleVO : quoteBundles){
					updateQuoteBundles(dstCon, slsQtnID, slsQtnVrsn, quoteBundleVO);
				}
			}
		
			removeDeletedQuoteBundles(dstCon, slsQtnID, slsQtnVrsn);
		}
		catch (Exception ex) {
			Logger.repError(logInfo, "update", "Exception while updating QuoteBundles details...", ex);
			throw new ReplicationException(ex.getMessage(),ex);
		}
		finally{
			closeConnection(dstCon);
		}
	}

	private void updateQuoteBundles(Connection con, long slsQtnID, String slsQtnVrsn,
			QuoteBundleVO quoteBundleVO) throws ReplicationException {
		Logger.repDebug(logInfo, "updateQuoteBundles", "Begining of updating QuoteBundles details for quotation id "+ slsQtnID+
				" and sls version "+slsQtnVrsn);
		String updateQuery = null;
		try {
			updateQuery = queryRepository.getQuery("Quote","Update","QuoteBundle"); 
			List<Object> options = new ArrayList<Object>();

			options.add(quoteBundleVO.getProductBaseNr());
			options.add(quoteBundleVO.getProdOptCd());
			options.add(String.valueOf(quoteBundleVO.getQty()));
			options.add(quoteBundleVO.getProductDescription());
			options.add(String.valueOf(quoteBundleVO.getListPrice()));
			options.add(String.valueOf(quoteBundleVO.getAuthBdNetPrcAm()));
			options.add(String.valueOf(quoteBundleVO.getAuthIncrDiscAm()));
			options.add(String.valueOf(quoteBundleVO.getVersionCreated()));
			options.add(quoteBundleVO.getAssocPl());
			options.add(String.valueOf(quoteBundleVO.getDiscAsPct()));
			options.add(quoteBundleVO.getDiscTypeCd());
			options.add(String.valueOf(quoteBundleVO.getAuthDiscOtPc()));
			options.add(quoteBundleVO.getBandedFl());
			options.add(String.valueOf(quoteBundleVO.getDeleteInd()));
			options.add(ReplicationUtility.dateString(quoteBundleVO.getCreatedTs()));
			options.add(quoteBundleVO.getCreatedBy());
			options.add(ReplicationUtility.dateString(quoteBundleVO.getLastModifiedTs()));
			options.add(quoteBundleVO.getLastModifiedBy());

			//for where conditions
			options.add(String.valueOf(slsQtnID));
			options.add(slsQtnVrsn);
			options.add(quoteBundleVO.getSlsQtnItmSqnNr());
			options.add(String.valueOf(quoteBundleVO.getScaleId()));
			options.add(String.valueOf(quoteBundleVO.getItemNr()));

			int rowUpdated = executeUpdatePreparedStatement(con,updateQuery,options);
			if(rowUpdated == 0){
				Logger.repDebug(logInfo, "updateQuoteBundles", "QuoteBundles doesn't exist for SLS_QTN_ID =  "+slsQtnID+ " and SLS_QTN_VRSN = "
						+slsQtnVrsn+" .Inserting record now.");
				insertQuoteBundles(quoteBundleVO);
			}
		}
		catch (Exception ex) {
			Logger.repError(logInfo, "updateQuoteBundles", "Exception while updating QuoteBundles details...", ex);
			throw new ReplicationException(ex.getMessage(),ex);
		}
		
		Logger.repDebug(logInfo, "updateQuoteBundles", "End of updating QuoteBundles details for quotation id "+ slsQtnID+" and sls version "+slsQtnVrsn);

	}

	@Override
	public void delete(Connection con, long slsQtnID, String slsQtnVrsn)
	throws ReplicationException {
		List<QuoteBundleVO> quoteBundles = getQuoteBundles(con,slsQtnID,slsQtnVrsn);
		if(quoteBundles!=null && quoteBundles.size()>0){
			Connection dstCon = null;		
			try {
				dstCon = getConnection(dataSourceName);
				
				for(QuoteBundleVO quoteBundleVO : quoteBundles){
					deleteQuoteBundles(dstCon, slsQtnID, slsQtnVrsn, quoteBundleVO);
				}
			}
			catch (Exception ex) {
				Logger.repError(logInfo, "deleteQuoteBundles", "Exception while deleting QuoteBundles details...", ex);
				throw new ReplicationException(ex.getMessage(),ex);
			}
			finally{
				closeConnection(dstCon);
			}
		}

	}

	private void deleteQuoteBundles(Connection con, long slsQtnID, String slsQtnVrsn,
			QuoteBundleVO quoteBundleVO) throws ReplicationException {
			updateQuoteBundles(con, slsQtnID, slsQtnVrsn, quoteBundleVO);
	}

	@Override
	public void change(Connection con, long slsQtnID, String slsQtnVrsn)
	throws ReplicationException {
		Logger.repDebug(logInfo, "change", "Incorrectly called change for QUOTE_BUNDLE TABLE");

	}
	
	private void updateQuoteBundleDeleteFlag(Connection con, long slsQtnID, String slsQtnVrsn) throws ReplicationException {
		String query = null;
	
		try {
			query = queryRepository.getQuery("Quote","Update","QuoteBundleDeleteFlag"); 
			List<Object> options = new ArrayList<Object>();
			options.add(String.valueOf(slsQtnID));
			options.add(slsQtnVrsn);
			
			executeUpdatePreparedStatement(con,query,options);
		}
		catch (Exception ex) {
			Logger.repError(logInfo, "updateQuoteBundleDeleteFlag", "Exception while updating QuoteBundleDeleteFlag...", ex);
			throw new ReplicationException(ex.getMessage(),ex);
		}
			
		Logger.repDebug(logInfo, "updateQuoteBundleDeleteFlag", "End of updating QuoteBundleDeleteFlag for quotation id "+ slsQtnID +" and version "+slsQtnVrsn);		
	}

	private void removeDeletedQuoteBundles(Connection con, long slsQtnID, String slsQtnVrsn) throws ReplicationException {
		String query = null;
		try {
			query = queryRepository.getQuery("Quote","Delete","DeleteQuoteBundles"); 
			List<Object> options = new ArrayList<Object>();
			options.add(String.valueOf(slsQtnID));
			options.add(slsQtnVrsn);
			
			executeUpdatePreparedStatement(con,query,options);
		}
		catch (Exception ex) {
			Logger.repError(logInfo, "removeDeletedQuoteBundles", "Exception while removing DeleteQuoteBundles...", ex);
			throw new ReplicationException(ex.getMessage(),ex);
		}
			
		Logger.repDebug(logInfo, "removeDeletedQuoteBundles", "End of removing DeleteQuoteBundles for quotation id "+ slsQtnID + " and version "+slsQtnVrsn);	
	}

}
