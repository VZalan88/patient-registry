package com.patient.registry.repository;

import com.patient.registry.dto.model.Patient;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;

public interface PatientRepository extends JpaRepository<Patient, Long> {

    // Find by FHIR logical ID (not DB id)
    Optional<Patient> findByFhirId(String fhirId);

    // For search endpoint (minimum 3 chars is handled at service/controller level)
    List<Patient> findByFamilyNameContainingIgnoreCase(String familyName);
}