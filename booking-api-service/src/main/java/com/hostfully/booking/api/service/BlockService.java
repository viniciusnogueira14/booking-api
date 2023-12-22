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
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
@RequiredArgsConstructor
public class BlockService {

    private final BookingHelper helper;
    private final ModelMapper mapper;
    private final BookingRepository repository;
    private final PropertyService propertyService;

    public BookingResponseResource createBlock(BookingRequestResource resource) throws BusinessException {
        var property = propertyService.findByUUID(resource.getPropertyId())
                .orElseThrow(() -> new ObjectNotFoundException(Property.class));

        helper.validateOverlappedDates(0L, property.getUuid(), resource.getBeginAt(), resource.getEndAt());

        var entity = mapper.map(resource, Booking.class);
        entity.setProperty(mapper.map(property, Property.class));
        entity.setStatus(BookingStatus.BLOCKED);
        entity.setGuests(new ArrayList<>());

        var persisted = repository.save(entity);
        return mapper.map(persisted, BookingResponseResource.class);
    }

    public BookingResponseResource updateBlock(String uuid, BookingRequestResource requestResource)
            throws ParameterValidationException, BusinessException {

        var booking = helper.findBookingOrElseThrow(uuid);
        if (helper.isBlock(booking.getStatus())) {
            throw new BusinessException("You can update bookings only with BLOCKED status");
        }

        helper.validateOverlappedDates(booking.getId(), booking.getProperty().getUuid(), booking.getBeginAt(), booking.getEndAt());

        var mergedBooking = helper.mergeEntityToUpdate(requestResource, booking);
        var updated = repository.save(mergedBooking);
        return mapper.map(updated, BookingResponseResource.class);
    }

    public void deleteBlock(String uuid) throws ParameterValidationException, BusinessException {
        var booking = helper.findBookingOrElseThrow(uuid);
        if (helper.isBlock(booking.getStatus())) {
            throw new BusinessException("You can delete bookings only with BLOCKED status");
        }

        repository.delete(booking);
    }

    public BookingResponseResource findByUUID(String uuid) throws ParameterValidationException {
        var block = helper.findBookingOrElseThrow(uuid);
        if (helper.isBlock(block.getStatus())) {
            return mapper.map(block, BookingResponseResource.class);
        }

        throw new ObjectNotFoundException(Booking.class);
    }
}
