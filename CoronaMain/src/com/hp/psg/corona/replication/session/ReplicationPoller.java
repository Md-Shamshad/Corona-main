package com.hp.psg.corona.replication.session;

import java.rmi.RemoteException;

import javax.ejb.EJBException;
import javax.ejb.EJBObject;

/*
* @author dudeja
* @version 1.0
*
*/
public interface ReplicationPoller extends EJBObject {
	public void invokeRemote() throws EJBException, RemoteException;
	public void reinitializeTimer(Long timerInMilliSeconds) throws EJBException , RemoteException;

}
