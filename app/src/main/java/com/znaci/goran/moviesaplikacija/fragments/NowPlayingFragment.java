package com.znaci.goran.moviesaplikacija.fragments;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.znaci.goran.moviesaplikacija.R;
import com.znaci.goran.moviesaplikacija.activities.AdvancedSearchActivity;
import com.znaci.goran.moviesaplikacija.activities.ScrollingMovieDetailActivity;
import com.znaci.goran.moviesaplikacija.adapters.RecyclerViewPopularAdapter;
import com.znaci.goran.moviesaplikacija.api.RestApi;
import com.znaci.goran.moviesaplikacija.helpers.ApiCalls;
import com.znaci.goran.moviesaplikacija.listeners.OnRowMovieClickListener;
import com.znaci.goran.moviesaplikacija.models.Movie;
import com.znaci.goran.moviesaplikacija.models.MovieModel;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Goran on movie2/6/2018.
 */

public class NowPlayingFragment extends Fragment{
    @BindView(R.id.RvExplore)RecyclerView rv;
    @BindView(R.id.swipeRefresh)SwipeRefreshLayout swipe;
    RecyclerViewPopularAdapter adapter;
    private Unbinder mUnbind;
    MovieModel model = new MovieModel();
    MovieModel model2 = new MovieModel();
    int c = 1;
    ProgressDialog pd;
    ApiCalls apiCalls;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.exploreactivity_fragments_layout, null);
        mUnbind = ButterKnife.bind(this, view);
        apiCalls = new ApiCalls(getActivity());
        pd = new ProgressDialog(getActivity());
        pd.setMessage("loading");
        PopularMoviesNowPlaying();

        rv.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (!recyclerView.canScrollVertically(1)) {
                            pd.show();
                            c++;
                            RestApi api = new RestApi(getActivity());
                            Call<MovieModel> call = api.getMovies("now_playing",c);
                            call.enqueue(new Callback<MovieModel>() {
                                @Override
                                public void onResponse(Call<MovieModel> call, Response<MovieModel> response) {
                                    if (response.code() == 200) {

                                        model2 = response.body();
                                        model.results.addAll(model2.results);
                                        adapter.notifyDataSetChanged();
                                        pd.dismiss();
                                    }
                                }

                                @Override
                                public void onFailure(Call<MovieModel> call, Throwable t) {

                                }
                            });}


            }
        });

        swipe.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                PopularMoviesNowPlaying();
                swipe.setRefreshing(false);
            }
        });

        return view;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mUnbind.unbind();
    }

    public void PopularMoviesNowPlaying(){
        c = 1;
        pd.show();
        RestApi api = new RestApi(getActivity());
        Call<MovieModel> call = api.getMovies("now_playing",c);
        call.enqueue(new Callback<MovieModel>() {
            @Override
            public void onResponse(Call<MovieModel> call, Response<MovieModel> response) {
                if (response.code() == 200) {

                    model = response.body();
                            adapter = new RecyclerViewPopularAdapter(getActivity(), new OnRowMovieClickListener() {
                        @Override
                        public void onRowClick(Movie movie, int position) {
                            Intent intent = new Intent(getActivity(), ScrollingMovieDetailActivity.class);
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
                    adapter.setItems(model.results);
                    rv.setHasFixedSize(true);
                    rv.setLayoutManager(new GridLayoutManager(getActivity(),2));
                    rv.setAdapter(adapter);
                    pd.dismiss();
                }
            }

            @Override
            public void onFailure(Call<MovieModel> call, Throwable t) {

            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == getActivity().RESULT_OK && requestCode == 1111){
            adapter.notifyDataSetChanged();

        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (adapter != null) {
            adapter.notifyDataSetChanged();
        }
    }
}
