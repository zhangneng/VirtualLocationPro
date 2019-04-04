/**
 * FileName:     ${SysExitApplication.java}
 * @Description: ${todo}(程序完全退出类)
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

import java.util.LinkedList;
import java.util.List;
import android.app.Activity;
import android.app.Application;

public class LoginSysExitApplication extends Application {
	private List<Activity> mList = new LinkedList<Activity>();
	private static LoginSysExitApplication instance;

	private LoginSysExitApplication() {
	}

	public synchronized static LoginSysExitApplication getInstance() {
		if (null == instance) {
			instance = new LoginSysExitApplication();
		}
		return instance;
	}

	// add Activity
	public void addActivity(Activity activity) {
		mList.add(activity);
	}

	public void exit() {
		try {
			for (Activity activity : mList) {
				if (activity != null)
					activity.finish();
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			System.exit(0);
		}
	}

	public void onLowMemory() {
		super.onLowMemory();
		System.gc();
	}
}