package com.imdb.exceptions;

/**
 * If we are unable to parse a line from the csv file provided this exception is thrown
 */
public class ImdbReadLineException extends RuntimeException {
    public ImdbReadLineException(Exception ex) {
        super(ex);
    }
}
