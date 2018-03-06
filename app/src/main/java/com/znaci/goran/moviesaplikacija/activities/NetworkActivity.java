package com.znaci.goran.moviesaplikacija.activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.TextView;

import com.znaci.goran.moviesaplikacija.R;
import com.znaci.goran.moviesaplikacija.adapters.RecyclerViewShowsAdapter;
import com.znaci.goran.moviesaplikacija.api.RestApi;
import com.znaci.goran.moviesaplikacija.helpers.ApiCalls;
import com.znaci.goran.moviesaplikacija.models.ShowsModel;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class NetworkActivity extends AppCompatActivity {

    @BindView(R.id.nameNetwork)TextView nameNet;
    @BindView(R.id.networkShowRV)RecyclerView RvShow;
    ApiCalls apiCalls;
    ProgressDialog pd;
    int c = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_network);
        ButterKnife.bind(this);
        pd = new ProgressDialog(this);
        pd.setMessage("loading");

        Intent intent = getIntent();
        if (intent.hasExtra("GID")){

        int movieID = intent.getIntExtra("GID",0);
        String nameNetwork = intent.getStringExtra("name");
        nameNet.setText(nameNetwork);
        apiCalls = new ApiCalls(this);
        apiCalls.GetNetwork(movieID,RvShow);

        }
    }
}
