package cn.com.cmplatform.update;

import android.app.Application;
// import android.content.Intent;

public class UpdateApplication extends Application {

	private boolean isDownload;

	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		super.onCreate();
		isDownload = false;
	}

	public boolean isDownload() {
		return isDownload;
	}

	public void setDownload(boolean isDownload) {
		this.isDownload = isDownload;
	}

}
