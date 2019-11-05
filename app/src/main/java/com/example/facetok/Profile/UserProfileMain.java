package com.example.facetok.Profile;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.graphics.drawable.ClipDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.InsetDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomSheetDialogFragment;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.facetok.HelperPackeg.Audit;
import com.example.facetok.R;
import com.example.facetok.UploadImagesOption.UserModel;
import com.example.facetok.Video_Streaming_Tab.BottomSheetDialogClass;
import com.example.facetok.Video_Streaming_Tab.VideoStreamMainClass;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

import static android.content.Context.MODE_PRIVATE;
import static android.graphics.Color.*;

public class UserProfileMain extends Fragment implements View.OnClickListener {

    //widgets
    private ProgressBar progressBarUserProfile, progressBarRecyclerView;
    private RecyclerView recyclerView;
    private AppCompatActivity appCompatActivity;
    private View view;

    private ImageView imageViewUserImage, imageViewShareProfile, imageViewAboutProfile, imageViewThreeDotOptionMenu;
    private TextView textViewFollow, textViewUserUniqueName, textViewUserName, textViewSubscriberCount, textViewHeartCount, textViewUploadCount;
    //Text view alert dialog about profile
    private TextView textViewUserNameAlertDialogAboutProfile, textViewUserUniqueNameAlertDialogAboutProfile, textViewFollowOptionAlertDialogAboutProfile;
    private CircleImageView circleImageViewProfileImage, circleImageViewProfileImageAlertDialogAboutProfile;
    private LinearLayout linearLayoutSettingAlertDialog, linearLayoutTermAndConditionsAlertDialog, linearLayoutReportProblemAlertDialog, linearLayoutNeedHelpAlertDialog,
            linearLayoutPrivacyPolicyAlertDialog, linearLayoutAboutUsAlertDialog, linearLayoutShareAlertDialog, linearLayoutEditProfileAlertDialog, linearLayoutLogOutAlertDialog;
    //Database
    private SQLiteDatabase sqLiteDatabase;
    private FirebaseFirestore firebaseFirestore;
    private CollectionReference collectionReference;
    //variables
//    private onItemClickListenerProfile itemClickListener;
    private AlertDialog alertDialog, alertDialogAboutUserProfile;
    private Dialog dialogAboutUserProfile;  ////------>
    private final static String LOGIN_STATUS = "YES";
    private int IS_ALERT_DIALOG_VISITED = 0;
    private String loginStatus;
    private ArrayList<UserModel> mArrayList;
    private String uniquePhoneNumber;
    private static final String TAG = "UserProfileMain";
    private static int DIALOG_TYPE_OPTION_MENU = 1;
    //Recycler View Widget
    private RecyclerViewAdapterImagesAtProfile recyclerViewAdapterImagesAtProfile;

    public UserProfileMain() {
        // Required empty public constructor
    }

    @SuppressLint("ValidFragment")
    public UserProfileMain(String uniquePhoneNumber) {
        // Required empty public constructor
        this.uniquePhoneNumber = uniquePhoneNumber;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.activity_user_profile_main, container, false);
        appCompatActivity = (AppCompatActivity) view.getContext();
        getIdForAllVariables();

        sqLiteDatabase = getActivity().openOrCreateDatabase("FacTok", MODE_PRIVATE, null);
        sqLiteDatabase.execSQL("Create table if not exists UserDataSQlite (UserName varchar,ImageUrl varchar,Email varchar,Phone varchar,Password varchar,userLogInStatusSQLite varchar);");
        checkLoginStatus();

//        Audit.checkLoginStatus(appCompatActivity);
        dialogAboutUserProfile = new Dialog(getContext());
        progressBarUserProfile.setVisibility(View.VISIBLE);
        progressBarRecyclerView.setVisibility(View.VISIBLE);
        mArrayList = new ArrayList<>();

