package com.znaci.goran.moviesaplikacija.activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.znaci.goran.moviesaplikacija.R;
import com.znaci.goran.moviesaplikacija.adapters.RecyclerViewPopularAdapter;
import com.znaci.goran.moviesaplikacija.adapters.RecyclerViewShowsAdapter;
import com.znaci.goran.moviesaplikacija.api.RestApi;
import com.znaci.goran.moviesaplikacija.listeners.OnRowMovieClickListener;
import com.znaci.goran.moviesaplikacija.listeners.OnRowShowClickListener;
import com.znaci.goran.moviesaplikacija.models.Movie;
import com.znaci.goran.moviesaplikacija.models.MovieModel;
import com.znaci.goran.moviesaplikacija.models.Shows;
import com.znaci.goran.moviesaplikacija.models.ShowsModel;
import com.znaci.goran.moviesaplikacija.preferencesManager.LogInPreferences;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FavoriteShowsActivity extends AppCompatActivity {
    @BindView(R.id.Favorites)RecyclerView favoriteMovies;
    RecyclerViewShowsAdapter adapter;
    ShowsModel showModel;
    ProgressDialog pd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favorite_shows);
        ButterKnife.bind(this);
        pd = new ProgressDialog(FavoriteShowsActivity.this);
        pd.setMessage("loading");

        Intent intent = getIntent();
        if (intent.hasExtra("id")){
             pd.show();
            String session_id = LogInPreferences.getSessionID(this);
            RestApi api = new RestApi(FavoriteShowsActivity.this);
            Call<ShowsModel> call = api.getUserShowsFavorites("account_id",session_id);
            call.enqueue(new Callback<ShowsModel>() {
                @Override
                public void onResponse(Call<ShowsModel> call, Response<ShowsModel> response) {
                    if (response.code() == 200) {
                        showModel = response.body();
                        adapter = new RecyclerViewShowsAdapter(FavoriteShowsActivity.this, new OnRowShowClickListener() {
                            @Override
                            public void onRowClick(Shows shows, int position) {
                                Intent intent = new Intent(FavoriteShowsActivity.this, ScrollingShowsDetailActivity.class);
                                intent.putExtra("showid",shows.id);
                                intent.putExtra("position",position);
                                startActivityForResult(intent,1111);}
                        });
                        adapter.setItems(showModel.results);
                        favoriteMovies.setHasFixedSize(true);
                        favoriteMovies.setLayoutManager(new GridLayoutManager(FavoriteShowsActivity.this,2));
                        favoriteMovies.setAdapter(adapter);
                        pd.dismiss();}}
                @Override
                public void onFailure(Call<ShowsModel> call, Throwable t) {
                }});
        }}
}
