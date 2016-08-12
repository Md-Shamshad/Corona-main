package com.hp.psg.corona.datachange.cto.plugin.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import com.hp.psg.corona.common.cto.beans.ConfigComponentInfo;
import com.hp.psg.corona.common.cto.beans.ConfigHeaderInfo;
import com.hp.psg.corona.common.cto.beans.ConfigPermutationInfo;
import com.hp.psg.corona.common.cto.beans.CoronaBaseObject;
import com.hp.psg.corona.datachange.dao.DataChangeEventsFwkDao;
import com.hp.psg.corona.datachange.plugin.helper.CoronaObjHeader;
import com.hp.psg.corona.datachange.plugin.helper.CoronaObjectWrapper;
import com.hp.psg.corona.datachange.plugin.interfaces.FeaturePluginRequest;
import com.hp.psg.corona.datachange.plugin.interfaces.FeaturePluginResult;
import com.hp.psg.corona.datachange.plugin.interfaces.ITransformationPlugin;
import com.hp.psg.corona.propagation.handler.util.ConfigDataChangeUtil;



public class ConfigPermutationInfoTransformPlugin implements ITransformationPlugin {
	private String pluginName = "IPCS Config Permutation Info";
	private String pluginOwner = "IPCS";
	private String pluginRole = "Execution";
	private String pluginTypes = "Execution";
	private String pluginDescription = "Execution";


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

	public FeaturePluginResult transform(FeaturePluginRequest featureRequest) {
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
					coronaObject = (ArrayList) coronaObjectWrapperMapIterator.next();
					if(coronaObject!=null && coronaObject.size()>0){
						if (mockCoronaObjectWrapperMap.containsKey(coronaObject.get(0).getType())) {
							tempList = (mockCoronaObjectWrapperMap.get(coronaObject
									.get(0).getType()));
							tempList.addAll(coronaObject);
							mockCoronaObjectWrapperMap.put(coronaObject.get(0)
									.getType(), tempList);
						} else {
							mockCoronaObjectWrapperMap.put(coronaObject.get(0).getType(), coronaObject);
						}
					}
				}

				// Check the object type and process
				priceInfoList = processObject(mockCoronaObjectWrapperMap);
				
				// Create return type

