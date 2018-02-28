package com.znaci.goran.moviesaplikacija.activities;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.znaci.goran.moviesaplikacija.R;
import com.znaci.goran.moviesaplikacija.api.RestApi;
import com.znaci.goran.moviesaplikacija.models.Movie;
import com.znaci.goran.moviesaplikacija.models.Shows;
import com.znaci.goran.moviesaplikacija.models.VideoModel;
import com.znaci.goran.moviesaplikacija.models.Videos;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ShowDetailActivity extends AppCompatActivity {
    @BindView(R.id.nameShow)
    TextView nameOfShow;
    @BindView(R.id.play)
    TextView playShow;
    @BindView(R.id.ShowVideo)
    ImageView videoOfShow;
    @BindView(R.id.firstair)
    TextView showAirDate;
    @BindView(R.id.numberofepisodes)
    TextView episodesOfShow;
    @BindView(R.id.status)
    TextView statusOfShow;
    @BindView(R.id.description)
    TextView descriptionOfShow;
    Shows model;
    VideoModel model2;
    int position = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_detail);

        ButterKnife.bind(this);
        Intent intent = getIntent();
        if (intent.hasExtra("showid")) {

            final int movieID = intent.getIntExtra("showid", 0);
            position = intent.getIntExtra("position", 0);
            RestApi api = new RestApi(this);
            Call<Shows> call = api.getShow(movieID,"credits");
            call.enqueue(new Callback<Shows>() {
                @Override
                public void onResponse(Call<Shows> call, Response<Shows> response) {
                    if (response.code() == 200) {

                        model = response.body();
                        nameOfShow.setText(model.name);
                        descriptionOfShow.setText(model.overview);
                        if (model.first_air_date != null){
                            showAirDate.setText("First air date: " + model.first_air_date);
                        }

                        if (model.last_air_date != null){
                            showAirDate.setText("First air date: " + model.first_air_date + " Last air date: " + model.last_air_date);
                        }

                        else {showAirDate.setText("Air dates not available");

                        }

                        if (model.number_of_episodes != 0){

                            episodesOfShow.setText("Episodes: " + model.number_of_episodes);
                        }

                        if (model.number_of_seasons != 0){
                            episodesOfShow.setText("Episodes: " + model.number_of_episodes + " Seasons: " + model.number_of_seasons);
                        }

                        if (model.poster_path != null){
                            String path = "http://image.tmdb.org/t/p/w185" + model.poster_path;
                            Picasso.with(ShowDetailActivity.this).load(path).centerInside().fit().into(videoOfShow);
                        }

                        RestApi api3 = new RestApi(ShowDetailActivity.this);
                        Call<VideoModel> call3 = api3.getShowVideo(movieID);
                        call3.enqueue(new Callback<VideoModel>() {
                            @Override
                            public void onResponse(Call<VideoModel> call, Response<VideoModel> response) {
                                if (response.code() == 200) {
                                    model2 = response.body();
                                    if (model2.results.size() == 0){}else {
                                        final Videos video = model2.results.get(0);
                                        playShow.setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View v) {
                                                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.youtube.com/watch?v=" + video.key)));
                                                Log.i("Video", "Video Playing....");
                                            }
                                        });}}}
                            @Override
                            public void onFailure(Call<VideoModel> call, Throwable t) {
                            }});
                    }
                }

                @Override
                public void onFailure(Call<Shows> call, Throwable t) {
                }});

           }

        }
    }
