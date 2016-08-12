package com.hp.psg.corona.datachange.cto.plugin.impl;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.StringReader;
import java.util.Iterator;
import java.util.List;

import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;
import org.jdom.output.XMLOutputter;

import com.hp.psg.corona.common.cto.beans.CoronaBaseObject;
import com.hp.psg.corona.common.cto.beans.PriceInfo;
import com.hp.psg.corona.datachange.plugin.helper.ConfigInfoTransformationPluginHelperBean;
import com.hp.psg.corona.datachange.plugin.helper.CoronaObjHeader;
import com.hp.psg.corona.datachange.plugin.helper.CoronaObjectWrapper;
import com.hp.psg.corona.datachange.plugin.interfaces.FeaturePluginRequest;
import com.hp.psg.corona.datachange.plugin.interfaces.FeaturePluginResult;
import com.hp.psg.corona.datachange.plugin.interfaces.ITransformationPlugin;
import com.hp.psg.corona.error.util.CoronaErrorHandler;


public class ConfigInfoTransformPlugin_SAX implements ITransformationPlugin {

	public static final String BASE_NET_PRICE = "baseNetPrice";
	public static final String OPTION_NET_PRICE = "optionNetPrice";
	public static final String PRICE_SOURCE_TYPE_ER = "ER";
	public static final String NONE = "none";
	public static final String TRANSFORM_DATA_TYPE = "PRICE_INFO";
	public static final String ID_ATTRIBUTE = "id";

	public static final String PRICE_SOURCE = "priceSource";
	public static final String DEAL_NUMBER = "dealNumber";
	public static final String AGREEMENT = "agreement";
	public static final String TIER_ID = "tierId";
	public static final String DEAL_TYPE = "dealType";

