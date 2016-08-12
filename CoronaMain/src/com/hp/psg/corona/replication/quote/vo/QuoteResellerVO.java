package com.hp.psg.corona.replication.quote.vo;

import java.io.Serializable;
import java.util.Date;

public class QuoteResellerVO implements Serializable {

	/**
	 * Value object to hold QUOTE_RESELLER table data
	 */
	private static final long serialVersionUID = 1L;
	private Long slsQtnId;
	private String slsQtnVrsnSqnNr;
	private String rslrType;
	private String custName;
	private String rslrCityNm;
	private String rslrStProvCd;
	private String rslrCntryCd;
	private Long outletId;
	private String rslrSearchKey;
	private String ptnrCmpnyId;
	private char deleteInd;
	private Date createdTs;
	private String createdBy;
	private Date lastModifiedTs;
	private String lastModifiedBy;
	private Long rslrID;
	
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
	public String getRslrType() {
		return rslrType;
	}
	public void setRslrType(String rslrType) {
		this.rslrType = rslrType;
	}
	public String getCustName() {
		return custName;
	}
	public void setCustName(String custName) {
		this.custName = custName;
	}
	public String getRslrCityNm() {
		return rslrCityNm;
	}
	public void setRslrCityNm(String rslrCityNm) {
		this.rslrCityNm = rslrCityNm;
	}
	public String getRslrStProvCd() {
		return rslrStProvCd;
	}
	public void setRslrStProvCd(String rslrStProvCd) {
		this.rslrStProvCd = rslrStProvCd;
	}
	public String getRslrCntryCd() {
		return rslrCntryCd;
	}
	public void setRslrCntryCd(String rslrCntryCd) {
		this.rslrCntryCd = rslrCntryCd;
	}
	public Long getOutletId() {
		return outletId;
	}
	public void setOutletId(Long outletId) {
		this.outletId = outletId;
	}
	public String getRslrSearchKey() {
		return rslrSearchKey;
	}
	public void setRslrSearchKey(String rslrSearchKey) {
		this.rslrSearchKey = rslrSearchKey;
	}
	public String getPtnrCmpnyId() {
		return ptnrCmpnyId;
	}
	public void setPtnrCmpnyId(String ptnrCmpnyId) {
		this.ptnrCmpnyId = ptnrCmpnyId;
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
	public Long getRslrID() {
		return rslrID;
	}
	public void setRslrID(Long rslrID) {
		this.rslrID = rslrID;
	}
}
