package com.hp.psg.corona.replication.session;

import java.rmi.RemoteException;

import javax.ejb.CreateException;
import javax.ejb.EJBHome;

/**
 * @author dholakia
 * @version 1.0
 */
public interface ReplicationPollerHome extends EJBHome {
	ReplicationPoller create() throws RemoteException, CreateException;

}
