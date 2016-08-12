package com.hp.psg.corona.propagation.session;

import java.rmi.RemoteException;

import javax.ejb.EJBException;
import javax.ejb.EJBObject;

/*
* @author dudeja
* @version 1.0
*
*/
public interface PropagationQueuePoller extends EJBObject {
	public void invokeRemote() throws EJBException, RemoteException;

}
