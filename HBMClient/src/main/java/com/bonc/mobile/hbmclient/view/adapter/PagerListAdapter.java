/**
 * PagerListAdapter
 */
package com.bonc.mobile.hbmclient.view.adapter;

import java.util.List;
import java.util.Map;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bonc.mobile.hbmclient.R;
import com.bonc.mobile.hbmclient.enum_type.MenuEnum;

/**
 * @author liweigao
 *
 */
public class PagerListAdapter extends BaseAdapter {
	private List<Map<String, String>> outmoduleList;

	public PagerListAdapter(List<Map<String, String>> outmoduleList) {
		this.outmoduleList = outmoduleList;
	}

	@Override
	public int getCount() {
		return outmoduleList.size();
	}

	@Override
	public Object getItem(int position) {
		return null;
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder;
		if (convertView == null) {
			convertView = LayoutInflater.from(parent.getContext()).inflate(
					R.layout.home_list_item, null);
			holder = new ViewHolder();
			holder.ll_item_logo = (LinearLayout) convertView
					.findViewById(R.id.ll_item_logo);
			holder.textView1 = (TextView) convertView
					.findViewById(R.id.home_item_tv1);
			holder.textView2 = (TextView) convertView
					.findViewById(R.id.home_item_tv2);
			convertView.setTag(holder);
		} else {

			holder = (ViewHolder) convertView.getTag();
		}

		int menuCode = 0;

		try {
			menuCode = Integer.parseInt(outmoduleList.get(position).get(
					"menuCode"));
		} catch (Exception e) {
			e.printStackTrace();
			menuCode = 0;
		}
		int imageid = MenuEnum.getIdIcon(menuCode);
		holder.ll_item_logo.setBackgroundResource(imageid);
		holder.textView1.setText(outmoduleList.get(position).get("menuName"));
		holder.textView2
				.setText(outmoduleList.get(position).get("menuExpalin"));
		return convertView;
	}

	private class ViewHolder {
		public LinearLayout ll_item_logo;// 更换模块图片
		public TextView textView1;// 模块名称
		public TextView textView2;// 模块介绍
	}
}
