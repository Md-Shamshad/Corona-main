package com.hp.psg.corona.datachange.cto.plugin.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import com.hp.psg.corona.common.cto.beans.ConfigComponentInfo;
import com.hp.psg.corona.common.cto.beans.ConfigHeaderInfo;
import com.hp.psg.corona.common.cto.beans.ConfigPermutationInfo;
import com.hp.psg.corona.common.cto.beans.CoronaBaseObject;
import com.hp.psg.corona.common.cto.beans.PriceInfo;
import com.hp.psg.corona.common.util.CoronaFwkUtil;
import com.hp.psg.corona.datachange.plugin.helper.CoronaObjHeader;
import com.hp.psg.corona.datachange.plugin.helper.CoronaObjectWrapper;
import com.hp.psg.corona.datachange.plugin.interfaces.FeaturePluginRequest;
import com.hp.psg.corona.datachange.plugin.interfaces.FeaturePluginResult;
import com.hp.psg.corona.datachange.plugin.interfaces.IBusinessProcessPlugin;

/**
 * @author dudeja
 * @version 1.0
 * 
 */
public class ConfigInfoDataExecutePlugin implements IBusinessProcessPlugin {
	private String pluginName = "IPCS Price Info";
	private String pluginOwner = "IPCS";
	private String pluginRole = "Execution";
	private String pluginTypes = "Execution";
	private String pluginDescription = "Execution";
	private static String HASHSIGN = "#";
	private static String PRICING_PREFERENCES = "pricing.recipient";
	private static String PRICING_PREFERENCES_DELIM = ".";
	private static String PRICING_OBJECT_NAME = "Preferences";
	private static String ALL_COUNTRIES = "ALL";

	public String getPluginName() {
		return pluginName;
	}
	public String getPluginOwner() {
		return pluginOwner;
	}
	public String getPluginRole() {
		return pluginRole;
	}
	public String getPluginDescription() {
		return pluginDescription;
	}
	public int getPluginPriority() {
		return 1;
	}
	public String getPluginTypes() {
		return pluginTypes;
	}

