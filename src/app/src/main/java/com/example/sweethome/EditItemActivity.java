package com.example.sweethome;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;

import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.smarteist.autoimageslider.SliderView;
import java.util.ArrayList;

public class EditItemActivity extends AppCompatActivity {
    FirebaseStorage imageStorage = FirebaseStorage.getInstance();
    StorageReference imageStorageRef = imageStorage.getReference();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.manage_item);

        // array list for storing our image urls.
        ArrayList<ImageSliderData> sliderDataArrayList = new ArrayList<>();

        // populate the images array list
        Intent intent = getIntent();
        if (intent != null) {
            ArrayList<String> photos = intent.getStringArrayListExtra("photos");
            for (int i = 0; i < photos.size(); i++) {
                sliderDataArrayList.add(new ImageSliderData(photos.get(i)));
            }
        }

        // get the slider view frame and the slider view.
        FrameLayout sliderViewFrame = findViewById(R.id.image_slider_frame);
        SliderView sliderView = findViewById(R.id.image_slider);

        // get the no image placeholder view.
        RelativeLayout noImagePlaceholder = findViewById(R.id.no_image_placeholder);

        // Check if there are any images associated with the item.
        if (!sliderDataArrayList.isEmpty()) {
            ImageSliderAdapter adapter = new ImageSliderAdapter(this, sliderDataArrayList);
            sliderView.setSliderAdapter(adapter);
            noImagePlaceholder.setVisibility(View.GONE);
            sliderViewFrame.setVisibility(View.VISIBLE);

        } else {
            sliderViewFrame.setVisibility(View.GONE);
            noImagePlaceholder.setVisibility(View.VISIBLE);
        }
    }
}
