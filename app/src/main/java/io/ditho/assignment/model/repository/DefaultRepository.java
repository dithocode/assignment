package io.ditho.assignment.model.repository;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.RoomDatabase;

import io.ditho.assignment.model.repository.dao.ContactDao;
import io.ditho.assignment.model.repository.dao.MergeContactDao;
import io.ditho.assignment.model.repository.dao.MergeDao;
import io.ditho.assignment.model.repository.entity.MergeContactEntity;
import io.ditho.assignment.model.repository.entity.MergeEntity;
import io.ditho.assignment.model.repository.entity.ContactEntity;

@Database(entities = {ContactEntity.class, MergeEntity.class, MergeContactEntity.class}, version = 1)
public abstract class DefaultRepository extends RoomDatabase {
    public abstract ContactDao getContactDao();
    public abstract MergeDao getMergedDao();
    public abstract MergeContactDao getMergeContactDao();

}
