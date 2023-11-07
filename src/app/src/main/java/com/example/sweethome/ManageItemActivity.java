package com.example.sweethome;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
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
import java.util.Date;

import javax.annotation.Nullable;

public class ManageItemActivity extends AppCompatActivity {
    FirebaseStorage imageStorage = FirebaseStorage.getInstance();
    StorageReference imageStorageRef = imageStorage.getReference();
    private CustomAddTagsField add_tags_field;
    private LinearLayout tags_container;
    private TextView date_field;
    private EditText value_field;
    private ImageView nav_back_button;
    private String name;
    private String make;
    private String model;
    private String serialNumber;
    private Double estimatedValue;
    private Date purchaseDate;
    private String comment;
    private ArrayList<Uri> photos;
    private CardView open_gallery_button;
    private Uri imageUri;
    private ImageSliderAdapter adapter;
    private ArrayList<ImageSliderData> sliderDataArrayList;
    private SliderView sliderView;
    private FrameLayout sliderViewFrame;
    private TextView screen_name;
    private ImageView save_button;
    private EditText item_name_field;
    private EditText serial_number_field;
    private CustomAddTagsField tag_input;
    private EditText description_field;
    private EditText make_field;
    private EditText model_field;
    private EditText comment_field;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.manage_item);

        // define attributes and variables.
        add_tags_field = findViewById(R.id.tag_input);
        tags_container = findViewById(R.id.tags_container);
        screen_name = findViewById(R.id.screen_name);
        nav_back_button = findViewById(R.id.nav_back_button);
        value_field = findViewById(R.id.value_field);
        date_field = findViewById(R.id.date_field);
        sliderViewFrame = findViewById(R.id.image_slider_frame);
        sliderView = findViewById(R.id.image_slider);
        RelativeLayout noImagePlaceholder = findViewById(R.id.no_image_placeholder);
        sliderDataArrayList = new ArrayList<>();
        Intent intent = getIntent();
        open_gallery_button = findViewById(R.id.open_gallery_button);
        save_button = findViewById(R.id.check_icon);
        item_name_field = findViewById(R.id.item_name_field);
        serial_number_field = findViewById(R.id.serial_number_field);
        tag_input = findViewById(R.id.tag_input);
        description_field = findViewById(R.id.description_field);
        make_field = findViewById(R.id.make_field);
        model_field = findViewById(R.id.model_field);
        comment_field = findViewById(R.id.comment_field);

        // get item's information sent from home screen.
        if (intent != null) {
            String screen = intent.getStringExtra("screen");
            screen_name.setText(screen);
            name = intent.getStringExtra("name");
            make = intent.getStringExtra("make");
            model = intent.getStringExtra("model");
            serialNumber = intent.getStringExtra("serialNumber");
            estimatedValue = intent.getDoubleExtra("estimatedValue", 0.0);
            purchaseDate =  new Date(intent.getLongExtra("purchaseDate", 0L));
            comment = intent.getStringExtra("comment");
            photos = intent.getParcelableArrayListExtra("photos");
        }

        // populate the image slider if the item is associated with at least 1 photo.
        if (photos != null) {
            for (int i = 0; i < photos.size(); i++) {
                sliderDataArrayList.add(new ImageSliderData(photos.get(i)));
            }
        }

        // check if there are any images associated with the item.
        if (!sliderDataArrayList.isEmpty()) {
            adapter = new ImageSliderAdapter(this, sliderDataArrayList);
            sliderView.setSliderAdapter(adapter);
            noImagePlaceholder.setVisibility(View.GONE);
            sliderViewFrame.setVisibility(View.VISIBLE);

        } else {
            sliderViewFrame.setVisibility(View.GONE);
            noImagePlaceholder.setVisibility(View.VISIBLE);
        }

        // listen to the user choosing the date in the date-picker and display the date in the format YYYY-MM-DD.
        date_field.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar c = Calendar.getInstance();
                int year = c.get(Calendar.YEAR);
                int month = c.get(Calendar.MONTH);
                int day = c.get(Calendar.DAY_OF_MONTH);
                DatePickerDialog datePickerDialog = new DatePickerDialog(
                        ManageItemActivity.this,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                                date_field.setText(year + "-" + (monthOfYear + 1) + "-" + dayOfMonth);
                                date_field.setTextColor(ContextCompat.getColor(ManageItemActivity.this, R.color.black));
                            }
                        },
                        year, month, day);
                datePickerDialog.show();
            }
        });

        // the 2 listeners below listen to the user's keyboard. Add a dollar sign at the front after they click the "Enter", "Done", "Next" or any unspecified key.
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

        // navigate the user back to home screen when they click on the left arrow.
        nav_back_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        // opens device's photo gallery when the user clicks on the "Gallery" button
        open_gallery_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pickImageFromGallery();
            }
        });

        save_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isInputValid()) {
                    // Perform actions when all input fields are valid
                }
            }
        });
    }

    private void pickImageFromGallery() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("images/*");
        intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
        startActivityForResult(intent, 1);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == RESULT_OK && data != null && data.getClipData() != null) {
            int count = data.getClipData().getItemCount();
            for (int i = 0; i < count; i++) {
                imageUri = data.getClipData().getItemAt(i).getUri();
                sliderDataArrayList.add(new ImageSliderData(imageUri));
                adapter = new ImageSliderAdapter(this, sliderDataArrayList);
                sliderView.setSliderAdapter(adapter);
            }
        }
    }

    private boolean isInputValid() {
        boolean isValid = true;

        if (item_name_field.getText().toString().isEmpty()) {
            item_name_field.setError("Item name is required");
            isValid = false;
        }

        if (serial_number_field.getText().toString().isEmpty()) {
            serial_number_field.setError("Serial number is required");
            isValid = false;
        }

        if (tag_input.getText().toString().isEmpty()) {
            tag_input.setError("Tag is required");
            isValid = false;
        }

        if (description_field.getText().toString().isEmpty()) {
            description_field.setError("Description is required");
            isValid = false;
        }

        if (make_field.getText().toString().isEmpty()) {
            make_field.setError("Make is required");
            isValid = false;
        }

        if (model_field.getText().toString().isEmpty()) {
            model_field.setError("Model is required");
            isValid = false;
        }

        if (date_field.getText().toString().isEmpty()) {
            date_field.setError("Date is required");
            isValid = false;
        }

        if (value_field.getText().toString().isEmpty()) {
            value_field.setError("Date is required");
            isValid = false;
        }

        if (comment_field.getText().toString().isEmpty()) {
            comment_field.setError("Comment is required");
            isValid = false;
        }

        return isValid;
    }
}
