package com.patient.registry.service.impl;

import com.patient.registry.dto.model.Organization;
import com.patient.registry.repository.OrganizationRepository;
import com.patient.registry.service.OrganizationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class OrganizationServiceImpl implements OrganizationService {

    @Autowired
    private final OrganizationRepository organizationRepository;

    public OrganizationServiceImpl(OrganizationRepository organizationRepository) {
        this.organizationRepository = organizationRepository;
    }

    @Override
    public Organization saveOrganization(Organization organization) {
        return organizationRepository.save(organization);
    }

    @Override
    public Optional<Organization> findById(Long id) {
        return organizationRepository.findById(id);
    }

    @Override
    public Optional<Organization> findByFhirId(String fhirId) {
        return organizationRepository.findByFhirId(fhirId);
    }

    @Override
    public List<Organization> findAll() {
        return organizationRepository.findAll();
    }
}
