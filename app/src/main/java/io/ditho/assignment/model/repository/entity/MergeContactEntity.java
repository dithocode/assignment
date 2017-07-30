package io.ditho.assignment.model.repository.entity;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.ForeignKey;
import android.arch.persistence.room.Index;
import android.arch.persistence.room.PrimaryKey;

@Entity(indices =
            @Index(value = {
                "MergeID",
                "ContactID"
            }),
        foreignKeys = {
            @ForeignKey(
                entity = MergeEntity.class,
                parentColumns = "id",
                childColumns = "MergeID"),
            @ForeignKey(
                entity = ContactEntity.class,
                    parentColumns = "id",
                    childColumns = "ContactID"
        )})
public class MergeContactEntity {

    @PrimaryKey(autoGenerate = true)
    private Long id;

    @ColumnInfo(name = "MergeID")
    private String mergeId;

    @ColumnInfo(name = "ContactID")
    private String contactId;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getMergeId() {
        return mergeId;
    }

    public void setMergeId(String mergeId) {
        this.mergeId = mergeId;
    }

    public String getContactId() {
        return contactId;
    }

    public void setContactId(String contactId) {
        this.contactId = contactId;
    }
}
