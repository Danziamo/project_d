package com.mirsoft.easyfixmaster.api;

import com.mirsoft.easyfixmaster.models.Session;

import retrofit.Callback;
import retrofit.http.Body;
import retrofit.http.POST;

public interface SessionApi {
    @POST("/login/")
    void login(@Body Session session, Callback<Session> callback);

    @POST("/logout/")
    void logout(@Body Session session, Callback<?> callback);
}
