package com.hp.psg.corona.error.util;

import com.hp.psg.common.error.ErrorHandleException;
import com.hp.psg.common.error.util.CoronaErrorConstants;
import com.hp.psg.common.error.util.ErrorHandleUtil;
import com.hp.psg.common.error.util.ErrorHandler;
import com.hp.psg.common.util.logging.LoggerInfo;
import com.hp.psg.corona.common.util.Config;
import com.hp.psg.corona.common.util.Logger;

/**
 * @author dudeja
 * @version 1.0
 * 
 */

public class CoronaErrorHandler extends Exception {

	private Exception exception = null;
	private static LoggerInfo log = new LoggerInfo("FatalLoadException");

	public CoronaErrorHandler(String message) {
		super(message);
	}

	public CoronaErrorHandler(String message, Exception e) {
		super(message);
		exception = e;
	}

	public Exception getException() {
		return exception;
	}

	public String getMessage() {

		if (exception != null) {
			return (exception.getMessage() + " : " + super.getMessage());
		} else {
			return (super.getMessage());
		}
	}

	public void printStackTrace() {

		if (exception != null) {
			exception.printStackTrace();
		}
	}

	public static void logError(Class className, String errorCode,
			String methodName, String errMessage, boolean XMLMsg, Throwable e,
			boolean sendNotification) {
		String currentMethod = "error";
		ErrorHandleException eh = null;
		try {
			// TODO : write now making send notification to be parameterized for
			// testing purpose.
			sendNotification = new Boolean(Config.getSendNotification())
					.booleanValue();
			if (null != errMessage && errMessage.length() > 0) {
				if (XMLMsg) {
					eh = ErrorHandleUtil
							.XmlErrorStringToErrorHandleException(errMessage);
				} else {
					eh = new ErrorHandleException(errorCode, errMessage,
							className.getName(), methodName, e);
				}

				if (sendNotification)
					eh.setNotificationRequired(sendNotification);

				ErrorHandler.handleError(eh);
			}

		} catch (Throwable th) {
			Logger.error("EvfErrorHandler", "logError", th);
		}
	}

	public static void logError(String errorCode,
			String errMessage, Throwable e) {

		ErrorHandleException eh = null;
		try {
			// TODO : write now making send notification to be parameterized for
			// testing purpose.
			boolean sendNotification = new Boolean(Config.getSendNotification())
					.booleanValue();

			if (null != errMessage && errMessage.length() > 0) {
					eh = new ErrorHandleException(errorCode, errMessage,
							e.getStackTrace()[0].getClassName(), e.getStackTrace()[0].getMethodName(), e);
				
				if (sendNotification)
					eh.setNotificationRequired(sendNotification);

				ErrorHandler.handleError(eh);
				
			}

		} catch (Throwable th) {
			Logger.error("EvfErrorHandler", "logError", th);
		}
		//Printing strack trace for now for debugging purpose.
		e.printStackTrace();
		
	}

	public static void logError(Throwable e, String errMessage, String errorCode) {

		ErrorHandleException eh = null;
		
		if (null == errMessage) {
			errMessage=e.getMessage();
		}
		
		if (null == errorCode) {
			errorCode=CoronaErrorConstants.defaultErrorCode;
		}
		
		try {
			// TODO : write now making send notification to be parameterized for
			// testing purpose.
			boolean sendNotification = new Boolean(Config.getSendNotification())
					.booleanValue();

			if (null != errMessage && errMessage.length() > 0) {
					eh = new ErrorHandleException(errorCode, errMessage,
							e.getStackTrace()[0].getClassName(), e.getStackTrace()[0].getMethodName(), e);
				
				if (sendNotification)
					eh.setNotificationRequired(sendNotification);

				ErrorHandler.handleError(eh);
			}

		} catch (Throwable th) {
			Logger.error("EvfErrorHandler", "logError", th);
		}

		//Printing strack trace for now for debugging purpose.
		e.printStackTrace();

	}

}
