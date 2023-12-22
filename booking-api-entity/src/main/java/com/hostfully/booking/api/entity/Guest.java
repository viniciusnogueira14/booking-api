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
@Table(name = "GUEST")
public class Guest implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_guest")
    @SequenceGenerator(name = "seq_guest", sequenceName = "SEQ_GUEST", allocationSize = 1)
    @Column(name = "ID_GUEST", precision = 18, nullable = false, unique = true)
    private Long id;

    @Column(name = "UUID_GUEST", length = 36, nullable = false, unique = true)
    private UUID uuid;

    @Column(name = "NAME_GUEST", length = 100, nullable = false)
    private String name;

    @Column(name = "AGE_GUEST", nullable = false)
    private Integer age;

    @Column(name = "EMAIL_GUEST", length = 100)
    private String email;

    @Column(name = "DOC_TYPE_GUEST", length = 20)
    private DocumentType documentType;

    @Column(name = "DOC_NMB_GUEST", length = 100)
    private String documentNumber;

    @PrePersist
    private void generateUUID() {
        this.setUuid(UUID.randomUUID());
    }
}
