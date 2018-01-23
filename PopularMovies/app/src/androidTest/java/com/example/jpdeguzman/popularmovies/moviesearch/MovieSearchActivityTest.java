package com.example.jpdeguzman.popularmovies.moviesearch;

import android.app.Activity;
import android.app.Instrumentation;
import android.content.Context;
import android.content.Intent;
import android.support.test.espresso.contrib.RecyclerViewActions;
import android.support.test.espresso.matcher.ViewMatchers;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import com.example.jpdeguzman.popularmovies.R;
import com.example.jpdeguzman.popularmovies.moviedetails.MovieDetailsActivity;
import com.example.jpdeguzman.popularmovies.moviesearch.MovieSearchActivity;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.InstrumentationRegistry.getInstrumentation;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.Espresso.openActionBarOverflowOrOptionsMenu;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.intent.Intents.intended;
import static android.support.test.espresso.intent.Intents.intending;
import static android.support.test.espresso.intent.matcher.IntentMatchers.anyIntent;
import static android.support.test.espresso.intent.matcher.IntentMatchers.hasComponent;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.CoreMatchers.allOf;

@RunWith(AndroidJUnit4.class)
public class MovieSearchActivityTest {

    private Context mContext = getInstrumentation().getTargetContext();

    @Rule
    public ActivityTestRule<MovieSearchActivity> mActivityRule = new ActivityTestRule<>(MovieSearchActivity.class);

    @Test
    public void testSortByPopularMovies() {
        openActionBarOverflowOrOptionsMenu(mContext);
        onView(ViewMatchers.withText(R.string.menu_sort_popular))
                .perform(click());

        onView(withId(R.id.rv_movie_posters))
                .check(matches(isDisplayed()));
    }

    @Test
    public void testSortByTopRatedMovies() {
        openActionBarOverflowOrOptionsMenu(mContext);
        onView(withText(R.string.menu_sort_top_rated))
                .perform(click());

        onView(withId(R.id.rv_movie_posters))
                .check(matches(isDisplayed()));
    }

    @Test
    public void testSortByFavoriteMovies() {
        openActionBarOverflowOrOptionsMenu(mContext);
        onView(withText(R.string.menu_sort_favorites))
                .perform(click());

        onView(withId(R.id.rv_movie_posters))
                .check(matches(isDisplayed()));
    }

    public void testClickMoviePosterItem() {
        Intent intent = new Intent();
        Instrumentation.ActivityResult activityResult = new Instrumentation.ActivityResult(Activity.RESULT_OK, intent);
        intending(anyIntent()).respondWith(activityResult);

        onView(withId(R.id.rv_movie_posters))
                .perform(RecyclerViewActions.actionOnItemAtPosition(0, click()));

        intended(allOf(hasComponent(MovieDetailsActivity.class.getSimpleName())));
    }
}
