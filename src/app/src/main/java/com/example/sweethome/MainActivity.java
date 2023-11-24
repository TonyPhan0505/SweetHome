package com.example.sweethome;
/**
 * MainActivity
 *
 * This class serves as an entry point to the SweetHome application. It extends the
 * AppCompatActivity class and handles the main screen of the application. From here, users can
 * interact with various features in the app.
 * <p>The “screen” extra can be set to “Add Item” or “View / Edit” to indicate the desired screen
 * mode in the {@link com.example.sweethome.ManageItemActivity}.</p>
 *
 * November 10, 2023
 *
 * Sources: https://www.geeksforgeeks.org/how-to-delete-data-from-firebase-firestore-in-android/
 *
 */

/* necessary imports */

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.util.Pair;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentContainerView;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.Date;
import java.util.concurrent.atomic.AtomicInteger;

public class MainActivity extends AppCompatActivity implements IFilterable {
    /* attributes of this class */
    private ArrayList<Item> itemList;
    private ArrayList<Item> itemListCopy;
    private ListView itemListView;
    private TextView totalEstimatedValue;
    private ItemsCustomAdapter itemAdapter;
    private FirebaseFirestore db;
    private CollectionReference itemsRef;
    private CollectionReference tagsRef;
    private Spinner sortSpinner;
    private Spinner tagFilterSpinner;
    private Button addItemButton;
    private ArrayAdapter<CharSequence> sortAdapter;
    private ArrayAdapter<String> tagFilterAdapter;
    private ArrayList<Item> selectedItems;
    private PopupWindow popupWindow;
    private boolean isPanelShown = false; // keep track of action panel visibility
    final Context context = this;
    private FragmentContainerView fragmentContainer;
    private LinearLayout filterPanel;
    private ImageView filterIcon;
    private Button filterApplyButton;
    private Button createTagButton;
    private EditText keywordField;
    private EditText makeField;
    private TextView calendar_data;
    private static final String PREF_NAME = "DateRangePrefs";
    private static final String START_DATE_KEY = "startDate";
    private static final String END_DATE_KEY = "endDate";
    // Declare MaterialDatePicker as a field
    private MaterialDatePicker<Pair<Long, Long>> dateRangePicker;
    private Long selectedStartDate;
    private Long selectedEndDate;
    private boolean filtered;
    /* constants */
    private final long ONE_DAY = 86400000;
    private final long ONE_HOUR = 3600000;
    private final long ONE_SECOND = 1000;
    private static final int CAMERA_PERMISSION_REQUEST = 123;
    private AppContext app;
    private ArrayList<String> tagsList = new ArrayList<>();
    private String selectedTagForFiltering = "All";
    private TextView selected_filtering_tag_field;
    private Bundle savedInst;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        savedInst = savedInstanceState;
        app = (AppContext) getApplication();

