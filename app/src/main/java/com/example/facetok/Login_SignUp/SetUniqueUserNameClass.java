package com.example.facetok.Login_SignUp;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.facetok.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class SetUniqueUserNameClass extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = "SetUniqueUserNameClass";
    private FirebaseFirestore firebaseFirestore;
    private String uniquePhoneNumber;
    private Button buttonCheckingAvailability;
    private EditText editTextEnterUniqueUserName;
    private String stringUniqueUserName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_unique_user_name_main);

        getIdForAllVariables();

        uniquePhoneNumber = getIntent().getStringExtra("PHONE");
        editTextEnterUniqueUserName.setText(uniquePhoneNumber + "@factok");

        firebaseFirestore = FirebaseFirestore.getInstance();

        buttonCheckingAvailability.setOnClickListener(this);
    }

    private void getIdForAllVariables() {
        buttonCheckingAvailability = findViewById(R.id.button_verify_checking_availability_activity_set_unique_user_name_main);
        editTextEnterUniqueUserName = findViewById(R.id.edit_text_unique_use_name_activity_set_unique_user_name_main);
    }

    @Override
    public void onClick(View v) {
        if (v == buttonCheckingAvailability) {
            if (!editTextEnterUniqueUserName.equals("")) {
                checkAvailability();
            } else {
                Toast.makeText(this, "Please Enter a unique User Id", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void checkAvailability() {
        stringUniqueUserName = editTextEnterUniqueUserName.getText().toString();
        firebaseFirestore.collection("UniqueUserId").document(stringUniqueUserName).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if (documentSnapshot.exists()) {
                    Toast.makeText(SetUniqueUserNameClass.this, "This User Name has already taken ", Toast.LENGTH_SHORT).show();
                } else {
                    AlertDialog.Builder builder = new AlertDialog.Builder(SetUniqueUserNameClass.this);
                    builder.setCancelable(false);
                    builder.setMessage(R.string.unique_username);
                    builder.setTitle("User Name");
                    builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            setDataToFirebaseDatabase(stringUniqueUserName);
                            Intent intent = new Intent(SetUniqueUserNameClass.this, LoginMainClass.class);
                            startActivity(intent);
                        }
                    });
                    builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });
                    builder.show();
                }
            }
        });
    }

    private void setDataToFirebaseDatabase(String stringUniqueUserName) {

        Map<String, String> uniqueUserDetail = new HashMap<>();
        Map<String, String> userProfileDetail = new HashMap<>();
        uniqueUserDetail.put("Phone", uniquePhoneNumber);
        userProfileDetail.put("UniqueName", stringUniqueUserName);
        firebaseFirestore.collection("UserUniqueId").document(stringUniqueUserName).set(uniqueUserDetail).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Log.d(TAG, "Data saved successfully in unique user name");
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.w(TAG, "Error occurred during data saving ");
            }
        });
        firebaseFirestore.collection("UserData").document(uniquePhoneNumber).collection("UserProfileDetail").document("UserProfile").set(userProfileDetail).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {

            }
        }).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Log.d(TAG, "Data saved successfully in unique user name");
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.w(TAG, "Error occurred during data saving ");
            }
        });
    }

    @Override
    public void onBackPressed() {

    }
}
