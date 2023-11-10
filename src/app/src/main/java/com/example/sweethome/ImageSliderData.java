package com.example.sweethome;
/**
 * This class represents data for an image in the image slider. It encapsulates the URI of the
 * image to be displayed in the image slider.
 * <p>The class provides getter and setter to access the image URI. The image URI is typically
 * used by an {@link ImageSliderAdapter} to load and display the image in the image slider.</p>
 *
 * November 10, 2023
 */

/* necessary imports */
import android.net.Uri;

public class ImageSliderData {
    /* attributes of this class */
    private Uri imgUri;

    /**
     * Constructs an ImageSliderData object with the provided image URI.
     *
     * @param imgUri The URI of the image to be displayed in the image slider
     */
    public ImageSliderData(Uri imgUri) {
        this.imgUri = imgUri;
    }

    /**
     * Gets the URI of the image.
     *
     * @return The URI of the image
     */
    public Uri getImgUri() {
        return this.imgUri;
    }

    /**
     * Sets the URI of the image.
     *
     * @param imgUri The URI of the image to be set
     */
    public void setImgUri(Uri imgUri) {
        this.imgUri = imgUri;
    }
}
