package com.example.facetok.UploadOption;

import android.Manifest;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.facetok.R;
import com.example.facetok.RuntimePermissions.PhonePermissionAbstractClass;
import com.example.facetok.UploadImagesOption.UploadImageMainClass;
import com.example.facetok.UploadVideoOption.UploadVideoMainClass;
import com.example.facetok.Video_Streaming_Tab.VideoStreamMainClass;

public class UploadOptionMain extends PhonePermissionAbstractClass implements View.OnClickListener {

    private ImageView uploadImageImageView, uploadVideoImageView;
    private LinearLayout linearLayoutUploadImage, linearLayoutUploadVideo;

    private int PERMISSION_REQUEST_CODE = 10;
    private int permissionGranted;
    private String uniquePhoneNumber;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload_option_main);

        if (permissionGranted != 1) {
            checkPermissions();
        }

        uniquePhoneNumber=getIntent().getStringExtra("PHONE");
        getIdForVariable();
        uploadImageImageView.setOnClickListener(this);
        uploadVideoImageView.setOnClickListener(this);
        linearLayoutUploadVideo.setOnClickListener(this);
        linearLayoutUploadImage.setOnClickListener(this);
    }

    private void checkPermissions() {
        requestAppPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.CAMERA,
                Manifest.permission.READ_CONTACTS}, R.string.permissionMessage, PERMISSION_REQUEST_CODE);
    }

    @Override
    public void onPermissionGrantedCheck(int requestCode) {
        permissionGranted = 1;
    }

    private void getIdForVariable() {


        uploadImageImageView = findViewById(R.id.image_view_upload_image_activity_upload_option_main);
        uploadVideoImageView = findViewById(R.id.image_view_upload_video_activity_upload_option_main);
        linearLayoutUploadImage = findViewById(R.id.linear_layout_upload_image_option_upload_main_option);
        linearLayoutUploadVideo = findViewById(R.id.linear_layout_upload_video_option_upload_main_option);
//        horizontalRecyclerView = findViewById(R.id.recycler_view_horizontal_follow_user_activity_upload_option_main);
//        verticalRecyclerView = findViewById(R.id.recycler_view_vertical_user_images_activity_upload_option_main);

    }

    @Override
    public void onClick(View v) {
        if (v == uploadImageImageView) {
            Intent intent = new Intent(UploadOptionMain.this, UploadImageMainClass.class);
            intent.putExtra("PHONE", uniquePhoneNumber);
            startActivity(intent);
        } else if (v == uploadVideoImageView) {
            Intent intent = new Intent(UploadOptionMain.this, UploadVideoMainClass.class);
            startActivity(intent);
        } else if (v == linearLayoutUploadImage) {
            Toast.makeText(UploadOptionMain.this, "Image ", Toast.LENGTH_SHORT).show();
        } else if (v == linearLayoutUploadVideo) {

        }
    }

    @Override
    public void onBackPressed() {
        setContentView(R.layout.fragment_gloable_container_main);
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container_global_container, new VideoStreamMainClass()).commit();
    }
}