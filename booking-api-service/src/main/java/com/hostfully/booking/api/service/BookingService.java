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
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BookingService {

    private final BookingHelper helper;
    private final ModelMapper mapper;
    private final BookingRepository repository;

    private final PropertyService propertyService;
    private final GuestService guestService;

    public BookingResponseResource createBooking(BookingRequestResource resource) throws BusinessException {
        var property = propertyService.findByUUID(resource.getPropertyId())
                .orElseThrow(() -> new ObjectNotFoundException(Property.class));

        helper.validateOverlappedDates(0L, property.getUuid(), resource.getBeginAt(), resource.getEndAt());

        var entity = mapper.map(resource, Booking.class);
        entity.setProperty(mapper.map(property, Property.class));
        entity.setStatus(BookingStatus.BOOKED);

        var persisted = repository.save(entity);
        return mapper.map(persisted, BookingResponseResource.class);
    }

    public BookingResponseResource updateBooking(String uuid, BookingRequestResource requestResource) throws ParameterValidationException, BusinessException {
        var booking = helper.findBookingOrElseThrow(uuid);

        if (helper.isBooking(booking.getStatus()) && !booking.getStatus().equals(BookingStatus.BOOKED)) {
            throw new BusinessException("You can update bookings only with BOOKED status");
        }

        helper.validateOverlappedDates(booking.getId(), booking.getProperty().getUuid(), requestResource.getBeginAt(), requestResource.getEndAt());
        removeDetachedGuests(booking);

        var mergedBooking = helper.mergeEntityToUpdate(requestResource, booking);
        var updated = repository.save(mergedBooking);
        return mapper.map(updated, BookingResponseResource.class);
    }

    public void cancelBooking(String bookingUuid) throws ParameterValidationException, BusinessException {
        var booking = helper.findBookingOrElseThrow(bookingUuid);
        if (helper.isBooking(booking.getStatus()) && !booking.getStatus().equals(BookingStatus.BOOKED)) {
            throw new BusinessException("You can cancel bookings only with BOOKED status");
        }

        booking.setStatus(BookingStatus.CANCELED);
        repository.save(booking);
    }

    public void rebookBooking(String uuid) throws ParameterValidationException, BusinessException {
        var booking = helper.findBookingOrElseThrow(uuid);
        if (helper.isBooking(booking.getStatus()) && !booking.getStatus().equals(BookingStatus.CANCELED)) {
            throw new BusinessException("You can rebook bookings with CANCELED status");
        }

        helper.validateOverlappedDates(booking.getId(), booking.getProperty().getUuid(), booking.getBeginAt(), booking.getEndAt());

        booking.setStatus(BookingStatus.BOOKED);
        repository.save(booking);
    }

    public void deleteBooking(String uuid) throws ParameterValidationException, BusinessException {
        var booking = helper.findBookingOrElseThrow(uuid);
        if (helper.isBooking(booking.getStatus())) {
            throw new BusinessException("You can delete bookings only with BOOKED or CANCELED status");
        }

        removeDetachedGuests(booking);
        repository.delete(booking);
    }

    public BookingResponseResource findByUUID(String uuid) throws ParameterValidationException {
        return mapper.map(helper.findBookingOrElseThrow(uuid), BookingResponseResource.class);
    }

    private void removeDetachedGuests(Booking booking) {
        var guestIdsToRemove = booking.getGuests().stream()
                .map(Guest::getId)
                .toList();
        guestService.deleteGuestsById(guestIdsToRemove);
    }
}
