package com.hp.psg.corona.common.beans;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.Date;

import com.hp.psg.corona.common.cto.beans.CoronaBaseObject;

/**
 * @author dudeja
 * @version 1.0
 *
 */
public class ReplicationInfo extends CoronaBaseObject implements Serializable {

	private int id;
	private String source;
	private String objectName;
	private Date lastReplicatedDate;
	private String replicationMethod;
	private Long batchSize;
	private String createdBy;
	private Timestamp createdDate;
	private Timestamp lastModifiedDate;
	private String lastModifiedBy;
	private int permPickSize;
	

	public Long getBatchSize() {
		return batchSize;
	}
	public void setBatchSize(Long batchSize) {
		this.batchSize = batchSize;
	}
	public String getCreatedBy() {
		return createdBy;
	}
	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}
	public Timestamp getCreatedDate() {
		return createdDate;
	}
	public void setCreatedDate(Timestamp createdDate) {
		this.createdDate = createdDate;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getLastModifiedBy() {
		return lastModifiedBy;
	}
	public void setLastModifiedBy(String lastModifiedBy) {
		this.lastModifiedBy = lastModifiedBy;
	}
	public Timestamp getLastModifiedDate() {
		return lastModifiedDate;
	}
	public void setLastModifiedDate(Timestamp lastModifiedDate) {
		this.lastModifiedDate = lastModifiedDate;
	}
	public Date getLastReplicatedDate() {
		return lastReplicatedDate;
	}
	public void setLastReplicatedDate(Date lastReplicatedDate) {
		this.lastReplicatedDate = lastReplicatedDate;
	}
	public String getObjectName() {
		return objectName;
	}
	public void setObjectName(String objectName) {
		this.objectName = objectName;
	}
	public String getReplicationMethod() {
		return replicationMethod;
	}
	public void setReplicationMethod(String replicationMethod) {
		this.replicationMethod = replicationMethod;
	}
	public String getSource() {
		return source;
	}
	public void setSource(String source) {
		this.source = source;
	}
	public int getPermPickSize() {
		return permPickSize;
	}
	public void setPermPickSize(int permPickSize) {
		this.permPickSize = permPickSize;
	}
}
