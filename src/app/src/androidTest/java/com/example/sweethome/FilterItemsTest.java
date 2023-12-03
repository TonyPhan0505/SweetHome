package com.example.sweethome;

import static androidx.test.core.app.ApplicationProvider.getApplicationContext;
import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.intent.Intents.intended;
import static androidx.test.espresso.intent.matcher.IntentMatchers.hasComponent;
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
public class FilterItemsTest {
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
    public void testFilterItems() throws InterruptedException {
        // In MainActivity
        /* click filter button */
        onView(withId(R.id.filter_button)).perform(click());
        Thread.sleep(5000);
        /* filter by date */

        /* filter by keyword */

        /* filter by make */

        /* filter by tags */

    }
    @After
    public void dropMain() {
        Intents.release();
    }

}

