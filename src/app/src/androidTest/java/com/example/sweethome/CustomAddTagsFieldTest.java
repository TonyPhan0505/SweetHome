package com.example.sweethome;

import static androidx.test.core.app.ApplicationProvider.getApplicationContext;
import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.intent.Intents.intended;
import static androidx.test.espresso.intent.matcher.IntentMatchers.hasComponent;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import android.content.ComponentName;

import androidx.test.core.app.ActivityScenario;
import androidx.test.espresso.NoMatchingViewException;
import androidx.test.espresso.action.ViewActions;
import androidx.test.espresso.intent.Intents;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.rule.GrantPermissionRule;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.ArrayList;

/**
 * @class CustomAddTagsFieldTest
 * <p>This class tests adding a new tag into the input field in ManageItemActivity</p>
 *
 * @date <p>December 4, 2023</p>
 *
 */
@RunWith(AndroidJUnit4.class)
public class CustomAddTagsFieldTest {
    @Rule
    public ActivityScenarioRule<WelcomeActivity> welcomeScenario = new ActivityScenarioRule<>(WelcomeActivity.class);

    @Rule
    public GrantPermissionRule permissionRule = GrantPermissionRule.grant(android.Manifest.permission.CAMERA);

    @Before
    public void initWelcome() {
        Intents.init();
    }

    @Test
    public void testAddTag() throws InterruptedException {
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
        ActivityScenario<ManageItemActivity> scenario = ActivityScenario.launch(ManageItemActivity.class);
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

        // Close the activity after testing
        scenario.close();
    }

    @Test
    public void testRemoveTag() throws InterruptedException {
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
        ActivityScenario<ManageItemActivity> scenario = ActivityScenario.launch(ManageItemActivity.class);
        scenario.onActivity(activity -> {
            CustomAddTagsField customAddTagsField = new CustomAddTagsField(activity);

            // Add tags
            customAddTagsField.addTag("Tag1");
            customAddTagsField.addTag("Tag2");
            customAddTagsField.addTag("Tag3");

            // Remove Tag1
            customAddTagsField.removeTag("Tag1");

            // Check the list of added tag names
            ArrayList<String> addedTagNamesAfterRemoval = customAddTagsField.getAddedTagNames();

            // Check that Tag1 is removed
            assertEquals(2, addedTagNamesAfterRemoval.size());
            assertFalse(addedTagNamesAfterRemoval.contains("Tag1"));
            assertTrue(addedTagNamesAfterRemoval.contains("Tag2"));
            assertTrue(addedTagNamesAfterRemoval.contains("Tag3"));
        });

        // Close the activity after testing
        scenario.close();
    }

    @After
    public void after() {
        Intents.release();
    }
}
