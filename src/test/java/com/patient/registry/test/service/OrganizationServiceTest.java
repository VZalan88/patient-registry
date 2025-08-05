package com.patient.registry.test.service;

import com.patient.registry.dto.model.Organization;
import com.patient.registry.repository.OrganizationRepository;
import com.patient.registry.service.OrganizationService;
import com.patient.registry.service.impl.OrganizationServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class OrganizationServiceTest {

    private OrganizationRepository organizationRepository;
    private OrganizationService organizationService;

    @BeforeEach
    void setup() {
        organizationRepository = mock(OrganizationRepository.class);
        organizationService = new OrganizationServiceImpl(organizationRepository);
    }

    @Test
    void testFindByFhirId_Found() {
        Organization org = new Organization();
        org.setFhirId("org-1");
        when(organizationRepository.findByFhirId("org-1")).thenReturn(Optional.of(org));
        Optional<Organization> result = organizationService.findByFhirId("org-1");
        assertTrue(result.isPresent());
        assertEquals("org-1", result.get().getFhirId());
    }

    @Test
    void testFindByFhirId_NotFound() {
        when(organizationRepository.findByFhirId("doesnotexist")).thenReturn(Optional.empty());
        Optional<Organization> result = organizationService.findByFhirId("doesnotexist");
        assertTrue(result.isEmpty());
    }

    @Test
    void testSaveOrganization() {
        Organization org = new Organization();
        when(organizationRepository.save(org)).thenReturn(org);
        Organization saved = organizationService.saveOrganization(org);
        assertNotNull(saved);
        verify(organizationRepository).save(org);
    }
}
