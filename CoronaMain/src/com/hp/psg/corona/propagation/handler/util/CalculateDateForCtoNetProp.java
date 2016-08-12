package com.hp.psg.corona.propagation.handler.util;

import com.hp.psg.corona.propagation.handler.interfaces.IBeanPropagationUtil;

public class CalculateDateForCtoNetProp implements IBeanPropagationUtil{
	
	public Object getConstructArgObject(){
		return new java.util.Date();
	}
}
