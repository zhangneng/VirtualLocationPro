package cn.com.cmplatform.platform.activity;

import java.util.Timer;
import java.util.TimerTask;

import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
// import android.support.v4.view.PagerTitleStrip;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.ImageButton;
import android.widget.Toast;

import cn.com.cmplatform.gameplatform.R;
import cn.com.cmplatform.login.LoginSysExitApplication;

import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.jeremyfeinstein.slidingmenu.lib.app.SlidingFragmentActivity;
import cn.com.cmplatform.platform.adapter.MainPagerAdapter;
import cn.com.cmplatform.platform.fragment.LeftCategoryFragment;
import cn.com.cmplatform.platform.fragment.RightPerMsgCenterFragment;

public class PlatformMainActivity extends SlidingFragmentActivity {

	private ImageButton main_left_imgbtn;
	private ImageButton main_right_imgbtn;
	private ViewPager myViewPager;
	// private PagerTitleStrip pagertitle;
	private PagerAdapter mAdapter;
	private static Boolean isExit = false; // 退出标志位

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		initSlidingMenu();
		initView();
		initValidata();
		bindData();
		initListener();

		LoginSysExitApplication.getInstance().addActivity(this);
	}

	/**
	 * 初始化SlidingMenu视图
	 */
	private void initSlidingMenu() {
		// 设置滑动菜单的属性值
		getSlidingMenu().setMode(SlidingMenu.LEFT_RIGHT);
		getSlidingMenu().setTouchModeAbove(SlidingMenu.TOUCHMODE_MARGIN);
		getSlidingMenu().setShadowWidthRes(R.dimen.shadow_width);
		getSlidingMenu().setShadowDrawable(R.drawable.shadow);
		getSlidingMenu().setBehindOffsetRes(R.dimen.slidingmenu_offset);
		getSlidingMenu().setFadeDegree(0.35f);
		// 设置主界面的视图
		setContentView(R.layout.main_unicode);
		// 设置左边菜单打开后的视图界面
		setBehindContentView(R.layout.left_content);
		getSupportFragmentManager().beginTransaction()
				.replace(R.id.left_content_id, new LeftCategoryFragment())
				.commit();
		// 设置右边菜单打开后的视图界面
		getSlidingMenu().setSecondaryMenu(R.layout.right_content);
		getSupportFragmentManager()
				.beginTransaction()
				.replace(R.id.right_content_id, new RightPerMsgCenterFragment())
				.commit();
	}

	private void initView() {
		main_left_imgbtn = (ImageButton) this
				.findViewById(R.id.main_left_imgbtn);
		main_right_imgbtn = (ImageButton) this
				.findViewById(R.id.main_right_imgbtn);
		myViewPager = (ViewPager) this.findViewById(R.id.myviewpager);
		// pagertitle = (PagerTitleStrip) this.findViewById(R.id.pagertitle);
	}

	/**
	 * 初始化变量
	 */
	private void initValidata() {
		// pagertitle.setTextSize(0, 25);
		mAdapter = new MainPagerAdapter(getSupportFragmentManager());

	}

	/**
	 * 绑定数据
	 */
	private void bindData() {
		myViewPager.setAdapter(mAdapter);
		myViewPager.setCurrentItem(0);
	}

	private void initListener() {
		main_left_imgbtn.setOnClickListener(new MySetOnClickListener());
		main_right_imgbtn.setOnClickListener(new MySetOnClickListener());
		myViewPager.setOnPageChangeListener(new MySetOnPageChangeListener());
	}

	/**
	 * ViewPager页面选项卡切换监听器
	 */
	class MySetOnPageChangeListener implements OnPageChangeListener {
		public void onPageScrollStateChanged(int arg0) {
			Log.i("gge", "arg0");
		}

		public void onPageScrolled(int arg0, float arg1, int arg2) {
			Log.i("gge1", "arg0");
		}

		public void onPageSelected(int arg0) {
			Log.i("gge2", "arg0");
		}
	}

	/**
	 * 进行侧滑界面打开与关闭
	 * 
	 * @author jiangqq
	 * 
	 */
	class MySetOnClickListener implements OnClickListener {

		public void onClick(View v) {
			toggle();
		}
	}
	
	// 防止按home键死机
	@Override  
    protected void onSaveInstanceState(Bundle outState) {  
        // TODO Auto-generated method stub   
        //super.onSaveInstanceState(outState);    
    }  

	@Override
	public void finish() {

		if (isExit == false) { // 第一次按下返回键
			isExit = true;
			Toast.makeText(this, "再按一次后退键退出应用程序", Toast.LENGTH_SHORT).show(); 
			new Timer().schedule(new TimerTask() {
				@Override
				public void run() {
					isExit = false;
				}
			}, 2000);
		} else {
			// SysExitApplication.getInstance().exit();
			System.exit(0);
		}
	}
}
