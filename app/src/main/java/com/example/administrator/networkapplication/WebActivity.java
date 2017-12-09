package com.example.administrator.networkapplication;

import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.EditText;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

import static com.example.administrator.networkapplication.StringActivity.STRING_RECEIVER;

/**
 * Created by Administrator on 2017/12/9 0009.
 */

public class WebActivity extends AppCompatActivity {

    private EditText show_url;
    private WebView show_html;
    private String file = "";
    private ArrayList<String> list = new ArrayList<String>();
    private int num = 0;
    private int all = 0;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.web_layout);
        file = Environment.getExternalStorageDirectory().getPath() + "/000" + "/html_new.txt";
        show_html = (WebView) findViewById(R.id.show_html);
        show_url = (EditText) findViewById(R.id.show_url);
        show_html.loadUrl("http://m.baidu.com");
        //覆盖WebView默认使用第三方或系统默认浏览器打开网页的行为，使网页用WebView打开
        show_html.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                //返回值是true的时候控制去WebView打开，为false调用系统浏览器或第三方浏览器
                view.loadUrl(url);
                return true;
            }
        });
        if (new File(file).exists()) {
            FileInputStream fis = null;
            InputStreamReader isr = null;
            BufferedReader br = null; //用于包装InputStreamReader,提高处理性能。因为BufferedReader有缓冲的，而InputStreamReader没有。
            try {
                String str = "";
                fis = new FileInputStream(file);// FileInputStream
                // 从文件系统中的某个文件中获取字节
                isr = new InputStreamReader(fis);// InputStreamReader 是字节流通向字符流的桥梁,
                br = new BufferedReader(isr);// 从字符输入流中读取文件中的内容,封装了一个new InputStreamReader的对象
                while ((str = br.readLine()) != null) {
                    MainActivity.showLog(""+(str.indexOf("$")>-1));
                    if(!str.equals("")&&str.indexOf("$")>-1) {
                        list.add(str.replaceAll("\\$", ""));
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
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        } else {
            Toast.makeText(WebActivity.this, "文件不存在", Toast.LENGTH_SHORT).show();
        }
        if (list.size() > 0) {
            num = 0;
            all = list.size();
            Toast.makeText(WebActivity.this, "总数：" + list.size(), Toast.LENGTH_SHORT).show();
            if (all < SharedUtile.getSharedInt(WebActivity.this, "list_all", 0)) {
                num = list.size();
                SharedUtile.putSharedInt(WebActivity.this, "list_num", num);
            } else {
                num = SharedUtile.getSharedInt(WebActivity.this, "list_num", 0);
            }
            SharedUtile.putSharedInt(WebActivity.this, "list_all", list.size());
            show_html.loadUrl(getUrl(list.get(num)));
            show_url.setText(getUrl(list.get(num)));
        }

    }

    public void last_url(View view) {
        if (new File(file).exists() && list.size() > 0) {
            if (num == 0) {
                Toast.makeText(WebActivity.this, "没有了", Toast.LENGTH_SHORT).show();
            } else {
                num--;
                show_url.setText(getUrl(list.get(num)));
                show_html.loadUrl(getUrl(list.get(num)));
            }
            SharedUtile.putSharedInt(WebActivity.this, "list_num", num);
        }
    }

    public void next_url(View view) {
        if (new File(file).exists() && list.size() > 0) {
            if (num == list.size() - 1) {
                Toast.makeText(WebActivity.this, "没有了", Toast.LENGTH_SHORT).show();
            } else {
                num++;
                show_url.setText(getUrl(list.get(num)));
                show_html.loadUrl(getUrl(list.get(num)));
            }
            SharedUtile.putSharedInt(WebActivity.this, "list_num", num);
        }
    }

    public String getUrl(String string) {
        string = string.substring(0, string.indexOf("#"));
        string = "http://m.sanhao3.net/info-" + string + "/";
        return string;
    }

}
