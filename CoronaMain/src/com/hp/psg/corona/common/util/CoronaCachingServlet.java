package com.hp.psg.corona.common.util;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;

import com.hp.psg.common.ctx.util.CTX;
import com.hp.psg.common.util.logging.LogManager;
import com.hp.psg.common.util.logging.LoggerInfo;
import com.hp.psg.corona.common.handler.CachingModulesCallHandler;
import com.hp.psg.corona.error.util.CoronaErrorHandler;

/**
 * @author dudeja
 * @version 1.0
 * 
 */
public class CoronaCachingServlet extends HttpServlet {
	LoggerInfo logInfo = new LoggerInfo(
			"com.hp.psg.corona.evf.handler.EvfCachingServlet");
	final String CACHE_LOAD_ERROR_MESSAGE = "Error in loading cache";
	public void init(ServletConfig config) throws ServletException {
		super.init(config);
		CTX ctx = CTX.getCTX();
		try {

			String cacheModuleNames[] = new String[]{"FormatKeys",
					"EventMappingRules", "RegionalAttributeDefaults",
					"DataChangeCallHandlerMethodMap",
					"PropagationCallHandlerMethodMap", "CtoPluginMap",
					"CountryCodesValues", "CurrencyValues", "PriceTermValues","ModuleMap","KeyBeanMap",
					"ReplicationInfo"};

			for (String moduleName : cacheModuleNames) {
				cacheModule(moduleName);
			}

		} catch (Exception e) {
			LogManager.fatal(CACHE_LOAD_ERROR_MESSAGE, e);
			CoronaErrorHandler.logError(e, "Error while loading cache", "CACHE_EXCEP");

		}
	}

	private void cacheModule(String moduleName) throws Exception {
		Class clName = Class
				.forName("com.hp.psg.corona.common.handler.CachingModulesCallHandler");
		String cacheMethodName = "cache" + moduleName;

		try {
			java.lang.reflect.Method mthd = clName.getMethod(cacheMethodName);
			mthd.invoke(new CachingModulesCallHandler());

		} catch (Exception ex) {
			ex.printStackTrace();
			CoronaErrorHandler.logError(clName, "CacheErr", "cacheModule",
					"Error while loading cache" + cacheMethodName, false, ex,
					true);
		}
	}

}
