package com.patient.registry.repository;

import com.patient.registry.model.Patient;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface PatientRepository extends JpaRepository<Patient, Long> {
    // For search endpoint (min 3 chars)
    List<Patient> findByFamilyNameContainingIgnoreCase(String familyName);
}