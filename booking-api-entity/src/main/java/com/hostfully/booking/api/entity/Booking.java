package com.hostfully.booking.api.entity;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Data
@Entity
@Table(name = "BOOKING")
public class Booking implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_booking")
    @SequenceGenerator(name = "seq_booking", sequenceName = "SEQ_BOOKING", allocationSize = 1)
    @Column(name = "ID_BOOKING", precision = 18, nullable = false, unique = true)
    private Long id;

    @Column(name = "UUID_BOOKING", length = 36, nullable = false, unique = true)
    private UUID uuid;

    @ManyToOne
    @JoinColumn(name = "ID_PROPERTY", nullable = false)
    private Property property;

    @Column(name = "ST_BOOKING", length = 20, nullable = false)
    private BookingStatus status;

    @Column(name = "DT_START", length = 10, nullable = false)
    private LocalDate beginAt;

    @Column(name = "DT_END", length = 10, nullable = false)
    private LocalDate endAt;

    @ManyToMany(cascade = CascadeType.ALL)
    @JoinTable(
            name = "BOOKING_GUEST",
            joinColumns = @JoinColumn(name = "ID_BOOKING"),
            inverseJoinColumns = @JoinColumn(name = "ID_GUEST"))
    private List<Guest> guests;

    @PrePersist
    private void generateUUID() {
        this.setUuid(UUID.randomUUID());
    }
}
