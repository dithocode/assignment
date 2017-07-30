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

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(ContactEntity... contactEntities);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(List<ContactEntity> contactEntities);

    @Delete
    void delete(ContactEntity user);

    @Query("DELETE FROM ContactEntity WHERE 1")
    void deleteAll();
}
