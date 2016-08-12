package com.hp.psg.corona.propagation.dao;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.hp.psg.common.db.dax.DaxDB;
import com.hp.psg.common.db.dax.DaxDataBeanGeneral;
import com.hp.psg.common.db.dax.DaxMgr;
import com.hp.psg.common.db.dax.DaxParsedStmt;
import com.hp.psg.common.error.CoronaException;
import com.hp.psg.common.error.util.CoronaErrorConstants;
import com.hp.psg.common.util.logging.LoggerInfo;
import com.hp.psg.corona.common.beans.PropagationEvent;
import com.hp.psg.corona.common.constants.CoronaFwkConstants;
import com.hp.psg.corona.common.util.CoronaFwkUtil;
import com.hp.psg.corona.common.util.Logger;
import com.hp.psg.corona.error.util.CoronaErrorHandler;
import com.hp.psg.corona.propagation.beans.PropagationEventsGroup;

/**
 * @author dudeja
 * @version 1.0
 */

public class PropagationEventsFwkDao {

	private static LoggerInfo logInfo = new LoggerInfo(
			"com.hp.psg.corona.dataload.dao.PropagationEventsFwkDao");

	private static String UPDATE_PROPAGATION_EVENT_STATUS_BY_ID = "UPDATE_PROPAGATION_EVENT_STATUS_BY_ID";
	public static String PICK_EVENTS_GROUP_ID_ON_STATUS_AND_ID = "PICK_EVENTS_GROUP_ID_ON_STATUS_AND_ID";
	
	private static Map<String, List<String>> propagationCallHandlerMethodMap = new HashMap<String, List<String>>();
	
	public static final String GET_PROPAGATION_EVENTS_BY_GROUP_ID = "GET_PROPAGATION_EVENTS_BY_GROUP_ID";

	public static final String PROPAGATION_FWK_GROUP = "PROPAGATION_FWK_GROUP";
	public static final String GET_PROPAGATION_EVENT_QUEUED_COUNT="GET_PROPAGATION_EVENT_QUEUED_COUNT";
	
	public static final String PROPAGATION_EVENT_GROUPED_TO_WAIT = "PROPAGATION_EVENT_GROUPED_TO_WAIT";
	public static final String PROPAGATION_EVENT_GROUPED_TO_ERROR_WAIT = "PROPAGATION_EVENT_GROUPED_TO_ERROR_WAIT";
	public static final String PROPAGATION_EVENT_UPD_STATUS_BY_GROUP = "PROPAGATION_EVENT_UPD_STATUS_BY_GROUP";
	public static final String PROPAGATION_EVENT_ERRWAIT_TO_GROUPED = "PROPAGATION_EVENT_ERRWAIT_TO_GROUPED";
	public static final String PROPAGATION_EVENT_WAIT_TO_GROUPED = "PROPAGATION_EVENT_WAIT_TO_GROUPED";
	public static final String PROPAGATION_EVENT_WAIT_TO_ERRWAIT = "PROPAGATION_EVENT_WAIT_TO_ERRWAIT";
	public static final String GET_PROPAGATION_EVENTS_BY_STATUS = "GET_PROPAGATION_EVENTS_BY_STATUS";
	public static final String GET_PROPAGATION_EVENTS_BY_STATUS_W_ID = "GET_PROPAGATION_EVENTS_BY_STATUS_W_ID";
	private static final String UPDATE_PROPAGATION_EVENT_STATUS_AND_COMMENT_BY_ID = "UPDATE_PROPAGATION_EVENT_STATUS_AND_COMMENT_BY_ID";
	private static final String GET_PROPAGATION_EVENTS_BY_STRING="GET_PROPAGATION_EVENTS_BY_STRING" ;

	private static String UPDATE_PROPAGATION_EVENT_IN_PROGRESS_AND_COMMENT_BY_ID = "UPDATE_PROPAGATION_EVENT_IN_PROGRESS_AND_COMMENT_BY_ID";
	private static String UPDATE_PROPAGATION_EVENT_COMPLETED_AND_COMMENT_BY_ID = "UPDATE_PROPAGATION_EVENT_COMPLETED_AND_COMMENT_BY_ID";
	private static String UPDATE_PROPAGATION_EVENT_COMPLETED_BY_ID = "UPDATE_PROPAGATION_EVENT_COMPLETED_BY_ID";
	private static String UPDATE_PROPAGATION_EVENT_IN_PROGRESS_BY_ID = "UPDATE_PROPAGATION_EVENT_IN_PROGRESS_BY_ID";
	private static String UPDATE_PROPAGATION_EVENT_ERROR_AND_MSG_BY_ID="UPDATE_PROPAGATION_EVENT_ERROR_AND_MSG_BY_ID";


