package com.example.sweethome;
/*
 * Item
 *
 * This class models an item in SweetHome. Item objects
 * contain information on their name, their make, their model
 * an optional serial number, their estimated value, the date
 * they were purchased/acquired, and an optional comment on
 * the item. These fields are the attributes of this class and
 * they can be accessed through getters and setters only to
 * encourage encapsulation.
 *
 * October 28, 2023
 *
 * Sources:
 *
 */

/* necessary imports */

import com.google.firebase.Timestamp;

import java.sql.Time;
import java.util.ArrayList;
import java.util.Date;

public class Item {
    /* attributes of this class */
    private String name;
    private String description;
    private String make;
    private String model;
    private String serialNumber;
    private double estimatedValue;
    private Timestamp purchaseDate;
    private String comment;
    private Boolean selected = false;
    private ArrayList<String> photos;
    private String id;

    /* constructor for this class */
    public Item(String id, String name, String description, String make, String model, String serialNumber, double estimatedValue, Timestamp purchaseDate, String comment) {
        this.id = id;
        this.name = name;
        this.make = make;
        this.description = description;
        this.model = model;
        this.serialNumber = serialNumber;
        this.estimatedValue = estimatedValue;
        this.purchaseDate = purchaseDate;
        this.comment = comment;
        //this.photos = new ArrayList<String>(); // the photos array will be initialized as an empty array, but it can be added to and deleted from using the add and delete methods below
    }

    /* no-arg constructor for this class */
    public Item(){
        //no argument constructor is necessary for using toObject in firestore
    }

    /* getters and setters for each attribute in this class */
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getMake() {
        return make;
    }

    public void setMake(String make) {
        this.make = make;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public String getSerialNumber() {
        return serialNumber;
    }

    public void setSerialNumber(String serialNumber) {
        this.serialNumber = serialNumber;
    }

    public double getEstimatedValue() {
        return estimatedValue;
    }

    public void setEstimatedValue(double estimatedValue) {
        this.estimatedValue = estimatedValue;
    }

    public Timestamp getPurchaseDate() {
        return purchaseDate;
    }

    public void setPurchaseDate(Timestamp purchaseDate) {
        this.purchaseDate = purchaseDate;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getItemId() {
        return id;
    }

    public void setItemId(String id) {
        this.id = id;
    }

    public ArrayList<String> getPhotos() {
        return photos;
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    public void toggleSelected() {
        selected = !selected;
    }

    /* function to add a new image URL to the photo arraylist */
    public void addPhoto(String photo) {
        this.photos.add(photo);
    }

    /* function to delete an image URL from the photo arraylist */
    public void deletePhoto(String photo) {
        this.photos.remove(photo);
    }
}
