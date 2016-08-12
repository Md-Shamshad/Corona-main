package com.hp.psg.corona.replication.beans;

import java.io.Serializable;
import java.util.Date;

import com.hp.psg.corona.common.cto.beans.CoronaBaseObject;

public class ConfigPriceReplication extends CoronaBaseObject
			implements
				Serializable,
				Comparable<CoronaBaseObject> {

	private Long cphId;
	private String groupId;
	private String status;
	private Date cphiLastModifiedDate;
	private String cphiLastModifiedDateStr;
	private Date createdDate;
	private String createdBy;
	private Date lastModifiedDate;
	private String lastModifiedDateStr;
	private String lastModifiedBy;
	private String processedBy;
	private String cprLastRunDate;


	public String getCprLastRunDate() {
		return cprLastRunDate;
	}

	public void setCprLastRunDate(String cprLastRunDate) {
		this.cprLastRunDate = cprLastRunDate;
	}

	public String toString() {
		return super.toDebugString(this);
	}
	
	public int compareTo(CoronaBaseObject cbo) {
		return super.compareTo(this, cbo);
	}

	public String getGroupId() {
		return groupId;
	}

	public void setGroupId(String groupId) {
		this.groupId = groupId;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public Date getTimestamp() {
		return this.getCphiLastModifiedDate();		
	}

	public void setTimestamp(Date timestamp) {
		this.setCphiLastModifiedDate(timestamp);
	}

	public Long getCphId() {
		return cphId;
	}

	public void setCphId(Long cphId) {
		this.cphId = cphId;
	}

	public Date getCphiLastModifiedDate() {
		return cphiLastModifiedDate;
	}

	public void setCphiLastModifiedDate(Date cphiLastModifiedDate) {
		this.cphiLastModifiedDate = cphiLastModifiedDate;
	}

	public Date getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
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

	public String getProcessedBy() {
		return processedBy;
	}

	public void setProcessedBy(String processedBy) {
		this.processedBy = processedBy;
	}

	public String getCphiLastModifiedDateStr() {
		return cphiLastModifiedDateStr;
	}

	public void setCphiLastModifiedDateStr(String cphiLastModifiedDateStr) {
		this.cphiLastModifiedDateStr = cphiLastModifiedDateStr;
	}

	public Date getLastModifiedDate() {
		return lastModifiedDate;
	}

	public void setLastModifiedDate(Date lastModifiedDate) {
		this.lastModifiedDate = lastModifiedDate;
	}

	public String getLastModifiedDateStr() {
		return lastModifiedDateStr;
	}

	public void setLastModifiedDateStr(String lastModifiedDateStr) {
		this.lastModifiedDateStr = lastModifiedDateStr;
	}

}
