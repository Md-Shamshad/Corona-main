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
import com.hp.psg.corona.replication.quote.vo.QuoteNcGroupsVO;

public class QuoteNcgroupsDao extends QuoteConnection implements IBaseDao {
	QueryRepository queryRepository = null;
	String dataSourceName = null;
	LoggerInfo logInfo=null;


	public QuoteNcgroupsDao() {
		queryRepository = ReplicationQueryManager.getInstance();
		dataSourceName = Config.getProperty("corona.replication.secondary.data.source.name");
		logInfo = new LoggerInfo (this.getClass().getName());
	}

	@Override
	public void create(Connection con, long slsQtnID, String slsQtnVrsn)
	throws ReplicationException {
		List<QuoteNcGroupsVO> quoteNcGroups = getQuoteNcgroups(con,slsQtnID,slsQtnVrsn);
		if(quoteNcGroups!=null && quoteNcGroups.size()>0){
			for(QuoteNcGroupsVO quoteNcGroupsVO : quoteNcGroups){
				insertQuoteNcgroup(quoteNcGroupsVO);
			}
		}

	}

	private void insertQuoteNcgroup(QuoteNcGroupsVO quoteNcGroupsVO) throws ReplicationException {
		Logger.repDebug(logInfo, "insertQuoteNcgroup", "Begining of inserting QuoteNcgroup details...");

		String insertQuery = null;
		Connection con = null;

		try {
			con = getConnection(dataSourceName);

			insertQuery = queryRepository.getQuery("Quote","Create","QuoteNcgroups"); 
			List<Object> options = new ArrayList<Object>();

			options.add(quoteNcGroupsVO.getSlsQtnItmSqnNr());
			options.add(String.valueOf(quoteNcGroupsVO.getSlsQtnId()));
			options.add(quoteNcGroupsVO.getSlsQtnVrsnSqnNr());
			options.add(String.valueOf(quoteNcGroupsVO.getGroupId()));
			options.add(quoteNcGroupsVO.getGroupDesc());
			options.add(String.valueOf(quoteNcGroupsVO.getQty()));
			options.add(String.valueOf(quoteNcGroupsVO.getDeleteInd()));
			options.add(ReplicationUtility.dateString(quoteNcGroupsVO.getCreatedTs()));
			options.add(quoteNcGroupsVO.getCreatedBy());
			options.add(ReplicationUtility.dateString(quoteNcGroupsVO.getLastModifiedTs()));
			options.add(quoteNcGroupsVO.getLastModifiedBy());


			executeUpdatePreparedStatement(con,insertQuery,options);
		}
		catch (Exception ex) {
			Logger.repError(logInfo, "insertQuoteNcgroup", "Exception while inserting QuoteNcgroup details...", ex);
			throw new ReplicationException(ex.getMessage(),ex);
		}
		finally{
			closeConnection(con);
		}
		Logger.repDebug(logInfo, "insertQuoteNcgroup", "End of inserting QuoteNcgroup details.");

	}

	private List<QuoteNcGroupsVO> getQuoteNcgroups(Connection con,
			long slsQtnID, String slsQtnVrsn) throws ReplicationException {
		Logger.repDebug(logInfo, "getQuoteNcgroups", "Begining of getting QuoteNcgroups details for quotation id "+ slsQtnID+
				" and sls version "+slsQtnVrsn);
		List<QuoteNcGroupsVO> quoteNcgroups = new ArrayList<QuoteNcGroupsVO>();
		QuoteNcGroupsVO quoteNcGroupsVO = null;
		String selectQuery = null;

		try {
			selectQuery = queryRepository.getQuery("Quote","Select","QuoteNcgroups"); 
			List<Object> options = new ArrayList<Object>();
			options.add(String.valueOf(slsQtnID));
			options.add(slsQtnVrsn);

			ResultSet resultSet = runPreparedStatement(con, selectQuery, options);
			while (resultSet.next()){
				quoteNcGroupsVO = createVO(resultSet);
				quoteNcgroups.add(quoteNcGroupsVO);
			}
		}catch(SQLException sqlEx){
			Logger.repError(logInfo, "getQuoteNcgroups", "Exception while getting QuoteNcgroups details...", sqlEx);
			throw new ReplicationException(sqlEx.getMessage(), sqlEx);
		}catch (Exception e) {
			Logger.repError(logInfo, "getQuoteNcgroups", "Exception while getting QuoteNcgroups details...", e);
			throw new ReplicationException(e.getMessage(), e);
		}

		Logger.repDebug(logInfo, "getQuoteNcgroups", "End of getting QuoteNcgroups details for quotation id "+ slsQtnID+
				" and sls version "+slsQtnVrsn);
		return quoteNcgroups;
	}

