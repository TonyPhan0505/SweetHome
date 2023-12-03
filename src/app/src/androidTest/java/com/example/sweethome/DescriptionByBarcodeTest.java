package com.example.sweethome;

import static androidx.test.espresso.Espresso.closeSoftKeyboard;
import static androidx.test.espresso.Espresso.onData;
import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;

import static org.hamcrest.CoreMatchers.anything;
import static org.mockito.AdditionalMatchers.not;

import androidx.test.espresso.action.ViewActions;
import androidx.test.espresso.matcher.ViewMatchers;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.filters.LargeTest;
import androidx.test.rule.ActivityTestRule;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(AndroidJUnit4.class)
@LargeTest
public class DescriptionByBarcodeTest {
    @Rule
    public ActivityTestRule<ManageItemActivity> activityRule = new ActivityTestRule<>(ManageItemActivity.class);

    @Before
    public void setup() throws InterruptedException{
        Thread.sleep(5000);
    }

    @Test
    public void testBarcode() throws InterruptedException {
        onView(withId(R.id.barcode_scan_icon)).perform(click());
        Thread.sleep(3000);
        // scan barcode to get description
        // check if there is a description is description field
    }

}
