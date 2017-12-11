package com.example.jpdeguzman.popularmovies;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.jpdeguzman.popularmovies.Adapters.VideoAdapter;
import com.example.jpdeguzman.popularmovies.Clients.MovieClient;
import com.example.jpdeguzman.popularmovies.Models.MovieModel;
import com.example.jpdeguzman.popularmovies.Models.ReviewModel;
import com.example.jpdeguzman.popularmovies.Models.ReviewResultsModel;
import com.example.jpdeguzman.popularmovies.Models.VideoModel;
import com.example.jpdeguzman.popularmovies.Models.VideoResultsModel;
import com.example.jpdeguzman.popularmovies.Services.MovieDetailsService;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * MovieDetailsActivity is responsible for displaying the corresponding movie information from the
 * movie that was selected in {@link MainActivity}.
 */
public class MovieDetailsActivity extends AppCompatActivity {

    private static final String TAG = MovieDetailsActivity.class.getSimpleName();

    private static final String IMAGE_BASE_URL = "http://image.tmdb.org/t/p/";

    private static final String IMAGE_RECOMMENDED_SIZE = "w500";

    private static final String YOUTUBE_BASE_URL = "https://www.youtube.com/watch?v=";

    private ImageView mMoviePosterImageView;

    private TextView mMovieTitleTextView;

    private TextView mMovieReleaseDateTextView;

    private TextView mMovieUserRatingTextView;

    private TextView mMovieOverviewTextView;

    private ListView mMovieTrailersListView;

    private MovieModel mMovieDetails;

    private ArrayList<VideoModel> mVideoResultsList = new ArrayList<>();

    private ArrayList<String> mVideoNamesList = new ArrayList<>();

    private ArrayList<ReviewModel> mReviewResultsList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_details);
        Bundle data = getIntent().getExtras();
        mMovieDetails = data.getParcelable(".MovieModel");

        mMoviePosterImageView = findViewById(R.id.iv_movie_poster);
        mMovieTitleTextView = findViewById(R.id.tv_movie_title);
        mMovieReleaseDateTextView = findViewById(R.id.tv_movie_release_date);
        mMovieUserRatingTextView = findViewById(R.id.tv_movie_user_rating);
        mMovieOverviewTextView = findViewById(R.id.tv_movie_overview);
        mMovieTrailersListView = findViewById(R.id.lv_movie_trailers);
    }

    @Override
    protected void onStart() {
        super.onStart();
        setupMoviePage();
        setupMovieTrailers();
        setupMovieReviews();
    }

    private void setupMoviePage() {
        String moviePosterPath = mMovieDetails.getMoviePosterPath();
        Picasso.with(this)
                .load(IMAGE_BASE_URL + IMAGE_RECOMMENDED_SIZE + moviePosterPath)
                .into(mMoviePosterImageView);
        mMovieTitleTextView.setText(mMovieDetails.getMovieTitle());
        mMovieReleaseDateTextView.setText(mMovieDetails.getMovieReleaseDate());
        mMovieUserRatingTextView.setText(mMovieDetails.getMovieUserRating());
        mMovieOverviewTextView.setText(mMovieDetails.getMovieOverview());
    }

    private void setupMovieTrailers() {
        MovieDetailsService movieDetails = MovieClient.getMovieDetailsService();
        Call<VideoResultsModel> videoResults = movieDetails.getMovieVideos(mMovieDetails.getMovieId(),
                getResources().getString(R.string.api_key));
        if (videoResults != null) {
            videoResults.enqueue(new Callback<VideoResultsModel>() {
                @Override
                public void onResponse(Call<VideoResultsModel> call, Response<VideoResultsModel> response) {
                    if (response.isSuccessful()) {
                        Log.d("test", "got movie videos");
                        Log.i(TAG, "setupMovieTrailers:onResponse:isSuccessful");
                        if (response.body() != null) {
                            mVideoResultsList = response.body().getVideos();
                            for (VideoModel video : mVideoResultsList) {
                                mVideoNamesList.add(video.getVideoName());
                            }
                            if (mVideoResultsList != null && mVideoNamesList != null) {
                                ArrayAdapter<String> adapter = new ArrayAdapter<>(
                                        getApplicationContext(),
                                        android.R.layout.simple_list_item_1,
                                        mVideoNamesList
                                );
                                mMovieTrailersListView.setAdapter(adapter);
                            }
                        }
                    } else {
                        Log.i(TAG, "setupMovieTrailers:onResponse:notSuccessful:" + response.message());
                    }
                }

                @Override
                public void onFailure(Call<VideoResultsModel> call, Throwable t) {
                    Log.i(TAG, "setupMovieTrailers:onFailure");
                }
            });
        }
    }

    private void setupMovieReviews() {
        MovieDetailsService movieDetails = MovieClient.getMovieDetailsService();
        Call<ReviewResultsModel> reviewResults = movieDetails.getMovieReviews(mMovieDetails.getMovieId(),
                getResources().getString(R.string.api_key));
        if (reviewResults != null) {
            reviewResults.enqueue(new Callback<ReviewResultsModel>() {
                @Override
                public void onResponse(Call<ReviewResultsModel> call, Response<ReviewResultsModel> response) {
                    if (response.isSuccessful()) {
                        Log.i(TAG, "setupMovieReviews:onResponse:isSuccessful");
                        if (response.body() != null) {
                            mReviewResultsList = response.body().getReviews();
                            for (ReviewModel review : mReviewResultsList) {
                                Log.d(TAG, "Review author: " + review.getReviewAuthor());
                                Log.d(TAG, "Review content: " + review.getReviewContent());
                            }
                        }
                    } else {
                        Log.i(TAG, "setupMovieReviews:onResponse:notSuccessful: " + response.message());
                    }
                }

                @Override
                public void onFailure(Call<ReviewResultsModel> call, Throwable t) {
                    Log.i(TAG, "setupMovieReviews:onFailure");
                }
            });
        }
    }
}
