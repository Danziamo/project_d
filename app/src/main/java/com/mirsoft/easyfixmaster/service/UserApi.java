package com.mirsoft.easyfixmaster.service;

import com.mirsoft.easyfixmaster.models.User;

import java.util.List;

import retrofit.Callback;
import retrofit.http.Body;
import retrofit.http.GET;
import retrofit.http.POST;
import retrofit.http.Path;
import retrofit.http.Query;

public interface UserApi {
    @POST("/api/v1/users")
    User add(@Body User user);

    @GET("/api/v1/users/{id}")
    void getById(@Path("id") int id, Callback<User> callback);

    @GET("/api/users")
    List<User> getAll(@Query("offset") int offset, @Query("limit") int limit);
}
