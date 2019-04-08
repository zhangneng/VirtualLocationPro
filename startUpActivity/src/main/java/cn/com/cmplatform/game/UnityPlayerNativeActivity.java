package cn.com.cmplatform.game;

import com.gitonway.lee.niftymodaldialogeffects.lib.Effectstype;
import com.gitonway.lee.niftymodaldialogeffects.lib.NiftyDialogBuilder;
import com.unity3d.player.*;
import android.app.NativeActivity;
import android.content.res.Configuration;
import android.graphics.PixelFormat;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.app.Activity;

import cn.com.cmplatform.gameplatform.BuildConfig;
import cn.com.cmplatform.gameplatform.R;

public class UnityPlayerNativeActivity extends Activity
{
	protected UnityPlayer mUnityPlayer;		// don't change the name of this variable; referenced from native code
	NiftyDialogBuilder dialogBuilder;
	// Setup activity layout
	@Override protected void onCreate (Bundle savedInstanceState)
	{
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		super.onCreate(savedInstanceState);

		getWindow().takeSurface(null);
		getWindow().setFormat(PixelFormat.RGBX_8888); // <--- This makes xperia play happy

		mUnityPlayer = new UnityPlayer(this);
		if (mUnityPlayer.getSettings ().getBoolean ("hide_status_bar", true))
			getWindow ().setFlags (WindowManager.LayoutParams.FLAG_FULLSCREEN,
			                       WindowManager.LayoutParams.FLAG_FULLSCREEN);

		setContentView(mUnityPlayer);
		mUnityPlayer.requestFocus();
	}

	// Quit Unity
	@Override protected void onDestroy ()
	{
        /*
		dialogBuilder = NiftyDialogBuilder.getInstance(UnityPlayerNativeActivity.this);
		dialogBuilder
				.withTitle("注意")
				.withTitleColor("#FFFFFFFF")
				.withDividerColor("#11000000")
				.withMessage("确认退出应用程序")
				.withMessageColor("#FFFFFFFF")
				.withDialogColor("#00000000")
				.withIcon(getResources().getDrawable(R.drawable.myicon))
				.withDuration(700)
				.withEffect(Effectstype.Fadein)
				.withButton1Text("确认")
				.withButton2Text("取消")
				.isCancelableOnTouchOutside(true)
				.setButton1Click(new View.OnClickListener() {
					@Override
					public void onClick(View v) {
						mUnityPlayer.quit();
						UnityPlayerNativeActivity.this.finish();
					}
				})
				.setButton2Click(new View.OnClickListener() {
					@Override
					public void onClick(View v) {
						dialogBuilder.dismiss();
						return;
					}
				})
				.show();
        */
        mUnityPlayer.quit();
		super.onDestroy();
	}

    @Override
    public void onBackPressed() {

        if (BuildConfig.DEBUG)
            Log.i("TAG", "onBackPressed...");
        dialogBuilder = NiftyDialogBuilder.getInstance(UnityPlayerNativeActivity.this);
        dialogBuilder
                .withTitle("注意")
                .withTitleColor("#FFFFFFFF")
                .withDividerColor("#11000000")
                .withMessage("确认退出应用程序")
                .withMessageColor("#FFFFFFFF")
                .withDialogColor("#00000000")
                .withIcon(getResources().getDrawable(R.drawable.myicon))
                .withDuration(700)
                .withEffect(Effectstype.Fadein)
                .withButton1Text("确认")
                .withButton2Text("取消")
                .isCancelableOnTouchOutside(true)
                .setButton1Click(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        UnityPlayerNativeActivity.this.finish();
                    }
                })
                .setButton2Click(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialogBuilder.dismiss();
                        return;
                    }
                })
                .show();
    }

	// Pause Unity
	@Override protected void onPause()
	{
		super.onPause();
		mUnityPlayer.pause();
	}

	// Resume Unity
	@Override protected void onResume()
	{
		super.onResume();
		mUnityPlayer.resume();
	}

	// This ensures the layout will be correct.
	@Override public void onConfigurationChanged(Configuration newConfig)
	{
		super.onConfigurationChanged(newConfig);
		mUnityPlayer.configurationChanged(newConfig);
	}

	// Notify Unity of the focus change.
	@Override public void onWindowFocusChanged(boolean hasFocus)
	{
		super.onWindowFocusChanged(hasFocus);
		mUnityPlayer.windowFocusChanged(hasFocus);
	}

	// For some reason the multiple keyevent type is not supported by the ndk.
	// Force event injection by overriding dispatchKeyEvent().
	@Override public boolean dispatchKeyEvent(KeyEvent event)
	{
		if (event.getAction() == KeyEvent.ACTION_MULTIPLE)
			return mUnityPlayer.injectEvent(event);
		return super.dispatchKeyEvent(event);
	}

	// Pass any events not handled by (unfocused) views straight to UnityPlayer
	@Override public boolean onKeyUp(int keyCode, KeyEvent event)     { return mUnityPlayer.injectEvent(event); }
	@Override public boolean onKeyDown(int keyCode, KeyEvent event)   { return mUnityPlayer.injectEvent(event); }
	@Override public boolean onTouchEvent(MotionEvent event)          { return mUnityPlayer.injectEvent(event); }
	/*API12*/ public boolean onGenericMotionEvent(MotionEvent event)  { return mUnityPlayer.injectEvent(event); }
}
