package com.patient.registry.service.impl;

import com.patient.registry.dto.model.Patient;
import com.patient.registry.repository.PatientRepository;
import com.patient.registry.service.PatientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PatientServiceImpl implements PatientService {

    @Autowired
    private final PatientRepository patientRepository;

    public PatientServiceImpl(PatientRepository patientRepository) {
        this.patientRepository = patientRepository;
    }

    @Override
    public Patient savePatient(Patient patient) {
        return patientRepository.save(patient);
    }

    @Override
    public Optional<Patient> findById(Long id) {
        return patientRepository.findById(id);
    }

    @Override
    public Optional<Patient> findByFhirId(String fhirId) {
        return patientRepository.findByFhirId(fhirId);
    }

    @Override
    public List<Patient> findAll() {
        return patientRepository.findAll();
    }

    @Override
    public List<Patient> searchByFamilyName(String familyName) {
        return patientRepository.findByFamilyNameContainingIgnoreCase(familyName);
    }
}
