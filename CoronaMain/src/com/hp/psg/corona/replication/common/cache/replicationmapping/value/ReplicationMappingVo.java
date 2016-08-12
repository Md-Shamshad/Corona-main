package com.hp.psg.corona.replication.common.cache.replicationmapping.value;

import java.io.Serializable;
import java.util.Date;

public class ReplicationMappingVo implements Serializable {	
	
	private static final long serialVersionUID = 1L;
	private String processType = null;
	private String sourceTable = null;
    private String destinationTable = null;
    private String processingOrder = null;
    private String comments = null;
    private Date lastRunDate = null;
    private String createdBy = null;
    private Date createdDate = null;
    private String lastModifiedBy = null;
    private Date lastModifiedDate = null;
    private long lastRunEventId = -1;
	 
	public String getProcessType() {
		return processType;
	}
	public void setProcessType(String processType) {
		this.processType = processType;
	}
	public String getSourceTable() {
		return sourceTable;
	}
	public void setSourceTable(String sourceTable) {
		this.sourceTable = sourceTable;
	}
	public String getDestinationTable() {
		return destinationTable;
	}
	public void setDestinationTable(String destinationTable) {
		this.destinationTable = destinationTable;
	}
	public String getProcessingOrder() {
		return processingOrder;
	}
	public void setProcessingOrder(String processingOrder) {
		this.processingOrder = processingOrder;
	}
	public String getComments() {
		return comments;
	}
	public void setComments(String comments) {
		this.comments = comments;
	}
	public Date getLastRunDate() {
		return lastRunDate;
	}
	public void setLastRunDate(Date lastRunDate) {
		this.lastRunDate = lastRunDate;
	}
	public String getCreatedBy() {
		return createdBy;
	}
	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}
	public Date getCreatedDate() {
		return createdDate;
	}
	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}
	public String getLastModifiedBy() {
		return lastModifiedBy;
	}
	public void setLastModifiedBy(String lastModifiedBy) {
		this.lastModifiedBy = lastModifiedBy;
	}
	public Date getLastModifiedDate() {
		return lastModifiedDate;
	}
	public void setLastModifiedDate(Date lastModifiedDate) {
		this.lastModifiedDate = lastModifiedDate;
	}
	public long getLastRunEventId() {
		return lastRunEventId;
	}
	public void setLastRunEventId(long lastRunEventId) {
		this.lastRunEventId = lastRunEventId;
	}	
	

}//class
