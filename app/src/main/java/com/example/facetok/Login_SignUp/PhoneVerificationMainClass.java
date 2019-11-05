package com.example.facetok.Login_SignUp;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.facetok.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.hbb20.CountryCodePicker;

import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

public class PhoneVerificationMainClass extends AppCompatActivity implements View.OnClickListener {

    private boolean flag = true;
    private CountryCodePicker ccp;
    private FirebaseFirestore firebaseFirestore;
    private ProgressDialog progressDialog;
    private String CODE;
    private DatabaseReference databaseReference;
    private Button buttonVerifyPhoneNumber;
    private TextView textViewLogin;
    private EditText editTextPhoneNumber;
    private String stringUniquePhoneNumber;
    private String EXTRA;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_phone_number_signup_main);

        getIdForAllVariables();
        ccp.registerCarrierNumberEditText(editTextPhoneNumber);

        EXTRA = getIntent().getStringExtra("EXTRA");

        firebaseFirestore = FirebaseFirestore.getInstance();

        buttonVerifyPhoneNumber.setOnClickListener(this);
        textViewLogin.setOnClickListener(this);
    }

    private void getIdForAllVariables() {
        buttonVerifyPhoneNumber = findViewById(R.id.button_verify_phone_number_activity_phone_number_signup_main);
        textViewLogin = findViewById(R.id.text_view_login_here_activity_phone_number_verify);
        editTextPhoneNumber = findViewById(R.id.edit_text_phone_number_activity_phone_number_sign_up_main);
        ccp = findViewById(R.id.ccp);
    }

    @Override
    public void onClick(View v) {
        if (v == buttonVerifyPhoneNumber) {

            stringUniquePhoneNumber = ccp.getFullNumberWithPlus();
            if (stringUniquePhoneNumber.equals("")) {
                Toast.makeText(this, "Enter the mobile number ", Toast.LENGTH_SHORT).show();
                return;
            } else if (stringUniquePhoneNumber.length() < 10) {
                Toast.makeText(this, "Enter a valid phone number ", Toast.LENGTH_SHORT).show();
                return;
            }
            checkUserExistence(stringUniquePhoneNumber);

        } else if (v == textViewLogin) {
            Intent intent = new Intent(PhoneVerificationMainClass.this, LoginMainClass.class);
            startActivity(intent);
        }
    }

    private void checkUserExistence(final String stringUniquePhoneNumber) {

        progressDialog = new ProgressDialog(PhoneVerificationMainClass.this);
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Verifying Your Phone Number .... ");
        progressDialog.show();

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                while (flag) {
                    flag = false;
                    Toast.makeText(PhoneVerificationMainClass.this, "Please Check Your Internet Connection", Toast.LENGTH_SHORT).show();
                    progressDialog.dismiss();
                }
                flag = true;
            }
        }, 10000);

        checkingUserOnDatabase();
    }

    private void checkingUserOnDatabase() {
        firebaseFirestore.collection("UserData").document(stringUniquePhoneNumber).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot documentSnapshot = task.getResult();
                    if (documentSnapshot.exists() && documentSnapshot == null) {
                        if (EXTRA.equals("0")) {
                            progressDialog.dismiss();
                            Toast.makeText(PhoneVerificationMainClass.this, "User Doesn't exist", Toast.LENGTH_SHORT).show();
                            return;
                        } else {
                            generateOTP(stringUniquePhoneNumber);
                        }
                    } else {
                        if (!EXTRA.equals("0")) {      //Forgot password problem
                            generateOTP(stringUniquePhoneNumber);
                        } else {
                            progressDialog.dismiss();
                            Toast.makeText(PhoneVerificationMainClass.this, "This number is already registered", Toast.LENGTH_SHORT).show();
                            return;
                        }
                        return;
                    }
                } else {
                    Toast.makeText(PhoneVerificationMainClass.this, "There is some problem in checking user", Toast.LENGTH_SHORT).show();
                    progressDialog.dismiss();
                    return;
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                progressDialog.dismiss();
                Toast.makeText(PhoneVerificationMainClass.this, "There is something wrong " + e.getMessage(), Toast.LENGTH_SHORT).show();
                return;
            }
        });
    }

    private void generateOTP(final String stringUniquePhoneNumber) {

        PhoneAuthProvider.getInstance().verifyPhoneNumber(

                stringUniquePhoneNumber,        // Phone number to verify
                60,                 // Timeout duration
                TimeUnit.SECONDS,   // Unit of timeout
                this, new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

                    @Override
                    public void onVerificationCompleted(PhoneAuthCredential phoneAuthCredential) {
                        Toast.makeText(PhoneVerificationMainClass.this, "OTP sent ", Toast.LENGTH_SHORT).show();

                    }

                    @Override
                    public void onVerificationFailed(FirebaseException e) {
                        if (e instanceof FirebaseAuthInvalidCredentialsException) {
                            flag = false;
                            Toast.makeText(PhoneVerificationMainClass.this, "SMS Quota exceeded." + e.getMessage(), Toast.LENGTH_SHORT).show();
                            progressDialog.dismiss();
                        } else if (e instanceof FirebaseAuthInvalidCredentialsException) {
                            flag = false;
                            Toast.makeText(PhoneVerificationMainClass.this, "Invalid credential", Toast.LENGTH_SHORT).show();
                            progressDialog.dismiss();
                        }
                    }

                    @Override
                    public void onCodeSent(String s, PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                        super.onCodeSent(s, forceResendingToken);
                        CODE = s;

                        progressDialog.setMessage("Sending OTP To your Number....");

                        CountDownTimer countDownTimer = new CountDownTimer(2000, 100) {
                            @Override
                            public void onTick(long millisUntilFinished) {

                            }

                            @Override
                            public void onFinish() {
                                Intent intent = new Intent(PhoneVerificationMainClass.this, OtpVerificationMainClass.class);
                                intent.putExtra("CODE", CODE);
                                intent.putExtra("EXTRA", EXTRA);
                                intent.putExtra("PHONE", stringUniquePhoneNumber);
                                stopRunMethod();
                                progressDialog.dismiss();
                                startActivity(intent);
                                finish();
                            }
                        };
                        countDownTimer.start();
                    }
                });
    }

    private void stopRunMethod() {
        flag = false;
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(this, LoginMainClass.class);
        startActivity(intent);
    }
}