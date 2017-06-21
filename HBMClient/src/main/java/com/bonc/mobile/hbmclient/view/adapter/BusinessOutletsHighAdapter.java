/**
 * BusinessOutletsHighAdapter
 */
package com.bonc.mobile.hbmclient.view.adapter;

import java.text.DecimalFormat;
import java.util.List;
import java.util.Map;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.bonc.mobile.hbmclient.R;
import com.bonc.mobile.hbmclient.activity.BusinessOutletsKpiActivity;
import com.bonc.mobile.hbmclient.state.business_outlets.item.KeyValueView2;
import com.bonc.mobile.hbmclient.state.business_outlets.item.KeyValueView3;
import com.bonc.mobile.hbmclient.util.NumberUtil;

/**
 * @author liweigao
 * 
 */
public class BusinessOutletsHighAdapter extends BaseAdapter {
	private List<Map<String, String>> data;
	private String date;
	private String areaCode;
	private DecimalFormat format;

	public final static String KEY_KPI_CODE = "kpi_code";
	public final static String KEY_TYPE = "key_type";
	public final static String KEY_DATE = "key_date";
	public final static String KEY_AREA_CODE = "key_area_code";

	public BusinessOutletsHighAdapter(List<Map<String, String>> data,
			String date, String areaCode) {
		this.data = data;
		this.date = date;
		this.areaCode = areaCode;
		this.format = new DecimalFormat(",###");
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
	public View getView(int position, View convertView, ViewGroup parent) {
		final Context context = parent.getContext();
		if (convertView == null) {
			LayoutInflater inflater = LayoutInflater.from(context);
			View view = inflater.inflate(R.layout.business_outlets_item,
					parent, false);
			convertView = view;
		} else {

		}
		KeyValueView3 column1 = (KeyValueView3) convertView
				.findViewById(R.id.column1);
		KeyValueView2 column2 = (KeyValueView2) convertView
				.findViewById(R.id.column2);
		KeyValueView2 column3 = (KeyValueView2) convertView
				.findViewById(R.id.column3);

		column1.setDownKeyText("日累计值总数");
		column2.setUpKeyText("当日值");
		column2.setDownKeyText("日累计");
		column3.setUpKeyText("当日值");
		column3.setDownKeyText("日累计");

		Map<String, String> item = data.get(position);
		String kpiUnit = item.get("kpiUnit");
		String kpiName = item.get("kpiName");
		column1.setUpText(kpiName);
		String total = item.get("current");
		column1.setDownValueText(getValue(total, kpiUnit));
		String publicCurrent = item.get("publicCurrent");
		column2.setUpValueText(getValue(publicCurrent, kpiUnit));
		String publicTotal = item.get("publicTotal");
		column2.setDownValueText(getValue(publicTotal, kpiUnit));
		String privateCurrent = item.get("privateCurrent");
		column3.setUpValueText(getValue(privateCurrent, kpiUnit));
		String privateTotal = item.get("privateTotal");
		column3.setDownValueText(getValue(privateTotal, kpiUnit));

		column1.setDownValueColor(R.color.red);
		column2.setUpValueColor(R.color.green);
		column2.setDownValueColor(R.color.green);
		column3.setUpValueColor(R.color.red);
		column3.setDownValueColor(R.color.red);

		final String kpiCode = item.get("kpiCode");
		column1.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent(context,
						BusinessOutletsKpiActivity.class);
				intent.putExtra(KEY_KPI_CODE, kpiCode);
				intent.putExtra(KEY_TYPE, 0);
				intent.putExtra(KEY_DATE, date);
				intent.putExtra(KEY_AREA_CODE, areaCode);
				context.startActivity(intent);
			}
		});

		column2.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent(context,
						BusinessOutletsKpiActivity.class);
				intent.putExtra(KEY_KPI_CODE, kpiCode);
				intent.putExtra(KEY_TYPE, 1);
				intent.putExtra(KEY_DATE, date);
				intent.putExtra(KEY_AREA_CODE, areaCode);
				context.startActivity(intent);
			}
		});

		column3.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent(context,
						BusinessOutletsKpiActivity.class);
				intent.putExtra(KEY_KPI_CODE, kpiCode);
				intent.putExtra(KEY_TYPE, 2);
				intent.putExtra(KEY_DATE, date);
				intent.putExtra(KEY_AREA_CODE, areaCode);
				context.startActivity(intent);
			}
		});

		return convertView;
	}

	private String getValue(String value, String unit) {
		String temp = null;
		try {
			temp = this.format.format(NumberUtil.changeToDouble(value));
		} catch (Exception e) {
			temp = "--";
		}
		temp += unit;
		return temp;
	}

}
