package com.abhishek.mandi.Repository;

import android.app.Application;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;

import com.abhishek.mandi.Dao.MandiDao;
import com.abhishek.mandi.Database.MandiDatabase;
import com.abhishek.mandi.Modal.Record;

import java.util.List;

public class MandiRespository {
    private MandiDatabase database;
    private LiveData<List<Record>> getAllMandiData;

    public MandiRespository(Application application) {
        database = MandiDatabase.getInstance(application);
        getAllMandiData = database.mandiDao().getAllActors();
    }

    public void insert(List<Record> mandiList) {
        new InsertAsynTask(database).execute(mandiList);
    }

    public LiveData<List<Record>> getAllActors() {
        return getAllMandiData;
    }

    static class InsertAsynTask extends AsyncTask<List<Record>, Void, Void> {
        private MandiDao mandiDao;

        InsertAsynTask(MandiDatabase mandiDatabase) {
            mandiDao = mandiDatabase.mandiDao();
        }

        @SafeVarargs
        @Override
        protected final Void doInBackground(List<Record>... lists) {
            mandiDao.insert(lists[0]);
            return null;
        }
    }
}
