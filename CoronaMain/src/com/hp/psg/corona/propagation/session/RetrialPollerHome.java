package com.hp.psg.corona.propagation.session;

import java.rmi.RemoteException;

import javax.ejb.CreateException;
import javax.ejb.EJBHome;

/**
 * @author dudeja
 * @version 1.0
 */
public interface RetrialPollerHome extends EJBHome {
	RetrialPoller create() throws RemoteException, CreateException;
}
