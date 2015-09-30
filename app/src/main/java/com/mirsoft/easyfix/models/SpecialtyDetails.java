package com.mirsoft.easyfix.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by parviz on 8/17/15.
 */
public class SpecialtyDetails implements Serializable{

    @Expose
    private int id;

    @Expose
    private boolean is_certified;

    @Expose
    private String certificate;

    @Expose
    private String certificate_detail;

    @Expose
    @SerializedName("user")
    private int userId;

    @Expose
    @SerializedName("specialty")
    private int specialtyId;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public boolean is_certified() {
        return is_certified;
    }

    public void setIs_certified(boolean is_certified) {
        this.is_certified = is_certified;
    }

    public String getCertificate() {
        return certificate;
    }

    public void setCertificate(String certificate) {
        this.certificate = certificate;
    }

    public String getCertificate_detail() {
        return certificate_detail;
    }

    public void setCertificate_detail(String certificate_detail) {
        this.certificate_detail = certificate_detail;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getSpecialtyId() {
        return specialtyId;
    }

    public void setSpecialtyId(int specialtyId) {
        this.specialtyId = specialtyId;
    }
}
