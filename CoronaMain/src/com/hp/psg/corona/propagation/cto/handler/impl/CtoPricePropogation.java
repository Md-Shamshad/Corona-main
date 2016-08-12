package com.hp.psg.corona.propagation.cto.handler.impl;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.hp.psg.common.error.CoronaException;
import com.hp.psg.common.util.logging.LoggerInfo;
import com.hp.psg.corona.common.beans.CTODaxDataBeanGeneral;
import com.hp.psg.corona.common.constants.CTOConstants;
import com.hp.psg.corona.common.cto.beans.ConfigComponentInfo;
import com.hp.psg.corona.common.cto.beans.ConfigHeaderInfo;
import com.hp.psg.corona.common.cto.beans.ConfigPermutationInfo;
import com.hp.psg.corona.common.cto.beans.ConfigPriceHeaderInfo;
import com.hp.psg.corona.common.cto.beans.CoronaBaseObject;
import com.hp.psg.corona.common.cto.beans.PriceInfo;
import com.hp.psg.corona.common.util.CoronaFwkUtil;
import com.hp.psg.corona.propagation.handler.interfaces.IBeanPropagation;
import com.hp.psg.corona.propagation.handler.util.ConfigDataChangeUtil;


/*
* @author sjoshi
* @version 1.0
*
*/
public class CtoPricePropogation implements IBeanPropagation 
{

	LoggerInfo logInfo=null;
	
	private static final String PRICE_INFO = "PriceInfo";
	private static final String CPHI = "ConfigPriceHeaderInfo";
	private static final String CCI = "ConfigComponentInfo";
	private static final String CPI ="ConfigPermutationInfo";
	
	public CtoPricePropogation() {
		// TODO Auto-generated constructor stub
		logInfo = new LoggerInfo (this.getClass().getName());
	}
	public CTODaxDataBeanGeneral processBeans(CTODaxDataBeanGeneral generalBean)
									throws CoronaException 
	{
		if (generalBean == null) {
			throw new CoronaException(
					"CtoPricePropogation:: NULL Input CTODaxDataBeanGeneral has been passed as null");
		}
		
		String valueInType = null;
		Map<CoronaBaseObject, List<? extends CoronaBaseObject>> header2ComponentMap = Collections.EMPTY_MAP;
		Map<String, List<? extends CoronaBaseObject>> priceInfoMap = Collections.EMPTY_MAP;;
		Map<CoronaBaseObject, List<? extends CoronaBaseObject>> header2PermutationMap = Collections.EMPTY_MAP;;
		Map<CoronaBaseObject, List<? extends CoronaBaseObject>> header2PriceHeaderMap = Collections.EMPTY_MAP;;		
		List<Map<CoronaBaseObject, List<? extends CoronaBaseObject>>> myMappingList ;
		if (generalBean.getListMapRelations() != null)
			myMappingList = generalBean.getListMapRelations();
		else throw new CoronaException("CTODaxDataBeanGeneral getListMapRelations cannot have null ");
		List<Map<String, List<? extends CoronaBaseObject>>> myObjectsMappingList;
		if (generalBean.getListObjectsOnKeys() != null)
			myObjectsMappingList = generalBean.getListObjectsOnKeys();
		else throw new CoronaException("CTODaxDataBeanGeneral getListObjectsOnKeys cannot have null ");
		
		//Get Map of Objects On String Keys
		for (Iterator<Map<String, List<? extends CoronaBaseObject>>> iterator = 
			myObjectsMappingList.iterator(); iterator.hasNext();)
		{
			Map<String, List<? extends CoronaBaseObject>> myObjectMap = iterator.next();
			
			
			String childType = null;
			if (myObjectMap != null && myObjectMap.size() > 0){

				Set<String> cboKeySet = myObjectMap.keySet();
				if (cboKeySet.size()>0){
					//take first element
					for (String cboName :cboKeySet){
						List<? extends CoronaBaseObject> cboList = myObjectMap.get(cboName);
						if (cboList != null && cboList.size() > 0){
							childType = cboList.get(0).getType();
						}
						break;
					}
				}
			}
//			String childType = CoronaFwkUtil.getTypeOnKeysMap(myObjectMap);
//			
//			if (childType== null ||childType.equals("") ){
//				throw new CoronaException("CTODaxDataBeanGeneral CoronaFwkUtil.getTypeOnKeysMap cannot have null ");
//			}
			 
			if ((PRICE_INFO).equals(childType))
				priceInfoMap = myObjectMap;
			
		}
		
		//Get Map of Objects on Object Keys
		for (Iterator<Map<CoronaBaseObject, List<? extends CoronaBaseObject>>> iterator = 
			myMappingList.iterator(); iterator.hasNext();)
		{
			Map<CoronaBaseObject, List<? extends CoronaBaseObject>> myMap = iterator.next();
			valueInType = CoronaFwkUtil.getType(myMap);
			if (valueInType== null){
				throw new CoronaException("CTODaxDataBeanGeneral CoronaFwkUtil.getType(myMap) is null ");
			}
			if (CPHI.equals(valueInType))
				header2PriceHeaderMap = myMap;
			else if (CCI.equals(valueInType))
				header2ComponentMap = myMap;
			else if (CPI.equals(valueInType))
				header2PermutationMap = myMap;
		}
		
		ConfigHeaderInfo[] chi = generalBean.getConfigHeaderInfo();;
		if (chi== null)
			throw new CoronaException("CTODaxDataBeanGeneral generalBean.getConfigHeaderInfo() is null ");
		
		for (int i=0; i< chi.length; i++)
		{
			ConfigHeaderInfo configHeader  = chi[i];
			List<ConfigPermutationInfo> permutationList = 
				(List<ConfigPermutationInfo>)header2PermutationMap.get(configHeader);
			List<ConfigComponentInfo> componentList = 
				(List<ConfigComponentInfo>)header2ComponentMap.get(configHeader);
			processForPriceInfo(permutationList,componentList, header2PriceHeaderMap, priceInfoMap, generalBean);
		}
//		CoronaFwkUtil.println(generalBean.toString());
//		if (generalBean.getPriceInfo() != null){
//			for (PriceInfo pI : generalBean.getPriceInfo()){
//				Logger.debug(" Product ID >>> "+pI.getProductId());
//			}
//		}
		return generalBean;		
	}
    
