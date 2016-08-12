package com.hp.psg.corona.common.beans;

import java.io.Serializable;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.hp.psg.common.util.logging.LoggerInfo;
import com.hp.psg.corona.common.cto.beans.ConfigComponentInfo;
import com.hp.psg.corona.common.cto.beans.ConfigDescription;
import com.hp.psg.corona.common.cto.beans.ConfigHeaderInfo;
import com.hp.psg.corona.common.cto.beans.ConfigPermutationInfo;
import com.hp.psg.corona.common.cto.beans.ConfigPermutationPriceInfo;
import com.hp.psg.corona.common.cto.beans.ConfigPriceHeaderInfo;
import com.hp.psg.corona.common.cto.beans.CoronaBaseObject;
import com.hp.psg.corona.common.cto.beans.PriceInfo;
import com.hp.psg.corona.common.cto.beans.ProductDescription;
import com.hp.psg.corona.common.util.Logger;

/**
 * @author dudeja
 * @version 1.0
 *
 */
public class CTODaxDataBeanGeneral implements Serializable{

	private String status;
	private int returnCode;
	private String msg;
	private String priceId;
	private String bundleId;
	private String shipToCountry;
	private String priceDescriptor;
	private String shipToGeo;
	private String priceGeo;
	private String currency;
	private String incoTerm;
	private String priceListType;
	private Long trnId;
	private Long dceId;
	private Long peId;
	private Long srcEventId;
	private PriceInfo[] priceInfo;
	private ConfigComponentInfo[] configComponentInfo;
	private ConfigDescription[] configDescription;
	private ConfigHeaderInfo[] configHeaderInfo;
	private ConfigPermutationInfo[] configPermutationInfo;
	private ConfigPermutationPriceInfo[] configPermutationPriceInfo;
	private ProductDescription[] productDescription;

	private PropagationEvent[] propagationEvent;
	private ConfigPriceHeaderInfo[] configPriceHeaderInfo;
	private List<Map<CoronaBaseObject, List<? extends CoronaBaseObject>>> listMapRelations;
	private List<Map<String, List<? extends CoronaBaseObject>>> listObjectsOnKeys;

	LoggerInfo logInfo=null;
	
	public void CTODaxDataBeanGeneral() {
		logInfo = new LoggerInfo (this.getClass().getName());
	}
	
	public int getReturnCode() {
		return returnCode;
	}
	public void setReturnCode(int returnCode) {
		this.returnCode = returnCode;
	}
	public Long getDceId() {
		return dceId;
	}
	public void setDceId(Long dceId) {
		this.dceId = dceId;
	}
	public Long getPeId() {
		return peId;
	}
	public void setPeId(Long peId) {
		this.peId = peId;
	}
	public Long getSrcEventId() {
		return srcEventId;
	}
	public void setSrcEventId(Long srcEventId) {
		this.srcEventId = srcEventId;
	}
	public PropagationEvent[] getPropagationEvent() {
		return propagationEvent;
	}
	public void setPropagationEvent(PropagationEvent[] propagationEvent) {
		this.propagationEvent = propagationEvent;
	}
	public Long getTrnId() {
		return trnId;
	}
	public void setTrnId(Long trnId) {
		this.trnId = trnId;
	}
	public PriceInfo[] getPriceInfo() {
		return priceInfo;
	}
	public void setPriceInfo(PriceInfo[] priceInfo) {
		this.priceInfo = priceInfo;
	}
	public ConfigComponentInfo[] getConfigComponentInfo() {
		return configComponentInfo;
	}
	public void setConfigComponentInfo(ConfigComponentInfo[] configComponentInfo) {
		this.configComponentInfo = configComponentInfo;
	}
	public ConfigDescription[] getConfigDescription() {
		return configDescription;
	}
	public void setConfigDescription(ConfigDescription[] configDescription) {
		this.configDescription = configDescription;
	}
	public ConfigHeaderInfo[] getConfigHeaderInfo() {
		return configHeaderInfo;
	}
	public void setConfigHeaderInfo(ConfigHeaderInfo[] configHeaderInfo) {
		this.configHeaderInfo = configHeaderInfo;
	}
	public ConfigPermutationInfo[] getConfigPermutationInfo() {
		return configPermutationInfo;
	}
	public void setConfigPermutationInfo(
			ConfigPermutationInfo[] configPermutationInfo) {
		this.configPermutationInfo = configPermutationInfo;
	}
	public ConfigPermutationPriceInfo[] getConfigPermutationPriceInfo() {
		return configPermutationPriceInfo;
	}
	public void setConfigPermutationPriceInfo(
			ConfigPermutationPriceInfo[] configPermutationPriceInfo) {
		this.configPermutationPriceInfo = configPermutationPriceInfo;
	}
	public ProductDescription[] getProductDescription() {
		return productDescription;
	}
	public void setProductDescription(ProductDescription[] productDescription) {
		this.productDescription = productDescription;
	}

