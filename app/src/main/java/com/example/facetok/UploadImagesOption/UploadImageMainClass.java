package com.example.facetok.UploadImagesOption;

import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.facetok.MainActivity;
import com.example.facetok.R;
import com.example.facetok.UserPhotos.PhotosMainClass;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.okhttp.internal.Internal;
import com.squareup.picasso.Picasso;

import java.net.URI;

import de.hdodenhof.circleimageview.CircleImageView;


public class UploadImageMainClass extends AppCompatActivity implements View.OnClickListener {

    private CircleImageView circleImageViewSelectImages;
    private Button buttonChoose, buttonUpload;
    private final int IMAGE_REQUEST_CODE = 1;
    private final int CAMERA_REQUEST_CODE = 2;

    private String uniquePhoneNumber;
    private ImageView imageView;
    private Uri imageUri;
    private ProgressBar mProgressbar;
    private EditText editText;
    private TextView textViewProgress;
    private FirebaseFirestore mFirebaseFirestore;
    private StorageReference mStorageReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload_images_main);

        getIdForAllVariables();
//        setImagesToRecyclerView();

        uniquePhoneNumber = getIntent().getStringExtra("PHONE");

        Toast.makeText(this, "Phone : " + uniquePhoneNumber, Toast.LENGTH_SHORT).show();
        mStorageReference = FirebaseStorage.getInstance().getReference("Uploaded Posts");
        mFirebaseFirestore = FirebaseFirestore.getInstance();
        circleImageViewSelectImages.setOnClickListener(this);
        buttonUpload.setOnClickListener(this);
        buttonChoose.setOnClickListener(this);
    }

    private void setImagesToRecyclerView() {
        setSingleImageToImageView();

    }

    private void setSingleImageToImageView() {

    }

    private void getIdForAllVariables() {
        circleImageViewSelectImages = findViewById(R.id.circle_image_view_select_images_activity_upload_images_main);
        imageView = findViewById(R.id.image_view_add_image_activity_upload_images_main);
        buttonChoose = findViewById(R.id.button_choose_file);
        buttonUpload = findViewById(R.id.button_Upload_file);
        textViewProgress = findViewById(R.id.text_view_progress_upload_image_main_activity);
        mProgressbar = findViewById(R.id.progress_bar_upload_image_main_activity);
        editText = findViewById(R.id.edit_text_post_image_upload_main);
    }

    @Override
    public void onClick(View v) {
        if (v == circleImageViewSelectImages) {
            Intent intent = new Intent();
            intent.setAction("image/*");
            intent.setAction(Intent.ACTION_GET_CONTENT);
            startActivityForResult(intent, IMAGE_REQUEST_CODE);
        } else if (v == buttonChoose) {
            getImageFromGalleryOrCamera();
        } else if (v == buttonUpload) {
            uploadFile();
        }
    }

    private void uploadFile() {
        if (imageUri != null) {
            mProgressbar.setVisibility(View.VISIBLE);
            StorageReference storageReference = mStorageReference.child(System.currentTimeMillis() + "." + getFileExtension(imageUri));
            storageReference.putFile(imageUri).
                    addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            Handler handler = new Handler();
                            handler.postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    mProgressbar.setProgress(0);
                                    textViewProgress.setText("0");
                                }
                            }, 500);
                            uploadDataToFirebase(taskSnapshot);
                            Toast.makeText(UploadImageMainClass.this, "Upload Successful", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(UploadImageMainClass.this, PhotosMainClass.class);
                            intent.putExtra("PHONE", uniquePhoneNumber);
                            startActivity(intent);
                        }
                    }).
                    addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(UploadImageMainClass.this, "Uploading Fault occurred " + e.getMessage(), Toast.LENGTH_LONG).show();
                        }
                    }).
                    addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                            double progress = (100.0 * taskSnapshot.getBytesTransferred()) / taskSnapshot.getTotalByteCount();
                            mProgressbar.setProgress((int) progress);
                            textViewProgress.setText(String.valueOf((int) progress) + " %");
                        }
                    });
        } else {
            Toast.makeText(this, "No file selected", Toast.LENGTH_SHORT).show();
        }
    }

    private void uploadDataToFirebase(UploadTask.TaskSnapshot taskSnapshot) {

        String postName = editText.getText().toString();
        Task<Uri> urlTask = taskSnapshot.getStorage().getDownloadUrl();
        while (!urlTask.isSuccessful()) ;
        Uri downloadUrl = urlTask.getResult();


        UserModel userModel = new UserModel(String.valueOf(downloadUrl), postName);
//        mMapUploadData.put("Image URL ", String.valueOf(downloadUrl));
//        mMapUploadData.put("Post Name ", postName);

        mFirebaseFirestore.collection("UserData").document(uniquePhoneNumber).collection("Upload").document("ImageUpload").collection("Image").document(String.valueOf(System.currentTimeMillis())).set(userModel).
                addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(UploadImageMainClass.this, "URL Upload Successful", Toast.LENGTH_SHORT).show();
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(UploadImageMainClass.this, "URL Upload Failure", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void getImageFromGalleryOrCamera() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, IMAGE_REQUEST_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Toast.makeText(this, "lol", Toast.LENGTH_SHORT).show();
//        if (requestCode == PICK_IMAGE_REQUEST && requestCode == RESULT_OK && data != null && data.getData() != null) {
        imageUri = data.getData();

        Toast.makeText(this, "url : " + imageUri, Toast.LENGTH_SHORT).show();
        Picasso.with(UploadImageMainClass.this).
                load(imageUri).
                centerCrop().
                fit().
                into(imageView);

//        }
//        else{
//            Toast.makeText(this, "Image loading problem ", Toast.LENGTH_SHORT).show();
//        }
    }

    private String getFileExtension(Uri imageUri) {
        ContentResolver cr = getContentResolver();
        MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();
        return mimeTypeMap.getExtensionFromMimeType(
                cr.getType(imageUri)
        );
    }
}
