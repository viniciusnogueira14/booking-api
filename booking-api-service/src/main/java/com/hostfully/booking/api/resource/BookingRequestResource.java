package com.hostfully.booking.api.resource;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.time.LocalDate;
import java.util.List;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class BookingRequestResource {

    @NotNull(message = "The Property ID must not be NULL or EMPTY")
    @Size(min = 36, max = 36, message = "The Property ID must have 36 characters")
    private String propertyId;

    @NotNull(message = "The booking must have a beginAt date")
    @FutureOrPresent(message = "The beginAt date must not be a past date")
    private LocalDate beginAt;

    @NotNull(message = "The booking must have an endAt date")
    @FutureOrPresent(message = "The endAt date must not be a past date")
    private LocalDate endAt;

    @NotEmpty(message = "The Guest list must not be NULL or EMPTY")
    private List<GuestResource> guests;
}
