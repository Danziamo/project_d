package com.mirsoft.easyfixmaster.fragments;

import android.app.Fragment;
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

import com.mirsoft.easyfixmaster.R;
import com.mirsoft.easyfixmaster.Settings;
import com.mirsoft.easyfixmaster.TabsActivity;


/**
 * A placeholder fragment containing a simple view.
 */
public class SplashActivityFragment extends Fragment {
    Button btnLogin;
    Button btnSignUp;
    TextView tvInfo;

    public SplashActivityFragment() {
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
        getActivity().getFragmentManager().beginTransaction()
                .replace(R.id.fragment, LoginFragment.newInstance(null, null))
                .addToBackStack(backStateName)
                .commit();
    }

    private void openSignUp() {
        String backStateName = getActivity().getFragmentManager().getClass().getName();
        getActivity().getFragmentManager().beginTransaction()
                .replace(R.id.fragment, RegistrationFragment.newInstance(null, null))
                .addToBackStack(backStateName)
                .commit();
    }



   /* // To animate view slide out from left to right
    public void slideToRight(View view){
        TranslateAnimation animate = new TranslateAnimation(0,view.getWidth(),0,0);
        animate.setDuration(500);
        animate.setFillAfter(true);
        view.startAnimation(animate);
        view.setVisibility(View.VISIBLE);
    }
    // To animate view slide out from right to left
    public void slideToLeft(View view){
        TranslateAnimation animate = new TranslateAnimation(0,-view.getWidth(),0,0);
        animate.setDuration(500);
        animate.setFillAfter(true);
        view.startAnimation(animate);
        view.setVisibility(View.VISIBLE);
    }

    // To animate view slide out from top to bottom
    public void slideToBottom(View view){
        TranslateAnimation animate = new TranslateAnimation(0,0,0,view.getHeight());
        animate.setDuration(500);
        animate.setFillAfter(true);
        view.startAnimation(animate);
        view.setVisibility(View.GONE);
    }

    // To animate view slide out from bottom to top
    public void slideToTop(View view){
        TranslateAnimation animate = new TranslateAnimation(0,0,0,-view.getHeight());
        animate.setDuration(500);
        animate.setFillAfter(true);
        view.startAnimation(animate);
        view.setVisibility(View.GONE);
    }*/

  /*  public class ButtonActivity extends MainActivity {
        //Button View

        Button btnLogin = null;
        Button btnSignup = null;

        public ButtonActivity() {
            btnLogin = (Button) btnLogin.findViewById(View.VISIBLE);
            btnSignup = (Button) btnSignup.findViewById(View.VISIBLE);
        }
    }*/

// устанавливаем один обработчик для всех кнопок
/*        btnLogin.setOnClickListener(this);
        btnSignup.setOnClickListener(this);*/

}

