package cn.com.cmplatform.platform.fragment;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import cn.com.cmplatform.gameplatform.R;
import cn.com.cmplatform.platform.adapter.RightPerMsgCenterAdapter;
import cn.com.cmplatform.platform.model.ItemPerMsgCenterModel;

public class RightPerMsgCenterFragment extends Fragment {
	private View mView;
	private Context mContext;
	private RightPerMsgCenterAdapter mAdapter;
	private ListView right_permsg_center_listview;
	private List<ItemPerMsgCenterModel> mLists;
	private String[] msg_center;
	private Integer[] img_center;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		if (mView == null) {
			mView = inflater.inflate(R.layout.right_per_msg_center, container,
					false);
			initView();
			initValidata();
			bindData();
		}
		return mView;
	}

	/**
	 * 初始化界面元素
	 */
	private void initView() {
		right_permsg_center_listview = (ListView) mView
				.findViewById(R.id.right_permsg_center_listview);
	}

	/**
	 * 初始化变量
	 */
	private void initValidata() {
		mContext = mView.getContext();
		msg_center = mContext.getResources().getStringArray(R.array.msg_center);
		img_center = new Integer[] {
				R.drawable.biz_pc_list_extra_plugin_icon_dark,
				R.drawable.biz_pc_list_setting_icon_dark,
				R.drawable.biz_pc_list_weather_icon_dark,
				R.drawable.biz_pc_list_offline_icon_dark,
				R.drawable.biz_pc_list_theme_icon_night_dark,
				R.drawable.biz_pc_list_search_icon_dark,
				R.drawable.biz_pc_list_mail_icon_dark };
		mLists = new ArrayList<ItemPerMsgCenterModel>();
		for (int i = 0; i < msg_center.length; i++) {
			mLists.add(new ItemPerMsgCenterModel(img_center[i], msg_center[i]));
		}
		mAdapter = new RightPerMsgCenterAdapter(mContext, mLists);
	}

	/**
	 * 绑定数据
	 */
	private void bindData() {
		right_permsg_center_listview.setAdapter(mAdapter);
	}

}
