package com.example.demoapplication;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface ApiInterface {
    @GET("everything")
    Call<MyModel.Root> getEverything
        (@Query("q") String q,
         @Query("apiKey") String apiKey);
}
