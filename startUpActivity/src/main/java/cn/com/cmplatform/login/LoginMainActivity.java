/**
 * Created by Neng on 2016/8/12.
 */
package cn.com.cmplatform.login;

import android.app.ActionBar;
import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.telephony.TelephonyManager;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.Toast;

import com.gitonway.lee.niftymodaldialogeffects.lib.Effectstype;
import com.gitonway.lee.niftymodaldialogeffects.lib.NiftyDialogBuilder;

import java.lang.ref.WeakReference;
import java.util.List;

import cn.com.cmplatform.game.UnityPlayerNativeActivity;
import cn.com.cmplatform.gameplatform.BuildConfig;
import cn.com.cmplatform.gameplatform.R;
import cn.com.cmplatform.mqtt_push.PushService;
import cn.com.cmplatform.service.User;
import cn.com.cmplatform.service.UserService;
import cn.com.cmplatform.utils.UtilsJudgeEmailAndNumber;


public class LoginMainActivity extends Activity {

    private static class StaticHandler extends Handler {
        private final WeakReference<LoginMainActivity> mActivity;

        public StaticHandler(LoginMainActivity activity) {
            mActivity = new WeakReference<LoginMainActivity>(activity);
        }

        @Override
        public void handleMessage(Message msg) {
            LoginMainActivity activity = mActivity.get();
            if (activity != null) {

            }
        }
    }

    StaticHandler mhandler = new StaticHandler(this) {
        @Override
        public void handleMessage(Message msg) {
            try {
                Log.i("TAG", "mhandler接收到msg=" + msg.what);
                if (msg.obj != null) {
                    String s = msg.obj.toString();
                    if (s.trim().length() > 0) {
                        if (BuildConfig.DEBUG)
                            Log.i("TAG", "mhandler接收到obj=" + s);
                        if (BuildConfig.DEBUG)
                            Log.i("TAG", "开始更新UI");
                        Toast.makeText(LoginMainActivity.this, "登录成功!",
                                Toast.LENGTH_SHORT).show();
                        if (BuildConfig.DEBUG)
                            Log.i("TAG", "更新UI完毕");
                    } else {
                        if (BuildConfig.DEBUG)
                            Log.i("TAG", "没有数据返回不更新");
                    }
                }
            } catch (Exception e) {
                if (BuildConfig.DEBUG)
                    Log.i("TAG", "加载过程出现异常");
                e.printStackTrace();
            }
        }
    };

    StaticHandler mhandlerSend = new StaticHandler(this) {
        @Override
        public void handleMessage(Message msg) {
            try {
                Log.i("TAG", "mhandlerSend接收到msg.what=" + msg.what);
                String s = msg.obj.toString();
                if (msg.what == 1) {
                    Toast.makeText(LoginMainActivity.this,
                            "发送成功!", Toast.LENGTH_SHORT)
                            .show();
                } else {
                    Toast.makeText(LoginMainActivity.this,
                            "发送失败!", Toast.LENGTH_SHORT)
                            .show();
                }
            } catch (Exception ee) {
                if (BuildConfig.DEBUG)
                    Log.i("TAG", "加载过程出现异常");
                ee.printStackTrace();
            }
        }
    };

    TelephonyManager tm;
    String imei;
    String message;

    Button jump;
    Button email;
    Button login;
    Button forget;
    Button register;

    EditText username;
    EditText password;
    NiftyDialogBuilder dialogBuilder;
    private Context ctx;
    private DisplayMetrics dm;
    private String mDeviceID;
    private LoginSocket socketThread;
    private static Boolean isLoginDirect = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        LoginSysExitApplication.getInstance().addActivity(this);
        ctx = LoginMainActivity.this;
        ActivityManager mActivityManager = (ActivityManager) getSystemService(ACTIVITY_SERVICE);
        List<ActivityManager.RunningServiceInfo> mServiceList = mActivityManager
                .getRunningServices(30);

        dm = new DisplayMetrics();
        tm = (TelephonyManager) this.getSystemService(TELEPHONY_SERVICE);
        imei = tm.getDeviceId();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        LinearLayout.LayoutParams imagebtn_params = new LinearLayout.LayoutParams(
                LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        imagebtn_params.setMargins(0, 0, 10, 10);

        final String musicClassName = "cn.com.cmplatform.push.PushService";
        boolean b = MusicServiceIsStart(mServiceList, musicClassName);
        if (!b) {
            // new notifyThread().start(); // 开启单独线程接受推送消息
        }
        initViews();
        setListener();

        if (isLoginDirect) {
            this.loginButtonDown();
            isLoginDirect = false;
        }
    }

