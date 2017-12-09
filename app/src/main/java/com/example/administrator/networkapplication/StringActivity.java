package com.example.administrator.networkapplication;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

import static com.example.administrator.networkapplication.MainActivity.showLog;

/**
 * Created by Administrator on 2017/12/9 0009.
 */

public class StringActivity extends AppCompatActivity {

    public static final String STRING_RECEIVER = "com.android.receiver.string";
    private EditText need_string;
    private ShowReceiver showReceiver;
    private ArrayList<String> need_list = new ArrayList<String>();
    private String sd_filer = "";
    private String sd_file = "";
    private String other_file = "";
    private boolean read_start = true;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.string_layout);
        intoFile();
        need_string = (EditText) findViewById(R.id.need_string);
        showReceiver = new ShowReceiver();
        registerReceiver(showReceiver, new IntentFilter(STRING_RECEIVER));
        need_string.setText(SharedUtile.getSharedString(StringActivity.this,"need_string",""));
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(showReceiver);
        SharedUtile.putSharedString(StringActivity.this,"need_string",need_string.getText().toString());
    }

    private void intoFile() {
        sd_filer = Environment.getExternalStorageDirectory().getPath() + "/000";
        sd_file = sd_filer + "/html.txt";
        other_file = sd_filer + "/html_new.txt";
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

    public void start_str(View view) {
        need_list.clear();
        need_list = null;
        need_list = new ArrayList<String>();
        String str = need_string.getText().toString();
        String[] need = str.split("#");
        for (int i = 0; i < need.length; i++) {
            need_list.add(need[i]);
        }
        if (need_list.size() > 0) {
            if (read_start) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        read_start = false;
                        FileInputStream fis = null;
                        InputStreamReader isr = null;
                        BufferedReader br = null; //用于包装InputStreamReader,提高处理性能。因为BufferedReader有缓冲的，而InputStreamReader没有。
                        try {
                            String str = "";
                            fis = new FileInputStream("");// FileInputStream
                            // 从文件系统中的某个文件中获取字节
                            isr = new InputStreamReader(fis);// InputStreamReader 是字节流通向字符流的桥梁,
                            br = new BufferedReader(isr);// 从字符输入流中读取文件中的内容,封装了一个new InputStreamReader的对象
                            while ((str = br.readLine()) != null) {
                                for (String string : need_list) {
                                    if (str.indexOf(string) == -1) {
                                        NetworkService.writeLineFile(other_file, true, str);
                                    }
                                }
                            }
                            // 当读取的一行不为空时,把读到的str的值赋给str1
                            sendBroadcast(new Intent(STRING_RECEIVER).putExtra("type", 1).putExtra("string", "过滤完毕"));
                        } catch (FileNotFoundException e) {
                            System.out.println("找不到指定文件");
                        } catch (IOException e) {
                            System.out.println("读取文件失败");
                        } finally {
                            try {
                                br.close();
                                isr.close();
                                fis.close();
                                // 关闭的时候最好按照先后顺序关闭最后开的先关闭所以先关s,再关n,最后关m
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    }

                }).start();
            }
        } else {
            Toast.makeText(StringActivity.this, "关键词不够", Toast.LENGTH_LONG).show();
        }
    }

    class ShowReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            switch (intent.getIntExtra("type", -1)) {
                case 1:
                    String string = intent.getStringExtra("string");
                    Toast.makeText(StringActivity.this, string, Toast.LENGTH_LONG).show();
                    read_start = true;
                    break;
            }
        }
    }
}
