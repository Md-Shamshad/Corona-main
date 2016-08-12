package com.hp.psg.corona.propagation.handler;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;

import com.hp.psg.corona.common.beans.CTODaxDataBeanGeneral;
import com.hp.psg.corona.common.cto.beans.CoronaBaseObject;
import com.hp.psg.corona.common.util.CoronaFwkUtil;
import com.hp.psg.corona.datachange.dao.DataChangeEventsFwkDao;
import com.hp.psg.corona.error.util.CoronaErrorHandler;
import com.hp.psg.corona.util.cache.CtoTransformationUtilMap;
import com.hp.psg.corona.util.cache.CtoTransformationUtilMap.MappingInfo;

/*
* @author dudeja
* @version 1.0
*
*/
public class PropagationHandlerUtilities {

	// Method will expect the parent, child and the mapping key between them
	public static Map<String, List<? extends CoronaBaseObject>> mapRelations(
			CoronaBaseObject[] cboParent, CoronaBaseObject[] cboChild,
			String bindingKey) {
		Map<String, List<? extends CoronaBaseObject>> relationalMap = new HashMap<String, List<? extends CoronaBaseObject>>();

		try {
			// Map will have the key as map. binding Key should be same as bean
			// attribute.
			Class clParent = cboParent[0].getClass();
			Class clChild = cboChild[0].getClass();

			String getterMethodName = CoronaFwkUtil.getProcessorMethodName(
					"get", CoronaFwkUtil.initCap(bindingKey));

			if (cboParent != null && cboChild != null) {
				for (CoronaBaseObject cboP : cboParent) {

					List<CoronaBaseObject> childMapList = new ArrayList<CoronaBaseObject>();
					java.lang.reflect.Method parentMethod = clParent
							.getMethod(getterMethodName);
					String key = (String) parentMethod.invoke(cboP);
					for (CoronaBaseObject cboC : cboChild) {
						java.lang.reflect.Method childMethod = clChild
								.getMethod(getterMethodName);
						String val = (String) childMethod.invoke(cboC);
						if (key.equals(val)) {
							childMapList.add(cboC);
						}
						relationalMap.put(key, childMapList);
					}
				}

			}
		} catch (Exception ex) {
			CoronaErrorHandler.logError(ex,null,null);
			
		}
		return relationalMap;
	}

	// Method will expect the parent, child and the mapping key between them
	public static Map<String, List<? extends CoronaBaseObject>> mapRelationsWithMultipleKeys(
			CoronaBaseObject[] cboParent, CoronaBaseObject[] cboChild,
			String bindingKeys, String delim) {

		StringTokenizer keyTokens = new StringTokenizer(bindingKeys, delim);

		Map<String, List<? extends CoronaBaseObject>> relationalMap = new HashMap<String, List<? extends CoronaBaseObject>>();

		try {
			// Map will have the key as map. binding Key should be same as bean
			// attribute.
			Class clParent = cboParent[0].getClass();
			Class clChild = cboChild[0].getClass();
			StringBuffer mapKeyVal = null;

			List<String> getterMethodNames = new ArrayList<String>();

			while (keyTokens.hasMoreTokens()) {
				String bindingKey = keyTokens.nextToken();
				getterMethodNames.add(CoronaFwkUtil.getProcessorMethodName(
						"get", CoronaFwkUtil.initCap(bindingKey)));
			}

			if (cboParent != null && cboChild != null) {
				for (CoronaBaseObject cboP : cboParent) {
					List<CoronaBaseObject> childMapList = new ArrayList<CoronaBaseObject>();

					for (CoronaBaseObject cboC : cboChild) {
						boolean isChild = true;
						mapKeyVal = new StringBuffer();

						for (String getterMethodName : getterMethodNames) {
							java.lang.reflect.Method parentMethod = clParent
									.getMethod(getterMethodName);
							String key = (String) parentMethod.invoke(cboP);

							java.lang.reflect.Method childMethod = clChild
									.getMethod(getterMethodName);
							String val = (String) childMethod.invoke(cboC);

							if (!key.equals(val)) {
								isChild = false;
								break;
							} else {
								mapKeyVal.append(key + delim);
							}

						}
						if (isChild)
							childMapList.add(cboC);

						// Removing last | from the key .
						relationalMap.put(mapKeyVal.toString().substring(0,
								mapKeyVal.length() - 1), childMapList);
					}
				}
			}
		} catch (Exception ex) {
			CoronaErrorHandler.logError(ex,null,null);
		}
		return relationalMap;
	}

