package com.hp.psg.corona.datachange.web;

import javax.naming.InitialContext;
import javax.rmi.PortableRemoteObject;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;

import com.hp.psg.common.ctx.util.CTX;
import com.hp.psg.corona.common.util.Config;
import com.hp.psg.corona.common.util.JndiUtils;
import com.hp.psg.corona.common.util.Logger;
import com.hp.psg.corona.datachange.session.DataChangePoller;
import com.hp.psg.corona.datachange.session.DataChangePollerHome;
import com.hp.psg.corona.datachange.session.DataChangeQueuePoller;
import com.hp.psg.corona.datachange.session.DataChangeQueuePollerHome;

/**
 * @author dudeja
 * @version 1.0
 * 
 */
public class DataChangePollerTriggerServlet extends HttpServlet {

	public void init(ServletConfig config) throws ServletException {
		super.init(config);
		CTX ctx = CTX.getCTX();
		Logger.debug(this.getClass().getName(), "init",
				"Init method of DataChangeTriggerPoller is invoked");
		try {
			String data_change_config_jndi_name = Config
					.getDataChangeConfigPollerJndiName();
			String data_change_perm_jndi_name = Config
					.getDataChangePermPollerJndiName();
			String data_change_prs_jndi_name = Config
					.getDataChangePrsPollerJndiName();

			String data_change_list_price_jndi_name= Config.getDataChangeListPricePollerJndiName();
			
			String data_change_prod_desc_jndi_name = Config.
			getDataChangeProdDescPollerJndiName();
			
			String dataChange_queue_jndi_name = Config
					.getDataChangeQueuePollerJndiName();

			InitialContext context = null;
			context = (InitialContext) JndiUtils.getInitialContext();

			Object dataChangeConfigPollerObject = context
					.lookup(data_change_config_jndi_name);
			Object dataChangePermPollerObject = context
					.lookup(data_change_perm_jndi_name);
			Object dataChangePrsPollerObject = context
					.lookup(data_change_prs_jndi_name);
			
			Object dataChangeListPricePollerObject = context
			.lookup(data_change_list_price_jndi_name);
			
			Object dataChangeProdDescPollerObject = context
			.lookup(data_change_prod_desc_jndi_name);
			
			Object txnPollerObject = context.lookup(dataChange_queue_jndi_name);
			DataChangeQueuePollerHome txnQHome = (DataChangeQueuePollerHome) PortableRemoteObject
					.narrow(txnPollerObject, DataChangeQueuePollerHome.class);
			DataChangeQueuePoller txnQueuePoller = (DataChangeQueuePoller) txnQHome
					.create();
			txnQueuePoller.invokeRemote();

			
			//Poller instances which creates the events from datachange queue to data_change_events queue,
			DataChangePollerHome dataChangeHome = (DataChangePollerHome) PortableRemoteObject
					.narrow(dataChangeConfigPollerObject,
							DataChangePollerHome.class);
			DataChangePoller dataChangePoller = (DataChangePoller) dataChangeHome
					.create();
			dataChangePoller.invokeRemote();

			dataChangeHome = (DataChangePollerHome) PortableRemoteObject
					.narrow(dataChangePermPollerObject,
							DataChangePollerHome.class);
			dataChangePoller = (DataChangePoller) dataChangeHome.create();
			dataChangePoller.invokeRemote();

			dataChangeHome = (DataChangePollerHome) PortableRemoteObject
					.narrow(dataChangePrsPollerObject,
							DataChangePollerHome.class);
			dataChangePoller = (DataChangePoller) dataChangeHome.create();
			dataChangePoller.invokeRemote();
			
			dataChangeHome = (DataChangePollerHome) PortableRemoteObject
			.narrow(dataChangeListPricePollerObject,
					DataChangePollerHome.class);
			dataChangePoller = (DataChangePoller) dataChangeHome.create();
			dataChangePoller.invokeRemote();
	
			dataChangeHome = (DataChangePollerHome) PortableRemoteObject
			.narrow(dataChangeProdDescPollerObject,
					DataChangePollerHome.class);
			dataChangePoller = (DataChangePoller) dataChangeHome.create();
			dataChangePoller.invokeRemote(); 
	
			//Initialize a datachange jmx agent.
			//DataChangeJmxAgent dceJmxAgent = new DataChangeJmxAgent();
			
			Logger.debug(this.getClass().getName(), "init",
			"Message after invocation of datachange pollers");
		} catch (Throwable ex) {
			Logger.error(this.getClass().getName(), "init",
					"Error Occure while giving initializing any of the poller bean for "
							, ex); 
		}
	}
}
