package com.znaci.goran.moviesaplikacija.listeners;

import com.znaci.goran.moviesaplikacija.models.Cast;
import com.znaci.goran.moviesaplikacija.models.Genre;

/**
 * Created by Goran on 2/3/2018.
 */

public interface OnRowGenreClickListener {
    public void onRowClick(Genre genre, int position);
}