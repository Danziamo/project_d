package com.mirsoft.easyfix.fragments;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.mirsoft.easyfix.R;
import com.mirsoft.easyfix.Settings;
import com.mirsoft.easyfix.TabsActivity;
import com.mirsoft.easyfix.api.SessionApi;
import com.mirsoft.easyfix.models.ActivationCode;
import com.mirsoft.easyfix.models.Session;
import com.mirsoft.easyfix.networking.RestClient;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class AccountActivationFragment extends Fragment {

    EditText et_code_sms;
    Button btn_authorization;


    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private Boolean isSocial;
    private String mParam2;


    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment AccountActivationFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static AccountActivationFragment newInstance(Boolean param1, String param2) {
        AccountActivationFragment fragment = new AccountActivationFragment();
        Bundle args = new Bundle();
        args.putBoolean(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    public AccountActivationFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            isSocial = getArguments().getBoolean(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_account_activation, container, false);

        et_code_sms = (EditText)view.findViewById(R.id.et_code_sms);
        btn_authorization = (Button)view.findViewById(R.id.btn_authorization);
        btn_authorization.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                activate();
            }
        });

        return view;
    }

    private void activate () {
        String code = et_code_sms.getText().toString();
        if (code.length() != 4) {
            et_code_sms.setError("4 simvola");
            et_code_sms.requestFocus();
            return;
        }
        ActivationCode activationCode = new ActivationCode();
        activationCode.code = code;
        SessionApi api = RestClient.createService(SessionApi.class);
        if (isSocial) {
            api.activateSocial(activationCode, new Callback<Session>() {
                @Override
                public void success(Session session, Response response) {
                    Settings settings = new Settings(getActivity());
                    settings.setAccessToken(session.token);
                    settings.setUserId(session.id);
                    openDrawer();
                }

                @Override
                public void failure(RetrofitError error) {
                    Toast.makeText(getActivity(), "Cannot activate", Toast.LENGTH_SHORT).show();
                }
            });
        } else {
            api.activate(activationCode, new Callback<Session>() {
                @Override
                public void success(Session session, Response response) {
                    Settings settings = new Settings(getActivity());
                    settings.setAccessToken(session.token);
                    settings.setUserId(session.id);
                    openDrawer();
                }

                @Override
                public void failure(RetrofitError error) {
                    Toast.makeText(getActivity(), "Cannot activate", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    private void openDrawer(){
        Intent intent = new Intent(getActivity(), TabsActivity.class);
        startActivity(intent);
        getActivity().finish();
    }

}
