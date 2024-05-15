package com.ashcollege.responses;
public class BasicResponse {
    private static final int DEFAULT_ID=-1;
    private boolean success;
    private int id;
    private Integer errorCode;
    private double balance;

    public BasicResponse(boolean success,int id, Integer errorCode) {
        this.success = success;
        this.id=id;
        this.errorCode = errorCode;
        this.balance=0;
    }

    public BasicResponse(){
        this.success=false;
        this.id=DEFAULT_ID;
    };

    public boolean updateSuccess(){
        boolean success = false;
        if (this.id>DEFAULT_ID){
            this.success=true;
        }
        return success;
    }

    public double getBalance() {
        return balance;
    }

    public void setBalance(double balance) {
        this.balance = balance;
    }

    public boolean getSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Integer getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(Integer errorCode) {
        this.errorCode = errorCode;
    }
}
