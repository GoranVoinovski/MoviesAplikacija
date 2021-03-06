package com.znaci.goran.moviesaplikacija.activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.TextView;
import android.widget.Toast;

import com.znaci.goran.moviesaplikacija.R;
import com.znaci.goran.moviesaplikacija.adapters.RecyclerViewPopularAdapter;
import com.znaci.goran.moviesaplikacija.adapters.RecyclerViewShowsAdapter;
import com.znaci.goran.moviesaplikacija.api.RestApi;
import com.znaci.goran.moviesaplikacija.helpers.ApiCalls;
import com.znaci.goran.moviesaplikacija.listeners.OnRowShowClickListener;
import com.znaci.goran.moviesaplikacija.models.Shows;
import com.znaci.goran.moviesaplikacija.models.ShowsModel;
import com.znaci.goran.moviesaplikacija.preferencesManager.LogInPreferences;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class WatchlistShowsActivity extends AppCompatActivity {
    @BindView(R.id.Favorites)RecyclerView favoriteMovies;
    RecyclerViewShowsAdapter adapter;
    ShowsModel showModel;
    ProgressDialog pd;
    ApiCalls apiCalls;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_watchlist_shows);
        ButterKnife.bind(this);
        apiCalls = new ApiCalls(this);
        pd = new ProgressDialog(WatchlistShowsActivity.this);
        pd.setMessage("loading");
        Intent intent = getIntent();
        if (intent.hasExtra("id")){
            pd.show();
            String session_id = LogInPreferences.getSessionID(this);
            RestApi api = new RestApi(WatchlistShowsActivity.this);
            Call<ShowsModel> call = api.getUserShowsWatchlist("account_id",session_id);
            call.enqueue(new Callback<ShowsModel>() {
                @Override
                public void onResponse(Call<ShowsModel> call, Response<ShowsModel> response) {
                    if (response.code() == 200) {

                        showModel = response.body();
                        adapter = new RecyclerViewShowsAdapter(WatchlistShowsActivity.this, new OnRowShowClickListener() {
                            @Override
                            public void onRowClick(Shows shows, int position) {
                                Intent intent = new Intent(WatchlistShowsActivity.this, ScrollingShowsDetailActivity.class);
                                intent.putExtra("showid",shows.id);
                                intent.putExtra("position",position);
                                startActivityForResult(intent,1111);
                            }

                            @Override
                            public void onRowFavClick(Shows movie, int position, TextView tv) {
                                apiCalls.FavoriteShowsListener(movie.id,tv);
                            }

                            @Override
                            public void onRowWatchClick(Shows movie, int position, TextView tv) {
                                apiCalls.WatchlistShowsListener(movie.id,tv);
                            }
                        });
                        adapter.setItems(showModel.results);
                        favoriteMovies.setHasFixedSize(true);
                        favoriteMovies.setLayoutManager(new GridLayoutManager(WatchlistShowsActivity.this,2));
                        favoriteMovies.setAdapter(adapter);
                        pd.dismiss();
                    }
                }

                @Override
                public void onFailure(Call<ShowsModel> call, Throwable t) {
                    if(t.getMessage().contains("Unable to resolve host"));
                    Toast.makeText(WatchlistShowsActivity.this, "No internet connection", Toast.LENGTH_SHORT).show();
                    finish();
                }
            });



        }


    }
}