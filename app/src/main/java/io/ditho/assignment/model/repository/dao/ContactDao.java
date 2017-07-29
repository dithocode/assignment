package io.ditho.assignment.model.repository.dao;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;

import java.util.List;

import io.ditho.assignment.model.repository.entity.ContactEntity;

@Dao
public interface ContactDao {
    @Query("SELECT * FROM ContactEntity ORDER BY Account, FirstName ASC")
    List<ContactEntity> getAll();

    @Query("SELECT * FROM ContactEntity " +
            "WHERE Account != '' " +
            "AND FirstName != '' " +
            "ORDER BY Account, FirstName ASC")
    List<ContactEntity> getValidContact();

    @Query("SELECT BusinessEmail FROM ContactEntity " +
            "WHERE BusinessEmail != '' " +
            "AND Account LIKE :account " +
            "AND (FirstName LIKE :firstName OR FullName LIKE :firstName)" +
            "AND id != :id")
    List<String> getBusinessEmail(String account, String firstName, String id);

    @Query("SELECT BusinessPhone FROM ContactEntity " +
            "WHERE BusinessPhone != '' " +
            "AND Account LIKE :account " +
            "AND (FirstName LIKE :firstName OR FullName LIKE :firstName)" +
            "AND id != :id")
    List<String> getBusinessPhone(String account, String firstName, String id);

    @Query("SELECT BusinessMobile FROM ContactEntity " +
            "WHERE BusinessMobile != '' " +
            "AND Account LIKE :account " +
            "AND (FirstName LIKE :firstName OR FullName LIKE :firstName)" +
            "AND id != :id")
    List<String> getBusinessMobile(String account, String firstName, String id);

    @Query("SELECT Email FROM ContactEntity " +
            "WHERE Email != '' " +
            "AND Account LIKE :account " +
            "AND (FirstName LIKE :firstName OR FullName LIKE :firstName)" +
            "AND id != :id")
    List<String> getEmail(String account, String firstName, String id);

    @Query("SELECT FullName FROM ContactEntity " +
            "WHERE FullName != '' " +
            "AND Account LIKE :account " +
            "AND (FirstName LIKE :firstName OR FullName LIKE :firstName)" +
            "AND id != :id")
    List<String> getFullName(String account, String firstName, String id);

    @Query("SELECT Gender FROM ContactEntity " +
            "WHERE Gender != '' " +
            "AND Account LIKE :account " +
            "AND (FirstName LIKE :firstName OR FullName LIKE :firstName)" +
            "AND id != :id")
    List<String> getGender(String account, String firstName, String id);

    @Query("SELECT JobTitleDescription FROM ContactEntity " +
            "WHERE JobTitleDescription != '' " +
            "AND Account LIKE :account " +
            "AND (FirstName LIKE :firstName OR FullName LIKE :firstName)" +
            "AND id != :id")
    List<String> getJobTitleDescription(String account, String firstName, String id);

    @Query("SELECT LastName FROM ContactEntity " +
            "WHERE LastName != '' " +
            "AND Account LIKE :account " +
            "AND (FirstName LIKE :firstName OR FullName LIKE :firstName)" +
            "AND id != :id")
    List<String> getLastName(String account, String firstName, String id);

    @Query("SELECT MiddleName FROM ContactEntity " +
            "WHERE MiddleName != '' " +
            "AND Account LIKE :account " +
            "AND (FirstName LIKE :firstName OR FullName LIKE :firstName)" +
            "AND id != :id")
    List<String> getMiddleName(String account, String firstName, String id);

    @Query("SELECT Mobile FROM ContactEntity " +
            "WHERE Mobile != '' " +
            "AND Account LIKE :account " +
            "AND (FirstName LIKE :firstName OR FullName LIKE :firstName)" +
            "AND id != :id")
    List<String> getMobile(String account, String firstName, String id);

    @Query("SELECT Notes FROM ContactEntity " +
            "WHERE Notes != '' " +
            "AND Account LIKE :account " +
            "AND (FirstName LIKE :firstName OR FullName LIKE :firstName)" +
            "AND id != :id")
    List<String> getNotes(String account, String firstName, String id);

    @Query("SELECT Phone FROM ContactEntity " +
            "WHERE Phone != '' " +
            "AND Account LIKE :account " +
            "AND (FirstName LIKE :firstName OR FullName LIKE :firstName)" +
            "AND id != :id")
    List<String> getPhone(String account, String firstName, String id);

    @Query("SELECT PictureThumbnailUrl FROM ContactEntity " +
            "WHERE PictureThumbnailUrl != '' " +
            "AND Account LIKE :account " +
            "AND (FirstName LIKE :firstName OR FullName LIKE :firstName)" +
            "AND id != :id")
    List<String> getPictureThumbnailUrl(String account, String firstName, String id);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(ContactEntity... contactEntities);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(List<ContactEntity> contactEntities);

    @Delete
    void delete(ContactEntity user);

    @Query("DELETE FROM ContactEntity WHERE 1")
    void deleteAll();
}
