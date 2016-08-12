package com.hp.psg.corona.replication.quote.vo;

import java.io.Serializable;
import java.util.Date;

/**
 * QuoteItemVO class contains value objects for all the Database columns in
 * QUOTE_ITEM table.
 * 
 * @author rohitc
 *
 */

public class QuoteItemVO implements Serializable{

	private static final long serialVersionUID = 1L;
	private String slsQtnItmSqnNR;      
	private Long slsQtnID;              
	private String slsQtnVrsnSqnNR;     
	private String productNR;              
	private String ucID;                    
	private String ucSubConfigID;        
	private Long ucLineItemID;         
	private String priceErrorMessage;      
	private String productLine;            
	private Double pricingRate;            
	private String pricingRateType;       
	private String paNR;                   
	private String paExhibitNR;           
	private String paCac;                  
	private Double paDiscountRate;   
	private String paDiscountGeo;     
	private Date paExpiryDate;            
	private String paBuyerName;         
	private String paCustomerName; 
	private String productDescription; 
	private String priceStatus;            
	private String unitListPriceString; 
	private String unitNetPriceString; 
	private String activityStatusCode;   
	private String productClassCode;      
	private String priceSourceCode;       
	private String dealNR;                 
	private Long assetItemNR;   
	private String productOption; 
	private String airpackedUnitWeight;   
	private String bundleID;               
	private char deleteInd;              
	private Date createdTS;             
	private String createdBy;             
	private Date lastModifiedTS;  
	private String lastModifiedBy;   
	private String lineTypeCD;            
	private String assocPfCD;            
	private String lineProgCD;          
	private String spclConfigFL;       
	private String productId;  
	private Integer versionCreated;    
	private Double specialPaymentAmt; 
	private String srcConfigID;
	
