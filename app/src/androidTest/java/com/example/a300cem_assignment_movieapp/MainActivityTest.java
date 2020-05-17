package com.example.a300cem_assignment_movieapp;


import android.os.SystemClock;

import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.filters.SmallTest;
import androidx.test.rule.ActivityTestRule;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static androidx.test.espresso.Espresso.onData;
import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.closeSoftKeyboard;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.action.ViewActions.typeText;
import static org.hamcrest.Matchers.anything;

@RunWith(AndroidJUnit4.class)
@SmallTest
public class MainActivityTest {

    @Rule
    public ActivityTestRule<MainActivity> activityTestRule = new ActivityTestRule<>(
            MainActivity.class);

    @Test
    public void changeText_sameActivity() {
        onView(withId(R.id.edt_movieName)).perform(typeText("war"), closeSoftKeyboard());
        onView(withId(R.id.btn_search)).perform(click());
        onView(withId(R.id.lv_movieList)).check(matches(isDisplayed()));
        SystemClock.sleep(1000); // To set a delay of 1 second for waiting for listview is finished to get all movie
        onData(anything()).inAdapterView(withId(R.id.lv_movieList)).atPosition(0).perform(click());
    }
}