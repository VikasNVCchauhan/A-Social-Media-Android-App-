package com.example.facetok.HelperPackeg;


import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Toast;

public class Audit extends AppCompatActivity {

    private static String loginStatus;
    private static SQLiteDatabase sqLiteDatabase;
    private static String uniquePhoneNumber;
    private static onActivityStart activityStart;
    private final static String LOGIN_STATUS = "YES";
    private final static String TAG = "VideoStreamMainClass";



    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        sqLiteDatabase = openOrCreateDatabase("FacTok", MODE_PRIVATE, null);
        sqLiteDatabase.execSQL("Create table if not exists UserDataSQlite (UserName varchar,ImageUrl varchar,Email varchar,Phone varchar,Password varchar,userLogInStatusSQLite varchar);");

    }
//    public String checkLoginStatus() {
//        Cursor cursorLoginStatus = sqLiteDatabase.rawQuery("Select * from UserDataSQlite ", null);
//        if (cursorLoginStatus.getCount() != 0) {
//            cursorLoginStatus.moveToFirst();
//            for (int i = 0; i < cursorLoginStatus.getCount(); i++) {
//                loginStatus = cursorLoginStatus.getString(5);
//                if (loginStatus.equals(LOGIN_STATUS)) {
//                    loginStatus = cursorLoginStatus.getString(5);
//                    uniquePhoneNumber = cursorLoginStatus.getString(3);
//                    activityStart.oncheckStatus(loginStatus,uniquePhoneNumber);
//                    break;
//                }
//            }
//            Log.d(TAG, "Login Status is : " + loginStatus);
//        } else {
//            Toast.makeText(this, "No data is here ", Toast.LENGTH_SHORT).show();
//            Log.d(TAG, "No Data in the SQLite Database");
//        }
//        return loginStatus+" "+uniquePhoneNumber;
//    }
    public static interface  onActivityStart{
        public void oncheckStatus(String s,String d);
    }
    public static void checkLoginStatus(onActivityStart onActivityStart){
        activityStart=onActivityStart;

            Cursor cursorLoginStatus = sqLiteDatabase.rawQuery("Select * from UserDataSQlite ", null);
            if (cursorLoginStatus.getCount() != 0) {
                cursorLoginStatus.moveToFirst();
                for (int i = 0; i < cursorLoginStatus.getCount(); i++) {
                    loginStatus = cursorLoginStatus.getString(5);
                    if (loginStatus.equals(LOGIN_STATUS)) {
                        loginStatus = cursorLoginStatus.getString(5);
                        uniquePhoneNumber = cursorLoginStatus.getString(3);
                        onActivityStart.oncheckStatus(loginStatus,uniquePhoneNumber);
                        break;
                    }
                }
                Log.d(TAG, "Login Status is : " + loginStatus);
            } else {
                Log.d(TAG, "No Data in the SQLite Database");
            }

    }
}
