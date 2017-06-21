package com.bonc.mobile.portal.search;

import java.util.List;

import com.bonc.anhuimobile.ac.R;
import com.bonc.mobile.common.view.FlowLayout;
import com.bonc.mobile.portal.search.MyAdapter.ViewHolder;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RelativeLayout.LayoutParams;
import android.widget.TextView;

public class SearchAdapter extends BaseAdapter {

	private Context context;
	List<String> list;

	public SearchAdapter(Context context, List<String> list) {
		this.context = context;
		this.list = list;
	}

	public void setDate(List<String> list) {
		this.list = list;
		notifyDataSetChanged();
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return list.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder = null;
		if (convertView == null) {
			holder = new ViewHolder();
			convertView = LayoutInflater.from(context).inflate(
					R.layout.search_adapter_item, null);
			holder.layout = (FlowLayout) convertView
					.findViewById(R.id.search_adapter_item_flowlayout);
			holder.text = (TextView) convertView
					.findViewById(R.id.search_adapter_item_textView);

			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		// LayoutParams params = (LayoutParams) holder.text.getLayoutParams();
		// System.out.println("height："+params.height);
		// params.height = holder.layout.getLayoutParams().height;
		// holder.text.setLayoutParams(params);
		// System.out.println("height："+params.height);
		holder.layout.removeAllViews();
		int max = 3;
		if (position == 4)
			max = 4;
		for (int i = 0; i < max; i++) {
			TextView tv = (TextView) LayoutInflater.from(context).inflate(
					R.layout.flowlayout_item, holder.layout, false);
			tv.setText("小类：" + i);
			// if (position == 4) {
			// TextView tv2 = (TextView) LayoutInflater.from(context).inflate(
			// R.layout.flowlayout_item, holder.layout, false);
			// tv2.setText("小类：" + i);
			// holder.layout.addView(tv2);
			// }
			holder.layout.addView(tv);
		}
		holder.text.setText(list.get(position));
		return convertView;
	}

	public final class ViewHolder {
		public TextView text;
		public FlowLayout layout;
	}
}