	public void processForPriceInfo (List<ConfigPermutationInfo> permutationList,
									List<ConfigComponentInfo>componentList,
									Map<CoronaBaseObject, List<? extends CoronaBaseObject>> priceHeaderMap,
									Map<String, List<? extends CoronaBaseObject>> priceInfoMap,
									CTODaxDataBeanGeneral generalBean)
	{
		PriceInfo pi ;		
		ConfigPermutationInfo cpi;
		ConfigComponentInfo cci;

		if (permutationList !=null && permutationList.size() > 0) 
		{	
			List <ConfigPriceHeaderInfo> cphiList = new ArrayList<ConfigPriceHeaderInfo>();
			List <PriceInfo> piList = new ArrayList<PriceInfo>();
			for (Iterator<ConfigPermutationInfo> cpiIterator = permutationList.iterator();
						cpiIterator.hasNext();)
			{
				cpi = cpiIterator.next();
				
				//Get ConfigPriceHeaderInfo from ConfigPermutationInfo and 
				//check if needs to be modified or added
				List<ConfigPriceHeaderInfo> priceHeaderList = (List<ConfigPriceHeaderInfo>) priceHeaderMap.get(cpi);

				ConfigPriceHeaderInfo priceHeaderInfo;
				if(priceHeaderList != null && priceHeaderList.size() > 0)
				{
					for (Iterator<ConfigPriceHeaderInfo> cphiIterator = priceHeaderList.iterator();
								cphiIterator.hasNext();)		
					{						
						priceHeaderInfo = cphiIterator.next();
						if (! priceHeaderInfo.getDelFlag().equals(cpi.getDelFlag()))
						{
							priceHeaderInfo.setDelFlag(cpi.getDelFlag());
							priceHeaderInfo.setUpdtFlag(CTOConstants.UPDT_FLAG_MODIFIED);						
						}
						else
						{
							priceHeaderInfo.setUpdtFlag(CTOConstants.UPDT_FLAG_UNCHANGED);						
						}		
					}
				}
				else
				{
					
					priceHeaderInfo = new ConfigPriceHeaderInfo();
					constructConfigPriceHeaderInfo(cpi,priceHeaderInfo );
					cphiList.add(priceHeaderInfo);
				}
				
				//Go through ConfigComponentInfo and ConfigPermutationInfo and check 
				//if PriceInfo needs to be updated or added
				if (componentList !=null && componentList.size() > 0)
				{
					StringBuffer strBuffer = new StringBuffer();
					strBuffer.append(cpi.getBundleId());
					strBuffer.append("|");
					strBuffer.append(cpi.getShipToCountry());
					strBuffer.append("|");
					strBuffer.append(cpi.getPriceId());
					strBuffer.append("|");
					strBuffer.append(cpi.getPriceDescriptor());
					strBuffer.append("|");
					
					String key1 = strBuffer.toString();
					strBuffer = null;
					
					for (Iterator<ConfigComponentInfo> cciIterator = componentList.iterator();
									cciIterator.hasNext();)		
					{
						cci = cciIterator.next();
						String productId = cci.getProductId();
						
						String key= key1+productId;		

						if (priceInfoMap.containsKey(key))
						{
							List<PriceInfo> priceInfoList=
								(List<PriceInfo>)priceInfoMap.get(key);	
							if(priceInfoList != null && priceInfoList.size() > 0)
							{
								for (Iterator<PriceInfo> piIterator = priceInfoList.iterator();
										piIterator.hasNext();)		
								{								
									PriceInfo priceInfo = piIterator.next();
									if (! priceInfo.getDelFlag().equals(cci.getDelFlag()))
									{
										priceInfo.setDelFlag(cci.getDelFlag());
										priceInfo.setUpdtFlag(CTOConstants.UPDT_FLAG_MODIFIED);
									}
									else
									{
										//priceInfo.setUpdtFlag(CTOConstants.UPDT_FLAG_UNCHANGED);
										piIterator.remove();
									}
								}
							}
						}
						else
						{							
							pi = new PriceInfo();
							pi.setBundleId(cpi.getBundleId());
							pi.setShipToCountry(cpi.getShipToCountry());
							pi.setProductId(productId);
							pi.setPriceDescriptor(cpi.getPriceDescriptor());
							pi.setPriceId(cpi.getPriceId());
							int index = productId.indexOf(CTOConstants.PRODUCT_ID_DELIMITER);
							if (index > 0) {
								pi.setProductNumber(productId.substring(0, index));
								pi.setProductOptionCd(productId.substring(index + 1, productId.length()));
							} else {
								pi.setProductNumber(productId);
							}

							pi.setDelFlag(cci.getDelFlag());
							pi.setUpdtFlag(CTOConstants.UPDT_FLAG_ADD);

							//Revisit
							pi.setLastModifiedBy(this.getClass().getSimpleName().substring(0, 19));
							pi.setPriceStartDate(new Timestamp(new Date().getTime()));
							pi.setPriceEndDate(new Timestamp(new Date().getTime()));
							pi.setTrnId(new Long(0));

							piList.add(pi);						
						}
					}
				}
			}
			//set the CPHI array
			int oldLen = generalBean.getConfigPriceHeaderInfo().length;
			int newLen = oldLen + cphiList.size();
			ConfigPriceHeaderInfo[] cdesc = new ConfigPriceHeaderInfo[newLen];
			
			if (generalBean.getConfigPriceHeaderInfo() != null && oldLen > 0)
				System.arraycopy(generalBean.getConfigPriceHeaderInfo(), 0, cdesc,0,oldLen);
			
			for(int k=0; k<cphiList.size(); k++ ){
				cdesc[oldLen+k] = cphiList.get(k);
			}
			generalBean.setConfigPriceHeaderInfo(cdesc);
			
			//set the PI array
			oldLen = generalBean.getPriceInfo().length;
			newLen = generalBean.getPriceInfo().length + piList.size();
			PriceInfo[] pidesc = new PriceInfo[newLen];
			if (generalBean.getPriceInfo() != null && oldLen > 0)
				System.arraycopy(generalBean.getPriceInfo(), 0, pidesc,0,oldLen);
			
			for(int k=0; k< piList.size(); k++){
				pidesc[oldLen+k] = piList.get(k);
			}
			generalBean.setPriceInfo(pidesc);	
			
		}
	}
	
	
	public void constructPriceInfo(ConfigPermutationInfo cpi, 
			ConfigComponentInfo cci, PriceInfo pi)
	{
		String productId = cci.getProductId();		
		pi.setProductNumber(ConfigDataChangeUtil.getProductNumberFromProductId(productId));
		pi.setProductOptionCd(ConfigDataChangeUtil.getOptionCdFromProductId(productId));
		pi.setUpdtFlag(CTOConstants.UPDT_FLAG_ADD);
	}
	
