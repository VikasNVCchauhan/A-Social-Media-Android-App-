package com.example.facetok.Notifications;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

public class RecyclerViewAdapterNotificationMain extends RecyclerView.Adapter<RecyclerViewAdapterNotificationMain.NotificationViewHolder> {

    @NonNull
    @Override
    public NotificationViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull NotificationViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 0;
    }

    class NotificationViewHolder extends RecyclerView.ViewHolder{

        public NotificationViewHolder(View itemView) {
            super(itemView);
        }
    }
}
