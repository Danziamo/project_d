package com.mirsoft.easyfixmaster;

import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.mirsoft.easyfixmaster.adapters.TabsPagerAdapter;
import com.mirsoft.easyfixmaster.utils.RoundedImageView;
import com.mirsoft.easyfixmaster.utils.SlidingTabLayout;

public class TabsActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private static final long DRAWER_CLOSE_DELAY_MS = 250;
    private static final String NAV_ITEM_ID = "navItemId";
    TabsPagerAdapter pagerAdapter;
    ViewPager viewPager;
    SlidingTabLayout tabLayout;
    NavigationView navigationView;

    private final Handler mDrawerActionHandler = new Handler();
    private DrawerLayout mDrawerLayout;
    private ActionBarDrawerToggle mDrawerToggle;
    private int mNavItemId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tabs);

        Toolbar toolbar = (Toolbar)findViewById(R.id.toolbar);;
        setSupportActionBar(toolbar);

        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        viewPager = (ViewPager)findViewById(R.id.pager);
        tabLayout = (SlidingTabLayout)findViewById(R.id.tabs);

        viewPager.setOffscreenPageLimit(4);
        String[] titles = new String[]{
                getString(R.string.icon_map),
                getString(R.string.icon_orders),
                getString(R.string.icon_users),
                getString(R.string.icon_history)};
        pagerAdapter = new TabsPagerAdapter(getSupportFragmentManager(), titles);
        viewPager.setAdapter(pagerAdapter);

        tabLayout.setDistributeEvenly(true);
        tabLayout.setCustomTabColorizer(new SlidingTabLayout.TabColorizer() {
            @Override
            public int getIndicatorColor(int position) {
                return getResources().getColor(R.color.Toolbar_TabsScrollColor);
            }
        });

        tabLayout.setViewPager(viewPager);
        tabLayout.setSelectedIndicatorColors(getResources().getColor(R.color.white));

        Button btnMap = (Button)findViewById(R.id.btnMap);
        btnMap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewPager.setCurrentItem(0);
            }
        });

        Button btnList = (Button)findViewById(R.id.btnList);
        btnList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewPager.setCurrentItem(1);
            }
        });

        if (null == savedInstanceState) {
            mNavItemId = R.id.drawer_item_1;
        } else {
            mNavItemId = savedInstanceState.getInt(NAV_ITEM_ID);
        }

        navigationView = (NavigationView) findViewById(R.id.navigation);
        navigationView.setNavigationItemSelectedListener(this);

        View header = navigationView.inflateHeaderView(R.layout.custom_drawer);
        RoundedImageView imageView = (RoundedImageView)header.findViewById(R.id.iVphoto);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(TabsActivity.this, ProfileActivity.class);
                startActivity(intent);
            }
        });

        // select the correct nav menu item
        navigationView.getMenu().findItem(mNavItemId).setChecked(true);

        // set up the hamburger icon to open and close the drawer
        mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout, toolbar, R.string.icon_history_text,
                R.string.icon_orders_text);
        mDrawerLayout.setDrawerListener(mDrawerToggle);
        mDrawerToggle.syncState();

        navigate(mNavItemId);
    }

    private void navigate(final int itemId) {
        // perform the actual navigation logic, updating the main content fragment etc
    }

    @Override
    public boolean onNavigationItemSelected(final MenuItem menuItem) {
        // update highlighted item in the navigation menu
        menuItem.setChecked(true);
        mNavItemId = menuItem.getItemId();

        // allow some time after closing the drawer before performing real navigation
        // so the user can see what is happening
        mDrawerLayout.closeDrawer(GravityCompat.START);
        mDrawerActionHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                navigate(menuItem.getItemId());
            }
        }, DRAWER_CLOSE_DELAY_MS);
        return true;
    }

    @Override
    public void onConfigurationChanged(final Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        mDrawerToggle.onConfigurationChanged(newConfig);
    }

    @Override
    public boolean onOptionsItemSelected(final MenuItem item) {
        if (item.getItemId() == android.support.v7.appcompat.R.id.home) {
            return mDrawerToggle.onOptionsItemSelected(item);
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        if (mDrawerLayout.isDrawerOpen(GravityCompat.START)) {
            mDrawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    protected void onSaveInstanceState(final Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(NAV_ITEM_ID, mNavItemId);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_tabs, menu);
        return true;
    }
}
