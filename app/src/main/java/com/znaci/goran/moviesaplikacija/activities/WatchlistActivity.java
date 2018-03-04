package com.znaci.goran.moviesaplikacija.activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.TextView;

import com.znaci.goran.moviesaplikacija.R;
import com.znaci.goran.moviesaplikacija.adapters.RecyclerViewPopularAdapter;
import com.znaci.goran.moviesaplikacija.api.RestApi;
import com.znaci.goran.moviesaplikacija.helpers.ApiCalls;
import com.znaci.goran.moviesaplikacija.listeners.OnRowMovieClickListener;
import com.znaci.goran.moviesaplikacija.models.Movie;
import com.znaci.goran.moviesaplikacija.models.MovieModel;
import com.znaci.goran.moviesaplikacija.preferencesManager.LogInPreferences;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class WatchlistActivity extends AppCompatActivity {

    @BindView(R.id.Favorites)RecyclerView favoriteMovies;
    RecyclerViewPopularAdapter adapter;
    MovieModel movieModel;
    ProgressDialog pd;
    ApiCalls apiCalls = new ApiCalls(this);
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_watchlist);
        ButterKnife.bind(this);
        pd = new ProgressDialog(WatchlistActivity.this);
        pd.setMessage("loading");
        Intent intent = getIntent();
        if (intent.hasExtra("id")){
            pd.show();
            String session_id = LogInPreferences.getSessionID(this);
            RestApi api = new RestApi(WatchlistActivity.this);
            Call<MovieModel> call = api.getUserWatchlist("account_id",session_id);
            call.enqueue(new Callback<MovieModel>() {
                @Override
                public void onResponse(Call<MovieModel> call, Response<MovieModel> response) {
                    if (response.code() == 200) {

                        movieModel = response.body();
                        adapter = new RecyclerViewPopularAdapter(WatchlistActivity.this, new OnRowMovieClickListener() {
                            @Override
                            public void onRowClick(Movie movie, int position) {
                                Intent intent = new Intent(WatchlistActivity.this, ScrollingMovieDetailActivity.class);
                                intent.putExtra("id",movie.id);
                                intent.putExtra("position",position);
                                startActivityForResult(intent,1111);
                            }

                            @Override
                            public void onRowFavClick(Movie movie, int position, TextView tv) {
                                apiCalls.FavoriteListener(movie.id,tv);
                            }

                            @Override
                            public void onRowWatchClick(Movie movie, int position, TextView tv) {
                                apiCalls.WatchlistListener(movie.id,tv);
                            }
                        });
                        adapter.setItems(movieModel.results);
                        favoriteMovies.setHasFixedSize(true);
                        favoriteMovies.setLayoutManager(new GridLayoutManager(WatchlistActivity.this,2));
                        favoriteMovies.setAdapter(adapter);
                        pd.dismiss();
                    }
                }

                @Override
                public void onFailure(Call<MovieModel> call, Throwable t) {

                }
            });



        }


    }
}
