package com.example.administrator.networkapplication;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

/**
 * Created by Administrator on 2017/12/15 0015.
 */

public class SettingActivity extends AppCompatActivity {

    private EditText url_01, url_021, url_022, url_03, enclod, need_str, not_need_str, start_string, stop_string;
    private View click_view;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.setting_layout);
        url_01 = (EditText) findViewById(R.id.url_01);
        url_021 = (EditText) findViewById(R.id.url_021);
        url_022 = (EditText) findViewById(R.id.url_022);
        url_03 = (EditText) findViewById(R.id.url_03);
        enclod = (EditText) findViewById(R.id.enclod);
        start_string = (EditText) findViewById(R.id.start_string);
        stop_string = (EditText) findViewById(R.id.stop_string);
        need_str = (EditText) findViewById(R.id.need_str);
        not_need_str = (EditText) findViewById(R.id.not_need_str);
        click_view = findViewById(R.id.click_view);
        click_view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                url_01.setText(SettingUtil.getUrl01(SettingActivity.this));
                url_021.setText(SettingUtil.getStartNum(SettingActivity.this) + "");
                url_022.setText(SettingUtil.getStopNum(SettingActivity.this) + "");
                url_03.setText(SettingUtil.getUrl03(SettingActivity.this));
                enclod.setText(SettingUtil.getEnclod(SettingActivity.this));
                start_string.setText(SettingUtil.getStartString(SettingActivity.this));
                stop_string.setText(SettingUtil.getStopString(SettingActivity.this));
                need_str.setText(SettingUtil.getNeedString(SettingActivity.this));
                not_need_str.setText(SettingUtil.getNotNeedString(SettingActivity.this));
            }
        });
    }


    public void save_setting(View view) {
        SettingUtil.setUrl01(SettingActivity.this, url_01.getText().toString());
        SettingUtil.setStartNum(SettingActivity.this, Integer.parseInt(url_021.getText().toString()));
        SettingUtil.setStopNum(SettingActivity.this, Integer.parseInt(url_022.getText().toString()));
        SettingUtil.setUrl03(SettingActivity.this, url_03.getText().toString());
        SettingUtil.setEnclod(SettingActivity.this, enclod.getText().toString());
        SettingUtil.setStartString(SettingActivity.this, start_string.getText().toString());
        SettingUtil.setStopString(SettingActivity.this, stop_string.getText().toString());
        SettingUtil.setNeedString(SettingActivity.this, need_str.getText().toString());
        SettingUtil.setNotNeedString(SettingActivity.this, not_need_str.getText().toString());
        Toast.makeText(SettingActivity.this, "保存成功", Toast.LENGTH_LONG).show();
        startService(new Intent(SettingActivity.this, NetworkService.class));
        this.finish();
    }


}
