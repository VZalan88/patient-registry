package com.patient.registry.dto.mapper;

import com.patient.registry.dto.model.Organization;
import com.patient.registry.dto.model.Patient;
import org.hl7.fhir.r4.model.HumanName;
import org.hl7.fhir.r4.model.Enumerations.AdministrativeGender;
import org.hl7.fhir.r4.model.ContactPoint;
import org.hl7.fhir.r4.model.Address;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.SimpleDateFormat;

public class FhirPatientMapper {
    private static final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
    private static final Logger logger = LoggerFactory.getLogger(FhirPatientMapper.class);

    // ENTITY --> FHIR
    public static org.hl7.fhir.r4.model.Patient toFhirFromPatientEntity(Patient entity) {
        logger.debug("Mapping Patient entity to fhir.");
        org.hl7.fhir.r4.model.Patient fhir = new org.hl7.fhir.r4.model.Patient();
        fhir.setId(entity.getFhirId());

        // Name
        HumanName name = new HumanName()
                .setFamily(entity.getFamilyName())
                .addGiven(entity.getGivenName());
        if (entity.getMiddleName() != null) {
            name.addGiven(entity.getMiddleName());
        }
        fhir.addName(name);

        // Gender
        if (entity.getGender() != null) {
            fhir.setGender(toFhirGender(entity.getGender()));
        }


        // Birthdate
        if (entity.getBirthDate() != null) {
            logger.debug("Patient entity birthDate during mapping entity to fhir: " + sdf.format(entity.getBirthDate()));
            fhir.setBirthDate(entity.getBirthDate());
        } else {
            logger.debug("Patient entity birthDate is NULL during mapping entity to fhir!");
        }

        // Phone
        if (entity.getPhone() != null) {
            ContactPoint phone = new ContactPoint();
            phone.setSystem(ContactPoint.ContactPointSystem.PHONE);
            phone.setValue(entity.getPhone());
            fhir.addTelecom(phone);
        }

        // Address (simple: all in one line)
        if (entity.getAddress() != null) {
            Address address = new Address();
            address.setText(entity.getAddress());
            fhir.addAddress(address);
        }

        // ManagingOrganization
        if (entity.getManagingOrganization() != null && entity.getManagingOrganization().getFhirId() != null) {
            org.hl7.fhir.r4.model.Reference orgRef = new org.hl7.fhir.r4.model.Reference();
            orgRef.setReference("Organization/" + entity.getManagingOrganization().getFhirId());
            fhir.setManagingOrganization(orgRef);
        }

        return fhir;
    }

    // FHIR --> ENTITY
    public static Patient fromFhirToPatientEntity(org.hl7.fhir.r4.model.Patient fhir) {
        Patient entity = new Patient();

        entity.setFhirId(fhir.getIdElement().getIdPart());

        // Name
        if (!fhir.getName().isEmpty()) {
            HumanName name = fhir.getName().get(0);
            entity.setFamilyName(name.getFamily());
            if (!name.getGiven().isEmpty()) {
                entity.setGivenName(name.getGiven().get(0).getValue());
                if (name.getGiven().size() > 1) {
                    entity.setMiddleName(name.getGiven().get(1).getValue());
                }
            }
        }

        // Gender
        if (fhir.hasGender()) {
            entity.setGender(fromFhirGender(fhir.getGender()));
        }

        // Birthdate
        if (fhir.hasBirthDate()) {
            entity.setBirthDate(fhir.getBirthDate());
        }

        // Phone
        if (!fhir.getTelecom().isEmpty()) {
            // Get first phone (if there are multiple)
            for (ContactPoint telecom : fhir.getTelecom()) {
                if (telecom.getSystem() == ContactPoint.ContactPointSystem.PHONE) {
                    entity.setPhone(telecom.getValue());
                    break;
                }
            }
        }

        // Address
        if (!fhir.getAddress().isEmpty()) {
            Address fhirAddress = fhir.getAddress().get(0);
            entity.setAddress(fhirAddress.getText());
        }

        // ManagingOrganization
        if (fhir.hasManagingOrganization() && fhir.getManagingOrganization().hasReference()) {
            String orgRef = fhir.getManagingOrganization().getReference(); // e.g. "Organization/org123"
            if (orgRef != null && orgRef.startsWith("Organization/")) {
                String orgFhirId = orgRef.substring("Organization/".length());
                Organization org = new Organization();
                org.setFhirId(orgFhirId);
                entity.setManagingOrganization(org);
            }
        }

        return entity;
    }

    // Enum mapping helpers:
    private static AdministrativeGender toFhirGender(Patient.Gender gender) {
        if (gender == null) return null;
        switch (gender) {
            case MALE:    return AdministrativeGender.MALE;
            case FEMALE:  return AdministrativeGender.FEMALE;
            case OTHER:   return AdministrativeGender.OTHER;
            case UNKNOWN: return AdministrativeGender.UNKNOWN;
        }
        return null;
    }

    private static Patient.Gender fromFhirGender(AdministrativeGender fhirGender) {
        if (fhirGender == null) return null;
        switch (fhirGender) {
            case MALE:    return Patient.Gender.MALE;
            case FEMALE:  return Patient.Gender.FEMALE;
            case OTHER:   return Patient.Gender.OTHER;
            case UNKNOWN: return Patient.Gender.UNKNOWN;
        }
        return null;
    }
}