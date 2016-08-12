package com.hp.psg.corona.datachange.plugin.interfaces;



/**
 * @author dudeja
 * @version 1.0
 * 
 */
public interface ITransformationPlugin extends IFeaturePlugin {
	public FeaturePluginResult transform(FeaturePluginRequest featureBean);
}
