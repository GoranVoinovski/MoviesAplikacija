package com.znaci.goran.moviesaplikacija.api;

import com.znaci.goran.moviesaplikacija.models.CreditsModel;
import com.znaci.goran.moviesaplikacija.models.FavoriteMoviePost;
import com.znaci.goran.moviesaplikacija.models.GenresModel;
import com.znaci.goran.moviesaplikacija.models.Movie;
import com.znaci.goran.moviesaplikacija.models.MovieModel;
import com.znaci.goran.moviesaplikacija.models.Person;
import com.znaci.goran.moviesaplikacija.models.PersonModel;
import com.znaci.goran.moviesaplikacija.models.Rated;
import com.znaci.goran.moviesaplikacija.models.Shows;
import com.znaci.goran.moviesaplikacija.models.ShowsModel;
import com.znaci.goran.moviesaplikacija.models.User;
import com.znaci.goran.moviesaplikacija.models.VideoModel;
import com.znaci.goran.moviesaplikacija.models.WatchlistMoviePost;

import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;


/**
 * Created by Goran on 1/16/2018.
 */

public interface ApiService {


    //--------------------------------------------Movies------------------------------------------------------------------------------

    @GET("discover/movie?" + ApiConstants.ApiKey)
    Call<MovieModel> getMoviesByGenre(@Query("with_genres") String genre,@Query("page") int page);

    @GET("discover/movie?" + ApiConstants.ApiKey)
    Call<MovieModel> getMoviesByYear(@Query("primary_release_year") int year,@Query("page") int page);

    @GET("movie/{popular}?" + ApiConstants.ApiKey)
    Call<MovieModel> getMovies(@Path("popular")String popular,@Query("page") int page);

    @GET("movie/{movie_id}?" + ApiConstants.ApiKey)
    Call<Movie> getMovie(@Path("movie_id") int link,@Query("append_to_response")String credits);

    @GET("movie/{movie_id}/credits?" + ApiConstants.ApiKey)
    Call<CreditsModel> getMovieCredits(@Path("movie_id") int link);

    @GET("genre/movie/list?" + ApiConstants.ApiKey)
    Call<GenresModel> getGenres();

    @GET("movie/{movie_id}/" + "similar?" + ApiConstants.ApiKey)
    Call<MovieModel> getSimilar(@Path("movie_id") int link);

    @GET("movie/{movie_id}/" + "videos?" + ApiConstants.ApiKey)
    Call<VideoModel> getVideo(@Path("movie_id") int link);

    @GET("search/movie?" + ApiConstants.ApiKey)
    Call<MovieModel> getSearchMovie(@Query("query") String query);

    //--------------------------------------------Shows------------------------------------------------------------------------------
    @GET("genre/tv/list?" + ApiConstants.ApiKey)
    Call<GenresModel> getTVGenres();

    @GET("discover/tv?" + ApiConstants.ApiKey)
    Call<ShowsModel> getTVByGenre(@Query("with_genres") String genre,@Query("page") int page);

    @GET("discover/tv?" + ApiConstants.ApiKey)
    Call<ShowsModel> getTVByYear(@Query("primary_release_year") int year,@Query("page") int page);

    @GET("tv/{tv_id}?" + ApiConstants.ApiKey)
    Call<Shows> getShows(@Path("tv_id") int link ,@Query("append_to_response")String credits);

    @GET("tv/{shows}?" + ApiConstants.ApiKey)
    Call<ShowsModel> getShows(@Path("shows")String shows,@Query("page") int page);

    @GET("search/tv?" + ApiConstants.ApiKey)
    Call<ShowsModel> getSearchShows(@Query("query") String query);

    @GET("tv/{tv_id}/" + "videos?" + ApiConstants.ApiKey)
    Call<VideoModel> getShowVideo(@Path("tv_id") int link);

    @GET("tv/{tv_id}/" + "similar?" + ApiConstants.ApiKey)
    Call<ShowsModel> getSimilarShows(@Path("tv_id") int link);

    @GET("tv/{tv_id}/credits?" + ApiConstants.ApiKey)
    Call<CreditsModel> getShowCredits(@Path("tv_id") int link);

    //--------------------------------------------Person------------------------------------------------------------------------------

    @GET("person/popular?" + ApiConstants.ApiKey)
    Call<PersonModel> getPopularPeople(@Query("page") int page);

    @GET("person/{person_id}/" + "movie_credits?"  + ApiConstants.ApiKey)
    Call<CreditsModel> getPersonCredits(@Path("person_id") int link);

