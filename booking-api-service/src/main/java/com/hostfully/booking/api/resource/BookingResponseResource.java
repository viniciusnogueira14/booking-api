package com.hostfully.booking.api.resource;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.time.LocalDate;
import java.util.List;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class BookingResponseResource {

    @NotBlank(message = "The UUID of the Booking must not be NULL or EMPTY")
    @Size(min = 36, max = 36, message = "The UUID field must have 36 characters")
    private String uuid;

    @NotNull(message = "The Property must not be NULL")
    private PropertyResource property;

    private String status;

    @NotNull(message = "The booking must have an initial date")
    @FutureOrPresent(message = "The initial date must not be a past date")
    private LocalDate beginAt;

    @NotNull(message = "The booking must have an final date")
    @FutureOrPresent(message = "The final date must not be a past date")
    private LocalDate endAt;

    private List<GuestResource> guests;
}
