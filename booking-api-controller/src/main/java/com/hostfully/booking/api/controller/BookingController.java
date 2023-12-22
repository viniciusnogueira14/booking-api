package com.hostfully.booking.api.controller;

import com.hostfully.booking.api.exception.BusinessException;
import com.hostfully.booking.api.exception.ParameterValidationException;
import com.hostfully.booking.api.resource.BookingRequestResource;
import com.hostfully.booking.api.resource.BookingResponseResource;
import com.hostfully.booking.api.service.BookingService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.net.URI;

@Tag(name = "Bookings API", description = "API to manage the Bookings requests")
@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/api/v1/booking")
public class BookingController {

    private final BookingService service;

    @Operation(summary = "Create a new Booking in the database")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Booking successfully created"),
            @ApiResponse(responseCode = "400", description = "Bad Request. Might have any problem with the input"),
            @ApiResponse(responseCode = "404", description = "Not Found. Any object that was not found"),
            @ApiResponse(responseCode = "409", description = "Business Exception. Any business rule that was not met")
    })
    @PostMapping
    public ResponseEntity<BookingResponseResource> createBooking(
            @Valid @RequestBody BookingRequestResource requestResource) throws BusinessException {
        var persisted = service.createBooking(requestResource);
        return ResponseEntity.created(URI.create(getURI() + persisted.getUuid())).body(persisted);
    }

    @Operation(summary = "Update the dates or the Guest list of an existing Booking")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Booking successfully updated"),
            @ApiResponse(responseCode = "400", description = "Bad Request. Might have any problem with the input"),
            @ApiResponse(responseCode = "404", description = "Not Found. Any object that was not found"),
            @ApiResponse(responseCode = "409", description = "Business Exception. Any business rule that was not met")
    })
    @PutMapping("/{uuid}")
    public ResponseEntity<BookingResponseResource> updateBooking(
            @PathVariable String uuid, @Valid @RequestBody BookingRequestResource requestResource)
            throws ParameterValidationException, BusinessException {
        return ResponseEntity.ok(service.updateBooking(uuid, requestResource));
    }

    @Operation(summary = "Move the status of a Booking from BOOKED to CANCELED")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Booking successfully canceled"),
            @ApiResponse(responseCode = "400", description = "Bad Request. Might have any problem with the input"),
            @ApiResponse(responseCode = "404", description = "Not Found. Any object that was not found"),
            @ApiResponse(responseCode = "409", description = "Business Exception. Any business rule that was not met")
    })
    @PutMapping("/cancel/{uuid}")
    @ResponseStatus(HttpStatus.OK)
    public void cancelBooking(@PathVariable String uuid) throws BusinessException, ParameterValidationException {
        service.cancelBooking(uuid);
    }

    @Operation(summary = "Move the status of a Booking from CANCELED to BOOKED")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Booking successfully updated"),
            @ApiResponse(responseCode = "400", description = "Bad Request. Might have any problem with the input"),
            @ApiResponse(responseCode = "404", description = "Not Found. Any object that was not found"),
            @ApiResponse(responseCode = "409", description = "Business Exception. Any business rule that was not met")
    })
    @PutMapping("/rebook/{uuid}")
    @ResponseStatus(HttpStatus.OK)
    public void rebookBooking(@PathVariable String uuid) throws BusinessException, ParameterValidationException {
        service.rebookBooking(uuid);
    }

    @Operation(summary = "Delete permanently an existing Booking from the Database")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Booking successfully updated"),
            @ApiResponse(responseCode = "400", description = "Bad Request. Might have any problem with the input"),
            @ApiResponse(responseCode = "404", description = "Not Found. Any object that was not found"),
            @ApiResponse(responseCode = "409", description = "Business Exception. Any business rule that was not met")
    })
    @DeleteMapping("/{uuid}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteBooking(@PathVariable String uuid) throws ParameterValidationException, BusinessException {
        service.deleteBooking(uuid);
    }

    @Operation(summary = "Retrieve a Booking using its UUID as parameter")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Booking successfully found"),
            @ApiResponse(responseCode = "400", description = "Bad Request. Might have any problem with the input"),
            @ApiResponse(responseCode = "404", description = "Not Found. Any object that was not found")
    })
    @GetMapping("/{uuid}")
    public ResponseEntity<BookingResponseResource> getByUUID(@PathVariable String uuid) throws ParameterValidationException {
        return ResponseEntity.ok(service.findByUUID(uuid));
    }

    private String getURI() {
        ServletRequestAttributes servletRequestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();

        if (servletRequestAttributes != null) {
            HttpServletRequest request = servletRequestAttributes.getRequest();
            return request.getRequestURI() + "/";
        }

        return "/";
    }
}
