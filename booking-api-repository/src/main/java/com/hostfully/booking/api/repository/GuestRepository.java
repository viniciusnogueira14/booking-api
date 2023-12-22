package com.hostfully.booking.api.repository;

import com.hostfully.booking.api.entity.Guest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface GuestRepository extends JpaRepository<Guest, Long> {

    List<Guest> findByUuidIn(List<UUID> uuids);
}
