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
import com.znaci.goran.moviesaplikacija.activities.PeopleActivity;
import com.znaci.goran.moviesaplikacija.listeners.OnRowPersonClickListener;
import com.znaci.goran.moviesaplikacija.models.Known_For;
import com.znaci.goran.moviesaplikacija.models.Movie;
import com.znaci.goran.moviesaplikacija.models.Person;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Goran on 2/8/2018.
 */

public class RecyclerViewPeopleAdapter extends RecyclerView.Adapter<RecyclerViewPeopleAdapter.ViewHolder> {

    Context context;
    ArrayList<Person> peoples;
    OnRowPersonClickListener onRowPersonClickListener;

    public RecyclerViewPeopleAdapter(Context context, OnRowPersonClickListener _onRowPersonClickListener) {
        this.context = context;
        this.onRowPersonClickListener = _onRowPersonClickListener;
    }

    public void setItems(ArrayList<Person> movielist) {

        this.peoples = movielist;
        notifyDataSetChanged();
    }

    @Override
    public RecyclerViewPeopleAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        ViewHolder view = new ViewHolder(inflater.inflate(R.layout.people_layout, parent, false));

        return view;
    }

    @Override
    public void onBindViewHolder(RecyclerViewPeopleAdapter.ViewHolder holder, final int position) {
        final Person people = peoples.get(position);

        if (people.profile_path == null){
            Picasso.with(context).load(R.drawable.noimage).fit().into(holder.pImage);
        }else {
            String path = "http://image.tmdb.org/t/p/w185" + people.profile_path;
            Picasso.with(context).load(path).centerInside().fit().into(holder.pImage);
        }

        holder.pNumber.setText(position + 1 + "");
        holder.pName.setText(people.name);

        holder.pFilms.setText("");
        for (Movie movies : people.known_for) {
            if (movies.title == null){
                holder.pFilms.setText(holder.pFilms.getText().toString());
            }else {
                holder.pFilms.setText(holder.pFilms.getText().toString() + movies.title + ", ");
            }

        }


        holder.pLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onRowPersonClickListener.onRowClick(people, position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return peoples.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.peopleImage)
        ImageView pImage;
        @BindView(R.id.number)
        TextView pNumber;
        @BindView(R.id.Peoplename)
        TextView pName;
        @BindView(R.id.PeopleFilmography)
        TextView pFilms;
        @BindView(R.id.personLayout)
        RelativeLayout pLayout;


        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
