package com.vanashri.paylink.exception;

public class PaymentDateNotFoundException extends RuntimeException{
    public PaymentDateNotFoundException(String message){
        super(message);
    }
}
