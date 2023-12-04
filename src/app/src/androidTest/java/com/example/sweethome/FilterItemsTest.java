package com.example.sweethome;

import static androidx.test.core.app.ApplicationProvider.getApplicationContext;
import static androidx.test.espresso.Espresso.onData;
import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.pressImeActionButton;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.intent.Intents.intended;
import static androidx.test.espresso.intent.matcher.IntentMatchers.hasComponent;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;

import static org.hamcrest.Matchers.anything;

import android.content.ComponentName;

import androidx.test.espresso.NoMatchingViewException;
import androidx.test.espresso.action.ViewActions;
import androidx.test.espresso.intent.Intents;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.filters.LargeTest;

import com.google.firebase.Timestamp;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.ArrayList;
import java.util.Arrays;


@RunWith(AndroidJUnit4.class)
@LargeTest
public class FilterItemsTest {
    @Rule
    public ActivityScenarioRule<WelcomeActivity> welcomeScenario=new ActivityScenarioRule<WelcomeActivity>(WelcomeActivity.class);
    @Before
    public void init() {
        Intents.init();
    }

    @Test
    public void testFilterByDate() throws InterruptedException {
        try {
            Thread.sleep(3000);
            onView(withId(R.id.search_add_container)).check(matches(isDisplayed()));
        } catch (NoMatchingViewException e) {
            /* click the username field edit text, clear text (if applicable) and put our test username */
            onView(withId(R.id.editTextUsername)).perform(click(), ViewActions.clearText(), ViewActions.typeText("logintest"));
            /* click the password field edit text, clear text (if applicable) and put in our test password */
            onView(withId(R.id.editTextPassword)).perform(click(), ViewActions.clearText(), ViewActions.typeText("logintest"));
            /* click the login button */
            onView(withId(R.id.buttonLogin)).perform(click());
            Thread.sleep(3000);
            /* check if the main activity is launched */
            intended(hasComponent(new ComponentName(getApplicationContext(), MainActivity.class)));
        }

        // create items for testing
        Item item1 = new Item(
                "1",                 // id
                "Item 1",            // name
                "Description 1",     // description
                "Make 1",            // make
                "Model 1",           // model
                "SerialNumber 1",    // serialNumber
                100.0,                // estimatedValue
                Timestamp.now(),  // purchaseDate
                "Comment 1",         // comment
                new ArrayList<>(Arrays.asList("res/drawable/test_image1.png", "res/drawable/test_image2.png")),  // photos
                new ArrayList<>(Arrays.asList("tag1", "tag2")),    // tags
                "Username 1"         // username
        );
        Item item2 = new Item(
                "2",                 // id
                "Item 2",            // name
                "Description 2",     // description
                "Make 2",            // make
                "Model 2",           // model
                "SerialNumber 2",    // serialNumber
                150.0,                // estimatedValue
                Timestamp.now(),     // purchaseDate
                "Comment 2",         // comment
                new ArrayList<>(Arrays.asList("res/drawable/test_image1.png", "res/drawable/test_image2.png")),  // photos
                new ArrayList<>(Arrays.asList("tag2", "tag3")),    // tags
                "Username 2"         // username
        );
        Item item3 = new Item(
                "3",                 // id
                "Item 3",            // name
                "Description 3",     // description
                "Make 3",            // make
                "Model 3",           // model
                "SerialNumber 3",    // serialNumber
                150.0,                // estimatedValue
                Timestamp.now(),     // purchaseDate
                "Comment 3",         // comment
                new ArrayList<>(Arrays.asList("res/drawable/test_image1.png", "res/drawable/test_image2.png")),  // photos
                new ArrayList<>(Arrays.asList("tag2", "tag4")),    // tags
                "Username 3"         // username
        );

        /* click filter button */
        onView(withId(R.id.filter_button)).perform(click());
        Thread.sleep(5000);
        /* choose date */
        onView(withId(R.id.calendar_button)).perform(click());
        onView(withText("OK")).perform(click());
        Thread.sleep(3000);
        onView(withId(R.id.apply_filter_button)).perform(click());
        Thread.sleep(5000);
        /* check if all items are shown */
        onView(withText("Item 1")).check(matches(isDisplayed()));
        onView(withText("Item 2")).check(matches(isDisplayed()));
        onView(withText("Item 3")).check(matches(isDisplayed()));
    }
    @Test
    public void testFilterByKeyword() throws InterruptedException {
        try {
            Thread.sleep(3000);
            onView(withId(R.id.search_add_container)).check(matches(isDisplayed()));
        } catch (NoMatchingViewException e) {
            /* click the username field edit text, clear text (if applicable) and put our test username */
            onView(withId(R.id.editTextUsername)).perform(click(), ViewActions.clearText(), ViewActions.typeText("logintest"));
            /* click the password field edit text, clear text (if applicable) and put in our test password */
            onView(withId(R.id.editTextPassword)).perform(click(), ViewActions.clearText(), ViewActions.typeText("logintest"));
            /* click the login button */
            onView(withId(R.id.buttonLogin)).perform(click());
            Thread.sleep(3000);
            /* check if the main activity is launched */
            intended(hasComponent(new ComponentName(getApplicationContext(), MainActivity.class)));
        }

        // create items for testing
        Item item1 = new Item(
                "1",                 // id
                "Item 1",            // name
                "Description 1",     // description
                "Make 1",            // make
                "Model 1",           // model
                "SerialNumber 1",    // serialNumber
                100.0,                // estimatedValue
                Timestamp.now(),  // purchaseDate
                "Comment 1",         // comment
                new ArrayList<>(Arrays.asList("res/drawable/test_image1.png", "res/drawable/test_image2.png")),  // photos
                new ArrayList<>(Arrays.asList("tag1", "tag2")),    // tags
                "Username 1"         // username
        );
        Item item2 = new Item(
                "2",                 // id
                "Item 2",            // name
                "Description 2",     // description
                "Make 2",            // make
                "Model 2",           // model
                "SerialNumber 2",    // serialNumber
                150.0,                // estimatedValue
                Timestamp.now(),     // purchaseDate
                "Comment 2",         // comment
                new ArrayList<>(Arrays.asList("res/drawable/test_image1.png", "res/drawable/test_image2.png")),  // photos
                new ArrayList<>(Arrays.asList("tag2", "tag3")),    // tags
                "Username 2"         // username
        );
        Item item3 = new Item(
                "3",                 // id
                "Item 3",            // name
                "Description 3",     // description
                "Make 3",            // make
                "Model 3",           // model
                "SerialNumber 3",    // serialNumber
                150.0,                // estimatedValue
                Timestamp.now(),     // purchaseDate
                "Comment 3",         // comment
                new ArrayList<>(Arrays.asList("res/drawable/test_image1.png", "res/drawable/test_image2.png")),  // photos
                new ArrayList<>(Arrays.asList("tag2", "tag4")),    // tags
                "Username 3"         // username
        );

        /* click filter button */
        onView(withId(R.id.filter_button)).perform(click());
        Thread.sleep(5000);
        /* input make */
        onView(withId(R.id.keyword_field)).perform(typeText("testMake2"), pressImeActionButton());
        Thread.sleep(3000);
        onView(withText("OK")).perform(click());
        Thread.sleep(5000);
        /* check if 2nd item is shown */
        onView(withText("Item 2")).check(matches(isDisplayed()));

    }
    @Test
    public void testFilterByMake() throws InterruptedException {
        try {
            Thread.sleep(3000);
            onView(withId(R.id.search_add_container)).check(matches(isDisplayed()));
        } catch (NoMatchingViewException e) {
            /* click the username field edit text, clear text (if applicable) and put our test username */
            onView(withId(R.id.editTextUsername)).perform(click(), ViewActions.clearText(), ViewActions.typeText("logintest"));
            /* click the password field edit text, clear text (if applicable) and put in our test password */
            onView(withId(R.id.editTextPassword)).perform(click(), ViewActions.clearText(), ViewActions.typeText("logintest"));
            /* click the login button */
            onView(withId(R.id.buttonLogin)).perform(click());
            Thread.sleep(3000);
            /* check if the main activity is launched */
            intended(hasComponent(new ComponentName(getApplicationContext(), MainActivity.class)));
        }

        // create items for testing
        Item item1 = new Item(
                "1",                 // id
                "Item 1",            // name
                "Description 1",     // description
                "Make 1",            // make
                "Model 1",           // model
                "SerialNumber 1",    // serialNumber
                100.0,                // estimatedValue
                Timestamp.now(),  // purchaseDate
                "Comment 1",         // comment
                new ArrayList<>(Arrays.asList("res/drawable/test_image1.png", "res/drawable/test_image2.png")),  // photos
                new ArrayList<>(Arrays.asList("tag1", "tag2")),    // tags
                "Username 1"         // username
        );
        Item item2 = new Item(
                "2",                 // id
                "Item 2",            // name
                "Description 2",     // description
                "Make 2",            // make
                "Model 2",           // model
                "SerialNumber 2",    // serialNumber
                150.0,                // estimatedValue
                Timestamp.now(),     // purchaseDate
                "Comment 2",         // comment
                new ArrayList<>(Arrays.asList("res/drawable/test_image1.png", "res/drawable/test_image2.png")),  // photos
                new ArrayList<>(Arrays.asList("tag2", "tag3")),    // tags
                "Username 2"         // username
        );

        /* click filter button */
        onView(withId(R.id.filter_button)).perform(click());
        Thread.sleep(5000);
        /* input make */
        onView(withId(R.id.make_field)).perform(typeText("Make 2"), pressImeActionButton());
        Thread.sleep(3000);
        onView(withText("OK")).perform(click());
        Thread.sleep(5000);
        /* check if item 2 is shown */
        onView(withText("Item 2")).check(matches(isDisplayed()));
    }
    @Test
    public void testFilterByTag() throws InterruptedException {
        try {
            Thread.sleep(3000);
            onView(withId(R.id.search_add_container)).check(matches(isDisplayed()));
        } catch (NoMatchingViewException e) {
            /* click the username field edit text, clear text (if applicable) and put our test username */
            onView(withId(R.id.editTextUsername)).perform(click(), ViewActions.clearText(), ViewActions.typeText("logintest"));
            /* click the password field edit text, clear text (if applicable) and put in our test password */
            onView(withId(R.id.editTextPassword)).perform(click(), ViewActions.clearText(), ViewActions.typeText("logintest"));
            /* click the login button */
            onView(withId(R.id.buttonLogin)).perform(click());
            Thread.sleep(3000);
            /* check if the main activity is launched */
            intended(hasComponent(new ComponentName(getApplicationContext(), MainActivity.class)));
        }

        // create items for testing
        Item item1 = new Item(
                "1",                 // id
                "Item 1",            // name
                "Description 1",     // description
                "Make 1",            // make
                "Model 1",           // model
                "SerialNumber 1",    // serialNumber
                100.0,                // estimatedValue
                Timestamp.now(),  // purchaseDate
                "Comment 1",         // comment
                new ArrayList<>(Arrays.asList("res/drawable/test_image1.png", "res/drawable/test_image2.png")),  // photos
                new ArrayList<>(Arrays.asList("tag1", "tag2")),    // tags
                "Username 1"         // username
        );
        Item item2 = new Item(
                "2",                 // id
                "Item 2",            // name
                "Description 2",     // description
                "Make 2",            // make
                "Model 2",           // model
                "SerialNumber 2",    // serialNumber
                150.0,                // estimatedValue
                Timestamp.now(),     // purchaseDate
                "Comment 2",         // comment
                new ArrayList<>(Arrays.asList("res/drawable/test_image1.png", "res/drawable/test_image2.png")),  // photos
                new ArrayList<>(Arrays.asList("tag2", "tag3")),    // tags
                "Username 2"         // username
        );
        Item item3 = new Item(
                "3",                 // id
                "Item 3",            // name
                "Description 3",     // description
                "Make 3",            // make
                "Model 3",           // model
                "SerialNumber 3",    // serialNumber
                150.0,                // estimatedValue
                Timestamp.now(),     // purchaseDate
                "Comment 3",         // comment
                new ArrayList<>(Arrays.asList("res/drawable/test_image1.png", "res/drawable/test_image2.png")),  // photos
                new ArrayList<>(Arrays.asList("tag2", "tag4")),    // tags
                "Username 3"         // username
        );

        /* click filter button */
        onView(withId(R.id.filter_button)).perform(click());
        Thread.sleep(5000);
        /* input tag */
        onView(withId(R.id.tag_filter_field)).perform(click());
        /* click on the second item in the dropdown (testMake) */
        onData(anything()).atPosition(1).perform(click());
        Thread.sleep(3000);
        onView(withText("OK")).perform(click());
        Thread.sleep(5000);
        /* check if 1st item is shown */
        onView(withText("Item 1")).check(matches(isDisplayed()));
    }
    @After
    public void drop() {
        Intents.release();
    }

}

