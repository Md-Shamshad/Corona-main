package com.hp.psg.corona.common.cto.beans;

import com.hp.psg.corona.common.util.CoronaFwkUtil;

/**
 * @author dudeja
 * @version 1.0
 *
 */
public abstract class CoronaBaseObject {

	public CoronaBaseObject() {
		// TODO Auto-generated constructor stub
	}

	private String type;

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String toDebugString(CoronaBaseObject cbo) {
		return CoronaFwkUtil.toDebugString(cbo);
	}
	
	public int compareTo(CoronaBaseObject tempobj1, CoronaBaseObject tempobj2) {
		return CoronaFwkUtil.isEqual(tempobj1, tempobj2) ? 0 : -1;
	}
}
