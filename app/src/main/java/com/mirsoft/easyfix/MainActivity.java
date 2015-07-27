package com.mirsoft.easyfix;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //Intent intent = new Intent(this, FixNavigationDrawer.class);
        //Intent intent = new Intent(this, TabsActivity.class);
        Intent intent = new Intent(this, SplashActivity.class);
        startActivity(intent);
        finish();

        /*if (savedInstanceState == null) {
            getFragmentManager().beginTransaction()
                    .add(R.id.container, UserOrderListFragment.newInstance(null, null))
                    .commit();
        }*/
    }
}