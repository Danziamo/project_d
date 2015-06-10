package com.mirsoft.easyfixmaster.api;

import com.mirsoft.easyfixmaster.models.Session;

import retrofit.Callback;
import retrofit.http.Body;
import retrofit.http.POST;

public interface SessionApi {
    @POST("/api/v1/login/")
    void login(@Body Session session, Callback<Session> callback);

    @POST("/api/v1/logout/")
    void logout(Callback<?> callback);
}
