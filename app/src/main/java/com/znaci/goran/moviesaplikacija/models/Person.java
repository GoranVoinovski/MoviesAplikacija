package com.znaci.goran.moviesaplikacija.models;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by Goran on 2/8/2018.
 */

public class Person implements Serializable {

  public int id;
  public String profile_path;
  public String name;
  public String deathday;
  public String birthday;
  public String biography;
  public ArrayList<Movie>known_for;
}
