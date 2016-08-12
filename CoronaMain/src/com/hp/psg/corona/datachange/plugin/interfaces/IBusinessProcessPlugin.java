package com.hp.psg.corona.datachange.plugin.interfaces;



/**
 * @author dudeja
 * @version 1.0
 * 
 */
public interface IBusinessProcessPlugin extends IFeaturePlugin {
	public FeaturePluginResult execute(FeaturePluginRequest featureBean);
}
