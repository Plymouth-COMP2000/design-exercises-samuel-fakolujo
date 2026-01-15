package com.example.restaurantmanagement.utils;

import android.content.Context;
import android.content.SharedPreferences;

public class PrefsManager {

    private static final String PREFS = "restaurant_prefs";

    private static final String KEY_USERNAME = "logged_in_username";
    private static final String KEY_USERTYPE = "logged_in_usertype";

    public static final String KEY_NOTIF_ENABLED = "notif_enabled";
    public static final String KEY_NOTIF_GUEST_STATUS = "notif_guest_status";
    public static final String KEY_NOTIF_STAFF_NEW = "notif_staff_new";

    private final SharedPreferences sp;

    public PrefsManager(Context c) {
        sp = c.getSharedPreferences(PREFS, Context.MODE_PRIVATE);
    }

    public void saveSession(String username, String usertype) {
        sp.edit().putString(KEY_USERNAME, username)
                .putString(KEY_USERTYPE, usertype)
                .apply();
    }

    public String getUsername() { return sp.getString(KEY_USERNAME, null); }
    public String getUsertype() { return sp.getString(KEY_USERTYPE, null); }

    public void clear() { sp.edit().clear().apply(); }

    public void setBool(String key, boolean val) { sp.edit().putBoolean(key, val).apply(); }
    public boolean getBool(String key, boolean def) { return sp.getBoolean(key, def); }


    private static final String KEY_NOTIF = "notif_enabled";

    public void setNotificationsEnabled(boolean enabled) {
        sp.edit().putBoolean(KEY_NOTIF, enabled).apply();
    }

    public boolean isNotificationsEnabled() {
        return sp.getBoolean(KEY_NOTIF, true);
    }



}
