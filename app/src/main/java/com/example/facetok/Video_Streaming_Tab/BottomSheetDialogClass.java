package com.example.facetok.Video_Streaming_Tab;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetDialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.facetok.Login_SignUp.LoginMainClass;
import com.example.facetok.Login_SignUp.SignUpMainClass;
import com.example.facetok.R;

public class BottomSheetDialogClass extends BottomSheetDialogFragment implements View.OnClickListener {

    //    private BottomSheetListener mListener;
    private Button buttonLogin, buttonSignUp;

    private ImageView imageViewFacebook, imageViewGoogle;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_login_bottom_layout_main, container, false);
        getIdForAllVariables(v);
        buttonLogin.setOnClickListener(this);
        buttonSignUp.setOnClickListener(this);
        imageViewFacebook.setOnClickListener(this);
        imageViewGoogle.setOnClickListener(this);

        return v;


    }

    private void getIdForAllVariables(View v) {
        buttonLogin = v.findViewById(R.id.button_login_fragment_bottom_layout_login_main);
        buttonSignUp = v.findViewById(R.id.button_sign_up_fragment_bottom_layout_login_main);
        imageViewFacebook = v.findViewById(R.id.image_view_facebook_fragment_bottom_layout_login_main);
        imageViewGoogle = v.findViewById(R.id.image_view_google_fragment_bottom_layout_login_main);

    }

    @Override
    public void onClick(View v) {
        if (v == buttonLogin) {
            setColorChange(buttonLogin);
            Intent intent = new Intent(getContext(), LoginMainClass.class);
            startActivityButtonColorChange(intent);

        } else if (v == buttonSignUp) {
            setColorChange(buttonSignUp);
            Intent intent = new Intent(getContext(), SignUpMainClass.class);
            intent.putExtra("EXTRA", "1");
            startActivityButtonColorChange(intent);
        } else if (v == imageViewFacebook) {
            Toast.makeText(getContext(), "We are working on it", Toast.LENGTH_SHORT).show();
        } else if (v == imageViewGoogle) {
            Toast.makeText(getContext(), "We are working on it", Toast.LENGTH_SHORT).show();
        }
    }

    private void setColorChange(Button b) {
        b.setBackgroundResource(R.drawable.login_button);
        b.setTextColor(Color.parseColor("#FAF8F7"));
    }

    private void startActivityButtonColorChange(final Intent intent) {
        Handler handler=new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                startActivity(intent);
            }
        },20);
    }


    //    public interface BottomSheetListener{
//        void onButtonClicked();
//    }
}