	// Method will expect the parent, child and the mapping key between them
	public static void mapRelationsWithMultipleKeys(
			CTODaxDataBeanGeneral ctoDaxDataGeneralBean,
			String cboParentClassName, String cboChildClassName,
			String bindingKeys, String delim) {

		StringTokenizer keyTokens = new StringTokenizer(bindingKeys, delim);

		try {
			Class cl = Class
					.forName("com.hp.psg.corona.common.beans.CTODaxDataBeanGeneral");
			Class clParent = Class.forName(cboParentClassName);
			Class clChild = Class.forName(cboChildClassName);

			java.lang.reflect.Method parentGetterMethod = cl.getMethod("get"
					+ ((CoronaBaseObject) clParent.newInstance()).getType());
			CoronaBaseObject[] cboParentList = (CoronaBaseObject[]) parentGetterMethod
					.invoke(ctoDaxDataGeneralBean);

			java.lang.reflect.Method childGetterMethod = cl.getMethod("get"
					+ ((CoronaBaseObject) clChild.newInstance()).getType());
			CoronaBaseObject[] cboChildList = (CoronaBaseObject[]) childGetterMethod
					.invoke(ctoDaxDataGeneralBean);

			List<String> getterMethodNames = new ArrayList<String>();

			while (keyTokens.hasMoreTokens()) {
				String bindingKey = keyTokens.nextToken();
				getterMethodNames.add(CoronaFwkUtil.getProcessorMethodName(
						"get", CoronaFwkUtil.initCap(bindingKey)));
			}

			Map<CoronaBaseObject, List<? extends CoronaBaseObject>> parentChildMapHolder = new HashMap<CoronaBaseObject, List<? extends CoronaBaseObject>>();

			for (CoronaBaseObject cboP : cboParentList) {
				List<CoronaBaseObject> childMapList = new ArrayList<CoronaBaseObject>();

				for (CoronaBaseObject cboC : cboChildList) {
					boolean isChild = true;

					for (String getterMethodName : getterMethodNames) {
						java.lang.reflect.Method parentMethod = clParent
								.getMethod(getterMethodName);
						String parentValue = (String) parentMethod.invoke(cboP);

						java.lang.reflect.Method childMethod = clChild
								.getMethod(getterMethodName);
						String childValue = (String) childMethod.invoke(cboC);

						if (!parentValue.equals(childValue)) {
							isChild = false;
							break;
						}
					}
					if (isChild)
						childMapList.add(cboC);
				}

				putRelationsInMapHolder(parentChildMapHolder, cboP,
						childMapList);
			}
			putHolderMapInBean(ctoDaxDataGeneralBean, parentChildMapHolder);
		} catch (Exception ex) {
			CoronaErrorHandler.logError(ex,null,null);
		}
	}

	public static void putRelationsInMapHolder(
			Map<CoronaBaseObject, List<? extends CoronaBaseObject>> mapHolder,
			CoronaBaseObject cboParentObj,
			List<? extends CoronaBaseObject> cboChildList) {
		if (mapHolder != null) {
			if (mapHolder.containsKey(cboParentObj)) {
				List tempList = mapHolder.get(cboParentObj);
				for (CoronaBaseObject tempCboChild : cboChildList)
					tempList.add(tempCboChild);
			} else {
				mapHolder.put(cboParentObj, cboChildList);
			}
		}
	}

