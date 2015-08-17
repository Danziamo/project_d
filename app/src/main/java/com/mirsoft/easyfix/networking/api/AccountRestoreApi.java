package com.mirsoft.easyfix.networking.api;

import com.mirsoft.easyfix.models.ActivationCode;
import com.mirsoft.easyfix.models.Session;
import com.squareup.okhttp.Call;

import retrofit.Callback;
import retrofit.http.Body;
import retrofit.http.GET;
import retrofit.http.POST;
import retrofit.http.Query;

public interface AccountRestoreApi {
    @GET("/users/lost_password_request")
    void sendResetPasswordRequest(@Query("phone") String phone, Callback<Object> callback);

    @POST("/users/create_new_password")
    void sendNewPasswordRequest(@Body ActivationCode code, Callback<Object> callback);

    @POST("/social-activate")
    void activateSocial(@Body ActivationCode code, Callback<Session> callback);

    @POST("/activate")
    void activate(@Body ActivationCode code, Callback<Session> callback);

    @POST("/users/resend_activation_code")
    void resendActivationCode(@Body ActivationCode code, Callback<Object> callback);
}
