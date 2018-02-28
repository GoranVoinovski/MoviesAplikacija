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
import com.znaci.goran.moviesaplikacija.activities.FavoritesActivity;
import com.znaci.goran.moviesaplikacija.activities.RatedActivity;
import com.znaci.goran.moviesaplikacija.activities.ScrollingMovieDetailActivity;
import com.znaci.goran.moviesaplikacija.adapters.RecyclerViewPopularAdapter;
import com.znaci.goran.moviesaplikacija.api.RestApi;
import com.znaci.goran.moviesaplikacija.listeners.OnRowMovieClickListener;
import com.znaci.goran.moviesaplikacija.models.Movie;
import com.znaci.goran.moviesaplikacija.models.MovieModel;
import com.znaci.goran.moviesaplikacija.models.User;
import com.znaci.goran.moviesaplikacija.models.VideoModel;
import com.znaci.goran.moviesaplikacija.models.Videos;
import com.znaci.goran.moviesaplikacija.preferencesManager.LogInPreferences;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


/**
 * Created by Goran on 2/19/2018.
 */

public class ApiCalls {

    Context context;

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

}
