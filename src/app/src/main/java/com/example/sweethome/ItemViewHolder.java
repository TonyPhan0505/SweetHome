/*
 * ItemViewHolder
 *
 * This class acts as view holder for each item entry on the list.
 * This is necessary to allow selecting multiple items at the same time.
 *
 * November 8, 2023
 *
 * Sources: https://windrealm.org/tutorials/android/listview-with-checkboxes.php
 *
 */

package com.example.sweethome;

import android.widget.TextView;
import android.widget.CheckBox;

public class ItemViewHolder {
    private TextView itemNameView;
    private TextView itemPurchaseDateView;
//    private ShapeableImageView itemImageView;
    private TextView itemValueView;
    private CheckBox itemCheckBox;

    public ItemViewHolder(TextView itemNameView, TextView itemPurchaseDateView, TextView itemValueView, CheckBox itemCheckBox) {
        this.itemNameView = itemNameView;
        this.itemPurchaseDateView = itemPurchaseDateView;
        this.itemValueView = itemValueView;
        this.itemCheckBox = itemCheckBox;
//        this.itemNameView = itemImageView;
    }

    public ItemViewHolder() {}

    public TextView getItemNameView() {
        return itemNameView;
    }

    public void setItemNameView(TextView itemNameView) {
        this.itemNameView = itemNameView;
    }

    public TextView getItemPurchaseDateView() {
        return itemPurchaseDateView;
    }

    public void setItemPurchaseDateView(TextView itemPurchaseDateView) {
        this.itemPurchaseDateView = itemPurchaseDateView;
    }


//    public TextView getItemImageView() {
//        return itemImageView;
//    }
//
//    public void setItemImageView(TextView itemImageView) {
//        this.itemImageView = itemImageView;
//    }

    public TextView getItemValueView() {
        return itemValueView;
    }

    public void setItemValueView(TextView itemValueView) {
        this.itemValueView = itemValueView;
    }

    public CheckBox getItemCheckBox() {
        return itemCheckBox;
    }

    public void setItemCheckBox(CheckBox itemCheckBox) {
        this.itemCheckBox = itemCheckBox;
    }
}
