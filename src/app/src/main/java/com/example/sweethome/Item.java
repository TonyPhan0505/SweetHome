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
import java.net.URL;
import java.util.ArrayList;
import java.util.Date;

public class Item {
    /* attributes of this class */
    private String name;
    private String make;
    private String model;
    private String serialNumber;
    private double estimatedValue;
    private Date purchaseDate;
    private String comment;
    private ArrayList<URL> photos;

    /* constructor for this class */
    public Item(String name, String make, String model, String serialNumber, double estimatedValue, Date purchaseDate, String comment, ArrayList<URL> photos) {
        this.name = name;
        this.make = make;
        this.model = model;
        this.serialNumber = serialNumber;
        this.estimatedValue = estimatedValue;
        this.purchaseDate = purchaseDate;
        this.comment = comment;
        this.photos = photos;
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

    public Date getPurchaseDate() {
        return purchaseDate;
    }

    public void setPurchaseDate(Date purchaseDate) {
        this.purchaseDate = purchaseDate;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public ArrayList<URL> getPhotos() {
        return photos;
    }

    public void setPhotos(ArrayList<URL> photos) {
        this.photos = photos;
    }

    /* function to add a new image URL to the photo arraylist */
    public void addPhotos(URL photo) {
        this.photos.add(photo);
    }
}
