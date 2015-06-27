package com.mirsoft.easyfixmaster.models;

import com.google.android.gms.maps.model.LatLng;
import com.google.gson.annotations.Expose;
import com.mirsoft.easyfixmaster.common.OrderType;

import java.io.Serializable;

public class Order implements Serializable {

    @Expose
    private int id;
    @Expose
    private User client;
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
    private OrderType status;

    @Expose
    private Specialty specialty;

    public Order() {
        this.status = OrderType.NEW;
    }
    public Order(int id, String address, String description, Specialty specialty) {
        this.id = id;
        this.address = address;
        this.description = description;
        this.specialty = specialty;
        this.status = OrderType.NEW;
    }

    public Order(int id, String phone, String address, String description, Specialty specialty, double latitude, double longitude) {
        this.id = id;
        this.address = address;
        this.description = description;
        this.specialty = specialty;
        this.status = OrderType.NEW;
        this.latitude = latitude;
        this.longitude = longitude;
        this.phone = phone;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public User getMaster() {
        return client;
    }

    public void setMaster(User master) {
        this.client = master;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public OrderType getStatus() {
        return status;
    }

    public void setStatus(OrderType status) {
        this.status = status;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public Specialty getSpecialty() {
        return specialty;
    }

    public void setSpecialty(Specialty specialty) {
        this.specialty = specialty;

    }

    public LatLng getLatLng() {
        if (latitude == 0 || longitude == 0) return null;
        return new LatLng(this.latitude, this.longitude);
    }

    @Override
    public String toString() {
        return "Order{" +
                "id=" + id +
                ", client=" + client +
                ", latitude=" + latitude +
                ", longitude=" + longitude +
                ", address='" + address + '\'' +
                ", phone='" + phone + '\'' +
                ", description='" + description + '\'' +
                ", status=" + status +
                ", specialty=" + specialty +
                '}';
    }
}