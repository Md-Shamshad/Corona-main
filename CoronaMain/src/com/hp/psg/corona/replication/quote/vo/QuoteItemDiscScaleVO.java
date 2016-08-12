package com.hp.psg.corona.replication.quote.vo;

import java.io.Serializable;
import java.util.Date;

public class QuoteItemDiscScaleVO implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String slsQtnItmSqnNr;
	private Long slsQtnId;
	private String slsQtnVrsnSqnNr;
	private Integer scaleId;
	private String discTypeCd;
	private Double prodLstPrcAm;
	private Double authBdNetPrcAm;
	private Double authDiscOtPc;
	private Double highRslrASdPc;
	private Date upperBndryDt;
	private Date lowerBndryDt;
	private String authFl;
	private Double authFixDiscAm;
	private Double authIncrDiscAm;
	private char deleteInd;
	private Date createdTs;
	private String createdBy;
	private Date lastModifiedTs;
	private String lastModifiedBy;
	private Double extAuthBdNetPrcAm;
	
	public String getSlsQtnItmSqnNr() {
		return slsQtnItmSqnNr;
	}
	public void setSlsQtnItmSqnNr(String slsQtnItmSqnNr) {
		this.slsQtnItmSqnNr = slsQtnItmSqnNr;
	}
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
	public Integer getScaleId() {
		return scaleId;
	}
	public void setScaleId(Integer scaleId) {
		this.scaleId = scaleId;
	}
	public String getDiscTypeCd() {
		return discTypeCd;
	}
	public void setDiscTypeCd(String discTypeCd) {
		this.discTypeCd = discTypeCd;
	}
	public Double getProdLstPrcAm() {
		return prodLstPrcAm;
	}
	public void setProdLstPrcAm(Double prodLstPrcAm) {
		this.prodLstPrcAm = prodLstPrcAm;
	}
	public Double getAuthBdNetPrcAm() {
		return authBdNetPrcAm;
	}
	public void setAuthBdNetPrcAm(Double authBdNetPrcAm) {
		this.authBdNetPrcAm = authBdNetPrcAm;
	}
	public Double getAuthDiscOtPc() {
		return authDiscOtPc;
	}
	public void setAuthDiscOtPc(Double authDiscOtPc) {
		this.authDiscOtPc = authDiscOtPc;
	}
	public Double getHighRslrASdPc() {
		return highRslrASdPc;
	}
	public void setHighRslrASdPc(Double highRslrASdPc) {
		this.highRslrASdPc = highRslrASdPc;
	}
	public Date getUpperBndryDt() {
		return upperBndryDt;
	}
	public void setUpperBndryDt(Date upperBndryDt) {
		this.upperBndryDt = upperBndryDt;
	}
	public Date getLowerBndryDt() {
		return lowerBndryDt;
	}
	public void setLowerBndryDt(Date lowerBndryDt) {
		this.lowerBndryDt = lowerBndryDt;
	}
	public String getAuthFl() {
		return authFl;
	}
	public void setAuthFl(String authFl) {
		this.authFl = authFl;
	}
	public Double getAuthFixDiscAm() {
		return authFixDiscAm;
	}
	public void setAuthFixDiscAm(Double authFixDiscAm) {
		this.authFixDiscAm = authFixDiscAm;
	}
	public Double getAuthIncrDiscAm() {
		return authIncrDiscAm;
	}
	public void setAuthIncrDiscAm(Double authIncrDiscAm) {
		this.authIncrDiscAm = authIncrDiscAm;
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
	public Double getExtAuthBdNetPrcAm() {
		return extAuthBdNetPrcAm;
	}
	public void setExtAuthBdNetPrcAm(Double extAuthBdNetPrcAm) {
		this.extAuthBdNetPrcAm = extAuthBdNetPrcAm;
	}
	
	
}
