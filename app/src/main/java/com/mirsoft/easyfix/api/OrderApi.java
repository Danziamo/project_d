package com.mirsoft.easyfix.api;

import com.mirsoft.easyfix.models.Order;
import com.mirsoft.easyfix.models.User;
import com.mirsoft.easyfix.networking.models.CommonOrder;
import com.mirsoft.easyfix.networking.models.NOrder;
import com.squareup.okhttp.Call;

import java.lang.reflect.Array;
import java.util.ArrayList;

import retrofit.Callback;
import retrofit.http.Body;
import retrofit.http.DELETE;
import retrofit.http.GET;
import retrofit.http.PATCH;
import retrofit.http.POST;
import retrofit.http.Path;
import retrofit.http.Query;

public interface OrderApi {

    @GET("/users/{id}/orders")
    void getByUserId(@Path("id") int id, Callback<ArrayList<Order>> callback);

    @GET("/users/{id}/orders")
    void getByUserIdAndStatuses(@Path("id") int id, @Query("status") String statuses, Callback<ArrayList<Order> > callback);

    @GET("/users/{userId}/orders/{orderId}/list_contractor_requests")
    void getContractorRequests(@Path("userId") int userId,@Path("orderId") int orderId, Callback<ArrayList<Order>> callback);

    @GET("/orders/{id}")
    void getById(@Path("id") int id, Callback<Order> callback);

    @POST("/users/{userId}/orders/{orderId}/post_contractor_request")
    void postRequest(@Body Object object, @Path("userId") int userId, @Path("orderId") int orderId, Callback<Object> callback);

    @POST("/users/{userId}/orders")
    void createOrder(@Body NOrder order, @Path("userId") int userId, Callback<Order> callback);

    @POST("/users/{userId}/orders")
    void createCommonOrder(@Body CommonOrder order,@Path("userId") int userId, Callback<Order> callback);

    @PATCH("/users/{userId}/orders/{orderId}")
    void updateOrder(@Body CommonOrder updatedOrder, @Path("userId") int userId, @Path("orderId") int orderId, Callback<Order> cb);

//    @GET("/list-pending-orders/{orderId}")
//    void getPendingOrders(@Path("orderId") int orderId, Callback<ArrayList<User>> cb);

    @PATCH("/users/{userId}/orders/{orderId}")
    void cancelOrder(@Body CommonOrder cancelOrder,@Path("userId") int userId,@Path("orderId") int orderId, Callback<Order> callback);

}
