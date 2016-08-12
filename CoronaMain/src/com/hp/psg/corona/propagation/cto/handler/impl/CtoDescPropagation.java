package com.hp.psg.corona.propagation.cto.handler.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.hp.psg.common.error.CoronaException;
import com.hp.psg.corona.common.beans.CTODaxDataBeanGeneral;
import com.hp.psg.corona.common.constants.CTOConstants;
import com.hp.psg.corona.common.cto.beans.ConfigComponentInfo;
import com.hp.psg.corona.common.cto.beans.ConfigDescription;
import com.hp.psg.corona.common.cto.beans.ConfigHeaderInfo;
import com.hp.psg.corona.common.cto.beans.CoronaBaseObject;
import com.hp.psg.corona.common.cto.beans.ProductDescription;
import com.hp.psg.corona.common.util.CoronaFwkUtil;
import com.hp.psg.corona.propagation.handler.interfaces.IBeanPropagation;

/*
* @author bilgi
* @version 1.0
*
*/
public class CtoDescPropagation implements IBeanPropagation {
	List<String> msKeywordList;

	/** Default Constructor- used to initialize the keyword list from DB */
	public CtoDescPropagation() {
		msKeywordList = CoronaFwkUtil.getRegionalAttributeDefaultValueAsList(
				CTOConstants.REGION_WW, CTOConstants.MS_OBJ_NAME,
				CTOConstants.MS_ATTR_NAME);
	}

	/**
	 * Interface method implemented.
	 * 
	 * @return CTODaxDataBeanGeneral
	 * @throws CoronaException
	 */
	@SuppressWarnings("unchecked")
	public CTODaxDataBeanGeneral processBeans(CTODaxDataBeanGeneral generalBean)
			throws CoronaException {
		if (generalBean == null) {
			throw new CoronaException(
					"CTODescTransformation:: NULL Input CTODaxDataBeanGeneral has been passed as null");
		}

		Map<CoronaBaseObject, List<? extends CoronaBaseObject>> header2ComponentMap = Collections.EMPTY_MAP;
		Map<CoronaBaseObject, List<? extends CoronaBaseObject>> component2ProdDescMap = Collections.EMPTY_MAP;
		Map<CoronaBaseObject, List<? extends CoronaBaseObject>> header2ConfigDescMap = Collections.EMPTY_MAP;
		List<Map<CoronaBaseObject, List<? extends CoronaBaseObject>>> myMappingList = generalBean
				.getListMapRelations();

		// Initialize the maps
		for (Iterator<Map<CoronaBaseObject, List<? extends CoronaBaseObject>>> iterator = myMappingList
				.iterator(); iterator.hasNext();) {
			Map<CoronaBaseObject, List<? extends CoronaBaseObject>> myMap = iterator
					.next();
			String valueInListType = CoronaFwkUtil.getType(myMap);
			if (null != valueInListType) {
				if (valueInListType.equals(CTOConstants.CONFIG_COMPONENT_INFO)) {
					header2ComponentMap = myMap;
				} else if (valueInListType
						.equals(CTOConstants.PRODUCT_DESCRIPTION)) {
					component2ProdDescMap = myMap;
				} else if (valueInListType
						.equals(CTOConstants.CONFIG_DESCRIPTION)) {
					header2ConfigDescMap = myMap;
				}
			}
		}

		for (ConfigHeaderInfo chi : generalBean.getConfigHeaderInfo()) {
			List<ConfigComponentInfo> componentList = (List<ConfigComponentInfo>) header2ComponentMap
					.get(chi);
			loadConfigDescriptions(generalBean, chi, header2ConfigDescMap,
					componentList, component2ProdDescMap);
		}

		return generalBean;
	}

