package com.hp.psg.corona.datachange.plugin.interfaces;

import com.hp.psg.corona.common.plugin.interfaces.AbstractPlugin;
import com.hp.psg.corona.datachange.plugin.helper.CoronaObjHeader;

/**
 * @author dudeja
 * @version 1.0
 *
 */
public class FeaturePluginRequest extends AbstractPlugin {
	private CoronaObjHeader featureBeans;
	public void setFeatureBean(CoronaObjHeader featureBean) {
		this.featureBeans = featureBean;
	}
	public CoronaObjHeader getFeatureBean() {
		return this.featureBeans;
	}
}
