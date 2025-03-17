package com.multi_vendo_ecom.ecommerce.multivendor.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

import java.time.LocalDateTime;

@ControllerAdvice // this is because we are implementing a global error handler
public class GlobalException {

    @ExceptionHandler(SellerException.class)
    public ResponseEntity<ErrorDetails> sellerExceptionHandler(SellerException se, WebRequest req){
    ErrorDetails errorDetails = new ErrorDetails();
    errorDetails.setError(se.getMessage());
    errorDetails.setDetails(req.getDescription(false));
    errorDetails.setTimestamp(LocalDateTime.now());
    return new ResponseEntity<>(errorDetails, HttpStatus.BAD_REQUEST);

    }


    @ExceptionHandler(ProductException.class)
    public ResponseEntity<ErrorDetails> productExceptionHandler(SellerException se, WebRequest req){
        ErrorDetails errorDetails = new ErrorDetails();
        errorDetails.setError(se.getMessage());
        errorDetails.setDetails(req.getDescription(false));
        errorDetails.setTimestamp(LocalDateTime.now());
        return new ResponseEntity<>(errorDetails, HttpStatus.BAD_REQUEST);

    }

}
