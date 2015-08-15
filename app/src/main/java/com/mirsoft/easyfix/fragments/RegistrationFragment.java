package com.mirsoft.easyfix.fragments;


import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.support.v7.widget.AppCompatEditText;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.mirsoft.easyfix.R;
import com.mirsoft.easyfix.common.Constants;
import com.mirsoft.easyfix.networking.api.UserApi;
import com.mirsoft.easyfix.models.SocialUser;
import com.mirsoft.easyfix.models.User;
import com.mirsoft.easyfix.networking.RestClient;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class RegistrationFragment extends Fragment implements View.OnClickListener {

    private static final String ARG_SOCIAL_PROVIDER = "socialProvider";
    private static final String ARG_SOCIAL_PROFILE_ID = "socialProfileId";

    private AppCompatEditText etphone;
    private AppCompatEditText etpassword;
    private TextInputLayout tilPhone;
    private TextInputLayout tilPassword;
    private Button btnregistration;

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

        etphone = (AppCompatEditText)view.findViewById(R.id.etPhone);
        etpassword = (AppCompatEditText)view.findViewById(R.id.etPassword);

        tilPhone = (TextInputLayout)view.findViewById(R.id.tilPhone);
        tilPassword = (TextInputLayout)view.findViewById(R.id.tilPassword);

        if(mSocialProfileId != null && mSocialProvider != null) {
            tilPassword.setVisibility(View.INVISIBLE);
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
        String phone = etphone.getText().toString().trim();
        String password = etpassword.getText().toString().trim();
        boolean isError = false;

        if (phone.isEmpty()) {
            tilPhone.setError(getActivity().getResources().getString(R.string.error_not_empty));
            isError = true;
        }

        if (password.isEmpty() && mSocialProfileId == null) {
            tilPassword.setError(getActivity().getResources().getString(R.string.error_not_empty));
            isError = true;
        }

        if (!password.isEmpty() && password.length() < Constants.PASSWORD_MIN_LENGTH) {
            tilPassword.setError(getActivity().getResources().getString(R.string.error_password_length));
            isError = true;
        }

        if (isError) return;

        if (mSocialProfileId == null) {
            User user = new User();
            user.setPhone(phone);
            user.setPassword(password);

            UserApi api = RestClient.createService(UserApi.class);
            api.add(user, new Callback<User>() {
                @Override
                public void success(User user, Response response) {

                    String backStateName = getActivity().getFragmentManager().getClass().getName();
                    getActivity().getSupportFragmentManager().beginTransaction()
                            .replace(R.id.container, new LoginFragment())
                            .addToBackStack(backStateName)
                            .commit();

                }

                @Override
                public void failure(RetrofitError error) {
                    Toast.makeText(getActivity(), error.toString(), Toast.LENGTH_SHORT).show();
                }
            });
        } else {
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
                    Toast.makeText(getActivity(), "Failure", Toast.LENGTH_SHORT).show();
                }
            });
        }

    }

    private void goToActivation() {
        String backStateName = getActivity().getFragmentManager().getClass().getName();
        getActivity().getSupportFragmentManager().beginTransaction()
                .replace(R.id.container, AccountRestoringFragment.newInstance(true, null))
                .addToBackStack(backStateName)
                .commit();
    }
}
