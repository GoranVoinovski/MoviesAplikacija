package com.znaci.goran.moviesaplikacija.activities;

import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;
import com.znaci.goran.moviesaplikacija.R;
import com.znaci.goran.moviesaplikacija.api.RestApi;
import com.znaci.goran.moviesaplikacija.helpers.ApiCalls;
import com.znaci.goran.moviesaplikacija.helpers.MyReceiver;
import com.znaci.goran.moviesaplikacija.models.MovieModel;
import com.znaci.goran.moviesaplikacija.preferencesManager.LogInPreferences;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SplashScreenActivity extends AppCompatActivity {

    @BindView(R.id.moviesicon)ImageView icon;
    String session = "";
    String token = "";
    MovieModel movieModel;
    ApiCalls apiCalls;
    MyReceiver receiver;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
        ButterKnife.bind(this);
        final ApiCalls calls = new ApiCalls(this);
        Picasso.with(this).load(R.drawable.movieicon).centerInside().fit().into(icon);
        receiver = new MyReceiver();
        registerReceiver(receiver, new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION));
        String network = receiver.isNetworkAvailable(this);
        if (network.equals("You are not connected")){
            Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                 finish();
                }
            },10000);
        }else {
            session = LogInPreferences.getSessionID(SplashScreenActivity.this);
            token = LogInPreferences.getUserID(this);
            calls.GetListFavorites();
            calls.getWatchList();
            calls.GetListShowsFavorites();
            calls.getWatchListShows();
            calls.RatedMovies();
            calls.RatedShows();
            new Handler().postDelayed(new Runnable(){
                @Override
                public void run() {
                    if (!token.isEmpty()){

                        Intent intent = new Intent(SplashScreenActivity.this,ExploreActivity.class);
                        startActivity(intent);
                        finish();

                    }else {

                        Intent intent = new Intent(SplashScreenActivity.this,LoginActivity.class);
                        startActivity(intent);
                        finish();

                    }}}, 2000);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();

        unregisterReceiver(receiver);
    }
}
