package com.mirsoft.easyfixmaster;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.mirsoft.easyfixmaster.api.SessionApi;
import com.mirsoft.easyfixmaster.api.UserApi;
import com.mirsoft.easyfixmaster.models.Session;
import com.mirsoft.easyfixmaster.models.User;
import com.mirsoft.easyfixmaster.service.ServiceGenerator;

import javax.inject.Inject;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class TestSIgnUp extends Activity {

    TextView tvInfo;
    Button btnRun;
    SessionApi api;
    int mUserId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_sign_up);

        final Settings settings = new Settings(this);
        api = ServiceGenerator.createService(SessionApi.class, settings);
        mUserId = 0;

        tvInfo = (TextView)findViewById(R.id.tvInfo);
        btnRun = (Button)findViewById(R.id.btnRun);
        Button btnLogout = (Button) findViewById(R.id.btnLogout);

        btnRun.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                /*User user = new User();
                user.setPhone("996555112233");
                user.setFirstName("ddd");
                user.setLastName("uuu");
                user.setPassword("123456");
                user.setRole("contractor");

                UserApi api = ServiceGenerator.createService(UserApi.class);
                api.add(user, new Callback<User>() {
                    @Override
                    public void success(User user, Response response) {
                        tvInfo.setText(user.getToken() + '\n' + user.getFirstName() + '\n' + user.getRole());
                    }

                    @Override
                    public void failure(RetrofitError error) {
                        tvInfo.setText(error.toString());
                    }
                });*/

                Session session = new Session();
                session.username = "996555112233";
                session.password = "123456";
                api.login(session, new Callback<Session>() {
                    @Override
                    public void success(Session session, Response response) {
                        tvInfo.setText(session.token);
                        mUserId = session.id;
                        settings.setAccessToken(session.token);
                    }

                    @Override
                    public void failure(RetrofitError error) {
                        tvInfo.setText(error.toString());
                    }
                });
            }
        });

        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Session session = new Session();
                api.logout(session, new Callback<Object>() {
                    @Override
                    public void success(Object o, Response response) {
                        tvInfo.setText(o.toString());
                        settings.setAccessToken(null);
                    }

                    @Override
                    public void failure(RetrofitError error) {
                        tvInfo.setText(error.toString());
                    }
                });
            }
        });

        Button btnGetInfo = (Button)findViewById(R.id.btnInfo);
        btnGetInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UserApi userApi = ServiceGenerator.createService(UserApi.class, settings);
                userApi.getById(mUserId, new Callback<User>() {
                    @Override
                    public void success(User user, Response response) {
                        tvInfo.setText(user.getFirstName() + " " + user.getLastName());
                    }

                    @Override
                    public void failure(RetrofitError error) {
                        tvInfo.setText(error.toString());
                    }
                });
            }
        });
    }

}