	public static void updatePropagationEventReadyToWait() {
		updatePropagationEventStatus(PROPAGATION_EVENT_GROUPED_TO_WAIT,
				CoronaFwkConstants.ProcessingStatus.WAIT);
	}

	public static void updatePropagationEventReadyToErrorWait() {
		updatePropagationEventStatus(PROPAGATION_EVENT_GROUPED_TO_ERROR_WAIT,
				CoronaFwkConstants.ProcessingStatus.ERROR_WAIT);
	}

	public static void updatePropagationEventWaitToReady() {
		updatePropagationEventStatus(PROPAGATION_EVENT_WAIT_TO_GROUPED,
				CoronaFwkConstants.ProcessingStatus.GROUPED);
	}

	public static void updatePropagationEventErrorWaitToReady() {
		updatePropagationEventStatus(PROPAGATION_EVENT_ERRWAIT_TO_GROUPED,
				CoronaFwkConstants.ProcessingStatus.GROUPED);
	}

	public static void updatePropagationEventWaitToErrorWait() {
		updatePropagationEventStatus(PROPAGATION_EVENT_WAIT_TO_ERRWAIT,
				CoronaFwkConstants.ProcessingStatus.ERROR_WAIT);
	}

	public static void updatePropagationEventStatus(PropagationEvent pe,
			String newProcessingStatus, String message) {
		if (message != null) {
			if (CoronaFwkConstants.ProcessingStatus.IN_PROGRESS
					.equals(newProcessingStatus))
				updatePropagationEventStatusAndCommentById(
						UPDATE_PROPAGATION_EVENT_IN_PROGRESS_AND_COMMENT_BY_ID,
						newProcessingStatus, pe, message);
			else if (CoronaFwkConstants.ProcessingStatus.COMPLETED
					.equals(newProcessingStatus))
				updatePropagationEventStatusAndCommentById(
						UPDATE_PROPAGATION_EVENT_COMPLETED_AND_COMMENT_BY_ID,
						newProcessingStatus, pe, message);
			else if (CoronaFwkConstants.ProcessingStatus.ERROR
							.equals(newProcessingStatus))
				updatePropagationEventStatusAndCommentById(
						UPDATE_PROPAGATION_EVENT_ERROR_AND_MSG_BY_ID,
						newProcessingStatus, pe, message);
			else
				updatePropagationEventStatusAndCommentById(
						UPDATE_PROPAGATION_EVENT_STATUS_AND_COMMENT_BY_ID,
						newProcessingStatus, pe, message);
		} else {
			if (CoronaFwkConstants.ProcessingStatus.IN_PROGRESS
					.equals(newProcessingStatus))
				updatePropagationEventStatusById(
						UPDATE_PROPAGATION_EVENT_IN_PROGRESS_BY_ID,
						newProcessingStatus, pe);
			else if (CoronaFwkConstants.ProcessingStatus.COMPLETED
					.equals(newProcessingStatus))
				updatePropagationEventStatusById(
						UPDATE_PROPAGATION_EVENT_COMPLETED_BY_ID,
						newProcessingStatus, pe);
			else if (CoronaFwkConstants.ProcessingStatus.ERROR.equals(newProcessingStatus))
					updatePropagationEventStatusAndCommentById(
							UPDATE_PROPAGATION_EVENT_ERROR_AND_MSG_BY_ID,
							newProcessingStatus, pe, message);
			else
				updatePropagationEventStatusById(
						UPDATE_PROPAGATION_EVENT_STATUS_BY_ID,
						newProcessingStatus, pe);
		}
	}

	public static int updatePropagationEventStatusAndCommentById(String stmtId,
			String newStatus, PropagationEvent pEvent, String comment) {
		try {
			DaxDataBeanGeneral dataBean = new DaxDataBeanGeneral();
			DaxMgr dxMgr = DaxMgr.getInstance();

			dataBean.setString1(newStatus);
			dataBean.setLong1(pEvent.getId());
			dataBean.setString2(CoronaFwkConstants.FWK_PROCESSOR);
			dataBean.setString3(comment);

			DaxParsedStmt dxPstmt = dxMgr.makeParsedStmt(PROPAGATION_FWK_GROUP,
					stmtId, dataBean, null, null);
			DaxDB dxDb = dxMgr.getDaxDB();
			return dxDb.doUpdate(dxPstmt, dataBean);
		} catch (Exception e) {
			CoronaErrorHandler.logError(e.getClass(),
					CoronaErrorConstants.processingErr, "dopPropagationDbUpd",
					"updatePropagationEventStatusById", false, e, true);

			return 0;
		}
	}


