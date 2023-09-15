package com.example.Let.sCode.Exceptions;


import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import com.example.Let.sCode.json.error;


@ControllerAdvice
public class Execptionhandler {
    
    InternalErrorCodes[] errorCodes = InternalErrorCodes.values();
    
    @ExceptionHandler
    public ResponseEntity<error> errormethod(UserIdNotFoundExeption userIdNotFoundExeption)
    {
        error Error=new error();
        Error.setErrorCode(HttpStatus.NOT_FOUND.value());
        Error.setTimestamp(System.currentTimeMillis());
        String msg=userIdNotFoundExeption.getMessage();
        if(msg.contains("User not found with username")||msg.contains("UserName is taken")){Error.setMessage(msg);Error.setInternalCode("5");return new ResponseEntity<>(Error,HttpStatus.NOT_FOUND);}
        if(msg.contains("Email id not found with email")){Error.setMessage(msg);Error.setInternalCode("6");return new ResponseEntity<>(Error,HttpStatus.NOT_FOUND);}
        if(msg.contains("Phone Number not found")){Error.setMessage(msg);Error.setInternalCode("7");return new ResponseEntity<>(Error,HttpStatus.NOT_FOUND);}
        if(msg.contains("UsersIds Not Set")){Error.setMessage(msg);Error.setInternalCode("43");return new ResponseEntity<>(Error,HttpStatus.NOT_FOUND);}
        if(msg.contains("Email Not Exist")){Error.setMessage(msg);Error.setInternalCode("12");return new ResponseEntity<>(Error,HttpStatus.NOT_FOUND);}
       

        Error.setInternalCode(userIdNotFoundExeption.getMessage());
        String message="The Follwing userId's are incorrect ";
        for(InternalErrorCodes code:errorCodes)
        {
            if(userIdNotFoundExeption.getMessage().contains(code.getCode())){
                message+=code.getMessage();
            }
        }
        message=message.substring(0, message.length()-1);
        Error.setMessage(message);
        return new ResponseEntity<>(Error,HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler
    public ResponseEntity<error> error(Throwable err)
    {
        if(err.getMessage().contains("codeforces"))return errormethod(new UserIdNotFoundExeption(InternalErrorCodes.cfUserIdNotFound.getCode()));   
        error Error=new error();
        Error.setErrorCode(HttpStatus.BAD_REQUEST.value());
        Error.setMessage(err.getMessage());
        if(err.getMessage().equals("INVALID_CREDENTIALS"))Error.setInternalCode("4");
        if(err.getMessage().contains("User not found with username"))Error.setInternalCode("3");
        if(err.getMessage().contains("UserName is taken"))Error.setInternalCode("5");
        
        if(err.getMessage().contains("Email id not found with email"))Error.setInternalCode("6");
        if(err.getMessage().contains("Phone Number not found"))Error.setInternalCode("7");
       if(err.getMessage().contains("UsersIds Not Set"))Error.setInternalCode("43");
        if(err.getMessage().contains("Email Not Exist"))Error.setInternalCode("12");
        Error.setTimestamp(System.currentTimeMillis());
        return new ResponseEntity<>(Error,HttpStatus.BAD_REQUEST);
    }
}
