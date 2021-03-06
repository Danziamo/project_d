package com.mirsoft.easyfix.networking.api;

import com.mirsoft.easyfix.models.SocialUser;
import com.mirsoft.easyfix.models.User;
import com.mirsoft.easyfix.models.UserSpecialty;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import retrofit.Callback;
import retrofit.http.Body;
import retrofit.http.GET;
import retrofit.http.Multipart;
import retrofit.http.PATCH;
import retrofit.http.POST;
import retrofit.http.Part;
import retrofit.http.Path;
import retrofit.http.Query;
import retrofit.mime.TypedFile;

public interface UserApi {
    @POST("/users")
    void add(@Body User user, Callback<User> callback);

    @GET("/users/{id}")
    void getById(@Path("id") int id, Callback<User> callback);

    @PATCH("/users/{id}")
    void save(@Path("id") int id, @Body User user, Callback<User> callback);

    @GET("/users")
    List<User> getAll(@Query("offset") int offset, @Query("limit") int limit);

    @POST("/social-register")
    void add(@Body SocialUser user, Callback<Object> callback);

    @GET("/users")
    void getAllByQuery(@Query("specialty") String specialty, Callback<ArrayList<User> > callback);

    @GET("/users/{id}/user-specialties")
    void getSpecialties(@Path("id") int id, Callback<ArrayList<UserSpecialty>> callback);

    @Multipart
    @POST("/users/{userId}/upload_picture")
    void uploadPicture(@Path("userId") int userId,
                   @Part("picture") TypedFile picture,
                   Callback<Object> cb);
}