	public ConfigPriceHeaderInfo[] getConfigPriceHeaderInfo() {
		return configPriceHeaderInfo;
	}
	public void setConfigPriceHeaderInfo(
			ConfigPriceHeaderInfo[] configPriceHeaderInfo) {
		this.configPriceHeaderInfo = configPriceHeaderInfo;
	}

	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getMsg() {
		return msg;
	}
	public void setMsg(String msg) {
		this.msg = msg;
	}
	public String getPriceId() {
		return priceId;
	}
	public void setPriceId(String priceId) {
		this.priceId = priceId;
	}
	public String getBundleId() {
		return bundleId;
	}
	public void setBundleId(String bundleId) {
		this.bundleId = bundleId;
	}
	public String getShipToCountry() {
		return shipToCountry;
	}
	public void setShipToCountry(String shipToCountry) {
		this.shipToCountry = shipToCountry;
	}
	public String getPriceDescriptor() {
		return priceDescriptor;
	}
	public void setPriceDescriptor(String priceDescriptor) {
		this.priceDescriptor = priceDescriptor;
	}
	public String getShipToGeo() {
		return shipToGeo;
	}
	public void setShipToGeo(String shipToGeo) {
		this.shipToGeo = shipToGeo;
	}
	public String getPriceGeo() {
		return priceGeo;
	}
	public void setPriceGeo(String priceGeo) {
		this.priceGeo = priceGeo;
	}
	public String getCurrency() {
		return currency;
	}
	public void setCurrency(String currency) {
		this.currency = currency;
	}
	public String getIncoTerm() {
		return incoTerm;
	}
	public void setIncoTerm(String incoTerm) {
		this.incoTerm = incoTerm;
	}
	public String getPriceListType() {
		return priceListType;
	}
	public void setPriceListType(String priceListType) {
		this.priceListType = priceListType;
	}
	public List<Map<CoronaBaseObject, List<? extends CoronaBaseObject>>> getListMapRelations() {
		return listMapRelations;
	}
	public void setListMapRelations(
			List<Map<CoronaBaseObject, List<? extends CoronaBaseObject>>> listMapRelations) {
		this.listMapRelations = listMapRelations;
	}
	public List<Map<String, List<? extends CoronaBaseObject>>> getListObjectsOnKeys() {
		return listObjectsOnKeys;
	}
	public void setListObjectsOnKeys(
			List<Map<String, List<? extends CoronaBaseObject>>> listObjectsOnKeys) {
		this.listObjectsOnKeys = listObjectsOnKeys;
	}

