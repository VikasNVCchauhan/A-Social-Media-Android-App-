package com.example.facetok.Profile;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

public class RecyclerViewAdapterProfileVideos extends RecyclerView.Adapter<RecyclerViewAdapterProfileVideos.ProfileVideosViewHolder> {

    @NonNull
    @Override
    public ProfileVideosViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull ProfileVideosViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 0;
    }

    class ProfileVideosViewHolder extends RecyclerView.ViewHolder{

        public ProfileVideosViewHolder(View itemView) {
            super(itemView);
        }
    }
}
