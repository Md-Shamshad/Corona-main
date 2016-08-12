package com.hp.psg.corona.common.util;

import com.hp.psg.common.util.logging.LogManager;
import com.hp.psg.common.util.logging.LoggerInfo;

/**
 * @author dudeja
 * @version 1.0
 * 
 */
public class Logger  {

	public static void info(String className, String methodName, String msg) {
		info(new LoggerInfo(className), methodName, msg);
	}

	public static void error(String className, String methodName, String msg,
			Throwable t) {
		error(new LoggerInfo(className), methodName, msg, t);
	}

	public static void error(String className, String methodName, Throwable t) {
		error(new LoggerInfo(className), methodName, null, t);
	}

	public static void debug(String className, String methodName, String msg) {
		debug(new LoggerInfo(className), methodName, msg);
	}

	public static void info(LoggerInfo logInfo, String methodName, String msg) {
		try {
			LogManager.info(logInfo, methodName, msg);
		} catch (Throwable th) {
			CoronaFwkUtil.println("********Error in info of Logger");
			th.printStackTrace();
		}
	}

	public static void perfInfo(LoggerInfo logInfo, String methodName, String msg) {
		try {
			if (Config.doPerformanceTimeLogging())
				LogManager.perfInfo(logInfo, methodName, msg);
		} catch (Throwable th) {
			CoronaFwkUtil.println("********Error in info of Logger");
			th.printStackTrace();
		}
	}

	
	public static void error(LoggerInfo logInfo, String methodName, String msg,
			Throwable t) {
		try {
			LogManager.error(logInfo, methodName, msg, t);
		} catch (Throwable th) {
			CoronaFwkUtil.println("********Error in info of Logger");
			th.printStackTrace();
		}
	}

	public static void error(LoggerInfo logInfo, String methodName, Throwable t) {
		try {
			LogManager.error(logInfo, methodName, null, t);
		} catch (Throwable th) {
			CoronaFwkUtil.println("********Error in info of Logger");
			th.printStackTrace();
		}
	}

	public static void debug(LoggerInfo logInfo, String methodName, String msg) {
		try {
			LogManager.debug(logInfo, methodName, msg);
		} catch (Throwable th) {
			CoronaFwkUtil.println("********Error in info of Logger");
			th.printStackTrace();
		}
	}
	
	// Logger information for the Replication application.
	
	public static void repInfo(String className, String methodName, String msg) {
		repInfo(new LoggerInfo(className), methodName, msg);
	}

	public static void repError(String className, String methodName, String msg,
			Throwable t) {
		repError(new LoggerInfo(className), methodName, msg, t);
	}

	public static void repError(String className, String methodName, Throwable t) {
		repError(new LoggerInfo(className), methodName, null, t);
	}

	public static void repDebug(String className, String methodName, String msg) {
		repDebug(new LoggerInfo(className), methodName, msg);
	}

	public static void repInfo(LoggerInfo logInfo, String methodName, String msg) {
		try {
			LogManager.repInfo(logInfo, methodName, msg);
		} catch (Throwable th) {
			CoronaFwkUtil.println("********Error in info of Logger");
			th.printStackTrace();
		}
	}

	public static void repPerfInfo(LoggerInfo logInfo, String methodName, String msg) {
		try {
			if (Config.doPerformanceTimeLogging())
				LogManager.repPerfInfo(logInfo, methodName, msg);
		} catch (Throwable th) {
			CoronaFwkUtil.println("********Error in info of Logger");
			th.printStackTrace();
		}
	}

	
	public static void repError(LoggerInfo logInfo, String methodName, String msg,
			Throwable t) {
		try {
			LogManager.repError(logInfo, methodName, msg, t);
		} catch (Throwable th) {
			CoronaFwkUtil.println("********Error in info of Logger");
			th.printStackTrace();
		}
	}

	public static void repError(LoggerInfo logInfo, String methodName, Throwable t) {
		try {
			LogManager.repError(logInfo, methodName, null, t);
		} catch (Throwable th) {
			CoronaFwkUtil.println("********Error in info of Logger");
			th.printStackTrace();
		}
	}

	public static void repDebug(LoggerInfo logInfo, String methodName, String msg) {
		try {
			LogManager.repDebug(logInfo, methodName, msg);
		} catch (Throwable th) {
			CoronaFwkUtil.println("********Error in info of Logger");
			th.printStackTrace();
		}
	}	
}
