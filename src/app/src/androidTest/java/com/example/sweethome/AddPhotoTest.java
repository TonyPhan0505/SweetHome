package com.example.sweethome;

import static androidx.test.core.app.ApplicationProvider.getApplicationContext;
import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.intent.Intents.intended;
import static androidx.test.espresso.intent.matcher.IntentMatchers.hasComponent;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;


import android.content.ComponentName;
import android.net.Uri;
import android.view.View;

import androidx.test.espresso.NoMatchingViewException;
import androidx.test.espresso.UiController;
import androidx.test.espresso.ViewAction;
import androidx.test.espresso.action.ViewActions;
import androidx.test.espresso.intent.Intents;
import androidx.test.espresso.matcher.ViewMatchers;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.rule.GrantPermissionRule;

import com.smarteist.autoimageslider.SliderView;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import java.util.ArrayList;

public class AddPhotoTest {
    ArrayList<ImageSliderData> sliderDataArrayList = new ArrayList();
    private ImageSliderAdapter adapter;
    @Rule
    public ActivityScenarioRule<WelcomeActivity> welcomeScenario=new ActivityScenarioRule<WelcomeActivity>(WelcomeActivity.class);
    @Rule
    public GrantPermissionRule permissionRule = GrantPermissionRule.grant(android.Manifest.permission.CAMERA);
    @Before
    public void initWelcome() {
        Intents.init();
    }


    @Test
    public void testAddPhoto() throws InterruptedException {
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
        /* click add button on MainActivity */
        onView(withId(R.id.add_button)).perform(click());
        Thread.sleep(3000);
        /* Verify that we are in ManageItemActivity */
        intended(hasComponent(new ComponentName(getApplicationContext(), ManageItemActivity.class)));

        /* Add photo to slider */
        // Add an image to the sliderDataArrayList for testing
        Uri testImageUri = Uri.parse("android.resource://com.example.sweethome/drawable/test_image1");

        // Perform custom action to add the image to the slider
        onView(withId(R.id.image_slider)).perform(addImageToSliderAction(testImageUri));

        // You might want to wait for the image to load before asserting its presence
        Thread.sleep(3000);

        // Check if the image is displayed in the slider
        onView(withId(R.id.image_slider)).check(matches(isImageInSlider(testImageUri)));

    }
    private Matcher<View> isImageInSlider(final Uri expectedImageUri) {
        return new TypeSafeMatcher<View>() {
            @Override
            protected boolean matchesSafely(View view) {
                if (view instanceof SliderView) {
                    SliderView sliderView = (SliderView) view;
                    ImageSliderAdapter adapter = (ImageSliderAdapter) sliderView.getAdapter();

                    for (int i = 0; i < adapter.getCount(); i++) {
                        ImageSliderData sliderData = adapter.getItemAt(i);
                        if (sliderData != null && sliderData.getImageUri().equals(expectedImageUri)) {
                            return true;
                        }
                    }
                }
                return false;
            }

            @Override
            public void describeTo(Description description) {
                description.appendText("Image in slider with URI: " + expectedImageUri);
            }
        };
    }

    @After
    public void dropWelcome() {
        Intents.release();
    }
}
