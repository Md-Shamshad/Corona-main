package com.hp.psg.corona.common.util;

import java.util.ArrayList;
import java.util.List;

import com.hp.psg.corona.common.cto.beans.CoronaBaseObject;
import com.hp.psg.corona.common.plugin.interfaces.AbstractPlugin;
import com.hp.psg.corona.datachange.plugin.helper.CoronaObjHeader;
import com.hp.psg.corona.datachange.plugin.helper.CoronaObjectWrapper;
import com.hp.psg.corona.datachange.plugin.interfaces.FeaturePluginRequest;
import com.hp.psg.corona.datachange.plugin.interfaces.FeaturePluginResult;

/**
 * @author dudeja
 * @version 1.0
 *
 */

public class CoronaObjectWrapperUtil {

	public static FeaturePluginRequest getEmptyPluginReqObject() {
		FeaturePluginRequest fpr = new FeaturePluginRequest();
		CoronaObjHeader coh = new CoronaObjHeader();
		List<CoronaObjectWrapper> listCow = new ArrayList<CoronaObjectWrapper>();

		CoronaObjectWrapper cow = new CoronaObjectWrapper();
		listCow.add(cow);

		coh.setHeaders(listCow);
		fpr.setFeatureBean(coh);

		return fpr;
	}

	public static FeaturePluginRequest getInitialPluginReqObject(
			List<CoronaBaseObject> cb) {
		FeaturePluginRequest fpr = new FeaturePluginRequest();
		CoronaObjHeader coh = new CoronaObjHeader();
		List<CoronaObjectWrapper> listCow = new ArrayList<CoronaObjectWrapper>();

		CoronaObjectWrapper cow = new CoronaObjectWrapper();
		if (cb != null && cb.size() > 0)
			cow.insertCoronaObjectList(cb.get(0).getType(), cb);

		listCow.add(cow);

		coh.setHeaders(listCow);
		fpr.setFeatureBean(coh);

		return fpr;
	}

	public static FeaturePluginRequest getPluginReqObject(
			CoronaObjectWrapper cow) {
		FeaturePluginRequest fpr = new FeaturePluginRequest();
		CoronaObjHeader coh = new CoronaObjHeader();
		List<CoronaObjectWrapper> listCow = new ArrayList<CoronaObjectWrapper>();
		listCow.add(cow);

		coh.setHeaders(listCow);
		fpr.setFeatureBean(coh);

		return fpr;
	}

	public FeaturePluginRequest addWrapperToRequestList(
			FeaturePluginRequest fpr, CoronaObjectWrapper cow) {
		if (fpr != null) {
			fpr.getFeatureBean().addToList(cow);
			return fpr;
		} else
			return getPluginReqObject(cow);
	}

	// This method to check the feature plugin request going is valid in terms
	// of objects contained in list or not.
	public boolean validatePluginRequest(FeaturePluginRequest fpr) {
		return true;
	}

	public static FeaturePluginRequest putToCoronaObjectList(
			FeaturePluginRequest fpr, String str, List<CoronaBaseObject> list) {
		List<CoronaObjectWrapper> cowList = fpr.getFeatureBean().getHeaders();

		CoronaObjectWrapper newCow = new CoronaObjectWrapper();
		newCow.insertCoronaObjectList(str, list);

		if (cowList != null) {
			cowList.add(newCow);
		} else {
			cowList = new ArrayList<CoronaObjectWrapper>();
			cowList.add(newCow);
		}
		return fpr;
	}

	public static void prettyPrint(AbstractPlugin req) {
		FeaturePluginRequest fpr;
		FeaturePluginResult fpresult;

		if (req instanceof FeaturePluginRequest) {
			fpr = (FeaturePluginRequest) req;

			CoronaObjHeader coh = fpr.getFeatureBean();
			List<? extends CoronaBaseObject> cobList = coh.getHeaders();

			for (CoronaBaseObject cob : cobList) {
				if (cob instanceof CoronaObjectWrapper) {
					CoronaFwkUtil.println(cob.toString());
				}
			}

		} else if (req instanceof FeaturePluginResult) {
			fpresult = (FeaturePluginResult) req;

			CoronaObjHeader coh = fpresult.getFeatureBean();
			List<? extends CoronaBaseObject> cobList = coh.getHeaders();
			for (CoronaBaseObject cob : cobList) {
				if (cob instanceof CoronaObjectWrapper) {
					CoronaFwkUtil.println(cob.toString());
				}
			}

		}

	}

	public static FeaturePluginRequest covertResponseToRequest(
			FeaturePluginResult fprResult) {
		FeaturePluginRequest fpr = new FeaturePluginRequest();
		fpr.setFeatureBean(fprResult.getFeatureBean());

		return fpr;
	}

	public static List<? extends CoronaBaseObject> accessObject(
			CoronaObjectWrapper cow, String str) {

		List<? extends CoronaBaseObject> cb = cow.GetCoronaObj(str);
		return cb;
	}

}
