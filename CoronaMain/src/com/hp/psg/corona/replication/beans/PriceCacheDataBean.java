package com.hp.psg.corona.replication.beans;

//import java.util.Date;
import java.io.Serializable;
import java.util.Date;

import com.hp.psg.corona.common.cto.beans.CoronaBaseObject;
import com.hp.psg.corona.common.cto.beans.PriceInfo;

public class PriceCacheDataBean extends CoronaBaseObject
implements
Serializable,
Comparable<CoronaBaseObject> {
	private static final long serialVersionUID = 1L;
	private int configId;
	//private String configIdStr;	
	private String priceId;
	private String shipToCountry;
	private String priceDescriptor;
	private String materialId;
	private String priceSource;
	private String listPrice;
	private String netPrice;
	private String additionalData;
	private Date priceStartDate; 
	private Date priceEndDate;  
	private Date lastModifiedDate;
	private String lastModifiedDateStr;
	private String delFlag; 
	private String configIdStr;
	
	private Long priceInfoId;
	
	
	public PriceCacheDataBean(){}
	public PriceCacheDataBean(PriceInfo priceInfo){
		this.configId = Integer.parseInt(priceInfo.getBundleId());
		this.priceId = priceInfo.getPriceId();
		this.shipToCountry = priceInfo.getShipToCountry();
		this.priceDescriptor = priceInfo.getPriceDescriptor();
		this.materialId = priceInfo.getProductId();
		this.priceSource = priceInfo.getPriceSource();
		this.listPrice = priceInfo.getListPrice()+"";
		this.netPrice = priceInfo.getNetPrice()+"";
		this.additionalData = priceInfo.getWhereFromConfig();
		this.priceStartDate = new Date(priceInfo.getPriceStartDate().getTime());
		this.priceEndDate = new Date(priceInfo.getPriceEndDate().getTime());
		this.lastModifiedDate = new Date(priceInfo.getLastModifiedDate().getTime());
		this.priceInfoId = priceInfo.getId();
		this.delFlag = priceInfo.getDelFlag();
		
	}
	public void setConfigId(int id)
	{
		this.configId = id;
	}
	public void setPriceInfoId(Long id)
	{
		this.priceInfoId = id;
	}
	public void setPriceId(String value){
		this.priceId = value;
	}
	public void setShipToCountry(String value){
		this.shipToCountry = value;
	}
	public void setPriceDescriptor(String value){
		this.priceDescriptor = value;
	}
	public void setMaterialId(String value){
		this.materialId = value;
	}
	public void setPriceSource(String value){
		this.priceSource = value;
	}
	public void setListPrice(String value){
		this.listPrice = value;
	}
	public void setNetPrice(String value){
		this.netPrice = value;
	}
	public void setAdditionalData(String value){
		this.additionalData = value;
	}
	public void setPriceStartDate(Date value){
		this.priceStartDate = value;
	}
	public void setPriceEndDate(Date value){
		this.priceEndDate = value;
	}
	public void setLastModifiedDate(Date value){
		this.lastModifiedDate = value;
	}
	
	public int getConfigId()
	{
		return this.configId;
	}
	public String getPriceId(){
		return this.priceId;
	}
	public String getShipToCountry(){
		return this.shipToCountry;
	}
	public String getPriceDescriptor(){
		return this.priceDescriptor;
	}
	public String getMaterialId(){
		return this.materialId;
	}
	public String getPriceSource(){
		return this.priceSource;
	}
	public String getListPrice(){
		return this.listPrice;
	}
	public String getNetPrice(){
		return this.netPrice;
	}
	public String getAdditionalData(){
		return this.additionalData;
	}
	public Date getPriceStartDate(){
		return this.priceStartDate;
	}
	public Date getPriceEndDate(){
		return this.priceEndDate;
	}

	public Long getPriceInfoId()
	{
		return this.priceInfoId;
	}

	public String getDelFlag() {
		return delFlag;
	}
	public void setDelFlag(String delFlag) {
		this.delFlag = delFlag;
	}
	public String getConfigIdStr() {
		return configIdStr;
	}
	public void setConfigIdStr(String configIdStr) {
		this.configId=Integer.parseInt(configIdStr);
		this.configIdStr = configIdStr;
	}
	
	public Date getLastModifiedDate() {
		return lastModifiedDate;
	}
	
	public String toString() {
		return super.toDebugString(this);
	}

	public int compareTo(CoronaBaseObject cbo) {
		return super.compareTo(this, cbo);
	}
	public String getLastModifiedDateStr() {
		return lastModifiedDateStr;
	}
	public void setLastModifiedDateStr(String lastModifiedDateStr) {
		this.lastModifiedDateStr = lastModifiedDateStr;
	}

	
}
