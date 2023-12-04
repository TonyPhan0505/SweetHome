package com.example.sweethome;

import static androidx.test.core.app.ApplicationProvider.getApplicationContext;
import static androidx.test.espresso.Espresso.closeSoftKeyboard;
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
import androidx.test.espresso.matcher.ViewMatchers;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.filters.LargeTest;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;


@RunWith(AndroidJUnit4.class)
@LargeTest
public class FilterItemsTest {
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
        onView(withId(R.id.editTextUsername)).perform(click(), ViewActions.clearText(), typeText("logintest"));
        /* click the password field edit text, clear text (if applicable) and put in our test password */
        onView(withId(R.id.editTextPassword)).perform(click(), ViewActions.clearText(), typeText("logintest"));
        /* click the login button */
        onView(withId(R.id.buttonLogin)).perform(click());
        Thread.sleep(3000);
        /* check if the main activity is launched */
        intended(hasComponent(new ComponentName(getApplicationContext(), MainActivity.class)));
    }
    @Test
    public void testAddItems() throws InterruptedException {
        /* add 1st item */
        onView(withId(R.id.add_button)).perform(click());
        onView(withId(R.id.item_name_field)).perform(typeText("FilterItemTest1"));
        Thread.sleep(3000);
        onView(withId(R.id.serial_number_field)).perform(typeText("1224567"));
        onView(withId(R.id.serial_number_field)).perform(pressImeActionButton());
        onView(withId(R.id.tag_input)).perform(typeText("FilterItemTest1"));
        onView(withId(R.id.tag_input)).perform(pressImeActionButton());
        Thread.sleep(3000);
        closeSoftKeyboard();
        onView(withId(R.id.description_field)).perform(typeText("This is an add test"));
        closeSoftKeyboard();
        onView(withId(R.id.make_field)).perform(typeText("testMake"));
        closeSoftKeyboard();
        onView(withId(R.id.model_field)).perform(typeText("testModel"));
        closeSoftKeyboard();
        onView(withId(R.id.date_field)).perform(click());
        onView(withText("OK")).perform(click());
        Thread.sleep(3000);
        onView(withId(R.id.value_field)).perform(typeText("119.98"));
        closeSoftKeyboard();
        onView(withId(R.id.comment_field)).perform(typeText("testComment"));
        closeSoftKeyboard();
        onView(ViewMatchers.withId(R.id.scroll_view)).perform(ViewActions.swipeDown());
        Thread.sleep(3000);
        onView(withId(R.id.check_icon)).perform(click());
        /* check if the main activity is launched */
        intended(hasComponent(new ComponentName(getApplicationContext(), MainActivity.class)));

        /* add 2nd item */
        onView(withId(R.id.add_button)).perform(click());
        onView(withId(R.id.item_name_field)).perform(typeText("A FilterItemTest2"));
        Thread.sleep(3000);
        onView(withId(R.id.serial_number_field)).perform(typeText("1224567"));
        onView(withId(R.id.serial_number_field)).perform(pressImeActionButton());
        onView(withId(R.id.tag_input)).perform(typeText("A FilterItemTest2"));
        onView(withId(R.id.tag_input)).perform(pressImeActionButton());
        Thread.sleep(3000);
        onView(withId(R.id.tag_input)).perform(typeText("FilterItem2Tag"));
        onView(withId(R.id.tag_input)).perform(pressImeActionButton());
        Thread.sleep(3000);
        closeSoftKeyboard();
        onView(withId(R.id.description_field)).perform(typeText("This is an add test"));
        closeSoftKeyboard();
        onView(withId(R.id.make_field)).perform(typeText("testMake2"));
        closeSoftKeyboard();
        onView(withId(R.id.model_field)).perform(typeText("testModel"));
        closeSoftKeyboard();
        onView(withId(R.id.date_field)).perform(click());
        onView(withText("OK")).perform(click());
        Thread.sleep(3000);
        onView(withId(R.id.value_field)).perform(typeText("119.98"));
        closeSoftKeyboard();
        onView(withId(R.id.comment_field)).perform(typeText("testComment"));
        closeSoftKeyboard();
        onView(ViewMatchers.withId(R.id.scroll_view)).perform(ViewActions.swipeDown());
        Thread.sleep(3000);
        onView(withId(R.id.check_icon)).perform(click());
        Thread.sleep(3000);
    }
    @Test
    public void testFilterByDate() throws InterruptedException {
        // In MainActivity
        /* click filter button */
        onView(withId(R.id.filter_button)).perform(click());
        Thread.sleep(5000);
        /* choose date */
        onView(withId(R.id.calendar_button)).perform(click());
        onView(withText("OK")).perform(click());
        Thread.sleep(3000);
        onView(withId(R.id.apply_filter_button)).perform(click());
        Thread.sleep(5000);
        /* check if both item is shown */

    }
    @Test
    public void testFilterByKeyword() throws InterruptedException {
        // In MainActivity
        /* click filter button */
        onView(withId(R.id.filter_button)).perform(click());
        Thread.sleep(5000);
        /* input make */
        onView(withId(R.id.keyword_field)).perform(typeText("testMake2"), pressImeActionButton());
        Thread.sleep(3000);
        onView(withText("OK")).perform(click());
        Thread.sleep(5000);
        /* check if 2nd item is shown */

    }
    @Test
    public void testFilterByMake() throws InterruptedException {
        // In MainActivity
        /* click filter button */
        onView(withId(R.id.filter_button)).perform(click());
        Thread.sleep(5000);
        /* input make */
        onView(withId(R.id.make_field)).perform(typeText("testMake2"), pressImeActionButton());
        Thread.sleep(3000);
        onView(withText("OK")).perform(click());
        Thread.sleep(5000);
        /* check if 2nd item is shown */

    }
    @Test
    public void testFilterByTag() throws InterruptedException {
        // In MainActivity
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

    }
    @After
    public void drop() {
        Intents.release();
    }

}

