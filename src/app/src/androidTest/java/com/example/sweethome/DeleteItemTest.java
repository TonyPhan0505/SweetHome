package com.example.sweethome;

import static androidx.test.espresso.Espresso.onData;
import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.assertion.ViewAssertions.doesNotExist;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.CoreMatchers.anything;
import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.CoreMatchers.is;

import android.widget.EditText;

import androidx.test.core.app.ActivityScenario;
import androidx.test.espresso.action.ViewActions;
import androidx.test.espresso.matcher.RootMatchers;
import androidx.test.espresso.matcher.ViewMatchers;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.filters.LargeTest;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

// sources: https://stackoverflow.com/questions/27382147/write-a-test-that-clicks-on-views-inside-popupwindow
@RunWith(AndroidJUnit4.class)
@LargeTest
public class MainActivityTest {
    @Rule
    public ActivityScenarioRule<MainActivity> scenario=new ActivityScenarioRule<MainActivity>(MainActivity.class);

    @Test
    public void testDeleteItem() {
//        onView(withId(R.id.add_item_button)).perform(click);
//        onView(withId(R.id.item_name_edittext)).perform(ViewActions.typeText("Couch"));
//        onView(withId(R.id.item_description_edittext)).perform(ViewActions.typeText("This is an old couch"));
//        onView(withId(R.id.item_make_edittext)).perform(ViewActions.typeText("IKEA"));
//        onView(withId(R.id.purchasedate_edittext)).perform(ViewActions.typeText("1999-02-14"));
//        onView(withId(R.id.value_edittext)).perform(ViewActions.typeText("$1199"));
//        onView(withId(R.id.donebutton)).perform(click());
//        onData(withId(R.id.item_checkBox)).inAdapterView(withId(R.id.item_list)).atPosition(0).perform(click());
        onView(withId(R.id.delete_action_button)).perform(click());
//        onView(ViewMatchers.withContentDescription("Delete"))
//                .inRoot(RootMatchers.isPlatformPopup())
//                .perform(ViewActions.click());
    }
}

