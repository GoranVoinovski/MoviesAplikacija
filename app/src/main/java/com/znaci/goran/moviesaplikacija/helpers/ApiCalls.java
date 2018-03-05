package com.znaci.goran.moviesaplikacija.helpers;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


import com.squareup.picasso.Picasso;
import com.znaci.goran.moviesaplikacija.R;
import com.znaci.goran.moviesaplikacija.activities.ExploreActivity;
import com.znaci.goran.moviesaplikacija.activities.FavoriteShowsActivity;
import com.znaci.goran.moviesaplikacija.activities.FavoritesActivity;
import com.znaci.goran.moviesaplikacija.activities.RatedActivity;
import com.znaci.goran.moviesaplikacija.activities.ScrollingMovieDetailActivity;
import com.znaci.goran.moviesaplikacija.activities.ScrollingShowsDetailActivity;
import com.znaci.goran.moviesaplikacija.activities.SlikiFragmentActivity;
import com.znaci.goran.moviesaplikacija.activities.WatchlistActivity;
import com.znaci.goran.moviesaplikacija.activities.WatchlistShowsActivity;
import com.znaci.goran.moviesaplikacija.adapters.RecyclerViewImagesAdapter;
import com.znaci.goran.moviesaplikacija.adapters.RecyclerViewPopularAdapter;
import com.znaci.goran.moviesaplikacija.adapters.RecyclerViewShowsAdapter;
import com.znaci.goran.moviesaplikacija.api.RestApi;
import com.znaci.goran.moviesaplikacija.listeners.OnRowImageClickListener;
import com.znaci.goran.moviesaplikacija.listeners.OnRowMovieClickListener;
import com.znaci.goran.moviesaplikacija.listeners.OnRowShowClickListener;
import com.znaci.goran.moviesaplikacija.models.FavoriteModel;
import com.znaci.goran.moviesaplikacija.models.FavoriteMoviePost;
import com.znaci.goran.moviesaplikacija.models.ImageModel;
import com.znaci.goran.moviesaplikacija.models.Images;
import com.znaci.goran.moviesaplikacija.models.Movie;
import com.znaci.goran.moviesaplikacija.models.MovieModel;
import com.znaci.goran.moviesaplikacija.models.Rated;
import com.znaci.goran.moviesaplikacija.models.RatedList;
import com.znaci.goran.moviesaplikacija.models.Shows;
import com.znaci.goran.moviesaplikacija.models.ShowsModel;
import com.znaci.goran.moviesaplikacija.models.User;
import com.znaci.goran.moviesaplikacija.models.VideoModel;
import com.znaci.goran.moviesaplikacija.models.Videos;
import com.znaci.goran.moviesaplikacija.models.WatchModel;
import com.znaci.goran.moviesaplikacija.models.WatchlistMoviePost;
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
    RatedList ratedList;
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

    public void GetImages(int id, final RecyclerView rv) {
        final RecyclerViewImagesAdapter rvImg = new RecyclerViewImagesAdapter(context, new OnRowImageClickListener() {
            @Override
            public void onRowClick(Images image, int position) {
                Intent intent = new Intent(context, SlikiFragmentActivity.class);
                intent.putExtra("Position",position);
                context.startActivity(intent);
            }
        });

        RestApi api3 = new RestApi(context);
        Call<ImageModel> call3 = api3.getMovieImages(id);
        call3.enqueue(new Callback<ImageModel>() {
            @Override
            public void onResponse(Call<ImageModel> call, Response<ImageModel> response) {
                if (response.code() == 200) {
                    ImageModel model3;
                    model3 = response.body();
                    LogInPreferences.addImages(model3,context);
                    rvImg.setItems(model3.backdrops);
                    rv.setHasFixedSize(true);
                    rv.setLayoutManager(new LinearLayoutManager(context,LinearLayoutManager.HORIZONTAL,false));
                    rv.setAdapter(rvImg);
                }
            }
            @Override
            public void onFailure(Call<ImageModel> call, Throwable t) {
            }});
    }

    public void GetShowsImages(int id, final RecyclerView rv) {
        final RecyclerViewImagesAdapter rvImg = new RecyclerViewImagesAdapter(context, new OnRowImageClickListener() {
            @Override
            public void onRowClick(Images image, int position) {
                Intent intent = new Intent(context, SlikiFragmentActivity.class);
                intent.putExtra("Position",position);
                context.startActivity(intent);
            }
        });

        RestApi api3 = new RestApi(context);
        Call<ImageModel> call3 = api3.getShowsImages(id);
        call3.enqueue(new Callback<ImageModel>() {
            @Override
            public void onResponse(Call<ImageModel> call, Response<ImageModel> response) {
                if (response.code() == 200) {
                    ImageModel model3;
                    model3 = response.body();
                    LogInPreferences.addImages(model3,context);
                    rvImg.setItems(model3.backdrops);
                    rv.setHasFixedSize(true);
                    rv.setLayoutManager(new LinearLayoutManager(context,LinearLayoutManager.HORIZONTAL,false));
                    rv.setAdapter(rvImg);
                }
            }
            @Override
            public void onFailure(Call<ImageModel> call, Throwable t) {
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
                    movieModel = response.body();
                    ratedList = new RatedList();
                    for (Movie movie:movieModel.results){
                        Rated rated = new Rated();
                        rated.id = movie.id;
                        rated.value = movie.rating;
                        ratedList.ratedMovies.add(rated);
                        LogInPreferences.addRated(ratedList,context);
                    }

                }}
            @Override
            public void onFailure(Call<MovieModel> call, Throwable t) {
            }});
    }

    public void RatedShows(){

        String session_id = LogInPreferences.getSessionID(context);
        RestApi api = new RestApi(context);
        Call<ShowsModel> call = api.getUserRatedShows("account_id", session_id);
        call.enqueue(new Callback<ShowsModel>() {
            @Override
            public void onResponse(Call<ShowsModel> call, Response<ShowsModel> response) {
                if (response.code() == 200) {
                    showModel = response.body();
                    RatedList ratedList2 = new RatedList();
                    for (Shows shows:showModel.results){
                        Rated rated = new Rated();
                        rated.id = shows.id;
                        rated.value = shows.rating;
                        ratedList2.ratedMovies.add(rated);
                        LogInPreferences.addRatedShow(ratedList2,context);
                    }
                }}
            @Override
            public void onFailure(Call<ShowsModel> call, Throwable t) {
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

    public void FavoriteListener(int id,TextView tv) {
        String session;
        String favklik = "no";
        tv.setBackgroundResource(R.drawable.favourites_empty_hdpi);
        final ProgressDialog pd1;
        pd1  = new ProgressDialog(context);
        pd1.setMessage("working");
        session = LogInPreferences.getSessionID(context);
        final ApiCalls apiCalls = new ApiCalls(context);
        FavoriteModel movieModel;
        movieModel = LogInPreferences.getFavoriteList(context);

        for (Integer integer:movieModel.favorites){
         if (integer == id){
          favklik = "yes";
             tv.setBackgroundResource(R.drawable.favourites_full_hdpi);
         }


        }
        if (!session.isEmpty()){
            final int acc_id = LogInPreferences.getAccountID(context);
            if (favklik.equals("no")) {
                pd1.show();
                tv.setBackgroundResource(R.drawable.favourites_full_hdpi);
                favklik = "yes";
                FavoriteMoviePost favoriteMoviePost = new FavoriteMoviePost();
                favoriteMoviePost.favorite = true;
                favoriteMoviePost.media_id = id;
                favoriteMoviePost.media_type = "movie";
                RestApi api = new RestApi(context);
                Call<Movie> call = api.postUserFavorites("account_id", session, favoriteMoviePost);
                call.enqueue(new Callback<Movie>() {
                    @Override
                    public void onResponse(Call<Movie> call, Response<Movie> response) {
                        if (response.code() == 201) {
                            Movie movieModel;
                            movieModel = response.body();
                            apiCalls.GetListFavorites();
                        }
                        Handler handler = new Handler();
                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                pd1.dismiss();
                            }
                        },2000);

                    }

                    @Override
                    public void onFailure(Call<Movie> call, Throwable t) {
                    }
                });

            } else {
                pd1.show();
                tv.setBackgroundResource(R.drawable.favourites_empty_hdpi);
                favklik = "no";
                FavoriteMoviePost favoriteMoviePost = new FavoriteMoviePost();
                favoriteMoviePost.favorite = false;
                favoriteMoviePost.media_id = id;
                favoriteMoviePost.media_type = "movie";
                RestApi api = new RestApi(context);
                Call<Movie> call = api.postUserFavorites("account_id", session, favoriteMoviePost);
                call.enqueue(new Callback<Movie>() {
                    @Override
                    public void onResponse(Call<Movie> call, Response<Movie> response) {
                        if (response.code() == 200) {
                            Movie movieModel;
                            movieModel = response.body();
                            apiCalls.GetListFavorites();
                        }
                        Handler handler = new Handler();
                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                pd1.dismiss();
                            }
                        },2000);
                    }

                    @Override
                    public void onFailure(Call<Movie> call, Throwable t) {
                    }
                });

            }

        }else { Toast.makeText(context, "Please login to use this function", Toast.LENGTH_SHORT).show();}
    }


    public void WatchlistListener(int id,TextView tv) {
        String session;
        String watchklik = "no";
        tv.setBackgroundResource(R.drawable.watchlist_add_hdpi);
        final ProgressDialog pd1;
        pd1 = new ProgressDialog(context);
        pd1.setMessage("working");
        session = LogInPreferences.getSessionID(context);
        final ApiCalls apiCalls = new ApiCalls(context);
        final WatchModel movieModel;
        movieModel = LogInPreferences.getWtchList(context);

        for (Integer integer : movieModel.favorites) {
            if (integer == id) {
                watchklik = "yes";
                tv.setBackgroundResource(R.drawable.watchlist_remove_hdpi);
            }}
            if (!session.isEmpty()) {
                int acc_id = LogInPreferences.getAccountID(context);
                if (watchklik.equals("no")) {
                    pd1.show();
                    tv.setBackgroundResource(R.drawable.watchlist_remove_hdpi);
                    watchklik = "yes";
                    WatchlistMoviePost watchlistMoviePost = new WatchlistMoviePost();
                    watchlistMoviePost.watchlist = true;
                    watchlistMoviePost.media_id = id;
                    watchlistMoviePost.media_type = "movie";
                    RestApi api = new RestApi(context);
                    Call<Movie> call = api.postUserWatchlist("account_id", session, watchlistMoviePost);
                    call.enqueue(new Callback<Movie>() {
                        @Override
                        public void onResponse(Call<Movie> call, Response<Movie> response) {
                            if (response.code() == 201) {
                                Movie
                                movie = response.body();
                                apiCalls.getWatchList();

                            }

                            Handler handler = new Handler();
                            handler.postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    pd1.dismiss();
                                }
                            }, 2000);
                        }

                        @Override
                        public void onFailure(Call<Movie> call, Throwable t) {
                        }
                    });

                } else {
                    pd1.show();
                    tv.setBackgroundResource(R.drawable.watchlist_add_hdpi);
                    watchklik = "no";
                    WatchlistMoviePost watchlistMoviePost = new WatchlistMoviePost();
                    watchlistMoviePost.watchlist = false;
                    watchlistMoviePost.media_id = id;
                    watchlistMoviePost.media_type = "movie";
                    RestApi api = new RestApi(context);
                    Call<Movie> call = api.postUserWatchlist("account_id", session, watchlistMoviePost);
                    call.enqueue(new Callback<Movie>() {
                        @Override
                        public void onResponse(Call<Movie> call, Response<Movie> response) {
                            if (response.code() == 200) {
                                Movie model;
                                model = response.body();
                                apiCalls.getWatchList();
                            }
                            Handler handler = new Handler();
                            handler.postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    pd1.dismiss();
                                }
                            }, 2000);
                        }

                        @Override
                        public void onFailure(Call<Movie> call, Throwable t) {
                        }
                    });


                }
            } else {
                Toast.makeText(context, "Please login to use this function", Toast.LENGTH_SHORT).show();
            }
        }

    public void FavoriteShowsListener(int id,TextView tv) {

        String session;
        String favklik = "no";
        tv.setBackgroundResource(R.drawable.favourites_empty_hdpi);
        final ProgressDialog pd1;
        pd1  = new ProgressDialog(context);
        pd1.setMessage("working");
        session = LogInPreferences.getSessionID(context);
        final ApiCalls apiCalls = new ApiCalls(context);
        FavoriteModel movieModel;
        movieModel = LogInPreferences.getFavoriteListShows(context);

        for (Integer integer:movieModel.favorites){
            if (integer == id){
                favklik = "yes";
                tv.setBackgroundResource(R.drawable.favourites_full_hdpi);
            }


        }

                if (!session.isEmpty()){
                    pd1.show();
                    String session_id = LogInPreferences.getSessionID(context);
                    int acc_id = LogInPreferences.getAccountID(context);
                    if (favklik.equals("no")) {
                        tv.setBackgroundResource(R.drawable.favourites_full_hdpi);
                        favklik = "yes";
                        FavoriteMoviePost favoriteMoviePost = new FavoriteMoviePost();
                        favoriteMoviePost.favorite = true;
                        favoriteMoviePost.media_id = id;
                        favoriteMoviePost.media_type = "tv";
                        RestApi api = new RestApi(context);
                        Call<Shows> call = api.postUserShowFavorites("account_id", session_id, favoriteMoviePost);
                        call.enqueue(new Callback<Shows>() {
                            @Override
                            public void onResponse(Call<Shows> call, Response<Shows> response) {
                                if (response.code() == 201) {
                                    Shows model;
                                    model = response.body();
                                    apiCalls.GetListShowsFavorites();
                                }
                                Handler handler = new Handler();
                                handler.postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        pd1.dismiss();
                                    }
                                },2000);
                            }

                            @Override
                            public void onFailure(Call<Shows> call, Throwable t) {
                            }
                        });
                    } else {
                        pd1.show();
                        tv.setBackgroundResource(R.drawable.favourites_empty_hdpi);
                        favklik = "no";
                        FavoriteMoviePost favoriteMoviePost = new FavoriteMoviePost();
                        favoriteMoviePost.favorite = false;
                        favoriteMoviePost.media_id = id;
                        favoriteMoviePost.media_type = "tv";

                        RestApi api = new RestApi(context);
                        Call<Shows> call = api.postUserShowFavorites("account_id", session_id, favoriteMoviePost);
                        call.enqueue(new Callback<Shows>() {
                            @Override
                            public void onResponse(Call<Shows> call, Response<Shows> response) {
                                if (response.code() == 200) {
                                    Shows model;
                                    model = response.body();

                                    apiCalls.GetListShowsFavorites();
                                }
                                Handler handler = new Handler();
                                handler.postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        pd1.dismiss();
                                    }
                                },2000);
                            }

                            @Override
                            public void onFailure(Call<Shows> call, Throwable t) {
                            }
                        });
                    }


                }else {
                    Toast.makeText(context, "Please login to use this function", Toast.LENGTH_SHORT).show();
                }}

    public void WatchlistShowsListener(int id,TextView tv) {
        String session;
        String watchklik = "no";
        tv.setBackgroundResource(R.drawable.watchlist_add_hdpi);
        final ProgressDialog pd1;
        pd1 = new ProgressDialog(context);
        pd1.setMessage("working");
        session = LogInPreferences.getSessionID(context);
        final ApiCalls apiCalls = new ApiCalls(context);
        final WatchModel movieModel;
        movieModel = LogInPreferences.getWtchListShows(context);

        for (Integer integer : movieModel.favorites) {
            if (integer == id) {
                watchklik = "yes";
                tv.setBackgroundResource(R.drawable.watchlist_remove_hdpi);
            }}
                if (!session.isEmpty()){
                    String session_id = LogInPreferences.getSessionID(context);
                    int acc_id = LogInPreferences.getAccountID(context);
                    if (watchklik.equals("no")) {
                        pd1.show();
                        tv.setBackgroundResource(R.drawable.watchlist_remove_hdpi);
                        watchklik = "yes";
                        WatchlistMoviePost watchlistMoviePost = new WatchlistMoviePost();
                        watchlistMoviePost.watchlist = true;
                        watchlistMoviePost.media_id = id;
                        watchlistMoviePost.media_type = "tv";
                        RestApi api = new RestApi(context);
                        Call<Shows> call = api.postUserShowWatchlist("account_id", session_id, watchlistMoviePost);
                        call.enqueue(new Callback<Shows>() {
                            @Override
                            public void onResponse(Call<Shows> call, Response<Shows> response) {
                                if (response.code() == 201) {
                                    Shows model;
                                    model = response.body();
                                    apiCalls.getWatchListShows();
                                }
                                Handler handler = new Handler();
                                handler.postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        pd1.dismiss();
                                    }
                                },2000);
                            }

                            @Override
                            public void onFailure(Call<Shows> call, Throwable t) {
                            }
                        });
                    } else {
                        pd1.show();
                        tv.setBackgroundResource(R.drawable.watchlist_add_hdpi);
                        watchklik = "yes";
                        WatchlistMoviePost watchlistMoviePost = new WatchlistMoviePost();
                        watchlistMoviePost.watchlist = false;
                        watchlistMoviePost.media_id = id;
                        watchlistMoviePost.media_type = "tv";

                        RestApi api = new RestApi(context);
                        Call<Shows> call = api.postUserShowWatchlist("account_id", session_id, watchlistMoviePost);
                        call.enqueue(new Callback<Shows>() {
                            @Override
                            public void onResponse(Call<Shows> call, Response<Shows> response) {
                                if (response.code() == 200) {
                                    Shows model;
                                    model = response.body();
                                    apiCalls.getWatchListShows();
                                }
                                Handler handler = new Handler();
                                handler.postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        pd1.dismiss();
                                    }
                                },2000);
                            }

                            @Override
                            public void onFailure(Call<Shows> call, Throwable t) {
                            }
                        });
                    }
                }else {
                    Toast.makeText(context, "Please login to use this function", Toast.LENGTH_SHORT).show();
                }}
}