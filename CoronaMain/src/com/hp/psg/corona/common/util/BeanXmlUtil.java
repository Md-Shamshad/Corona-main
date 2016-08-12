package com.hp.psg.corona.common.util;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import com.hp.psg.common.util.logging.LoggerInfo;
import com.hp.psg.corona.error.util.CoronaErrorHandler;
import com.hp.psg.corona.util.cache.CtoTransformationUtilMap;
import com.hp.psg.corona.util.cache.CtoTransformationUtilMap.MappingInfo;
import com.hp.psg.corona.util.cache.CtoTransformationUtilMap.MappingListOnKeyObject;

/**
 * @author dudeja
 * @version 1.0
 *
 */
public class BeanXmlUtil {
	LoggerInfo logInfo=null;
	
	public BeanXmlUtil() {
		logInfo = new LoggerInfo (this.getClass().getName());
	}

	public Document parseXmlFile(String fileName) {
		// get the factory
		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
		Document dom = null;

		try {
			DocumentBuilder db = dbf.newDocumentBuilder();
			File file = new File(fileName);
			if (file.exists()) {
				dom = db.parse(fileName);
			} else {
				Logger.info(logInfo, "parseXmlFile","Not able to locate the config xml");
			}
		} catch (ParserConfigurationException pce) {
			pce.printStackTrace();
		} catch (SAXException se) {
			se.printStackTrace();
		} catch (IOException ioe) {
			ioe.printStackTrace();
		}

		return dom;
	}

	public Map<String, List<String>> parseDocumentForBeanKeyFields(Document dom) {

		Map<String, List<String>> cachedBeanMethodXML = new HashMap<String, List<String>>();
		Element docEle = dom.getDocumentElement();
		NodeList nl = docEle.getElementsByTagName("bean");

		if (nl != null && nl.getLength() > 0) {
			String nodeId = null;
			List<String> list = null;
			for (int i = 0; i < nl.getLength(); i++) {
				Element el = (Element) nl.item(i);
				nodeId = el.getAttribute("name");
				Logger.info(logInfo, "parseDocumentForBeanKeyFields", "get key fields name for " + nodeId);
				list = getBeanKeyFields(el, "keyfields");
				cachedBeanMethodXML.put(nodeId, list);
			}
		}

		return cachedBeanMethodXML;

	}

	public static List<String> getBeanKeyFields(Element ele, String tagName) {
		List<String> listVal = new ArrayList<String>();

		NodeList nl = ele.getElementsByTagName(tagName);

		if (nl != null && nl.getLength() > 0) {
			Element el = (Element) nl.item(0);
			NodeList nmsl = el.getElementsByTagName("field");

			if (nmsl != null && nmsl.getLength() > 0) {
				for (int i = 0; i < nmsl.getLength(); i++) {
					Element classtag = (Element) nmsl.item(i);
					String classname = classtag.getAttribute("name");
					listVal.add(classname);
				}
			}
		}
		return listVal;
	}

	public Map<String, List<HashMap<String, List<String>>>> parseDocumentForFeaturePluginClasses(
			Document dom) {

		Map<String, List<HashMap<String, List<String>>>> cachedNodeXML = new HashMap<String, List<HashMap<String, List<String>>>>();

		Element docEle = dom.getDocumentElement();
		NodeList nl = docEle.getElementsByTagName("node");

		if (nl != null && nl.getLength() > 0) {
			String nodeId = null;
			List<HashMap<String, List<String>>> list = null;
			for (int i = 0; i < nl.getLength(); i++) {
				Element el = (Element) nl.item(i);
				nodeId = el.getAttribute("id");
				Logger.info(logInfo, "parseDocumentForFeaturePluginClasses","get id " + nodeId);
				list = loadTxnHandlerMap(el, "txn_handler");
				cachedNodeXML.put(nodeId.toUpperCase(), list);
			}
		}

		return cachedNodeXML;
	}

	public Map<String, String> parseDocumentForPtToDaoMethodMap(Document dom) {

		Map<String, String> cachedPtToDaoMethodMap = new HashMap<String, String>();

		Element docEle = dom.getDocumentElement();
		NodeList nl = docEle.getElementsByTagName("processtype");
		if (nl != null && nl.getLength() > 0) {
			String nodeId = null;
			for (int i = 0; i < nl.getLength(); i++) {
				Element el = (Element) nl.item(i);
				nodeId = el.getAttribute("id");
				Logger.info(logInfo, "parseDocumentForPtToDaoMethodMap","processtype get id "
						+ nodeId);
				String methodValue = getTextValue(el, "daomethod_identified");// el.getElementsByTagName("daomethod_identified").item(0).getNodeValue();
				Logger.info(logInfo, "parseDocumentForPtToDaoMethodMap","method value while setting the field for dao method name coming as "
								+ methodValue);

				cachedPtToDaoMethodMap.put(nodeId.toUpperCase(), methodValue);
			}
		}

		return cachedPtToDaoMethodMap;

	}

