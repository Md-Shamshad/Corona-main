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
import com.hp.psg.corona.replication.quote.vo.SlsQtnVrsnVO;

/**
 * DAO for SLS_QTN_VRSN table
 * @author rohitc
 *
 */
public class SlsQtnVrsnDao extends QuoteConnection implements IBaseDao {
	QueryRepository queryRepository = null;	
	String dataSourceName = null;
	LoggerInfo logInfo=null;
	
	public SlsQtnVrsnDao(){
		queryRepository = ReplicationQueryManager.getInstance();
		dataSourceName = Config.getProperty("corona.replication.secondary.data.source.name");
		logInfo = new LoggerInfo (this.getClass().getName());
	}
	
	@Override
	public void create(Connection con, long slsQtnID, String slsQtnVrsn)
			throws ReplicationException {
		SlsQtnVrsnVO slsQtnVrsnVO = getSlsQtnVrsn(con,slsQtnID,slsQtnVrsn);
		if(slsQtnVrsnVO!=null){
			insertSlsQtnVrsn(slsQtnVrsnVO);
		}
	}

	@Override
	public void update(Connection con, long slsQtnID, String slsQtnVrsn)
			throws ReplicationException {
		SlsQtnVrsnVO slsQtnVrsnVO = getSlsQtnVrsn(con,slsQtnID,slsQtnVrsn);
		if(slsQtnVrsnVO!=null){
			updateSlsQtnVrsn(slsQtnID,slsQtnVrsn,slsQtnVrsnVO);
		}
	}

	@Override
	public void delete(Connection con, long slsQtnID, String slsQtnVrsn)
			throws ReplicationException {
		SlsQtnVrsnVO slsQtnVrsnVO = getSlsQtnVrsn(con,slsQtnID,slsQtnVrsn);
		if(slsQtnVrsnVO!=null){
			deleteSlsQtnVrsn(slsQtnID,slsQtnVrsn,slsQtnVrsnVO);
		}
	}
	
	@Override
	public void change(Connection con, long slsQtnID, String slsQtnVrsn) 
			throws ReplicationException {
		String status = getStatus(con,slsQtnID,slsQtnVrsn);
		updateStatus(slsQtnID, slsQtnVrsn, status);
	}
	
	public String getStatus(Connection con, long slsQtnID, String slsQtnVRsn)throws ReplicationException{
		Logger.repDebug(logInfo, "getStatus", "Begining of getting Sls quote status details for quotation id "+ slsQtnID);
		String status = null;
		String selectQuery = null;
	
		try {
			selectQuery = queryRepository.getQuery("Quote","Select","GetStatus");
			List<Object> options = new ArrayList<Object>();
			options.add(String.valueOf(slsQtnID));
			options.add(slsQtnVRsn);

			ResultSet resultSet = runPreparedStatement(con, selectQuery, options);
			if (resultSet.next()){
				status = resultSet.getString("SLS_QTN_VRSN_STTS_CD");
			}
		}catch(SQLException sqlEx){
			Logger.repError(logInfo, "getQuoteVersion", "Exception while getting Sls quote status details...", sqlEx);
			throw new ReplicationException(sqlEx.getMessage(), sqlEx);
		}catch (Exception e) {
			Logger.repError(logInfo, "getQuoteVersion", "Exception while getting Sls quote status details...", e);
			throw new ReplicationException(e.getMessage(), e);
		}
		Logger.repDebug(logInfo, "getStatus", "End of getting Sls quote status details for quotation id "+ slsQtnID);
		 
		return status;		
	}//getStatus
	
	
	public void updateStatus (long slsQtnID,String slsQtnVrsn,String status) throws ReplicationException{
		Logger.repDebug(logInfo, "updateStatus", "Begining of updating Sls quote vrsn status for quotation id "+ slsQtnID);
		String updateQuery = null;
		Connection con = null;
		
		try {
			con = getConnection(dataSourceName);
			
			updateQuery = queryRepository.getQuery("Quote","Change","UpdateStatus"); 
			List<Object> options = new ArrayList<Object>();
			
			options.add(status);
			options.add(String.valueOf(slsQtnID)); //for where condition
			options.add(slsQtnVrsn); //for where condition
			
			executeUpdatePreparedStatement(con,updateQuery,options);
		}
		catch (Exception ex) {
			Logger.repError(logInfo, "updateStatus", "Exception while updating Sls quote vrsn status...", ex);
			throw new ReplicationException(ex.getMessage(),ex);
		}
		finally{
			closeConnection(con);
		}
		Logger.repDebug(logInfo, "updateStatus", "End of updating Sls quote vrsn status for quotation id "+ slsQtnID);
				
	}//updateStatus

