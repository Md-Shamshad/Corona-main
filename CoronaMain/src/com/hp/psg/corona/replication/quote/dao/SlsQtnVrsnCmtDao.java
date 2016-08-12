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
import com.hp.psg.corona.replication.quote.vo.SlsQtnVrsnCmtVO;

public class SlsQtnVrsnCmtDao extends QuoteConnection implements IBaseDao {
	QueryRepository queryRepository = null;
	String dataSourceName = null;
	LoggerInfo logInfo=null;


	public SlsQtnVrsnCmtDao() {
		queryRepository = ReplicationQueryManager.getInstance();
		dataSourceName = Config.getProperty("corona.replication.secondary.data.source.name");
		logInfo = new LoggerInfo (this.getClass().getName());
	}

	@Override
	public void create(Connection con, long slsQtnID, String slsQtnVrsn)
	throws ReplicationException {
		List<SlsQtnVrsnCmtVO> slsQtnVrsnCmts = getSlsQtnVrsnCmt(con,slsQtnID,slsQtnVrsn);
		if(slsQtnVrsnCmts!=null && slsQtnVrsnCmts.size()>0){
			for(SlsQtnVrsnCmtVO slsQtnVrsnCmtVO : slsQtnVrsnCmts){
				insertSlsQtnVrsnCmts(slsQtnVrsnCmtVO);
			}
		}

	}

	private void insertSlsQtnVrsnCmts(SlsQtnVrsnCmtVO slsQtnVrsnCmtVO) throws ReplicationException {
		Logger.repDebug(logInfo, "insertSlsQtnVrsnCmts", "Begining of inserting SlsQtnVrsnCmt details...");

		String insertQuery = null;
		Connection con = null;

		try {
			con = getConnection(dataSourceName);

			insertQuery = queryRepository.getQuery("Quote","Create","SlsQtnVrsnCmt"); 
			List<Object> options = new ArrayList<Object>();

			options.add(String.valueOf(slsQtnVrsnCmtVO.getSlsQtnId()));
			options.add(slsQtnVrsnCmtVO.getSlsQtnVrsnSqnNr());
			options.add(String.valueOf(slsQtnVrsnCmtVO.getSlsQtnVrsnCmtSqnNr()));
			options.add(slsQtnVrsnCmtVO.getCmtTxt());
			options.add(slsQtnVrsnCmtVO.getCmtInrnOnlyInd());
			options.add(String.valueOf(slsQtnVrsnCmtVO.getDeleteInd()));
			options.add(ReplicationUtility.dateString(slsQtnVrsnCmtVO.getCreatedTs()));
			options.add(slsQtnVrsnCmtVO.getCreatedBy());
			options.add(ReplicationUtility.dateString(slsQtnVrsnCmtVO.getLastModifiedTs()));
			options.add(slsQtnVrsnCmtVO.getLastModifiedBy());

			executeUpdatePreparedStatement(con,insertQuery,options);
		}
		catch (Exception ex) {
			Logger.repError(logInfo, "insertSlsQtnVrsnCmts", "Exception while inserting SlsQtnVrsnCmt details...", ex);
			throw new ReplicationException(ex.getMessage(),ex);
		}
		finally{
			closeConnection(con);
		}
		Logger.repDebug(logInfo, "insertSlsQtnVrsnCmts", "End of inserting SlsQtnVrsnCmt details.");

	}

	private List<SlsQtnVrsnCmtVO> getSlsQtnVrsnCmt(Connection con,
			long slsQtnID, String slsQtnVrsn) throws ReplicationException {
		Logger.repDebug(logInfo, "getSlsQtnVrsnCmt", "Begining of getting SlsQtnVrsnCmt details for quotation id "+ slsQtnID+
				" and sls version "+slsQtnVrsn);
		List<SlsQtnVrsnCmtVO> slsQtnVrsnCmts = new ArrayList<SlsQtnVrsnCmtVO>();
		SlsQtnVrsnCmtVO slsQtnVrsnCmtVO = null;
		String selectQuery = null;

		try {
			selectQuery = queryRepository.getQuery("Quote","Select","SlsQtnVrsnCmt"); 
			List<Object> options = new ArrayList<Object>();
			options.add(String.valueOf(slsQtnID));
			options.add(slsQtnVrsn);

			ResultSet resultSet = runPreparedStatement(con, selectQuery, options);
			while (resultSet.next()){
				slsQtnVrsnCmtVO = createVO(resultSet);
				slsQtnVrsnCmts.add(slsQtnVrsnCmtVO);
			}
		}catch(SQLException sqlEx){
			Logger.repError(logInfo, "getSlsQtnVrsnCmt", "Exception while getting SlsQtnVrsnCmt details...", sqlEx);
			throw new ReplicationException(sqlEx.getMessage(), sqlEx);
		}catch (Exception e) {
			Logger.repError(logInfo, "getSlsQtnVrsnCmt", "Exception while getting SlsQtnVrsnCmt details...", e);
			throw new ReplicationException(e.getMessage(), e);
		}

		Logger.repDebug(logInfo, "getSlsQtnVrsnCmt", "End of getting SlsQtnVrsnCmt details for quotation id "+ slsQtnID+
				" and sls version "+slsQtnVrsn);
		return slsQtnVrsnCmts;
	}

