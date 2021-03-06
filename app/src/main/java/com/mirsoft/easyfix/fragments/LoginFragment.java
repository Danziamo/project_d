package com.mirsoft.easyfix.fragments;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.design.widget.TextInputLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;
import com.mirsoft.easyfix.R;
import com.mirsoft.easyfix.Settings;
import com.mirsoft.easyfix.TabsActivity;
import com.mirsoft.easyfix.common.Constants;
import com.mirsoft.easyfix.models.Session;
import com.mirsoft.easyfix.networking.RestClient;

import org.w3c.dom.Text;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;


public class LoginFragment extends BaseFragment {


    TextInputLayout tilPhone;
    TextInputLayout tilPassword;
    EditText etPhone;
    EditText etPassword;
    MaterialDialog dialog;
    private boolean mTask = false;

    public LoginFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_login, container, false);

        tilPhone = (TextInputLayout)view.findViewById(R.id.tilPhone);
        tilPassword = (TextInputLayout)view.findViewById(R.id.tilPassword);

        etPhone = (EditText)view.findViewById(R.id.etLogin);
        etPassword = (EditText)view.findViewById(R.id.etPassword);

        Settings settings = new Settings(getActivity());
        if (!settings.getUserPhone().isEmpty()) {
            etPhone.setText(settings.getUserPhone());
        }

        Button btnSignIn = (Button)view.findViewById(R.id.btnSubmit);

        btnSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                makeLogin();
            }
        });

        TextView viewForgotPassword = (TextView)view.findViewById(R.id.forgot_password);
        viewForgotPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToForgotPassword();
            }
        });

        return view;
    }

    private void goToForgotPassword() {
        String backStateName = getActivity().getFragmentManager().getClass().getName();
        getActivity().getSupportFragmentManager().beginTransaction()
                .replace(R.id.container, new ForgotPasswordFragment())
                .addToBackStack(backStateName)
                .commit();
    }

    private void makeLogin() {
        if (mTask) return;
        String username;
        final String password;
        password = etPassword.getText().toString().trim();
        username = etPhone.getText().toString().trim();
        boolean isError = false;

        if (username.isEmpty()) {
            tilPhone.setError(getActivity().getString(R.string.error_not_empty));
            isError = true;
        }

        if (password.isEmpty()) {
            tilPassword.setError(getActivity().getResources().getString(R.string.error_not_empty));
            isError = true;
        }

        if (!password.isEmpty() && password.length() < Constants.PASSWORD_MIN_LENGTH) {
            tilPassword.setError(getActivity().getResources().getString(R.string.error_password_length));
            isError = true;
        }

        if (isError) return;

        Session session = new Session();
        session.username = username;
        session.password = password;
        final Settings settings = new Settings(getActivity());

        showProgress(true);
        RestClient.getSessionApi(true).login(session, new Callback<Session>() {
            @Override
            public void success(Session session, Response response) {
                showProgress(false);
                settings.setAccessToken(session.token);
                settings.setUserId(session.id);
                settings.setPassword(password);
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
        showProgress(state, "Авторизация", "Пожалуйста подождите");
    }
}
