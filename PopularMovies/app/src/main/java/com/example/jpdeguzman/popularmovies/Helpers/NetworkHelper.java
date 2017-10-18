package com.example.jpdeguzman.popularmovies.Helpers;

import android.net.Uri;
import android.util.Log;

import com.example.jpdeguzman.popularmovies.R;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Scanner;

/**
 * Created by jpdeguzman on 10/17/17.
 */

public class NetworkHelper {

    private static final String TAG = NetworkHelper.class.getSimpleName();

    private static final String NETWORK_BASE_URL = "http://api.themoviedb.org/3";
    private static final String ENDPOINT_POPULAR_MOVIE = "/movie/popular";
    private static final String ENDPOINT_TOP_RATED_MOVIE = "/movie/top_rated";

    /**
     * Note that any users attempting to build and run this application should include their own
     * api key for The Movie DB. Api keys can be created here:
     *
     * See <a href="https://www.themoviedb.org">https://www.themoviedb.org</a>
     */
    private static final String USER_API_KEY = "";
    private static final String API_KEY_PARAM = "api_key";

    public static URL buildUrl(String typeOfEndpoint) {
        String selectedEndpoint = determineEndpointSelection(typeOfEndpoint);
        Uri builtUri = Uri.parse(NETWORK_BASE_URL + selectedEndpoint).buildUpon()
                .appendQueryParameter(API_KEY_PARAM, USER_API_KEY)
                .build();

        URL url = null;
        try {
            url = new URL(builtUri.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        Log.i(TAG, R.string.tmdb_build_uri + url.toString());

        return url;
    }

    public static String getResponseFromHttpUrl(URL url) throws IOException {
        HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
        try {
            InputStream in = urlConnection.getInputStream();

            Scanner scanner = new Scanner(in);
            scanner.useDelimiter("\\A");

            boolean hasInput = scanner.hasNext();
            if (hasInput) {
                return scanner.next();
            } else {
                return null;
            }
        } finally {
            urlConnection.disconnect();
        }
    }

    private static String determineEndpointSelection(String typeOfEndpoint) {
        String selectedEndpoint;
        switch (typeOfEndpoint) {
            case "popular":
                selectedEndpoint = ENDPOINT_POPULAR_MOVIE;
                break;
            case "top_rated":
                selectedEndpoint = ENDPOINT_TOP_RATED_MOVIE;
                break;
            default:
                selectedEndpoint = "";
                break;
        }

        return selectedEndpoint;
    }
}
