package com.example.sweethome;
import org.junit.Before;
import org.junit.Test;

import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.Date;

/**
 * Unit Test to tests the methods found in MainActivity class
 */
public class DatabaseAccessTest {
    private FirebaseFirestore mockDb;
    private CollectionReference mockItemsRef;

    @Before
    private void setUp() {
        mockDb = FirebaseFirestore.getInstance();
        mockItemsRef = mockDb.collection("items");
    }

    private ArrayList<Item> mockItemArrayList() {
        ArrayList<Item> itemArrayList = new ArrayList<Item>();
        return itemArrayList;
    }

    private Item mockItem0() {
        return new Item("UnitTest0", "My mama gave it", "LG", "X2020", "45199121", 120.75, new Date(), "Nice equipment" );
    }

    private Item mockItem1() {
        return new Item("UnitTest1", "A brand new one.", "Samsung", "I43", "6789432", 278.99, new Date(), "Heats up nice" );
    }

    private Item mockItem2() {
        return new Item("UnitTest2", "Intel 12th", "Lenovo", "ThinkPad T14s", "62720010", 899.99, new Date(), "Bought in boxing day 2021" );
    }

    @Test
    private void testAddItem() {
        Item item = mockItem0();

//        MainActivity.addItem(item, mockItemsRef);
    }

    @Test
    private void testDeleteItem() {

    }
    @Test
    private void testDeleteMultipleItems() {

    }
}
