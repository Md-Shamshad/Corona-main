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
import com.hp.psg.corona.replication.quote.vo.QuoteVersionVO;

/**
 * DAO for QUOTE_VERSION table
 * @author rohitc
 *
 */
public class QuoteVersionDao extends QuoteConnection implements IBaseDao{
	QueryRepository queryRepository = null;
	String dataSourceName = null;
	LoggerInfo logInfo=null;
	
	public QuoteVersionDao() {
		super();
		queryRepository = ReplicationQueryManager.getInstance();
		dataSourceName = Config.getProperty("corona.replication.secondary.data.source.name");
		logInfo = new LoggerInfo (this.getClass().getName());
	}
		
	@Override
	public void create(Connection con, long slsQtnID, String slsQtnVrsn)throws ReplicationException{
		QuoteVersionVO quoteVersionVO = getQuoteVersion(con,slsQtnID,slsQtnVrsn);
		if(quoteVersionVO!=null){
			insertQuoteVersion(quoteVersionVO);
		}
	}
	
	@Override
	public void update(Connection con, long slsQtnID, String slsQtnVrsn)throws ReplicationException{
		QuoteVersionVO quoteVersionVO = getQuoteVersion(con,slsQtnID,slsQtnVrsn);
		if(quoteVersionVO!=null){
			updateQuoteVersion(slsQtnID,slsQtnVrsn,quoteVersionVO);
		}
	}
	
	@Override
	public void delete(Connection con, long slsQtnID, String slsQtnVrsn)throws ReplicationException{
		QuoteVersionVO quoteVersionVO = getQuoteVersion(con,slsQtnID,slsQtnVrsn);
		if(quoteVersionVO!=null){
			deleteQuoteVersion(slsQtnID,slsQtnVrsn,quoteVersionVO);
		}
	}
	
	@Override
	public void change(Connection con, long slsQtnID, String slsQtnVrsn)
			throws ReplicationException {
		Logger.repDebug(logInfo, "change", "Incorrectly called change for QUOTE_VRSN TABLE");
	}
	
	public QuoteVersionVO getQuoteVersion(Connection con, long slsQtnID, String slsQtnVrsn) throws ReplicationException{
		Logger.repDebug(logInfo, "getQuoteVersion", "Begining of getting quote version details for quotation id "+ slsQtnID);
		QuoteVersionVO quoteVersionVO = null;
		String selectQuery = null;
				
		try {
			selectQuery = queryRepository.getQuery("Quote","Select","QuoteVersion"); 
			List<Object> options = new ArrayList<Object>();
			options.add(String.valueOf(slsQtnID));
			options.add(slsQtnVrsn);

			ResultSet resultSet = runPreparedStatement(con, selectQuery, options);
			if (resultSet.next()){
				quoteVersionVO = createVO(resultSet);
			}
		}catch(SQLException sqlEx){
			Logger.repError(logInfo, "getQuoteVersion", "Exception while getting quote item details...", sqlEx);
			throw new ReplicationException(sqlEx.getMessage(), sqlEx);
		}catch (Exception ex) {
			Logger.repError(logInfo, "getQuoteVersion", "Exception while getting quote item details...", ex);
			throw new ReplicationException(ex.getMessage(), ex);
		}
		Logger.repDebug(logInfo, "getQuoteVersion", "End of getting quote version details for quotation id "+ slsQtnID);
		
		return quoteVersionVO;
	}//getQuoteVersion
	
