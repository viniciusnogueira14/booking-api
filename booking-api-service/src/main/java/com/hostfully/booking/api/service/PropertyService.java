package com.hostfully.booking.api.service;

import com.hostfully.booking.api.entity.Property;
import com.hostfully.booking.api.repository.PropertyRepository;
import com.hostfully.booking.api.resource.PropertyResource;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class PropertyService {

    private final ModelMapper modelMapper;
    private final PropertyRepository repository;

    public Optional<PropertyResource> findByUUID(String uuid) {
        var entity = repository.findByUuid(UUID.fromString(uuid)).orElseGet(Property::new);
        if (entity.isEmpty()) {
            return Optional.empty();
        }
        return Optional.of(modelMapper.map(entity, PropertyResource.class));
    }
}
