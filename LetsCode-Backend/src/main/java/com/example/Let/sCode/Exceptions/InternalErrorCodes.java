package com.example.Let.sCode.Exceptions;

public enum InternalErrorCodes {

    cfUserIdNotFound("CodeForces Id,","1"),
    AtUserIdNotFound("AtCoder Id,","2"),
    LcUserIdNotFound("LeetCode Id,","0"),
    UserIdNotFound("","3");

    private final String message;
    private final String code;

    InternalErrorCodes(String message,String code){
        this.message = message;
        this.code=code;
    }

    public String getMessage() {
        return message;
    }
    public String getCode() {
        return code;
    }
}

