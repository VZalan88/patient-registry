package com.patient.registry.dto.model;

import jakarta.persistence.*;
import lombok.*;

import java.io.Serializable;
import java.util.Date;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class Patient implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // For mapping to FHIR 'id'
    @Column(unique = true)
    private String fhirId;

    @Column(nullable = false)
    private String familyName;

    @Column(nullable = false)
    private String givenName;

    private String middleName; // Optional

    @Enumerated(EnumType.STRING)
    private Gender gender;

    private Date birthDate;

    private String phone;

    private String address; // (or use individual address parts)

    @ManyToOne
    @JoinColumn(name = "organization_id")
    private Organization managingOrganization;

    public enum Gender {
        MALE, FEMALE, OTHER, UNKNOWN
    }

}
