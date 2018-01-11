package com.example.jpdeguzman.popularmovies.moviedetails;

import com.example.jpdeguzman.popularmovies.data.models.MovieModel;
import com.example.jpdeguzman.popularmovies.data.models.ReviewModel;
import com.example.jpdeguzman.popularmovies.data.models.VideoModel;

import java.util.ArrayList;

/**
 * Created by jpdeguzman on 1/9/18.
 */

public interface MovieDetailsContract {

    interface View {
        void bindDataToAdapter(ArrayList<MovieModel> detailsList, ArrayList<VideoModel>
                videoList, ArrayList<ReviewModel> reviewList);
    }

    interface Presenter {
        void getDetailsResults(MovieModel movieSelected);
    }
}
