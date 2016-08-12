package com.hp.psg.corona.replication.beans;

import java.io.Serializable;
import java.util.Date;

import com.hp.psg.corona.common.cto.beans.CoronaBaseObject;

/**
 * @author dudeja
 * @version 1.0
 *
 */
public class ReplicationDataBean extends CoronaBaseObject
		implements
			Serializable,
			Comparable<CoronaBaseObject> {

	private Long id;
	private String priceId;
	private String bundleId;
	private String shipToCountry;
	private String priceDescriptor;
	private Date priceStartDate;
	private Date priceEndDate;
	private Double listPrice;
	private Double netPrice;
	private String priceWhereFrom;
	private String updtFlag;
	private String priceStatus;
	private String delFlag;
	private String priceErrMsg;
	private String createdBy;
	private Date createdDate;
	private Date lastModifiedDate;
	private String lastModifiedBy;
	private Long trnId;
	private Long srcTrnId;
	private String action;

	private String genericPriceFlag;
	private String priceProcedure;
	private String overallStatus;
	private String regionCode;
	private String newPermFlag;
	private String custKey;
	private Date overallEol;
	private String priceIdType;
	private String configProdId;

	private String materialId;

	private String priceSourceBase;

	public ReplicationDataBean() {
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

	public Date getPriceStartDate() {
		return priceStartDate;
	}

	public void setPriceStartDate(Date priceStartDate) {
		this.priceStartDate = priceStartDate;
	}

	public Date getPriceEndDate() {
		return priceEndDate;
	}

	public void setPriceEndDate(Date priceEndDate) {
		this.priceEndDate = priceEndDate;
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

	public Date getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
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

	public Long getTrnId() {
		return trnId;
	}

	public void setTrnId(Long trnId) {
		this.trnId = trnId;
	}

	public String getConfigProdId() {
		return configProdId;
	}

	public void setConfigProdId(String configProdId) {
		this.configProdId = configProdId;
	}

	public String getCustKey() {
		return custKey;
	}

	public void setCustKey(String custKey) {
		this.custKey = custKey;
	}

	public String getNewPermFlag() {
		return newPermFlag;
	}

	public void setNewPermFlag(String newPermFlag) {
		this.newPermFlag = newPermFlag;
	}

	public Date getOverallEol() {
		return overallEol;
	}

	public void setOverallEol(Date overallEol) {
		this.overallEol = overallEol;
	}

	public String getOverallStatus() {
		return overallStatus;
	}

	public void setOverallStatus(String overallStatus) {
		this.overallStatus = overallStatus;
	}

	public String getPriceIdType() {
		return priceIdType;
	}

	public void setPriceIdType(String priceIdType) {
		this.priceIdType = priceIdType;
	}

	public String getPriceProcedure() {
		return priceProcedure;
	}

	public void setPriceProcedure(String priceProcedure) {
		this.priceProcedure = priceProcedure;
	}

	public String getRegionCode() {
		return regionCode;
	}

	public void setRegionCode(String regionCode) {
		this.regionCode = regionCode;
	}

	public String getGenericPriceFlag() {
		return genericPriceFlag;
	}

	public void setGenericPriceFlag(String genericPriceFlag) {
		this.genericPriceFlag = genericPriceFlag;
	}

	public Double getListPrice() {
		return listPrice;
	}

	public void setListPrice(Double listPrice) {
		this.listPrice = listPrice;
	}

	public String getPriceWhereFrom() {
		return priceWhereFrom;
	}

	public void setPriceWhereFrom(String priceWhereFrom) {
		this.priceWhereFrom = priceWhereFrom;
	}

	public String getMaterialId() {
		return materialId;
	}

	public void setMaterialId(String materialId) {
		this.materialId = materialId;
	}

	public String getPriceSourceBase() {
		return priceSourceBase;
	}

	public void setPriceSourceBase(String priceSourceBase) {
		this.priceSourceBase = priceSourceBase;
	}
	public String toString() {
		return super.toDebugString(this);
	}

	public int compareTo(CoronaBaseObject cbo) {
		return super.compareTo(this, cbo);
	}
}
