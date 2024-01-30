package com.example.hokelamini.Models;

public class Campaign {

    long id;
    String name;
    int code;
    String starting;
    String ending;
    int type;
    long owner;
    long project_id;
    String created_at;

    public Campaign(){

    }

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

    public Campaign(long id, String name, int code, String starting, String ending, int type, long owner, long project_id, String created_at) {
        this.id = id;
        this.name = name;
        this.code = code;
        this.starting = starting;
        this.ending = ending;
        this.type = type;
        this.owner = owner;
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

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getStarting() {
        return starting;
    }

    public void setStarting(String starting) {
        this.starting = starting;
    }

    public String getEnding() {
        return ending;
    }

    public void setEnding(String ending) {
        this.ending = ending;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public long getOwner() {
        return owner;
    }

    public void setOwner(long owner) {
        this.owner = owner;
    }

    public long getProject_id() {
        return project_id;
    }

    public void setProject_id(long project_id) {
        this.project_id = project_id;
    }
}
