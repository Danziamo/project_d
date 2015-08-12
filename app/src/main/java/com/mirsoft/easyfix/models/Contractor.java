package com.mirsoft.easyfix.models;

import com.google.gson.annotations.Expose;

public class Contractor {
    @Expose
    private User contractor;

    public void setContractor(User user) {
        this.contractor = user;
    }

    public User getContractor() {
        return this.contractor;
    }
}
