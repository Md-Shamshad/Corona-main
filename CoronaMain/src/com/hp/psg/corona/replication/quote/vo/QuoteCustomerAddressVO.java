package com.hp.psg.corona.replication.quote.vo;

import java.io.Serializable;
import java.util.Date;

public class QuoteCustomerAddressVO implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private Long slsQtnID;
	private String slsQtnVrsnSqnNR;
	private String customerType;
	private String city;
	private String cityArea;
	private String country;
	private String phone;
	private String phoneExt;
	private String region;
	private String state;
	private String street;
	private String street2;
	private String street3;
	private String postalCode;
	private char deleteInd;
	private Date createdTS;
	private String createdBy;
	private Date lastModifiedTS;
	private String lastModifiedBy;
	
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
	public String getCity() {
		return city;
	}
	public void setCity(String city) {
		this.city = city;
	}
	public String getCityArea() {
		return cityArea;
	}
	public void setCityArea(String cityArea) {
		this.cityArea = cityArea;
	}
	public String getCountry() {
		return country;
	}
	public void setCountry(String country) {
		this.country = country;
	}
	public String getPhone() {
		return phone;
	}
	public void setPhone(String phone) {
		this.phone = phone;
	}
	public String getPhoneExt() {
		return phoneExt;
	}
	public void setPhoneExt(String phoneExt) {
		this.phoneExt = phoneExt;
	}
	public String getRegion() {
		return region;
	}
	public void setRegion(String region) {
		this.region = region;
	}
	public String getState() {
		return state;
	}
	public void setState(String state) {
		this.state = state;
	}
	public String getStreet() {
		return street;
	}
	public void setStreet(String street) {
		this.street = street;
	}
	public String getStreet2() {
		return street2;
	}
	public void setStreet2(String street2) {
		this.street2 = street2;
	}
	public String getStreet3() {
		return street3;
	}
	public void setStreet3(String street3) {
		this.street3 = street3;
	}
	public String getPostalCode() {
		return postalCode;
	}
	public void setPostalCode(String postalCode) {
		this.postalCode = postalCode;
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
}
