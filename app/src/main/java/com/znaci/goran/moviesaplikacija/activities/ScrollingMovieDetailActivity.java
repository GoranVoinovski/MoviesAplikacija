package com.znaci.goran.moviesaplikacija.activities;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.media.Rating;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.view.ViewCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;
import com.znaci.goran.moviesaplikacija.R;
import com.znaci.goran.moviesaplikacija.adapters.RecyclerViewGenretAdapter;
import com.znaci.goran.moviesaplikacija.adapters.RecyclerViewSimilarAdapter;
import com.znaci.goran.moviesaplikacija.api.RestApi;
import com.znaci.goran.moviesaplikacija.helpers.ApiCalls;
import com.znaci.goran.moviesaplikacija.listeners.OnRowGenreClickListener;
import com.znaci.goran.moviesaplikacija.listeners.OnRowMovieClickListener;
import com.znaci.goran.moviesaplikacija.models.Cast;
import com.znaci.goran.moviesaplikacija.models.Crew;
import com.znaci.goran.moviesaplikacija.models.FavoriteMoviePost;
import com.znaci.goran.moviesaplikacija.models.Genre;
import com.znaci.goran.moviesaplikacija.models.Movie;
import com.znaci.goran.moviesaplikacija.models.MovieModel;
import com.znaci.goran.moviesaplikacija.models.Rated;
import com.znaci.goran.moviesaplikacija.models.RatedList;
import com.znaci.goran.moviesaplikacija.models.WatchlistMoviePost;
import com.znaci.goran.moviesaplikacija.preferencesManager.LogInPreferences;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ScrollingMovieDetailActivity extends AppCompatActivity {

    @BindView(R.id.MovieVideo)
    ImageView videoMovie;
    @BindView(R.id.favorite)
    ImageView favorite;
    @BindView(R.id.watchlist)
    ImageView watchlist;
    @BindView(R.id.ratingimg)
    ImageView ratingimg;
    @BindView(R.id.direktor)
    TextView textDirektor;
    @BindView(R.id.play)
    TextView playMovie;
    @BindView(R.id.writers)
    TextView textWriters;
    @BindView(R.id.stars)
    TextView textStars;
    @BindView(R.id.nameMovie)
    TextView titleMovie;
    @BindView(R.id.seeCast)
    Button btnSeeCast;
    @BindView(R.id.description)
    TextView textDescription;
    @BindView(R.id.similarMovies)
    LinearLayout similarMovie;
    @BindView(R.id.similarMoviesRV)
    RecyclerView similarMovierv;
    @BindView(R.id.genreRV)
    RecyclerView genresRV;
    @BindView(R.id.rating)
    TextView ratiingtxt;
    @BindView(R.id.setarating)
    TextView rateit;
    @BindView(R.id.movieavgrating)
    TextView movieavg;
    @BindView(R.id.app_bar)
    AppBarLayout apb;
    RecyclerViewSimilarAdapter adapter;
    RecyclerViewGenretAdapter genreAdapter;
    Movie model = new Movie();
    MovieModel movieModel;
    Crew crew;
    Cast cast;
    int position = 0;
    int movieID;
    String favklik = "no";
    String watchklik = "no";
    CollapsingToolbarLayout toolbarLayout;
    FloatingActionButton floating;
    Toolbar toolbar;
    ApiCalls apiCalls;
    String session;
    RatedList ratedList;
    ProgressDialog pd,pd1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scrolling_movie_detail);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        floating = (FloatingActionButton) findViewById(R.id.fab);
        setSupportActionBar(toolbar);
        ButterKnife.bind(this);
        pd = new ProgressDialog(ScrollingMovieDetailActivity.this);
        pd.setMessage("loading");
        pd1 = new ProgressDialog(ScrollingMovieDetailActivity.this);
        pd1.setMessage("working");
        toolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.toolbar_layout);
        toolbarLayout.setExpandedTitleColor(getResources().getColor(R.color.transparent2));
        apiCalls = new ApiCalls(this);
        session = LogInPreferences.getSessionID(this);
        ratedList = LogInPreferences.getRated(this);
        if (ratedList == null){
            ratedList = new RatedList();
            if (ratedList.ratedMovies == null){
                ratedList.ratedMovies = new ArrayList<>();
            }
        }
        Intent intent = getIntent();
        if (intent.hasExtra("id")) {

            movieID = intent.getIntExtra("id", 0);
            position = intent.getIntExtra("position", 0);
            for (Rated rated:ratedList.ratedMovies){
               if (movieID == rated.id){

                   if (rated.value <= 6){
                       ratiingtxt.setText("" + rated.value);
                       Picasso.with(ScrollingMovieDetailActivity.this).load(R.drawable.rate_02_hdpi).centerInside().fit().into(ratingimg);
                   }else if (rated.value >= 7 && rated.value <9){
                       ratiingtxt.setText("" + rated.value);
                       Picasso.with(ScrollingMovieDetailActivity.this).load(R.drawable.rate_04_hdpi).centerInside().fit().into(ratingimg);
                   }else if (rated.value >=9 && rated.value <10){
                       ratiingtxt.setText("" + rated.value);
                       Picasso.with(ScrollingMovieDetailActivity.this).load(R.drawable.rate_06_hdpi).centerInside().fit().into(ratingimg);
                   }else if (rated.value == 10){
                       ratiingtxt.setText("" + rated.value);
                       Picasso.with(ScrollingMovieDetailActivity.this).load(R.drawable.rate_full_hdpi).centerInside().fit().into(ratingimg);
                   }else {
                       ratiingtxt.setText(""+0.0);

                   }
                   rateit.setText("Update rating");
               }}
            GetMovie();
            if (!session.isEmpty()){
                GetWatchList();
                GetFavorite();
            }

            apiCalls.GetVideo(movieID, floating);
            GetSimilar();
        }

        apb.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                if (toolbarLayout.getHeight() + verticalOffset < 2 * ViewCompat.getMinimumHeight(toolbarLayout)) {
                    titleMovie.setVisibility(View.INVISIBLE);
                } else {
                    titleMovie.setVisibility(View.VISIBLE);
                }
            }
        });

    }

    @OnClick(R.id.seeCast)
    public void Cast(){

        Intent intent = new Intent(ScrollingMovieDetailActivity.this,FullCastActivity.class);
        intent.putExtra("id",movieID);
        startActivity(intent);


    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent();
        setResult(RESULT_OK, intent);
        finish();
    }

    public void GetWatchList() {
        RestApi apiw = new RestApi(ScrollingMovieDetailActivity.this);
        Call<Movie> callw = apiw.getWatchlist(movieID, session);
        callw.enqueue(new Callback<Movie>() {
            @Override
            public void onResponse(Call<Movie> call, Response<Movie> response) {
                if (response.code() == 200) {

                    model = response.body();
                    if (model.watchlist) {
                        Picasso.with(ScrollingMovieDetailActivity.this).load(R.drawable.watchlist_remove_hdpi).into(watchlist);
                        watchklik = "yes";
                    } else {
                        Picasso.with(ScrollingMovieDetailActivity.this).load(R.drawable.watchlist_add_hdpi).into(watchlist);
                        watchklik = "no";
                    }
                }
            }

            @Override
            public void onFailure(Call<Movie> call, Throwable t) {
            }
        });
    }


    public void GetMovie() {
        pd.show();
        RestApi api = new RestApi(this);
        Call<Movie> call = api.getMovie(movieID, "credits");
        call.enqueue(new Callback<Movie>() {
            @Override
            public void onResponse(Call<Movie> call, Response<Movie> response) {
                if (response.code() == 200) {
                    ArrayList<Crew> writers = new ArrayList<>();
                    ArrayList<Cast> stars = new ArrayList<>();
                    model = response.body();

                    if (model.overview != null) {
                        textDescription.setText(model.overview);
                    } else {
                        textDescription.setText("No overview available");
                    }

                    if (model.credits.crew.size() > 0) {

                        for (int i = 0; i < 1; i++) {
                            crew = model.credits.crew.get(i);
                            textDirektor.setText("Director: " + crew.name);
                        }
                    }

                    for (Crew crew : model.credits.crew) {
                        if (crew.department.equals("Writing")) {
                            writers.add(crew);
                        } else {
                            textWriters.setText("");
                        }
                    }


                    if (writers.size() == 1) {
                        crew = writers.get(0);
                        textWriters.setText(crew.name);
                    } else if (writers.size() > 1) {
                        for (int i = 0; i < 2; i++) {
                            crew = writers.get(i);
                            if (writers.size() > 0) {
                                textWriters.setText(textWriters.getText().toString() + crew.name + ", ");
                            }
                        }
                    }

                    textWriters.setText("Writers: " + textWriters.getText().toString());


                    if (model.credits.cast.size() >= 3) {
                        for (int i = 0; i < 3; i++) {
                            cast = model.credits.cast.get(i);
                            if (cast != null) {
                                stars.add(cast);
                            }
                        }
                    } else if (model.credits.cast.size() == 2) {
                        for (int i = 0; i < 2; i++) {
                            cast = model.credits.cast.get(i);
                            if (cast != null) {
                                stars.add(cast);
                            }
                        }
                    } else {
                        if (cast != null) {
                            stars.add(cast);
                        }
                    }


                    if (stars.size() > 0) {
                        for (int i = 0; i < stars.size(); i++) {
                            cast = stars.get(i);
                            if (stars.size() > 0) {
                                textStars.setText(textStars.getText().toString() + cast.name + ", ");
                            }
                        }
                        textStars.setText("Stars: " + textStars.getText().toString());
                    } else {
                        textStars.setText("Stars: ");
                    }

                    toolbarLayout.setTitle(model.title);
                    movieavg.setText("TMDB rating: " + model.vote_average);
                    titleMovie.setText(model.title);
                    String path = "http://image.tmdb.org/t/p/w185" + model.backdrop_path;
                    Picasso.with(ScrollingMovieDetailActivity.this).load(path).centerInside().fit().into(videoMovie);
                    genreAdapter = new RecyclerViewGenretAdapter(ScrollingMovieDetailActivity.this, new OnRowGenreClickListener() {
                        @Override
                        public void onRowClick(Genre genre, int position) {
                            Intent intent = new Intent(ScrollingMovieDetailActivity.this, AdvancedSearchActivity.class);
                            intent.putExtra("GID", genre.id);
                            intent.putExtra("name", genre.name);
                            startActivityForResult(intent, 1212);
                        }
                    });
                    genreAdapter.setItems(model.genres);
                    genresRV.setHasFixedSize(true);
                    genresRV.setLayoutManager(new LinearLayoutManager(ScrollingMovieDetailActivity.this, LinearLayoutManager.HORIZONTAL, false));
                    genresRV.setAdapter(genreAdapter);
                    pd.dismiss();
                    FavoriteListener();
                    WatchlistListener();
                    RatingListener();
                }
            }

            @Override
            public void onFailure(Call<Movie> call, Throwable t) {
            }
        });
    }

    public void GetSimilar() {

        RestApi api5 = new RestApi(ScrollingMovieDetailActivity.this);
        Call<MovieModel> call11 = api5.getSimilar(movieID);
        call11.enqueue(new Callback<MovieModel>() {
            @Override
            public void onResponse(Call<MovieModel> call, Response<MovieModel> response) {
                movieModel = response.body();
                adapter = new RecyclerViewSimilarAdapter(ScrollingMovieDetailActivity.this, new OnRowMovieClickListener() {
                    @Override
                    public void onRowClick(Movie movie, int position) {
                        movieID = movie.id;
                        Intent intent = new Intent(ScrollingMovieDetailActivity.this, ScrollingMovieDetailActivity.class);
                        intent.putExtra("id", movieID);
                        startActivity(intent);
                        finish();
                    }
                });
                adapter.setItems(movieModel.results);
                similarMovierv.setHasFixedSize(true);
                similarMovierv.setLayoutManager(new LinearLayoutManager(ScrollingMovieDetailActivity.this, LinearLayoutManager.HORIZONTAL, false));
                similarMovierv.setAdapter(adapter);
            }

            @Override
            public void onFailure(Call<MovieModel> call, Throwable t) {
            }
        });
    }


    public void FavoriteListener() {
            favorite.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                  if (!session.isEmpty()){
                      final int acc_id = LogInPreferences.getAccountID(ScrollingMovieDetailActivity.this);
                      if (favklik.equals("no")) {
                          pd1.show();
                          Picasso.with(ScrollingMovieDetailActivity.this).load(R.drawable.favourites_full_hdpi).into(favorite);
                          favklik = "yes";
                          FavoriteMoviePost favoriteMoviePost = new FavoriteMoviePost();
                          favoriteMoviePost.favorite = true;
                          favoriteMoviePost.media_id = movieID;
                          favoriteMoviePost.media_type = "movie";
                          RestApi api = new RestApi(ScrollingMovieDetailActivity.this);
                          Call<Movie> call = api.postUserFavorites("account_id", session, favoriteMoviePost);
                          call.enqueue(new Callback<Movie>() {
                              @Override
                              public void onResponse(Call<Movie> call, Response<Movie> response) {
                                  if (response.code() == 201) {
                                      model = response.body();
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
                          Picasso.with(ScrollingMovieDetailActivity.this).load(R.drawable.favourites_empty_hdpi).into(favorite);
                          favklik = "no";
                          FavoriteMoviePost favoriteMoviePost = new FavoriteMoviePost();
                          favoriteMoviePost.favorite = false;
                          favoriteMoviePost.media_id = movieID;
                          favoriteMoviePost.media_type = "movie";
                          RestApi api = new RestApi(ScrollingMovieDetailActivity.this);
                          Call<Movie> call = api.postUserFavorites("account_id", session, favoriteMoviePost);
                          call.enqueue(new Callback<Movie>() {
                              @Override
                              public void onResponse(Call<Movie> call, Response<Movie> response) {
                                  if (response.code() == 200) {
                                      model = response.body();
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

                  }else { Toast.makeText(ScrollingMovieDetailActivity.this, "Please login to use this function", Toast.LENGTH_SHORT).show();}
                }
            });

    }

    public void WatchlistListener() {
        watchlist.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                  if (!session.isEmpty()){
                      int acc_id = LogInPreferences.getAccountID(ScrollingMovieDetailActivity.this);
                      if (watchklik.equals("no")) {
                          pd1.show();
                          Picasso.with(ScrollingMovieDetailActivity.this).load(R.drawable.watchlist_remove_hdpi).into(watchlist);
                          watchklik = "yes";
                          WatchlistMoviePost watchlistMoviePost = new WatchlistMoviePost();
                          watchlistMoviePost.watchlist = true;
                          watchlistMoviePost.media_id = movieID;
                          watchlistMoviePost.media_type = "movie";
                          RestApi api = new RestApi(ScrollingMovieDetailActivity.this);
                          Call<Movie> call = api.postUserWatchlist("account_id", session, watchlistMoviePost);
                          call.enqueue(new Callback<Movie>() {
                              @Override
                              public void onResponse(Call<Movie> call, Response<Movie> response) {
                                  if (response.code() == 201) {
                                      model = response.body();
                                      apiCalls.getWatchList();

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
                          Picasso.with(ScrollingMovieDetailActivity.this).load(R.drawable.watchlist_add_hdpi).into(watchlist);
                          favklik = "no";
                          WatchlistMoviePost watchlistMoviePost = new WatchlistMoviePost();
                          watchlistMoviePost.watchlist = false;
                          watchlistMoviePost.media_id = movieID;
                          watchlistMoviePost.media_type = "movie";
                          RestApi api = new RestApi(ScrollingMovieDetailActivity.this);
                          Call<Movie> call = api.postUserWatchlist("account_id", session, watchlistMoviePost);
                          call.enqueue(new Callback<Movie>() {
                              @Override
                              public void onResponse(Call<Movie> call, Response<Movie> response) {
                                  if (response.code() == 200) {
                                      model = response.body();
                                      apiCalls.getWatchList();
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
                  }else {
                      Toast.makeText(ScrollingMovieDetailActivity.this, "Please login to use this function", Toast.LENGTH_SHORT).show();
                  }
                }
            });
    }


    public void GetFavorite() {
        RestApi api6 = new RestApi(ScrollingMovieDetailActivity.this);
        Call<Movie> call12 = api6.getFavorites(movieID, session);
        call12.enqueue(new Callback<Movie>() {
            @Override
            public void onResponse(Call<Movie> call, Response<Movie> response) {
                model = response.body();
                if (model.favorite) {
                    Picasso.with(ScrollingMovieDetailActivity.this).load(R.drawable.favourites_full_hdpi).into(favorite);
                    favklik = "yes";
                } else {
                    Picasso.with(ScrollingMovieDetailActivity.this).load(R.drawable.favourites_empty_hdpi).into(favorite);
                    favklik = "no";
                }
            }

            @Override
            public void onFailure(Call<Movie> call, Throwable t) {
            }
        });
    }

    public void RatingListener() {
        rateit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Dialog rankDialog = new Dialog(ScrollingMovieDetailActivity.this, R.style.FullHeightDialog);
                rankDialog.setContentView(R.layout.dialograte);
                rankDialog.setCancelable(true);
                final RatingBar ratingBar = (RatingBar)rankDialog.findViewById(R.id.dialog_ratingbar);


                TextView text = (TextView) rankDialog.findViewById(R.id.rank_dialog_text1);
                text.setText(model.title);

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
                        Picasso.with(ScrollingMovieDetailActivity.this).load(R.drawable.rate_02_hdpi).centerInside().fit().into(ratingimg);
                        }else if (rate >= 7 && rate <9){
                        ratiingtxt.setText("" + rate);
                        Picasso.with(ScrollingMovieDetailActivity.this).load(R.drawable.rate_04_hdpi).centerInside().fit().into(ratingimg);
                        }else if (rate == 9){
                        ratiingtxt.setText("" + rate);
                        Picasso.with(ScrollingMovieDetailActivity.this).load(R.drawable.rate_06_hdpi).centerInside().fit().into(ratingimg);
                        }else if (rate == 10){
                        ratiingtxt.setText("" + rate);
                        Picasso.with(ScrollingMovieDetailActivity.this).load(R.drawable.rate_full_hdpi).centerInside().fit().into(ratingimg);
                        }
                        rated.value = rate;
                        rated.id = movieID;
                        RestApi api = new RestApi(ScrollingMovieDetailActivity.this);
                        Call<Movie> call = api.postUserRateing(movieID, session, rated);
                        call.enqueue(new Callback<Movie>() {
                            @Override
                            public void onResponse(Call<Movie> call, Response<Movie> response) {
                                pd1.show();
                                if (response.code() == 201) {
                                    model = response.body();
                                    ratedList.ratedMovies.add(rated);
                                    LogInPreferences.addRated(ratedList,ScrollingMovieDetailActivity.this);
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
                            public void onFailure(Call<Movie> call, Throwable t) {
                            }});


                        rankDialog.dismiss();
                    }
                });
                rankDialog.show();
            }
        });}

}