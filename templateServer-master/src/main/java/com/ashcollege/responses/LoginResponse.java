package com.ashcollege.responses;

public class LoginResponse extends BasicResponse {
    private int id;
    private String secret;

    public LoginResponse(boolean success, Integer errorCode, int id, String secret) {
        super(success, errorCode,id);
        this.secret = secret;
    }

    public LoginResponse(boolean success, Integer errorCode, int id) {
        super(success, errorCode,id);
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getSecret() {
        return secret;
    }

    public void setSecret(String secret) {
        this.secret = secret;
    }
}
