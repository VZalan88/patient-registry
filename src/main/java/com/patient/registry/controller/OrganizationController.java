package com.patient.registry.controller;

import ca.uhn.fhir.context.FhirContext;
import com.patient.registry.dto.mapper.FhirOrganizationMapper;
import com.patient.registry.dto.model.Organization;
import com.patient.registry.service.OrganizationService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;
import java.util.List;

@RestController
@RequestMapping("/fhir/r4/Organization")
public class OrganizationController {

    private final OrganizationService organizationService;
    private final FhirContext fhirContext = FhirContext.forR4(); // create once, reuse

    public OrganizationController(OrganizationService organizationService) {
        this.organizationService = organizationService;
    }

    // Create Organization
    @PostMapping(consumes = {"application/fhir+json", "application/json"}, produces = "application/fhir+json")
    public ResponseEntity<String> createOrganization(@RequestBody String json) {
        // Parse FHIR JSON using HAPI
        org.hl7.fhir.r4.model.Organization fhirOrganization =
                fhirContext.newJsonParser().parseResource(org.hl7.fhir.r4.model.Organization.class, json);

        Organization org = FhirOrganizationMapper.fromFhirToOrganizationEntity(fhirOrganization);
        Organization saved = organizationService.saveOrganization(org);
        org.hl7.fhir.r4.model.Organization response = FhirOrganizationMapper.toFhirFromOrganizationEntity(saved);

        String resultJson = fhirContext.newJsonParser().encodeResourceToString(response);
        return ResponseEntity.ok()
                .header("Content-Type", "application/fhir+json")
                .body(resultJson);
    }

    // Retrieve Organization by FHIR ID
    @GetMapping(value = "/{id}", produces = "application/fhir+json")
    public ResponseEntity<String> getOrganizationById(@PathVariable String id) {
        Optional<Organization> orgOpt = organizationService.findByFhirId(id);
        if (orgOpt.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        org.hl7.fhir.r4.model.Organization fhir = FhirOrganizationMapper.toFhirFromOrganizationEntity(orgOpt.get());
        String json = fhirContext.newJsonParser().encodeResourceToString(fhir);
        return ResponseEntity.ok()
                .header("Content-Type", "application/fhir+json")
                .body(json);
    }

    // Retrieve all Organizations
    @GetMapping(produces = "application/fhir+json")
    public ResponseEntity<String> getAllOrganizations() {
        List<org.hl7.fhir.r4.model.Organization> all = organizationService.findAll().stream()
                .map(FhirOrganizationMapper::toFhirFromOrganizationEntity)
                .toList();

        // In FHIR, a search result is typically a "Bundle"
        org.hl7.fhir.r4.model.Bundle bundle = new org.hl7.fhir.r4.model.Bundle();
        bundle.setType(org.hl7.fhir.r4.model.Bundle.BundleType.SEARCHSET);
        all.forEach(org -> bundle.addEntry().setResource(org));

        String json = fhirContext.newJsonParser().encodeResourceToString(bundle);
        return ResponseEntity.ok()
                .header("Content-Type", "application/fhir+json")
                .body(json);
    }
}
