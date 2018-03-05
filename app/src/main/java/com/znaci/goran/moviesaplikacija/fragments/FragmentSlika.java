package com.znaci.goran.moviesaplikacija.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;
import com.znaci.goran.moviesaplikacija.R;
import com.znaci.goran.moviesaplikacija.helpers.ApiCalls;
import com.znaci.goran.moviesaplikacija.models.ImageModel;
import com.znaci.goran.moviesaplikacija.models.Images;
import com.znaci.goran.moviesaplikacija.preferencesManager.LogInPreferences;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Created by Goran on 3/5/2018.
 */

public class FragmentSlika extends Fragment {
    @BindView(R.id.slika)
    ImageView pic;
    private Unbinder mUnbind;
    ImageModel imageModel;
    Images slika;
    int position;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragmentslika, null);
        mUnbind = ButterKnife.bind(this, view);

        String link = getArguments().getString("link");
        String complete = "http://image.tmdb.org/t/p/w500" + link;
        Picasso.with(getActivity()).load(complete).centerInside().fit().into(pic);




        return view;
    }

}
