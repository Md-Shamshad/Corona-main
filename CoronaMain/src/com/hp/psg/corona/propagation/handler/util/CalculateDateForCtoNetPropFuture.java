package com.hp.psg.corona.propagation.handler.util;

import java.util.Calendar;

import com.hp.psg.corona.propagation.handler.interfaces.IBeanPropagationUtil;

public class CalculateDateForCtoNetPropFuture implements IBeanPropagationUtil{
	
	public Object getConstructArgObject(){
		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.DATE, 1);
		
		return cal.getTime();
	}
}
