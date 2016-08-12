package com.hp.psg.corona.replication.quote.vo;

import java.io.Serializable;
import java.util.Date;

public class QuoteBundleVO implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String slsQtnItmSqnNr;
	private Long  slsQtnId;
	private String slsQtnVrsnSqnNr;
	private Integer scaleId;
	private Long itemNr;
	private String productBaseNr;
	private String prodOptCd;
	private Long qty;
	private String productDescription;
	private Double listPrice;
	private Double authBdNetPrcAm;
	private Double authIncrDiscAm;
	private Long versionCreated;
	private String assocPl;
	private Double discAsPct;
	private String discTypeCd;
	private Double authDiscOtPc;
	private String bandedFl;
	private char deleteInd;
	private Date createdTs;
	private String createdBy;
	private Date lastModifiedTs;
	private String lastModifiedBy;
	
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
	public Long getItemNr() {
		return itemNr;
	}
	public void setItemNr(Long itemNr) {
		this.itemNr = itemNr;
	}
	public String getProductBaseNr() {
		return productBaseNr;
	}
	public void setProductBaseNr(String productBaseNr) {
		this.productBaseNr = productBaseNr;
	}
	public String getProdOptCd() {
		return prodOptCd;
	}
	public void setProdOptCd(String prodOptCd) {
		this.prodOptCd = prodOptCd;
	}
	public Long getQty() {
		return qty;
	}
	public void setQty(Long qty) {
		this.qty = qty;
	}
	public String getProductDescription() {
		return productDescription;
	}
	public void setProductDescription(String productDescription) {
		this.productDescription = productDescription;
	}
	public Double getListPrice() {
		return listPrice;
	}
	public void setListPrice(Double listPrice) {
		this.listPrice = listPrice;
	}
	public Double getAuthBdNetPrcAm() {
		return authBdNetPrcAm;
	}
	public void setAuthBdNetPrcAm(Double authBdNetPrcAm) {
		this.authBdNetPrcAm = authBdNetPrcAm;
	}
	public Double getAuthIncrDiscAm() {
		return authIncrDiscAm;
	}
	public void setAuthIncrDiscAm(Double authIncrDiscAm) {
		this.authIncrDiscAm = authIncrDiscAm;
	}
	public Long getVersionCreated() {
		return versionCreated;
	}
	public void setVersionCreated(Long versionCreated) {
		this.versionCreated = versionCreated;
	}
	public String getAssocPl() {
		return assocPl;
	}
	public void setAssocPl(String assocPl) {
		this.assocPl = assocPl;
	}
	public Double getDiscAsPct() {
		return discAsPct;
	}
	public void setDiscAsPct(Double discAsPct) {
		this.discAsPct = discAsPct;
	}
	public String getDiscTypeCd() {
		return discTypeCd;
	}
	public void setDiscTypeCd(String discTypeCd) {
		this.discTypeCd = discTypeCd;
	}
	public Double getAuthDiscOtPc() {
		return authDiscOtPc;
	}
	public void setAuthDiscOtPc(Double authDiscOtPc) {
		this.authDiscOtPc = authDiscOtPc;
	}
	public String getBandedFl() {
		return bandedFl;
	}
	public void setBandedFl(String bandedFl) {
		this.bandedFl = bandedFl;
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
