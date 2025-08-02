package com.patient.registry.model;

import jakarta.persistence.*;
import lombok.*;
import java.io.Serializable;
import java.time.LocalDate;

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

    // FHIR ID, for FHIR interoperability
    @Column(unique = true, nullable = false)
    private String fhirId;

    @Column(nullable = false)
    private String lastName;

    @Column(nullable = false)
    private String firstName;

    @Enumerated(EnumType.STRING)
    private Gender gender;

    private LocalDate birthDate;
    private String address;
    private String phone;

    // Organization as a foreign key (many-to-one)
    @ManyToOne
    @JoinColumn(name = "organization_id")
    private Organization managingOrganization;

    public enum Gender {
        MALE, FEMALE, OTHER, UNKNOWN
    }

}
