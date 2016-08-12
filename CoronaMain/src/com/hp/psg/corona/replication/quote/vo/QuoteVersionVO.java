package com.hp.psg.corona.replication.quote.vo;

import java.io.Serializable;
import java.util.Date;


/**
 * QuoteVersionVO class contains value objects for all the Database columns in
 * QUOTE_VERSION table.
 * 
 * @author rohitc
 *
 */
public class QuoteVersionVO implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Long slsQtnID;               
	private String slsQtnVrsnSqnNR;    
	private String uqID;                     
	private String referencedUQID;  
	private String origAsset;               
	private Date completionTimestamp;      
	private String deliveryTerms;      
	private String priceListType;       
	private String lastPricedDate;       
	private String visibilityState;       
	private String shareState;              
	private String regionCode;                
	private String priceGeo;                
	private String customerCompanyName;    
	private String hideZeroDollarInd;     
	private String paNR;                    
	private String paCac;                  
	private String paDiscountGeo;   
	private String eDeliveryEmail;      
	private String opportunityID;         
	private Date modifiedTimestamp;              
	private String modifiedPersonID;    
	private Double totalAmt;                
	private String airPackedTotalWeight;   
	private Date paExpiryTS;            
	private String assetQuoteNR;    
	private char deleteInd;              
	private Date createdTimestamp;             
	private String createdBy;             
	private Date lastModifiedTimestamp;  
	private String lastModifiedBy;   
	private String creationPersonID; 
	private String eucRfpNR;               
	private String rslrExclvFL;          
	private String wLStatCD;             
	private Long estTotalKAM;           
	private Date quoteDistribDT;   
	private String busModelCD;          
	private String mcCharge;              
	private String opgNum;                  
	private Long paymentDays;         
	private Double cashDiscPct;         
	private Double balToFinance;     
	private Long leaseTerm;               
	private Long payFrequency;           
	private String bdmEmpNotifyFL;  
	private Double payAmount;               
	private Date orderBgnDT;             
	private Date orderEndDT;             
	private Date quoteDistDtGmt; 
	private String complexDealFL;   
	private String agentDealFL;       
	private String dealKind;                
	private String primaryQuoteURL;   
	private String directFL;                
	private Long termCondID;            
	private String quotePrimaryRslsType;  
	private String opportunityOrigSystem;  
	private String requestID;               
	private Integer assetQuoteVrsn;  
	private String pdfRefID;               
	private String pdfEncryptedID;  
	private String name;                     
	private String paymentTerm;      
	private Double subTotalAmt;    
	private Double totalTaxAmt;    
	private Double discountPct;       
	private String dealNR;                  
	private Double totalListPriceAmt;
	private Date requestFQuoteRcvdTS;
	private String assetInstance;
	private Double shippingAndHandlingAmt;
	private String sfdcStatus;

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
	public String getUqID() {
		return uqID;
	}
	public void setUqID(String uqID) {
		this.uqID = uqID;
	}
	public String getReferencedUQID() {
		return referencedUQID;
	}
	public void setReferencedUQID(String referencedUQID) {
		this.referencedUQID = referencedUQID;
	}
	public String getOrigAsset() {
		return origAsset;
	}
	public void setOrigAsset(String origAsset) {
		this.origAsset = origAsset;
	}
	public Date getCompletionTimestamp() {
		return completionTimestamp;
	}
	public void setCompletionTimestamp(Date completionTimestamp) {
		this.completionTimestamp = completionTimestamp;
	}
	public String getDeliveryTerms() {
		return deliveryTerms;
	}
	public void setDeliveryTerms(String deliveryTerms) {
		this.deliveryTerms = deliveryTerms;
	}
	public String getPriceListType() {
		return priceListType;
	}
	public void setPriceListType(String priceListType) {
		this.priceListType = priceListType;
	}
	public String getLastPricedDate() {
		return lastPricedDate;
	}
	public void setLastPricedDate(String lastPricedDate) {
		this.lastPricedDate = lastPricedDate;
	}
	public String getVisibilityState() {
		return visibilityState;
	}
	public void setVisibilityState(String visibilityState) {
		this.visibilityState = visibilityState;
	}
	public String getShareState() {
		return shareState;
	}
	public void setShareState(String shareState) {
		this.shareState = shareState;
	}
	public String getRegionCode() {
		return regionCode;
	}
	public void setRegionCode(String regionCode) {
		this.regionCode = regionCode;
	}
	public String getPriceGeo() {
		return priceGeo;
	}
	public void setPriceGeo(String priceGeo) {
		this.priceGeo = priceGeo;
	}
	public String getCustomerCompanyName() {
		return customerCompanyName;
	}
	public void setCustomerCompanyName(String customerCompanyName) {
		this.customerCompanyName = customerCompanyName;
	}
	public String getHideZeroDollarInd() {
		return hideZeroDollarInd;
	}
	public void setHideZeroDollarInd(String hideZeroDollarInd) {
		this.hideZeroDollarInd = hideZeroDollarInd;
	}
	public String getPaNR() {
		return paNR;
	}
	public void setPaNR(String paNR) {
		this.paNR = paNR;
	}
	public String getPaCac() {
		return paCac;
	}
	public void setPaCac(String paCac) {
		this.paCac = paCac;
	}
	public String getPaDiscountGeo() {
		return paDiscountGeo;
	}
	public void setPaDiscountGeo(String paDiscountGeo) {
		this.paDiscountGeo = paDiscountGeo;
	}
	public String geteDeliveryEmail() {
		return eDeliveryEmail;
	}
	public void seteDeliveryEmail(String eDeliveryEmail) {
		this.eDeliveryEmail = eDeliveryEmail;
	}
	public String getOpportunityID() {
		return opportunityID;
	}
	public void setOpportunityID(String opportunityID) {
		this.opportunityID = opportunityID;
	}
	public Date getModifiedTimestamp() {
		return modifiedTimestamp;
	}
	public void setModifiedTimestamp(Date modifiedTimestamp) {
		this.modifiedTimestamp = modifiedTimestamp;
	}
	public String getModifiedPersonID() {
		return modifiedPersonID;
	}
	public void setModifiedPersonID(String modifiedPersonID) {
		this.modifiedPersonID = modifiedPersonID;
	}
	public Double getTotalAmt() {
		return totalAmt;
	}
	public void setTotalAmt(Double totalAmt) {
		this.totalAmt = totalAmt;
	}
	public String getAirPackedTotalWeight() {
		return airPackedTotalWeight;
	}
	public void setAirPackedTotalWeight(String airPackedTotalWeight) {
		this.airPackedTotalWeight = airPackedTotalWeight;
	}
	public Date getPaExpiryTS() {
		return paExpiryTS;
	}
	public void setPaExpiryTS(Date paExpiryTS) {
		this.paExpiryTS = paExpiryTS;
	}
	public String getAssetQuoteNR() {
		return assetQuoteNR;
	}
	public void setAssetQuoteNR(String assetQuoteNR) {
		this.assetQuoteNR = assetQuoteNR;
	}
	public char getDeleteInd() {
		return deleteInd;
	}
	public void setDeleteInd(char deleteInd) {
		this.deleteInd = deleteInd;
	}
	public Date getCreatedTimestamp() {
		return createdTimestamp;
	}
	public void setCreatedTimestamp(Date createdTimestamp) {
		this.createdTimestamp = createdTimestamp;
	}
	public String getCreatedBy() {
		return createdBy;
	}
	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}
	public Date getLastModifiedTimestamp() {
		return lastModifiedTimestamp;
	}
	public void setLastModifiedTimestamp(Date lastModifiedTimestamp) {
		this.lastModifiedTimestamp = lastModifiedTimestamp;
	}
	public String getLastModifiedBy() {
		return lastModifiedBy;
	}
	public void setLastModifiedBy(String lastModifiedBy) {
		this.lastModifiedBy = lastModifiedBy;
	}
	public String getCreationPersonID() {
		return creationPersonID;
	}
	public void setCreationPersonID(String creationPersonID) {
		this.creationPersonID = creationPersonID;
	}
	public String getEucRfpNR() {
		return eucRfpNR;
	}
	public void setEucRfpNR(String eucRfpNR) {
		this.eucRfpNR = eucRfpNR;
	}
	public String getRslrExclvFL() {
		return rslrExclvFL;
	}
	public void setRslrExclvFL(String rslrExclvFL) {
		this.rslrExclvFL = rslrExclvFL;
	}
	public String getwLStatCD() {
		return wLStatCD;
	}
	public void setwLStatCD(String wLStatCD) {
		this.wLStatCD = wLStatCD;
	}
	public Long getEstTotalKAM() {
		return estTotalKAM;
	}
	public void setEstTotalKAM(Long estTotalKAM) {
		this.estTotalKAM = estTotalKAM;
	}
	public Date getQuoteDistribDT() {
		return quoteDistribDT;
	}
	public void setQuoteDistribDT(Date quoteDistribDT) {
		this.quoteDistribDT = quoteDistribDT;
	}
	public String getBusModelCD() {
		return busModelCD;
	}
	public void setBusModelCD(String busModelCD) {
		this.busModelCD = busModelCD;
	}
	public String getMcCharge() {
		return mcCharge;
	}
	public void setMcCharge(String mcCharge) {
		this.mcCharge = mcCharge;
	}
	public String getOpgNum() {
		return opgNum;
	}
	public void setOpgNum(String opgNum) {
		this.opgNum = opgNum;
	}
	public Long getPaymentDays() {
		return paymentDays;
	}
	public void setPaymentDays(Long paymentDays) {
		this.paymentDays = paymentDays;
	}
	public Double getCashDiscPct() {
		return cashDiscPct;
	}
	public void setCashDiscPct(Double cashDiscPct) {
		this.cashDiscPct = cashDiscPct;
	}
	public Double getBalToFinance() {
		return balToFinance;
	}
	public void setBalToFinance(Double balToFinance) {
		this.balToFinance = balToFinance;
	}
	public Long getLeaseTerm() {
		return leaseTerm;
	}
	public void setLeaseTerm(Long leaseTerm) {
		this.leaseTerm = leaseTerm;
	}
	public Long getPayFrequency() {
		return payFrequency;
	}
	public void setPayFrequency(Long payFrequency) {
		this.payFrequency = payFrequency;
	}
	public String getBdmEmpNotifyFL() {
		return bdmEmpNotifyFL;
	}
	public void setBdmEmpNotifyFL(String bdmEmpNotifyFL) {
		this.bdmEmpNotifyFL = bdmEmpNotifyFL;
	}
	public Double getPayAmount() {
		return payAmount;
	}
	public void setPayAmount(Double payAmount) {
		this.payAmount = payAmount;
	}
	public Date getOrderBgnDT() {
		return orderBgnDT;
	}
	public void setOrderBgnDT(Date orderBgnDT) {
		this.orderBgnDT = orderBgnDT;
	}
	public Date getOrderEndDT() {
		return orderEndDT;
	}
	public void setOrderEndDT(Date orderEndDT) {
		this.orderEndDT = orderEndDT;
	}
	public Date getQuoteDistDtGmt() {
		return quoteDistDtGmt;
	}
	public void setQuoteDistDtGmt(Date quoteDistDtGmt) {
		this.quoteDistDtGmt = quoteDistDtGmt;
	}
	public String getComplexDealFL() {
		return complexDealFL;
	}
	public void setComplexDealFL(String complexDealFL) {
		this.complexDealFL = complexDealFL;
	}
	public String getAgentDealFL() {
		return agentDealFL;
	}
	public void setAgentDealFL(String agentDealFL) {
		this.agentDealFL = agentDealFL;
	}
	public String getDealKind() {
		return dealKind;
	}
	public void setDealKind(String dealKind) {
		this.dealKind = dealKind;
	}
	public String getPrimaryQuoteURL() {
		return primaryQuoteURL;
	}
	public void setPrimaryQuoteURL(String primaryQuoteURL) {
		this.primaryQuoteURL = primaryQuoteURL;
	}
	public String getDirectFL() {
		return directFL;
	}
	public void setDirectFL(String directFL) {
		this.directFL = directFL;
	}
	public Long getTermCondID() {
		return termCondID;
	}
	public void setTermCondID(Long termCondID) {
		this.termCondID = termCondID;
	}
	public String getQuotePrimaryRslsType() {
		return quotePrimaryRslsType;
	}
	public void setQuotePrimaryRslsType(String quotePrimaryRslsType) {
		this.quotePrimaryRslsType = quotePrimaryRslsType;
	}
	public String getOpportunityOrigSystem() {
		return opportunityOrigSystem;
	}
	public void setOpportunityOrigSystem(String opportunityOrigSystem) {
		this.opportunityOrigSystem = opportunityOrigSystem;
	}
	public String getRequestID() {
		return requestID;
	}
	public void setRequestID(String requestID) {
		this.requestID = requestID;
	}
	public Integer getAssetQuoteVrsn() {
		return assetQuoteVrsn;
	}
	public void setAssetQuoteVrsn(Integer assetQuoteVrsn) {
		this.assetQuoteVrsn = assetQuoteVrsn;
	}
	public String getPdfRefID() {
		return pdfRefID;
	}
	public void setPdfRefID(String pdfRefID) {
		this.pdfRefID = pdfRefID;
	}
	public String getPdfEncryptedID() {
		return pdfEncryptedID;
	}
	public void setPdfEncryptedID(String pdfEncryptedID) {
		this.pdfEncryptedID = pdfEncryptedID;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getPaymentTerm() {
		return paymentTerm;
	}
	public void setPaymentTerm(String paymentTerm) {
		this.paymentTerm = paymentTerm;
	}
	public Double getSubTotalAmt() {
		return subTotalAmt;
	}
	public void setSubTotalAmt(Double subTotalAmt) {
		this.subTotalAmt = subTotalAmt;
	}
	public Double getTotalTaxAmt() {
		return totalTaxAmt;
	}
	public void setTotalTaxAmt(Double totalTaxAmt) {
		this.totalTaxAmt = totalTaxAmt;
	}
	public Double getDiscountPct() {
		return discountPct;
	}
	public void setDiscountPct(Double discountPct) {
		this.discountPct = discountPct;
	}
	public String getDealNR() {
		return dealNR;
	}
	public void setDealNR(String dealNR) {
		this.dealNR = dealNR;
	}
	public Double getTotalListPriceAmt() {
		return totalListPriceAmt;
	}
	public void setTotalListPriceAmt(Double totalListPriceAmt) {
		this.totalListPriceAmt = totalListPriceAmt;
	}
	public Date getRequestFQuoteRcvdTS() {
		return requestFQuoteRcvdTS;
	}
	public void setRequestFQuoteRcvdTS(Date requestFQuoteRcvdTS) {
		this.requestFQuoteRcvdTS = requestFQuoteRcvdTS;
	}
	public String getAssetInstance() {
		return assetInstance;
	}
	public void setAssetInstance(String assetInstance) {
		this.assetInstance = assetInstance;
	}
	public Double getShippingAndHandlingAmt() {
		return shippingAndHandlingAmt;
	}
	public void setShippingAndHandlingAmt(Double shippingAndHandlingAmt) {
		this.shippingAndHandlingAmt = shippingAndHandlingAmt;
	}
	public String getSfdcStatus() {
		return sfdcStatus;
	}
	public void setSfdcStatus(String sfdcStatus) {
		this.sfdcStatus = sfdcStatus;
	}

}
