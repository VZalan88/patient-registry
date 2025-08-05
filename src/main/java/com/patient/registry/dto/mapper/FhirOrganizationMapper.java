package com.patient.registry.dto.mapper;

import com.patient.registry.dto.model.Organization;
import org.hl7.fhir.r4.model.Identifier;
import org.hl7.fhir.r4.model.Address;
import org.hl7.fhir.r4.model.ContactPoint;

public class FhirOrganizationMapper {
    // ENTITY --> FHIR
    public static org.hl7.fhir.r4.model.Organization toFhirFromOrganizationEntity(Organization entity) {
        org.hl7.fhir.r4.model.Organization fhir = new org.hl7.fhir.r4.model.Organization();

        // Identifier
        if (entity.getFhirId() != null) {
            fhir.setId(entity.getFhirId());
            Identifier id = new Identifier();
            id.setValue(entity.getFhirId());
            fhir.addIdentifier(id);
        }

        // Name
        if (entity.getName() != null) {
            fhir.setName(entity.getName());
        }

        // Address
        if (entity.getAddress() != null) {
            Address address = new Address();
            address.setText(entity.getAddress());
            fhir.addAddress(address);
        }

        // Phone (as ContactPoint)
        if (entity.getPhone() != null) {
            ContactPoint phone = new ContactPoint();
            phone.setSystem(ContactPoint.ContactPointSystem.PHONE);
            phone.setValue(entity.getPhone());
            fhir.addTelecom(phone);
        }

        return fhir;
    }

    // FHIR --> ENTITY
    public static Organization fromFhirToOrganizationEntity(org.hl7.fhir.r4.model.Organization fhir) {
        Organization entity = new Organization();

        // Identifier
        if (fhir.hasId()) {
            entity.setFhirId(fhir.getIdElement().getIdPart());
        } else if (!fhir.getIdentifier().isEmpty()) {
            entity.setFhirId(fhir.getIdentifier().getFirst().getValue());
        }

        // Name
        entity.setName(fhir.getName());

        // Address
        if (!fhir.getAddress().isEmpty()) {
            entity.setAddress(fhir.getAddress().getFirst().getText());
        }

        // Phone
        if (!fhir.getTelecom().isEmpty()) {
            for (ContactPoint cp : fhir.getTelecom()) {
                if (cp.getSystem() == ContactPoint.ContactPointSystem.PHONE) {
                    entity.setPhone(cp.getValue());
                    break;
                }
            }
        }

        return entity;
    }
}
