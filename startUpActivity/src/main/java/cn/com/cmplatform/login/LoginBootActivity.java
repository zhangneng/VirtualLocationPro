/**
 * FileName:     ${StartUpActivity.java}
 *
 * @Description: ${todo}(程序启动欢迎界面类)
 * All rights Reserved, Designed By ZTE-ITS
 * Copyright:    Copyright(C) 2010-2014
 * Company       ZTE-ITS WuXi LTD.
 * @author: zhangneng
 * @version V1.0
 * Createdate:         ${date} ${time}
 * <p/>
 * Modification  History:
 * Date         Author        Version        Discription
 * -----------------------------------------------------------------------------------
 * ${date}       wu.zh          1.0             1.0
 * Why & What is modified: <修改原因描述>
 */
package cn.com.cmplatform.login;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Menu;
import android.widget.Toast;

import java.util.List;

import cn.com.cmplatform.gameplatform.R;

public class LoginBootActivity extends Activity {
    private static final String TAG = "log";
    private final int SPLASH_DISPLAY_LENGHT = 2300; // 多长时间最合适？
    private Context ctx;

    public static boolean hasShortCut(Context context) {
        String url = "";
        System.out.println(getSystemVersion());
        if (getSystemVersion() < 8) {
            url = "content://com.android.launcher.settings/favorites?notify=true";
        } else {
            url = "content://com.android.launcher2.settings/favorites?notify=true";
        }
        ContentResolver resolver = context.getContentResolver();
        Cursor cursor = resolver.query(Uri.parse(url), null, "title=?",
                new String[]{context.getString(R.string.app_name)}, null);

        if (cursor != null && cursor.moveToFirst()) {
            cursor.close();
            return true;
        }
        return false;
    }

    private static int getSystemVersion() {
        return android.os.Build.VERSION.SDK_INT;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_startup);
        LoginSysExitApplication.getInstance().addActivity(this);
        ctx = LoginBootActivity.this;
        if (!hasShortCut(ctx)) {
            addShortcut();
        }
        ActivityManager am = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningAppProcessInfo> infoList = am.getRunningAppProcesses();
        List<ActivityManager.RunningServiceInfo> serviceInfos = am.getRunningServices(100);
        long beforeMem = getAvailMemory(LoginBootActivity.this);
        Log.d(TAG, "-----------before memory info : " + beforeMem);
        int count = 0;
        if (infoList != null) {
            for (int i = 0; i < infoList.size(); ++i) {
                ActivityManager.RunningAppProcessInfo appProcessInfo = infoList.get(i);
                Log.d(TAG, "process name : " + appProcessInfo.processName);
                //importance 该进程的重要程度  分为几个级别，数值越低就越重要。
                Log.d(TAG, "importance : " + appProcessInfo.importance);

                // 一般数值大于RunningAppProcessInfo.IMPORTANCE_SERVICE的进程都长时间没用或者空进程了
                // 一般数值大于RunningAppProcessInfo.IMPORTANCE_VISIBLE的进程都是非可见进程，也就是在后台运行着
                if (appProcessInfo.importance > ActivityManager.RunningAppProcessInfo.IMPORTANCE_VISIBLE) {
                    String[] pkgList = appProcessInfo.pkgList;
                    for (int j = 0; j < pkgList.length; ++j) {//pkgList 得到该进程下运行的包名
                        Log.d(TAG, "It will be killed, package name : " + pkgList[j]);
                        am.killBackgroundProcesses(pkgList[j]);
                        count++;
                    }
                }
                long afterMem = getAvailMemory(LoginBootActivity.this);
                Log.d(TAG, "----------- after memory info : " + afterMem);
                Toast.makeText(LoginBootActivity.this, "clear " + count + " process, "
                        + (afterMem - beforeMem) + "M", Toast.LENGTH_LONG).show();

            }
        }

        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                Intent mainIntent = new Intent(LoginBootActivity.this,
                        cn.com.cmplatform.login.LoginMainActivity.class);
                LoginBootActivity.this.startActivity(mainIntent);
                overridePendingTransition(android.R.anim.fade_in,
                        android.R.anim.fade_out);
                LoginBootActivity.this.finish();
            }

        }, SPLASH_DISPLAY_LENGHT);
    }

    //获取可用内存大小
    private long getAvailMemory(Context context) {
        // 获取android当前可用内存大小
        ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        ActivityManager.MemoryInfo mi = new ActivityManager.MemoryInfo();
        am.getMemoryInfo(mi);
        //mi.availMem; 当前系统的可用内存
        //return Formatter.formatFileSize(context, mi.availMem);// 将获取的内存大小规格化
        Log.d(TAG, "可用内存---->>>" + mi.availMem / (1024 * 1024));
        return mi.availMem / (1024 * 1024);
    }
    /**
     * 为程序创建桌面图标
     */
    private void addShortcut() {
        // Add shotcut
        Intent shortcutIntent = new Intent(Intent.ACTION_MAIN);
        Intent shortcut = new Intent("com.android.launcher.action.INSTALL_SHORTCUT");
        Intent.ShortcutIconResource iconRes = Intent.ShortcutIconResource.fromContext(this, R.drawable.ic_launcher);

        shortcut.putExtra(Intent.EXTRA_SHORTCUT_NAME, getString(R.string.app_name));
        shortcut.putExtra("duplicate", false);

        shortcutIntent.setClassName(this, this.getClass().getName());
        shortcut.putExtra(Intent.EXTRA_SHORTCUT_INTENT, shortcutIntent);

        shortcut.putExtra(Intent.EXTRA_SHORTCUT_ICON_RESOURCE, iconRes);
        sendBroadcast(shortcut);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.activity_startup, menu);
        return true;
    }

}