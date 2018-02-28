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

public class RecyclerViewCastAdapter extends RecyclerView.Adapter<RecyclerViewCastAdapter.ViewHolder> {

   Context context;
    ArrayList<Cast> results = new ArrayList<>();
    OnRowCastClickListener onRowCastClickListener;

    public RecyclerViewCastAdapter(Context context,OnRowCastClickListener _onRowCastClickListener) {
        this.context = context;
        this.onRowCastClickListener = _onRowCastClickListener;
    }

    public void setItems(ArrayList<Cast> movielist){

        this.results = movielist;
        notifyDataSetChanged();
    }

    @Override
    public RecyclerViewCastAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        ViewHolder view = new ViewHolder (inflater.inflate(R.layout.filmcredits_recview_layout,parent,false));



        return view;

    }

    @Override
    public void onBindViewHolder(RecyclerViewCastAdapter.ViewHolder holder, final int position) {
        final Cast cast = results.get(position);


        if (cast.poster_path == null || cast.poster_path.isEmpty()){

            Picasso.with(context).load(R.drawable.noimage).fit().into(holder.moviesImage);

        }else {

            String path = "http://image.tmdb.org/t/p/w185" + cast.poster_path;
            Picasso.with(context).load(path).fit().into(holder.moviesImage);
        }
        holder.nameMovie.setText("  " + cast.original_title);
        holder.nameMovie.setText(cast.original_title);
        holder.yearMovie.setText(cast.release_date);
        holder.layoutCast.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onRowCastClickListener.onRowClick(cast,position);
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
