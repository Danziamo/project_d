package com.mirsoft.easyfix.fragments;

import android.app.Fragment;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.mirsoft.easyfix.R;
import com.mirsoft.easyfix.adapters.ProfilePagerAdapter;


public class ProfileFragment extends Fragment {

    private ViewPager viewPager;
    private ProfilePagerAdapter pagerAdapter;
    private TabLayout tabLayout;

    public static ProfileFragment newInstance() {
        return new ProfileFragment();
    }

    public ProfileFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        AppCompatActivity activity = (AppCompatActivity) getActivity();
        String[] titles = new String[]{
                getString(R.string.my_profile),
                getString(R.string.my_speciality)
        };
        viewPager = (ViewPager) view.findViewById(R.id.pager);
        tabLayout = (TabLayout) view.findViewById(R.id.tabLayout);

        viewPager.setOffscreenPageLimit(2);

        pagerAdapter = new ProfilePagerAdapter(activity.getSupportFragmentManager(), titles);
        viewPager.setAdapter(pagerAdapter);



        tabLayout.setupWithViewPager(viewPager);
        tabLayout.setTabMode(TabLayout.MODE_FIXED);

        return view;
    }


}
