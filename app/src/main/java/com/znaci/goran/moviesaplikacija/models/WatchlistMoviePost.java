package com.znaci.goran.moviesaplikacija.models;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by Goran on 2/16/2018.
 */

public class WatchlistMoviePost implements Serializable{
  @SerializedName("media_type")
public String media_type = "movie";
  @SerializedName("media_id")
public int media_id;
  @SerializedName("watchlist")
public boolean watchlist;
}
