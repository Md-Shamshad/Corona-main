package com.hp.psg.corona.common.util;

import javax.jms.JMSException;
import javax.jms.Session;
import javax.jms.TextMessage;
import javax.jms.Topic;
import javax.jms.TopicConnection;
import javax.jms.TopicConnectionFactory;
import javax.jms.TopicPublisher;
import javax.jms.TopicSession;
import javax.naming.Context;
import javax.naming.NamingException;

import com.hp.psg.common.util.logging.LoggerInfo;
import com.hp.psg.corona.error.util.CoronaErrorHandler;

/**
 * @author dudeja
 * @version 1.0
 *
 */
public class PublishTopicToJms {

	protected TopicConnectionFactory tconFactory;
	protected TopicConnection tcon;
	protected TopicSession tsession;
	protected TopicPublisher tpublisher;
	protected Topic topic;
	protected TextMessage msg;
	LoggerInfo logInfo=null;
	
	public void init(String topicName, String topicConnectionFactory)
			throws NamingException, JMSException, Exception {
		logInfo = new LoggerInfo (this.getClass().getName());
		
		Context ctx = JndiUtils.getInitialContext(Config.getJndiClusterUrl());
		tconFactory = (TopicConnectionFactory) ctx
				.lookup(topicConnectionFactory);
		tcon = tconFactory.createTopicConnection();
		tsession = tcon.createTopicSession(false, Session.AUTO_ACKNOWLEDGE);
		topic = (Topic) ctx.lookup(topicName);
		tpublisher = tsession.createPublisher(topic);
		msg = tsession.createTextMessage();
		tcon.start();
	}

	public void close() throws JMSException {
		tpublisher.close();
		tsession.close();
		tcon.close();
	}

	public void sendMessage(String topicName, String connFactory,
			String messageText) {
		try {
			init(topicName, connFactory);
			msg.setText(messageText);
			tpublisher.publish(msg);
			Logger.info(logInfo,"sendMessage", "Message : "+ msg+" Publishing a message to --- > "+topicName);
		} catch (Exception ex) {
			CoronaErrorHandler.logError(ex, null, null);
		}
	}

}
