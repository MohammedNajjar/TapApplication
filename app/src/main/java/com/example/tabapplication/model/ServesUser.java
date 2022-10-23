package com.example.tabapplication.model;

public class ServesUser {
    int idNumber;
    String userName;
    String password;

    public ServesUser(String userName, int idNumber, String password) {
        this.userName = userName;
        this.idNumber = idNumber;
        this.password = password;
    }


    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public int getIdNumber() {
        return idNumber;
    }

    public void setIdNumber(int idNumber) {
        this.idNumber = idNumber;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

}
