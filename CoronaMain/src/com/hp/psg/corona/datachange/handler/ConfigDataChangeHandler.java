package com.hp.psg.corona.datachange.handler;

import com.hp.psg.common.error.CoronaException;
import com.hp.psg.common.error.util.CoronaErrorConstants;
import com.hp.psg.common.util.logging.LoggerInfo;
import com.hp.psg.corona.common.util.Logger;
import com.hp.psg.corona.datachange.dao.DataChangeEventsDao;
import com.hp.psg.corona.error.util.CoronaErrorHandler;

/*
* @author dudeja
* @version 1.0
*
*/

public class ConfigDataChangeHandler {
	LoggerInfo logInfo=null;
	
	public ConfigDataChangeHandler() {
		// TODO Auto-generated constructor stub
		logInfo = new LoggerInfo (this.getClass().getName());
	}
	
	public void addConfigPermDataChangeEvents() {
		Logger.info(logInfo,"addConfigPermDataChangeEvents", "@@@@@@@@@ Start processing permutation events");
		try {
			DataChangeEventsDao.addConfigPermDataChangeEvents();
		} catch (CoronaException e) {
			CoronaErrorHandler.logError(this.getClass(),
					CoronaErrorConstants.addConfigPermDataChangeEvents,
					"addConfigPermDataChangeEvents", e.getMessage(), false, e,
					true);

		}
	}

	public void addConfigCntryDataChangeEvents() {
		Logger.info(logInfo,"addConfigCntryDataChangeEvents", "@@@@@@@@@ Start processing permutation events");
		try {
			DataChangeEventsDao.addConfigCntryDataChangeEvents();
		} catch (CoronaException e) {
			CoronaErrorHandler.logError(this.getClass(),
					CoronaErrorConstants.addConfigCntryDataChangeEvents,
					"addConfigCntryDataChangeEvents", e.getMessage(), false, e,
					true);
		}

	}

	public void addConfigPriceDataChangeEvents() {
		Logger.info(logInfo,"addConfigPriceDataChangeEvents", "@@@@@@@@@ Start processing permutation events");
		try {
			DataChangeEventsDao.addConfigPriceDataChangeEvents();
		} catch (CoronaException e) {
			CoronaErrorHandler.logError(this.getClass(),
					CoronaErrorConstants.addConfigPriceDataChangeEvents,
					"addConfigPriceDataChangeEvents", e.getMessage(), false, e,
					true);
		}
	}

	public void addProductDescDataChangeEvents() {
		Logger.info(logInfo,"addProductDescDataChangeEvents", "@@@@@@@@@ Start processing product descripton events");
		try {
			DataChangeEventsDao.addProductDescDataChangeEvents();
		} catch (CoronaException e) {
			CoronaErrorHandler.logError(this.getClass(),
					CoronaErrorConstants.addProductDescDataChangeEvents,
					"addProductDescDataChangeEvents", e.getMessage(), false, e,
					true);
		}
	}

	public void addListPriceDataChangeEvents() {
		Logger.info(logInfo,"addListPriceDataChangeEvents", "@@@@@@@@@ Start processing list price events");
		try {
			DataChangeEventsDao.addListPriceDataChangeEvents();
		} catch (CoronaException e) {
			CoronaErrorHandler.logError(this.getClass(),
					CoronaErrorConstants.addListPriceDataChangeEvents,
					"addListPriceDataChangeEvents", e.getMessage(), false, e,
					true);
		}
	}
	
}
