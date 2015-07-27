package com.mirsoft.easyfix.utils;

/**
 * Created by mbt on 7/27/15.
 */
public class SocialNetworkHelper {

    /* Asne social network ids */
    public static final int TWITTER_ID = 1;
    public static final int LINKEDIN_ID = 2;
    public static final int GOOGLEPLUS_ID = 3;
    public static final int FACEBOOK_ID = 4;
    public static final int VKONTAKTE_ID = 5;
    public static final int ODNOKLASSNIKI_ID = 6;
    public static final int INSTAGRAM_ID = 7;

    public static final String TWITTER_KEY = "";
    public static final String LINKEDIN_KEY = "";
    public static final String GOOGLEPLUS_KEY = "";
    public static final String FACEBOOK_KEY = "fb";
    public static final String VKONTAKTE_KEY = "vk";
    public static final String ODNOKLASSNIKI_KEY = "ok";
    public static final String INSTAGRAM_KEY = "";


    public static String getKeyById(int id){
        String key = "";
        switch (id){
            case FACEBOOK_ID: key = FACEBOOK_KEY; break;
            case VKONTAKTE_ID: key = VKONTAKTE_KEY; break;
            case ODNOKLASSNIKI_ID: key = ODNOKLASSNIKI_KEY; break;
        }
        return key;
    }

    public static int getIdByKey(String key){
        int id = 0;
        switch (key){
            case FACEBOOK_KEY: id = FACEBOOK_ID; break;
            case VKONTAKTE_KEY: id = VKONTAKTE_ID; break;
            case ODNOKLASSNIKI_KEY: id = ODNOKLASSNIKI_ID; break;
        }
        return id;
    }

}
