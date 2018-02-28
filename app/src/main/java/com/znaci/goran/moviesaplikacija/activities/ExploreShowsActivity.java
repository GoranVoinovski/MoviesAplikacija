package com.znaci.goran.moviesaplikacija.activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
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
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;
import com.znaci.goran.moviesaplikacija.R;
import com.znaci.goran.moviesaplikacija.adapters.ExplorePagerAdapter;
import com.znaci.goran.moviesaplikacija.adapters.RecyclerViewPopularAdapter;
import com.znaci.goran.moviesaplikacija.adapters.RecyclerViewShowsAdapter;
import com.znaci.goran.moviesaplikacija.api.RestApi;
import com.znaci.goran.moviesaplikacija.fragments.AiringTodayFragment;
import com.znaci.goran.moviesaplikacija.fragments.NowPlayingFragment;
import com.znaci.goran.moviesaplikacija.fragments.OnTheAirFragment;
import com.znaci.goran.moviesaplikacija.fragments.PopularFragment;
import com.znaci.goran.moviesaplikacija.fragments.PopularShowsFragment;
import com.znaci.goran.moviesaplikacija.fragments.TopRatedFragment;
import com.znaci.goran.moviesaplikacija.fragments.TopRatedShowsFragment;
import com.znaci.goran.moviesaplikacija.fragments.UpcomingFragment;
import com.znaci.goran.moviesaplikacija.helpers.ApiCalls;
import com.znaci.goran.moviesaplikacija.listeners.OnRowMovieClickListener;
import com.znaci.goran.moviesaplikacija.listeners.OnRowShowClickListener;
import com.znaci.goran.moviesaplikacija.models.Movie;
import com.znaci.goran.moviesaplikacija.models.MovieModel;
import com.znaci.goran.moviesaplikacija.models.Shows;
import com.znaci.goran.moviesaplikacija.models.ShowsModel;
import com.znaci.goran.moviesaplikacija.models.User;
import com.znaci.goran.moviesaplikacija.preferencesManager.LogInPreferences;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ExploreShowsActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    @BindView(R.id.search_bar)EditText searchMovie;
    @BindView(R.id.searchMoviesView)RecyclerView layoutSearch;
    @BindView(R.id.vp)ViewPager vPage;
    @BindView(R.id.tablayout)TabLayout tabs;
    RecyclerViewShowsAdapter adapter2;
    ExplorePagerAdapter adapter;
    ShowsModel showsModel = new ShowsModel();
    Handler handler;
    TextView profileName,profileMail;
    CircleImageView profileIMG;
    ApiCalls apiCalls;
    public int selectedTabposition = 0;
    String token = "";
    String itemLog = "";
    String session = "";
    ProgressDialog pd;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_explore_shows);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ButterKnife.bind(this);
        pd = new ProgressDialog(ExploreShowsActivity.this);
        pd.setMessage("loading");
        apiCalls = new ApiCalls(this);
        token = LogInPreferences.getUserID(this);
        session = LogInPreferences.getSessionID(this);
        selectedTabposition = vPage.getCurrentItem();

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
                            MovieSearch(movie);
                        }}
                },1000);}
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
        Intent intent = new Intent(ExploreShowsActivity.this,AdvancedSearchShowsActivity.class);
        startActivity(intent);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.explore_shows, menu);
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
        int id = item.getItemId();
        if (id == R.id.explore) {
            Intent intentExplore = new Intent(ExploreShowsActivity.this,ExploreActivity.class);
            startActivity(intentExplore);
        }else if (id == R.id.exploreshows) {
            recreate();
        } else if (id == R.id.favorites) {
            Intent intent = new Intent(ExploreShowsActivity.this,FavoriteShowsActivity.class);
            intent.putExtra("id","id");
            startActivity(intent);
        } else if (id == R.id.rated) {
            Intent intent = new Intent(ExploreShowsActivity.this,RatedShowActivity.class);
            intent.putExtra("id","id");
            startActivity(intent);


        } else if (id == R.id.watchlist) {
            Intent intent = new Intent(ExploreShowsActivity.this,WatchlistShowsActivity.class);
            intent.putExtra("id","id");
            startActivity(intent);
        } else if (id == R.id.people) {
            Intent intent = new Intent(ExploreShowsActivity.this,PeopleActivity.class);
            startActivity(intent);

        } else if (id == R.id.logout_login) {
            if (itemLog.equals("Logout")){
                LogInPreferences.removeUserID(ExploreShowsActivity.this);
                LogInPreferences.removeSessionID(ExploreShowsActivity.this);
                Intent intent = new Intent(ExploreShowsActivity.this,LoginActivity.class);
                startActivity(intent);
                finish();

            }else {
                LogInPreferences.removeGuestUserID(this);
                Intent intent = new Intent(ExploreShowsActivity.this,LoginActivity.class);
                startActivity(intent);
                finish();}}

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public void setUpViewPager(ViewPager mojpager) {
        adapter = new ExplorePagerAdapter(this.getSupportFragmentManager());
        adapter.dodadiFragment(new AiringTodayFragment(),"Airing Today");
        adapter.dodadiFragment(new OnTheAirFragment(),"On The Air");
        adapter.dodadiFragment(new PopularShowsFragment(),"Popular");
        adapter.dodadiFragment(new TopRatedShowsFragment(),"Top Rated");
        mojpager.setAdapter(adapter);}

    public void MovieSearch(String link){
        RestApi api = new RestApi(ExploreShowsActivity.this);
        Call<ShowsModel> call = api.getSearchShows(link);
        call.enqueue(new Callback<ShowsModel>() {
            @Override
            public void onResponse(Call<ShowsModel> call, Response<ShowsModel> response) {
                if (response.code() == 200) {

                    showsModel = response.body();
                    adapter2 = new RecyclerViewShowsAdapter(ExploreShowsActivity.this, new OnRowShowClickListener() {
                        @Override
                        public void onRowClick(Shows movie, int position) {
                            Intent intent = new Intent(ExploreShowsActivity.this, ScrollingShowsDetailActivity.class);
                            intent.putExtra("showid",movie.id);
                            intent.putExtra("position",position);
                            startActivityForResult(intent,1111);
                        }
                    });
                    adapter2.setItems(showsModel.results);
                    layoutSearch.setHasFixedSize(true);
                    layoutSearch.setLayoutManager(new GridLayoutManager(ExploreShowsActivity.this,2));
                    layoutSearch.setAdapter(adapter2);}}
            @Override
            public void onFailure(Call<ShowsModel> call, Throwable t) {
            }});}
}
