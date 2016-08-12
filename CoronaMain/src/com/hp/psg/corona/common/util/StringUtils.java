package com.hp.psg.corona.common.util;

/**
 * @author dudeja
 * @version 1.0
 * 
 */
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

public class StringUtils {

	public static String replace(String src, Map maps) {
		// String str=src;
		// Set mapSet = maps.entrySet();
		Set entries = maps.entrySet();
		Iterator it = entries.iterator();
		while (it.hasNext()) {
			Map.Entry entry = (Map.Entry) it.next();
			String val = (String) entry.getValue();
			if (val == null)
				val = "";
			src = replace(src, (String) entry.getKey(), val);
		}
		return src;
	}

	public static String nonull(String str) {
		return (str != null ? str : "");
	}

	public static String replace(String srcString, String oldSubstr,
			String newSubstr) {
		int index = 0;
		StringBuffer destString = new StringBuffer();
		String remString = srcString;
		boolean done = false;

		while (!done) {
			index = remString.indexOf(oldSubstr);
			if (index == -1) {
				destString.append(remString);
				done = true;
				continue;
			} else {
				destString.append(remString.substring(0, index));
				destString.append(newSubstr);
				remString = remString.substring(index + oldSubstr.length());
			}
		}
		String st = new String(destString);
		destString = null;
		remString = null;
		return st;

	}

}
