package com.znaci.goran.moviesaplikacija.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.znaci.goran.moviesaplikacija.R;
import com.znaci.goran.moviesaplikacija.listeners.OnRowImageClickListener;
import com.znaci.goran.moviesaplikacija.models.Images;
import com.znaci.goran.moviesaplikacija.models.Reviews;
import com.znaci.goran.moviesaplikacija.models.ReviewsModel;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Goran on movie2/6/2018.
 */

public class RecyclerViewReviewsAdapter extends RecyclerView.Adapter<RecyclerViewReviewsAdapter.ViewHolder> {

    Context context;
    ReviewsModel reviewsModel = new ReviewsModel();
    OnRowImageClickListener onRowImageClickListener;

    public RecyclerViewReviewsAdapter(Context context) {
        this.context = context;
    }

    public void setItems(ArrayList<Reviews> reviews){

        this.reviewsModel.results = reviews;
        notifyDataSetChanged();
    }

    @Override
    public RecyclerViewReviewsAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        ViewHolder view = new ViewHolder (inflater.inflate(R.layout.reviews_layout,parent,false));



        return view;

    }

    @Override
    public void onBindViewHolder(RecyclerViewReviewsAdapter.ViewHolder holder, final int position) {
        final Reviews reviews = reviewsModel.results.get(position);

        holder.reviewer.setText(reviews.author);
        holder.review.setText(reviews.content);
    }

    @Override
    public int getItemCount() {
        return reviewsModel.results.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
      @BindView(R.id.reviewer)TextView reviewer;
      @BindView(R.id.review)TextView review;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
        }
    }
}