	public SlsQtnVrsnVO getSlsQtnVrsn (Connection con, long slsQtnID,String slsQtnVrsn) throws ReplicationException{
		Logger.repDebug(logInfo, "getSlsQtnVrsn", "Begining of getting Sls quote version details for quotation id "+ slsQtnID);
		SlsQtnVrsnVO slsQtnVrsnVO = null;
		String selectQuery = null;
		
		try {
			selectQuery = queryRepository.getQuery("Quote","Select","SlsQtnVrsn"); 
			List<Object> options = new ArrayList<Object>();
			options.add(String.valueOf(slsQtnID));
			options.add(slsQtnVrsn);

			ResultSet resultSet = runPreparedStatement(con, selectQuery, options);
			if (resultSet.next()){
				slsQtnVrsnVO = createVO(resultSet);
			}
		}catch(SQLException sqlEx){
			Logger.repError(logInfo, "getSlsQtnVrsn", "Exception while getting Sls quote version details...", sqlEx);
			throw new ReplicationException(sqlEx.getMessage(), sqlEx);
		}catch (Exception e) {
			Logger.repError(logInfo, "getSlsQtnVrsn", "Exception while getting Sls quote version details...", e);
			throw new ReplicationException(e.getMessage(), e);
		}
		Logger.repDebug(logInfo, "getSlsQtnVrsn", "End of getting Sls quote version details for quotation id "+ slsQtnID);
		 
		return slsQtnVrsnVO;
	}//getSlsQtnVrsn

