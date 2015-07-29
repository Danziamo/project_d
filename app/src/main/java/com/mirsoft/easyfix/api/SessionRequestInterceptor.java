package com.mirsoft.easyfix.api;

import com.mirsoft.easyfix.App;
import com.mirsoft.easyfix.Settings;

import retrofit.RequestInterceptor;

public class SessionRequestInterceptor implements RequestInterceptor{

    private Settings settings;
    private boolean isContractor;

    public SessionRequestInterceptor(boolean isContractor) {
        this.isContractor = isContractor;
        settings = new Settings(App.getContext());
    }

    public SessionRequestInterceptor() {
        this.isContractor = false;
    }

    @Override
    public void intercept(RequestFacade request) {
        String token = settings.getAccessToken();
        if (token.isEmpty()) return;
        request.addHeader("Authorization", "Token " + token);

        if (this.isContractor)
            request.addHeader("AUTHROLE", "218561869884004337978616805706827077109L");
        else
            request.addHeader("AUTHROLE", "110728838694634726063666881111550518935L");
    }
}
