package com.example.facetok.UserPhotosMainPackeg;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

public class RecyclereViewAdapterUserImages extends RecyclerView.Adapter<RecyclereViewAdapterUserImages.UserImagesViewHolder> {

    @NonNull
    @Override
    public UserImagesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull UserImagesViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 0;
    }

    class UserImagesViewHolder extends RecyclerView.ViewHolder {

        public UserImagesViewHolder(View itemView) {
            super(itemView);
        }
    }
}
