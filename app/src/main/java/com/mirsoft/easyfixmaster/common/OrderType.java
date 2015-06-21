package com.mirsoft.easyfixmaster.common;

import android.os.Parcel;
import android.os.Parcelable;

public enum OrderType {
    NEW,
    PENDING,
    ACTIVE,
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
