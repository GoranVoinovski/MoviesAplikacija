package com.znaci.goran.moviesaplikacija.api;

import android.content.Context;

import com.znaci.goran.moviesaplikacija.interceptor.LoggingInterceptor;
import com.znaci.goran.moviesaplikacija.models.CreditsModel;
import com.znaci.goran.moviesaplikacija.models.FavoriteMoviePost;
import com.znaci.goran.moviesaplikacija.models.GenresModel;
import com.znaci.goran.moviesaplikacija.models.ImageModel;
import com.znaci.goran.moviesaplikacija.models.Movie;
import com.znaci.goran.moviesaplikacija.models.MovieModel;
import com.znaci.goran.moviesaplikacija.models.Person;
import com.znaci.goran.moviesaplikacija.models.PersonModel;
import com.znaci.goran.moviesaplikacija.models.Rated;
import com.znaci.goran.moviesaplikacija.models.ReviewsModel;
import com.znaci.goran.moviesaplikacija.models.Shows;
import com.znaci.goran.moviesaplikacija.models.ShowsModel;
import com.znaci.goran.moviesaplikacija.models.User;
import com.znaci.goran.moviesaplikacija.models.VideoModel;
import com.znaci.goran.moviesaplikacija.models.WatchlistMoviePost;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by Goran on 1/16/2018.
 */

public class RestApi {
    Context activity;

    public static final int requste_max_time_in_seconds = 20;

    public RestApi(Context activity) {
        this.activity = activity;
    }

    public Retrofit getRetrofitInstanceO(){

        OkHttpClient client = new OkHttpClient.Builder().addInterceptor(new LoggingInterceptor()).readTimeout(requste_max_time_in_seconds, TimeUnit.SECONDS)
        .connectTimeout(requste_max_time_in_seconds, TimeUnit.SECONDS).build();


        return new Retrofit.Builder().baseUrl(ApiConstants.baseURL).addConverterFactory(GsonConverterFactory.create())
                .client(client).build();
    }



    public ApiService request(){return  getRetrofitInstanceO().create(ApiService.class);}

    //--------------------------------------------Movies------------------------------------------------------------------------------

    public Call<MovieModel> getMoviesByGenre(String id,int page){return request().getMoviesByGenre(id,page);}

    public Call<MovieModel> getMoviesByYear(int id,int page){return request().getMoviesByYear(id,page);}

    public Call<MovieModel> getMovies(String popular,int page){return request().getMovies(popular,page);}

    public Call<VideoModel> getVideo(int id){return request().getVideo(id);}

    public Call<Movie> getMovie(int id,String append_to_response){return request().getMovie(id,append_to_response);}

    public Call<MovieModel> getSearchMovie(String query){return request().getSearchMovie(query);}

    public Call<GenresModel> getGenres(){return request().getGenres();}

    public Call<CreditsModel> getMovieCredits(int id){return request().getMovieCredits(id);}

    public Call<MovieModel> getSimilar(int id){return request().getSimilar(id);}

    public Call<ImageModel> getMovieImages(int id){return request().getMovieImages(id);}

    public Call<ReviewsModel> getMovieReviews(int id){return request().getMovieReviews(id);}
    //--------------------------------------------Shows------------------------------------------------------------------------------

    public Call<ShowsModel> getTVByGenre(String id,int page){return request().getTVByGenre(id,page);}

    public Call<ShowsModel> getTVByYear(int id,int page){return request().getTVByYear(id,page);}

    public Call<ShowsModel> getTVByNetwork(int id,int page){return request().getTVByNetwork(id,page);}

    public Call<Shows> getShow(int id,String append_to_response){return request().getShows(id,append_to_response);}

    public Call<ShowsModel> getShows(String shows,int page){return request().getShows(shows,page);}

    public Call<VideoModel> getShowVideo(int id){return request().getShowVideo(id);}

    public Call<ShowsModel> getSearchShows(String query){return request().getSearchShows(query);}

    public Call<ShowsModel> getSimilarShows(int id){return request().getSimilarShows(id);}

    public Call<CreditsModel> getShowCredits(int id){return request().getShowCredits(id);}

    public Call<GenresModel> getTVGenres(){return request().getTVGenres();}

    public Call<ImageModel> getShowsImages(int id){return request().getShowsImages(id);}

    //--------------------------------------------Person------------------------------------------------------------------------------

    public Call<PersonModel> getPopularPeople(int page){return request().getPopularPeople(page);}

    public Call<Person> getPerson(int id){return request().getPerson(id);}

    public Call<PersonModel> getSearchPerson(String query){return request().getSearchPerson(query);}

    public Call<CreditsModel> getPersonCredits(int id){return request().getPersonCredits(id);}

    //--------------------------------------------Login------------------------------------------------------------------------------

    public Call<User> getGuestUser(String guest){return request().getGuestUser(guest);}

    public Call<User> getValidateUser(String username,String password,String request_token){return request().getValidateUser(username,password,request_token);}

    public Call<User> getUserSession(String session){return request().getUserSession(session);}

    public Call<User> getUserDetail(String session){return request().getUserDetails(session);}

    //--------------------------------------------Favorites------------------------------------------------------------------------------

    public Call<MovieModel> getUserFavorites(String account_id,String session_id){return request().getUserFavorites(account_id,session_id);}

    public Call<Movie> getFavorites(int id,String session_id){return request().getFavorites(id,session_id);}

    public Call<Movie> postUserFavorites(String account_id, String session_id, FavoriteMoviePost body){return request().postUserFavorites(account_id,session_id,body);}

    public Call<Shows> postUserShowFavorites(String account_id, String session_id, FavoriteMoviePost body){return request().postUserShowFavorites(account_id,session_id,body);}

    public Call<Shows> getShowFavorite(int id,String session_id){return request().getShowFavorite(id,session_id);}

    public Call<ShowsModel> getUserShowsFavorites(String account_id,String session_id){return request().getUserShowsFavorites(account_id,session_id);}

    //--------------------------------------------Watchlist------------------------------------------------------------------------------

    public Call<MovieModel> getUserWatchlist(String account_id,String session_id){return request().getUserWatchlist(account_id,session_id);}

    public Call<Movie> getWatchlist(int id,String session_id){return request().getWatchlist(id,session_id);}

    public Call<Movie> postUserWatchlist(String account_id, String session_id, WatchlistMoviePost body){return request().postUserWatchlist(account_id,session_id,body);}

    public Call<Shows> postUserShowWatchlist(String account_id, String session_id, WatchlistMoviePost body){return request().postUserShowWatchlist(account_id,session_id,body);}

    public Call<ShowsModel> getUserShowsWatchlist(String account_id,String session_id){return request().getUserShowsWatchlist(account_id,session_id);}

    //--------------------------------------------Rated------------------------------------------------------------------------------

    public Call<Movie> postUserRateing(int movie_id, String session_id, Rated body){return request().postUserRating(movie_id,session_id,body);}

    public Call<Shows> postUserShowRateing(int movie_id, String session_id, String json, Rated body){return request().postUserShowRating(movie_id,session_id,body);}

    public Call<ShowsModel> getUserRatedShows(String account_id,String session_id){return request().getUserRatedShows(account_id,session_id);}

    public Call<MovieModel> getUserRated(String account_id,String session_id){return request().getUserRated(account_id,session_id);}
}
