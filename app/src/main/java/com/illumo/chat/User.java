package com.illumo.chat;

import com.google.gson.annotations.SerializedName;

public class User {

    @SerializedName("id")
    private String id;
    @SerializedName("createdAt")
    private String createdAt;
    @SerializedName("nickname")
    private String nickname;
    @SerializedName("token")
    private String token;

    public String getToken() {
        return token;
    }

    public String getId() {
        return id;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public String getNickname() {
        return nickname;
    }
}
