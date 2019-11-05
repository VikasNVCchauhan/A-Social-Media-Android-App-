package com.example.facetok.UserPhotos;

public class PhotosDataHelperClass  {

    private String userNamePhotosMain,descriptionPhotosMain,likeCountPhotosMain,rePostCountPhotosMain,commentCountPhotosMain,impressionCountPhotosMain,imageLinkPhotosMain,datePhotosMain,timePhotosMain;

    public PhotosDataHelperClass(String userNamePhotosMain, String descriptionPhotosMain, String likeCountPhotosMain, String rePostCountPhotosMain, String commentCountPhotosMain, String impressionCountPhotosMain, String imageLinkPhotosMain, String datePhotosMain, String timePhotosMain) {
        this.userNamePhotosMain = userNamePhotosMain;
        this.descriptionPhotosMain = descriptionPhotosMain;
        this.likeCountPhotosMain = likeCountPhotosMain;
        this.rePostCountPhotosMain = rePostCountPhotosMain;
        this.commentCountPhotosMain = commentCountPhotosMain;
        this.impressionCountPhotosMain = impressionCountPhotosMain;
        this.imageLinkPhotosMain = imageLinkPhotosMain;
        this.datePhotosMain = datePhotosMain;
        this.timePhotosMain = timePhotosMain;
    }

    public PhotosDataHelperClass() {

    }

    public String getUserNamePhotosMain() {
        return userNamePhotosMain;
    }

    public String getDescriptionPhotosMain() {
        return descriptionPhotosMain;
    }

    public String getLikeCountPhotosMain() {
        return likeCountPhotosMain;
    }

    public String getRePostCountPhotosMain() {
        return rePostCountPhotosMain;
    }

    public String getCommentCountPhotosMain() {
        return commentCountPhotosMain;
    }

    public String getImpressionCountPhotosMain() {
        return impressionCountPhotosMain;
    }

    public String getImageLinkPhotosMain() {
        return imageLinkPhotosMain;
    }

    public String getDatePhotosMain() {
        return datePhotosMain;
    }

    public String getTimePhotosMain() {
        return timePhotosMain;
    }
}
