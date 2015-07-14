package com.mirsoft.easyfix.networking;

import java.net.HttpURLConnection;
import java.net.SocketTimeoutException;

import retrofit.ErrorHandler;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class CustomErrorHandler implements ErrorHandler {

    public CustomErrorHandler() {}

    @Override
    public Throwable handleError(RetrofitError cause) {
        String errorDescription = null;
        if (cause.getKind() == RetrofitError.Kind.NETWORK) {
            if (cause.getCause() instanceof SocketTimeoutException) {
                errorDescription = "Не удалось подключится к серверу";
            } else {
                errorDescription = "Проверьте интернет соединение";
            }
        } else if (cause.getKind() == RetrofitError.Kind.HTTP) {
            Response r = cause.getResponse();
            if (r == null) {
                errorDescription = "Нет ответа от сервера";
            } else if (r.getStatus() == HttpURLConnection.HTTP_UNAUTHORIZED) {
                errorDescription = "Не авторизированы";
            }
        }
        return new Exception(errorDescription);
    }
}
