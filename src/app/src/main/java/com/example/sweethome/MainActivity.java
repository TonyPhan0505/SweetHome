package com.example.sweethome;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import android.content.Intent;
import android.view.View;
import android.widget.Button;

import com.google.firebase.Timestamp;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Button to open Edit Item screen
        Button editItemButton = findViewById(R.id.editItemButton);
        editItemButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, ManageItemActivity.class);
                Item itemToUpdate = new Item("Y5uWegAYWmCwMeBcyNKw", "Robot", "A smart robot.", "Google", "Bot2", "RGB123", 10000, Timestamp.now(), "Train it so it can get smarter.", new ArrayList<>());
                intent.putExtra("screen", "View / Edit");
                intent.putExtra("id", itemToUpdate.getItemId());
                intent.putExtra("name", itemToUpdate.getName());
                intent.putExtra("description", itemToUpdate.getDescription());
                intent.putExtra("make", itemToUpdate.getMake());
                intent.putExtra("model", itemToUpdate.getModel());
                intent.putExtra("serialNumber", itemToUpdate.getSerialNumber());
                intent.putExtra("estimatedValue", itemToUpdate.getEstimatedValue());
                intent.putExtra("purchaseDate", itemToUpdate.getPurchaseDate().toDate());
                intent.putExtra("comment", itemToUpdate.getComment());
                intent.putExtra("photos", itemToUpdate.getPhotos());
                startActivity(intent);
            }
        });
    }
}