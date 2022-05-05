package com.illumo.chat;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import retrofit2.Call;
import retrofit2.Response;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface RetrofitInterface {

    String BASE_URL = "http://192.168.100.10:3001/";

    @POST("auth")
    Call<User> executeLogin(@Body HashMap<String, String> map);

    @POST("user")
    Call<Void> executeSignUp(@Body HashMap<String, String> map);

    @GET("user")
    Call<User> executeGetUserID(@Header("Cookie") String token);

    @GET("auth")
    Call<Void> executeAuthCheck(@Header("Cookie") String token);

    @POST("chat")
    Call<Chat> executeNewChat(@Header("Cookie") String token, @Body HashMap<String, Object> map);

    @GET("user")
    Call<List<User>> executeGetUserByNickname(@Header("Cookie") String token, @Query("nickname") String nick);

    @GET("chat")
    Call<List<Chat>> executeGetChatByParticipantId(@Header("Cookie") String token, @Query("participantId") String participantId);

    @POST("message")
    Call<Message> executeSendMessage(@Header("Cookie") String token, @Body HashMap<String, String> map);

    @GET("message")
    Call<List<Message>> executeGetAllMessage(@Header("Cookie") String token, @Query("chatId") String chatId);
}
