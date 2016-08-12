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
import com.hp.psg.corona.replication.quote.vo.QuoteCustomerAddressVO;

public class QuoteCustomerAddressDao extends QuoteConnection implements
IBaseDao {
	QueryRepository queryRepository = null;
	String dataSourceName = null;
	LoggerInfo logInfo=null;


	public QuoteCustomerAddressDao() {
		queryRepository = ReplicationQueryManager.getInstance();
		dataSourceName = Config.getProperty("corona.replication.secondary.data.source.name");
		logInfo = new LoggerInfo (this.getClass().getName());
	}

	@Override
	public void create(Connection con, long slsQtnID, String slsQtnVrsn)
	throws ReplicationException {
		List<QuoteCustomerAddressVO> quoteCustomerAddrs = getQuoteAddresses(con,slsQtnID,slsQtnVrsn);
		if(quoteCustomerAddrs!=null && quoteCustomerAddrs.size()>0){
			for(QuoteCustomerAddressVO quoteCustomerAddressVO : quoteCustomerAddrs){
				insertQuoteCustomerAddress(quoteCustomerAddressVO);
			}
		}
	}

	@Override
	public void update(Connection con, long slsQtnID, String slsQtnVrsn)
	throws ReplicationException {
		
		Connection dstCon = null;		
		try {
			dstCon = getConnection(dataSourceName);
			
			updateQuoteCustAddressDeleteFlag(dstCon, slsQtnID, slsQtnVrsn);
		
			List<QuoteCustomerAddressVO> quoteCustomerAddrs = getQuoteAddresses(con,slsQtnID,slsQtnVrsn);
			if(quoteCustomerAddrs!=null && quoteCustomerAddrs.size()>0){
				for(QuoteCustomerAddressVO quoteCustomerAddressVO : quoteCustomerAddrs){
					updateQuoteCustomerAddress(dstCon, slsQtnID, slsQtnVrsn, quoteCustomerAddressVO);
				}
			}
			removeDeletedQuoteCustAddress(dstCon, slsQtnID, slsQtnVrsn);
		}
		catch (Exception ex) {
			Logger.repError(logInfo, "update", "Exception while updating QuoteCustAddress...", ex);
			throw new ReplicationException(ex.getMessage(),ex);
		}
		finally{
			closeConnection(dstCon);
		}	
	}

	@Override
	public void delete(Connection con, long slsQtnID, String slsQtnVrsn)
	throws ReplicationException {
		List<QuoteCustomerAddressVO> quoteCustomerAddrs = getQuoteAddresses(con,slsQtnID,slsQtnVrsn);
		if(quoteCustomerAddrs!=null && quoteCustomerAddrs.size()>0){
			Connection dstCon = null;		
			try {
				dstCon = getConnection(dataSourceName);
				
				for(QuoteCustomerAddressVO quoteCustomerAddressVO : quoteCustomerAddrs){
					deleteQuoteCustomerAddress(dstCon, slsQtnID, slsQtnVrsn, quoteCustomerAddressVO);
				}
			}
			catch (Exception ex) {
				Logger.repError(logInfo, "delete", "Exception while deleting QuoteCustAddress...", ex);
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
		Logger.repDebug(logInfo, "change", "Incorrectly called change for QUOTE_CUSTOMER_ADDRESS TABLE");

	}

	private void insertQuoteCustomerAddress(
			QuoteCustomerAddressVO quoteCustomerAddressVO) throws ReplicationException {

		Logger.repDebug(logInfo, "insertQuoteCustomerAddress", "Begining of inserting quote customer address details...");

		String insertQuery = null;
		Connection con = null;

		try {
			con = getConnection(dataSourceName);

			insertQuery = queryRepository.getQuery("Quote","Create","QuoteCustomerAddress"); 
			List<Object> options = new ArrayList<Object>();

			options.add(String.valueOf(quoteCustomerAddressVO.getSlsQtnID()));
			options.add(quoteCustomerAddressVO.getSlsQtnVrsnSqnNR());
			options.add(quoteCustomerAddressVO.getCustomerType());
			options.add(quoteCustomerAddressVO.getCity());
			options.add(quoteCustomerAddressVO.getCityArea());
			options.add(quoteCustomerAddressVO.getCountry());
			options.add(quoteCustomerAddressVO.getPhone());
			options.add(quoteCustomerAddressVO.getPhoneExt());
			options.add(quoteCustomerAddressVO.getRegion());
			options.add(quoteCustomerAddressVO.getState());
			options.add(quoteCustomerAddressVO.getStreet());
			options.add(quoteCustomerAddressVO.getStreet2());
			options.add(quoteCustomerAddressVO.getStreet3());
			options.add(quoteCustomerAddressVO.getPostalCode());
			options.add(String.valueOf(quoteCustomerAddressVO.getDeleteInd()));
			options.add(ReplicationUtility.dateString(quoteCustomerAddressVO.getCreatedTS()));
			options.add(quoteCustomerAddressVO.getCreatedBy());
			options.add(ReplicationUtility.dateString(quoteCustomerAddressVO.getLastModifiedTS()));
			options.add(quoteCustomerAddressVO.getLastModifiedBy());

			executeUpdatePreparedStatement(con,insertQuery,options);
		}
		catch (Exception ex) {
			Logger.repError(logInfo, "insertQuoteCustomerAddress", "Exception while inserting quote customer address details...", ex);
			throw new ReplicationException(ex.getMessage(),ex);
		}
		finally{
			closeConnection(con);
		}
		Logger.repDebug(logInfo, "insertQuoteCustomerAddress", "End of inserting quote customer address details.");
	}

	private List<QuoteCustomerAddressVO> getQuoteAddresses(Connection con,
			long slsQtnID, String slsQtnVrsn) throws ReplicationException {
		Logger.repDebug(logInfo, "getQuoteCustomerAddresses", "Begining of getting quote customer address details for quotation id "+ slsQtnID+
				" and sls version "+slsQtnVrsn);
		List<QuoteCustomerAddressVO> quoteAddresses = new ArrayList<QuoteCustomerAddressVO>();
		QuoteCustomerAddressVO quoteCustomerAddressVO = null;
		String selectQuery = null;

		try {
			selectQuery = queryRepository.getQuery("Quote","Select","QuoteCustomerAddress"); 
			List<Object> options = new ArrayList<Object>();
			options.add(String.valueOf(slsQtnID));
			options.add(slsQtnVrsn);

			ResultSet resultSet = runPreparedStatement(con, selectQuery, options);
			while (resultSet.next()){
				quoteCustomerAddressVO = createVO(resultSet);
				quoteAddresses.add(quoteCustomerAddressVO);
			}
		}catch(SQLException sqlEx){
			Logger.repError(logInfo, "getQuoteCustomerAddresses", "Exception while getting quote customer address details...", sqlEx);
			throw new ReplicationException(sqlEx.getMessage(), sqlEx);
		}catch (Exception e) {
			Logger.repError(logInfo, "getQuoteCustomerAddresses", "Exception while getting quote customer address details...", e);
			throw new ReplicationException(e.getMessage(), e);
		}

		Logger.repDebug(logInfo, "getQuoteCustomerAddresses", "End of getting quote customer address details for quotation id "+ slsQtnID+
				" and sls version "+slsQtnVrsn);
		return quoteAddresses;
	}

	private QuoteCustomerAddressVO createVO(ResultSet resultSet) {
		QuoteCustomerAddressVO quoteCustomerAddressVO =  new QuoteCustomerAddressVO();
		try{
			quoteCustomerAddressVO.setCity(resultSet.getString("CITY"));
			quoteCustomerAddressVO.setCityArea(resultSet.getString("CITYAREA"));
			quoteCustomerAddressVO.setCountry(resultSet.getString("COUNTRY"));
			quoteCustomerAddressVO.setCreatedBy(resultSet.getString("CREATED_BY"));
			quoteCustomerAddressVO.setCreatedTS(resultSet.getTimestamp("CREATED_TS"));
			quoteCustomerAddressVO.setCustomerType(resultSet.getString("CUSTOMER_TYPE"));
			quoteCustomerAddressVO.setDeleteInd((resultSet.getString("DELETE_IND")).charAt(0));
			quoteCustomerAddressVO.setLastModifiedBy(resultSet.getString("LAST_MODIFIED_BY"));
			quoteCustomerAddressVO.setLastModifiedTS(resultSet.getTimestamp("LAST_MODIFIED_TS"));
			quoteCustomerAddressVO.setPhone(resultSet.getString("PHONE"));
			quoteCustomerAddressVO.setPhoneExt(resultSet.getString("PHONE_EXT"));
			quoteCustomerAddressVO.setPostalCode(resultSet.getString("POSTALCODE"));
			quoteCustomerAddressVO.setRegion(resultSet.getString("REGION"));
			quoteCustomerAddressVO.setSlsQtnID(resultSet.getLong("SLS_QTN_ID"));
			quoteCustomerAddressVO.setSlsQtnVrsnSqnNR(resultSet.getString("SLS_QTN_VRSN_SQN_NR"));
			quoteCustomerAddressVO.setState(resultSet.getString("STATE"));
			quoteCustomerAddressVO.setStreet(resultSet.getString("STREET"));
			quoteCustomerAddressVO.setStreet2(resultSet.getString("STREET2"));
			quoteCustomerAddressVO.setStreet3(resultSet.getString("STREET3"));
		}catch(SQLException sqlEx){
			Logger.repError(logInfo, "createVO", "Exception while creating QuoteCustomerAddressVO...", sqlEx);
		}
		return quoteCustomerAddressVO;
	}

	private void deleteQuoteCustomerAddress(Connection con, long slsQtnID, String slsQtnVrsn,
			QuoteCustomerAddressVO quoteCustomerAddressVO) throws ReplicationException {
		updateQuoteCustomerAddress(con, slsQtnID, slsQtnVrsn, quoteCustomerAddressVO);

	}


	private void updateQuoteCustomerAddress(Connection con, long slsQtnID, String slsQtnVrsn,
			QuoteCustomerAddressVO quoteCustomerAddressVO) throws ReplicationException {
		Logger.repDebug(logInfo, "updateQuoteCustomerAddress", "Begining of updating quote customer address details for quotation id "+ slsQtnID+
				" and sls version "+slsQtnVrsn);
		String updateQuery = null;
		try {
			updateQuery = queryRepository.getQuery("Quote","Update","QuoteCustomerAddress"); 
			List<Object> options = new ArrayList<Object>();

			options.add(quoteCustomerAddressVO.getCity());
			options.add(quoteCustomerAddressVO.getCityArea());
			options.add(quoteCustomerAddressVO.getCountry());
			options.add(quoteCustomerAddressVO.getPhone());
			options.add(quoteCustomerAddressVO.getPhoneExt());
			options.add(quoteCustomerAddressVO.getRegion());
			options.add(quoteCustomerAddressVO.getState());
			options.add(quoteCustomerAddressVO.getStreet());
			options.add(quoteCustomerAddressVO.getStreet2());
			options.add(quoteCustomerAddressVO.getStreet3());
			options.add(quoteCustomerAddressVO.getPostalCode());
			options.add(String.valueOf(quoteCustomerAddressVO.getDeleteInd()));
			options.add(ReplicationUtility.dateString(quoteCustomerAddressVO.getCreatedTS()));
			options.add(quoteCustomerAddressVO.getCreatedBy());
			options.add(ReplicationUtility.dateString(quoteCustomerAddressVO.getLastModifiedTS()));
			options.add(quoteCustomerAddressVO.getLastModifiedBy());

			//for where condition
			options.add(String.valueOf(slsQtnID));
			options.add(slsQtnVrsn);
			options.add(quoteCustomerAddressVO.getCustomerType());

			int rowUpdated = executeUpdatePreparedStatement(con,updateQuery,options);
			if(rowUpdated == 0){
				Logger.repDebug(logInfo, "updateQuoteCustomerAddress", "Quote customer address doesn't exist for SLS_QTN_ID =  "+slsQtnID+ " and SLS_QTN_VRSN = "
						+slsQtnVrsn+" .Inserting record now.");
				insertQuoteCustomerAddress(quoteCustomerAddressVO);
			}
		}
		catch (Exception ex) {
			Logger.repError(logInfo, "updateQuoteCustomerA+ddress", "Exception while updating quote customer address details...", ex);
			throw new ReplicationException(ex.getMessage(),ex);
		}
		
		Logger.repDebug(logInfo, "updateQuoteCustomerAddress", "End of updating quote customer address details for quotation id "+ slsQtnID+" and sls version "+slsQtnVrsn);

	}
	
	private void removeDeletedQuoteCustAddress(Connection con, long slsQtnID, String slsQtnVrsn) throws ReplicationException {
		String query = null;
		try {
			query = queryRepository.getQuery("Quote","Delete","DeleteQuoteCustAddress"); 
			List<Object> options = new ArrayList<Object>();
			options.add(String.valueOf(slsQtnID));
			options.add(slsQtnVrsn);
			
			executeUpdatePreparedStatement(con,query,options);
		}
		catch (Exception ex) {
			Logger.repError(logInfo, "removeDeletedQuoteCustAddress", "Exception while removing DeletedQuoteCustAddress...", ex);
			throw new ReplicationException(ex.getMessage(),ex);
		}
				
		Logger.repDebug(logInfo, "removeDeletedQuoteCustAddress", "End of removing DeletedQuoteCustAddress for quotation id "+ slsQtnID + " and version "+slsQtnVrsn);
		
	}

	private void updateQuoteCustAddressDeleteFlag(Connection con, long slsQtnID,
			String slsQtnVrsn) throws ReplicationException {
		String query = null;
				
		try {
			query = queryRepository.getQuery("Quote","Update","QuoteCustAddressDeleteFlag"); 
			List<Object> options = new ArrayList<Object>();
			options.add(String.valueOf(slsQtnID));
			options.add(slsQtnVrsn);
			
			executeUpdatePreparedStatement(con,query,options);
		}
		catch (Exception ex) {
			Logger.repError(logInfo, "updateQuoteCustAddressDeleteFlag", "Exception while updating QuoteCustAddressDeleteFlag...", ex);
			throw new ReplicationException(ex.getMessage(),ex);
		}
			
		Logger.repDebug(logInfo, "updateQuoteCustAddressDeleteFlag", "End of updating QuoteCustAddressDeleteFlag for quotation id "+ slsQtnID +" and version "+slsQtnVrsn);
		
	}

}
