package com.znaci.goran.moviesaplikacija.models;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by Goran on movie2/6/2018.
 */

public class Movie implements Serializable{

  public int id;
  public String title;
  public String release_date;
  public float vote_average;
  public String original_title;
  public String backdrop_path;
  public String poster_path;
  public String overview;
  public boolean watchlist;
  public boolean favorite;
  public CreditsModel credits;
  public ArrayList<Genre>genres;
  transient boolean favorites;

    public Movie() {


    }

  public boolean isFavorites() {
    return favorites;
  }

  public void setFavorites(boolean favorites) {
    this.favorites = favorites;
  }
}
