package com.vanashri.paylink.exception;

public class DuplicatePaymentException extends RuntimeException{
    public DuplicatePaymentException(String paymentId){
        super("The PaymentId already exists "+ paymentId);
    }
}
