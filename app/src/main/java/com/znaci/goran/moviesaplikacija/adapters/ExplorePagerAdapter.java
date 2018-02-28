package com.znaci.goran.moviesaplikacija.adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentStatePagerAdapter;

import java.util.ArrayList;

/**
 * Created by Goran on movie2/6/2018.
 */

public class ExplorePagerAdapter extends FragmentPagerAdapter{


    ArrayList<Fragment> fragments = new ArrayList<Fragment>();
    ArrayList<String> titles = new ArrayList<String>();


    public void dodadiFragment (Fragment fragment, String title){
        titles.add(title);
        fragments.add(fragment);

    }

    public ExplorePagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        return fragments.get(position);
    }

    @Override
    public int getCount() {
        return fragments.size();
    }

    @Override
    public CharSequence getPageTitle(int position) {return titles.get(position);}
}