	public String getSlsQtnItmSqnNR() {
		return slsQtnItmSqnNR;
	}
	public void setSlsQtnItmSqnNR(String slsQtnItmSqnNR) {
		this.slsQtnItmSqnNR = slsQtnItmSqnNR;
	}
	public Long getSlsQtnID() {
		return slsQtnID;
	}
	public void setSlsQtnID(Long slsQtnID) {
		this.slsQtnID = slsQtnID;
	}
	public String getSlsQtnVrsnSqnNR() {
		return slsQtnVrsnSqnNR;
	}
	public void setSlsQtnVrsnSqnNR(String slsQtnVrsnSqnNR) {
		this.slsQtnVrsnSqnNR = slsQtnVrsnSqnNR;
	}
	public String getProductNR() {
		return productNR;
	}
	public void setProductNR(String productNR) {
		this.productNR = productNR;
	}
	public String getUcID() {
		return ucID;
	}
	public void setUcID(String ucID) {
		this.ucID = ucID;
	}
	public String getUcSubConfigID() {
		return ucSubConfigID;
	}
	public void setUcSubConfigID(String ucSubConfigID) {
		this.ucSubConfigID = ucSubConfigID;
	}
	public Long getUcLineItemID() {
		return ucLineItemID;
	}
	public void setUcLineItemID(Long ucLineItemID) {
		this.ucLineItemID = ucLineItemID;
	}
	public String getPriceErrorMessage() {
		return priceErrorMessage;
	}
	public void setPriceErrorMessage(String priceErrorMessage) {
		this.priceErrorMessage = priceErrorMessage;
	}
	public String getProductLine() {
		return productLine;
	}
	public void setProductLine(String productLine) {
		this.productLine = productLine;
	}
	public Double getPricingRate() {
		return pricingRate;
	}
	public void setPricingRate(Double pricingRate) {
		this.pricingRate = pricingRate;
	}
	public String getPricingRateType() {
		return pricingRateType;
	}
	public void setPricingRateType(String pricingRateType) {
		this.pricingRateType = pricingRateType;
	}
	public String getPaNR() {
		return paNR;
	}
	public void setPaNR(String paNR) {
		this.paNR = paNR;
	}
	public String getPaExhibitNR() {
		return paExhibitNR;
	}
	public void setPaExhibitNR(String paExhibitNR) {
		this.paExhibitNR = paExhibitNR;
	}
	public String getPaCac() {
		return paCac;
	}
	public void setPaCac(String paCac) {
		this.paCac = paCac;
	}
	public Double getPaDiscountRate() {
		return paDiscountRate;
	}
	public void setPaDiscountRate(Double paDiscountRate) {
		this.paDiscountRate = paDiscountRate;
	}
	public String getPaDiscountGeo() {
		return paDiscountGeo;
	}
	public void setPaDiscountGeo(String paDiscountGeo) {
		this.paDiscountGeo = paDiscountGeo;
	}
	public Date getPaExpiryDate() {
		return paExpiryDate;
	}
	public void setPaExpiryDate(Date paExpiryDate) {
		this.paExpiryDate = paExpiryDate;
	}
	public String getPaBuyerName() {
		return paBuyerName;
	}
	public void setPaBuyerName(String paBuyerName) {
		this.paBuyerName = paBuyerName;
	}
	public String getPaCustomerName() {
		return paCustomerName;
	}
	public void setPaCustomerName(String paCustomerName) {
		this.paCustomerName = paCustomerName;
	}
	public String getProductDescription() {
		return productDescription;
	}
	public void setProductDescription(String productDescription) {
		this.productDescription = productDescription;
	}
	public String getPriceStatus() {
		return priceStatus;
	}
	public void setPriceStatus(String priceStatus) {
		this.priceStatus = priceStatus;
	}
	public String getUnitListPriceString() {
		return unitListPriceString;
	}
	public void setUnitListPriceString(String unitListPriceString) {
		this.unitListPriceString = unitListPriceString;
	}
	public String getUnitNetPriceString() {
		return unitNetPriceString;
	}
	public void setUnitNetPriceString(String unitNetPriceString) {
		this.unitNetPriceString = unitNetPriceString;
	}
	public String getActivityStatusCode() {
		return activityStatusCode;
	}
	public void setActivityStatusCode(String activityStatusCode) {
		this.activityStatusCode = activityStatusCode;
	}
	public String getProductClassCode() {
		return productClassCode;
	}
	public void setProductClassCode(String productClassCode) {
		this.productClassCode = productClassCode;
	}
	public String getPriceSourceCode() {
		return priceSourceCode;
	}
	public void setPriceSourceCode(String priceSourceCode) {
		this.priceSourceCode = priceSourceCode;
	}
	public String getDealNR() {
		return dealNR;
	}
	public void setDealNR(String dealNR) {
		this.dealNR = dealNR;
	}
	public Long getAssetItemNR() {
		return assetItemNR;
	}
	public void setAssetItemNR(Long assetItemNR) {
		this.assetItemNR = assetItemNR;
	}
	public String getProductOption() {
		return productOption;
	}
	public void setProductOption(String productOption) {
		this.productOption = productOption;
	}
	public String getAirpackedUnitWeight() {
		return airpackedUnitWeight;
	}
	public void setAirpackedUnitWeight(String airpackedUnitWeight) {
		this.airpackedUnitWeight = airpackedUnitWeight;
	}
	public String getBundleID() {
		return bundleID;
	}
	public void setBundleID(String bundleID) {
		this.bundleID = bundleID;
	}
	public char getDeleteInd() {
		return deleteInd;
	}
	public void setDeleteInd(char deleteInd) {
		this.deleteInd = deleteInd;
	}
	public Date getCreatedTS() {
		return createdTS;
	}
	public void setCreatedTS(Date createdTS) {
		this.createdTS = createdTS;
	}
	public String getCreatedBy() {
		return createdBy;
	}
	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}
	public Date getLastModifiedTS() {
		return lastModifiedTS;
	}
	public void setLastModifiedTS(Date lastModifiedTS) {
		this.lastModifiedTS = lastModifiedTS;
	}
	public String getLastModifiedBy() {
		return lastModifiedBy;
	}
	public void setLastModifiedBy(String lastModifiedBy) {
		this.lastModifiedBy = lastModifiedBy;
	}
	public String getLineTypeCD() {
		return lineTypeCD;
	}
	public void setLineTypeCD(String lineTypeCD) {
		this.lineTypeCD = lineTypeCD;
	}
	public String getAssocPfCD() {
		return assocPfCD;
	}
	public void setAssocPfCD(String assocPfCD) {
		this.assocPfCD = assocPfCD;
	}
	public String getLineProgCD() {
		return lineProgCD;
	}
	public void setLineProgCD(String lineProgCD) {
		this.lineProgCD = lineProgCD;
	}
	public String getSpclConfigFL() {
		return spclConfigFL;
	}
	public void setSpclConfigFL(String spclConfigFL) {
		this.spclConfigFL = spclConfigFL;
	}
	public String getProductId() {
		return productId;
	}
	public void setProductId(String productId) {
		this.productId = productId;
	}
	public Integer getVersionCreated() {
		return versionCreated;
	}
	public void setVersionCreated(Integer versionCreated) {
		this.versionCreated = versionCreated;
	}
	public Double getSpecialPaymentAmt() {
		return specialPaymentAmt;
	}
	public void setSpecialPaymentAmt(Double specialPaymentAmt) {
		this.specialPaymentAmt = specialPaymentAmt;
	}
	public String getSrcConfigID() {
		return srcConfigID;
	}
	public void setSrcConfigID(String srcConfigID) {
		this.srcConfigID = srcConfigID;
	}
	

	
}
