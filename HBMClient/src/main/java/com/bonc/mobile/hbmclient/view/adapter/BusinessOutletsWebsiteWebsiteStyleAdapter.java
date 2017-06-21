/**
 * BusinessOutletsWebsiteKpiStyleAdapter
 */
package com.bonc.mobile.hbmclient.view.adapter;

import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bonc.mobile.hbmclient.R;
import com.bonc.mobile.hbmclient.state.business_outlets.DeleteFocusWebsite;
import com.bonc.mobile.hbmclient.state.business_outlets.DeleteFocusWebsite.OnDeleteWebsiteListener;
import com.bonc.mobile.hbmclient.util.NumberUtil;

/**
 * @author liweigao
 *
 */
public class BusinessOutletsWebsiteWebsiteStyleAdapter extends BaseAdapter {
	private List<Map<String, String>> data;
	private String[] kpiCodes;
	private DeleteFocusWebsite deleteFocusWebsite;
	private DecimalFormat decimalFormat;

	public BusinessOutletsWebsiteWebsiteStyleAdapter(
			List<Map<String, String>> data, String[] kpiCodes) {
		this.data = data;
		this.kpiCodes = kpiCodes;
		this.decimalFormat = new DecimalFormat(",###");
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.widget.Adapter#getCount()
	 */
	@Override
	public int getCount() {
		if (this.data != null) {
			return this.data.size();
		} else {
			return 0;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.widget.Adapter#getItem(int)
	 */
	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.widget.Adapter#getItemId(int)
	 */
	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return 0;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.widget.Adapter#getView(int, android.view.View,
	 * android.view.ViewGroup)
	 */
	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		if (convertView == null) {
			LayoutInflater inflater = LayoutInflater.from(parent.getContext());
			convertView = inflater.inflate(
					R.layout.business_outlets_webstyle_item, parent, false);
		}
		TextView websiteNameTV = (TextView) convertView
				.findViewById(R.id.websiteName);
		TextView kpiValue1TV = (TextView) convertView
				.findViewById(R.id.kpiValue1);
		TextView kpiValue2TV = (TextView) convertView
				.findViewById(R.id.kpiValue2);
		TextView kpiValue3TV = (TextView) convertView
				.findViewById(R.id.kpiValue3);
		TextView kpiValue4TV = (TextView) convertView
				.findViewById(R.id.kpiValue4);
		TextView[] tvs = { kpiValue1TV, kpiValue2TV, kpiValue3TV, kpiValue4TV };
		Map<String, String> item = this.data.get(position);
		final String websiteName = item.get("areaName");
		final String websiteCode = item.get("areaCode");
		websiteNameTV.setText(websiteName);
		for (int i = 0; i < kpiCodes.length; i++) {
			String kpiValue = item.get(this.kpiCodes[i]);
			if (kpiValue == null) {
				kpiValue = "--";
			}
			tvs[i].setText(getValue(kpiValue));
		}
		ImageView image = (ImageView) convertView
				.findViewById(R.id.websiteDelete);
		image.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				final Context context = v.getContext();
				Builder builder = new Builder(context);
				builder.setTitle("提示");
				builder.setMessage("删除网点：" + websiteName);
				builder.setPositiveButton("确定", new OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						dialog.dismiss();
						deleteFocusWebsite = new DeleteFocusWebsite(context);
						deleteFocusWebsite
								.setListener(new OnDeleteWebsiteListener() {

									@Override
									public void onPost() {
										data.remove(position);
										notifyDataSetChanged();
									}
								});
						Map<String, String> questMap = new HashMap<String, String>();
						questMap.put("areaCode", websiteCode);
						deleteFocusWebsite.deleteFocusWebsite(questMap);
					}
				});
				builder.setNegativeButton("取消", new OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						dialog.dismiss();
					}
				});
				builder.show();
			}
		});

		return convertView;
	}

	private String getValue(String value) {
		String temp = null;
		try {
			temp = this.decimalFormat.format(NumberUtil.changeToDouble(value));
		} catch (Exception e) {
			temp = "--";
		}

		return temp;
	}

}
