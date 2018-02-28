package com.znaci.goran.moviesaplikacija.fragments;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.znaci.goran.moviesaplikacija.R;
import com.znaci.goran.moviesaplikacija.activities.AdvancedSearchActivity;
import com.znaci.goran.moviesaplikacija.activities.ScrollingShowsDetailActivity;
import com.znaci.goran.moviesaplikacija.activities.ShowDetailActivity;
import com.znaci.goran.moviesaplikacija.adapters.RecyclerViewShowsAdapter;
import com.znaci.goran.moviesaplikacija.api.RestApi;
import com.znaci.goran.moviesaplikacija.listeners.OnRowShowClickListener;
import com.znaci.goran.moviesaplikacija.models.Shows;
import com.znaci.goran.moviesaplikacija.models.ShowsModel;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Goran on movie2/6/2018.
 */

public class OnTheAirFragment extends Fragment{
    @BindView(R.id.RvExplore)RecyclerView rv;
    RecyclerViewShowsAdapter adapter;
    private Unbinder mUnbind;
    ShowsModel model = new ShowsModel();
    ShowsModel model2 = new ShowsModel();
    int c = 1;
    ProgressDialog pd;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.exploreactivity_fragments_layout, null);
        mUnbind = ButterKnife.bind(this, view);
        pd = new ProgressDialog(getActivity());
        pd.setMessage("loading");
        OnTheAirShows();
        rv.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (!recyclerView.canScrollVertically(1)) {
                            pd.show();
                            c++;
                            RestApi api = new RestApi(getActivity());
                            Call<ShowsModel> call = api.getShows("on_the_air",c);
                            call.enqueue(new Callback<ShowsModel>() {
                                @Override
                                public void onResponse(Call<ShowsModel> call, Response<ShowsModel> response) {
                                    if (response.code() == 200) {
                                        model2 = response.body();
                                        model.results.addAll(model2.results);
                                        adapter.notifyDataSetChanged();
                                        pd.dismiss();
                                    }
                                }

                                @Override
                                public void onFailure(Call<ShowsModel> call, Throwable t) {

                                }
                            });}}});

        return view;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mUnbind.unbind();
    }

    public void OnTheAirShows(){
        pd.show();
        RestApi api = new RestApi(getActivity());
        Call<ShowsModel> call = api.getShows("on_the_air",c);
        call.enqueue(new Callback<ShowsModel>() {
            @Override
            public void onResponse(Call<ShowsModel> call, Response<ShowsModel> response) {
                if (response.code() == 200) {

                    model = response.body();
                            adapter = new RecyclerViewShowsAdapter(getActivity(), new OnRowShowClickListener() {
                        @Override
                        public void onRowClick(Shows movie, int position) {
                            Intent intent = new Intent(getActivity(), ScrollingShowsDetailActivity.class);
                            intent.putExtra("showid",movie.id);
                            intent.putExtra("position",position);
                            startActivityForResult(intent,1111);
                        }
                    });
                    adapter.setItems(model.results);
                    rv.setHasFixedSize(true);
                    rv.setLayoutManager(new GridLayoutManager(getActivity(),2));
                    rv.setAdapter(adapter);
                    pd.dismiss();
                }
            }

            @Override
            public void onFailure(Call<ShowsModel> call, Throwable t) {

            }
        });
    }
}
