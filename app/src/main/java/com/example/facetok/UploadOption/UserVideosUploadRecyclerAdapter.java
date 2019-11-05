package com.example.facetok.UploadOption;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

public class UserVideosUploadRecyclerAdapter extends RecyclerView.Adapter<UserVideosUploadRecyclerAdapter.UserVideosUploadViewHolder> {

    @NonNull
    @Override
    public UserVideosUploadViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull UserVideosUploadViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 0;
    }

    class UserVideosUploadViewHolder extends RecyclerView.ViewHolder{

        public UserVideosUploadViewHolder(View itemView) {
            super(itemView);
        }
    }
}
