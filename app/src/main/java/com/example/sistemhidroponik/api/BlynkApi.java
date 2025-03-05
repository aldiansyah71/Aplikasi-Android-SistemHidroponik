package com.example.sistemhidroponik.api;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface BlynkApi {
    @GET("/external/api/get")
    Call<String> getPinValue(@Query("token") String token, @Query("pin") String pin);

    @GET("external/api/update")
    Call<Void> updatePinValue(@Query("token") String token, @Query("pin") String pin, @Query("value") String value);
}
