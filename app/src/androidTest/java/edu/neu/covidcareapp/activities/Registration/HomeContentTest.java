package edu.neu.covidcareapp.activities.Registration;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;

import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import edu.neu.covidcareapp.R;
import edu.neu.covidcareapp.activities.Home;

@RunWith(AndroidJUnit4.class)
public class HomeContentTest {
    @Rule
    public ActivityScenarioRule<Home> activityRule =
            new ActivityScenarioRule<>(Home.class);
    @Test
    public void testVisibilityCommunityButton() {
        onView(withId(R.id.communityBtn)).check(matches(isDisplayed()));
    }

    @Test
    public void testVisibilityDailyCheckButton() {
        onView(withId(R.id.checkBtn)).check(matches(isDisplayed()));
    }

    @Test
    public void testVisibilityNewsAndArticleButton() {
        onView(withId(R.id.newsABtn)).check(matches(isDisplayed()));
    }

    @Test
    public void testVisibilityLoginUserPhoto() {
        onView(withId(R.id.LoginUserPhoto)).check(matches(isDisplayed()));
    }
}