	private SlsQtnVrsnVO createVO(ResultSet resultSet) throws ReplicationException {
		SlsQtnVrsnVO slsQtnVrsnVO = new SlsQtnVrsnVO();

		try{
			slsQtnVrsnVO.setCharScriptCd(resultSet.getString("CHAR_SCRIPT_CD"));
			slsQtnVrsnVO.setCnfgnOvrdAprvlInd(resultSet.getString("CNFGN_OVRD_APRVL_IND"));
			slsQtnVrsnVO.setCountryCd(resultSet.getString("COUNTRY_CD"));
			slsQtnVrsnVO.setCreatedBy(resultSet.getString("CREATED_BY"));
			slsQtnVrsnVO.setCreatesTS(resultSet.getTimestamp("CREATED_TS"));
			slsQtnVrsnVO.setCreationTS(resultSet.getTimestamp("CREATION_TS"));
			slsQtnVrsnVO.setCstmCnfgnInd(resultSet.getString("CSTM_CNFGN_IND"));
			slsQtnVrsnVO.setCurrencyCd(resultSet.getString("CURRENCY_CD"));
			slsQtnVrsnVO.setCustPrchOrdNr(resultSet.getString("CUST_PRCH_ORD_NR"));
			slsQtnVrsnVO.setCustPrqstTS(resultSet.getTimestamp("CUST_PRCH_ORD_NR"));
			slsQtnVrsnVO.setCustRqstHndOffTS(resultSet.getTimestamp("CUST_RQST_HND_OFF_TS"));
			slsQtnVrsnVO.setDeleteInd((resultSet.getString("DELETE_IND")).charAt(0));
			slsQtnVrsnVO.setDescription(resultSet.getString("DESCRIPTION"));
			slsQtnVrsnVO.setDfltEndCustOtrPrtySiteID(resultSet.getLong("DFLT_END_CUST_OTR_PRTY_SITE_ID"));
			slsQtnVrsnVO.setDfltInfcrOtrPrtySiteID(resultSet.getLong("DFLT_INFCR_OTR_PRTY_SITE_ID"));
			slsQtnVrsnVO.setDfltInvToOtrPrtySiteID(resultSet.getLong("DFLT_INV_TO_OTR_PRTY_SITE_ID"));
			slsQtnVrsnVO.setDfltPyrOtrPrtySiteInsnID(resultSet.getLong("DFLT_PYR_OTR_PRTY_SITE_INSN_ID"));
			slsQtnVrsnVO.setDfltRqrdDlvryLeadTmQty(resultSet.getLong("DFLT_RQRD_DLVRY_LEAD_TM_QTY"));
			slsQtnVrsnVO.setDfltRqrdDlvryTS(resultSet.getTimestamp("DFLT_RQRD_DLVRY_TS"));
			slsQtnVrsnVO.setDfltShpToOtrPrtySiteID(resultSet.getLong("DFLT_SHP_TO_OTR_PRTY_SITE_ID"));
			slsQtnVrsnVO.setDfltUltCnsgOtrPrtySiteID(resultSet.getLong("DFLT_ULT_CNSG_OTR_PRTY_SITE_ID"));
			slsQtnVrsnVO.setEffectiveTS(resultSet.getTimestamp("EFFECTIVE_TS"));
			slsQtnVrsnVO.setEntrsLglEntNr(resultSet.getString("ENTRS_LGL_ENT_NR"));
			slsQtnVrsnVO.setExpiryTS(resultSet.getTimestamp("EXPIRY_TS"));
			slsQtnVrsnVO.setHandOffTS(resultSet.getTimestamp("EXPIRY_TS"));
			slsQtnVrsnVO.setLangCd(resultSet.getString("LANG_CD"));
			slsQtnVrsnVO.setLastModifiedBy(resultSet.getString("LAST_MODIFIED_BY"));
			slsQtnVrsnVO.setLastModifiedTS(resultSet.getTimestamp("LAST_MODIFIED_TS"));
			slsQtnVrsnVO.setRqstFOrdRcvdTS(resultSet.getTimestamp("RQST_F_ORD_RCVD_TS"));
			slsQtnVrsnVO.setSldToOtrPrtySiteInsnID(resultSet.getLong("SLD_TO_OTR_PRTY_SITE_INSN_ID"));
			slsQtnVrsnVO.setSlsChnlCd(resultSet.getString("SLS_CHNL_CD"));
			slsQtnVrsnVO.setSlsPersID(resultSet.getLong("SLS_PERS_ID"));
			slsQtnVrsnVO.setSlsQtnID(resultSet.getLong("SLS_QTN_ID"));
			slsQtnVrsnVO.setSlsQtnTatXcddRsnCd(resultSet.getString("SLS_QTN_TAT_XCDD_RSN_CD"));
			slsQtnVrsnVO.setSlsQtnVrsnRqstrCd(resultSet.getString("SLS_QTN_VRSN_RQSTR_CD"));
			slsQtnVrsnVO.setSlsQtnVrsnRsnCd(resultSet.getString("SLS_QTN_VRSN_RSN_CD"));
			slsQtnVrsnVO.setSlsQtnVrsnSqnNr(resultSet.getString("SLS_QTN_VRSN_SQN_NR"));
			slsQtnVrsnVO.setSlsQtnVrsnSttsCd(resultSet.getString("SLS_QTN_VRSN_STTS_CD"));
			slsQtnVrsnVO.setSlsQtnVrsnTatGoalCd(resultSet.getString("SLS_QTN_VRSN_TAT_GOAL_CD"));
			slsQtnVrsnVO.setSlsQtnVrsnTatGoalTS(resultSet.getTimestamp("SLS_QTN_VRSN_TAT_GOAL_TS"));
			slsQtnVrsnVO.setSlsQtnVrsnTypCd(resultSet.getString("SLS_QTN_VRSN_TYP_CD"));
			slsQtnVrsnVO.setTatXcddInd(resultSet.getString("TAT_XCDD_IND"));
		
		}catch(SQLException sqEx){
			Logger.repError(logInfo, "getSlsQtnVrsn", "Exception while creating VO for Sls quote version...", sqEx);
			throw new ReplicationException(sqEx.getMessage(), sqEx);
		}

		return slsQtnVrsnVO;
	}//createVO
	
