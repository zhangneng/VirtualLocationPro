/**
 * FileName:     ${LoginClientActivity.java}
 * @Description: ${todo}(帐户信息及游戏启动类)
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

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.util.Timer;
import java.util.TimerTask;
import cn.com.cmplatform.gameplatform.R;
import cn.com.cmplatform.push.PushActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class LoginClientActivity extends Activity {

	public static Handler mHandler = new Handler();
	TextView textView, textView2;
	EditText editText2;
	String tmp;
	Socket clientSocket;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.client);
		LoginSysExitApplication.getInstance().addActivity(this);
		textView = (TextView) findViewById(R.id.TextView01); // 帐户信息
		textView2 = (TextView) findViewById(R.id.textView2);
		editText2 = (EditText) findViewById(R.id.EditText02);

		Thread t = new Thread(readData);
		t.start();
		Bundle bu = this.getIntent().getExtras();
		textView2.setText(bu.getString("name"));

		Button button1 = (Button) findViewById(R.id.Button01);
		button1.setOnClickListener(new Button.OnClickListener() {
			@Override
			public void onClick(View v) {

				try {
					if (clientSocket.isConnected()) {
						BufferedWriter bw;
						try {
							bw = new BufferedWriter(new OutputStreamWriter(
									clientSocket.getOutputStream()));

							bw.write(textView2.getText() + ":"
									+ editText2.getText() + "\n");

							bw.flush();
						} catch (IOException e) {
							Toast.makeText(LoginClientActivity.this,
									"瀹㈡埛绔緭鍏ヨ緭鍑洪敊璇瘇", Toast.LENGTH_LONG).show();
						}
						editText2.setText("");
					}
				} catch (NullPointerException e) {
					Toast.makeText(LoginClientActivity.this,
							"杩炴帴鏈嶅姟鍣ㄧ澶辫触锛岃纭鏈嶅姟鍣ㄥ凡寮�惎锛", Toast.LENGTH_SHORT)
							.show();
				}
				Intent intent = new Intent(LoginClientActivity.this,
						PushActivity.class);
				startActivity(intent);
			}
		});

		Button button2 = (Button) findViewById(R.id.Button02);
		button2.setOnClickListener(new Button.OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(LoginClientActivity.this,
						PushActivity.class); // 启动游戏入口
				startActivity(intent);
			}
		});
	}

	private Runnable updateText = new Runnable() {
		@Override
		public void run() {
			textView.append(tmp + "\n");
		}
	};

	private Runnable readData = new Runnable() {
		@Override
		public void run() {
			Looper.prepare();
			try {
				int serverPort = 5050; // 端口号
				clientSocket = new Socket("172.22.71.129", serverPort); // 连接服务器

				BufferedReader br = new BufferedReader(new InputStreamReader(
						clientSocket.getInputStream())); // 读取数据

				while (clientSocket.isConnected()) {
					tmp = br.readLine();
					if (null != tmp) {
						mHandler.post(updateText);
					}
				}
			} catch (IOException e) {
				Toast.makeText(LoginClientActivity.this, "瀹㈡埛绔緭鍏ヨ緭鍑洪敊璇瘇",
						Toast.LENGTH_SHORT).show();
			}
		}
	};

	// 退出标志位
	private static Boolean isExit = false;

	// 覆写要退出页面的finish()方法
	@Override
	public void finish() {
		if (isExit == false) { // 第一次按下返回键
			isExit = true; // 退出标志位设置成true
			Toast.makeText(this, "再按一次后退键退出应用程序", Toast.LENGTH_SHORT).show();// 向用户提示
			// 一个Timer()对象，如果用户在第一次按返回键两秒后没有再按一次返回键退出，表示用户取消了操作，则重新将标志位设置成false
			new Timer().schedule(new TimerTask() {
				@Override
				public void run() {
					isExit = false;
				}
			}, 2000);
		} else {
			LoginSysExitApplication.getInstance().exit();
		}
	}
	
}
