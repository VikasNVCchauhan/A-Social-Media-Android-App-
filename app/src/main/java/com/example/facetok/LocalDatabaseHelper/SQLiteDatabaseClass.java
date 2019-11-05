package com.example.facetok.LocalDatabaseHelper;

public class SQLiteDatabaseClass {
    String userNameSQLite, userEmailSQLite, userPhoneNumberSQLite, userLogInStatusSQLite;

    public SQLiteDatabaseClass(String userNameSQLite, String userEmailSQLite, String userPhoneNumberSQLite, String userLogInStatusSQLite) {
        this.userNameSQLite = userNameSQLite;
        this.userEmailSQLite = userEmailSQLite;
        this.userPhoneNumberSQLite = userPhoneNumberSQLite;
        this.userLogInStatusSQLite = userLogInStatusSQLite;
    }

    public String getUserNameSQLite() {
        return userNameSQLite;
    }

    public String getUserEmailSQLite() {
        return userEmailSQLite;
    }

    public String getUserPhoneNumberSQLite() {
        return userPhoneNumberSQLite;
    }

    public String getUserLogInStatusSQLite() {
        return userLogInStatusSQLite;
    }

    public SQLiteDatabaseClass() {
    }
}
