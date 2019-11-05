package com.example.facetok.Video_Streaming_Tab;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.example.facetok.R;

public class RecyclerViewAdapterVideoStreamClass extends RecyclerView.Adapter<RecyclerViewAdapterVideoStreamClass.VideoStreamClassHolder> {


    @NonNull
    @Override
    public VideoStreamClassHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull VideoStreamClassHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 0;
    }

    class VideoStreamClassHolder extends RecyclerView.ViewHolder {

        LinearLayout linearLayoutHeartLike, linearLayoutUserProfile, linearLayoutDownloadVideo, linearLayoutComment, linearLayoutShareVideo;

        public VideoStreamClassHolder(View itemView) {
            super(itemView);
            getIdForVariables(itemView);
        }

        private void getIdForVariables(View itemView) {
            linearLayoutComment = itemView.findViewById(R.id.relative_layout_comments_video_stream_main_activity);
            linearLayoutDownloadVideo = itemView.findViewById(R.id.relative_layout_download_video_stream_main_activity);
            linearLayoutHeartLike = itemView.findViewById(R.id.relative_layout_heart_like_video_stream_main_activity);
            linearLayoutUserProfile = itemView.findViewById(R.id.linear_layout_video_uploader_profile_video_stream_main_activity);
            linearLayoutShareVideo = itemView.findViewById(R.id.relative_layout_share_video_stream_main_activity);
        }
    }
}
