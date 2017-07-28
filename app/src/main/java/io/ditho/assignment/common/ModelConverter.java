package io.ditho.assignment.common;

import io.ditho.assignment.repository.entity.ContactEntity;

public class ModelConverter {

    public static ContactEntity from(io.ditho.assignment.rest.model.Contact contact) {
        ContactEntity result = new ContactEntity();

        result.setAccount(contact.getAccount());
        result.setBusinessPhone(contact.getBusinessPhone());
        result.setFirstName(contact.getFirstName());
        result.setMiddleName(contact.getMiddleName());
        result.setLastName(contact.getLastName());
        result.setFullName(contact.getFullName());
        result.setGender(contact.getGender());
        result.setId(contact.getID());
        result.setEmail(contact.getEmail());
        result.setMobile(contact.getMobile());
        result.setNotes(contact.getNotes());
        result.setPhone(contact.getPhone());
        result.setGender(contact.getGender());
        result.setBusinessEmail(contact.getBusinessEmail());
        result.setBusinessMobile(contact.getBusinessMobile());
        result.setJobTitleDescription(contact.getJobTitleDescription());
        result.setPictureThumbnailUrl(contact.getPictureThumbnailUrl());

        return result;
    }
}