	private SlsQtnVrsnCmtVO createVO(ResultSet resultSet) throws ReplicationException {
		SlsQtnVrsnCmtVO slsQtnVrsnCmtVO = new SlsQtnVrsnCmtVO();

		try{
			slsQtnVrsnCmtVO.setCmtInrnOnlyInd(resultSet.getString("CMT_INRN_ONLY_IND"));
			slsQtnVrsnCmtVO.setCmtTxt(resultSet.getString("CMT_TXT"));
			slsQtnVrsnCmtVO.setCreatedBy(resultSet.getString("CREATED_BY"));
			slsQtnVrsnCmtVO.setCreatedTs(resultSet.getTimestamp("CREATED_TS"));
			slsQtnVrsnCmtVO.setDeleteInd((resultSet.getString("DELETE_IND")).charAt(0));
			slsQtnVrsnCmtVO.setLastModifiedBy(resultSet.getString("LAST_MODIFIED_BY"));
			slsQtnVrsnCmtVO.setLastModifiedTs(resultSet.getTimestamp("LAST_MODIFIED_TS"));
			slsQtnVrsnCmtVO.setSlsQtnId(resultSet.getLong("SLS_QTN_ID"));
			slsQtnVrsnCmtVO.setSlsQtnVrsnCmtSqnNr(resultSet.getLong("SLS_QTN_VRSN_CMT_SQN_NR"));
			slsQtnVrsnCmtVO.setSlsQtnVrsnSqnNr(resultSet.getString("SLS_QTN_VRSN_SQN_NR"));
		}catch(SQLException sqlEx){
			Logger.repError(logInfo, "createVO", "Exception while creating SlsQtnVrsnCmtVO...", sqlEx);
			throw new ReplicationException(sqlEx.getMessage(), sqlEx);
		}

		return slsQtnVrsnCmtVO;
	}

	@Override
	public void update(Connection con, long slsQtnID, String slsQtnVrsn)
	throws ReplicationException {
		Connection dstCon = null;
		try {
			dstCon = getConnection(dataSourceName);
			updateSlsQtnVrsnCmtDeleteFlag(dstCon,slsQtnID, slsQtnVrsn);

			List<SlsQtnVrsnCmtVO> slsQtnVrsnCmts = getSlsQtnVrsnCmt(con,slsQtnID,slsQtnVrsn);
			if(slsQtnVrsnCmts!=null && slsQtnVrsnCmts.size()>0){
				for(SlsQtnVrsnCmtVO slsQtnVrsnCmtVO : slsQtnVrsnCmts){
					updateSlsQtnVrsnCmts(dstCon,slsQtnID, slsQtnVrsn, slsQtnVrsnCmtVO);
				}
			}
			removeDeletedSlsQtnVrsnCmt(dstCon,slsQtnID, slsQtnVrsn);
		}
		catch (Exception ex) {
			Logger.repError(logInfo, "update", "Exception while updating slsQtnVrsnCmt details...", ex);
			throw new ReplicationException(ex.getMessage(),ex);
		}
		finally{
			closeConnection(dstCon);
		}
	}

	private void updateSlsQtnVrsnCmts(Connection con,long slsQtnID, String slsQtnVrsn,
			SlsQtnVrsnCmtVO slsQtnVrsnCmtVO) throws ReplicationException {
		Logger.repDebug(logInfo, "updateSlsQtnVrsnCmts", "Begining of updating SlsQtnVrsnCmts details for quotation id "+ slsQtnID+
				" and sls version "+slsQtnVrsn);
		String updateQuery = null;
		try {
			updateQuery = queryRepository.getQuery("Quote","Update","SlsQtnVrsnCmt"); 
			List<Object> options = new ArrayList<Object>();

			options.add(slsQtnVrsnCmtVO.getCmtTxt());
			options.add(slsQtnVrsnCmtVO.getCmtInrnOnlyInd());
			options.add(String.valueOf(slsQtnVrsnCmtVO.getDeleteInd()));
			options.add(ReplicationUtility.dateString(slsQtnVrsnCmtVO.getCreatedTs()));
			options.add(slsQtnVrsnCmtVO.getCreatedBy());
			options.add(ReplicationUtility.dateString(slsQtnVrsnCmtVO.getLastModifiedTs()));
			options.add(slsQtnVrsnCmtVO.getLastModifiedBy());

			//for where conditions
			options.add(String.valueOf(slsQtnID));
			options.add(slsQtnVrsn);
			options.add(String.valueOf(slsQtnVrsnCmtVO.getSlsQtnVrsnCmtSqnNr()));

			int rowUpdated = executeUpdatePreparedStatement(con,updateQuery,options);
			if(rowUpdated == 0){
				Logger.repDebug(logInfo, "updateSlsQtnVrsnCmts", "SlsQtnVrsnCmts doesn't exist for SLS_QTN_ID =  "+slsQtnID+ " and SLS_QTN_VRSN = "
						+slsQtnVrsn+" .Inserting record now.");
				insertSlsQtnVrsnCmts(slsQtnVrsnCmtVO);
			}
		}
		catch (Exception ex) {
			Logger.repError(logInfo, "updateSlsQtnVrsnCmts", "Exception while updating SlsQtnVrsnCmts details...", ex);
			throw new ReplicationException(ex.getMessage(),ex);
		}
		Logger.repDebug(logInfo, "updateSlsQtnVrsnCmts", "End of updating SlsQtnVrsnCmts details for quotation id "+ slsQtnID+
				" and sls version "+slsQtnVrsn);
	}

