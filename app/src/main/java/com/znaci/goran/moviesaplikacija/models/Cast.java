package com.znaci.goran.moviesaplikacija.models;

import java.io.Serializable;

/**
 * Created by Goran on 2/7/2018.
 */

public class Cast implements Serializable {

  public int id;
  public String character;
  public String name;
  public int order;
  public String release_date;
  public String original_title;
  public String poster_path;
  public String profile_path;
}
