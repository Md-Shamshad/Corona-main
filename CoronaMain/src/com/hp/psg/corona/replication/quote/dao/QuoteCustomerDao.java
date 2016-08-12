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
import com.hp.psg.corona.replication.quote.vo.QuoteCustomerVO;

public class QuoteCustomerDao extends QuoteConnection implements IBaseDao {
	QueryRepository queryRepository = null;
	String dataSourceName = null;
	LoggerInfo logInfo=null;


	public QuoteCustomerDao() {
		queryRepository = ReplicationQueryManager.getInstance();
		dataSourceName = Config.getProperty("corona.replication.secondary.data.source.name");
		logInfo = new LoggerInfo (this.getClass().getName());
	}

	@Override
	public void create(Connection con, long slsQtnID, String slsQtnVrsn)
	throws ReplicationException {
		List<QuoteCustomerVO> quoteCustomers = getQuoteCustomers(con,slsQtnID,slsQtnVrsn);
		if(quoteCustomers!=null && quoteCustomers.size()>0){
			for(QuoteCustomerVO quoteCustomerVO : quoteCustomers){
				insertQuoteCustomer(quoteCustomerVO);
			}
		}
	}

	@Override
	public void update(Connection con, long slsQtnID, String slsQtnVrsn)
	throws ReplicationException {
		
		Connection dstCon = null;		
		try {
			dstCon = getConnection(dataSourceName);
			
			updateQuoteCustomersDeleteFlag(dstCon, slsQtnID, slsQtnVrsn);
		
			List<QuoteCustomerVO> quoteCustomers = getQuoteCustomers(con,slsQtnID,slsQtnVrsn);
			if(quoteCustomers!=null && quoteCustomers.size()>0){
				for(QuoteCustomerVO quoteCustomerVO : quoteCustomers){
					updateQuoteCustomer(dstCon, slsQtnID, slsQtnVrsn, quoteCustomerVO);
				}
			}
			removeDeletedQuoteCustomers(dstCon, slsQtnID, slsQtnVrsn);
		}
		catch (Exception ex) {
			Logger.repError(logInfo, "update", "Exception while updating QuoteCustomer details...", ex);
			throw new ReplicationException(ex.getMessage(),ex);
		}
		finally{
			closeConnection(dstCon);
		}
	}

