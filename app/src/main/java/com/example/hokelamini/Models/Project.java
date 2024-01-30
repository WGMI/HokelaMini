package com.example.hokelamini.Models;

public class Project {

    long id;
    String name;
    long owner;
    String created_at;

    public Project(long id, String name, long owner, String created_at) {
        this.id = id;
        this.name = name;
        this.owner = owner;
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

    public long getOwner() {
        return owner;
    }

    public void setOwner(long owner) {
        this.owner = owner;
    }

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }
}