				coronaWrapperObj.insertCoronaObjectList("ConfigPermutationInfo",priceInfoList);
				coronaWrapperObj.setType("ConfigPermutationInfo");
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
		}
		return featureResponse;
	}

	public List<CoronaBaseObject> processObject(
			HashMap<String, ArrayList> coronaObjectWrapperMap) {
		ArrayList<CoronaBaseObject> coronaBaseList = null;

		ConfigPermutationInfo permutationInfo;


		try {
			if (!coronaObjectWrapperMap.isEmpty()) {
				// If ArrayList if of Object Type ConfigPermutationInfo
				// Create an ArrayList of corresponding
				// ConfigPermutationInfo.
				// associate the key (bundleId, ShipToCountry) to
				// corresponding ArrayList
				coronaBaseList = coronaObjectWrapperMap.get("ConfigPermutationInfo");

				if (coronaBaseList != null) {
					if (coronaBaseList.get(0) instanceof ConfigPermutationInfo) {
						Iterator listIt = coronaBaseList.iterator();

						while (listIt.hasNext()) {
							permutationInfo = (ConfigPermutationInfo) (listIt.next());
							if (!permutationInfo.equals(null)) {
								String region = permutationInfo.getRegionCode();
								String dealId = permutationInfo.getPriceId();
								
								try{
									String pricingProcedure = ConfigDataChangeUtil.getPriceProcedure(region, dealId);
									permutationInfo.setPriceProcedure(pricingProcedure);									
								}catch(Exception e){
									e.printStackTrace();
								}
								
							}
						}
					}
					coronaBaseList.clear();

				}// if (!coronaBaseList for ConfigPermutationInfo
				
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			permutationInfo = null;
		}
		return coronaBaseList;
	}


	public static void main(String args[]) {
		try{
			DataChangeEventsFwkDao.cacheModuleMap();
		}catch (Exception e){
			
		}
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
		corhead.setBundleId("04739609");
		corhead.setShipToCountry("SG");
		corBaseList.add(corhead);
		ConfigHeaderInfo corhead2 = new ConfigHeaderInfo();
		corhead2.setBundleId("04739600");
		corhead2.setShipToCountry("FR");
		corBaseList.add(corhead2);
		ConfigHeaderInfo corhead3 = new ConfigHeaderInfo();
		corhead3.setBundleId("04793210");
		corhead3.setShipToCountry("US");
		corBaseList.add(corhead3);
		
		tempMap.put("ConfigHeaderInfo", corBaseList);
		corWrp.insertCoronaObjectList("ConfigHeaderInfo", corBaseList);

		
		
		corBaseList2 = new ArrayList<ConfigPermutationInfo>(10);
		corperm.setBundleId("04739609");
		corperm.setShipToCountry("SG");
		corperm.setPriceDescriptor("SGUSDDF");
		corperm.setRegionCode("AP");
		corperm.setPriceId("P296937");
		corBaseList2.add(corperm);
		ConfigPermutationInfo corperm2 = new ConfigPermutationInfo();
		corperm2.setBundleId("04739600");
		corperm2.setShipToCountry("FR");
		corperm2.setPriceDescriptor("FREURDP");
		corperm2.setRegionCode("EU");
		corperm2.setPriceId("P296943");
		corBaseList2.add(corperm2);
		ConfigPermutationInfo corperm4 = new ConfigPermutationInfo();
		corperm4.setBundleId("04793210");
		corperm4.setShipToCountry("US");
		corperm4.setPriceDescriptor("USUSDDP");
		corperm4.setRegionCode("NA");
		corperm4.setPriceId("FSA0016231");
		corBaseList2.add(corperm4);
		ConfigPermutationInfo corperm5 = new ConfigPermutationInfo();
		corperm5.setBundleId("04793210");
		corperm5.setShipToCountry("US");
		corperm5.setRegionCode("NA");
		corperm5.setPriceDescriptor("USUSDDP");
		corperm5.setPriceId("FSA0012419");
		corBaseList2.add(corperm5);
		tempMap.put("ConfigPermutationInfo", corBaseList2);
		corWrp.insertCoronaObjectList("ConfigPermutationInfo", corBaseList2);

		corBaseList3 = new ArrayList<ConfigComponentInfo>(10);
		// corBaseList = new ArrayList<ConfigComponentInfo>(10);
		// corBaseList.removeAll(corBaseList);
		conComp.setBundleId("04739609");
		conComp.setShipToCountry("SG");
		conComp.setProductId("KX851AV");
		corBaseList3.add(conComp);
		ConfigComponentInfo conComp2 = new ConfigComponentInfo();
		conComp2.setBundleId("04739609");
		conComp2.setShipToCountry("SG");
		conComp2.setProductId("NU615AV#ABG");
		corBaseList3.add(conComp2);
		ConfigComponentInfo conComp3 = new ConfigComponentInfo();
		conComp3.setBundleId("04739600");
		conComp3.setShipToCountry("FR");
		conComp3.setProductId("AU247AV");
		corBaseList3.add(conComp3);
		ConfigComponentInfo conComp4 = new ConfigComponentInfo();
		conComp4.setBundleId("04739600");
		conComp4.setShipToCountry("FR");
		conComp4.setProductId("AU251AV");
		corBaseList3.add(conComp4);
		ConfigComponentInfo conComp5 = new ConfigComponentInfo();
		conComp5.setBundleId("04793210");
		conComp5.setShipToCountry("US");
		conComp5.setProductId("C5952A");
		corBaseList3.add(conComp5);
		tempMap.put("ConfigComponentInfo", corBaseList3);
		corWrp.insertCoronaObjectList("ConfigComponentInfo", corBaseList3);

		corWrpList.add(corWrp);
		corHeadObj.setHeaders(corWrpList);
		// .setFeatureBean(corHeadObj);

		ConfigPermutationInfoTransformPlugin obj = new ConfigPermutationInfoTransformPlugin();
		obj.processObject(tempMap);
		// res=obj.returnPriceInfo(req);
		// System.out.println((res.getFeatureBean().getHeaders()).get(0).GetCoronaObj("PriceInfo").toString());

	}

}