	private QuoteNcGroupsVO createVO(ResultSet resultSet) throws ReplicationException {
		QuoteNcGroupsVO quoteNcGroupsVO = new QuoteNcGroupsVO();
		try{
			quoteNcGroupsVO.setCreatedBy(resultSet.getString("CREATED_BY"));
			quoteNcGroupsVO.setCreatedTs(resultSet.getTimestamp("CREATED_TS"));
			quoteNcGroupsVO.setDeleteInd((resultSet.getString("DELETE_IND")).charAt(0));
			quoteNcGroupsVO.setGroupDesc(resultSet.getString("GROUP_DESC"));
			quoteNcGroupsVO.setGroupId(resultSet.getLong("GROUP_ID"));
			quoteNcGroupsVO.setLastModifiedBy(resultSet.getString("LAST_MODIFIED_BY"));
			quoteNcGroupsVO.setLastModifiedTs(resultSet.getTimestamp("LAST_MODIFIED_TS"));
			quoteNcGroupsVO.setQty(resultSet.getLong("QTY"));
			quoteNcGroupsVO.setSlsQtnId(resultSet.getLong("SLS_QTN_ID"));
			quoteNcGroupsVO.setSlsQtnItmSqnNr(resultSet.getString("SLS_QTN_ITM_SQN_NR"));
			quoteNcGroupsVO.setSlsQtnVrsnSqnNr(resultSet.getString("SLS_QTN_VRSN_SQN_NR"));
		}catch(SQLException sqlEx){
			Logger.repError(logInfo, "createVO", "Exception while creating QuoteNcGroupsVO...", sqlEx);
			throw new ReplicationException(sqlEx.getMessage(), sqlEx);
		}


		return quoteNcGroupsVO;
	}

	@Override
	public void update(Connection con, long slsQtnID, String slsQtnVrsn)
	throws ReplicationException {
		Connection dstCon = null;
		try {
			dstCon = getConnection(dataSourceName);
			updateQuoteNcGroupsDeleteFlag(dstCon,slsQtnID, slsQtnVrsn);

			List<QuoteNcGroupsVO> quoteNcGroups = getQuoteNcgroups(con,slsQtnID,slsQtnVrsn);
			if(quoteNcGroups!=null && quoteNcGroups.size()>0){
				for(QuoteNcGroupsVO quoteNcGroupsVO : quoteNcGroups){
					updateQuoteNcgroup(dstCon,slsQtnID, slsQtnVrsn, quoteNcGroupsVO);
				}
			}
			removeDeletedQuoteNcGroups(dstCon,slsQtnID, slsQtnVrsn);
		}
		catch (Exception ex) {
			Logger.repError(logInfo, "update", "Exception while updating QuoteMultiCountry details...", ex);
			throw new ReplicationException(ex.getMessage(),ex);
		}
		finally{
			closeConnection(dstCon);
		}
	}

	private void updateQuoteNcgroup(Connection con,long slsQtnID, String slsQtnVrsn,
			QuoteNcGroupsVO quoteNcGroupsVO) throws ReplicationException {
		Logger.repDebug(logInfo, "updateQuoteNcgroup", "Begining of updating QuoteNcgroup details for quotation id "+ slsQtnID+
				" and sls version "+slsQtnVrsn);
		String updateQuery = null;
		try {
			updateQuery = queryRepository.getQuery("Quote","Update","QuoteNcgroups"); 
			List<Object> options = new ArrayList<Object>();

			options.add(quoteNcGroupsVO.getGroupDesc());
			options.add(String.valueOf(quoteNcGroupsVO.getQty()));
			options.add(String.valueOf(quoteNcGroupsVO.getDeleteInd()));
			options.add(ReplicationUtility.dateString(quoteNcGroupsVO.getCreatedTs()));
			options.add(quoteNcGroupsVO.getCreatedBy());
			options.add(ReplicationUtility.dateString(quoteNcGroupsVO.getLastModifiedTs()));
			options.add(quoteNcGroupsVO.getLastModifiedBy());

			//for where conditions
			options.add(String.valueOf(slsQtnID));
			options.add(slsQtnVrsn);
			options.add(quoteNcGroupsVO.getSlsQtnItmSqnNr());
			options.add(String.valueOf(quoteNcGroupsVO.getGroupId()));

			int rowUpdated = executeUpdatePreparedStatement(con,updateQuery,options);
			if(rowUpdated == 0){
				Logger.repDebug(logInfo, "updateQuoteNcgroup", "QuoteNcgroup doesn't exist for SLS_QTN_ID =  "+slsQtnID+ " and SLS_QTN_VRSN = "
						+slsQtnVrsn+" .Inserting record now.");
				insertQuoteNcgroup(quoteNcGroupsVO);
			}
		}
		catch (Exception ex) {
			Logger.repError(logInfo, "updateQuoteNcgroup", "Exception while updating QuoteNcgroup details...", ex);
			throw new ReplicationException(ex.getMessage(),ex);
		}
		Logger.repDebug(logInfo, "updateQuoteNcgroup", "End of updating QuoteNcgroup details for quotation id "+ slsQtnID+
				" and sls version "+slsQtnVrsn);
	}

