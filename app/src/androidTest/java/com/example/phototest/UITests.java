package com.example.phototest;

import androidx.test.rule.ActivityTestRule;
import androidx.test.runner.AndroidJUnit4;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.closeSoftKeyboard;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.matcher.ViewMatchers.withId;

@RunWith(AndroidJUnit4.class)
public class UITests {
    @Rule
        public ActivityTestRule<MainActivity> mActivityRule =
                new ActivityTestRule<>(MainActivity.class);

        @Test
        public void TestFilter() {
            onView(withId(R.id.btnSearch)).perform(click());
            onView(withId(R.id.EndDate)).perform(typeText("20200201000000"), closeSoftKeyboard());
            onView(withId(R.id.StartDate)).perform(typeText("20200101000000"), closeSoftKeyboard());
            onView(withId(R.id.Caption)).perform(typeText("-"), closeSoftKeyboard());
            onView(withId(R.id.Searchbutton)).perform(click());
            for (int i = 0; i <= 5; i++) {
                onView(withId(R.id.right)).perform(click());
            }
        }
    }

