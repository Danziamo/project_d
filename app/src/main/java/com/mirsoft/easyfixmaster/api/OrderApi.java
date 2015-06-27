package com.mirsoft.easyfixmaster.api;

import com.mirsoft.easyfixmaster.models.Order;

import java.util.ArrayList;

import retrofit.Callback;
import retrofit.http.GET;
import retrofit.http.POST;
import retrofit.http.Path;

public interface OrderApi {

    @GET("/users/{id}/orders")
    void getByUserId(@Path("id") int id, Callback<ArrayList<Order>> callback);

    @GET("/orders/{id}")
    void getById(@Path("id") int id, Callback<Order> callback);

    @POST("/users/{userId}/orderes/{orderId}/post_contractor_request")
    void postRequest(@Path("userId") int userId, @Path("orderId") int orderId);
}
