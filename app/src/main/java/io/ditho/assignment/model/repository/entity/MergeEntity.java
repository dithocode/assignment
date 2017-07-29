package io.ditho.assignment.model.repository.entity;

import android.arch.persistence.room.Entity;

@Entity
public class MergeEntity extends ContactEntity {

    public MergeEntity() { super(); }

    public MergeEntity(ContactEntity contactEntity) {
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
