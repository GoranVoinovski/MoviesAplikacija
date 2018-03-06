package com.znaci.goran.moviesaplikacija.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.znaci.goran.moviesaplikacija.R;
import com.znaci.goran.moviesaplikacija.adapters.RecyclerViewReviewsAdapter;
import com.znaci.goran.moviesaplikacija.api.RestApi;
import com.znaci.goran.moviesaplikacija.helpers.ApiCalls;
import com.znaci.goran.moviesaplikacija.models.ReviewsModel;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ReviewActivity extends AppCompatActivity {
    @BindView(R.id.reviewRV)
    RecyclerView rvReviews;
    @BindView(R.id.noreview)
    TextView noReviews;
    RecyclerViewReviewsAdapter rvImg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_review);
        ButterKnife.bind(this);
        noReviews.setVisibility(View.GONE);
        Intent intent = getIntent();
        if (intent.hasExtra("id")){

        int movieID = intent.getIntExtra("id",0);

                rvImg = new RecyclerViewReviewsAdapter(ReviewActivity.this);
                RestApi api3 = new RestApi(ReviewActivity.this);
                Call<ReviewsModel> call3 = api3.getMovieReviews(movieID);
                call3.enqueue(new Callback<ReviewsModel>() {
                    @Override
                    public void onResponse(Call<ReviewsModel> call, Response<ReviewsModel> response) {
                        if (response.code() == 200) {
                            ReviewsModel model3;
                            model3 = response.body();
                            if (model3.results.size() > 0){
                                rvImg.setItems(model3.results);
                                rvReviews.setHasFixedSize(true);
                                rvReviews.setLayoutManager(new LinearLayoutManager(ReviewActivity.this));
                                rvReviews.setAdapter(rvImg);
                            }else {
                             noReviews.setVisibility(View.VISIBLE);
                             noReviews.setText("No available reviews for this movie");
                            }
                        }
                    }
                    @Override
                    public void onFailure(Call<ReviewsModel> call, Throwable t) {
                    }});
            }
        }

    }

