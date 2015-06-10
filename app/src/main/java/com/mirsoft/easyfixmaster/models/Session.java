package com.mirsoft.easyfixmaster.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Session {
    @Expose
    @SerializedName("phone")
    public String username;
    @Expose
    public String password;
    @Expose
    public String token;
}
