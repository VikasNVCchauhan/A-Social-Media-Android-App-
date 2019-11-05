package com.example.facetok.Login_SignUp;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.facetok.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.database.DatabaseReference;

import java.util.concurrent.TimeUnit;

public class OtpVerificationMainClass extends AppCompatActivity implements View.OnClickListener {

    final String TAG = "OtpVerificationMainClass";
    private boolean flag = true;
    private int time = 20;
    private FirebaseAuth firebaseAuth;
    private ProgressDialog progressDialog;
    private String CODE;
    private String OTP;
    private int count = 2;
    private String uniquePhoneNumber;
    private Button buttonVerifyOtp;
    private TextView textViewResendOtp;
    private EditText editTextOTP;
    private String EXTRA;
    private DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verify_otp_main);

        getIdForVariable();

        //------This following code is for stopping resending otp for 60s
        stopResendingOTP();
        //------Here we are fetching data from previous activity
        CODE = getIntent().getStringExtra("CODE");
        EXTRA = getIntent().getStringExtra("EXTRA");
        uniquePhoneNumber = getIntent().getStringExtra("PHONE");
        firebaseAuth = FirebaseAuth.getInstance();

        buttonVerifyOtp.setOnClickListener(this);
        textViewResendOtp.setOnClickListener(this);
    }

    private void stopResendingOTP() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                time = 0;
            }
        }, 20000);
    }

    private void getIdForVariable() {
        buttonVerifyOtp = findViewById(R.id.button_verify_otp_activity_verify_otp_main);
        textViewResendOtp = findViewById(R.id.text_view_resend_otp_activity_verify_otp_main);
        editTextOTP = findViewById(R.id.edit_text_phone_number_activity_verify_otp_main);
    }

    @Override
    public void onBackPressed() {
        count--;
        if (count == 0) {
            super.onBackPressed();
            return;
        }
        Toast.makeText(this, "Click Once Again back to go back", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onClick(View v) {
        if (v == buttonVerifyOtp) {
            OTP = editTextOTP.getText().toString();
            if (OTP.equals("")) {
                Toast.makeText(this, "Enter the mobile number ", Toast.LENGTH_SHORT).show();
                return;
            } else if (OTP.length() < 6 && OTP.length() > 6) {
                Toast.makeText(this, "Enter a valid phone number ", Toast.LENGTH_SHORT).show();
                return;
            }
            checkingInternetConnectivityStatic();
            verifyOTP();
        } else if (v == textViewResendOtp) {
            reSendOTP();
        }
    }

    private void reSendOTP() {
        if (time != 0) {
            Toast.makeText(this, "Retry for new OTP after 20s", Toast.LENGTH_SHORT).show();
            return;
        }
        stopResendingOTP();
        checkingInternetConnectivityStatic();
        time = 20;
        getNewOTP();
    }

    private void getNewOTP() {
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Please wait we'r re-sending OTP ");
        progressDialog.setCancelable(false);
        progressDialog.show();
        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                uniquePhoneNumber,
                60,
                TimeUnit.SECONDS,
                this,
                new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
                    @SuppressLint("LongLogTag")
                    @Override
                    public void onVerificationCompleted(PhoneAuthCredential phoneAuthCredential) {
                        Log.d(TAG, "OTP has been sent your phone number ");
                        progressDialog.dismiss();
                    }

                    @Override
                    public void onVerificationFailed(FirebaseException e) {
                        if (e instanceof FirebaseAuthInvalidCredentialsException) {
                            Toast.makeText(OtpVerificationMainClass.this, "Invalid Credential " + e.getMessage(), Toast.LENGTH_SHORT).show();
                            progressDialog.dismiss();
                            return;
                        } else if (e instanceof FirebaseAuthInvalidCredentialsException) {
                            Toast.makeText(OtpVerificationMainClass.this, "Daily SMS Quota exceeded " + e.getMessage(), Toast.LENGTH_SHORT).show();
                            progressDialog.dismiss();
                            return;
                        }
                    }

                    @Override
                    public void onCodeSent(final String s, PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                        super.onCodeSent(s, forceResendingToken);

                        CountDownTimer countDownTimer = new CountDownTimer(2000, 100) {
                            @Override
                            public void onTick(long millisUntilFinished) {

                            }

                            @Override
                            public void onFinish() {
                                CODE = s;
                                stopRunMethod();
                                Toast.makeText(OtpVerificationMainClass.this, "OTP has been sent", Toast.LENGTH_SHORT).show();
                                progressDialog.dismiss();
                            }
                        };
                        countDownTimer.start();
                    }
                }
        );
    }

    private void checkingInternetConnectivityStatic() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                while (flag) {
                    flag = false;
                    Toast.makeText(OtpVerificationMainClass.this, "Please Check Your Internet Connection", Toast.LENGTH_SHORT).show();
                    progressDialog.dismiss();
                }
                flag = true;
            }
        }, 10000);
    }

    private void verifyOTP() {
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Please wait Verifying OTP.... ");
        progressDialog.setCancelable(false);
        progressDialog.show();

        System.out.println("\n\nthis is otp : " + OTP + "\n\nthis is the code : " + CODE);

        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(CODE, OTP);
        firebaseAuth.signInWithCredential(credential).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    CountDownTimer countDownTimer = new CountDownTimer(2000, 100) {
                        @Override
                        public void onTick(long millisUntilFinished) {

                        }

                        @SuppressLint("LongLogTag")
                        @Override
                        public void onFinish() {
                            progressDialog.dismiss();
                            Log.d(TAG, "OTP verification Completed");
                            Intent intent = new Intent(OtpVerificationMainClass.this, PasswordSettingMain.class);
                            intent.putExtra("PHONE", uniquePhoneNumber);
                            intent.putExtra("EXTRA", EXTRA);
                            stopRunMethod();
                            startActivity(intent);
                        }
                    };
                    countDownTimer.start();
                } else {
                    if (task.getException() instanceof FirebaseAuthInvalidCredentialsException) {
                        progressDialog.dismiss();
                        Toast.makeText(OtpVerificationMainClass.this, "Incorrect Verification", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
    }

    private void stopRunMethod() {
        flag = false;
    }
}
