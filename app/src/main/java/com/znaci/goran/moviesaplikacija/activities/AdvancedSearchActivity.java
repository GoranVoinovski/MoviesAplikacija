package com.znaci.goran.moviesaplikacija.activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;
import com.znaci.goran.moviesaplikacija.R;
import com.znaci.goran.moviesaplikacija.adapters.RecyclerViewGenretAdapter;
import com.znaci.goran.moviesaplikacija.adapters.RecyclerViewPopularAdapter;
import com.znaci.goran.moviesaplikacija.adapters.RecyclerViewYeartAdapter;
import com.znaci.goran.moviesaplikacija.api.RestApi;
import com.znaci.goran.moviesaplikacija.listeners.OnRowGenreClickListener;
import com.znaci.goran.moviesaplikacija.listeners.OnRowMovieClickListener;
import com.znaci.goran.moviesaplikacija.listeners.OnRowYearClickListener;
import com.znaci.goran.moviesaplikacija.models.Genre;
import com.znaci.goran.moviesaplikacija.models.GenresModel;
import com.znaci.goran.moviesaplikacija.models.Movie;
import com.znaci.goran.moviesaplikacija.models.MovieModel;

import java.util.ArrayList;
import java.util.Collections;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AdvancedSearchActivity extends AppCompatActivity {
    @BindView(R.id.RvExplore)RecyclerView rv;
    @BindView(R.id.RvExploreGenre)RecyclerView rvGenre;
    @BindView(R.id.RvExploreYear)RecyclerView rvYear;
    @BindView(R.id.closeSearch)Button closeSearch;
    @BindView(R.id.llSearch)LinearLayout llsearch;
    @BindView(R.id.textsearch)TextView txtSearch;
    RecyclerViewGenretAdapter adapterGenre;
    RecyclerViewPopularAdapter adapter;
    RecyclerViewYeartAdapter adapterYear;
    GenresModel modelGenre = new GenresModel();
    MovieModel model2 = new MovieModel();
    MovieModel model3 = new MovieModel();
    int godina;
    String genres = "";
    int genreID;
    boolean hidden = false;
    int c = 1;
    ProgressDialog pd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_advanced_search);
        ButterKnife.bind(this);
        pd = new ProgressDialog(AdvancedSearchActivity.this);
        pd.setMessage("loading");

        closeSearch.setBackgroundResource(R.mipmap.nothidden);
        ArrayList<Integer> years = new ArrayList<>();
        for (int i = 1900; i < 2019; i++) {
            years.add(i);
        }

        adapterYear = new RecyclerViewYeartAdapter(AdvancedSearchActivity.this, new OnRowYearClickListener() {
            @Override
            public void onRowClick(Integer integer, int position) {
                txtSearch.setText(integer + "");
                godina = integer;
                model3.results = new ArrayList<>();
                model2.results = new ArrayList<>();
                pd.show();
                RestApi api3 = new RestApi(AdvancedSearchActivity.this);
                Call<MovieModel> call3 = api3.getMoviesByYear(godina,c);
                call3.enqueue(new Callback<MovieModel>() {
                    @Override
                    public void onResponse(Call<MovieModel> call, Response<MovieModel> response) {
                        if (response.code() == 200) {
                            model3 = response.body();
                            adapter = new RecyclerViewPopularAdapter(AdvancedSearchActivity.this, new OnRowMovieClickListener() {
                                @Override
                                public void onRowClick(Movie movie, int position) {
                                    Intent intent = new Intent(AdvancedSearchActivity.this, ScrollingMovieDetailActivity.class);
                                    intent.putExtra("id", movie.id);
                                    intent.putExtra("position", position);
                                    startActivityForResult(intent, 1111);
                                }
                            });
                            adapter.setItems(model3.results);
                            rv.setHasFixedSize(true);
                            rv.setLayoutManager(new GridLayoutManager(AdvancedSearchActivity.this, 2));
                            rv.setAdapter(adapter);
                            pd.dismiss();

                            rv.addOnScrollListener(new RecyclerView.OnScrollListener() {
                                @Override
                                public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                                    super.onScrollStateChanged(recyclerView, newState);
                                    if (!recyclerView.canScrollVertically(1)) {
                                        c++;
                                        pd.show();
                                        RestApi api = new RestApi(AdvancedSearchActivity.this);
                                        Call<MovieModel> call = api.getMoviesByYear(godina,c);
                                        call.enqueue(new Callback<MovieModel>() {
                                            @Override
                                            public void onResponse(Call<MovieModel> call, Response<MovieModel> response) {
                                                if (response.code() == 200) {

                                                    model2 = response.body();
                                                    model3.results.addAll(model2.results);
                                                    adapter.notifyDataSetChanged();
                                                    pd.dismiss();
                                                }}
                                            @Override
                                            public void onFailure(Call<MovieModel> call, Throwable t) {
                                            }});
                                    }}});
                        }
                    }

                    @Override
                    public void onFailure(Call<MovieModel> call, Throwable t) {

                    }
                });


            }
        });
            Collections.reverse(years);
            adapterYear.setItems(years);
            rvYear.setHasFixedSize(true);
            rvYear.setLayoutManager(new LinearLayoutManager(AdvancedSearchActivity.this,LinearLayoutManager.HORIZONTAL,false));
            rvYear.setAdapter(adapterYear);




        RestApi api = new RestApi(AdvancedSearchActivity.this);
        Call<GenresModel> call = api.getGenres();
        call.enqueue(new Callback<GenresModel>() {
            @Override
            public void onResponse(Call<GenresModel> call, Response<GenresModel> response) {
                if (response.code() == 200) {

                    modelGenre = response.body();
                    adapterGenre = new RecyclerViewGenretAdapter(AdvancedSearchActivity.this, new OnRowGenreClickListener() {
                        @Override
                        public void onRowClick(final Genre genre, int position) {
                            txtSearch.setText(genre.name);
                            genreID = genre.id;
                            model3.results = new ArrayList<>();
                            model2.results = new ArrayList<>();
                            pd.show();
                            genres = String.valueOf(genreID);
                            RestApi api = new RestApi(AdvancedSearchActivity.this);
                            Call<MovieModel> call = api.getMoviesByGenre(genres,c);
                            call.enqueue(new Callback<MovieModel>() {
                                @Override
                                public void onResponse(Call<MovieModel> call, Response<MovieModel> response) {
                                    if (response.code() == 200) {

                                        model3 = response.body();
                                        adapter = new RecyclerViewPopularAdapter(AdvancedSearchActivity.this, new OnRowMovieClickListener() {
                                            @Override
                                            public void onRowClick(Movie movie, int position) {
                                                Intent intent = new Intent(AdvancedSearchActivity.this, ScrollingMovieDetailActivity.class);
                                                intent.putExtra("id",movie.id);
                                                intent.putExtra("position",position);
                                                startActivityForResult(intent,1111);
                                            }
                                        });
                                        adapter.setItems(model3.results);
                                        rv.setHasFixedSize(true);
                                        rv.setLayoutManager(new GridLayoutManager(AdvancedSearchActivity.this,2));
                                        rv.setAdapter(adapter);
                                        pd.dismiss();
                                    }
                                }

                                @Override
                                public void onFailure(Call<MovieModel> call, Throwable t) {

                                }
                            });

                            rv.addOnScrollListener(new RecyclerView.OnScrollListener() {
                                @Override
                                public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                                    super.onScrollStateChanged(recyclerView, newState);
                                    if (!recyclerView.canScrollVertically(1)) {
                                        c++;
                                        pd.show();
                                        RestApi api = new RestApi(AdvancedSearchActivity.this);
                                        Call<MovieModel> call = api.getMoviesByGenre(genres,c);
                                        call.enqueue(new Callback<MovieModel>() {
                                            @Override
                                            public void onResponse(Call<MovieModel> call, Response<MovieModel> response) {
                                                if (response.code() == 200) {

                                                    model2 = response.body();
                                                    model3.results.addAll(model2.results);
                                                    adapter.notifyDataSetChanged();
                                                    pd.dismiss();
                                                }}
                                            @Override
                                            public void onFailure(Call<MovieModel> call, Throwable t) {
                                            }});
                                    }}});

                        }
                    });
                    adapterGenre.setItems(modelGenre.genres);
                    rvGenre.setHasFixedSize(true);
                    rvGenre.setLayoutManager(new LinearLayoutManager(AdvancedSearchActivity.this,LinearLayoutManager.HORIZONTAL,false));
                    rvGenre.setAdapter(adapterGenre);
                }
            }

            @Override
            public void onFailure(Call<GenresModel> call, Throwable t) {

            }
        });


        Intent genreIntent = AdvancedSearchActivity.this.getIntent();
        if (genreIntent.hasExtra("GID")){
            int genreid = genreIntent.getIntExtra("GID",0);
            String name = genreIntent.getStringExtra("name");
            txtSearch.setText(name);
            genres = String.valueOf(genreid);
            pd.show();
            model3.results = new ArrayList<>();
            model2.results = new ArrayList<>();
            RestApi api2 = new RestApi(AdvancedSearchActivity.this);
            Call<MovieModel> call2 = api2.getMoviesByGenre(genres,c);
            call2.enqueue(new Callback<MovieModel>() {
                @Override
                public void onResponse(Call<MovieModel> call, Response<MovieModel> response) {
                    if (response.code() == 200) {

                        model3 = response.body();
                        adapter = new RecyclerViewPopularAdapter(AdvancedSearchActivity.this, new OnRowMovieClickListener() {
                            @Override
                            public void onRowClick(Movie movie, int position) {
                                Intent intent = new Intent(AdvancedSearchActivity.this, ScrollingMovieDetailActivity.class);
                                intent.putExtra("id",movie.id);
                                intent.putExtra("position",position);
                                startActivityForResult(intent,1111);
                            }
                        });
                        adapter.setItems(model3.results);
                        rv.setHasFixedSize(true);
                        rv.setLayoutManager(new GridLayoutManager(AdvancedSearchActivity.this,2));
                        rv.setAdapter(adapter);
                        pd.dismiss();
                    }
                }

                @Override
                public void onFailure(Call<MovieModel> call, Throwable t) {

                }
            });

            rv.addOnScrollListener(new RecyclerView.OnScrollListener() {
                @Override
                public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                    super.onScrollStateChanged(recyclerView, newState);
                    if (!recyclerView.canScrollVertically(1)) {
                        pd.show();
                        c++;
                        RestApi api = new RestApi(AdvancedSearchActivity.this);
                        Call<MovieModel> call = api.getMoviesByGenre(genres,c);
                        call.enqueue(new Callback<MovieModel>() {
                            @Override
                            public void onResponse(Call<MovieModel> call, Response<MovieModel> response) {
                                if (response.code() == 200) {

                                    model2 = response.body();
                                    model3.results.addAll(model2.results);
                                    adapter.notifyDataSetChanged();
                                    pd.dismiss();
                                }}
                            @Override
                            public void onFailure(Call<MovieModel> call, Throwable t) {
                            }});
                    }}});
        }
    }

    @OnClick(R.id.closeSearch)
    public void HideSearch(){
    if (hidden){
     llsearch.setVisibility(View.VISIBLE);
     hidden = false;
     closeSearch.setBackgroundResource(R.mipmap.nothidden);
    }else {
      llsearch.setVisibility(View.GONE);
      hidden = true;
      closeSearch.setBackgroundResource(R.mipmap.hidden);
    }

    }
}
