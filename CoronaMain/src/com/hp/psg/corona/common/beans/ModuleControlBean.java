package com.hp.psg.corona.common.beans;

import java.util.Date;

/**
 * @author dudeja
 * @version 1.0
 * 
 */
public class ModuleControlBean {
	private String moduleId;
	private String moduleExpression;
	private Date date;

	public String getModuleExpression() {
		return this.moduleExpression;
	}

	public void setModuleExpression(String expression) {
		this.moduleExpression = expression;
	}

	public Date getDate() {
		return this.date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public String getModuleId() {
		return moduleId;
	}

	public void setModuleId(String moduleId) {
		this.moduleId = moduleId;
	}
}
