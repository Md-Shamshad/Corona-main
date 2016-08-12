package com.hp.psg.corona.datachange.plugin.interfaces;

import com.hp.psg.common.error.CoronaException;

public interface IDataChangeEventsCallHandler {

	public void processConfigPriceDataChange(Long dceId)
	throws CoronaException;

	public void processListPriceUpdate(Long dceId)
	throws CoronaException;

	public void processListPriceInsert(Long dceId)
	throws CoronaException ;

}
