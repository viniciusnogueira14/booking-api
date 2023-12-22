package com.hostfully.booking.api.resource;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class GuestResource {

    @NotBlank(message = "The name of the guest must not be NULL or EMPTY")
    @Size(max = 100, message = "The name of the guest must be lower than 100 characters")
    private String name;

    @NotNull(message = "The age of the guest must not be NULL")
    private Integer age;

    @Size(max = 100, message = "The email of the guest must be lower than 100 characters")
    private String email;

    @Size(max = 20, message = "The document type of the guest must be lower than 20 characters")
    private String documentType;

    @Size(max = 100, message = "The document number of the guest must be lower than 100 characters")
    private String documentNumber;
}
