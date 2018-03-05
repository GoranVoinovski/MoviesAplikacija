package com.znaci.goran.moviesaplikacija.activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.view.ViewCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.format.DateFormat;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;
import com.znaci.goran.moviesaplikacija.R;
import com.znaci.goran.moviesaplikacija.adapters.RecyclerViewCastAdapter;
import com.znaci.goran.moviesaplikacija.api.RestApi;
import com.znaci.goran.moviesaplikacija.listeners.OnRowCastClickListener;
import com.znaci.goran.moviesaplikacija.models.Cast;
import com.znaci.goran.moviesaplikacija.models.CreditsModel;
import com.znaci.goran.moviesaplikacija.models.Person;



import java.util.Calendar;
import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ScrollingPEopleActivity extends AppCompatActivity {

    @BindView(R.id.PersonImage)ImageView personImage;
    @BindView(R.id.PeopleBio)TextView personBio;
    @BindView(R.id.PeopleBorn)TextView personBorn;
    @BindView(R.id.name)TextView personName;
    @BindView(R.id.filmographyRV)RecyclerView personMovies;
    @BindView(R.id.app_bar)AppBarLayout apb;
    RecyclerViewCastAdapter adapter;
    CreditsModel creditsModel;
    Person person;
    Toolbar toolbar;
    CollapsingToolbarLayout toolbarLayout;
    ProgressDialog pd;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scrolling_people);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.toolbar_layout);
        ButterKnife.bind(this);
        pd = new ProgressDialog(ScrollingPEopleActivity.this);
        pd.setMessage("loading");

        Intent intent = getIntent();
        if (intent.hasExtra("id")){
            int id = intent.getIntExtra("id",0);
            PeopleDetails(id);
            PeopleCredits(id);
        }



    }

    public void PeopleDetails(int id){
        pd.show();
        RestApi api = new RestApi(ScrollingPEopleActivity.this);
        Call<Person> call1 = api.getPerson(id);
        call1.enqueue(new Callback<Person>() {
            @Override
            public void onResponse(Call<Person> call, Response<Person> response) {
                person = response.body();
                personName.setText(person.name);
                String path = "http://image.tmdb.org/t/p/w185" + person.profile_path;
                toolbarLayout.setTitle(person.name);
                toolbarLayout.setExpandedTitleColor(getResources().getColor(R.color.transparent2));
                Picasso.with(ScrollingPEopleActivity.this).load(path).fit().into(personImage);

                personBio.setText(person.biography);

                if (person.birthday != null){
                    String bday = DateConvert(person.birthday);
                    DateConvert(person.birthday);
                    if (person.deathday == null){
                        personBorn.setText("Birthday: " + bday);
                    }else {
                        String dday = DateConvert(person.deathday);
                        personBorn.setText("Birthday: " + bday + " Deathday: " + dday);
                    }

                }

                pd.dismiss();
            }
            @Override
            public void onFailure(Call<Person> call, Throwable t) {
                if(t.getMessage().contains("Unable to resolve host"));
                Toast.makeText(ScrollingPEopleActivity.this, "No internet connection", Toast.LENGTH_SHORT).show();
                finish();
            }
        });
    }

    public void PeopleCredits(int id){
        pd.show();
        RestApi api = new RestApi(ScrollingPEopleActivity.this);
        Call<CreditsModel> call1 = api.getPersonCredits(id);
        call1.enqueue(new Callback<CreditsModel>() {
            @Override
            public void onResponse(Call<CreditsModel> call, Response<CreditsModel> response) {
                creditsModel = response.body();
                adapter = new RecyclerViewCastAdapter(ScrollingPEopleActivity.this, new OnRowCastClickListener() {
                    @Override
                    public void onRowClick(Cast cast, int position) {
                        Intent intentCast = new Intent(ScrollingPEopleActivity.this,ScrollingMovieDetailActivity.class);
                        intentCast.putExtra("id",cast.id);
                        intentCast.putExtra("position",position);
                        startActivity(intentCast);
                    }
                });
                adapter.setItems(creditsModel.cast);
                personMovies.setHasFixedSize(true);
                personMovies.setLayoutManager(new LinearLayoutManager(ScrollingPEopleActivity.this,LinearLayoutManager.HORIZONTAL,false));
                personMovies.setAdapter(adapter);
                pd.dismiss();

            }
            @Override
            public void onFailure(Call<CreditsModel> call, Throwable t) {
                if(t.getMessage().contains("Unable to resolve host"));
                Toast.makeText(ScrollingPEopleActivity.this, "No internet connection", Toast.LENGTH_SHORT).show();
                finish();
            }
        });
    }

    public String DateConvert(String date){

        String str[] = date.split("-");
        String year = (str[0]);
        String month = (str[1]);
        String day = (str[2]);
        if (month.equals("01")){
            month = "January";
        }else if (month.equals("02")){
            month = "February";
        }else if (month.equals("03")){
            month = "March";
        }else if (month.equals("04")){
            month = "April";
        }else if (month.equals("05")){
            month = "May";
        }else if (month.equals("06")){
            month = "June";
        }else if (month.equals("07")){
            month = "July";
        }else if (month.equals("08")){
            month = "August";
        }else if (month.equals("09")){
            month = "September";
        }else if (month.equals("10")){
            month = "October";
        }else if (month.equals("11")){
            month = "November";
        }else if (month.equals("12")){
            month = "December";
        }

       return day + " " + month + " " + year;
    }

}