	public Map<String, String> parseDocumentForPtToPluginTypeMap(Document dom) {

		Map<String, String> cachedPtToPluginTypeMap = new HashMap<String, String>();

		Element docEle = dom.getDocumentElement();
		NodeList nl = docEle.getElementsByTagName("processtype");
		if (nl != null && nl.getLength() > 0) {
			String nodeId = null;
			for (int i = 0; i < nl.getLength(); i++) {
				Element el = (Element) nl.item(i);
				nodeId = el.getAttribute("id");

				String pluginTypeValue = el.getAttribute("plugin_type");

				cachedPtToPluginTypeMap.put(nodeId.toUpperCase(),
						pluginTypeValue);
			}
		}

		return cachedPtToPluginTypeMap;

	}

	public Map<String, List<CtoTransformationUtilMap>> parseDocumentForPtToTransformUtilClass(
			Document dom) {

		Map<String, List<CtoTransformationUtilMap>> cachedPtToTransformUtilList = new HashMap<String, List<CtoTransformationUtilMap>>();

		Element docEle = dom.getDocumentElement();
		NodeList nl = docEle.getElementsByTagName("processtype");
		if (nl != null && nl.getLength() > 0) {
			String nodeId = null;
			List<CtoTransformationUtilMap> list = null;
			for (int i = 0; i < nl.getLength(); i++) {
				Element el = (Element) nl.item(i);
				nodeId = el.getAttribute("id");
				Logger.info(logInfo, "parseDocumentForPtToTransformUtilClass","get id " + nodeId);
				list = loadTransformationHandlerMap(el,
						"transformation_classes");
				cachedPtToTransformUtilList.put(nodeId.toUpperCase(), list);
			}
		}

		return cachedPtToTransformUtilList;

	}

	// Generate the transformation map object
	private List<CtoTransformationUtilMap> loadTransformationHandlerMap(
			Element ele, String tagName) {

		NodeList nl = ele.getElementsByTagName(tagName);
		List<CtoTransformationUtilMap> list = new ArrayList<CtoTransformationUtilMap>();

		if (nl != null && nl.getLength() > 0) {

			Element el = (Element) nl.item(0);
			NodeList nmsl = el.getElementsByTagName("class");

			if (nmsl != null && nmsl.getLength() > 0) {
				for (int i = 0; i < nmsl.getLength(); i++) {

					Element elementDS = (Element) nmsl.item(i);
					String className = elementDS.getAttribute("name");
					Map<String, Map<String, String>> constructArgs = Collections.EMPTY_MAP;
					
					try {
						 constructArgs = getConstructorArguments(elementDS, "construct_args");
						 Logger.info(logInfo,"loadTransformationHandlerMap", "Argument constructor needs to be provided for class "+className + " elements count "+constructArgs.size());
						 
					}catch (Exception ex){
						CoronaErrorHandler.logError(null, "Exception while loading constructor arguments for "+className, ex);
						//Still will continue from here...
					}
					List<MappingInfo> mappingInfo = getMappingInfoListForThisClass(
							elementDS, "mappingobj");
					List<MappingListOnKeyObject> listMappingOnKeys = getMappingOnKeyForSingleObject(
							elementDS, "mappinglistonkeys");
					
					CtoTransformationUtilMap ctoUtilObj = new CtoTransformationUtilMap(
							className,constructArgs, mappingInfo, listMappingOnKeys);
					list.add(ctoUtilObj);
				}
			}
		}

		return list;
	}

	
	private Map<String , Map<String,String>> getConstructorArguments (Element ele,
			String tagname) {
		NodeList nl = ele.getElementsByTagName(tagname);
		Map <String, Map<String, String>> returnMap = new HashMap<String, Map<String, String>>();
		
		if (nl != null && nl.getLength() > 0) {
				NodeList nmsl = ele.getElementsByTagName("var");
				if (nmsl != null && nmsl.getLength() > 0) {
					for (int i = 0; i < nmsl.getLength(); i++) {
						Element eleVar = (Element) nmsl.item(i);
						String varName = eleVar.getAttribute("name");
						String varValue = eleVar.getAttribute("value");
						String varType = eleVar.getAttribute("type");
						
						if (varName != null && varValue != null){
							Map<String,String> typeValue = new HashMap<String, String>();
							typeValue.put ("value",varValue);
							typeValue.put ("type", varType);
							
							returnMap.put(varName, typeValue);
						}
							
					}
			}
		}
		return returnMap;
	}
	
