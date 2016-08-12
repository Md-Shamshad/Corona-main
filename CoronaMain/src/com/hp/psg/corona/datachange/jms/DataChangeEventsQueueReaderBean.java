package com.hp.psg.corona.datachange.jms;


import java.util.List;

import javax.ejb.EJBException;
import javax.ejb.MessageDrivenBean;
import javax.ejb.MessageDrivenContext;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.Queue;
import javax.jms.QueueConnection;
import javax.jms.QueueConnectionFactory;
import javax.jms.QueueReceiver;
import javax.jms.QueueSession;
import javax.jms.Session;
import javax.jms.TextMessage;
import javax.naming.Context;

import com.hp.psg.common.error.util.CoronaErrorConstants;
import com.hp.psg.common.util.logging.LoggerInfo;
import com.hp.psg.corona.common.beans.DataChangeEvent;
import com.hp.psg.corona.common.util.Config;
import com.hp.psg.corona.common.util.JndiUtils;
import com.hp.psg.corona.common.util.Logger;
import com.hp.psg.corona.datachange.dao.DataChangeEventsFwkDao;
import com.hp.psg.corona.datachange.handler.DataChangeEventsProcessor;
import com.hp.psg.corona.error.util.CoronaErrorHandler;

public class DataChangeEventsQueueReaderBean implements MessageDrivenBean, MessageListener {
	private MessageDrivenContext _context;

	QueueConnection connect=null;
	QueueReceiver receiver =null;
	static boolean firstTime=true;
	LoggerInfo logInfo=null;


	public void ejbCreate() {
		logInfo = new LoggerInfo(this.getClass().getName());
		//startListener(); // start the listener on queue.
	}

	public void setMessageDrivenContext(MessageDrivenContext context) throws EJBException {
		_context = context;
	}

	public void ejbRemove() throws EJBException {
	}

	public DataChangeEventsQueueReaderBean() {        
	}



	public void startListener() {

		Logger.info(this.getClass().getName(),"startListener","Starting DataChangeEventsQueueReaderBean listener");
		try {

			Context jndiContext = JndiUtils.getInitialContext(Config.getJndiClusterUrl());

			//Need to change the Queue name on base of source system.
			String connectionFactory=Config.getDefaultTxnQueueConnectionFactory();
			QueueConnectionFactory factory = (QueueConnectionFactory)jndiContext.lookup(connectionFactory);

			String queueName;

			queueName=Config.getDefaultDataChangeEventsTxnQueue();
			Queue dgQueue = (Queue)jndiContext.lookup(queueName);
			Logger.debug(this.getClass().getName(),"startListener","Connecting to ConnectionFactory DataChangeEventsQueueReaderBean "+connectionFactory+" and queue name "+queueName);

			connect = factory.createQueueConnection();
			QueueSession session = connect.createQueueSession(false,Session.AUTO_ACKNOWLEDGE);

			receiver = session.createReceiver(dgQueue );

			receiver.setMessageListener(this);

			connect.start();
		} catch (Exception e) {
			Logger.error(this.getClass().getName(),"startListener","Unable to start Data Load Events Queue listener",e);
			CoronaErrorHandler.logError(this.getClass(),CoronaErrorConstants.dataloadJMSErr,"startListener","Caught exception while starting Listener for DataChangeEventsQueueReaderBean MDB.",false,e,true);
		}
	}

	public void onMessage(Message message) {
		Logger.debug(this.getClass().getName(),"onMessage","OnMessage Method of DataLoadEventsQueueReaderBean invoked");
		try {

			TextMessage tMsg = (TextMessage)message;
			if (tMsg != null){
				Long dceId= tMsg.getLongProperty("ID");
				List<DataChangeEvent> dceList = DataChangeEventsFwkDao.getDataChangeEventOnId(dceId);
				processEvent(dceList);
				
				Logger.info(logInfo, "onMessage", "Processing completed for header Id " + dceId);
				
			}
		} catch (Throwable e) {
				Logger.error(this.getClass().getName(),"onMessage","Error retrieving message in DataChangeEventsQueueReaderBean",e);
				CoronaErrorHandler.logError(this.getClass(),"EVF0046","onMessage","Caught exception while triggering onMessage for MDB DataChangeEventsQueueReaderBean",false,e,true);            
		} 
	}
	

	protected void processEvent(List<DataChangeEvent> events) {
		DataChangeEventsProcessor gProcessor = new DataChangeEventsProcessor(
				events);
		
		String grpStatus = gProcessor.getProcessingStatusGroup();
		Logger.info(logInfo, "processEvent", " ID  - "
				+ gProcessor.getDataChangeEventBatchId() + "%%%%%%%%%%%%% processing status : "
				+ grpStatus);
	}
}
