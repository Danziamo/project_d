package com.mirsoft.easyfix.utils;

import com.mirsoft.easyfix.R;

public class HelperUtils {

    public static int getResIdFromSpecialtySlug (String slug) {
        int resId = R.drawable.ic_my_marker;
        if (slug.equals("plumber")) {
            resId = R.drawable.ic_plumbing_marker;
        }
        if (slug.equals("electrician")){
            resId = R.drawable.ic_electro_marker;
        }
        return resId;
    }
}
