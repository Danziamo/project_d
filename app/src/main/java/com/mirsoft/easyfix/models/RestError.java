package com.mirsoft.easyfix.models;

import com.google.gson.annotations.Expose;

public class RestError {

    @Expose
    private String code;
    @Expose
    private String message;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMsg() {
        return message;
    }

    public void setMsg(String msg) {
        this.message = msg;
    }

    @Override
    public String toString() {
        return "code='" + code + ": " +
                ", message='" + message;
    }
}
