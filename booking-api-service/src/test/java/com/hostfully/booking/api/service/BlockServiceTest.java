package com.hostfully.booking.api.service;

import com.hostfully.booking.api.entity.Booking;
import com.hostfully.booking.api.entity.BookingStatus;
import com.hostfully.booking.api.entity.Property;
import com.hostfully.booking.api.exception.BusinessException;
import com.hostfully.booking.api.exception.ObjectNotFoundException;
import com.hostfully.booking.api.exception.ParameterValidationException;
import com.hostfully.booking.api.helper.BookingHelper;
import com.hostfully.booking.api.repository.BookingRepository;
import com.hostfully.booking.api.resource.BookingRequestResource;
import com.hostfully.booking.api.resource.BookingResponseResource;
import com.hostfully.booking.api.utils.ObjectMockUtils;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;

import java.time.LocalDate;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class BlockServiceTest {

    @Mock
    private BookingHelper helper;
    @Mock
    private ModelMapper mapper;
    @Mock
    private BookingRepository repository;
    @Mock
    private PropertyService propertyService;

    @InjectMocks
    private BlockService service;


    @Test
    void createBlockTest_PropertyIdNotFound_MustThrowObjectNotFoundException() {
        var resourceMock = new BookingRequestResource();
        resourceMock.setPropertyId("c0f926fa-5d63-4d33-8476-74ce938d6bff");
        when(propertyService.findByUUID(anyString())).thenReturn(Optional.empty());
        var exception = assertThrows(ObjectNotFoundException.class, () ->
                service.createBlock(resourceMock));
        assertNotNull(exception);
        assertEquals("The Property was not found in the Database", exception.getMessage());
    }

    @Test
    void createBlockTest_HasOverlap_MustThrowBusinessException() throws BusinessException {
        var resourceMock = ObjectMockUtils.getBlockRequestResourceMockHappyPath();
        when(propertyService.findByUUID("c0f926fa-5d63-4d33-8476-74ce938d6bff"))
                .thenReturn(Optional.of(ObjectMockUtils.getBlockPropertyResourceMockHappyPath()));
        doThrow(new BusinessException("Error on saving Booking. The dates selected overlaps another existing booking"))
                .when(helper)
                .validateOverlappedDates(0L, UUID.fromString("c0f926fa-5d63-4d33-8476-74ce938d6bff"),
                        resourceMock.getBeginAt(), resourceMock.getEndAt());

        var exception = assertThrows(BusinessException.class, () ->
                service.createBlock(resourceMock));
        assertNotNull(exception);
        assertEquals("Error on saving Booking. The dates selected overlaps another existing booking",
                exception.getMessage());
    }

    @Test
    void createBlockTest_HappyPath() throws BusinessException {
        when(propertyService.findByUUID("c0f926fa-5d63-4d33-8476-74ce938d6bff"))
                .thenReturn(Optional.of(ObjectMockUtils.getBlockPropertyResourceMockHappyPath()));
        when(mapper.map(ObjectMockUtils.getBlockRequestResourceMockHappyPath(), Booking.class))
                .thenReturn(ObjectMockUtils.getBlockMockHappyPath());
        when(mapper.map(ObjectMockUtils.getBlockPropertyResourceMockHappyPath(), Property.class))
                .thenReturn(ObjectMockUtils.getBlockPropertyMockHappyPath());
        var beforeSave = ObjectMockUtils.getBlockMockHappyPath();
        beforeSave.setStatus(BookingStatus.BLOCKED);
        when(repository.save(beforeSave))
                .thenReturn(ObjectMockUtils.getSavedBlockMockHappyPath());
        when(mapper.map(ObjectMockUtils.getSavedBlockMockHappyPath(), BookingResponseResource.class))
                .thenReturn(ObjectMockUtils.getBlockResponseResourceMockHappyPath());

        var result = service.createBlock(ObjectMockUtils.getBlockRequestResourceMockHappyPath());
        assertNotNull(result);
        assertEquals(ObjectMockUtils.getSavedBlockMockHappyPath().getUuid().toString(), result.getUuid());
        assertEquals(ObjectMockUtils.getBlockPropertyResourceMockHappyPath().getUuid(), result.getProperty().getUuid());
        assertEquals(ObjectMockUtils.getBlockPropertyResourceMockHappyPath().getName(), result.getProperty().getName());
        assertEquals(ObjectMockUtils.getBlockPropertyResourceMockHappyPath().getDescription(), result.getProperty().getDescription());
        assertEquals(BookingStatus.BLOCKED.name(), result.getStatus());
        assertEquals(ObjectMockUtils.getBlockRequestResourceMockHappyPath().getBeginAt(), result.getBeginAt());
        assertEquals(ObjectMockUtils.getBlockRequestResourceMockHappyPath().getEndAt(), result.getEndAt());
    }

    @Test
    void updateBlockTest_BookingStatusBooked_MustThrowBusinessException() throws ParameterValidationException {
        var resource = ObjectMockUtils.getBlockRequestResourceMockHappyPath();
        var booking = ObjectMockUtils.getSavedBlockMockHappyPath();
        booking.setStatus(BookingStatus.BOOKED);
        when(helper.findBookingOrElseThrow(anyString())).thenReturn(booking);
        when(helper.isNotBlock(BookingStatus.BOOKED)).thenReturn(true);

        var except = assertThrows(BusinessException.class,
                () -> service.updateBlock("77497a01-57e0-41ca-b880-02963f9cc6a0", resource));

        assertNotNull(except);
        assertEquals("You can update bookings only with BLOCKED status", except.getMessage());
    }

    @Test
    void updateBlockTest_BookingStatusCanceled_MustThrowBusinessException() throws ParameterValidationException {
        var resource = ObjectMockUtils.getBlockRequestResourceMockHappyPath();
        var booking = ObjectMockUtils.getSavedBlockMockHappyPath();
        booking.setStatus(BookingStatus.CANCELED);
        when(helper.findBookingOrElseThrow(anyString())).thenReturn(booking);
        when(helper.isNotBlock(BookingStatus.CANCELED)).thenReturn(true);

        var except = assertThrows(BusinessException.class,
                () -> service.updateBlock("77497a01-57e0-41ca-b880-02963f9cc6a0", resource));

        assertNotNull(except);
        assertEquals("You can update bookings only with BLOCKED status", except.getMessage());
    }

    @Test
    void updateBlockTest_HasOverlap_MustThrowBusinessException() throws BusinessException, ParameterValidationException {
        var resourceMock = ObjectMockUtils.getBlockRequestResourceMockHappyPath();
        when(helper.findBookingOrElseThrow("77497a01-57e0-41ca-b880-02963f9cc6a0"))
                .thenReturn(ObjectMockUtils.getSavedBlockMockHappyPath());
        doThrow(new BusinessException("Error on saving Booking. The dates selected overlaps another existing booking"))
                .when(helper)
                .validateOverlappedDates(9L, UUID.fromString("c0f926fa-5d63-4d33-8476-74ce938d6bff"),
                        resourceMock.getBeginAt(), resourceMock.getEndAt());

        var exception = assertThrows(BusinessException.class, () ->
                service.updateBlock("77497a01-57e0-41ca-b880-02963f9cc6a0", resourceMock));
        assertNotNull(exception);
        assertEquals("Error on saving Booking. The dates selected overlaps another existing booking",
                exception.getMessage());
    }

    @Test
    void updateBlockTest_HapyPath() throws BusinessException, ParameterValidationException {
        var resource = ObjectMockUtils.getBookingRequestResourceMockHappyPath();
        resource.setBeginAt(LocalDate.of(2024, 1, 10));
        resource.setEndAt(LocalDate.of(2024, 1, 20));

        var entity = ObjectMockUtils.getSavedBlockMockHappyPath();
        when(helper.findBookingOrElseThrow("77497a01-57e0-41ca-b880-02963f9cc6a0"))
                .thenReturn(entity);

        var expectedEntity = ObjectMockUtils.getSavedBlockMockHappyPath();
        expectedEntity.setBeginAt(resource.getBeginAt());
        expectedEntity.setEndAt(resource.getEndAt());
        when(helper.mergeEntityToUpdate(resource, entity)).thenReturn(expectedEntity);
        when(repository.save(expectedEntity)).thenReturn(expectedEntity);

        var expectedResource = ObjectMockUtils.getBlockResponseResourceMockHappyPath();
        expectedResource.setBeginAt(expectedEntity.getBeginAt());
        expectedResource.setEndAt(expectedEntity.getEndAt());
        when(mapper.map(expectedEntity, BookingResponseResource.class)).thenReturn(expectedResource);

        var updatedBooking = service.updateBlock("77497a01-57e0-41ca-b880-02963f9cc6a0", resource);
        assertNotNull(updatedBooking);
        assertEquals(entity.getUuid().toString(), updatedBooking.getUuid());
        assertNotEquals(entity.getBeginAt(), updatedBooking.getBeginAt());
        assertNotEquals(entity.getEndAt(), updatedBooking.getEndAt());
        assertEquals(entity.getProperty().getUuid(), updatedBooking.getProperty().getUuid());
        assertEquals(entity.getProperty().getName(), updatedBooking.getProperty().getName());

        assertEquals(expectedEntity.getUuid().toString(), updatedBooking.getUuid());
        assertEquals(expectedEntity.getBeginAt(), updatedBooking.getBeginAt());
        assertEquals(expectedEntity.getEndAt(), updatedBooking.getEndAt());
        assertEquals(expectedEntity.getProperty().getUuid(), updatedBooking.getProperty().getUuid());
        assertEquals(expectedEntity.getProperty().getName(), updatedBooking.getProperty().getName());
    }
}
