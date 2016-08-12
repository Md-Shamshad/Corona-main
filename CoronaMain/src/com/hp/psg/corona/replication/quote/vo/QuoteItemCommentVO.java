package com.hp.psg.corona.replication.quote.vo;

import java.io.Serializable;
import java.util.Date;

public class QuoteItemCommentVO implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String slsQtnItmSqnNr;
	private Long slsQtnId;
	private String slsQtnVrsnSqnNr;
	private Integer cmntId;
	private String cmntTextMemo;
	private char deleteInd;
	private Date createdTs;
	private String createdBy;
	private Date lastModifiedTs;
	private String lastModifiedBy;
	
	public String getSlsQtnItmSqnNr() {
		return slsQtnItmSqnNr;
	}
	public void setSlsQtnItmSqnNr(String slsQtnItmSqnNr) {
		this.slsQtnItmSqnNr = slsQtnItmSqnNr;
	}
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
	public Integer getCmntId() {
		return cmntId;
	}
	public void setCmntId(Integer cmntId) {
		this.cmntId = cmntId;
	}
	public String getCmntTextMemo() {
		return cmntTextMemo;
	}
	public void setCmntTextMemo(String cmntTextMemo) {
		this.cmntTextMemo = cmntTextMemo;
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
