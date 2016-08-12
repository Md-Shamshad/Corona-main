package com.hp.psg.corona.common.beans;

import java.io.Serializable;

/**
 * @author dudeja
 * @version 1.0
 *
 */
public class EventMappingRuleBean implements Serializable {

	private Long id;
	private String ruleName;
	private String inProcessType;
	private String outProcessType;
	private String mappingSql;

	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getRuleName() {
		return ruleName;
	}
	public void setRuleName(String ruleName) {
		this.ruleName = ruleName;
	}
	public String getInProcessType() {
		return inProcessType;
	}
	public void setInProcessType(String inProcessType) {
		this.inProcessType = inProcessType;
	}
	public String getOutProcessType() {
		return outProcessType;
	}
	public void setOutProcessType(String outProcessType) {
		this.outProcessType = outProcessType;
	}
	public String getMappingSql() {
		return mappingSql;
	}
	public void setMappingSql(String mappingSql) {
		this.mappingSql = mappingSql;
	}

}
