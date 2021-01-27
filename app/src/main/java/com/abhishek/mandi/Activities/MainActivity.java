package com.abhishek.mandi.Activities;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.abhishek.mandi.Adapter.MandiAdapter;
import com.abhishek.mandi.Database.MandiDatabase;
import com.abhishek.mandi.Modal.DataList;
import com.abhishek.mandi.Modal.Record;
import com.abhishek.mandi.Network.Api;
import com.abhishek.mandi.Network.Retrofit;
import com.abhishek.mandi.R;
import com.abhishek.mandi.Repository.MandiRespository;
import com.abhishek.mandi.ViewModal.MandiViewModal;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    private static String MY_PREFS_NAME = "Mandi_SP";
    ProgressBar pb;
    boolean doubleBackToExitPressedOnce = false;
    private MandiViewModal mandiViewModal;
    private RecyclerView recyclerView;
    private List<Record> mandiList;
    private MandiRespository mandiRespository;
    private MandiAdapter mandiAdapter;
    private int page = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);//  set status text dark


        pb = findViewById(R.id.pb);
        recyclerView = findViewById(R.id.recyclerview);
        recyclerView.setHasFixedSize(false);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        mandiRespository = new MandiRespository(getApplication());
        mandiList = new ArrayList<>();
        mandiAdapter = new MandiAdapter(this, mandiList);
        recyclerView.setAdapter(mandiAdapter);

        mandiViewModal = new ViewModelProvider(this).get(MandiViewModal.class);
        SharedPreferences prefs = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);
        int newPage = prefs.getInt("page", 0);
        page = newPage;
        networkRequest(String.valueOf(page));


        mandiViewModal.getAllMandiData().observe(this, new Observer<List<Record>>() {
            @Override
            public void onChanged(List<Record> actorList) {
                mandiAdapter.notifyDataSetChanged();
                mandiAdapter.getAllMandi(actorList);

                Log.d("main", "onChanged: " + actorList);
            }
        });


        //below funtion is used for finding out the postion of the scroll in recycle view so that is user is at end new data can be loaded
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (!recyclerView.canScrollVertically(1)) { //1 for down
                    page += 10;
                    SharedPreferences.Editor editor = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE).edit();
                    editor.putInt("page", page);
                    editor.commit();
                    networkRequest(String.valueOf(page));
                }
            }
        });


    }

    private void networkRequest(String offset) {
        pb.setVisibility(View.VISIBLE);
        Api api = Retrofit.getApiService();
        Call<DataList> call = api.getAllMandiData(offset);
        call.enqueue(new Callback<DataList>() {
            @Override
            public void onResponse(Call<DataList> call, Response<DataList> response) {
                if (response.isSuccessful()) {

                    String Updatedate = response.body().getUpdatedDate();

                    SharedPreferences prefs = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);
                    String modifiedDataDate = prefs.getString("Date", "");//"No name defined" is the default value.

                    // if data is updated remove previous data and enter new data in ROOM DB
                    if (!(modifiedDataDate.equals(Updatedate))) {
                        page = 0;
                        SharedPreferences.Editor editor = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE).edit();
                        editor.putString("Date", Updatedate);
                        editor.putInt("page", page);
                        editor.commit();

                        MandiDatabase.deleteAll();
                        mandiAdapter.notifyDataSetChanged();
                        networkRequest(String.valueOf(0));
                        return;
                    }
                    for (int i = 0; i < response.body().getRecords().size(); i++) {
                        response.body().getRecords().get(i).setTimestamp(String.valueOf(i + page));
                    }

                    mandiRespository.insert(response.body().getRecords());
                }

                pb.setVisibility(View.GONE);

            }

            @Override
            public void onFailure(Call<DataList> call, Throwable t) {
                pb.setVisibility(View.GONE);

                Log.e("Error", t.toString());
                if (t instanceof IOException) {
                    Toast.makeText(MainActivity.this, "this is an actual network failure :( inform the user and possibly retry", Toast.LENGTH_SHORT).show();
                    // logging probably not necessary
                } else {
                    Toast.makeText(MainActivity.this, "conversion issue! big problems :(", Toast.LENGTH_SHORT).show();
                }
            }

        });


    }

    @Override
    public void onBackPressed() {
        if (doubleBackToExitPressedOnce) {
            super.onBackPressed();
            return;
        }

        this.doubleBackToExitPressedOnce = true;
        Toast.makeText(this, "Please click BACK again to exit", Toast.LENGTH_SHORT).show();

        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                doubleBackToExitPressedOnce = false;
            }
        }, 2000);
    }
}


