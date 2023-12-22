package com.hostfully.booking.api.helper;

import com.hostfully.booking.api.entity.BookingStatus;
import com.hostfully.booking.api.exception.BusinessException;
import com.hostfully.booking.api.exception.ObjectNotFoundException;
import com.hostfully.booking.api.exception.ParameterValidationException;
import com.hostfully.booking.api.repository.BookingRepository;
import com.hostfully.booking.api.utils.ObjectMockUtils;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class BookingHelperTest {

    @Mock
    private ModelMapper mapper;
    @Mock
    private BookingRepository repository;

    @InjectMocks
    private BookingHelper helper;


    @Test
    void validateOverlappedDatesTest_HasOverlap_MustThrowBusinessException() {

        when(repository.hasOverlappingDates(
                anyLong(), any(UUID.class), anyList(), any(LocalDate.class), any(LocalDate.class)))
                .thenReturn(true);

        var except = assertThrows(BusinessException.class,
                () -> helper.validateOverlappedDates(100L,
                        UUID.fromString("a50df57f-8554-4268-97c4-a0777f77317a"),
                        LocalDate.of(2023, 12, 10),
                        LocalDate.of(2023, 12, 20)));

        assertNotNull(except);
        assertEquals("Error on saving Booking. The dates selected overlaps another existing booking", except.getMessage());
    }

    @ParameterizedTest
    @CsvSource(value = {
            "'' , 'Invalid UUID string: '",
            "123Testing456, Invalid UUID string: 123Testing456",
            "null, The Booking ID must not be NULL or EMPTY"
    }, nullValues = { "null"})
    void findBookingOrElseThrowParameterizedTest(String parameter, String exceptionMessage) {
        var except = assertThrows(ParameterValidationException.class,
                () -> helper.findBookingOrElseThrow(parameter));

        assertNotNull(except);
        assertEquals(exceptionMessage, except.getMessage());
    }

    @Test
    void findBookingOrElseThrowTest_UUIDNotFound_MustThrowObjectNotFoundException() {
        when(repository.findByUuid(UUID.fromString("55a6b2ea-7d44-40d3-8eb1-8967110d3df8")))
                .thenReturn(Optional.empty());
        var except = assertThrows(ObjectNotFoundException.class,
                () -> helper.findBookingOrElseThrow("55a6b2ea-7d44-40d3-8eb1-8967110d3df8"));

        assertNotNull(except);
        assertEquals("The Booking was not found in the Database", except.getMessage());
    }

    @Test
    void findBookingOrElseThrowTest_UUIDFound_MustReturnBooking() throws ParameterValidationException {
        var bookingMock = ObjectMockUtils.getSavedBookingMockHappyPath();
        when(repository.findByUuid(UUID.fromString("55a6b2ea-7d44-40d3-8eb1-8967110d3df8")))
                .thenReturn(Optional.of(ObjectMockUtils.getSavedBookingMockHappyPath()));

        var savedBooking = helper.findBookingOrElseThrow("55a6b2ea-7d44-40d3-8eb1-8967110d3df8");
        assertNotNull(savedBooking);
        assertEquals(bookingMock.getId(), savedBooking.getId());
        assertEquals(bookingMock.getUuid(), savedBooking.getUuid());
        assertEquals(bookingMock.getGuests(), savedBooking.getGuests());
        assertEquals(bookingMock.getStatus(), savedBooking.getStatus());
        assertEquals(bookingMock.getProperty(), savedBooking.getProperty());
        assertEquals(bookingMock.getBeginAt(), savedBooking.getBeginAt());
        assertEquals(bookingMock.getEndAt(), savedBooking.getEndAt());
    }

    @Test
    void mergeEntityToUpdateTest_BlockedStatus() {
        var entity = ObjectMockUtils.getSavedBookingMockHappyPath();
        entity.setStatus(BookingStatus.BLOCKED);
        entity.setGuests(new ArrayList<>());
        var resource = ObjectMockUtils.getBookingRequestResourceMockHappyPath();
        resource.setBeginAt(LocalDate.of(2024, 5, 20));
        resource.setEndAt(LocalDate.of(2024, 8, 30));

        var mergedBooking = helper.mergeEntityToUpdate(resource, entity);
        assertEquals(resource.getBeginAt(), mergedBooking.getBeginAt());
        assertEquals(resource.getEndAt(), mergedBooking.getEndAt());
        assertEquals(BookingStatus.BLOCKED, entity.getStatus());
        assertTrue(mergedBooking.getGuests().isEmpty());
        assertFalse(resource.getGuests().isEmpty());
    }

    @Test
    void mergeEntityToUpdateTest_BookedStatus() {
        var entity = ObjectMockUtils.getSavedBookingMockHappyPath();
        entity.setStatus(BookingStatus.BOOKED);
        entity.setGuests(new ArrayList<>());
        var resource = ObjectMockUtils.getBookingRequestResourceMockHappyPath();
        resource.setBeginAt(LocalDate.of(2024, 5, 20));
        resource.setEndAt(LocalDate.of(2024, 8, 30));

        var mergedBooking = helper.mergeEntityToUpdate(resource, entity);
        assertEquals(resource.getBeginAt(), mergedBooking.getBeginAt());
        assertEquals(resource.getEndAt(), mergedBooking.getEndAt());
        assertEquals(BookingStatus.BOOKED, entity.getStatus());
        assertFalse(mergedBooking.getGuests().isEmpty());
        assertFalse(resource.getGuests().isEmpty());
        assertEquals(resource.getGuests().size(), mergedBooking.getGuests().size());
    }

    @Test
    void mergeEntityToUpdateTest_CanceledStatus() {
        var entity = ObjectMockUtils.getSavedBookingMockHappyPath();
        entity.setStatus(BookingStatus.CANCELED);
        entity.setGuests(new ArrayList<>());
        var resource = ObjectMockUtils.getBookingRequestResourceMockHappyPath();
        resource.setBeginAt(LocalDate.of(2024, 5, 20));
        resource.setEndAt(LocalDate.of(2024, 8, 30));

        var mergedBooking = helper.mergeEntityToUpdate(resource, entity);
        assertEquals(resource.getBeginAt(), mergedBooking.getBeginAt());
        assertEquals(resource.getEndAt(), mergedBooking.getEndAt());
        assertEquals(BookingStatus.CANCELED, entity.getStatus());
        assertFalse(mergedBooking.getGuests().isEmpty());
        assertFalse(resource.getGuests().isEmpty());
        assertEquals(resource.getGuests().size(), mergedBooking.getGuests().size());
    }
}