	public static void cachePropagationCallHandlerMethodMap()
			throws CoronaException, Exception {
		Class clDao = Class
				.forName("com.hp.psg.corona.propagation.handler.PropagationEventsCallHandler");

		Method[] mAllMethods = clDao.getDeclaredMethods();
		for (Method m : mAllMethods) {
			List<String> parmList = new ArrayList<String>();
			for (Class cl : m.getParameterTypes()) {
				String paramType = cl.getName();
				if (paramType.startsWith("[L")) {
					String str = paramType.substring(2, paramType.length() - 1);
					str = str.substring(str.lastIndexOf(".") + 1);
					parmList.add(str);
				} else {
					String str = paramType
							.substring(paramType.lastIndexOf(".") + 1);
					parmList.add(str);
				}
			}
			propagationCallHandlerMethodMap.put(m.getName().trim(), parmList);
		}
	}

	public static int pickGroupedEventsToBeQueued(String stmtId, int count,
			String lastModifiedBy, String oldStatus, String newStatus)
	throws Exception {
		try {
			DaxDataBeanGeneral dataBean = new DaxDataBeanGeneral();
			dataBean.setString1(lastModifiedBy);
			dataBean.setString2(newStatus);
			dataBean.setString3(oldStatus);
			dataBean.setInt1(count);

			DaxMgr dxMgr = DaxMgr.getInstance();
			DaxParsedStmt dxPstmt = dxMgr.makeParsedStmt(PROPAGATION_FWK_GROUP,
					stmtId, dataBean, null, null);
			DaxDB dxDb = dxMgr.getDaxDB();
			return dxDb.doUpdate(dxPstmt, dataBean);

		} catch (Exception e) {
			CoronaErrorHandler.logError(e, null, null);
			return 0;
		}
	}

	public static List<PropagationEvent> retrievePropagationErrorEvents() {
		return retrievePropagationEventByStatusFromDB(
				CoronaFwkConstants.ProcessingStatus.ERROR, null);
	}

	protected static List<PropagationEvent> retrievePropagationEventByStatusFromDB(
			String status,  String lastModifiedBy) {

		List<PropagationEvent> events = null;
		DaxDataBeanGeneral dataBean = new DaxDataBeanGeneral();
		dataBean.setString1(status);
		dataBean.setString2(lastModifiedBy);
		String stmtId = (lastModifiedBy == null)
				? GET_PROPAGATION_EVENTS_BY_STATUS
				: GET_PROPAGATION_EVENTS_BY_STATUS_W_ID;
		if (status != null && "ERROR".equals(status)) {
			stmtId = "GET_ERR_PROPAGATION_EVENTS";
		}

		PropagationEvent pEvent = new PropagationEvent();

		DaxMgr dxMgr = DaxMgr.getInstance();
		DaxParsedStmt dxPstmt = dxMgr.makeParsedStmt(PROPAGATION_FWK_GROUP,
				stmtId, pEvent, null, null);
		DaxDB dxDb = dxMgr.getDaxDB();
		events = dxDb.doSelect(dxPstmt, PropagationEvent.class, dataBean);
		
		return events;
	}

	public static int updatePropagationEventStatus(String stmtId, String status) {
		PropagationEvent pEvent = new PropagationEvent();
		pEvent.setProcessingStatus(status);
		pEvent.setLastModifiedBy(CoronaFwkConstants.FWK_PROCESSOR);
		return doPropagationEventDbUpd(stmtId, pEvent);
	}

	public static int updatePropagationEventStatusById(String stmtId,
			String newStatus, PropagationEvent pEvent) {
		try {
			DaxDataBeanGeneral dataBean = new DaxDataBeanGeneral();
			DaxMgr dxMgr = DaxMgr.getInstance();

			dataBean.setString1(newStatus);
			dataBean.setLong1(pEvent.getId());
			dataBean.setDate1(pEvent.getStartDate());
			dataBean.setDate2(pEvent.getEndDate());

			dataBean.setString2(CoronaFwkConstants.FWK_PROCESSOR);

			DaxParsedStmt dxPstmt = dxMgr.makeParsedStmt(PROPAGATION_FWK_GROUP,
					stmtId, dataBean, null, null);
			DaxDB dxDb = dxMgr.getDaxDB();
			return dxDb.doUpdate(dxPstmt, dataBean);
		} catch (Exception e) {
			CoronaErrorHandler.logError(e.getClass(),
					CoronaErrorConstants.processingErr,
					"doPropagationEventDbUpd",
					"updatePropagationEventStatusById", false, e, true);

			return 0;
		}
	}

