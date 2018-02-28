package com.znaci.goran.moviesaplikacija.listeners;

import com.znaci.goran.moviesaplikacija.models.Movie;

/**
 * Created by Goran on 2/3/2018.
 */

public interface OnRowMovieClickListener {
    public void onRowClick(Movie movie, int position);
}