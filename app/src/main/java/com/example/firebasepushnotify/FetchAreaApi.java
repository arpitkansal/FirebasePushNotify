package com.example.firebasepushnotify;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

public interface FetchAreaApi {

//    String base_url = "http://127.0.0.1:8000/garbage/driverpath/";
    String base_url = "http://192.168.43.112:8000/";
    @GET("allareas")
    Call<List<Areas>> getAreas();

}
