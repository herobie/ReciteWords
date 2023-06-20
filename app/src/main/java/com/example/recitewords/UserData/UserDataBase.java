package com.example.recitewords.UserData;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.example.recitewords.model.Word;

@Database(entities = {Word.class}, version = 1, exportSchema = false)
public abstract class UserDataBase extends RoomDatabase {
    private static UserDataBase INSTANCE;
    public static synchronized UserDataBase getInstance(Context context){
        if (INSTANCE == null){
            INSTANCE = Room.databaseBuilder(context.getApplicationContext() , UserDataBase.class , "UserWordsData")
                    .build();
        }
        return INSTANCE;
    }

    public abstract WordDao getWordDao();
}