	// Returns the mapping object for class tag.
	private List<MappingInfo> getMappingInfoListForThisClass(Element ele,
			String tagname) {
		List<MappingInfo> list = new ArrayList<MappingInfo>();
		NodeList nl = ele.getElementsByTagName(tagname);

		if (nl != null && nl.getLength() > 0) {
			for (int i = 0; i < nl.getLength(); i++) {

				Element elementDS = (Element) nl.item(i);
				String parentClass = getTextValue(elementDS, "parent");
				String childClass = getTextValue(elementDS, "child");
				String bindingKey = getTextValue(elementDS, "bindingkey");
				String delimeter = getTextValue(elementDS, "delim");

				MappingInfo mappingObj = new MappingInfo(parentClass,
						childClass, bindingKey, delimeter);

				list.add(mappingObj);
			}
		}
		return list;
	}

	// Returns the mapping object for class tag.
	private List<MappingListOnKeyObject> getMappingOnKeyForSingleObject(
			Element ele, String tagname) {
		List<MappingListOnKeyObject> list = new ArrayList<MappingListOnKeyObject>();
		NodeList nl = ele.getElementsByTagName(tagname);

		if (nl != null && nl.getLength() > 0) {
			for (int i = 0; i < nl.getLength(); i++) {

				Element elementDS = (Element) nl.item(i);
				String objectClass = getTextValue(elementDS, "object");
				String bindingKey = getTextValue(elementDS, "bindingkey");
				String delimeter = getTextValue(elementDS, "delim");

				MappingListOnKeyObject mappingObj = new MappingListOnKeyObject(
						objectClass, bindingKey, delimeter);

				list.add(mappingObj);
			}
		}
		return list;
	}
	private List<HashMap<String, List<String>>> loadTxnHandlerMap(Element ele,
			String tagName) {

		NodeList nl = ele.getElementsByTagName(tagName);
		List<HashMap<String, List<String>>> list = new ArrayList<HashMap<String, List<String>>>();

		if (nl != null && nl.getLength() > 0) {
			Element el = (Element) nl.item(0);
			NodeList nmsl = el.getElementsByTagName("call");

			HashMap<String, List<String>> hm = new HashMap<String, List<String>>();
			if (nmsl != null && nmsl.getLength() > 0) {
				for (int i = 0; i < nmsl.getLength(); i++) {
					String idKey = "";
					List<String> listClasses = new ArrayList<String>();
					Element elementDS = (Element) nmsl.item(i);
					idKey = elementDS.getAttribute("id");
					if (idKey != null) {
						listClasses = getListValues(elementDS, "classes");
						hm.put(idKey.toUpperCase(), listClasses);
					}
					list.add(hm);
				}
			}
		}

		return list;
	}
	private List getListValues(Element ele, String tagName) {
		List<String> listVal = new ArrayList<String>();

		NodeList nl = ele.getElementsByTagName(tagName);

		if (nl != null && nl.getLength() > 0) {
			Element el = (Element) nl.item(0);
			NodeList nmsl = el.getElementsByTagName("class");

			if (nmsl != null && nmsl.getLength() > 0) {
				for (int i = 0; i < nmsl.getLength(); i++) {
					Element classtag = (Element) nmsl.item(i);
					String classname = classtag.getAttribute("name");
					listVal.add(classname);
				}
			}
		}
		return listVal;
	}

	private String getTextValue(Element ele, String tagName) {
		String textVal = null;
		NodeList nl = ele.getElementsByTagName(tagName);
		if (nl != null && nl.getLength() > 0) {
			Element el = (Element) nl.item(0);
			textVal = el.getFirstChild().getNodeValue();
		}

		return textVal;
	}

	private String getAttributeValue(Element ele, String tagname,
			String attributename) {
		return ele.getAttribute(attributename);
	}

	private int getIntValue(Element ele, String tagName) {
		// in production application you would catch the exception
		return Integer.parseInt(getTextValue(ele, tagName));
	}
}
