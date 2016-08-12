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
import com.hp.psg.corona.replication.quote.vo.QuoteContactVO;

public class QuoteContactDao extends QuoteConnection implements IBaseDao {
	QueryRepository queryRepository = null;
	String dataSourceName = null;
	LoggerInfo logInfo=null;


	public QuoteContactDao() {
		queryRepository = ReplicationQueryManager.getInstance();
		dataSourceName = Config.getProperty("corona.replication.secondary.data.source.name");
		logInfo = new LoggerInfo (this.getClass().getName());
	}

	@Override
	public void create(Connection con, long slsQtnID, String slsQtnVrsn)
	throws ReplicationException {
		List<QuoteContactVO> quoteContacts = getQuoteContacts(con,slsQtnID,slsQtnVrsn);
		if(quoteContacts!=null && quoteContacts.size()>0){
			for(QuoteContactVO quoteContactVO : quoteContacts){
				insertQuoteContact(quoteContactVO);
			}
		}
	}

	@Override
	public void update(Connection con, long slsQtnID, String slsQtnVrsn)
	throws ReplicationException {
		
		Connection dstCon = null;		
		try {
			dstCon = getConnection(dataSourceName);
			
			updateQuoteContactsDeleteFlag(dstCon, slsQtnID, slsQtnVrsn);
		
			List<QuoteContactVO> quoteContacts = getQuoteContacts(con,slsQtnID,slsQtnVrsn);
			if(quoteContacts!=null && quoteContacts.size()>0){
				for(QuoteContactVO quoteContactVO : quoteContacts){
					updateQuoteContact(dstCon, slsQtnID, slsQtnVrsn, quoteContactVO);
				}
			}
		
			removeDeletedQuoteContacts(dstCon, slsQtnID, slsQtnVrsn);
		}
		catch (Exception ex) {
			Logger.repError(logInfo, "update", "Exception while updating QuoteContact details...", ex);
			throw new ReplicationException(ex.getMessage(),ex);
		}
		finally{
			closeConnection(dstCon);
		}	
	}

