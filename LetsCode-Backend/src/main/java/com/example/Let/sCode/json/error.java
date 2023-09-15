package com.example.Let.sCode.json;

public class error {
    private int errorCode;
    private String message;
    private String internalCode;
    private long timestamp;
    
    public error(int errorCode, String message, String internalCode, long timestamp) {
        this.errorCode = errorCode;
        this.message = message;
        this.internalCode = internalCode;
        this.timestamp = timestamp;
    }

    public error(String message) {
        this.message = message;
    }

    public error()
    {

    }
    
  

    public error(int errorCode, String message, long timestamp) {
        this.errorCode = errorCode;
        this.message = message;
        this.timestamp = timestamp;
    }

    public error(Throwable msg) {
    }

    public error(String msg, Throwable err) {
    }

    public int getErrorCode() {
        return errorCode;
    }
    public void setErrorCode(int errorCode) {
        this.errorCode = errorCode;
    }
    public String getMessage() {
        return message;
    }
    public void setMessage(String message) {
        this.message = message;
    }
    public long getTimestamp() {
        return timestamp;
    }
    public void setTimestamp(long l) {
        this.timestamp = l;
    }

    public void setInternalCode(String internalCode) {
        this.internalCode = internalCode;
    }

    public String getInternalCode() {
        return internalCode;
    }

}
