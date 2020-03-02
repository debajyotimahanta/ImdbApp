package com.imdb.exceptions;

public class RetryException extends RuntimeException {

    public RetryException(Exception ex) {
        super(ex);
    }
}
