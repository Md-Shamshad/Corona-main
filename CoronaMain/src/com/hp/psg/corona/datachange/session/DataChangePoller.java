package com.hp.psg.corona.datachange.session;

import java.rmi.RemoteException;

import javax.ejb.EJBException;
import javax.ejb.EJBObject;

/*
* @author dudeja
* @version 1.0
*
*/
public interface DataChangePoller extends EJBObject {
	public void invokeRemote() throws EJBException, RemoteException;

}
