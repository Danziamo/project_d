package com.mirsoft.easyfix.api;

import com.mirsoft.easyfix.models.Session;

import retrofit.Callback;
import retrofit.http.Body;
import retrofit.http.POST;

public interface SessionApi {
    @POST("/login/")
    void login(@Body Session session, Callback<Session> callback);

    @POST("/logout/")
    void logout(@Body Session session, Callback<?> callback);
}
