package com.patient.registry.controller;

import ca.uhn.fhir.context.FhirContext;
import com.patient.registry.dto.mapper.FhirPatientMapper;
import com.patient.registry.dto.model.Patient;
import com.patient.registry.service.PatientService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;
import java.util.List;

@RestController
@RequestMapping("/fhir/r4/Patient")
public class PatientController {

    private final PatientService patientService;
    private final FhirContext fhirContext = FhirContext.forR4();

    public PatientController(PatientService patientService) {
        this.patientService = patientService;
    }

    // Create Patient (accept FHIR R4 JSON)
    @PostMapping(consumes = {"application/fhir+json", "application/json"}, produces = "application/fhir+json")
    public ResponseEntity<String> createPatient(@RequestBody String json) {
        org.hl7.fhir.r4.model.Patient fhirPatient =
                fhirContext.newJsonParser().parseResource(org.hl7.fhir.r4.model.Patient.class, json);

        Patient patient = FhirPatientMapper.fromFhirToPatientEntity(fhirPatient);
        Patient saved = patientService.savePatient(patient);
        org.hl7.fhir.r4.model.Patient response = FhirPatientMapper.toFhirFromPatientEntity(saved);

        String resultJson = fhirContext.newJsonParser().encodeResourceToString(response);
        return ResponseEntity.ok()
                .header("Content-Type", "application/fhir+json")
                .body(resultJson);
    }

    // Retrieve Patient by FHIR ID
    @GetMapping(value = "/{id}", produces = "application/fhir+json")
    public ResponseEntity<String> getPatientById(@PathVariable String id) {
        Optional<Patient> patientOpt = patientService.findByFhirId(id);
        if (patientOpt.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        org.hl7.fhir.r4.model.Patient fhir = FhirPatientMapper.toFhirFromPatientEntity(patientOpt.get());
        String json = fhirContext.newJsonParser().encodeResourceToString(fhir);
        return ResponseEntity.ok()
                .header("Content-Type", "application/fhir+json")
                .body(json);
    }

    // Retrieve all Patients
    @GetMapping(produces = "application/fhir+json")
    public ResponseEntity<String> getAllPatients() {
        List<org.hl7.fhir.r4.model.Patient> all = patientService.findAll().stream()
                .map(FhirPatientMapper::toFhirFromPatientEntity)
                .toList();

        // FHIR search results should be in a Bundle
        org.hl7.fhir.r4.model.Bundle bundle = new org.hl7.fhir.r4.model.Bundle();
        bundle.setType(org.hl7.fhir.r4.model.Bundle.BundleType.SEARCHSET);
        all.forEach(patient -> bundle.addEntry().setResource(patient));

        String json = fhirContext.newJsonParser().encodeResourceToString(bundle);
        return ResponseEntity.ok()
                .header("Content-Type", "application/fhir+json")
                .body(json);
    }

    // Search Patients by family name (minimum 3 characters)
    @GetMapping(value = "/search", produces = "application/fhir+json")
    public ResponseEntity<String> searchPatients(@RequestParam("family") String familyName) {
        if (familyName == null || familyName.length() < 3) {
            return ResponseEntity.badRequest().build();
        }
        List<org.hl7.fhir.r4.model.Patient> found = patientService.searchByFamilyName(familyName).stream()
                .map(FhirPatientMapper::toFhirFromPatientEntity)
                .toList();

        org.hl7.fhir.r4.model.Bundle bundle = new org.hl7.fhir.r4.model.Bundle();
        bundle.setType(org.hl7.fhir.r4.model.Bundle.BundleType.SEARCHSET);
        found.forEach(patient -> bundle.addEntry().setResource(patient));

        String json = fhirContext.newJsonParser().encodeResourceToString(bundle);
        return ResponseEntity.ok()
                .header("Content-Type", "application/fhir+json")
                .body(json);
    }
}
