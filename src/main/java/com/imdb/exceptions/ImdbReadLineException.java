package com.imdb.exceptions;

public class ImdbReadLineException extends RuntimeException {
    public ImdbReadLineException(Exception ex) {
        super(ex);
    }
}
