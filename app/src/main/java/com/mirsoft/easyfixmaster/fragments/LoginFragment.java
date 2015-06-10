package com.mirsoft.easyfixmaster.fragments;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.mirsoft.easyfixmaster.R;
import com.squareup.otto.Bus;

import javax.inject.Inject;

public class LoginFragment extends Fragment {
    private Button mLoginButton;
    private ProgressBar mProgressBar;

    @Inject
    public LoginFragment() {}

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_main, container, false);

        /*mLoginButton = (Button) view.findViewById(R.id.login_button);
        mProgressBar = (ProgressBar) view.findViewById(R.id.login_progress_bar);

        mLoginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username = ((EditText) view.findViewById(R.id.username_edit_text)).getText().toString();
                String password = ((EditText) view.findViewById(R.id.password_edit_text)).getText().toString();
                username(username, password);
            }
        });*/
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    private void showMessage(String message) {
        Toast.makeText(getActivity(), message, Toast.LENGTH_LONG).show();
    }

    private void startLogin() {
        mProgressBar.setVisibility(View.VISIBLE);
        mLoginButton.setEnabled(false);
    }

    private void stopLogin() {
        mProgressBar.setVisibility(View.INVISIBLE);
        mLoginButton.setEnabled(true);
    }
}
