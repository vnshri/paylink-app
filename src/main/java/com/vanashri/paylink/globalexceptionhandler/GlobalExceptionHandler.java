package com.vanashri.paylink.globalexceptionhandler;

import com.vanashri.paylink.exception.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

@RestControllerAdvice
public class GlobalExceptionHandler extends RuntimeException{

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<String> resourceNotFoundException(ResourceNotFoundException ex){
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.NOT_FOUND);
    }
    @ExceptionHandler(DuplicatePaymentException.class)
    public ResponseEntity<String> duplicatePaymentId(DuplicatePaymentException ex){
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.NOT_ACCEPTABLE);
    }
    @ExceptionHandler(PaymentIdNotFoundException.class)
    public ResponseEntity<String> handlePaymentIdNotFound(PaymentIdNotFoundException ex){
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.NOT_FOUND);
    }
@ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<String> handleMethodArgumentTypeMistMatch(MethodArgumentTypeMismatchException ex){
        String message= "Invalid value: '" + ex.getValue()+" for parameter: "+ ex.getName()+" the required type is: "+
                ex.getRequiredType().getSimpleName();
        return new ResponseEntity<>(message, HttpStatus.NOT_ACCEPTABLE);
    }
    @ExceptionHandler(IdNotFoundException.class)
    public ResponseEntity<String> handleIdNotFoundException(IdNotFoundException ex){
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.NOT_FOUND);
    }
    @ExceptionHandler(PayerNameNotFound.class)
    public ResponseEntity<String> handlePayerNameNotFound(PayerNameNotFound ex){
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.NOT_FOUND);
    }
    @ExceptionHandler(StatusNotFound.class)
    public ResponseEntity<String> handleStatusNotFound(StatusNotFound ex){
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.NOT_FOUND);
    }
    @ExceptionHandler(PaymentDateNotFoundException.class)
    public ResponseEntity<String> handlePaymentDateNotFound(PaymentDateNotFoundException ex){
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(KeywordNotFoundException.class)
    public ResponseEntity<String> handleKeywordNotFound(KeywordNotFoundException ex){
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.NOT_FOUND);
    }

//    Users
    @ExceptionHandler(UserEmailAlreadyExistsException.class)
    public ResponseEntity<String> handleuserNameExists(UserEmailAlreadyExistsException ex){
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.ALREADY_REPORTED);
    }

    @ExceptionHandler(NoUserFountException.class)
    public ResponseEntity<String> handleNoUserFound(NoUserFountException ex){
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.NOT_FOUND);
    }
}
