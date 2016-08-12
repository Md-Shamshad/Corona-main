package com.hp.psg.corona.common.util;

import javax.jms.QueueConnection;
import javax.jms.QueueConnectionFactory;
import javax.jms.QueueSender;
import javax.jms.Session;
import javax.jms.TextMessage;
import javax.naming.Context;

import com.hp.psg.common.error.util.CoronaErrorConstants;
import com.hp.psg.corona.error.util.CoronaErrorHandler;

public class SendMessageToJmsQueue {
		public SendMessageToJmsQueue() {
		}

		private javax.jms.QueueSession qSession = null;
		private QueueConnection qConnection = null;
		private QueueSender qSender = null;

		public void sendPropagationMessage(String msg, Long id) throws Exception {

			try {
				if (qSender == null || qSession == null){
					String connectionFactory=Config.getDefaultPropConnectionFactory();
					String queueName=Config.getDefaultPropagationEventsQueue();
					initJMS(queueName, connectionFactory);
				}
				TextMessage tm = qSession.createTextMessage (msg);
				tm.setLongProperty("GROUPID",id);

				//qSender.setPriority(priority.get);
				qSender.send (tm);

			}catch (Exception e){
				CoronaErrorHandler.logError(this.getClass(),CoronaErrorConstants.dataloadJMSErr,"send","Error sending message JMS Queue",false,e,true);
				throw e;
			}
		}

		public void sendDataChangeMessage(String msg, Long priority, Long id) throws Exception {

			try {
				if (qSender == null || qSession == null){
					String connectionFactory=Config.getDefaultTxnQueueConnectionFactory();
					String queueName=Config.getDefaultDataChangeEventsTxnQueue();
					initJMS(queueName, connectionFactory);
				}
				TextMessage tm = qSession.createTextMessage (msg);
				tm.setLongProperty("ID",id);

				//qSender.setPriority(priority.get);
				qSender.send (tm);

			}catch (Exception e){
				CoronaErrorHandler.logError(this.getClass(),CoronaErrorConstants.dataloadJMSErr,"send","Error sending message JMS Queue",false,e,true);
				throw e;
			}
		}


		public void initJMS(String queueName, String connectionFactory){
			try{
				destroyJMS();

				Logger.debug(this.getClass().getName(),"initJMS","**** Initial JMS ***** ");
				Context context =JndiUtils.getInitialContext(Config.getJndiClusterUrl());

				Logger.debug(this.getClass().getName(),"initJMS","*********Queue Depositor: Connecting to the "+connectionFactory
						+ " ConnFactory and the "+queueName +" Queue");

				QueueConnectionFactory factory = (QueueConnectionFactory) context.lookup(connectionFactory);

				qConnection = factory.createQueueConnection(); 
				qSession = qConnection.createQueueSession(false,Session.AUTO_ACKNOWLEDGE);

				javax.jms.Queue queue = (javax.jms.Queue)context.lookup(queueName);
				qSender = qSession.createSender(queue);

			} catch(Throwable t){
				CoronaErrorHandler.logError(this.getClass(),CoronaErrorConstants.dataloadJMSErr,"initJMS","Error sending message in DefaultDataChangeEventsTxnQueue",false,t,true);
				destroyJMS();
			}
		}

		public void destroyJMS(){
			try {

				if (qConnection != null){
					qConnection.close();
				}

			}catch (Throwable t){
				CoronaErrorHandler.logError(this.getClass(),CoronaErrorConstants.dataloadJMSErr,"destroyJMS","Error could not close jms connection.",false,t,true);
			}

		}
}
