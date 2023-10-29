package com.example.sweethome;

public class ImageSliderData {
    private String imgUrl;

    public ImageSliderData(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    // Constructor for placeholder
    public ImageSliderData(int imageResource) {
        this.imgUrl = String.valueOf(imageResource);
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }
}
