package io.ditho.assignment.repository.dao;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;

import java.util.List;

import io.ditho.assignment.repository.entity.ContactEntity;
import io.ditho.assignment.repository.entity.MergedContactEntity;


@Dao
public interface MergedContactDao {

    @Query("SELECT * FROM MergedContactEntity ORDER BY Account, FirstName ASC")
    List<MergedContactEntity> getAll();

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void insertAll(MergedContactEntity... users);

    @Query("DELETE FROM MergedContactEntity WHERE 1")
    void deleteAll();

}
