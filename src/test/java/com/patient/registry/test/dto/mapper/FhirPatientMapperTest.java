package com.patient.registry.test.dto.mapper;

import com.patient.registry.dto.model.Organization;
import com.patient.registry.dto.model.Patient;
import com.patient.registry.dto.mapper.FhirPatientMapper;
import org.junit.jupiter.api.Test;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;

class FhirPatientMapperTest {

    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

    @Test
    void mappingFromPatientEntityToFhirAndBack() {
        // Create an Organization entity
        Organization organizationEntity = new Organization();
        organizationEntity.setFhirId("org123");
        organizationEntity.setName("TestOrg");
        organizationEntity.setAddress("AddressLine");
        organizationEntity.setPhone("555-1234");

        // Create a Patient entity
        Patient patientEntity = new Patient();
        patientEntity.setFhirId("pat456");
        patientEntity.setFamilyName("Doe");
        patientEntity.setGivenName("John");
        patientEntity.setMiddleName("Middle");
        patientEntity.setGender(Patient.Gender.MALE);
        patientEntity.setBirthDate(Date.from(LocalDate.of(1980, 2, 1).atStartOfDay(ZoneId.systemDefault()).toInstant()));
        patientEntity.setPhone("555-5678");
        patientEntity.setAddress("123 Main St");
        patientEntity.setManagingOrganization(organizationEntity);

        System.out.println("Test: Entity BirthDate: " + patientEntity.getBirthDate().toString());


        // Map to FHIR Patient
        org.hl7.fhir.r4.model.Patient fhir = FhirPatientMapper.toFhirFromPatientEntity(patientEntity);

        System.out.println(fhir.getBirthDate().toString());

        // Map back to entity
        Patient result = FhirPatientMapper.fromFhirToPatientEntity(fhir);

        // Assertions
        assertEquals(patientEntity.getFhirId(), fhir.getId());
        assertEquals(patientEntity.getFamilyName(), fhir.getNameFirstRep().getFamily());
        assertEquals(patientEntity.getGivenName(), fhir.getNameFirstRep().getGiven().getFirst().getValue());
        assertEquals(patientEntity.getGender().name().toLowerCase(), fhir.getGender().toCode());
        assertEquals(
                sdf.format(patientEntity.getBirthDate()),
                fhir.getBirthDate().toInstant().atZone(java.time.ZoneId.systemDefault()).toLocalDate().toString());
        assertEquals(patientEntity.getPhone(), fhir.getTelecomFirstRep().getValue());
        assertEquals(patientEntity.getAddress(), fhir.getAddressFirstRep().getText());
        assertEquals(patientEntity.getManagingOrganization().getFhirId(),
                fhir.getManagingOrganization().getReference().replace("Organization/", ""));
        // And back
        assertEquals(patientEntity.getFhirId(), result.getFhirId());
        assertEquals(patientEntity.getFamilyName(), result.getFamilyName());
        assertEquals(patientEntity.getGivenName(), result.getGivenName());
        assertEquals(patientEntity.getMiddleName(), result.getMiddleName());
        assertEquals(patientEntity.getGender(), result.getGender());
        assertEquals(patientEntity.getBirthDate(), result.getBirthDate());
        assertEquals(patientEntity.getPhone(), result.getPhone());
        assertEquals(patientEntity.getAddress(), result.getAddress());
        assertEquals(patientEntity.getManagingOrganization().getFhirId(), result.getManagingOrganization().getFhirId());
    }
}
