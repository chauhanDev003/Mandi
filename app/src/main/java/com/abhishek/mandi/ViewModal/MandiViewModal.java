package com.abhishek.mandi.ViewModal;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.abhishek.mandi.Modal.Record;
import com.abhishek.mandi.Repository.MandiRespository;

import java.util.List;

public class MandiViewModal extends AndroidViewModel {

    private MandiRespository mandiRespository;
    private LiveData<List<Record>> getAllActors;

    public MandiViewModal(@NonNull Application application) {
        super(application);
        mandiRespository = new MandiRespository(application);
        getAllActors = mandiRespository.getAllActors();
    }

    public void insert(List<Record> list) {
        mandiRespository.insert(list);
    }

    public LiveData<List<Record>> getAllMandiData() {
        return getAllActors;
    }

}
