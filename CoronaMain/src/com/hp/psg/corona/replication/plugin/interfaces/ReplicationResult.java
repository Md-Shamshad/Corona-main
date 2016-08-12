package com.hp.psg.corona.replication.plugin.interfaces;

import java.util.List;

import com.hp.psg.corona.common.plugin.interfaces.AbstractPlugin;

/**
 * @author dudeja
 * @version 1.0
 *
 */
public class ReplicationResult extends AbstractPlugin {

	private String status;
	private String message;
	private Long numberOfRecordsProcessed;
	private List<Long> errorList; // I will think it of header ids for error list

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public Long getNumberOfRecordsProcessed() {
		return numberOfRecordsProcessed;
	}

	public void setNumberOfRecordsProcessed(Long numberOfRecordsProcessed) {
		this.numberOfRecordsProcessed = numberOfRecordsProcessed;
	}

	public List<Long> getErrorList() {
		return errorList;
	}

	public void setErrorList(List<Long> errorList) {
		this.errorList = errorList;
	}
}
