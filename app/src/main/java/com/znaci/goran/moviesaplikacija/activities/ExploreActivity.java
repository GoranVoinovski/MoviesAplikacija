package com.znaci.goran.moviesaplikacija.activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.znaci.goran.moviesaplikacija.R;
import com.znaci.goran.moviesaplikacija.adapters.ExplorePagerAdapter;
import com.znaci.goran.moviesaplikacija.adapters.RecyclerViewPopularAdapter;
import com.znaci.goran.moviesaplikacija.api.RestApi;
import com.znaci.goran.moviesaplikacija.fragments.NowPlayingFragment;
import com.znaci.goran.moviesaplikacija.fragments.PopularFragment;
import com.znaci.goran.moviesaplikacija.fragments.TopRatedFragment;
import com.znaci.goran.moviesaplikacija.fragments.UpcomingFragment;
import com.znaci.goran.moviesaplikacija.helpers.ApiCalls;
import com.znaci.goran.moviesaplikacija.listeners.OnRowMovieClickListener;
import com.znaci.goran.moviesaplikacija.models.Movie;
import com.znaci.goran.moviesaplikacija.models.MovieModel;
import com.znaci.goran.moviesaplikacija.preferencesManager.LogInPreferences;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ExploreActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    public @BindView(R.id.vp)ViewPager vPage;
    public @BindView(R.id.tablayout)TabLayout tabs;
    public @BindView(R.id.search_bar)EditText searchMovie;
    @BindView(R.id.searchMoviesView)RecyclerView layoutSearch;
    @BindView(R.id.btnpager)Button btnpage;
    RecyclerViewPopularAdapter adapter2;
    public ExplorePagerAdapter adapter;
    TextView profileName,profileMail;
    CircleImageView profileIMG;
    public int selectedTabposition = 0;
    public MovieModel movieModel = new MovieModel();
    public MovieModel movieModelproverka = new MovieModel();
    Handler handler;
    public int id = 0;
    String itemLog ="";
    String session ="";
    String token ="";
    ApiCalls apiCalls;
    ProgressDialog pd;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_explore);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ButterKnife.bind(this);
        pd = new ProgressDialog(ExploreActivity.this);
        pd.setMessage("Loading");
        apiCalls = new ApiCalls(this);
        token = LogInPreferences.getUserID(this);
        session = LogInPreferences.getSessionID(this);
        selectedTabposition = vPage.getCurrentItem();
        RestApi api = new RestApi(ExploreActivity.this);
        Call<MovieModel> call = api.getUserFavorites("account_id", session);
        call.enqueue(new Callback<MovieModel>() {
            @Override
            public void onResponse(Call<MovieModel> call, Response<MovieModel> response) {
                if (response.code() == 200) {
                    movieModel = response.body();
                    for (Movie movie:movieModel.results){
                        movie.setFavorites(true);
                    }
                }}
            @Override
            public void onFailure(Call<MovieModel> call, Throwable t) {
            }});

        searchMovie.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }
            @Override
            public void onTextChanged(final CharSequence s, int start, int before, int count) {
                handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        Log.e("handler",s + "");
                        if (s.toString().isEmpty()){
                            layoutSearch.setVisibility(View.GONE);
                        }else {
                            String movie = searchMovie.getText().toString();
                            layoutSearch.setVisibility(View.VISIBLE);
                            MovieSearch(movie);}}},1000);}
            @Override
            public void afterTextChanged(Editable s) {
            }});


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        View header = navigationView.getHeaderView(0);
        Menu menu = navigationView.getMenu();
        profileIMG = (CircleImageView) header.findViewById(R.id.profileImg);
        profileName = (TextView) header.findViewById(R.id.profileName);
        profileMail = (TextView) header.findViewById(R.id.profileEmail);
        MenuItem logout_login = menu.findItem(R.id.logout_login);
        if (!token.isEmpty()){
            logout_login.setTitle("Logout");
            itemLog = "Logout";

        }else {
            logout_login.setTitle("Login");
            itemLog = "Login";
        }
        apiCalls.GetUserDetails(profileName,profileMail,profileIMG,session);

        vPage.setOffscreenPageLimit(3);
        setUpViewPager(vPage);
    }

    @OnClick(R.id.btnpager)
    public void SetPager(){
    Intent intent = new Intent(ExploreActivity.this,AdvancedSearchActivity.class);
    startActivity(intent);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            Intent intentgenre = getIntent();
            if (intentgenre.hasExtra("GID")){
            Intent intent = new Intent();
            setResult(RESULT_OK,intent);
            finish();
            }}}

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.home, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.explore) {
            recreate();
        }else if (id == R.id.exploreshows) {

            Intent intent = new Intent(ExploreActivity.this,ExploreShowsActivity.class);
            startActivity(intent);

        } else if (id == R.id.favorites) {
            Intent intent = new Intent(ExploreActivity.this,FavoritesActivity.class);
            intent.putExtra("id","id");
            startActivity(intent);

        } else if (id == R.id.rated) {
            Intent intent = new Intent(ExploreActivity.this,RatedActivity.class);
            intent.putExtra("id","id");
            startActivity(intent);

        } else if (id == R.id.watchlist) {
            Intent intent = new Intent(ExploreActivity.this,WatchlistActivity.class);
            intent.putExtra("id","id");
            startActivity(intent);

        } else if (id == R.id.people) {
            Intent intent = new Intent(ExploreActivity.this,PeopleActivity.class);
            startActivity(intent);

        } else if (id == R.id.logout_login) {
            if (itemLog.equals("Logout")){
                LogInPreferences.removeUserID(ExploreActivity.this);
                LogInPreferences.removeSessionID(ExploreActivity.this);
                Intent intent = new Intent(ExploreActivity.this,LoginActivity.class);
                startActivity(intent);
                finish();

            }else {
                LogInPreferences.removeGuestUserID(this);
                Intent intent = new Intent(ExploreActivity.this,LoginActivity.class);
                startActivity(intent);
                finish();

            }
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public void setUpViewPager(ViewPager mojpager) {
            adapter = new ExplorePagerAdapter(this.getSupportFragmentManager());
            adapter.dodadiFragment(new PopularFragment(),"Popular");
            adapter.dodadiFragment(new TopRatedFragment(),"Top Rated");
            adapter.dodadiFragment(new UpcomingFragment(),"Upcoming");
            adapter.dodadiFragment(new NowPlayingFragment(),"Now Playing");
            mojpager.setAdapter(adapter);
        }

    public void MovieSearch(String link){
        RestApi api = new RestApi(ExploreActivity.this);
        Call<MovieModel> call = api.getSearchMovie(link);
        call.enqueue(new Callback<MovieModel>() {
            @Override
            public void onResponse(Call<MovieModel> call, Response<MovieModel> response) {
                if (response.code() == 200) {
                    movieModel = response.body();
                    adapter2 = new RecyclerViewPopularAdapter(ExploreActivity.this, new OnRowMovieClickListener() {
                        @Override
                        public void onRowClick(Movie movie, int position) {
                            Intent intent = new Intent(ExploreActivity.this, ScrollingMovieDetailActivity.class);
                            intent.putExtra("id",movie.id);
                            intent.putExtra("position",position);
                            startActivityForResult(intent,1111);
                        }});
                    adapter2.setItems(movieModel.results);
                    layoutSearch.setHasFixedSize(true);
                    layoutSearch.setLayoutManager(new GridLayoutManager(ExploreActivity.this,2));
                    layoutSearch.setAdapter(adapter2);}}

            @Override
            public void onFailure(Call<MovieModel> call, Throwable t) {
            }});}

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK && requestCode == 1111){
            String session_id = LogInPreferences.getSessionID(this);
            pd.show();
            RestApi api = new RestApi(ExploreActivity.this);
            Call<MovieModel> call = api.getUserFavorites("account_id",session_id);
            call.enqueue(new Callback<MovieModel>() {
                @Override
                public void onResponse(Call<MovieModel> call, Response<MovieModel> response) {
                    if (response.code() == 200) {
                        movieModel = response.body();
                        adapter2 = new RecyclerViewPopularAdapter(ExploreActivity.this, new OnRowMovieClickListener() {
                            @Override
                            public void onRowClick(Movie movie, int position) {
                            }});
                        adapter2.setItems(movieModel.results);
                        layoutSearch.setHasFixedSize(true);
                        layoutSearch.setLayoutManager(new GridLayoutManager(ExploreActivity.this,2));
                        layoutSearch.setAdapter(adapter2);
                        pd.dismiss();
                    }}
                @Override
                public void onFailure(Call<MovieModel> call, Throwable t) {
                }});}}

    public String getURLForResource (int resourceId) {
        return Uri.parse("android.resource://"+R.class.getPackage().getName()+"/" +resourceId).toString();
    }

}
