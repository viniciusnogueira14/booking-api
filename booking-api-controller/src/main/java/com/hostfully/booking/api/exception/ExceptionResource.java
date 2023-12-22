package com.hostfully.booking.api.exception;


public record ExceptionResource(
        Integer status,
        String message
) { }
