package com.example.sweethome;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.smarteist.autoimageslider.SliderView;
import java.util.ArrayList;

public class EditItemActivity extends AppCompatActivity {
    FirebaseStorage imageStorage = FirebaseStorage.getInstance();
    StorageReference imageStorageRef = imageStorage.getReference();
    private CustomAddTagsField add_tags_field;
    private LinearLayout tags_container;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.manage_item);
        add_tags_field = findViewById(R.id.tag_input);
        tags_container = findViewById(R.id.tags_container);

        TextView screen_name = findViewById(R.id.screen_name);
        screen_name.setText("View / Edit Item");

        ArrayList<ImageSliderData> sliderDataArrayList = new ArrayList<>();
        Intent intent = getIntent();
        if (intent != null) {
            ArrayList<String> photos = intent.getStringArrayListExtra("photos");
            if (photos != null) {
                for (int i = 0; i < photos.size(); i++) {
                    sliderDataArrayList.add(new ImageSliderData(photos.get(i)));
                }
            }
        }

        FrameLayout sliderViewFrame = findViewById(R.id.image_slider_frame);
        SliderView sliderView = findViewById(R.id.image_slider);
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
