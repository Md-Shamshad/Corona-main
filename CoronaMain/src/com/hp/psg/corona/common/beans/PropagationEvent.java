package com.hp.psg.corona.common.beans;

import java.io.Serializable;
import java.util.Date;

import com.hp.psg.corona.common.cto.beans.CoronaBaseObject;
import com.hp.psg.corona.common.util.CoronaFwkUtil;

/**
 * @author dudeja
 * @version 1.0
 *
 */
public class PropagationEvent extends CoronaBaseObject
		implements
			Serializable,
			Comparable<CoronaBaseObject> {
	private Long id;
	private String source;
	private Long srcEventId;
	private String processKey;
	private String processType;
	private String processingStatus;
	private Date startDate;
	private Date endDate;
	private Long retryCnt;
	private String errCode;
	private String errMsg;
	private Date createdDate;
	private String createdDatetimestamp;
	private String createdBy;
	private Date lastModifiedDate;
	private String lastModifiedDatetimestamp;
	private String lastModifiedBy;
	private String eventSkipFlag;
	private String comment;
	private String processedBy;
	private Long priority;
	private Long groupId;
	private String groupObject1;

	public String getGroupObject1() {
		return groupObject1;
	}

	public void setGroupObject1(String groupObject1) {
		this.groupObject1 = groupObject1;
	}

	public Long getGroupId() {
		return groupId;
	}

	public void setGroupId(Long groupId) {
		this.groupId = groupId;
	}

	public String getProcessKey() {
		return processKey;
	}

	public void setProcessKey(String processKey) {
		this.processKey = processKey;
	}

	public String getEventSkipFlag() {
		return eventSkipFlag;
	}

	public void setEventSkipFlag(String eventSkipFlag) {
		this.eventSkipFlag = eventSkipFlag;
	}

	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

	public String getProcessedBy() {
		return processedBy;
	}

	public void setProcessedBy(String processedBy) {
		this.processedBy = processedBy;
	}

	public Long getPriority() {
		return priority;
	}

	public void setPriority(Long priority) {
		this.priority = priority;
	}

	public PropagationEvent() {
		// TODO Auto-generated constructor stub
		this.setType("PropagationEvent");
	}

	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getSource() {
		return source;
	}
	public void setSource(String source) {
		this.source = source;
	}
	public Long getSrcEventId() {
		return srcEventId;
	}
	public void setSrcEventId(Long srcEventId) {
		this.srcEventId = srcEventId;
	}
	public String getProcessType() {
		return processType;
	}
	public void setProcessType(String processType) {
		this.processType = processType;
	}
	public String getProcessingStatus() {
		return processingStatus;
	}
	public void setProcessingStatus(String processingStatus) {
		this.processingStatus = processingStatus;
	}
	public Date getStartDate() {
		return startDate;
	}
	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}
	public Date getEndDate() {
		return endDate;
	}
	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}
	public Long getRetryCnt() {
		return retryCnt;
	}
	public void setRetryCnt(Long retryCnt) {
		this.retryCnt = retryCnt;
	}
	public String getErrCode() {
		return errCode;
	}
	public void setErrCode(String errCode) {
		this.errCode = errCode;
	}
	public String getErrMsg() {
		return errMsg;
	}
	public void setErrMsg(String errMsg) {
		this.errMsg = errMsg;
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
	public Date getLastModifiedDate() {
		return lastModifiedDate;
	}
	public void setLastModifiedDate(Date lastModifiedDate) {
		this.lastModifiedDate = lastModifiedDate;
	}
	public String getLastModifiedBy() {
		return lastModifiedBy;
	}
	public void setLastModifiedBy(String lastModifiedBy) {
		this.lastModifiedBy = lastModifiedBy;
	}

	public String getCreatedDatetimestamp() {
		return createdDatetimestamp;
	}

	public void setCreatedDatetimestamp(String createdDatetimestamp) {
		this.createdDatetimestamp = createdDatetimestamp;
	}

	public String getLastModifiedDatetimestamp() {
		return lastModifiedDatetimestamp;
	}

	public void setLastModifiedDatetimestamp(String lastModifiedDatetimestamp) {
		this.lastModifiedDatetimestamp = lastModifiedDatetimestamp;
	}
	public String toString() {
		return super.toDebugString(this);
	}

	public String toDebugString() {
		return CoronaFwkUtil.toDebugString(this);
	}

	public int compareTo(CoronaBaseObject cbo) {
		return super.compareTo(this, cbo);
	}

}
