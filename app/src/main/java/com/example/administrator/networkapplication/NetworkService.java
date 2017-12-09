package com.example.administrator.networkapplication;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Environment;
import android.os.IBinder;
import android.os.PowerManager;
import android.support.annotation.Nullable;
import android.widget.Toast;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.Calendar;

import static com.example.administrator.networkapplication.MainActivity.ERROR_STRING;
import static com.example.administrator.networkapplication.MainActivity.GET_URL_HTML;
import static com.example.administrator.networkapplication.MainActivity.RECEVIER;
import static com.example.administrator.networkapplication.MainActivity.showLog;

/**
 * Created by Administrator on 2017/12/9 0009.
 */

public class NetworkService extends Service {

    private PowerManager.WakeLock mWakeLock;
    private String network_str = "";
    private int num = 1;
    private String url = "http://m.sanhao3.net/info-";
    private String enclod = "gbk";
    private String fengge = "#";
    private ArrayList<String> need_list = new ArrayList<String>();
    private NetworkReceiver networkReceiver;
    private String sd_filer = "";
    private String sd_file = "";
    private int error_num = 0;
    private String last_str = "";
    private boolean is_start = false;


    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        getLock(NetworkService.this);
//        restartService();
        intoFile();
        networkReceiver = new NetworkReceiver();
        registerReceiver(networkReceiver, new IntentFilter(RECEVIER));
        num = SharedUtile.getSharedInt(NetworkService.this, "need_num", 0);
        error_num = SharedUtile.getSharedInt(NetworkService.this, "error_num", 0);
    }

    private void intoFile() {
        sd_filer = Environment.getExternalStorageDirectory().getPath() + "/000";
        sd_file = sd_filer + "/html.txt";
        File filer = new File(sd_filer);
        File file = new File(sd_file);
        if (!filer.exists()) {
            filer.mkdir();
        }
        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException e) {
                showLog(e.getMessage());
            }
        }
    }

    //  同步方法   得到休眠锁
    synchronized private PowerManager.WakeLock getLock(Context context) {
        if (mWakeLock == null) {
            PowerManager mgr = (PowerManager) context.getSystemService(Context.POWER_SERVICE);
            mWakeLock = mgr.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK | PowerManager.ACQUIRE_CAUSES_WAKEUP, NetworkService.class.getName());
        }
        mWakeLock.acquire();
        return (mWakeLock);
    }

    //重启Service
    public void restartService() {
        Calendar cal = Calendar.getInstance();
        Intent intent = new Intent(this, NetworkService.class);
        PendingIntent pintent = PendingIntent.getService(this, 0, intent, 0);
        AlarmManager alarm = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        // 每5分钟启动一次，这个时间值视具体情况而定
        alarm.setRepeating(AlarmManager.RTC_WAKEUP, cal.getTimeInMillis(), 1 * 60 * 1000, pintent);//restartTime分钟，单位：毫秒
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        String[] temp = "院#园#潦".split(fengge);
        is_start = SharedUtile.getSharedBoolean(NetworkService.this,"is_start",false);
        num = SharedUtile.getSharedInt(NetworkService.this, "need_num", 0);
        for (int i = 0; i < temp.length; i++) {
            need_list.add(temp[i]);
        }
        if (need_list.size() > 0) {
            if (is_start) {
                is_start = false;
                getHtml(url + num + "/", "gbk");
            } else {
                is_start = true;
            }
        } else {
            Toast.makeText(NetworkService.this, "关键词为null", Toast.LENGTH_LONG).show();
        }
        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        if (mWakeLock != null) {
            mWakeLock.release();
        }


        startService(new Intent(NetworkService.this, NetworkService.class));
    }

    public void getHtml(final String string, final String encold) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                network_str = "";
                network_str = HtmlUtil.getHtml(NetworkService.this, string, encold);
                sendBroadcast(new Intent(RECEVIER).putExtra("type", GET_URL_HTML).putExtra("get_url_string", network_str).putExtra("get_url", string));
            }
        }).start();
    }

    public static void writeLineFile(String filename, boolean is, String content) {
        if (!new File(filename).exists()) {
            try {
                new File(filename).createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        try {
            FileOutputStream out = new FileOutputStream(filename, is);// true追加，false覆盖
            OutputStreamWriter outWriter = new OutputStreamWriter(out, "UTF-8");
            BufferedWriter bufWrite = new BufferedWriter(outWriter);
            bufWrite.write(content + "\r\n");
            bufWrite.close();
            outWriter.close();
            out.close();
        } catch (Exception e) {
            e.printStackTrace();

        }
    }

    private class NetworkReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {

            switch (intent.getIntExtra("type", -1)) {
                case GET_URL_HTML:
                    String string = intent.getStringExtra("get_url_string") + "";
                    if (!string.equals("") && !string.equals("ERROR")) {
                        string = string.replaceAll("[^\u4E00-\u9FA5]", "");//保留中文
                        if (string.indexOf("小说简介") > -1 && string.indexOf("最新章节") > -1) {
                            int num01 = string.indexOf("小说简介") + "小说简介".length();
                            int num02 = string.indexOf("最新章节");
                            if (num02 < num01) {
                                num02 = string.length();
                            }
                            String temp_str = string.substring(num01, num02);
                            for (String temp : need_list) {
                                if (temp_str.indexOf(temp) > -1) {
                                    writeLineFile(sd_file, true, "$" + num + "#" + temp_str + "$");
                                    break;
                                }
                            }
                        }
                        last_str = "ok";
                    } else {
                        if (last_str.equals("error")) {
                            error_num++;
                            SharedUtile.putSharedInt(NetworkService.this, "error_num", error_num);
                            if (error_num >= 30) {
                                is_start = false;
                            }
                        }
                        last_str = "error";
                    }
                    num++;
                    SharedUtile.putSharedInt(NetworkService.this, "need_num", num);
//                    showLog("service num: "+num);
                    is_start = SharedUtile.getSharedBoolean(NetworkService.this,"is_start",false);
                    sendBroadcast(new Intent(RECEVIER).putExtra("type",1).putExtra("num",num));
                    if (num > 19000) {
                        is_start = false;
                        SharedUtile.putSharedBoolean(NetworkService.this,"is_start",false);
                    }
                    if (is_start && need_list.size() > 0) {
                        getHtml(url + SharedUtile.getSharedInt(NetworkService.this, "need_num", 0) + "/", enclod);
                    }
                    break;
                case ERROR_STRING:
                    is_start = false;
                    break;
            }

        }
    }
}
