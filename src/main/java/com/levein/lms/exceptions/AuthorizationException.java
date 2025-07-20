package com.levein.lms.exceptions;

public class AuthorizationException extends RuntimeException{
    AuthorizationException(String msg){
        super(msg);
    }
}
