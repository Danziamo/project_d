package com.mirsoft.easyfix.common;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public enum OrderType implements Serializable {
    @SerializedName("new")
    NEW,
    @SerializedName("pending")
    PENDING,
    @SerializedName("active")
    ACTIVE,
    @SerializedName("finished")
    FINISHED,
    @SerializedName("cancelled")
    CANCELED;


    @Override
    public String toString(){
        switch (this) {
            case NEW:
                return "new";
            case PENDING:
                return "pending";
            case ACTIVE:
                return "active";
            case FINISHED:
                return "finished";
            case CANCELED:
                return "canceled";
        }
        return null;
    }
}
