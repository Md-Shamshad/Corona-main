/**
 * 
 */
package com.hp.psg.corona.propagation.dao;

import com.hp.psg.common.db.util.DaxManager;
import com.hp.psg.common.error.CoronaException;
import com.hp.psg.corona.common.cto.beans.ConfigPermutationPriceInfo;

/**
 * @author dholakia
 * @version 1.0
 */
public class FuturePriceDao {

	public static String FUTURE_PRICE_PROPAGATION = "FUTURE_PRICE_PROPAGATION";
	public static String VALID_FUTURE_PRICE_UPDATE = "VALID_FUTURE_PRICE_UPDATE";
	public static String INVALID_FUTURE_PRICE_UPDATE = "INVALID_FUTURE_PRICE_UPDATE";

	public static void updateInvalidPrice(ConfigPermutationPriceInfo priceInfo)
			throws CoronaException, Exception {

		DaxManager.getDAOMgr().executeUpdate(FUTURE_PRICE_PROPAGATION,
				INVALID_FUTURE_PRICE_UPDATE, priceInfo);

	}

	public static void updateValidPrice(ConfigPermutationPriceInfo priceInfo)
			throws CoronaException, Exception {

		DaxManager.getDAOMgr().executeUpdate(FUTURE_PRICE_PROPAGATION,
				VALID_FUTURE_PRICE_UPDATE, priceInfo);

	}

	public static ConfigPermutationPriceInfo getPriceInfo(
			ConfigPermutationPriceInfo cppi) {
		return new ConfigPermutationPriceInfo();
	}

}
