package io.ditho.assignment.rest.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Contact {
    @JsonProperty("Account")
    private String account;

    @JsonProperty("BusinessEmail")
    private String businessEmail;

    @JsonProperty("BusinessPhone")
    private String businessPhone;

    @JsonProperty("BusinessMobile")
    private String businessMobile;

    @JsonProperty("Email")
    private String email;

    @JsonProperty("FirstName")
    private String firstName;

    @JsonProperty("FullName")
    private String fullName;

    @JsonProperty("Gender")
    private String gender;

    @JsonProperty("ID")
    private String id;

    @JsonProperty("JobTitleDescription")
    private String jobTitleDescription;

    @JsonProperty("LastName")
    private String lastName;

    @JsonProperty("MiddleName")
    private String middleName;

    @JsonProperty("Mobile")
    private String mobile;

    @JsonProperty("Notes")
    private String notes;

    @JsonProperty("Phone")
    private String phone;

    @JsonProperty("PictureThumbnailUrl")
    private String pictureThumbnailUrl;

    public Contact() {}

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public String getBusinessEmail() {
        return businessEmail;
    }

    public void setBusinessEmail(String businessEmail) {
        this.businessEmail = businessEmail;
    }

    public String getBusinessPhone() {
        return businessPhone;
    }

    public void setBusinessPhone(String businessPhone) {
        this.businessPhone = businessPhone;
    }

    public String getBusinessMobile() {
        return businessMobile;
    }

    public void setBusinessMobile(String businessMobile) {
        this.businessMobile = businessMobile;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFirstName() {
        return this.firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getID() {
        return id;
    }

    public void setID(String ID) { this.id = ID; }

    public String getJobTitleDescription() {
        return jobTitleDescription;
    }

    public void setJobTitleDescription(String jobTitleDescription) { this.jobTitleDescription = jobTitleDescription; }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getMiddleName() {
        return middleName;
    }

    public void setMiddleName(String middleName) {
        this.middleName = middleName;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getPictureThumbnailUrl() {
        return pictureThumbnailUrl;
    }

    public void setPictureThumbnailUrl(String pictureThumbnailUrl) { this.pictureThumbnailUrl = pictureThumbnailUrl; }
}
