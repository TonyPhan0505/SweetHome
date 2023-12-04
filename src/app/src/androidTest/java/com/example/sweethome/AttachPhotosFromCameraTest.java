package com.example.sweethome;

import static androidx.test.core.app.ApplicationProvider.getApplicationContext;
import static androidx.test.espresso.Espresso.closeSoftKeyboard;
import static androidx.test.espresso.Espresso.onData;
import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.intent.Intents.intended;
import static androidx.test.espresso.intent.matcher.IntentMatchers.hasComponent;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;

import static org.hamcrest.CoreMatchers.anything;
import static org.mockito.AdditionalMatchers.not;

import android.content.ComponentName;

import androidx.test.espresso.NoMatchingViewException;
import androidx.test.espresso.action.ViewActions;
import androidx.test.espresso.intent.Intents;
import androidx.test.espresso.matcher.ViewMatchers;
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
public class AttachPhotosFromCameraTest {
    @Rule
    public ActivityScenarioRule<WelcomeActivity> welcomeScenario=new ActivityScenarioRule<WelcomeActivity>(WelcomeActivity.class);
    @Before
    public void initWelcome() {
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
    public void testAddPhotoFromCamera() throws InterruptedException {
        onView(withId(R.id.open_camera_button)).perform(click());
        Thread.sleep(3000);
        // add image
        onView(withId(R.id.image_slider)).check(matches(isDisplayed()));
        // add other fields
        onView(withId(R.id.item_name_field)).perform(ViewActions.typeText("AttachPhotoGalleryTest1"));
        Thread.sleep(3000);
        onView(withId(R.id.serial_number_field)).perform(ViewActions.typeText("1234567"));
        onView(withId(R.id.serial_number_field)).perform(ViewActions.pressImeActionButton());
        onView(withId(R.id.tag_input)).perform(ViewActions.typeText("AttachPhotoGalleryTest1"));
        onView(withId(R.id.tag_input)).perform(ViewActions.pressImeActionButton());
        Thread.sleep(3000);
        closeSoftKeyboard();
        onView(withId(R.id.description_field)).perform(ViewActions.typeText("This is a delete photo test"));
        closeSoftKeyboard();
        onView(withId(R.id.make_field)).perform(ViewActions.typeText("testMake"));
        closeSoftKeyboard();
        onView(withId(R.id.model_field)).perform(ViewActions.typeText("testModel"));
        closeSoftKeyboard();
        onView(withId(R.id.date_field)).perform(click());
        onView(withText("OK")).perform(click());
        Thread.sleep(3000);
        onView(withId(R.id.value_field)).perform(ViewActions.typeText("100.00"));
        closeSoftKeyboard();
        onView(withId(R.id.comment_field)).perform(ViewActions.typeText("testComment"));
        closeSoftKeyboard();
        onView(ViewMatchers.withId(R.id.scroll_view)).perform(ViewActions.swipeDown());
        Thread.sleep(3000);
        onView(withId(R.id.check_icon)).perform(click());
        Thread.sleep(3000);

        // check if there is an image in slider
        onData(anything()).inAdapterView(withId(R.id.image_slider_frame)).atPosition(0).onChildView(withId(R.id.image_slider)).check(matches(withText("DeletePhotoTest1")));
    }
    @After
    public void dropWelcome() {
        Intents.release();
    }

}

