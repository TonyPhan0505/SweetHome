package com.example.sweethome;
/*
 * MainActivity
 *
 * This class controls the main activity of our SweetHome app.
 *
 * October 28, 2023
 *
 * Sources: https://www.geeksforgeeks.org/how-to-delete-data-from-firebase-firestore-in-android/
 *
 */

/* necessary imports */
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.Query;
import java.util.ArrayList;
import java.util.Date;

public class MainActivity extends AppCompatActivity {
    /* attributes of this class */
    private ArrayList<Item> dataList;
    private ArrayList<Item> selectedItem;

    private ListView itemList;
    private View itemView;
    private TextView totalValueView;
    private CheckBox itemCheckBox;
    private ItemsCustomAdapter itemAdapter;
    private FirebaseFirestore db;
    private CollectionReference itemsRef;
    private Spinner sortSpinner;
    private ArrayAdapter<String> sortAdapter;
    private ArrayList<Item> selectedItems;
    private PopupWindow popupWindow;
    private boolean isPanelShown = false; // keep track of action panel visibility

    final Context context = this;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        /* set up a connection to our db and a reference to the items collection */
        db = FirebaseFirestore.getInstance();
        itemsRef = db.collection("items");

        /* set up our list of items, find the list on our frontend layout, and set the corresponding array adapter */
        dataList = new ArrayList<Item>();
        itemList = findViewById(R.id.item_list);
        itemAdapter = new ItemsCustomAdapter(this, dataList);
        itemList.setAdapter(itemAdapter);

        /* Retrieve all existing items(if there are any) from Firestore Database */
        itemsRef.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                dataList.clear();
                if (!queryDocumentSnapshots.isEmpty()) {
                    for (QueryDocumentSnapshot doc : queryDocumentSnapshots) {
                        Item item = doc.toObject(Item.class);
                        item.setItemId(doc.getId());
                        Log.i("Firestore", String.format("Item %s fetched", item.getName()));
                        dataList.add(item);
                    }
                    itemAdapter.notifyDataSetChanged();
                    totalEstimatedValue();
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.e("Firestore", "Error fetching sorted data", e);
            }
        });

        /* setup the Sort Spinner*/
        sortSpinner = findViewById(R.id.spinner_sort_options);
        sortAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, getResources().getStringArray(R.array.sort_options));
        sortAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sortSpinner.setAdapter(sortAdapter);

//        setUpActionButtonPanel();

        // Spinner selection listener
        sortSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                // Handle sorting based on selection
                String selectedSortOption = parentView.getItemAtPosition(position).toString();
                Toast.makeText(MainActivity.this, "Selected: " + selectedSortOption, Toast.LENGTH_SHORT).show();
                getAllItems(selectedSortOption); // Sort and load data based on the selected option
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // Do nothing here
            }
        });

        //TODO: Link the add button to ManageItemActivity
//        final Button addItemButton = findViewById(R.id.add_button);
//        addItemButton.setOnClickListener(view -> {
//            Intent i = new Intent(context, ManageItemActivity.class;
//            context.startActivity(i);
//        });

//        itemList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                Item item = dataList.get(position);
//                Intent i = new Intent(context, Tag.class);
//                i.putExtra("item", item); // TODO: make sure that ManageItemActivity implements Serializable
//            }
//        });

        //TODO: Implement the following in the ManageItemActivity
//        Item item = (Item) getIntent().getSerializableExtra("item");

        //TODO: Delete addItem() instances below when add button is implemented
