package com.mirsoft.easyfix.fragments;



import android.support.v4.app.Fragment;

import com.afollestad.materialdialogs.MaterialDialog;
import com.mirsoft.easyfix.R;

/**
 * Created by mbt on 7/27/15.
 */
public class BaseFragment extends Fragment {

    protected MaterialDialog dialog;

    protected void showProgress(final boolean state, String title, String content) {
        if (state) {
            MaterialDialog.Builder builder = new MaterialDialog.Builder(getActivity())
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
