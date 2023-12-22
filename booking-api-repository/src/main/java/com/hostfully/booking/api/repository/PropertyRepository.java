package com.hostfully.booking.api.repository;

import com.hostfully.booking.api.entity.Property;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface PropertyRepository extends JpaRepository<Property, Long> {

    Optional<Property> findByUuid(UUID uuid);
}
