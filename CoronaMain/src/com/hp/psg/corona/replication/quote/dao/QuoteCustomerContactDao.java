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
import com.hp.psg.corona.replication.quote.vo.QuoteCustomerContactVO;

public class QuoteCustomerContactDao extends QuoteConnection implements IBaseDao {
	QueryRepository queryRepository = null;
	String dataSourceName = null;
	LoggerInfo logInfo=null;

	public QuoteCustomerContactDao() {
		queryRepository = ReplicationQueryManager.getInstance();
		dataSourceName = Config.getProperty("corona.replication.secondary.data.source.name");
		logInfo = new LoggerInfo (this.getClass().getName());
	}

	@Override
	public void create(Connection con, long slsQtnID, String slsQtnVrsn)
	throws ReplicationException {
		List<QuoteCustomerContactVO> quoteCustomerContacts = getQuoteCustomerContacts(con,slsQtnID,slsQtnVrsn);
		if(quoteCustomerContacts!=null && quoteCustomerContacts.size()>0){
			for(QuoteCustomerContactVO quoteCustomerContactVO : quoteCustomerContacts){
				insertQuoteCustomerContact(quoteCustomerContactVO);
			}
		}
	}

	@Override
	public void update(Connection con, long slsQtnID, String slsQtnVrsn)
	throws ReplicationException {
		List<QuoteCustomerContactVO> quoteCustomerContacts = getQuoteCustomerContacts(con,slsQtnID,slsQtnVrsn);
		if(quoteCustomerContacts!=null && quoteCustomerContacts.size()>0){
			for(QuoteCustomerContactVO quoteCustomerContactVO : quoteCustomerContacts){
				updateQuoteCustomerContact(slsQtnID, slsQtnVrsn, quoteCustomerContactVO);
			}
		}
	}


	@Override
	public void delete(Connection con, long slsQtnID, String slsQtnVrsn)
	throws ReplicationException {
		List<QuoteCustomerContactVO> quoteCustomerContacts = getQuoteCustomerContacts(con,slsQtnID,slsQtnVrsn);
		if(quoteCustomerContacts!=null && quoteCustomerContacts.size()>0){
			for(QuoteCustomerContactVO quoteCustomerContactVO : quoteCustomerContacts){
				deleteQuoteCustomerContact(slsQtnID, slsQtnVrsn, quoteCustomerContactVO);
			}
		}
	}


	@Override
	public void change(Connection con, long slsQtnID, String slsQtnVrsn)
	throws ReplicationException {
		Logger.repDebug(logInfo, "change", "Incorrectly called change for QUOTE_CUSTOMER_CONTACT TABLE");
	}

	private void insertQuoteCustomerContact(
			QuoteCustomerContactVO quoteCustomerContactVO) throws ReplicationException {
		Logger.repDebug(logInfo, "insertQuoteCustomerContact", "Begining of inserting quote customer contact details...");

		String insertQuery = null;
		Connection con = null;

		try {
			con = getConnection(dataSourceName);

			insertQuery = queryRepository.getQuery("Quote","Create","QuoteCustomerContact"); 
			List<Object> options = new ArrayList<Object>();

			options.add(String.valueOf(quoteCustomerContactVO.getSlsQtnId()));
			options.add(quoteCustomerContactVO.getSlsQtnVrsnSqnNr());
			options.add(quoteCustomerContactVO.getCustomerType());
			options.add(quoteCustomerContactVO.getDepartment());
			options.add(quoteCustomerContactVO.getEmail());
			options.add(quoteCustomerContactVO.getEmail2());
			options.add(quoteCustomerContactVO.getFax());
			options.add(quoteCustomerContactVO.getFaxExt());
			options.add(quoteCustomerContactVO.getFirstName());
			options.add(quoteCustomerContactVO.getMiddleName());
			options.add(quoteCustomerContactVO.getLastName());
			options.add(quoteCustomerContactVO.getPhone());
			options.add(quoteCustomerContactVO.getPhoneExt());
			options.add(quoteCustomerContactVO.getPhone2());
			options.add(quoteCustomerContactVO.getPhone2Ext());
			options.add(quoteCustomerContactVO.getTitle());
			options.add(String.valueOf(quoteCustomerContactVO.getDeleteInd()));
			options.add(ReplicationUtility.dateString(quoteCustomerContactVO.getCreatedTs()));
			options.add(quoteCustomerContactVO.getCreatedBy());
			options.add(ReplicationUtility.dateString(quoteCustomerContactVO.getLastModifiedTs()));
			options.add(quoteCustomerContactVO.getLastModifiedBy());

			executeUpdatePreparedStatement(con,insertQuery,options);
		}
		catch (Exception ex) {
			Logger.repError(logInfo, "insertQuoteCustomerContact", "Exception while inserting quote customer contact details...", ex);
			throw new ReplicationException(ex.getMessage(),ex);
		}
		finally{
			closeConnection(con);
		}
		Logger.repDebug(logInfo, "insertQuoteCustomerContact", "End of inserting quote customer contact details.");
	}

