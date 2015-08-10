package com.mirsoft.easyfix;

import android.content.Intent;
import android.support.design.widget.CollapsingToolbarLayout;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TableLayout;

import com.mirsoft.easyfix.adapters.ProfilePagerAdapter;
import com.mirsoft.easyfix.fragments.BaseFragment;
import com.mirsoft.easyfix.fragments.ProfileFragment;
import com.mirsoft.easyfix.fragments.ProfileInfoFragment;


public class ProfileActivity extends AppCompatActivity {
    private static final int SELECT_PICTURE = 1;

    private ViewPager viewPager;
    private ProfilePagerAdapter pagerAdapter;
    private TabLayout tabLayout;
    public FloatingActionButton addNewProffestionBtn;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        Toolbar toolbar = (Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

//        CollapsingToolbarLayout collapsingToolbar =
//                (CollapsingToolbarLayout)findViewById(R.id.collapsing_toolbar);
//        collapsingToolbar.setTitle("Tamasha");

//        loadBackdrop();

//        if (savedInstanceState == null) {
//            getFragmentManager().beginTransaction()
//                    .add(R.id.container, ProfileFragment.newInstance())
//                    .commit();
//        }


        String[] titles = new String[]{
                getString(R.string.my_profile),
                getString(R.string.my_speciality)
        };
        viewPager = (ViewPager) findViewById(R.id.pager);
        tabLayout = (TabLayout) findViewById(R.id.tabLayout);

        viewPager.setOffscreenPageLimit(2);
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }


            @Override
            public void onPageSelected(int position) {
                switch (position){
                    case 0:
                        addNewProffestionBtn.setVisibility(View.GONE);
                        break;
                    case 1:
                        addNewProffestionBtn.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        pagerAdapter = new ProfilePagerAdapter(getSupportFragmentManager(), titles);
        viewPager.setAdapter(pagerAdapter);

        addNewProffestionBtn = (FloatingActionButton)findViewById(R.id.btnAdd);

        tabLayout.setupWithViewPager(viewPager);
        tabLayout.setTabMode(TabLayout.MODE_FIXED);

    }

    @Override
    public void onBackPressed()
    {
        if(getFragmentManager().getBackStackEntryCount() > 0)
            getFragmentManager().popBackStack();
        else
            super.onBackPressed();
    }

    private void changePhoto() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent,
                "Select Picture"), SELECT_PICTURE);
    }

    private void editButtonClicked(){
        int currentItemId = viewPager.getCurrentItem();

        BaseFragment fragment = pagerAdapter.getItem(currentItemId);
        fragment.updateViewsForEdit();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_profile, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        switch (id){
            case android.R.id.home: finish(); return true;
            case R.id.action_edit: editButtonClicked(); return true;
        }

        //noinspection SimplifiableIfStatement
        if (id == android.R.id.home) {
            finish();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
