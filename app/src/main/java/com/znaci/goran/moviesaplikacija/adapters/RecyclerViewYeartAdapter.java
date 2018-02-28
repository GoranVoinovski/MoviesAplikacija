package com.znaci.goran.moviesaplikacija.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.znaci.goran.moviesaplikacija.R;
import com.znaci.goran.moviesaplikacija.listeners.OnRowGenreClickListener;
import com.znaci.goran.moviesaplikacija.listeners.OnRowYearClickListener;
import com.znaci.goran.moviesaplikacija.models.Genre;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Goran on movie2/6/2018.
 */

public class RecyclerViewYeartAdapter extends RecyclerView.Adapter<RecyclerViewYeartAdapter.ViewHolder> {

   Context context;
    ArrayList<Integer> year = new ArrayList<>();
    OnRowYearClickListener onRowYearClickListener;

    public RecyclerViewYeartAdapter(Context context, OnRowYearClickListener _onRowYearClickListener) {
        this.context = context;
        this.onRowYearClickListener = _onRowYearClickListener;
    }

    public void setItems(ArrayList<Integer> movielist){

        this.year = movielist;
        notifyDataSetChanged();
    }

    @Override
    public RecyclerViewYeartAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        ViewHolder view = new ViewHolder (inflater.inflate(R.layout.genre_layout,parent,false));



        return view;

    }

    @Override
    public void onBindViewHolder(RecyclerViewYeartAdapter.ViewHolder holder, final int position) {
        final Integer integer = year.get(position);


        holder.genreName.setText(integer + "");
        holder.layoutCast.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onRowYearClickListener.onRowClick(integer,position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return year.size();
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
