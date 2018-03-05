package com.znaci.goran.moviesaplikacija.adapters;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import com.znaci.goran.moviesaplikacija.fragments.FragmentSlika;
import com.znaci.goran.moviesaplikacija.models.ImageModel;
import com.znaci.goran.moviesaplikacija.models.Images;

import java.util.ArrayList;

/**
 * Created by goran on 15.12.17.
 */

public class SlikiAdapter extends FragmentPagerAdapter{

    ImageModel imageModel = new ImageModel();

    public void addSliki(ArrayList<Images> sliki){imageModel.backdrops = sliki;}

    public SlikiAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
            Bundle args = new Bundle();
            args.putString("link", imageModel.backdrops.get(position).file_path);
            args.putInt("pozicija",position);
            FragmentSlika fragment = new FragmentSlika();
            fragment.setArguments(args);
            return fragment;
    }

    @Override
    public int getCount() {
        return imageModel.backdrops.size();
    }
}
