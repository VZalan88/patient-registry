package com.patient.registry.test.service;

import com.patient.registry.dto.model.Patient;
import com.patient.registry.repository.PatientRepository;
import com.patient.registry.service.PatientService;
import com.patient.registry.service.impl.PatientServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class PatientServiceTest {

    private PatientRepository patientRepository;
    private PatientService patientService;

    @BeforeEach
    void setup() {
        patientRepository = mock(PatientRepository.class);
        patientService = new PatientServiceImpl(patientRepository);
    }

    @Test
    void testFindByFhirId_Found() {
        Patient patient = new Patient();
        patient.setFhirId("pat001");
        when(patientRepository.findByFhirId("pat001")).thenReturn(Optional.of(patient));

        Optional<Patient> result = patientService.findByFhirId("pat001");
        assertTrue(result.isPresent());
        assertEquals("pat001", result.get().getFhirId());
    }

    @Test
    void testFindByFhirId_NotFound() {
        when(patientRepository.findByFhirId("unknown")).thenReturn(Optional.empty());
        Optional<Patient> result = patientService.findByFhirId("unknown");
        assertTrue(result.isEmpty());
    }

    @Test
    void testSavePatient() {
        Patient patient = new Patient();
        when(patientRepository.save(patient)).thenReturn(patient);
        Patient saved = patientService.savePatient(patient);
        assertNotNull(saved);
        verify(patientRepository).save(patient);
    }

    @Test
    void testSearchByFamilyName() {
        Patient p1 = new Patient();
        p1.setFamilyName("Smith");
        Patient p2 = new Patient();
        p2.setFamilyName("Smithson");
        when(patientRepository.findByFamilyNameContainingIgnoreCase("smi"))
                .thenReturn(Arrays.asList(p1, p2));

        List<Patient> result = patientService.searchByFamilyName("smi");
        assertEquals(2, result.size());
        assertTrue(result.stream().allMatch(p -> p.getFamilyName().toLowerCase().contains("smi")));
    }
}
