package com.mirsoft.easyfixmaster.common;

import android.os.Parcel;
import android.os.Parcelable;

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
