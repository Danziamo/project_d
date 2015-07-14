package com.mirsoft.easyfix.networking;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.mirsoft.easyfix.Settings;
import com.mirsoft.easyfix.api.OrderApi;
import com.mirsoft.easyfix.api.SessionApi;
import com.mirsoft.easyfix.api.SessionRequestInterceptor;
import com.mirsoft.easyfix.api.UserApi;
import com.mirsoft.easyfix.networking.service.SessionService;

import dagger.Module;
import dagger.Provides;
import retrofit.RestAdapter;
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
