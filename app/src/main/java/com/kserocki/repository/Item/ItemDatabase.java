package com.kserocki.repository.Item;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.kserocki.repository.List.ListEntity;

@Database(entities = {ListEntity.class, ItemEntity.class}, version = 3, exportSchema = false)
public abstract class ItemDatabase extends RoomDatabase {

    private static final String DB_NAME = "Items.db";
    private static final Object sLock = new Object();
    private static volatile ItemDatabase INSTANCE;

    public static ItemDatabase getInstance(Context context) {
        if (INSTANCE == null) {
            synchronized (sLock) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                            ItemDatabase.class, DB_NAME)
                            .fallbackToDestructiveMigration()
                            .build();
                }
            }
        }
        return INSTANCE;
    }

    public abstract ItemDao itemDao();

}