    private boolean MusicServiceIsStart(
            List<ActivityManager.RunningServiceInfo> mServiceList,
            String className) {
        for (int i = 0; i < mServiceList.size(); i++) {
            if (className.equals(mServiceList.get(i).service.getClassName())) {
                return true;
            }
        }
        return false;
    }

    class notifyThread extends Thread {
        public void run() {
            startNotify();
        }

        public void startNotify() {
            Editor editor = getSharedPreferences(PushService.TAG, MODE_PRIVATE)
                    .edit();
            editor.putString(PushService.PREF_DEVICE_ID, getContentResolver());
            editor.commit();
            PushService.actionStart(getApplicationContext());
        }
        private String getContentResolver() {
            return "000000001";
        }
    }

    private void initViews() {
        jump = (Button) findViewById(R.id.jump);
        login = (Button) findViewById(R.id.login);
        email = (Button) findViewById(R.id.email_button);
        forget = (Button) findViewById(R.id.forget_button);
        register = (Button) findViewById(R.id.register);
        username = (EditText) findViewById(R.id.username);
        password = (EditText) findViewById(R.id.password);

        ActionBar actionBar = getActionBar();
        actionBar.setDisplayShowHomeEnabled(false);
        actionBar.setDisplayShowTitleEnabled(false);

        // 自动登录功能，获取数据库前两组数据
        final UserService service = new UserService(this);
        List<User> users = service.getScrollData(0, 1);
        if (!users.isEmpty()) {

            String dilution_user_number = users.get(0).getUsername()
                    .substring(3, users.get(0).getUsername().length());
            username.setText(dilution_user_number);
            password.setText(users.get(0).getPassword());
            isLoginDirect = true;
        }

        // 设置底部居中对齐
        dm = getResources().getDisplayMetrics();
        int w_screen = dm.widthPixels;
        int h_screen = dm.heightPixels;
        jump.setWidth(w_screen / 2);
        register.setWidth(w_screen / 2);
    }