	@Override
	public void delete(Connection con, long slsQtnID, String slsQtnVrsn)
	throws ReplicationException {
		List<QuoteCustomerVO> quoteCustomers = getQuoteCustomers(con,slsQtnID,slsQtnVrsn);
		if(quoteCustomers!=null && quoteCustomers.size()>0){
			Connection dstCon = null;		
			try {
				dstCon = getConnection(dataSourceName);
				
				for(QuoteCustomerVO quoteCustomerVO : quoteCustomers){
					deleteQuoteCustomer(dstCon, slsQtnID, slsQtnVrsn, quoteCustomerVO);
				}
			}
			catch (Exception ex) {
				Logger.repError(logInfo, "delete", "Exception while deleting QuoteCustomer details...", ex);
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
		Logger.repDebug(logInfo, "change", "Incorrectly called change for QUOTE_CUSTOMER TABLE");

	}

	private void insertQuoteCustomer(QuoteCustomerVO quoteCustomerVO) throws ReplicationException {
		Logger.repDebug(logInfo, "insertQuoteCustomer", "Begining of inserting quote customer details...");

		String insertQuery = null;
		Connection con = null;

		try {
			con = getConnection(dataSourceName);

			insertQuery = queryRepository.getQuery("Quote","Create","QuoteCustomer"); 
			List<Object> options = new ArrayList<Object>();

			options.add(String.valueOf(quoteCustomerVO.getSlsQtnID()));
			options.add(quoteCustomerVO.getSlsQtnVrsnSqnNR());
			options.add(quoteCustomerVO.getCustomerType());
			options.add(quoteCustomerVO.getCompanyName());
			options.add(quoteCustomerVO.getCrmID());
			options.add(quoteCustomerVO.getCustomerLangCode());
			options.add(quoteCustomerVO.getCompnayNR());
			options.add(quoteCustomerVO.getDivision());
			options.add(quoteCustomerVO.getDuns());
			options.add(quoteCustomerVO.getPartnerID());
			options.add(String.valueOf(quoteCustomerVO.getDeleteInd()));
			options.add(ReplicationUtility.dateString(quoteCustomerVO.getCreatedTS()));
			options.add(quoteCustomerVO.getCreatedBy());
			options.add(ReplicationUtility.dateString(quoteCustomerVO.getLastModifiedTS()));
			options.add(quoteCustomerVO.getLastModifiedBy());
			options.add(quoteCustomerVO.getCustSegCd());

			executeUpdatePreparedStatement(con,insertQuery,options);
		}
		catch (Exception ex) {
			Logger.repError(logInfo, "insertQuoteCustomer", "Exception while inserting quote customer details...", ex);
			throw new ReplicationException(ex.getMessage(),ex);
		}
		finally{
			closeConnection(con);
		}
		Logger.repDebug(logInfo, "insertQuoteCustomer", "End of inserting quote customer details.");

	}

	private List<QuoteCustomerVO> getQuoteCustomers(Connection con,
			long slsQtnID, String slsQtnVrsn) throws ReplicationException {
		Logger.repDebug(logInfo, "getQuoteCustomers", "Begining of getting quote customer details for quotation id "+ slsQtnID+
				" and sls version "+slsQtnVrsn);
		List<QuoteCustomerVO> quoteCustomers = new ArrayList<QuoteCustomerVO>();
		QuoteCustomerVO quoteCustomerVO = null;
		String selectQuery = null;

		try {
			selectQuery = queryRepository.getQuery("Quote","Select","QuoteCustomer"); 
			List<Object> options = new ArrayList<Object>();
			options.add(String.valueOf(slsQtnID));
			options.add(slsQtnVrsn);

			ResultSet resultSet = runPreparedStatement(con, selectQuery, options);
			while (resultSet.next()){
				quoteCustomerVO = createVO(resultSet);
				quoteCustomers.add(quoteCustomerVO);
			}
		}catch(SQLException sqlEx){
			Logger.repError(logInfo, "getQuoteCustomers", "Exception while getting quote customer details...", sqlEx);
			throw new ReplicationException(sqlEx.getMessage(), sqlEx);
		}catch (Exception e) {
			Logger.repError(logInfo, "getQuoteCustomers", "Exception while getting quote customer details...", e);
			throw new ReplicationException(e.getMessage(), e);
		}

		Logger.repDebug(logInfo, "getQuoteCustomers", "End of getting quote customer details for quotation id "+ slsQtnID+
				" and sls version "+slsQtnVrsn);
		return quoteCustomers;
	}

	private QuoteCustomerVO createVO(ResultSet resultSet) throws ReplicationException {
		QuoteCustomerVO quoteCustomerVO = new QuoteCustomerVO();

		try{
			quoteCustomerVO.setCompanyName(resultSet.getString("COMPANY_NAME"));
			quoteCustomerVO.setCompnayNR(resultSet.getString("COMPANY_NR"));
			quoteCustomerVO.setCreatedBy(resultSet.getString("CREATED_BY"));
			quoteCustomerVO.setCreatedTS(resultSet.getTimestamp("CREATED_TS"));
			quoteCustomerVO.setCrmID(resultSet.getString("CRM_ID"));
			quoteCustomerVO.setCustomerLangCode(resultSet.getString("CUSTOMER_LANG_CODE"));
			quoteCustomerVO.setCustomerType(resultSet.getString("CUSTOMER_TYPE"));
			quoteCustomerVO.setCustSegCd(resultSet.getString("CUST_SEG_CD"));
			quoteCustomerVO.setDeleteInd((resultSet.getString("DELETE_IND")).charAt(0));
			quoteCustomerVO.setDivision(resultSet.getString("DIVISION"));
			quoteCustomerVO.setDuns(resultSet.getString("DUNS"));
			quoteCustomerVO.setLastModifiedBy(resultSet.getString("LAST_MODIFIED_BY"));
			quoteCustomerVO.setLastModifiedTS(resultSet.getTimestamp("LAST_MODIFIED_TS"));
			quoteCustomerVO.setPartnerID(resultSet.getString("PARTNER_ID"));
			quoteCustomerVO.setSlsQtnID(resultSet.getLong("SLS_QTN_ID"));
			quoteCustomerVO.setSlsQtnVrsnSqnNR(resultSet.getString("SLS_QTN_VRSN_SQN_NR"));

		}catch(SQLException sqlEx){
			Logger.repError(logInfo, "createVO", "Exception while creating QuoteCustomerVO...", sqlEx);
			throw new ReplicationException(sqlEx.getMessage(), sqlEx);
		}
		return quoteCustomerVO;
	}

	private void deleteQuoteCustomer(Connection con, long slsQtnID, String slsQtnVrsn,
			QuoteCustomerVO quoteCustomerVO) throws ReplicationException {

		updateQuoteCustomer(con, slsQtnID, slsQtnVrsn, quoteCustomerVO);

	}

	private void updateQuoteCustomer(Connection con, long slsQtnID, String slsQtnVrsn,
			QuoteCustomerVO quoteCustomerVO) throws ReplicationException {
		Logger.repDebug(logInfo, "updateQuoteCustomer", "Begining of updating quote customer details for quotation id "+ slsQtnID+
				" and sls version "+slsQtnVrsn);
		String updateQuery = null;
		try {
			updateQuery = queryRepository.getQuery("Quote","Update","QuoteCustomer"); 
			List<Object> options = new ArrayList<Object>();

			options.add(quoteCustomerVO.getCompanyName());
			options.add(quoteCustomerVO.getCrmID());
			options.add(quoteCustomerVO.getCustomerLangCode());
			options.add(quoteCustomerVO.getCompnayNR());
			options.add(quoteCustomerVO.getDivision());
			options.add(quoteCustomerVO.getDuns());
			options.add(quoteCustomerVO.getPartnerID());
			options.add(String.valueOf(quoteCustomerVO.getDeleteInd()));
			options.add(ReplicationUtility.dateString(quoteCustomerVO.getCreatedTS()));
			options.add(quoteCustomerVO.getCreatedBy());
			options.add(ReplicationUtility.dateString(quoteCustomerVO.getLastModifiedTS()));
			options.add(quoteCustomerVO.getLastModifiedBy());
			options.add(quoteCustomerVO.getCustSegCd());

			//for where conditions
			options.add(String.valueOf(slsQtnID));
			options.add(slsQtnVrsn);
			options.add(quoteCustomerVO.getCustomerType());

			int rowUpdated = executeUpdatePreparedStatement(con,updateQuery,options);
			if(rowUpdated == 0){
				Logger.repDebug(logInfo, "updateQuoteCustomer", "Quote customer doesn't exist for SLS_QTN_ID =  "+slsQtnID+ " and SLS_QTN_VRSN = "
						+slsQtnVrsn+" .Inserting record now.");
				insertQuoteCustomer(quoteCustomerVO);
			}
		}
		catch (Exception ex) {
			Logger.repError(logInfo, "updateQuoteCustomer", "Exception while updating quote customer details...", ex);
			throw new ReplicationException(ex.getMessage(),ex);
		}
		
		Logger.repDebug(logInfo, "updateQuoteCustomer", "End of updating quote customer details for quotation id "+ slsQtnID+
				" and sls version "+slsQtnVrsn);

	}
	
	private void removeDeletedQuoteCustomers(Connection con, long slsQtnID, String slsQtnVrsn) throws ReplicationException {
		String query = null;
		
		try {
			query = queryRepository.getQuery("Quote","Delete","DeleteQuoteCustomers"); 
			List<Object> options = new ArrayList<Object>();
			options.add(String.valueOf(slsQtnID));
			options.add(slsQtnVrsn);
			
			executeUpdatePreparedStatement(con,query,options);
		}
		catch (Exception ex) {
			Logger.repError(logInfo, "removeDeletedQuoteCustomers", "Exception while removeDeleteQuoteCustomers...", ex);
			throw new ReplicationException(ex.getMessage(),ex);
		}
			
		Logger.repDebug(logInfo, "removeDeletedQuoteCustomers", "End of removeDeleteQuoteCustomers for quotation id "+ slsQtnID + " and version "+slsQtnVrsn);
		
	}

	private void updateQuoteCustomersDeleteFlag(Connection con, long slsQtnID, String slsQtnVrsn) throws ReplicationException {
		String query = null;
		try {
			query = queryRepository.getQuery("Quote","Update","QuoteCustomerDeleteFlag"); 
			List<Object> options = new ArrayList<Object>();
			options.add(String.valueOf(slsQtnID));
			options.add(slsQtnVrsn);
			
			executeUpdatePreparedStatement(con,query,options);
		}
		catch (Exception ex) {
			Logger.repError(logInfo, "updateQuoteCustomersDeleteFlag", "Exception while updating QuoteCustomerDeleteFlag...", ex);
			throw new ReplicationException(ex.getMessage(),ex);
		}
				
		Logger.repDebug(logInfo, "updateQuoteCustomersDeleteFlag", "End of updating QuoteCustomerDeleteFlag for quotation id "+ slsQtnID +" and version "+slsQtnVrsn);
		
	}

}