//        addItem(new Item("chair", "this is a wooden chair", "The Brick", "Birch 2000", "12345678", 54.45, new Date(),"No comment"));
//        addItem(new Item("sofa", "this is soft aahh", "The Brick", "Birch 2000", "12345678", 54.45, new Date(),"No comment"));

        /* find our add button on the frontend and set an onclicklistener for it */
        final FloatingActionButton tagActionButton = findViewById(R.id.tag_action_button);
        tagActionButton.setOnClickListener(view -> {
            if (popupWindow == null) {
                setUpActionButtonPanel();
            }
            if (!popupWindow.isShowing()) {
                setUpActionButtonPanel();
                showPanel(view);
//                Toast.makeText(MainActivity.this, "Show", Toast.LENGTH_SHORT).show();
            } else {
                hidePanel();
//                Toast.makeText(MainActivity.this, "Hide", Toast.LENGTH_SHORT).show();
            }
        });

        final FloatingActionButton deleteActionButton = findViewById(R.id.delete_action_button);
        deleteActionButton.setOnClickListener(view -> {
            selectedItems = new ArrayList<Item>();
            for (int i = 0; i < itemList.getCount(); i++) {
                Item item = itemAdapter.getItem(i);
                Boolean select = item.isSelected();
                if (item != null && item.isSelected()) {
                    selectedItems.add(item);
                }
            }
//            new DeleteItemFragment().show(getSupportFragmentManager(), "DELETE ITEM");
            final Dialog deleteDialog = new Dialog(context);
            deleteDialog.setContentView(R.layout.delete_popup);
            Button deleteButton = (Button) deleteDialog.findViewById(R.id.delete_button);
            Button cancelDeleteButton = (Button) deleteDialog.findViewById(R.id.cancel_delete);
            deleteButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    deleteItems(selectedItems);
                    deleteDialog.dismiss();
                }
            });

            cancelDeleteButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    deleteDialog.dismiss();
                }
            });

            deleteDialog.show();

        });

        /* listen for changes in the collection and update our list of items accordingly */
        itemsRef.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                if (error != null) {
                    Log.e("Firestore",error.toString()); //if there was any error, log it
                }
                if (value != null) {
                    getAllItems("Newest"); // Initial load sorted by Newest
                }
            }
        });

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
    }



    /**
     * Adds a new item to the items collection
     * @param item
     */
    private void addItem(Item item){
        itemsRef.add(item) //add the item to our items collection
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        Log.i("Firestore", "db write succeeded"); //log if we were successful in adding the new item
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.e("Firestore", "db write fails"); //log if we were unsuccessful in adding the new item
                    }
                });
    }

    /**
     * Delete item/items
     * @param items list of item to delete
     */
    private void deleteItems(ArrayList<Item> items) {
        for (Item item: items) {
            itemsRef.document(item.getItemId())
                    .delete()
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void unused) {
                            Log.d("Firestore", "DocumentSnapshot successfully deleted!");
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Log.e("Firestore", "DocumentSnapshot deleted Failed!");
                        }
                    });
        }
    }

    /**
     * Gets all of the items in the items collection
     * and updates the frontend to display them in the list
     * order: oldest added items at the top
     */
    private void getAllItems(String sortBy) {
        Query query;

        switch (sortBy) {
            case "Oldest":
                query = itemsRef.orderBy("purchaseDate", Query.Direction.ASCENDING);
                break;
            case "Highest value":
                query = itemsRef.orderBy("estimatedValue", Query.Direction.DESCENDING);
                break;
            case "Lowest value":
                query = itemsRef.orderBy("estimatedValue", Query.Direction.ASCENDING);
                break;
            case "Make: A - Z":
                query = itemsRef.orderBy("make", Query.Direction.ASCENDING);
                break;
            case "Make: Z - A":
                query = itemsRef.orderBy("make", Query.Direction.DESCENDING);
                break;
            case "Description: A - Z":
                query = itemsRef.orderBy("description", Query.Direction.ASCENDING);
                break;
            case "Description: Z - A":
                query = itemsRef.orderBy("description", Query.Direction.DESCENDING);
                break;
            case "Tags: A - Z":
                query = itemsRef.orderBy("tags", Query.Direction.ASCENDING);
                break;
            case "Tags: Z - A":
                query = itemsRef.orderBy("tags", Query.Direction.DESCENDING);
                break;
            default:
                // If none of the specified cases match, default to Newest
                query = itemsRef.orderBy("purchaseDate", Query.Direction.DESCENDING);
                break;
        }

        query.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                dataList.clear();
                if (!queryDocumentSnapshots.isEmpty()) {
                    for (QueryDocumentSnapshot doc : queryDocumentSnapshots) {
                        Item item = doc.toObject(Item.class);
                        item.setItemId(doc.getId());
                        Log.i("Firestore", String.format("Item %s fetched", item.getName()));
                        dataList.add(item);
                    }
                    itemAdapter.notifyDataSetChanged();
                    totalEstimatedValue();
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.e("Firestore", "Error fetching sorted data", e);
            }
        });

    }



    /**
     * Calculates the total estimated value of all the items in the items list
     * and updates the frontend to display the correct total
     */
    private void totalEstimatedValue() {
        double total = 0.00; // initialize the total amount of the estimated value to zero
        for (int i = 0; i < dataList.size(); i++) { // for every item on our list
            Item item = (Item) dataList.get(i); // get the item
            total += item.getEstimatedValue(); // add the estimated value of the current item to the total
        }
        totalValueView = findViewById(R.id.total_estimated_value_footer); // find our total estimated value textview from our frontend layout
        String totalText = String.format("%.2f", total); // format the total we calculated as a string
        totalValueView.setText(this.getString(R.string.total) + totalText); // and updated our frontend to display the updated amount
    }

}