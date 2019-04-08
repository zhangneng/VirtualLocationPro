package cn.com.cmplatform.update;

import cn.com.cmplatform.gameplatform.R;

import android.app.Activity;
import android.app.AlertDialog;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

import android.view.Window;
import android.view.animation.AlphaAnimation;

import android.widget.LinearLayout;
import android.widget.Toast;


public class UpdateMain extends Activity {
    private UpdateApplication app;
    // private int currentVersionCode;
    private String versiontext;
    private UpdataInfo info;
    private LinearLayout splash_main;

    private Handler handler = new Handler() {
        // 判断服务器版本号 和客户端的版本号 是否相同
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (isNeedUpdate(versiontext)) {
                showUpdateDialog();
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.splash);

        app = (UpdateApplication) getApplication();
        splash_main = (LinearLayout) this.findViewById(R.id.splash_main);
        versiontext = getVersion();

        new Thread() { // 让当前的activity延时两秒钟 检查更新
            @Override
            public void run() {
                super.run();
                try {
                    sleep(1000);
                    handler.sendEmptyMessage(0);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }.start();

        // AlphaAnimation animation = new AlphaAnimation(0.0f, 1.0f);
        // animation.setDuration(2500);
        // splash_main.startAnimation(animation);
        /*
         * // TODO Auto-generated method stub PackageManager manager =
		 * Main.this.getPackageManager(); try { PackageInfo info =
		 * manager.getPackageInfo(Main.this.getPackageName(), 0); String
		 * appVersion = info.versionName; // 版本名 currentVersionCode =
		 * info.versionCode; // 版本号 System.out.println(currentVersionCode + " "
		 * + appVersion); } catch (NameNotFoundException e) { // TODO
		 * Auto-generated catch blockd e.printStackTrace(); }
		 * //上面是获取manifest中的版本数据，我是使用versionCode //在从服务器获取到最新版本的versionCode,比较
		 * showUpdateDialog();
		 */
    }

    private void showUpdateDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("检测到新版本");
        builder.setMessage("是否下载更新?");
        builder.setPositiveButton("立即更新",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // TODO Auto-generated method stub
                        Intent it = new Intent(UpdateMain.this,
                                UpdateNotificationActivity.class);
                        startActivity(it);
                        // MapApp.isDownload = true;
                        app.setDownload(true);
                        // loadMainUI();
                        finish();
                    }
                }).setNegativeButton("以后再说",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // TODO Auto-generated method stub
                        loadMainUI();
                    }
                });
        builder.show();
    }

    /**
     * @param versiontext
     * @return 是否需要更新
     */
    private boolean isNeedUpdate(String versiontext) {
        UpdataInfoService service = new UpdataInfoService(this);
        try {
            info = service.getUpdataInfo(R.string.updataurl);
            String version = info.getVersion();
            if (versiontext.equals(version)) {
                loadMainUI();
                return false;
            } else {
                return true;
            }
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(this, "请检查网络连接!", Toast.LENGTH_SHORT).show();
            loadMainUI();
            return false;
        }
    }

    private void loadMainUI() {
        Intent mainIntent = new Intent(this,
                cn.com.cmplatform.login.LoginMainActivity.class);
        mainIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(mainIntent);
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
        UpdateMain.this.finish(); // 把当前activity从任务栈里面移除
    }

    /**
     * 获取当前应用程序的版本号
     *
     * @return
     */
    private String getVersion() {
        try {
            PackageManager manager = getPackageManager();
            PackageInfo info = manager.getPackageInfo(getPackageName(), 0);
            return info.versionName;
        } catch (Exception e) {
            e.printStackTrace();
            return "版本号未知";
        }
    }
}
