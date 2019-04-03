/**
 * FileName:     ${LoginMainActivity.java}
 * @Description: ${todo}(主程序入口类)
 * All rights Reserved, Designed By ZTE-ITS
 * Copyright:    Copyright(C) 2010-2014
 * Company       ZTE-ITS WuXi LTD.
 * @author:    zhangneng
 * @version    V1.0 
 * Createdate:         ${date} ${time}
 *
 * Modification  History:
 * Date         Author        Version        Discription
 * -----------------------------------------------------------------------------------
 * ${date}       wu.zh          1.0             1.0
 * Why & What is modified: <修改原因描述>
 */
package cn.com.cmplatform.login;

import java.util.List;
import com.ant.liao.GifView;
import android.os.Bundle;
// import android.provider.Settings.Secure;
import android.app.Activity;
import android.app.ActivityManager;
import android.app.AlertDialog;

import android.widget.TextView;
import android.view.animation.Animation;
import android.view.animation.AlphaAnimation;

import cn.com.cmplatform.platform.activity.PlatformMainActivity;
import cn.com.cmplatform.push.PushService;
import cn.com.cmplatform.service.User;
import cn.com.cmplatform.service.UserService;
import cn.com.cmplatform.utils.UtilsJudgeEmailAndNumber;
import cn.com.cmplatform.gameplatform.R;
import cn.com.cmplatform.guide.GuideViewPagerActivity;

// import android.content.ContentResolver;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences.Editor;

import android.util.DisplayMetrics;

import android.view.Gravity;
// import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;

import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.Toast;

// import com.ant.liao.GifView;
import com.ant.liao.GifView.GifImageType;

public class LoginMainActivity extends Activity {

	private TextView tvHint;
	LoginPullDoorView pullDoor;
	EditText username;
	EditText password;
	// private String mDeviceID;
	Button login, register, jump;
	private DisplayMetrics dm;
	// private static Boolean isExit = false; // 退出标志位
	private static Boolean isLoginDirect = false;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		LoginSysExitApplication.getInstance().addActivity(this);

		ActivityManager mActivityManager = (ActivityManager) getSystemService(ACTIVITY_SERVICE);
		List<ActivityManager.RunningServiceInfo> mServiceList = mActivityManager
				.getRunningServices(30);

		dm = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(dm);
		tvHint = (TextView) this.findViewById(R.id.tv_hint);
		pullDoor = (LoginPullDoorView) this.findViewById(R.id.myImage);

		LinearLayout.LayoutParams imagebtn_params = new LinearLayout.LayoutParams(
				LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		imagebtn_params.setMargins(0, 0, 10, 10);

		final String musicClassName = "cn.com.cmplatform.push.PushService"; // 我要判断的服务名字
		boolean b = MusicServiceIsStart(mServiceList, musicClassName);
		if (!b) {
			new notifyThread().start(); // 开启单独线程接受推送消息
		}
		initViews();
		setListener();
		Animation ani = new AlphaAnimation(0f, 1f);
		ani.setDuration(1500);
		ani.setRepeatMode(Animation.REVERSE);
		ani.setRepeatCount(Animation.INFINITE);
		tvHint.startAnimation(ani);

		if (isLoginDirect) {
			this.loginButtonDown();
			isLoginDirect = false;
		}
	}

	// 通过Service的类名来判断是否启动某个服务
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
			// this.startService(new Intent(this,PushService.class));
			// mDeviceID = Secure.getString(this.getContentResolver(),
			// Secure.ANDROID_ID);
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
		username = (EditText) findViewById(R.id.username);
		password = (EditText) findViewById(R.id.password);
		login = (Button) findViewById(R.id.login);
		register = (Button) findViewById(R.id.register);
		jump = (Button) findViewById(R.id.jump);

		// 自动登录功能
		final UserService service = new UserService(this); // 获取数据库前两组数据
		List<User> users = service.getScrollData(0, 1);
		if (!users.isEmpty()) {

			String dilution_user_number = users.get(0).getUsername()
					.substring(3, users.get(0).getUsername().length());

			username.setText(dilution_user_number);
			password.setText(users.get(0).getPassword());

			pullDoor.jumpBounceAnim();
			isLoginDirect = true;
		}

