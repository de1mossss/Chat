package com.illumo.chat;

import com.google.gson.annotations.SerializedName;

public class LoginResult
{
    @SerializedName("username")
    private String username;
    @SerializedName("token")
    private String token;

    public String getName() {
        return username;
    }

    public void setName(String name) {
        this.username = name;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
