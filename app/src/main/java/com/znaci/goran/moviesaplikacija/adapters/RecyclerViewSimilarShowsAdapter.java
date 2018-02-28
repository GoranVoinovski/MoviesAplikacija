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
import com.znaci.goran.moviesaplikacija.listeners.OnRowMovieClickListener;
import com.znaci.goran.moviesaplikacija.listeners.OnRowShowClickListener;
import com.znaci.goran.moviesaplikacija.models.Movie;
import com.znaci.goran.moviesaplikacija.models.Shows;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Goran on movie2/6/2018.
 */

public class RecyclerViewSimilarShowsAdapter extends RecyclerView.Adapter<RecyclerViewSimilarShowsAdapter.ViewHolder> {

   Context context;
    ArrayList<Shows> results = new ArrayList<>();
    OnRowShowClickListener onRowShowClickListener;

    public RecyclerViewSimilarShowsAdapter(Context context, OnRowShowClickListener _onRowShowClickListener) {
        this.context = context;
        this.onRowShowClickListener = _onRowShowClickListener;
    }

    public void setItems(ArrayList<Shows> movielist){

        this.results = movielist;
        notifyDataSetChanged();
    }

    @Override
    public RecyclerViewSimilarShowsAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        ViewHolder view = new ViewHolder (inflater.inflate(R.layout.filmcredits_recview_layout,parent,false));



        return view;

    }

    @Override
    public void onBindViewHolder(RecyclerViewSimilarShowsAdapter.ViewHolder holder, final int position) {
        final Shows shows = results.get(position);
        String path = "http://image.tmdb.org/t/p/w185" + shows.poster_path;
        holder.nameMovie.setText("  " + shows.name);
        holder.yearMovie.setText(shows.first_air_date);
        Picasso.with(context).load(path).fit().into(holder.moviesImage);
        holder.layoutCast.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onRowShowClickListener.onRowClick(shows,position);
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
