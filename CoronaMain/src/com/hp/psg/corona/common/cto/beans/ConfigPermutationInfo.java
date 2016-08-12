package com.hp.psg.corona.common.cto.beans;

import java.io.Serializable;
import java.sql.Timestamp;


/**
 * @author dudeja
 * @version 1.0
 *
 */
public class ConfigPermutationInfo extends CoronaBaseObject
		implements
			Serializable,
			Comparable<CoronaBaseObject> {

	private Long id;
	private String priceId;
	private String bundleId;
	private String shipToCountry;
	private String priceDescriptor;
	private String genericPriceFlag;
	private Timestamp createdDate;
	private String createdBy;
	private Timestamp lastModifiedDate;
	private String lastModifiedBy;
	private String updtFlag;
	private String priceProcedure;
	private String delFlag;
	private String overallStatus;
	private String regionCode;
	private String custKey;
	private Timestamp overallEol;
	private String priceIdType;
	private Long trnId;
	private Long srcTrnId;
	private String action;
	private String configProdId;
	private Long cppiId;
	private String registerIPCS;
	private String registerPRS;
	public String getParentBundleId() {
		return parentBundleId;
	}

	public void setParentBundleId(String parentBundleId) {
		this.parentBundleId = parentBundleId;
	}

	private String parentBundleId;
	private String cpiActualRegion;
	private String cpiCustSegment;

	public String getCpiCustSegment() {
		return cpiCustSegment;
	}

	public void setCpiCustSegment(String cpiCustSegment) {
		this.cpiCustSegment = cpiCustSegment;
	}

	public String getRegisterIPCS() {
		return registerIPCS;
	}

	public void setRegisterIPCS(String registerIPCS) {
		this.registerIPCS = registerIPCS;
	}

	public String getRegisterPRS() {
		return registerPRS;
	}

	public void setRegisterPRS(String registerPRS) {
		this.registerPRS = registerPRS;
	}

	public String getCpiActualRegion() {
		return cpiActualRegion;
	}

	public void setCpiActualRegion(String cpiActualRegion) {
		this.cpiActualRegion = cpiActualRegion;
	}

	public Long getCppiId() {
		return cppiId;
	}

	public void setCppiId(Long cppiId) {
		this.cppiId = cppiId;
	}

	public ConfigPermutationInfo() {
		// TODO Auto-generated constructor stub
		this.setType("ConfigPermutationInfo");
	}

	public String getConfigProdId() {
		return configProdId;
	}
	public void setConfigProdId(String configProdId) {
		this.configProdId = configProdId;
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
	public String getGenericPriceFlag() {
		return genericPriceFlag;
	}
	public void setGenericPriceFlag(String genericPriceFlag) {
		this.genericPriceFlag = genericPriceFlag;
	}

	public String getCreatedBy() {
		return createdBy;
	}
	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}

	public String getLastModifiedBy() {
		return lastModifiedBy;
	}
	public void setLastModifiedBy(String lastModifiedBy) {
		this.lastModifiedBy = lastModifiedBy;
	}
	public String getUpdtFlag() {
		return updtFlag;
	}
	public void setUpdtFlag(String updtFlag) {
		this.updtFlag = updtFlag;
	}
	public String getPriceProcedure() {
		return priceProcedure;
	}
	public void setPriceProcedure(String priceProcedure) {
		this.priceProcedure = priceProcedure;
	}
	public String getDelFlag() {
		return delFlag;
	}
	public void setDelFlag(String delFlag) {
		this.delFlag = delFlag;
	}
	public String getOverallStatus() {
		return overallStatus;
	}
	public void setOverallStatus(String overallStatus) {
		this.overallStatus = overallStatus;
	}
	public String getRegionCode() {
		return regionCode;
	}
	public void setRegionCode(String regionCode) {
		this.regionCode = regionCode;
	}
	public String getCustKey() {
		return custKey;
	}
	public void setCustKey(String custKey) {
		this.custKey = custKey;
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

	public String getPriceIdType() {
		return priceIdType;
	}
	public void setPriceIdType(String priceIdType) {
		this.priceIdType = priceIdType;
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

	public Timestamp getOverallEol() {
		return overallEol;
	}

	public void setOverallEol(Timestamp overallEol) {
		this.overallEol = overallEol;
	}

}
