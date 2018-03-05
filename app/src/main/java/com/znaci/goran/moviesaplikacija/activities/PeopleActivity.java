package com.znaci.goran.moviesaplikacija.activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.znaci.goran.moviesaplikacija.R;
import com.znaci.goran.moviesaplikacija.adapters.RecyclerViewPeopleAdapter;
import com.znaci.goran.moviesaplikacija.adapters.RecyclerViewPopularAdapter;
import com.znaci.goran.moviesaplikacija.api.RestApi;
import com.znaci.goran.moviesaplikacija.listeners.OnRowMovieClickListener;
import com.znaci.goran.moviesaplikacija.listeners.OnRowPersonClickListener;
import com.znaci.goran.moviesaplikacija.models.Movie;
import com.znaci.goran.moviesaplikacija.models.MovieModel;
import com.znaci.goran.moviesaplikacija.models.Person;
import com.znaci.goran.moviesaplikacija.models.PersonModel;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PeopleActivity extends AppCompatActivity {
    @BindView(R.id.peopleRV)
    RecyclerView peopleRV;
    @BindView(R.id.searchPeople)EditText searchPeople;
    @BindView(R.id.loadmore)Button load;
    RecyclerViewPeopleAdapter adapter;
    PersonModel peopleModel = new PersonModel();
    PersonModel peopleModel2 = new PersonModel();
    Handler handler;
    int c= 1;
    ProgressDialog pd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_people);
        ButterKnife.bind(this);
        pd = new ProgressDialog(PeopleActivity.this);
        pd.setMessage("loading");

        searchPeople.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override
            public void onTextChanged(final CharSequence s, int start, int before, int count) {
                handler = new Handler();
                 handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        Log.e("handler",s + "");
                        if (s.toString().isEmpty()){
                            PeopleMovieSearch();
                        }else {
                            String name = searchPeople.getText().toString();
                            PersonSearch(name);}}
                },1000);}
            @Override
            public void afterTextChanged(Editable s) {}});

        PeopleMovieSearch();

        peopleRV.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                load.setVisibility(View.GONE);
                if (!recyclerView.canScrollVertically(1)) {
                            pd.show();
                            c++;
                            RestApi api = new RestApi(PeopleActivity.this);
                            Call<PersonModel> call = api.getPopularPeople(c);
                            call.enqueue(new Callback<PersonModel>() {
                                @Override
                                public void onResponse(Call<PersonModel> call, Response<PersonModel> response) {
                                    if (response.code() == 200) {

                                        peopleModel2 = response.body();
                                        peopleModel.results.addAll(peopleModel2.results);
                                        adapter.notifyDataSetChanged();
                                        pd.dismiss();    }}
                                @Override
                                public void onFailure(Call<PersonModel> call, Throwable t) {
                                    if(t.getMessage().contains("Unable to resolve host"));
                                    Toast.makeText(PeopleActivity.this, "No internet connection", Toast.LENGTH_SHORT).show();
                                    finish();
                                }});}}});

    }

    public void PersonSearch(String link) {
        RestApi api = new RestApi(PeopleActivity.this);
        Call<PersonModel> call = api.getSearchPerson(link);
        call.enqueue(new Callback<PersonModel>() {
            @Override
            public void onResponse(Call<PersonModel> call, Response<PersonModel> response) {
                if (response.code() == 200) {
                    peopleModel = response.body();
                    adapter = new RecyclerViewPeopleAdapter(PeopleActivity.this, new OnRowPersonClickListener() {
                        @Override
                        public void onRowClick(Person person, int position) {
                            Intent intent = new Intent(PeopleActivity.this, ScrollingPEopleActivity.class);
                            intent.putExtra("id", person.id);
                            startActivityForResult(intent, 1111);
                        }
                    });
                    adapter.setItems(peopleModel.results);
                    peopleRV.setHasFixedSize(true);
                    peopleRV.setLayoutManager(new LinearLayoutManager(PeopleActivity.this));
                    peopleRV.setAdapter(adapter);}}
            @Override
            public void onFailure(Call<PersonModel> call, Throwable t) {
                if(t.getMessage().contains("Unable to resolve host"));
                Toast.makeText(PeopleActivity.this, "No internet connection", Toast.LENGTH_SHORT).show();
                finish();
            }});}

    public void PeopleMovieSearch() {
        pd.show();
        RestApi api = new RestApi(PeopleActivity.this);
        Call<PersonModel> call = api.getPopularPeople(c);
        call.enqueue(new Callback<PersonModel>() {
            @Override
            public void onResponse(Call<PersonModel> call, Response<PersonModel> response) {
                peopleModel = response.body();
                adapter = new RecyclerViewPeopleAdapter(PeopleActivity.this, new OnRowPersonClickListener() {
                    @Override
                    public void onRowClick(Person person, int position) {
                        Intent intent = new Intent(PeopleActivity.this, ScrollingPEopleActivity.class);
                        intent.putExtra("id", person.id);
                        startActivityForResult(intent, 1111);
                    }});
                adapter.setItems(peopleModel.results);
                peopleRV.setHasFixedSize(true);
                peopleRV.setLayoutManager(new LinearLayoutManager(PeopleActivity.this));
                peopleRV.setAdapter(adapter);
                pd.dismiss();
            }
            @Override
            public void onFailure(Call<PersonModel> call, Throwable t) {
                if(t.getMessage().contains("Unable to resolve host"));
                Toast.makeText(PeopleActivity.this, "No internet connection", Toast.LENGTH_SHORT).show();
                finish();
            }});}

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK && requestCode == 1111) {}}
}