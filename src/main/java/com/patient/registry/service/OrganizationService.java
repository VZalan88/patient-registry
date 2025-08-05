package com.patient.registry.service;

import com.patient.registry.dto.model.Organization;
import java.util.List;
import java.util.Optional;

public interface OrganizationService {
    Organization saveOrganization(Organization organization);
    Optional<Organization> findById(Long id);
    Optional<Organization> findByFhirId(String fhirId);
    List<Organization> findAll();
}
