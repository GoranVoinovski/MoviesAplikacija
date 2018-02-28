package com.znaci.goran.moviesaplikacija.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.znaci.goran.moviesaplikacija.R;
import com.znaci.goran.moviesaplikacija.listeners.OnRowCastClickListener;
import com.znaci.goran.moviesaplikacija.listeners.OnRowMovieClickListener;
import com.znaci.goran.moviesaplikacija.models.Cast;
import com.znaci.goran.moviesaplikacija.models.Movie;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Goran on movie2/6/2018.
 */

public class RecyclerViewSimilarAdapter extends RecyclerView.Adapter<RecyclerViewSimilarAdapter.ViewHolder> {

   Context context;
    ArrayList<Movie> results = new ArrayList<>();
    OnRowMovieClickListener onRowMovieClickListener;

    public RecyclerViewSimilarAdapter(Context context, OnRowMovieClickListener _onRowMovieClickListener) {
        this.context = context;
        this.onRowMovieClickListener = _onRowMovieClickListener;
    }

    public void setItems(ArrayList<Movie> movielist){

        this.results = movielist;
        notifyDataSetChanged();
    }

    @Override
    public RecyclerViewSimilarAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        ViewHolder view = new ViewHolder (inflater.inflate(R.layout.filmcredits_recview_layout,parent,false));



        return view;

    }

    @Override
    public void onBindViewHolder(RecyclerViewSimilarAdapter.ViewHolder holder, final int position) {
        final Movie movie = results.get(position);
        String path = "http://image.tmdb.org/t/p/w185" + movie.poster_path;
        holder.nameMovie.setText("  " + movie.title);
        holder.yearMovie.setText(movie.release_date);
        Picasso.with(context).load(path).fit().into(holder.moviesImage);
        holder.layoutCast.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onRowMovieClickListener.onRowClick(movie,position);
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
        @BindView(R.id.movieName)
        TextView nameMovie;
        @BindView(R.id.movieYear)
        TextView yearMovie;
        @BindView(R.id.castLayout)
        RelativeLayout layoutCast;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
        }
    }
}