	@Override
	public void delete(Connection con, long slsQtnID, String slsQtnVrsn)
	throws ReplicationException {
		List<SlsQtnVrsnCmtVO> slsQtnVrsnCmts = getSlsQtnVrsnCmt(con,slsQtnID,slsQtnVrsn);
		if(slsQtnVrsnCmts!=null && slsQtnVrsnCmts.size()>0){
			Connection dstCon = null;
			try {
				dstCon = getConnection(dataSourceName);
				for(SlsQtnVrsnCmtVO slsQtnVrsnCmtVO : slsQtnVrsnCmts){
					deleteSlsQtnVrsnCmts(dstCon, slsQtnID, slsQtnVrsn, slsQtnVrsnCmtVO);
				}
			}
			catch (Exception ex) {
				Logger.repError(logInfo, "delete", "Exception while updating slsQtnVrsnCmt details...", ex);
				throw new ReplicationException(ex.getMessage(),ex);
			}
			finally{
				closeConnection(dstCon);
			}
		}

	}

	private void deleteSlsQtnVrsnCmts(Connection con,long slsQtnID, String slsQtnVrsn,
			SlsQtnVrsnCmtVO slsQtnVrsnCmtVO) throws ReplicationException {
		updateSlsQtnVrsnCmts(con,slsQtnID, slsQtnVrsn, slsQtnVrsnCmtVO);

	}

	@Override
	public void change(Connection con, long slsQtnID, String slsQtnVrsn)
	throws ReplicationException {
		Logger.repDebug(logInfo, "change", "Incorrectly called change for slsQtnVrsnCmt TABLE");

	}


	private void removeDeletedSlsQtnVrsnCmt(Connection con,long slsQtnID, String slsQtnVrsn) throws ReplicationException {

		String query = null;
		try {
			query = queryRepository.getQuery("Quote","Delete","DeleteSlsQtnVrsnCmt"); 
			List<Object> options = new ArrayList<Object>();
			options.add(String.valueOf(slsQtnID));
			options.add(slsQtnVrsn);

			executeUpdatePreparedStatement(con,query,options);
		}
		catch (Exception ex) {
			Logger.repError(logInfo, "removeDeletedSlsQtnVrsnCmt", "Exception while removing DeleteSlsQtnVrsnCmt...", ex);
			throw new ReplicationException(ex.getMessage(),ex);
		}
		Logger.repDebug(logInfo, "removeDeletedSlsQtnVrsnCmt", "End of removing DeleteSlsQtnVrsnCmt for quotation id "+ slsQtnID + " and version "
				+slsQtnVrsn);
	}

	private void updateSlsQtnVrsnCmtDeleteFlag(Connection con,long slsQtnID, String slsQtnVrsn) throws ReplicationException {
		String query = null;
		try {
			query = queryRepository.getQuery("Quote","Update","SlsQtnVrsnCmtDeleteFlag"); 
			List<Object> options = new ArrayList<Object>();
			options.add(String.valueOf(slsQtnID));
			options.add(slsQtnVrsn);

			executeUpdatePreparedStatement(con,query,options);
		}
		catch (Exception ex) {
			Logger.repError(logInfo, "updateSlsQtnVrsnCmtDeleteFlag", "Exception while updating SlsQtnVrsnCmtDeleteFlag...", ex);
			throw new ReplicationException(ex.getMessage(),ex);
		}
		Logger.repDebug(logInfo, "updateSlsQtnVrsnCmtDeleteFlag", "End of updating SlsQtnVrsnCmtDeleteFlag for quotation id "+ slsQtnID +" and version "
				+slsQtnVrsn);

	}

}
