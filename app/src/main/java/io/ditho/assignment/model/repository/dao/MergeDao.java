package io.ditho.assignment.model.repository.dao;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;

import java.util.List;

import io.ditho.assignment.model.repository.entity.MergeEntity;


@Dao
public interface MergeDao {

    @Query("SELECT * FROM MergeEntity ORDER BY Account, FirstName ASC")
    List<MergeEntity> getAll();

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void insertAll(MergeEntity... users);

    @Query("DELETE FROM MergeEntity WHERE 1")
    void deleteAll();

}
