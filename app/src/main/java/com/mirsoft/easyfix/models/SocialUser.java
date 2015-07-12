package com.mirsoft.easyfix.models;

import com.google.gson.annotations.Expose;

public class SocialUser {
    @Expose
    public String id;
    @Expose
    public String provider;
    @Expose
    public String phone;
}
