package com.example.Let.sCode.Exceptions;


public class UserIdNotFoundExeption extends RuntimeException{
    public UserIdNotFoundExeption(String msg)
    {
        super(msg);
    }
   public UserIdNotFoundExeption(Throwable msg)
    {
        super(msg);
    }

    public UserIdNotFoundExeption(String msg,Throwable err)
    {
        super(msg,err);
    }
}
