package com.hp.psg.corona.datachange.cto.plugin.impl;

import java.util.Iterator;
import java.util.List;

import com.hp.psg.common.util.logging.LoggerInfo;
import com.hp.psg.corona.common.beans.CTODaxDataBeanGeneral;
import com.hp.psg.corona.common.cto.beans.ConfigPriceHeaderInfo;
import com.hp.psg.corona.common.cto.beans.CoronaBaseObject;
import com.hp.psg.corona.common.cto.beans.PriceInfo;
import com.hp.psg.corona.common.util.CoronaFwkUtil;
import com.hp.psg.corona.common.util.CoronaObjectWrapperUtil;
import com.hp.psg.corona.common.util.Logger;
import com.hp.psg.corona.datachange.plugin.helper.CoronaObjHeader;
import com.hp.psg.corona.datachange.plugin.helper.CoronaObjectWrapper;
import com.hp.psg.corona.datachange.plugin.interfaces.FeaturePluginRequest;
import com.hp.psg.corona.datachange.plugin.interfaces.FeaturePluginResult;
import com.hp.psg.corona.datachange.plugin.interfaces.ITransformationPlugin;
import com.hp.psg.corona.propagation.handler.util.ConfigDataChangeUtil;

/**
 * @author sjoshi
 * @version 1.0
 *
 */
public class PriceInfoTransformPlugin implements ITransformationPlugin {

	private String pluginName = "IPCS Transformation";
	private String pluginOwner = "IPCS";
	private String pluginDescription = "Transform Country, PriceDesc and ProductId data from PRS";
	private int pluginPriority = 0;
	private String pluginRole = "Helper";
	private String pluginType = "PriceInfoTransformPlugin";
	LoggerInfo logInfo=null;
	
	public PriceInfoTransformPlugin() {
		// TODO Auto-generated constructor stub
		logInfo = new LoggerInfo (this.getClass().getName());
	}

	public FeaturePluginResult transform(
			FeaturePluginRequest featurePluginRequest) {

		FeaturePluginRequest featureBeans = featurePluginRequest;
		// FeaturePluginResult configPluginResult = new FeaturePluginResult();
		FeaturePluginResult featurePluginResult = new FeaturePluginResult();
		// Corona Object Header for ConfigPluginResult
		CoronaObjHeader coronaObjHeaderPluginResult = new CoronaObjHeader();
		// ConfigPluginRequest
		CoronaObjHeader coronaObjectHeader = featureBeans.getFeatureBean();
		List<CoronaObjectWrapper> coronaWrapperList = coronaObjectHeader
				.getHeaders();

		Logger.info(logInfo, "transform", " coronaWrapperList size" + coronaWrapperList.size());

		if (coronaWrapperList != null && coronaWrapperList.size() > 0) {
			for (CoronaObjectWrapper header : coronaWrapperList) {
				Iterator<String> objectNameIterator = header
						.getAllCoronaObjName();
				// Getting all the keys from iterator and checking the list
				// values
				// of the keys
				// are price info objects
				while (objectNameIterator.hasNext()) {
					String objectName = "";
					objectName = objectNameIterator.next();
					if ((objectName != null) && !("".equals(objectName))) {
						List<? extends CoronaBaseObject> coronaObjectsList = (List<? extends CoronaBaseObject>) header
								.GetCoronaObj(objectName);
						if (coronaObjectsList != null
								&& coronaObjectsList.size() > 0) {
							for (CoronaBaseObject coronaBaseObject : coronaObjectsList) {
								if (coronaBaseObject instanceof PriceInfo) {
									PriceInfo priceInfoBean = (PriceInfo) coronaBaseObject;
									if (priceInfoBean != null) {
										// transform PriceInfo bean

										priceInfoBean
												.setProductId(ConfigDataChangeUtil
														.getProductIdFromProductNumberAndOptionCd(
																(priceInfoBean
																		.getProductNumber() == null
																		? ""
																		: priceInfoBean
																				.getProductNumber()),
																(priceInfoBean
																		.getProductOptionCd() == null
																		? ""
																		: priceInfoBean
																				.getProductOptionCd())));

//										Logger.info(logInfo, "transform", " priceInfoBean.getProductId() "
//														+ priceInfoBean
//																.getProductId());

										
									} // if condition - priceInfoBean !=null
								}// end of if condition - PriceInfo
								if (coronaBaseObject instanceof ConfigPriceHeaderInfo) {
									ConfigPriceHeaderInfo priceHeaderInfoBean = (ConfigPriceHeaderInfo) coronaBaseObject;
									if (priceHeaderInfoBean != null) {

										priceHeaderInfoBean
												.setShipToCountry(ConfigDataChangeUtil
														.getGpsyCountryFromPRSCountry((priceHeaderInfoBean
																.getShipToGeo() == null
																? ""
																: priceHeaderInfoBean
																		.getShipToGeo())));

//										Logger.info(logInfo, "transform", " priceInfoBean.getPriceGeo() "
//														+ priceHeaderInfoBean
//																.getPriceGeo()
//														+ " priceInfoBean.getCurrency() "
//														+ priceHeaderInfoBean
//																.getCurrency()
//														+ " priceInfoBean.getIncoTerm() "
//														+ priceHeaderInfoBean
//																.getIncoTerm()
//														+ " priceInfoBean.getPriceListType() "
//														+ priceHeaderInfoBean
//																.getPriceListType()
//														+ " priceInfoBean.getShipToGeo() "
//														+ priceHeaderInfoBean
//																.getShipToGeo());

										priceHeaderInfoBean
												.setPriceDescriptor(ConfigDataChangeUtil
														.getPriceDescriptorFromPRSParams(
																(priceHeaderInfoBean
																		.getPriceGeo() == null
																		? ""
																		: priceHeaderInfoBean
																				.getPriceGeo()),
																(priceHeaderInfoBean
																		.getCurrency() == null
																		? ""
																		: priceHeaderInfoBean
																				.getCurrency()),
																(priceHeaderInfoBean
																		.getIncoTerm() == null
																		? ""
																		: priceHeaderInfoBean
																				.getIncoTerm()),
																(priceHeaderInfoBean
																		.getPriceListType() == null
																		? ""
																		: priceHeaderInfoBean
																				.getPriceListType())));

									}
								}
							} // end of for loop - CoronaBaseObject
						} // end of if
					} // end of if
				} // end of while

			}// end of for loop: CoronaObjectWrapper
		}
		coronaObjHeaderPluginResult.setHeaders(coronaWrapperList);

		// Wrapping the price info beans into result objects
		featurePluginResult.setFeatureBean(coronaObjHeaderPluginResult);
		return featurePluginResult;
	}

