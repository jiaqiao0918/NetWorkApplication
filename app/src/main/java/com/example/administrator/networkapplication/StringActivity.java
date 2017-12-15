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
import java.io.FileOutputStream;
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
    private String old_file = "";
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
        need_string.setText(SettingUtil.getNotNeedString(StringActivity.this));
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(showReceiver);
        SettingUtil.setNotNeedString(StringActivity.this, need_string.getText().toString());
    }

    private void intoFile() {
        sd_filer = Environment.getExternalStorageDirectory().getPath() + "/000";
        sd_file = sd_filer + "/html.txt";
        other_file = sd_filer + "/html_new.txt";
        old_file = sd_filer + "/html_old.txt";
        File filer = new File(sd_filer);
        File file = new File(sd_file);
        File file02 = new File(other_file);
        File file03 = new File(old_file);
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
        if (!file03.exists()) {
            try {
                file03.createNewFile();
            } catch (IOException e) {
                showLog(e.getMessage());
            }
        }
    }

    public void start_str(View view) {
        NetworkService.writeLineFile(other_file, false, "");
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
                            fis = new FileInputStream(old_file);// FileInputStream
                            // 从文件系统中的某个文件中获取字节
                            isr = new InputStreamReader(fis);// InputStreamReader 是字节流通向字符流的桥梁,
                            br = new BufferedReader(isr);// 从字符输入流中读取文件中的内容,封装了一个new InputStreamReader的对象
                            int all_num = 0;
                            while ((str = br.readLine()) != null) {
                                int num = 0;
                                all_num++;
                                for (String string : need_list) {
                                    if (str.indexOf(string) > 0) {
                                        break;
                                    } else {
                                        num++;
                                    }
                                }
                                if (num == need_list.size()) {
                                    NetworkService.writeLineFile(other_file, true, str);
                                }
                            }
                            if (all_num == 0) {
                                sendBroadcast(new Intent(STRING_RECEIVER).putExtra("type", 1).putExtra("string", "html_old文件为空"));
                            } else {
                                sendBroadcast(new Intent(STRING_RECEIVER).putExtra("type", 1).putExtra("string", "过滤完毕"));
                            }
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

    public void clearn_html_new(View view) {
        NetworkService.writeLineFile(other_file, false, "");
        Toast.makeText(StringActivity.this, "清空html_new完毕", Toast.LENGTH_LONG).show();
    }

    public void copy_html(View view) {
        copyFile(sd_file, old_file);
        Toast.makeText(StringActivity.this, "复制html完毕", Toast.LENGTH_LONG).show();
    }

    public void copyFile(String fromFile, String toFile) {
        try {
            FileInputStream ins = new FileInputStream(new File(fromFile));
            FileOutputStream out = new FileOutputStream(new File(toFile));
            byte[] b = new byte[1024];
            int n = 0;
            while ((n = ins.read(b)) != -1) {
                out.write(b, 0, n);
            }

            ins.close();
            out.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
