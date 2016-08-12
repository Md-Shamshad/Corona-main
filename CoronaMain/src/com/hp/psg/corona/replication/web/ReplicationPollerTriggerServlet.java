package com.hp.psg.corona.replication.web;

import javax.naming.InitialContext;
import javax.rmi.PortableRemoteObject;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;

import com.hp.psg.common.ctx.util.CTX;
import com.hp.psg.corona.common.util.DynamicConfig;
import com.hp.psg.corona.common.util.JndiUtils;
import com.hp.psg.corona.common.util.Logger;
import com.hp.psg.corona.replication.session.ReplicationPoller;
import com.hp.psg.corona.replication.session.ReplicationPollerHome;
import com.hp.psg.corona.replication.common.cache.CacheManager;
import com.hp.psg.corona.replication.common.cache.replicationmapping.ReplicationMappingTable;
import com.hp.psg.corona.replication.quote.dao.QuoteRepMonitorDao;
import com.hp.psg.corona.common.util.Config;

/**
 * @author dudeja
 * @version 1.0
 * 
 */
public class ReplicationPollerTriggerServlet extends HttpServlet {

	public void init(ServletConfig config) throws ServletException {
		super.init(config);
		CTX ctx = CTX.getCTX();
		Logger.repDebug(this.getClass().getName(), "init",
		"Init method of TriggerPoller is invoked");
		try {

			//Make sure to make the entry at following places  whenever a new thread needs to be configured and rest should be configured in dynamic_corona.properties file.
			String[] allThreadNamesInSystem = DynamicConfig.getAllReplicationThreadNames();
			
			if (allThreadNamesInSystem != null){

				//For making PICKED to READY state
				String idsReplicationEnabled = Config.getProperty("replication.ids.enabled");				
				//If it is enabled then only initiates the IDS replication
				if( idsReplicationEnabled != null && idsReplicationEnabled.equals("true")){
					QuoteRepMonitorDao monitorDao = new QuoteRepMonitorDao();
					monitorDao.updatePickedToReadyStatus();
				}

				for (String threadName : allThreadNamesInSystem) {
					
					if (threadName != null){
						InitialContext context = null;
						context = (InitialContext) JndiUtils.getInitialContext();
	
						Object replicationPollerIpcsObject = context
						.lookup(threadName);
	
						// Start a poller bean which will send messages to a JMS queue.
						ReplicationPollerHome replicationHome = (ReplicationPollerHome) PortableRemoteObject
						.narrow(replicationPollerIpcsObject,
								ReplicationPollerHome.class);
						ReplicationPoller replicationPoller = (ReplicationPoller) replicationHome.create();
						replicationPoller.invokeRemote();
						
					}else {
						System.out.println("Got null jndi name for thread "+ threadName);
					}
				}
			}

			//Initializing cache
			CacheManager.getInstance();
			ReplicationMappingTable.getInstance();

		} catch (Throwable ex) {
			Logger.repError(this.getClass().getName(), "init",
					"Error Occure while giving initializing any of the poller bean for "
					, ex);
		}
	}
}
