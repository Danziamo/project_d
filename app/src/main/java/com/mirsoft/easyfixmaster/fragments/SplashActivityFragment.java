package com.mirsoft.easyfixmaster.fragments;

import android.support.v4.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.view.View.OnClickListener;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.facebook.AccessToken;
import com.facebook.AccessTokenTracker;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.Profile;
import com.facebook.ProfileTracker;
import com.facebook.appevents.AppEventsLogger;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.mirsoft.easyfixmaster.R;
import com.mirsoft.easyfixmaster.Settings;
import com.mirsoft.easyfixmaster.TabsActivity;
import com.mirsoft.easyfixmaster.bus.facebook.FacebookActivityResultBus;
import com.mirsoft.easyfixmaster.common.OrderType;
import com.mirsoft.easyfixmaster.events.facebook.FacebookActivityResultEvent;
import com.squareup.otto.Subscribe;


/**
 * A placeholder fragment containing a simple view.
 */
public class SplashActivityFragment extends Fragment {
    Button btnLogin;
    Button btnSignUp;
    TextView tvInfo;
    CallbackManager callbackManager;
    AccessTokenTracker accessTokenTracker;
    ProfileTracker profileTracker;

    private Object mFacebookActivityResultSubscriber = new Object() {
        @Subscribe
        public void onActivityResultReceived(FacebookActivityResultEvent event) {
            int requestCode = event.getRequestCode();
            int resultCode = event.getResultCode();
            Intent data = event.getData();
            onActivityResult(requestCode, resultCode, data);
        }
    };

    public SplashActivityFragment() {
    }

    public static TestFragment newInstance() {
        TestFragment fragment = new TestFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FacebookSdk.sdkInitialize(getActivity().getApplicationContext());
        callbackManager = CallbackManager.Factory.create();

        accessTokenTracker = new AccessTokenTracker() {
            @Override
            protected void onCurrentAccessTokenChanged(
                    AccessToken oldAccessToken,
                    AccessToken currentAccessToken) {
                // Set the access token using
                // currentAccessToken when it's loaded or set.
            }
        };

        profileTracker = new ProfileTracker() {
            @Override
            protected void onCurrentProfileChanged(
                    Profile oldProfile,
                    Profile currentProfile) {

                Profile temp = oldProfile;
                temp = currentProfile;
                Profile.setCurrentProfile(currentProfile);
                // App code
            }
        };
    }

    @Override
    public void onStart() {
        super.onStart();
        FacebookActivityResultBus.getInstance().register(mFacebookActivityResultSubscriber);
        AppEventsLogger.activateApp(getActivity());
    }

    @Override
    public void onStop() {
        super.onStop();
        FacebookActivityResultBus.getInstance().unregister(mFacebookActivityResultSubscriber);
        AppEventsLogger.deactivateApp(getActivity());
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        accessTokenTracker.stopTracking();
        profileTracker.stopTracking();

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
                openSignUp();
            }
        });

        btnLogin.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                openLogin();
            }
        });

        LoginButton btnFacebookLogin = (LoginButton) view.findViewById(R.id.login_button);
        btnFacebookLogin.setReadPermissions("public_profile");
        btnFacebookLogin.setFragment(this);
        btnFacebookLogin.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                AccessToken.getCurrentAccessToken();
                Profile.fetchProfileForCurrentAccessToken();
                getActivity().getSupportFragmentManager().beginTransaction()
                        .replace(R.id.container, RegistrationFragment.newInstance(null, null))
                        .commit();
            }

            @Override
            public void onCancel() {

            }

            @Override
            public void onError(FacebookException e) {

            }
        });
        ///slideToLeft(splashLayout);
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
                .replace(R.id.container, LoginFragment.newInstance(null, null))
                .addToBackStack(backStateName)
                .commit();
    }

    private void openSignUp() {
        String backStateName = getActivity().getFragmentManager().getClass().getName();
        getActivity().getSupportFragmentManager().beginTransaction()
                .replace(R.id.container, RegistrationFragment.newInstance(null, null))
                .addToBackStack(backStateName)
                .commit();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }

}

