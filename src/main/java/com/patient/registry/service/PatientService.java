package com.patient.registry.service;

import com.patient.registry.dto.model.Patient;
import java.util.List;
import java.util.Optional;

public interface PatientService {
    Patient savePatient(Patient patient);
    Optional<Patient> findById(Long id);
    Optional<Patient> findByFhirId(String fhirId);
    List<Patient> findAll();
    List<Patient> searchByFamilyName(String familyName);
}
