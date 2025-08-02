package com.patient.registry.repository;

import com.patient.registry.model.Organization;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrganizationRepository extends JpaRepository<Organization, Long> {
    Organization findByFhirId(String fhirId);
}