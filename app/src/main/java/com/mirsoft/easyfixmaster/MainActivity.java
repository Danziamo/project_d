package com.mirsoft.easyfixmaster;

import android.os.Bundle;
import android.app.Fragment;
import android.support.v7.app.ActionBarActivity;

public class MainActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //Intent intent = new Intent(this, FixNavigationDrawer.class);
        /*Intent intent = new Intent(this, TestSIgnUp.class);
        startActivity(intent);
        finish();*/

        if (savedInstanceState == null) {
            getFragmentManager().beginTransaction()
                    .add(R.id.container, ProfileFragment.newInstance(null, null))
                    .commit();
        }
    }
}