	//create record in QuoteVersion table 
	public void insertQuoteVersion(QuoteVersionVO quoteVersionVO)throws ReplicationException{
		Logger.repDebug(logInfo, "insertQuoteVersion", "Begining of inserting quote version details...");
		
		String insertQuery = null;
		Connection con = null;
		
		try {
			con = getConnection(dataSourceName);
			
			insertQuery = queryRepository.getQuery("Quote","Create","QuoteVersion"); 
			
			List<Object> options = new ArrayList<Object>();
			options.add(String.valueOf(quoteVersionVO.getSlsQtnID()));
			options.add(quoteVersionVO.getSlsQtnVrsnSqnNR());
			options.add(quoteVersionVO.getUqID());
			options.add(quoteVersionVO.getReferencedUQID());
			options.add(quoteVersionVO.getOrigAsset());
			options.add(ReplicationUtility.dateString(quoteVersionVO.getCompletionTimestamp()));
			options.add(quoteVersionVO.getDeliveryTerms());
			options.add(quoteVersionVO.getPriceListType());
			options.add(quoteVersionVO.getLastPricedDate());
			options.add(quoteVersionVO.getVisibilityState());
			options.add(quoteVersionVO.getShareState());
			options.add(quoteVersionVO.getRegionCode());
			options.add(quoteVersionVO.getPriceGeo());
			options.add(quoteVersionVO.getCustomerCompanyName());
			options.add(quoteVersionVO.getHideZeroDollarInd());
			options.add(quoteVersionVO.getPaNR());
			options.add(quoteVersionVO.getPaCac());
			options.add(quoteVersionVO.getPaDiscountGeo());
			options.add(quoteVersionVO.geteDeliveryEmail());
			options.add(quoteVersionVO.getOpportunityID());
			options.add(ReplicationUtility.dateString(quoteVersionVO.getModifiedTimestamp()));
			options.add(quoteVersionVO.getModifiedPersonID());
			options.add(String.valueOf(quoteVersionVO.getTotalAmt()));
			options.add(quoteVersionVO.getAirPackedTotalWeight());
			options.add(ReplicationUtility.dateString(quoteVersionVO.getPaExpiryTS()));
			options.add(quoteVersionVO.getAssetQuoteNR());
			options.add(String.valueOf(quoteVersionVO.getDeleteInd()));
			options.add(ReplicationUtility.dateString(quoteVersionVO.getCreatedTimestamp()));
			options.add(quoteVersionVO.getCreatedBy());
			options.add(ReplicationUtility.dateString(quoteVersionVO.getLastModifiedTimestamp()));
			options.add(quoteVersionVO.getLastModifiedBy());
			options.add(quoteVersionVO.getCreationPersonID());
			options.add(quoteVersionVO.getEucRfpNR());
			options.add(quoteVersionVO.getRslrExclvFL());
			options.add(quoteVersionVO.getwLStatCD());
			options.add(String.valueOf(quoteVersionVO.getEstTotalKAM()));
			options.add(ReplicationUtility.dateString(quoteVersionVO.getQuoteDistDtGmt()));
			options.add(quoteVersionVO.getBusModelCD());
			options.add(quoteVersionVO.getMcCharge());
			options.add(quoteVersionVO.getOpgNum());
			options.add(String.valueOf(quoteVersionVO.getPaymentDays()));
			options.add(String.valueOf(quoteVersionVO.getCashDiscPct()));
			options.add(String.valueOf(quoteVersionVO.getBalToFinance()));
			options.add(String.valueOf(quoteVersionVO.getLeaseTerm()));
			options.add(String.valueOf(quoteVersionVO.getPayFrequency()));
			options.add(quoteVersionVO.getBdmEmpNotifyFL());
			options.add(String.valueOf(quoteVersionVO.getPayAmount()));
			options.add(ReplicationUtility.dateString(quoteVersionVO.getOrderBgnDT()));
			options.add(ReplicationUtility.dateString(quoteVersionVO.getOrderEndDT()));
			options.add(ReplicationUtility.dateString(quoteVersionVO.getQuoteDistribDT()));
			options.add(quoteVersionVO.getComplexDealFL());
			options.add(quoteVersionVO.getAgentDealFL());
			options.add(quoteVersionVO.getDealKind());
			options.add(quoteVersionVO.getPrimaryQuoteURL());
			options.add(quoteVersionVO.getDirectFL());
			options.add(String.valueOf(quoteVersionVO.getTermCondID()));
			options.add(quoteVersionVO.getQuotePrimaryRslsType());
			options.add(quoteVersionVO.getOpportunityOrigSystem());
			options.add(quoteVersionVO.getRequestID());
			options.add(String.valueOf(quoteVersionVO.getAssetQuoteVrsn()));
			options.add(quoteVersionVO.getPdfRefID());
			options.add(quoteVersionVO.getPdfEncryptedID());
			options.add(quoteVersionVO.getName());
			options.add(quoteVersionVO.getPaymentTerm());
			options.add(String.valueOf(quoteVersionVO.getSubTotalAmt()));
			options.add(String.valueOf(quoteVersionVO.getTotalTaxAmt()));
			options.add(String.valueOf(quoteVersionVO.getDiscountPct()));
			options.add(quoteVersionVO.getDealNR());
			options.add(String.valueOf(quoteVersionVO.getTotalListPriceAmt()));
			options.add(quoteVersionVO.getAssetInstance());
			options.add(quoteVersionVO.getSfdcStatus());
			options.add(ReplicationUtility.dateString(quoteVersionVO.getRequestFQuoteRcvdTS()));
			options.add(String.valueOf(quoteVersionVO.getShippingAndHandlingAmt()));
			
			executeUpdatePreparedStatement(con,insertQuery,options);
			
		}catch (Exception ex) {
			Logger.repError(logInfo, "insertQuoteVersion", "Exception while inserting quote version details...", ex);
			throw new ReplicationException(ex.getMessage(),ex);
		}
		finally{
			closeConnection(con);
		}	
		Logger.repDebug(logInfo, "insertQuoteVersion", "End of inserting quote version details.");
		
	}//insertQuoteVersion
	
	
	public void updateQuoteVersion(long slsQtnID,String slsQtnVrsn, QuoteVersionVO quoteVersionVO) throws ReplicationException {
		Logger.repDebug(logInfo, "updateQuoteVersion", "Begining of updating quote version details for quotation id "+ slsQtnID);	
		
		String updateQuery = null;
		Connection con = null;
		
		try {
			con = getConnection(dataSourceName);
			
			updateQuery = queryRepository.getQuery("Quote","Update","QuoteVersion"); 
			
			List<Object> options = new ArrayList<Object>();
			
			options.add(quoteVersionVO.getUqID());
			options.add(quoteVersionVO.getReferencedUQID());
			options.add(quoteVersionVO.getOrigAsset());
			options.add(ReplicationUtility.dateString(quoteVersionVO.getCompletionTimestamp()));
			options.add(quoteVersionVO.getDeliveryTerms());
			options.add(quoteVersionVO.getPriceListType());
			options.add(quoteVersionVO.getLastPricedDate());
			options.add(quoteVersionVO.getVisibilityState());
			options.add(quoteVersionVO.getShareState());
			options.add(quoteVersionVO.getRegionCode());
			options.add(quoteVersionVO.getPriceGeo());
			options.add(quoteVersionVO.getCustomerCompanyName());
			options.add(quoteVersionVO.getHideZeroDollarInd());
			options.add(quoteVersionVO.getPaNR());
			options.add(quoteVersionVO.getPaCac());
			options.add(quoteVersionVO.getPaDiscountGeo());
			options.add(quoteVersionVO.geteDeliveryEmail());
			options.add(quoteVersionVO.getOpportunityID());
			options.add(ReplicationUtility.dateString(quoteVersionVO.getModifiedTimestamp()));
			options.add(quoteVersionVO.getModifiedPersonID());
			options.add(String.valueOf(quoteVersionVO.getTotalAmt()));
			options.add(quoteVersionVO.getAirPackedTotalWeight());
			options.add(ReplicationUtility.dateString(quoteVersionVO.getPaExpiryTS()));
			options.add(quoteVersionVO.getAssetQuoteNR());
			options.add(String.valueOf(quoteVersionVO.getDeleteInd()));
			options.add(ReplicationUtility.dateString(quoteVersionVO.getCreatedTimestamp()));
			options.add(quoteVersionVO.getCreatedBy());
			options.add(ReplicationUtility.dateString(quoteVersionVO.getLastModifiedTimestamp()));
			options.add(quoteVersionVO.getLastModifiedBy());
			options.add(quoteVersionVO.getCreationPersonID());
			options.add(quoteVersionVO.getEucRfpNR());
			options.add(quoteVersionVO.getRslrExclvFL());
			options.add(quoteVersionVO.getwLStatCD());
			options.add(String.valueOf(quoteVersionVO.getEstTotalKAM()));
			options.add(ReplicationUtility.dateString(quoteVersionVO.getQuoteDistDtGmt()));
			options.add(quoteVersionVO.getBusModelCD());
			options.add(quoteVersionVO.getMcCharge());
			options.add(quoteVersionVO.getOpgNum());
			options.add(String.valueOf(quoteVersionVO.getPaymentDays()));
			options.add(String.valueOf(quoteVersionVO.getCashDiscPct()));
			options.add(String.valueOf(quoteVersionVO.getBalToFinance()));
			options.add(String.valueOf(quoteVersionVO.getLeaseTerm()));
			options.add(String.valueOf(quoteVersionVO.getPayFrequency()));
			options.add(quoteVersionVO.getBdmEmpNotifyFL());
			options.add(String.valueOf(quoteVersionVO.getPayAmount()));
			options.add(ReplicationUtility.dateString(quoteVersionVO.getOrderBgnDT()));
			options.add(ReplicationUtility.dateString(quoteVersionVO.getOrderEndDT()));
			options.add(ReplicationUtility.dateString(quoteVersionVO.getQuoteDistribDT()));
			options.add(quoteVersionVO.getComplexDealFL());
			options.add(quoteVersionVO.getAgentDealFL());
			options.add(quoteVersionVO.getDealKind());
			options.add(quoteVersionVO.getPrimaryQuoteURL());
			options.add(quoteVersionVO.getDirectFL());
			options.add(String.valueOf(quoteVersionVO.getTermCondID()));
			options.add(quoteVersionVO.getQuotePrimaryRslsType());
			options.add(quoteVersionVO.getOpportunityOrigSystem());
			options.add(quoteVersionVO.getRequestID());
			options.add(String.valueOf(quoteVersionVO.getAssetQuoteVrsn()));
			options.add(quoteVersionVO.getPdfRefID());
			options.add(quoteVersionVO.getPdfEncryptedID());
			options.add(quoteVersionVO.getName());
			options.add(quoteVersionVO.getPaymentTerm());
			options.add(String.valueOf(quoteVersionVO.getSubTotalAmt()));
			options.add(String.valueOf(quoteVersionVO.getTotalTaxAmt()));
			options.add(String.valueOf(quoteVersionVO.getDiscountPct()));
			options.add(quoteVersionVO.getDealNR());
			options.add(String.valueOf(quoteVersionVO.getTotalListPriceAmt()));
			options.add(quoteVersionVO.getAssetInstance());
			options.add(quoteVersionVO.getSfdcStatus());
			options.add(ReplicationUtility.dateString(quoteVersionVO.getRequestFQuoteRcvdTS()));
			options.add(String.valueOf(quoteVersionVO.getShippingAndHandlingAmt()));
		
			options.add(String.valueOf(quoteVersionVO.getSlsQtnID()));  //for where condition
			options.add(slsQtnVrsn); //for where condition
			
			int rowUpdated = executeUpdatePreparedStatement(con,updateQuery,options);
			if(rowUpdated == 0){
				Logger.repDebug(logInfo, "updateQuote", "QUOTE_VERSION doesn't exist for SLS_QTN_ID =  "+slsQtnID+ " and SLS_QTN_VRSN = "+slsQtnVrsn+" .Inserting record now.");
				insertQuoteVersion(quoteVersionVO);
			}
			
		}catch (Exception ex) {
			Logger.repError(logInfo, "getQuoteVersion", "Exception while updating quote version details...", ex);
			throw new ReplicationException(ex.getMessage(),ex);
		}
		finally{
			closeConnection(con);
		}	
		Logger.repDebug(logInfo, "updateQuoteVersion", "End of updating quote version details for quotation id "+ slsQtnID);
		
	}//updateQuoteVersion
	