	public void insertSlsQtnVrsn (SlsQtnVrsnVO slsQtnVrsnVO)throws ReplicationException{
		Logger.repDebug(logInfo, "insertSlsQtnVrsn", "Begining of inserting Sls quote version details...");
		String insertQuery = null;
		Connection con = null;
		
		try {
			con = getConnection(dataSourceName);
			
			insertQuery = queryRepository.getQuery("Quote","Create","SlsQtnVrsn"); 
			List<Object> options = new ArrayList<Object>();
			
			options.add(String.valueOf(slsQtnVrsnVO.getSlsQtnID()));
			options.add(slsQtnVrsnVO.getSlsQtnVrsnSqnNr());
			options.add(slsQtnVrsnVO.getDescription());
			options.add(ReplicationUtility.dateString(slsQtnVrsnVO.getCreationTS()));
			options.add(ReplicationUtility.dateString(slsQtnVrsnVO.getExpiryTS()));
			options.add(slsQtnVrsnVO.getCurrencyCd());
			options.add(ReplicationUtility.dateString(slsQtnVrsnVO.getHandOffTS()));
			options.add(slsQtnVrsnVO.getSlsQtnVrsnRsnCd());
			options.add(ReplicationUtility.dateString(slsQtnVrsnVO.getEffectiveTS()));
			options.add(slsQtnVrsnVO.getSlsQtnVrsnTypCd());
			options.add(slsQtnVrsnVO.getSlsQtnVrsnRqstrCd());
			options.add(slsQtnVrsnVO.getSlsQtnVrsnTatGoalCd());
			options.add(slsQtnVrsnVO.getCountryCd());
			options.add(slsQtnVrsnVO.getLangCd());
			options.add(slsQtnVrsnVO.getCharScriptCd());
			options.add(slsQtnVrsnVO.getSlsQtnTatXcddRsnCd());
			options.add(slsQtnVrsnVO.getCustPrchOrdNr());
			options.add(ReplicationUtility.dateString(slsQtnVrsnVO.getRqstFOrdRcvdTS()));
			options.add(ReplicationUtility.dateString(slsQtnVrsnVO.getCustRqstHndOffTS()));
			options.add(slsQtnVrsnVO.getEntrsLglEntNr());
			options.add(slsQtnVrsnVO.getSlsQtnVrsnSttsCd());
			options.add(String.valueOf(slsQtnVrsnVO.getSlsPersID()));
			options.add(slsQtnVrsnVO.getSlsChnlCd());
			options.add(ReplicationUtility.dateString(slsQtnVrsnVO.getSlsQtnVrsnTatGoalTS()));
			options.add(slsQtnVrsnVO.getTatXcddInd());
			options.add(ReplicationUtility.dateString(slsQtnVrsnVO.getDfltRqrdDlvryTS()));
			options.add(String.valueOf(slsQtnVrsnVO.getDfltRqrdDlvryLeadTmQty()));
			options.add(String.valueOf(slsQtnVrsnVO.getDfltInfcrOtrPrtySiteID()));
			options.add(String.valueOf(slsQtnVrsnVO.getDfltUltCnsgOtrPrtySiteID()));
			options.add(String.valueOf(slsQtnVrsnVO.getDfltPyrOtrPrtySiteInsnID()));
			options.add(String.valueOf(slsQtnVrsnVO.getDfltEndCustOtrPrtySiteID()));
			options.add(String.valueOf(slsQtnVrsnVO.getDfltInvToOtrPrtySiteID()));
			options.add(String.valueOf(slsQtnVrsnVO.getDfltShpToOtrPrtySiteID()));
			options.add(String.valueOf(slsQtnVrsnVO.getSldToOtrPrtySiteInsnID()));
			options.add(slsQtnVrsnVO.getCstmCnfgnInd());
			options.add(slsQtnVrsnVO.getCnfgnOvrdAprvlInd());
			options.add(String.valueOf(slsQtnVrsnVO.getDeleteInd()));
			options.add(ReplicationUtility.dateString(slsQtnVrsnVO.getCreatesTS()));
			options.add(slsQtnVrsnVO.getCreatedBy());
			options.add(ReplicationUtility.dateString(slsQtnVrsnVO.getLastModifiedTS()));
			options.add(slsQtnVrsnVO.getLastModifiedBy());
			options.add(ReplicationUtility.dateString(slsQtnVrsnVO.getCustPrqstTS()));

			executeUpdatePreparedStatement(con,insertQuery,options);
		}
		catch (Exception ex) {
			Logger.repError(logInfo, "insertSlsQtnVrsn", "Exception while inserting Sls quote version details.", ex);
			throw new ReplicationException(ex.getMessage(),ex);
		}
		finally{
			closeConnection(con);
		}
		Logger.repDebug(logInfo, "insertSlsQtnVrsn", "End of inserting Sls quote version details...");
				
	}//insertSlsQtnVrsn
	
	
	public void updateSlsQtnVrsn (long slsQtnID,String slsQtnVrsn, SlsQtnVrsnVO slsQtnVrsnVO)throws ReplicationException{
		Logger.repDebug(logInfo, "updateSlsQtnVrsn", "Begining of updating Sls quote version details for quotation id "+ slsQtnID);
		String updateQuery = null;
		Connection con = null;
		
		try {
			con = getConnection(dataSourceName);
			
			updateQuery = queryRepository.getQuery("Quote","Update","SlsQtnVrsn"); 
			List<Object> options = new ArrayList<Object>();
			
			//options.add(slsQtnVrsnVO.getSlsQtnVrsnSqnNr());
			options.add(slsQtnVrsnVO.getDescription());
			options.add(ReplicationUtility.dateString(slsQtnVrsnVO.getCreationTS()));
			options.add(ReplicationUtility.dateString(slsQtnVrsnVO.getExpiryTS()));
			options.add(slsQtnVrsnVO.getCurrencyCd());
			options.add(ReplicationUtility.dateString(slsQtnVrsnVO.getHandOffTS()));
			options.add(slsQtnVrsnVO.getSlsQtnVrsnRsnCd());
			options.add(ReplicationUtility.dateString(slsQtnVrsnVO.getEffectiveTS()));
			options.add(slsQtnVrsnVO.getSlsQtnVrsnTypCd());
			options.add(slsQtnVrsnVO.getSlsQtnVrsnRqstrCd());
			options.add(slsQtnVrsnVO.getSlsQtnVrsnTatGoalCd());
			options.add(slsQtnVrsnVO.getCountryCd());
			options.add(slsQtnVrsnVO.getLangCd());
			options.add(slsQtnVrsnVO.getCharScriptCd());
			options.add(slsQtnVrsnVO.getSlsQtnTatXcddRsnCd());
			options.add(slsQtnVrsnVO.getCustPrchOrdNr());
			options.add(ReplicationUtility.dateString(slsQtnVrsnVO.getRqstFOrdRcvdTS()));
			options.add(ReplicationUtility.dateString(slsQtnVrsnVO.getCustRqstHndOffTS()));
			options.add(slsQtnVrsnVO.getEntrsLglEntNr());
			options.add(slsQtnVrsnVO.getSlsQtnVrsnSttsCd());
			options.add(String.valueOf(slsQtnVrsnVO.getSlsPersID()));
			options.add(slsQtnVrsnVO.getSlsChnlCd());
			options.add(ReplicationUtility.dateString(slsQtnVrsnVO.getSlsQtnVrsnTatGoalTS()));
			options.add(slsQtnVrsnVO.getTatXcddInd());
			options.add(ReplicationUtility.dateString(slsQtnVrsnVO.getDfltRqrdDlvryTS()));
			options.add(String.valueOf(slsQtnVrsnVO.getDfltRqrdDlvryLeadTmQty()));
			options.add(String.valueOf(slsQtnVrsnVO.getDfltInfcrOtrPrtySiteID()));
			options.add(String.valueOf(slsQtnVrsnVO.getDfltUltCnsgOtrPrtySiteID()));
			options.add(String.valueOf(slsQtnVrsnVO.getDfltPyrOtrPrtySiteInsnID()));
			options.add(String.valueOf(slsQtnVrsnVO.getDfltEndCustOtrPrtySiteID()));
			options.add(String.valueOf(slsQtnVrsnVO.getDfltInvToOtrPrtySiteID()));
			options.add(String.valueOf(slsQtnVrsnVO.getDfltShpToOtrPrtySiteID()));
			options.add(String.valueOf(slsQtnVrsnVO.getSldToOtrPrtySiteInsnID()));
			options.add(slsQtnVrsnVO.getCstmCnfgnInd());
			options.add(slsQtnVrsnVO.getCnfgnOvrdAprvlInd());
			options.add(String.valueOf(slsQtnVrsnVO.getDeleteInd()));
			options.add(ReplicationUtility.dateString(slsQtnVrsnVO.getCreatesTS()));
			options.add(slsQtnVrsnVO.getCreatedBy());
			options.add(ReplicationUtility.dateString(slsQtnVrsnVO.getLastModifiedTS()));
			options.add(slsQtnVrsnVO.getLastModifiedBy());
			options.add(ReplicationUtility.dateString(slsQtnVrsnVO.getCustPrqstTS()));

			options.add(String.valueOf(slsQtnID)); //for where condition
			options.add(slsQtnVrsn); //for where condition
			
			int rowUpdated = executeUpdatePreparedStatement(con,updateQuery,options);
			if(rowUpdated == 0){
				Logger.repDebug(logInfo, "updateQuote", "SLS_QTN_VRSN doesn't exist for SLS_QTN_ID =  "+slsQtnID+ " and SLS_QTN_VRSN = "+slsQtnVrsn+" .Inserting record now.");
				insertSlsQtnVrsn(slsQtnVrsnVO);
			}
		}
		catch (Exception ex) {
			Logger.repError(logInfo, "updateSlsQtnVrsn", "Exception while updating Slsq uote version details...", ex);
			throw new ReplicationException(ex.getMessage(),ex);
		}
		finally{
			closeConnection(con);
		}
		Logger.repDebug(logInfo, "updateSlsQtnVrsn", "End of updating Sls quote version details for quotation id "+ slsQtnID);
				
	}//updateSlsQtnVrsn
	
	
	public void deleteSlsQtnVrsn (long slsQtnID, String slsQtnVrsn,SlsQtnVrsnVO slsQtnVrsnVO)throws ReplicationException{
		Logger.repDebug(logInfo, "deleteSlsQtnVrsn", "Begining of deleting Sls quote version for quotation id "+ slsQtnID);
		
		updateSlsQtnVrsn(slsQtnID,slsQtnVrsn, slsQtnVrsnVO);
		
		Logger.repDebug(logInfo, "deleteSlsQtnVrsn", "End of deleting Sls quote version for quotation id "+ slsQtnID);
	}//deleteSlsQtnVrsn


	
}//class
