package com.hp.psg.corona.propagation.cto.handler.impl;

import java.util.Date;
import java.util.Iterator;
import java.util.List;

import com.hp.psg.corona.common.constants.CTOConstants;
import com.hp.psg.corona.common.cto.beans.PriceInfo;
import com.hp.psg.corona.common.util.Logger;
import com.hp.psg.corona.propagation.handler.util.ConfigDataChangeUtil;

public class CtoPriceInfoStatus {

	private boolean allPiHavePrices = false;
	private boolean anyPiHaveError = false;
	private boolean allPiHavePending = false;
	private boolean anyPiHavePending = false;
	private int pendingCounter = 0;
	private int errorCounter = 0;
	private int validCounter = 0;
	private Date currentStartDate = null;
	private Date currentEndDate = null;
	private StringBuffer pendingMsg = new StringBuffer();
	private StringBuffer errorMsg = new StringBuffer();							

	// setters and getters
	public boolean isAllPiHavePrices() {
		return allPiHavePrices;
	}
	public boolean isAnyPiHaveError() {
		return anyPiHaveError;
	}
	public boolean isAllPiHavePending() {
		return allPiHavePending;
	}
	public boolean isAnyPiHavePending() {
		return anyPiHavePending;
	}
	public int getPendingCounter() {
		return pendingCounter;
	}
	public int getValidCounter() {
		return validCounter;
	}
	public StringBuffer getPendingMsg() {
		return pendingMsg;
	}
	public StringBuffer getErrorMsg() {
		return errorMsg;
	}
	public Date getCurrentStartDate() {
		return currentStartDate;
	}
	public Date getCurrentEndDate() {
		return currentEndDate;
	}

	public void setPriceInfoStatus(List<PriceInfo> piList, Date priceDate)
	{
		allPiHavePrices = false;
		anyPiHaveError = false;
		allPiHavePending = false;
		anyPiHavePending = false;
		pendingCounter = 0;
		errorCounter = 0;
		validCounter = 0;
		currentStartDate = null;
		currentEndDate = null;
		pendingMsg = new StringBuffer();
		errorMsg = new StringBuffer();			
		Date maxStartDate = null;
		Date minEndDate = null;
		String prevProductId = null;
		boolean validForProduct = false;


		if (priceDate != null) {
			priceDate = ConfigDataChangeUtil.formatDate(priceDate);
			Logger.debug(CtoPriceInfoStatus.class.getName(),
					"setPriceInfoStatus", "Price Date "
							+ priceDate.toString()
							+ "before iterating through piList");
		}
		int piListSize = 0;
		for (Iterator<PriceInfo> piIterator = piList.iterator();
		piIterator.hasNext();)
		{	
			PriceInfo pi = piIterator.next();
			if (prevProductId == null)
				prevProductId = pi.getProductId();
			else
			{
				if (!prevProductId.equals(pi.getProductId()))
				{
					if (!validForProduct)
					{
						this.anyPiHaveError = true;
						errorCounter++;
						errorMsg.append("Price is missing for material ");
						errorMsg.append(prevProductId);
						errorMsg.append(". ");
					}
					prevProductId = pi.getProductId();
					validForProduct = false;
				}
				
			}

			//yls -todo use date comparsion instead of before and after
			// Current Price
			Date piStartDate = ConfigDataChangeUtil.formatDate(pi.getPriceStartDate());
			Date piEndDate = ConfigDataChangeUtil.formatDate(pi.getPriceEndDate());
			if (piStartDate.compareTo(priceDate) <= 0
					&& piEndDate.compareTo(priceDate) >= 0) {
				piListSize++;
				if (pi.getPriceStatus().equals(CTOConstants.ERROR_STATUS))
				{
					anyPiHaveError = true;
					errorCounter++;
					errorMsg.append(pi.getPriceErrorMsg()+" ");
					
					////////////////////////////////////////////////////////
					//get the start and end dates of the error record
					if (maxStartDate == null) {
						maxStartDate = pi.getPriceStartDate();
					}
					else
					{
						if (maxStartDate.before(pi.getPriceStartDate()))
								maxStartDate = pi.getPriceStartDate();
					}
					if (minEndDate == null) {
						minEndDate = pi.getPriceEndDate();
					}
					else
					{
						if (minEndDate.after(pi.getPriceEndDate()))
							minEndDate = pi.getPriceEndDate();
					}
					
					///////////////////////////////////////////////////////
				}
				else if(pi.getPriceStatus().equals(CTOConstants.PENDING_STATUS))
				{
					anyPiHavePending = true;
					pendingCounter++;
					Logger.debug(CtoPriceInfoStatus.class.getName(),
							"setPriceInfoStatus",pi.getProductId() +" has price missing. ");
					pendingMsg.append(pi.getProductId() +" has price missing. ");
				}
				else if(pi.getPriceStatus().equals(CTOConstants.VALID_STATUS))
				{
					//Get a list of all PI dates
					validCounter++;

					if (maxStartDate == null) {
						maxStartDate = pi.getPriceStartDate();
					}
					else
					{
						if (maxStartDate.before(pi.getPriceStartDate()))
								maxStartDate = pi.getPriceStartDate();
					}
					if (minEndDate == null) {
						minEndDate = pi.getPriceEndDate();
					}
					else
					{
						if (minEndDate.after(pi.getPriceEndDate()))
							minEndDate = pi.getPriceEndDate();
					}
					validForProduct = true;
					System.out.println(" validForProduct "+validForProduct+" "+pi.getProductId());
				
				}
			} // if correct date range								
		} //for all PI
	

		if (anyPiHavePending || anyPiHaveError){
			if (errorCounter>pendingCounter){
				anyPiHaveError = true;
			}else 
			if(anyPiHavePending){
				anyPiHaveError = false;
				if (pendingCounter == piListSize){
					anyPiHavePending = false;
					allPiHavePending = true;
				}
			}
		}
		else
			allPiHavePrices = true;

		
		this.currentStartDate = maxStartDate;
		this.currentEndDate = minEndDate;
		System.out.println("maxStartDate "+maxStartDate+" minEndDate "+minEndDate);

	}
}
