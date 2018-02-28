package com.znaci.goran.moviesaplikacija.models;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by Goran on 2/20/2018.
 */

public class Rated implements Serializable {

    @SerializedName("value")
    public float value;

    public int id;
}
