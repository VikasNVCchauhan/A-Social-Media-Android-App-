package com.example.facetok.UploadOption;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.facetok.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class UserImagesHorizontalRecyclerFragment extends Fragment {


    private RecyclerView horizontalRecyclerView, verticalRecyclerView;
    public UserImagesHorizontalRecyclerFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_user_images_horizontal_recycler_upload_option, container, false);
    }

}