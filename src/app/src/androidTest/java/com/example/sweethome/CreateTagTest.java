package com.example.sweethome;

import static androidx.test.espresso.Espresso.closeSoftKeyboard;
import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import android.view.View;
import androidx.test.espresso.ViewAction;
import androidx.test.espresso.action.ViewActions;
import androidx.test.espresso.contrib.RecyclerViewActions;
import androidx.test.espresso.UiController;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.filters.LargeTest;

import org.hamcrest.Matcher;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(AndroidJUnit4.class)
@LargeTest
public class CreateTagTest {
    @Rule
    public ActivityScenarioRule<MainActivity> scenario=new ActivityScenarioRule<MainActivity>(MainActivity.class);

    @Before
    public void setup() throws InterruptedException{
        Thread.sleep(5000);
    }
    @Test
    public void testCreateTag() throws InterruptedException {
        onView(withId(R.id.tag_action_on_button)).perform(click());
        onView(withId(R.id.create_tag_button)).perform(click());
        onView(withId(R.id.tag_editable_input)).perform(ViewActions.typeText("sampleTag"));
        Thread.sleep(3000);
        closeSoftKeyboard();
        Thread.sleep(1000);
        onView(withId(R.id.create_new_tag_button)).perform(click());
        Thread.sleep(3000);
        onView(withText("SampleTag")).check(matches(isDisplayed()));
    }

    @After
    public void clear() {
        onView(withId(R.id.tags_recycler_view)).perform(RecyclerViewActions.actionOnItemAtPosition(0, clickChildViewWithId(R.id.remove_tag_from_list)));
        onView(withId(R.id.done_create_button)).perform(click());
    }

    /* Source: https://stackoverflow.com/questions/28476507/using-espresso-to-click-view-inside-recyclerview-item */
    public static ViewAction clickChildViewWithId(final int id) {
        return new ViewAction() {
            @Override
            public Matcher<View> getConstraints() {
                return null;
            }
            @Override
            public String getDescription() {
                return "Click on a child view with specified id.";
            }

            @Override
            public void perform(UiController uiController, View view) {
                View v = view.findViewById(id);
                v.performClick();
            }
        };
    }
}