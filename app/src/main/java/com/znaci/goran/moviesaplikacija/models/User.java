package com.znaci.goran.moviesaplikacija.models;

import android.net.Uri;

/**
 * Created by Goran on movie2/6/2018.
 */

public class User {

  public String username;
  public Avatar avatar;
  public int id;
  public String name;
  public boolean success;
  public String guest_session_id;
  public String session_id;
  public String expires_at;
  public String request_token;

    public User(String username, String name) {
        this.username = username;
        this.name = name;
    }
}
