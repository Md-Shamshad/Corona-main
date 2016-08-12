package com.hp.psg.corona.common.beans;

import java.lang.reflect.Field;
import java.util.Date;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

import com.hp.psg.common.util.logging.LoggerInfo;
import com.hp.psg.corona.error.util.CoronaErrorHandler;

/**
 * @author dudeja
 * @version 1.0
 * 
 */
public class EventKeyFormatBean implements java.io.Serializable {
	private Long id;
	private String processType;
	private String processKeyFormat;
	private String procKeyDelimiter;
	private Date createdDate;
	private String createdBy;
	private Date lastModifiedDate;
	private String lastModifiedBy;
	private List<String> getMethodCallsForKeys;
	private List<String> setMethodCallsForKeys;
	private KeyFormatBeanMap keyFormatBean;

	public EventKeyFormatBean() {
		this.keyFormatBean = new KeyFormatBeanMap();
	}

	public class KeyFormatBeanMap {
		LoggerInfo logInfo = null;

		public KeyFormatBeanMap() {
			logInfo=new LoggerInfo(this.getClass().getName());
		}
		
		// Below attributes should be updated manual whenever key in formatBean
		// is added with new attribute.
		private String PRICE_ID = "PriceId";
		private String BUNDLE_ID = "BundleId";
		private String SHIP_CNTRY = "ShipCntry";
		private String PRICE_DESCRIPTOR = "PriceDescriptor";
		private String SHIP_TO_GEO = "ShipToGeo";
		private String PRICE_GEO = "PriceGeo";
		private String CURRENCY = "Currency";
		private String INCO_TERM = "IncoTerm";
		private String PRICE_LIST_TYPE = "PriceListType";
		private String PRODUCT_ID = "ProductId";

		private String getKeyValue(String key) {
			String fieldValue = "";
			try {
				Field privateStringField = this.getClass()
						.getDeclaredField(key);
				privateStringField.setAccessible(true);
				fieldValue = (String) privateStringField.get(this);
			} catch (Exception e) {
				CoronaErrorHandler.logError("FWK1000", "Key not found  - "+key,e);
			}
			return fieldValue;
		}

		public String getKeyGetter(String key) throws Exception {
			String fieldVal = getKeyValue(key);
			if (fieldVal != null && !"".equals(fieldVal))
				return "get" + fieldVal;
			else
				throw new Exception("Field not found");
		}

		public String getKeySetter(String key) throws Exception {
			String fieldVal = getKeyValue(key);
			if (fieldVal != null && !"".equals(fieldVal))
				return "set" + fieldVal;
			else
				throw new Exception("Field not found");
		}

	}

	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getProcessType() {
		return processType;
	}
	public void setProcessType(String processType) {
		this.processType = processType;
	}
	public String getProcessKeyFormat() {
		return processKeyFormat;
	}
	public void setProcessKeyFormat(String processKeyFormat) {
		this.processKeyFormat = processKeyFormat;
	}
	public String getProcKeyDelimiter() {
		return procKeyDelimiter;
	}
	public void setProcKeyDelimiter(String procKeyDelimiter) {
		this.procKeyDelimiter = procKeyDelimiter;
	}
	public Date getCreatedDate() {
		return createdDate;
	}
	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}
	public String getCreatedBy() {
		return createdBy;
	}
	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}
	public Date getLastModifiedDate() {
		return lastModifiedDate;
	}
	public void setLastModifiedDate(Date lastModifiedDate) {
		this.lastModifiedDate = lastModifiedDate;
	}
	public String getLastModifiedBy() {
		return lastModifiedBy;
	}
	public void setLastModifiedBy(String lastModifiedBy) {
		this.lastModifiedBy = lastModifiedBy;
	}

	private void formatMethodCalls() {
		List<String> getterCalls = new ArrayList<String>();
		List<String> setterCalls = new ArrayList<String>();

		try {
			StringTokenizer st = new StringTokenizer(
					this.getProcessKeyFormat(), this.getProcKeyDelimiter());
			while (st.hasMoreTokens()) {
				String str = st.nextToken();
				getterCalls.add(getKeyFormatBean().getKeyGetter(str));
				setterCalls.add(getKeyFormatBean().getKeySetter(str));
			}

			setSetMethodCallsForKeys(setterCalls);
			setGetMethodCallsForKeys(getterCalls);
		} catch (Exception e) {
			CoronaErrorHandler.logError(e,null,null);		
		}
	}

	public List<String> getKeyList() {
		StringTokenizer st = new StringTokenizer(this.getProcessKeyFormat(),
				this.getProcKeyDelimiter());
		List<String> stList = new ArrayList<String>();
		while (st.hasMoreTokens()) {
			String str = st.nextToken();
			stList.add(str);
		}
		return stList;
	}

	public KeyFormatBeanMap getKeyFormatBean() {
		return keyFormatBean;
	}
	public void setKeyFormatBean(KeyFormatBeanMap keyFormatBean) {
		this.keyFormatBean = keyFormatBean;
	}
	public List<String> getGetMethodCallsForKeys() {
		if (getMethodCallsForKeys == null)
			formatMethodCalls();
		return getMethodCallsForKeys;
	}
	public void setGetMethodCallsForKeys(List<String> getMethodCallsForKeys) {
		this.getMethodCallsForKeys = getMethodCallsForKeys;
	}
	public List<String> getSetMethodCallsForKeys() {
		if (getMethodCallsForKeys == null)
			formatMethodCalls();
		return setMethodCallsForKeys;
	}
	public void setSetMethodCallsForKeys(List<String> setMethodCallsForKeys) {
		this.setMethodCallsForKeys = setMethodCallsForKeys;
	}
	
}
