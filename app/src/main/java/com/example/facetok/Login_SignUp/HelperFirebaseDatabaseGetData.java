package com.example.facetok.Login_SignUp;

public class HelperFirebaseDatabaseGetData {
    private String stringPhoneNumber,stringPassword;

    public HelperFirebaseDatabaseGetData() {

    }

    public HelperFirebaseDatabaseGetData(String phoneNumber) {
        this.stringPhoneNumber = phoneNumber;
    }

    public String getPhoneNumber() {
        return stringPhoneNumber;
    }
}
