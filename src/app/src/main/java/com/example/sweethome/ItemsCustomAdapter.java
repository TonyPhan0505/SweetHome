package com.example.sweethome;
/*
 * ItemsCustomAdapter
 *
 * This class defines a custom list to hold Item objects
 * and display the content of them on our frontend layout.
 * This is used by our item adapter in the main activity.
 *
 * October 28, 2023
 *
 * Sources:
 *
 */

/* necessary imports */
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.text.SimpleDateFormat;
import java.util.ArrayList;

public class ItemsCustomAdapter extends ArrayAdapter<Item> {
    /* attributes for this class */
    private ArrayList<Item> items;
    private Context context;

    /* constructor for this class */
    public ItemsCustomAdapter(Context context, ArrayList<Item> items) {
        super(context, 0, items);
        this.items = items;
        this.context = context;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View view = convertView;

        /* view attached to the item list content layout that we created */
        if(view == null) {
            view = LayoutInflater.from(context).inflate(R.layout.item_list_content, parent, false);
        }

        /* get the item object in the current position */
        Item item = items.get(position);

        /* find the text views inside the view */
        TextView name = view.findViewById(R.id.item_name);
        TextView purchaseDate = view.findViewById(R.id.item_purchase_date);
        TextView estimatedValue = view.findViewById(R.id.item_estimated_value);

        /* then set them to be the correct corresponding value for each element of the item (ie. name, purchase date, estimated value) */
        name.setText(item.getName());
        SimpleDateFormat df = new SimpleDateFormat(context.getString(R.string.date_format)); //create a new format for the date to be in YYYY/MM/DD format
        String date = df.format(item.getPurchaseDate()); //convert the date to a string in the specified format
        purchaseDate.setText(date);
        String value = String.format("%.2f", item.getEstimatedValue()); //ensure there are only 2 places after the decimal when formatting the string
        estimatedValue.setText(context.getString(R.string.cad_currency) + value); //format the estimated value to include the currency ie. CAD$*.xx

        /* return the view we inflated */
        return view;
    }
}
