package com.example.facetok;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import com.example.facetok.Video_Streaming_Tab.VideoStreamMainClass;

public class MainActivity extends AppCompatActivity {

    private int PERMISSION_REQUEST_CODE = 10;
    private int permissionGranted;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setContentView(R.layout.fragment_gloable_container_main);
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container_global_container, new VideoStreamMainClass()).commit();


//            Intent intent = new Intent(MainActivity.this, Home_Main.class);
//            startActivity(intent);
//        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,new VideoStreamMainClass()).addToBackStack(null).commit();
    }

    @Override
    public void onBackPressed() {

    }
}
