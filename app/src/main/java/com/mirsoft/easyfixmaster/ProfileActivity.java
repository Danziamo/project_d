package com.mirsoft.easyfixmaster;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;

import com.mirsoft.easyfixmaster.fragments.ProfileFragment;


public class ProfileActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        if (savedInstanceState == null) {
            getFragmentManager().beginTransaction()
                    .add(R.id.container, ProfileFragment.newInstance(null, null))
                    .commit();
        }
    }
}
