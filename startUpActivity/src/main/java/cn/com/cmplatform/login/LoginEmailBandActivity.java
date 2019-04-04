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
import cn.com.cmplatform.update.UpdateMain;
import cn.com.cmplatform.utils.UtilsSIMCardInfo;


public class LoginEmailBandActivity extends Activity implements
        View.OnClickListener {

    private final StaticHandler mhandlerSend = new StaticHandler(this) {
        @Override
        public void handleMessage(Message msg) {
            try {
                if (BuildConfig.DEBUG)
                    Log.i("TAG", "mhandlerSend接收到msg.what=" + msg.what);
                String s = msg.obj.toString();
                if (msg.what == 1) {
                    Toast.makeText(LoginEmailBandActivity.this,
                            "发送成功!", Toast.LENGTH_SHORT)
                            .show();
                } else {
                    Toast.makeText(LoginEmailBandActivity.this,
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
    private final StaticHandler mhandler = new StaticHandler(this) {
        @Override
        public void handleMessage(Message msg) {
            try {
                Log.i("TAG", "mhandler接收到msg=" + msg.what);
                if (msg.obj != null) {

                    String s = msg.obj.toString();
                    if (s.trim().length() > 0) {
                        Log.i("TAG", "mhandler接收到obj=" + s);
                        Log.i("TAG", "开始更新UI");
                        Toast.makeText(LoginEmailBandActivity.this, "注册成功",
                                Toast.LENGTH_SHORT).show();
                        Log.i("TAG", "更新UI完毕更新UI完毕更新UI完毕更新UI完毕");
                        Intent intent = new Intent(LoginEmailBandActivity.this, LoginMainActivity.class);
                        startActivity(intent);
                        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                        LoginEmailBandActivity.this.finish();
                        Log.i("TAG", "更新UI完毕");
                    } else {
                        Log.i("TAG", "没有数据返回不更新");
                    }
                }
            } catch (Exception e) {
                Log.i("TAG", "加载过程出现异常");
                e.printStackTrace();
            }
        }
    };
    private String email = null;
    private String message = null;
    private String phone_user_number = null;
    private Button email_band_button = null;
    private EditText email_contant = null;
    private Context ctx = null;
    private LoginSocket socketThread = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_email_band);
        LoginSysExitApplication.getInstance().addActivity(this);
        ctx = LoginEmailBandActivity.this;

        UtilsSIMCardInfo phone_number = new UtilsSIMCardInfo(this);
        phone_user_number = phone_number.getNativePhoneNumber();
        initViews();
    }

    public void startSocket() {
        socketThread = new LoginSocket(mhandler, mhandlerSend, ctx, message);
        socketThread.start();
    }

    private void initViews() {
        email_contant = (EditText) findViewById(R.id.email_contant);
        email_band_button = (Button) findViewById(R.id.email_band_button);
        email_band_button.setOnClickListener(this);

        ActionBar actionBar = getActionBar();
        actionBar.setDisplayShowHomeEnabled(false);
        actionBar.setDisplayShowTitleEnabled(false);
        actionBar.setDisplayHomeAsUpEnabled(true);
    }

    /**
     * DATA000#0001&999999@19923949494%1234567890123456$000000/
     */
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.email_band_button:
                if (null != email_contant) {
                    email = email_contant.getText().toString();

                    if ("".equals(email.trim())) {
                        Toast.makeText(LoginEmailBandActivity.this, "请输入邮件地址",
                                Toast.LENGTH_LONG).show();
                        return;
                    }
                    message = new String("DATA000#" + "0001&" + email + "@" + phone_user_number + "/");
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
            Log.i("TAG", "Socket已终止");
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
                i.setClass(LoginEmailBandActivity.this, UpdateMain.class);
                startActivity(i);
                break;
            case Menu.FIRST + 2:
                break;
            case Menu.FIRST + 3:
                break;
        }
        return false;
    }

    public void onBackPressed() {
        Intent intent = new Intent(LoginEmailBandActivity.this, LoginMainActivity.class);
        startActivity(intent);
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
        super.onBackPressed();
        LoginEmailBandActivity.this.finish();
    }

    private static class StaticHandler extends Handler {
        private final WeakReference<LoginEmailBandActivity> mActivity;

        public StaticHandler(LoginEmailBandActivity activity) {
            mActivity = new WeakReference<LoginEmailBandActivity>(activity);
        }

        @Override
        public void handleMessage(Message msg) {
            LoginEmailBandActivity activity = mActivity.get();
            if (activity != null) {

            }
        }
    }
}
