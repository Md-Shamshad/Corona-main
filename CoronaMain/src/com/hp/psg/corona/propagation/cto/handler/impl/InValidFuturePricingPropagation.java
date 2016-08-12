/**
 * 
 */
package com.hp.psg.corona.propagation.cto.handler.impl;

import java.util.Map;

import com.hp.psg.common.error.util.CoronaErrorConstants;
import com.hp.psg.corona.common.beans.CTODaxDataBeanGeneral;
import com.hp.psg.corona.common.beans.PropagationEvent;
import com.hp.psg.corona.common.cto.beans.ConfigPermutationPriceInfo;
import com.hp.psg.corona.error.util.CoronaErrorHandler;
import com.hp.psg.corona.propagation.handler.interfaces.IBeanPropagation;

/**
 * @author dholakia
 * @version 1.0
 */
public class InValidFuturePricingPropagation implements IBeanPropagation {

	public CTODaxDataBeanGeneral processBeans(CTODaxDataBeanGeneral cto) {

		try {

			PropagationEvent[] events = cto.getPropagationEvent();

			if (events != null && events.length < 1) {

				throw new Exception(
						"No event to process in invalid future price");

			}

			ConfigPermutationPriceInfo[] priceInfos = new ConfigPermutationPriceInfo[events.length];

			for (int i = 0; i < events.length; i++) {

				ConfigPermutationPriceInfo priceInfo = new ConfigPermutationPriceInfo();

				priceInfo.setPriceId(cto.getPriceId());
				priceInfo.setBundleId(cto.getBundleId());
				priceInfo.setShipToCountry(cto.getShipToCountry());
				priceInfo.setPriceDescriptor(cto.getPriceDescriptor());

				priceInfos[i] = priceInfo;

			}

			cto.setConfigPermutationPriceInfo(priceInfos);

		} catch (Exception e) {

			CoronaErrorHandler.logError(this.getClass(),
					CoronaErrorConstants.processingErr, " processBeans",
					"Error while processing for invalid future price", false,
					e, true);

		}

		return cto;

	}

	public void prepareBean(Map<String, Object> constructArgs) {
		// TODO Auto-generated method stub
		
	}
}
