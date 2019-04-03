/**
 * FileName:     ${LoginPullDoorView.java}
 * @Description: ${todo}(上退滑动效果类)
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

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.BounceInterpolator;
import android.view.animation.Interpolator;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Scroller;
import cn.com.cmplatform.gameplatform.R;

public class LoginPullDoorView extends RelativeLayout {
	private Context mContext;
	private Scroller mScroller;
	private int mScreenHeigh = 0;
	@SuppressWarnings("unused")
	private int mScreenWidth = 0;
	private int mLastDownY = 0;
	private int mCurryY;
	private int mDelY;
	private boolean mCloseFlag = false;
	private boolean mTouchControl = false;
	private ImageView mImgView;

	public LoginPullDoorView(Context context) {
		super(context);
		mContext = context;
		setupView();
	}

	public LoginPullDoorView(Context context, AttributeSet attrs) {
		super(context, attrs);
		mContext = context;
		setupView();
	}

	@SuppressLint("NewApi")
	private void setupView() {
		Interpolator polator = new BounceInterpolator();
		mScroller = new Scroller(mContext, polator);

		WindowManager wm = (WindowManager) (mContext
				.getSystemService(Context.WINDOW_SERVICE));
		DisplayMetrics dm = new DisplayMetrics();
		wm.getDefaultDisplay().getMetrics(dm);
		mScreenHeigh = dm.heightPixels;
		mScreenWidth = dm.widthPixels;

		this.setBackgroundColor(Color.argb(0, 0, 0, 0)); // 设置背景透明
		mImgView = new ImageView(mContext);
		mImgView.setLayoutParams(new LayoutParams(
				android.view.ViewGroup.LayoutParams.MATCH_PARENT,
				android.view.ViewGroup.LayoutParams.MATCH_PARENT));
		mImgView.setScaleType(ImageView.ScaleType.FIT_XY);
		mImgView.setImageResource(R.drawable.bg1); // 设置背景图片
		addView(mImgView);
	}

	/*
	 * ! fuction: setBgImage prama: int id 设置背景图片
	 */
	public void setBgImage(int id) {
		mImgView.setImageResource(id);
	}

	/*
	 * ! fuction: setBgImage prama: Drawable drawable 设置推动门背景
	 */
	public void setBgImage(Drawable drawable) {
		mImgView.setImageDrawable(drawable);
	}

	/*
	 * ! fuction: startBounceAnim prama: int startY, int dy, int duration
	 * 设置推动门背景
	 */
	public void startBounceAnim(int startY, int dy, int duration) {
		mScroller.startScroll(0, startY, 0, dy, duration);
		invalidate();
	}

	public void jumpBounceAnim() {
		// startBounceAnim(0, mScreenHeigh, 450);
		scrollTo(0, mScreenHeigh);
		mCloseFlag = true;
		postInvalidate();
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		int action = event.getAction();
		switch (action) {
		case MotionEvent.ACTION_DOWN: {
			if (mTouchControl) {
				// return true;
			}
			System.err.println("ACTION_MOVE 1=" + mTouchControl);
			mTouchControl = true;
			mLastDownY = (int) event.getY();
			System.err.println("ACTION_DOWN=" + mLastDownY);
			return true;
		}
		case MotionEvent.ACTION_MOVE: {
			mCurryY = (int) event.getY();
			System.err.println("ACTION_MOVE=" + mCurryY);
			mDelY = mCurryY - mLastDownY;

			if (mDelY < 0) {
				scrollTo(0, -mDelY);
			}
			System.err.println("-------------  " + mDelY);
			break;
		}
		case MotionEvent.ACTION_UP: {
			if (mCloseFlag) // 防止最后一次按下
				return true;
			mCurryY = (int) event.getY();
			mDelY = mCurryY - mLastDownY;
			System.err.println("ACTION_UP=" + mCurryY);
			System.err.println("ACTION_UP=" + mDelY);
			if (mDelY < 0) {
				if (Math.abs(mDelY) > mScreenHeigh / 3) {
					startBounceAnim(this.getScrollY(), mScreenHeigh, 450);
					mCloseFlag = true;
				} else {
					startBounceAnim(this.getScrollY(), -this.getScrollY(), 1000);
				}
			} else {
				startBounceAnim(this.getScrollY(), -this.getScrollY(), 1000);
			}
			break;
		}
		}
		return super.onTouchEvent(event);
	}

	@Override
	public void computeScroll() {
		if (mScroller.computeScrollOffset()) {
			scrollTo(mScroller.getCurrX(), mScroller.getCurrY());
			postInvalidate();
		} else {
			if (mCloseFlag) {
				this.setVisibility(View.GONE);
				mTouchControl = false;
			}
		}
	}
}
