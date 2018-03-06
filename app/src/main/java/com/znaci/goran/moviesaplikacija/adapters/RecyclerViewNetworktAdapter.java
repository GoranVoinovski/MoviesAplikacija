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
import com.znaci.goran.moviesaplikacija.listeners.OnRowNetworkClickListener;
import com.znaci.goran.moviesaplikacija.models.Genre;
import com.znaci.goran.moviesaplikacija.models.Network;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Goran on movie2/6/2018.
 */

public class RecyclerViewNetworktAdapter extends RecyclerView.Adapter<RecyclerViewNetworktAdapter.ViewHolder> {

   Context context;
    ArrayList<Network> networks = new ArrayList<>();
    OnRowNetworkClickListener onRowNetworkClickListener;

    public RecyclerViewNetworktAdapter(Context context, OnRowNetworkClickListener _onRowNetworkClickListener) {
        this.context = context;
        this.onRowNetworkClickListener = _onRowNetworkClickListener;
    }

    public void setItems(ArrayList<Network> net){

        this.networks = net;
        notifyDataSetChanged();
    }

    @Override
    public RecyclerViewNetworktAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        ViewHolder view = new ViewHolder (inflater.inflate(R.layout.genre_layout,parent,false));



        return view;

    }

    @Override
    public void onBindViewHolder(final RecyclerViewNetworktAdapter.ViewHolder holder, final int position) {
        final Network network = networks.get(position);
        holder.genreName.setText(network.name);
        holder.layoutCast.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onRowNetworkClickListener.onRowClick(network,position);
    }
});
    }

    @Override
    public int getItemCount() {
        return networks.size();
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
