package com.znaci.goran.moviesaplikacija.activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.Toast;

import com.znaci.goran.moviesaplikacija.R;
import com.znaci.goran.moviesaplikacija.adapters.RecyclerViewFullCastAdapter;
import com.znaci.goran.moviesaplikacija.adapters.RecyclerViewPeopleAdapter;
import com.znaci.goran.moviesaplikacija.api.RestApi;
import com.znaci.goran.moviesaplikacija.listeners.OnRowCastClickListener;
import com.znaci.goran.moviesaplikacija.listeners.OnRowPersonClickListener;
import com.znaci.goran.moviesaplikacija.models.Cast;
import com.znaci.goran.moviesaplikacija.models.CreditsModel;
import com.znaci.goran.moviesaplikacija.models.Person;
import com.znaci.goran.moviesaplikacija.models.PersonModel;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FullCastActivity extends AppCompatActivity {

    @BindView(R.id.fullcastrv)RecyclerView fullcast;
    RecyclerViewFullCastAdapter adapter;
    int movieID;
    CreditsModel model;
    ProgressDialog pd;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_full_cast);
        ButterKnife.bind(this);
        pd = new ProgressDialog(FullCastActivity.this);
        pd.setMessage("loading");
        Intent intent = getIntent();
        if (intent.hasExtra("id")){

         movieID = intent.getIntExtra("id",0);
         PeopleMovieSearch(movieID);


        }else if (intent.hasExtra("showid")){
            movieID = intent.getIntExtra("showid",0);
            PeopleShowSearch(movieID);
        }
    }


    public void PeopleMovieSearch(int id) {
        pd.show();
        RestApi api = new RestApi(FullCastActivity.this);
        Call<CreditsModel> call = api.getMovieCredits(id);
        call.enqueue(new Callback<CreditsModel>() {
            @Override
            public void onResponse(Call<CreditsModel> call, Response<CreditsModel> response) {
                model = response.body();
                adapter = new RecyclerViewFullCastAdapter(FullCastActivity.this, new OnRowCastClickListener() {
                    @Override
                    public void onRowClick(Cast cast, int position) {
                        Intent intent = new Intent(FullCastActivity.this, ScrollingPEopleActivity.class);
                        intent.putExtra("id", cast.id);
                        startActivityForResult(intent, 1111);
                    }});
                adapter.setItems(model.cast);
                fullcast.setHasFixedSize(true);
                fullcast.setLayoutManager(new GridLayoutManager(FullCastActivity.this,3));
                fullcast.setAdapter(adapter);
                pd.dismiss();
            }
            @Override
            public void onFailure(Call<CreditsModel> call, Throwable t) {
                if(t.getMessage().contains("Unable to resolve host"));
                Toast.makeText(FullCastActivity.this, "No internet connection", Toast.LENGTH_SHORT).show();
                finish();
            }});}

    public void PeopleShowSearch(int id) {
        pd.show();
        RestApi api = new RestApi(FullCastActivity.this);
        Call<CreditsModel> call = api.getShowCredits(id);
        call.enqueue(new Callback<CreditsModel>() {
            @Override
            public void onResponse(Call<CreditsModel> call, Response<CreditsModel> response) {
                model = response.body();
                adapter = new RecyclerViewFullCastAdapter(FullCastActivity.this, new OnRowCastClickListener() {
                    @Override
                    public void onRowClick(Cast cast, int position) {
                        Intent intent = new Intent(FullCastActivity.this, ScrollingPEopleActivity.class);
                        intent.putExtra("id", cast.id);
                        startActivityForResult(intent, 1111);
                    }});
                adapter.setItems(model.cast);
                fullcast.setHasFixedSize(true);
                fullcast.setLayoutManager(new GridLayoutManager(FullCastActivity.this,3));
                fullcast.setAdapter(adapter);
                pd.dismiss();
            }
            @Override
            public void onFailure(Call<CreditsModel> call, Throwable t) {
                if(t.getMessage().contains("Unable to resolve host"));
                Toast.makeText(FullCastActivity.this, "No internet connection", Toast.LENGTH_SHORT).show();
                finish();

            }});}

}
