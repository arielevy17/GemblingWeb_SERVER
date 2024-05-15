package com.ashcollege.entities;

public class User {
    private int id;
    private String name;
    private String password;
    private String email;
    private String secret;
    private College college;
    private double balance;



    public User(String username, String password,String email,double balance) {
        this.name = username;
        this.password = password;
        this.email=email;
        this.balance=balance;
    }

    public User() {

    }

    public User(int id,double balance){
        this.id=id;
        this.balance= balance;
    }

    public double getBalance() {
        return balance;
    }

    public void setBalance(double balance) {
        this.balance = balance;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public boolean isSameUsername (String username) {
        return this.name.equals(username);
    }

    public boolean isSameCreds (String username, String password) {
        return this.name.equals(username) && this.password.equals(password);
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getSecret() {
        return secret;
    }

    public void setSecret(String secret) {
        this.secret = secret;
    }

    public College getCollege() {
        return college;
    }

    public void setCollege(College college) {
        this.college = college;
    }
}
