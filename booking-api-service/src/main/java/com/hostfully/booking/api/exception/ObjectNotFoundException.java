package com.hostfully.booking.api.exception;

public class ObjectNotFoundException extends RuntimeException {

    public ObjectNotFoundException(String message) {
        super(message);
    }

    public ObjectNotFoundException(Class<?> clazz) {
        var message = "The " + clazz.getSimpleName() + " was not found in the Database";
        throw new ObjectNotFoundException(message);
    }
}
