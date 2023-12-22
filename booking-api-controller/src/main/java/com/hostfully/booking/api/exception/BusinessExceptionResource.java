package com.hostfully.booking.api.exception;

public record BusinessExceptionResource(
        int statusCode,
        String statusName,
        String message
) { }
