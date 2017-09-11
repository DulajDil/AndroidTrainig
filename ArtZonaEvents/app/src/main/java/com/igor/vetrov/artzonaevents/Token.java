package com.igor.vetrov.artzonaevents;



public class Token {

    private String token;
    private final int id = 1;


    public String getToken() {
        return token;
    }

    public Token setToken(String token) {
        this.token = token;
        return this;
    }

    public int getId() {
        return id;
    }
}
