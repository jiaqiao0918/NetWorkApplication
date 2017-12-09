package com.example.administrator.networkapplication;

import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.os.Bundle;
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

public class MainActivity extends AppCompatActivity {

    public static final String RECEVIER = "com.android.network.recevier";
    public static final int GET_URL_HTML = 1001;
    public static final int ERROR_STRING = 1000;
    private final int WRITE_WRITE_SUCCESS = 1;
    private EditText input_str, show_encold, need_str, set_num;
    private TextView show_text, show_other;
    private Button start_stop_network;
    private boolean is_start = false;
    private MainReceiver mainReceiver;


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
        input_str = (EditText) findViewById(R.id.input_str);
        show_encold = (EditText) findViewById(R.id.show_encold);
        need_str = (EditText) findViewById(R.id.need_str);
        set_num = (EditText) findViewById(R.id.set_num);
        show_text = (TextView) findViewById(R.id.show_text);
        show_other = (TextView) findViewById(R.id.show_other);
        start_stop_network = (Button) findViewById(R.id.start_stop_network);
        is_start = false;
//        need_str.setText(SharedUtile.getSharedString(MainActivity.this, "need_str", ""));
        show_other.setText(SharedUtile.getSharedInt(MainActivity.this, "need_num", 0) + "");
        startService(new Intent(MainActivity.this, NetworkService.class));
    }


    public void start_stop(View view) {
        if (SharedUtile.getSharedBoolean(MainActivity.this, "is_start", false)) {
            SharedUtile.putSharedBoolean(MainActivity.this, "is_start", false);
            start_stop_network.setText("开始");
        } else {
            SharedUtile.putSharedBoolean(MainActivity.this, "is_start", true);
            start_stop_network.setText("停止");
        }
        startService(new Intent(MainActivity.this, NetworkService.class));
    }

    public void auto_start(View view) {
        Intent intent = new Intent();
        ComponentName componentName = new ComponentName("com.miui.securitycenter",
                "com.miui.permcenter.autostart.AutoStartManagementActivity");
        intent.setComponent(componentName);
        startActivity(intent);
    }

    public void set_num(View view) {
        String temp_num = set_num.getText().toString() + "";
        if (!temp_num.equals("")) {
            int num = Integer.parseInt(temp_num);
            SharedUtile.putSharedInt(MainActivity.this, "need_num", num);
            Toast.makeText(MainActivity.this, "设置num：" + SharedUtile.getSharedInt(MainActivity.this, "need_num", -1), Toast.LENGTH_LONG).show();
            show_other.setText(SharedUtile.getSharedInt(MainActivity.this, "need_num", 0) + "");
        }
    }

    public void start_other(View view){
        startActivity(new Intent(MainActivity.this,StringActivity.class));
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
        SharedUtile.putSharedString(MainActivity.this, "need_str", need_str.getText().toString());
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
