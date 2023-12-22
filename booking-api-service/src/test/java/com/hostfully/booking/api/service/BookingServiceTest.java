package com.hostfully.booking.api.service;

import com.hostfully.booking.api.entity.Booking;
import com.hostfully.booking.api.entity.BookingStatus;
import com.hostfully.booking.api.entity.Property;
import com.hostfully.booking.api.exception.BusinessException;
import com.hostfully.booking.api.exception.ObjectNotFoundException;
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

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyString;
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
    void createBookingTest_HappyPath() throws BusinessException {
        when(propertyService.findByUUID("a50df57f-8554-4268-97c4-a0777f77317a"))
                .thenReturn(Optional.of(ObjectMockUtils.getPropertyResourceMockHappyPath()));
        when(mapper.map(ObjectMockUtils.getBookingRequestResourceMockHappyPath(), Booking.class))
                .thenReturn(ObjectMockUtils.getBookingMockHappyPath());
        when(mapper.map(ObjectMockUtils.getPropertyResourceMockHappyPath(), Property.class))
                .thenReturn(ObjectMockUtils.getPropertyMockHappyPath());
        var beforeSave = ObjectMockUtils.getBookingMockHappyPath();
        beforeSave.setStatus(BookingStatus.BOOKED);
        when(repository.save(beforeSave))
                .thenReturn(ObjectMockUtils.getSavedBookingMockHappyPath());
        when(mapper.map(ObjectMockUtils.getSavedBookingMockHappyPath(), BookingResponseResource.class))
                .thenReturn(ObjectMockUtils.getBookingResponseResourceMockHappyPath());

        var result = service.createBooking(ObjectMockUtils.getBookingRequestResourceMockHappyPath());
        assertNotNull(result);
        assertEquals(ObjectMockUtils.getSavedBookingMockHappyPath().getUuid().toString(), result.getUuid());
        assertEquals(ObjectMockUtils.getPropertyResourceMockHappyPath().getUuid(), result.getProperty().getUuid());
        assertEquals(ObjectMockUtils.getPropertyResourceMockHappyPath().getName(), result.getProperty().getName());
        assertEquals(ObjectMockUtils.getPropertyResourceMockHappyPath().getDescription(), result.getProperty().getDescription());
        assertEquals(BookingStatus.BOOKED.name(), result.getStatus());
        assertEquals(ObjectMockUtils.getBookingRequestResourceMockHappyPath().getBeginAt(), result.getBeginAt());
        assertEquals(ObjectMockUtils.getBookingRequestResourceMockHappyPath().getEndAt(), result.getEndAt());
    }
}