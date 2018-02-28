package com.znaci.goran.moviesaplikacija.activities;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.znaci.goran.moviesaplikacija.R;
import com.znaci.goran.moviesaplikacija.api.RestApi;
import com.znaci.goran.moviesaplikacija.helpers.ApiCalls;
import com.znaci.goran.moviesaplikacija.models.User;
import com.znaci.goran.moviesaplikacija.preferencesManager.LogInPreferences;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {

    @BindView(R.id.username)EditText userName;
    @BindView(R.id.password)EditText pass;
    @BindView(R.id.ctnGuest)TextView guestUser;
    @BindView(R.id.forgotPass)TextView newAccount;
    User user;
    ApiCalls apiCalls;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);
        apiCalls = new ApiCalls(this);


    }

    @OnClick(R.id.login)
    public void LoginBtn(){

        RestApi api = new RestApi(LoginActivity.this);
        Call<User> call = api.getGuestUser("token");
        call.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                if (response.code() == 200) {

                    user = response.body();
                    RestApi api = new RestApi(LoginActivity.this);
                    Call<User> call2 = api.getValidateUser(userName.getText().toString(),pass.getText().toString(),user.request_token);
                    call2.enqueue(new Callback<User>() {
                        @Override
                        public void onResponse(Call<User> call, Response<User> response) {
                            if (response.code() == 200) {
                                user = response.body();
                                LogInPreferences.setUserID(user.request_token,LoginActivity.this);
                                RestApi api = new RestApi(LoginActivity.this);
                                Call<User> callid = api.getUserSession(user.request_token);
                                callid.enqueue(new Callback<User>() {
                                    @Override
                                    public void onResponse(Call<User> call, Response<User> response) {
                                        if (response.code() == 200) {

                                            user = response.body();
                                            LogInPreferences.setSessionID(user.session_id,LoginActivity.this);

                                        }}

                                    @Override
                                    public void onFailure(Call<User> call, Throwable t) {
                                    }});

                                Intent intent = new Intent(LoginActivity.this,ExploreActivity.class);
                                startActivity(intent);
                                finish();
                            }}
                        @Override
                        public void onFailure(Call<User> call, Throwable t) {
                            Toast.makeText(LoginActivity.this, "Failed validation", Toast.LENGTH_SHORT).show();
                        }});}}
            @Override
            public void onFailure(Call<User> call, Throwable t) {
                Toast.makeText(LoginActivity.this, "Failed token", Toast.LENGTH_SHORT).show();
            }});
    }

    @OnClick(R.id.ctnGuest)
    public void CtnGuest(){

        RestApi api = new RestApi(LoginActivity.this);
        Call<User> call = api.getGuestUser("guest_session");
        call.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                if (response.code() == 200) {

                    user = response.body();
                    LogInPreferences.setGuestUserID(user.guest_session_id,LoginActivity.this);
                    Intent intent = new Intent(LoginActivity.this,ExploreActivity.class);
                    startActivity(intent);
                    finish();
                }}

            @Override
            public void onFailure(Call<User> call, Throwable t) {
            }});}

    @OnClick(R.id.forgotPass)
    public void NewAccount(){

        Intent i = new Intent();
        i.putExtra(Intent.EXTRA_TEXT, "");
        i.setAction(Intent.ACTION_VIEW);
        i.setData(Uri.parse("https://www.themoviedb.org/account/signup"));
        startActivity(i);
    }
}