	private List<QuoteCustomerContactVO> getQuoteCustomerContacts(
			Connection con, long slsQtnID, String slsQtnVrsn) throws ReplicationException {
		Logger.repDebug(logInfo, "getQuoteCustomerContacts", "Begining of getting quote customer contact details for quotation id "+ slsQtnID+
				" and sls version "+slsQtnVrsn);
		List<QuoteCustomerContactVO> quoteCustomerContacts = new ArrayList<QuoteCustomerContactVO>();
		QuoteCustomerContactVO quoteCustomerContactVO = null;
		String selectQuery = null;

		try {
			selectQuery = queryRepository.getQuery("Quote","Select","QuoteCustomerContact"); 
			List<Object> options = new ArrayList<Object>();
			options.add(String.valueOf(slsQtnID));
			options.add(slsQtnVrsn);

			ResultSet resultSet = runPreparedStatement(con, selectQuery, options);
			while (resultSet.next()){
				quoteCustomerContactVO = createVO(resultSet);
				quoteCustomerContacts.add(quoteCustomerContactVO);
			}
		}catch(SQLException sqlEx){
			Logger.repError(logInfo, "getQuoteCustomerContacts", "Exception while getting quote customer contact details...", sqlEx);
			throw new ReplicationException(sqlEx.getMessage(), sqlEx);
		}catch (Exception e) {
			Logger.repError(logInfo, "getQuoteCustomerContacts", "Exception while getting quote customer contact details...", e);
			throw new ReplicationException(e.getMessage(), e);
		}

		Logger.repDebug(logInfo, "getQuoteCustomerContacts", "End of getting quote customer contact details for quotation id "+ slsQtnID+
				" and sls version "+slsQtnVrsn);
		return quoteCustomerContacts;
	}


	private QuoteCustomerContactVO createVO(ResultSet resultSet) throws ReplicationException {
		QuoteCustomerContactVO quoteCustomerContactVO = new QuoteCustomerContactVO();

		try{
			quoteCustomerContactVO.setCreatedBy(resultSet.getString("CREATED_BY"));
			quoteCustomerContactVO.setCreatedTs(resultSet.getTimestamp("CREATED_TS"));
			quoteCustomerContactVO.setCustomerType(resultSet.getString("CUSTOMER_TYPE"));
			quoteCustomerContactVO.setDeleteInd((resultSet.getString("DELETE_IND")).charAt(0));
			quoteCustomerContactVO.setDepartment(resultSet.getString("DEPARTMENT"));
			quoteCustomerContactVO.setEmail(resultSet.getString("EMAIL"));
			quoteCustomerContactVO.setEmail2(resultSet.getString("EMAIL2"));
			quoteCustomerContactVO.setFax(resultSet.getString("FAX"));
			quoteCustomerContactVO.setFaxExt(resultSet.getString("FAX_EXT"));
			quoteCustomerContactVO.setFirstName(resultSet.getString("FIRST_NAME"));
			quoteCustomerContactVO.setLastModifiedBy(resultSet.getString("LAST_MODIFIED_BY"));
			quoteCustomerContactVO.setLastModifiedTs(resultSet.getTimestamp("LAST_MODIFIED_TS"));
			quoteCustomerContactVO.setLastName(resultSet.getString("LAST_NAME"));
			quoteCustomerContactVO.setMiddleName(resultSet.getString("MIDDLE_NAME"));
			quoteCustomerContactVO.setPhone(resultSet.getString("PHONE"));
			quoteCustomerContactVO.setPhone2(resultSet.getString("PHONE2"));
			quoteCustomerContactVO.setPhone2Ext(resultSet.getString("PHONE2_EXT"));
			quoteCustomerContactVO.setPhoneExt(resultSet.getString("PHONE_EXT"));
			quoteCustomerContactVO.setSlsQtnId(resultSet.getLong("SLS_QTN_ID"));
			quoteCustomerContactVO.setSlsQtnVrsnSqnNr(resultSet.getString("SLS_QTN_VRSN_SQN_NR"));
			quoteCustomerContactVO.setTitle(resultSet.getString("TITLE"));
			
		}catch(SQLException sqlEx){
			Logger.repError(logInfo, "createVO", "Exception while creating QuoteCustomerContactVO...", sqlEx);
			throw new ReplicationException(sqlEx.getMessage(), sqlEx);
		}
		return quoteCustomerContactVO;
	}

