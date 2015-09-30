package com.mirsoft.easyfix.utils;

import android.content.Context;

import com.mirsoft.easyfix.App;
import com.mirsoft.easyfix.R;
import com.mirsoft.easyfix.common.OrderType;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.RequestCreator;

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

    public static RequestCreator getUserPhotoRequestCreator(Context context, String url){
        RequestCreator requestCreator;
        if(url != null && !url.equals("")){
            url = url.replace("192.168.0.123", "192.168.0.123:1337"); //@TODO remove replace part
            requestCreator = Picasso.with(context).load(url);
        }else{
            requestCreator = Picasso.with(context).load(R.drawable.no_avatar);
        }
        requestCreator.centerCrop()
                .placeholder(R.drawable.no_avatar)
                .error(R.drawable.no_avatar);
        return requestCreator;
    }
}
