package com.hp.psg.corona.replication.quote.vo;

import java.io.Serializable;
import java.util.Date;

public class QuoteCustomerVO implements Serializable {

	/**
	 * Value object to hold QUOTE_CUSTOMER table data
	 */
	private static final long serialVersionUID = 1L;
	private Long slsQtnID;
	private String slsQtnVrsnSqnNR;
	private String customerType;
	private String companyName;
	private String crmID;
	private String customerLangCode;
	private String companyNR;
	private String division;
	private String duns;
	private String partnerID;
	private char deleteInd;
	private Date createdTS;
	private String createdBy;
	private Date lastModifiedTS;
	private String lastModifiedBy;
	private String custSegCd;
	
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
	public String getCustomerType() {
		return customerType;
	}
	public void setCustomerType(String customerType) {
		this.customerType = customerType;
	}
	public String getCompanyName() {
		return companyName;
	}
	public void setCompanyName(String companyName) {
		this.companyName = companyName;
	}
	public String getCrmID() {
		return crmID;
	}
	public void setCrmID(String crmID) {
		this.crmID = crmID;
	}
	public String getCustomerLangCode() {
		return customerLangCode;
	}
	public void setCustomerLangCode(String customerLangCode) {
		this.customerLangCode = customerLangCode;
	}
	public String getCompnayNR() {
		return companyNR;
	}
	public void setCompnayNR(String compnayNR) {
		this.companyNR = compnayNR;
	}
	public String getDivision() {
		return division;
	}
	public void setDivision(String division) {
		this.division = division;
	}
	public String getDuns() {
		return duns;
	}
	public void setDuns(String duns) {
		this.duns = duns;
	}
	public String getPartnerID() {
		return partnerID;
	}
	public void setPartnerID(String partnerID) {
		this.partnerID = partnerID;
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
	public String getCustSegCd() {
		return custSegCd;
	}
	public void setCustSegCd(String custSegCd) {
		this.custSegCd = custSegCd;
	}
}
