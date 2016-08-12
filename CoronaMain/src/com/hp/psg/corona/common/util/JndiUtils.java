package com.hp.psg.corona.common.util;

import java.util.Properties;

import javax.naming.Context;
import javax.naming.InitialContext;

/**
 * @author dudeja
 * @version 1.0
 * 
 */
public class JndiUtils {
	private JndiUtils() {
	}

	public static Object lookup(Context context, String jndiName)
			throws Exception {
		return context.lookup(jndiName);
	}

	public static Context getInitialContext(String connectString)
			throws Exception {
		Properties prop = new Properties();
		prop.setProperty("java.naming.security.principal", Config
				.getWeblogicUser());
		prop.setProperty("java.naming.security.credentials", Config
				.getWeblogicPassword());
		prop.setProperty(Context.INITIAL_CONTEXT_FACTORY,
				"weblogic.jndi.WLInitialContextFactory");
		prop.setProperty(Context.PROVIDER_URL, connectString);
		return new InitialContext(prop);
	}

	/*
	 * Overloaded method for looking up without provider URL in case of
	 * clustered environment.
	 */
	public static Context getInitialContext() throws Exception {
		Properties prop = new Properties();
		prop.setProperty("java.naming.security.principal", Config
				.getWeblogicUser());
		prop.setProperty("java.naming.security.credentials", Config
				.getWeblogicPassword());
		prop.setProperty(Context.INITIAL_CONTEXT_FACTORY,
				"weblogic.jndi.WLInitialContextFactory");
		return new InitialContext(prop);
	}

	public static void trace(String msg) {
		Logger.debug("JndiUtils", "trace", msg);
	}
	public static void main(String[] args) throws Exception {
		if (args.length != 2) {
			trace("Usage: JndiUtils <connectString> <jndiName>");
			System.exit(1);
		} else {
			String jndiName = args[0];
			Context context = getInitialContext(jndiName);
			Object o = lookup(context, args[1]);
		}
	}

}
