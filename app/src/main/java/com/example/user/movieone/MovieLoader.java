package com.example.user.movieone;

import android.content.AsyncTaskLoader;
import android.content.Context;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Lavinia Dragunoi on 2/25/2018.
 * this class helps to move on the background thread those actions that could cause malfunction
 * to the device
 */

public class MovieLoader extends AsyncTaskLoader<List<MovieObject>> {
    private String murl;

    public MovieLoader(Context context, String url) {
        super(context);
        murl = url;
    }

    @Override
    protected void onStartLoading() {
        forceLoad();
    }

    @Override
    public List<MovieObject> loadInBackground() {
        if (murl == null) return null;
        return JsonUtils.fetchMoviesData(murl);
    }
}

