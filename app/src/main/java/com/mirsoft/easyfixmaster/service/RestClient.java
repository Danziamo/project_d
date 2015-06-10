package com.mirsoft.easyfixmaster.service;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.mirsoft.easyfixmaster.Settings;
import com.mirsoft.easyfixmaster.api.OrderApi;
import com.mirsoft.easyfixmaster.api.SessionApi;
import com.mirsoft.easyfixmaster.api.SessionRequestInterceptor;
import com.mirsoft.easyfixmaster.api.UserApi;
import com.mirsoft.easyfixmaster.models.Session;
import com.squareup.okhttp.OkHttpClient;

import dagger.Module;
import dagger.Provides;
import retrofit.RequestInterceptor;
import retrofit.RestAdapter;
import retrofit.client.OkClient;
import retrofit.converter.GsonConverter;

@Module(
        injects = {
            SessionService.class
        },
        library = true,
        complete = false
)

public class RestClient {
    private static String ROOT = "http://api.openweathermap.org/data/2.5";
    private static String MAIN_URI = "http://81.88.192.37";

    @Provides
    public SessionApi provideSessionApi(Settings settings) {
        return getRestAdapter(settings).create(SessionApi.class);
    }

    @Provides
    public UserApi provideUserApi(Settings settings) {
        return getRestAdapter(settings).create(UserApi.class);
    }

    @Provides
    public OrderApi provideOrderApi(Settings settings) {
        return getRestAdapter(settings).create(OrderApi.class);
    }

    private RestAdapter getRestAdapter(Settings settings) {
        Gson gson = new GsonBuilder()
                .setDateFormat("yyyy-MM-dd'T'HH:mm:ss")
                .create();

        return new RestAdapter
                .Builder()
                .setEndpoint(MAIN_URI)
                .setRequestInterceptor(new SessionRequestInterceptor(settings))
                .setConverter(new GsonConverter(gson))
                .build();
    }
}
