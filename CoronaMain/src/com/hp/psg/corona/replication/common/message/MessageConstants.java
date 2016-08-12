package com.hp.psg.corona.replication.common.message;

import java.io.Serializable;

/**
 * This class list the Message constants and its corresponding messages.
 *
 * @author
 *
 */
public class MessageConstants implements Serializable {
	
	 public final static int GENERAL_PROCESSING_ERROR = 3000;
	 public final static int DB_PROCESSING_ERROR = 3007;
	 public final static int INVALID_CONFIGURATION = 3001;
	 public final static int DATA_PROCESSING_ERROR = 1000;
	 public final static int DATA_ALREADY_EXISTS = 1001;
	 public final static String HP_MESSAGE_3001 = "Configuration doesn't exist";
	 public final static String IDS_MESSAGE_1001 = "Data already created, can't be created again";
	 public final static String IDS_MESSAGE_1000 = "Request not completed, check the error message";
	 public final static String HP_MESSAGE_3007 = "The request has not completed. We apologize for any inconvenience. Please contact admin.";
		

}//end of class