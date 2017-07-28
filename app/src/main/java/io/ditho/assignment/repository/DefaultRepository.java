package io.ditho.assignment.repository;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.RoomDatabase;

import io.ditho.assignment.repository.dao.MergedContactDao;
import io.ditho.assignment.repository.dao.ContactDao;
import io.ditho.assignment.repository.entity.ContactEntity;
import io.ditho.assignment.repository.entity.MergedContactEntity;

@Database(entities = {ContactEntity.class, MergedContactEntity.class}, version = 1)
public abstract class DefaultRepository extends RoomDatabase {
    public abstract ContactDao getContactDao();
    public abstract MergedContactDao getMergedContactDao();
}
