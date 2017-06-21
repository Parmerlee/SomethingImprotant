/**
 * LevelKpiListLeftAdapter
 */
package com.bonc.mobile.hbmclient.view.adapter;

import java.util.List;
import java.util.Map;

import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.bonc.mobile.hbmclient.adapter.levelkpi.ConfigAndDataAdapter;
import com.bonc.mobile.hbmclient.levelkpi.LevelKpiItemBuilder;
import com.bonc.mobile.hbmclient.levelkpi.LevelKpiItemProduct;
import com.bonc.mobile.hbmclient.levelkpi.LinearDirector;

/**
 * @author liweigao
 *
 */
public class LevelKpiListLeftAdapter extends BaseAdapter {
	private ConfigAndDataAdapter dataAdapter;
	private List<Map<String, String>> kpiData;
	private String[] colKey;
	private Map<String, Map<String, String>> kpiFlag;

	public LevelKpiListLeftAdapter(ConfigAndDataAdapter cada) {
		this.dataAdapter = cada;
		this.kpiData = cada.getKpiData();
		this.colKey = cada.getColKey();
		this.kpiFlag = cada.getLevelKpiFlagMap();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.widget.Adapter#getCount()
	 */
	@Override
	public int getCount() {
		int size = 0;
		if (kpiData != null) {
			size = kpiData.size();
		}
		return size;
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
		if (convertView == null) {
			LevelKpiItemBuilder lkib = new LevelKpiItemBuilder(
					parent.getContext());
			LinearDirector director = new LinearDirector();
			director.buildLevelKpiNameProduct(lkib);
			LevelKpiItemProduct lkip = lkib.getProduct();
			lkip.setConfigAndDataAdapter(dataAdapter);
			convertView = lkip;
			convertView.setTag(lkip);
		}

		LevelKpiItemProduct product = (LevelKpiItemProduct) convertView
				.getTag();

		String name = null;
		try {
			String kpiCode = kpiData.get(position).get("kpi_code");
			if (kpiCode != null) {
				Map<String, String> flag = kpiFlag.get(kpiCode);
				String level = flag.get("kpiLevel");
				product.setLevelStyle(level);
				try {
					name = this.kpiData.get(position).get(colKey[0]);
					product.setOnClickListener(kpiCode);
					product.setOnLongClickListener(kpiCode);
				} catch (Exception e) {
					name = "--";
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			product.setText(name);
		}

		return convertView;
	}

}
