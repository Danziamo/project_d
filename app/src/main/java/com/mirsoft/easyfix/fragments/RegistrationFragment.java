package com.mirsoft.easyfix.fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.mirsoft.easyfix.R;
import com.mirsoft.easyfix.api.UserApi;
import com.mirsoft.easyfix.models.SocialUser;
import com.mirsoft.easyfix.models.User;
import com.mirsoft.easyfix.networking.RestClient;
import com.rengwuxian.materialedittext.MaterialEditText;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class RegistrationFragment extends Fragment implements View.OnClickListener {

    private static final String ARG_SOCIAL_PROVIDER = "socialProvider";
    private static final String ARG_SOCIAL_PROFILE_ID = "socialProfileId";

    private MaterialEditText etphone;
    private MaterialEditText etpassword;
    private Button btnregistration;


    // TODO: Rename and change types of parameters
    private String mSocialProvider;
    private String mSocialProfileId;

    public static RegistrationFragment newInstance(String socialProvider, String socialProfileId) {
        RegistrationFragment fragment = new RegistrationFragment();
        Bundle args = new Bundle();
        args.putString(ARG_SOCIAL_PROVIDER, socialProvider);
        args.putString(ARG_SOCIAL_PROFILE_ID, socialProfileId);
        fragment.setArguments(args);
        return fragment;
    }

    public RegistrationFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mSocialProvider = getArguments().getString(ARG_SOCIAL_PROVIDER);
            mSocialProfileId = getArguments().getString(ARG_SOCIAL_PROFILE_ID);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_registration, container, false);

        etphone = (MaterialEditText)view.findViewById(R.id.etPhone);
        etpassword = (MaterialEditText)view.findViewById(R.id.etPassword);

        if(mSocialProfileId != null && mSocialProvider != null) {
            etpassword.setVisibility(View.INVISIBLE);
        }

        btnregistration = (Button)view.findViewById(R.id.btnSubmit);
        btnregistration.setOnClickListener(this);

        return view;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnSubmit:
                makeSignUp();
        }
    }

    private void makeSignUp() {
        String phone = etphone.getText().toString();
        String password = etpassword.getText().toString();
        boolean isError = false;

        if (phone.trim().isEmpty()) {
            etphone.setError("Не может быть пустым");
            isError = true;
        }

        if (password.trim().isEmpty() && mSocialProfileId == null) {
            etpassword.setError("Не может быть пустым");
            isError = true;
        }

        if (isError) return;

        if (mSocialProfileId == null) {
            User user = new User();
            user.setPhone(phone);
            user.setPassword(password);
//            final Settings settings = new Settings(getActivity());
            UserApi api = RestClient.createService(UserApi.class);
            api.add(user, new Callback<User>() {
                @Override
                public void success(User user, Response response) {

                    String backStateName = getActivity().getFragmentManager().getClass().getName();
                    getActivity().getSupportFragmentManager().beginTransaction()
                            .replace(R.id.fragment, AccountActivationFragment.newInstance(false, null))
                            .addToBackStack(backStateName)
                            .commit();

                /*settings.setAccessToken(user.getToken());
                Intent intent = new Intent(getActivity(), FixNavigationDrawer.class);
                startActivity(intent);
                getActivity().finish();*/
                }

                @Override
                public void failure(RetrofitError error) {
                    Toast.makeText(getActivity(), error.toString(), Toast.LENGTH_SHORT).show();
                }
            });
        } else {
//            final Settings settings = new Settings(getActivity());
            UserApi api = RestClient.createService(UserApi.class);
            SocialUser user = new SocialUser();
            user.id = mSocialProfileId;
            user.provider = mSocialProvider;
            user.phone = phone;
            api.add(user, new Callback<Object>() {
                @Override
                public void success(Object o, Response response) {
                    goToActivation();
                }

                @Override
                public void failure(RetrofitError error) {
                    Toast.makeText(getActivity(), "Cannot sign up", Toast.LENGTH_SHORT).show();
                }
            });
        }

    }

    private void goToActivation() {
        String backStateName = getActivity().getFragmentManager().getClass().getName();
        getActivity().getSupportFragmentManager().beginTransaction()
                .replace(R.id.container, AccountActivationFragment.newInstance(true, null))
                .addToBackStack(backStateName)
                .commit();
    }
}
