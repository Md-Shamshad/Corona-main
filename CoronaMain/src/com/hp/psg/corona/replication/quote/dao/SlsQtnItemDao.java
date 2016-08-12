package com.hp.psg.corona.replication.quote.dao;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.hp.psg.common.util.logging.LoggerInfo;
import com.hp.psg.corona.common.util.Config;
import com.hp.psg.corona.replication.common.cache.query.QueryRepository;
import com.hp.psg.corona.replication.common.cache.query.ReplicationQueryManager;
import com.hp.psg.corona.replication.common.exception.ReplicationException;
import com.hp.psg.corona.replication.common.util.ReplicationUtility;
import com.hp.psg.corona.common.util.Logger;
import com.hp.psg.corona.replication.quote.QuoteConnection;
import com.hp.psg.corona.replication.quote.vo.SlsQtnItemVO;

/**
 * DAO for SLS_QTN_ITEM table
 * @author rohitc
 *
 */
public class SlsQtnItemDao extends QuoteConnection implements IBaseDao{
	QueryRepository queryRepository = null;
	String dataSourceName = null;
	LoggerInfo logInfo=null;

	public SlsQtnItemDao(){
		queryRepository = ReplicationQueryManager.getInstance();
		dataSourceName = Config.getProperty("corona.replication.secondary.data.source.name");
		logInfo = new LoggerInfo (this.getClass().getName());
	}

	@Override
	public void create(Connection con, long slsQtnID, String slsQtnVrsn)
	throws ReplicationException {
		List<SlsQtnItemVO> slsQtnItems = getSlsQtnItem(con,slsQtnID,slsQtnVrsn);
		if(slsQtnItems!=null && slsQtnItems.size()>0){
			for(SlsQtnItemVO slsQtnItemVO : slsQtnItems){
				insertSlsQtnItem(slsQtnItemVO);
			}
		}
	}

	@Override
	public void update(Connection con, long slsQtnID, String slsQtnVrsn)
	throws ReplicationException {
		Connection dstCon = null;
		try {
			dstCon = getConnection(dataSourceName);
			updateSlsQtnItmDeleteFlag(dstCon,slsQtnID, slsQtnVrsn);

			List<SlsQtnItemVO> slsQtnItems = getSlsQtnItem(con,slsQtnID,slsQtnVrsn);
			if(slsQtnItems!=null && slsQtnItems.size()>0){
				for(SlsQtnItemVO slsQtnItemVO : slsQtnItems){
					updateSlsQtnItem(dstCon,slsQtnID,slsQtnVrsn,slsQtnItemVO);
				}
			}
			removeDeletedSlsQtnItm(dstCon,slsQtnID, slsQtnVrsn);
		}
		catch (Exception ex) {
			Logger.repError(logInfo, "update", "Exception while updating sls qtn items details...", ex);
			throw new ReplicationException(ex.getMessage(),ex);
		}
		finally{
			closeConnection(dstCon);
		}
	}

