package com.example.sweethome;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import java.util.Calendar;
import android.app.DatePickerDialog;
import android.widget.DatePicker;

import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.smarteist.autoimageslider.SliderView;
import java.util.ArrayList;

public class EditItemActivity extends AppCompatActivity {
    FirebaseStorage imageStorage = FirebaseStorage.getInstance();
    StorageReference imageStorageRef = imageStorage.getReference();
    private CustomAddTagsField add_tags_field;
    private LinearLayout tags_container;
    private TextView date_field;
    private EditText value_field;
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

        date_field = findViewById(R.id.date_field);
        date_field.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar c = Calendar.getInstance();
                int year = c.get(Calendar.YEAR);
                int month = c.get(Calendar.MONTH);
                int day = c.get(Calendar.DAY_OF_MONTH);
                DatePickerDialog datePickerDialog = new DatePickerDialog(
                        EditItemActivity.this,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                                date_field.setText(year + "-" + (monthOfYear + 1) + "-" + dayOfMonth);
                                date_field.setTextColor(ContextCompat.getColor(EditItemActivity.this, R.color.black));
                            }
                        },
                        year, month, day);
                datePickerDialog.show();
            }
        });

        value_field = findViewById(R.id.value_field);
        value_field.setOnKeyListener((v, keyCode, event) -> {
            if ((keyCode == KeyEvent.KEYCODE_ENTER)) {
                value_field.setText("$" + value_field.getText().toString().replace("$", ""));
                InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
                if (imm != null) {
                    imm.hideSoftInputFromWindow(value_field.getWindowToken(), 0);
                }
                return true;
            }
            return false;
        });
        value_field.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int actionId, KeyEvent keyEvent) {
                if (actionId == EditorInfo.IME_ACTION_DONE || actionId == EditorInfo.IME_ACTION_NEXT || actionId == EditorInfo.IME_ACTION_UNSPECIFIED) {
                    value_field.setText("$" + value_field.getText().toString().replace("$", ""));
                    InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
                    if (imm != null) {
                        imm.hideSoftInputFromWindow(value_field.getWindowToken(), 0);
                    }
                    return true;
                }
                return false;
            }
        });
    }
}