    private void loginButtonDown() {

        if (null != username || null != password) {
            String judge_name = username.getText().toString(); // 正则表达式验证号码
            String name = "+86" + username.getText().toString();
            String pass = password.getText().toString();

            if ("".equals(name.trim())) {
                Toast.makeText(LoginMainActivity.this, "请输入用户名", Toast.LENGTH_SHORT)
                        .show();
            }
            UserService userService = new UserService(LoginMainActivity.this);
            UtilsJudgeEmailAndNumber hudge = new UtilsJudgeEmailAndNumber();
            if (hudge.isMobileNO(judge_name)) {
                boolean flag = userService.login(name, pass);
                if (flag) {
                    Toast.makeText(LoginMainActivity.this, "登录成功",
                            Toast.LENGTH_SHORT).show();
                    Bundle b = new Bundle();
                    b.putString("name", name);
                    Intent i = new Intent();
                    i.setClass(LoginMainActivity.this,
                            UnityPlayerNativeActivity.class);
                    i.putExtras(b);
                    startActivity(i);
                    overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                    finish();
                } else {
                    message = new String("DATA000#" + "0002&" + pass + "@" + name + "%" + imei + "/");
                    startSocket();
                }
            } else {
                Toast.makeText(LoginMainActivity.this, "登录失败，请检查您的密码和帐号",
                        Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void setListener() {

        login.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                if (null != username || null != password) {
                    String judge_name = username.getText().toString(); // 正则表达式验证号码
                    String name = "+86" + username.getText().toString();
                    String pass = password.getText().toString();

                    if ("".equals(name.trim())) {
                        Toast.makeText(LoginMainActivity.this, "请输入用户名",
                                Toast.LENGTH_SHORT).show();
                    }
                    UserService userService = new UserService(LoginMainActivity.this);
                    UtilsJudgeEmailAndNumber hudge = new UtilsJudgeEmailAndNumber();
                    if (hudge.isMobileNO(judge_name)) {
                        boolean flag = userService.login(name, pass);
                        if (flag) {
                            Toast.makeText(LoginMainActivity.this, "登录成功",
                                    Toast.LENGTH_SHORT).show();
                            Bundle b = new Bundle();
                            b.putString("name", name);
                            Intent i = new Intent();
                            i.setClass(LoginMainActivity.this,
                                    UnityPlayerNativeActivity.class);
                            i.putExtras(b);
                            startActivity(i);
                            overridePendingTransition(android.R.anim.fade_in,
                                    android.R.anim.fade_out);
                            LoginMainActivity.super.finish();
                        } else {
                            message = new String("DATA000#" + name + "@" + pass + "/");
                            startSocket();
                            Toast.makeText(LoginMainActivity.this,
                                    "登录失败，请检查您的密码和帐号", Toast.LENGTH_SHORT)
                                    .show();
                        }
                    } else {
                        Toast.makeText(LoginMainActivity.this, "登录失败，请检查您的密码和帐号",
                                Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

        register.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginMainActivity.this,
                        LoginRegisterActivity.class);
                startActivity(intent);
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                LoginMainActivity.this.finish();
            }
        });

        email.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                /*
                Intent intent = new Intent(LoginMainActivity.this,
                        LoginRegisterActivity.class);
                startActivity(intent);
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                LoginMainActivity.this.finish();
                */
            }
        });

        forget.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                /*
                Intent intent = new Intent(LoginMainActivity.this,
                        LoginRegisterActivity.class);
                startActivity(intent);
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                LoginMainActivity.this.finish();
                */
            }
        });

        jump.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                dialogBuilder = NiftyDialogBuilder.getInstance(LoginMainActivity.this);
                dialogBuilder
                        .withTitle("注意")
                        .withTitleColor("#FFFFFFFF")
                        .withDividerColor("#11000000")
                        .withMessage("非注册用户，游戏内将无法获得积分！！！为了更好的游戏体验，建议请先注册并登录进行游戏！")
                        .withMessageColor("#FFFFFFFF")
                        .withDialogColor("#00000000")
                        .withIcon(getResources().getDrawable(R.drawable.myicon))
                        .withDuration(700)
                        .withEffect(Effectstype.Fadein)
                        .withButton1Text("注册")
                        .withButton2Text("取消")
                        .isCancelableOnTouchOutside(true)
                        .setButton1Click(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Intent intent = new Intent(
                                        LoginMainActivity.this,
                                        LoginRegisterActivity.class);
                                startActivity(intent);
                                overridePendingTransition(android.R.anim.fade_in,
                                        android.R.anim.fade_out);
                                LoginMainActivity.this.finish();
                            }
                        })
                        .setButton2Click(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                dialogBuilder.dismiss();
                                return;
                            }
                        })
                        .show();
            }
        });
    }

    @Override
    protected void onStop() {

        super.onStop();
        if (BuildConfig.DEBUG)
            Log.i("TAG", "onStop...");
        stopSocket();
    }

    @Override
    protected void onDestroy() {

        super.onDestroy();
        if (BuildConfig.DEBUG)
            Log.i("TAG", "onDestroy...");
        if (dialogBuilder != null)
            dialogBuilder.dismiss();
        stopSocket();
    }

    @Override
    public void onBackPressed() {

        if (BuildConfig.DEBUG)
            Log.i("TAG", "onBackPressed...");
        dialogBuilder = NiftyDialogBuilder.getInstance(LoginMainActivity.this);
        dialogBuilder
                .withTitle("注意")
                .withTitleColor("#FFFFFFFF")
                .withDividerColor("#11000000")
                .withMessage("确认退出应用程序")
                .withMessageColor("#FFFFFFFF")
                .withDialogColor("#00000000")
                .withIcon(getResources().getDrawable(R.drawable.myicon))
                .withDuration(700)
                .withEffect(Effectstype.Fadein)
                .withButton1Text("确认")
                .withButton2Text("取消")
                .isCancelableOnTouchOutside(true)
                .setButton1Click(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        LoginMainActivity.this.finish();
                    }
                })
                .setButton2Click(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialogBuilder.dismiss();
                        return;
                    }
                })
                .show();
    }

    public void startSocket() {

        socketThread = new LoginSocket(mhandler, mhandlerSend, ctx, message);
        socketThread.start();
    }

    private void stopSocket() {

        if (socketThread != null) {
            socketThread.isRun = false;
            socketThread.close();
            socketThread = null;
            if (BuildConfig.DEBUG)
                Log.i("TAG", "Socket已终止");
        }
    }
}