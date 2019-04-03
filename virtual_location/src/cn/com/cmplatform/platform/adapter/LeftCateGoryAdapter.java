package cn.com.cmplatform.platform.adapter;

import java.util.List;

import cn.com.cmplatform.gameplatform.R;
import cn.com.cmplatform.platform.model.ItemCategoryModel;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * 左边底部列别服务自定义适配器
 * 
 * @author jiangqingqing
 * 
 */
public class LeftCateGoryAdapter extends BaseAdapter {

	private Context mContext;
	private List<ItemCategoryModel> mLists;
	private LayoutInflater mLayoutInflater;

	public LeftCateGoryAdapter(Context pContext, List<ItemCategoryModel> pLists) {
		this.mContext = pContext;
		this.mLists = pLists;
		mLayoutInflater = LayoutInflater.from(mContext);
	}

	public int getCount() {

		return mLists != null ? mLists.size() : 0;
	}

	public Object getItem(int arg0) {

		return mLists.get(arg0);
	}

	public long getItemId(int arg0) {
		return arg0;
	}

	public View getView(int arg0, View view, ViewGroup arg2) {
		Holder _Holder = null;
		if (null == view) {
			_Holder = new Holder();
			view = mLayoutInflater.inflate(R.layout.left_category_item, null);
			_Holder.left_category_item_img = (ImageView) view
					.findViewById(R.id.left_category_item_img);
			_Holder.left_category_item_name = (TextView) view
					.findViewById(R.id.left_category_item_name);
			_Holder.left_category_item_title = (TextView) view
					.findViewById(R.id.left_category_item_title);
			view.setTag(_Holder);
		} else {
			_Holder = (Holder) view.getTag();
		}
		_Holder.left_category_item_img.setImageResource(mLists.get(arg0)
				.getId());
		_Holder.left_category_item_name.setText(mLists.get(arg0).getName());
		_Holder.left_category_item_title.setText(mLists.get(arg0).getTitle());
		return view;
	}

	private static class Holder {
		ImageView left_category_item_img;
		TextView left_category_item_name;
		TextView left_category_item_title;
	}
}
