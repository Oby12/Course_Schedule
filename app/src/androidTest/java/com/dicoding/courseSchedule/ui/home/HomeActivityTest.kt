package com.dicoding.courseSchedule.ui.home

import androidx.test.core.app.ActivityScenario
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.intent.Intents
import androidx.test.espresso.intent.Intents.intended
import androidx.test.internal.runner.junit4.AndroidJUnit4ClassRunner
import com.dicoding.courseschedule.ui.home.HomeActivity
import androidx.test.espresso.matcher.ViewMatchers.withId
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.intent.matcher.IntentMatchers
import org.junit.runner.RunWith
import com.dicoding.courseschedule.R
import com.dicoding.courseschedule.ui.add.AddCourseActivity

@RunWith(AndroidJUnit4ClassRunner::class)
class HomeActivityTest{

    @Before
    fun setup(){
        ActivityScenario.launch(HomeActivity::class.java)
    }

    @Test
    fun clickActionAdd(){
        Intents.init()
        onView(withId(R.id.action_add)).check(matches(isDisplayed()))
        onView(withId(R.id.action_add)).perform(click())
        intended(IntentMatchers.hasComponent(AddCourseActivity::class.java.name))
    }
}