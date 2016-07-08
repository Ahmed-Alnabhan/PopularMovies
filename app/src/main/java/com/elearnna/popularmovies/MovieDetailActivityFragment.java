package com.elearnna.popularmovies;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * A placeholder fragment containing a simple view.
 */
public class MovieDetailActivityFragment extends Fragment {
    // data of selected movie
    private String posterPath;
    private String title;
    private String releaseDate;
    private String voteAverage;
    private String overView;
    private final String MOVIE_POSTER_BASE_URL = "https://image.tmdb.org/t/p/w500";
    private String posterFullPath;
    // Widgets and fields
    private ImageView moviePoster;
    private TextView movieTitle;
    private TextView movieDate;
    private TextView movieVoteRate;
    public MovieDetailActivityFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_movie_detail, container, false);
        Intent intent = getActivity().getIntent();
        //if(intent != null && intent.hasExtra(Intent.EXTRA_TEXT)){
        String movie = intent.getStringExtra("Movie");
        try {
            JSONObject jsonMovie = new JSONObject(movie);
            posterPath = jsonMovie.getString("poster_path");
            title = jsonMovie.getString("title");
            releaseDate = jsonMovie.getString("release_date");
            voteAverage = jsonMovie.getString("vote_average");
            overView = jsonMovie.getString("overview");
            posterFullPath = MOVIE_POSTER_BASE_URL.concat(posterPath);
            moviePoster = (ImageView) rootView.findViewById(R.id.moviePoster);
            Picasso.with(getContext()).load(posterFullPath).into(moviePoster);
            movieTitle = (TextView)rootView.findViewById(R.id.movieTitle);
            movieDate = (TextView)rootView.findViewById(R.id.movieDate);
            movieVoteRate = (TextView)rootView.findViewById(R.id.movieVoteRate);
            movieTitle.setText(title);
            movieDate.setText(releaseDate);
            movieVoteRate.setText(voteAverage + "/10");
            ((TextView) rootView.findViewById(R.id.movieThumb)).setText(overView);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        //}
        return rootView;
    }
}
