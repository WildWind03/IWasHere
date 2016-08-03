package com.noveogroup.teamzolotov.iwashere.api;

import com.noveogroup.teamzolotov.iwashere.model.Place;

import retrofit2.http.GET;
import retrofit2.http.Query;
import rx.Observable;

/**
 * Created by dserov on 03/08/16.
 */
public interface NominatimService {

    String API_ENDPOINT = " http://nominatim.openstreetmap.org/";

    @GET("reverse?format=json")
    Observable<Place> getPlace(@Query("lat") double latitude, @Query("lon") double longitude);

}
