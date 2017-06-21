/**
 * ModuleProfitBranch
 */
package com.bonc.mobile.hbmclient.composite.channel;

import java.text.NumberFormat;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONObject;

import android.view.View;
import android.widget.TextView;

import com.bonc.mobile.hbmclient.R;
import com.bonc.mobile.hbmclient.activity.ChannelAnalyzeActivity;
import com.bonc.mobile.hbmclient.util.JsonUtil;
import com.bonc.mobile.hbmclient.view.MiddleKeyValueView;

/**
 * @author liweigao
 *
 */
public class ModuleProfitBranch extends AChannelBranch {
	private MiddleKeyValueView profit_rk1;
	private MiddleKeyValueView profit_rk2;
	private MiddleKeyValueView profit_rk3;
	private TextView profitTitle;

	public ModuleProfitBranch() {

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.bonc.mobile.hbmclient.composite.channel.AChannelBranch#dispatchView
	 * (android.view.View)
	 */
	@Override
	public void dispatchView(View view) {
		View profitView = view.findViewById(R.id.id_profit);
		this.profit_rk1 = (MiddleKeyValueView) profitView
				.findViewById(R.id.id_profit_column1);
		this.profit_rk2 = (MiddleKeyValueView) profitView
				.findViewById(R.id.id_profit_column2);
		this.profit_rk3 = (MiddleKeyValueView) profitView
				.findViewById(R.id.id_profit_column3);
		this.profitTitle = (TextView) profitView
				.findViewById(R.id.id_profit_title);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.bonc.mobile.hbmclient.composite.channel.AChannelBranch#setData(java
	 * .lang.Object)
	 */
	@Override
	public void setData(Object data) {
		JSONObject jo = (JSONObject) data;
		JSONArray ja_3 = jo.optJSONArray("valuen23Rank");
		if (ja_3 == null) {
			setNameDefault();
		} else {
			List<Map<String, String>> profit3 = JsonUtil.toDataList(ja_3);
			MiddleKeyValueView[] views = { this.profit_rk1, this.profit_rk2,
					this.profit_rk3 };
			for (int i = 0; i < views.length; i++) {
				String rankName = null;
				String rankValue = null;
				try {
					rankName = profit3.get(i).get("area");
					rankValue = profit3.get(i).get("attrValue");
				} catch (Exception e) {
					rankName = "--";
					rankValue = "--";
				}
				setValue(views[i], rankName, rankValue);
			}
		}
	}

	private void setNameDefault() {
		setValue(this.profit_rk1, "--", "--");
		setValue(this.profit_rk2, "--", "--");
		setValue(this.profit_rk3, "--", "--");
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.bonc.mobile.hbmclient.composite.channel.AChannelBranch#reset()
	 */
	@Override
	public void reset() {
		this.profitTitle.setText("利润率前3");
		setNameDefault();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.bonc.mobile.hbmclient.composite.channel.AChannelBranch#setOutEnvironment
	 * (java.lang.Object)
	 */
	@Override
	public void setOutEnvironment(Object env) {
		final ChannelAnalyzeActivity caa = (ChannelAnalyzeActivity) env;
		String areaLevel = caa.getAreaLevel();
		String end = null;
		if ("1".equals(areaLevel)) {
			end = "地市";
		} else if ("2".equals(areaLevel)) {
			end = "营销区";
		} else if ("3".equals(areaLevel)) {
			end = "营业厅";
		} else {
			end = "";
		}
		this.profitTitle.setText("利润率前3" + end);
	}

	private void setValue(MiddleKeyValueView v, String name, String value) {
		v.setKeyText(name);
		if (value == null || "".equals(value) || "null".equals(value)) {
			value = "--";
		} else {

		}
		try {
			NumberFormat nf = NumberFormat.getPercentInstance();
			nf.setMaximumFractionDigits(2);
			value = nf.format(Double.parseDouble(value));
		} catch (Exception e) {
			value += "%";
		}
		v.setValueText(value);
	}
}
