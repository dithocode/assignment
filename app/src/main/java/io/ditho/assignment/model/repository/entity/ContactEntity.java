package io.ditho.assignment.model.repository.entity;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;
import android.util.Log;

@Entity
public class ContactEntity implements Cloneable {
    @ColumnInfo(name = "Account")
    private String account;

    @ColumnInfo(name = "BusinessEmail")
    private String businessEmail;

    @ColumnInfo(name = "BusinessPhone")
    private String businessPhone;

    @ColumnInfo(name = "BusinessMobile")
    private String businessMobile;

    @ColumnInfo(name = "Email")
    private String email;

    @ColumnInfo(name = "FirstName")
    private String firstName;

    @ColumnInfo(name = "FullName")
    private String fullName;

    @ColumnInfo(name = "Gender")
    private String gender;

    @PrimaryKey
    private String id;

    @ColumnInfo(name = "JobTitleDescription")
    private String jobTitleDescription;

    @ColumnInfo(name = "LastName")
    private String lastName;

    @ColumnInfo(name = "MiddleName")
    private String middleName;

    @ColumnInfo(name = "Mobile")
    private String mobile;

    @ColumnInfo(name = "Notes")
    private String notes;

    @ColumnInfo(name = "Phone")
    private String phone;

    @ColumnInfo(name = "PictureThumbnailUrl")
    private String pictureThumbnailUrl;

    @Ignore
    //@ColumnInfo(name = "ParentID")
    private String parentId;

    @Ignore
    private int linkCount;

    public ContactEntity() {}

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

    public String getId() {
        return id;
    }

    public void setId(String id) { this.id = id; }

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

    public int getLinkCount() { return linkCount; }

    public void setLinkCount(int linkCount) { this.linkCount = linkCount; }

    public String getParentId() { return parentId; }

    public void setParentId(String parentId) { this.parentId = parentId; }

    @Override
    public String toString() {
        return String.format(
            "[account=%s, id=%s, firstName=%s, middleName=%s, lastName=%s, fullName=%s, gender=%s, email=%s, phone=%s, mobile=%s, " +
                    "businessEmail=%s, businessPhone=%s, businessMobile=%s, jobTitle=%s, pictureUrl=%s, notes=%s, parentId=%s, linkCount=%d]",
            this.getAccount(),
            this.getId(),
            this.getFirstName(),
            this.getMiddleName(),
            this.getLastName(),
            this.getFullName(),
            this.getGender(),
            this.getEmail(),
            this.getPhone(),
            this.getMobile(),
            this.getBusinessEmail(),
            this.getBusinessPhone(),
            this.getBusinessMobile(),
            this.getJobTitleDescription(),
            this.getPictureThumbnailUrl(),
            this.getNotes(),
            this.getParentId(),
            this.getLinkCount());
    }
}
