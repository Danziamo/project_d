package com.mirsoft.easyfix.networking.api;

import com.mirsoft.easyfix.models.Comment;
import com.mirsoft.easyfix.models.Order;
import com.mirsoft.easyfix.models.PendingContractor;
import com.mirsoft.easyfix.models.Review;
import com.mirsoft.easyfix.models.User;
import com.mirsoft.easyfix.networking.models.ApproveMasterOrder;
import com.mirsoft.easyfix.networking.models.CommonOrder;
import com.mirsoft.easyfix.networking.models.NOrder;
import com.mirsoft.easyfix.networking.models.StatusOrder;
import com.squareup.okhttp.Call;

import java.util.ArrayList;
import java.util.Objects;

import retrofit.Callback;
import retrofit.http.Body;
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
    void getContractorRequests(@Path("userId") int userId,@Path("orderId") int orderId, Callback<ArrayList<PendingContractor> > callback);

    @GET("/users/{userId}/orders/{id}")
    void getById(@Path("userId") int userId,@Path("id") int id, Callback<Order> callback);

    @POST("/users/{userId}/orders/{orderId}/post_contractor_request")
    void postRequest(@Body Object object, @Path("userId") int userId, @Path("orderId") int orderId, Callback<Object> callback);

    @POST("/users/{userId}/orders")
    void createOrder(@Body NOrder order, @Path("userId") int userId, Callback<Order> callback);

    @POST("/users/{userId}/orders")
    void createCommonOrder(@Body CommonOrder order,@Path("userId") int userId, Callback<Order> callback);

    @PATCH("/users/{userId}/orders/{orderId}")
    void updateOrder(@Body CommonOrder updatedOrder, @Path("userId") int userId, @Path("orderId") int orderId, Callback<Order> cb);

    @PATCH("/users/{userId}/orders/{orderId}")
    void updateOrderStatus(@Body StatusOrder updatedOrder, @Path("userId") int userId, @Path("orderId") int orderId, Callback<Order> cb);

//    @GET("/list-pending-orders/{orderId}")
//    void getPendingOrders(@Path("orderId") int orderId, Callback<ArrayList<User>> cb);

    @PATCH("/users/{userId}/orders/{orderId}")
    void cancelOrder(@Body CommonOrder cancelOrder,@Path("userId") int userId,@Path("orderId") int orderId, Callback<Order> callback);

    @POST("/users/{userId}/orders/{orderId}/approve_contractor_request")
    void setMaster(@Body ApproveMasterOrder order, @Path("userId") int userId, @Path("orderId") int orderId, Callback<Object> callback);

    @GET("/orders/{orderId}/reviews")
    void getReviewsByOrderId(@Path("orderId") int orderId,Callback<ArrayList<Comment>> callback);

    @POST("/orders/{orderId}/reviews")
    void addComment(@Body Comment comment,@Path("orderId") int orderId,Callback<Object> callback);
}
