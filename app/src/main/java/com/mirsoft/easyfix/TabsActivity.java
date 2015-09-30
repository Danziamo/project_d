package com.mirsoft.easyfix;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.gcm.GoogleCloudMessaging;
import com.google.android.gms.location.LocationServices;
import com.mirsoft.easyfix.adapters.TabsPagerAdapter;
import com.mirsoft.easyfix.common.BaseActivity;
import com.mirsoft.easyfix.fragments.MasterListFragment;
import com.mirsoft.easyfix.models.User;
import com.mirsoft.easyfix.networking.api.OrderApi;
import com.mirsoft.easyfix.networking.api.SessionApi;
import com.mirsoft.easyfix.common.OrderType;
import com.mirsoft.easyfix.fragments.NewOrdersFragment;
import com.mirsoft.easyfix.models.Order;
import com.mirsoft.easyfix.models.Session;
import com.mirsoft.easyfix.networking.RestClient;
import com.mirsoft.easyfix.utils.HelperUtils;
import com.mirsoft.easyfix.views.RoundedImageView;
import com.mirsoft.easyfix.utils.Singleton;
import com.mirsoft.easyfix.views.RoundedTransformation;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.util.ArrayList;

import javax.security.auth.Subject;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class TabsActivity extends BaseActivity implements NavigationView.OnNavigationItemSelectedListener {

    private static final long DRAWER_CLOSE_DELAY_MS = 250;
    private static final String NAV_ITEM_ID = "navItemId";
    public TabsPagerAdapter pagerAdapter;
    ViewPager viewPager;
    TabLayout tabLayout;
    NavigationView navigationView;
    RoundedImageView imageView;
    public FloatingActionButton btnCreateOrder;

    private ArrayList<Order> mOrderList;
    private ArrayList<Order> mFinishedOrderList;

    public Button mapButton;
    public Button ordersListButton;
    public Button myMastersButton;
    public Button myClientsButton;
    public LinearLayout ordersLayout;
    public LinearLayout myOrderslayout;
    protected MaterialDialog dialog;

    private final Handler mDrawerActionHandler = new Handler();
    private DrawerLayout mDrawerLayout;
    private ActionBarDrawerToggle mDrawerToggle;
    private int mNavItemId;

    private int progressType = 0;

    private final int USER_ORDER_PAGE= 2;

    private final int ORDERS_PAGE       = 0;
    private final int MASTRES_BASE_PAGE = 1;
    private final int MY_ORDERS_PAGE    = 2;

    Singleton dc;

    private final static int CONNECTION_FAILURE_RESOLUTION_REQUEST = 9000;
    GoogleApiClient googleApiClient;
    GoogleCloudMessaging gcm;
    String gcmRegistrationId;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tabs);

        Toolbar toolbar = (Toolbar)findViewById(R.id.toolbar);;
        setSupportActionBar(toolbar);

        dc = Singleton.getInstance(this);

        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        viewPager = (ViewPager)findViewById(R.id.pager);
        tabLayout = (TabLayout)findViewById(R.id.tabLayout);
        btnCreateOrder = (FloatingActionButton)findViewById(R.id.btnSwitch);

        mapButton = (Button)findViewById(R.id.map_button);
        ordersListButton = (Button)findViewById(R.id.list_button);
        myMastersButton = (Button)findViewById(R.id.my_masters_button);
        myClientsButton = (Button)findViewById(R.id.myclients_button);

        ordersLayout = (LinearLayout)findViewById(R.id.orders_linear_layout);
        myOrderslayout = (LinearLayout)findViewById(R.id.my_orders_linear_layout);

        btnCreateOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                performCreateNewOrder();
            }
        });

        registrInBackground();

        viewPager.setOffscreenPageLimit(3);
        /*String[] titles = new String[]{
                getString(R.string.icon_orders),
                getString(R.string.icon_users),
                getString(R.string.icon_history)};*/



        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                switch(position){
                    case 0:
                        myOrderslayout.setVisibility(View.INVISIBLE);
                        ordersLayout.setVisibility(View.VISIBLE);
                        btnCreateOrder.setVisibility(View.VISIBLE);
                        dc.currentSelectedTabPage = 0;
                        break;
                    case 1:
                        myOrderslayout.setVisibility(View.INVISIBLE);
                        ordersLayout.setVisibility(View.INVISIBLE);
                        btnCreateOrder.setVisibility(View.INVISIBLE);
                        dc.currentSelectedTabPage = 1;
                        break;
                    case 2:
                        myOrderslayout.setVisibility(View.VISIBLE);
                        ordersLayout.setVisibility(View.INVISIBLE);
                        btnCreateOrder.setVisibility(View.VISIBLE);
                        btnCreateOrder.setVisibility(View.INVISIBLE);
                        dc.currentSelectedTabPage = 2;
                        break;
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        String[] titles = new String[]{
                "Заказы",
                "База мастеров",
                "Мои заказы"};
        pagerAdapter = new TabsPagerAdapter(getSupportFragmentManager(), titles);
        viewPager.setAdapter(pagerAdapter);

        tabLayout.setupWithViewPager(viewPager);
        tabLayout.setTabMode(TabLayout.MODE_FIXED);

        if (null == savedInstanceState) {
            mNavItemId = R.id.drawer_orders;
        } else {
            mNavItemId = savedInstanceState.getInt(NAV_ITEM_ID);
        }

        navigationView = (NavigationView) findViewById(R.id.navigation);
        navigationView.setNavigationItemSelectedListener(this);

        View header = navigationView.inflateHeaderView(R.layout.custom_drawer);
        imageView = (RoundedImageView)header.findViewById(R.id.iVphoto);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(TabsActivity.this, ProfileActivity.class);
                startActivity(intent);
            }
        });

        // select the correct nav menu item
        navigationView.getMenu().findItem(mNavItemId).setChecked(true);

        // set up the hamburger icon to open and close the menu_drawer
        mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout, toolbar, R.string.icon_history_text,
                R.string.icon_orders_text);
        mDrawerLayout.setDrawerListener(mDrawerToggle);
        mDrawerToggle.syncState();

        navigate(mNavItemId);

        mOrderList = new ArrayList<>();
        mFinishedOrderList = new ArrayList<>();
       // viewPager.setCurrentItem(dc.currentSelectedTabPage);

        getCurrentUser();

    }

    private void getCurrentUser() {
        Settings settings = new Settings(TabsActivity.this);
        RestClient.getUserService(false).getById(settings.getUserId(), new Callback<User>() {
            @Override
            public void success(User user, Response response) {
                dc.currentUser = user;
                updateLogo();
            }

            @Override
            public void failure(RetrofitError error) {

            }
        });
    }

    private void updateLogo(){
        HelperUtils.getUserPhotoRequestCreator(TabsActivity.this, dc.currentUser.getPicture())
                .resize(150, 150)
                .into(imageView);
    }

    private void registrInBackground(){
        new AsyncTask<Void,Void,String>(){

            @Override
            protected String doInBackground(Void... voids) {
                String message = "";
                try {
                    if(gcm == null){
                        gcm = GoogleCloudMessaging.getInstance(getApplicationContext());
                    }
                    gcmRegistrationId = gcm.register(getResources().getString(R.string.gcm_sender_id));
                    //patchRegistrationId
                } catch (IOException e) {
                    e.printStackTrace();
                }
                return message;
            }
            @Override
            protected void onPostExecute(String msg) {
              //  Toast.makeText(TabsActivity.this,gcmRegistrationId,Toast.LENGTH_SHORT).show();
            }
        }.execute(null, null, null);
    }

    private void navigate(final int itemId) {
        // perform the actual navigation logic, updating the main content fragment etc
        switch (itemId) {
            case R.id.drawer_item_exit:
                performSignOut();
                break;
            case R.id.drawer_orders:
                swipPage(ORDERS_PAGE);
                break;
            case R.id.drawer_create_order:
                performCreateNewOrder();
                break;
            case R.id.drawer_base_wizards:
                swipPage(MASTRES_BASE_PAGE);
                break;
            case R.id.drawer_my_order:
                swipPage(MY_ORDERS_PAGE);
                break;
            case R.id.drawer_helper:
                performStartActivity(HelpRulesActivity.class);
                break;
            case R.id.drawer_pay:
                performStartActivity(PayActivity.class);
                break;
            case R.id.drawer_about:
                performStartActivity(AboutActivity.class);
                break;
            case R.id.drawer_share:
                //performStartActivity(CommentActivity.class);
                Intent intent = new Intent();
                intent.setAction(Intent.ACTION_SEND);
                intent.setType("text/plain");
                intent.putExtra(Intent.EXTRA_TEXT, "EasyFix test test");
                startActivity(Intent.createChooser(intent,getResources().getString(R.string.share)));
                break;
            default:

        }
    }

    private void performSignOut() {
        final Settings settings = new Settings(TabsActivity.this);
        SessionApi api = RestClient.createService(SessionApi.class);
        Session session = new Session();
        showProgress(true, "Ожидайте", "Выход");
        api.logout(session, new Callback<Object>() {
            @Override
            public void success(Object o, Response response) {
                hideProgress();
                settings.setAccessToken(null);
                goToStartPage();
            }

            @Override
            public void failure(RetrofitError error) {
                hideProgress();
                settings.setAccessToken(null);
                goToStartPage();
            }
        });
    }

    private void performCreateNewOrder(){
        Intent intent = new Intent(TabsActivity.this, ClientOrderDetailsActivity.class);
        intent.putExtra("activityMode", "createOrder");
        startActivity(intent);
    }

    private void performStartActivity(Class<?> activity){
        Intent intent = new Intent(TabsActivity.this, activity);
        startActivity(intent);
    }

    private void performFragmentTransaction(Fragment fragment){
       // getSupportFragmentManager().beginTransaction()
    }

    private void goToStartPage() {
        Intent intent = new Intent(TabsActivity.this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        finish();
    }

    public void swipPage(int page){
        viewPager.setCurrentItem(page);
    }

    @Override
    public boolean onNavigationItemSelected(final MenuItem menuItem) {
        // update highlighted item in the navigation menu
        menuItem.setChecked(true);
        mNavItemId = menuItem.getItemId();

        // allow some time after closing the menu_drawer before performing real navigation
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

        int id = item.getItemId();
        switch(id){
            case R.id.action_refresh:
                if(viewPager.getCurrentItem() == ORDERS_PAGE) {
                    NewOrdersFragment fragment = (NewOrdersFragment) pagerAdapter.getRegisteredFragment(viewPager.getCurrentItem());
                    fragment.getData();
                }
                break;

        }
        return super.onOptionsItemSelected(item);
    };

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

    public void onRestoreInstanceState(@NonNull Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        mNavItemId = savedInstanceState.getInt(NAV_ITEM_ID);
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
        if(dc.fromCreateBasicOrderFragment) {
            viewPager.setCurrentItem(USER_ORDER_PAGE);
            dc.fromCreateBasicOrderFragment = false;
        }

        if(dc.isUserLogoUpdated == true){
            updateLogo();

            MasterListFragment fragment = (MasterListFragment) pagerAdapter.getRegisteredFragment(1);
            fragment.getSpecialties();

            dc.isUserLogoUpdated = false;
        }
    }
}
