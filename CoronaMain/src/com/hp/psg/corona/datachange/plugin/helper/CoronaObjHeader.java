package com.hp.psg.corona.datachange.plugin.helper;

import java.util.List;

/**
 * @author dudeja
 * @version 1.0
 *
 */
public class CoronaObjHeader {
	private List<CoronaObjectWrapper> headers;

	public List<CoronaObjectWrapper> getHeaders() {
		return headers;
	}

	public void setHeaders(List<CoronaObjectWrapper> headers) {
		this.headers = headers;
	}

	public void addToList(CoronaObjectWrapper cow) {
		headers.add(cow);
	}
}
