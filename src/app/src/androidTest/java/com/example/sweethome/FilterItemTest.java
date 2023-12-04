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
import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.allOf;

import android.content.ComponentName;

import androidx.test.espresso.NoMatchingViewException;
import androidx.test.espresso.action.ViewActions;
import androidx.test.espresso.intent.Intents;
import androidx.test.espresso.matcher.ViewMatchers;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.filters.LargeTest;
import androidx.test.rule.GrantPermissionRule;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * @class @class FilterItemTest
 * <p>This class tests filtering items </p>
 *
 * @date <p>Dec 4, 2023</p>
 *
 * @source Stackoverflow's answer to: Write a test that clicks on views inside PopupWindow. The answer was posted
 * by Nishanth on Apr 17, 2016.
 *
 * The content of the posts on StackOverflow are licensed under Creative Commons Attribution-ShareAlike.
 *
 * @link https://stackoverflow.com/questions/27382147/write-a-test-that-clicks-on-views-inside-popupwindow
 */

@RunWith(AndroidJUnit4.class)
@LargeTest
public class FilterItemTest {
    @Rule
    public ActivityScenarioRule<WelcomeActivity> scenario=new ActivityScenarioRule<WelcomeActivity>(WelcomeActivity.class);

    @Rule
    public GrantPermissionRule permissionRule = GrantPermissionRule.grant(android.Manifest.permission.CAMERA);

    @Before
    public void setup() {
        Intents.init();
    }
    @Test
    public void testFilterItem() throws InterruptedException{
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

        //test
        onView(withId(R.id.add_button)).perform(click());
        onView(withId(R.id.item_name_field)).perform(ViewActions.typeText("FilterItemTest"));
        Thread.sleep(2000);
        onView(withId(R.id.serial_number_field)).perform(ViewActions.typeText("8224567"));
        onView(withId(R.id.serial_number_field)).perform(ViewActions.pressImeActionButton());
        onView(withId(R.id.tag_input)).perform(ViewActions.typeText("Filter"));
        onView(withId(R.id.tag_input)).perform(ViewActions.pressImeActionButton());
        Thread.sleep(3000);
        closeSoftKeyboard();
        onView(withId(R.id.description_field)).perform(ViewActions.typeText("This is a filter test"));
        closeSoftKeyboard();
        onView(withId(R.id.make_field)).perform(ViewActions.typeText("testFilterMake"));
        closeSoftKeyboard();
        onView(withId(R.id.model_field)).perform(ViewActions.typeText("testFilterModel"));
        closeSoftKeyboard();
        onView(withId(R.id.date_field)).perform(click());
        onView(withText("OK")).perform(click());
        Thread.sleep(3000);
        onView(withId(R.id.value_field)).perform(ViewActions.typeText("119.98"));
        closeSoftKeyboard();
        onView(withId(R.id.comment_field)).perform(ViewActions.typeText("testComment filter"));
        closeSoftKeyboard();
        onView(ViewMatchers.withId(R.id.scroll_view)).perform(ViewActions.swipeDown());
        Thread.sleep(3000);
        onView(withId(R.id.check_icon)).perform(click());
        Thread.sleep(3000);

        //test implementation
        onView(withId(R.id.filter_button)).perform(click());
        onView(withId(R.id.make_field)).perform(click(), ViewActions.typeText("Filter"), ViewActions.pressImeActionButton());
        Thread.sleep(2000);
        onView(withId(R.id.keyword_field)).perform(click(), ViewActions.typeText("filter"), ViewActions.pressImeActionButton());
        Thread.sleep(2000);
        onView(withId(R.id.tag_filter_field)).perform(click());
        onData(allOf(is(instanceOf(String.class)), is("Filter"))).perform(click());
        Thread.sleep(1000);
        onView(withId(R.id.apply_filter_button)).perform(click());

        //verify
        onData(anything()).inAdapterView(withId(R.id.item_list))
                .atPosition(0).onChildView(withId(R.id.item_name))
                .check(matches(withText("FilterItemTest")));

        //Delete the item
        onData(anything()).inAdapterView(withId(R.id.item_list)).atPosition(0).onChildView(withId(R.id.item_name)).check(matches(withText("FilterItemTest")));
        onData(anything()).inAdapterView(withId(R.id.item_list)).atPosition(0).onChildView(withId(R.id.item_checkBox)).perform(click());
        Thread.sleep(3000);
        onView(withId(R.id.delete_action_button)).perform(click());
        Thread.sleep(3000);
        onView(withId(R.id.delete_button)).perform(click());

        //logout
        onView(withId(R.id.btn_logout)).perform(click());
        onView(withId(R.id.profile_logout)).perform(click());
    }

    @After
    public void after() {
        Intents.release();
    }
}

