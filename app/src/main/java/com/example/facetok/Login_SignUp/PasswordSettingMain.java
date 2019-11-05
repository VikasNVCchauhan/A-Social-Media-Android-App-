package com.example.facetok.Login_SignUp;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextClock;
import android.widget.TextView;
import android.widget.Toast;

import com.example.facetok.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;


public class PasswordSettingMain extends AppCompatActivity implements View.OnClickListener {

    private StorageReference mStorageReference;
    private StorageReference reference;
    private FirebaseFirestore firebaseFirestore;
    private EditText editTextEnterPassword, editTextReEnterPassword;
    private String stringEnterPassword;
    private Button buttonSavePassword;
    private TextView textViewLoginHere;
    private String stringUniquePhoneNumber;
    private String EXTRA;
    private ProgressDialog progressDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_reset_password_main);

        getIdForAllVariables();

        reference = FirebaseStorage.getInstance().getReference();
        mStorageReference = reference.child("UserLoginDetail");
        firebaseFirestore = FirebaseFirestore.getInstance();

        EXTRA = getIntent().getStringExtra("EXTRA");
        stringUniquePhoneNumber = getIntent().getStringExtra("PHONE");
        buttonSavePassword.setOnClickListener(this);
        textViewLoginHere.setOnClickListener(this);
    }

    private void getIdForAllVariables() {

        editTextEnterPassword = findViewById(R.id.edit_text_set_password_activity_set_reset_password_main);
//        editTextReEnterPassword = findViewById(R.id.edit_text_set_password_reenter_activity_set_reset_password_main);
        buttonSavePassword = findViewById(R.id.button_save_password_set_reset_password_main_activity);
        textViewLoginHere = findViewById(R.id.text_view_login_set_reset_password_activity_main);

    }

    @Override
    public void onClick(View v) {
        if (v == buttonSavePassword) {
            stringEnterPassword = editTextEnterPassword.getText().toString();

            if (stringEnterPassword.length() < 8) {
                Toast.makeText(this, "Password Must contain minimum 8 character", Toast.LENGTH_SHORT).show();
                return;
            }
            setPasswordToFirebase(stringEnterPassword);

        } else if (v == textViewLoginHere) {
            Intent intent = new Intent(PasswordSettingMain.this, LoginMainClass.class);
            startActivity(intent);
        }
    }

    private void setPasswordToFirebase(String stringEnterPassword) {
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Saving Password in progress....");
        progressDialog.setCancelable(false);
        progressDialog.show();
        Map<String, String> userLoginDetail = new HashMap<>();
        userLoginDetail.put("Password", stringEnterPassword);
        userLoginDetail.put("PhoneNumber", stringUniquePhoneNumber);

        firebaseFirestore.collection("UserData").document(stringUniquePhoneNumber).collection("UserLoginDetail").document("Password").set(userLoginDetail).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                progressDialog.dismiss();
                Toast.makeText(PasswordSettingMain.this, "Password Saved Successfully", Toast.LENGTH_SHORT).show();
                if (EXTRA.equals("0")) {
                    Intent intent = new Intent(PasswordSettingMain.this, LoginMainClass.class);
                    startActivity(intent);
                } else if (EXTRA.equals("1")) {
                    Intent intent = new Intent(PasswordSettingMain.this, SetUniqueUserNameClass.class);
                    intent.putExtra("PHONE",stringUniquePhoneNumber);
                    startActivity(intent);
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                progressDialog.dismiss();
                Toast.makeText(PasswordSettingMain.this, "Some issue during password saving"+e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

}
