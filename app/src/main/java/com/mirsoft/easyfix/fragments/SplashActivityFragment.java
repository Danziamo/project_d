package com.mirsoft.easyfix.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.RelativeLayout;
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
import com.mirsoft.easyfix.api.SessionApi;
import com.mirsoft.easyfix.common.Constants;
import com.mirsoft.easyfix.models.RestError;
import com.mirsoft.easyfix.models.Session;
import com.mirsoft.easyfix.models.SocialSession;
import com.mirsoft.easyfix.service.ServiceGenerator;
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
public class SplashActivityFragment extends Fragment implements SocialNetworkManager.OnInitializationCompleteListener, OnLoginCompleteListener
        , View.OnClickListener, OnRequestSocialPersonCompleteListener{

    public static final int FACEBOOK = 4;
    public static final int VKONTAKTE = 5;
    public static final int ODNOKLASSNIKI = 6;

    Button btnLogin;
    Button btnSignUp;
    Button btnFacebook;
    Button btnVkontakte;
    Button btnOdnoklassniki;

    SocialNetworkManager mSocialNetworkManager;
    TextView tvInfo;

    public SplashActivityFragment() {
    }

    public static TestFragment newInstance() {
        TestFragment fragment = new TestFragment();
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
        RelativeLayout splashLayout = (RelativeLayout)view.findViewById(R.id.layoutSplash);
        Animation animation = new TranslateAnimation(1500,0,0,1000);
        animation.setDuration(1500);
        splashLayout.startAnimation(animation);
        checkSession();
        tvInfo = (TextView) view.findViewById(R.id.tvInfo);
        btnLogin = (Button) view.findViewById(R.id.btnLogin);
        btnSignUp = (Button) view.findViewById(R.id.btnSignUp);
        btnSignUp.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                openSignUp(null, null);
            }
        });
        btnLogin.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                openLogin();
            }
        });

        btnFacebook = (Button)view.findViewById(R.id.facebook);
        btnFacebook.setOnClickListener(this);
        btnVkontakte = (Button)view.findViewById(R.id.vk);
        btnVkontakte.setOnClickListener(this);
        btnOdnoklassniki = (Button)view.findViewById(R.id.ok);
        btnOdnoklassniki.setOnClickListener(this);

        String FACEBOOK_KEY = getActivity().getString(R.string.facebook_app_id);
        String VKONTAKTE_KEY = getActivity().getString(R.string.vk_app_id);
        /*String OK_APP_ID = getActivity().getString(R.string.ok_app_id);
        String OK_PUBLIC_KEY = getActivity().getString(R.string.ok_public_key);
        String OK_SECRET_KEY = getActivity().getString(R.string.ok_secret_key);*/

        ArrayList<String> fbScope = new ArrayList<String>();
        fbScope.addAll(Arrays.asList("public_profile, email, user_friends"));

        //FacebookSocialNetwork fbNetwork = new FacebookSocialNetwork(this, fbScope);


        /*mSocialNetworkManager = new SocialNetworkManager();

        mSocialNetworkManager.addSocialNetwork(fbNetwork);*/
        /*mSocialNetworkManager.addSocialNetwork(vkNetwork);
        mSocialNetworkManager.addSocialNetwork(okNetwork);*/
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

            /*//Init and add to manager TwitterSocialNetwork
            TwitterSocialNetwork twNetwork = new TwitterSocialNetwork(this, TWITTER_CONSUMER_KEY, TWITTER_CONSUMER_SECRET);
            mSocialNetworkManager.addSocialNetwork(twNetwork);

            //Init and add to manager LinkedInSocialNetwork
            LinkedInSocialNetwork liNetwork = new LinkedInSocialNetwork(this, LINKEDIN_CONSUMER_KEY, LINKEDIN_CONSUMER_SECRET, linkedInScope);
            mSocialNetworkManager.addSocialNetwork(liNetwork);*/

            //Initiate every network from mSocialNetworkManager
            getFragmentManager().beginTransaction().add(mSocialNetworkManager, Constants.SOCIAL_NETWORK_TAG).commit();
            mSocialNetworkManager.setOnInitializationCompleteListener(this);
        } else {
            //if manager exist - get and setup login only for initialized SocialNetworks
            if(!mSocialNetworkManager.getInitializedSocialNetworks().isEmpty()) {
                List<SocialNetwork> socialNetworks = mSocialNetworkManager.getInitializedSocialNetworks();
                for (SocialNetwork socialNetwork : socialNetworks) {
                    socialNetwork.setOnLoginCompleteListener(this);
                    initSocialNetwork(socialNetwork);
                }
            }
        }

        /*getFragmentManager().beginTransaction().add(mSocialNetworkManager, Constants.SOCIAL_NETWORK_TAG).commit();
        mSocialNetworkManager.setOnInitializationCompleteListener(this);*/
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
        SocialNetwork socialNetwork = mSocialNetworkManager.getSocialNetwork(FACEBOOK);
        socialNetwork.setOnRequestCurrentPersonCompleteListener(this);
        socialNetwork.requestCurrentPerson();
        /*String backStateName = getActivity().getFragmentManager().getClass().getName();
        getActivity().getSupportFragmentManager().beginTransaction()
                .replace(R.id.container, LoginFragment.newInstance(null, null))
                .addToBackStack(backStateName)
                .commit();*/
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
            initSocialNetwork(socialNetwork);
        }
    }

    private void initSocialNetwork(SocialNetwork socialNetwork){
        if(socialNetwork.isConnected()){
            switch (socialNetwork.getID()){
                case FACEBOOK:
                    btnFacebook.setText("Show Facebook profile");
                    break;
                case VKONTAKTE:
                    btnVkontakte.setText("Show VK profile");
                    break;
                /*case TWITTER:
                    twitter.setText("Show Twitter profile");
                    break;
                case LINKEDIN:
                    linkedin.setText("Show LinkedIn profile");
                    break;*/
            }
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
                networkId = FACEBOOK;
                break;
            case R.id.vk:
                networkId = VKONTAKTE;
                break;
            /*case R.id.twitter:
                networkId = TWITTER;
                break;
            case R.id.linkedin:
                networkId = LINKEDIN;
                break;*/
        }
        SocialNetwork socialNetwork = mSocialNetworkManager.getSocialNetwork(networkId);
        if(!socialNetwork.isConnected()) {
            if(networkId != 0) {
                socialNetwork.requestLogin();
            } else {
                Toast.makeText(getActivity(), "Wrong networkId", Toast.LENGTH_LONG).show();
            }
        } else {
            Toast.makeText(getActivity(), socialNetwork.getAccessToken().token, Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onRequestSocialPersonSuccess(int i, SocialPerson socialPerson) {
        final Settings settings = new Settings(getActivity());
        SessionApi api = ServiceGenerator.createService(SessionApi.class, settings);
        final SocialSession ss = new SocialSession();
        ss.id = socialPerson.id;
        ss.provider = "fb";
        api.login(ss, new Callback<Session>() {
            @Override
            public void success(Session session, Response response) {
                getActivity().startActivity(new Intent(getActivity(), TabsActivity.class));
                settings.setUserId(session.id);
                settings.setAccessToken(session.token);
                getActivity().finish();
            }

            @Override
            public void failure(RetrofitError error) {
                if (error.getResponse() != null) {
                    RestError body = (RestError)error.getBodyAs(RestError.class);
                    Toast.makeText(getActivity(), body.toString(), Toast.LENGTH_SHORT).show();
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

