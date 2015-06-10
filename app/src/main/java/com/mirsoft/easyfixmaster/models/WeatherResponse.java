package com.mirsoft.easyfixmaster.models;

import com.google.gson.annotations.Expose;

public class WeatherResponse {
    @Expose
    private int cod;
    @Expose
    private String base;
    @Expose
    private Weather main;

    // default constructor, getters and setters
}