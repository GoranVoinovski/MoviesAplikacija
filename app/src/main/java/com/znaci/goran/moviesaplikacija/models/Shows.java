package com.znaci.goran.moviesaplikacija.models;

import java.util.ArrayList;

/**
 * Created by Goran on 2/11/2018.
 */

public class Shows {

    public int id;
    public float vote_average;
    public String name;
    public String poster_path;
    public String backdrop_path;
    public String overview;
    public String first_air_date;
    public String last_air_date;
    public String original_name;
    public int number_of_episodes;
    public int number_of_seasons;
    public String status;
    public boolean favorite;
    public boolean watchlist;
    public CreditsModel credits;
    public ArrayList<Genre> genres;
}
