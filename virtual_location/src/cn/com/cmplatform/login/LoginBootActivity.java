/**
 * FileName:     ${StartUpActivity.java}
 * @Description: ${todo}(程序启动欢迎界面类)
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

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.content.Intent;
import android.os.Handler;
import cn.com.cmplatform.gameplatform.R;

public class LoginBootActivity extends Activity {
	private final int SPLASH_DISPLAY_LENGHT = 223; // 多长时间最合适？

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_startup);
		LoginSysExitApplication.getInstance().addActivity(this);

		new Handler().postDelayed(new Runnable() {

			@Override
			public void run() {
				Intent mainIntent = new Intent(LoginBootActivity.this,
						cn.com.cmplatform.update.UpdateMain.class);
				LoginBootActivity.this.startActivity(mainIntent);
				overridePendingTransition(R.anim.alpha_scale_translate,
						R.anim.my_alpha_action);
				LoginBootActivity.this.finish();
			}

		}, SPLASH_DISPLAY_LENGHT);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_startup, menu);
		return true;
	}

}