        if (LOGIN_STATUS.equals(loginStatus)) {

            recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 3, GridLayoutManager.VERTICAL, false));
            firebaseFirestore = FirebaseFirestore.getInstance();
            collectionReference = firebaseFirestore.collection("UserData").document(uniquePhoneNumber).collection("Upload").document("ImageUpload").collection("Image");

            getDataFromFirebase();

            imageViewThreeDotOptionMenu.setOnClickListener(this);
            imageViewAboutProfile.setOnClickListener(this);
        } else {
            progressBarRecyclerView.setVisibility(View.INVISIBLE);
            progressBarUserProfile.setVisibility(View.INVISIBLE);
            popUpBottomSheetDialog();
        }
        return view;
    }

    private void getDataFromFirebase() {
        collectionReference.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                if (queryDocumentSnapshots != null) {
                    for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                        UserModel userModel = documentSnapshot.toObject(UserModel.class);
                        mArrayList.add(userModel);
                        if (mArrayList.size() < 2) {     //this is just for temporary bases
                            setProfile(mArrayList);
                        }
                    }
                    progressBarRecyclerView.setVisibility(View.INVISIBLE);
                    progressBarUserProfile.setVisibility(View.INVISIBLE);
                    recyclerViewAdapterImagesAtProfile = new RecyclerViewAdapterImagesAtProfile(getContext(), mArrayList);
                    recyclerView.setAdapter(recyclerViewAdapterImagesAtProfile);
                    recyclerViewAdapterImagesAtProfile.notifyDataSetChanged();
                } else {
                    Toast.makeText(getContext(), "No Profile Exist", Toast.LENGTH_SHORT).show();
                    return;
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.d(TAG, "Error in Data Fetching " + e.getMessage());
                Toast.makeText(getActivity(), "Error in data fetching " + e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void setProfile(ArrayList<UserModel> arrayList) {

        Picasso.with(getContext()).load(arrayList.get(0).getImageURL())
                .placeholder(R.drawable.placeholder_image)
                .centerCrop()
                .fit()
                .into(circleImageViewProfileImage);
    }

    private void getIdForAllVariables() {
        imageViewAboutProfile = view.findViewById(R.id.image_view_about_profile_activity_user_profile_main);
        imageViewShareProfile = view.findViewById(R.id.image_view_share_profile_activity_user_profile_main);
        imageViewUserImage = view.findViewById(R.id.circle_image_user_profile_activity_user_profile_main);
        textViewFollow = view.findViewById(R.id.text_view_follow__activity_user_profile_main);
        textViewHeartCount = view.findViewById(R.id.text_view_heart_user_profile_main_activity);
        textViewSubscriberCount = view.findViewById(R.id.text_view_followers_count_activity_user_profile_main);
        textViewUploadCount = view.findViewById(R.id.text_view_video_uploader_profile_video_steam_main_activity);
        textViewUserName = view.findViewById(R.id.text_view_name_user_profile_main_activity);
        textViewUserUniqueName = view.findViewById(R.id.text_view_unique_user_name_user_profile_main_activity);
        recyclerView = view.findViewById(R.id.recycler_view_videos_and_images_activity_user_profile_main);
        imageViewThreeDotOptionMenu = view.findViewById(R.id.image_view_three_dot_option_menu_user_profile_main_activity);
        circleImageViewProfileImage = view.findViewById(R.id.circle_image_user_profile_activity_user_profile_main);
        progressBarRecyclerView = view.findViewById(R.id.progress_bar_recycler_vie_activity_user_profile_main);
        progressBarUserProfile = view.findViewById(R.id.progress_bar_profile_image_view_activity_user_profile_main);

    }

    private void checkLoginStatus() {
        Cursor cursorLoginStatus = sqLiteDatabase.rawQuery("Select * from UserDataSQlite", null);
        if (cursorLoginStatus.getCount() != 0) {
            cursorLoginStatus.moveToFirst();
            Toast.makeText(appCompatActivity, "" + cursorLoginStatus.getCount(), Toast.LENGTH_SHORT).show();
            loginStatus = cursorLoginStatus.getString(5);
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
            Log.d(TAG, "No Data in the SQLite Database");
            return;
        }
    }

    public void popUpBottomSheetDialog() {
        BottomSheetDialogFragment bottomSheetDialogFragment = new BottomSheetDialogClass();
        bottomSheetDialogFragment.show(appCompatActivity.getSupportFragmentManager(), "BottomSheet");
    }


    @Override
    public void onClick(View v) {
        if (v == imageViewThreeDotOptionMenu) {
            onClickCustomAlertDialogOptionMenu();
        } else if (v == imageViewAboutProfile) {
            onClickCustomAlertDialogAboutProfile();
        } else if (v == textViewFollowOptionAlertDialogAboutProfile) {

            DIALOG_TYPE_OPTION_MENU = 0;
            textViewFollowOptionAlertDialogAboutProfile.setBackgroundResource(R.drawable.butto_upload_video);
            textViewFollowOptionAlertDialogAboutProfile.setTextColor(Color.parseColor("#000000"));
            Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    dialogAboutUserProfile.dismiss();
                }
            }, 500);

            Toast.makeText(appCompatActivity, "Working on it", Toast.LENGTH_SHORT).show();
        } else {   //Option Menu
            IS_ALERT_DIALOG_VISITED = 0;
            DIALOG_TYPE_OPTION_MENU = 1;
            onClickAlertDialogAction(v, IS_ALERT_DIALOG_VISITED);
        }
    }

    //custom alert dialog about profile
    private void onClickCustomAlertDialogAboutProfile() {

        dialogAboutUserProfile.setContentView(R.layout.item_custom_alert_dialog_about_profile);
        dialogAboutUserProfile.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialogAboutUserProfile.show();

        getIdForAllWidgetsAboutProfile(dialogAboutUserProfile);

        textViewFollowOptionAlertDialogAboutProfile.setOnClickListener(this);
//        View view = LayoutInflater.from(getContext()).inflate(R.layout.item_custom_alert_dialog_about_profile, null);
//        dialogAboutUserProfile.setContentView(R.layout.item_custom_alert_dialog_about_profile);
//        dialogAboutUserProfile.show();
//        dialogAboutUserProfile.getWindow().setBackgroundDrawable(new ClipDrawable(TRANSPARENT));


//        alertDialogAboutUserProfile = new AlertDialog.Builder(getContext())
//                .setView(view)
//                .show();
    }

    private void getIdForAllWidgetsAboutProfile(Dialog dialogAboutUserProfile) {
        textViewUserNameAlertDialogAboutProfile = dialogAboutUserProfile.findViewById(R.id.text_view_user_name_item_custom_alert_dialog_about_profile);
        textViewUserUniqueNameAlertDialogAboutProfile = dialogAboutUserProfile.findViewById(R.id.text_view_user_unique_name_item_custom_alert_dialog_about_profile);
        textViewFollowOptionAlertDialogAboutProfile = dialogAboutUserProfile.findViewById(R.id.text_view_follow_option_item_custom_alert_dialog_about_profile);
        circleImageViewProfileImageAlertDialogAboutProfile = dialogAboutUserProfile.findViewById(R.id.circle_image_view_user_profile_picture_item_custom_alert_dialog_about_profile);

    }

    private void onClickAlertDialogAction(View v, int is_alert_dialog_visited) {
        if (v == linearLayoutAboutUsAlertDialog) {
            if (is_alert_dialog_visited == 0) {
                onOnClickAlertDialogAction(linearLayoutAboutUsAlertDialog);
            } else if (is_alert_dialog_visited == 1) {
                //appCompatActivity.getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container_global_container, new VideoStreamMainClass()).commit();
            }
        } else if (v == linearLayoutEditProfileAlertDialog) {
            if (is_alert_dialog_visited == 0) {
                onOnClickAlertDialogAction(linearLayoutEditProfileAlertDialog);
            } else if (is_alert_dialog_visited == 1) {
                // appCompatActivity.getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container_global_container, new VideoStreamMainClass()).commit();
            }
        } else if (v == linearLayoutLogOutAlertDialog) {
            if (is_alert_dialog_visited == 0) {
                updateSqlDatabase();
                onOnClickAlertDialogAction(linearLayoutLogOutAlertDialog);
            } else if (is_alert_dialog_visited == 1) {
                appCompatActivity.getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container_global_container, new VideoStreamMainClass()).commit();
            }
        } else if (v == linearLayoutNeedHelpAlertDialog) {
            if (is_alert_dialog_visited == 0) {
                onOnClickAlertDialogAction(linearLayoutNeedHelpAlertDialog);
            } else if (is_alert_dialog_visited == 1) {
                // appCompatActivity.getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container_global_container, new VideoStreamMainClass()).commit();
            }
        } else if (v == linearLayoutPrivacyPolicyAlertDialog) {
            if (is_alert_dialog_visited == 0) {
                onOnClickAlertDialogAction(linearLayoutPrivacyPolicyAlertDialog);
            } else if (is_alert_dialog_visited == 1) {
                //appCompatActivity.getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container_global_container, new VideoStreamMainClass()).commit();
            }
        } else if (v == linearLayoutReportProblemAlertDialog) {
            if (is_alert_dialog_visited == 0) {
                onOnClickAlertDialogAction(linearLayoutReportProblemAlertDialog);
            } else if (is_alert_dialog_visited == 1) {
                // appCompatActivity.getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container_global_container, new VideoStreamMainClass()).commit();
            }
        } else if (v == linearLayoutSettingAlertDialog) {
            if (is_alert_dialog_visited == 0) {
                onOnClickAlertDialogAction(linearLayoutSettingAlertDialog);
            } else if (is_alert_dialog_visited == 1) {
                //appCompatActivity.getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container_global_container, new VideoStreamMainClass()).commit();
            }
        } else if (v == linearLayoutShareAlertDialog) {
            if (is_alert_dialog_visited == 0) {
                onOnClickAlertDialogAction(linearLayoutShareAlertDialog);
            } else if (is_alert_dialog_visited == 1) {
                // appCompatActivity.getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container_global_container, new VideoStreamMainClass()).commit();
            }
        } else if (v == linearLayoutTermAndConditionsAlertDialog) {
            if (is_alert_dialog_visited == 0) {
                onOnClickAlertDialogAction(linearLayoutTermAndConditionsAlertDialog);
            } else if (is_alert_dialog_visited == 1) {
                //appCompatActivity.getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container_global_container, new VideoStreamMainClass()).commit();
            }
        }
    }

    private void updateSqlDatabase() {
        sqLiteDatabase.execSQL("update UserDataSQlite set userLogInStatusSQLite='" + "No" + "' where Phone='" + uniquePhoneNumber + "'");
    }

    private void onOnClickAlertDialogAction(View v) {
        v.setBackgroundResource(R.color.NormaTextColor);
        setOnClickChangeColor(v);
    }

    private void setOnClickChangeColor(final View v) {
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                alertDialog.dismiss();
                if (DIALOG_TYPE_OPTION_MENU == 1) {
                    onClickAlertDialogAction(v, 1);
                }
            }
        }, 100);
    }

    //custom alert dialog option menu
    private void onClickCustomAlertDialogOptionMenu() {
        View view = LayoutInflater.from(getContext()).inflate(R.layout.item_custom_alert_dialog_user_profile_main, null);
        getIdForAlertDialogWidgets(view);

        alertDialog = new AlertDialog.Builder(getContext())
                .setView(view)
                .show();

//        alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        ColorDrawable back = new ColorDrawable(Color.TRANSPARENT);
        InsetDrawable inset = new InsetDrawable(back, 40);    //here margin from left and right are set
        alertDialog.getWindow().setBackgroundDrawable(inset);

//        itemClickListener.onClickAbout();
        linearLayoutSettingAlertDialog.setOnClickListener(this);
        linearLayoutTermAndConditionsAlertDialog.setOnClickListener(this);
        linearLayoutReportProblemAlertDialog.setOnClickListener(this);
        linearLayoutNeedHelpAlertDialog.setOnClickListener(this);
        linearLayoutPrivacyPolicyAlertDialog.setOnClickListener(this);
        linearLayoutAboutUsAlertDialog.setOnClickListener(this);
        linearLayoutShareAlertDialog.setOnClickListener(this);
        linearLayoutEditProfileAlertDialog.setOnClickListener(this);
        linearLayoutLogOutAlertDialog.setOnClickListener(this);
    }

    private void getIdForAlertDialogWidgets(View view) {
        linearLayoutSettingAlertDialog = view.findViewById(R.id.linear_layout_settings_item_custom_alert_dialog_user_profile_main);
        linearLayoutTermAndConditionsAlertDialog = view.findViewById(R.id.linear_layout_terms_and_condition_item_custom_alert_dialog_user_profile_main);
        linearLayoutReportProblemAlertDialog = view.findViewById(R.id.linear_layout_report_problem_item_custom_alert_dialog_user_profile_main);
        linearLayoutNeedHelpAlertDialog = view.findViewById(R.id.linear_layout_help_item_custom_alert_dialog_user_profile_main);
        linearLayoutPrivacyPolicyAlertDialog = view.findViewById(R.id.linear_layout_security_and_privacy_item_custom_alert_dialog_user_profile_main);
        linearLayoutAboutUsAlertDialog = view.findViewById(R.id.linear_layout_about_us_item_custom_alert_dialog_user_profile_main);
        linearLayoutShareAlertDialog = view.findViewById(R.id.linear_layout_share_profile_item_custom_alert_dialog_user_profile_main);
        linearLayoutEditProfileAlertDialog = view.findViewById(R.id.linear_layout_edit_profile_item_custom_alert_dialog_user_profile_main);
        linearLayoutLogOutAlertDialog = view.findViewById(R.id.linear_layout_log_out_item_custom_alert_dialog_user_profile_main);
    }

//    public interface onItemClickListenerProfile {
//        public void onClickAbout(View view);
//    }
//
//    public void setOnItemClickListener(onItemClickListenerProfile itemClickListener) {
//        this.itemClickListener = itemClickListener;
//    }
}
