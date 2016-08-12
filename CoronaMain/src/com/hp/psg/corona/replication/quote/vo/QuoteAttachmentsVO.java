package com.hp.psg.corona.replication.quote.vo;

import java.io.Serializable;
import java.util.Date;

public class QuoteAttachmentsVO implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Long slsQtnId;
	private String slsQtnVrsnSqnNr;
	private Long attachNr;
	private String fileNm;
	private String docDesc;
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
	public Long getAttachNr() {
		return attachNr;
	}
	public void setAttachNr(Long attachNr) {
		this.attachNr = attachNr;
	}
	public String getFileNm() {
		return fileNm;
	}
	public void setFileNm(String fileNm) {
		this.fileNm = fileNm;
	}
	public String getDocDesc() {
		return docDesc;
	}
	public void setDocDesc(String docDesc) {
		this.docDesc = docDesc;
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
