package com.myblog.blogapp.exception;

import com.myblog.blogapp.payload.ErrorDetails;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.Date;

@ControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    // handle Specific exception
    @ExceptionHandler(ResourceNotFoundException.class)                     //when specific exception obj. created, it will move here
    public ResponseEntity<ErrorDetails> handleResourceNotFoundException(   //method created
            ResourceNotFoundException exception,                           //passing ResourceNotFoundException
            WebRequest webRequest                                          //builtin class
    ){
        ErrorDetails errorDetails = new ErrorDetails(new Date(), exception.getMessage(),        //timestamp , msg: Post not found with id:100
                webRequest.getDescription(false));   //error detail obj created   //uri details: /api/posts/100
        return new ResponseEntity<>(errorDetails, HttpStatus.NOT_FOUND);                       //error detail obj return
    }


    // handle Global exception
    @ExceptionHandler(Exception.class)                                      //when other exception obj. created, it will move here
    public ResponseEntity<ErrorDetails> handleAllException(                 //method created
            Exception exception,                                            //passing Exception
            WebRequest webRequest                                           //builtin class
    ){
        ErrorDetails errorDetails = new ErrorDetails(new Date(), exception.getMessage(),        //msg created internally
                webRequest.getDescription(false));
        return new ResponseEntity<>(errorDetails, HttpStatus.CONFLICT);
    }

}