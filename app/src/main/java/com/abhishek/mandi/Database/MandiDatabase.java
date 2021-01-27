package com.abhishek.mandi.Database;

import android.content.Context;
import android.os.AsyncTask;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

import com.abhishek.mandi.Dao.MandiDao;
import com.abhishek.mandi.Modal.Record;
//import com.jayant.roomdatabaseretrofit.Modal.DataList;

@Database(entities = {Record.class}, version = 2)
public abstract class MandiDatabase extends RoomDatabase {
    private static final String DATABASE_NAME = "MandiDatabase";
    private static volatile MandiDatabase INSTANCE;
    public static Callback callback = new Callback() {
        @Override
        public void onCreate(@NonNull SupportSQLiteDatabase db) {
            super.onCreate(db);
            new PopulateAsynTask(INSTANCE);
        }
    };

    public static MandiDatabase getInstance(Context context) {
        if (INSTANCE == null) {
            synchronized (MandiDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context, MandiDatabase.class,
                            DATABASE_NAME)
                            .addCallback(callback)
                            .fallbackToDestructiveMigration()
                            .build();
                }
            }
        }
        return INSTANCE;
    }

    public static void deleteAll() {
        new PopulateAsynTask(INSTANCE).execute();
    }

    public abstract MandiDao mandiDao();

    public static class PopulateAsynTask extends AsyncTask<Void, Void, Void> {
        private MandiDao mandiDao;

        public PopulateAsynTask(MandiDatabase mandiDatabase) {
            mandiDao = mandiDatabase.mandiDao();
        }

        @Override
        protected Void doInBackground(Void... voids) {

            mandiDao.deleteAll();
            return null;
        }
    }
}
