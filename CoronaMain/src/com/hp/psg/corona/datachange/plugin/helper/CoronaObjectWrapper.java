package com.hp.psg.corona.datachange.plugin.helper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.hp.psg.corona.common.cto.beans.CoronaBaseObject;

/**
 * @author dudeja
 * @version 1.0
 *
 */
public class CoronaObjectWrapper extends CoronaBaseObject
		implements
			CoronaObjWrapperI {

	private Map<String, List<? extends CoronaBaseObject>> hm = null;

	public CoronaObjectWrapper() {
		int initialCapacity = 5;
		float loadFactor = (float) 0.8;
		this.setType("CoronaObjectWrapper");

		hm = new HashMap<String, List<? extends CoronaBaseObject>>(
				initialCapacity, loadFactor);
	}

	public List<? extends CoronaBaseObject> GetCoronaObj(String name) {
		List<? extends CoronaBaseObject> obj = null;
		if (hm != null)
			obj = hm.get(name);
		return obj;
	}

	public Iterator<String> getAllCoronaObjName() {
		if (hm != null) {
			Iterator<String> itr = hm.keySet().iterator();
			return itr;
		} else
			return null;
	}

	public int getCoronaObjCount() {
		int hmSize = 0;
		if (hm != null)
			hmSize = hm.size();
		return hmSize;
	}

	public void insertCoronaObjectList(String objName,
			List<? extends CoronaBaseObject> listObj) {
		if (hm != null) {
			hm.put(objName, listObj);
		}
	}

	public void removeCoronaByObjName(String objName) {
		if (hm != null)
			hm.remove(objName);
	}

	public Iterator<Object> getAllCoronaObj() {
		List<Object> listObject = new ArrayList<Object>();
		if (hm != null) {
			for (String objKey : hm.keySet()) {
				listObject.add(hm.get(objKey));
			}
		}
		return listObject.iterator();
	}

	public void insertCoronaObject(String objName, CoronaBaseObject obj) {
		List<CoronaBaseObject> listObj = new ArrayList<CoronaBaseObject>();
		listObj.add(obj);
		if (hm != null)
			hm.put(objName, listObj);
	}

	public String toString() {
		StringBuffer sb = new StringBuffer("");
		if (this.hm != null) {
			Iterator<String> itr = getAllCoronaObjName();
			while (itr.hasNext()) {
				List<? extends CoronaBaseObject> cobList = GetCoronaObj(itr
						.next());
				for (CoronaBaseObject cob : cobList) {
					sb.append(cob.getType() + "\n");
				}
			}
		}
		return sb.toString();
	}
}
