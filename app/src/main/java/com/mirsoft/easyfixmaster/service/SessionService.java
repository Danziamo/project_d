package com.mirsoft.easyfixmaster.service;

import com.mirsoft.easyfixmaster.api.SessionApi;

import javax.inject.Inject;
import javax.inject.Provider;

public class SessionService {

    private static final int UNAUTHORIZED = 401;
    private static final int SUCCESS = 200;

    @Inject
    Provider<SessionApi> sessionApiProvider;



}