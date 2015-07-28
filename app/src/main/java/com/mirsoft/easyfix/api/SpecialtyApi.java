package com.mirsoft.easyfix.api;

import com.mirsoft.easyfix.models.Specialty;

import java.util.ArrayList;

import retrofit.Callback;
import retrofit.http.GET;

public interface SpecialtyApi {
    @GET("/specialty")
    void getSpecialties(Callback<ArrayList<Specialty> > callback);
}
