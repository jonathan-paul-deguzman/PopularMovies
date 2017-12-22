package com.example.jpdeguzman.popularmovies;

import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;

/**
 *  Tests {@link MainActivity} UI components
 */
@RunWith(AndroidJUnit4.class)
public class MainActivityTest {

    @Rule
    public ActivityTestRule<MainActivity> mActivityRule = new ActivityTestRule<>(MainActivity.class);

    @Test
    public void testSortByPopularMovies() {
        onView(withId(R.id.menu_sort_by_popular))
            .perform(click())
            .check(matches(isDisplayed()));
    }

    @Test
    public void testSortByTopRatedMovies() {
        onView(withId(R.id.menu_sort_by_top_rated))
                .perform(click())
                .check(matches(isDisplayed()));
    }

    @Test
    public void testSortByFavoriteMovies() {
        onView(withId(R.id.menu_sort_by_favorites))
                .perform(click())
                .check(matches(isDisplayed()));
    }

}
