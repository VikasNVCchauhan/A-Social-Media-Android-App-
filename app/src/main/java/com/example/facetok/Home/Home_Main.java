package com.example.facetok.Home;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import com.example.facetok.MainActivity;
import com.example.facetok.R;
import com.example.facetok.Video_Streaming_Tab.VideoStreamMainClass;

public class Home_Main extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_gloable_container_main);

        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container_global_container, new VideoStreamMainClass()).commit();
    }
}
