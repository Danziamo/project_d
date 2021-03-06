package com.mirsoft.easyfix.networking;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.mirsoft.easyfix.common.Constants;
import com.mirsoft.easyfix.networking.api.AccountRestoreApi;
import com.mirsoft.easyfix.networking.api.OrderApi;
import com.mirsoft.easyfix.networking.api.SessionApi;
import com.mirsoft.easyfix.networking.api.SessionRequestInterceptor;
import com.mirsoft.easyfix.networking.api.SpecialtyApi;
import com.mirsoft.easyfix.networking.api.UserApi;
import com.squareup.okhttp.OkHttpClient;

import retrofit.RestAdapter;
import retrofit.client.OkClient;
import retrofit.converter.GsonConverter;

public class RestClient {
    private RestClient() {
    }

    private static RestAdapter getRestAdapter(boolean isContractor) {
        Gson gson = new GsonBuilder()
                .setDateFormat("yyyy-MM-dd'T'HH:mm:ss")
                .excludeFieldsWithoutExposeAnnotation()
                .create();

        RestAdapter.Builder builder = new RestAdapter.Builder()
                .setEndpoint(Constants.ROOT)
                .setConverter(new GsonConverter(gson))
                .setRequestInterceptor(new SessionRequestInterceptor(isContractor))
                .setLogLevel(RestAdapter.LogLevel.FULL)
                //.setErrorHandler(new CustomErrorHandler())
                .setClient(new OkClient(new OkHttpClient()));

        return builder.build();
    }

    public static <S> S createService(Class<S> serviceClass) {
        RestAdapter adapter = getRestAdapter(false);
        return adapter.create(serviceClass);
    }

    public static <S> S createService(Class<S> serviceClass, boolean isContractor) {
        RestAdapter adapter = getRestAdapter(isContractor);
        return adapter.create(serviceClass);
    }

    public static OrderApi getOrderService(boolean isContractor) {
        return createService(OrderApi.class, isContractor);
    }

    public static UserApi getUserService(boolean isContractor) {
        return createService(UserApi.class, isContractor);
    }

    public static SessionApi getSessionApi(boolean isContractor) {
        return createService(SessionApi.class, isContractor);
    }

    public static SpecialtyApi getSpecialtyApi(boolean isContractor) {
        return createService(SpecialtyApi.class, isContractor);
    }

    public static AccountRestoreApi getAccountRestoreApi() {
        return createService(AccountRestoreApi.class, false);
    }
}
