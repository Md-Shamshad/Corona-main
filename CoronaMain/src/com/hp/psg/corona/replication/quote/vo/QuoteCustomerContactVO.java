package com.hp.psg.corona.replication.quote.vo;

import java.io.Serializable;
import java.util.Date;

public class QuoteCustomerContactVO implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Long slsQtnId;
	private String slsQtnVrsnSqnNr;
	private String customerType;
	private String department;
	private String email;
	private String email2;
	private String fax;
	private String faxExt;
	private String firstName;
	private String middleName;
	private String lastName;
	private String phone;
	private String phoneExt;
	private String phone2;
	private String phone2Ext;
	private String title;
	private char deleteInd;
	private Date createdTs;
	private String createdBy;
	private Date lastModifiedTs;
	private String lastModifiedBy;
	
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
	public String getCustomerType() {
		return customerType;
	}
	public void setCustomerType(String customerType) {
		this.customerType = customerType;
	}
	public String getDepartment() {
		return department;
	}
	public void setDepartment(String department) {
		this.department = department;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getEmail2() {
		return email2;
	}
	public void setEmail2(String email2) {
		this.email2 = email2;
	}
	public String getFax() {
		return fax;
	}
	public void setFax(String fax) {
		this.fax = fax;
	}
	public String getFaxExt() {
		return faxExt;
	}
	public void setFaxExt(String faxExt) {
		this.faxExt = faxExt;
	}
	public String getFirstName() {
		return firstName;
	}
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}
	public String getMiddleName() {
		return middleName;
	}
	public void setMiddleName(String middleName) {
		this.middleName = middleName;
	}
	public String getLastName() {
		return lastName;
	}
	public void setLastName(String lastName) {
		this.lastName = lastName;
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
	public String getPhone2() {
		return phone2;
	}
	public void setPhone2(String phone2) {
		this.phone2 = phone2;
	}
	public String getPhone2Ext() {
		return phone2Ext;
	}
	public void setPhone2Ext(String phone2Ext) {
		this.phone2Ext = phone2Ext;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
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
