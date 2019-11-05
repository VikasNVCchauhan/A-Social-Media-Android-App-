package com.example.facetok.Profile;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.facetok.R;
import com.example.facetok.UploadImagesOption.UserModel;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class RecyclerViewAdapterImagesAtProfile extends RecyclerView.Adapter<RecyclerViewAdapterImagesAtProfile.ProfileImageViewHolder> {

    //widgets
    private Context context;
    //Variables
    private ArrayList<UserModel> modelArrayList;

    public RecyclerViewAdapterImagesAtProfile(Context context, ArrayList<UserModel> modelArrayList) {
        this.context = context;
        this.modelArrayList = modelArrayList;
    }

    @NonNull
    @Override
    public ProfileImageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_imaages_activity_user_profile, null);
        return new ProfileImageViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ProfileImageViewHolder holder, int position) {
        Picasso.with(context).load(modelArrayList.get(position).getImageURL())
                .placeholder(R.drawable.placeholder_image)
                .centerCrop()
                .fit()
                .into(holder.imageViewUserUploadedImages);

    }

    @Override
    public int getItemCount() {
        return modelArrayList.size();
    }

    class ProfileImageViewHolder extends RecyclerView.ViewHolder {
        //Widgets
        public ImageView imageViewUserUploadedImages;

        public ProfileImageViewHolder(View itemView) {
            super(itemView);
            getIdForAllWidgets(itemView);
        }

        private void getIdForAllWidgets(View itemView) {
            imageViewUserUploadedImages = itemView.findViewById(R.id.image_view_images_item_images_activity_user_profile);
        }
    }
}
