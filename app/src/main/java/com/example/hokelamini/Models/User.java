package com.example.hokelamini.Models;

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
}
