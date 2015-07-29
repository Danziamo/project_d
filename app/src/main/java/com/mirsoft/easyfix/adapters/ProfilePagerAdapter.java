package com.mirsoft.easyfix.adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.mirsoft.easyfix.fragments.BaseFragment;
import com.mirsoft.easyfix.fragments.ProfileInfoFragment;
import com.mirsoft.easyfix.fragments.ProfileSpecialityFragment;

/**
 * Created by mbt on 7/27/15.
 */
public class ProfilePagerAdapter extends FragmentStatePagerAdapter {

    private final String[] TITLES;

    public ProfilePagerAdapter(FragmentManager fm, String[] titles) {
        super(fm);
        this.TITLES = titles;
    }

    private ProfileInfoFragment profileInfoFragment;
    private ProfileSpecialityFragment profileSpecialityFragment;


    @Override
    public BaseFragment getItem(int position) {

        if(position == 0){
            if(profileInfoFragment == null){
                profileInfoFragment = ProfileInfoFragment.newInstance();
            }
            return profileInfoFragment;
        }

        if(position == 1){
            if(profileSpecialityFragment == null){
                profileSpecialityFragment = ProfileSpecialityFragment.newInstance();
            }
            return profileSpecialityFragment;
        }

        return null;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return TITLES[position];
    }

    @Override
    public int getCount() {
        return TITLES.length;
    }
}
