package com.patient.registry.test.dto.mapper;


import com.patient.registry.dto.model.Organization;
import com.patient.registry.dto.mapper.FhirOrganizationMapper;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class FhirOrganizationMapperTest {

    @Test
    void entityToFhirOrganization_andBack() {
        Organization entity = new Organization();
        entity.setFhirId("org123");
        entity.setName("Test Organization");
        entity.setAddress("123 Org St");
        entity.setPhone("555-9999");

        // Map to FHIR Organization
        org.hl7.fhir.r4.model.Organization fhir = FhirOrganizationMapper.toFhirFromOrganizationEntity(entity);

        // Map back to entity
        Organization result = FhirOrganizationMapper.fromFhirToOrganizationEntity(fhir);

        assertEquals(entity.getFhirId(), fhir.getId());
        assertEquals(entity.getName(), fhir.getName());
        assertEquals(entity.getAddress(), fhir.getAddressFirstRep().getText());
        assertEquals(entity.getPhone(), fhir.getTelecomFirstRep().getValue());

        // And back
        assertEquals(entity.getFhirId(), result.getFhirId());
        assertEquals(entity.getName(), result.getName());
        assertEquals(entity.getAddress(), result.getAddress());
        assertEquals(entity.getPhone(), result.getPhone());
    }
}