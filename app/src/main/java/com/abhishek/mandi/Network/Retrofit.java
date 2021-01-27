package com.abhishek.mandi.Network;

import retrofit2.converter.gson.GsonConverterFactory;


public class Retrofit {

    private static final String ROOT_URL = Url.URL_DATA;

    /**
     * Get Retrofit Instance
     */
    private static retrofit2.Retrofit getRetrofitInstance() {

        return new retrofit2.Retrofit.Builder()
                .baseUrl(ROOT_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
    }

    /**
     * Get API Service
     *
     * @return API Service
     */
    public static Api getApiService() {
        return getRetrofitInstance().create(Api.class);
    }


}
