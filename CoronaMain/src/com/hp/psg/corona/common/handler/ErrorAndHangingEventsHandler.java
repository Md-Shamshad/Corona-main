package com.hp.psg.corona.common.handler;

import java.util.Date;
import java.util.List;

import com.hp.psg.common.notification.ConfiguredNotificationRequest;
import com.hp.psg.common.notification.client.NotificationAgent;
import com.hp.psg.common.util.logging.LogManager;
import com.hp.psg.common.util.logging.LoggerInfo;
import com.hp.psg.corona.common.beans.DataChangeEvent;
import com.hp.psg.corona.common.beans.PropagationEvent;
import com.hp.psg.corona.common.util.Logger;
import com.hp.psg.corona.datachange.dao.DataChangeEventsFwkDao;
import com.hp.psg.corona.propagation.dao.PropagationEventsFwkDao;

/*
* @author dudeja
* @version 1.0
*
*/
public class ErrorAndHangingEventsHandler {
	
	private LoggerInfo logInfo; 

	public static String ERROR_NOTIFICATION_CODE = "CoronaErrorEvents";
	public static String HANING_NOTIFICATION_CODE = "CoronaHangingEvents";

	public void processErrorAndHangingEvents() {
		logInfo = new LoggerInfo (this.getClass().getName());
		
		Logger.debug(logInfo,"processErrorAndHangingEvents","Starting processing for Error and hanging events");

		//Sending notification for Error events and Hanging events.
		//processDataChangeErrorEvents();
		//processPropagationErrorEvents();
	}

	/**
	 * retrieve the error events from evf_prop_event table and send out a
	 * notification to the support team.
	 * 
	 */
	public void processDataChangeErrorEvents() {
		logInfo = new LoggerInfo (this.getClass().getName());
		
		try {
			Logger.info (logInfo, "processErrorEvents", "@@@@@ Processing Error events notification ");
			
			List<DataChangeEvent> errorEvents = DataChangeEventsFwkDao
					.retrieveHangingDataChangeEventsForNotification();
			Date dt = new Date();

			notifyDataChangeEvents(errorEvents, ERROR_NOTIFICATION_CODE,
					"Error Events timestamp: " + dt.toString());
		} catch (Throwable e) {
			Logger.error(this.getClass().getName(), "processErrorEvents",
					"**** retrieve error events failed.", e);
		}
	}

	/**
	 * retrieve the error events from evf_prop_event table and send out a
	 * notification to the support team.
	 * 
	 */
	public void processPropagationErrorEvents() {
		logInfo = new LoggerInfo (this.getClass().getName());
		
		try {
			Logger.info (logInfo, "processErrorEvents", "@@@@@ Processing Error events notification ");
			
			List<PropagationEvent> errorEvents = PropagationEventsFwkDao
					.retrieveHangingPropEventsForNotification();
			Date dt = new Date();

			notifyPropagationEvents(errorEvents, ERROR_NOTIFICATION_CODE,
					"Error Events timestamp: " + dt.toString());
		} catch (Throwable e) {
			Logger.error(this.getClass().getName(), "processErrorEvents",
					"**** retrieve error events failed.", e);
		}
	}

	/**
	 * construct a notification.
	 * 
	 * @param pEvents
	 * @param requestCode
	 * @param subject
	 * @throws Exception
	 */
	protected void notifyDataChangeEvents(List<DataChangeEvent> pEvents,
			String requestCode, String subject) throws Exception {
		String msg = "";
		logInfo = new LoggerInfo (this.getClass().getName());
		
		StringBuffer sb = new StringBuffer();
		if (pEvents != null && pEvents.size() > 0) {
			String serverInfo = LogManager.getServerInfo();
			sb.append(serverInfo).append("\n\n");
			for (DataChangeEvent pEvent : pEvents) {
				sb.append(pEvent.toDebugString()).append("\n");
			}

			msg = sb.toString();

			ConfiguredNotificationRequest nr = NotificationAgent
					.newInfoConfiguredNotificationRequest(requestCode, sb
							.toString());
			nr.setSubject(subject);
			NotificationAgent.notify(nr);
		}
	}
	protected void notifyPropagationEvents(List<PropagationEvent> pEvents,
			String requestCode, String subject) throws Exception {
		String msg = "";
		logInfo = new LoggerInfo (this.getClass().getName());
		
		StringBuffer sb = new StringBuffer();
		if (pEvents != null && pEvents.size() > 0) {
			String serverInfo = LogManager.getServerInfo();
			sb.append(serverInfo).append("\n\n");
			for (PropagationEvent pEvent : pEvents) {
				sb.append(pEvent.toDebugString()).append("\n");
			}

			msg = sb.toString();

			ConfiguredNotificationRequest nr = NotificationAgent
					.newInfoConfiguredNotificationRequest(requestCode, sb
							.toString());
			nr.setSubject(subject);
			NotificationAgent.notify(nr);
		}
	}

}
