package com.znaci.goran.moviesaplikacija.adapters;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Handler;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;
import com.znaci.goran.moviesaplikacija.R;
import com.znaci.goran.moviesaplikacija.activities.ExploreActivity;
import com.znaci.goran.moviesaplikacija.activities.ScrollingMovieDetailActivity;
import com.znaci.goran.moviesaplikacija.api.RestApi;
import com.znaci.goran.moviesaplikacija.helpers.ApiCalls;
import com.znaci.goran.moviesaplikacija.listeners.OnRowMovieClickListener;
import com.znaci.goran.moviesaplikacija.models.FavoriteModel;
import com.znaci.goran.moviesaplikacija.models.FavoriteMoviePost;
import com.znaci.goran.moviesaplikacija.models.Movie;
import com.znaci.goran.moviesaplikacija.models.MovieModel;
import com.znaci.goran.moviesaplikacija.models.Rated;
import com.znaci.goran.moviesaplikacija.models.RatedList;
import com.znaci.goran.moviesaplikacija.models.WatchModel;
import com.znaci.goran.moviesaplikacija.preferencesManager.LogInPreferences;

import java.util.ArrayList;


import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Goran on movie2/6/2018.
 */

public class RecyclerViewPopularAdapter extends RecyclerView.Adapter<RecyclerViewPopularAdapter.ViewHolder> {

   Context context;
    ArrayList<Movie> results = new ArrayList<>();
    OnRowMovieClickListener onRowMovieClickListener;
    FavoriteModel model;
    WatchModel watchModel;
    RatedList ratedList;


    public RecyclerViewPopularAdapter (Context context,OnRowMovieClickListener _onRowMovieClickListener) {
        this.context = context;
        this.onRowMovieClickListener = _onRowMovieClickListener;
    }

    public void setItems(ArrayList<Movie> movielist){

        this.results = movielist;
        notifyDataSetChanged();
    }

    @Override
    public RecyclerViewPopularAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        ViewHolder view = new ViewHolder (inflater.inflate(R.layout.film_recview_layout,parent,false));



        return view;

    }

    @Override
    public void onBindViewHolder(final RecyclerViewPopularAdapter.ViewHolder holder, final int position) {
        final Movie movie = results.get(position);
        model = LogInPreferences.getFavoriteList(context);
        watchModel = LogInPreferences.getWtchList(context);
        holder.fav.setBackgroundResource(R.drawable.favourites_empty_hdpi);
        holder.wtch.setBackgroundResource(R.drawable.watchlist_add_hdpi);
        for (Integer integer:model.favorites){
            if (integer == movie.id){
                holder.fav.setBackgroundResource(R.drawable.favourites_full_hdpi);
            }

        }

        for (Integer integer:watchModel.favorites){
            if (integer == movie.id){
                holder.wtch.setBackgroundResource(R.drawable.watchlist_remove_hdpi);
            }

        }

        ratedList = LogInPreferences.getRated(context);
        if (ratedList == null){
            ratedList = new RatedList();
            if (ratedList.ratedMovies == null){
                ratedList.ratedMovies = new ArrayList<>();
            }}
        holder.moviesrat.setBackgroundResource(R.drawable.rate_empty_hdpi);
        holder.ratingMovie.setTextColor(context.getResources().getColor(R.color.white));
        holder.ratingMovie.setText("" + movie.vote_average);
            for (Rated rated:ratedList.ratedMovies){
                if (movie.id == rated.id){

                    if (rated.value <= 6){
                        holder.ratingMovie.setTextColor(context.getResources().getColor(R.color.zoltarejting));
                        holder.ratingMovie.setText("" + rated.value);
                        holder.moviesrat.setBackgroundResource(R.drawable.rate_02_hdpi);
                    }else if (rated.value >= 7 && rated.value <9){
                        holder.ratingMovie.setTextColor(context.getResources().getColor(R.color.zoltarejting));
                        holder.ratingMovie.setText("" + rated.value);
                        holder.moviesrat.setBackgroundResource(R.drawable.rate_04_hdpi);
                    }else if (rated.value >=9 && rated.value <10){
                        holder.ratingMovie.setTextColor(context.getResources().getColor(R.color.zoltarejting));
                        holder.ratingMovie.setText("" + rated.value);
                        holder.moviesrat.setBackgroundResource(R.drawable.rate_06_hdpi);
                    }else if (rated.value == 10){
                        holder.ratingMovie.setTextColor(context.getResources().getColor(R.color.zoltarejting));
                        holder.ratingMovie.setText("" + rated.value);
                        holder.moviesrat.setBackgroundResource(R.drawable.rate_08_hdpi);
                    }
                }

        }


        String path = "http://image.tmdb.org/t/p/w185" + movie.poster_path;
        holder.nameMovie.setText("  " + movie.title);
        holder.movieswatch.setText(movie.release_date);
        Picasso.with(context).load(path).fit().into(holder.moviesImage);
        holder.moviesImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onRowMovieClickListener.onRowClick(movie,position);
            }
        });

        holder.fav.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onRowMovieClickListener.onRowFavClick(movie,position,holder.fav);

            }
        });

        holder.wtch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onRowMovieClickListener.onRowWatchClick(movie,position,holder.wtch);
            }
        });


    }

    @Override
    public int getItemCount() {
        return results.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.imageMovie)
        ImageView moviesImage;
        @BindView(R.id.imagerating)
        TextView moviesrat;
        @BindView(R.id.imagewatch)
        TextView movieswatch;
        @BindView(R.id.movieName)
        TextView nameMovie;
        @BindView(R.id.movieRating)
        TextView ratingMovie;
        @BindView(R.id.imagefav)
        TextView fav;
        @BindView(R.id.imagewtch)
        TextView wtch;
        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
        }
    }
}
