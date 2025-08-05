package com.patient.registry.repository;

import com.patient.registry.dto.model.Organization;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface OrganizationRepository extends JpaRepository<Organization, Long> {
    Optional<Organization> findByFhirId(String fhirId);
}