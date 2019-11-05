package com.example.facetok.Login_SignUp;

import android.app.ProgressDialog;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.facetok.Home.Home_Main;
import com.example.facetok.MainActivity;
import com.example.facetok.R;
import com.example.facetok.Video_Streaming_Tab.VideoStreamMainClass;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.hbb20.CountryCodePicker;

import java.util.List;

import javax.annotation.Nullable;

public class LoginMainClass extends AppCompatActivity implements View.OnClickListener {

    //Widgets
    private Button buttonLogin;
    private EditText editTextUserNameContact, editTextPassword;
    private TextView textViewSignUp, textViewForgottenPassword, textViewClose;
    private ImageView imageViewLogInFacebook, imageViewLoginGoogle, imageViewClose, imageViewSkip;
    private CountryCodePicker ccp;
    private SQLiteDatabase sqLiteDatabase;
    private ProgressDialog progressDialog;
    private DocumentReference documentReference;
    private FirebaseFirestore firebaseFirestore;
    //Variables
    private int COUNTER = 5;
    private boolean flag = true;
    private String passwordString, phoneNumberString;
    private String firebaseUserPhoneNumber, firebaseUserPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_main);

        getIdForVariables();

        firebaseFirestore = FirebaseFirestore.getInstance();

        ccp.registerCarrierNumberEditText(editTextUserNameContact);
        sqLiteDatabase = openOrCreateDatabase("FacTok", MODE_PRIVATE, null);
        sqLiteDatabase.execSQL("Create table if not exists UserDataSQlite (UserName varchar,ImageUrl varchar,Email varchar,Phone varchar,Password varchar,userLogInStatusSQLite varchar);");

        buttonLogin.setOnClickListener(this);
        textViewSignUp.setOnClickListener(this);
        textViewForgottenPassword.setOnClickListener(this);
        imageViewLoginGoogle.setOnClickListener(this);
        imageViewLogInFacebook.setOnClickListener(this);

        imageViewClose.setOnClickListener(this);
        textViewClose.setOnClickListener(this);
        imageViewSkip.setOnClickListener(this);

    }

    private void getIdForVariables() {

        buttonLogin = findViewById(R.id.button_login_login_main_activity);
        editTextPassword = findViewById(R.id.edit_text_password_login_main);
        editTextUserNameContact = findViewById(R.id.edit_text_contact_login_main);
        textViewForgottenPassword = findViewById(R.id.text_view_forgotten_password_login_main_activity);
        textViewSignUp = findViewById(R.id.text_view_sign_up_activity_login_main);
        imageViewLogInFacebook = findViewById(R.id.image_view_facebook_login_option_activity_login_main);
        imageViewLoginGoogle = findViewById(R.id.image_view_google_login_option_activity_login_main);
        ccp = findViewById(R.id.country_code_picker_login_activity);

        imageViewClose = findViewById(R.id.image_view_close_login_activity_login_main);
        imageViewSkip = findViewById(R.id.image_view_skip_login_activity_login_main);
        textViewClose = findViewById(R.id.text_view_skip_login_activity_login_main);

    }

    @Override
    public void onClick(View v) {
        if (v == buttonLogin) {
            checkingInternetConnectivityStatic();
            getLoggedIn();
        } else if (v == textViewForgottenPassword) {
            Intent intent = new Intent(LoginMainClass.this, PhoneVerificationMainClass.class);
            intent.putExtra("EXTRA", "0");
            startActivity(intent);
        } else if (v == textViewSignUp) {
            Intent intent = new Intent(LoginMainClass.this, SignUpMainClass.class);
            intent.putExtra("EXTRA", "1");
            startActivity(intent);
        } else if (v == imageViewLogInFacebook) {
            Toast.makeText(this, "We are still working on it...", Toast.LENGTH_SHORT).show();
        } else if (v == imageViewLoginGoogle) {
            Toast.makeText(this, "We are still working on it...", Toast.LENGTH_SHORT).show();
        } else if (v == imageViewClose || v == imageViewSkip || v == textViewClose) {
            startVideoStreamMainFragment();
        }
    }

    private void checkingInternetConnectivityStatic() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (flag) {
                    progressDialog.dismiss();
                    Toast.makeText(LoginMainClass.this, "Please check your Internet connection", Toast.LENGTH_SHORT).show();
                }
            }
        }, 15000);
    }

    private void getLoggedIn() {

        passwordString = editTextPassword.getText().toString();
        phoneNumberString = ccp.getFullNumberWithPlus();

        if (passwordString.equals("") || phoneNumberString.equals("")) {
            Toast.makeText(this, "Password or User Name Field can't be empty", Toast.LENGTH_SHORT).show();
            return;
        } else if (phoneNumberString.length() < 10) {
            Toast.makeText(this, "Please enter a veiled Phone Number", Toast.LENGTH_SHORT).show();
            return;
        } else if (passwordString.length() < 8) {
            Toast.makeText(this, "Please Enter a veiled password", Toast.LENGTH_SHORT).show();
            return;
        }
        getDataFromFirebase(phoneNumberString);
    }

    private void getDataFromFirebase(String phoneNumberString) {

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Please Wait Login in progress...");
        progressDialog.setCancelable(false);
        progressDialog.show();

        firebaseFirestore.collection("UserData").document(phoneNumberString).collection("UserLoginDetail").document("Password").get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {

                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {

                        progressDialog.dismiss();

                        firebaseUserPhoneNumber = document.getString("PhoneNumber");
                        firebaseUserPassword = document.getString("Password");

                        if (passwordString.equals(firebaseUserPassword)) {
                            checkUserInSQLiteDatabase();
                            flag = false;
                            Toast.makeText(LoginMainClass.this, "You are logged in Successfully", Toast.LENGTH_SHORT).show();
                            progressDialog.dismiss();
                            startVideoStreamMainFragment();
                        } else {
                            progressDialog.dismiss();
                            waiting();
                        }
                    } else {
                        Toast.makeText(LoginMainClass.this, "Account not exists ", Toast.LENGTH_SHORT).show();
                        progressDialog.dismiss();
                    }
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(LoginMainClass.this, "There is some problem " + e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void checkUserInSQLiteDatabase() {
        Cursor cr = sqLiteDatabase.rawQuery("Select * from UserDataSQlite where Phone='" + firebaseUserPhoneNumber + "'", null);
        if (cr.getCount() != 0) {
            upDateDataInSQLiteDatabase();
        } else {
            setSQLData(firebaseUserPassword, firebaseUserPhoneNumber);
        }
    }

    private void startVideoStreamMainFragment() {

        setContentView(R.layout.fragment_gloable_container_main);    //This must be there with Fragment manager otherwise it will not be able to know what is fragment_container_global_container
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container_global_container, new VideoStreamMainClass(firebaseUserPhoneNumber)).commit();

    }

    private void setSQLData(String firebaseUserPassword, String firebaseUserName) {
        Log.d("setSDLData", "Password : " + firebaseUserPassword);
        sqLiteDatabase.execSQL("insert into UserDataSQlite values ('" + 0 + "','" + 0 + "','" + 0 + "','" + firebaseUserName + "','" + firebaseUserPassword + "','" + "YES" + "')");
    }

    private void upDateDataInSQLiteDatabase() {
        sqLiteDatabase.execSQL("update UserDataSQlite set userLogInStatusSQLite='" + "YES" + "' where Phone='" + firebaseUserPhoneNumber + "'");
    }

    private void waiting() {
        if (COUNTER == 0) {
            countDown();
        } else {
            COUNTER--;
            if (COUNTER == 2) {
                Toast.makeText(LoginMainClass.this, "Are you forgotten your password then recover it by clicking on forgotten", Toast.LENGTH_SHORT).show();
                return;
            }
            if (COUNTER == 0) {
                Toast.makeText(LoginMainClass.this, "Now wait for 30 sec", Toast.LENGTH_SHORT).show();
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        COUNTER = 5;
                    }
                }, 30000);
                return;
            }
            Toast.makeText(LoginMainClass.this, "Please Enter the correct password", Toast.LENGTH_SHORT).show();
        }
    }

    private void countDown() {
    }

    @Override
    public void onBackPressed() {
        Toast.makeText(this, "Here This functionality is disabled", Toast.LENGTH_SHORT).show();
    }
}
