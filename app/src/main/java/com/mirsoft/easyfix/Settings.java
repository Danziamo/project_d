package com.mirsoft.easyfix;

import android.content.Context;
import android.content.SharedPreferences;

public class Settings {
    private final static String NAME = "easyfixmaster";

    public enum CacheState {
        NOT_INITIALIZED(0), INITIALIZING(1), INITIALIZED(2);

        private final int value;

        private CacheState(int value) {
            this.value = value;
        }

        public int getId() {
            return value;
        }

        public static CacheState getValue(int id) {
            return CacheState.values()[id];
        }
    }

    public static final int NOT_CACHED = 0;
    public static final int CACHING = 1;
    public static final int CACHED = 2;

    private Context context;

    public Settings(Context context) {
        this.context = context;
    }

    private String getStringValue(String name) {
        return context.getSharedPreferences(NAME, Context.MODE_PRIVATE).getString(name, "");
    }

    private void putStringValue(String name, String value) {
        SharedPreferences.Editor editor = context.getSharedPreferences(NAME, Context.MODE_PRIVATE).edit();
        editor.putString(name, value);
        editor.apply();
    }

    private int getIntValue(String name) {
        return context.getSharedPreferences(NAME, Context.MODE_PRIVATE).getInt(name, 0);
    }

    private void putIntValue(String name, int value) {
        SharedPreferences.Editor editor = context.getSharedPreferences(NAME, Context.MODE_PRIVATE).edit();
        editor.putInt(name, value);
        editor.apply();
    }

    public int getUserId () {
        return getIntValue("user_id");
    }

    public void setUserId(int userId) {
        putIntValue("user_id", userId);
    }

    public String getUserPhone() { return getStringValue("phone"); }

    public void setUserPhone(String phone) { putStringValue("phone", phone); }

    public void setPassword(String password){putStringValue("password", password);}

    public String getPassword(){return getStringValue("password"); }

    public void setSocialProvider (String provider) { putStringValue("socialProvider", provider); }

    public String getSocialProvider () { return getStringValue("socialProvider"); }

    public void setSocialId (String socialId) { putStringValue("socialId", socialId); }

    public String getSocialId () { return getStringValue("socialId"); }

    public String getEndpoint() {
        return getStringValue("endpoint");
    }

    public void setEndpoint(String endpoint) {
        putStringValue("endpoint", endpoint);
    }

    public String getAccessToken() {
        return getStringValue("access_token");
    }

    public void setAccessToken(String accessToken) {
        putStringValue("access_token", accessToken);
    }

    public CacheState getCacheState() {
        return CacheState.getValue(getIntValue("cache_state"));
    }

    public void setCacheState(CacheState cacheState) {
        putIntValue("cache_state", cacheState.getId());
    }
}
