package com.mirsoft.easyfix.models;

import com.google.gson.annotations.Expose;

/**
 * Created by parviz on 8/15/15.
 */
public class Comment {

    @Expose
    private int user;
    @Expose
    private float rating;
    @Expose
    private String description;

    public int getUser() {
        return user;
    }

    public void setUser(int user) {
        this.user = user;
    }

    public float getRating() {
        return rating;
    }

    public void setRating(float rating) {
        this.rating = rating;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
