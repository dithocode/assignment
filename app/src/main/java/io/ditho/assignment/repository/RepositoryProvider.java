package io.ditho.assignment.repository;

import android.arch.persistence.room.Room;
import android.content.Context;

public class RepositoryProvider {

    private static RepositoryProvider instance;

    private DefaultRepository defaultRepository;

    private RepositoryProvider(Context context) {
        defaultRepository = Room.databaseBuilder(context, DefaultRepository.class, "default")
                .build();
    }

    public static synchronized RepositoryProvider getInstance(Context context) {
        if (instance == null) {
            instance = new RepositoryProvider(context);
        }

        return instance;
    }

    public DefaultRepository getDefaultRepository() {
        return defaultRepository;
    }

}
