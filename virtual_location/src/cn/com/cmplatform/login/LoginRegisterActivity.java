/**
 * FileName:     ${ActivityRegister.java}
 * @Description: ${todo}(用户注册类)
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

import cn.com.cmplatform.gameplatform.R;
import cn.com.cmplatform.service.User;
import cn.com.cmplatform.service.UserService;
import cn.com.cmplatform.utils.UtilsSIMCardInfo;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class LoginRegisterActivity extends Activity implements
		android.view.View.OnClickListener {

	EditText username;
	EditText password;
	Button register, register_back;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.register);
		LoginSysExitApplication.getInstance().addActivity(this);
		initViews();
	}

	private void initViews() {
		username = (EditText) findViewById(R.id.usernameRegister);
		password = (EditText) findViewById(R.id.passwordRegister);
		register = (Button) findViewById(R.id.register_button);
		register_back = (Button) findViewById(R.id.register_back);

		register.setOnClickListener(this);
		register_back.setOnClickListener(this);

		UtilsSIMCardInfo phone_number = new UtilsSIMCardInfo(this); // 读取用户电话号码
		String phone_user_number = phone_number.getNativePhoneNumber();
		if (phone_user_number.length() == 0) // 防止读不出号码死机
		{
			return; 
		}
		else 
		{
			String dilution_user_number = phone_user_number.substring(3,
					phone_user_number.length());
			username.setText(dilution_user_number);
		}
	}

	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.register_button:
			if (null != username && null != password) {
				String name = "+86" + username.getText().toString();
				String pass = password.getText().toString();
				if ("".equals(name.trim()) && "".equals(pass.trim())) {
					Log.i("TAG", "请输入用户名和密码");
					Toast.makeText(LoginRegisterActivity.this, "请输入用户名和密码",
							Toast.LENGTH_SHORT).show();
					return;
				}
				UserService userService = new UserService(LoginRegisterActivity.this);
				User user = new User(name, pass);
				boolean flag = userService.register(user);
				if (flag) {
					Log.i("TAG", "注册成功");
					Toast.makeText(LoginRegisterActivity.this, "注册成功",
							Toast.LENGTH_SHORT).show();

					this.onBackPressed();
				}
			}
			break;
		case R.id.register_back:
			this.onBackPressed();
			break;
		}
	}

	public boolean onCreateOptionsMenu(Menu menu) {

		menu.add(Menu.NONE, Menu.FIRST + 1, 1, "显示").setIcon(
				android.R.drawable.ic_menu_search);

		menu.add(Menu.NONE, Menu.FIRST + 2, 2, "编辑").setIcon(
				android.R.drawable.ic_menu_edit);

		menu.add(Menu.NONE, Menu.FIRST + 3, 3, "帮助").setIcon(
				android.R.drawable.ic_menu_help);
		return true;

	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case Menu.FIRST + 1:
			Intent i = new Intent();
			i.setClass(LoginRegisterActivity.this, LoginDBActivity.class);
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
		Intent intent = new Intent(LoginRegisterActivity.this, LoginMainActivity.class);
		startActivity(intent);
		overridePendingTransition(R.anim.activity_in_to_left,
				R.anim.activity_out_to_right);
		super.onBackPressed();
		finish();
	}
}
