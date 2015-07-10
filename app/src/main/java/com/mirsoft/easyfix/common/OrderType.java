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
    FINISHED;

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
        }
        return null;
    }
}
