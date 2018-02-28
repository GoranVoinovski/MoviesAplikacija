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
import com.znaci.goran.moviesaplikacija.models.Cast;
import com.znaci.goran.moviesaplikacija.models.CreditsModel;
import com.znaci.goran.moviesaplikacija.models.Crew;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Goran on movie2/6/2018.
 */

public class RecyclerViewFullCastAdapter extends RecyclerView.Adapter<RecyclerViewFullCastAdapter.ViewHolder> {

    Context context;
    CreditsModel model = new CreditsModel();
    OnRowCastClickListener onRowCastClickListener;

    public RecyclerViewFullCastAdapter(Context context, OnRowCastClickListener _onRowCastClickListener) {
        this.context = context;
        this.onRowCastClickListener = _onRowCastClickListener;
    }

    public void setItems(ArrayList<Cast> cast){

        this.model.cast = cast;
        notifyDataSetChanged();
    }

    @Override
    public RecyclerViewFullCastAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        ViewHolder view = new ViewHolder (inflater.inflate(R.layout.fullcastlayout,parent,false));



        return view;

    }

    @Override
    public void onBindViewHolder(RecyclerViewFullCastAdapter.ViewHolder holder, final int position) {
        final Cast cast = model.cast.get(position);


        if (cast.profile_path == null || cast.profile_path.isEmpty()){

            Picasso.with(context).load(R.drawable.noimage).fit().into(holder.castimg);

        }else {

            String path = "http://image.tmdb.org/t/p/w185" + cast.profile_path;
            Picasso.with(context).load(path).fit().into(holder.castimg);
        }
        holder.characterCast.setText("As " + cast.character);
        holder.nameCast.setText(cast.name);
        holder.layoutCast.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onRowCastClickListener.onRowClick(cast,position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return model.cast.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.castimage)
        ImageView castimg;
        @BindView(R.id.fullcastname)
        TextView nameCast;
        @BindView(R.id.castcharacter)
        TextView characterCast;
        @BindView(R.id.castlayout)
        LinearLayout layoutCast;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
        }
    }
}
