package com.mirsoft.easyfixmaster;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.mirsoft.easyfixmaster.api.Api;
import com.mirsoft.easyfixmaster.api.SessionApi;
import com.mirsoft.easyfixmaster.models.Session;
import com.mirsoft.easyfixmaster.service.ServiceGenerator;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;


/**
 * A placeholder fragment containing a simple view.
 */
public class MainActivityFragment extends Fragment {

    Button btnFetch;
    TextView tvInfo;

    /*@Inject
    Provider<SessionApi> sessionApiProvider;*/

    public MainActivityFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_main, container, false);

        tvInfo = (TextView) rootView.findViewById(R.id.tvInfo);
        btnFetch = (Button) rootView.findViewById(R.id.btnFetch);

        btnFetch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //BusProvider.getInstance().post();
                Session session = new Session();
                session.username = "+996557656649";
                session.password = "qwerty";

                SessionApi api = ServiceGenerator.createService(SessionApi.class);
                api.login(session, new Callback<Session>() {
                    @Override
                    public void success(Session session, Response response) {
                        tvInfo.setText(session.token);
                    }

                    @Override
                    public void failure(RetrofitError error) {
                        tvInfo.setText(error.toString());
                    }
                });
            }
        });

        return rootView;
    }
}
