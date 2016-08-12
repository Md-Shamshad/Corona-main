package com.hp.psg.corona.replication.common.exception;

/**
 * <p>Title: ServiceLocatorException</p>
 * <p>Description: Exception thrown by ServiceLocator </p>
 * <p>Copyright: Copyright (c) 2012</p>
 * <p>Company: Hewlett Packard</p>
 * 
 * @version 1.0
 */
public class ServiceLocatorException extends ReplicationException {
    /**
	 * 
	 */
	private static final long serialVersionUID = -1689685477444876201L;

	public ServiceLocatorException() {
        super();
    }

    public ServiceLocatorException(String message, Throwable throwable) {
        super(message,throwable);
    }

    public ServiceLocatorException(String message) {
        super(message);
    }
    
}//class

