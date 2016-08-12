package com.hp.psg.corona.replication.quote.vo;

import java.io.Serializable;
import java.util.Date;


/**
 * QuoteVO class used as a Helper class to hold all the required inputs
 * of the Quote operations.
 * 
 * @author rohitc
 *
 */
public class QuoteVO implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private long slsQtnID ;         
	private String creationPersonID  ;
	private char deleteInd;         
	private Date createTimestamp;          
	private String createdBy;          
	private Date lastModifiedTimestamp;    
	private String lastModifiedBy;
	
	public long getSlsQtnID() {
		return slsQtnID;
	}
	public void setSlsQtnID(long slsQtnID) {
		this.slsQtnID = slsQtnID;
	}
	public String getCreationPersonID() {
		return creationPersonID;
	}
	public void setCreationPersonID(String creationPersonID) {
		this.creationPersonID = creationPersonID;
	}
	public char getDeleteInd() {
		return deleteInd;
	}
	public void setDeleteInd(char deleteInd) {
		this.deleteInd = deleteInd;
	}
	public Date getCreateTimestamp() {
		return createTimestamp;
	}
	public void setCreateTimestamp(Date createTimestamp) {
		this.createTimestamp = createTimestamp;
	}
	public String getCreatedBy() {
		return createdBy;
	}
	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}
	public Date getLastModifiedTimestamp() {
		return lastModifiedTimestamp;
	}
	public void setLastModifiedTimestamp(Date lastModifiedTimestamp) {
		this.lastModifiedTimestamp = lastModifiedTimestamp;
	}
	public String getLastModifiedBy() {
		return lastModifiedBy;
	}
	public void setLastModifiedBy(String lastModifiedBy) {
		this.lastModifiedBy = lastModifiedBy;
	}
}
