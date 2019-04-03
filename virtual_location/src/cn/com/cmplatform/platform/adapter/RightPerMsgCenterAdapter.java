package cn.com.cmplatform.platform.adapter;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import cn.com.cmplatform.gameplatform.R;
import cn.com.cmplatform.platform.model.ItemPerMsgCenterModel;

public class RightPerMsgCenterAdapter extends BaseAdapter {

	private List<ItemPerMsgCenterModel> mLists;
	private Context mContext;
	private LayoutInflater mLayoutInflater;

	public RightPerMsgCenterAdapter(Context pContext,
			List<ItemPerMsgCenterModel> pLists) {
		this.mContext = pContext;
		this.mLists = pLists;
		mLayoutInflater = LayoutInflater.from(mContext);
	}

	public int getCount() {

		return mLists != null ? mLists.size() : 0;
	}

	public Object getItem(int position) {

		return mLists.get(position);
	}

	public long getItemId(int position) {

		return position;
	}

	public View getView(int position, View convertView, ViewGroup parent) {
		Holder _Holder = null;
		if (null == convertView) {
			_Holder = new Holder();
			convertView = mLayoutInflater.inflate(
					R.layout.right_per_msg_center_item, null);
			_Holder.right_permsg_center_item_img = (ImageView) convertView
					.findViewById(R.id.right_permsg_center_item_img);
			_Holder.right_permsg_center_item_msg = (TextView) convertView
					.findViewById(R.id.right_permsg_center_item_msg);
			convertView.setTag(_Holder);
		} else {
			_Holder = (Holder) convertView.getTag();
		}
		_Holder.right_permsg_center_item_img.setImageResource(mLists.get(
				position).getId());
		_Holder.right_permsg_center_item_msg.setText(mLists.get(position)
				.getMsg());
		return convertView;
	}

	private static class Holder {
		ImageView right_permsg_center_item_img;
		TextView right_permsg_center_item_msg;
	}
}
