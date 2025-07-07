package com.vanashri.paylink.exception;

public class PaymentIdNotFoundException extends RuntimeException{
    public PaymentIdNotFoundException(String message){
        super(message);
    }

}
