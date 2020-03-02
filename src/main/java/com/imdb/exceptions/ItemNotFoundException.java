package com.imdb.exceptions;

/**
 * When an TV Series doesn not exists and we are trying to update it, this exception is thrown
 */
public class ItemNotFoundException extends RuntimeException {
    public ItemNotFoundException(String message) {
        super(message);
    }
}
