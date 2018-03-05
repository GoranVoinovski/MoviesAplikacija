package com.znaci.goran.moviesaplikacija.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;
import com.znaci.goran.moviesaplikacija.R;
import com.znaci.goran.moviesaplikacija.listeners.OnRowImageClickListener;
import com.znaci.goran.moviesaplikacija.models.Images;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Goran on movie2/6/2018.
 */

public class RecyclerViewImagesAdapter extends RecyclerView.Adapter<RecyclerViewImagesAdapter.ViewHolder> {

    Context context;
    ArrayList<Images> images = new ArrayList<>();
    OnRowImageClickListener onRowImageClickListener;

    public RecyclerViewImagesAdapter(Context context,OnRowImageClickListener _onRowImageClickListener) {
        this.context = context;
        this.onRowImageClickListener = _onRowImageClickListener;
    }

    public void setItems(ArrayList<Images> movielist){

        this.images = movielist;
        notifyDataSetChanged();
    }

    @Override
    public RecyclerViewImagesAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        ViewHolder view = new ViewHolder (inflater.inflate(R.layout.images_layout,parent,false));



        return view;

    }

    @Override
    public void onBindViewHolder(RecyclerViewImagesAdapter.ViewHolder holder, final int position) {
        final Images image = images.get(position);

        String link = "http://image.tmdb.org/t/p/w1280" + image.file_path;
        Picasso.with(context).load(link).into(holder.img);


    holder.img.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            onRowImageClickListener.onRowClick(image,position);
        }
    });
    }

    @Override
    public int getItemCount() {
        return images.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
      @BindView(R.id.imageMovie)ImageView img;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
        }
    }
}
