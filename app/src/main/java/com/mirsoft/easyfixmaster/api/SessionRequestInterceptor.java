package com.mirsoft.easyfixmaster.api;

import com.mirsoft.easyfixmaster.Settings;

import retrofit.RequestInterceptor;

public class SessionRequestInterceptor implements RequestInterceptor{

    private Settings settings;

    public SessionRequestInterceptor(Settings settings) {
        this.settings = settings;
    }

    @Override
    public void intercept(RequestFacade request) {
        String token = settings.getAccessToken();
        if (token.isEmpty()) return;
        request.addHeader("Authorization", "Token " + token);
    }
}
