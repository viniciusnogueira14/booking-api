package com.hostfully.booking.api.helper;

import com.hostfully.booking.api.entity.Booking;
import com.hostfully.booking.api.entity.BookingStatus;
import com.hostfully.booking.api.entity.Guest;
import com.hostfully.booking.api.exception.BusinessException;
import com.hostfully.booking.api.exception.ObjectNotFoundException;
import com.hostfully.booking.api.exception.ParameterValidationException;
import com.hostfully.booking.api.repository.BookingRepository;
import com.hostfully.booking.api.resource.BookingRequestResource;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
public class BookingHelper {

    private final ModelMapper mapper;
    private final BookingRepository repository;

    public void validateOverlappedDates(Long bookingId, UUID propertyUuid, LocalDate beginDate, LocalDate endDate) throws BusinessException {
        if (repository.hasOverlappingDates(bookingId, propertyUuid,
                Arrays.asList(BookingStatus.BOOKED, BookingStatus.BLOCKED), beginDate, endDate)) {

            throw new BusinessException("Error on saving Booking. The dates selected overlaps another existing booking");
        }
    }

    public Booking findBookingOrElseThrow(String bookingUuid) throws ParameterValidationException {
        try {
            var uuid = Optional.ofNullable(bookingUuid)
                    .orElseThrow(() -> new ParameterValidationException("The Booking ID must not be NULL or EMPTY"));

            return repository.findByUuid(UUID.fromString(uuid))
                    .orElseThrow(() -> new ObjectNotFoundException(Booking.class));
        } catch (IllegalArgumentException ex) {
            throw new ParameterValidationException(ex.getMessage());
        }
    }

    public Booking mergeEntityToUpdate(BookingRequestResource resource, Booking entity) {
        entity.setBeginAt(resource.getBeginAt());
        entity.setEndAt(resource.getEndAt());
        if (isBooking(entity.getStatus())) {
            entity.setGuests(resource.getGuests().stream()
                    .map(guest -> mapper.map(guest, Guest.class))
                    .collect(Collectors.toCollection(ArrayList::new)));
        }

        return entity;
    }

    public boolean isBlock(BookingStatus bookingStatus) {
        return bookingStatus.equals(BookingStatus.BLOCKED);
    }

    public boolean isNotBlock(BookingStatus bookingStatus) {
        return !isBlock(bookingStatus);
    }

    public boolean isBooking(BookingStatus bookingStatus) {
        return bookingStatus.equals(BookingStatus.BOOKED) || bookingStatus.equals(BookingStatus.CANCELED);
    }

    public boolean isNotBooking(BookingStatus bookingStatus) {
        return !isBooking(bookingStatus);
    }
}
