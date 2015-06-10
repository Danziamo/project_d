package com.mirsoft.easyfixmaster.models;

import com.google.gson.annotations.Expose;

public class Order {

    @Expose
    private int id;
    private User master;
    @Expose
    private double latitude;
    @Expose
    private double longitude;
    @Expose
    private String address;
    @Expose
    private String phone;
    @Expose
    private String description;
    @Expose
    private String type;
}