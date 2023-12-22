package com.hostfully.booking.api.repository;

import com.hostfully.booking.api.entity.Booking;
import com.hostfully.booking.api.entity.BookingStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface BookingRepository extends JpaRepository<Booking, Long> {

    Optional<Booking> findByUuid(UUID uuid);

    @Query(value = "SELECT CASE WHEN (COUNT(b) > 0) THEN true ELSE false END " +
                    "   FROM Booking b " +
                    "       WHERE (:bookingId != b.id) " +
                    "         AND b.property.uuid = :propertyUuid " +
                    "         AND b.status IN :bookingStatuses " +
                    "         AND (" +
                    "               (:beginDate <= b.beginAt AND :endDate >= b.beginAt) " +
                    "            OR (:endDate >= b.endAt AND :beginDate <= b.endAt) " +
                    "            OR (:beginDate >= b.beginAt AND :endDate <= b.endAt)" +
                    "         )")
    boolean hasOverlappingDates(@Param("bookingId") Long bookingId,
                                @Param("propertyUuid") UUID propertyUuid,
                                @Param("bookingStatuses") List<BookingStatus> bookingStatuses,
                                @Param("beginDate") LocalDate beginDate,
                                @Param("endDate") LocalDate endDate);
}
