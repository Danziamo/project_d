package com.mirsoft.easyfix.fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.mirsoft.easyfix.R;
import com.mirsoft.easyfix.networking.RestClient;

import java.net.HttpURLConnection;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class ForgotPasswordFragment extends BaseFragment {

    EditText etPhone;


    public ForgotPasswordFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_forgot_password, container, false);

        etPhone = (EditText)view.findViewById(R.id.etPhone);

        Button btnSubmit = (Button)view.findViewById(R.id.btnSubmit);
        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                makeRestoreRequest();
            }
        });

        return view;
    }

    private void makeRestoreRequest() {
        String phone = etPhone.getText().toString();

        RestClient.getAccountRestoreApi().sendResetPasswordRequest(phone, new Callback<Object>() {
            @Override
            public void success(Object o, Response response) {
                goToAccountActivation();
            }

            @Override
            public void failure(RetrofitError error) {
                if (error.getKind() == RetrofitError.Kind.HTTP && error.getResponse().getStatus() == HttpURLConnection.HTTP_UNAUTHORIZED) {
                    Toast.makeText(getActivity(), "Такого пользователя не существует", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getActivity(), "Не удалось отправить данные на сервер", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void goToAccountActivation() {
        String backStateName = getActivity().getFragmentManager().getClass().getName();
        getActivity().getSupportFragmentManager().beginTransaction()
                .replace(R.id.container, AccountRestoringFragment.newInstance(false, false, etPhone.getText().toString()))
                .addToBackStack(backStateName)
                .commit();
    }
}
