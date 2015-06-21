package com.mirsoft.easyfixmaster;

import android.app.ActionBar;
import android.app.Activity;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import com.mirsoft.easyfixmaster.adapters.TabsPagerAdapter;
import com.mirsoft.easyfixmaster.utils.SlidingTabLayout;

public class TestSIgnUp extends AppCompatActivity {

    TabsPagerAdapter pagerAdapter;
    ViewPager viewPager;
    SlidingTabLayout tabs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_sign_up);

        tabs = (SlidingTabLayout)findViewById(R.id.tabs);
        viewPager = (ViewPager)findViewById(R.id.pager);

        Toolbar toolbar = (Toolbar)findViewById(R.id.toolbar);;
        setSupportActionBar(toolbar);

        String[] titles = new String[]{
                getString(R.string.icon_orders),
                getString(R.string.icon_users),
                getString(R.string.icon_history)};
        pagerAdapter = new TabsPagerAdapter(getSupportFragmentManager(), titles);
        viewPager.setAdapter(pagerAdapter);

        tabs.setDistributeEvenly(true);
        tabs.setCustomTabColorizer(new SlidingTabLayout.TabColorizer() {
            @Override
            public int getIndicatorColor(int position) {
                return getResources().getColor(R.color.Toolbar_TabsScrollColor);
            }
        });

        tabs.setViewPager(viewPager);


        //tabHost = (MaterialTabHost)findViewById(R.id.materialTabHost);

    }
}
