package com.mirsoft.easyfix.networking.api;

import com.mirsoft.easyfix.models.ActivationCode;
import com.mirsoft.easyfix.models.Session;
import com.mirsoft.easyfix.models.SocialSession;

import retrofit.Callback;
import retrofit.http.Body;
import retrofit.http.POST;

public interface SessionApi {
    @POST("/login")
    void login(@Body Session session, Callback<Session> callback);

    @POST("/logout")
    void logout(@Body Session session, Callback<?> callback);

    @POST("/social-login")
    void login(@Body SocialSession session, Callback<Session> callback);

    @POST("/social-activate")
    void activateSocial(@Body ActivationCode code, Callback<Session> callback);

    @POST("/activate")
    void activate(@Body ActivationCode code, Callback<Session> callback);
}
