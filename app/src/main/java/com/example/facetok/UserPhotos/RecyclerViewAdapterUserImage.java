package com.example.facetok.UserPhotos;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.facetok.R;
import com.example.facetok.UploadImagesOption.UserModel;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class RecyclerViewAdapterUserImage extends RecyclerView.Adapter<RecyclerViewAdapterUserImage.ImageViewHolder> {

    //Widgets
    private onItemClickListener itemClickListener;
    private ArrayList<UserModel> mArrayList;
    private Context context;

    //Variable
    private int position;

    public RecyclerViewAdapterUserImage(Context context, ArrayList<UserModel> mArrayList) {
        this.mArrayList = mArrayList;
        this.context = context;
    }

    @NonNull
    @Override
    public ImageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.list_item_photos_recycler_view, null);
        return new ImageViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ImageViewHolder holder, int position) {

        Picasso.with(context).load(mArrayList.get(position).getImageURL()).placeholder(R.drawable.placeholder_image)
                .centerCrop()
                .fit()
                .into(holder.imageViewUserPost);
        Picasso.with(context).load(mArrayList.get(position).getImageURL()).placeholder(R.drawable.placeholder_image)
                .centerCrop()
                .fit()
                .into(holder.circleImageViewUserProfile);
        holder.textViewUserDescription.setText(mArrayList.get(position).getText_Post());
    }

    @Override
    public int getItemCount() {
        return mArrayList.size();
    }

    class ImageViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        //Widgets
        public ImageView imageViewUserPost;
        public CircleImageView circleImageViewUserProfile;
        public RelativeLayout relativeLayoutPeopleLikedImageCircleImage;
        private LinearLayout linearLayoutMoreOption;
        public TextView textViewUserDescription, textViewViewComment, textViewLikeCount, textViewCommentCount, textViewImpressionCount, textViewRePostCount, textViewFollowRequest, textViewUserName, textViewLikedPeopleUserName, textViewLikedPeopleCountCircleImage, textViewDate, textViewTime;


        public ImageViewHolder(View itemView) {
            super(itemView);

            getIdForAllWidgets(itemView);

            circleImageViewUserProfile.setOnClickListener(this);
            textViewViewComment.setOnClickListener(this);
            textViewUserName.setOnClickListener(this);
            textViewFollowRequest.setOnClickListener(this);
            textViewLikedPeopleUserName.setOnClickListener(this);
            textViewLikedPeopleCountCircleImage.setOnClickListener(this);
            linearLayoutMoreOption.setOnClickListener(this);
            relativeLayoutPeopleLikedImageCircleImage.setOnClickListener(this);
        }

        private void getIdForAllWidgets(View itemView) {
            imageViewUserPost = itemView.findViewById(R.id.image_view_user_uploaded_image_list_item_photos_recycler_view);
            textViewUserDescription = itemView.findViewById(R.id.text_view_user_description_list_item_photos_recycler_view);
            circleImageViewUserProfile = itemView.findViewById(R.id.circle_image_user_profile_list_item_photos_recycler_view);
            textViewViewComment = itemView.findViewById(R.id.text_view_view_comments_list_item_photos_recycler_view);
            textViewLikeCount = itemView.findViewById(R.id.text_view_like_count_list_item_photos_recycler_view);
            textViewCommentCount = itemView.findViewById(R.id.text_view_comment_count_list_item_photos_recycler_view);
            textViewImpressionCount = itemView.findViewById(R.id.text_view_impression_count_list_item_photos_recycler_view);
            textViewRePostCount = itemView.findViewById(R.id.text_view_re_post_count_list_item_photos_recycler_view);
            textViewFollowRequest = itemView.findViewById(R.id.text_view_follow_button_list_item_photo_recycler_view);
            textViewUserName = itemView.findViewById(R.id.text_view_user_unique_name_list_item_photo_recycler_view);
            linearLayoutMoreOption = itemView.findViewById(R.id.linear_layout_more_option_list_item_photos_recycler_view);
            textViewLikedPeopleUserName = itemView.findViewById(R.id.text_view_people_liked_photo_list_item_photos_recycler_view);
            textViewLikedPeopleCountCircleImage = itemView.findViewById(R.id.text_view_people_liked_photo_count_list_item_photos_recycler_view);
            textViewDate = itemView.findViewById(R.id.text_view_date_list_item_photos_recycler_view);
            textViewTime = itemView.findViewById(R.id.text_view_time_list_item_photos_recycler_view);
            relativeLayoutPeopleLikedImageCircleImage = itemView.findViewById(R.id.relative_layout_liked_people_circle_image__list_item_photos_recycler_view1);
        }

        @Override
        public void onClick(View v) {
            position = getAdapterPosition();
            if (v == circleImageViewUserProfile) {
                checkPositionExistence(position);
                itemClickListener.onProfileClick(position);
            } else if (v == textViewViewComment) {
                checkPositionExistence(position);
                itemClickListener.onCommentClick(position);
            } else if (v == textViewFollowRequest) {
                checkPositionExistence(position);
                itemClickListener.onFollowRequest(position);
            } else if (v == textViewLikedPeopleCountCircleImage) {
                checkPositionExistence(position);
                itemClickListener.onLikedPeopleCountCircleImage(position);
            } else if (v == textViewUserName) {
                checkPositionExistence(position);
                itemClickListener.onUserName(position);
            } else if (v == relativeLayoutPeopleLikedImageCircleImage) {
                checkPositionExistence(position);
                itemClickListener.onLikedPeopleCircleImage(position);
            } else if (v == linearLayoutMoreOption) {
                checkPositionExistence(position);
                itemClickListener.onMoreOption(position);
            }
        }
    }

    private void checkPositionExistence(int position) {
        if (position == RecyclerView.NO_POSITION) {
            Toast.makeText(context, "There is some issue with it", Toast.LENGTH_SHORT).show();
            return;
        }
    }

    public interface onItemClickListener {
        public void onProfileClick(int position);

        public void onCommentClick(int position);

        public void onFollowRequest(int position);

        public void onLikedPeopleCountCircleImage(int position);

        public void onLikedPeopleCircleImage(int position);

        public void onUserName(int position);

        public void onMoreOption(int position);

    }

    public void setOnItemClickListener(onItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }
}
