package com.mirsoft.easyfix.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;


/**
 * A placeholder fragment containing a simple view.
 */
public class MainActivityFragment extends Fragment {

    Button btnFetch;
    TextView tvInfo;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        TextView text = new TextView(this.getActivity());
        text.setText("Section");
        text.setGravity(Gravity.CENTER);
        return text;
    }
    /*@Inject
    Provider<SessionApi> sessionApiProvider;*/

   // public MainActivityFragment() {
   // }

   /* @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_main, container, false);*/

        /*tvInfo = (TextView) rootView.findViewById(R.id.tvInfo);
        btnFetch = (Button) rootView.findViewById(R.id.btnFetch);

        btnFetch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //BusProvider.getInstance().post();
                Session session = new Session();
                session.username = "+996557656649";
                session.password = "qwerty";

                SessionApi api = RestClient.createService(SessionApi.class);
                api.login(session, new Callback<Session>() {
                    @Override
                    public void success(Session session, Response response) {
                        tvInfo.setText(session.token);
                    }

                    @Override
                    public void failure(RetroftiError error) {
                        tvInfo.setText(error.toString());
                    }
                });
            }
        });*/

        //return rootView;
   // }
}
