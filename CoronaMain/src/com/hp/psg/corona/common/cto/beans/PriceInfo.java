package com.hp.psg.corona.common.cto.beans;

import java.io.Serializable;
import java.sql.Timestamp;


/**
 * @author dudeja
 * @version 1.0
 *
 */
public class PriceInfo extends CoronaBaseObject
		implements
			Serializable,
			Comparable<CoronaBaseObject> {
	private static final long serialVersionUID = 1L;
	private Long id;
	private String recipient;
	private Long cphiId;
	private String priceProcedure;
	private String priceId;
	private String productId;
	private String productNumber;
	private String productOptionCd;
	private String bundleId;
	private String shipToCountry;
	private String priceDescriptor;
	private String priceIdType;
	private String shipToGeo;
	private String priceGeo;
	private String currency;
	private String incoTerm;
	private String priceListType;
	private Double netPrice;
	private Double netPriceBase;
	private Double netPriceOption;
	private Double listPrice;
	private Double listPriceBase;
	private Double listPriceOption;
	private String priceSourceBase;
	private String priceSourceOption;
	private String dealNumberBase;
	private String dealNumberOption;
	private String agreementBase;
	private String agreementOption;
	private String tierIdBase;
	private String tierIdOption;
	private Timestamp priceStartDate;
	private Timestamp priceEndDate;
	private String priceStatus;
	private String priceErrorMsg;
	private String quantityBreak;
	private String genericPriceFlag;
	private String whereFromInfo;
	private Timestamp versionDate;
	private Timestamp createdDate;
	public String createdBy;
	private Timestamp lastModifiedDate;
	private String lastModifiedBy;
	private String updtFlag;
	private Long trnId;
	private String delFlag;
	private Long whereFromLng;
	private String dealTypeBase;
	private String dealTypeOption;
	private String priceSource;
	private String dealNumber;
	private String agreement;
	private String tierId;
	private String dealType;
	private String srcTrnId;
	private String action;
	private String whereFromConfig;
	private Long configQty;

	
/////////////////// Missing from table., Moved to ConfigPriceHeaderInfo.
//	private String priceProcedure;
//	private String priceId;
//	private String bundleId;
//	private String shipToCountry;
//	private String priceDescriptor;
//	private String priceIdType;
//	private String shipToGeo;
//	private String priceGeo;
//	private String currency;
//	private String incoTerm;
//	private String priceListType;
//	
	
	
	
	public Long getConfigQty() {
		return configQty;
	}

	public void setConfigQty(Long configQty) {
		this.configQty = configQty;
	}

	public PriceInfo() {
		// TODO Auto-generated constructor stub
		this.setType("PriceInfo");
	}

	public Timestamp getVersionDate() {
		return versionDate;
	}
	public void setVersionDate(Timestamp versionDate) {
		this.versionDate = versionDate;
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
	public String getAction() {
		return action;
	}
	public String getWhereFromConfig() {
		return whereFromConfig;
	}
	public void setWhereFromConfig(String whereFromConfig) {
		this.whereFromConfig = whereFromConfig;
	}
	public void setAction(String action) {
		this.action = action;
	}
	public String getSrcTrnId() {
		return srcTrnId;
	}
	public void setSrcTrnId(String srcTrnId) {
		this.srcTrnId = srcTrnId;
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
	public String getProductId() {
		return productId;
	}
	public void setProductId(String productId) {
		this.productId = productId;
	}
	public String getProductNumber() {
		return productNumber;
	}
	public void setProductNumber(String productNumber) {
		this.productNumber = productNumber;
	}
	public String getProductOptionCd() {
		return productOptionCd;
	}
	public void setProductOptionCd(String productOptionCd) {
		this.productOptionCd = productOptionCd;
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

	public Double getNetPrice() {
		return netPrice;
	}

	public void setNetPrice(Double netPrice) {
		this.netPrice = netPrice;
	}

	public Double getNetPriceBase() {
		return netPriceBase;
	}

	public void setNetPriceBase(Double netPriceBase) {
		this.netPriceBase = netPriceBase;
	}

	public Double getNetPriceOption() {
		return netPriceOption;
	}

	public void setNetPriceOption(Double netPriceOption) {
		this.netPriceOption = netPriceOption;
	}

	public Double getListPrice() {
		return listPrice;
	}

	public void setListPrice(Double listPrice) {
		this.listPrice = listPrice;
	}

	public Double getListPriceBase() {
		return listPriceBase;
	}

	public void setListPriceBase(Double listPriceBase) {
		this.listPriceBase = listPriceBase;
	}

	public Double getListPriceOption() {
		return listPriceOption;
	}

	public void setListPriceOption(Double listPriceOption) {
		this.listPriceOption = listPriceOption;
	}

	public String getPriceSourceBase() {
		return priceSourceBase;
	}
	public void setPriceSourceBase(String priceSourceBase) {
		this.priceSourceBase = priceSourceBase;
	}
	public String getPriceSourceOption() {
		return priceSourceOption;
	}
	public void setPriceSourceOption(String priceSourceOption) {
		this.priceSourceOption = priceSourceOption;
	}
	public String getDealNumberBase() {
		return dealNumberBase;
	}
	public void setDealNumberBase(String dealNumberBase) {
		this.dealNumberBase = dealNumberBase;
	}
	public String getDealNumberOption() {
		return dealNumberOption;
	}
	public void setDealNumberOption(String dealNumberOption) {
		this.dealNumberOption = dealNumberOption;
	}
	public String getAgreementBase() {
		return agreementBase;
	}
	public void setAgreementBase(String agreementBase) {
		this.agreementBase = agreementBase;
	}
	public String getAgreementOption() {
		return agreementOption;
	}
	public void setAgreementOption(String agreementOption) {
		this.agreementOption = agreementOption;
	}
	public String getTierIdBase() {
		return tierIdBase;
	}
	public void setTierIdBase(String tierIdBase) {
		this.tierIdBase = tierIdBase;
	}
	public String getTierIdOption() {
		return tierIdOption;
	}
	public void setTierIdOption(String tierIdOption) {
		this.tierIdOption = tierIdOption;
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
	public String getQuantityBreak() {
		return quantityBreak;
	}
	public void setQuantityBreak(String quantityBreak) {
		this.quantityBreak = quantityBreak;
	}
	public String getGenericPriceFlag() {
		return genericPriceFlag;
	}
	public void setGenericPriceFlag(String genericPriceFlag) {
		this.genericPriceFlag = genericPriceFlag;
	}
	public String getWhereFromInfo() {
		return whereFromInfo;
	}
	public void setWhereFromInfo(String whereFromInfo) {
		this.whereFromInfo = whereFromInfo;
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
	public Long getTrnId() {
		return trnId;
	}
	public void setTrnId(Long trnId) {
		this.trnId = trnId;
	}
	public String getDelFlag() {
		return delFlag;
	}
	public void setDelFlag(String delFlag) {
		this.delFlag = delFlag;
	}
	public Long getWhereFromLng() {
		return whereFromLng;
	}
	public void setWhereFromLng(Long whereFromLng) {
		this.whereFromLng = whereFromLng;
	}
	public String getDealTypeBase() {
		return dealTypeBase;
	}
	public void setDealTypeBase(String dealTypeBase) {
		this.dealTypeBase = dealTypeBase;
	}
	public String getDealTypeOption() {
		return dealTypeOption;
	}
	public void setDealTypeOption(String dealTypeOption) {
		this.dealTypeOption = dealTypeOption;
	}
	public String getPriceSource() {
		return priceSource;
	}
	public void setPriceSource(String priceSource) {
		this.priceSource = priceSource;
	}
	public String getDealNumber() {
		return dealNumber;
	}
	public void setDealNumber(String dealNumber) {
		this.dealNumber = dealNumber;
	}
	public String getAgreement() {
		return agreement;
	}
	public void setAgreement(String agreement) {
		this.agreement = agreement;
	}
	public String getTierId() {
		return tierId;
	}
	public void setTierId(String tierId) {
		this.tierId = tierId;
	}
	public String getDealType() {
		return dealType;
	}
	public void setDealType(String dealType) {
		this.dealType = dealType;
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

	public Long getCphiId() {
		return cphiId;
	}

	public void setCphiId(Long cphiId) {
		this.cphiId = cphiId;
	}
}
