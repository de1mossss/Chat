package com.illumo.chat;

import java.util.HashMap;

import retrofit2.Call;
import retrofit2.Response;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface RetrofitInterface {

    String BASE_URL = "http://192.168.100.10:3001/";

    @POST("auth")
    Call<User> executeLogin(@Body HashMap<String, String> map);

    @POST("user")
    Call<Void> executeSignUp(@Body HashMap<String, String> map);

    @GET("user/{id}")
    Call<User> executeGetUserID(@Header("Cookie") String token, @Path("id") String id);

    @GET("auth")
    Call<Void> executeAuthCheck(@Header("Cookie") String token);
}