	public void constructConfigPriceHeaderInfo(ConfigPermutationInfo cpi, 
			 ConfigPriceHeaderInfo priceHeaderInfo)
	{
		String priceDesc = cpi.getPriceDescriptor();
		System.out.println("Sending Price descriptor >>>>>>>>>>>>>>>>>> "+priceDesc );
		
		String tempPrsCountryCode=ConfigDataChangeUtil.getPrsCountryCodeFromGpsyCountry((cpi.getShipToCountry()));
		priceHeaderInfo.setShipToGeo((tempPrsCountryCode != null && !"".equals(tempPrsCountryCode.trim()))? tempPrsCountryCode :"TR");
		
		priceHeaderInfo.setDelFlag(CTOConstants.DELETE_FLAG_NOT_SET);
		priceHeaderInfo.setLastModifiedBy(this.getClass().getSimpleName().substring(0, 19));
		priceHeaderInfo.setBundleId(cpi.getBundleId());
		priceHeaderInfo.setShipToCountry(cpi.getShipToCountry());
		priceHeaderInfo.setPriceDescriptor(cpi.getPriceDescriptor());
		priceHeaderInfo.setPriceId(cpi.getPriceId());					
		
		String tempPriceGeo=ConfigDataChangeUtil.getPriceGeoFromPriceDesc((priceDesc));
		priceHeaderInfo.setPriceGeo((tempPriceGeo != null && !"".equals(tempPriceGeo.trim())) ? tempPriceGeo :"Test");

		priceHeaderInfo.setPriceListType(ConfigDataChangeUtil.getPriceListFromPriceDesc(priceDesc));
		
		String tempCurrCd=ConfigDataChangeUtil.getCurrencyFromPriceDesc((priceDesc));
		priceHeaderInfo.setCurrency((tempCurrCd != null && !"".equals(tempCurrCd.trim())) ? tempCurrCd :"Test");

		priceHeaderInfo.setIncoTerm(ConfigDataChangeUtil.getIncoTermFromPriceDesc(priceDesc));
		priceHeaderInfo.setPriceIdType(cpi.getPriceIdType());
		
		priceHeaderInfo.setPriceProcedure((cpi.getPriceProcedure() != null && !"".equals(cpi.getPriceProcedure().trim())) ? cpi.getPriceProcedure():
			                              "Test" );
		
		try
		{
			String recipient = ConfigDataChangeUtil.getPricingRecipient(cpi.getShipToCountry(), cpi.getRegionCode(), cpi.getPriceProcedure());
			priceHeaderInfo.setRecipient((recipient!= null && !"".equals(recipient.trim())) ? recipient:  "recipient");
		}
		catch (CoronaException ex)
		{
			System.out.println(ex.getMessage());
		}					
		priceHeaderInfo.setUpdtFlag(CTOConstants.UPDT_FLAG_ADD);	
	}
	public void prepareBean(Map<String, Object> constructArgs) {
		// TODO Auto-generated method stub
		
	}	
}
