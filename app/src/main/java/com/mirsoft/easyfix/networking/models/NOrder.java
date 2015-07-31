package com.mirsoft.easyfix.networking.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class NOrder extends CommonOrder {

    @Expose
    public int contractor;

    public int getContractor() {
        return contractor;
    }

    public void setContractor(int contractor) {
        this.contractor = contractor;
    }
}