	private String pluginName = "IPCS Transformation";
	private String pluginOwner = "IPCS";
	private String pluginDescription = "Transformation WherFrom data from PRS";
	private int pluginPriority = 0;
	private String pluginRole = "Helper";
	private String pluginType = "ConfigInfoTransformPlugin";

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
		for (CoronaObjectWrapper header : coronaWrapperList) {
			Iterator<String> objectNameIterator = header.getAllCoronaObjName();
			// Getting all the keys from iterator and checking the list values
			// of the keys
			// are price info objects
			while (objectNameIterator.hasNext()) {
				String objectName = "";
				objectName = objectNameIterator.next();
				if ((objectName != null) && !("".equals(objectName))) {
					List<CoronaBaseObject> coronaObjectsList = (List<CoronaBaseObject>) header
							.GetCoronaObj(objectName);
					if (coronaObjectsList != null) {
						for (CoronaBaseObject coronaBaseObject : coronaObjectsList) {
							if (coronaBaseObject instanceof PriceInfo) {
								PriceInfo priceInfoBean = (PriceInfo) coronaBaseObject;
								if (priceInfoBean != null) {
									String tempWhereFromConfigXML = "";
									if ((priceInfoBean.getWhereFromInfo() != null)
											&& !("".equals(priceInfoBean
													.getWhereFromInfo()))) {
										priceInfoBean.setWhereFromConfig(priceInfoBean.getWhereFromInfo());
										try {
											tempWhereFromConfigXML = processXMLDocument(priceInfoBean);
										} catch (Exception ex) {
											CoronaErrorHandler.logError(ex,null,null);
										} 
										priceInfoBean.setWhereFromConfig(tempWhereFromConfigXML);
										//Logger.debug("Output XML String: \n" + tempWhereFromConfigXML);
									} // if condition
									// copying the modified request bean to
									// result bean

								} // if condition - priceInfoBean !=null

							}// end of if condition - PriceInfo
						} // end of for loop - CoronaBaseObject

					} // end of if
				} // end of if
			} // end of while

		}// end of for loop: CoronaObjectWrapper
		coronaObjHeaderPluginResult.setHeaders(coronaWrapperList);
		// Wrapping the price info beans into result objects
		featurePluginResult.setFeatureBean(coronaObjHeaderPluginResult);
		return featurePluginResult;
	}

	public static Element extractElementNodeByName(String xmlString, String tagName) throws JDOMException, IOException {
	    Element elementNode = null;
	    if (xmlString != null) {
	      StringReader reader = new StringReader(xmlString);
	      SAXBuilder saxBuilder = new SAXBuilder();
	      Document doc = saxBuilder.build(reader);
	      if (doc != null) {
	        Element root = doc.getRootElement();
	        if (root != null) {
	          elementNode = root.getChild(tagName);
	        }
	      }
	    }
	    return elementNode;
	  }
	
	public String processXMLDocument(PriceInfo priceInfoBean)
			throws Exception{

		String whereFromString = priceInfoBean.getWhereFromInfo();
		String output = null;
		if(whereFromString == null ||whereFromString.trim().length()==0){
			return output;
		}
		ConfigInfoTransformationPluginHelperBean helperBean = new ConfigInfoTransformationPluginHelperBean();


		try{
			Element optionNetPriceElement = null;
			StringReader reader = new StringReader(whereFromString);
		    SAXBuilder saxBuilder = new SAXBuilder();
		    Element root = null;
		    Document doc = saxBuilder.build(reader);
		    reader.close();
		 
		    if (doc != null) {
		        root = doc.getRootElement();
		        if (root != null) {
		        	optionNetPriceElement = root.getChild(OPTION_NET_PRICE);
		        }
		     }
			
			String priceSelected = "";
			if ((priceInfoBean.getProductOptionCd() != null)
					&& !("".equals(priceInfoBean.getProductOptionCd()))) {
				if (optionNetPriceElement != null) {
					if (priceInfoBean.getGenericPriceFlag()
							.equalsIgnoreCase("Y")
							|| priceInfoBean.getPriceId().equalsIgnoreCase(NONE)) {
						priceSelected = OPTION_NET_PRICE;
						parseXML(root, priceSelected, helperBean);
					} else { // parse optionNetPrice only if the price is not ER.
						if (!priceInfoBean.getPriceSourceOption().equalsIgnoreCase(PRICE_SOURCE_TYPE_ER)) {
							if (!priceInfoBean.getPriceSourceBase().equalsIgnoreCase(PRICE_SOURCE_TYPE_ER)) {
								priceSelected = BASE_NET_PRICE;
								parseXML(root, priceSelected, helperBean);
							}else{
								root.removeContent();			//case: additionalData = "";
								helperBean.setPriceSource(PRICE_SOURCE_TYPE_ER);
								root = null;
							}
						} else { // priceSourceBase = "ER";
							String priceSource = PRICE_SOURCE_TYPE_ER;
							priceSelected = OPTION_NET_PRICE;
							parseXML(root, priceSelected, helperBean);
							helperBean.setPriceSource(priceSource);
						}
					}
				}
			} else {
				priceSelected = BASE_NET_PRICE;
				parseXML(root, priceSelected, helperBean);
			}
	
			priceInfoBean.setPriceSource(helperBean.getPriceSource());
			priceInfoBean.setDealNumber(helperBean.getDealNumber());
			priceInfoBean.setAgreement(helperBean.getAgreement());
			priceInfoBean.setTierId(helperBean.getTierId());
			priceInfoBean.setDealType(helperBean.getDealType());
			
			
			if(root!=null){
				XMLOutputter outputter = new XMLOutputter();
			    ByteArrayOutputStream out = new ByteArrayOutputStream();
			    outputter.output(root.getDocument(), out);
			    output = out.toString();
			    out.close();
			}
		}catch (Exception e){
			CoronaErrorHandler.logError(e,null,null);
		}
  		
		return output;
	}
	


	public void parseXML(Element root, String priceSelected,
			ConfigInfoTransformationPluginHelperBean helperBean) {


		Element elementToCopy = root.getChild(priceSelected);
		elementToCopy.detach();
		List nodesList = elementToCopy.getChildren();
		root.removeContent();
		// Looping through the selected element and checking for the
		// fields
		if (nodesList != null && nodesList.size() > 0) {
			int numberElement = nodesList.size();
		      for(int i=numberElement-1; i>-1;i--){
					Element headerChildNode = (Element) nodesList.get(i);

					String attributeName = headerChildNode.getAttributeValue(ID_ATTRIBUTE);
					String tagValue = headerChildNode.getValue();

					if (attributeName != null || "".equals(attributeName)) {
						if (tagValue != null || "".equals(tagValue)) {
							helperBean = checkForFields(attributeName,
									tagValue, helperBean);
						}
					}

					headerChildNode.detach();
					root.addContent(0,headerChildNode);
				}
			}
	}

	public static ConfigInfoTransformationPluginHelperBean checkForFields(String tagName,
			String tagValue, ConfigInfoTransformationPluginHelperBean helperBean) {
		if (tagName.equalsIgnoreCase(PRICE_SOURCE)) {
			helperBean.setPriceSource(tagValue);
		}
		if (tagName.equalsIgnoreCase(DEAL_NUMBER)) {
			helperBean.setDealNumber(tagValue);
		}
		if (tagName.equalsIgnoreCase(AGREEMENT)) {
			helperBean.setAgreement(tagValue);
		}
		if (tagName.equalsIgnoreCase(TIER_ID)) {
			helperBean.setTierId(tagValue);
		}
		if (tagName.equalsIgnoreCase(DEAL_TYPE)) {
			helperBean.setDealType(tagValue);
		}
		return helperBean;
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

}
