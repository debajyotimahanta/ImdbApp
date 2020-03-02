package com.imdb.exceptions;

/**
 * General File handeling exception thrown by {@link com.imdb.dao.ImdbFile}
 */
public class FileHandlerException extends RuntimeException {
    public FileHandlerException(String message) {
        super(message);
    }
}
