package com.example.administrator.networkapplication;

import android.app.Activity;
import android.app.Service;
import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by jiaqiao on 2017/9/12/0012.
 */

public class SharedUtile {

    private static final String SHARED="setting";

    //Actvity
    public static String getSharedString(Activity activity, String key, String default_str) {
        String temp = "";
        SharedPreferences userSettings = activity.getSharedPreferences(SHARED, 0);
        temp = userSettings.getString(key, "");
        return temp;
    }

    public static boolean getSharedBoolean(Activity activity, String key, boolean default_bool) {
        boolean temp = default_bool;
        SharedPreferences userSettings = activity.getSharedPreferences(SHARED, 0);
        temp = userSettings.getBoolean(key, default_bool);
        return temp;
    }

    public static int getSharedInt(Activity activity, String key, int default_int) {
        int temp = default_int;
        SharedPreferences userSettings = activity.getSharedPreferences(SHARED, 0);
        temp = userSettings.getInt(key, default_int);
        return temp;
    }

    public static Long getSharedLong(Context activity, String key, Long default_long) {
        Long temp = default_long;
        SharedPreferences userSettings = activity.getSharedPreferences(SHARED, 0);
        temp = userSettings.getLong(key, default_long);
        return temp;
    }

    public static void putSharedString(Activity activity, String key, String put_str) {
        activity.getSharedPreferences(SHARED, 0).edit().putString(key, put_str).commit();
    }

    public static void putSharedBoolean(Activity activity, String key, boolean put_bool) {
        activity.getSharedPreferences(SHARED, 0).edit().putBoolean(key, put_bool).commit();
    }

    public static void putSharedInt(Activity activity, String key, int put_bool) {
        activity.getSharedPreferences(SHARED, 0).edit().putInt(key, put_bool).commit();
    }

    public static void putSharedLong(Context activity, String key, long value) {
        activity.getSharedPreferences(SHARED, 0).edit().putLong(key, value).commit();
    }


    //Service
    public static String getSharedString(Service activity, String key, String default_str) {
        String temp = "";
        SharedPreferences userSettings = activity.getSharedPreferences(SHARED, 0);
        temp = userSettings.getString(key, "");
        return temp;
    }

    public static boolean getSharedBoolean(Service activity, String key, boolean default_bool) {
        boolean temp = default_bool;
        SharedPreferences userSettings = activity.getSharedPreferences(SHARED, 0);
        temp = userSettings.getBoolean(key, default_bool);
        return temp;
    }

    public static int getSharedInt(Service activity, String key, int default_int) {
        int temp = default_int;
        SharedPreferences userSettings = activity.getSharedPreferences(SHARED, 0);
        temp = userSettings.getInt(key, default_int);
        return temp;
    }

    public static void putSharedString(Service activity, String key, String put_str) {
        activity.getSharedPreferences(SHARED, 0).edit().putString(key, put_str).commit();
    }

    public static void putSharedBoolean(Service activity, String key, boolean put_bool) {
        activity.getSharedPreferences(SHARED, 0).edit().putBoolean(key, put_bool).commit();
    }

    public static void putSharedInt(Service activity, String key, int put_bool) {
        activity.getSharedPreferences(SHARED, 0).edit().putInt(key, put_bool).commit();
    }

    //Context
    public static String getSharedString(Context activity, String key, String default_str) {
        String temp = "";
        SharedPreferences userSettings = activity.getSharedPreferences(SHARED, 0);
        temp = userSettings.getString(key, "");
        return temp;
    }

    public static boolean getSharedBoolean(Context activity, String key, boolean default_bool) {
        boolean temp = default_bool;
        SharedPreferences userSettings = activity.getSharedPreferences(SHARED, 0);
        temp = userSettings.getBoolean(key, default_bool);
        return temp;
    }

    public static int getSharedInt(Context activity, String key, int default_int) {
        int temp = default_int;
        SharedPreferences userSettings = activity.getSharedPreferences(SHARED, 0);
        temp = userSettings.getInt(key, default_int);
        return temp;
    }

    public static void putSharedString(Context activity, String key, String put_str) {
        activity.getSharedPreferences(SHARED, 0).edit().putString(key, put_str).commit();
    }

    public static void putSharedBoolean(Context activity, String key, boolean put_bool) {
        activity.getSharedPreferences(SHARED, 0).edit().putBoolean(key, put_bool).commit();
    }

    public static void putSharedInt(Context activity, String key, int put_bool) {
        activity.getSharedPreferences(SHARED, 0).edit().putInt(key, put_bool).commit();
    }

}