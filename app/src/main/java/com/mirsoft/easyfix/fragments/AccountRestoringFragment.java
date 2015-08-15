package com.mirsoft.easyfix.fragments;


import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
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
import com.mirsoft.easyfix.common.Constants;
import com.mirsoft.easyfix.models.ActivationCode;
import com.mirsoft.easyfix.models.Session;
import com.mirsoft.easyfix.networking.RestClient;

import java.net.HttpURLConnection;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class AccountRestoringFragment extends Fragment {

    EditText et_code_sms;
    Button btnAuthorization;
    TextInputLayout tilCodeSms;
    TextInputLayout tilPassword;
    Button btnResend;


    private static final String SOCIAL_KEY = "IS_SOCIAL";
    private static final String ACTIVATE_KEY = "IS_ACTIVATION";
    private static final String PHONE_KEY = "PHONE";

    private Boolean isSocial;
    private Boolean isActivation;
    private String phone;

    public static AccountRestoringFragment newInstance(Boolean param1, Boolean param2) {
        AccountRestoringFragment fragment = new AccountRestoringFragment();
        Bundle args = new Bundle();
        args.putBoolean(SOCIAL_KEY, param1);
        args.putBoolean(ACTIVATE_KEY, param2);
        fragment.setArguments(args);
        return fragment;
    }

    public static AccountRestoringFragment newInstance(Boolean param1, Boolean param2, String phone) {
        AccountRestoringFragment fragment = new AccountRestoringFragment();
        Bundle args = new Bundle();
        args.putBoolean(SOCIAL_KEY, param1);
        args.putBoolean(ACTIVATE_KEY, param2);
        args.putString(PHONE_KEY, phone);
        fragment.setArguments(args);
        return fragment;
    }

    public AccountRestoringFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            isSocial = getArguments().getBoolean(SOCIAL_KEY, false);
            isActivation = getArguments().getBoolean(ACTIVATE_KEY, false);
            phone = getArguments().getString(PHONE_KEY, null);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_account_restoring, container, false);

        tilCodeSms = (TextInputLayout)view.findViewById(R.id.til_activation_code);
        tilPassword = (TextInputLayout)view.findViewById(R.id.til_new_password);
        et_code_sms = (EditText)view.findViewById(R.id.et_code_sms);
        btnAuthorization = (Button)view.findViewById(R.id.btn_authorization);
        btnAuthorization.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                activate();
            }
        });

        btnResend = (Button)view.findViewById(R.id.btn_resend);
        btnResend.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                resendActivationCode();
            }
        });

        return view;
    }

    private void resendActivationCode() {
        if (isActivation) {

        } else {
            RestClient.getAccountRestoreApi().sendResetPasswordRequest(phone, new Callback<Object>() {
                @Override
                public void success(Object o, Response response) {
                    Toast.makeText(getActivity(), "Выслан новый код", Toast.LENGTH_SHORT).show();
                }

                @Override
                public void failure(RetrofitError error) {
                    Toast.makeText(getActivity(), "Не удалось отправить данные на сервер", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    private boolean validate() {
        String code = et_code_sms.getText().toString().trim();
        String password = tilPassword.getEditText().getText().toString().trim();

        if (code.length() != Constants.ACTIVATION_CODE_LENGTH) {
            tilCodeSms.setError(getActivity().getResources().getString(R.string.error_activation_code_length));
            tilCodeSms.requestFocus();
            return false;
        }

        if (!isActivation && password.length() < Constants.PASSWORD_MIN_LENGTH) {
            tilPassword.setError(getActivity().getResources().getString(R.string.error_password_length));
            tilPassword.requestFocus();
            return false;
        }

        return true;
    }

    private void activate () {
        if (!validate()) return;

        if (isActivation) {

            ActivationCode activationCode = new ActivationCode();
            activationCode.code = et_code_sms.getText().toString().trim();
            if (isSocial) {
                RestClient.getAccountRestoreApi().activateSocial(activationCode, new Callback<Session>() {
                    @Override
                    public void success(Session session, Response response) {
                        Settings settings = new Settings(getActivity());
                        settings.setAccessToken(session.token);
                        settings.setUserId(session.id);
                        openTabsActivity();
                    }

                    @Override
                    public void failure(RetrofitError error) {
                        Toast.makeText(getActivity(), "Cannot activate", Toast.LENGTH_SHORT).show();
                    }
                });
            } else {
                RestClient.getAccountRestoreApi().activate(activationCode, new Callback<Session>() {
                    @Override
                    public void success(Session session, Response response) {
                        Settings settings = new Settings(getActivity());
                        settings.setAccessToken(session.token);
                        settings.setUserId(session.id);
                        openTabsActivity();
                    }

                    @Override
                    public void failure(RetrofitError error) {
                        Toast.makeText(getActivity(), "Cannot activate", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        } else {
            if (!validate()) return;
            ActivationCode code = new ActivationCode();
            code.code = et_code_sms.getText().toString();
            code.password = tilPassword.getEditText().getText().toString();
            code.phone = phone;
            RestClient.getAccountRestoreApi().sendNewPasswordRequest(code, new Callback<Object>() {
                @Override
                public void success(Object o, Response response) {
                    Toast.makeText(getActivity(), "Пароль изменен", Toast.LENGTH_SHORT).show();
                    openLogin();
                }

                @Override
                public void failure(RetrofitError error) {
                    Toast.makeText(getActivity(), "Не удалось отправить данные на сервер", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    private void openLogin() {
        getActivity().getSupportFragmentManager().beginTransaction()
                .replace(R.id.container, new LoginFragment())
                .commit();
    }

    private void openTabsActivity(){
        Intent intent = new Intent(getActivity(), TabsActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        getActivity().finish();
    }

}
