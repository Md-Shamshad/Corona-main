package com.hp.psg.corona.propagation.jms;

import java.util.Hashtable;
import java.util.List;

import javax.ejb.MessageDrivenContext;
import javax.jms.Message;
import javax.jms.Queue;
import javax.jms.QueueConnection;
import javax.jms.QueueConnectionFactory;
import javax.jms.QueueReceiver;
import javax.jms.QueueSession;
import javax.jms.Session;
import javax.jms.TextMessage;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;

import com.hp.psg.common.error.util.CoronaErrorConstants;
import com.hp.psg.common.util.logging.LoggerInfo;
import com.hp.psg.corona.common.beans.PropagationEvent;
import com.hp.psg.corona.common.util.Config;
import com.hp.psg.corona.common.util.JndiUtils;
import com.hp.psg.corona.common.util.Logger;
import com.hp.psg.corona.error.util.CoronaErrorHandler;
import com.hp.psg.corona.propagation.dao.PropagationEventsFwkDao;
import com.hp.psg.corona.propagation.handler.PropagationEventsProcessor;

/**
 * @author dudeja
 * @version 1.0
 *
 */
public class PropagationEventsQueueReaderBean
		implements
			javax.ejb.MessageDrivenBean,
			javax.jms.MessageListener {

	public final static String JNDI_FACTORY = Config
			.getWeblogicInitialContextFactoryName();
	public final static String JMS_FACTORY = Config
			.getDefaultPropConnectionFactory();
	public final static String QUEUE = Config.getDefaultPropagationEventsQueue();
	private boolean quit = false;
	LoggerInfo logInfo=null;
	private MessageDrivenContext _context;

	QueueConnection connect=null;
	QueueReceiver receiver =null;
	static boolean firstTime=true;
	
	private javax.ejb.MessageDrivenContext messageContext = null;

	/**
	 * Required method for container to set context.
	 * 
	 * @generated
	 */
	public void setMessageDrivenContext(
			javax.ejb.MessageDrivenContext messageContext)
			throws javax.ejb.EJBException {
		this.messageContext = messageContext;
	}

	public void ejbCreate() {
		try {
			logInfo = new LoggerInfo (this.getClass().getName());
		} catch (Exception e) {
			CoronaErrorHandler.logError(e, null, null);
		}
	}

	public void ejbRemove() {
		messageContext = null;
	}

	private static InitialContext getInitialContext(String url)
			throws NamingException {
		Hashtable env = new Hashtable();
		env.put(Context.INITIAL_CONTEXT_FACTORY, JNDI_FACTORY);
		env.put(Context.PROVIDER_URL, url);
		env.put("weblogic.jndi.createIntermediateContexts", "true");
		return new InitialContext(env);
	}

	public void startListener() {

		Logger.info(this.getClass().getName(),"startListener","Starting PropagationEventsQueueReaderBean listener");
		try {

			Context jndiContext = JndiUtils.getInitialContext(Config.getJndiClusterUrl());

			//Need to change the Queue name on base of source system.
			String connectionFactory=Config.getDefaultTxnQueueConnectionFactory();
			QueueConnectionFactory factory = (QueueConnectionFactory)jndiContext.lookup(connectionFactory);

			String queueName;

			queueName=Config.getDefaultDataChangeEventsTxnQueue();
			Queue dgQueue = (Queue)jndiContext.lookup(queueName);
			Logger.debug(this.getClass().getName(),"startListener","Connecting to ConnectionFactory PropagationEventsQueueReaderBean "+connectionFactory+" and queue name "+queueName);

			connect = factory.createQueueConnection();
			QueueSession session = connect.createQueueSession(false,Session.AUTO_ACKNOWLEDGE);

			receiver = session.createReceiver(dgQueue );

			receiver.setMessageListener(this);

			connect.start();
		} catch (Exception e) {
			Logger.error(this.getClass().getName(),"startListener","Unable to start Data Load Events Queue listener",e);
			CoronaErrorHandler.logError(this.getClass(),CoronaErrorConstants.dataloadJMSErr,"startListener","Caught exception while starting Listener for PropagationEventsQueueReaderBean MDB.",false,e,true);
		}
	}

	public void onMessage(Message message) {
		Logger.debug(this.getClass().getName(),"onMessage","OnMessage Method of PropagationEventsQueueReaderBean invoked");
		try {

			TextMessage tMsg = (TextMessage)message;
			if (tMsg != null){
				Long peGroupId= tMsg.getLongProperty("GROUPID");
				List<PropagationEvent> peList = PropagationEventsFwkDao.getPropagationEventsOnGroupId(peGroupId);
				processGroupedEvents(peList);
				
				Logger.info(logInfo, "onMessage", "Processing completed for header Id " + peList);
				
			}
		} catch (Throwable e) {
				Logger.error(this.getClass().getName(),"onMessage","Error retrieving message in PropagationEventsQueueReaderBean",e);
				CoronaErrorHandler.logError(this.getClass(),"EVF0046","onMessage","Caught exception while triggering onMessage for MDB PropagationEventsQueueReaderBean",false,e,true);            
		} 
	}
	

	public void processGroupedEvents(List<PropagationEvent> peList) {
		try {
			PropagationEventsProcessor gProcessor = new PropagationEventsProcessor(peList);

			String grpStatus = gProcessor.getProcessingStatusGroup();
			Logger.info(logInfo, "processEvent", " ID  - "
					+ gProcessor.getPropagationEventGroupId() + "%%%%%%%%%%%%% propagation group processing status : "
					+ grpStatus);
			
		} catch (Exception e) {
			String message=("Exception while processing group for propagation events");
			CoronaErrorHandler.logError(e, message, null);
		}
	}

	public PropagationEventsQueueReaderBean() {
	}

}
