package com.mirsoft.easyfix.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.github.gorbin.asne.core.SocialNetwork;
import com.github.gorbin.asne.core.SocialNetworkManager;
import com.github.gorbin.asne.core.listener.OnLoginCompleteListener;
import com.github.gorbin.asne.core.listener.OnRequestSocialPersonCompleteListener;
import com.github.gorbin.asne.core.persons.SocialPerson;
import com.github.gorbin.asne.facebook.FacebookSocialNetwork;
import com.github.gorbin.asne.vk.VkSocialNetwork;
import com.mirsoft.easyfix.R;
import com.mirsoft.easyfix.Settings;
import com.mirsoft.easyfix.TabsActivity;
import com.mirsoft.easyfix.common.Constants;
import com.mirsoft.easyfix.networking.models.RestError;
import com.mirsoft.easyfix.models.Session;
import com.mirsoft.easyfix.models.SocialSession;
import com.mirsoft.easyfix.networking.RestClient;
import com.mirsoft.easyfix.utils.SocialNetworkHelper;
import com.vk.sdk.VKScope;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;


/**
 * A placeholder fragment containing a simple view.
 */
public class SplashActivityFragment extends BaseFragment implements SocialNetworkManager.OnInitializationCompleteListener, OnLoginCompleteListener
        , View.OnClickListener, OnRequestSocialPersonCompleteListener{

    Button btnLogin;
    Button btnSignUp;
    Button btnFacebook;
    Button btnVkontakte;
    Button btnOdnoklassniki;

    SocialNetworkManager mSocialNetworkManager;
    TextView tvInfo;

    public SplashActivityFragment() {
    }

    public static UserOrderListFragment newInstance() {
        UserOrderListFragment fragment = new UserOrderListFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onStop() {
        super.onStop();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_splash, container, false);
        checkSession();
        tvInfo = (TextView) view.findViewById(R.id.tvInfo);
        btnLogin = (Button) view.findViewById(R.id.btnLogin);
        btnSignUp = (Button) view.findViewById(R.id.btnSignUp);

        btnSignUp.setOnClickListener(this);
        btnLogin.setOnClickListener(this);

        btnFacebook = (Button)view.findViewById(R.id.facebook);
        btnFacebook.setOnClickListener(this);
        btnVkontakte = (Button)view.findViewById(R.id.vk);
        btnVkontakte.setOnClickListener(this);
        btnOdnoklassniki = (Button)view.findViewById(R.id.ok);
        btnOdnoklassniki.setOnClickListener(this);

        String VKONTAKTE_KEY = getActivity().getString(R.string.vk_app_id);

        ArrayList<String> fbScope = new ArrayList<String>();
        fbScope.addAll(Arrays.asList("public_profile, email, user_friends"));

        mSocialNetworkManager = (SocialNetworkManager) getFragmentManager().findFragmentByTag(Constants.SOCIAL_NETWORK_TAG);
        if (mSocialNetworkManager == null) {
            mSocialNetworkManager = new SocialNetworkManager();

            //Init and add to manager FacebookSocialNetwork
            FacebookSocialNetwork fbNetwork = new FacebookSocialNetwork(this, fbScope);
            mSocialNetworkManager.addSocialNetwork(fbNetwork);


            //Init and add to manager VkontakteSocialNetwork
            String[] vkScope = new String[] {
                    VKScope.FRIENDS,
                    VKScope.WALL,
                    VKScope.PHOTOS,
                    VKScope.NOHTTPS,
                    VKScope.STATUS,
            };
            VkSocialNetwork vkNetwork = new VkSocialNetwork(this, VKONTAKTE_KEY, vkScope);
            mSocialNetworkManager.addSocialNetwork(vkNetwork);

            //Initiate every network from mSocialNetworkManager
            getFragmentManager().beginTransaction().add(mSocialNetworkManager, Constants.SOCIAL_NETWORK_TAG).commit();
            mSocialNetworkManager.setOnInitializationCompleteListener(this);
        } else {
            //if manager exist - get and setup login only for initialized SocialNetworks
            if(!mSocialNetworkManager.getInitializedSocialNetworks().isEmpty()) {
                List<SocialNetwork> socialNetworks = mSocialNetworkManager.getInitializedSocialNetworks();
                for (SocialNetwork socialNetwork : socialNetworks) {
                    socialNetwork.setOnLoginCompleteListener(this);
                }
            }
        }
        return view;
    }

    private void checkSession() {
        Settings settings = new Settings(getActivity());
        if (settings.getAccessToken().isEmpty()) return;
        Intent intent = new Intent(getActivity(), TabsActivity.class);
        startActivity(intent);
        getActivity().finish();
    }

    private void openLogin() {
        String backStateName = getActivity().getFragmentManager().getClass().getName();
        getActivity().getSupportFragmentManager().beginTransaction()
                .replace(R.id.container, new LoginFragment())
                .addToBackStack(backStateName)
                .commit();
    }

    private void openSocialLogin(int networkId){
        showProgress(true, "Авторизация", "Пожалуйста подождите");
        SocialNetwork socialNetwork = mSocialNetworkManager.getSocialNetwork(networkId);
        if(!socialNetwork.isConnected()) {
            socialNetwork.requestLogin();
        } else {
            socialNetwork.setOnRequestCurrentPersonCompleteListener(this);
            socialNetwork.requestCurrentPerson();
        }
    }

    private void openSignUp(String provider, String profileId) {
        String backStateName = getActivity().getFragmentManager().getClass().getName();
        getActivity().getSupportFragmentManager().beginTransaction()
                .replace(R.id.container, RegistrationFragment.newInstance(provider, profileId))
                .addToBackStack(backStateName)
                .commit();
    }

    @Override
    public void onSocialNetworkManagerInitialized() {
        //when init SocialNetworks - get and setup login only for initialized SocialNetworks
        for (SocialNetwork socialNetwork : mSocialNetworkManager.getInitializedSocialNetworks()) {
            socialNetwork.setOnLoginCompleteListener(this);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onLoginSuccess(int networkId) {
        SocialNetwork socialNetwork = mSocialNetworkManager.getSocialNetwork(networkId);
        socialNetwork.setOnRequestCurrentPersonCompleteListener(this);
        socialNetwork.requestCurrentPerson();
    }

    @Override
    public void onError(int i, String s, String s1, Object o) {
        Toast.makeText(getActivity(), s1, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onClick(View v) {
        int networkId = 0;
        switch (v.getId()){
            case R.id.facebook:
                networkId = SocialNetworkHelper.FACEBOOK_ID;
                break;
            case R.id.vk:
                networkId = SocialNetworkHelper.VKONTAKTE_ID;
                break;

            case R.id.btnLogin:
                openLogin();
                break;
            case R.id.btnSignUp:
                openSignUp(null, null);
                break;
        }

        if(networkId != 0){
            openSocialLogin(networkId);
        }
    }

    @Override
    public void onRequestSocialPersonSuccess(int i, SocialPerson socialPerson) {
        final Settings settings = new Settings(getActivity());
        final SocialSession ss = new SocialSession();
        ss.id = socialPerson.id;
        ss.provider = SocialNetworkHelper.getKeyById(i);
        RestClient.getSessionApi(true).login(ss, new Callback<Session>() {
            @Override
            public void success(Session session, Response response) {
                hideProgress();
                getActivity().startActivity(new Intent(getActivity(), TabsActivity.class));
                settings.setUserId(session.id);
                settings.setAccessToken(session.token);
                getActivity().finish();
            }

            @Override
            public void failure(RetrofitError error) {
                hideProgress();
                if (error.getResponse() != null) {
                    RestError body = (RestError) error.getBodyAs(RestError.class);
                    if (error.getResponse().getStatus() == 404) {
                        if (body.getCode().equals("002_SOCIAL_NOT_VERIFIED")) {
                            goToActivation(true);
                        } else {
                            openSignUp(ss.provider, ss.id);
                        }
                    }
                }
            }
        });
    }

    private void goToActivation(boolean isSocial) {
        String backStateName = getActivity().getFragmentManager().getClass().getName();
        getActivity().getSupportFragmentManager().beginTransaction()
                .replace(R.id.container, AccountActivationFragment.newInstance(isSocial, null))
                .addToBackStack(backStateName)
                .commit();
    }
}

