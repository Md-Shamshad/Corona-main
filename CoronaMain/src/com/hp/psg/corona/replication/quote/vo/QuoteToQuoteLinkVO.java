package com.hp.psg.corona.replication.quote.vo;

import java.io.Serializable;
import java.util.Date;

public class QuoteToQuoteLinkVO implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Long slsQtnId;
	private String slsQtnVrsnSqnNr;
	private String qtnSource;
	private Long lnkSlsQtnId;
	private String lnkSlsQtnVrsnSqnNr;
	private String lnkQtnSource;
	private char updateInd;
	private char deleteInd;
	private Date createdDate;
	private String createdBy;
	private Date lastModifiedDate;
	private String lastModifiedBy;
	
	public Long getSlsQtnId() {
		return slsQtnId;
	}
	public void setSlsQtnId(Long slsQtnId) {
		this.slsQtnId = slsQtnId;
	}
	public String getSlsQtnVrsnSqnNr() {
		return slsQtnVrsnSqnNr;
	}
	public void setSlsQtnVrsnSqnNr(String slsQtnVrsnSqnNr) {
		this.slsQtnVrsnSqnNr = slsQtnVrsnSqnNr;
	}
	public String getQtnSource() {
		return qtnSource;
	}
	public void setQtnSource(String qtnSource) {
		this.qtnSource = qtnSource;
	}
	public Long getLnkSlsQtnId() {
		return lnkSlsQtnId;
	}
	public void setLnkSlsQtnId(Long lnkSlsQtnId) {
		this.lnkSlsQtnId = lnkSlsQtnId;
	}
	public String getLnkSlsQtnVrsnSqnNr() {
		return lnkSlsQtnVrsnSqnNr;
	}
	public void setLnkSlsQtnVrsnSqnNr(String lnkSlsQtnVrsnSqnNr) {
		this.lnkSlsQtnVrsnSqnNr = lnkSlsQtnVrsnSqnNr;
	}
	public String getLnkQtnSource() {
		return lnkQtnSource;
	}
	public void setLnkQtnSource(String lnkQtnSource) {
		this.lnkQtnSource = lnkQtnSource;
	}
	public char getUpdateInd() {
		return updateInd;
	}
	public void setUpdateInd(char updateInd) {
		this.updateInd = updateInd;
	}
	public char getDeleteInd() {
		return deleteInd;
	}
	public void setDeleteInd(char deleteInd) {
		this.deleteInd = deleteInd;
	}
	public Date getCreatedDate() {
		return createdDate;
	}
	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}
	public String getCreatedBy() {
		return createdBy;
	}
	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}
	public Date getLastModifiedDate() {
		return lastModifiedDate;
	}
	public void setLastModifiedDate(Date lastModifiedDate) {
		this.lastModifiedDate = lastModifiedDate;
	}
	public String getLastModifiedBy() {
		return lastModifiedBy;
	}
	public void setLastModifiedBy(String lastModifiedBy) {
		this.lastModifiedBy = lastModifiedBy;
	}
	

}
