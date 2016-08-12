package com.hp.psg.corona.common.jmx.handler;

import java.lang.management.ManagementFactory;

import javax.management.MBeanServer;
import javax.management.ObjectName;

import com.hp.psg.corona.datachange.handler.DataChangeCallHandler;
import com.hp.psg.corona.datachange.plugin.interfaces.IDataChangeEventsCallHandler;

public class DataChangeJmxAgent {

	   private MBeanServer mbs = null;

	   public DataChangeJmxAgent() {

	      // Get the platform MBeanServer
	       mbs = ManagementFactory.getPlatformMBeanServer();

	      // Unique identification of MBeans
	      IDataChangeEventsCallHandler dcCallHandlerBean = new DataChangeCallHandler();
	      ObjectName dceCallHandlerJmxBeanName = null;

	      try {
	         // Uniquely identify the MBeans and register them with the platform MBeanServer 
	    	  dceCallHandlerJmxBeanName = new ObjectName("DataChangeJmxAgent:name=dceCallHandlerJmxBean");
	         mbs.registerMBean(dcCallHandlerBean, dceCallHandlerJmxBeanName);
	      } catch(Exception e) {
	         e.printStackTrace();
	      }
	   }
}
