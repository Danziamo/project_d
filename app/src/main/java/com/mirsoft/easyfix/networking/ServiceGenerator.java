package com.mirsoft.easyfix.networking;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.mirsoft.easyfix.Settings;
import com.mirsoft.easyfix.api.SessionRequestInterceptor;
import com.squareup.okhttp.OkHttpClient;

import retrofit.RestAdapter;
import retrofit.client.OkClient;
import retrofit.converter.GsonConverter;

public class ServiceGenerator {
    private static String ROOT = "http://192.168.0.108:1337/api/v1";
    private static String MAIN_URI = "http://192.168.0.112:8000/api/v1";

    private ServiceGenerator() {
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
}
