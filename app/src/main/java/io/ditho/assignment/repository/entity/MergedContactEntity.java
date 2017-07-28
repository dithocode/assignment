package io.ditho.assignment.repository.entity;

import android.arch.persistence.room.Entity;

@Entity
public class MergedContactEntity extends ContactEntity {

    public MergedContactEntity() { super(); }

    public MergedContactEntity(ContactEntity contactEntity) {
        setAccount(contactEntity.getAccount());
        setId(contactEntity.getId());
        setFirstName(contactEntity.getFirstName());
        setMiddleName(contactEntity.getMiddleName());
        setLastName(contactEntity.getLastName());
        setFullName(contactEntity.getFullName());
        setGender(contactEntity.getGender());
        setEmail(contactEntity.getEmail());
        setPhone(contactEntity.getPhone());
        setMobile(contactEntity.getMobile());
        setBusinessEmail(contactEntity.getBusinessEmail());
        setBusinessPhone(contactEntity.getBusinessPhone());
        setBusinessMobile(contactEntity.getBusinessMobile());
        setJobTitleDescription(contactEntity.getJobTitleDescription());
        setPictureThumbnailUrl(contactEntity.getPictureThumbnailUrl());
        setNotes(contactEntity.getNotes());
        setExpand(contactEntity.isExpand());
    }
}
