package com.znaci.goran.moviesaplikacija.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.znaci.goran.moviesaplikacija.R;
import com.znaci.goran.moviesaplikacija.listeners.OnRowMovieClickListener;
import com.znaci.goran.moviesaplikacija.listeners.OnRowShowClickListener;
import com.znaci.goran.moviesaplikacija.models.Movie;
import com.znaci.goran.moviesaplikacija.models.Rated;
import com.znaci.goran.moviesaplikacija.models.RatedList;
import com.znaci.goran.moviesaplikacija.models.Shows;
import com.znaci.goran.moviesaplikacija.preferencesManager.LogInPreferences;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Goran on movie2/6/2018.
 */

public class RecyclerViewPopularShowsAdapter extends RecyclerView.Adapter<RecyclerViewPopularShowsAdapter.ViewHolder> {

   Context context;
    ArrayList<Shows> results = new ArrayList<>();
    OnRowShowClickListener onRowMovieClickListener;
    RatedList ratedList;

    public RecyclerViewPopularShowsAdapter(Context context, OnRowShowClickListener _onRowMovieClickListener) {
        this.context = context;
        this.onRowMovieClickListener = _onRowMovieClickListener;
    }

    public void setItems(ArrayList<Shows> movielist){

        this.results = movielist;
        notifyDataSetChanged();
    }

    @Override
    public RecyclerViewPopularShowsAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        ViewHolder view = new ViewHolder (inflater.inflate(R.layout.film_recview_layout,parent,false));



        return view;

    }

    @Override
    public void onBindViewHolder(RecyclerViewPopularShowsAdapter.ViewHolder holder, final int position) {
        final Shows show = results.get(position);
        ratedList = LogInPreferences.getRatedShow(context);
        if (ratedList == null){
            ratedList = new RatedList();
            if (ratedList.ratedMovies == null){
                ratedList.ratedMovies = new ArrayList<>();
            }}
        holder.moviesrat.setBackgroundResource(R.drawable.rate_empty_hdpi);
        holder.ratingMovie.setTextColor(context.getResources().getColor(R.color.white));
        holder.ratingMovie.setText("" + show.vote_average);
        for (Rated rated:ratedList.ratedMovies){
            if (show.id == rated.id){

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
        String path = "http://image.tmdb.org/t/p/w185" + show.poster_path;
        holder.nameMovie.setText("  " + show.original_name);
        Picasso.with(context).load(path).fit().into(holder.moviesImage);
        holder.moviesImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onRowMovieClickListener.onRowClick(show,position);
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

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
        }
    }
}
