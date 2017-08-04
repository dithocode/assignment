package io.ditho.assignment.common;

import android.text.TextUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import io.ditho.assignment.model.repository.entity.ContactEntity;
import io.ditho.assignment.model.rest.model.Contact;

public class ModelConverter {

    public static ContactEntity from(Contact contact) {
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

    public static void transform(
            List<ContactEntity> model,
            ArrayList<String> destListTitle,
            ArrayList<ArrayList<String>> destFieldValueList) {

        destListTitle.clear();
        destFieldValueList.clear();

        destListTitle.add("First Name");
        destListTitle.add("Middle Name");
        destListTitle.add("Last Name");
        destListTitle.add("Full Name");
        destListTitle.add("Gender");
        destListTitle.add("Email");
        destListTitle.add("Phone");
        destListTitle.add("Mobile");
        destListTitle.add("Business Email");
        destListTitle.add("Business Phone");
        destListTitle.add("Business Mobile");
        destListTitle.add("Job Title");
        destListTitle.add("Notes");
        destListTitle.add("Picture Url");

        int modelSize = model.size();

        ArrayList<String> fieldFirstNameValueList = new ArrayList<>();
        ArrayList<String> fieldMidleNameValueList = new ArrayList<>();
        ArrayList<String> fieldLastNameValueList = new ArrayList<>();
        ArrayList<String> fieldFullNameValueList = new ArrayList<>();
        ArrayList<String> fieldGenderValueList = new ArrayList<>();
        ArrayList<String> fieldEmailValueList = new ArrayList<>();
        ArrayList<String> fieldPhoneValueList = new ArrayList<>();
        ArrayList<String> fieldMobileValueList = new ArrayList<>();
        ArrayList<String> fieldBusinessEmailValueList = new ArrayList<>();
        ArrayList<String> fieldBusinessPhoneValueList = new ArrayList<>();
        ArrayList<String> fieldBusinessMobileValueList = new ArrayList<>();
        ArrayList<String> fieldJobTitleValueList = new ArrayList<>();
        ArrayList<String> fieldNotesValueList = new ArrayList<>();
        ArrayList<String> fieldPictureUrlValueList = new ArrayList<>();

        for (int counter = 0; counter < modelSize; counter++) {
            ContactEntity contactEntity = model.get(counter);
            fieldFirstNameValueList.add(contactEntity.getFirstName());
            fieldMidleNameValueList.add(contactEntity.getMiddleName());
            fieldLastNameValueList.add(contactEntity.getLastName());
            fieldFullNameValueList.add(contactEntity.getFullName());
            fieldGenderValueList.add(contactEntity.getGender());
            fieldEmailValueList.add(contactEntity.getEmail());
            fieldPhoneValueList.add(contactEntity.getPhone());
            fieldMobileValueList.add(contactEntity.getMobile());
            fieldBusinessEmailValueList.add(contactEntity.getBusinessEmail());
            fieldBusinessPhoneValueList.add(contactEntity.getBusinessPhone());
            fieldBusinessMobileValueList.add(contactEntity.getBusinessMobile());
            fieldJobTitleValueList.add(contactEntity.getJobTitleDescription());
            fieldNotesValueList.add(contactEntity.getNotes());
            fieldPictureUrlValueList.add(contactEntity.getPictureThumbnailUrl());
        }

        destFieldValueList.add(fieldFirstNameValueList);
        destFieldValueList.add(fieldMidleNameValueList);
        destFieldValueList.add(fieldLastNameValueList);
        destFieldValueList.add(fieldFullNameValueList);
        destFieldValueList.add(fieldGenderValueList);
        destFieldValueList.add(fieldEmailValueList);
        destFieldValueList.add(fieldPhoneValueList);
        destFieldValueList.add(fieldMobileValueList);
        destFieldValueList.add(fieldBusinessEmailValueList);
        destFieldValueList.add(fieldBusinessPhoneValueList);
        destFieldValueList.add(fieldBusinessMobileValueList);
        destFieldValueList.add(fieldJobTitleValueList);
        destFieldValueList.add(fieldNotesValueList);
        destFieldValueList.add(fieldPictureUrlValueList);
    }

    public static void transform(HashMap<String, String> selectedResult, ContactEntity destEntity) {
        String value = "";

        value = selectedResult.get("First Name");
        if (!TextUtils.isEmpty(value)) destEntity.setFirstName(value);

        value = selectedResult.get("Middle Name");
        if (!TextUtils.isEmpty(value)) destEntity.setMiddleName(value);

        value = selectedResult.get("Last Name");
        if (!TextUtils.isEmpty(value)) destEntity.setLastName(value);

        value = selectedResult.get("Full Name");
        if (!TextUtils.isEmpty(value)) destEntity.setFullName(value);

        value = selectedResult.get("Gender");
        if (!TextUtils.isEmpty(value)) destEntity.setGender(value);

        value = selectedResult.get("Email");
        if (!TextUtils.isEmpty(value)) destEntity.setEmail(value);

        value = selectedResult.get("Phone");
        if (!TextUtils.isEmpty(value)) destEntity.setPhone(value);

        value = selectedResult.get("Mobile");
        if (!TextUtils.isEmpty(value)) destEntity.setMobile(value);

        value = selectedResult.get("Business Email");
        if (!TextUtils.isEmpty(value)) destEntity.setBusinessEmail(value);

        value = selectedResult.get("Business Phone");
        if (!TextUtils.isEmpty(value)) destEntity.setBusinessPhone(value);

        value = selectedResult.get("Business Mobile");
        if (!TextUtils.isEmpty(value)) destEntity.setBusinessMobile(value);

        value = selectedResult.get("Job Title");
        if (!TextUtils.isEmpty(value)) destEntity.setJobTitleDescription(value);

        value = selectedResult.get("Notes");
        if (!TextUtils.isEmpty(value)) destEntity.setNotes(value);

        value = selectedResult.get("Picture Url");
        if (!TextUtils.isEmpty(value)) destEntity.setPictureThumbnailUrl(value);
    }
}