	@Override
	public void delete(Connection con, long slsQtnID, String slsQtnVrsn)
	throws ReplicationException {
		List<QuoteContactVO> quoteContacts = getQuoteContacts(con,slsQtnID,slsQtnVrsn);
		if(quoteContacts!=null && quoteContacts.size()>0){
			Connection dstCon = null;		
			try {
				dstCon = getConnection(dataSourceName);
				
				for(QuoteContactVO quoteContactVO : quoteContacts){
					deleteQuoteContact(dstCon, slsQtnID, slsQtnVrsn,quoteContactVO);
				}
			}
			catch (Exception ex) {
				Logger.repError(logInfo, "delete", "Exception while deleting QuoteContact details...", ex);
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
		Logger.repDebug(logInfo, "change", "Incorrectly called change for QUOTE_CONTACT TABLE");
	}

	private List<QuoteContactVO> getQuoteContacts(Connection con,
			long slsQtnID, String slsQtnVrsn) throws ReplicationException {
		Logger.repDebug(logInfo, "getQuoteContacts", "Begining of getting quote contact details for quotation id "+ slsQtnID+
				" and sls version "+slsQtnVrsn);
		List<QuoteContactVO> quoteContacts = new ArrayList<QuoteContactVO>();
		QuoteContactVO quoteContactVO = null;
		String selectQuery = null;

		try {
			selectQuery = queryRepository.getQuery("Quote","Select","QuoteContact"); 
			List<Object> options = new ArrayList<Object>();
			options.add(String.valueOf(slsQtnID));
			options.add(slsQtnVrsn);

			ResultSet resultSet = runPreparedStatement(con, selectQuery, options);
			while (resultSet.next()){
				quoteContactVO = createVO(resultSet);
				quoteContacts.add(quoteContactVO);
			}
		}catch(SQLException sqlEx){
			Logger.repError(logInfo, "getQuoteContacts", "Exception while getting quote contact details...", sqlEx);
			throw new ReplicationException(sqlEx.getMessage(), sqlEx);
		}catch (Exception e) {
			Logger.repError(logInfo, "getQuoteContacts", "Exception while getting quote contact details...", e);
			throw new ReplicationException(e.getMessage(), e);
		}

		Logger.repDebug(logInfo, "getQuoteContacts", "End of getting quote contact details for quotation id "+ slsQtnID+
				" and sls version "+slsQtnVrsn);
		return quoteContacts;
	}

	private QuoteContactVO createVO(ResultSet resultSet) throws ReplicationException {
		QuoteContactVO quoteContactVO = new QuoteContactVO();
		try{
			quoteContactVO.setContactType(resultSet.getString("CONTACT_TYPE"));
			quoteContactVO.setCreatedBy(resultSet.getString("CREATED_BY"));
			quoteContactVO.setCreatedTS(resultSet.getTimestamp("CREATED_TS"));
			quoteContactVO.setDeleteInd((resultSet.getString("DELETE_IND")).charAt(0));
			quoteContactVO.setDepartment(resultSet.getString("DEPARTMENT"));
			quoteContactVO.setEmail(resultSet.getString("EMAIL"));
			quoteContactVO.setEmail2(resultSet.getString("EMAIL2"));
			quoteContactVO.setEmployeeNR(resultSet.getString("EMPLOYEE_NR"));
			quoteContactVO.setFax(resultSet.getString("FAX"));
			quoteContactVO.setFaxExt(resultSet.getString("FAX_EXT"));
			quoteContactVO.setFirstName(resultSet.getString("FIRST_NAME"));
			quoteContactVO.setLastModifiedBy(resultSet.getString("LAST_MODIFIED_BY"));
			quoteContactVO.setLastModifiedTS(resultSet.getTimestamp("LAST_MODIFIED_TS"));
			quoteContactVO.setLastName(resultSet.getString("LAST_NAME"));
			quoteContactVO.setMiddleName(resultSet.getString("MIDDLE_NAME"));
			quoteContactVO.setPhone(resultSet.getString("PHONE"));
			quoteContactVO.setPhone2(resultSet.getString("PHONE2"));
			quoteContactVO.setPhoneExt(resultSet.getString("PHONE_EXT"));
			quoteContactVO.setPhone2Ext(resultSet.getString("PHONE2_EXT"));
			quoteContactVO.setSlsQtnID(resultSet.getLong("SLS_QTN_ID"));
			quoteContactVO.setSlsQtnVrsnSqnNR(resultSet.getString("SLS_QTN_VRSN_SQN_NR"));
			quoteContactVO.setTitle(resultSet.getString("TITLE"));
		}catch(SQLException sqlEx){
			Logger.repError(logInfo, "createVO", "Exception while creating QuoteContactVO...", sqlEx);
			throw new ReplicationException(sqlEx.getMessage(), sqlEx);
		}

		return quoteContactVO;
	}

	private void insertQuoteContact(QuoteContactVO quoteContactVO) throws ReplicationException {
		Logger.repDebug(logInfo, "insertQuoteContact", "Begining of inserting quote contact details...");

		String insertQuery = null;
		Connection con = null;

		try {
			con = getConnection(dataSourceName);

			insertQuery = queryRepository.getQuery("Quote","Create","QuoteContact"); 
			List<Object> options = new ArrayList<Object>();

			options.add(String.valueOf(quoteContactVO.getSlsQtnID()));
			options.add(quoteContactVO.getSlsQtnVrsnSqnNR());
			options.add(quoteContactVO.getDepartment());
			options.add(quoteContactVO.getEmail());
			options.add(quoteContactVO.getEmail2());
			options.add(quoteContactVO.getFax());
			options.add(quoteContactVO.getFaxExt());
			options.add(quoteContactVO.getFirstName());
			options.add(quoteContactVO.getMiddleName());
			options.add(quoteContactVO.getLastName());
			options.add(quoteContactVO.getEmployeeNR());
			options.add(quoteContactVO.getPhone());
			options.add(quoteContactVO.getPhoneExt());
			options.add(quoteContactVO.getPhone2());
			options.add(quoteContactVO.getPhone2Ext());
			options.add(quoteContactVO.getTitle());
			options.add(quoteContactVO.getContactType());
			options.add(String.valueOf(quoteContactVO.getDeleteInd()));
			options.add(ReplicationUtility.dateString(quoteContactVO.getCreatedTS()));
			options.add(quoteContactVO.getCreatedBy());
			options.add(ReplicationUtility.dateString(quoteContactVO.getLastModifiedTS()));
			options.add(quoteContactVO.getLastModifiedBy());

			executeUpdatePreparedStatement(con,insertQuery,options);
		}
		catch (Exception ex) {
			Logger.repError(logInfo, "insertQuoteContact", "Exception while inserting quote contact details...", ex);
			throw new ReplicationException(ex.getMessage(),ex);
		}
		finally{
			closeConnection(con);
		}
		Logger.repDebug(logInfo, "insertQuoteContact", "End of inserting quote contact details.");

	}

	private void deleteQuoteContact(Connection con, long slsQtnID, String slsQtnVrsn, QuoteContactVO quoteContactVO) throws ReplicationException {

		updateQuoteContact(con, slsQtnID, slsQtnVrsn, quoteContactVO);
	}

	private void updateQuoteContact(Connection con, long slsQtnID, String slsQtnVrsn, QuoteContactVO quoteContactVO) throws ReplicationException {
		Logger.repDebug(logInfo, "updateQuoteContact", "Begining of updating quote contact details for quotation id "+ slsQtnID+
				" and sls version "+slsQtnVrsn);
		String updateQuery = null;
		try {
			updateQuery = queryRepository.getQuery("Quote","Update","QuoteContact"); 
			List<Object> options = new ArrayList<Object>();

			options.add(quoteContactVO.getDepartment());
			options.add(quoteContactVO.getEmail());
			options.add(quoteContactVO.getEmail2());
			options.add(quoteContactVO.getFax());
			options.add(quoteContactVO.getFaxExt());
			options.add(quoteContactVO.getFirstName());
			options.add(quoteContactVO.getMiddleName());
			options.add(quoteContactVO.getLastName());
			options.add(quoteContactVO.getEmployeeNR());
			options.add(quoteContactVO.getPhone());
			options.add(quoteContactVO.getPhoneExt());
			options.add(quoteContactVO.getPhone2());
			options.add(quoteContactVO.getPhone2Ext());
			options.add(quoteContactVO.getTitle());
			options.add(String.valueOf(quoteContactVO.getDeleteInd()));
			options.add(ReplicationUtility.dateString(quoteContactVO.getCreatedTS()));
			options.add(quoteContactVO.getCreatedBy());
			options.add(ReplicationUtility.dateString(quoteContactVO.getLastModifiedTS()));
			options.add(quoteContactVO.getLastModifiedBy());
			
			//for where conditions
			options.add(String.valueOf(slsQtnID));
			options.add(slsQtnVrsn);
			options.add(quoteContactVO.getContactType());

			int rowUpdated = executeUpdatePreparedStatement(con,updateQuery,options);
			if(rowUpdated == 0){
				Logger.repDebug(logInfo, "updateQuoteContact", "Quote contact doesn't exist for SLS_QTN_ID =  "+slsQtnID+ " and SLS_QTN_VRSN = "
						+slsQtnVrsn+" .Inserting record now.");
				insertQuoteContact(quoteContactVO);
			}
		}
		catch (Exception ex) {
			Logger.repError(logInfo, "updateQuoteContact", "Exception while updating quote contact details...", ex);
			throw new ReplicationException(ex.getMessage(),ex);
		}
		
		Logger.repDebug(logInfo, "updateQuoteContact", "End of updating quote contact details for quotation id "+ slsQtnID+ " and sls version "+slsQtnVrsn);
	}
	
	private void removeDeletedQuoteContacts(Connection con, long slsQtnID, String slsQtnVrsn) throws ReplicationException {
		String query = null;
		
		try {
			query = queryRepository.getQuery("Quote","Delete","DeleteQuoteContacts"); 
			List<Object> options = new ArrayList<Object>();
			options.add(String.valueOf(slsQtnID));
			options.add(slsQtnVrsn);
			
			executeUpdatePreparedStatement(con,query,options);
		}
		catch (Exception ex) {
			Logger.repError(logInfo, "removeDeletedQuoteContacts", "Exception while removeDeletedQuoteContacts...", ex);
			throw new ReplicationException(ex.getMessage(),ex);
		}
				
		Logger.repDebug(logInfo, "removeDeletedQuoteContacts", "End of removeDeletedQuoteContacts for quotation id "+ slsQtnID + " and version "+slsQtnVrsn);
		
	}

	private void updateQuoteContactsDeleteFlag(Connection con, long slsQtnID, String slsQtnVrsn) throws ReplicationException {
		String query = null;
		
		try {				
			query = queryRepository.getQuery("Quote","Update","QuoteContactDeleteFlag"); 
			List<Object> options = new ArrayList<Object>();
			options.add(String.valueOf(slsQtnID));
			options.add(slsQtnVrsn);
			
			executeUpdatePreparedStatement(con,query,options);
		}
		catch (Exception ex) {
			Logger.repError(logInfo, "updateQuoteContactsDeleteFlag", "Exception while updating QuoteContactDeleteFlag...", ex);
			throw new ReplicationException(ex.getMessage(),ex);
		}
			
		Logger.repDebug(logInfo, "updateQuoteContactsDeleteFlag", "End of updating QuoteContactDeleteFlag for quotation id "+ slsQtnID +" and version "	+slsQtnVrsn);
		
	}

}
