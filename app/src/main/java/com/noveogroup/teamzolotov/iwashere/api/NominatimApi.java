package com.noveogroup.teamzolotov.iwashere.api;

import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by dserov on 03/08/16.
 */
public class NominatimApi {
    private static NominatimService ourInstance;

    private NominatimApi() {}

    public static NominatimService getInstance() {
        if (ourInstance == null) {
            final Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(NominatimService.API_ENDPOINT)
                    .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
            ourInstance = retrofit.create(NominatimService.class);
        }
        return ourInstance;
    }
}
