package com.hp.psg.corona.common.cto.beans;

import java.io.Serializable;
import java.sql.Timestamp;


/**
 * @author dudeja
 * @version 1.0
 *
 */
public class ConfigDescription extends CoronaBaseObject
		implements
			Serializable,
			Comparable<CoronaBaseObject> {

	private Long id;
	private String bundleId;
	private String locale;
	private String longDesc;
	private Timestamp createdDate;
	private String createdBy;
	private Timestamp lastModifiedDate;
	private String lastModifiedBy;
	private String updtFlag;
	private String deleteFlag;
	private Long trnId;
	private String action;
	private String shipToCountry;
	private Long srcTrnId;

	public ConfigDescription() {
		// TODO Auto-generated constructor stub
		this.setType("ConfigDescription");
	}

	public Long getSrcTrnId() {
		return srcTrnId;
	}
	public void setSrcTrnId(Long srcTrnId) {
		this.srcTrnId = srcTrnId;
	}
	public String getShipToCountry() {
		return shipToCountry;
	}
	public void setShipToCountry(String shipToCountry) {
		this.shipToCountry = shipToCountry;
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
	public String getBundleId() {
		return bundleId;
	}
	public void setBundleId(String bundleId) {
		this.bundleId = bundleId;
	}
	public String getLocale() {
		return locale;
	}
	public void setLocale(String locale) {
		this.locale = locale;
	}
	public String getLongDesc() {
		return longDesc;
	}
	public void setLongDesc(String longDesc) {
		this.longDesc = longDesc;
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
	public String getDeleteFlag() {
		return deleteFlag;
	}
	public void setDeleteFlag(String deleteFlag) {
		this.deleteFlag = deleteFlag;
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

}
