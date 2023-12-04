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

import androidx.test.espresso.NoMatchingViewException;
import androidx.test.espresso.action.ViewActions;
import androidx.test.espresso.intent.Intents;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
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
    public ActivityScenarioRule<WelcomeActivity> welcomeScenario=new ActivityScenarioRule<WelcomeActivity>(WelcomeActivity.class);
    @Before
    public void init() {
        Intents.init();
    }
    /* check if the app is logged in */
    private boolean isLoggedIn(){
        try {
            onView(withId(R.id.btn_logout)).check(matches(isDisplayed()));
        } catch (NoMatchingViewException e) {
            return false;
        }
        return true;
    }
    /* wait to log out before login again */
    @Test
    public void testLogOut() {
        boolean state = isLoggedIn();
        while (!state) {
            state = isLoggedIn();
        }
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
    @Test
    public void testGoToAdd() throws InterruptedException {
        // In MainActivity
        /* click add button on MainActivity */
        onView(withId(R.id.add_button)).perform(click());
        onView(withId(R.id.open_gallery_button)).perform(click());
        Thread.sleep(3000);
        /* Verify that we are in ManageItemActivity */
        intended(hasComponent(new ComponentName(getApplicationContext(), ManageItemActivity.class)));
    }
    @Test
    public void testBarcode() throws InterruptedException {
        onView(withId(R.id.barcode_scan_icon)).perform(click());
        Thread.sleep(3000);
        /* check if there is a description is description field */
        onView(withId(R.id.item_name_field)).check(matches(isDisplayed()));
    }
    @After
    public void drop() {
        Intents.release();
    }
}

