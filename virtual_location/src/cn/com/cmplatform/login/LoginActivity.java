package cn.com.cmplatform.login;

import cn.com.cmplatform.service.UserService;
import cn.com.cmplatform.gameplatform.R;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class LoginActivity extends Activity {
	EditText username;
	EditText password;
	Button login, register;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.login);
		LoginSysExitApplication.getInstance().addActivity(this);
		initViews();
		setListener();
	}

	private void initViews() {
		username = (EditText) findViewById(R.id.username);
		password = (EditText) findViewById(R.id.password);
		login = (Button) findViewById(R.id.login);
		register = (Button) findViewById(R.id.register);
	}

	private void setListener() {
		login.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (null != username || null != password) {
					String name = username.getText().toString();
					String pass = password.getText().toString();
					Log.i("TAG", name + "," + pass);
					if ("".equals(name.trim())) {
						Toast.makeText(LoginActivity.this, "请输入用户名",
								Toast.LENGTH_SHORT).show();
					}
					UserService userService = new UserService(
							LoginActivity.this);
					boolean flag = userService.login(name, pass);
					if (flag) {
						Toast.makeText(LoginActivity.this, "登录成功",
								Toast.LENGTH_SHORT).show();
						Bundle b = new Bundle();
						b.putString("name", name);
						Intent i = new Intent();
						i.setClass(LoginActivity.this,
								LoginClientActivity.class);
						i.putExtras(b);
						startActivity(i);
					} else {
						Toast.makeText(LoginActivity.this, "登录失败",
								Toast.LENGTH_SHORT).show();
					}
				}
			}
		});

		register.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent(LoginActivity.this,
						LoginRegisterActivity.class);
				startActivity(intent);
			}
		});
	}

}