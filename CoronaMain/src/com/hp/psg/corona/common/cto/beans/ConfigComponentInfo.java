package com.hp.psg.corona.common.cto.beans;

import java.io.Serializable;
import java.sql.Timestamp;



/**
 * @author dudeja
 * @version 1.0
 *
 */
public class ConfigComponentInfo extends CoronaBaseObject
		implements
			Serializable,
			Comparable<CoronaBaseObject> {
	private Long id;
	private String bundleId;
	private String shipToCountry;
	private String productId;
	private String pl;
	private String startingPointFlag;
	private Long qty;
	private Timestamp createdDate;
	private String createdBy;
	private Timestamp lastModifiedDate;
	private String lastModifiedBy;
	private String updtFlag;
	private String delFlag;
	private Long trnId;
	private String srcTrnId;
	private String action;

	public ConfigComponentInfo() {
		// TODO Auto-generated constructor stub
		this.setType("ConfigComponentInfo");
	}

	public Long getTrnId() {
		return trnId;
	}
	public void setTrnId(Long trnId) {
		this.trnId = trnId;
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
	public String getShipToCountry() {
		return shipToCountry;
	}
	public void setShipToCountry(String shipToCountry) {
		this.shipToCountry = shipToCountry;
	}
	public String getProductId() {
		return productId;
	}
	public void setProductId(String productId) {
		this.productId = productId;
	}
	public String getPl() {
		return pl;
	}
	public void setPl(String pl) {
		this.pl = pl;
	}
	public String getStartingPointFlag() {
		return startingPointFlag;
	}
	public void setStartingPointFlag(String startingPointFlag) {
		this.startingPointFlag = startingPointFlag;
	}
	public Long getQty() {
		return qty;
	}
	public void setQty(Long qty) {
		this.qty = qty;
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
	public String getDelFlag() {
		return delFlag;
	}
	public void setDelFlag(String delFlag) {
		this.delFlag = delFlag;
	}
	public String toString() {
		return super.toDebugString(this);
	}

	public int compareTo(CoronaBaseObject cbo) {
		return super.compareTo(this, cbo);
	}

}
