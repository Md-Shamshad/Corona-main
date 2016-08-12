package com.hp.psg.corona.replication.common.exception;

public class CoronaException extends Exception {
	
	private int errorNumber = 0;
	
	public CoronaException() {
        super();
    }

    public CoronaException(String message, Throwable throwable) {
        super(message,throwable);
    }

    public CoronaException(String message) {
        super(message);
    }
    
    public CoronaException(String message, Exception ex) {
        super(message,ex);
    }
    
    public CoronaException(int messageNumber, String message, Exception ex) {
        super(message,ex);
        this.errorNumber = messageNumber;
    }

	public int getErrorNumber() {
		return errorNumber;
	}

	public void setErrorNumber(int errorNumber) {
		this.errorNumber = errorNumber;
	}
    
      
}//class
