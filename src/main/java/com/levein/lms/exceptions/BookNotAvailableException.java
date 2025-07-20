package com.levein.lms.exceptions;

public class BookNotAvailableException extends RuntimeException {

    public BookNotAvailableException(String msg) {
        super(msg);
    }
}
