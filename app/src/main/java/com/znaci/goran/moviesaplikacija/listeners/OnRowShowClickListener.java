package com.znaci.goran.moviesaplikacija.listeners;

import com.znaci.goran.moviesaplikacija.models.Shows;

/**
 * Created by Goran on 2/3/2018.
 */

public interface OnRowShowClickListener {
    public void onRowClick(Shows movie, int position);
}