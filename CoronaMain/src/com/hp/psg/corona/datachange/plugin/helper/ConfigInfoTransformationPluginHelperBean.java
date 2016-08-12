package com.hp.psg.corona.datachange.plugin.helper;

/**
 * @author rnath
 * @version 1.0
 *
 */
public class ConfigInfoTransformationPluginHelperBean {

	public String priceSource;
	public String dealNumber;
	public String agreement;
	public String tierId;
	public String dealType;

	public ConfigInfoTransformationPluginHelperBean() {

	}

	public String getPriceSource() {
		return priceSource;
	}
	public void setPriceSource(String priceSource) {
		this.priceSource = priceSource;
	}
	public String getDealNumber() {
		return dealNumber;
	}
	public void setDealNumber(String dealNumber) {
		this.dealNumber = dealNumber;
	}
	public String getAgreement() {
		return agreement;
	}
	public void setAgreement(String agreement) {
		this.agreement = agreement;
	}
	public String getTierId() {
		return tierId;
	}
	public void setTierId(String tierId) {
		this.tierId = tierId;
	}
	public String getDealType() {
		return dealType;
	}
	public void setDealType(String dealType) {
		this.dealType = dealType;
	}
}
