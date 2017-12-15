package com.example.administrator.networkapplication;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Environment;
import android.provider.Settings;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;


/**
 * Created by Administrator on 2017/10/16.
 */

public class RomUtil {

    public static String getRom() {
        try {
            FileInputStream fis = new FileInputStream(new File(Environment.getRootDirectory(), "build.prop"));
            InputStreamReader inReader = new InputStreamReader(fis, "UTF-8");
            BufferedReader bufReader = new BufferedReader(inReader);
            String line = "";
            while ((line = bufReader.readLine()) != null) {
                if (line.toLowerCase().indexOf("flyme") > -1) {
                    return "flyme";//魅族
                } else if (line.toLowerCase().indexOf("miui") > -1) {
                    return "miui";//小米
                } else if (line.toLowerCase().indexOf("emui") > -1) {
                    return "emui";//华为
                } else if (line.toLowerCase().indexOf("color") > -1) {
                    return "color";//OPPO
                } else if (line.toLowerCase().indexOf("function") > -1) {
                    return "function";//vivo
                } else if (line.toLowerCase().indexOf("smartisan") > -1) {
                    return "smartisan";//锤子
                } else if (line.toLowerCase().indexOf("samsung") > -1) {
                    return "samsung";//三星
                }
            }
            bufReader.close();
            inReader.close();
            fis.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "Android";
    }

    public static void openAutoStartSetting(Context context) {
        String rom = getRom();
        Intent intent = new Intent();
        ComponentName componentName = null;
//        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        try {
            if (rom.equals("flyme")) {
                componentName = new ComponentName("com.meizu.safe",
                        "com.meizu.safe.permission.SmartBGActivity");
            } else if (rom.equals("miui")) {
                componentName = new ComponentName("com.miui.securitycenter",
                        "com.miui.permcenter.autostart.AutoStartManagementActivity");
            } else if (rom.equals("emui")) {
                componentName = new ComponentName("com.huawei.systemmanager",
                        "com.huawei.systemmanager.optimize.process.ProtectActivity");
            } else if (rom.equals("color")) {
                componentName = ComponentName.unflattenFromString("com.oppo.safe" +
                        "/.permission.startup.StartupAppListActivity");
                Intent intentOppo = new Intent();
                intentOppo.setClassName("com.oppo.safe/.permission.startup",
                        "StartupAppListActivity");
                if (context.getPackageManager().resolveActivity(intentOppo, 0) == null) {
                    componentName = ComponentName.unflattenFromString("com.coloros.safecenter" +
                            "/.startupapp.StartupAppListActivity");
                }
            } else if (rom.equals("function")) {
                componentName = ComponentName.unflattenFromString("com.iqoo.secure" +
                        "/.safeguard.PurviewTabActivity");
            } else if (rom.equals("smartisan")) {
                componentName = new ComponentName("com.yulong.android.coolsafe",
                        ".ui.activity.autorun.AutoRunListActivity");
            } else if (rom.equals("samsung")) {
                componentName = new ComponentName("com.samsung.android.sm_cn",
                        "com.samsung.android.sm.ui.ram.AutoRunActivity");
            } else if (rom.equals("letv")) {
                intent.setAction("com.letv.android.permissionautoboot");
            }
            intent.setComponent(componentName);
            context.startActivity(intent);
        } catch (Exception e) {
            intent = new Intent(Settings.ACTION_SETTINGS);
            context.startActivity(intent);
        }
    }

}
