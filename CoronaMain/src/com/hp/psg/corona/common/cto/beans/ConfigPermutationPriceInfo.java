package com.hp.psg.corona.common.cto.beans;

import java.io.Serializable;
import java.sql.Timestamp;


/**
 * @author dudeja
 * @version 1.0
 *
 */
public class ConfigPermutationPriceInfo extends CoronaBaseObject
		implements
			Serializable,
			Comparable<CoronaBaseObject> {

	private Long id;
	private String priceId;
	private String bundleId;
	private String shipToCountry;
	private String priceDescriptor;
	private Timestamp priceStartDate;
	private Timestamp priceEndDate;
	private Double netPrice;
	private String updtFlag;
	private String priceStatus;
	private String delFlag;
	private String priceErrMsg;
	private String createdBy;
	private Timestamp createdDate;
	private Timestamp lastModifiedDate;
	private String lastModifiedBy;
	private Long trnId;
	private Long srcTrnId;
	private String action;

	public ConfigPermutationPriceInfo() {
		// TODO Auto-generated constructor stub
		this.setType("ConfigPermutationPriceInfo");
	}

	public Long getSrcTrnId() {
		return srcTrnId;
	}
	public void setSrcTrnId(Long srcTrnId) {
		this.srcTrnId = srcTrnId;
	}
	public String getAction() {
		return action;
	}
	public void setAction(String action) {
		this.action = action;
	}
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
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

	public String getUpdtFlag() {
		return updtFlag;
	}
	public Double getNetPrice() {
		return netPrice;
	}

	public void setNetPrice(Double netPrice) {
		this.netPrice = netPrice;
	}

	public void setUpdtFlag(String updtFlag) {
		this.updtFlag = updtFlag;
	}
	public String getPriceStatus() {
		return priceStatus;
	}
	public void setPriceStatus(String priceStatus) {
		this.priceStatus = priceStatus;
	}
	public String getDelFlag() {
		return delFlag;
	}
	public void setDelFlag(String delFlag) {
		this.delFlag = delFlag;
	}
	public String getPriceErrMsg() {
		return priceErrMsg;
	}
	public void setPriceErrMsg(String priceErrMsg) {
		this.priceErrMsg = priceErrMsg;
	}
	public String getCreatedBy() {
		return createdBy;
	}
	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}
	public Timestamp getCreatedDate() {
		return createdDate;
	}
	public void setCreatedDate(Timestamp createdDate) {
		this.createdDate = createdDate;
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
	public Long getTrnId() {
		return trnId;
	}
	public void setTrnId(Long trnId) {
		this.trnId = trnId;
	}

	public String toString() {
		return super.toDebugString(this);
	}

	public int compareTo(CoronaBaseObject cbo) {
		return super.compareTo(this, cbo);
	}

	public Timestamp getPriceStartDate() {
		return priceStartDate;
	}

	public void setPriceStartDate(Timestamp priceStartDate) {
		this.priceStartDate = priceStartDate;
	}

	public Timestamp getPriceEndDate() {
		return priceEndDate;
	}

	public void setPriceEndDate(Timestamp priceEndDate) {
		this.priceEndDate = priceEndDate;
	}

}
