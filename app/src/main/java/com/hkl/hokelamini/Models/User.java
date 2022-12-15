package com.hkl.hokelamini.Models;

public class User {

    long id;
    String name;
    String phone_number;
    String country;
    String id_number;
    String type;
    String dob;
    String email;
    String password;

    public User(){

    }

    public User(long id, String name, String phone_number, String country, String id_number, String type, String dob, String email, String password) {
        this.id = id;
        this.name = name;
        this.phone_number = phone_number;
        this.country = country;
        this.id_number = id_number;
        this.type = type;
        this.dob = dob;
        this.email = email;
        this.password = password;
    }

    public User(String name, String phone_number, String country, String id_number, String type, String dob, String email, String password) {
        this.name = name;
        this.phone_number = phone_number;
        this.country = country;
        this.id_number = id_number;
        this.type = type;
        this.dob = dob;
        this.email = email;
        this.password = password;
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

    public String getPhone_number() {
        return phone_number;
    }

    public void setPhone_number(String phone_number) {
        this.phone_number = phone_number;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getId_number() {
        return id_number;
    }

    public void setId_number(String id_number) {
        this.id_number = id_number;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getDob() {
        return dob;
    }

    public void setDob(String dob) {
        this.dob = dob;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
