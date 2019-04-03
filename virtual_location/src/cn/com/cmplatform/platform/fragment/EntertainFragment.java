package cn.com.cmplatform.platform.fragment;

import cn.com.cmplatform.gameplatform.R;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * 娱乐Fragment
 * 
 * @author jiangqq
 * 
 */
public class EntertainFragment extends Fragment {
	private View mView;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		mView = inflater.inflate(R.layout.entertain, container, false);
		return mView;
	}
}
