package com.example.facetok.Login_SignUp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.facetok.R;


public class SignUpMainClass extends AppCompatActivity implements View.OnClickListener {
    private Button buttonSignUpWIthNumber;
    private LinearLayout linearLayoutViewFacebook, linearLayoutViewGoogle;
    private String CODE;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up_main);

        getIdForAllVariables();

        CODE = getIntent().getStringExtra("EXTRA");
        buttonSignUpWIthNumber.setOnClickListener(this);
        linearLayoutViewGoogle.setOnClickListener(this);
        linearLayoutViewFacebook.setOnClickListener(this);
    }

    private void getIdForAllVariables() {
        buttonSignUpWIthNumber = findViewById(R.id.button_sign_up_option_phone_number_activity_sign_up_main);
        linearLayoutViewFacebook = findViewById(R.id.linear_layout_facebook_sign_up_activity_sign_up_main);
        linearLayoutViewGoogle = findViewById(R.id.linear_layout_google_sign_up_activity_sign_up_main);
    }

    @Override
    public void onClick(View v) {
        if (v == buttonSignUpWIthNumber) {
            Intent intent = new Intent(SignUpMainClass.this, PhoneVerificationMainClass.class);
            intent.putExtra("EXTRA",CODE);
            startActivity(intent);
        } else if (v == linearLayoutViewFacebook) {
            Toast.makeText(this, "We are still working on it ", Toast.LENGTH_SHORT).show();
        } else if (v == linearLayoutViewGoogle) {
            Toast.makeText(this, "We are still working on it ", Toast.LENGTH_SHORT).show();
        }
    }
}
