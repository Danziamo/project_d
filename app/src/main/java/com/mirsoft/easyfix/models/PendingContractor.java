package com.mirsoft.easyfix.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class PendingContractor {
    @Expose
    private int id;
    @Expose
    private User contractor;
    @Expose
    @SerializedName("is_active")
    private boolean isActive;
    @SerializedName("order")
    private int orderId;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public User getContractor() {
        return contractor;
    }

    public void setContractor(User contractor) {
        this.contractor = contractor;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setIsActive(boolean isActive) {
        this.isActive = isActive;
    }

    public int getOrderId() {
        return orderId;
    }

    public void setOrderId(int orderId) {
        this.orderId = orderId;
    }

}
