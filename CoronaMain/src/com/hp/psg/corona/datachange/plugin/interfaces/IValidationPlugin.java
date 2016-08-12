package com.hp.psg.corona.datachange.plugin.interfaces;



/**
 * @author dudeja
 * @version 1.0
 * 
 */
public interface IValidationPlugin extends IFeaturePlugin {

	public FeaturePluginResult validate(FeaturePluginRequest featureBean);
}
