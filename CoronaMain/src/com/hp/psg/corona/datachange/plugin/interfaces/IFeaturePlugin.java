package com.hp.psg.corona.datachange.plugin.interfaces;

/**
 * @author dudeja
 * @version 1.0
 *
 */
public interface IFeaturePlugin {
	public String getPluginName();
	public String getPluginOwner();
	public String getPluginRole();
	public String getPluginDescription();
	public int getPluginPriority();
	public String getPluginTypes();

}