		GifView gf2;
		// 从xml中得到GifView的句柄
		gf2 = (GifView) findViewById(R.id.gif3);
		// 设置Gif图片源
		gf2.setGifImage(R.drawable.gif1);
		// 添加监听器
		// gf1.setOnClickListener(this);
		// 设置显示的大小，拉伸或者压缩
		gf2.setShowDimension(300, 300);
		// 设置加载方式：先加载后显示、边加载边显示、只显示第一帧再显示
		gf2.setGifImageType(GifImageType.COVER);
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
					// i.setClass(LoginMainActivity.this,
					// ClientActivity.class);
					// i.setClass(LoginMainActivity.this, PlatformMainActivity.class);
					i.setClass(LoginMainActivity.this, GuideViewPagerActivity.class);
					
					i.putExtras(b);
					startActivity(i);
					overridePendingTransition(R.anim.activity_in_to_right,
							R.anim.activity_out_to_left);
					finish();
				} else {
					Toast.makeText(LoginMainActivity.this, "登录失败，请检查您的密码和帐号",
							Toast.LENGTH_SHORT).show();
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
							// i.setClass(LoginMainActivity.this,
							// ClientActivity.class);
							i.setClass(LoginMainActivity.this,
									PlatformMainActivity.class);

							i.putExtras(b);
							startActivity(i);

							overridePendingTransition(
									R.anim.activity_in_to_right,
									R.anim.activity_out_to_left);
							finish();
						} else {
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
				overridePendingTransition(R.anim.activity_in_to_right,
						R.anim.activity_out_to_left);
				LoginMainActivity.super.finish();
			}
		});

		jump.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) { // 特别注意点
				AlertDialog dialog = new AlertDialog.Builder(LoginMainActivity.this)
						.setIcon(R.drawable.myicon)
						.setTitle("特别提示！")
						.setMessage("非注册用户，游戏内将无法获得积分！！！为了更好的游戏体验，请先注册并登录进行游戏！")
						.setPositiveButton("确定",
								new DialogInterface.OnClickListener() {
									@Override
									public void onClick(DialogInterface dialog,
											int which) {
										Intent intent = new Intent(
												LoginMainActivity.this,
												LoginRegisterActivity.class);
										startActivity(intent);
										overridePendingTransition(
												R.anim.activity_in_to_right,
												R.anim.activity_out_to_left);
										LoginMainActivity.super.finish();
									}
								})
						.setNegativeButton("进入游戏",
								new DialogInterface.OnClickListener() {
									@Override
									public void onClick(DialogInterface dialog,
											int which) {

									}
								}).create();

				Window window = dialog.getWindow();
				window.setGravity(Gravity.BOTTOM); // 此处可以设置dialog显示的位置
				window.setWindowAnimations(R.style.mystyle); // 添加动画
				dialog.show();
			}
		});
	}

	public void false_finish() {
		Intent i = new Intent(Intent.ACTION_MAIN); // 开启后台运行
		i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK); // 如果是服务里调用，必须加入FLAG_ACTIVITY_NEW_TASK标识
		i.addCategory(Intent.CATEGORY_HOME);
		startActivity(i);
	}

	/*
	 * @Override public void finish() {
	 * 
	 * if (isExit == false) { // 第一次按下返回键 isExit = true; Toast.makeText(this,
	 * "再按一次后退键退出应用程序", Toast.LENGTH_SHORT).show(); //
	 * 一个Timer()对象，如果用户在第一次按返回键两秒后没有再按一次返回键退出，表示用户取消了操作，则重新将标志位设置成false new
	 * Timer().schedule(new TimerTask() {
	 * 
	 * @Override public void run() { isExit = false; } }, 2000); } else {
	 * LoginMainActivity.super.finish(); }
	 * 
	 * AlertDialog dialog = new AlertDialog.Builder(LoginMainActivity.this)
	 * .setIcon(R.drawable.myicon).setTitle("提示！") .setMessage("确定退出软件?")
	 * .setPositiveButton("确定", new DialogInterface.OnClickListener() {
	 * 
	 * @Override public void onClick(DialogInterface dialog, int which) {
	 * 
	 * SysExitApplication.getInstance().exit(); } }) .setNegativeButton("否", new
	 * DialogInterface.OnClickListener() {
	 * 
	 * @Override public void onClick(DialogInterface dialog, int which) {
	 * 
	 * } }).create();
	 * 
	 * Window window = dialog.getWindow(); window.setGravity(Gravity.BOTTOM); //
	 * 此处可以设置dialog显示的位置 window.setWindowAnimations(R.style.mystyle); // 添加动画
	 * dialog.show(); }
	 */
}