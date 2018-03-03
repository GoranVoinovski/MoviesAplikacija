package com.znaci.goran.moviesaplikacija.helpers;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.GridLayoutManager;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;


import com.squareup.picasso.Picasso;
import com.znaci.goran.moviesaplikacija.activities.FavoriteShowsActivity;
import com.znaci.goran.moviesaplikacija.activities.FavoritesActivity;
import com.znaci.goran.moviesaplikacija.activities.RatedActivity;
import com.znaci.goran.moviesaplikacija.activities.ScrollingMovieDetailActivity;
import com.znaci.goran.moviesaplikacija.activities.ScrollingShowsDetailActivity;
import com.znaci.goran.moviesaplikacija.activities.WatchlistActivity;
import com.znaci.goran.moviesaplikacija.activities.WatchlistShowsActivity;
import com.znaci.goran.moviesaplikacija.adapters.RecyclerViewPopularAdapter;
import com.znaci.goran.moviesaplikacija.adapters.RecyclerViewShowsAdapter;
import com.znaci.goran.moviesaplikacija.api.RestApi;
import com.znaci.goran.moviesaplikacija.listeners.OnRowMovieClickListener;
import com.znaci.goran.moviesaplikacija.listeners.OnRowShowClickListener;
import com.znaci.goran.moviesaplikacija.models.FavoriteModel;
import com.znaci.goran.moviesaplikacija.models.Movie;
import com.znaci.goran.moviesaplikacija.models.MovieModel;
import com.znaci.goran.moviesaplikacija.models.Shows;
import com.znaci.goran.moviesaplikacija.models.ShowsModel;
import com.znaci.goran.moviesaplikacija.models.User;
import com.znaci.goran.moviesaplikacija.models.VideoModel;
import com.znaci.goran.moviesaplikacija.models.Videos;
import com.znaci.goran.moviesaplikacija.models.WatchModel;
import com.znaci.goran.moviesaplikacija.preferencesManager.LogInPreferences;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


/**
 * Created by Goran on 2/19/2018.
 */

public class ApiCalls {

    Context context;
    MovieModel movieModel;
    ShowsModel showModel;
    FavoriteModel favoriteModel;
    WatchModel watchModel;

    public ApiCalls(Context context) {
        this.context = context;
    }


    public void GetUserDetails(final TextView txt1, final TextView txt2, final ImageView img, String session) {

        RestApi api = new RestApi(context);
        Call<User> call2 = api.getUserDetail(session);
        call2.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                if (response.code() == 200) {
                    User user;
                    user = response.body();
                    txt1.setText(user.name);
                    txt2.setText(user.username);
                    String avatar = "http://www.gravatar.com/avatar/" + user.avatar.gravatar.hash;
                    Picasso.with(context).load(avatar).into(img);
                }
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
            }});}


    public void GetVideo(int id, final FloatingActionButton floating) {

        RestApi api3 = new RestApi(context);
        Call<VideoModel> call3 = api3.getVideo(id);
        call3.enqueue(new Callback<VideoModel>() {
            @Override
            public void onResponse(Call<VideoModel> call, Response<VideoModel> response) {
                if (response.code() == 200) {
                    VideoModel model3;
                    model3 = response.body();
                    if (model3.results.size() == 0) {
                    } else {
                        final Videos video = model3.results.get(0);
                        floating.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                context.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.youtube.com/watch?v=" + video.key)));
                                Log.i("Video", "Video Playing....");
                            }});}}}
            @Override
            public void onFailure(Call<VideoModel> call, Throwable t) {
            }});
    }

    public void RatedMovies(){

        String session_id = LogInPreferences.getSessionID(context);
        RestApi api = new RestApi(context);
        Call<MovieModel> call = api.getUserRated("account_id", session_id);
        call.enqueue(new Callback<MovieModel>() {
            @Override
            public void onResponse(Call<MovieModel> call, Response<MovieModel> response) {
                if (response.code() == 200) {
                    MovieModel movieModel;
                    movieModel = response.body();

                }}
            @Override
            public void onFailure(Call<MovieModel> call, Throwable t) {
            }});
    }

    public void GetListFavorites(){

        String session_id = LogInPreferences.getSessionID(context);
        RestApi api = new RestApi(context);
        Call<MovieModel> call = api.getUserFavorites("account_id", session_id);
        call.enqueue(new Callback<MovieModel>() {
            @Override
            public void onResponse(Call<MovieModel> call, Response<MovieModel> response) {
                if (response.code() == 200) {
                    movieModel = response.body();
                    favoriteModel = new FavoriteModel();
                    for (Movie movie:movieModel.results){
                        favoriteModel.favorites.add(movie.id);
                       LogInPreferences.addFavoriteList(favoriteModel,context);
                    }
                }}
            @Override
            public void onFailure(Call<MovieModel> call, Throwable t) {
            }});


    }

    public void getWatchList() {

        String session_id = LogInPreferences.getSessionID(context);
        RestApi api = new RestApi(context);
        Call<MovieModel> call = api.getUserWatchlist("account_id", session_id);
        call.enqueue(new Callback<MovieModel>() {
            @Override
            public void onResponse(Call<MovieModel> call, Response<MovieModel> response) {
                if (response.code() == 200) {
                    watchModel = new WatchModel();
                    movieModel = response.body();
                    for (Movie movie : movieModel.results) {
                        watchModel.favorites.add(movie.id);
                        LogInPreferences.addWtchList(watchModel, context);
                    }
                }
            }

            @Override
            public void onFailure(Call<MovieModel> call, Throwable t) {

            }
        });
    }

    public void GetListShowsFavorites(){

        String session_id = LogInPreferences.getSessionID(context);
        RestApi api = new RestApi(context);
        Call<ShowsModel> call = api.getUserShowsFavorites("account_id",session_id);
        call.enqueue(new Callback<ShowsModel>() {
            @Override
            public void onResponse(Call<ShowsModel> call, Response<ShowsModel> response) {
                if (response.code() == 200) {
                    FavoriteModel favoriteModel2 = new FavoriteModel();
                    showModel = response.body();
                    for (Shows shows : showModel.results) {
                        favoriteModel2.favorites.add(shows.id);
                        LogInPreferences.addFavoriteListShows(favoriteModel2, context);
                    }
                  }
            }
            @Override
            public void onFailure(Call<ShowsModel> call, Throwable t) {
            }});

    }

    public void getWatchListShows(){

        String session_id = LogInPreferences.getSessionID(context);
        RestApi api = new RestApi(context);
        Call<ShowsModel> call = api.getUserShowsWatchlist("account_id",session_id);
        call.enqueue(new Callback<ShowsModel>() {
            @Override
            public void onResponse(Call<ShowsModel> call, Response<ShowsModel> response) {
                if (response.code() == 200) {
                    WatchModel watchModel2 = new WatchModel();
                    showModel = response.body();
                    for (Shows shows : showModel.results) {
                        watchModel2.favorites.add(shows.id);
                        LogInPreferences.addWtchListShows(watchModel2, context);
                    }

                }
            }

            @Override
            public void onFailure(Call<ShowsModel> call, Throwable t) {

            }
        });
    }

}
