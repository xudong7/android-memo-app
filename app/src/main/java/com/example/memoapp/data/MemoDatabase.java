package com.example.memoapp.data;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

@Database(entities = {Memo.class}, version = 1, exportSchema = false)
public abstract class MemoDatabase extends RoomDatabase {

    private static MemoDatabase instance;

    public abstract MemoDao memoDao();

    public static synchronized MemoDatabase getInstance(Context context) {
        if (instance == null) {
            instance = Room.databaseBuilder(context.getApplicationContext(),
                            MemoDatabase.class, "memo_database")
                    .fallbackToDestructiveMigration()
                    .build();
        }
        return instance;
    }
}