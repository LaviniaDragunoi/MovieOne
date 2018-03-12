package com.example.user.movieone;

import android.content.Intent;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import static java.lang.String.valueOf;

/**
 * This class will help to populate the details activity layout with each movie characteristics.
 */
public class DetailActivity extends AppCompatActivity {

    TextView releaseDateTV, ratingTV, overviewTV, voteCountTV;
    ImageView posterMovie;
    RatingBar ratingBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        posterMovie = findViewById(R.id.movie_poster_details_view);

        Intent intent = getIntent();
        MovieObject currentMovie = intent.getParcelableExtra("Movie");

        String moviePosterUrlString = MovieAdapter.buildPosterUrl(currentMovie.getMoviePoster());
        Picasso.with(this).load(moviePosterUrlString).into(posterMovie);
        displayMovieUI(currentMovie);

        setTitle(currentMovie.getOriginalTitle());
    }

    /**
     * This method takes a movieObject and extracts from it each info needed to be displayed
     * in the activity_detail.xml layout
     * @param movie the MovieObject that contains all information needed
     */
    private void displayMovieUI(MovieObject movie) {
        releaseDateTV = findViewById(R.id.release_date_tv);
        releaseDateTV.setText(movie.getReleaseDate());

        ratingTV = findViewById(R.id.rating_value_detail_tv);
        ratingTV.setText(valueOf(movie.getRating()));

        overviewTV = findViewById(R.id.overview_tv);
        overviewTV.setText(movie.getOverview());

        voteCountTV = findViewById(R.id.vote_count);
        voteCountTV.setText(valueOf(movie.getVoteCount()));

        ratingBar = findViewById(R.id.rating_bar);
        ratingBar.setRating((float) movie.getRating());
    }
}
