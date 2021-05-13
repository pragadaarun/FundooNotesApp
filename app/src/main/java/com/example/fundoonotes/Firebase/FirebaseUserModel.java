package com.example.fundoonotes.Firebase;

public class FirebaseUserModel {

    private String userName;
    private String userEmail;

    public FirebaseUserModel(String userName, String userEmail) {
        this.userName = userName;
        this.userEmail = userEmail;
    }

    public FirebaseUserModel() {

    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }
}
