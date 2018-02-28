package com.znaci.goran.moviesaplikacija.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.znaci.goran.moviesaplikacija.R;
import com.znaci.goran.moviesaplikacija.listeners.OnRowCastClickListener;
import com.znaci.goran.moviesaplikacija.listeners.OnRowGenreClickListener;
import com.znaci.goran.moviesaplikacija.models.Cast;
import com.znaci.goran.moviesaplikacija.models.Genre;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Goran on movie2/6/2018.
 */

public class RecyclerViewGenretAdapter extends RecyclerView.Adapter<RecyclerViewGenretAdapter.ViewHolder> {

   Context context;
    ArrayList<Genre> genres = new ArrayList<>();
    OnRowGenreClickListener onRowGenreClickListener;
    int firstposition;
    int prevposition;
    int c = 0;
    public RecyclerViewGenretAdapter(Context context, OnRowGenreClickListener _onRowGenreClickListener) {
        this.context = context;
        this.onRowGenreClickListener = _onRowGenreClickListener;
    }

    public void setItems(ArrayList<Genre> movielist){

        this.genres = movielist;
        notifyDataSetChanged();
    }

    @Override
    public RecyclerViewGenretAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        ViewHolder view = new ViewHolder (inflater.inflate(R.layout.genre_layout,parent,false));



        return view;

    }

    @Override
    public void onBindViewHolder(final RecyclerViewGenretAdapter.ViewHolder holder, final int position) {
        final Genre genre = genres.get(position);
        holder.genreName.setText(genre.name);
        holder.layoutCast.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onRowGenreClickListener.onRowClick(genre,position);
    }
});
    }

    @Override
    public int getItemCount() {
        return genres.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.genrename)
        TextView genreName;
        @BindView(R.id.genreClick)
        LinearLayout layoutCast;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
        }
    }
}
