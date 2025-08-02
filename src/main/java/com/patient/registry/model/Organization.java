package com.patient.registry.model;

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
    @Column(unique = true, nullable = false)
    private String fhirId;

    @Column(nullable = false)
    private String name;

    // Optional: Address, Phone
    private String address;
    private String phone;

    // Constructors, getters, setters...

}
