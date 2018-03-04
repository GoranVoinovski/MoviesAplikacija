package com.znaci.goran.moviesaplikacija.listeners;

import android.widget.TextView;

import com.znaci.goran.moviesaplikacija.models.Movie;

/**
 * Created by Goran on 2/3/2018.
 */

public interface OnRowMovieClickListener {
    public void onRowClick(Movie movie, int position);
    public void onRowFavClick(Movie movie, int position, TextView tv);
    public void onRowWatchClick(Movie movie, int position, TextView tv);
}