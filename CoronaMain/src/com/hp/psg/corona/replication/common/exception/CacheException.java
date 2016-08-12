package com.hp.psg.corona.replication.common.exception;

/**
 * This class represents the CacheFramework related exception.
 * During CacheFramework if any exception comes need to wrap the exception 
 * into to this exception class.  
 * 
 * @author kn
 *
 */
public class CacheException extends CoronaException  {
    
	private static final long serialVersionUID = -2061283911246600230L;
	private Throwable origin=null;
    private String languageCode = null;
        
    private int errorNumber = 0;
   
    //The message shown as part of the exception
    private String errorMessage = null;
    
    private String faultCode = null;
	
	public CacheException() {
        super();
    }
	
    public CacheException(String message, Throwable throwable) {
        super(message,throwable);
    }

    public CacheException(String message) {
        super(message);
    }

    public CacheException(int messageNumber, String languageCode, String messageText, Throwable ex){
    	super(messageText);
    	this.errorNumber = messageNumber;
    	this.languageCode = languageCode;
    	this.errorMessage = messageText;    	
    }
    
    public CacheException(int messageNumber, String messageText, Throwable ex){
    	super(messageText,ex);
    	this.errorNumber = messageNumber;
    	this.languageCode = languageCode;
    }
    
    public CacheException(int messageNumber, String messageText){
    	super(messageText);
    	this.errorNumber = messageNumber;
    }

	public Throwable getOrigin() {
		return origin;
	}

	public void setOrigin(Throwable origin) {
		this.origin = origin;
	}

	public String getLanguageCode() {
		return languageCode;
	}

	public void setLanguageCode(String languageCode) {
		this.languageCode = languageCode;
	}

	public int getErrorNumber() {
		return errorNumber;
	}

	public void setErrorNumber(int errorNumber) {
		this.errorNumber = errorNumber;
	}

	public String getErrorMessage() {
		return errorMessage;
	}

	public void setErrorMessage(String errorMessage) {
		this.errorMessage = errorMessage;
	}

	public String getFaultCode() {
		return faultCode;
	}

	public void setFaultCode(String faultCode) {
		this.faultCode = faultCode;
	}
       
    
}//end of class