	public static void updatePropagationEventStatus(
			PropagationEventsGroup eventGroup) {
		PropagationEvent pEvent = null;
		DaxParsedStmt stmt = null;
		DaxMgr mgr = DaxMgr.getInstance();
		DaxDB db = mgr.getDaxDB();
		boolean error = false;

		if (eventGroup != null && eventGroup.getPropagationEvents() != null) {

			try {
				List<PropagationEvent> pEvents = eventGroup
						.getPropagationEvents();

				stmt = mgr.makeParsedStmt(PROPAGATION_FWK_GROUP,
						PROPAGATION_EVENT_UPD_STATUS_BY_GROUP, pEvent, null,
						null);
				db.startBatch();
				for (PropagationEvent pe : pEvents) {
					pe.setLastModifiedBy(CoronaFwkConstants.FWK_PROCESSOR);
					db.doUpdate(stmt, pe);
				}
				db.finishBatch();

			} catch (Exception e) {
				error = true;
				CoronaErrorHandler.logError(e, null, null);
			} finally {
				db.finish(error);
			}
		}
	}

	public static void updatePropagationEventStatus(
			PropagationEventsGroup eventGroup, String newStatus) {
		PropagationEvent pEvent = null;
		DaxParsedStmt stmt = null;
		DaxMgr mgr = DaxMgr.getInstance();
		DaxDB db = mgr.getDaxDB();
		boolean error = false;

		if (eventGroup != null && eventGroup.getPropagationEvents() != null) {

			try {
				List<PropagationEvent> pEvents = eventGroup
						.getPropagationEvents();

				stmt = mgr.makeParsedStmt(PROPAGATION_FWK_GROUP,
						PROPAGATION_EVENT_UPD_STATUS_BY_GROUP, pEvent, null,
						null);
				db.startBatch();
				for (PropagationEvent pe : pEvents) {
					pe.setLastModifiedBy(CoronaFwkConstants.FWK_PROCESSOR);
					pe.setProcessingStatus(newStatus);
					db.doUpdate(stmt, pe);
				}
				db.finishBatch();

			} catch (Exception e) {
				error = true;
				CoronaErrorHandler.logError(e, null, null);
			} finally {
				db.finish(error);
			}
		}
	}

	public static void verifyPropagationRedundancyState() {
		LoggerInfo logInfo= new LoggerInfo("com.hp.psg.corona.dataload.dao.PropagationEventsFwkDao");

		Logger.info(logInfo, "verifyPropagationRedundancyState",
		"***** inside verifyPropagationRedundancyState for datachange events");

		updatePropagationEventWaitToReady();
		Logger.info(logInfo, "verifyPropagationRedundancyState",
		"***** updatePropagationEventWaitToReady in retry poller");

		updatePropagationEventErrorWaitToReady();
		Logger.info(logInfo, "verifyPropagationRedundancyState",
		"***** updatePropagationEventErrorWaitToReady in retry poller");

		updatePropagationEventReadyToErrorWait();
		Logger.info(logInfo, "verifyPropagationRedundancyState",
		"***** updatePropagationEventReadyToErrorWait in retry poller");

		updatePropagationEventReadyToWait();
		Logger.info(logInfo, "verifyPropagationRedundancyState",
		"***** updatePropagationEventReadyToWait in retry poller");

	}


	public static List<PropagationEvent> retrieveHangingPropEventsForNotification() {

		StringBuffer whereString = new StringBuffer();
		whereString.append(" where epe.pe_last_modified_date between (SYSDATE - 5/24) and (SYSDATE -1/24)");
		whereString.append(" and epe.pe_eventskip_flag = 'N' ");
		whereString.append(" and epe.pe_processing_status <> 'COMPLETED' ");

		return getPropagationEventsByString(whereString.toString());
	}

