package com.mirsoft.easyfix.fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.mirsoft.easyfix.R;
import com.mirsoft.easyfix.Settings;
import com.mirsoft.easyfix.api.UserApi;
import com.mirsoft.easyfix.models.SocialUser;
import com.mirsoft.easyfix.models.User;
import com.mirsoft.easyfix.networking.ServiceGenerator;
import com.rengwuxian.materialedittext.MaterialEditText;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link RegistrationFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class RegistrationFragment extends Fragment implements View.OnClickListener {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private MaterialEditText etphone;
    private MaterialEditText etpassword;
    private Button btnregistration;


    // TODO: Rename and change types of parameters
    private String mSocialProvider;
    private String mSocialProfileId;


    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment RegistrationFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static RegistrationFragment newInstance(String param1, String param2) {
        RegistrationFragment fragment = new RegistrationFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
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
            mSocialProvider = getArguments().getString(ARG_PARAM1);
            mSocialProfileId = getArguments().getString(ARG_PARAM2);
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
            final Settings settings = new Settings(getActivity());
            UserApi api = ServiceGenerator.createService(UserApi.class, settings);
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
            final Settings settings = new Settings(getActivity());
            UserApi api = ServiceGenerator.createService(UserApi.class, settings);
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
