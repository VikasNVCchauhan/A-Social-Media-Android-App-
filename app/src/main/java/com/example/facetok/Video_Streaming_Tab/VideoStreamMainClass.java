package com.example.facetok.Video_Streaming_Tab;

import android.annotation.SuppressLint;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.design.widget.BottomSheetDialogFragment;
import android.support.v4.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.example.facetok.Login_SignUp.LoginMainClass;
import com.example.facetok.Notifications.NotificationsMain;
import com.example.facetok.Profile.UserProfileMain;
import com.example.facetok.R;
import com.example.facetok.UploadOption.UploadOptionMain;
import com.example.facetok.UserPhotos.PhotosMainClass;

import static android.content.Context.MODE_PRIVATE;

public class VideoStreamMainClass extends Fragment implements View.OnClickListener {


    //widgets
    private LinearLayout linearLayoutHome, linearLayoutNotification, linearLayoutSearch, linearLayoutTrends, linearLayoutProfile, linearLayoutPhotos;
    private RelativeLayout relativeLayoutAddVideo;
    private AppCompatActivity appCompatActivity;
    private View view;
    private SQLiteDatabase sqLiteDatabase;
    //************>Interface normal Variables<************//
    private String loginStatus = "0";
    private String uniquePhoneNumber;
    private Intent intent;
    private ImageView imageView_user_profile, imageView_like_hearts;
    //constants
    private final static String LOGIN_STATUS = "YES";
    private final static String TAG = "VideoStreamMainClass";

    public VideoStreamMainClass() {
        // Required empty public constructor
    }

    @SuppressLint("ValidFragment")
    public VideoStreamMainClass(String uniquePhoneNumber) {
        this.uniquePhoneNumber = uniquePhoneNumber;
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,

                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_activity_video_streaming_main, container, false);

        appCompatActivity = (AppCompatActivity) view.getContext();

        setIdsToAllTheVariables(view);
        sqLiteDatabase = getActivity().openOrCreateDatabase("FacTok", MODE_PRIVATE, null);
        sqLiteDatabase.execSQL("Create table if not exists UserDataSQlite (UserName varchar,ImageUrl varchar,Email varchar,Phone varchar,Password varchar,userLogInStatusSQLite varchar);");
        checkLoginStatus();

        linearLayoutHome.setOnClickListener(this);
        linearLayoutNotification.setOnClickListener(this);
        linearLayoutSearch.setOnClickListener(this);
        linearLayoutTrends.setOnClickListener(this);
        linearLayoutProfile.setOnClickListener(this);
        linearLayoutPhotos.setOnClickListener(this);
        relativeLayoutAddVideo.setOnClickListener(this);

        return view;
    }

    private void setIdsToAllTheVariables(View view) {

        linearLayoutHome = view.findViewById(R.id.relative_layout_home_video_stream_main_activity);
        linearLayoutNotification = view.findViewById(R.id.relative_layout_notification_stream_main_activity);
        linearLayoutSearch = view.findViewById(R.id.relative_layout_search_video_stream_main_activity);
        linearLayoutTrends = view.findViewById(R.id.relative_layout_trending_video_stream_main_activity);
        linearLayoutProfile = view.findViewById(R.id.linear_layout_profile_video_stream_main_activity);
        linearLayoutPhotos = view.findViewById(R.id.relative_layout_photos_stream_main_activity);
        relativeLayoutAddVideo = view.findViewById(R.id.relative_layout_add_video_video_stream_main_activity);
    }

    @Override
    public void onClick(View v) {

        if (v == linearLayoutTrends) {
        } else if (v == linearLayoutNotification) {
            Intent intent = new Intent(getContext(), NotificationsMain.class);
            getActivity().startActivity(intent);
        } else if (v == linearLayoutProfile) {
            if (loginStatus.equals(LOGIN_STATUS)) {
                UserProfileMain fragment = new UserProfileMain();
                appCompatActivity.getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container_global_container, fragment).addToBackStack(null).commit();
            } else {
                popUpBottomSheetDialog();
                return;
            }
        } else if (v == linearLayoutHome) {

        } else if (v == linearLayoutPhotos) {
            intent = new Intent(getContext(), PhotosMainClass.class);
            checkLoginStatus();         //for fetching unique phone number
            putUniqueNumber(intent);
            getActivity().startActivity(intent);
        } else if (v == linearLayoutSearch) {

        } else if (v == relativeLayoutAddVideo) {
            if (loginStatus.equals(LOGIN_STATUS)) {
                intent = new Intent(getContext(), UploadOptionMain.class);
                putUniqueNumber(intent);
                getActivity().startActivity(intent);

            } else {
                popUpBottomSheetDialog();
            }
        }
    }

    private void putUniqueNumber(Intent intent) {
        intent.putExtra("PHONE", uniquePhoneNumber);
    }

    public void popUpBottomSheetDialog() {
        BottomSheetDialogFragment bottomSheetDialogFragment = new BottomSheetDialogClass();
        bottomSheetDialogFragment.show(appCompatActivity.getSupportFragmentManager(), "BottomSheet");
    }

    private void checkLoginStatus() {
        Cursor cursorLoginStatus = sqLiteDatabase.rawQuery("Select * from UserDataSQlite ", null);
        if (cursorLoginStatus.getCount() != 0) {
            cursorLoginStatus.moveToFirst();
            for (int i = 0; i < cursorLoginStatus.getCount(); i++) {
                loginStatus = cursorLoginStatus.getString(5);
                if (loginStatus.equals(LOGIN_STATUS)) {
                    loginStatus = cursorLoginStatus.getString(5);
                    uniquePhoneNumber = cursorLoginStatus.getString(3);
                    break;
                }
            }
            Log.d(TAG, "Login Status is : " + loginStatus);
        } else {
            Toast.makeText(appCompatActivity, "No data is here ", Toast.LENGTH_SHORT).show();
            Log.d(TAG, "No Data in the SQLite Database");
        }
    }

}
