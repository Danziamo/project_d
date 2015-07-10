package com.mirsoft.easyfix.models;

public class Review {
    private int id;
    private String author;
    private float rating;
    private String description;

    public Review() {}

    public Review(int id, String author, float rating, String description) {
        this.id = id;
        this.author = author;
        this.rating = rating;
        this.description = description;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
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
