package com.hostfully.booking.api.controller;

import com.hostfully.booking.api.exception.BusinessException;
import com.hostfully.booking.api.exception.ParameterValidationException;
import com.hostfully.booking.api.resource.BookingRequestResource;
import com.hostfully.booking.api.resource.BookingResponseResource;
import com.hostfully.booking.api.service.BlockService;
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

@Tag(name = "Blocks API", description = "API to manage the Blocks requests")
@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/api/v1/block")
public class BlockController {

    private final BlockService service;

    @Operation(summary = "Create a new Block in the database")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Block successfully created"),
            @ApiResponse(responseCode = "400", description = "Bad Request. Might have any problem with the input"),
            @ApiResponse(responseCode = "404", description = "Not Found. Any object that was not found"),
            @ApiResponse(responseCode = "409", description = "Business Exception. Any business rule that was not met")
    })
    @PostMapping
    public ResponseEntity<BookingResponseResource> createBlock(@Valid @RequestBody BookingRequestResource resource) throws BusinessException {
        var persisted = service.createBlock(resource);
        return ResponseEntity.created(URI.create(getURI() + persisted.getUuid())).body(persisted);
    }

    @Operation(summary = "Update the dates of an existing Block")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Block successfully updated"),
            @ApiResponse(responseCode = "400", description = "Bad Request. Might have any problem with the input"),
            @ApiResponse(responseCode = "404", description = "Not Found. Any object that was not found"),
            @ApiResponse(responseCode = "409", description = "Business Exception. Any business rule that was not met")
    })
    @PutMapping("/{uuid}")
    public ResponseEntity<BookingResponseResource> updateBlock(
            @PathVariable String uuid, @Valid @RequestBody BookingRequestResource requestResource)
            throws ParameterValidationException, BusinessException {
        return ResponseEntity.ok(service.updateBlock(uuid, requestResource));
    }

    @Operation(summary = "Delete permanently an existing Block from the Database")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Block successfully updated"),
            @ApiResponse(responseCode = "400", description = "Bad Request. Might have any problem with the input"),
            @ApiResponse(responseCode = "404", description = "Not Found. Any object that was not found"),
            @ApiResponse(responseCode = "409", description = "Business Exception. Any business rule that was not met")
    })
    @DeleteMapping("/{uuid}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteBlock(@PathVariable String uuid) throws ParameterValidationException, BusinessException {
        service.deleteBlock(uuid);
    }

    @Operation(summary = "Retrieve a Block using its UUID as parameter")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Block successfully found"),
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
