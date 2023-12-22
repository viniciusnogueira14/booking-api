package com.hostfully.booking.api.exception;

import java.util.List;

public record ValidationExceptionResource(
        int statusCode,
        String statusName,
        int errorCount,
        List<String> errorMessages
) { }
