package com.hp.psg.corona.common.constants;

/**
 * This class hold all corona constants
 *
 * @author dudeja
 * @version 1.0
 * 
 */

public class CoronaFwkConstants {

	public static class ProcessingStatus  {
		public static String NEW = "NEW";
		public static final String READY = "READY";
		public static final String GROUPED = "GROUPED";
		public static String PICKED = "PICKED";
		public static String IN_PROGRESS = "INPROGRESS";
		public static final String PENDING_PROPAGATION = "PENDING_PROP";
		public static final String PROPAGATING = "PROPAGATING";
		public static final String PROCESSING = "PROCESSING";
		public static String COMPLETED = "COMPLETED";
		public static final String ERROR = "ERROR";
		public static String WAIT = "WAIT";
		public static String ERROR_WAIT = "ERROR_WAIT";
		public static final String QUEUED = "QUEUED";
	}

	public static final String ProcessingCreator = "EvfProcessor";
	public static final String DataChangeEventsTimer = "DataLoadEventsTimer";
	public static final String PropagationEventsTimer = "PropagationEventsTimer";
	public static final String RetrialTimer = "RetryEventsTimer";
	public static final String ReplicationTimer = "ReplicationTimer";
	public static final String TxnEventsTimer = "TxnQueueEventsTimer";
	public static final String Success = "SUCCESS";
	public static final String Warning = "WARNING";
	public static final String Failure = "FAILURE";
	public static final String FWK_PROCESSOR = "FWK_PROCESSOR";
	public static final String RepSuccess = "S";
	public static final String RepWarning = "W";
	public static final String RepFailure = "F";
	
	public static final int SUCCESS = 0;
	public static final int WARNING = 1;
	public static final int ERROR = 2;
	public static final int MULTIPLE_ROWS = 2;

	public static String OK = "Ok";
}
