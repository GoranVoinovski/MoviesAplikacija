package com.znaci.goran.moviesaplikacija.activities;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.view.ViewCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;
import com.znaci.goran.moviesaplikacija.R;
import com.znaci.goran.moviesaplikacija.adapters.RecyclerViewGenretAdapter;
import com.znaci.goran.moviesaplikacija.adapters.RecyclerViewSimilarAdapter;
import com.znaci.goran.moviesaplikacija.adapters.RecyclerViewSimilarShowsAdapter;
import com.znaci.goran.moviesaplikacija.api.RestApi;
import com.znaci.goran.moviesaplikacija.helpers.ApiCalls;
import com.znaci.goran.moviesaplikacija.listeners.OnRowGenreClickListener;
import com.znaci.goran.moviesaplikacija.listeners.OnRowMovieClickListener;
import com.znaci.goran.moviesaplikacija.listeners.OnRowShowClickListener;
import com.znaci.goran.moviesaplikacija.models.FavoriteMoviePost;
import com.znaci.goran.moviesaplikacija.models.Genre;
import com.znaci.goran.moviesaplikacija.models.Movie;
import com.znaci.goran.moviesaplikacija.models.MovieModel;
import com.znaci.goran.moviesaplikacija.models.Rated;
import com.znaci.goran.moviesaplikacija.models.RatedList;
import com.znaci.goran.moviesaplikacija.models.Shows;
import com.znaci.goran.moviesaplikacija.models.ShowsModel;
import com.znaci.goran.moviesaplikacija.models.VideoModel;
import com.znaci.goran.moviesaplikacija.models.Videos;
import com.znaci.goran.moviesaplikacija.models.WatchlistMoviePost;
import com.znaci.goran.moviesaplikacija.preferencesManager.LogInPreferences;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ScrollingShowsDetailActivity extends AppCompatActivity {

    @BindView(R.id.nameShow)
    TextView nameOfShow;
    @BindView(R.id.ratingimg)
    ImageView ratingimg;
    @BindView(R.id.play)
    TextView playShow;
    @BindView(R.id.ShowVideo)
    ImageView videoOfShow;
    @BindView(R.id.firstair)
    TextView showAirDate;
    @BindView(R.id.numberofepisodes)
    TextView episodesOfShow;
    @BindView(R.id.seeCast)
    Button castOfShow;
    @BindView(R.id.description)
    TextView descriptionOfShow;
    @BindView(R.id.favorite)
    ImageView favorite;
    @BindView(R.id.watchlist)
    ImageView watchlist;
    @BindView(R.id.similarShowsRV)
    RecyclerView similarShowsrv;
    @BindView(R.id.rating)
    TextView ratiingtxt;
    @BindView(R.id.setarating)
    TextView rateit;
    @BindView(R.id.app_bar)AppBarLayout apb;
    @BindView(R.id.genreRV)
    RecyclerView genresRV;
    @BindView(R.id.movieavgrating)
    TextView movieavg;
    RecyclerViewGenretAdapter adapterGenre;
    RecyclerViewSimilarShowsAdapter adapterSimilar;
    FloatingActionButton fab;
    CollapsingToolbarLayout toolbarLayout;
    Shows model;
    ShowsModel showsModel;
    VideoModel model2;
    int position = 0;
    int movieID = 0;
    String session;
    String favklik = "no";
    String watchklik = "no";
    RatedList ratedList;
    ProgressDialog pd,pd1;
    ApiCalls apiCalls;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scrolling_shows_detail);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ButterKnife.bind(this);
        apiCalls = new ApiCalls(this);
        pd = new ProgressDialog(ScrollingShowsDetailActivity.this);
        pd.setMessage("loading");
        pd1 = new ProgressDialog(ScrollingShowsDetailActivity.this);
        pd1.setMessage("working");
        fab = (FloatingActionButton) findViewById(R.id.fab);
        toolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.toolbar_layout);
        toolbarLayout.setExpandedTitleColor(getResources().getColor(R.color.transparent2));
        session = LogInPreferences.getSessionID(this);
        ratedList = LogInPreferences.getRatedShow(this);
        if (ratedList == null){
            ratedList = new RatedList();
            if (ratedList.ratedMovies == null){
                ratedList.ratedMovies = new ArrayList<>();
            }
        }
        apb.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                if(toolbarLayout.getHeight() + verticalOffset < 2 * ViewCompat.getMinimumHeight(toolbarLayout)){
                    nameOfShow.setVisibility(View.INVISIBLE);
                }else {
                    nameOfShow.setVisibility(View.VISIBLE);

                }

            }
        });

        Intent intent = getIntent();
        if (intent.hasExtra("showid")) {

            movieID = intent.getIntExtra("showid", 0);
            position = intent.getIntExtra("position", 0);
            for (Rated rated:ratedList.ratedMovies){
                if (movieID == rated.id){
                    if (rated.value <= 6){
                        ratiingtxt.setText("" + rated.value);
                        Picasso.with(ScrollingShowsDetailActivity.this).load(R.drawable.rate_02_hdpi).centerInside().fit().into(ratingimg);
                    }else if (rated.value >= 7 && rated.value <9){
                        ratiingtxt.setText("" + rated.value);
                        Picasso.with(ScrollingShowsDetailActivity.this).load(R.drawable.rate_04_hdpi).centerInside().fit().into(ratingimg);
                    }else if (rated.value >=9 && rated.value <10){
                        ratiingtxt.setText("" + rated.value);
                        Picasso.with(ScrollingShowsDetailActivity.this).load(R.drawable.rate_06_hdpi).centerInside().fit().into(ratingimg);
                    }else if (rated.value == 10){
                        ratiingtxt.setText("" + rated.value);
                        Picasso.with(ScrollingShowsDetailActivity.this).load(R.drawable.rate_full_hdpi).centerInside().fit().into(ratingimg);
                    }else {
                        ratiingtxt.setText("" + 0.0);

                    }
                    rateit.setText("Update rating");
                }}
                if (!session.isEmpty()){
                    GetFavorite();
                }
            pd.show();
            RestApi api = new RestApi(this);
            Call<Shows> call = api.getShow(movieID,"credits");
            call.enqueue(new Callback<Shows>() {
                @Override
                public void onResponse(Call<Shows> call, Response<Shows> response) {
                    if (response.code() == 200) {

                        model = response.body();
                        nameOfShow.setText(model.name);
                        toolbarLayout.setTitle(model.name);
                        movieavg.setText("TMDB rating: " + model.vote_average);
                        descriptionOfShow.setText(model.overview);
                        if (model.first_air_date != null){
                            String firstdate = DateConvert(model.first_air_date);
                            showAirDate.setText(" First air date: " + firstdate);
                        }

                        if (model.last_air_date != null && model.first_air_date!= null){
                            String firstdate = DateConvert(model.first_air_date);
                            String lastdate = DateConvert(model.last_air_date);
                            showAirDate.setText(" First air date: " + firstdate + "\n Last air date: " + lastdate);
                        }

                        else {showAirDate.setText("Air dates not available");

                        }

                        if (model.number_of_episodes != 0){

                            episodesOfShow.setText(" Episodes: " + model.number_of_episodes);
                        }

                        if (model.number_of_seasons != 0){
                            episodesOfShow.setText(" Episodes: " + model.number_of_episodes + "\n Seasons: " + model.number_of_seasons);
                        }

                        if (model.backdrop_path != null){
                            String path = "http://image.tmdb.org/t/p/w185" + model.backdrop_path;
                            Picasso.with(ScrollingShowsDetailActivity.this).load(path).centerInside().fit().into(videoOfShow);
                        }

                        adapterGenre = new RecyclerViewGenretAdapter(ScrollingShowsDetailActivity.this, new OnRowGenreClickListener() {
                            @Override
                            public void onRowClick(Genre genre, int position) {
                                Intent intent = new Intent(ScrollingShowsDetailActivity.this, AdvancedSearchShowsActivity.class);
                                intent.putExtra("GID", genre.id);
                                intent.putExtra("name", genre.name);
                                startActivityForResult(intent, 1212);
                            }
                        });
                        adapterGenre.setItems(model.genres);
                        genresRV.setHasFixedSize(true);
                        genresRV.setLayoutManager(new LinearLayoutManager(ScrollingShowsDetailActivity.this, LinearLayoutManager.HORIZONTAL, false));
                        genresRV.setAdapter(adapterGenre);

                        RestApi api3 = new RestApi(ScrollingShowsDetailActivity.this);
                        Call<VideoModel> call3 = api3.getShowVideo(movieID);
                        call3.enqueue(new Callback<VideoModel>() {
                            @Override
                            public void onResponse(Call<VideoModel> call, Response<VideoModel> response) {
                                if (response.code() == 200) {
                                    model2 = response.body();
                                    if (model2.results.size() == 0){}else {
                                        final Videos video = model2.results.get(0);
                                        fab.setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View v) {
                                                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.youtube.com/watch?v=" + video.key)));
                                                Log.i("Video", "Video Playing....");
                                            }
                                        });}}}
                            @Override
                            public void onFailure(Call<VideoModel> call, Throwable t) {
                            }});}
                }

                @Override
                public void onFailure(Call<Shows> call, Throwable t) {
                }});
            GetSimilar();
            pd.dismiss();
            FavoriteListener();
            WatchlistListener();
            RatingListener();


        }}

    @OnClick(R.id.seeCast)
    public void Cast(){

        Intent intent = new Intent(ScrollingShowsDetailActivity.this,FullCastActivity.class);
        intent.putExtra("showid",movieID);
        startActivity(intent);


    }

    public void GetSimilar() {

        RestApi api5 = new RestApi(ScrollingShowsDetailActivity.this);
        Call<ShowsModel> call11 = api5.getSimilarShows(movieID);
        call11.enqueue(new Callback<ShowsModel>() {
            @Override
            public void onResponse(Call<ShowsModel> call, Response<ShowsModel> response) {
                showsModel = response.body();
                adapterSimilar = new RecyclerViewSimilarShowsAdapter(ScrollingShowsDetailActivity.this, new OnRowShowClickListener() {
                    @Override
                    public void onRowClick(Shows shows, int position) {
                        movieID = shows.id;
                        Intent intent = new Intent(ScrollingShowsDetailActivity.this, ScrollingShowsDetailActivity.class);
                        intent.putExtra("showid", movieID);
                        startActivity(intent);
                        finish();
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
                adapterSimilar.setItems(showsModel.results);
                similarShowsrv.setHasFixedSize(true);
                similarShowsrv.setLayoutManager(new LinearLayoutManager(ScrollingShowsDetailActivity.this, LinearLayoutManager.HORIZONTAL, false));
                similarShowsrv.setAdapter(adapterSimilar);
            }

            @Override
            public void onFailure(Call<ShowsModel> call, Throwable t) {
            }
        });
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent();
        setResult(RESULT_OK, intent);
        finish();
    }

    public void FavoriteListener() {
            favorite.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
            if (!session.isEmpty()){
                pd1.show();
                String session_id = LogInPreferences.getSessionID(ScrollingShowsDetailActivity.this);
                int acc_id = LogInPreferences.getAccountID(ScrollingShowsDetailActivity.this);
                if (favklik.equals("no")) {
                    Picasso.with(ScrollingShowsDetailActivity.this).load(R.drawable.favourites_full_hdpi).into(favorite);
                    favklik = "yes";
                    FavoriteMoviePost favoriteMoviePost = new FavoriteMoviePost();
                    favoriteMoviePost.favorite = true;
                    favoriteMoviePost.media_id = movieID;
                    favoriteMoviePost.media_type = "tv";
                    RestApi api = new RestApi(ScrollingShowsDetailActivity.this);
                    Call<Shows> call = api.postUserShowFavorites("account_id", session_id, favoriteMoviePost);
                    call.enqueue(new Callback<Shows>() {
                        @Override
                        public void onResponse(Call<Shows> call, Response<Shows> response) {
                            if (response.code() == 201) {
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
                    Picasso.with(ScrollingShowsDetailActivity.this).load(R.drawable.favourites_empty_hdpi).into(favorite);
                    favklik = "no";
                    FavoriteMoviePost favoriteMoviePost = new FavoriteMoviePost();
                    favoriteMoviePost.favorite = false;
                    favoriteMoviePost.media_id = movieID;
                    favoriteMoviePost.media_type = "tv";

                    RestApi api = new RestApi(ScrollingShowsDetailActivity.this);
                    Call<Shows> call = api.postUserShowFavorites("account_id", session_id, favoriteMoviePost);
                    call.enqueue(new Callback<Shows>() {
                        @Override
                        public void onResponse(Call<Shows> call, Response<Shows> response) {
                            if (response.code() == 200) {
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
                Toast.makeText(ScrollingShowsDetailActivity.this, "Please login to use this function", Toast.LENGTH_SHORT).show();
            }

                }
            });}

    public void WatchlistListener() {
            watchlist.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                if (!session.isEmpty()){
                    String session_id = LogInPreferences.getSessionID(ScrollingShowsDetailActivity.this);
                    int acc_id = LogInPreferences.getAccountID(ScrollingShowsDetailActivity.this);
                    if (watchklik.equals("no")) {
                        pd1.show();
                        Picasso.with(ScrollingShowsDetailActivity.this).load(R.drawable.watchlist_remove_hdpi).into(watchlist);
                        favklik = "yes";
                        WatchlistMoviePost watchlistMoviePost = new WatchlistMoviePost();
                        watchlistMoviePost.watchlist = true;
                        watchlistMoviePost.media_id = movieID;
                        watchlistMoviePost.media_type = "tv";
                        RestApi api = new RestApi(ScrollingShowsDetailActivity.this);
                        Call<Shows> call = api.postUserShowWatchlist("account_id", session_id, watchlistMoviePost);
                        call.enqueue(new Callback<Shows>() {
                            @Override
                            public void onResponse(Call<Shows> call, Response<Shows> response) {
                                if (response.code() == 201) {
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
                        Picasso.with(ScrollingShowsDetailActivity.this).load(R.drawable.watchlist_add_hdpi).into(watchlist);
                        favklik = "yes";
                        WatchlistMoviePost watchlistMoviePost = new WatchlistMoviePost();
                        watchlistMoviePost.watchlist = false;
                        watchlistMoviePost.media_id = movieID;
                        watchlistMoviePost.media_type = "tv";

                        RestApi api = new RestApi(ScrollingShowsDetailActivity.this);
                        Call<Shows> call = api.postUserShowWatchlist("account_id", session_id, watchlistMoviePost);
                        call.enqueue(new Callback<Shows>() {
                            @Override
                            public void onResponse(Call<Shows> call, Response<Shows> response) {
                                if (response.code() == 200) {
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
                    Toast.makeText(ScrollingShowsDetailActivity.this, "Please login to use this function", Toast.LENGTH_SHORT).show();
                }

                }
            });
    }

    public void GetFavorite(){
        String session_id = LogInPreferences.getSessionID(ScrollingShowsDetailActivity.this);
        RestApi api6 = new RestApi(ScrollingShowsDetailActivity.this);
        Call<Shows> call12 = api6.getShowFavorite(movieID, session_id);
        call12.enqueue(new Callback<Shows>() {
            @Override
            public void onResponse(Call<Shows> call, Response<Shows> response) {
                model = response.body();
                if (model.favorite) {
                    Picasso.with(ScrollingShowsDetailActivity.this).load(R.drawable.favourites_full_hdpi).into(favorite);
                    favklik = "yes";
                } else {
                    Picasso.with(ScrollingShowsDetailActivity.this).load(R.drawable.favourites_empty_hdpi).into(favorite);
                    favklik = "no";
                }

                if (model.watchlist) {
                    Picasso.with(ScrollingShowsDetailActivity.this).load(R.drawable.watchlist_remove_hdpi).into(watchlist);
                    watchklik = "yes";
                } else {
                    Picasso.with(ScrollingShowsDetailActivity.this).load(R.drawable.watchlist_add_hdpi).into(watchlist);
                    watchklik = "no";
                }}
            @Override
            public void onFailure(Call<Shows> call, Throwable t) {
            }});}

    public void RatingListener() {
        rateit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Dialog rankDialog = new Dialog(ScrollingShowsDetailActivity.this, R.style.FullHeightDialog);
                rankDialog.setContentView(R.layout.dialograte);
                rankDialog.setCancelable(true);
                final RatingBar ratingBar = (RatingBar)rankDialog.findViewById(R.id.dialog_ratingbar);


                TextView text = (TextView) rankDialog.findViewById(R.id.rank_dialog_text1);
                text.setText(model.name);

                Button updateButton = (Button) rankDialog.findViewById(R.id.rank_dialog_button);
                updateButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        final Rated rated = new Rated();
                        float rate = ratingBar.getRating() + 5;
                        ratingBar.setRating(rate);
                        rateit.setText("Update rating");
                        if (rate <= 6){
                            ratiingtxt.setText("" + rate);
                            Picasso.with(ScrollingShowsDetailActivity.this).load(R.drawable.rate_02_hdpi).centerInside().fit().into(ratingimg);
                        }else if (rate >= 7 && rate <=8){
                            ratiingtxt.setText("" + rate);
                            Picasso.with(ScrollingShowsDetailActivity.this).load(R.drawable.rate_04_hdpi).centerInside().fit().into(ratingimg);
                        }else if (rate == 9){
                            ratiingtxt.setText("" + rate);
                            Picasso.with(ScrollingShowsDetailActivity.this).load(R.drawable.rate_06_hdpi).centerInside().fit().into(ratingimg);
                        }else if (rate == 10){
                            ratiingtxt.setText("" + rate);
                            Picasso.with(ScrollingShowsDetailActivity.this).load(R.drawable.rate_full_hdpi).centerInside().fit().into(ratingimg);
                        }
                        rated.value = rate;
                        rated.id = movieID;
                        RestApi api = new RestApi(ScrollingShowsDetailActivity.this);
                        Call<Shows> call = api.postUserShowRateing(movieID, session, "json/application", rated);
                        call.enqueue(new Callback<Shows>() {
                            @Override
                            public void onResponse(Call<Shows> call, Response<Shows> response) {
                                pd1.show();
                                if (response.code() == 201) {
                                    model = response.body();
                                    ratedList.ratedMovies.add(rated);
                                    LogInPreferences.addRatedShow(ratedList,ScrollingShowsDetailActivity.this);
                                    rankDialog.dismiss();
                                    ratiingtxt.setText("" + rated.value);
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
                            }});


                        rankDialog.dismiss();
                    }
                });

                rankDialog.show();
            }
        });}

    public String DateConvert(String date){

        String str[] = date.split("-");
        String year = (str[0]);
        String month = (str[1]);
        String day = (str[2]);
        if (month.equals("01")){
            month = "January";
        }else if (month.equals("02")){
            month = "February";
        }else if (month.equals("03")){
            month = "March";
        }else if (month.equals("04")){
            month = "April";
        }else if (month.equals("05")){
            month = "May";
        }else if (month.equals("06")){
            month = "June";
        }else if (month.equals("07")){
            month = "July";
        }else if (month.equals("08")){
            month = "August";
        }else if (month.equals("09")){
            month = "September";
        }else if (month.equals("10")){
            month = "October";
        }else if (month.equals("11")){
            month = "November";
        }else if (month.equals("12")){
            month = "December";
        }

        return day + " " + month + " " + year;
    }
}
