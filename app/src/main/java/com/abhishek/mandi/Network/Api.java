package com.abhishek.mandi.Network;

import com.abhishek.mandi.Modal.DataList;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface Api {
    @GET("resource/9ef84268-d588-465a-a308-a864a43d0070?api-key=" + Url.key + "&format=json")
    Call<DataList> getAllMandiData(@Query("offset") String offset);
}
