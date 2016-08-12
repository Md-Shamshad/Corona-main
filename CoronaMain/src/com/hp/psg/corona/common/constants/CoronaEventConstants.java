package com.hp.psg.corona.common.constants;

import com.hp.psg.common.util.constants.EventConstants;

/**
 * @author dudeja
 * @version 1.0
 * 
 */

public class CoronaEventConstants  extends EventConstants{

	public interface CompletionStatus {
		static String SUCCESS = "SUCCESS";
		static String ERROR = "ERROR";
		static String REDUNDANT = "REDUNDANT";
		static String NO_DATA = "NO_DATA";
	}

	public String ComcatConfigDir = "comcat.config.dir";

	public interface EventType {
		static String Propagation = "PROPAGATION";
	}
}
