package com.example.sweethome;

import android.net.Uri;

public class ImageSliderData {
    private Uri imgUri;

    public ImageSliderData(Uri imgUri) {
        this.imgUri = imgUri;
    }

    public Uri getImgUri() {
        return imgUri;
    }

    public void setImgUri(Uri imgUri) {
        this.imgUri = imgUri;
    }
}
