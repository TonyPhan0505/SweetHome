package com.example.sweethome;

import static androidx.test.core.app.ApplicationProvider.getApplicationContext;
import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.intent.Intents.intended;
import static androidx.test.espresso.intent.matcher.IntentMatchers.hasComponent;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;

import android.content.ComponentName;

import androidx.test.espresso.action.ViewActions;
import androidx.test.espresso.intent.Intents;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.filters.LargeTest;
import androidx.test.rule.ActivityTestRule;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(AndroidJUnit4.class)
@LargeTest
public class DescriptionByBarcodeTest {
    @Rule
    public ActivityTestRule<LoginActivity> scenario=new ActivityTestRule<LoginActivity>(LoginActivity.class);
    @Before
    public void initLogin() {
        Intents.init();
    }
    @Test
    public void testGoToMain() throws InterruptedException {
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
    @After
    public void dropLogin() {
        Intents.release();
    }

    @Rule
    public ActivityTestRule<MainActivity> mainActivity = new ActivityTestRule<>(MainActivity.class);
    @Before
    public void initMain() {
        Intents.init();
    }
    @Test
    public void testGoToAdd() throws InterruptedException {
        // In MainActivity
        /* click add button on MainActivity */
        onView(withId(R.id.add_button)).perform(click());
        onView(withId(R.id.open_gallery_button)).perform(click());
        Thread.sleep(3000);
        /* Verify that we are in ManageItemActivity */
        intended(hasComponent(ManageItemActivity.class.getName()));
    }
    @After
    public void dropMain() {
        Intents.release();
    }

    @Rule
    public ActivityTestRule<ManageItemActivity> activityRule = new ActivityTestRule<>(ManageItemActivity.class);

    @Before
    public void initAdd() {
        Intents.init();
    }

    @Test
    public void testBarcode() throws InterruptedException {
        onView(withId(R.id.barcode_scan_icon)).perform(click());
        Thread.sleep(3000);
        /* check if there is a description is description field */
        onView(withId(R.id.item_name_field)).check(matches(isDisplayed()));
    }
    @After
    public void dropAdd() {
        Intents.release();
    }

}

