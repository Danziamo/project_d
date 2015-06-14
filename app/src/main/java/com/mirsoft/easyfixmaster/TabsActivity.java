package com.mirsoft.easyfixmaster;

import android.app.TabActivity;
import android.graphics.Typeface;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableString;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.TabHost;
import android.widget.TextView;


public class TabsActivity extends ActionBarActivity implements ActionBar.TabListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tabs);
        Typeface font = Typeface.createFromAsset( getAssets(), "fonts/fontawesome-webfont.ttf" );
        Button button = (Button)findViewById( R.id.button2 );
        button.setTypeface(font);
        setUpTabs();
    }

    private void setUpTabs() {
        ActionBar ab = getSupportActionBar();
        ab.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
        ab.setDisplayShowTitleEnabled(true);

        ab.addTab(ab.newTab().setText(getTypeString(getString(R.string.icon_orders) + "\n" + getString(R.string.icon_orders_text))).setTabListener(this));
        ab.addTab(ab.newTab().setText(R.string.icon_history + "\n" + R.string.icon_history_text).setTabListener(this));
        ab.addTab(ab.newTab().setText(R.string.icon_users + "\n" + R.string.icon_users_text).setTabListener(this));
    }

    private SpannableString getTypeString(String test) {
        SpannableString s = new SpannableString(test);
        s.setSpan(new TypefaceSpan(TabsActivity.this, "fontawesome-webfont.ttf"), 0, s.length(),
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        return s;
    }

    @Override
    public void onTabSelected(ActionBar.Tab tab, FragmentTransaction ft) {

    }

    @Override
    public void onTabUnselected(ActionBar.Tab tab, FragmentTransaction ft) {

    }

    @Override
    public void onTabReselected(ActionBar.Tab tab, FragmentTransaction ft) {

    }
}
