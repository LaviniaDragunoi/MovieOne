package com.example.user.movieone;


import android.annotation.SuppressLint;
import android.app.LoaderManager;
import android.content.Context;
import android.content.Loader;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DrawableUtils;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;


import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import static java.lang.Integer.valueOf;

/**
 * Main class of the project
 */
public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks
        <List<MovieObject>>, NavigationView.OnNavigationItemSelectedListener {

    private static final int MOVIE_LOADER_ID = 3;
    public String sortBy = "top_rated";
    private MovieAdapter mAdapter;
    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle actionBarDrawerToggle;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        progressBar = findViewById(R.id.progress_bar);

        //setting up the navigation drawer
        drawerLayout = findViewById(R.id.drawer);
        actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout,
                R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        //find the recycler view that will host the movie list
        RecyclerView movieRV = findViewById(R.id.movie_list);
        View noConnectionView = findViewById(R.id.no_connection_group);

        //set the movie list
        int gridRows = valueOf(getString(R.string.grid_rows));
        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(movieRV.getContext(),
                gridRows);
        movieRV.setLayoutManager(layoutManager);

        mAdapter = new MovieAdapter(this, new ArrayList<MovieObject>());
        movieRV.setAdapter(mAdapter);

        //if is no connection an error message will appear on the device
        if (isConnected()) {
            LoaderManager loaderManager = getLoaderManager();
            loaderManager.initLoader(MOVIE_LOADER_ID, null, this);
        } else {
            noConnectionView.setVisibility(View.VISIBLE);
        }
    }

    /**
     * This is a method that will verify if the device has internet connection
     * @return true if exists connection and false otherwise
     */
    private boolean isConnected() {
        ConnectivityManager connectivityManager = (ConnectivityManager)
                getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        boolean hasConnection = false;
        if (networkInfo != null && networkInfo.isConnected()) hasConnection = true;
        return hasConnection;
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        // Handle navigation view item clicks here.
        TextView selectedIconPopularity = findViewById(R.id.popularity_list_movie_title);
        TextView selectedIconRated = findViewById(R.id.rated_list_movie_title);

        int id = item.getItemId();
        if (id == R.id.most_popular) {
            // sets order for the movie list by the most popular
            sortBy = getString(R.string.popular);
            drawerLayout.closeDrawer(GravityCompat.START);
            getLoaderManager().restartLoader(MOVIE_LOADER_ID, null, this);
            selectedIconRated.setVisibility(View.GONE);
            selectedIconPopularity.setVisibility(View.VISIBLE);
        } else if (id == R.id.top_rated) {
            // sets order for the movie list by the top rated
            sortBy = getString(R.string.rated);
            drawerLayout.closeDrawer(GravityCompat.START);
            getLoaderManager().restartLoader(MOVIE_LOADER_ID, null, this);
            selectedIconPopularity.setVisibility(View.GONE);
            selectedIconRated.setVisibility(View.VISIBLE);
        }
        return true;
    }

    @Override
    public Loader<List<MovieObject>> onCreateLoader(int id, Bundle bundle) {
        URL requestUrl = JsonUtils.createUrl(sortBy);
        return new MovieLoader(this, requestUrl.toString());
    }

    @Override
    public void onLoadFinished(Loader<List<MovieObject>> loader, List<MovieObject> movieObjectList) {
        progressBar.setVisibility(View.GONE);
        mAdapter.addAll(movieObjectList);
    }

    @Override
    public void onLoaderReset(Loader<List<MovieObject>> loader) {
        mAdapter.clearAll();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (actionBarDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * When the back button is clicked and the user has the navigationDrawer opened, this method
     * will sent on the main Activity layout
     */
    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }
}
