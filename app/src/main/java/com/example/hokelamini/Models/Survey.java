package com.example.hokelamini.Models;

public class Survey {

    long id;
    String name;
    String slug;
    long project_id;
    String created_at;

    public Survey(long id, String name, String slug, long project_id, String created_at) {
        this.id = id;
        this.name = name;
        this.slug = slug;
        this.project_id = project_id;
        this.created_at = created_at;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
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

    public long getProject_id() {
        return project_id;
    }

    public void setProject_id(long project_id) {
        this.project_id = project_id;
    }

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }
}
