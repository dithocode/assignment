package io.ditho.assignment.model.repository.dao;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;

import java.util.List;

import io.ditho.assignment.model.repository.entity.MergeContactEntity;

@Dao
public interface MergeContactDao {
    @Query("SELECT * FROM MergeContactEntity ORDER BY id ASC")
    List<MergeContactEntity> getAll();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(MergeContactEntity... mergeContact);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(List<MergeContactEntity> mergeContactList);

    @Query("DELETE FROM MergeContactEntity WHERE 1")
    void deleteAll();
}
