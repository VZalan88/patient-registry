package com.patient.registry.dto.model;

import jakarta.persistence.*;
import lombok.*;

import java.io.Serializable;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class Organization implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // For mapping to FHIR 'id'
    @Column(unique = true)
    private String fhirId;

    @Column(nullable = false)
    private String name;

    private String address;

    private String phone;

}
