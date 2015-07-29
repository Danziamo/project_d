package com.mirsoft.easyfix.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.mirsoft.easyfix.R;

import java.io.Serializable;

public class UserSpecialty implements Serializable {
    @Expose
    private int id;

    @Expose
    @SerializedName("is_certified")
    private boolean isCertified;

    @Expose
    @SerializedName("specialty_id")
    private int specialtyId;

    //@TODO remove if not used
    private String specialtyName;

    public UserSpecialty(){
        this.isCertified = false;
    }


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public boolean isCertified() {
        return isCertified;
    }

    public void setIsCertified(boolean isCertified) {
        this.isCertified = isCertified;
    }

    public int getSpecialtyId() {
        return specialtyId;
    }

    public void setSpecialtyId(int specialtyId) {
        this.specialtyId = specialtyId;
    }

    public String getSpecialtyName() {
        return specialtyName;
    }

    public void setSpecialtyName(String specialtyName) {
        this.specialtyName = specialtyName;
    }

    @Override
    public String toString() {
        return this.specialtyName;
    }
}
