package com.myblog.blogapp.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NOT_FOUND)                                                            //Exception handling
public class ResourceNotFoundException extends RuntimeException{
    private String resourceName;
    private String fieldName;
    private long fieldValue;

    public ResourceNotFoundException(String resourceName, String fieldName, long fieldValue){            //constructor with 3args use for setter
        super(String.format("%s not found with %s : '%s'", resourceName, fieldName, fieldValue));        //Post not found with id:1000 (format)     //resourceName:Post, fieldName:id, fieldValue:1
        this.resourceName = resourceName;                                                                //initialization of variables
        this.fieldName = fieldName;
        this.fieldValue = fieldValue;
    }

    public String getResourceName() {          //getters
        return resourceName;
    }
    public String getFieldName() {
        return fieldName;
    }
    public long getFieldValue() {
        return fieldValue;
    }
}
