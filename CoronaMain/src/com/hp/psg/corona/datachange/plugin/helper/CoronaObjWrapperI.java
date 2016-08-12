package com.hp.psg.corona.datachange.plugin.helper;

import java.util.Iterator;
import java.util.List;

import com.hp.psg.corona.common.cto.beans.CoronaBaseObject;

/**
 * @author dudeja
 * @version 1.0
 *
 */
public interface CoronaObjWrapperI {

	/*
	 * This interface will provide methods to insert and remove objects out of
	 * CoronaObjectWrapper Also all inserts objects should be extending
	 * CoronaBaseObject.
	 */

	public void insertCoronaObjectList(String objName,
			List<? extends CoronaBaseObject> objList);
	public void insertCoronaObject(String objName, CoronaBaseObject obj);

	public void removeCoronaByObjName(String objName);

	public Iterator<String> getAllCoronaObjName();
	public Iterator<Object> getAllCoronaObj();
	public int getCoronaObjCount();

	// Need to discuss as now the return will also have kind of list which will
	// fail in type cast,
	public List<? extends CoronaBaseObject> GetCoronaObj(String name);

}