	public FeaturePluginResult execute(FeaturePluginRequest featureRequest) {
		FeaturePluginResult featureResponse = new FeaturePluginResult();

		List<CoronaObjectWrapper> coronaHeaderObjectList = featureRequest
				.getFeatureBean().getHeaders();
		Iterator<CoronaObjectWrapper> coronaHeaderObjectListIterator = coronaHeaderObjectList
				.iterator();

		List<CoronaObjectWrapper> coronaWrapperObjList = new ArrayList<CoronaObjectWrapper>();
		CoronaObjectWrapper coronaObjectWrapperMap = new CoronaObjectWrapper();
		HashMap<String, ArrayList> mockCoronaObjectWrapperMap;
		Iterator<Object> coronaObjectWrapperMapIterator;

		ArrayList<CoronaBaseObject> tempList;
		ArrayList<CoronaBaseObject> coronaObject = new ArrayList<CoronaBaseObject>();
		List<CoronaBaseObject> priceInfoList;

		CoronaObjectWrapper coronaWrapperObj = new CoronaObjectWrapper();
		CoronaObjHeader coronaHeaderObj = new CoronaObjHeader();

		try {
			while (coronaHeaderObjectListIterator.hasNext()) {
				// Iterates through the CoronaObjectHeader List to get the
				// HashMap(CoronaObjectWrapper)
				coronaObjectWrapperMap = coronaHeaderObjectListIterator.next();
				coronaObjectWrapperMapIterator = coronaObjectWrapperMap
						.getAllCoronaObj();

				// Create another Map with ConfigHeaderInfo, ConfigComponentInfo
				// and ConfigPermutationInfo as keys
				mockCoronaObjectWrapperMap = new HashMap<String, ArrayList>(
						coronaObjectWrapperMap.getCoronaObjCount());

				// coronaObjectWrapper is a map
				while (coronaObjectWrapperMapIterator.hasNext()) {
					coronaObject = (ArrayList) coronaObjectWrapperMapIterator
							.next();
					if (mockCoronaObjectWrapperMap.containsKey(coronaObject
							.get(0).getType())) {
						tempList = (mockCoronaObjectWrapperMap.get(coronaObject
								.get(0).getType()));
						tempList.addAll(coronaObject);
						mockCoronaObjectWrapperMap.put(coronaObject.get(0)
								.getType(), tempList);
					} else {
						mockCoronaObjectWrapperMap.put(coronaObject.get(0)
								.getType(), coronaObject);
					}
				}

				// Check the object type and process
				priceInfoList = processObject(mockCoronaObjectWrapperMap);

				// Create return type

				coronaWrapperObj.insertCoronaObjectList("PriceInfo",
						priceInfoList);
				coronaWrapperObj.setType("PriceInfo");
				coronaWrapperObjList.add(coronaWrapperObj);

				coronaHeaderObj.setHeaders(coronaWrapperObjList);
				featureResponse.setFeatureBean(coronaHeaderObj);
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			coronaHeaderObjectList = null;
			coronaHeaderObjectListIterator = null;
			// coronaWrapperObjList = null;
			coronaObjectWrapperMap = null;
			mockCoronaObjectWrapperMap = null;
			coronaObjectWrapperMapIterator = null;
			tempList = null;
			coronaObject = null;
			priceInfoList = null;
		}
		return featureResponse;
	}

	public List<CoronaBaseObject> processObject(
			HashMap<String, ArrayList> coronaObjectWrapperMap) {
		ArrayList coronaBaseList = new ArrayList();

		ConfigHeaderInfo mockConfigHeaderInfo = new ConfigHeaderInfo();
		ConfigHeaderInfo key = new ConfigHeaderInfo();
		ConfigHeaderInfo tempInfo;
		ConfigHeaderInfo[] mockConfigHeaderInfoObject;

		Iterator<List> listIt;
		Iterator list2It;

		ArrayList<ConfigPermutationInfo> configPermutationInfoList;
		ConfigPermutationInfo permutationInfo;
		ConfigPermutationInfo tempPermInfo;
		HashMap<ConfigHeaderInfo, ArrayList<ConfigPermutationInfo>> permutationMapper = new HashMap<ConfigHeaderInfo, ArrayList<ConfigPermutationInfo>>(
				10, 10);

		ConfigComponentInfo configComponentInfoObj;

		PriceInfo priceInfoObj;
		ArrayList<CoronaBaseObject> priceInfoList = new ArrayList<CoronaBaseObject>();

		int counter;

		try {
			if (!coronaObjectWrapperMap.isEmpty()) {
				coronaBaseList = coronaObjectWrapperMap.get("ConfigHeaderInfo");
				if (!coronaBaseList.equals(null)) {
					mockConfigHeaderInfoObject = new ConfigHeaderInfo[coronaBaseList
							.size()];
					counter = 0;
					// If ArrayList is of Object Type ConfigHeaderInfo
					// Create a Map of ConfigHeaderInfo and ArrayList of
					// corresponding ConfigPermutationInfo.
					// Currently associate the key (bundleId, ShipToCountry) to
					// empty
					if (coronaBaseList.get(0) instanceof ConfigHeaderInfo) {
						listIt = coronaBaseList.iterator();
						tempInfo = new ConfigHeaderInfo();
						while (listIt.hasNext()) {
							tempInfo = (ConfigHeaderInfo) (listIt.next());
							mockConfigHeaderInfoObject[counter] = new ConfigHeaderInfo();
							mockConfigHeaderInfoObject[counter]
									.setShipToCountry(tempInfo
											.getShipToCountry());
							mockConfigHeaderInfoObject[counter]
									.setBundleId(tempInfo.getBundleId());
							mockConfigHeaderInfo = mockConfigHeaderInfoObject[counter];
							// Check if the key already exists, if it does, dont
							// add it again
							// key=
							// getKey(mockConfigHeaderInfoObject,mockConfigHeaderInfo);
							key = getKey(mockConfigHeaderInfoObject,
									mockConfigHeaderInfo);

							if (!permutationMapper.containsKey(key)) {
								permutationMapper
										.put(
												mockConfigHeaderInfoObject[counter++],
												new ArrayList<ConfigPermutationInfo>(
														10));
							}
						}
					}

					coronaBaseList.clear();

					// If ArrayList if of Object Type ConfigPermutationInfo
					// Create an ArrayList of corresponding
					// ConfigPermutationInfo.
					// associate the key (bundleId, ShipToCountry) to
					// corresponding ArrayList
					coronaBaseList = coronaObjectWrapperMap
							.get("ConfigPermutationInfo");

					if (!coronaBaseList.equals(null)) {
						if (coronaBaseList.get(0) instanceof ConfigPermutationInfo) {
							configPermutationInfoList = new ArrayList<ConfigPermutationInfo>();
							listIt = coronaBaseList.iterator();
							permutationInfo = new ConfigPermutationInfo();

							while (listIt.hasNext()) {
								permutationInfo = (ConfigPermutationInfo) (listIt
										.next());
								mockConfigHeaderInfo
										.setBundleId(permutationInfo
												.getBundleId());
								mockConfigHeaderInfo
										.setShipToCountry(permutationInfo
												.getShipToCountry());

								if (!permutationInfo.equals(null)) {
									key = getKey(mockConfigHeaderInfoObject,
											mockConfigHeaderInfo);

									// If key does not already exist, ignore the
									// record as it does not
									// have a corresponding HeaderInfo record
									if (permutationMapper.containsKey(key)) {
										// Create a list of Permutations for a
										// HeaderInfo
										if (!(permutationMapper.get(key))
												.equals(null)) {
											configPermutationInfoList = permutationMapper
													.get(key);

										}
										configPermutationInfoList
												.add(permutationInfo);
										permutationMapper.put(key,
												configPermutationInfoList);
									}
								}
							}
						}
						coronaBaseList.clear();

						// If ArrayList if of Object Type ConfigComponentInfo
						// Create an arrayList of Object type PriceInfo
						// using the ConfigComponentInfo, ConfigPermutationInfo
						// and ConfigHeaderInfo.
						coronaBaseList = coronaObjectWrapperMap
								.get("ConfigComponentInfo");
						if (!coronaBaseList.equals(null)) {
							if (coronaBaseList.get(0) instanceof ConfigComponentInfo) {
								listIt = coronaBaseList.iterator();
								tempPermInfo = new ConfigPermutationInfo();

								// For each ConfigComponentInfo record, check if
								// a similar record
								// exists in the ConfigHeaderInfo. If it does
								// not, ignore, else
								// use every ConfigPermutationInfo record with
								// the same details
								// to create the PriceInfo record
								while (listIt.hasNext()) {
									configComponentInfoObj = new ConfigComponentInfo();
									configComponentInfoObj = (ConfigComponentInfo) (listIt
											.next());
									mockConfigHeaderInfo
											.setBundleId(configComponentInfoObj
													.getBundleId());
									mockConfigHeaderInfo
											.setShipToCountry(configComponentInfoObj
													.getShipToCountry());

									key = getKey(mockConfigHeaderInfoObject,
											mockConfigHeaderInfo);

									if (permutationMapper.containsKey(key)) {
										configPermutationInfoList = permutationMapper
												.get(key);
										list2It = configPermutationInfoList
												.iterator();

										// recipient,,productNumber,productOptionCd,
										// ,shipToGeo,priceGeo,currency,incoTerm,
										// priceListType,configDelFlag

										while (list2It.hasNext()) {
											priceInfoObj = new PriceInfo();
											tempPermInfo = (ConfigPermutationInfo) (list2It
													.next());
											priceInfoObj
													.setBundleId(configComponentInfoObj
															.getBundleId());
											priceInfoObj
													.setShipToCountry(configComponentInfoObj
															.getShipToCountry());
											priceInfoObj
													.setProductId(configComponentInfoObj
															.getProductId());
											priceInfoObj
													.setDelFlag(configComponentInfoObj
															.getDelFlag());

											priceInfoObj = getProductNumberAndOptionCd(
													configComponentInfoObj,
													priceInfoObj);

											priceInfoObj
													.setPriceIdType(tempPermInfo
															.getPriceIdType());
											priceInfoObj
													.setPriceProcedure(tempPermInfo
															.getPriceProcedure());
											priceInfoObj
													.setPriceDescriptor(tempPermInfo
															.getPriceDescriptor());
											priceInfoObj
													.setPriceId(tempPermInfo
															.getPriceId());
											priceInfoObj
													.setShipToGeo(tempPermInfo
															.getShipToCountry());
											priceInfoObj
													.setRecipient(getRecipient(tempPermInfo));
											priceInfoObj = getPriceGeoCurrInco(
													tempPermInfo, priceInfoObj);

											priceInfoList.add(priceInfoObj);
										}
									}
								}
							}
						}// if (!coronaBaseList for ConfigComponentInfo
							// equals(null))
					}// if (!coronaBaseList for ConfigPermutationInfo
						// equals(null))
				}// if (!coronaBaseList for ConfigHeaderInfo equals(null))
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			coronaBaseList = null;

			mockConfigHeaderInfo = null;
			key = null;
			tempInfo = null;
			mockConfigHeaderInfoObject = null;

			listIt = null;;
			list2It = null;;

			configPermutationInfoList = null;;
			permutationInfo = null;;
			tempPermInfo = null;;
			permutationMapper = null;

			configComponentInfoObj = null;
		}
		return priceInfoList;
	}

	public String getRecipient(ConfigPermutationInfo permutationInfo) {
		String recipient = "";
		String attributeName_ShipToCountry = PRICING_PREFERENCES
				+ PRICING_PREFERENCES_DELIM + permutationInfo.getRegionCode()
				+ PRICING_PREFERENCES_DELIM
				+ permutationInfo.getShipToCountry()
				+ PRICING_PREFERENCES_DELIM
				+ permutationInfo.getPriceProcedure();

		String attributeName_ALL = PRICING_PREFERENCES
				+ PRICING_PREFERENCES_DELIM + permutationInfo.getRegionCode()
				+ PRICING_PREFERENCES_DELIM + ALL_COUNTRIES
				+ PRICING_PREFERENCES_DELIM
				+ permutationInfo.getPriceProcedure();

		recipient = CoronaFwkUtil.getRegionalAttributeDefaultValueAsString(
				permutationInfo.getRegionCode(), PRICING_OBJECT_NAME,
				attributeName_ALL);
		// if (recipient.equals(null)) return "";
		return recipient;
	}
	// Converting productId into productNumber and productOptionCd
	public static PriceInfo getProductNumberAndOptionCd(
			ConfigComponentInfo componentInfo, PriceInfo priceInfo) {
		String productId = (String) componentInfo.getProductId();
		int index = productId.indexOf(HASHSIGN);
		if (index > 0) {
			priceInfo.setProductNumber(productId.substring(0, index));
			priceInfo.setProductOptionCd(productId.substring(index + 1,
					productId.length()));
		} else {
			priceInfo.setProductNumber(productId.substring(0, productId
					.length()));
			priceInfo.setProductOptionCd(null);
		}

		return priceInfo;
	}

	// Converting priceDescriptor into priceGeo and Currency and IncoTerm
	public static PriceInfo getPriceGeoCurrInco(
			ConfigPermutationInfo permutationInfo, PriceInfo priceInfo) {
		String priceDesc = (String) permutationInfo.getPriceDescriptor();
		if (priceDesc.length() > 6) {
			priceInfo.setPriceGeo(priceDesc.substring(0, 2));
			priceInfo.setCurrency(priceDesc.substring(2, 5));
			priceInfo.setIncoTerm(priceDesc.substring(5, priceDesc.length()));
		}
		return priceInfo;
	}

	public ConfigHeaderInfo getKey(ConfigHeaderInfo[] keyList,
			ConfigHeaderInfo keyToMatch) {
		for (ConfigHeaderInfo key : keyList) {
			if ((key.getBundleId() == keyToMatch.getBundleId())
					&& (key.getShipToCountry()) == keyToMatch
							.getShipToCountry()) {
				return key;
			}
		}
		return null;
	}

	public ConfigHeaderInfo getKey(Set<ConfigHeaderInfo> keySet,
			ConfigHeaderInfo keyToMatch) {
		for (ConfigHeaderInfo key : (ConfigHeaderInfo[]) keySet.toArray()) {
			if ((key.getBundleId() == keyToMatch.getBundleId())
					&& (key.getShipToCountry()) == keyToMatch
							.getShipToCountry()) {
				return key;
			}
		}
		return null;
	}

	public static void main(String args[]) {
		ConfigHeaderInfo corhead = new ConfigHeaderInfo();
		ConfigPermutationInfo corperm = new ConfigPermutationInfo();
		ConfigComponentInfo conComp = new ConfigComponentInfo();
		ArrayList corBaseList = new ArrayList(10);
		ArrayList corBaseList2 = new ArrayList(10);
		ArrayList corBaseList3 = new ArrayList(10);
		CoronaObjectWrapper corWrp = new CoronaObjectWrapper();
		ArrayList<CoronaObjectWrapper> corWrpList = new ArrayList<CoronaObjectWrapper>(
				10);
		HashMap<String, ArrayList> tempMap = new HashMap<String, ArrayList>(10);
		CoronaObjHeader corHeadObj = new CoronaObjHeader();
		// FeaturePluginRequest req = null;
		FeaturePluginResult res = null;

		corBaseList = new ArrayList<ConfigHeaderInfo>(10);
		corhead.setBundleId("1");
		corhead.setShipToCountry("FR");
		corBaseList.add(corhead);
		ConfigHeaderInfo corhead2 = new ConfigHeaderInfo();
		corhead2.setBundleId("1");
		corhead2.setShipToCountry("SG");
		corBaseList.add(corhead2);
		ConfigHeaderInfo corhead3 = new ConfigHeaderInfo();
		corhead3.setBundleId("2");
		corhead3.setShipToCountry("SG");
		corBaseList.add(corhead3);
		ConfigHeaderInfo corhead4 = new ConfigHeaderInfo();
		corhead4.setBundleId("2");
		corhead4.setShipToCountry("SG");
		corBaseList.add(corhead4);
		tempMap.put("ConfigHeaderInfo", corBaseList);
		corWrp.insertCoronaObjectList("ConfigHeaderInfo", corBaseList);

		corBaseList2 = new ArrayList<ConfigPermutationInfo>(10);
		corperm.setBundleId("1");
		corperm.setShipToCountry("SG");
		corperm.setPriceDescriptor("SGUSDDP");
		corperm.setRegionCode("NA");
		corperm.setPriceProcedure("HPCE05");
		corperm.setPriceId("P1");
		corBaseList2.add(corperm);
		ConfigPermutationInfo corperm2 = new ConfigPermutationInfo();
		corperm2.setBundleId("1");
		corperm2.setShipToCountry("FR");
		corperm2.setPriceDescriptor("FREURDP");
		corperm2.setRegionCode("EU");
		corperm2.setPriceProcedure("HPLP01");
		corperm2.setPriceId("P2");
		corBaseList2.add(corperm2);
		ConfigPermutationInfo corperm3 = new ConfigPermutationInfo();
		corperm3.setBundleId("1");
		corperm3.setShipToCountry("FR");
		corperm3.setPriceDescriptor("FREURDP");
		corperm3.setPriceId("P3");
		corBaseList2.add(corperm3);
		ConfigPermutationInfo corperm4 = new ConfigPermutationInfo();
		corperm4.setBundleId("2");
		corperm4.setShipToCountry("SG");
		corperm4.setPriceDescriptor("SGUSDDP");
		corperm4.setRegionCode("NA");
		corperm4.setPriceProcedure("HPLP01");
		corperm4.setPriceId("P4");
		corBaseList2.add(corperm4);
		ConfigPermutationInfo corperm5 = new ConfigPermutationInfo();
		corperm5.setBundleId("2");
		corperm5.setShipToCountry("FR");
		corperm5.setPriceDescriptor("FREURDP");
		corperm5.setPriceId("P2");
		corBaseList2.add(corperm5);
		tempMap.put("ConfigPermutationInfo", corBaseList2);
		corWrp.insertCoronaObjectList("ConfigPermutationInfo", corBaseList2);

		corBaseList3 = new ArrayList<ConfigComponentInfo>(10);
		// corBaseList = new ArrayList<ConfigComponentInfo>(10);
		// corBaseList.removeAll(corBaseList);
		conComp.setBundleId("1");
		conComp.setShipToCountry("FR");
		conComp.setProductId("E0009AV#0D1");
		corBaseList3.add(conComp);
		ConfigComponentInfo conComp2 = new ConfigComponentInfo();
		conComp2.setBundleId("1");
		conComp2.setShipToCountry("FR");
		conComp2.setProductId("E0009AV");
		corBaseList3.add(conComp2);
		ConfigComponentInfo conComp3 = new ConfigComponentInfo();
		conComp3.setBundleId("1");
		conComp3.setShipToCountry("SG");
		conComp3.setProductId("E0009AV#0D1");
		corBaseList3.add(conComp3);
		ConfigComponentInfo conComp4 = new ConfigComponentInfo();
		conComp4.setBundleId("2");
		conComp4.setShipToCountry("SG");
		conComp4.setProductId("24");
		corBaseList3.add(conComp4);
		ConfigComponentInfo conComp5 = new ConfigComponentInfo();
		conComp5.setBundleId("2");
		conComp5.setShipToCountry("SG");
		conComp5.setProductId("25");
		corBaseList3.add(conComp5);
		tempMap.put("ConfigComponentInfo", corBaseList3);
		corWrp.insertCoronaObjectList("ConfigComponentInfo", corBaseList3);

		corWrpList.add(corWrp);
		corHeadObj.setHeaders(corWrpList);
		// .setFeatureBean(corHeadObj);

		ConfigInfoDataExecutePlugin obj = new ConfigInfoDataExecutePlugin();
		obj.processObject(tempMap);
		// res=obj.returnPriceInfo(req);
		// System.out.println((res.getFeatureBean().getHeaders()).get(0).GetCoronaObj("PriceInfo").toString());

	}

}
