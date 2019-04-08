/**
 * Created by Neng on 2016/8/12.
 */
package cn.com.cmplatform.login;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.lang.ref.WeakReference;

import cn.com.cmplatform.gameplatform.BuildConfig;
import cn.com.cmplatform.gameplatform.R;
import cn.com.cmplatform.service.User;
import cn.com.cmplatform.service.UserService;
import cn.com.cmplatform.update.UpdateMain;
import cn.com.cmplatform.utils.UtilsSIMCardInfo;


public class LoginRegisterActivity extends Activity implements
        android.view.View.OnClickListener {

    private final StaticHandler mhandlerSend = new StaticHandler(this) {
        @Override
        public void handleMessage(Message msg) {
            try {
                if (BuildConfig.DEBUG)
                    Log.i("TAG", "mhandlerSend接收到msg.what=" + msg.what);
                String s = msg.obj.toString();
                if (msg.what == 1) {
                    Toast.makeText(LoginRegisterActivity.this,
                            "发送成功!", Toast.LENGTH_SHORT)
                            .show();
                } else {
                    Toast.makeText(LoginRegisterActivity.this,
                            "发送失败!", Toast.LENGTH_SHORT)
                            .show();
                }
            } catch (Exception e) {
                if (BuildConfig.DEBUG)
                    Log.i("TAG", "加载过程出现异常");
                e.printStackTrace();
            }
        }
    };
    private String imei;
    private String name;
    private String pass;
    private final StaticHandler mhandler = new StaticHandler(this) {
        @Override
        public void handleMessage(Message msg) {
            try {
                Log.i("TAG", "mhandler接收到msg=" + msg.what);
                if (msg.obj != null) {

                    String s = msg.obj.toString();
                    if (s.trim().length() > 0) {
                        int message_type = Integer.parseInt(s);
                        if (message_type == 0) {
                            Log.i("TAG", "mhandler接收到obj=" + s);
                            UserService userService = new UserService(LoginRegisterActivity.this);
                            User user = new User(name, pass);
                            int flag = userService.register(user);
                            if (flag == 1) {

                                Toast.makeText(LoginRegisterActivity.this, "注册成功",
                                        Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(LoginRegisterActivity.this, LoginMainActivity.class);
                                startActivity(intent);
                                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                                LoginRegisterActivity.this.finish();
                            }
                        } else {
                            Toast.makeText(LoginRegisterActivity.this, "服务器错误",
                                    Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Log.i("TAG", "没有数据返回不更新");
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    };
    private String message;
    private Button register;
    private EditText username;
    private EditText password;
    private Context ctx;
    private LoginSocket socketThread;
    private TelephonyManager tm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        LoginSysExitApplication.getInstance().addActivity(this);
        ctx = LoginRegisterActivity.this;
        tm = (TelephonyManager) this.getSystemService(TELEPHONY_SERVICE);
        initViews();
    }

    public void startSocket() {
        socketThread = new LoginSocket(mhandler, mhandlerSend, ctx, message);
        socketThread.start();
    }

    private void initViews() {
        username = (EditText) findViewById(R.id.usernameRegister);
        password = (EditText) findViewById(R.id.passwordRegister);
        register = (Button) findViewById(R.id.register_button);
        register.setOnClickListener(this);

        ActionBar actionBar = getActionBar();
        actionBar.setDisplayShowHomeEnabled(false);
        actionBar.setDisplayShowTitleEnabled(false);
        actionBar.setDisplayHomeAsUpEnabled(true);

        UtilsSIMCardInfo phone_number = new UtilsSIMCardInfo(this);
        String phone_user_number = phone_number.getNativePhoneNumber();
        if (phone_user_number.length() == 0) {
            return;
        } else {
            String dilution_user_number = phone_user_number.substring(3,
                    phone_user_number.length());
            username.setText(dilution_user_number);
        }

        imei = tm.getDeviceId();
    }

    /**
     * DATA000#0001&999999@19923949494%1234567890123456$000000/
     */
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.register_button:
                if (null != username && null != password) {
                    name = "+86" + username.getText().toString();
                    pass = password.getText().toString();
                    if ("".equals(name.trim()) && "".equals(pass.trim())) {
                        Toast.makeText(LoginRegisterActivity.this, "请输入用户名和密码",
                                Toast.LENGTH_LONG).show();
                        return;
                    }

                    message = new String("DATA000#" + "0001&" + pass + "@" + name + "%" + imei + "/");
                    startSocket();
                }
                break;
            default:
                this.onBackPressed();
                break;
        }
    }

    private void stopSocket() {
        if (socketThread != null) {

            socketThread.isRun = false;
            socketThread.close();
            socketThread = null;
            Log.i("TAG", "socket stop");
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.e("TAG", "onStop~~~");
        stopSocket();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                break;
            case Menu.FIRST + 1:
                Intent i = new Intent();
                i.setClass(LoginRegisterActivity.this, UpdateMain.class);
                startActivity(i);
                break;
            case Menu.FIRST + 2:
                break;
            case Menu.FIRST + 3:
                break;
        }
        return false;
    }

    /*
    public boolean onCreateOptionsMenu(Menu menu) {

        menu.add(Menu.NONE, Menu.FIRST + 1, 1, "显示").setIcon(
                android.R.drawable.ic_menu_search);

        menu.add(Menu.NONE, Menu.FIRST + 2, 2, "编辑").setIcon(
                android.R.drawable.ic_menu_edit);

        menu.add(Menu.NONE, Menu.FIRST + 3, 3, "帮助").setIcon(
                android.R.drawable.ic_menu_help);
        return true;
    }
    */

    public void onBackPressed() {
        Intent intent = new Intent(LoginRegisterActivity.this, LoginMainActivity.class);
        startActivity(intent);
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
        super.onBackPressed();
        LoginRegisterActivity.this.finish();
    }

    private static class StaticHandler extends Handler {
        private final WeakReference<LoginRegisterActivity> mActivity;

        public StaticHandler(LoginRegisterActivity activity) {
            mActivity = new WeakReference<LoginRegisterActivity>(activity);
        }

        @Override
        public void handleMessage(Message msg) {
            LoginRegisterActivity activity = mActivity.get();
            if (activity != null) {

            }
        }
    }
}
