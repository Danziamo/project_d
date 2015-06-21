package com.mirsoft.easyfixmaster.models;

import com.google.gson.annotations.Expose;
import com.mirsoft.easyfixmaster.R;

import java.io.Serializable;

public class Specialty implements Serializable {
    @Expose
    private int id;
    @Expose
    private String name;
    @Expose
    private String slug;
    @Expose
    private String description;

    private int drawable;

    public Specialty() {}

    public Specialty(int id, String name, String slug, String description) {
        this.id = id;
        this.name = name;
        this.slug = slug;
        this.description = description;
        switch (id % 4) {
            case 0:
                this.drawable = R.drawable.plumbing;
                break;
            case 1:
                this.drawable = R.drawable.electricity;
                break;
            case 2:
                this.drawable = R.drawable.decoration;
                break;
            case 3:
                this.drawable = R.drawable.repairing;
                break;
            default:
                this.drawable = R.mipmap.ic_launcher;
        }
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSlug() {
        return slug;
    }

    public void setSlug(String slug) {
        this.slug = slug;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getDrawable() {
        return drawable;
    }

    public void setDrawable(int drawable) {
        this.drawable = drawable;
    }
}
