package com.hp.psg.corona.replication.common.exception;

import com.hp.psg.corona.replication.common.message.MessageConstants;

/**
 * <p>Title: HpException</p>
 * <p>Description:  Represents a generic service exception.</p>
 * <p>Company: HP</p>
 * 
 * @author 
 */

public class ReplicationException extends CoronaException {
    
    private static final long serialVersionUID = 8650641258966391435L;
    private Throwable origin=null;
    private String languageCode = null;
        
    private int errorNumber = 0;
   
    //The message shown as part of the exception
    private String errorMessage = null;
    
    private String faultCode = null;
    
    public ReplicationException() {
        super();       
    }

    public ReplicationException(String message, Throwable throwable) {
        super(message,throwable);
        this.errorMessage = message;
    }

    public ReplicationException(String message) {
        super(message);
        this.errorMessage = message;
    }

    public ReplicationException(int errorNumber, String message, Throwable throwable) {
        super(message,throwable);
        this.errorNumber = errorNumber;
        this.errorMessage = message;
    }
    
    public ReplicationException(int messageNumber, String languageCode, String messageText, Throwable underlyingException){
    	super(messageText);
    	this.errorNumber = messageNumber;
    	this.languageCode = languageCode;
    	this.errorMessage = messageText;
    	this.origin = underlyingException;     	
	}

    public ReplicationException(int errorNumber, String message) {
        super(message);
        this.errorNumber = errorNumber;
        this.errorMessage = message;
    }
    
    public ReplicationException(String errorCode, String message){
    	super(message);
    	
    	this.faultCode = errorCode;
    	this.errorMessage = message;
    	
    }

    public void setErrorNumber(int errorNumber) {
        this.errorNumber = errorNumber;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public int getErrorNumber() {
        return errorNumber;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

	public String getLanguageCode() {
		return languageCode;
	}

	public void setLanguageCode(String languageCode) {
		this.languageCode = languageCode;
	}

	public Throwable getOrigin() {
		return origin;
	}

	public void setOrigin(Throwable origin) {
		this.origin = origin;
	}

	/**
	 * @return the faultCode
	 */
	public String getFaultCode() {
		return faultCode;
	}

	/**
	 * @param faultCode the faultCode to set
	 */
	public void setFaultCode(String faultCode) {
		this.faultCode = faultCode;
	}
	
}//end of class