	/**
	 * 
	 * @param configHeaderInfo
	 * @param header2ConfigDescMap
	 * @param componentList
	 * @param component2ProdDescMap
	 * @param generalBean
	 */
	@SuppressWarnings("unchecked")
	private void loadConfigDescriptions(
			CTODaxDataBeanGeneral generalBean,
			ConfigHeaderInfo configHeaderInfo,
			Map<CoronaBaseObject, List<? extends CoronaBaseObject>> header2ConfigDescMap,
			List<ConfigComponentInfo> componentList,
			Map<CoronaBaseObject, List<? extends CoronaBaseObject>> component2ProdDescMap) {

		// Create a Product Description List for the components.This can contain
		// multiple locales
		List<ProductDescription> descList = new ArrayList<ProductDescription>();

		if (componentList != null){
			for (ConfigComponentInfo cci : componentList) {
				List<ProductDescription> productDescList = (List<ProductDescription>) component2ProdDescMap
						.get(cci);
				descList.addAll(productDescList);
			}
		}
	
		Set<String> uniqueLocales = getUniqueLocales(descList);
		for (String locale : uniqueLocales) {
			
			String desc = generateLongDescription(configHeaderInfo, locale,
					componentList, descList);
			boolean foundMatch = false;
			// check if the description is the same for the locale and the
			// header
			List<ConfigDescription> configDescList = (List<ConfigDescription>) header2ConfigDescMap
					.get(configHeaderInfo);
			if (null != configDescList) {
				for (ConfigDescription cd : configDescList) {
					if (cd.getLocale().equals(locale)) {
						foundMatch = true;
						if ((null != cd.getLongDesc())
								&& (cd.getLongDesc().equals(desc))) {
							// same description mark as unchanged
							cd.setUpdtFlag(CTOConstants.UPDT_FLAG_UNCHANGED);
						} else {
							cd.setLongDesc(desc);
							cd.setUpdtFlag(CTOConstants.UPDT_FLAG_MODIFIED);
						}
					}
				}
			}

			// There is no description for this locale - so create new
			// ConfigDescription.
			if (!foundMatch) {
				ConfigDescription cd = new ConfigDescription();
				cd.setBundleId(configHeaderInfo.getBundleId());
				cd.setShipToCountry(configHeaderInfo.getShipToCountry());
				cd.setLongDesc(desc);
				cd.setLocale(locale);
				cd.setDeleteFlag(CTOConstants.DELETE_FLAG_NOT_SET);
				cd.setUpdtFlag(CTOConstants.UPDT_FLAG_ADD);
				cd.setLastModifiedBy(this.getClass().getSimpleName());
				int newLen = generalBean.getConfigDescription().length + 1;
				ConfigDescription[] cdesc = new ConfigDescription[newLen];
				System.arraycopy(generalBean.getConfigDescription(), 0, cdesc,
						0, generalBean.getConfigDescription().length);
				cdesc[newLen - 1] = cd;
				generalBean.setConfigDescription(cdesc);
			}
		}
	}

	/**
	 * Generate HTML formatted long desc Sequence is : add base products
	 * description, add all microsoft descriptions, add remaining product
	 * descriptions
	 */
	private String generateLongDescription(ConfigHeaderInfo cinfo,
			String locale, List<ConfigComponentInfo> inComponentList,
			List<ProductDescription> inDescList) {

		StringBuffer otherDescriptions = new StringBuffer();
		StringBuffer descriptionBuffer = new StringBuffer();
		StringBuffer baseProdDesc = new StringBuffer();
		StringBuffer microsoftDescriptions = new StringBuffer();
		String baseProductId = cinfo.getBaseProductId();

		if ((null != inComponentList) && (inComponentList.size() > 0)) {
			for (ConfigComponentInfo compInfo : inComponentList) {
				if (compInfo.getProductId().equals(baseProductId)) {
					ProductDescription pd = getProductDescription(locale,
							baseProductId, inDescList);
					baseProdDesc = new StringBuffer(pd.getShortDesc());
					descriptionBuffer.append("<li>" + baseProdDesc + "</li>");
				} else if (compInfo.getStartingPointFlag().equals(
						CTOConstants.STARTING_PT_FLAG_SET)) {
					ProductDescription pd = getProductDescription(locale,
							compInfo.getProductId(), inDescList);
					if (checkIfKeywordExists(pd.getShortDesc())) {
						microsoftDescriptions.append("<br><li>"
								+ pd.getShortDesc().trim() + "</li>");
					} else {
						otherDescriptions.append("<br><li>"
								+ pd.getShortDesc().trim() + "</li>");
					}
				}
			}
		} // end null check
		
		descriptionBuffer.append(microsoftDescriptions).append(
				otherDescriptions);
		return descriptionBuffer.toString();
	}

	/**
	 * Get Product Description based on locale and product
	 * 
	 * @param locale
	 * @param productId
	 * @param inDescList
	 * @return ProductDescription
	 */
	private ProductDescription getProductDescription(String locale,
			String productId, List<ProductDescription> inDescList) {
		// Return new Product Description object with short desc as product ID
		// in case we dont find a match. Should never occur.
		ProductDescription pdesc1 = new ProductDescription();
		pdesc1.setShortDesc(productId);
		pdesc1.setLocale(locale);

		for (ProductDescription pdesc : inDescList) {
			if (pdesc.getProductId().equals(productId)
					&& (pdesc.getLocale().equals(locale))) {
				return pdesc;
			}
		}
		return pdesc1;
	}

	/**
	 * Get List of unique locales
	 * 
	 * @param inProdDesc
	 * @return Set<String>
	 */
	private Set<String> getUniqueLocales(List<ProductDescription> inProdDesc) {
		Set<String> localeSet = new HashSet<String>();
		for (ProductDescription pd : inProdDesc) {
			localeSet.add(pd.getLocale());
		}
		return localeSet;
	}

	/**
	 * Private method to check if Microsoft Keywords are a part of the
	 * description.
	 * 
	 * @param description
	 * @return
	 */
	private boolean checkIfKeywordExists(String description) {
		boolean keywordExists = false;
		for (String keyDescription : msKeywordList) {
			if (description.toLowerCase().trim().contains(
					keyDescription.toLowerCase().trim())) {
				keywordExists = true;
				return keywordExists;
			}
		}
		return keywordExists;
	}

	public void prepareBean(Map<String, Object> constructArgs) {
		// TODO Auto-generated method stub
		
	}

}