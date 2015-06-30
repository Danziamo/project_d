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
import android.widget.Toast;

import com.mirsoft.easyfixmaster.adapters.TabsPagerAdapter;
import com.mirsoft.easyfixmaster.api.OrderApi;
import com.mirsoft.easyfixmaster.api.SessionApi;
import com.mirsoft.easyfixmaster.common.OrderType;
import com.mirsoft.easyfixmaster.models.Order;
import com.mirsoft.easyfixmaster.models.Session;
import com.mirsoft.easyfixmaster.service.ServiceGenerator;
import com.mirsoft.easyfixmaster.utils.RoundedImageView;
import com.mirsoft.easyfixmaster.utils.SlidingTabLayout;

import java.util.ArrayList;
import java.util.List;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class TabsActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private static final long DRAWER_CLOSE_DELAY_MS = 250;
    private static final String NAV_ITEM_ID = "navItemId";
    TabsPagerAdapter pagerAdapter;
    ViewPager viewPager;
    TabLayout tabLayout;
    NavigationView navigationView;
    private ArrayList<Order> mOrderList;
    private ArrayList<Order> mFinishedOrderList;

    private final Handler mDrawerActionHandler = new Handler();
    private DrawerLayout mDrawerLayout;
    private ActionBarDrawerToggle mDrawerToggle;
    private int mNavItemId;

    private int progressType = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tabs);

        Toolbar toolbar = (Toolbar)findViewById(R.id.toolbar);;
        setSupportActionBar(toolbar);

        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        viewPager = (ViewPager)findViewById(R.id.pager);
        tabLayout = (TabLayout)findViewById(R.id.tabLayout);

        viewPager.setOffscreenPageLimit(4);
        /*String[] titles = new String[]{
                getString(R.string.icon_map),
                getString(R.string.icon_orders),
                getString(R.string.icon_users),
                getString(R.string.icon_history)};*/
        String[] titles = new String[]{
                "Карта",
                getString(R.string.icon_orders_text),
                getString(R.string.icon_users_text),
                getString(R.string.icon_history_text)};
        pagerAdapter = new TabsPagerAdapter(getSupportFragmentManager(), titles);
        viewPager.setAdapter(pagerAdapter);

        tabLayout.setupWithViewPager(viewPager);
        tabLayout.setTabMode(TabLayout.MODE_FIXED);

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

        mOrderList = new ArrayList<>();
        mFinishedOrderList = new ArrayList<>();
        getOrdersList();
    }

    private void getOrdersList() {
        progressType = 0;
        showProgress(true);
        Settings settings = new Settings(TabsActivity.this);
        OrderApi api = ServiceGenerator.createService(OrderApi.class, settings);
        api.getByUserId(settings.getUserId(), new Callback<ArrayList<Order>>() {
            @Override
            public void success(ArrayList<Order> orders, Response response) {
                if (orders.size() > 0) {
                    progressType += 1;
                    mOrderList.clear();
                    mOrderList = orders;
                    Intent data = new Intent("update");
                    TabsActivity.this.sendBroadcast(data);
                    showProgress(false);
                }
            }

            @Override
            public void failure(RetrofitError error) {
                progressType += 1;
                Toast.makeText(TabsActivity.this, error.toString(), Toast.LENGTH_LONG).show();
                showProgress(false);
            }
        });

        api.getByUserIdAndStatuses(settings.getUserId(), "finished", new Callback<ArrayList<Order>>() {
            @Override
            public void success(ArrayList<Order> orders, Response response) {
                progressType += 1;
                mFinishedOrderList.clear();
                mFinishedOrderList = orders;
                Intent data = new Intent("updatefinished");
                TabsActivity.this.sendBroadcast(data);
                showProgress(false);
            }

            @Override
            public void failure(RetrofitError error) {
                progressType += 1;
                showProgress(false);
            }
        });
    }

    public ArrayList<Order> getNewOrders() {
        ArrayList<Order> list = new ArrayList<>();
        for (int i = 0; i < mOrderList.size(); ++i) {
            if (mOrderList.get(i).getStatus() == OrderType.NEW)
                list.add(mOrderList.get(i));
        }
        return list;
    }

    public ArrayList<Order> getActiveOrders() {
        ArrayList<Order> list = new ArrayList<>();
        for (int i = 0; i < mOrderList.size(); ++i) {
            if (mOrderList.get(i).getStatus() == OrderType.ACTIVE || mOrderList.get(i).getStatus() == OrderType.PENDING)
                list.add(mOrderList.get(i));
        }
        return list;
    }

    public ArrayList<Order> getFinishedOrders() {
        ArrayList<Order> list = new ArrayList<>();
        for (int i = 0; i < mOrderList.size(); ++i) {
            if (mOrderList.get(i).getStatus() == OrderType.FINISHED)
                list.add(mOrderList.get(i));
        }
        return list;
    }

    private void navigate(final int itemId) {
        // perform the actual navigation logic, updating the main content fragment etc
        switch (itemId) {
            case R.id.drawer_item_exit:
                performSignOut();
                break;
            default:

        }
    }

    private void performSignOut() {
        final Settings settings = new Settings(TabsActivity.this);
        SessionApi api = ServiceGenerator.createService(SessionApi.class, settings);
        Session session = new Session();
        api.logout(session, new Callback<Object>() {
            @Override
            public void success(Object o, Response response) {
                settings.setAccessToken(null);
                goToStartPage();
            }

            @Override
            public void failure(RetrofitError error) {
                settings.setAccessToken(null);
                goToStartPage();
            }
        });
    }

    private void goToStartPage() {
        Intent intent = new Intent(TabsActivity.this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        finish();
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

    @Override
    public void onResume() {
        super.onResume();

        getOrdersList();
    }

    private void showProgress(final boolean show) {

    }
}
