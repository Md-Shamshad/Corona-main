package com.hp.psg.corona.common.cto.beans;

import java.io.Serializable;
import java.sql.Timestamp;


/**
 * @author dudeja
 * @version 1.0
 *
 */
public class ConfigPriceHeaderInfo extends CoronaBaseObject
		implements
			Serializable,
			Comparable<CoronaBaseObject> {

	private Long id;
	private String recipient;
	private String priceProcedure;
	private String priceId;
	private String bundleId;
	private String shipToCountry;
	private String priceDescriptor;
	private String priceIdType;
	private String shipToGeo;
	private String priceGeo;
	private String currency;
	private String incoTerm;
	private String priceListType;
	private String priceStatus;
	private String priceErrorMsg;
	private Timestamp createdDate;
	public String createdBy;
	private Timestamp lastModifiedDate;
	private String lastModifiedBy;
	private String delFlag;
	private String srcTrnId;
	private Long trnId;
	private String action;
	private String updtFlag;
	private String srcPkey;

	public ConfigPriceHeaderInfo() {
		// TODO Auto-generated constructor stub
		this.setType("ConfigPriceHeaderInfo");
	}

	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getRecipient() {
		return recipient;
	}
	public void setRecipient(String recipient) {
		this.recipient = recipient;
	}
	public String getPriceProcedure() {
		return priceProcedure;
	}
	public void setPriceProcedure(String priceProcedure) {
		this.priceProcedure = priceProcedure;
	}
	public String getPriceId() {
		return priceId;
	}
	public void setPriceId(String priceId) {
		this.priceId = priceId;
	}
	public String getBundleId() {
		return bundleId;
	}
	public void setBundleId(String bundleId) {
		this.bundleId = bundleId;
	}
	public String getShipToCountry() {
		return shipToCountry;
	}
	public void setShipToCountry(String shipToCountry) {
		this.shipToCountry = shipToCountry;
	}
	public String getPriceDescriptor() {
		return priceDescriptor;
	}
	public void setPriceDescriptor(String priceDescriptor) {
		this.priceDescriptor = priceDescriptor;
	}
	public String getPriceIdType() {
		return priceIdType;
	}
	public void setPriceIdType(String priceIdType) {
		this.priceIdType = priceIdType;
	}
	public String getShipToGeo() {
		return shipToGeo;
	}
	public void setShipToGeo(String shipToGeo) {
		this.shipToGeo = shipToGeo;
	}
	public String getPriceGeo() {
		return priceGeo;
	}
	public void setPriceGeo(String priceGeo) {
		this.priceGeo = priceGeo;
	}
	public String getCurrency() {
		return currency;
	}
	public void setCurrency(String currency) {
		this.currency = currency;
	}
	public String getIncoTerm() {
		return incoTerm;
	}
	public void setIncoTerm(String incoTerm) {
		this.incoTerm = incoTerm;
	}
	public String getPriceListType() {
		return priceListType;
	}
	public void setPriceListType(String priceListType) {
		this.priceListType = priceListType;
	}
	public String getPriceStatus() {
		return priceStatus;
	}
	public void setPriceStatus(String priceStatus) {
		this.priceStatus = priceStatus;
	}
	public String getPriceErrorMsg() {
		return priceErrorMsg;
	}
	public void setPriceErrorMsg(String priceErrorMsg) {
		this.priceErrorMsg = priceErrorMsg;
	}
	public Timestamp getCreatedDate() {
		return createdDate;
	}
	public void setCreatedDate(Timestamp createdDate) {
		this.createdDate = createdDate;
	}
	public String getCreatedBy() {
		return createdBy;
	}
	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}
	public Timestamp getLastModifiedDate() {
		return lastModifiedDate;
	}
	public void setLastModifiedDate(Timestamp lastModifiedDate) {
		this.lastModifiedDate = lastModifiedDate;
	}
	public String getLastModifiedBy() {
		return lastModifiedBy;
	}
	public void setLastModifiedBy(String lastModifiedBy) {
		this.lastModifiedBy = lastModifiedBy;
	}
	public String getDelFlag() {
		return delFlag;
	}
	public void setDelFlag(String delFlag) {
		this.delFlag = delFlag;
	}
	public String getSrcTrnId() {
		return srcTrnId;
	}
	public void setSrcTrnId(String srcTrnId) {
		this.srcTrnId = srcTrnId;
	}
	public Long getTrnId() {
		return trnId;
	}
	public void setTrnId(Long trnId) {
		this.trnId = trnId;
	}
	public String getAction() {
		return action;
	}
	public void setAction(String action) {
		this.action = action;
	}
	public String getUpdtFlag() {
		return updtFlag;
	}
	public void setUpdtFlag(String updtFlag) {
		this.updtFlag = updtFlag;
	}

	public String toString() {
		return super.toDebugString(this);
	}
	public String getSrcPkey() {
		return srcPkey;
	}

	public int compareTo(CoronaBaseObject cbo) {
		return super.compareTo(this, cbo);
	}
	public void setSrcPkey(String srcPkey) {
		this.srcPkey = srcPkey;
	}

}