	@Override
	public void delete(Connection con, long slsQtnID, String slsQtnVrsn)
	throws ReplicationException {
		List<QuoteNcGroupsVO> quoteNcGroups = getQuoteNcgroups(con,slsQtnID,slsQtnVrsn);
		if(quoteNcGroups!=null && quoteNcGroups.size()>0){
			Connection dstCon = null;
			try {
				dstCon = getConnection(dataSourceName);	

				for(QuoteNcGroupsVO quoteNcGroupsVO : quoteNcGroups){
					deleteQuoteNcgroup(dstCon,slsQtnID, slsQtnVrsn, quoteNcGroupsVO);
				}
			}
			catch (Exception ex) {
				Logger.repError(logInfo, "delete", "Exception while updating QuoteNcGroups details...", ex);
				throw new ReplicationException(ex.getMessage(),ex);
			}
			finally{
				closeConnection(dstCon);
			}
		}

	}

	private void deleteQuoteNcgroup(Connection con, long slsQtnID, String slsQtnVrsn,
			QuoteNcGroupsVO quoteNcGroupsVO) throws ReplicationException {
		updateQuoteNcgroup(con, slsQtnID, slsQtnVrsn, quoteNcGroupsVO);

	}

	@Override
	public void change(Connection con, long slsQtnID, String slsQtnVrsn)
	throws ReplicationException {
		Logger.repDebug(logInfo, "change", "Incorrectly called change for QUOTE_NCGROUPS TABLE");

	}

	private void removeDeletedQuoteNcGroups(Connection con,long slsQtnID, String slsQtnVrsn) throws ReplicationException {

		String query = null;
		try {
			query = queryRepository.getQuery("Quote","Delete","DeleteQuoteNcGroups"); 
			List<Object> options = new ArrayList<Object>();
			options.add(String.valueOf(slsQtnID));
			options.add(slsQtnVrsn);

			executeUpdatePreparedStatement(con,query,options);
		}
		catch (Exception ex) {
			Logger.repError(logInfo, "removeDeletedQuoteNcGroups", "Exception while removing DeleteQuoteNcGroups...", ex);
			throw new ReplicationException(ex.getMessage(),ex);
		}
		Logger.repDebug(logInfo, "removeDeletedQuoteNcGroups", "End of removing DeleteQuoteNcGroups for quotation id "+ slsQtnID + " and version "
				+slsQtnVrsn);
	}

	private void updateQuoteNcGroupsDeleteFlag(Connection con, long slsQtnID, String slsQtnVrsn) throws ReplicationException {
		String query = null;
		try {
			query = queryRepository.getQuery("Quote","Update","QuoteNcGroupsDeleteFlag"); 
			List<Object> options = new ArrayList<Object>();
			options.add(String.valueOf(slsQtnID));
			options.add(slsQtnVrsn);

			executeUpdatePreparedStatement(con,query,options);
		}
		catch (Exception ex) {
			Logger.repError(logInfo, "updateQuoteNcGroupsDeleteFlag", "Exception while updating QuoteNcGroupsDeleteFlag...", ex);
			throw new ReplicationException(ex.getMessage(),ex);
		}
		Logger.repDebug(logInfo, "updateQuoteNcGroupsDeleteFlag", "End of updating QuoteNcGroupsDeleteFlag for quotation id "+ slsQtnID +" and version "
				+slsQtnVrsn);

	}

}
