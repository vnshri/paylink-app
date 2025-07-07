package com.vanashri.paylink.exception;

public class UserEmailAlreadyExistsException extends RuntimeException{
    public UserEmailAlreadyExistsException(String message){
        super(message);
    }
}
