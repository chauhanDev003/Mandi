package com.abhishek.mandi.Dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.abhishek.mandi.Modal.Record;

import java.util.List;

@Dao
public interface MandiDao {
    /**
     * @param recordList
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(List<Record> recordList);

    @Query("SELECT * FROM MandiData")
    LiveData<List<Record>> getAllActors();

    @Query("DELETE FROM MandiData")
    void deleteAll();
}
