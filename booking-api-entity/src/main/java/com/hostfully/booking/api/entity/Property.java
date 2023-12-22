package com.hostfully.booking.api.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import lombok.Data;

import java.io.Serializable;
import java.util.UUID;

@Data
@Entity
@Table(name = "PROPERTY")
public class Property implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_property")
    @SequenceGenerator(name = "seq_property", sequenceName = "SEQ_PROPERTY", allocationSize = 1)
    @Column(name = "ID_PROPERTY", precision = 18, nullable = false, unique = true)
    private Long id;

    @Column(name = "UUID_PROPERTY", length = 36, nullable = false, unique = true)
    private UUID uuid;

    @Column(name = "NAME_PROPERTY", length = 100, nullable = false)
    private String name;

    @Column(name = "DESC_PROPERTY", length = 1024)
    private String description;

    @PrePersist
    private void generateUUID() {
        this.setUuid(UUID.randomUUID());
    }

    public boolean isEmpty() {
        return id == null && uuid == null && name == null && description == null;
    }
}
