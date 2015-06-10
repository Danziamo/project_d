package com.mirsoft.easyfixmaster.common;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;

import com.mirsoft.easyfixmaster.BusProvider;

import javax.inject.Inject;

public abstract class BaseActivity extends Activity {

    @Inject

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    public <T extends View> T findById(int resId) {
        return (T) findViewById(resId);
    }
}
