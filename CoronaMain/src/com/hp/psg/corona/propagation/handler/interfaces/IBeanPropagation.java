package com.hp.psg.corona.propagation.handler.interfaces;

import java.util.Map;

import com.hp.psg.common.error.CoronaException;
import com.hp.psg.corona.common.beans.CTODaxDataBeanGeneral;

/**
 * @author dudeja
 * @version 1.0
 * 
 */
public interface IBeanPropagation {

	public void prepareBean(Map<String, Object> constructArgs);
	
	public CTODaxDataBeanGeneral processBeans(CTODaxDataBeanGeneral cto)
			throws CoronaException;

}
