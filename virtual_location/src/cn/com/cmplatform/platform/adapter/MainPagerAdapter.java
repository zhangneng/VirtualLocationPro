package cn.com.cmplatform.platform.adapter;

import java.util.ArrayList;
import java.util.List;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import cn.com.cmplatform.platform.fragment.EconomicsFragment;
import cn.com.cmplatform.platform.fragment.EntertainFragment;
import cn.com.cmplatform.platform.fragment.HeadlinesFragment;
import cn.com.cmplatform.platform.fragment.ScienceFragment;
import cn.com.cmplatform.platform.fragment.SportFragment;

/**
 * 自定义ViewPager页面选项卡适配器
 * 
 * @author jiangqq
 * 
 */
public class MainPagerAdapter extends FragmentPagerAdapter {
	private List<Fragment> mFragments;
	private String[] mViewpager_title;

	public MainPagerAdapter(FragmentManager fm) {
		super(fm);
		mFragments = new ArrayList<Fragment>();
		// 把所有要显示的Fragment选项卡加入到集合中
		mFragments.add(new HeadlinesFragment());
		mFragments.add(new EntertainFragment());
		mFragments.add(new SportFragment());
		mFragments.add(new EconomicsFragment());
		mFragments.add(new ScienceFragment());
		mViewpager_title = new String[] { "头条", "娱乐", "体育", "财经", "科技" };
	}

	@Override
	public CharSequence getPageTitle(int position) {
		// TODO Auto-generated method stub
		return mViewpager_title[position];
	}

	@Override
	public Fragment getItem(int arg0) {
		return mFragments.get(arg0);
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return mFragments != null ? mFragments.size() : 0;
	}
}
