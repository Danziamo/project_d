package com.mirsoft.easyfix.models;

import com.google.gson.annotations.Expose;

public class SocialSession {
    @Expose
    public String id;
    @Expose
    public String provider;
    @Expose
    public String token;
}
