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

import com.znaci.goran.moviesaplikacija.R;
import com.znaci.goran.moviesaplikacija.adapters.RecyclerViewGenretAdapter;
import com.znaci.goran.moviesaplikacija.adapters.RecyclerViewPopularAdapter;
import com.znaci.goran.moviesaplikacija.adapters.RecyclerViewPopularShowsAdapter;
import com.znaci.goran.moviesaplikacija.adapters.RecyclerViewYeartAdapter;
import com.znaci.goran.moviesaplikacija.api.RestApi;
import com.znaci.goran.moviesaplikacija.listeners.OnRowGenreClickListener;
import com.znaci.goran.moviesaplikacija.listeners.OnRowMovieClickListener;
import com.znaci.goran.moviesaplikacija.listeners.OnRowShowClickListener;
import com.znaci.goran.moviesaplikacija.listeners.OnRowYearClickListener;
import com.znaci.goran.moviesaplikacija.models.Genre;
import com.znaci.goran.moviesaplikacija.models.GenresModel;
import com.znaci.goran.moviesaplikacija.models.Movie;
import com.znaci.goran.moviesaplikacija.models.MovieModel;
import com.znaci.goran.moviesaplikacija.models.Shows;
import com.znaci.goran.moviesaplikacija.models.ShowsModel;