	public String getPluginDescription() {

		return pluginDescription;
	}

	public String getPluginName() {

		return pluginName;
	}

	public String getPluginOwner() {

		return pluginOwner;
	}

	public int getPluginPriority() {

		return pluginPriority;
	}

	public String getPluginRole() {

		return pluginRole;
	}

	public String getPluginTypes() {
		return pluginType;
	}

	public void setPluginDescription(String pluginDescription) {
		this.pluginDescription = pluginDescription;
	}

	public void setPluginName(String pluginName) {
		this.pluginName = pluginName;
	}

	public void setPluginOwner(String pluginOwner) {
		this.pluginOwner = pluginOwner;
	}

	public void setPluginPriority(int pluginPriority) {
		this.pluginPriority = pluginPriority;
	}

	public void setPluginRole(String pluginRole) {
		this.pluginRole = pluginRole;
	}

	public void setPluginTypes(String pluginType) {
		this.pluginType = pluginType;
	}

	public static void main(String[] args) {
		CTODaxDataBeanGeneral generalBean = new CTODaxDataBeanGeneral();
		PriceInfo[] priceInfo = new PriceInfo[10];
		for (int i = 0; i < 10; i++) {
			priceInfo[i] = new PriceInfo();
			priceInfo[i].setBundleId("bundle" + i);
			priceInfo[i].setDelFlag("N");
			priceInfo[i].setUpdtFlag("A");

			priceInfo[i].setCurrency("Curr" + i);
			priceInfo[i].setIncoTerm("Inco" + i);
			priceInfo[i].setShipToCountry("stc" + i);
			priceInfo[i].setPriceListType("PriceList" + i);
			priceInfo[i].setProductNumber("ProductNumber" + i);
			priceInfo[i].setProductOptionCd("optionCd" + i);
			priceInfo[i].setPriceGeo("priceGeo" + i);
			priceInfo[i].setShipToGeo("shipToGeo" + i);
		}
		generalBean.setPriceInfo(priceInfo);

		PriceInfoTransformPlugin pi = new PriceInfoTransformPlugin();
		FeaturePluginRequest fpr = CoronaFwkUtil
				.getFeaturePluginRequestFromCTOBean(generalBean);

		System.out.println("Request :::::::::::::::::::::::::");
		CoronaObjectWrapperUtil.prettyPrint(fpr);
		FeaturePluginResult response = pi.transform(fpr);
		System.out.println("Response ::::::::::::	:::::::::::::");
		CoronaObjectWrapperUtil.prettyPrint(response);
	}
}
