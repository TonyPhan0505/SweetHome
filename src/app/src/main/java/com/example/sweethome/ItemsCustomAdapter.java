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
 * Sources: https://windrealm.org/tutorials/android/listview-with-checkboxes.php
 *
 */

/* necessary imports */
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.material.imageview.ShapeableImageView;

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
        /* get the item object in the current position */
        Item item = items.get(position);

        View view = convertView;
//        ShapeableImageView imageView;
        TextView nameView;
        TextView purchaseDateView;
        TextView estimatedValueView;
        CheckBox itemCheckboxView;


        /* view attached to the item list content layout that we created */
        if(view == null) {
            view = LayoutInflater.from(context).inflate(R.layout.item_list_content, parent, false);

            /* find the text views inside the view */
            nameView = view.findViewById(R.id.item_name);
            purchaseDateView = view.findViewById(R.id.item_purchase_date);
            estimatedValueView = view.findViewById(R.id.item_estimated_value);
            itemCheckboxView = view.findViewById(R.id.item_checkBox);
//        imageView = view.findViewById(R.id.item_image);

            /* Tag row so that it's not necessary to call findViewById for the ItemViewHolder again when reusing the row. */
            view.setTag(new ItemViewHolder(nameView, purchaseDateView, estimatedValueView, itemCheckboxView));

            itemCheckboxView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    CheckBox checkbox = (CheckBox) v;
                    Item item = (Item) checkbox.getTag();
                    item.setSelected(checkbox.isChecked());

                }
            });
        } else {
            ItemViewHolder itemViewHolder= (ItemViewHolder) view.getTag();
            nameView = itemViewHolder.getItemNameView();
            purchaseDateView = itemViewHolder.getItemPurchaseDateView();
            estimatedValueView = itemViewHolder.getItemValueView();
            itemCheckboxView = itemViewHolder.getItemCheckBox();

        }

        /* Tag the check box with the item it diplays to access the item onClick() when the check box is toggled.
        itemCheckboxView.setTag(item);

        /* then set them to be the correct corresponding value for each element of the item (ie. name, purchase date, estimated value) */
        nameView.setText(item.getName());
        SimpleDateFormat df = new SimpleDateFormat(context.getString(R.string.date_format)); //create a new format for the date to be in YYYY/MM/DD format
        String date = df.format(item.getPurchaseDate()); //convert the date to a string in the specified format
        purchaseDateView.setText(date);
        String value = String.format("%.2f", item.getEstimatedValue()); //ensure there are only 2 places after the decimal when formatting the string
        estimatedValueView.setText(context.getString(R.string.cad_currency) + value); //format the estimated value to include the currency ie. CAD$*.xx
        itemCheckboxView.setChecked(item.isSelected());
        itemCheckboxView.setTag(item);
        /* return the view we inflated */
        return view;
    }
}
