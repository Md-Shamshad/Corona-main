package com.hp.psg.corona.datachange.cto.plugin.impl;

import java.util.Iterator;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.xmlbeans.XmlCursor;
import org.apache.xmlbeans.XmlException;
import org.apache.xmlbeans.XmlObject;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.output.XMLOutputter;

import com.hp.psg.corona.common.cto.beans.CoronaBaseObject;
import com.hp.psg.corona.common.cto.beans.PriceInfo;
import com.hp.psg.corona.datachange.plugin.helper.ConfigInfoTransformationPluginHelperBean;
import com.hp.psg.corona.datachange.plugin.helper.CoronaObjHeader;
import com.hp.psg.corona.datachange.plugin.helper.CoronaObjectWrapper;
import com.hp.psg.corona.datachange.plugin.interfaces.FeaturePluginRequest;
import com.hp.psg.corona.datachange.plugin.interfaces.FeaturePluginResult;
import com.hp.psg.corona.datachange.plugin.interfaces.ITransformationPlugin;

import coronaxmlbeans.headers.BaseNetPrice;
import coronaxmlbeans.headers.HeadersDocument;
import coronaxmlbeans.headers.OptionNetPrice;

public class ConfigInfoTransformPlugin implements ITransformationPlugin {

	public static final String BASE_NET_PRICE = "BaseNetPrice";
	public static final String OPTION_NET_PRICE = "OptionNetPrice";
	public static final String PRICE_SOURCE_TYPE_ER = "ER";
	public static final String NONE = "none";
	public static final String TRANSFORM_DATA_TYPE = "PRICE_INFO";
	public static final String REGEX = "headers";
	public static final String HEADERS_ELEMENT = "headers";
	public static final String HEADER_ELEMENT = "header";
	public static final String ID_ATTRIBUTE = "id";
	public static final String HEADER_XML_NAMESPACE = "headers xmlns=\"http://coronaxmlbeans/headers\"";

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
		System.out.println("Transform ...");
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
			System.out.println("coronaWrapperList.size = "+coronaWrapperList.size());
			while (objectNameIterator.hasNext()) {
				String objectName = "";
				objectName = objectNameIterator.next();
				if ((objectName != null) && !("".equals(objectName))) {
					List<CoronaBaseObject> coronaObjectsList = (List<CoronaBaseObject>) header
							.GetCoronaObj(objectName);
					
					if (coronaObjectsList != null) {
						System.out.println("coronaObjectsList size = "+coronaObjectsList.size());
						System.out.println("Object in the corona list"+coronaObjectsList.get(0).getType());
						for (CoronaBaseObject coronaBaseObject : coronaObjectsList) {
							if (coronaBaseObject instanceof PriceInfo) {
								
								PriceInfo priceInfoBean = (PriceInfo) coronaBaseObject;
								if (priceInfoBean != null) {
									String whereFromInfoXML = "";
									String tempWhereFromInfoXML = "";
									if ((priceInfoBean.getWhereFromInfo() != null)
											&& !("".equals(priceInfoBean
													.getWhereFromInfo()))) {
										whereFromInfoXML = priceInfoBean
												.getWhereFromInfo();
										// In the headerXML we do not have
										// namespace specified, so the logic
										// below
										// adds the namespace to the headers
										// root element. If namespace is not
										// specified
										// parser will fail.
										Pattern headersPattern = Pattern
												.compile(REGEX);
										// Xml string is checked if a
										// "headers element" match is present.
										// If a match is found then headers
										// element is replaced with headers
										// element having namespace.
										Matcher match = headersPattern
												.matcher(whereFromInfoXML);
										String modifiedWhereFromXML = "";
										if (match != null) {
											modifiedWhereFromXML = match
													.replaceFirst(HEADER_XML_NAMESPACE);
										} else {
											modifiedWhereFromXML = whereFromInfoXML;
										}
										priceInfoBean.setWhereFromConfig(modifiedWhereFromXML);
										tempWhereFromInfoXML = processXMLDocument(priceInfoBean);
										priceInfoBean.setWhereFromConfig(tempWhereFromInfoXML);

										System.out.println("Output XML String"+ tempWhereFromInfoXML);
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

	public String processXMLDocument(PriceInfo priceInfoBean) {
		HeadersDocument poDoc = null;
		String headerXML = "";
		try {
			// Bind the incoming XML to an XMLBeans type.
			poDoc = HeadersDocument.Factory.parse(priceInfoBean
					.getWhereFromConfig());
		} catch (XmlException e) {
			Class cls = org.apache.xmlbeans.XmlBeans.class;
			//CodeSource cSource = cls.getProtectionDomain().getCodeSource();
			java.net.URL loc = cls.getProtectionDomain().getCodeSource().getLocation(); 
			System.out.println("The xml bean jar file is :"+loc); 
			e.printStackTrace();
		}
		ConfigInfoTransformationPluginHelperBean helperBean = new ConfigInfoTransformationPluginHelperBean();
		// write the business logic to select base net price or option net price
		BaseNetPrice basenetprice = poDoc.getHeaders().getBaseNetPrice();
		OptionNetPrice optionnetprice = poDoc.getHeaders().getOptionNetPrice();

		String priceSelected = "";

		if ((priceInfoBean.getProductOptionCd() != null)
				&& !("".equals(priceInfoBean.getProductOptionCd()))) {
			if (optionnetprice != null) {
				if (priceInfoBean.getGenericPriceFlag()
						.equalsIgnoreCase("Y")
						|| priceInfoBean.getPriceId().equalsIgnoreCase(NONE)) {
					priceSelected = OPTION_NET_PRICE;
					headerXML = parseXML(poDoc, priceSelected, helperBean);
				} else { // parse optionNetPrice only if the price is not ER.
					if (!priceInfoBean.getPriceSourceOption().equalsIgnoreCase(
							PRICE_SOURCE_TYPE_ER)) {
						if (!priceInfoBean.getPriceSourceBase()
								.equalsIgnoreCase(PRICE_SOURCE_TYPE_ER)) {
							priceSelected = BASE_NET_PRICE;
							headerXML = parseXML(poDoc, priceSelected,
									helperBean);
						}
					} else { // priceSourceBase = "ER";
						priceSelected = OPTION_NET_PRICE;
						headerXML = parseXML(poDoc, priceSelected, helperBean);
					}
				}
			}
		} else {
			priceSelected = BASE_NET_PRICE;
			headerXML = parseXML(poDoc, priceSelected, helperBean);
		}

		priceInfoBean.setPriceSource(helperBean.getPriceSource());
		priceInfoBean.setDealNumber(helperBean.getDealNumber());
		priceInfoBean.setAgreement(helperBean.getAgreement());
		priceInfoBean.setTierId(helperBean.getTierId());
		priceInfoBean.setDealType(helperBean.getDealType());

		return headerXML;
	}

	private static String parseXML(HeadersDocument poDoc, String priceSelected,
			ConfigInfoTransformationPluginHelperBean helperBean) {
		String queryString = "$this/*";
		String headerXml = null;
		XmlObject[] xmlBeanArray = null;
		if (priceSelected.equalsIgnoreCase("BaseNetPrice")) {
			BaseNetPrice basenetprice = poDoc.getHeaders().getBaseNetPrice();
			xmlBeanArray = basenetprice.selectPath(queryString);
		} else {
			OptionNetPrice optionnetprice = poDoc.getHeaders()
					.getOptionNetPrice();
			xmlBeanArray = optionnetprice.selectPath(queryString);
		}

		if (xmlBeanArray != null) {
			Element rootElement = new Element(HEADERS_ELEMENT);
			Document doc = new Document(rootElement);
			XmlCursor cursor = null;

			for (int i = 0; i < xmlBeanArray.length; i++) {
				cursor = xmlBeanArray[i].newCursor();
				String tagName = null;
				String tagValue = null;
				Element header = null;
				// tagValue: returns the text value of the element
				tagValue = cursor.getTextValue();
				cursor.toFirstAttribute();
				// tagName: returns the text value of the attribute
				tagName = cursor.getTextValue();
				// add any element you get but not price
				header = new Element(HEADER_ELEMENT);
				header.setAttribute(ID_ATTRIBUTE, tagName);
				header.setText(tagValue);

				if (tagName != null || "".equals(tagName)) {
					if (tagValue != null || "".equals(tagValue)) {
						helperBean = checkForFields(tagName, tagValue,
								helperBean);
					}
				}
				rootElement.addContent(header);
			}

			XMLOutputter output = new XMLOutputter();
			headerXml = output.outputString(doc);
			// System.out.println("ROOT ELEMENT " + headerXml.toString());
			output = null;
		}
		return headerXml.toString();
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
