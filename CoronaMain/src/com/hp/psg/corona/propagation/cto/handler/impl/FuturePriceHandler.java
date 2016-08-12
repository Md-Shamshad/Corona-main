package com.hp.psg.corona.propagation.cto.handler.impl;

import com.hp.psg.corona.common.beans.PropagationEvent;
import com.hp.psg.corona.common.cto.beans.ConfigPermutationPriceInfo;
import com.hp.psg.corona.propagation.dao.FuturePriceDao;

/*
* @author dholakia
* @version 1.0
*
*/
public class FuturePriceHandler {

	public void processValidPrice(PropagationEvent event) throws Exception {

		ConfigPermutationPriceInfo cppi = FuturePriceDao
				.getPriceInfo(processProcessType(event));

		FuturePriceDao.updateValidPrice(cppi);

	}

	public void processInvalidPrice(PropagationEvent event) throws Exception {

		ConfigPermutationPriceInfo cppi = FuturePriceDao
				.getPriceInfo(processProcessType(event));

		FuturePriceDao.updateInvalidPrice(cppi);

	}

	public ConfigPermutationPriceInfo processProcessType(PropagationEvent event)
			throws Exception {

		ConfigPermutationPriceInfo cppi = new ConfigPermutationPriceInfo();

		String[] params = event.getProcessType().split("^");

		if (params.length < 4) {
			throw new Exception("Process type is incorrect");
		}

		cppi.setPriceId(params[0]); // 1st parameter is Price Id
		cppi.setBundleId(params[1]); // 2nd paramtere is Bundle Id
		cppi.setShipToCountry(params[2]); // 3rd parameter is Ship to Country
		cppi.setPriceDescriptor(params[3]); // 4th paramtere is Price Descriptor

		return cppi;

	}

}
