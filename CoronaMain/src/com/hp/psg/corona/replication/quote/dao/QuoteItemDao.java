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
import com.hp.psg.corona.replication.quote.vo.QuoteItemVO;


/**
 * DAO for QUOTE_ITEM table
 * @author rohitc
 *
 */
public class QuoteItemDao extends QuoteConnection implements IBaseDao{
	QueryRepository queryRepository = null;
	String dataSourceName = null;
	LoggerInfo logInfo=null;
	
	public QuoteItemDao() {
		super();
		queryRepository = ReplicationQueryManager.getInstance();
		dataSourceName = Config.getProperty("corona.replication.secondary.data.source.name");
		logInfo = new LoggerInfo (this.getClass().getName());
	}
	
	@Override
	public void create(Connection con, long slsQtnID, String slsQtnVrsn)throws ReplicationException{
		List<QuoteItemVO> quoteItems = getQuoteItem(con,slsQtnID,slsQtnVrsn);
		if(quoteItems!=null && quoteItems.size()>0){
			for(QuoteItemVO quoteItemVO : quoteItems){
				insertQuoteItem(quoteItemVO);
			}
		}
	}
	
	@Override
	public void update(Connection con, long slsQtnID, String slsQtnVrsn)throws ReplicationException{
		
		Connection dstCon = null;		
		try {
			dstCon = getConnection(dataSourceName);
			
			// 1) update all items based on slsQtnID and slsQtnVrsn making delete flag = 'Y'
			updateQuoteItemsDeleteFlag(dstCon, slsQtnID, slsQtnVrsn);
				
			List<QuoteItemVO> quoteItems = getQuoteItem(con,slsQtnID,slsQtnVrsn);
			if(quoteItems!=null && quoteItems.size()>0){
				for(QuoteItemVO quoteItemVO : quoteItems){
					updateQuoteItem(dstCon, slsQtnID,slsQtnVrsn,quoteItemVO);
				}
			}
		
			// 3) now delete items which still have delete flag= 'Y'
			removeDeletedQuoteItems(dstCon, slsQtnID, slsQtnVrsn);
		}
		catch (Exception ex) {
			Logger.repError(logInfo, "update", "Exception while QuoteItems detail...", ex);
			throw new ReplicationException(ex.getMessage(),ex);
		}
		finally{
			closeConnection(dstCon);
		}
	}
	