import java.util.ArrayList;
import java.util.Collections;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AdvancedSearchShowsActivity extends AppCompatActivity {

    @BindView(R.id.RvExplore)
    RecyclerView rv;
    @BindView(R.id.RvExploreGenre)
    RecyclerView rvGenre;
    @BindView(R.id.RvExploreYear)
    RecyclerView rvYear;
    @BindView(R.id.closeSearch)
    Button closeSearch;
    @BindView(R.id.llSearch)
    LinearLayout llsearch;
    @BindView(R.id.textsearch)
    TextView txtSearch;
    RecyclerViewGenretAdapter adapterGenre;
    RecyclerViewPopularShowsAdapter adapter;
    RecyclerViewYeartAdapter adapterYear;
    GenresModel modelGenre = new GenresModel();
    ShowsModel model2 = new ShowsModel();
    ShowsModel model3 = new ShowsModel();
    boolean hidden = false;
    int c = 1;
    ProgressDialog pd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_advanced_search_shows);
        ButterKnife.bind(this);
        pd = new ProgressDialog(AdvancedSearchShowsActivity.this);
        pd.setMessage("loading");
        closeSearch.setBackgroundResource(R.mipmap.nothidden);
        ArrayList<Integer> years = new ArrayList<>();
        for (int i = 1900; i < 2019; i++) {
            years.add(i);
        }

        adapterYear = new RecyclerViewYeartAdapter(AdvancedSearchShowsActivity.this, new OnRowYearClickListener() {
            @Override
            public void onRowClick(final Integer integer, int position) {
                txtSearch.setText(integer + "");
                pd.show();
                RestApi api3 = new RestApi(AdvancedSearchShowsActivity.this);
                Call<ShowsModel> call3 = api3.getTVByYear(integer, c);
                call3.enqueue(new Callback<ShowsModel>() {
                    @Override
                    public void onResponse(Call<ShowsModel> call, Response<ShowsModel> response) {
                        if (response.code() == 200) {

                            model3 = response.body();
                            adapter = new RecyclerViewPopularShowsAdapter(AdvancedSearchShowsActivity.this, new OnRowShowClickListener() {
                                @Override
                                public void onRowClick(Shows shows, int position) {
                                    Intent intent = new Intent(AdvancedSearchShowsActivity.this, ScrollingShowsDetailActivity.class);
                                    intent.putExtra("showid", shows.id);
                                    intent.putExtra("position", position);
                                    startActivityForResult(intent, 1111);
                                }
                            });
                            adapter.setItems(model3.results);
                            rv.setHasFixedSize(true);
                            rv.setLayoutManager(new GridLayoutManager(AdvancedSearchShowsActivity.this, 2));
                            rv.setAdapter(adapter);
                            pd.dismiss();
                        }
                    }

                    @Override
                    public void onFailure(Call<ShowsModel> call, Throwable t) {

                    }
                });
                rv.addOnScrollListener(new RecyclerView.OnScrollListener() {
                    @Override
                    public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                        super.onScrollStateChanged(recyclerView, newState);
                        if (!recyclerView.canScrollVertically(1)) {
                            pd.show();
                            c++;
                            RestApi api = new RestApi(AdvancedSearchShowsActivity.this);
                            Call<ShowsModel> call = api.getTVByYear(integer, c);
                            call.enqueue(new Callback<ShowsModel>() {
                                @Override
                                public void onResponse(Call<ShowsModel> call, Response<ShowsModel> response) {
                                    if (response.code() == 200) {

                                        model2 = response.body();
                                        model3.results.addAll(model2.results);
                                        adapter.notifyDataSetChanged();
                                        pd.dismiss();
                                    }
                                }

                                @Override
                                public void onFailure(Call<ShowsModel> call, Throwable t) {
                                }
                            });
                        }
                    }
                });
            }
        });
        Collections.reverse(years);
        adapterYear.setItems(years);
        rvYear.setHasFixedSize(true);
        rvYear.setLayoutManager(new LinearLayoutManager(AdvancedSearchShowsActivity.this, LinearLayoutManager.HORIZONTAL, false));
        rvYear.setAdapter(adapterYear);


        RestApi api = new RestApi(AdvancedSearchShowsActivity.this);
        Call<GenresModel> call = api.getTVGenres();
        call.enqueue(new Callback<GenresModel>() {
            @Override
            public void onResponse(Call<GenresModel> call, Response<GenresModel> response) {
                if (response.code() == 200) {

                    modelGenre = response.body();
                    adapterGenre = new RecyclerViewGenretAdapter(AdvancedSearchShowsActivity.this, new OnRowGenreClickListener() {
                        @Override
                        public void onRowClick(Genre genre, int position) {
                            txtSearch.setText(genre.name);
                            final String id = String.valueOf(genre.id);
                            pd.show();
                            RestApi api = new RestApi(AdvancedSearchShowsActivity.this);
                            Call<ShowsModel> call = api.getTVByGenre(id, c);
                            call.enqueue(new Callback<ShowsModel>() {
                                @Override
                                public void onResponse(Call<ShowsModel> call, Response<ShowsModel> response) {
                                    if (response.code() == 200) {

                                        model3 = response.body();
                                        adapter = new RecyclerViewPopularShowsAdapter(AdvancedSearchShowsActivity.this, new OnRowShowClickListener() {
                                            @Override
                                            public void onRowClick(Shows shows, int position) {
                                                Intent intent = new Intent(AdvancedSearchShowsActivity.this, ScrollingShowsDetailActivity.class);
                                                intent.putExtra("showid", shows.id);
                                                intent.putExtra("position", position);
                                                startActivityForResult(intent, 1111);
                                            }
                                        });
                                        adapter.setItems(model3.results);
                                        rv.setHasFixedSize(true);
                                        rv.setLayoutManager(new GridLayoutManager(AdvancedSearchShowsActivity.this, 2));
                                        rv.setAdapter(adapter);
                                        pd.dismiss();
                                    }
                                }

                                @Override
                                public void onFailure(Call<ShowsModel> call, Throwable t) {

                                }
                            });

                            rv.addOnScrollListener(new RecyclerView.OnScrollListener() {
                                @Override
                                public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                                    super.onScrollStateChanged(recyclerView, newState);
                                    if (!recyclerView.canScrollVertically(1)) {
                                        pd.show();
                                        c++;
                                        RestApi api = new RestApi(AdvancedSearchShowsActivity.this);
                                        Call<ShowsModel> call = api.getTVByGenre(id, c);
                                        call.enqueue(new Callback<ShowsModel>() {
                                            @Override
                                            public void onResponse(Call<ShowsModel> call, Response<ShowsModel> response) {
                                                if (response.code() == 200) {

                                                    model2 = response.body();
                                                    model3.results.addAll(model2.results);
                                                    adapter.notifyDataSetChanged();
                                                    pd.dismiss();
                                                }
                                            }

                                            @Override
                                            public void onFailure(Call<ShowsModel> call, Throwable t) {
                                            }
                                        });
                                    }
                                }
                            });

                        }
                    });
                    adapterGenre.setItems(modelGenre.genres);
                    rvGenre.setHasFixedSize(true);
                    rvGenre.setLayoutManager(new LinearLayoutManager(AdvancedSearchShowsActivity.this, LinearLayoutManager.HORIZONTAL, false));
                    rvGenre.setAdapter(adapterGenre);
                }
            }

            @Override
            public void onFailure(Call<GenresModel> call, Throwable t) {

            }
        });


        Intent genreIntent = AdvancedSearchShowsActivity.this.getIntent();
        if (genreIntent.hasExtra("GID")) {
            int genreid = genreIntent.getIntExtra("GID", 0);
            String name = genreIntent.getStringExtra("name");
            final String id = String.valueOf(genreid);
            txtSearch.setText(name);
            pd.show();
            RestApi api2 = new RestApi(AdvancedSearchShowsActivity.this);
            Call<ShowsModel> call2 = api2.getTVByGenre(id, c);
            call2.enqueue(new Callback<ShowsModel>() {
                @Override
                public void onResponse(Call<ShowsModel> call, Response<ShowsModel> response) {
                    if (response.code() == 200) {

                        model3 = response.body();
                        adapter = new RecyclerViewPopularShowsAdapter(AdvancedSearchShowsActivity.this, new OnRowShowClickListener() {
                            @Override
                            public void onRowClick(Shows shows, int position) {
                                Intent intent = new Intent(AdvancedSearchShowsActivity.this, ScrollingShowsDetailActivity.class);
                                intent.putExtra("showid", shows.id);
                                intent.putExtra("position", position);
                                startActivityForResult(intent, 1111);
                            }
                        });
                        adapter.setItems(model3.results);
                        rv.setHasFixedSize(true);
                        rv.setLayoutManager(new GridLayoutManager(AdvancedSearchShowsActivity.this, 2));
                        rv.setAdapter(adapter);
                        pd.dismiss();
                    }
                }

                @Override
                public void onFailure(Call<ShowsModel> call, Throwable t) {

                }
            });

            rv.addOnScrollListener(new RecyclerView.OnScrollListener() {
                @Override
                public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                    super.onScrollStateChanged(recyclerView, newState);
                    if (!recyclerView.canScrollVertically(1)) {
                        pd.show();
                        c++;
                        RestApi api = new RestApi(AdvancedSearchShowsActivity.this);
                        Call<ShowsModel> call = api.getTVByGenre(id, c);
                        call.enqueue(new Callback<ShowsModel>() {
                            @Override
                            public void onResponse(Call<ShowsModel> call, Response<ShowsModel> response) {
                                if (response.code() == 200) {

                                    model2 = response.body();
                                    model3.results.addAll(model2.results);
                                    adapter.notifyDataSetChanged();
                                    pd.dismiss();
                                }
                            }

                            @Override
                            public void onFailure(Call<ShowsModel> call, Throwable t) {
                            }
                        });
                    }
                }
            });
        }
    }

    @OnClick(R.id.closeSearch)
    public void HideSearch() {
        if (hidden) {
            llsearch.setVisibility(View.VISIBLE);
            hidden = false;
            closeSearch.setBackgroundResource(R.mipmap.nothidden);
        } else {
            llsearch.setVisibility(View.GONE);
            hidden = true;
            closeSearch.setBackgroundResource(R.mipmap.hidden);
        }

    }

}