package com.hostfully.booking.api;

import com.hostfully.booking.api.exception.BusinessException;
import com.hostfully.booking.api.helper.BookingHelper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlGroup;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.LocalDate;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@RunWith(SpringRunner.class)
@Sql(value = "classpath:init.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_CLASS)
class BookingHelperIntegratedTest {

    @Autowired
    private BookingHelper helper;

    @DisplayName(value = "Check all scenarios where there is no overlapping between dates. " +
            "The booking of reference is the code 1 in the init.sql script")
    @ParameterizedTest
    @CsvSource({
            "0, 'c0f926fa-5d63-4d33-8476-74ce938d6bff', '2024-09-01', '2024-09-30'",
            "0, 'c0f926fa-5d63-4d33-8476-74ce938d6bff', '2024-11-15', '2024-11-20'",
            "1, 'c0f926fa-5d63-4d33-8476-74ce938d6bff', '2024-09-10', '2024-10-30'",
            "1, 'c0f926fa-5d63-4d33-8476-74ce938d6bff', '2024-11-01', '2024-11-20'",
            "1, 'c0f926fa-5d63-4d33-8476-74ce938d6bff', '2024-10-01', '2024-11-20'",
            "1, 'c0f926fa-5d63-4d33-8476-74ce938d6bff', '2024-10-20', '2024-11-10'",
            "1, 'c0f926fa-5d63-4d33-8476-74ce938d6bff', '2024-10-10', '2024-10-10'",
            "1, 'c0f926fa-5d63-4d33-8476-74ce938d6bff', '2024-11-11', '2024-11-11'",
            "1, 'c0f926fa-5d63-4d33-8476-74ce938d6bff', '2024-11-01', '2024-11-01'",
    })
    void validateOverlappedDatesTest_NoOverlap(Long bookingId, String propertyUuid, LocalDate beginDate, LocalDate endDate) throws BusinessException {
        helper.validateOverlappedDates(bookingId, UUID.fromString(propertyUuid),
                beginDate, endDate);
    }

    @DisplayName(value = "Check all scenarios where there is any kind of overlapping. " +
            "The booking of reference is the code 1 in the init.sql script")
    @ParameterizedTest
    @CsvSource({
            "0, '2024-09-10', '2024-10-30'",
            "0, '2024-11-01', '2024-12-31'",
            "0, '2024-10-01', '2024-11-20'",
            "0, '2024-10-20', '2024-11-10'",
            "0, '2024-10-10', '2024-10-10'",
            "0, '2024-11-11', '2024-11-11'",
            "0, '2024-11-01', '2024-11-01'",
            "2, '2024-09-10', '2024-10-30'",
            "2, '2024-11-01', '2024-12-31'",
            "2, '2024-10-01', '2024-11-20'",
            "2, '2024-10-20', '2024-11-10'",
            "2, '2024-10-10', '2024-10-10'",
            "2, '2024-11-11', '2024-11-11'",
            "2, '2024-11-01', '2024-11-01'"
    })
    void validateOverlappedDatesTest_HasOverlap(Long bookingId, LocalDate beginDate, LocalDate endDate) {
        var except = assertThrows(BusinessException.class,
                () -> helper.validateOverlappedDates(bookingId, UUID.fromString("c0f926fa-5d63-4d33-8476-74ce938d6bff"),
                        beginDate, endDate));
        assertNotNull(except);
        assertEquals("Error on saving Booking. The dates selected overlaps another existing booking", except.getMessage());
    }



}
