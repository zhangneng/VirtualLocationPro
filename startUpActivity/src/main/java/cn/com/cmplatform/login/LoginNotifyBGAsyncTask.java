package cn.com.cmplatform.login;

import android.content.Context;
import android.os.AsyncTask;

class LoginNotifyBGAsyncTask extends AsyncTask<String, Integer, String> {
	public LoginNotifyBGAsyncTask(Context context) {

	}

	@Override
	protected String doInBackground(String... params) {
		return null;
	}

	@Override
	protected void onCancelled() {
		super.onCancelled();
	}

	@Override
	protected void onPostExecute(String result) {

	}

	@Override
	protected void onPreExecute() {
		// 任务启动，可以在这里显示一个对话框，这里简单处理
		// message.setText(R.string.task_started);
	}

	@Override
	protected void onProgressUpdate(Integer... values) {

	}
}
