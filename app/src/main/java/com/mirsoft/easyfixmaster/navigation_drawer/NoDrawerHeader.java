package com.mirsoft.easyfixmaster.navigation_drawer;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;

import com.mirsoft.easyfixmaster.MainActivityFragment;

import com.mirsoft.easyfixmaster.MainActivity;
import com.mirsoft.easyfixmaster.MainActivityFragment;
import com.mirsoft.easyfixmaster.R;
import com.mirsoft.easyfixmaster.Settings;
import it.neokree.materialnavigationdrawer.MaterialNavigationDrawer;

/**
 * Created by neokree on 18/01/15.
 */
public class NoDrawerHeader extends MaterialNavigationDrawer {
    @Override
    public void init(Bundle savedInstanceState) {
        // create sections
        this.addSection(newSection("Section 1", new MainActivityFragment()));
        this.addSection(newSection("Section 2",new MainActivityFragment()));
        //this.addSection(newSection("Section 3", R.drawable.ic_mic_white_24dp,new FragmentButton()).setSectionColor(Color.parseColor("#9c27b0")));
        //this.addSection(newSection("Section",R.drawable.ic_hotel_grey600_24dp,new FragmentButton()).setSectionColor(Color.parseColor("#03a9f4")));

        // create bottom section
        //this.addBottomSection(newSection("Bottom Section",R.drawable.ic_settings_black_24dp,new Intent(this,Settings.class)));
    }
}
