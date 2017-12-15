package com.example.administrator.networkapplication;

import android.content.Context;

/**
 * Created by Administrator on 2017/12/15 0015.
 */

public class SettingUtil {
    public static void setStartNum(Context context, int start_num) {
        SharedUtile.putSharedInt(context, "start_num", start_num);
    }

    public static int getStartNum(Context context) {
        return SharedUtile.getSharedInt(context, "start_num", 0);
    }

    public static void setStopNum(Context context, int stop_num) {
        SharedUtile.putSharedInt(context, "stop_num", stop_num);
    }

    public static int getStopNum(Context context) {
        return SharedUtile.getSharedInt(context, "stop_num", 0);
    }

    public static void setUrl01(Context context, String url_01) {
        SharedUtile.putSharedString(context, "url_01", url_01);
    }

    public static String getUrl01(Context context) {
        return SharedUtile.getSharedString(context, "url_01", "");
    }

    public static void setUrl03(Context context, String url_03) {
        SharedUtile.putSharedString(context, "url_03", url_03);
    }

    public static String getUrl03(Context context) {
        return SharedUtile.getSharedString(context, "url_03", "");
    }

    public static void setNeedString(Context context, String need_str) {
        SharedUtile.putSharedString(context, "need_str", need_str);
    }

    public static String getNeedString(Context context) {
        return SharedUtile.getSharedString(context, "need_str", "");
    }

    public static void setNotNeedString(Context context, String not_need_str) {
        SharedUtile.putSharedString(context, "not_need_str", not_need_str);
    }

    public static String getNotNeedString(Context context) {
        return SharedUtile.getSharedString(context, "not_need_str", "");
    }

    public static void setListNum(Context context, int list_num) {
        SharedUtile.putSharedInt(context, "list_num", list_num);
    }

    public static int getListNum(Context context) {
        return SharedUtile.getSharedInt(context, "list_num", 0);
    }

    public static void setListAll(Context context, int list_all) {
        SharedUtile.putSharedInt(context, "list_all", list_all);
    }

    public static int getListAll(Context context) {
        return SharedUtile.getSharedInt(context, "list_all", 0);
    }

    public static void setNeedNum(Context context, int need_num) {
        SharedUtile.putSharedInt(context, "need_num", need_num);
    }

    public static int getNeedNum(Context context) {
        return SharedUtile.getSharedInt(context, "need_num", 0);
    }

    public static void setIsStart(Context context, boolean is_start) {
        SharedUtile.putSharedBoolean(context, "is_start", is_start);
    }

    public static boolean getIsStart(Context context, boolean is_start) {
        return SharedUtile.getSharedBoolean(context, "is_start", is_start);
    }

    public static void setErrorNum(Context context, int error_num) {
        SharedUtile.putSharedInt(context, "error_num", error_num);
    }

    public static int getErrorNum(Context context) {
        return SharedUtile.getSharedInt(context, "error_num", 0);
    }

    public static void setEnclod(Context context, String enclod) {
        SharedUtile.putSharedString(context, "enclod", enclod);
    }

    public static String getEnclod(Context context) {
        return SharedUtile.getSharedString(context, "enclod", "gbk");
    }


    public static void setStartString(Context context, String start_string) {
        SharedUtile.putSharedString(context, "start_string", start_string);
    }

    public static String getStartString(Context context) {
        return SharedUtile.getSharedString(context, "start_string", "");
    }

    public static void setStopString(Context context, String stop_string) {
        SharedUtile.putSharedString(context, "stop_string", stop_string);
    }

    public static String getStopString(Context context) {
        return SharedUtile.getSharedString(context, "stop_string", "");
    }

}
