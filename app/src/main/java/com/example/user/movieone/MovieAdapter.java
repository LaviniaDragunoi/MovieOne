package com.example.user.movieone;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import static java.lang.String.valueOf;

/**
 * Created by Lavinia Dragunoi on 2/20/2018. The MovieAdapter that will set up the recyclerView and
 * will help to populate the layouts
 */

public class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.MovieViewHolder> {
    //the list of movie that will fill the recyclerView;
    private List<MovieObject> moviesList;
    private Context mContext;

    /**
     * MovieAdapter constructor that will take the movieList to display within context
     *
     * @param context    the context within will be displayed the moviesList
     * @param moviesList the list of movie that will be displayed
     */
    public MovieAdapter(Context context, List<MovieObject> moviesList) {
        this.mContext = context;
        this.moviesList = moviesList;
    }

    /**
     * This method helps to create the Url for the movie poster
     *
     * @param moviePosterString
     * @return
     */
    public static String buildPosterUrl(String moviePosterString) {
        final String BASE_URL_POSTER = "https://image.tmdb.org/t/p/";
        final String IMAGE_SIZE = "w185";

        Uri buildUri = Uri.parse(BASE_URL_POSTER).buildUpon()
                .appendPath(IMAGE_SIZE)
                .appendEncodedPath(moviePosterString)
                .build();
        return buildUri.toString();
    }

    /**
     * This method will create Views each time the RecyclerView will need it
     *
     * @param parent   the ViewGroup that will within the ViewHolder
     * @param viewType
     * @return a new MovieViewHolder that will have the View for each item
     */
    @Override
    public MovieViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        //Inflate the layout list_item.xml
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_item, parent, false);
        return new MovieViewHolder(itemView);
    }

    /**
     * This method will be called to display information on a specific position and to register
     * a clickListener that will opened the detail layout for each movie item from the list.
     *
     * @param holder   this MovieViewHolder should be updated
     * @param position the position of the item within the adapter
     */
    @Override
    public void onBindViewHolder(MovieViewHolder holder, final int position) {
        //get the position
        final MovieObject movieItem = moviesList.get(position);

        ImageView moviePosterImageView = holder.moviePoster;
        String moviePosterUrlString = buildPosterUrl(movieItem.getMoviePoster());
        final TextView errorLoadMessage = holder.errorMessage;
        final TextView ratingValueTextView = holder.ratingValue;
        ratingValueTextView.setVisibility(View.INVISIBLE);
        final String ratingString = valueOf(movieItem.getRating());


        Picasso.with(mContext).load(moviePosterUrlString).into(moviePosterImageView,
                new com.squareup.picasso.Callback() {
                    @Override
                    public void onSuccess() {
                        ratingValueTextView.setText(ratingString);
                        ratingValueTextView.setVisibility(View.VISIBLE);
                    }

                    @Override
                    public void onError() {
                        errorLoadMessage.setVisibility(View.VISIBLE);
                    }
                });

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, DetailActivity.class);
                intent.putExtra("Movie", movieItem);
                mContext.startActivity(intent);
            }
        });
    }

    /**
     * this method is counting the number of items in the list displayed
     *
     * @return the number of items available
     */
    @Override
    public int getItemCount() {
        return moviesList.size();
    }

    /**
     * This method adds new objects to the listVew
     */
    public void addAll(List<MovieObject> moviesList) {
        this.moviesList.clear();
        this.moviesList.addAll(moviesList);
        notifyDataSetChanged();
    }

    /**
     * This method clear the news list
     */
    public void clearAll() {
        this.moviesList.clear();
        notifyDataSetChanged();
    }

    /**
     * This class will help hold the views
     */
    class MovieViewHolder extends RecyclerView.ViewHolder {
        //Variables that will help to set the information needed in the RecyclerView that will display
        // the movies list in the activity_main.xml layout.
        TextView ratingValue;
        ImageView moviePoster;
        TextView errorMessage;

        //ViewHolder's constructor
        public MovieViewHolder(View itemView) {
            super(itemView);

            //Find Views that will display each item
            ratingValue = itemView.findViewById(R.id.rating_value_tv);
            moviePoster = itemView.findViewById(R.id.movie_image_List);
            errorMessage = itemView.findViewById(R.id.error_loading_movie_list);
        }
    }
}