	public static List<PropagationEvent> getPropagationEventsByString(
			String whereString) {
		DaxDataBeanGeneral dataBean = new DaxDataBeanGeneral();
		DaxMgr dMgr = DaxMgr.getInstance();
		DaxDB db = dMgr.getDaxDB();
		dataBean.setString1(whereString);

		DaxParsedStmt pstmt = dMgr.makeParsedStmt(PROPAGATION_FWK_GROUP,
				GET_PROPAGATION_EVENTS_BY_STRING, dataBean, null, null);
		List<PropagationEvent> list = db.doSelect(pstmt,
				PropagationEvent.class, dataBean);
		return list;

	}

	public static int getPropagationEventsQueuedCount() {

		DaxDataBeanGeneral dataBean = new DaxDataBeanGeneral();
		DaxMgr dMgr = DaxMgr.getInstance();
		DaxDB db = dMgr.getDaxDB();

		DaxParsedStmt pstmt = dMgr.makeParsedStmt(PROPAGATION_FWK_GROUP,
				GET_PROPAGATION_EVENT_QUEUED_COUNT, dataBean, null, null);
		List list = db.doSelect(pstmt, DaxDataBeanGeneral.class, dataBean);
		dataBean = (DaxDataBeanGeneral) list.get(0);
		return dataBean.getInt1();
	}
	

	public static List<Long> retriveEventGroupIdOnStatusAndIdFromDb (String lastModifiedBy, String status)
			throws Exception {
		
		List<Long> groupIdList = new ArrayList<Long>();

		DaxDataBeanGeneral dataBean = new DaxDataBeanGeneral();
		dataBean.setString1(lastModifiedBy);
		dataBean.setString2(status);

		PropagationEvent propEvent = new PropagationEvent();
		
		DaxMgr dxMgr = DaxMgr.getInstance();
		DaxParsedStmt dxPstmt = dxMgr.makeParsedStmt(PROPAGATION_FWK_GROUP,
				PICK_EVENTS_GROUP_ID_ON_STATUS_AND_ID, propEvent, null, null);
		DaxDB dxDb = dxMgr.getDaxDB();
		
		List<PropagationEvent> dgList = dxDb.doSelect(dxPstmt, PropagationEvent.class, dataBean);
		
		if (dgList != null && dgList.size() > 0){
			for (PropagationEvent dg :dgList){
				groupIdList.add(dg.getGroupId());
			}
		}
		
		return groupIdList;
		
	}
	
	
	protected static int doPropagationEventDbUpd(String stmtId,
			PropagationEvent pEvent) {
		try {
			DaxDataBeanGeneral dataBean = new DaxDataBeanGeneral();
			DaxMgr dxMgr = DaxMgr.getInstance();

			dataBean.setString1(pEvent.getLastModifiedBy());
			dataBean.setString2(CoronaFwkUtil.getManageServerName());

			DaxParsedStmt dxPstmt = dxMgr.makeParsedStmt(PROPAGATION_FWK_GROUP,
					stmtId, dataBean, null, null);
			DaxDB dxDb = dxMgr.getDaxDB();
			return dxDb.doUpdate(dxPstmt, dataBean);
		} catch (Exception e) {
			CoronaErrorHandler.logError(e.getClass(),
					CoronaErrorConstants.processingErr,
					"doPropagationEventDbUpd",
					"Error in Events Handler while updating events status",
					false, e, true);

			return 0;
		}
	}

	public static Map<String, List<String>> getPropagationCallHandlerMethodMap() {
		return propagationCallHandlerMethodMap;
	}

	public static void setPropagationCallHandlerMethodMap(
			Map<String, List<String>> propagationCallHandlerMethodMap) {
		PropagationEventsFwkDao.propagationCallHandlerMethodMap = propagationCallHandlerMethodMap;
	}
	
	public static List<PropagationEvent> getPropagationEventsOnGroupId(Long groupId){
		List<PropagationEvent> events = null;

		DaxDataBeanGeneral dataBean = new DaxDataBeanGeneral();
		dataBean.setLong1(groupId);

		PropagationEvent pEvent = new PropagationEvent();

		DaxMgr dxMgr = DaxMgr.getInstance();
		DaxParsedStmt dxPstmt = dxMgr.makeParsedStmt(PROPAGATION_FWK_GROUP,
				GET_PROPAGATION_EVENTS_BY_GROUP_ID, pEvent, null, null);
		DaxDB dxDb = dxMgr.getDaxDB();
		
		events = dxDb.doSelect(dxPstmt, PropagationEvent.class, dataBean);
		return events;
		
	}
	
}
