package com.doordashlite.oye.doordashlite;

import android.app.Activity;
import android.app.PendingIntent;
import android.content.ComponentName;
import android.content.Loader;
import android.support.test.espresso.action.ScrollToAction;
import android.support.test.espresso.intent.Intents;
import android.support.test.internal.runner.junit4.AndroidAnnotatedBuilder;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.InstrumentationRegistry.getTargetContext;
import static android.support.test.espresso.Espresso.onData;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.intent.Intents.intended;
import static android.support.test.espresso.intent.matcher.IntentMatchers.hasComponent;
import static android.support.test.espresso.matcher.ViewMatchers.hasDescendant;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;

@RunWith(AndroidJUnit4.class)
// Control Launching the app and running test
public class MainActivityBasicTest extends ExampleInstrumentedTest {
    @Rule
    public ActivityTestRule<MainActivity>  mActivityTestRule
            = new ActivityTestRule<>(MainActivity.class);

    @Test
    public void LocateView() {
        // locate the view
        onView( (withId(R.id.recyclerView)) );

        // Perform click
        onView( (withId(R.id.recyclerView)) ).perform( click() );

    }



}
