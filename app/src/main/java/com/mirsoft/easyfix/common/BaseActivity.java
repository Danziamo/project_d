package com.mirsoft.easyfix.common;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.afollestad.materialdialogs.MaterialDialog;

import javax.inject.Inject;

public abstract class BaseActivity extends AppCompatActivity {

    MaterialDialog dialog;

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


    protected void showProgress(final boolean state, String title, String content) {
        if (state) {
            MaterialDialog.Builder builder = new MaterialDialog.Builder(this)
                    .title(title)
                    .content(content)
                    .progress(true, 0);
            dialog = builder.build();
            dialog.show();
        } else {
            if(dialog != null && dialog.isShowing()) {
                dialog.dismiss();
            }
        }
    }

    protected void hideProgress(){
        showProgress(false, "", "");
    }
}
