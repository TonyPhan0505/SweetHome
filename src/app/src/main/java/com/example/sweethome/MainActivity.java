package com.example.sweethome;
/*
 * MainActivity
 *
 * This class controls the main activity of our SweetHome app.
 *
 * October 28, 2023
 *
 * Sources:
 *
 */

/* necessary imports */
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
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

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    /* attributes of this class */
    private ArrayList<Item> dataList;
    private ListView itemList;
    private TextView totalEstimateValue;
    private ItemsCustomAdapter itemAdapter;
    private FirebaseFirestore db;
    private CollectionReference itemsRef;
    private Spinner sortSpinner;
    private ArrayAdapter<String> sortAdapter;


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

        /* setup the Sort Spinner*/
        sortSpinner = findViewById(R.id.spinner_sort_options);
        sortAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, getResources().getStringArray(R.array.sort_options));
        sortAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sortSpinner.setAdapter(sortAdapter);

        // Spinner selection listener
        sortSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                // Handle sorting based on selection
                //TODO: sortDataList(position);
                String selectedSortOption = parentView.getItemAtPosition(position).toString();
                Toast.makeText(MainActivity.this, "Selected: " + selectedSortOption, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // Do nothing here
            }
        });

        /* find our add button on the frontend and set an onclicklistener for it */
        final FloatingActionButton addButton = findViewById(R.id.add_button);
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast add = Toast.makeText(getApplicationContext(), R.string.no_adding_msg, Toast.LENGTH_LONG); //make a little temporary message on the bottom to tell users we aren't adding right now
                add.show(); //show the message
            }
        });

        /* listen for changes in the collection and update our list of items accordingly */
        itemsRef.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                if (error != null) {
                    Log.e("Firestore",error.toString()); //if there was any error, log it
                }
                if (value != null) {
                    getAllItems(); //otherwise get all items currently in the items collection and display them in our list
                }
            }
        });
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
     * Gets all of the items in the items collection
     * and updates the frontend to display them in the list
     * order: oldest added items at the top
     */
    private void getAllItems(){
        itemsRef.get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        dataList.clear(); //clear whatever data we currently have stored in our item list
                        for (QueryDocumentSnapshot doc : queryDocumentSnapshots){ //get everything that is stored in our db at the moment
                            Item item = doc.toObject(Item.class); //convert the contents of each document in the items collection to an item object
                            Log.i("Firestore", String.format("Item %s fetched", item.getName())); //log the name of the item we successfully got from the db
                            dataList.add(item); //add the item object to our item list
                        }
                        itemAdapter.notifyDataSetChanged(); //notify changes were made to update frontend
                        totalEstimateValue(); //recalculate and display the total estimated value
                    }
                });
    }

    /**
     * Calculates the total estimated value of all the items in the items list
     * and updates the frontend to display the correct total
     */
    private void totalEstimateValue() {
        double total = 0.00; // initialize the total amount of the estimated value to zero
        for (int i = 0; i < dataList.size(); i++) { // for every item on our list
            Item item = (Item) dataList.get(i); // get the item
            total += item.getEstimatedValue(); // add the estimated value of the current item to the total
        }
        totalEstimateValue = findViewById(R.id.total_estimated_value_footer); // find our total estimated value textview from our frontend layout
        String totalText = String.format("%.2f", total); // format the total we calculated as a string
        totalEstimateValue.setText(this.getString(R.string.total) + totalText); // and updated our frontend to display the updated amount
    }
}