	//in case of delete copy complete record. #to be checked...do we need to update DELETE_IND column 
	public void deleteQuoteVersion(long slsQtnID,String slsQtnVrsn, QuoteVersionVO quoteVersionVO) throws ReplicationException{
		Logger.repDebug(logInfo, "deleteQuoteVersion", "Begining of deleting quote version for quotation id "+ slsQtnID);
		
		updateQuoteVersion(slsQtnID,slsQtnVrsn,quoteVersionVO);
		
		Logger.repDebug(logInfo, "deleteQuoteVersion", "End of deleting quote version for quotation id "+ slsQtnID);
	}

	private QuoteVersionVO createVO(ResultSet rs) throws ReplicationException {
		QuoteVersionVO quoteVersionVO =  new QuoteVersionVO();
		
		try{
			quoteVersionVO.setAgentDealFL(rs.getString("AGENT_DEAL_FL"));
			quoteVersionVO.setAirPackedTotalWeight(rs.getString("AIRPACKED_TOTALWEIGHT"));
			quoteVersionVO.setAssetQuoteNR(rs.getString("ASSET_QUOTE_NR"));
			quoteVersionVO.setAssetQuoteVrsn(rs.getInt("ASSET_QUOTE_VRSN"));
			quoteVersionVO.setBalToFinance(rs.getDouble("BAL_TO_FINANCE"));
			quoteVersionVO.setBdmEmpNotifyFL(rs.getString("BDM_EXP_NOTIFY_FL"));
			quoteVersionVO.setBusModelCD(rs.getString("BUS_MODEL_CD"));
			quoteVersionVO.setCashDiscPct(rs.getDouble("CASH_DISC_PCT"));
			quoteVersionVO.setCompletionTimestamp(rs.getDate("COMPLETION_TS"));
			quoteVersionVO.setComplexDealFL(rs.getString("COMPLEX_DEAL_FL"));
			quoteVersionVO.setCreatedBy(rs.getString("CREATED_BY"));
			quoteVersionVO.setCreatedTimestamp(rs.getDate("CREATED_TS"));
			quoteVersionVO.setCreationPersonID(rs.getString("CREATION_PERSON_ID"));
			quoteVersionVO.setCustomerCompanyName(rs.getString("CUSTOMER_COMPANY_NAME"));
			quoteVersionVO.setDealKind(rs.getString("DEAL_KIND"));
			quoteVersionVO.setDealNR(rs.getString("DEAL_NR"));
			quoteVersionVO.setDeleteInd(rs.getString("DELETE_IND").charAt(0));
			quoteVersionVO.setDeliveryTerms(rs.getString("DELIVERY_TERMS"));
			quoteVersionVO.setDirectFL(rs.getString("DIRECT_FL"));
			quoteVersionVO.setDiscountPct(rs.getDouble("DISCOUNT_PCT"));
			quoteVersionVO.seteDeliveryEmail(rs.getString("E_DELIVERY_EMAIL"));
			quoteVersionVO.setEstTotalKAM(rs.getLong("EST_TOT_K_AM"));
			quoteVersionVO.setEucRfpNR(rs.getString("EUC_RFP_NR"));
			quoteVersionVO.setHideZeroDollarInd(rs.getString("HIDE_ZERO_DOLLAR_IND"));
			quoteVersionVO.setLastModifiedBy(rs.getString("LAST_MODIFIED_BY"));
			quoteVersionVO.setLastModifiedTimestamp(rs.getDate("LAST_MODIFIED_TS"));
			quoteVersionVO.setLastPricedDate(rs.getString("LAST_PRICED_DT"));
			quoteVersionVO.setLeaseTerm(rs.getLong("LEASE_TERM"));
			quoteVersionVO.setMcCharge(rs.getString("MC_CHARGE"));
			quoteVersionVO.setModifiedPersonID(rs.getString("MODIFIED_PERSON_ID"));
			quoteVersionVO.setModifiedTimestamp(rs.getDate("MODIFIED_TS"));
			quoteVersionVO.setName(rs.getString("NAME"));
			quoteVersionVO.setOpgNum(rs.getString("OPG_NUM"));
			quoteVersionVO.setOpportunityID(rs.getString("OPPORTUNITY_ID"));
			quoteVersionVO.setOpportunityOrigSystem(rs.getString("OPPORTUNITY_ORIG_SYSTEM"));
			quoteVersionVO.setOrderBgnDT(rs.getDate("ORDER_BGN_DT"));
			quoteVersionVO.setOrderEndDT(rs.getDate("ORDER_END_DT"));
			quoteVersionVO.setOrigAsset(rs.getString("ORIG_ASSET"));
			quoteVersionVO.setPaCac(rs.getString("PA_CAC"));
			quoteVersionVO.setPaDiscountGeo(rs.getString("PA_DISCOUNT_GEO"));
			quoteVersionVO.setPaExpiryTS(rs.getDate("PA_EXPIRY_TS"));
			quoteVersionVO.setPaNR(rs.getString("PA_NR"));
			quoteVersionVO.setPayAmount(rs.getDouble("PAY_AMOUNT"));
			quoteVersionVO.setPayFrequency(rs.getLong("PAY_FREQUENCY"));
			quoteVersionVO.setPaymentDays(rs.getLong("PAYMENT_DAYS"));
			quoteVersionVO.setPaymentTerm(rs.getString("PAYMENT_TERM"));
			quoteVersionVO.setPdfEncryptedID(rs.getString("PDF_ENCRYPTED_ID"));
			quoteVersionVO.setPdfRefID(rs.getString("PDF_REF_ID"));
			quoteVersionVO.setPriceGeo(rs.getString("PRICE_GEO"));
			quoteVersionVO.setPriceListType(rs.getString("PRICE_LIST_TYPE"));
			quoteVersionVO.setPrimaryQuoteURL(rs.getString("PRMRY_QUOTE_URL"));
			quoteVersionVO.setQuoteDistDtGmt(rs.getDate("QUOTE_DIST_DT_GMT"));
			quoteVersionVO.setQuoteDistribDT(rs.getDate("QUOTE_DISTRIB_DT"));
			quoteVersionVO.setQuotePrimaryRslsType(rs.getString("QUOTE_PRIMARY_RSLR_TYPE"));
			quoteVersionVO.setReferencedUQID(rs.getString("REFERENCED_UQID"));
			quoteVersionVO.setRegionCode(rs.getString("REGION_CD"));
			quoteVersionVO.setRequestID(rs.getString("REQUEST_ID"));
			quoteVersionVO.setRslrExclvFL(rs.getString("RSLR_EXCLV_FL"));
			quoteVersionVO.setShareState(rs.getString("SHARE_STATE"));
			quoteVersionVO.setSlsQtnID(rs.getLong("SLS_QTN_ID"));
			quoteVersionVO.setSlsQtnVrsnSqnNR(rs.getString("SLS_QTN_VRSN_SQN_NR"));
			quoteVersionVO.setSubTotalAmt(rs.getDouble("SUB_TOTAL_AMT"));
			quoteVersionVO.setTermCondID(rs.getLong("TERM_COND_ID"));
			quoteVersionVO.setTotalAmt(rs.getDouble("TOTAL_AMT"));
			quoteVersionVO.setTotalListPriceAmt(rs.getDouble("TOTAL_LIST_PRICE_AMT"));
			quoteVersionVO.setTotalTaxAmt(rs.getDouble("TOTAL_TAX_AMT"));
			quoteVersionVO.setUqID(rs.getString("UQID"));
			quoteVersionVO.setVisibilityState(rs.getString("VISIBILITY_STATE"));
			quoteVersionVO.setwLStatCD(rs.getString("W_L_STAT_CD"));
			quoteVersionVO.setAssetInstance(rs.getString("ASSET_INSTANCE"));
			quoteVersionVO.setRequestFQuoteRcvdTS(rs.getDate("REQUEST_F_QUOTE_RCVD_TS"));
			quoteVersionVO.setShippingAndHandlingAmt(rs.getDouble("SHIPPING_AND_HANDLING_AMT"));
			quoteVersionVO.setSfdcStatus(rs.getString("SFDC_STATUS"));
			
		}catch(SQLException sqlEx){
			Logger.repError(logInfo, "getQuoteVersion", "Exception while creating VOs for quote version...", sqlEx);
			throw new ReplicationException(sqlEx.getMessage(), sqlEx);
		}
		return quoteVersionVO;
	}
}
