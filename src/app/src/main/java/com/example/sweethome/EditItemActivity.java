package com.example.sweethome;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.smarteist.autoimageslider.SliderView;
import java.util.ArrayList;

public class EditItemActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.manage_item);

        // array list for storing our image urls.
        ArrayList<ImageSliderData> sliderDataArrayList = new ArrayList<>();

        // initializing the slider view.
        SliderView sliderView = findViewById(R.id.image_slider);

        // in case the item has no associated images.
        int noImagePlaceholderSrc = R.drawable.no_image_placeholder;
        ImageSliderData noImagePlaceholder = new ImageSliderData(noImagePlaceholderSrc);
        sliderDataArrayList.add(noImagePlaceholder);

        // add the image urls inside array list
        // sliderDataArrayList.add(new ImageSliderData(example image url));

        // passing this array list inside our adapter class.
        ImageSliderAdapter adapter = new ImageSliderAdapter(this, sliderDataArrayList);

        // setadapter to sliderview.
        sliderView.setSliderAdapter(adapter);
    }
}
