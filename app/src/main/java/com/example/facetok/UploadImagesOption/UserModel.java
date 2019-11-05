package com.example.facetok.UploadImagesOption;

public class UserModel {
    private String imageURL, text_Post;

    public UserModel(String imageURL, String text_Post) {
        if (text_Post.trim().equals("")) {
            text_Post = "No name";
        }
        this.imageURL = imageURL;
        this.text_Post = text_Post;
    }

    public UserModel() {
    }

    public void setImageURL(Object imageURL) {
        this.imageURL = (String) imageURL;
    }

    public void setText_Post(String text_Post) {
        this.text_Post = text_Post;
    }

    public String getImageURL() {
        return imageURL;
    }

    public String getText_Post() {
        return text_Post;
    }
}
