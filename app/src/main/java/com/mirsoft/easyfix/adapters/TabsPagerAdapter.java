package com.mirsoft.easyfix.adapters;

import android.support.v4.app.FragmentManager;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.mirsoft.easyfix.fragments.MapFragment;
import com.mirsoft.easyfix.common.OrderType;
import com.mirsoft.easyfix.fragments.MasterListFragment;
import com.mirsoft.easyfix.fragments.NewOrdersFragment;
import com.mirsoft.easyfix.fragments.TestFragment;

public class TabsPagerAdapter extends FragmentStatePagerAdapter {

    private final String[] TITLES;

    public TabsPagerAdapter(FragmentManager fm, String[] titles) {
        super(fm);
        this.TITLES = titles;
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return NewOrdersFragment.newInstance(null, null);
            case 1:
                return MasterListFragment.newInstance(null, null);
            case 2:
                return TestFragment.newInstance(OrderType.FINISHED, null);
            default:
                return null;
        }
    }

    @Override
    public String getPageTitle(int position) {
        return TITLES[position];
    }

    @Override
    public int getCount() {
        return TITLES.length;
    }
}
