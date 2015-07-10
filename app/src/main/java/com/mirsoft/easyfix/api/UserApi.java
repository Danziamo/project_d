package com.mirsoft.easyfix.api;

import com.mirsoft.easyfix.models.User;

import java.util.List;

import retrofit.Callback;
import retrofit.http.Body;
import retrofit.http.GET;
import retrofit.http.POST;
import retrofit.http.Path;
import retrofit.http.Query;

public interface UserApi {
    @POST("/users")
    void add(@Body User user, Callback<User> callback);

    @GET("/users/{id}")
    void getById(@Path("id") int id, Callback<User> callback);

    @GET("/users")
    List<User> getAll(@Query("offset") int offset, @Query("limit") int limit);
}
