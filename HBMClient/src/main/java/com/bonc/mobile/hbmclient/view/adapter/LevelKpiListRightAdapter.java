/**
 * LevelKpiListRightAdapter
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
public class LevelKpiListRightAdapter extends BaseAdapter {
	private ConfigAndDataAdapter dataAdapter;
	private List<Map<String, String>> kpiData;
	private Map<String, Map<String, String>> kpiFlag;
	private Map<String, List<Double>> trendList;
	private Map<String, Map<String, String>> kpiRuleAndUnitMap;

	public LevelKpiListRightAdapter(ConfigAndDataAdapter cada) {
		this.dataAdapter = cada;
		this.kpiData = cada.getKpiData();
		this.kpiFlag = cada.getLevelKpiFlagMap();
		this.trendList = cada.getTrendList();
		this.kpiRuleAndUnitMap = cada.getKpiRuleAndUnit();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.widget.Adapter#getCount()
	 */
	@Override
	public int getCount() {
		int size = 0;
		if (this.kpiData != null) {
			size = this.kpiData.size();
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
			LevelKpiItemProduct lkip = lkib.getProduct();
			lkip.setConfigAndDataAdapter(dataAdapter);
			director.buildLevelRightProduct(lkib);
			convertView = lkip;
			convertView.setTag(lkip);
		}
		LevelKpiItemProduct product = (LevelKpiItemProduct) convertView
				.getTag();

		try {
			Map<String, String> singleKpiData = kpiData.get(position);
			String kpiCode = singleKpiData.get("kpi_code");
			if (kpiCode != null) {
				Map<String, String> flag = kpiFlag.get(kpiCode);
				String level = flag.get("kpiLevel");
				product.setLevelStyle(level);

				String relation = flag.get("relationTag");
				product.setImageResource(relation, kpiCode);

				String tag = this.dataAdapter.getMenuCode() + "|T-1|" + kpiCode;
				List<Double> trendData = this.trendList.get(tag);
				if (trendData != null) {
					product.setTrendData(trendData);
				}

				Map<String, String> singleKpiRuleAndUnit = this.kpiRuleAndUnitMap
						.get(kpiCode);
				if (singleKpiRuleAndUnit != null) {
					singleKpiData.putAll(singleKpiRuleAndUnit);
					product.setOnClickListener(kpiCode);
				}
				product.setKpiData(singleKpiData);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {

		}

		return convertView;
	}

}
