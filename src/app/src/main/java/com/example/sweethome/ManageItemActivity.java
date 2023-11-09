package com.example.sweethome;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.Manifest;

import java.io.ByteArrayOutputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import android.app.DatePickerDialog;
import android.widget.DatePicker;
import android.widget.Toast;

import com.google.firebase.Timestamp;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.smarteist.autoimageslider.SliderView;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

import javax.annotation.Nullable;

public class ManageItemActivity extends AppCompatActivity {
    FirebaseStorage imageStorage = FirebaseStorage.getInstance();
    StorageReference imageStorageRef = imageStorage.getReference();
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    private CustomAddTagsField add_tags_field;
    private LinearLayout tags_container;
    private TextView date_field;
    private EditText value_field;
    private ImageView nav_back_button;
    private String name;
    private String description;
    private String make;
    private String model;
    private String serialNumber;
    private Double estimatedValue;
    private String purchaseDate;
    private String comment;
    private ArrayList<Uri> photos;
    private CardView open_gallery_button;
    private CardView open_camera_button;
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
    private RelativeLayout noImagePlaceholder;
    private static final int CAMERA_PERMISSION_REQUEST = 123;
    private SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());

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
        noImagePlaceholder = findViewById(R.id.no_image_placeholder);
        open_gallery_button = findViewById(R.id.open_gallery_button);
        open_camera_button = findViewById(R.id.open_camera_button);
        save_button = findViewById(R.id.check_icon);
        item_name_field = findViewById(R.id.item_name_field);
        serial_number_field = findViewById(R.id.serial_number_field);
        tag_input = findViewById(R.id.tag_input);
        description_field = findViewById(R.id.description_field);
        make_field = findViewById(R.id.make_field);
        model_field = findViewById(R.id.model_field);
        comment_field = findViewById(R.id.comment_field);
        sliderDataArrayList = new ArrayList<>();
        adapter = new ImageSliderAdapter(this, sliderDataArrayList);
        Intent intent = getIntent();

        // check if the user has granted permission to access their camera
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA}, CAMERA_PERMISSION_REQUEST);
        }

        // get item's information sent from home screen.
        if (intent != null) {
            String screen = intent.getStringExtra("screen");
            screen_name.setText(screen);
            if ("View / Edit".equals(screen)) {
                name = intent.getStringExtra("name");
                item_name_field.setText(name);
                description = intent.getStringExtra("description");
                description_field.setText(description);
                make = intent.getStringExtra("make");
                make_field.setText(make);
                model = intent.getStringExtra("model");
                model_field.setText(model);
                serialNumber = intent.getStringExtra("serialNumber");
                serial_number_field.setText(serialNumber);
                estimatedValue = intent.getDoubleExtra("estimatedValue", 0.0);
                value_field.setText(String.valueOf(estimatedValue));
                purchaseDate =  dateFormat.format(new Date(intent.getLongExtra("purchaseDate", 0L)));
                date_field.setText(purchaseDate);
                comment = intent.getStringExtra("comment");
                comment_field.setText(comment);
                photos = intent.getParcelableArrayListExtra("photos");
            }
        }

        // populate the image slider if the item is associated with at least 1 photo.
        if (photos != null) {
            for (int i = 0; i < photos.size(); i++) {
                sliderDataArrayList.add(new ImageSliderData(photos.get(i)));
            }
            adapter.notifyDataSetChanged();
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

        // save item's information and send them to the database
        save_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean isValid = isInputValid();
                if (isValid) {
                    name = item_name_field.getText().toString();
                    description = description_field.getText().toString();
                    make = make_field.getText().toString();
                    model = model_field.getText().toString();
                    serialNumber = serial_number_field.getText().toString();
                    estimatedValue = Double.parseDouble(value_field.getText().toString().replace("$", ""));
                    purchaseDate = date_field.getText().toString();
                    comment = comment_field.getText().toString();
                    try {
                        Date parsedPurchaseDate = dateFormat.parse(purchaseDate);
                        Timestamp purchaseDateTS = new Timestamp(parsedPurchaseDate);
                        Item newItem = new Item(name, description, make, model, serialNumber, estimatedValue, purchaseDateTS, comment);
                        db.collection("items").add(newItem);
                        if ("Add Item".equals(screen_name.getText().toString())) {
                            Toast.makeText(ManageItemActivity.this, "Successfully added new item.", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(ManageItemActivity.this, "Successfully updated item.", Toast.LENGTH_SHORT).show();
                        }
                        Intent intent = new Intent(ManageItemActivity.this, MainActivity.class);
                        startActivity(intent);
                    } catch (ParseException err) {
                        System.out.println("ERROR: invalid date format used.");
                    }
                }
            }
        });

        // open device's camera when the user clicks on the open camera button
        open_camera_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                takePhotoWithCamera();
            }
        });
    }

    // open device's gallery to select 1 or multiple image(s)
    private void pickImageFromGallery() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("image/*");
        intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
        startActivityForResult(intent, 1);
    }

    // open device's camera to take a photo
    private void takePhotoWithCamera() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent, 2);
    }

    // convert photo's bitmap to an image uri to store in sliderDataArrayList
    private Uri getImageUriFromBitmap(Bitmap bitmap) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(getContentResolver(), bitmap, "Title", null);
        return Uri.parse(path);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == CAMERA_PERMISSION_REQUEST) {
            if (grantResults.length <= 0 || grantResults[0] != PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "Please allow access to your camera.", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == RESULT_OK && data != null) {
            if (data.getData() != null) {
                imageUri = data.getData();
                sliderDataArrayList.add(new ImageSliderData(imageUri));
            } else if (data.getClipData() != null) {
                int count = data.getClipData().getItemCount();
                for (int i = 0; i < count; i++) {
                    imageUri = data.getClipData().getItemAt(i).getUri();
                    sliderDataArrayList.add(new ImageSliderData(imageUri));
                }
            }
            adapter.notifyDataSetChanged();
            sliderView.setSliderAdapter(adapter);
            noImagePlaceholder.setVisibility(View.GONE);
            sliderViewFrame.setVisibility(View.VISIBLE);
        } else if (requestCode == 2 && resultCode == RESULT_OK && data != null) {
            Bundle extras = data.getExtras();
            if (extras != null) {
                Bitmap photo = (Bitmap) extras.get("data");
                imageUri = getImageUriFromBitmap(photo);
                if (imageUri != null) {
                    sliderDataArrayList.add(new ImageSliderData(imageUri));
                    adapter.notifyDataSetChanged();
                }
                sliderView.setSliderAdapter(adapter);
                noImagePlaceholder.setVisibility(View.GONE);
                sliderViewFrame.setVisibility(View.VISIBLE);
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

        if (tags_container.getChildCount() <= 0) {
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
            value_field.setError("Value is required");
            isValid = false;
        }

        if (comment_field.getText().toString().isEmpty()) {
            comment_field.setError("Comment is required");
            isValid = false;
        }

        return isValid;
    }
}
