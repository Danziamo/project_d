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
    private static String ROOT = "http://192.168.0.101:1337/api/v1";
    private static String MAIN_URI = "http://192.168.0.112:8000/api/v1";

    private ServiceGenerator() {
    }

    public static <S> S createService(Class<S> serviceClass, Settings settings) {
        Gson gson = new GsonBuilder()
                .setDateFormat("yyyy-MM-dd'T'HH:mm:ss")
                .excludeFieldsWithoutExposeAnnotation()
                .create();

        RestAdapter.Builder builder = new RestAdapter.Builder()
                .setEndpoint(MAIN_URI)
                .setConverter(new GsonConverter(gson))
                .setRequestInterceptor(new SessionRequestInterceptor(settings))
                .setLogLevel(RestAdapter.LogLevel.FULL)
                .setErrorHandler(new CustomErrorHandler())
                .setClient(new OkClient(new OkHttpClient()));

        RestAdapter adapter = builder.build();

        return adapter.create(serviceClass);
    }
}
