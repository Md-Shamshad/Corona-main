package com.hp.psg.corona.datachange.session;

import java.rmi.RemoteException;

import javax.ejb.CreateException;
import javax.ejb.EJBHome;

/*
* @author dudeja
* @version 1.0
*
*/
public interface DataChangePollerHome extends EJBHome {
	DataChangePoller create() throws RemoteException, CreateException;
}
