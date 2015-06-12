package com.mirsoft.easyfixmaster;

import android.content.Intent;
import android.os.Bundle;
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
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.container, new MainActivityFragment())
                    .commit();
        }
    }
}