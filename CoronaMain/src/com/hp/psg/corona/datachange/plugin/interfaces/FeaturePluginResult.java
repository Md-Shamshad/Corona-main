package com.hp.psg.corona.datachange.plugin.interfaces;

import java.util.HashMap;
import java.util.Iterator;

import com.hp.psg.corona.common.plugin.interfaces.AbstractPlugin;
import com.hp.psg.corona.datachange.plugin.helper.CoronaObjHeader;

/**
 * @author dudeja
 * @version 1.0
 *
 */
public class FeaturePluginResult extends AbstractPlugin {

	private HashMap<String, String> errorCodes;
	private HashMap<String, String> warningCodes;
	private HashMap<String, String> infoMessages;
	private CoronaObjHeader featureBeans;

	public FeaturePluginResult() {
		errorCodes = new HashMap<String, String>();
		infoMessages = new HashMap<String, String>();
		warningCodes = new HashMap<String, String>();

	}
	public void setFeatureBean(CoronaObjHeader featureBean) {
		this.featureBeans = featureBean;
	}
	public CoronaObjHeader getFeatureBean() {
		return this.featureBeans;
	}

	public void setWarnings(String warningCode, String message) {
		if (warningCodes != null) {
			warningCodes.put(warningCode, message);
		}
	}
	public Iterator<String> getWarningCodes() {
		if (warningCodes != null)
			return warningCodes.keySet().iterator();
		return null;
	}

	public void setInfoMessages(String infoCode, String message) {
		if (infoMessages != null) {
			infoMessages.put(infoCode, message);
		}
	}
	public Iterator<String> getInfoMessages() {
		if (infoMessages != null)
			return infoMessages.keySet().iterator();
		return null;
	}

	public void setError(String errorCode, String message) {
		if (errorCodes != null) {
			errorCodes.put(errorCode, message);
		}
	}
	public Iterator<String> getErrorCodes() {
		if (errorCodes != null)
			return errorCodes.keySet().iterator();
		return null;
	}
}
