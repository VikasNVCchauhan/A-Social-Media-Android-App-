package com.example.facetok.UserPhotos;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.facetok.Profile.UserProfileMain;
import com.example.facetok.R;
import com.example.facetok.UploadImagesOption.UserModel;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class PhotosMainClass extends AppCompatActivity implements RecyclerViewAdapterUserImage.onItemClickListener {


    //Widgets
    private ProgressBar progressBarInitial;
    private CollectionReference collectionReference;
    private RecyclerView recyclerView;
    private RecyclerViewAdapterUserImage recyclerViewAdapterUserImage;
    //Database
    private SQLiteDatabase sqLiteDatabase;
    private FirebaseFirestore mFirebaseFirestore;
    //Variables
    private final static String LOGIN_STATUS = "YES";
    private String loginStatus;
    private String uniquePhoneNumber;
    private ArrayList<UserModel> mArrayList;
    private static final String TAG = "PhotoMainClass";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photos_main);

        getIdForWidgets();
        progressBarInitial.setVisibility(View.VISIBLE);
        sqLiteDatabase = openOrCreateDatabase("FacTok", MODE_PRIVATE, null);
        sqLiteDatabase.execSQL("Create table if not exists UserDataSQlite (UserName varchar,Email varchar,Phone varchar,Password varchar,userLogInStatusSQLite varchar);");
        checkLoginStatus();

        mArrayList = new ArrayList<>();
        mFirebaseFirestore = FirebaseFirestore.getInstance();
        collectionReference = mFirebaseFirestore.collection("UserData").document(uniquePhoneNumber).collection("Upload").document("ImageUpload").collection("Image");

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        loadDataFromFirebase();
    }

    private void loadDataFromFirebase() {
        collectionReference.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                if (queryDocumentSnapshots == null) {
                    Log.d(TAG, "Data Fetching Some Error");
                    return;
                } else {
                    for (QueryDocumentSnapshot queryDocumentSnapshot : queryDocumentSnapshots) {
                        UserModel userModel = queryDocumentSnapshot.toObject(UserModel.class);
                        mArrayList.add(userModel);
                    }
                    progressBarInitial.setVisibility(View.INVISIBLE);
                    recyclerViewAdapterUserImage = new RecyclerViewAdapterUserImage(PhotosMainClass.this, mArrayList);
                    recyclerView.setAdapter(recyclerViewAdapterUserImage);
                    recyclerViewAdapterUserImage.setOnItemClickListener(PhotosMainClass.this);
                    recyclerViewAdapterUserImage.notifyDataSetChanged();
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(PhotosMainClass.this, "There is some Error" + e.getMessage(), Toast.LENGTH_SHORT).show();
                Log.d(TAG, "Data Fetching Error" + e.getMessage());
            }
        });
    }

    private void getIdForWidgets() {
        recyclerView = findViewById(R.id.recycler_view_activity_photos_main);
        progressBarInitial = findViewById(R.id.progress_bar_activity_photo_main);
    }

    @Override
    public void onProfileClick(int position) {
        setContentView(R.layout.fragment_gloable_container_main);
        UserProfileMain fragment = new UserProfileMain(uniquePhoneNumber);
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container_global_container, fragment).addToBackStack(null).commit();
    }

    @Override
    public void onCommentClick(int position) {
        Toast.makeText(this, "We are working on this functionality ", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onFollowRequest(int position) {

    }

    @Override
    public void onLikedPeopleCountCircleImage(int position) {

    }

    @Override
    public void onLikedPeopleCircleImage(int position) {

    }

    @Override
    public void onUserName(int position) {
        setContentView(R.layout.fragment_gloable_container_main);
        UserProfileMain fragment = new UserProfileMain(uniquePhoneNumber);
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container_global_container, fragment).addToBackStack(null).commit();
    }

    @Override
    public void onMoreOption(int position) {

    }

    private void checkLoginStatus() {
        Cursor cursorLoginStatus = sqLiteDatabase.rawQuery("Select * from UserDataSQlite ", null);
        if (cursorLoginStatus.getCount() != 0) {
            cursorLoginStatus.moveToFirst();
            loginStatus = cursorLoginStatus.getString(5);
            if (LOGIN_STATUS.equals(loginStatus)) {
                uniquePhoneNumber = cursorLoginStatus.getString(3);
            }
            Log.d(TAG, "Login Status is : " + loginStatus);
        } else {
            Log.d(TAG, "No Data in the SQLite Database");
        }
    }
}
