package com.hp.psg.corona.common.cto.beans;

import java.io.Serializable;
import java.sql.Timestamp;


/**
 * @author dudeja
 * @version 1.0
 *
 */
public class ConfigHeaderInfo extends CoronaBaseObject
		implements
			Serializable,
			Comparable<CoronaBaseObject> {

	private Long id;
	private String bundleId;
	private String shipToCountry;
	private String baseProductId;
	private String registerFlag;
	private Timestamp configCntryEol;
	private String kmatId;
	private String validityStatus;
	private String errorCode;
	private String errorMsg;
	private Timestamp createdDate;
	private String createdBy;
	private Timestamp lastModifiedDate;
	private String lastModifiedBy;
	private String updtFlag;
	private String configDesc;
	private String delFlag;
	private String regionCode;
	private Long trnId;
	private String srcTrnId;
	private String action;
	//Added for IPP project: May 2014.
	private String bomFrmt;

	public ConfigHeaderInfo() {
		// TODO Auto-generated constructor stub
		this.setType("ConfigHeaderInfo");
	}

	public Long getId() {
		return id;
	}
	public String getSrcTrnId() {
		return srcTrnId;
	}
	public void setSrcTrnId(String srcTrnId) {
		this.srcTrnId = srcTrnId;
	}
	public String getAction() {
		return action;
	}
	public void setAction(String action) {
		this.action = action;
	}
	public void setId(Long id) {
		this.id = id;
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
	public String getBaseProductId() {
		return baseProductId;
	}
	public void setBaseProductId(String baseProductId) {
		this.baseProductId = baseProductId;
	}
	public String getRegisterFlag() {
		return registerFlag;
	}
	public void setRegisterFlag(String registerFlag) {
		this.registerFlag = registerFlag;
	}

	public String getKmatId() {
		return kmatId;
	}
	public void setKmatId(String kmatId) {
		this.kmatId = kmatId;
	}
	public String getValidityStatus() {
		return validityStatus;
	}
	public void setValidityStatus(String validityStatus) {
		this.validityStatus = validityStatus;
	}
	public String getErrorCode() {
		return errorCode;
	}
	public void setErrorCode(String errorCode) {
		this.errorCode = errorCode;
	}
	public String getErrorMsg() {
		return errorMsg;
	}
	public void setErrorMsg(String errorMsg) {
		this.errorMsg = errorMsg;
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
	public String getUpdtFlag() {
		return updtFlag;
	}
	public void setUpdtFlag(String updtFlag) {
		this.updtFlag = updtFlag;
	}
	public String getConfigDesc() {
		return configDesc;
	}
	public void setConfigDesc(String configDesc) {
		this.configDesc = configDesc;
	}
	public String getDelFlag() {
		return delFlag;
	}
	public void setDelFlag(String delFlag) {
		this.delFlag = delFlag;
	}
	public String getRegionCode() {
		return regionCode;
	}
	public void setRegionCode(String regionCode) {
		this.regionCode = regionCode;
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

	public Timestamp getConfigCntryEol() {
		return configCntryEol;
	}

	public void setConfigCntryEol(Timestamp configCntryEol) {
		this.configCntryEol = configCntryEol;
	}

	//Added setter and getter method for bomFrmt for IPP project.
	public String getBomFrmt(){
	   return bomFrmt;
	}

	public void setBomFrmt(String bomFrmt){
	    this.bomFrmt = bomFrmt;
	}
}
