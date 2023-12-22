package com.hostfully.booking.api.resource;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.util.UUID;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class PropertyResource {

    @JsonIgnore
    private Long id;

    private UUID uuid;

    @NotBlank(message = "The name of the property must not be NULL or EMPTY")
    @Size(max = 100, message = "The name of the property must be lower than 100 characters")
    private String name;

    @Size(max = 1024, message = "The description of the property must be lower than 1024 characters")
    private String description;
}
