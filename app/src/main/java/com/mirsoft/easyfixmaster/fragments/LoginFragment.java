package com.mirsoft.easyfixmaster.fragments;


import android.content.Intent;
import android.os.Bundle;
import android.app.Fragment;
import android.support.design.widget.TextInputLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;
import com.mirsoft.easyfixmaster.R;
import com.mirsoft.easyfixmaster.Settings;
import com.mirsoft.easyfixmaster.TabsActivity;
import com.mirsoft.easyfixmaster.api.SessionApi;
import com.mirsoft.easyfixmaster.models.Session;
import com.mirsoft.easyfixmaster.service.ServiceGenerator;
import com.rengwuxian.materialedittext.MaterialEditText;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link LoginFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class LoginFragment extends Fragment {


    TextInputLayout etPhone;
    TextInputLayout etPassword;
    //MaterialEditText etPhone;
    //MaterialEditText etPassword;
    MaterialDialog dialog;
    private boolean mTask = false;
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;


    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment LoginFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static LoginFragment newInstance(String param1, String param2) {
        LoginFragment fragment = new LoginFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    public LoginFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_login, container, false);

        //etPhone = (MaterialEditText)view.findViewById(R.id.etLogin);
        //etPassword = (MaterialEditText)view.findViewById(R.id.etPassword);

        etPhone = (TextInputLayout)view.findViewById(R.id.tilLogin);
        etPhone.setErrorEnabled(true);
        etPhone.setHint(getString(R.string.login));


        etPassword  = (TextInputLayout)view.findViewById(R.id.tilPassword);
        etPassword.setHint(getString(R.string.password));

        Button btnSignIn = (Button)view.findViewById(R.id.btnSubmit);

        btnSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                makeLogin();
            }
        });

        Button btnForgotPassword = (Button)view.findViewById(R.id.btnForgotPassword);
        btnForgotPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), TabsActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                getActivity().finish();
            }
        });

        return view;
    }

    private void makeLogin() {
        if (mTask) return;
        String password;
        //password = etPassword.getText().toString().trim();
        password = etPassword.getEditText().getText().toString().trim();
        String username;
        //username = etPhone.getText().toString().trim();
        username = etPhone.getEditText().getText().toString().trim();
        boolean isError = false;

        if (username.isEmpty()) {
            etPhone.setError("Не может быть пустым");
            isError = true;
        }

        if (password.isEmpty()) {
            etPassword.setError("Не может быть пустым");
            isError = true;
        }

        if (isError) return;

        Session session = new Session();
        session.username = username;
        session.password = password;
        final Settings settings = new Settings(getActivity());

        showProgress(true);
        SessionApi api = ServiceGenerator.createService(SessionApi.class, settings);
        api.login(session, new Callback<Session>() {
            @Override
            public void success(Session session, Response response) {
                showProgress(false);
                settings.setAccessToken(session.token);
                Intent intent = new Intent(getActivity(), TabsActivity.class);
                startActivity(intent);
                getActivity().finish();
            }

            @Override
            public void failure(RetrofitError error) {
                showProgress(false);
                Toast.makeText(getActivity(), error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void showProgress(final boolean state) {
        mTask = state;
        if (state) {
            MaterialDialog.Builder builder = new MaterialDialog.Builder(getActivity())
                    .title("Авторизация")
                    .content("Пожалуйста подождите")
                    .progress(true, 0);
            dialog = builder.build();
            dialog.show();
        } else {
            dialog.dismiss();
        }
    }
}
