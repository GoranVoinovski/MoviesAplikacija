package com.znaci.goran.moviesaplikacija.listeners;

import android.widget.TextView;

import com.znaci.goran.moviesaplikacija.models.Movie;
import com.znaci.goran.moviesaplikacija.models.Shows;

/**
 * Created by Goran on 2/3/2018.
 */

public interface OnRowShowClickListener {
    public void onRowClick(Shows movie, int position);
    public void onRowFavClick(Shows movie, int position, TextView tv);
    public void onRowWatchClick(Shows movie, int position, TextView tv);
}