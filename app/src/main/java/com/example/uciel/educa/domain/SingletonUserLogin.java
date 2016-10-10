package com.example.uciel.educa.domain;

public class SingletonUserLogin {
    private static SingletonUserLogin instance = null;
    private String userName, userID;
    protected SingletonUserLogin() {
        this.userName = "Harcodeado";
        this.userID = "10";
    }
    public static SingletonUserLogin getInstance() {
        if(instance == null) {
            instance = new SingletonUserLogin();
        }
        return instance;
    }

    public void setUserLoginData(String un, String uID){
        this.userName = un;
        this.userID = uID;
    }

    public void setUserName(String un){
        this.userName = un;
    }

    public void setUserID(String uID){
        this.userID = uID;
    }

    public String  getUserName(){
        return this.userName;
    }

    public String getUserID(){
        return this.userID;
    }
}
