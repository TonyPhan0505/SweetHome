package com.example.sweethome;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import androidx.test.core.app.ActivityScenario;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.ArrayList;

@RunWith(AndroidJUnit4.class)
public class CustomAddTagsFieldTest {

    private ActivityScenario<MainActivity> scenario;

    @Before
    public void setUp() {
        scenario = ActivityScenario.launch(MainActivity.class);
    }

    @After
    public void tearDown() {
        scenario.close();
    }

    @Test
    public void testAddTag() {
        scenario.onActivity(activity -> {
            CustomAddTagsField customAddTagsField = new CustomAddTagsField(activity);

            // Add tags
            customAddTagsField.addTag("Tag1");
            customAddTagsField.addTag("Tag2");
            customAddTagsField.addTag("Tag3");

            // Check the list of added tag names
            ArrayList<String> addedTagNames = customAddTagsField.getAddedTagNames();
            assertEquals(3, addedTagNames.size());
            assertTrue(addedTagNames.contains("Tag1"));
            assertTrue(addedTagNames.contains("Tag2"));
            assertTrue(addedTagNames.contains("Tag3"));


        });
    }
}
