package com.hp.psg.corona.replication.quote.vo;

import java.io.Serializable;
import java.util.Date;



/**
 * Value object to hold SLS_QTN table data
 * @author rohitc
 *
 */
public class SlsQtnVO implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Long slsQtnID;
	private String description;
	private String name;
	private String custSpcfdQtNr;
	private char bidInd;
	private String bidNr;
	private Date bidTs;
	private char deleteInd;
	private Date createdTs;
	private String createdBy;
	private Date lastModifiedTs;
	private String lastModifiedBy;
	
	public Long getSlsQtnID() {
		return slsQtnID;
	}
	public void setSlsQtnID(Long slsQtnID) {
		this.slsQtnID = slsQtnID;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getCustSpcfdQtNr() {
		return custSpcfdQtNr;
	}
	public void setCustSpcfdQtNr(String custSpcfdQtNr) {
		this.custSpcfdQtNr = custSpcfdQtNr;
	}
	public char getBidInd() {
		return bidInd;
	}
	public void setBidInd(char bidInd) {
		this.bidInd = bidInd;
	}
	public String getBidNr() {
		return bidNr;
	}
	public void setBidNr(String bidNr) {
		this.bidNr = bidNr;
	}
	public Date getBidTs() {
		return bidTs;
	}
	public void setBidTs(Date bidTs) {
		this.bidTs = bidTs;
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
