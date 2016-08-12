package com.hp.psg.corona.util.cache;

import java.util.List;
import java.util.Map;

/**
 * @author dudeja
 * @version 1.0
 * 
 */
public class CtoTransformationUtilMap {

	public static class MappingInfo {
		private String cboParent;
		private String cboChild;
		// Binding key will be seperated by a delimiter.
		private String bindingKey;
		private String bindingKeyDelim;

		public MappingInfo(String cboParent, String cboChild,
				String mappingKey, String delim) {
			this.cboParent = cboParent;
			this.cboChild = cboChild;
			this.bindingKey = mappingKey;
			this.bindingKeyDelim = delim;
		}

		public String getBindingKeyDelim() {
			return bindingKeyDelim;
		}

		public void setBindingKeyDelim(String bindingKeyDelim) {
			this.bindingKeyDelim = bindingKeyDelim;
		}

		public String getCboParent() {
			return cboParent;
		}

		public void setCboParent(String cboParent) {
			this.cboParent = cboParent;
		}

		public String getCboChild() {
			return cboChild;
		}

		public void setCboChild(String cboChild) {
			this.cboChild = cboChild;
		}

		public String getBindingKey() {
			return bindingKey;
		}

		public void setBindingKey(String bindingKey) {
			this.bindingKey = bindingKey;
		}
	}

	public static class MappingListOnKeyObject {
		private String cboObject;
		private String bindingKey;
		private String bindingKeyDelim;

		public MappingListOnKeyObject(String cboObject, String mappingKey,
				String delim) {
			this.cboObject = cboObject;
			this.bindingKey = mappingKey;
			this.bindingKeyDelim = delim;
		}

		public String getBindingKeyDelim() {
			return bindingKeyDelim;
		}

		public void setBindingKeyDelim(String bindingKeyDelim) {
			this.bindingKeyDelim = bindingKeyDelim;
		}

		public String getBindingKey() {
			return bindingKey;
		}

		public void setBindingKey(String bindingKey) {
			this.bindingKey = bindingKey;
		}

		public String getCboObject() {
			return cboObject;
		}

		public void setCboObject(String cboObject) {
			this.cboObject = cboObject;
		}
	}

	private String className;
	private List<MappingInfo> mappingList;
	private List<MappingListOnKeyObject> listObjectsOnKeys;
	
	//Format is <key, <value,type>>
	private Map<String, Map<String,String>> constructArguments;
	
	
	public String getClassName() {
		return className;
	}
	public void setClassName(String className) {
		this.className = className;
	}
	public List<MappingInfo> getMappingList() {
		return mappingList;
	}
	public void setMappingList(List<MappingInfo> mappingList) {
		this.mappingList = mappingList;
	}
	public List<MappingListOnKeyObject> getListObjectsOnKeys() {
		return listObjectsOnKeys;
	}
	public void setListObjectsOnKeys(
			List<MappingListOnKeyObject> listObjectsOnKeys) {
		this.listObjectsOnKeys = listObjectsOnKeys;
	}

	public CtoTransformationUtilMap(String className,
			List<MappingInfo> listObj, List<MappingListOnKeyObject> listObj2) {
		this.className = className;
		this.mappingList = listObj;
		this.listObjectsOnKeys = listObj2;
	}

	public CtoTransformationUtilMap(String className,Map constructArgs,
			List<MappingInfo> listObj, List<MappingListOnKeyObject> listObj2) {
		if (constructArgs != null && constructArgs.size() > 0) 
			this.constructArguments = constructArgs;
		this.className = className;
		this.mappingList = listObj;
		this.listObjectsOnKeys = listObj2;
	}
	public Map<String, Map<String,String>> getConstructArguments() {
		return constructArguments;
	}
	public void setConstructArguments(Map<String, Map<String,String>> constructArguments) {
		this.constructArguments = constructArguments;
	}
	


}
