package com.mirsoft.easyfix.networking;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.mirsoft.easyfix.Settings;
import com.mirsoft.easyfix.api.OrderApi;
import com.mirsoft.easyfix.api.SessionApi;
import com.mirsoft.easyfix.api.SessionRequestInterceptor;
import com.mirsoft.easyfix.api.SpecialtyApi;
import com.mirsoft.easyfix.api.UserApi;
import com.mirsoft.easyfix.models.Order;
import com.mirsoft.easyfix.models.User;
import com.squareup.okhttp.OkHttpClient;

import retrofit.RestAdapter;
import retrofit.client.OkClient;
import retrofit.converter.GsonConverter;

public class RestClient {
    private static String ROOT = "http://192.168.0.123:1337/api/v1";
    private static String MAIN_URI = "http://192.168.0.112:8000/api/v1";

    private RestClient() {
    }

    private static RestAdapter getRestAdapter(boolean isContractor) {
        Gson gson = new GsonBuilder()
                .setDateFormat("yyyy-MM-dd'T'HH:mm:ss")
                .excludeFieldsWithoutExposeAnnotation()
                .create();

        RestAdapter.Builder builder = new RestAdapter.Builder()
                .setEndpoint(ROOT)
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

    public static OrderApi getOrderService(boolean isConctractor) {
        return createService(OrderApi.class, isConctractor);
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
}
