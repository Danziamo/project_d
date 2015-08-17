package com.mirsoft.easyfix.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class User implements Serializable{
    @Expose
    private int id;
    @Expose
    private String phone;
    @Expose
    @SerializedName("first_name")
    private String firstName;
    @Expose
    @SerializedName("last_name")
    private String lastName;
    @Expose
    private String password;
    @Expose
    @SerializedName("date_of_birth")
    private String dateOfBirth;
    @Expose
    private String role;

    @Expose
    private float rating;
    @Expose
    private int reviewsCount;

    @Expose
    private String token;

    @Expose
    private boolean is_certified;

    @Expose
    private String certificate;

    @Expose
    private String certificate_detail;

    @Expose
    private String picture;

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

    public User() {
        this.role = "contractor";
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getFullName() {
        return this.lastName + " " + this.firstName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(String dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String type) {
        this.role = type;
    }

    @Override
    public int hashCode() {
        return System.identityHashCode(id);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        final User other = (User) obj;
        return this.id == other.id;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public float getRating() {
        return this.rating;
    }

    public void setRating(float rating) {
        this.rating = rating;
    }

    public int getReviewsCount() {
        return this.reviewsCount;
    }

    public void setReviewsCount(int reviewsCount) {
        this.reviewsCount = reviewsCount;
    }

    public String getPicture() {
        return picture;
    }

    public void setPicture(String picture) {
        this.picture = picture;
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", phone='" + phone + '\'' +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", password='" + password + '\'' +
                ", dateOfBirth='" + dateOfBirth + '\'' +
                ", role='" + role + '\'' +
                ", token='" + token + '\'' +
                '}';
    }
}