	private void updateQuoteCustomerContact(long slsQtnID, String slsQtnVrsn,
			QuoteCustomerContactVO quoteCustomerContactVO) throws ReplicationException {
		Logger.repDebug(logInfo, "updateQuoteCustomerContact", "Begining of updating quote customer contact details for quotation id "+ slsQtnID+
				" and sls version "+slsQtnVrsn);
		String updateQuery = null;
		Connection con = null;

		try {
			con = getConnection(dataSourceName);

			updateQuery = queryRepository.getQuery("Quote","Update","QuoteCustomerContact"); 
			List<Object> options = new ArrayList<Object>();

			options.add(quoteCustomerContactVO.getDepartment());
			options.add(quoteCustomerContactVO.getEmail());
			options.add(quoteCustomerContactVO.getEmail2());
			options.add(quoteCustomerContactVO.getFax());
			options.add(quoteCustomerContactVO.getFaxExt());
			options.add(quoteCustomerContactVO.getFirstName());
			options.add(quoteCustomerContactVO.getMiddleName());
			options.add(quoteCustomerContactVO.getLastName());
			options.add(quoteCustomerContactVO.getPhone());
			options.add(quoteCustomerContactVO.getPhoneExt());
			options.add(quoteCustomerContactVO.getPhone2());
			options.add(quoteCustomerContactVO.getPhone2Ext());
			options.add(quoteCustomerContactVO.getTitle());
			options.add(String.valueOf(quoteCustomerContactVO.getDeleteInd()));
			options.add(ReplicationUtility.dateString(quoteCustomerContactVO.getCreatedTs()));
			options.add(quoteCustomerContactVO.getCreatedBy());
			options.add(ReplicationUtility.dateString(quoteCustomerContactVO.getLastModifiedTs()));
			options.add(quoteCustomerContactVO.getLastModifiedBy());

			//for where condition
			options.add(String.valueOf(slsQtnID));
			options.add(slsQtnVrsn);
			options.add(quoteCustomerContactVO.getCustomerType());

			int rowUpdated = executeUpdatePreparedStatement(con,updateQuery,options);
			if(rowUpdated == 0){
				Logger.repDebug(logInfo, "updateQuoteCustomerContact", "Quote customer contact doesn't exist for SLS_QTN_ID =  "+slsQtnID+ " and SLS_QTN_VRSN = "
						+slsQtnVrsn+" .Inserting record now.");
				insertQuoteCustomerContact(quoteCustomerContactVO);
			}
		}
		catch (Exception ex) {
			Logger.repError(logInfo, "updateQuoteCustomerContact", "Exception while updating quote customer contact details...", ex);
			throw new ReplicationException(ex.getMessage(),ex);
		}
		finally{
			closeConnection(con);
		}
		Logger.repDebug(logInfo, "updateQuoteCustomerContact", "End of updating quote customer contact details for quotation id "+ slsQtnID+
				" and sls version "+slsQtnVrsn);

	}

	private void deleteQuoteCustomerContact(long slsQtnID, String slsQtnVrsn,
			QuoteCustomerContactVO quoteCustomerContactVO) throws ReplicationException {
		updateQuoteCustomerContact(slsQtnID, slsQtnVrsn, quoteCustomerContactVO);

	}
}
