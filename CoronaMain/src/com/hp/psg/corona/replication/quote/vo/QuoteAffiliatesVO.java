package com.hp.psg.corona.replication.quote.vo;

import java.io.Serializable;
import java.util.Date;

public class QuoteAffiliatesVO implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Long  slsQtnId;
	private String slsQtnVrsnSqnNr;
	private Long euAuthAffilNr;
	private String euAuthAffilNm;
	private String euAuthCityNm;
	private String euAuthStProvCd;
	private char deleteInd;
	private Date createdTs;
	private String createdBy;
	private Date lastModifiedTs;
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
	public Long getEuAuthAffilNr() {
		return euAuthAffilNr;
	}
	public void setEuAuthAffilNr(Long euAuthAffilNr) {
		this.euAuthAffilNr = euAuthAffilNr;
	}
	public String getEuAuthAffilNm() {
		return euAuthAffilNm;
	}
	public void setEuAuthAffilNm(String euAuthAffilNm) {
		this.euAuthAffilNm = euAuthAffilNm;
	}
	public String getEuAuthCityNm() {
		return euAuthCityNm;
	}
	public void setEuAuthCityNm(String euAuthCityNm) {
		this.euAuthCityNm = euAuthCityNm;
	}
	public String getEuAuthStProvCd() {
		return euAuthStProvCd;
	}
	public void setEuAuthStProvCd(String euAuthStProvCd) {
		this.euAuthStProvCd = euAuthStProvCd;
	}
	public char getDeleteInd() {
		return deleteInd;
	}
	public void setDeleteInd(char deleteInd) {
		this.deleteInd = deleteInd;
	}
	public Date getCreatedTs() {
		return createdTs;
	}
	public void setCreatedTs(Date createdTs) {
		this.createdTs = createdTs;
	}
	public String getCreatedBy() {
		return createdBy;
	}
	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}
	public Date getLastModifiedTs() {
		return lastModifiedTs;
	}
	public void setLastModifiedTs(Date lastModifiedTs) {
		this.lastModifiedTs = lastModifiedTs;
	}
	public String getLastModifiedBy() {
		return lastModifiedBy;
	}
	public void setLastModifiedBy(String lastModifiedBy) {
		this.lastModifiedBy = lastModifiedBy;
	}
	
	
}