	@Override
	public void delete(Connection con, long slsQtnID, String slsQtnVrsn)
	throws ReplicationException {
		List<SlsQtnItemVO> slsQtnItems = getSlsQtnItem(con,slsQtnID,slsQtnVrsn);
		if(slsQtnItems!=null && slsQtnItems.size()>0){
			Connection dstCon = null;
			try {
				dstCon = getConnection(dataSourceName);
				for(SlsQtnItemVO slsQtnItemVO : slsQtnItems){
					deleteSlsQtnItem(dstCon,slsQtnID,slsQtnVrsn,slsQtnItemVO);
				}
			}
			catch (Exception ex) {
				Logger.repError(logInfo, "delete", "Exception while delete sls qtn items details...", ex);
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
		Logger.repDebug(logInfo, "change", "Incorrectly called change for SLS_QTN_ITM TABLE");
	}

	public List<SlsQtnItemVO> getSlsQtnItem (Connection con, long slsQtnID, String slsQtnVrsn) throws ReplicationException{
		Logger.repDebug(logInfo, "getSlsQtnItem", "Begining of getting Sls quote item details for quotation id "+ slsQtnID);
		List<SlsQtnItemVO> slsQtnItems = new ArrayList<SlsQtnItemVO>();
		SlsQtnItemVO slsQtnItemVO = null;
		String selectQuery = null;

		try {
			selectQuery = queryRepository.getQuery("Quote","Select","SlsQtnItem"); 
			List<Object> options = new ArrayList<Object>();
			options.add(String.valueOf(slsQtnID));
			options.add(slsQtnVrsn);

			ResultSet resultSet = runPreparedStatement(con, selectQuery, options);
			while (resultSet.next()){
				slsQtnItemVO = createVO(resultSet);
				slsQtnItems.add(slsQtnItemVO);
			}
		}catch(SQLException sqlEx){
			Logger.repError(logInfo, "getSlsQtnItem", "Exception while getting quote item details...", sqlEx);
			throw new ReplicationException(sqlEx.getMessage(), sqlEx);
		}catch (Exception e) {
			Logger.repError(logInfo, "getSlsQtnItem", "Exception while getting quote item details...", e);
			throw new ReplicationException(e.getMessage(), e);
		}
		Logger.repDebug(logInfo, "getSlsQtnItem", "End of getting Sls quote item details for quotation id "+ slsQtnID);

		return slsQtnItems;
	}


	private SlsQtnItemVO createVO(ResultSet resultSet) throws ReplicationException {
		SlsQtnItemVO slsQtnItemVO = new SlsQtnItemVO();

		try{
			slsQtnItemVO.setAcctUntLstAmt(resultSet.getDouble("ACCT_UNT_LST_AMT"));
			slsQtnItemVO.setAcctUntNtAmt(resultSet.getDouble("ACCT_UNT_NT_AMT"));
			slsQtnItemVO.setAcctXtndNtAmt(resultSet.getDouble("ACCT_XTND_NT_AMT"));
			slsQtnItemVO.setAgrmntVrblAcctAmt(resultSet.getDouble("AGRMNT_VRBL_ACCT_AMT"));
			slsQtnItemVO.setAgrmntVrblLclAmt(resultSet.getDouble("AGRMNT_VRBL_LCL_AMT"));
			slsQtnItemVO.setAgrmntVrblPrcInd(resultSet.getString("AGRMNT_VRBL_PRC_IND"));
			slsQtnItemVO.setAgrmntVrblRtPc(resultSet.getDouble("AGRMNT_VRBL_RT_PC"));
			slsQtnItemVO.setCreatedBy(resultSet.getString("CREATED_BY"));
			slsQtnItemVO.setCreatedTs(resultSet.getTimestamp("CREATED_TS"));
			slsQtnItemVO.setCustSpcfDn(resultSet.getString("CUST_SPCF_DN"));
			slsQtnItemVO.setDeleteInd((resultSet.getString("DELETE_IND")).charAt(0));
			slsQtnItemVO.setDsplySqnNr(resultSet.getInt("DSPLY_SQN_NR"));
			slsQtnItemVO.setExplsnInd(resultSet.getString("EXPLSN_IND"));
			slsQtnItemVO.setLastModifiedBy(resultSet.getString("LAST_MODIFIED_BY"));
			slsQtnItemVO.setLastModifiedTs(resultSet.getTimestamp("LAST_MODIFIED_TS"));
			slsQtnItemVO.setLclUntLstAmt(resultSet.getDouble("LCL_UNT_LST_AMT"));
			slsQtnItemVO.setLclUntNtAmt(resultSet.getDouble("LCL_UNT_NT_AMT"));
			slsQtnItemVO.setLclXtnfNtAmt(resultSet.getDouble("LCL_XTND_NT_AMT"));
			slsQtnItemVO.setPrcAdjMtdCd(resultSet.getString("PRC_ADJ_MTD_CD"));
			slsQtnItemVO.setPrcgGrpInd(resultSet.getString("PRCG_GRP_IND"));
			slsQtnItemVO.setPrcOvrrdInd(resultSet.getString("PRC_OVRRD_IND"));
			slsQtnItemVO.setPrcTs(resultSet.getTimestamp("PRC_TS"));
			slsQtnItemVO.setProdBsPrcTypCd(resultSet.getString("PROD_BS_PRC_TYP_CD"));
			slsQtnItemVO.setProdClsfnCd(resultSet.getString("PROD_CLSFN_CD"));
			slsQtnItemVO.setProdID(resultSet.getLong("PROD_ID"));
			slsQtnItemVO.setProdLastPrcTs(resultSet.getTimestamp("PROD_LAST_PRC_TS"));
			slsQtnItemVO.setProdPrcEffTs(resultSet.getTimestamp("PROD_PRC_EFF_TS"));
			slsQtnItemVO.setProdPrmtnID(resultSet.getLong("PROD_PRMTN_ID"));
			slsQtnItemVO.setQuantity(resultSet.getLong("QUANTITY"));
			slsQtnItemVO.setSlsAgrmntInd(resultSet.getString("SLS_AGRMNT_IND"));
			slsQtnItemVO.setSlsEvtTrmsTmplGrpCd(resultSet.getString("SLS_EVT_TRMS_TMPL_GRP_CD"));
			slsQtnItemVO.setSlsOprtnyID(resultSet.getLong("SLS_OPRTNY_ID"));
			slsQtnItemVO.setSlsOpprtntyInd(resultSet.getString("SLS_OPPRTNTY_IND"));
			slsQtnItemVO.setSlsOprtnyItmNr(resultSet.getString("SLS_OPRTNY_ITM_NR"));
			slsQtnItemVO.setSlsQtnID(resultSet.getLong("SLS_QTN_ID"));
			slsQtnItemVO.setSlsQtnItmSqnNr(resultSet.getString("SLS_QTN_ITM_SQN_NR"));
			slsQtnItemVO.setSlsQtnPrcgGrpID(resultSet.getLong("SLS_QTN_PRCG_GRP_ID"));
			slsQtnItemVO.setSlsQtnSctnSqnNr(resultSet.getString("SLS_QTN_SCTN_SQN_NR"));
			slsQtnItemVO.setSlsQtnVrsnSqnNr(resultSet.getString("SLS_QTN_VRSN_SQN_NR"));
			slsQtnItemVO.setUntOfMsrCd(resultSet.getString("UNT_OF_MSR_CD"));
			slsQtnItemVO.setWrldRgnID(resultSet.getLong("WRLD_RGN_ID"));
		}catch(SQLException sqEx){
			Logger.repError(logInfo, "createVO", "Exception while creating VO for Sls quote item...", sqEx);
			throw new ReplicationException(sqEx.getMessage(), sqEx);
		}
		return slsQtnItemVO;
	}//createVO


	public void insertSlsQtnItem (SlsQtnItemVO slsQtnItemVO)throws ReplicationException{
		Logger.repDebug(logInfo, "insertSlsQtnItem", "Begining of inserting Sls quote item details...");

		String insertQuery = null;
		Connection con = null;

		try {
			con = getConnection(dataSourceName);

			insertQuery = queryRepository.getQuery("Quote","Create","SlsQtnItem"); 
			List<Object> options = new ArrayList<Object>();

			options.add(slsQtnItemVO.getSlsQtnItmSqnNr());
			options.add(String.valueOf(slsQtnItemVO.getSlsQtnID()));
			options.add(slsQtnItemVO.getSlsQtnVrsnSqnNr());
			options.add(String.valueOf(slsQtnItemVO.getSlsQtnPrcgGrpID()));
			options.add(String.valueOf(slsQtnItemVO.getAgrmntVrblRtPc()));
			options.add(String.valueOf(slsQtnItemVO.getAgrmntVrblLclAmt()));
			options.add(slsQtnItemVO.getSlsQtnSctnSqnNr());
			options.add(slsQtnItemVO.getCustSpcfDn());
			options.add(String.valueOf(slsQtnItemVO.getDsplySqnNr()));
			options.add(ReplicationUtility.dateString(slsQtnItemVO.getProdPrcEffTs()));
			options.add(ReplicationUtility.dateString(slsQtnItemVO.getProdLastPrcTs()));
			options.add(String.valueOf(slsQtnItemVO.getQuantity()));
			options.add(String.valueOf(slsQtnItemVO.getLclXtnfNtAmt()));
			options.add(String.valueOf(slsQtnItemVO.getLclUntLstAmt()));
			options.add(String.valueOf(slsQtnItemVO.getLclUntNtAmt()));
			options.add(String.valueOf(slsQtnItemVO.getAcctXtndNtAmt()));
			options.add(String.valueOf(slsQtnItemVO.getAcctUntLstAmt()));
			options.add(String.valueOf(slsQtnItemVO.getAcctUntNtAmt()));
			options.add(slsQtnItemVO.getExplsnInd());
			options.add(slsQtnItemVO.getProdClsfnCd());
			options.add(String.valueOf(slsQtnItemVO.getProdID()));
			options.add(slsQtnItemVO.getUntOfMsrCd());
			options.add(slsQtnItemVO.getProdBsPrcTypCd());
			options.add(String.valueOf(slsQtnItemVO.getWrldRgnID()));
			options.add(String.valueOf(slsQtnItemVO.getProdPrmtnID()));
			options.add(slsQtnItemVO.getSlsAgrmntInd());
			options.add(slsQtnItemVO.getSlsOpprtntyInd());
			options.add(slsQtnItemVO.getPrcOvrrdInd());
			options.add(slsQtnItemVO.getPrcgGrpInd());
			options.add(ReplicationUtility.dateString(slsQtnItemVO.getPrcTs()));
			options.add(String.valueOf(slsQtnItemVO.getSlsOprtnyID()));
			options.add(slsQtnItemVO.getSlsOprtnyItmNr());
			options.add(slsQtnItemVO.getSlsEvtTrmsTmplGrpCd());
			options.add(slsQtnItemVO.getAgrmntVrblPrcInd());
			options.add(slsQtnItemVO.getPrcAdjMtdCd());
			options.add(String.valueOf(slsQtnItemVO.getAgrmntVrblAcctAmt()));
			options.add(String.valueOf(slsQtnItemVO.getDeleteInd()));
			options.add(ReplicationUtility.dateString(slsQtnItemVO.getCreatedTs()));
			options.add(slsQtnItemVO.getCreatedBy());
			options.add(ReplicationUtility.dateString(slsQtnItemVO.getLastModifiedTs()));
			options.add(slsQtnItemVO.getLastModifiedBy());

			executeUpdatePreparedStatement(con,insertQuery,options);
		}
		catch (Exception ex) {
			Logger.repError(logInfo, "insertSlsQtnItem", "Exception while inserting Sls quote item details...", ex);
			throw new ReplicationException(ex.getMessage(),ex);
		}
		finally{
			closeConnection(con);
		}
		Logger.repDebug(logInfo, "insertSlsQtnItem", "End of inserting Sls quote item details.");

	}//insertSlsQtnItem


	public void updateSlsQtnItem (Connection con,long slsQtnID,String slsQtnVrsn, SlsQtnItemVO slsQtnItemVO)throws ReplicationException{
		Logger.repDebug(logInfo, "updateSlsQtnItem", "Begining of updating Sls quote item details for quotation id "+ slsQtnID);
		String updateQuery = null;
		try {
			updateQuery = queryRepository.getQuery("Quote","Update","SlsQtnItem"); 
			List<Object> options = new ArrayList<Object>();

			//options.add(slsQtnItemVO.getSlsQtnItmSqnNr());
			//options.add(slsQtnItemVO.getSlsQtnVrsnSqnNr());
			options.add(String.valueOf(slsQtnItemVO.getSlsQtnPrcgGrpID()));
			options.add(String.valueOf(slsQtnItemVO.getAgrmntVrblRtPc()));
			options.add(String.valueOf(slsQtnItemVO.getAgrmntVrblLclAmt()));
			options.add(slsQtnItemVO.getSlsQtnSctnSqnNr());
			options.add(slsQtnItemVO.getCustSpcfDn());
			options.add(String.valueOf(slsQtnItemVO.getDsplySqnNr()));
			options.add(ReplicationUtility.dateString(slsQtnItemVO.getProdPrcEffTs()));
			options.add(ReplicationUtility.dateString(slsQtnItemVO.getProdLastPrcTs()));
			options.add(String.valueOf(slsQtnItemVO.getQuantity()));
			options.add(String.valueOf(slsQtnItemVO.getLclXtnfNtAmt()));
			options.add(String.valueOf(slsQtnItemVO.getLclUntLstAmt()));
			options.add(String.valueOf(slsQtnItemVO.getLclUntNtAmt()));
			options.add(String.valueOf(slsQtnItemVO.getAcctXtndNtAmt()));
			options.add(String.valueOf(slsQtnItemVO.getAcctUntLstAmt()));
			options.add(String.valueOf(slsQtnItemVO.getAcctUntNtAmt()));
			options.add(slsQtnItemVO.getExplsnInd());
			options.add(slsQtnItemVO.getProdClsfnCd());
			options.add(String.valueOf(slsQtnItemVO.getProdID()));
			options.add(slsQtnItemVO.getUntOfMsrCd());
			options.add(slsQtnItemVO.getProdBsPrcTypCd());
			options.add(String.valueOf(slsQtnItemVO.getWrldRgnID()));
			options.add(String.valueOf(slsQtnItemVO.getProdPrmtnID()));
			options.add(slsQtnItemVO.getSlsAgrmntInd());
			options.add(slsQtnItemVO.getSlsOpprtntyInd());
			options.add(slsQtnItemVO.getPrcOvrrdInd());
			options.add(slsQtnItemVO.getPrcgGrpInd());
			options.add(ReplicationUtility.dateString(slsQtnItemVO.getPrcTs()));
			options.add(String.valueOf(slsQtnItemVO.getSlsOprtnyID()));
			options.add(slsQtnItemVO.getSlsOprtnyItmNr());
			options.add(slsQtnItemVO.getSlsEvtTrmsTmplGrpCd());
			options.add(slsQtnItemVO.getAgrmntVrblPrcInd());
			options.add(slsQtnItemVO.getPrcAdjMtdCd());
			options.add(String.valueOf(slsQtnItemVO.getAgrmntVrblAcctAmt()));
			options.add(String.valueOf(slsQtnItemVO.getDeleteInd()));
			options.add(ReplicationUtility.dateString(slsQtnItemVO.getCreatedTs()));
			options.add(slsQtnItemVO.getCreatedBy());
			options.add(ReplicationUtility.dateString(slsQtnItemVO.getLastModifiedTs()));
			options.add(slsQtnItemVO.getLastModifiedBy());

			options.add(String.valueOf(slsQtnID));  		//for where condition
			options.add(slsQtnItemVO.getSlsQtnItmSqnNr()); //for where condition
			options.add(slsQtnVrsn); //for where condition

			int rowUpdated = executeUpdatePreparedStatement(con,updateQuery,options);
			if(rowUpdated == 0){
				Logger.repDebug(logInfo, "updateQuote", "SLS_QTN_ITEM doesn't exist for SLS_QTN_ID =  "+slsQtnID+ " and SLS_QTN_VRSN = "+slsQtnVrsn+" .Inserting record now.");
				insertSlsQtnItem(slsQtnItemVO);
			}
		}
		catch (Exception ex) {
			Logger.repError(logInfo, "updateSlsQtnItem", "Exception while inserting Sls quote item details...", ex);
			throw new ReplicationException(ex.getMessage(),ex);
		}
		Logger.repDebug(logInfo, "updateSlsQtnItem", "End of updating Sls quote item details for quotation id "+ slsQtnID);

	}//updateSlsQtnItem

	public void deleteSlsQtnItem(Connection conn , long slsQtnID, String slsQtnVrsn,SlsQtnItemVO slsQtnItemVO) throws ReplicationException{
		Logger.repDebug(logInfo, "deleteSlsQtnItem", "Begining of deleting Sls quote item for quotation id "+ slsQtnID);

		updateSlsQtnItem(conn, slsQtnID,slsQtnVrsn, slsQtnItemVO);

		Logger.repDebug(logInfo, "deleteSlsQtnItem", "Begining of deleting Sls quote item for quotation id "+ slsQtnID);
	}//deleteSlsQtnItem

	private void removeDeletedSlsQtnItm(Connection con,long slsQtnID, String slsQtnVrsn) throws ReplicationException {
		String query = null;
		try {
			query = queryRepository.getQuery("Quote","Delete","DeleteSlsQtnItms"); 
			List<Object> options = new ArrayList<Object>();
			options.add(String.valueOf(slsQtnID));
			options.add(slsQtnVrsn);

			executeUpdatePreparedStatement(con,query,options);
		}
		catch (Exception ex) {
			Logger.repError(logInfo, "removeDeletedSlsQtnItm", "Exception while removing DeleteSlsQtnItms...", ex);
			throw new ReplicationException(ex.getMessage(),ex);
		}
		Logger.repDebug(logInfo, "removeDeletedSlsQtnItm", "End of removing DeleteSlsQtnItms for quotation id "+ slsQtnID + " and version "
				+slsQtnVrsn);
	}

	private void updateSlsQtnItmDeleteFlag(Connection con, long slsQtnID, String slsQtnVrsn) throws ReplicationException {
		String query = null;
		try {
			query = queryRepository.getQuery("Quote","Update","SlsQtnItmDeleteFlag"); 
			List<Object> options = new ArrayList<Object>();
			options.add(String.valueOf(slsQtnID));
			options.add(slsQtnVrsn);

			executeUpdatePreparedStatement(con,query,options);
		}
		catch (Exception ex) {
			Logger.repError(logInfo, "updateSlsQtnItmDeleteFlag", "Exception while updating SlsQtnItmDeleteFlag...", ex);
			throw new ReplicationException(ex.getMessage(),ex);
		}
		Logger.repDebug(logInfo, "updateSlsQtnItmDeleteFlag", "End of updating SlsQtnItmDeleteFlag for quotation id "+ slsQtnID +" and version "
				+slsQtnVrsn);
	}

}//class
