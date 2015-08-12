package com.mirsoft.easyfix.networking.models;

import com.google.gson.annotations.Expose;
import com.mirsoft.easyfix.common.OrderType;

public class StatusOrder {
    @Expose
    public OrderType status;
}