        // check if the user has granted permission to access their camera
        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA}, CAMERA_PERMISSION_REQUEST);
        }

        /* set up a connection to our db and references to the items and tags collection */
        db = FirebaseFirestore.getInstance();
        itemsRef = db.collection("items");
        tagsRef = db.collection("tags");

        /* set up our list of items, find the list on our frontend layout, and set the corresponding array adapter */
        itemList = new ArrayList<Item>();
        itemListView = findViewById(R.id.item_list);
        itemAdapter = new ItemsCustomAdapter(this, itemList);
        itemListView.setAdapter(itemAdapter);

        /* Retrieve all existing items(if there are any) from Firestore Database */
        getAllItemsFromDatabase(itemsRef);

        /* setup the sorting spinner */
        sortSpinner = findViewById(R.id.spinner_sort_options);
        sortAdapter = ArrayAdapter.createFromResource(this, R.array.sort_options, android.R.layout.simple_spinner_dropdown_item);
        sortAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sortSpinner.setAdapter(sortAdapter);

        /* sorting spinner selection listener */
        sortSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                // Handle sorting based on selection
                String selectedSortOption = parentView.getItemAtPosition(position).toString();
                sortDataList(selectedSortOption, itemList, itemAdapter, getApplicationContext()); // Sort and load data based on the selected option
            }
            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                //do nothing here
            }
        });

        /* Retrieve all existing tags(if there are any) from Firestore Database */
        getAllTagsFromDatabase(tagsRef);

        /* setup the filter by tag spinner */
        selected_filtering_tag_field = findViewById(R.id.selected_filtering_tag_field);
        tagFilterSpinner = findViewById(R.id.tag_filter_field);
        tagFilterAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, tagsList);
        tagFilterAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        tagFilterSpinner.setAdapter(tagFilterAdapter);

        /* filter by tag spinner listener */
        tagFilterSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                // Handle filtering by tag based on selection
                String selectedTagToFilterBy = parentView.getItemAtPosition(position).toString();
                selectedTagForFiltering = selectedTagToFilterBy;
                selected_filtering_tag_field.setText(selectedTagForFiltering);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                //do nothing here
            }
        });

        addItemButton = findViewById(R.id.add_button);
        addItemButton.setOnClickListener(view -> {
            Intent intent = new Intent(MainActivity.this, ManageItemActivity.class);
            intent.putExtra("screen", "Add Item");
            startActivity(intent);
        });

        /* find our frontend elements for filtering */
        filterPanel = findViewById(R.id.filter_panel);
        filterIcon = findViewById(R.id.filter_button);
        filterApplyButton = findViewById(R.id.apply_filter_button);
        fragmentContainer = findViewById(R.id.fragment_container_view);
        makeField = findViewById(R.id.make_field);
        keywordField = findViewById(R.id.keyword_field);

        /* set the view of the filter panel and onclicklisteners for the icon and button */
        filterPanel.setVisibility(View.GONE); //should be invisible until the filterIcon is pressed
        fragmentContainer.setVisibility(View.GONE);
        filterIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (filterPanel.getVisibility() == View.VISIBLE) { //if the panel is already visible
                    filterIcon.setColorFilter(ContextCompat.getColor(MainActivity.this, R.color.black), PorterDuff.Mode.SRC_IN);
                    filterPanel.setVisibility(View.GONE); //then make it invisible
                    /* clear the edit texts for the next time the user uses the panel */
                    makeField.setText("");
                    keywordField.setText("");
                    getAllItemsFromDatabase(itemsRef); //also clear all of the filters
                } else {
                    filterIcon.setColorFilter(ContextCompat.getColor(MainActivity.this, R.color.light_grey), PorterDuff.Mode.SRC_IN);
                    filterPanel.setVisibility(View.VISIBLE); //otherwise just show the panel since it must currently be invisible
                    filtered = false; //set the filtered flag as false
                    selectedStartDate = 0L; //restart the start day
                    selectedEndDate = 0L; //restart the end day
                    dateRangePicker = createMaterialDatePicker(); //reset the picker
                }
            }
        });
        filterApplyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String make = makeField.getText().toString();
                String keyword = keywordField.getText().toString();
                if (filtered) { //if it has been previously filtered, reset the list
                    itemList.clear();
                    itemList.addAll(itemListCopy);
                } else { //otherwise if it has not been filtered, make a copy so we can reset the list later
                    itemListCopy = new ArrayList<Item>();
                    itemListCopy.addAll(itemList);
                }
                if (!make.trim().isEmpty()){ //apply filter by make if it was provided
                    filterByMake(make);
                    filtered = true;
                }
                if(!keyword.trim().isEmpty()) { //apply filter by keyword if it was provided
                    filterByKeyword(keyword);
                    filtered = true;
                }
                if(selectedEndDate!=0L && selectedStartDate!=0L) { //apply filter by date range if it was selected
                    /* get the selected dates by the user, adding extra time due to epoch conversion error */
                    Date dateStart = new Date(selectedStartDate + ONE_DAY - ((ONE_DAY / 4) *3) + ONE_HOUR);
                    Date dateEnd = new Date(selectedEndDate + ONE_DAY + (ONE_DAY / 4) + ONE_HOUR - ONE_SECOND);
                    /* let the user know the time constraints */
                    Toast.makeText(MainActivity.this, "START: " + dateStart.toString() + " END: " + dateEnd.toString(), Toast.LENGTH_SHORT).show();
                    /* convert them to timestamps like our items store purchaseDate as */
                    Timestamp start = new Timestamp(dateStart);
                    Timestamp end = new Timestamp(dateEnd);
                    filterByDate(start, end);
                    filtered = true;
                }
                if (!selectedTagForFiltering.equals("All")) {
                    filterByTag();
                }
                if(make.trim().isEmpty() && keyword.trim().isEmpty() && (selectedEndDate==0L || selectedStartDate==0L) && selectedTagForFiltering.equals("All")) { //if apply filter was selected but nothing is inputted
                    getAllItemsFromDatabase(itemsRef);
                }
            }
        });

        // Button to open Edit Item screen
        Button addItemButton = findViewById(R.id.add_button);
        addItemButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, ManageItemActivity.class);
                intent.putExtra("screen", "Add Item");
                startActivity(intent);
            }
        });

        // Initialize the date range values from SharedPreferences
        displaySavedDateRange();

        // Update the button text with the saved date range
        calendar_data = findViewById(R.id.calendar_data);
        updateCalendar(calendar_data, selectedStartDate, selectedEndDate);

        // Date range picker
        ImageView openCalendarButton = findViewById(R.id.calendar_button);

        // Set an OnClickListener to handle the button click
        openCalendarButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Create the MaterialDatePicker if not already created
                if (dateRangePicker == null) {
                    dateRangePicker = createMaterialDatePicker();
                }
                // Show the date range picker
                dateRangePicker.show(getSupportFragmentManager(), dateRangePicker.toString());
            }
        });

        final FloatingActionButton tagActionButton = findViewById(R.id.tag_action_button);
        tagActionButton.setOnClickListener(view -> {
            if (popupWindow == null) {
                setUpActionButtonPanel();
            }
            if (!popupWindow.isShowing()) {
                setUpActionButtonPanel();
                showPanel(view);
            } else {
                hidePanel();
            }
        });


        final FloatingActionButton deleteActionButton = findViewById(R.id.delete_action_button);
        deleteActionButton.setOnClickListener(view -> {
            selectedItems = new ArrayList<Item>();
            for (int i = 0; i < itemListView.getCount(); i++) {
                Item item = itemAdapter.getItem(i);
                Boolean selected = item.isSelected();
                if (item != null && selected) {
                    selectedItems.add(item);
                }
            }
            final Dialog deleteDialog = new Dialog(context);
            deleteDialog.setContentView(R.layout.delete_popup);
            Button deleteButton = (Button) deleteDialog.findViewById(R.id.delete_button);
            Button cancelDeleteButton = (Button) deleteDialog.findViewById(R.id.cancel_delete);
            deleteButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    deleteItems(MainActivity.this, selectedItems, itemsRef);
                    if (filterPanel.getVisibility() == View.VISIBLE) { //if the filter panel is visible
                        filterPanel.setVisibility(View.GONE); //then make it invisible
                        /* clear the edit texts for the next time the user uses the panel */
                        makeField.setText("");
                        keywordField.setText("");
                        getAllItemsFromDatabase(itemsRef); //also clear all of the filters
                    }
                    deleteDialog.dismiss();
                }
            });

            cancelDeleteButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    deleteDialog.dismiss();
                }
            });

            if (selectedItems.size() > 0) {
                deleteDialog.show();
            } else {
                Toast.makeText(MainActivity.this, "Please select at least 1 item.", Toast.LENGTH_SHORT).show();
            }
        });

        /* listen for changes in the collection and update our list of items accordingly */
        itemsRef.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                if (error != null) {
                    Log.e("Firestore", error.toString()); //if there was any error, log it
                }
                if (value != null) {
                    getAllItemsFromDatabase(itemsRef); //otherwise get all items currently in the items collection and display them in our list
                }
            }
        });

        /* listen for changes in the collection and update our list of tags accordingly */
        tagsRef.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                if (error != null) {
                    Log.e("Firestore", error.toString()); //if there was any error, log it
                }
                if (value != null) {
                    getAllTagsFromDatabase(tagsRef); //otherwise get all relevant tags currently in the items collection and display them in our list
                }
            }
        });
    }

    @Override
    protected void onStop() {
        getApplicationContext().getCacheDir().delete();
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        calendar_data.setText("");
        filterPanel.setVisibility(View.GONE);
        makeField.setText("");
        keywordField.setText("");
        getApplicationContext().getCacheDir().delete();
        super.onDestroy();
    }

    // Create the MaterialDatePicker with optional initial range
    private MaterialDatePicker<Pair<Long, Long>> createMaterialDatePicker() {
        MaterialDatePicker<Pair<Long, Long>> builder = MaterialDatePicker.Builder.dateRangePicker().build();

        // Set a listener for when the user confirms the date range
        builder.addOnPositiveButtonClickListener(selection -> {
            // Get the selected date range
            selectedStartDate = selection.first;
            selectedEndDate = selection.second;
            // Save the selected date range
            saveDateRange(selectedStartDate, selectedEndDate);
            // Update the button text
            updateCalendar(findViewById(R.id.calendar_data), selectedStartDate, selectedEndDate);
        });
        return builder;
    }

    private void saveDateRange(Long startDate, Long endDate) {
        SharedPreferences.Editor editor = getSharedPreferences(PREF_NAME, MODE_PRIVATE).edit();
        editor.putLong(START_DATE_KEY, startDate);
        editor.putLong(END_DATE_KEY, endDate);
        editor.apply();
    }

    private void displaySavedDateRange() {
        SharedPreferences sharedPreferences = getSharedPreferences(PREF_NAME, MODE_PRIVATE);
        selectedStartDate = sharedPreferences.getLong(START_DATE_KEY, 0);
        selectedEndDate = sharedPreferences.getLong(END_DATE_KEY, 0);

        // Display the saved date range on the button
        updateCalendar(findViewById(R.id.calendar_data), selectedStartDate, selectedEndDate);
    }

    private void updateCalendar(TextView calendar_data, Long startDate, Long endDate) {
        if (startDate != 0 && endDate != 0) {
            // Format the date range string
            String formattedDateRange = formatDateRange(startDate, endDate);
            calendar_data.setText(formattedDateRange);
        }
    }

    private String formatDateRange(Long startDate, Long endDate) {
        // You can customize the date format as needed
        // This is just an example format, adjust it based on your preference
        return String.format("%tF - %tF", startDate, endDate);
    }

    private void hidePanel() {
//        if (popupWindow != null && popupWindow.isShowing()) {
//            isPanelShown = false;
            Toast.makeText(MainActivity.this, "false", Toast.LENGTH_SHORT).show();
            popupWindow.dismiss();
//        }
    }

    private void showPanel(View view) {
////        if (popupWindow != null) {
//            isPanelShown = true;
            view.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED);
            int xOffset = view.getMeasuredWidth() - view.getWidth();
