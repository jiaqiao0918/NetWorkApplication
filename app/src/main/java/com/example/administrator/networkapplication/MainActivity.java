package com.example.administrator.networkapplication;

import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;

public class MainActivity extends AppCompatActivity {

    public static final String RECEVIER = "com.android.network.recevier";
    public static final int GET_URL_HTML = 1001;
    public static final int ERROR_STRING = 1000;
    private final int WRITE_WRITE_SUCCESS = 1;
    private EditText set_num;
    private TextView show_other;
    private Button start_stop_network;
    private boolean is_start = false;
    private MainReceiver mainReceiver;
    private String sd_filer = "";
    private String sd_file = "";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if (ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            //无权限，开始申请权限
            ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, WRITE_WRITE_SUCCESS);
        }
        mainReceiver = new MainReceiver();
        registerReceiver(mainReceiver, new IntentFilter(RECEVIER));
        set_num = (EditText) findViewById(R.id.set_num);
        show_other = (TextView) findViewById(R.id.show_other);
        start_stop_network = (Button) findViewById(R.id.start_stop_network);
        is_start = false;
        show_other.setText(SettingUtil.getNeedNum(MainActivity.this) + "");
        startService(new Intent(MainActivity.this, NetworkService.class));

    }

    private void intoFile() {
        sd_filer = Environment.getExternalStorageDirectory().getPath() + "/000";
        sd_file = sd_filer + "/html.txt";
        String other_file = sd_filer + "/html_new.txt";
        File filer = new File(sd_filer);
        File file = new File(sd_file);
        File file02 = new File(other_file);
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
        if (!file02.exists()) {
            try {
                file02.createNewFile();
            } catch (IOException e) {
                showLog(e.getMessage());
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (SettingUtil.getIsStart(MainActivity.this, false)) {
            start_stop_network.setText("停止");
        } else {
            start_stop_network.setText("开始");
        }
    }

    public void start_stop(View view) {
        if (SettingUtil.getIsStart(MainActivity.this, false)) {
            SettingUtil.setIsStart(MainActivity.this, false);
            start_stop_network.setText("开始");
        } else {
            SettingUtil.setIsStart(MainActivity.this, true);
            start_stop_network.setText("停止");
        }
        startService(new Intent(MainActivity.this, NetworkService.class));
    }

    public void auto_start(View view) {
        RomUtil.openAutoStartSetting(MainActivity.this);
    }

    public void set_num(View view) {
        String temp_num = set_num.getText().toString() + "";
        if (!temp_num.equals("")) {
            int num = Integer.parseInt(temp_num);
            SettingUtil.setNeedNum(MainActivity.this, num);
            Toast.makeText(MainActivity.this, "设置num：" + SettingUtil.getNeedNum(MainActivity.this), Toast.LENGTH_LONG).show();
            show_other.setText(SettingUtil.getNeedNum(MainActivity.this) + "");
        }
    }

    public void start_other(View view) {
        startActivity(new Intent(MainActivity.this, StringActivity.class));
    }

    public void start_web(View view) {
        startActivity(new Intent(MainActivity.this, WebActivity.class));
    }

    public void setting_click(View view) {
        startActivity(new Intent(MainActivity.this, SettingActivity.class));
    }

    public void clear_all(View view) {
        String sd_file01 = Environment.getExternalStorageDirectory().getPath() + "/000" + "/html.txt";
        String sd_file02 = Environment.getExternalStorageDirectory().getPath() + "/000" + "/html_new.txt";
        NetworkService.writeLineFile(sd_file01, false, "");
        NetworkService.writeLineFile(sd_file02, false, "");
        SettingUtil.setNeedNum(MainActivity.this, SettingUtil.getStartNum(MainActivity.this));
        show_other.setText(SettingUtil.getNeedNum(MainActivity.this) + "");
        Toast.makeText(MainActivity.this,"清空完毕",Toast.LENGTH_LONG).show();
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case WRITE_WRITE_SUCCESS:
                toastStr("授权成功");
                break;
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    public static void showLog(String str) {
        Log.i("into", str);
    }

    private void toastStr(String str) {
        Toast.makeText(MainActivity.this, str, Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(mainReceiver);
    }

    public class MainReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            switch (intent.getIntExtra("type", -1)) {
                case 1:
                    show_other.setText(intent.getIntExtra("num", -2) + "");
                    break;
            }
        }
    }

}