	public String toString() {
		StringBuffer sb = new StringBuffer("[ \n");
		logInfo = new LoggerInfo (this.getClass().getName());
		try {
			if (priceInfo != null) {
				sb.append("PriceInfo array size :"
						+ priceInfo.length + "\n");
				for (PriceInfo pI : priceInfo) {
					Logger.debug(logInfo, "toString"," Bundle ID in PriceInfo >>> "
							+ pI.getBundleId() + "Delete flag "
							+ pI.getDelFlag() + " update flag"
							+ pI.getUpdtFlag());
				}
			}
			if (configComponentInfo != null) {
				sb.append("ConfigComponentInfo array size :"
						+ configComponentInfo.length + "\n");
				for (ConfigComponentInfo pI : configComponentInfo) {
					Logger.debug(logInfo, "toString"," Bundle ID in ConfigComponentInfo >>> "
							+ pI.getBundleId() + "Delete flag "
							+ pI.getDelFlag() + " update flag"
							+ pI.getUpdtFlag());
				}
			}
			if (configDescription != null) {
				sb.append("ConfigDescription array size :"
						+ configDescription.length + "\n");
				for (ConfigDescription pI : configDescription) {
					Logger.debug(logInfo, "toString"," Bundle ID in ConfigDescription >>> "
							+ pI.getBundleId() + " update flag"
							+ pI.getUpdtFlag());
				}
			}
			if (configHeaderInfo != null) {
				sb.append("ConfigHeaderInfo array size :"
						+ configHeaderInfo.length + "\n");
				for (ConfigHeaderInfo pI : configHeaderInfo) {
					Logger.debug(logInfo, "toString"," Bundle ID in ConfigHeaderInfo >>> "
							+ pI.getBundleId() + "Delete flag "
							+ pI.getDelFlag() + " update flag"
							+ pI.getUpdtFlag());
				}
			}
			if (configPermutationInfo != null) {
				sb
						.append("ConfigPermutationInfo array size :"
								+ configPermutationInfo.length + "\n");
				for (ConfigPermutationInfo pI : configPermutationInfo) {
					Logger.debug(logInfo, "toString"," Bundle ID in ConfigPermutationInfo >>> "
									+ pI.getBundleId() + "Delete flag "
									+ pI.getDelFlag() + " update flag"
									+ pI.getUpdtFlag());
				}
			}
			if (configPermutationPriceInfo != null) {
				sb
						.append("ConfigPermutationPriceInfo array size :"
								+ configPermutationPriceInfo.length + "\n");
				for (ConfigPermutationPriceInfo pI : configPermutationPriceInfo) {
					Logger.debug(logInfo, "toString"," Bundle ID in configPermutationPriceInfo >>> "
									+ pI.getBundleId()
									+ " Price id "
									+ pI.getPriceId()
									+ "Delete flag "
									+ pI.getDelFlag()
									+ " update flag"
									+ pI.getUpdtFlag());
				}
			}
			if (productDescription != null) {
				sb.append("CroductDescription array size :"
						+ productDescription.length + "\n");
				for (ProductDescription pI : productDescription) {
					Logger.debug(logInfo, "toString"," Product ID in productDescription >>> "
							+ pI.getProductId() + " update flag"
							+ pI.getUpdtFlag());
				}
			}
			if (configPriceHeaderInfo != null) {
				sb
						.append("ConfigPriceHeaderInfo array size :"
								+ configPriceHeaderInfo.length + "\n");
				for (ConfigPriceHeaderInfo pI : configPriceHeaderInfo) {
					Logger.debug(logInfo, "toString"," Bundle ID in configPriceHeaderInfo >>> "
									+ pI.getBundleId() + "Delete flag "
									+ pI.getDelFlag() + " update flag"
									+ pI.getUpdtFlag());
				}

			}

			sb.append("]");

		} catch (Exception e) {
			e.printStackTrace();
		}
		return sb.toString();
	}
	
	
	public void printItemsInRelationMap(){
		int i =1;
		if (listMapRelations != null){
			for (Map map : listMapRelations){
				
				System.out.println("Iteration ... "+i);
				Set<CoronaBaseObject> keys = (Set<CoronaBaseObject>) map.keySet();
				if (keys != null){
					for (CoronaBaseObject cboKeyObject : keys ){
						System.out.println("Parent key --- > "+cboKeyObject.getType());
						List <? extends CoronaBaseObject> listObj = (List<CoronaBaseObject> ) map.get(cboKeyObject);
						for (CoronaBaseObject  cboObj : listObj ){
								System.out.println("  Child Key -- > "+cboObj.getType());
								System.out.println("       "+cboObj.toDebugString(cboObj));
						}
					}
				}
				i++;
			}
		}
	}
}