	public static void putHolderMapInBean(CTODaxDataBeanGeneral ctoDxDataBean,
			Map<CoronaBaseObject, List<? extends CoronaBaseObject>> hm) {

		if (ctoDxDataBean.getListMapRelations() == null) {
			ctoDxDataBean
					.setListMapRelations(new ArrayList<Map<CoronaBaseObject, List<? extends CoronaBaseObject>>>());
		}

		ctoDxDataBean.getListMapRelations().add(hm);
	}

	public static List<MappingInfo> getMappingInfoObject(String processType,
			String className) {
		List<CtoTransformationUtilMap> mapObjList = null;
		mapObjList = DataChangeEventsFwkDao.getHmTransoformationClassList()
				.get(processType);
		for (CtoTransformationUtilMap ctoUtilObj : mapObjList) {
			if (className.equalsIgnoreCase(ctoUtilObj.getClassName()))
				;
			return ctoUtilObj.getMappingList();
		}
		return null;
	}

	// ***************************************************************//

	// Method will expect the parent, child and the mapping key between them
	public static void populateListObjectsWithMultipleKeys(
			CTODaxDataBeanGeneral ctoDaxDataGeneralBean, String objectName,
			String bindingKeys, String delim) {

		StringTokenizer keyTokens = new StringTokenizer(bindingKeys, delim);

		try {
			Class cl = Class
					.forName("com.hp.psg.corona.common.beans.CTODaxDataBeanGeneral");
			Class clObjet = Class.forName(objectName);

			java.lang.reflect.Method parentGetterMethod = cl.getMethod("get"
					+ ((CoronaBaseObject) clObjet.newInstance()).getType());
			CoronaBaseObject[] cboObjectList = (CoronaBaseObject[]) parentGetterMethod
					.invoke(ctoDaxDataGeneralBean);

			List<String> getterMethodNames = new ArrayList<String>();

			while (keyTokens.hasMoreTokens()) {
				String bindingKey = keyTokens.nextToken();
				getterMethodNames.add(CoronaFwkUtil.getProcessorMethodName(
						"get", CoronaFwkUtil.initCap(bindingKey)));
			}

			Map<String, List<? extends CoronaBaseObject>> keyObjectListMap = new HashMap<String, List<? extends CoronaBaseObject>>();

			if(cboObjectList!=null){
				for (CoronaBaseObject cboO : cboObjectList) {
					List<CoronaBaseObject> childMapList = new ArrayList<CoronaBaseObject>();
					StringBuffer sb = new StringBuffer("");
	
					for (String getterMethodName : getterMethodNames) {
						java.lang.reflect.Method objectMethod = clObjet
								.getMethod(getterMethodName);
						String objectValue = (String) objectMethod.invoke(cboO);
						sb.append(objectValue + "|");
					}
	
					List tmpList = null;
	
					if (keyObjectListMap.containsKey(sb.toString())) {
						tmpList = (keyObjectListMap.get(sb));
					} else {
						tmpList = new ArrayList<CoronaBaseObject>();
						tmpList.add(cboO);
					}
					// Removing last |
					keyObjectListMap.put(sb.toString()
							.substring(0, sb.length() - 1), tmpList);
				}
			}
			insertHolderMapInBean(ctoDaxDataGeneralBean, keyObjectListMap);
		} catch (Exception ex) {
			CoronaErrorHandler.logError(ex,null,null);
		}
	}

	public static void insertHolderMapInBean(
			CTODaxDataBeanGeneral ctoDxDataBean,
			Map<String, List<? extends CoronaBaseObject>> hm) {

		if (ctoDxDataBean.getListObjectsOnKeys() == null) {
			ctoDxDataBean
					.setListObjectsOnKeys(new ArrayList<Map<String, List<? extends CoronaBaseObject>>>());
		}

		ctoDxDataBean.getListObjectsOnKeys().add(hm);
	}

}
