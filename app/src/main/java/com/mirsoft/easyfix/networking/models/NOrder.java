package com.mirsoft.easyfix.networking.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class NOrder {
    @Expose
    public String address;
    @Expose
    public double latitude;
    @Expose
    public double longitude;
    @Expose
    public int contractor;
    @Expose
    public String description;

    @Expose
    @SerializedName("rating__gte")
    public float rating;

    @Expose
    public String specialty;
}