//            Toast.makeText(MainActivity.this, "true", Toast.LENGTH_SHORT).show();
//            popupWindow.showAsDropDown(view, xOffset, -view.getHeight()); // Fix overlapping display
                popupWindow.showAtLocation(findViewById(android.R.id.content).getRootView(), 10, 250, 720);
//        } else {
//            isPanelShown = false;
//        }
    }

    private void setUpActionButtonPanel() {
        // inflate layout for panel with 3 buttons
        @SuppressLint("InflateParams") View panelView = LayoutInflater.from(this).inflate(R.layout.action_button_panel, null);

        // create PopupWindow
        popupWindow = new PopupWindow(panelView, ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT, true);
        popupWindow.setOutsideTouchable(false);

        createTagButton = panelView.findViewById(R.id.create_tag_panel);
        createTagButton.setOnClickListener(view -> {
            hidePanel();
            fragmentContainer.setVisibility(View.VISIBLE);
            if (savedInst == null) {
                Bundle arg = new Bundle();
                arg.putString("USER", app.getUsername());

                CreateTagFragment ctFragment = new CreateTagFragment();
//                FragmentManager fragmentManager = getSupportFragmentManager();
//                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
//                fragmentTransaction.add(R.id.fragmentContainerView, ctFragment, null)
//                        .addToBackStack(null)
//                        .commit();
                ctFragment.setArguments(arg);
                getSupportFragmentManager().beginTransaction()
                        .setReorderingAllowed(true)
                        .replace(R.id.fragment_container_view, ctFragment)
                        .commit();
            }
        });
    }

    /**
     * Delete item/items
     * @param items list of item to delete
     */
    public static void deleteItems(final Context context, ArrayList<Item> items, CollectionReference itemsRef) {
        int totalItems = items.size();
        AtomicInteger deletedItemsCount = new AtomicInteger(0);
        for (Item item: items) {
            ArrayList<String> photos = item.getPhotos();
            for (String photoUrl : photos) {
                StorageReference storageReference = FirebaseStorage.getInstance().getReferenceFromUrl(photoUrl);
                storageReference.delete().addOnSuccessListener(aVoid -> {
                    Log.d("Firestore", "Photo successfully deleted!");
                })
                .addOnFailureListener(exception -> {
                    Log.e("Firestore", "Photo deleted failed!");
                });
            }
            itemsRef.document(item.getItemId())
                    .delete()
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void unused) {
                            Log.d("Firestore", "DocumentSnapshot successfully deleted!");
                            int count = deletedItemsCount.incrementAndGet();
                            if (count == totalItems) {
                                // All items are deleted, show toast
                                Toast.makeText(context, "Deleted successfully.", Toast.LENGTH_SHORT).show();
                            }
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Log.e("Firestore", "DocumentSnapshot deleted failed!");
                            Toast.makeText(context, "Failed to delete.", Toast.LENGTH_SHORT).show();
                        }
                    });
        }
    }

    /**
     * Given a selected sorting option, sorts the current item
     * list according to the selected criteria.
     * @param selectedSortOption
     * @param itemList
     * @param itemAdapter
     * @param context
     */
    public static void sortDataList(String selectedSortOption, ArrayList<Item> itemList, ItemsCustomAdapter itemAdapter, Context context) {
        if (selectedSortOption.equals(context.getString(R.string.sort_least_recent))) { //if we are sorting items by oldest to newest acquired
            itemList.sort((item1, item2) -> item1.getPurchaseDate().compareTo(item2.getPurchaseDate()));
        }
        else if (selectedSortOption.equals(context.getString(R.string.sort_most_recent))) { //if we are sorting items by newest to oldest acquired
            itemList.sort((item1, item2) -> item2.getPurchaseDate().compareTo(item1.getPurchaseDate()));
        }
        else if (selectedSortOption.equals(context.getString(R.string.sort_highest_value))) { //if we are sorting items by highest to lowest value
            itemList.sort((item1, item2) -> Double.compare(item2.getEstimatedValue(), item1.getEstimatedValue()));
        }
        else if (selectedSortOption.equals(context.getString(R.string.sort_lowest_value))) { //if we are sorting items by lowest to highest value
            itemList.sort((item1, item2) -> Double.compare(item1.getEstimatedValue(), item2.getEstimatedValue()));
        }
        else if (selectedSortOption.equals(context.getString(R.string.sort_make_az))) { //if we are sorting items by make alphabetically
            itemList.sort((item1, item2) -> item1.getMake().compareTo(item2.getMake()));
        }
        else if (selectedSortOption.equals(context.getString(R.string.sort_make_za))) { //if we are sorting items by make reverse alphabetically
            itemList.sort((item1, item2) -> item2.getMake().compareTo(item1.getMake()));
        }
        else if (selectedSortOption.equals(context.getString(R.string.sort_description_az))) { //if we are sorting items by description reverse alphabetically
            itemList.sort((item1, item2) -> item1.getDescription().compareTo(item2.getDescription()));
        }
        else if (selectedSortOption.equals(context.getString(R.string.sort_description_za))) { //if we are sorting items by description reverse alphabetically
            itemList.sort((item1, item2) -> item2.getDescription().compareTo(item1.getDescription()));
        } else if (selectedSortOption.equals(context.getString(R.string.sort_tags_az))) {
            itemList.sort((item1, item2) -> item1.getTags().get(0).compareTo(item2.getTags().get(0)));
        } else if (selectedSortOption.equals(context.getString(R.string.sort_tags_za))) {
            itemList.sort((item1, item2) -> item2.getTags().get(0).compareTo(item1.getTags().get(0)));
        }
        itemAdapter.notifyDataSetChanged(); //notify changes were made to update frontend
    }

    /**
     * Gets all of the items in the items collection
     * and updates the frontend to display them in the list
     * @param itemsRef
     */
    private void getAllItemsFromDatabase(CollectionReference itemsRef) {
        itemsRef.get()
            .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                @Override
                public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                    itemList.clear(); //clear whatever data we currently have stored in our item list
                    for (QueryDocumentSnapshot doc : queryDocumentSnapshots){ //get everything that is stored in our db at the moment
                        Item item = doc.toObject(Item.class); //convert the contents of each document in the items collection to an item object
                        item.setItemId(doc.getId()); //set the item ID
                        Log.i("Firestore", String.format("Item %s fetched", item.getName())); //log the name of the item we successfully got from the db
                        itemList.add(item); //add the item object to our item list
                    }
                    String currentSortOption = sortSpinner.getSelectedItem().toString(); //get the currently selected sort option
                    sortDataList(currentSortOption, itemList, itemAdapter, getApplicationContext()); //sort the list accordingly and notify changes were made to update frontend
                    calculateTotalEstimatedValue(); //recalculate and display the total estimated value
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Log.e("Firestore", "Error fetching data", e);
                }
            });
    }

    private void getAllTagsFromDatabase(CollectionReference tagsRef) {
        tagsRef.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                tagsList.clear();
                tagsList.add("All");
                for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                    ArrayList<String> usernames = (ArrayList<String>) documentSnapshot.get("usernames");
                    if (usernames.contains(app.getUsername())) {
                        String name = documentSnapshot.getString("name");
                        tagsList.add(name);
                    }
                }
                tagFilterAdapter.notifyDataSetChanged();
            }
        });
    }

    /**
     * Given a start date and end date, filters the current item list
     * accordingly (ie. keeps items between start and end INCLUSIVE).
     * @param startDate
     * @param endDate
     */
    public void filterByDate(Timestamp startDate, Timestamp endDate) {
        ArrayList<Item> filteredList = new ArrayList<Item>(); //a new list to store the items that are being filtered out
        for (int i = 0; i < itemList.size(); i++) { //for every item in the current list
            Item item = itemList.get(i); //get the item
            Timestamp purchaseDate = item.getPurchaseDate(); //get the purchase date of the item
            if (purchaseDate.toDate().before(startDate.toDate()) || purchaseDate.toDate().after(endDate.toDate())) { //if the purchase date does not fall within the given date range
                filteredList.add(item); //add it to the filtered list
            }
        }
        itemList.removeAll(filteredList); //remove all items that are to be filtered out from our current list ie. were not purchased in the provided time frame
        itemAdapter.notifyDataSetChanged(); //notify changes were made to update frontend
        calculateTotalEstimatedValue(); //recalculate and display the total estimated value
    }

    /**
     * Given a make, filters the current item list
     * accordingly (ie. keeps items with the specified make).
     * @param make
     */
    public void filterByMake(String make) {
        ArrayList<Item> filteredList = new ArrayList<Item>(); //a new list to store the items that are being filtered out
        for (int i = 0; i < itemList.size(); i++) { //for every item in the current list
            Item item = itemList.get(i); //get the item
            String itemMake = item.getMake(); //get the make of the item
            if (!itemMake.equalsIgnoreCase(make)) { //if it DOES NOT match the given make (case insensitive)
                filteredList.add(item); //add it to the filtered list
            }
        }
        itemList.removeAll(filteredList); //remove all items that are to be filtered out from our current list ie. don't match the given make
        itemAdapter.notifyDataSetChanged(); //notify changes were made to update frontend
        calculateTotalEstimatedValue(); //recalculate and display the total estimated value
    }

    /**
     * Given a description keyword, filters the current item list
     * accordingly (ie. keeps items with the specified keyword).
     * @param keyword
     */
    public void filterByKeyword(String keyword) {
        keyword = keyword.toLowerCase(); //change the keyword to lowercase so we can be case insensitive
        ArrayList<Item> filteredList = new ArrayList<Item>(); //a new list to store the items that are being filtered out
        for (int i = 0; i < itemList.size(); i++) { //for every item in the current list
            Item item = itemList.get(i); //get the item
            String description = item.getDescription().toLowerCase(); //get the description of the item (also lowercase)
            if (!description.contains(keyword)) { //if it DOES NOT contain the given keyword
                filteredList.add(item); //add it to the filtered list
            }
        }
        itemList.removeAll(filteredList); //remove all items from the current list that are to be filtered out ie. don't contain the given keyword in their description
        itemAdapter.notifyDataSetChanged(); //notify changes were made to update frontend
        calculateTotalEstimatedValue(); //recalculate and display the total estimated value
    }

    /**
     * Given a tag, filters the current item list accordingly
     * (ie. keeps items associated with the specified tag).
     */
    public void filterByTag() {
        if (!selectedTagForFiltering.equals("All")) {
            ArrayList<Item> filteredList = new ArrayList<Item>();
            for (int i = 0; i < itemList.size(); i++) {
                Item item = itemList.get(i);
                ArrayList<String> tagsOfItem = item.getTags();
                if (!tagsOfItem.contains(selectedTagForFiltering)) {
                    filteredList.add(item);
                }
            }
            itemList.removeAll(filteredList);
            itemAdapter.notifyDataSetChanged();
            calculateTotalEstimatedValue();
        }
    }

    /**
     * Calculates the total estimated value of all the items currently in the list
     * and updates the frontend to display the correct total
     */
    private void calculateTotalEstimatedValue() {
        double total = 0.00; //initialize the total amount of the estimated value to zero
        for (int i = 0; i < itemList.size(); i++) { //for every item on our list
            Item item = (Item) itemList.get(i); //get the item
            total += item.getEstimatedValue(); //add the estimated value of the current item to the total
        }
        totalEstimatedValue = findViewById(R.id.total_estimated_value_footer); //find our total estimated value textview from our frontend layout
        String totalText = String.format("%.2f", total); //format the total we calculated as a string
        totalEstimatedValue.setText(this.getString(R.string.total) + totalText); //and updated our frontend to display the updated amount
    }

    /**
     * Handles the result of the permission request for accessing the camera.
     *
     * @param requestCode The request code passed to requestPermissions.
     * @param permissions The requested permissions.
     * @param grantResults The grant results for the corresponding permissions.
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == CAMERA_PERMISSION_REQUEST) {
            if (grantResults.length <= 0 || grantResults[0] != PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "Please allow access to your camera.", Toast.LENGTH_SHORT).show();
            }
        }
    }

    public void closeFragment() {
        fragmentContainer.setVisibility(View.GONE);
    }

}