package com.igor.vetrov.artzonaevents;



public class Token {

    private String token;
    private int id = 1;
    private boolean expiredToken;


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

    public Token setId(int id) {
        this.id = id;
        return this;
    }


    public boolean isExpiredToken() {
        return expiredToken;
    }

    public Token setExpiredToken(boolean expiredToken) {
        this.expiredToken = expiredToken;
        return this;
    }
}
