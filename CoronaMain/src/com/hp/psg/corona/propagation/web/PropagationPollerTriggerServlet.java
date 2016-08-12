package com.hp.psg.corona.propagation.web;

import javax.naming.InitialContext;
import javax.rmi.PortableRemoteObject;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;

import com.hp.psg.common.ctx.util.CTX;
import com.hp.psg.corona.common.util.Config;
import com.hp.psg.corona.common.util.JndiUtils;
import com.hp.psg.corona.common.util.Logger;
import com.hp.psg.corona.propagation.session.PropagationQueuePoller;
import com.hp.psg.corona.propagation.session.PropagationQueuePollerHome;
import com.hp.psg.corona.propagation.session.RetrialPoller;
import com.hp.psg.corona.propagation.session.RetrialPollerHome;

/**
 * @author dudeja
 * @version 1.0
 * 
 */
public class PropagationPollerTriggerServlet extends HttpServlet {

	public void init(ServletConfig config) throws ServletException {
		super.init(config);
		CTX ctx = CTX.getCTX();
		Logger.debug(this.getClass().getName(), "init",
				"Init method of PropagationTriggerPollerServlet is invoked");
		try {

			String prop_jndi_name = Config.getPropagationPollerJndiName();
			String retrial_jndi_name = Config.getRetrialPollerJndiName();


			InitialContext context = null;
			context = (InitialContext) JndiUtils.getInitialContext();

			try {
				Object propPollerObject = context.lookup(prop_jndi_name);

				// Start a poller bean which will send messages to a JMS queue.
				PropagationQueuePollerHome propQHome = (PropagationQueuePollerHome) PortableRemoteObject
						.narrow(propPollerObject, PropagationQueuePollerHome.class);
				PropagationQueuePoller propQueuePoller = (PropagationQueuePoller) propQHome
						.create();
				propQueuePoller.invokeRemote();
				
			}catch (Exception ex3){
				Logger.error(this.getClass().getName(), "init",
						"Error Occure while giving initializing any of the propagation poller bean for Propagation pollers"
								, ex3);
			}
			
			try {
				Object retrialPollerObject = context.lookup(retrial_jndi_name);
				RetrialPollerHome retrialPollerHome = (RetrialPollerHome) PortableRemoteObject
				.narrow(retrialPollerObject,
						RetrialPollerHome.class);
				RetrialPoller retrialPoller = (RetrialPoller) retrialPollerHome
						.create();
				retrialPoller.invokeRemote();
	
			}catch (Exception ex2) {
				Logger.error(this.getClass().getName(), "init",
						"Error Occure while giving initializing any of the propagation poller bean for Retrial pollers"
								, ex2);
			}
			
			Logger.debug(this.getClass().getName(), "init", "Message after invocation of propagation pollers");
			
		} catch (Throwable ex) {
			Logger.error(this.getClass().getName(), "init",
					"Error Occure while giving initializing any of the propagation poller bean for "
							, ex);
		}
	}
}