	@Override
	public void delete(Connection con, long slsQtnID, String slsQtnVrsn)throws ReplicationException{
		List<QuoteItemVO> quoteItems = getQuoteItem(con,slsQtnID,slsQtnVrsn);
		if(quoteItems!=null && quoteItems.size()>0){
			Connection dstCon = null;		
			try {
				dstCon = getConnection(dataSourceName);
				
				for(QuoteItemVO quoteItemVO : quoteItems){
					deleteQuoteItem(dstCon, slsQtnID,slsQtnVrsn,quoteItemVO);
				}
			}
			catch (Exception ex) {
				Logger.repError(logInfo, "delete", "Exception while QuoteItems delete...", ex);
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
		Logger.repDebug(logInfo, "change", "Incorrectly called change for QUOTE_ITEM TABLE");
	}

	public List<QuoteItemVO> getQuoteItem(Connection con, long slsQtnID, String slsQtnVrsn) throws ReplicationException{
		Logger.repDebug(logInfo, "getQuoteItem", "Begining of getting quote item details for quotation id "+ slsQtnID);
		List<QuoteItemVO> quoteItems = new ArrayList<QuoteItemVO>();
		QuoteItemVO quoteItemVO = null;
		String selectQuery = null;
	
		try {
			selectQuery = queryRepository.getQuery("Quote","Select","QuoteItem"); 
			List<Object> options = new ArrayList<Object>();
			options.add(String.valueOf(slsQtnID));
			options.add(slsQtnVrsn);

			ResultSet resultSet = runPreparedStatement(con, selectQuery, options);
			while (resultSet.next()){
				quoteItemVO = createVO(resultSet);
				quoteItems.add(quoteItemVO);
			}
		}catch(SQLException sqlEx){
			Logger.repError(logInfo, "getQuoteItem", "Exception while getting quote item details...", sqlEx);
			throw new ReplicationException(sqlEx.getMessage(), sqlEx);
		}catch (Exception e) {
			Logger.repError(logInfo, "getQuoteItem", "Exception while getting quote item details...", e);
			throw new ReplicationException(e.getMessage(), e);
		}
		
		Logger.repDebug(logInfo, "getQuoteItem", "End of getting quote item details for quotation id "+ slsQtnID);
		return quoteItems;
	}
	
	/**
	 *  
	 * @param quoteItemVO
	 * @throws ReplicationException
	 */
	public void insertQuoteItem(QuoteItemVO quoteItemVO) throws ReplicationException{
		Logger.repDebug(logInfo, "insertQuoteItem", "Begining of inserting quote item details...");
		
		String insertQuery = null;
		Connection con = null;
		
		try {
			con = getConnection(dataSourceName);
			
			insertQuery = queryRepository.getQuery("Quote","Create","QuoteItem"); 
			List<Object> options = new ArrayList<Object>();
			
			//add all columns data from VO object for insert query
			options.add(quoteItemVO.getSlsQtnItmSqnNR());
			options.add(String.valueOf(quoteItemVO.getSlsQtnID()));
			options.add(quoteItemVO.getSlsQtnVrsnSqnNR());
			options.add(quoteItemVO.getProductNR());
			options.add(quoteItemVO.getUcID());
			options.add(quoteItemVO.getUcSubConfigID());
			options.add(String.valueOf(quoteItemVO.getUcLineItemID()));
			options.add(quoteItemVO.getPriceErrorMessage());
			options.add(quoteItemVO.getProductLine());
			options.add(String.valueOf(quoteItemVO.getPricingRate()));
			options.add(quoteItemVO.getPricingRateType());
			options.add(quoteItemVO.getPaNR());
			options.add(quoteItemVO.getPaExhibitNR());
			options.add(quoteItemVO.getPaCac());
			options.add(String.valueOf(quoteItemVO.getPaDiscountRate()));
			options.add(quoteItemVO.getPaDiscountGeo());
			options.add(ReplicationUtility.dateString(quoteItemVO.getPaExpiryDate()));
			options.add(quoteItemVO.getPaBuyerName());
			options.add(quoteItemVO.getPaCustomerName());
			options.add(quoteItemVO.getProductDescription());
			options.add(quoteItemVO.getPriceStatus());
			options.add(quoteItemVO.getUnitListPriceString());
			options.add(quoteItemVO.getUnitNetPriceString());
			options.add(quoteItemVO.getActivityStatusCode());
			options.add(quoteItemVO.getProductClassCode());
			options.add(quoteItemVO.getPriceSourceCode());
			options.add(quoteItemVO.getDealNR());
			options.add(String.valueOf(quoteItemVO.getAssetItemNR()));
			options.add(quoteItemVO.getProductOption());
			options.add(quoteItemVO.getAirpackedUnitWeight());
			options.add(quoteItemVO.getBundleID());
			options.add(String.valueOf(quoteItemVO.getDeleteInd()));
			options.add(ReplicationUtility.dateString(quoteItemVO.getCreatedTS()));
			options.add(quoteItemVO.getCreatedBy());
			options.add(ReplicationUtility.dateString(quoteItemVO.getLastModifiedTS()));
			options.add(quoteItemVO.getLastModifiedBy());
			options.add(quoteItemVO.getLineTypeCD());
			options.add(quoteItemVO.getAssocPfCD());
			options.add(quoteItemVO.getLineProgCD());
			options.add(quoteItemVO.getSpclConfigFL());
			options.add(quoteItemVO.getProductId());
			options.add(String.valueOf(quoteItemVO.getVersionCreated()));
			options.add(String.valueOf(quoteItemVO.getSpecialPaymentAmt()));
			options.add(quoteItemVO.getSrcConfigID());
			
			executeUpdatePreparedStatement(con,insertQuery,options);
		}
		catch (Exception ex) {
			Logger.repError(logInfo, "getQuoteItem", "Exception while inserting quote item details...", ex);
			throw new ReplicationException(ex.getMessage(),ex);
		}
		finally{
			closeConnection(con);
		}		
		Logger.repDebug(logInfo, "insertQuoteItem", "End of inserting quote item details.");
		
	}
	
	//update complete record based on slsQtnID and slsQtnVrsn
	public void updateQuoteItem(Connection con, long slsQtnID, String slsQtnVrsn, QuoteItemVO quoteItemVO) throws ReplicationException {
		Logger.repDebug(logInfo, "updateQuoteItem", "Begining of updating quote item details for quotation id "+ slsQtnID);
		
		String updateQuery = null;
		try {
			updateQuery = queryRepository.getQuery("Quote","Update","QuoteItem"); 
			List<Object> options = new ArrayList<Object>();
			
			//options.add(quoteItemVO.getSlsQtnItmSqnNR());
			//options.add(quoteItemVO.getSlsQtnVrsnSqnNR());
			options.add(quoteItemVO.getProductNR());
			options.add(quoteItemVO.getUcID());
			options.add(quoteItemVO.getUcSubConfigID());
			options.add(String.valueOf(quoteItemVO.getUcLineItemID()));
			options.add(quoteItemVO.getPriceErrorMessage());
			options.add(quoteItemVO.getProductLine());
			options.add(String.valueOf(quoteItemVO.getPricingRate()));
			options.add(quoteItemVO.getPricingRateType());
			options.add(quoteItemVO.getPaNR());
			options.add(quoteItemVO.getPaExhibitNR());
			options.add(quoteItemVO.getPaCac());
			options.add(String.valueOf(quoteItemVO.getPaDiscountRate()));
			options.add(quoteItemVO.getPaDiscountGeo());
			options.add(ReplicationUtility.dateString(quoteItemVO.getPaExpiryDate()));
			options.add(quoteItemVO.getPaBuyerName());
			options.add(quoteItemVO.getPaCustomerName());
			options.add(quoteItemVO.getProductDescription());
			options.add(quoteItemVO.getPriceStatus());
			options.add(quoteItemVO.getUnitListPriceString());
			options.add(quoteItemVO.getUnitNetPriceString());
			options.add(quoteItemVO.getActivityStatusCode());
			options.add(quoteItemVO.getProductClassCode());
			options.add(quoteItemVO.getPriceSourceCode());
			options.add(quoteItemVO.getDealNR());
			options.add(String.valueOf(quoteItemVO.getAssetItemNR()));
			options.add(quoteItemVO.getProductOption());
			options.add(quoteItemVO.getAirpackedUnitWeight());
			options.add(quoteItemVO.getBundleID());
			options.add(String.valueOf(quoteItemVO.getDeleteInd()));
			options.add(ReplicationUtility.dateString(quoteItemVO.getCreatedTS()));
			options.add(quoteItemVO.getCreatedBy());
			options.add(ReplicationUtility.dateString(quoteItemVO.getLastModifiedTS()));
			options.add(quoteItemVO.getLastModifiedBy());
			options.add(quoteItemVO.getLineTypeCD());
			options.add(quoteItemVO.getAssocPfCD());
			options.add(quoteItemVO.getLineProgCD());
			options.add(quoteItemVO.getSpclConfigFL());
			options.add(quoteItemVO.getProductId());
			options.add(String.valueOf(quoteItemVO.getVersionCreated()));
			options.add(String.valueOf(quoteItemVO.getSpecialPaymentAmt()));
			options.add(quoteItemVO.getSrcConfigID());
						
			options.add(String.valueOf(slsQtnID)); 		 	//for where condition
			options.add(quoteItemVO.getSlsQtnItmSqnNR()); 	//for where condition
			options.add(slsQtnVrsn); 						//for where condition
			
			int rowUpdated = executeUpdatePreparedStatement(con,updateQuery,options);
			if(rowUpdated == 0){
				Logger.repDebug(logInfo, "updateQuote", "QUOTE_ITEM doesn't exist for SLS_QTN_ID =  "+slsQtnID+ " and SLS_QTN_VRSN = "+slsQtnVrsn+" .Inserting record now.");
				insertQuoteItem(quoteItemVO);
			}
		}
		catch (Exception ex) {
			Logger.repError(logInfo, "getQuoteItem", "Exception while updating quote item details...", ex);
			throw new ReplicationException(ex.getMessage(),ex);
		}			
		Logger.repDebug(logInfo, "updateQuoteItem", "End of updating quote item details for quotation id "+ slsQtnID);
	}

	//in case of delete copy complete record. #to be checked...do we need to update DELETE_IND column 
	public void deleteQuoteItem(Connection con, long slsQtnID, String slsQtnVrsn, QuoteItemVO quoteItemVO) throws ReplicationException{
		Logger.repDebug(logInfo, "deleteQuoteItem", "Begining of deleting quote item for quotation id "+ slsQtnID);
		
		updateQuoteItem(con, slsQtnID,slsQtnVrsn,quoteItemVO);
		
		Logger.repDebug(logInfo, "deleteQuoteItem", "Begining of deleting quote item for quotation id "+ slsQtnID);
	}

	private QuoteItemVO createVO(ResultSet rs) throws ReplicationException {
		QuoteItemVO quoteItemVO = new QuoteItemVO();
		
		try{
			quoteItemVO.setActivityStatusCode(rs.getString("ACTIVITY_STATUS_CODE"));
			quoteItemVO.setAirpackedUnitWeight(rs.getString("AIRPACKED_UNITWEIGHT"));
			quoteItemVO.setAssetItemNR(rs.getLong("ASSET_ITEM_NR"));
			quoteItemVO.setAssocPfCD(rs.getString("ASSOC_PF_CD"));
			quoteItemVO.setBundleID(rs.getString("BUNDLE_ID"));
			quoteItemVO.setCreatedBy(rs.getString("CREATED_BY"));
			quoteItemVO.setCreatedTS(rs.getTimestamp("CREATED_TS"));
			quoteItemVO.setDealNR(rs.getString("DEAL_NR"));
			quoteItemVO.setDeleteInd((rs.getString("DELETE_IND")).charAt(0));
			quoteItemVO.setLastModifiedBy(rs.getString("LAST_MODIFIED_BY"));
			quoteItemVO.setLastModifiedTS(rs.getTimestamp("LAST_MODIFIED_TS"));
			quoteItemVO.setLineProgCD(rs.getString("LINE_PROG_CD"));
			quoteItemVO.setLineTypeCD(rs.getString("LINE_TYPE_CD"));
			quoteItemVO.setPaBuyerName(rs.getString("PA_BUYER_TYPE"));
			quoteItemVO.setPaCac(rs.getString("PA_CAC"));
			quoteItemVO.setPaCustomerName(rs.getString("PA_CUSTOMER_NAME"));
			quoteItemVO.setPaDiscountGeo(rs.getString("PA_DISCOUNT_GEO"));
			quoteItemVO.setPaDiscountRate(rs.getDouble("PA_DISCOUNT_RATE"));
			quoteItemVO.setPaExhibitNR(rs.getString("PA_EXHIBIT_NR"));
			quoteItemVO.setPaExpiryDate(rs.getDate("PA_EXPIRY_DT"));
			quoteItemVO.setPaNR(rs.getString("PA_NR"));
			quoteItemVO.setPriceErrorMessage(rs.getString("PRICE_ERROR_MSG"));
			quoteItemVO.setPriceSourceCode(rs.getString("PRICE_SOURCE_CODE"));
			quoteItemVO.setPriceStatus(rs.getString("PRICE_STATUS"));
			quoteItemVO.setPricingRate(rs.getDouble("PRICING_RATE"));
			quoteItemVO.setPricingRateType(rs.getString("PRICING_RATE_TYPE"));
			quoteItemVO.setProductId(rs.getString("PRODUCT_ID"));
			quoteItemVO.setProductClassCode(rs.getString("PRODUCT_CLASS_CODE"));
			quoteItemVO.setProductDescription(rs.getString("PRODUCT_DESCRIPTION"));
			quoteItemVO.setProductLine(rs.getString("PRODUCT_LINE"));
			quoteItemVO.setProductNR(rs.getString("PRODUCT_NR"));
			quoteItemVO.setProductOption(rs.getString("PRODUCT_OPTION"));
			quoteItemVO.setSlsQtnID(rs.getLong("SLS_QTN_ID"));
			quoteItemVO.setSlsQtnItmSqnNR(rs.getString("SLS_QTN_ITM_SQN_NR"));
			quoteItemVO.setSlsQtnVrsnSqnNR(rs.getString("SLS_QTN_VRSN_SQN_NR"));
			quoteItemVO.setSpclConfigFL(rs.getString("SPCL_CONFIG_FL"));
			quoteItemVO.setSpecialPaymentAmt(rs.getDouble("SPECIAL_PAYMENT_AMT"));
			quoteItemVO.setSrcConfigID(rs.getString("SOURCE_CONFIG_ID"));
			quoteItemVO.setUcID(rs.getString("UCID"));
			quoteItemVO.setUcLineItemID(rs.getLong("UC_LINE_ITEM_ID"));
			quoteItemVO.setUcSubConfigID(rs.getString("UC_SUB_CONFIG_ID"));
			quoteItemVO.setUnitListPriceString(rs.getString("UNIT_LIST_PRICE_STRING"));
			quoteItemVO.setUnitNetPriceString(rs.getString("UNIT_NET_PRICE_STRING"));
			quoteItemVO.setVersionCreated(rs.getInt("VERSION_CREATED"));
			
		}catch(SQLException sqlEx){
			Logger.repError(logInfo, "createVO", "Exception while creating VO details...", sqlEx);
			throw new ReplicationException(sqlEx.getMessage(), sqlEx);
		}
		return quoteItemVO;
	}
	
	private void removeDeletedQuoteItems(Connection con, long slsQtnID, String slsQtnVrsn) throws ReplicationException {
		String query = null;
		try {
			query = queryRepository.getQuery("Quote","Delete","DeleteQuoteItems"); 
			List<Object> options = new ArrayList<Object>();
			options.add(String.valueOf(slsQtnID));
			options.add(slsQtnVrsn);
			
			executeUpdatePreparedStatement(con,query,options);
		}
		catch (Exception ex) {
			Logger.repError(logInfo, "removeDeletedQuoteItems", "Exception while removeDeletedQuoteItems...", ex);
			throw new ReplicationException(ex.getMessage(),ex);
		}
			
		Logger.repDebug(logInfo, "removeDeletedQuoteItems", "End of removeDeletedQuoteItems for quotation id "+ slsQtnID);
	}

	private void updateQuoteItemsDeleteFlag(Connection con, long slsQtnID, String slsQtnVrsn) throws ReplicationException {
		String query = null;
		try {
			query = queryRepository.getQuery("Quote","Update","QuoteItemDeleteFlag"); 
			List<Object> options = new ArrayList<Object>();
			options.add(String.valueOf(slsQtnID));
			options.add(slsQtnVrsn);
			
			executeUpdatePreparedStatement(con,query,options);
		}
		catch (Exception ex) {
			Logger.repError(logInfo, "updateQuoteItemsDeleteFlag", "Exception while updating QuoteItemDeleteFlag...", ex);
			throw new ReplicationException(ex.getMessage(),ex);
		}
			
		Logger.repDebug(logInfo, "updateQuoteItemsDeleteFlag", "End of updating QuoteItemDeleteFlag for quotation id "+ slsQtnID);
	}
}
