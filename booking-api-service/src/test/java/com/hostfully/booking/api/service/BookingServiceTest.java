package com.hostfully.booking.api.service;

import com.hostfully.booking.api.entity.Booking;
import com.hostfully.booking.api.entity.BookingStatus;
import com.hostfully.booking.api.entity.Guest;
import com.hostfully.booking.api.entity.Property;
import com.hostfully.booking.api.exception.BusinessException;
import com.hostfully.booking.api.exception.ObjectNotFoundException;
import com.hostfully.booking.api.exception.ParameterValidationException;
import com.hostfully.booking.api.helper.BookingHelper;
import com.hostfully.booking.api.repository.BookingRepository;
import com.hostfully.booking.api.resource.BookingRequestResource;
import com.hostfully.booking.api.resource.BookingResponseResource;
import com.hostfully.booking.api.resource.GuestResource;
import com.hostfully.booking.api.utils.ObjectMockUtils;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class BookingServiceTest {

    @Mock
    private BookingHelper helper;

    @Mock
    private ModelMapper mapper;

    @Mock
    private BookingRepository repository;

    @Mock
    private PropertyService propertyService;

    @Mock
    private GuestService guestService;

    @InjectMocks
    private BookingService service;

    @Test
    void createBookingTest_PropertyIdNotFound_MustThrowObjectNotFoundException() {
        var resourceMock = new BookingRequestResource();
        resourceMock.setPropertyId("55a6b2ea-7d44-40d3-8eb1-8967110d3df8");
        when(propertyService.findByUUID(anyString())).thenReturn(Optional.empty());
        var exception = assertThrows(ObjectNotFoundException.class, () ->
                service.createBooking(resourceMock));
        assertNotNull(exception);
        assertEquals("The Property was not found in the Database", exception.getMessage());
    }

    @Test
    void createBookingTest_HasOverlap_MustThrowBusinessException() throws BusinessException {
        var resourceMock = ObjectMockUtils.getBookingRequestResourceMockHappyPath();
        when(propertyService.findByUUID("a50df57f-8554-4268-97c4-a0777f77317a"))
                .thenReturn(Optional.of(ObjectMockUtils.getBookingPropertyResourceMockHappyPath()));
        doThrow(new BusinessException("Error on saving Booking. The dates selected overlaps another existing booking"))
                .when(helper)
                .validateOverlappedDates(0L, UUID.fromString("a50df57f-8554-4268-97c4-a0777f77317a"),
                        resourceMock.getBeginAt(), resourceMock.getEndAt());

        var exception = assertThrows(BusinessException.class, () ->
                service.createBooking(resourceMock));
        assertNotNull(exception);
        assertEquals("Error on saving Booking. The dates selected overlaps another existing booking",
                exception.getMessage());
    }

    @Test
    void createBookingTest_HappyPath() throws BusinessException {
        when(propertyService.findByUUID("a50df57f-8554-4268-97c4-a0777f77317a"))
                .thenReturn(Optional.of(ObjectMockUtils.getBookingPropertyResourceMockHappyPath()));
        when(mapper.map(ObjectMockUtils.getBookingRequestResourceMockHappyPath(), Booking.class))
                .thenReturn(ObjectMockUtils.getBookingMockHappyPath());
        when(mapper.map(ObjectMockUtils.getBookingPropertyResourceMockHappyPath(), Property.class))
                .thenReturn(ObjectMockUtils.getBookingPropertyMockHappyPath());
        var beforeSave = ObjectMockUtils.getBookingMockHappyPath();
        beforeSave.setStatus(BookingStatus.BOOKED);
        when(repository.save(beforeSave))
                .thenReturn(ObjectMockUtils.getSavedBookingMockHappyPath());
        when(mapper.map(ObjectMockUtils.getSavedBookingMockHappyPath(), BookingResponseResource.class))
                .thenReturn(ObjectMockUtils.getBookingResponseResourceMockHappyPath());

        var result = service.createBooking(ObjectMockUtils.getBookingRequestResourceMockHappyPath());
        assertNotNull(result);
        assertEquals(ObjectMockUtils.getSavedBookingMockHappyPath().getUuid().toString(), result.getUuid());
        assertEquals(ObjectMockUtils.getBookingPropertyResourceMockHappyPath().getUuid(), result.getProperty().getUuid());
        assertEquals(ObjectMockUtils.getBookingPropertyResourceMockHappyPath().getName(), result.getProperty().getName());
        assertEquals(ObjectMockUtils.getBookingPropertyResourceMockHappyPath().getDescription(), result.getProperty().getDescription());
        assertEquals(BookingStatus.BOOKED.name(), result.getStatus());
        assertEquals(ObjectMockUtils.getBookingRequestResourceMockHappyPath().getBeginAt(), result.getBeginAt());
        assertEquals(ObjectMockUtils.getBookingRequestResourceMockHappyPath().getEndAt(), result.getEndAt());
    }

    @Test
    void updateBookingTest_BookingStatusBlocked_MustThrowBusinessException() throws ParameterValidationException {
        var resource = ObjectMockUtils.getBookingRequestResourceMockHappyPath();
        var booking = ObjectMockUtils.getSavedBookingMockHappyPath();
        booking.setStatus(BookingStatus.BLOCKED);
        when(helper.findBookingOrElseThrow(anyString())).thenReturn(booking);
        when(helper.isNotBooking(BookingStatus.BLOCKED)).thenReturn(true);

        var except = assertThrows(BusinessException.class,
                () -> service.updateBooking("55a6b2ea-7d44-40d3-8eb1-8967110d3df8", resource));

        assertNotNull(except);
        assertEquals("You can update bookings only with BOOKED status", except.getMessage());
    }

    @Test
    void updateBookingTest_BookingStatusCanceled_MustThrowBusinessException() throws ParameterValidationException {
        var resource = ObjectMockUtils.getBookingRequestResourceMockHappyPath();
        var booking = ObjectMockUtils.getSavedBookingMockHappyPath();
        booking.setStatus(BookingStatus.CANCELED);
        when(helper.findBookingOrElseThrow(anyString())).thenReturn(booking);
        when(helper.isNotBooking(BookingStatus.CANCELED)).thenReturn(false);

        var except = assertThrows(BusinessException.class,
                () -> service.updateBooking("55a6b2ea-7d44-40d3-8eb1-8967110d3df8", resource));

        assertNotNull(except);
        assertEquals("You can update bookings only with BOOKED status", except.getMessage());
    }

    @Test
    void updateBookingTest_HasOverlap_MustThrowBusinessException() throws BusinessException, ParameterValidationException {
        var resourceMock = ObjectMockUtils.getBookingRequestResourceMockHappyPath();
        when(helper.findBookingOrElseThrow("55a6b2ea-7d44-40d3-8eb1-8967110d3df8"))
                .thenReturn(ObjectMockUtils.getSavedBookingMockHappyPath());
        doThrow(new BusinessException("Error on saving Booking. The dates selected overlaps another existing booking"))
                .when(helper)
                .validateOverlappedDates(1L, UUID.fromString("a50df57f-8554-4268-97c4-a0777f77317a"),
                        resourceMock.getBeginAt(), resourceMock.getEndAt());

        var exception = assertThrows(BusinessException.class, () ->
                service.updateBooking("55a6b2ea-7d44-40d3-8eb1-8967110d3df8", resourceMock));
        assertNotNull(exception);
        assertEquals("Error on saving Booking. The dates selected overlaps another existing booking",
                exception.getMessage());
    }

    @Test
    void updateBookingTest_HapyPath() throws BusinessException, ParameterValidationException {
        var updatedGuest = new GuestResource();
        updatedGuest.setName("Updated Guest");
        updatedGuest.setEmail("updated@guest.com");
        updatedGuest.setAge(25);

        var resource = ObjectMockUtils.getBookingRequestResourceMockHappyPath();
        resource.setBeginAt(LocalDate.of(2024, 1, 10));
        resource.setEndAt(LocalDate.of(2024, 1, 20));
        resource.setGuests(List.of(updatedGuest));

        var entity = ObjectMockUtils.getSavedBookingMockHappyPath();
        when(helper.findBookingOrElseThrow("55a6b2ea-7d44-40d3-8eb1-8967110d3df8"))
                .thenReturn(entity);

        var expectedUpdatedGuest = new Guest();
        expectedUpdatedGuest.setName("Updated Guest");
        expectedUpdatedGuest.setEmail("updated@guest.com");
        expectedUpdatedGuest.setAge(25);

        var expectedEntity = ObjectMockUtils.getSavedBookingMockHappyPath();
        expectedEntity.setBeginAt(resource.getBeginAt());
        expectedEntity.setEndAt(resource.getEndAt());
        expectedEntity.setGuests(List.of(expectedUpdatedGuest));
        when(helper.mergeEntityToUpdate(resource, entity)).thenReturn(expectedEntity);
        when(repository.save(expectedEntity)).thenReturn(expectedEntity);

        var expectedResource = ObjectMockUtils.getBookingResponseResourceMockHappyPath();
        expectedResource.setBeginAt(expectedEntity.getBeginAt());
        expectedResource.setEndAt(expectedEntity.getEndAt());
        expectedResource.setGuests(List.of(updatedGuest));
        when(mapper.map(expectedEntity, BookingResponseResource.class)).thenReturn(expectedResource);

        var updatedBooking = service.updateBooking("55a6b2ea-7d44-40d3-8eb1-8967110d3df8", resource);
        assertNotNull(updatedBooking);
        assertEquals(entity.getUuid().toString(), updatedBooking.getUuid());
        assertNotEquals(entity.getBeginAt(), updatedBooking.getBeginAt());
        assertNotEquals(entity.getEndAt(), updatedBooking.getEndAt());
        assertEquals(entity.getProperty().getUuid(), updatedBooking.getProperty().getUuid());
        assertEquals(entity.getProperty().getName(), updatedBooking.getProperty().getName());
        assertEquals(2, entity.getGuests().size());
        assertEquals(1, updatedBooking.getGuests().size());

        assertEquals(expectedEntity.getUuid().toString(), updatedBooking.getUuid());
        assertEquals(expectedEntity.getBeginAt(), updatedBooking.getBeginAt());
        assertEquals(expectedEntity.getEndAt(), updatedBooking.getEndAt());
        assertEquals(expectedEntity.getProperty().getUuid(), updatedBooking.getProperty().getUuid());
        assertEquals(expectedEntity.getProperty().getName(), updatedBooking.getProperty().getName());
        assertEquals(expectedEntity.getGuests().size(), updatedBooking.getGuests().size());
        assertEquals(expectedEntity.getGuests().get(0).getName(), updatedBooking.getGuests().get(0).getName());
        assertEquals(expectedEntity.getGuests().get(0).getEmail(), updatedBooking.getGuests().get(0).getEmail());
        assertEquals(expectedEntity.getGuests().get(0).getAge(), updatedBooking.getGuests().get(0).getAge());
        assertNull(updatedBooking.getGuests().get(0).getDocumentType());
        assertNull(updatedBooking.getGuests().get(0).getDocumentNumber());
    }

    @Test
    void cancelBookingTest_BlockedStatus_MustThrowBusinessException() throws ParameterValidationException {
        var booking = ObjectMockUtils.getSavedBookingMockHappyPath();
        booking.setStatus(BookingStatus.BLOCKED);
        when(helper.findBookingOrElseThrow("55a6b2ea-7d44-40d3-8eb1-8967110d3df8"))
                .thenReturn(booking);

        var except = assertThrows(BusinessException.class,
                () -> service.cancelBooking("55a6b2ea-7d44-40d3-8eb1-8967110d3df8"));

        assertNotNull(except);
        assertEquals("You can cancel bookings only with BOOKED status", except.getMessage());

    }

    @Test
    void cancelBookingTest_CanceledStatus_MustThrowBusinessException() throws ParameterValidationException {
        var booking = ObjectMockUtils.getSavedBookingMockHappyPath();
        booking.setStatus(BookingStatus.CANCELED);
        when(helper.findBookingOrElseThrow("55a6b2ea-7d44-40d3-8eb1-8967110d3df8"))
                .thenReturn(booking);

        var except = assertThrows(BusinessException.class,
                () -> service.cancelBooking("55a6b2ea-7d44-40d3-8eb1-8967110d3df8"));

        assertNotNull(except);
        assertEquals("You can cancel bookings only with BOOKED status", except.getMessage());

    }

    @Test
    void rebookBookingTest_BlockedStatus_MustThrowBusinessException() throws ParameterValidationException {
        var booking = ObjectMockUtils.getSavedBookingMockHappyPath();
        booking.setStatus(BookingStatus.BLOCKED);
        when(helper.findBookingOrElseThrow("55a6b2ea-7d44-40d3-8eb1-8967110d3df8"))
                .thenReturn(booking);

        var except = assertThrows(BusinessException.class,
                () -> service.rebookBooking("55a6b2ea-7d44-40d3-8eb1-8967110d3df8"));

        assertNotNull(except);
        assertEquals("You can rebook bookings with CANCELED status", except.getMessage());

    }

    @Test
    void rebookBookingTest_BookedStatus_MustThrowBusinessException() throws ParameterValidationException {
        var booking = ObjectMockUtils.getSavedBookingMockHappyPath();
        when(helper.findBookingOrElseThrow("55a6b2ea-7d44-40d3-8eb1-8967110d3df8"))
                .thenReturn(booking);

        var except = assertThrows(BusinessException.class,
                () -> service.rebookBooking("55a6b2ea-7d44-40d3-8eb1-8967110d3df8"));

        assertNotNull(except);
        assertEquals("You can rebook bookings with CANCELED status", except.getMessage());

    }

}