    @GET("person/{person_id}?" + ApiConstants.ApiKey)
    Call<Person> getPerson(@Path("person_id") int link);

    @GET("search/person?" + ApiConstants.ApiKey)
    Call<PersonModel> getSearchPerson(@Query("query") String query);

    //--------------------------------------------Login------------------------------------------------------------------------------

    @GET("authentication/{guest_session}/new?" + ApiConstants.ApiKey)
    Call<User> getGuestUser(@Path("guest_session")String guest);

    @GET("authentication/token/validate_with_login?" + ApiConstants.ApiKey)
    Call<User> getValidateUser(@Query("username") String username,@Query("password") String password,@Query("request_token")String request_token);

    @GET("authentication/session/new?" + ApiConstants.ApiKey)
    Call<User> getUserSession(@Query("request_token") String reques_token);

    @GET("account?" + ApiConstants.ApiKey)
    Call<User> getUserDetails(@Query("session_id") String session_id);

    //--------------------------------------------Favorites------------------------------------------------------------------------------

    @GET("account/{account_id}/favorite/movies?" + ApiConstants.ApiKey)
    Call<MovieModel> getUserFavorites(@Path("account_id") String account_id,@Query("session_id") String session_id);

    @GET("movie/{movie_id}/" + "account_states?" + ApiConstants.ApiKey)
    Call<Movie> getFavorites(@Path("movie_id") int link,@Query("session_id")String session_id);

    @POST("account/{account_id}/favorite?" + ApiConstants.ApiKey)
    Call<Movie> postUserFavorites(@Path("account_id") String account_id, @Query("session_id") String session_id, @Header("json/application") String json, @Body FavoriteMoviePost body);

    @POST("account/{account_id}/favorite?" + ApiConstants.ApiKey)
    Call<Shows> postUserShowFavorites(@Path("account_id") String account_id, @Query("session_id") String session_id, @Header("json/application") String json, @Body FavoriteMoviePost body);

    @GET("account/{account_id}/favorite/tv?" + ApiConstants.ApiKey)
    Call<ShowsModel> getUserShowsFavorites(@Path("account_id") String account_id,@Query("session_id") String session_id);

    @GET("tv/{tv_id}/" + "account_states?" + ApiConstants.ApiKey)
    Call<Shows> getShowFavorite(@Path("tv_id") int link,@Query("session_id")String session_id);

    //--------------------------------------------Watchlist------------------------------------------------------------------------------

    @GET("account/{account_id}/watchlist/movies?" + ApiConstants.ApiKey)
    Call<MovieModel> getUserWatchlist(@Path("account_id") String account_id,@Query("session_id") String session_id);

    @GET("movie/{movie_id}/" + "account_states?" + ApiConstants.ApiKey)
    Call<Movie> getWatchlist(@Path("movie_id") int link,@Query("session_id")String session_id);

    @POST("account/{account_id}/watchlist?" + ApiConstants.ApiKey)
    Call<Movie> postUserWatchlist(@Path("account_id") String account_id, @Query("session_id") String session_id, @Header("json/application") String json, @Body WatchlistMoviePost body);

    @POST("account/{account_id}/watchlist?" + ApiConstants.ApiKey)
    Call<Shows> postUserShowWatchlist(@Path("account_id") String account_id, @Query("session_id") String session_id, @Header("json/application") String json, @Body WatchlistMoviePost body);

    @GET("account/{account_id}/watchlist/tv?" + ApiConstants.ApiKey)
    Call<ShowsModel> getUserShowsWatchlist(@Path("account_id") String account_id,@Query("session_id") String session_id);

    //--------------------------------------------Rated------------------------------------------------------------------------------

    @POST("movie/{movie_id}/rating?" + ApiConstants.ApiKey)
    Call<Movie> postUserRating(@Path("movie_id") int account_id, @Query("session_id") String session_id, @Header("json/application") String json, @Body Rated body);

    @POST("tv/{tv_id}/rating?" + ApiConstants.ApiKey)
    Call<Shows> postUserShowRating(@Path("tv_id") int account_id, @Query("session_id") String session_id, @Header("json/application") String json, @Body Rated body);

    @GET("account/{account_id}/rated/movies?" + ApiConstants.ApiKey)
    Call<MovieModel> getUserRated(@Path("account_id") String account_id,@Query("session_id") String session_id);

    @GET("account/{account_id}/rated/tv?" + ApiConstants.ApiKey)
    Call<ShowsModel> getUserRatedShows(@Path("account_id") String account_id,@Query("session_id") String session_id);
}
