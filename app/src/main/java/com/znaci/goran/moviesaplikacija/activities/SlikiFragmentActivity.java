package com.znaci.goran.moviesaplikacija.activities;

import android.content.Intent;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.znaci.goran.moviesaplikacija.R;
import com.znaci.goran.moviesaplikacija.adapters.SlikiAdapter;
import com.znaci.goran.moviesaplikacija.models.ImageModel;
import com.znaci.goran.moviesaplikacija.preferencesManager.LogInPreferences;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SlikiFragmentActivity extends AppCompatActivity {
    public @BindView(R.id.pager)
    ViewPager mojPager;
    SlikiAdapter adapter;
    ImageModel imageModel = new ImageModel();
    int position;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getWindow().getDecorView().setSystemUiVisibility( View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION // hide nav bar
                | View.SYSTEM_UI_FLAG_FULLSCREEN // hide status bar
                | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY );

        setContentView(R.layout.activity_sliki_fragment);
        ButterKnife.bind(this);

        adapter = new SlikiAdapter(this.getSupportFragmentManager());
        imageModel = LogInPreferences.getImages(this);
        Intent pozicija = getIntent();
        position = pozicija.getIntExtra("Position", 0);

        adapter.addSliki(imageModel.backdrops);
        mojPager.setAdapter(adapter);
        mojPager.setCurrentItem(position);


    }
}
