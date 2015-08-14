package com.mirsoft.easyfix.utils;

import com.mirsoft.easyfix.App;
import com.mirsoft.easyfix.R;
import com.mirsoft.easyfix.common.OrderType;

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

    public static boolean isNewOrPending(OrderType status) {
        if (status == OrderType.NEW)
            return true;
        if (status == OrderType.PENDING)
            return true;

        return false;
    }

    public static String getStringById(int resId) {
        return App.getContext().getResources().getString(resId);
    }
}
